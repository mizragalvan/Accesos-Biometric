package mx.pagos.admc.contracts.business.digitalsignature;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.opensagres.xdocreport.core.io.IOUtils;
import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.business.digitalsignature.evisignapi.EviSign;
import mx.pagos.admc.contracts.business.digitalsignature.evisignapi.EviSignApiClient;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.constants.ESMessagesConstants;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Documentable;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Recipientable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.digitalsignature.Affidavit;
import mx.pagos.admc.contracts.structures.digitalsignature.DocumentEviSign;
import mx.pagos.admc.contracts.structures.digitalsignature.DocumentsResultEviSign;
import mx.pagos.admc.contracts.structures.digitalsignature.EviSignQuery;
import mx.pagos.admc.contracts.structures.digitalsignature.Options;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.contracts.structures.digitalsignature.RemindersEvi;
import mx.pagos.admc.contracts.structures.digitalsignature.ResultRequestEviSign;
import mx.pagos.admc.contracts.structures.digitalsignature.SigningParty;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.DigitalSignatureProviderEnum;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.admc.enums.EviSignStatusEnum;
import mx.pagos.admc.enums.digitalsignature.EviSignSigningMethodEnum;
import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;
import mx.pagos.admc.enums.digitalsignature.RoleSigningPartiesEviSignEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.DirUtil;

@Service
public class EviSignBusiness {

	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@Autowired
	private Configurable configurable;

	@Autowired
	private Documentable documentable;

	@Autowired
	private Recipientable recipientable;

	@Autowired
	private UserSession userSession;

	@Autowired
	private UserSession session;

	private static final Logger LOG = Logger.getLogger(EviSignBusiness.class);

