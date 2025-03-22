package mx.pagos.admc.core.business.digitalsignature;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


///
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.client.auth.OAuth.OAuthToken;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.InitialHere;
//import com.migcomponents.migbase64.Base64;
import com.docusign.esign.client.RFC3339DateFormat;
import com.docusign.esign.model.*;

import fr.opensagres.xdocreport.core.io.IOUtils;
import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Recipientable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.digitalsignature.BaseDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.Page;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;

@Service
public class DigitalSignatureBusiness {

	@Autowired
	private UserSession session;

	@Autowired
	private DocuSignBusiness docuSignBusiness;
	
	@Autowired
    private Configurable configurable;
	
//	@Autowired
//	@Lazy
//	private Documentable documentable;
	
	@Autowired
    @Lazy
    private Recipientable recipientable;
	
	private static final Logger LOG = Logger.getLogger(DigitalSignatureBusiness.class);


	public Page<DocumentDS> getUserDocuments(Page<DocumentDS> request) throws BusinessException {
		try {
			
//			Integer idUser = this.session.getIdUsuarioSession();
//			Page<DocumentDS> documentsDB = documentable.getDocuments(idUser, request);
			
//			return mapUserDocuments(documentsDB);
			return null;

		} catch (Exception exception) {
			throw new BusinessException(exception);
		}
	}
	
	public Page<DocumentDS> mapUserDocuments(Page<DocumentDS> documentsDB) throws BusinessException {
		try {
			
			
//			final String APP_ID = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_APP_ID);
//			final String APP_SECRET = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_APP_SECRET);
//			final String URL_API = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_URL_API);
			
//			ApiClient apiClient = new ApiClient(APP_ID, APP_SECRET);
//			apiClient.setUrl(URL_API);
//			Documents documentsAPI = new Documents(apiClient);
//			
//			for(DocumentDS documentDB : documentsDB.getItems()) {
//				
//				Document documentMiFiel = documentsAPI.find(documentDB.getIdMiFiel());
//				List<Recipient> recipientsUser = recipientable.finByIdDocument(documentDB.getIdDocument());
//				
//				documentDB.setRecipients(mapRecipient(documentMiFiel, recipientsUser));
//				documentDB.setFilePath(null);
//				
//				if (documentDB.getStatus()) {
//					continue;
//				}
//				
//				recipientsUser = recipientable.finByIdDocument(documentDB.getIdDocument());
//				boolean hasUnsignedSignatory = recipientsUser.stream().anyMatch(recipient -> !recipient.getSigned());
//				if (!hasUnsignedSignatory) {
//					documentable.updateStatusById(documentDB.getIdDocument(), Boolean.TRUE);
//					documentDB.setStatus(Boolean.TRUE);
//				}
//			}
			
			
			return documentsDB;
			
//		} catch (IOException mifielException) {
//			mifielException.printStackTrace();
//            throw new BusinessException(DsMessagesConstants.ERROR_API_MIFIEL, mifielException);
//            
		}catch (Exception exception) {
			exception.printStackTrace();
			throw new BusinessException(exception);
		}
	}
	
	private List<Recipient> mapRecipient(Document documentMiFiel, List<Recipient> recipientsUser) throws BusinessException {
		try {

			final String URL_APP_SIGN = this.configurable.findByName(Constants.CONFIG_DS_URL_APP_SIGN);
			
//			for(Recipient recipient : recipientsUser) {
//				
//				String linkToDocument = getLinkToDocument(documentMiFiel, URL_APP_SIGN, recipient);
//				recipient.setLinkToDocument(linkToDocument);
//
//				Predicate<Signer> signerPredicate = signer -> {
//						String signerId = signer.getId();
//						String idMiFiel = recipient.getIdMiFiel();
//					    return idMiFiel != null && idMiFiel.equals(signerId);
//					}; 
//						
//				Optional<Signer> signerOptional = documentMiFiel.getSigners()
//					.stream().filter(signerPredicate).collect(Collectors.toList())
//					.stream().findFirst();
//				
//				if (signerOptional.isEmpty() 
//						&& recipient.getRecipientAction().equals(RecipientActionEnum.RECEIVES_A_COPY)) {
//					LOG.info("El contacto: \'" + recipient.getFullName() + "\' solo recibe una copia "
//							+ "y no fue registrado en MiFiel");
//					continue;
//				}
//				
//				if (signerOptional.isEmpty()) {
//					LOG.error("Firmante no encontrado en MiFiel: " + recipient.getFullName());
//					LOG.error("Acción del firmante: " + recipient.getRecipientAction());
//					throw new BusinessException(DsMessagesConstants.ERROR_DATA_SIGNER_MIFIEL,
//							new NullPointerException());
//				}
//				
//				Signer signer = signerOptional.get();
//
//				Boolean isRecipientNotUpdated = signer.getSigned() && !recipient.getSigned();
//				if (isRecipientNotUpdated) {
//					setRecipientSigned(recipient);
//				}
//				recipient.setSigned(signer.getSigned());
//				
//			}
			
			return recipientsUser;
			
		} catch (Exception exception) {
			LOG.error(exception.getMessage());
			throw new BusinessException(exception);
		}
	}

