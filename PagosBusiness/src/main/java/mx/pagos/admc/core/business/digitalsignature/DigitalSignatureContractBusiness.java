package mx.pagos.admc.core.business.digitalsignature;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.text.WordUtils;
//import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import fr.opensagres.xdocreport.core.io.IOUtils;
import mx.engineer.utils.pdf.PDFUtils;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.interfaces.FinancialEntityable;
import mx.pagos.admc.contracts.interfaces.SupplierPersonable;
import mx.pagos.admc.contracts.interfaces.Supplierable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.contracts.structures.SupplierRequisition;
import mx.pagos.admc.contracts.structures.digitalsignature.BaseDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.business.SuplierRequisitionBusiness;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.core.utils.DirectoryUtils;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.Page;
import mx.pagos.document.versioning.interfaces.Versionable;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.DirUtil;

@Service
public class DigitalSignatureContractBusiness {
	private static final String MESSAGE_RETRIEVING_BASE_PATH_ERROR =
            "Hubo un problema al recuperar la ruta base para guardar los archivos";

	@Autowired
	private RequisitionBusiness requisitionBusiness;
	
//	@Autowired
//	private DigitalSignatureBusiness digitalSignatureBusiness;
	
	@Autowired
	private ConfigurationsBusiness configurationsBusiness;
	
	@Autowired
	private SupplierPersonable supplierPersonable;
	
	@Autowired
	private Supplierable supplierable;

	@Autowired
	private FinancialEntityable financialEntityable;
	
//	@Autowired
//	private Documentable documentable;

	@Autowired
	private Configurable configurable;
	
	@Autowired
	private SuplierRequisitionBusiness suplierRequisitionBusiness;
	
	@Autowired
	private Versionable versionable;
	
	 @Autowired
	    private UserSession userSession;
	
