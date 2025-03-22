package mx.pagos.admc.core.business.digitalsignature;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.core.io.IOUtils;
import mx.pagos.admc.contracts.business.digitalsignature.EviSignBusiness;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Documentable;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Recipientable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.DigitalSignatureProviderEnum;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class DigitalSignatureDashboardBusiness {

	@Autowired
	private Configurable configurable;

	@Autowired
	Documentable documentable;

	@Autowired
	Recipientable recipientable;
	
	@Autowired
	EviSignBusiness eviSignBusiness; 
	
	@Autowired
	DocuSignBusiness docuSignBusiness; 
	
	@Autowired
	PscWorldBusiness pscWorldBusiness; 
	
	
	private static final Logger LOG = Logger.getLogger(DigitalSignatureDashboardBusiness.class);
	
	
	public DocumentDS validateDraftDocusign(DocumentDS documentRequest) throws Exception {
		try {

			DocumentDS response = new DocumentDS();
			
			DocumentDS documentDB = 
					documentable.findDocumentByIdRequisition(documentRequest.getIdRequisition());
			
			if (documentDB == null) {
				response.setStatus(Boolean.FALSE);
				return response;
			}
			
			if (documentDB.getDigitalSignatureProvider() != DigitalSignatureProviderEnum.DOCUSIGN) {
				LOG.info("Existe una solicitud de firma eletrónica activa para la solicitud: "
						+ documentRequest.getIdRequisition());
				response.setStatus(Boolean.FALSE);
				return response;				
			}

			response.setDigitalSignatureProvider(DigitalSignatureProviderEnum.DOCUSIGN);
			response.setStatus(Boolean.TRUE);
			return response;
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	

	public DocumentDS sendDocumentToProvider(DocumentDS documentRequest) throws Exception {
		try {
			
			switch (documentRequest.getDigitalSignatureProvider()) {
			case DOCUSIGN:
				return docuSignBusiness.sendDocument(documentRequest);
			case ABSIGN:
				return eviSignBusiness.eviSignSubmit(documentRequest);
			default:
				throw new Exception("No hay proveedor de firma digital para este documento");
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	

	public DocumentDS getDocumentByIdRequisition(DocumentDS documentRequest) throws Exception {
		try {
			
			DocumentDS documentDB = documentable.findDocumentByIdRequisition(documentRequest.getIdRequisition());
			
			if (documentDB == null) {
				return new DocumentDS(DsMessagesConstants.ERROR_DS_DOCUMENT_NOT_FOUND,
						DsMessagesConstants.ERROR_DS_DOCUMENT_NOT_FOUND);
			}

			validateDocumentStatusProvider(documentDB);

			List<Recipient> recipients = recipientable.findRecipientsByIdDocument(documentDB.getIdDocument());

			Boolean emptyList = recipients == null || recipients.size() == Constants.CERO;

			if (emptyList && !documentDB.isOnlySigner()) {
				return new DocumentDS(DsMessagesConstants.ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS,
						DsMessagesConstants.ERROR_DS_DOCUMENT_WITHOUT_RECIPIENTS);
			}

			final String URL_APP_SIGN = this.configurable.findByName(Constants.CONFIG_DS_URL_APP_SIGN);
			for (Recipient recipient : recipients) {
				String linkToDocument = getLinkToDocument(recipient, URL_APP_SIGN);
				recipient.setLinkToDocument(linkToDocument);
			}
			documentDB.setRecipients(recipients);

			return documentDB;

		} catch (DatabaseException databaseException) {
			databaseException.printStackTrace();
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);

		} catch (Exception exception) {
			exception.printStackTrace();
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}
	
	
	private void validateDocumentStatusProvider(DocumentDS documentDB) throws Exception {
		try {

			switch (documentDB.getDigitalSignatureProvider()) {
				case DOCUSIGN:
					LOG.error("La validación del proveedor DocuSign no puede ser procesada en esta clase");
					break;
				case ABSIGN:
					eviSignBusiness.validateDocumentStatusProvider(documentDB);
					break;
				default:
					throw new Exception("No hay proveedor de firma digital para este documento");
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	
	public void viewDocument(DocumentDS documentDS, final HttpServletResponse response) throws Exception {
		try {

			DocumentDS documentDB = null;
			if (documentDS.getIdDocument() != null) {
				documentDB = documentable.findById(documentDS.getIdDocument());
			}
			String filePath = documentDB.getFilePath();
			
			File newFile = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(newFile);
			IOUtils.copy(fileInputStream, response.getOutputStream());
			
			fileInputStream.close();
			
		} catch (Exception exception) {
			throw new Exception(exception);
		}
	}
	

	public DocumentDS deleteByIdDocument(DocumentDS documentRequest) throws Exception {
		try {
			
			DocumentDS document = documentable.findDocumentByIdRequisition(documentRequest.getIdRequisition());
			
			switch (document.getDigitalSignatureProvider()) {
				case DOCUSIGN:
					docuSignBusiness.deleteRecipienSignedDocument(documentRequest);
					break;
				case ABSIGN:
					eviSignBusiness.cancelDocument(document);
					break;
				default:
					throw new Exception("No hay proveedor de firma digital para este documento");
			}

			recipientable.deleteByIdDocument(document.getIdDocument());

			documentable.deleteByIdDocument(document.getIdDocument());

			return new DocumentDS(DsMessagesConstants.SUCCESS_CODE, DsMessagesConstants.SUCCESS_CODE);

		} catch (DatabaseException databaseException) {
			databaseException.printStackTrace();
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);

		} catch (Exception exception) {
			exception.printStackTrace();
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
	}


	private String getLinkToDocument(Recipient recipient, String URL_APP_SIGN) throws Exception {
		try {

			Boolean isViewer = recipient.getRecipientAction().equals(RecipientActionEnum.RECEIVES_A_COPY);

			String documentId = recipient.getIdDocument().toString();
			String userId = recipient.getIdRecipient().toString();

			return URL_APP_SIGN + DsMessagesConstants.QUESTION + DsMessagesConstants.DOCUMENT
					+ DsMessagesConstants.EQUAL + documentId + DsMessagesConstants.AMPERSAND + DsMessagesConstants.USER
					+ DsMessagesConstants.EQUAL + userId + DsMessagesConstants.AMPERSAND + DsMessagesConstants.IS_VIEWER
					+ DsMessagesConstants.EQUAL + isViewer;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	

	public DocumentDS resendInvitationEmail(DocumentDS documentDS) throws Exception {
		try {
			
			switch (documentDS.getDigitalSignatureProvider()) {
				case DOCUSIGN:
					docuSignBusiness.reseendSignedDocument(documentDS);
					break;
				case ABSIGN:
					LOG.error("El proveedor EviSing no cuenta con reenvio de invitación por correo electrónico");
					break;
				default:
					throw new Exception("No hay proveedor de firma digital para este documento");
			}

			return new DocumentDS(DsMessagesConstants.SUCCESS_CODE, DsMessagesConstants.SUCCESS_MESSAGE);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception(exception);
		}
	}

	public void downloadSentDocument(DocumentDS documentDS, final HttpServletResponse response) throws Exception {
		try {

			DocumentDS documentDB = documentable.findById(documentDS.getIdDocument());
			String filePath = documentDB.getFilePath();

			File newFile = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(newFile);
			IOUtils.copy(fileInputStream, response.getOutputStream());

			fileInputStream.close();

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public void downloadSignedDocument(DocumentDS documentDS, final HttpServletResponse response) throws Exception {
		try {
			
			switch (documentDS.getDigitalSignatureProvider()) {
				case DOCUSIGN:
					docuSignBusiness.downloadDocument(documentDS, response);
					break;
				case ABSIGN:
					eviSignBusiness.downloadSignedDocument(documentDS, response);
					break;
				default:
					throw new Exception("No hay proveedor de firma digital para este documento");
			}
			

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	
	public void saveSignedDocument(DocumentDS documentDS, final HttpServletResponse response) throws Exception {
		try {
			DocumentDS document = documentable.findDocumentByIdRequisition(documentDS.getIdRequisition());
			if (document.getStatusDigitalSignature() == DigitalSignatureStatusEnum.SIGNED) {
				switch (document.getDigitalSignatureProvider()) {
				case DOCUSIGN:
					docuSignBusiness.saveDocuSign(documentDS);
					break;
				case ABSIGN:
					eviSignBusiness.saveEviSign(document);
					break;
				default:
					throw new Exception("No hay proveedor de firma digital para este documento");
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

}