	private String getLinkToDocument(Document documentMiFiel, final String URL_APP_SIGN, Recipient recipient) {
		Boolean isViewer = recipient.getRecipientAction().equals(RecipientActionEnum.RECEIVES_A_COPY);
		
//		String documentId = isViewer
//				? documentMiFiel.getId()
//						: recipient.getWidgetId();
//		
//		String userId = isViewer
//				? recipient.getIdRecipient().toString()
//				: recipient.getIdMiFiel();
		
		return URL_APP_SIGN + DsMessagesConstants.QUESTION
//				+ DsMessagesConstants.DOCUMENT + DsMessagesConstants.EQUAL + documentId 
//				+ DsMessagesConstants.AMPERSAND
//				+ DsMessagesConstants.USER + DsMessagesConstants.EQUAL + userId
				+ DsMessagesConstants.AMPERSAND
				+ DsMessagesConstants.IS_VIEWER + DsMessagesConstants.EQUAL + isViewer;
	}
	
	
	public DocumentDS resendInvitationEmail(DocumentDS documentDS) throws BusinessException {
		try {
			
//			docuSignBusiness.sendEmailToRecipients(documentDS);
			return new DocumentDS(DsMessagesConstants.SUCCESS_CODE, DsMessagesConstants.SUCCESS_MESSAGE);
			
		} catch (Exception exception) {
			throw new BusinessException(exception);
		}
	}
	