	public DocumentDS eviSignSubmit(DocumentDS documentRequest) throws Exception {
		try {

			String username = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_USERNAME);
			String password = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_PASSWORD);
			String apiUrl = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_URL_API);

			EviSignApiClient apiClient = new EviSignApiClient(username, password);
			apiClient.setUrl(apiUrl);
			EviSign eviSign = new EviSign(apiClient);

			File docx = new File(this.requisitionBusiness.findTemplate(documentRequest.getIdRequisition()));
			String docxBaseName = FilenameUtils.getBaseName(docx.getName());
			StringBuilder folderPath = buildFolderPath(documentRequest.getIdRequisition(), "Solicitudes");
			String filePath = folderPath + File.separator + docxBaseName + Constants.PDF_FILE;

			LOG.info("Ruta del documento gurdado: " + filePath);
			File documentFile = new File(filePath);

			if (!documentFile.exists()) {
				LOG.info("No ha sido posible crear el archivo " + docxBaseName);
				throw new BusinessException("No ha sido posible crear el archivo " + docxBaseName);
			}
			documentRequest.setDocumentName(docxBaseName);
			documentRequest.setFilePath(filePath);

			byte[] fileContent = new byte[(int) documentFile.length()];
			try (FileInputStream fileInputStream = new FileInputStream(documentFile)) {
				fileInputStream.read(fileContent);
			}
			String base64Document = Base64.getEncoder().encodeToString(fileContent);

			DocumentEviSign documentEviSign = new DocumentEviSign();
			documentEviSign.setSubject(documentRequest.getEmailSubject());
			documentEviSign.setDocument(base64Document);
			documentEviSign.setSigningParties(getSigningParties(documentRequest));
			documentEviSign.setInterestedParties(getInterestedParties(documentRequest));
			documentEviSign.setOptions(getOptions(documentRequest));
			DocumentEviSign providerResponse = eviSign.save(documentEviSign);

			saveInDatabase(providerResponse, documentRequest);

			return new DocumentDS(ESMessagesConstants.SUCCESS_CODE, ESMessagesConstants.SUCCESS_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(ESMessagesConstants.ERROR_CODE, e);
		}
	}

	private Options getOptions(DocumentDS documentRequest) {
		Options options = new Options();
		
		if (documentRequest.isHasReminders()) {
			
			RemindersEvi reminders = new RemindersEvi();
			
			if (documentRequest.getDaysBeforeFirstReminder() != null) {
				reminders.setInitial("P" + documentRequest.getDaysBeforeFirstReminder() + "D");
			}
			
			if (documentRequest.getDaysBetweenReminders() != null) {
				reminders.setRepeat("P" + documentRequest.getDaysBetweenReminders() + "D");
			}
			
			String[] weekdays = documentRequest
					.getWeekdays()
					.stream()
			        .map(Enum::toString)
			        .toArray(String[]::new);
			reminders.setDays(weekdays.length != 0 ? weekdays : null);
			
			String[] timeRanges = documentRequest
					.getTimeRanges()
					.stream()
			        .map(Enum::toString)
			        .toArray(String[]::new);
			reminders.setTimeRange(timeRanges.length != 0 ? timeRanges : null);
			
			reminders.setMax( weekdays.length * timeRanges.length );
			reminders.setStop("P" + Constants.FIFTY_NINE + "D");
			reminders.setTimeZone(Constants.TIME_ZONE_ISO);
			
			options.setReminders(reminders);
		}

		if (documentRequest.isHasExpiration()) {
			options.setTimeToLive(documentRequest.getDaysValidity() * Constants.MINUTES_PER_DAY);
		} else {
			options.setTimeToLive(Constants.FIFTY_NINE * Constants.MINUTES_PER_DAY);
		}

		options.setSignatureRequestInfoText(documentRequest.getEmailMessage());
		options.setLanguage(Constants.SPANISH);
		return options;
	}

	private StringBuilder buildFolderPath(final Integer idObject, final String objectName) throws BusinessException {
		final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
		pathBuilder.append(File.separator).append(objectName);
		pathBuilder.append(File.separator).append(this.userSession.getIdFlow());
		pathBuilder.append(DirUtil.obtenRutaSolicitud(idObject)).append(File.separator);
		pathBuilder.append(idObject.toString());
		this.validateDirectory(pathBuilder.toString());
		return pathBuilder;
	}

	private String getBasePath() throws BusinessException {
		try {
			return this.configurable.findByName(Constants.ROOT_PATH);
		} catch (DatabaseException databaseException) {
			LOG.error(ESMessagesConstants.MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
			throw new BusinessException(ESMessagesConstants.MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
		}
	}

	private void validateDirectory(final String path) {
		final File requisitionPath = new File(path);
		if (!requisitionPath.exists())
			requisitionPath.mkdirs();
	}

	public List<SigningParty> getSigningParties(DocumentDS documentRequest) throws Exception {
		try {

			List<SigningParty> signingParties = new ArrayList<>();

			for (Recipient recipient : documentRequest.getRecipients()) {

				if (recipient.getRecipientAction() == RecipientActionEnum.RECEIVES_A_COPY) {
					continue;
				}

				SigningParty signingParty = new SigningParty();
				signingParty.setName(recipient.getFullName());
				signingParty.setAddress(recipient.getEmail());
				
				switch (recipient.getRecipientAction()) {
					case NEEDS_TO_SIGN: 
						signingParty.setRole(RoleSigningPartiesEviSignEnum.SIGNER.getValue());
						break;
					case NEEDS_TO_VIEW: 
						signingParty.setRole(RoleSigningPartiesEviSignEnum.REVIEWER.getValue());
						break;
					default:
						throw new Exception("No hay una acción definida para el destinatario:" 
								+ recipient.getFullName());
				}
				
				if (documentRequest.isSigningOrder() || documentRequest.isManualSigningOrder()) {
					signingParty.setSigningOrder( recipient.getSigningOrder() - 1 );
				}
				
				if ( !StringUtils.isEmptyString(recipient.getNote()) ) {
					signingParty.setSigningMethod(EviSignSigningMethodEnum.CHALLENGE.getValue());
					signingParty.setSignatureChallenge(recipient.getNote());
					signingParty.setSignatureChallengeResponse(recipient.getSecretCode());
				}	
				
				signingParties.add(signingParty);
			}

			return signingParties;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public List<SigningParty> getInterestedParties(DocumentDS documentRequest) {
		try {

			List<SigningParty> interestedParties = new ArrayList<>();

			for (Recipient recipient : documentRequest.getRecipients()) {

				if (recipient.getRecipientAction() != RecipientActionEnum.RECEIVES_A_COPY) {
					continue;
				}

				SigningParty interestedParty = new SigningParty();
				interestedParty.setName(recipient.getFullName());
				interestedParty.setAddress(recipient.getEmail());
				
				interestedParties.add(interestedParty);
			}

			return interestedParties;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	private void saveInDatabase(DocumentEviSign providerResponse, DocumentDS documentRequest) throws Exception {
		try {

			DocumentDS documentDS = new DocumentDS();
			documentDS.setIdUser(session.getIdUsuarioSession());
			documentDS.setIdRequisition(documentRequest.getIdRequisition());
			documentDS.setDocumentName(documentRequest.getDocumentName());
			documentDS.setDigitalSignatureProvider(DigitalSignatureProviderEnum.ABSIGN);
			documentDS.setProviderDocumentId(providerResponse.getUniqueId());
			documentDS.setFilePath(documentRequest.getFilePath());
			documentDS.setOnlySigner(documentRequest.isOnlySigner());
			documentDS.setEmailSubject(documentRequest.getEmailSubject());
			documentDS.setEmailMessage(documentRequest.getEmailMessage());
			documentDS.setStatusDigitalSignature(DigitalSignatureStatusEnum.IN_PROGRESS);
			documentDS.setCreatedAt(new Date());
			documentDS.setUpdatedAt(new Date());

			Integer documentId = documentable.save(documentDS);

			for (Recipient recipientRequest : documentRequest.getRecipients()) {

				Recipient newRecipient = new Recipient();
				newRecipient.setIdDocument(documentId);
				newRecipient.setRecipientAction(recipientRequest.getRecipientAction());
				newRecipient.setProviderRecipientId(Constants.BLANK);
				newRecipient.setSigningOrder(recipientRequest.getSigningOrder());
				newRecipient.setRfc(Constants.BLANK);
				newRecipient.setFullName(recipientRequest.getFullName());
				newRecipient.setEmail(recipientRequest.getEmail());
				newRecipient.setSecretCode(recipientRequest.getSecretCode());
				newRecipient.setNote(recipientRequest.getNote());
				newRecipient.setWidgetId(null);
				newRecipient.setSigned(Boolean.FALSE);
				newRecipient.setCreatedAt(new Date());
				newRecipient.setUpdatedAt(new Date());

				recipientable.save(newRecipient);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public void validateDocumentStatusProvider(DocumentDS documentDB) throws Exception {
		try {

			String username = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_USERNAME);
			String password = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_PASSWORD);
			String apiUrl = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_URL_API);

			EviSignApiClient apiClient = new EviSignApiClient(username, password);
			apiClient.setUrl(apiUrl);
			EviSign eviSign = new EviSign(apiClient);

			EviSignQuery query = new EviSignQuery();
			query.setWithUniqueIds(documentDB.getProviderDocumentId());
			query.setIncludeDocumentOnResult(Boolean.FALSE);
			query.setIncludeAffidavitsOnResult(Boolean.FALSE);
			query.setIncludeAffidavitBlobsOnResult(Boolean.FALSE);
			ResultRequestEviSign result = eviSign.find(query);

			if (result.getTotalMatches() == Constants.CERO) {
				LOG.error("No hay resultados para la consulta");
				throw new NullPointerException("No hay resultados para la consulta");
			}

			if (result.getResults().stream().findFirst().isEmpty()) {
				LOG.error("No hay un documento presente en la consulta");
				throw new NullPointerException("No hay un documento presente en la consulta");
			}

			DocumentsResultEviSign documentEvilsign = result.getResults().stream().findFirst().get();

			DigitalSignatureStatusEnum statusProvider;
			switch (EviSignStatusEnum.findByValue(documentEvilsign.getState())) {
			case STATE_SENT: {
				statusProvider = DigitalSignatureStatusEnum.IN_PROGRESS;
				break;
			}
			case STATE_PROCESSED: {
				statusProvider = DigitalSignatureStatusEnum.IN_PROGRESS;
				break;
			}
			case STATE_SUBMITTED: {
				statusProvider = DigitalSignatureStatusEnum.IN_PROGRESS;
				break;
			}
			case STATE_CLOSED: {
				String outcomeDocument = documentEvilsign.getOutcome();

				statusProvider = outcomeDocument.equals(EviSignStatusEnum.OUTCOME_SIGNED.getValue())
						? DigitalSignatureStatusEnum.SIGNED
						: DigitalSignatureStatusEnum.WARNING;
				break;
			}
			case STATE_UNKNOWN: {
				LOG.error("Estatus del proveedor no identificado: " + documentEvilsign.getState());
				throw new IllegalArgumentException(
						"Estatus del proveedor no identificado: " + documentEvilsign.getState());
			}
			default:
				LOG.error("Estatus del proveedor no identificado: " + documentEvilsign.getState());
				throw new IllegalArgumentException(
						"Estatus del proveedor no identificado: " + documentEvilsign.getState());
			}

			LOG.info("Validando estatus del idDocument: " + documentDB.getIdDocument());
			LOG.info("Documento estatus en BD: " + documentDB.getStatusDigitalSignature());
			LOG.info("Proveedor State: " + documentEvilsign.getState());
			LOG.info("Proveedor Outcome: " + documentEvilsign.getOutcome());

			if (statusProvider.equals(documentDB.getStatusDigitalSignature())) {
				LOG.info("El estatus ya se encuentra homologado con el proveedor");
				return;
			}

			updateStatusDocument(documentDB, statusProvider);

			LOG.info("Se ha homologado el estatus de forma correcta");

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	private void updateStatusDocument(DocumentDS documentDB, DigitalSignatureStatusEnum statusProvider)
			throws Exception {
		try {

			LOG.info("Actualizando estatus del documento en BD");

			documentable.updateStatusById(documentDB.getIdDocument(), statusProvider);
			documentDB.setStatusDigitalSignature(statusProvider);

			if (!documentDB.getStatusDigitalSignature().equals(DigitalSignatureStatusEnum.SIGNED)) {
				return;
			}

			List<Recipient> recipients = recipientable.findRecipientsByIdDocument(documentDB.getIdDocument());

			Boolean emptyList = recipients == null || recipients.size() == Constants.CERO;

			if (emptyList && !documentDB.isOnlySigner()) {
				String errorMessage = "No hay destinatarios para firma digital con el registro IdDocument: "
						+ documentDB.getIdDocument();
				LOG.error(errorMessage);
				throw new NullPointerException(errorMessage);
			}

			LOG.info("Actualizando estatus de los destinatarios en BD");

			for (Recipient recipient : recipients) {
//				if (!recipient.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_SIGN)) {
//					continue;
//				}
				recipient.setSigned(Boolean.TRUE);
				recipientable.updateRecipientSigned(recipient);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public DocumentDS downloadSignedDocument(DocumentDS documentRequest, final HttpServletResponse response)
			throws Exception {
		try {
			File zipFile = this.getAfidavits(documentRequest);
			FileInputStream fileInputStream = new FileInputStream(zipFile);
			IOUtils.copy(fileInputStream, response.getOutputStream());

			fileInputStream.close();

			return new DocumentDS(ESMessagesConstants.SUCCESS_CODE, ESMessagesConstants.SUCCESS_MESSAGE);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BusinessException(ESMessagesConstants.ERROR_CODE, exception);
		}
	}

	private File[] getAffidavitFiles(ResultRequestEviSign result) {
		try {

			List<File> files = new ArrayList<>();

			if (result.getTotalMatches() == Constants.CERO) {
				LOG.error("No hay resultados para la consulta");
				throw new NullPointerException("No hay resultados para la consulta");
			}

			if (result.getResults().stream().findFirst().isEmpty()) {
				LOG.error("No hay un documento presente en la consulta");
				throw new NullPointerException("No hay un documento presente en la consulta");
			}

			DocumentsResultEviSign documentEvilsign = result.getResults().stream().findFirst().get();

			if (documentEvilsign.getAffidavits().size() == Constants.CERO) {
				LOG.error("No hay Affidavits presentes en el documento");
				throw new NullPointerException("No hay Affidavits presentes en el documento");
			}

			for (Affidavit affidavit : documentEvilsign.getAffidavits()) {
				File file = ApiRestUtils.createPdfFileFromBase64(affidavit.getDescription(), affidavit.getBytes());
				files.add(file);
			}

			if (documentEvilsign.getDocument() == null) {
				LOG.error("El documento original no está presente en la consulta");
				throw new NullPointerException("El documento original no está presente en la consulta");
			}

			File file = ApiRestUtils.createPdfFileFromBase64(documentEvilsign.getSubject(),
					documentEvilsign.getDocument());
			files.add(file);

			return files.toArray(new File[files.size()]);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public void cancelDocument(DocumentDS documentRequest) throws Exception {
		try {

			LOG.info("Cancelando documento con el idRequisition: " + documentRequest.getIdRequisition());

			String username = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_USERNAME);
			String password = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_PASSWORD);
			String apiUrl = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_URL_API);

			EviSignApiClient apiClient = new EviSignApiClient(username, password);
			apiClient.setUrl(apiUrl);
			EviSign eviSign = new EviSign(apiClient);

			DocumentEviSign documentToCancel = new DocumentEviSign();
			documentToCancel.setUniqueId(documentRequest.getProviderDocumentId());
			documentToCancel.setComments(DsMessagesConstants.MSG_CANCELED_BY_USER_REQUEST);
			eviSign.cancel(documentToCancel);

			LOG.info("Documento " + documentToCancel.getUniqueId() + " cancelado exitosamente en EviSign");

		} catch (Exception exception) {
			exception.printStackTrace();
			throw new BusinessException(ESMessagesConstants.ERROR_CODE, exception);
		}
	}
	
	public File getAfidavits(DocumentDS documentRequest) throws Exception {
		try {
			String username = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_USERNAME);
			String password = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_PASSWORD);
			String apiUrl = this.configurable.findByName(Constants.CONFIG_DS_EVISIGN_URL_API);
			EviSignApiClient apiClient = new EviSignApiClient(username, password);
			apiClient.setUrl(apiUrl);
			EviSign eviSign = new EviSign(apiClient);
			EviSignQuery query = new EviSignQuery();
			query.setWithUniqueIds(documentRequest.getProviderDocumentId());
			query.setIncludeDocumentOnResult(Boolean.TRUE);
			query.setIncludeAffidavitsOnResult(Boolean.TRUE);
			query.setIncludeAffidavitBlobsOnResult(Boolean.TRUE);
			ResultRequestEviSign result = eviSign.find(query);
			File[] files = getAffidavitFiles(result);
//			File[] files = new File[0];
	        
	        if (files == null || files.length == 0) {
	            throw new Exception("El archivo zip está vacío. No se puede proceder.");
	        }
	        
			File tempFile = File.createTempFile(Constants.TEMP_FILE_NAME, Constants.TEMP_FILE_EXTENSION);
			File zipFile = ApiRestUtils.createZipArchive(tempFile.getPath(), files);
			
	        if (zipFile.length() == 0) {
	            throw new Exception("El archivo zip está vacío. No se puede proceder.");
	        }
	        
			return zipFile;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public void saveEviSign(DocumentDS documentRequest) throws Exception {
		try {
			String rutaAbsoluta = documentRequest.getFilePath();
			Path rutaAbsolutaPath = Paths.get(rutaAbsoluta);
			String rutaSinExtension = quitarExtension(rutaAbsolutaPath);
			System.out.println(rutaSinExtension);
			String rutaArchivoSalida = rutaSinExtension + Constants.EVISIGN;
			File file = new File(rutaArchivoSalida);
			File zipFile = this.getAfidavits(documentRequest);
			OutputStream outputStream = new FileOutputStream(file);
			if (outputStream != null) {
				FileInputStream fileInputStream = new FileInputStream(zipFile);
				IOUtils.copy(fileInputStream, outputStream);
				outputStream.flush();
				outputStream.close();
				fileInputStream.close();

				Requisition req = new Requisition();
				req.setDocumentDS(documentRequest);
				req.setIdRequisition(documentRequest.getIdRequisition());
				FileUploadInfo fle = new FileUploadInfo();
				File filex = new File(rutaSinExtension + Constants.EVISIGN);
				String fileName = filex.getName();
				fle.setDocumentName(fileName);
				fle.setIdRequisition(documentRequest.getIdRequisition());
				fle.setName(fileName);
				req.setSupplierApprovalDocument(fle);
				this.requisitionBusiness.sendToDocumentFinalDS(req);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String quitarExtension(Path rutaAbsolutaPath) {
		String nombreArchivoConExtension = rutaAbsolutaPath.getFileName().toString();
		int posicionPunto = nombreArchivoConExtension.lastIndexOf('.');
		if (posicionPunto > 0 && posicionPunto < nombreArchivoConExtension.length() - 1) {
			return rutaAbsolutaPath.getParent().resolve(nombreArchivoConExtension.substring(0, posicionPunto))
					.toString();
		} else {
			return rutaAbsolutaPath.toString();
		}
    }

}