	private static final Logger LOG = Logger.getLogger(DigitalSignatureContractBusiness.class);
	
	
	public BaseDS getSignatureOption() throws BusinessException, DatabaseException {
		try {

//			String SIGNATURE_OPTION = this.configurable.findByName(Constants.CONFIG_DS_SIGNATURE_OPTION);
			
//			return new BaseDS(SIGNATURE_OPTION, DsMessagesConstants.SUCCESS_CODE);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
//		} catch (DatabaseException databaseException) {
//			databaseException.printStackTrace();
//			return new BaseDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
//		}
	}

	
	public DocumentDS getDocumentByIdRequisition(DocumentDS documentRequest) throws BusinessException, DatabaseException {
		try {

//			DocumentDS documentDB = documentable.findByIdRequisition(documentRequest.getIdRequisition());
			
//			if (documentDB != null) {
//				documentDB.setResponseCode(DsMessagesConstants.SUCCESS_CODE);
//				getRecipientsDocument(documentDB);
//			}
//
//			return documentDB;
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
		}
//		catch (DatabaseException databaseException) {
//			databaseException.printStackTrace();
//			return new DocumentDS(DsMessagesConstants.ERROR_CODE, DsMessagesConstants.ERROR_MESSAGE);
//		}
	}
	
	private void getRecipientsDocument(DocumentDS documentDB) throws BusinessException {
		try {
			
			List<DocumentDS> documents = new ArrayList<>();
			documents.add(documentDB);

			Page<DocumentDS> pageDocumentsRequest = new Page<DocumentDS>();
			pageDocumentsRequest.setItems(documents);
			
//			Page<DocumentDS> pageDocuments = digitalSignatureBusiness.mapUserDocuments(pageDocumentsRequest);
//			documentDB = pageDocuments.getItems().stream().findFirst().get();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
//		} catch (BusinessException exception) {
//			exception.printStackTrace();
//			throw exception;
//		}
		
	}
	
	
	public void viewDocument(DocumentDS documentRequest, final HttpServletResponse response)
			throws BusinessException, InvalidFormatException {
		LOG.info("INICIA CON ID : " + documentRequest.getIdRequisition());
		try {
			String rutaPath="";
			List<VersionDTO> versions = this.versionable.findContractVersionDTO(documentRequest.getIdRequisition());
			for (VersionDTO versionDto : versions) {
				versionDto.setFileName(FilenameUtils.getName(versionDto.getDocumentPath()));
				LOG.info("LA RUTA DE VERSION DEL DOCUMENTO ES " + versionDto.getFileName());
				LOG.info("LA RUTA ES " + versionDto.getDocumentPath());
				rutaPath=versionDto.getDocumentPath();
			}

			LOG.info("LA VARIABLE rutaPath ES  " + rutaPath);
			Integer idRequest = documentRequest.getIdRequisition();
			LOG.info("EL IDREQUISITION ES   " + idRequest);
			if (idRequest == null || idRequest == 0) {
				throw new NullPointerException("No se especificó un idRequisition válido");
			}

//			Boolean isColorMark = Boolean.FALSE;
//			File docx = this.requisitionBusiness.downloadDraftRequisition(idRequest, isColorMark);
			File docx = new File(this.requisitionBusiness.findTemplate(idRequest));
			String docxBaseName = FilenameUtils.getBaseName(docx.getName());

			String pdfPath = this.buildPath(idRequest, "Solicitudes") + File.separator + docxBaseName + Constants.PDF_FILE;
			File pdf = new File(pdfPath);

//			String CONVERTER_OPTION = this.configurable.findByName(Constants.CONFIG_DS_DOC_CONVERTER_OPTION);
//			LOG.info("Inicia conversión de Docx a PDF utilizando " + CONVERTER_OPTION);

//			if (CONVERTER_OPTION.equals(Constants.CONFIG_DS_DOC_CONVERTER_LIBRE_OFFICE)) {
//				String OFFICE_HOME = this.configurable.findByName(Constants.CONFIG_DS_LIBRE_OFFICE_PATH);
//				PDFUtils.convertDocxToPdfLibreOffice(OFFICE_HOME, docx, pdf);
//			}
//			
//			if (CONVERTER_OPTION.equals(Constants.CONFIG_DS_DOC_CONVERTER_MS_OFFICE)) {
//				PDFUtils.convertDocxToPdfMsOffice(docx, pdf);
//			}

			LOG.info("Finaliza conversión de Docx a PDF");
			
			FileInputStream fileInputStream = new FileInputStream(pdf);
			IOUtils.copy(fileInputStream, response.getOutputStream());

			fileInputStream.close();

		} catch (Exception exception) {
			LOG.info("Error de conversions de word a PDF");
			LOG.info(exception);
			exception.printStackTrace();
			throw new BusinessException(exception);
		}
	}

	private StringBuilder buildPath(final Integer idObject, final String objectName)
            throws BusinessException {
        final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
        pathBuilder.append(File.separator).append(objectName).append(File.separator);
        pathBuilder.append(File.separator).append(this.userSession.getIdFlow()).append(File.separator);
        pathBuilder.append(DirUtil.obtenRutaSolicitud(idObject)).append(File.separator);
        pathBuilder.append(idObject.toString());
        this.validateDirectory(pathBuilder.toString());
        return pathBuilder;
    }
	 private String getBasePath() throws BusinessException {
	        try {
	            return this.configurable.findByName("ROOT_PATH");
	        } catch (DatabaseException databaseException) {
	            LOG.error(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	            throw new BusinessException(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	        }
	    }
	 private void validateDirectory(final String path) {
	        final File requisitionPath = new File(path);
	        if (!requisitionPath.exists())
	            requisitionPath.mkdirs();
	    }
	
	private String getTemporalPath() throws BusinessException {
		String tempPath = "";
		try {
			tempPath = this.configurationsBusiness.getTemporalPath() + File.separator;
		} catch (ConfigurationException configurationException) {
			throw new BusinessException(configurationException);
		}
		if (!new File(tempPath).exists())
			throw new BusinessException("La carpeta de archivos temporales no existe");
		return tempPath;
	}

	public DocumentDS getDocumentInformation(DocumentDS documentRequest) throws Exception {
		try {

			getRecipientsToSignature(documentRequest);

			return documentRequest;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	private void getRecipientsToSignature(DocumentDS documentRequest) throws BusinessException, DatabaseException, EmptyResultException {
		try {
			
			List<Recipient> recipients = new ArrayList<>();
			
			List<FinancialEntity> financialEntities = financialEntityable.findByRecordStatus(RecordStatusEnum.ACTIVE);
			
			for (FinancialEntity financialEntity : financialEntities) {
				Recipient recipient = new Recipient();
				recipient.setIsRequired(Boolean.TRUE);
				recipient.setFullName(financialEntity.getName());
				recipient.setRfc(financialEntity.getRfc());
				recipient.setEmail(financialEntity.getCorreo());
				recipient.setRecipientAction(RecipientActionEnum.NEEDS_TO_SIGN);
				recipient.setRecipientType(DsMessagesConstants.RECIPIENT_TYPE_FINANCIAL_ENTITY);
				recipients.add(recipient);
			}
			
			Requisition requisition = this.requisitionBusiness.findById(documentRequest.getIdRequisition());
			
			Supplier supplierBD = supplierable.findById(requisition.getIdSupplier());
			
			Recipient recipientToSign = new Recipient();
			recipientToSign.setIsRequired(Boolean.TRUE);
			recipientToSign.setFullName(supplierBD.getCommercialName());
			recipientToSign.setRfc(supplierBD.getRfc());
			recipientToSign.setEmail(supplierBD.getEmail());
			recipientToSign.setRecipientAction(RecipientActionEnum.NEEDS_TO_SIGN);
			recipientToSign.setRecipientType(DsMessagesConstants.RECIPIENT_TYPE_SUPPLIER);
			recipients.add(recipientToSign);
			
			Requisition supplierRequisition = new Requisition();
			supplierRequisition.setIdRequisition(requisition.getIdRequisition());
			supplierRequisition.setIdSupplier(requisition.getIdSupplier());
			
			List<SupplierPerson> supplierPersons = this.supplierPersonable.findSupplierPersonsByIdSupplierAndType(
					supplierRequisition.getIdSupplier(), SupplierPersonTypeEnum.LEGALREPRESENTATIVE);
			
			for (SupplierPerson supplier : supplierPersons) {
				Recipient recipient = new Recipient();
				recipient.setIsRequired(Boolean.TRUE);
				recipient.setFullName(supplier.getName());
				recipient.setRfc(supplier.getRfc());
				recipient.setEmail(supplier.getCorreoElectronico());
				recipient.setRecipientAction(RecipientActionEnum.NEEDS_TO_SIGN);
				recipient.setRecipientType(DsMessagesConstants.RECIPIENT_TYPE_LEGAL_REPRESENTATIVE);
				recipients.add(recipient);
			}

			List<Requisition> suppliersRequisition = this.suplierRequisitionBusiness
					.findGarantesByIdRequisition(documentRequest.getIdRequisition());

			for (Requisition supplier : suppliersRequisition) {
				Recipient recipient = new Recipient();
				recipient.setIsRequired(Boolean.TRUE);
				recipient.setFullName(supplier.getSupplier().getCommercialName());
				recipient.setRfc(supplier.getSupplier().getRfc());
				recipient.setEmail(supplier.getSupplier().getEmail());
				recipient.setRecipientAction(RecipientActionEnum.NEEDS_TO_SIGN);
//				String recipientType = supplier.getTipoRelacion().toLowerCase();
//				recipient.setRecipientType( WordUtils.capitalize(recipientType) );

				recipients.add(recipient);
			}

			documentRequest.setRecipients(recipients);

		} catch (BusinessException exception) {
			throw exception;
		}
	}
	

}