	public BaseDS validateSecretCodeRecipient(Recipient recipientRequest) throws BusinessException {
		try {
			
			Recipient recipientDB = null;
			if (!recipientRequest.getIsViewer()) {
				recipientDB = recipientable.findByProviderRecipientId(recipientRequest.getProviderRecipientId()); // Evaluate
			}
			if (recipientRequest.getIsViewer()) {
				recipientDB = recipientable.findById(recipientRequest.getIdRecipient());
			}
			
			if (StringUtils.isEmptyString(recipientDB.getSecretCode())) {
				return new BaseDS(DsMessagesConstants.NO_NEED_AUTHORIZATION_CODE, 
						DsMessagesConstants.NEED_AUTHORIZATION_MESSAGE);
			}
			
			if (recipientRequest.getSecretCode() == null) {
				return new BaseDS(DsMessagesConstants.NEED_AUTHORIZATION_CODE,
						DsMessagesConstants.NEED_AUTHORIZATION_MESSAGE);
			}
			
			if (recipientRequest.getSecretCode().equals(recipientDB.getSecretCode())) {
				return new BaseDS(DsMessagesConstants.SUCCESS_CODE, DsMessagesConstants.SUCCESS_MESSAGE);
			}
			
			return new BaseDS(DsMessagesConstants.UNAUTHORIZED_CODE, DsMessagesConstants.UNAUTHORIZED_MESSAGE);
			
		} catch (Exception exception) {
			throw new BusinessException(exception);
		}
	}
	
	
	public void viewDocument(DocumentDS documentDS, final HttpServletResponse response) throws BusinessException {
		try {

			DocumentDS documentDB = null;
//			if (documentDS.getIdDocument() != null) {
//				documentDB = documentable.findById(documentDS.getIdDocument());
//			}
//			if (documentDS.getIdMiFiel() != null) {
//				documentDB = documentable.findByIdMiFiel(documentDS.getIdMiFiel());
//			}
			String filePath = documentDB.getFilePath();
			
			File newFile = new File(filePath);
			FileInputStream fileInputStream = new FileInputStream(newFile);
			IOUtils.copy(fileInputStream, response.getOutputStream());
			
			fileInputStream.close();
			
		} catch (Exception exception) {
			throw new BusinessException(exception);
		}
	}

	
	public void downloadDocument(DocumentDS documentDS, final HttpServletResponse response)
			throws BusinessException {
		try {
			
//			final String APP_ID = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_APP_ID);
//			final String APP_SECRET = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_APP_SECRET);
//			final String URL_API = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_URL_API);
			
//			String idUser = this.session.getIdUsuarioSession().toString();
//			String folderPath = miFielBusiness.createPath(idUser);
//			File folderPathFile = new File(folderPath);
//		    if (!folderPathFile.exists()) {
//		    	LOG.info("Nuevo directorio creado en " + folderPath);
//		    	folderPathFile.mkdirs();
//		    }
//			
//		    String fileName = documentDS.getDocumentName();
//			String filePath = folderPath + File.separator + fileName + Constants.ZIP_FILE;
//
//			ApiClient apiClient = new ApiClient(APP_ID, APP_SECRET);
//			apiClient.setUrl(URL_API);
//			Documents documents = new Documents(apiClient);
//			documents.saveZip(documentDS.getIdMiFiel(), filePath);
//			
//			File newFile = new File(filePath);
//			FileInputStream fileInputStream = new FileInputStream(newFile);
//			IOUtils.copy(fileInputStream, response.getOutputStream());
//			
//			fileInputStream.close();
			
//		} catch (IOException mifielException) {
//			LOG.error(DsMessagesConstants.ERROR_API_MIFIEL, mifielException);
//			throw new BusinessException(DsMessagesConstants.ERROR_API_MIFIEL, mifielException);
//			
		} catch (Exception exception) {
			throw new BusinessException(exception);
		}
	}
	
	
	public DocumentDS deleteDocument(DocumentDS documentDS) throws BusinessException {
		try {
			
			final String APP_ID = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_APP_ID);
			final String APP_SECRET = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_APP_SECRET);
			final String URL_API = this.configurable.findByName(Constants.CONFIG_DS_MIFIEL_URL_API);
			
//			ApiClient apiClient = new ApiClient(APP_ID, APP_SECRET);
//			apiClient.setUrl(URL_API);
//			Documents documents = new Documents(apiClient);
//			documents.delete(documentDS.getIdMiFiel());
//			
//			recipientable.deleteByIdDocument(documentDS.getIdDocument());
//			documentable.deleteByIdDocument(documentDS.getIdDocument());
//			  
			return new DocumentDS(DsMessagesConstants.SUCCESS_CODE, DsMessagesConstants.SUCCESS_MESSAGE);
			
//		} catch (IOException mifielException) {
//			LOG.error(DsMessagesConstants.ERROR_API_MIFIEL, mifielException);
//            throw new BusinessException(DsMessagesConstants.ERROR_API_MIFIEL, mifielException);
//            
		} catch (DatabaseException databaseException) {
			LOG.error(DsMessagesConstants.ERROR_DELETE_DB, databaseException);
			throw new BusinessException(DsMessagesConstants.ERROR_DELETE_DB, databaseException);
            
		} catch (Exception exception) {
			throw new BusinessException(exception);
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public DocumentDS setRecipientSigned(Recipient recipient) throws BusinessException {
		try {
			
			String providerRecipientId = recipient.getProviderRecipientId(); // Evaluate
			Recipient recipientDB = recipientable.findByProviderRecipientId(providerRecipientId);
			recipientDB.setSigned(Boolean.TRUE);
			recipientDB.setWidgetId(null);
			recipientable.updateRecipientSigned(recipientDB);
			return new DocumentDS(DsMessagesConstants.SUCCESS_CODE, DsMessagesConstants.SUCCESS_MESSAGE);

		} catch (DatabaseException databaseException) {
			LOG.error(DsMessagesConstants.ERROR_INSERT_DB, databaseException);
			throw new BusinessException(DsMessagesConstants.ERROR_INSERT_DB, databaseException);
            
		} catch (Exception exception) {
			LOG.error(DsMessagesConstants.ERROR_CODE, exception);
            throw new BusinessException(DsMessagesConstants.ERROR_CODE, exception);
		}
	}
	
}

