package mx.pagos.admc.core.business.digitalsignature;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.auth.OAuth.OAuthToken;
import com.docusign.esign.client.auth.OAuth.UserInfo;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.CertifiedDelivery;
import com.docusign.esign.model.CorrectViewRequest;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeUpdateSummary;
import com.docusign.esign.model.Expirations;
import com.docusign.esign.model.Notification;
import com.docusign.esign.model.RecipientEmailNotification;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.Reminders;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.ViewUrl;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import fr.opensagres.xdocreport.core.io.IOUtils;
import mx.engineer.utils.pdf.PDFUtils;
import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.business.digitalsignature.ApiRestUtils;
import mx.pagos.admc.contracts.constants.DsMessagesConstants;
import mx.pagos.admc.contracts.constants.ESMessagesConstants;
import mx.pagos.admc.contracts.interfaces.InformationDocSign;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Contactable;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Documentable;
import mx.pagos.admc.contracts.interfaces.digitalsignature.Recipientable;
import mx.pagos.admc.contracts.interfaces.digitalsignature.UserInformatable;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.InfoDocument;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.digitalsignature.ContactDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.DigitalSignatureProviderEnum;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.admc.enums.DocuSignStatusEnum;
import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.ConfigurationException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.DirUtil;


@Service
public class DocuSignBusiness {
	
	
	@Autowired
	private UserSession session;
	
	@Autowired
	private ConfigurationsBusiness configuration;
	
	@Autowired
	private ConfigurationsBusiness configurationsBusiness;

	@Autowired
	Configurable configurable;
	
	@Autowired
    private UserSession userSession;

	@Autowired
	private Recipientable recipientable;

	@Autowired
	private Documentable documentable;

	@Autowired
	@Lazy
	private UserInformatable userInformatable;
		
	@Autowired
	 @Lazy
	private Contactable contactable;
	
	@Autowired
	@Lazy
	private InformationDocSign infoDocuSignDAO;

	@Autowired
	private RequisitionBusiness requisitionBusiness;
	
	@Autowired
	PscWorldBusiness pscWorldBusiness;
		
	private String CLIENT_ID;

	private static final Logger LOG = Logger.getLogger(DocuSignBusiness.class);
	private static final String MESSAGE_RETRIEVING_BASE_PATH_ERROR = "Hubo un problema al recuperar la ruta base para guardar los archivos";
	private static final String devCenterPage = "https://developers.docusign.com/platform/auth/consent";

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public DocumentDS sendDocument(DocumentDS documentDS) throws Exception {
		try {

			File pdf=  new File(this.requisitionBusiness.findTemplate(documentDS.getIdRequisition()));
			String docxBaseName = FilenameUtils.getBaseName(pdf.getName());
			String pdfPathh = this.buildPath(documentDS.getIdRequisition(),"Solicitudes")+ File.separator + docxBaseName + Constants.PDF_FILE;
			String rutaSolicitud = pdfPathh.replace("/", "\\");
		    String nuevarutaSolicitud = rutaSolicitud.replace("\\\\", "\\");

			
			String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
			ApiClient apiClient = new ApiClient(urlApi);

			String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
			apiClient.setOAuthBasePath(oAuthBasePath);

			OAuthToken oAuthToken = getAuthToken(apiClient);

			String accessToken = oAuthToken.getAccessToken();
			UserInfo userInfo = apiClient.getUserInfo(accessToken);
			String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();

			EnvelopeDefinition envelope = new EnvelopeDefinition();
			envelope.setEmailSubject(documentDS.getEmailSubject());
			envelope.setEmailBlurb(documentDS.getEmailMessage());
			envelope.setStatus(DocuSignStatusEnum.CREATED.getValue());
			

			Recipients recipients = getRecipients(documentDS);
			envelope.setRecipients(recipients);
			
			setConfigurationsEnvelopeNotification(envelope, documentDS);
			documentDS.setFilePath(nuevarutaSolicitud);
			documentDS.setDocumentName(docxBaseName);
			

			Document document = new Document();
			document.setDocumentBase64(encodeToBase64(nuevarutaSolicitud.toString()));
			document.setName(FilenameUtils.getBaseName(docxBaseName));
			document.setFileExtension(Constants.STRING_PDF);
			document.setDocumentId(documentDS.getIdRequisition().toString());
			envelope.setDocuments(Arrays.asList(document));
			
			apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);

			EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
			EnvelopeSummary resultDocuSign = envelopesApi.createEnvelope(accountId, envelope);

			saveInDatabase(resultDocuSign, documentDS);
			
			return new DocumentDS(ESMessagesConstants.SUCCESS_CODE, ESMessagesConstants.SUCCESS_MESSAGE);

		} catch (ApiException apiException) {

			LOG.error("Ha ocurrido un error al consumir la API");
			apiException.printStackTrace();

			if (!apiException.getMessage().contains("consent_required")) {
				throw apiException;
			}

			try {

				LOG.error("Se requiere consentimiento, brinde su consentimiento en la ventana del navegador y "
						+ "luego ejecute esta aplicación nuevamente.");
				
				String account=this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
				StringBuilder urlStringBuilder = new StringBuilder();
				urlStringBuilder.append("https://");
				urlStringBuilder.append(account);

				urlStringBuilder.append("/oauth/auth");
				urlStringBuilder.append("?response_type=code");
				urlStringBuilder.append("&scope=impersonation%20signature");
				urlStringBuilder.append("&client_id="+this.CLIENT_ID );
				urlStringBuilder.append("&redirect_uri="+ devCenterPage);

				//https://account-d.docusign.com/oauth/auth?response_type=code&scope=impersonation%20signature&client_id=30d6e157-7fa5-4098-ada8-0ca519788237&redirect_uri=https://developers.docusign.com/platform/auth/consent
				
				String url= urlStringBuilder.toString();
				
				LOG.info(url);
				
				Desktop.getDesktop().browse(new URI(url));
				
				throw new SecurityException("Se requieren permisos adicionales para hacer uso del proveedor");

			} catch (Exception exception) {
				exception.printStackTrace();
				throw exception;
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

		
		
//		@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
//		public DocumentDS statusDocuSignDocument(DocumentDS documentDS) throws BusinessException, IllegalArgumentException, IOException, ApiException {
//			 Properties prop = new Properties();
//		        String fileName = "app2.config";
//		        FileInputStream fis = new FileInputStream(fileName);
//		        prop.load(fis);
//		        
//			String envelopeId = this.envelopeId; // Reemplaza con el ID del sobre que deseas consultar
//	        String accountId = this.accountId; // Reemplaza con tu ID de cuenta de DocuSign
//
//            ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
//            apiClient.setOAuthBasePath("account-d.docusign.com");
//	        
////	        HttpHeaders headers = new HttpHeaders();
////	        headers.setBasicAuth(email, password);
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.setBasicAuth(email, password); // Reemplaza email y password con tus credenciales de DocuSign
//
//	        
//	        RestTemplate restTemplate = new RestTemplate();
//	        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class, accountId, envelopeId);
//
//	        if (response.getStatusCode() == HttpStatus.OK) {
//	            System.out.println("Respuesta de la API:");
//	            System.out.println(response.getBody());
//	        } else {
//	            System.out.println("Error al consultar el estatus del sobre: " + response.getStatusCode());
//	        }
//			return documentDS;
//	    }

		@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
		public InfoDocument statusDocuSignDocument(DocumentDS documentDS) throws Exception {
			InfoDocument info=new InfoDocument();
			DocumentDS document=new DocumentDS();
			LOG.info("//////////////////////////////////");
			LOG.info("ENTRO EL ID ::::: "+documentDS.getIdRequisition());
			document=documentable.findDocumentByIdRequisition(documentDS.getIdRequisition());
			LOG.info("ENTRO EL IDDOCUMENT ::::: "+document.getProviderDocumentId());
			String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
			ApiClient apiClient = new ApiClient(urlApi);

			String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
			apiClient.setOAuthBasePath(oAuthBasePath);

			OAuthToken oAuthToken = getAuthToken(apiClient);

			String accessToken = oAuthToken.getAccessToken();
			UserInfo userInfo = apiClient.getUserInfo(accessToken);
			String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();

			apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);

		     LOG.info("ENTRO A LA CONFIGURACION ::::: ");
		     EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);


		     String envelopeId = document.getProviderDocumentId();
		     LOG.info("ENTRO A RECIBIR EL ID DEL SOBRE ::::: ");
		     Envelope envelope = envelopesApi.getEnvelope(accountId, envelopeId);
		     
		     LOG.info("ENTRO AL ESTADO DEL SOBRE ::::: ");
		     String envelopeStatus = envelope.getStatus();
		     info.setEmailMessage(document.getEmailMessage());
		     info.setEmailSubject(document.getEmailSubject());
		     info.setStatusDigitalSignature(envelopeStatus);
		     info.setCreatedAt(document.getCreatedAt().toString());
		     System.out.println("Estado del sobre: " + envelopeStatus);
		        
			return info;
	    }


	
		
		public String posicionFirma(int idRequisition) throws IOException, URISyntaxException, DatabaseException {

			DocumentDS documentDS=documentable.findDocumentByIdRequisition(idRequisition);
			try
            {
				String urlDocusign=this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_ADD_FIELDS);
				String url=urlDocusign+documentDS.getProviderDocumentId() +Constants.STRING_ADD_FIELDS;
//				String url="https://apps-d.docusign.com/send/prepare/"+documentDS.getProviderDocumentId() +"/add-fields";
//			 	Desktop.getDesktop().browse(new URI("https://account-d.docusign.com/send/prepare/"+documentDS.getProviderDocumentId() +"/add-fields"));
				return url;
            }
            catch (Exception e)
            {
                System.out.print ("Error al encontrar la ruta de posicion de firma DocuSign!!!  ");
                System.out.print (e.getMessage());
            }
			return null;


		}
		
		
			
		public void infoSignDocusign(EnvelopeSummary results,DocumentDS document ) throws DatabaseException {
//			this.docuSignDAO.saveInfoDocumentDocuSign(results.getEnvelopeId(), results.getStatus(),results.getStatusDateTime());
			LOG.info("Indicando path del documento a enviar: "); // Log temporal
			try {
			Integer idDocument = null;
			DocumentDS documentDS = new DocumentDS(); 
//			documentDS.setIdDocument(document.getIdDocument());
			documentDS.setIdUser(document.getIdUser()); 
			documentDS.setIdRequisition(document.getIdRequisition()); 
			documentDS.setDocumentName(document.getDocumentName()); 
			documentDS.setDigitalSignatureProvider(DigitalSignatureProviderEnum.DOCUSIGN); 
			documentDS.setProviderDocumentId(results.getEnvelopeId()); 
			documentDS.setFilePath(document.getFilePath()); 
			documentDS.setOnlySigner(Boolean.FALSE); 
			documentDS.setEmailSubject(document.getEmailSubject()); 
			documentDS.setEmailMessage(document.getEmailMessage()); 
			documentDS.setStatusDigitalSignature(DigitalSignatureStatusEnum.SENT); 
			documentDS.setCreatedAt(new Date()); 
			documentDS.setUpdatedAt(new Date());
			LOG.info("Indicando path del documento a enviar: " + documentDS.getClass().toString()); // Log temporal
			idDocument = documentable.save(documentDS);
			 
			for(Recipient contentRecipient: document.getRecipients()) {
				Recipient recipient = new Recipient(); 
			if(contentRecipient.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_SIGN))
			recipient.setIdDocument(idDocument); 
			recipient.setRecipientAction(RecipientActionEnum.NEEDS_TO_SIGN);
			recipient.setProviderRecipientId(contentRecipient.getProviderRecipientId()); 
			recipient.setSigningOrder(contentRecipient.getSigningOrder()); 
			recipient.setRfc(contentRecipient.getRfc()); 
			recipient.setFullName(contentRecipient.getFullName()); 
			recipient.setEmail(contentRecipient.getEmail()); 
			recipient.setSecretCode(contentRecipient.getSecretCode()); 
			recipient.setNote(contentRecipient.getNote()); 
			recipient.setWidgetId(contentRecipient.getWidgetId()); 
			recipient.setSigned(Boolean.FALSE); 
			recipient.setCreatedAt(new Date()); 
			recipient.setUpdatedAt(new Date());
			if(contentRecipient.getRecipientAction().equals(RecipientActionEnum.RECEIVES_A_COPY)) {
			recipient.setIdDocument(idDocument); 
			recipient.setRecipientAction(RecipientActionEnum.RECEIVES_A_COPY);
			recipient.setProviderRecipientId(contentRecipient.getProviderRecipientId()); 
			recipient.setSigningOrder(contentRecipient.getSigningOrder()); 
			recipient.setRfc(""); 
			recipient.setFullName(contentRecipient.getFullName()); 
			recipient.setEmail(contentRecipient.getEmail()); 
			recipient.setSecretCode(contentRecipient.getSecretCode()); 
			recipient.setNote(contentRecipient.getNote()); 
			recipient.setWidgetId(contentRecipient.getWidgetId()); 
			recipient.setSigned(Boolean.FALSE); 
			recipient.setCreatedAt(new Date()); 
			recipient.setUpdatedAt(new Date());
			}
			recipientable.save(recipient);			
			LOG.info("EL RECIPIENTE CONTIENE LO SIGUIENTE: \n"+recipient.getIdDocument()+
					"\n"+recipient.getRecipientAction()+"\n"+recipient.getProviderRecipientId() +
					"\n"+recipient.getSigningOrder() +"\n"+recipient.getRfc() +
					"\n"+recipient.getFullName() +"\n"+recipient.getEmail() +
					"\n"+recipient.getSecretCode() +"\n"+recipient.getNote() +
					"\n"+recipient.getWidgetId() +"\n"+recipient.getSigned() +
					"\n"+recipient.getCreatedAt()  +"\n"+ recipient.getUpdatedAt() +"\n" +" /////// FIN RECIPIENTE"); // Log temporal
			}
			}catch (Exception e) {
				// TODO: handle exception
				LOG.error("Hubo un error en el metodo infoSignDocusign : "+e);
			}
			
		}
		
		public void updateStatusDocument(int idRequisition,DocumentDS document ) throws DatabaseException {
			try {
				DocumentDS documentDS = new DocumentDS();
				LOG.info("///////////////////////////////////");
				LOG.info("EL ESTADO DEL SOBRE ES :::::: "+document.getStatusDigitalSignature());
				switch (document.getStatusDigitalSignature()) {
	            case SENT:
	                System.out.println("El estado es SENT.");
	    			documentDS.setStatusDigitalSignature(DigitalSignatureStatusEnum.SENT); 
	    			recipientable.updateStatusDocumentSigned(idRequisition,documentDS);
	                break;
	            case IN_PROGRESS:
	                System.out.println("El estado es IN_PROGRESS.");
	    			documentDS.setStatusDigitalSignature(DigitalSignatureStatusEnum.IN_PROGRESS); 
	    			recipientable.updateStatusDocumentSigned(idRequisition,documentDS);
	                break;
	            case SIGNED:
	                System.out.println("El estado es SIGNED.");
	    			documentDS.setStatusDigitalSignature(DigitalSignatureStatusEnum.SIGNED); 
	    			
					File conservationCertificate = pscWorldBusiness.getConservationCertificate(document);
					if (conservationCertificate == null) {
						LOG.info("Creando el certificado de conservación para el folio (idRequisition): "
								+ idRequisition);
						generateConservationCertificate(document);
					}
	    			
	    			recipientable.updateStatusDocumentSigned(idRequisition,documentDS);
	                break;
	            default:
	                System.out.println("Estado desconocido.");
	                // Código a ejecutar para el caso por defecto (opcional)
	                break;
	        }
//				recipientable.updateStatusDocumentSigned(idRequisition,document);
			}catch (Exception e) {
				// TODO: handle exception
				LOG.error("Hubo un error en el metodo updateStatusDocument : "+e);
			}
		}
		
		public void updateStatusRecipienSignedDocument(DocumentDS document ) throws DatabaseException {
			try {
				Recipient recipient= new Recipient();
				DocumentDS documentDS=documentable.findDocumentByIdRequisition(document.getIdRequisition());
				recipient.setSigned(Boolean.TRUE);
				
				LOG.info("///////////////////////////////////");
				LOG.info("EL ESTADO DE LA FIRMA :::::: "+recipient.getSigned());
				LOG.info("EL ID RECIPIENT :::::: "+documentDS.getIdDocument()); 
				
				recipientable.updateStatusRecipientSigned(documentDS.getIdDocument(),recipient);
				
			}catch (Exception e) {
				// TODO: handle exception
				LOG.error("Hubo un error en el metodo updateStatusDocument : "+e);
			}
		}
		
		public void deleteRecipienSignedDocument(DocumentDS documentDS ) throws DatabaseException {
			try {
				InfoDocument info=new InfoDocument();
				DocumentDS document=new DocumentDS();
				LOG.info("//////////////////////////////////");
				LOG.info("ENTRO EL ID ::::: "+documentDS.getIdRequisition());
				document=documentable.findDocumentByIdRequisition(documentDS.getIdRequisition());
				LOG.info("ENTRO EL IDDOCUMENT ::::: "+document.getProviderDocumentId());
				 Properties prop = new Properties();
			        String fileName = "app2.config";
			        FileInputStream fis = new FileInputStream(fileName);
			        prop.load(fis);
			        if(document.getProviderDocumentId().length()>0) {
			        // Crea una instancia del cliente
			        ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
		            apiClient.setOAuthBasePath("account-d.docusign.com");
//		            Configura la autenticación OAuth con tu token de acceso
		            ArrayList<String> scopes = new ArrayList<String>();
		            scopes.add("signature");
		            scopes.add("impersonation");
		            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));
		            OAuthToken oAuthToken = apiClient.requestJWTUserToken(
		                prop.getProperty("clientId"),
		                prop.getProperty("userId"),
		                scopes,
		                privateKeyBytes,
		                3600);
		            String accessToken = oAuthToken.getAccessToken();
		            UserInfo userInfo = apiClient.getUserInfo(accessToken);
		            String accountId = userInfo.getAccounts().get(0).getAccountId();
		            LOG.info("ENTRO EL CONEXION ::::: ");
			        // Configura la autenticación OAuth con tu token de acceso
//			        OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
//			            "your_client_id",
//			            "your_user_id",
//			            "your_private_key_bytes",
//			            "your_integration_key",
//			            3600
//			        );

			       // Configura el token en el cliente
			        apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());
			        LOG.info("ENTRO A LA CONFIGURACION ::::: ");
			     EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

			     // Reemplaza "your_envelope_id" con el ID del sobre que deseas verificar
			     String envelopeId = document.getProviderDocumentId();
			     LOG.info("ENTRO A RECIBIR EL ID DEL SOBRE ::::: ");
			     // Obtiene el estado del sobre
			     Envelope envelope = envelopesApi.getEnvelope(accountId, envelopeId);
			     LOG.info("ENTRO AL ESTADO DEL SOBRE ::::: ");
			     // Accede al estado del sobre
			     String envelopeStatus = envelope.getStatus();
//			     info.setDocumntName(envelope.getD);
			     info.setEmailMessage(document.getEmailMessage());
			     info.setEmailSubject(document.getEmailSubject());
			     info.setStatusDigitalSignature(envelopeStatus);
			     info.setCreatedAt(document.getCreatedAt().toString());
			     System.out.println("Estado del sobre: " + envelopeStatus);
			   

			        try {
			        	// Update envelope status to "deleted"
			            Envelope env = new Envelope();
			            env.setStatus("voided");
			            env.setVoidedReason("Razón de anulación aquí");

			            EnvelopeUpdateSummary envSummary = new EnvelopeUpdateSummary();
			            envSummary.setEnvelopeId(envelopeId);

//			            EnvelopeUpdateResults results = envelopesApi.update(accountId, envelopeId, env);
			            envelopesApi.update(accountId, envelopeId, env);

			            System.out.println("El sobre ha sido eliminado correctamente.");
			        } catch (ApiException e) {
			            System.err.println("Hubo un error al intentar eliminar el sobre: " + e.getResponseBody());
			        }
			     
			        }
			
			}catch (Exception e) {
				LOG.error("Hubo un error en el metodo updateStatusDocument : "+e);
			}
		}
		public void reseendSignedDocument(DocumentDS documentDS) throws Exception {
			try {
				
				String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
				ApiClient apiClient = new ApiClient(urlApi);

				String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
				apiClient.setOAuthBasePath(oAuthBasePath);

				OAuthToken oAuthToken = getAuthToken(apiClient);

				String accessToken = oAuthToken.getAccessToken();
				UserInfo userInfo = apiClient.getUserInfo(accessToken);
				String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();


				String envelopeId = documentDS.getProviderDocumentId();
				
				apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);

				EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
				
//				Recipients recipients = getRecipients(documentDS);
				
				
//				RecipientEmailNotification recipientNotification = new RecipientEmailNotification();
//				recipientNotification.setSupportedLanguage("es"); 
				
				EnvelopesApi.UpdateOptions updateOptions = envelopesApi.new UpdateOptions();
				updateOptions.setResendEnvelope("true");
				
				Envelope envelope = new Envelope();
				envelope.setEmailSubject(documentDS.getEmailSubject());
	            envelope.setStatus("sent");
//	            envelope.setRecipients(recipients);
//	            setConfigurationsEnvelopeNotification(envelope, documentDS);
				
		            EnvelopeUpdateSummary updateSummary = envelopesApi.update(accountId, envelopeId, envelope,updateOptions);
		            System.out.println("El sobre ha sido reenviado exitosamente. Resumen: " + updateSummary);

		        } catch (ApiException e) {
		            System.err.println("Error resending envelope: " + e.getResponseBody());
		        }
		}
		
		public DocumentDS correctSignedDocument(DocumentDS documentDS) throws Exception {
			try {
				
				String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
				ApiClient apiClient = new ApiClient(urlApi);

				String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
				apiClient.setOAuthBasePath(oAuthBasePath);

				OAuthToken oAuthToken = getAuthToken(apiClient);

				String accessToken = oAuthToken.getAccessToken();
				UserInfo userInfo = apiClient.getUserInfo(accessToken);
				String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();


				String envelopeId = documentDS.getProviderDocumentId();
				
				apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);

				EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
				
				
				EnvelopesApi.UpdateOptions updateOptions = envelopesApi.new UpdateOptions();
				updateOptions.setResendEnvelope("true");
				DocumentDS rutadocument=new DocumentDS();
			
		            
		            CorrectViewRequest correctViewRequest = new CorrectViewRequest();
		            correctViewRequest.setSuppressNavigation("true"); // Opciones adicionales según tus necesidades

		            ViewUrl viewUrl = envelopesApi.createCorrectView(accountId, envelopeId, correctViewRequest);

		            // Imprimir la URL de la vista correcta
		            System.out.println("URL de la vista correcta: " + viewUrl.getUrl());
		            rutadocument.setExtension(viewUrl.getUrl().toString());
		            return rutadocument;
		        } catch (ApiException e) {
		            System.err.println("Error resending envelope: " + e.getResponseBody());
		        }
			return null;
		}
		
		public DocumentDS viewSignedDocument(DocumentDS documentDS) throws Exception {
			try {
				
				String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
				ApiClient apiClient = new ApiClient(urlApi);

				String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
				apiClient.setOAuthBasePath(oAuthBasePath);

				OAuthToken oAuthToken = getAuthToken(apiClient);

				String accessToken = oAuthToken.getAccessToken();
				UserInfo userInfo = apiClient.getUserInfo(accessToken);
				String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();


				String envelopeId = documentDS.getProviderDocumentId();
				
				apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);

				EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
				
				DocumentDS rutadocument=new DocumentDS();
				
				RecipientViewRequest viewRequest = new RecipientViewRequest();
	            viewRequest.setReturnUrl("https://google.com");
	            viewRequest.setAuthenticationMethod("gemelo_mizraim@hotmail.com"); // You can change the authentication method if needed
//
//	            // Specify recipient information
//	            viewRequest.setClientUserId("unique-recipient-id"); // This should be unique for each recipient
//	            viewRequest.setRecipientId("1"); // Assuming recipient id is 1, update as needed

	            // Generate the recipient view URL
	            ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);

	            System.out.println("Recipient View URL: " + viewUrl.getUrl());

		            System.out.println("URL de la vista correcta: " + viewUrl.getUrl());
		            rutadocument.setExtension(viewUrl.getUrl().toString());
		            return rutadocument;
		        } catch (ApiException e) {
		            System.err.println("Error resending envelope: " + e.getResponseBody());
		        }
			return null;
		}
		
		
		
		
		public void reenviarSignedDocument(DocumentDS documentDS ) throws DatabaseException {
			try {
				InfoDocument info=new InfoDocument();
				DocumentDS document=new DocumentDS();
				document=documentable.findDocumentByIdRequisition(documentDS.getIdRequisition());
				 Properties prop = new Properties();
			        String fileName = "app2.config";
			        FileInputStream fis = new FileInputStream(fileName);
			        prop.load(fis);
			        if(document.getProviderDocumentId().length()>0) {
			        ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
		            apiClient.setOAuthBasePath("account-d.docusign.com");
		            ArrayList<String> scopes = new ArrayList<String>();
		            scopes.add("signature");
		            scopes.add("impersonation");
		            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(prop.getProperty("rsaKeyFile")));
		            OAuthToken oAuthToken = apiClient.requestJWTUserToken(
		                prop.getProperty("clientId"),
		                prop.getProperty("userId"),
		                scopes,
		                privateKeyBytes,
		                3600);
		            String accessToken = oAuthToken.getAccessToken();
		            UserInfo userInfo = apiClient.getUserInfo(accessToken);
		            String accountId = userInfo.getAccounts().get(0).getAccountId();
		            LOG.info("ENTRO EL CONEXION ::::: ");
			        // Configura la autenticación OAuth con tu token de acceso
//			        OAuth.OAuthToken oAuthToken = apiClient.requestJWTUserToken(
//			            "your_client_id",
//			            "your_user_id",
//			            "your_private_key_bytes",
//			            "your_integration_key",
//			            3600
//			        );
			     apiClient.setAccessToken(oAuthToken.getAccessToken(), oAuthToken.getExpiresIn());
			     EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
			     String envelopeId = document.getProviderDocumentId();
			     Envelope envelope = envelopesApi.getEnvelope(accountId, envelopeId);
			     String envelopeStatus = envelope.getStatus();
			     info.setEmailMessage(document.getEmailMessage());
			     info.setEmailSubject(document.getEmailSubject());
			     info.setStatusDigitalSignature(envelopeStatus);
			     info.setCreatedAt(document.getCreatedAt().toString());
			     System.out.println("Estado del sobre: " + envelopeStatus);
			   

			        try {
			        	Signer signer = new Signer();
			        	for (Recipient recipientRequest : documentDS.getRecipients()) {
			        	if (recipientRequest.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_SIGN)) {
							signer.setEmail(recipientRequest.getEmail());
							signer.setName(recipientRequest.getFullName());
							
							RecipientEmailNotification configEmail = new RecipientEmailNotification();
//							configEmail.setSupportedLanguage(Constants.LOCALE_ES);
							String secretNote = StringUtils.isEmptyString(recipientRequest.getNote())
									? null : recipientRequest.getNote();
							configEmail.emailBody(secretNote);
							signer.setEmailNotification(configEmail);
							
							String accessCode = StringUtils.isEmptyString(recipientRequest.getSecretCode())
									? null : recipientRequest.getSecretCode();
							signer.accessCode(accessCode);
							signer.recipientId("1");
							recipientRequest.setProviderRecipientId("1");
							continue;
						}
			        	}
			             Recipients recipients = new Recipients();
			             recipients.setSigners(List.of(signer));
			             
			            Envelope env = new Envelope();
			            env.setStatus("sent"); // Cambia el estado a "sent" para reenviar el sobre
			            env.setVoidedReason("Por favor firme el documento ");
			            env.setRecipients(recipients);


			            EnvelopeUpdateSummary updateSummary = envelopesApi.update(accountId, envelopeId, env);
			            System.out.println("El sobre ha sido reenviado exitosamente. Resumen: " + updateSummary);
			        } catch (ApiException e) {
			            System.err.println("Hubo un error al intentar reenviar el sobre: " + e.getResponseBody());
			        }
			     
			        }
			
			}catch (Exception e) {
				LOG.error("Hubo un error en el metodo reenviarSignedDocument : "+e);
			}
		}
		
		public File getDocument(DocumentDS documentRequest) throws BusinessException, InvalidFormatException {
			try {

				Integer idRequest = documentRequest.getIdRequisition();

				if (idRequest == null || idRequest == 0) {
					throw new NullPointerException("No se especificó un idRequisition válido");
				}

				Boolean isColorMark = Boolean.FALSE;
				File docx = this.requisitionBusiness.downloadDraftRequisition(idRequest, isColorMark);
				String docxBaseName = FilenameUtils.getBaseName(docx.getName());

				String pdfPath = getTemporalPath() + File.separator + docxBaseName + Constants.PDF_FILE;
				File pdf = new File(pdfPath);

				String OFFICE_HOME = this.configurable.findByName(Constants.CONFIG_DS_LIBRE_OFFICE_PATH);

				LOG.info("Inicia conversión de Docx a PDF utilizando LibreOffice");
				PDFUtils.convertDocxToPDF(OFFICE_HOME, docx, pdf);
				LOG.info("Finaliza conversión de Docx a PDF");

				return pdf;

			} catch (Exception exception) {
				throw new BusinessException(exception);
			}
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
		
		
		public String createPath(final String idSession) throws BusinessException {
			try {
				
				return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) 
						+ File.separator + Constants.PATH_DIGITAL_SIGNATURE
						+ File.separator + idSession;
	            
			} catch (Exception exception) {
				LOG.error(DsMessagesConstants.ERROR_FILE_PATH, exception);
	            throw new BusinessException(DsMessagesConstants.ERROR_FILE_PATH, exception);
			}
	    }
		

		
		
		/**
		 * Register a new contact to the user
		 * @param isOnlySigner
		 * @param recipient
		 * @throws BusinessException
		 */
		@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
		private void validateContact(final Boolean isOnlySigner, Recipient recipient) throws BusinessException {
			try {
				
				if (isOnlySigner) {
					return;
				}
				
				if (recipient.getIdContact() != null) {
					return;
				}
				
				Integer idUser = this.session.getIdUsuarioSession();
				LOG.info("Registrando nuevo contacto '" + recipient.getFullName() + "' del usuario " + idUser);
				ContactDS newContact = new ContactDS();
				newContact.setIdUser(idUser);
				newContact.setFullName(recipient.getFullName());
				newContact.setRfc(recipient.getRfc());
				newContact.setEmail(recipient.getEmail());
				newContact.setCreatedAt(new Date());
				newContact.setUpdatedAt(new Date());
				contactable.save(newContact);
				LOG.info("Contacto registrado");
				
			} catch (DatabaseException databaseException) {
				LOG.error(DsMessagesConstants.ERROR_USER_INFORMATION, databaseException);
				throw new BusinessException(DsMessagesConstants.ERROR_USER_INFORMATION, databaseException);
				
			} catch (Exception exception) {
				throw new BusinessException(exception);
			}
		}
		



	private OAuthToken getAuthToken(ApiClient apiClient) throws Exception {
		try {

			ArrayList<String> scopes = new ArrayList<String>();
			scopes.add(Constants.CONFIG_DS_DOCUSIGN_SCOPE_SIGNATURE);
			scopes.add(Constants.CONFIG_DS_DOCUSIGN_SCOPE_IMPERSONATION);

			Properties properties = new Properties();
			String fileName = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_PATH_FILE_CONFIG);
			FileInputStream fileInputStream = new FileInputStream(fileName);
			properties.load(fileInputStream);

			this.CLIENT_ID = properties.getProperty(Constants.CONFIG_DS_DOCUSIGN_STRING_CLIENT_ID);

			Path path = Paths.get(properties.getProperty(Constants.CONFIG_DS_DOCUSIGN_RSA_KEY_FILE));
			byte[] privateKeyBytes = Files.readAllBytes(path);
			return apiClient.requestJWTUserToken(properties.getProperty(Constants.CONFIG_DS_DOCUSIGN_STRING_CLIENT_ID),
					properties.getProperty(Constants.CONFIG_DS_DOCUSIGN_STRING_USER_ID), scopes, privateKeyBytes, 3600);

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	private Recipients getRecipients(DocumentDS documentDS) throws Exception {
		try {

			List<CertifiedDelivery> viewers = new ArrayList<>();
			List<Signer> signers = new ArrayList<>();
			List<CarbonCopy> carbonCopies = new ArrayList<>();

			Integer idRecipient = 1;
			for (Recipient recipientRequest : documentDS.getRecipients()) {

				if (recipientRequest.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_VIEW)) {
					CertifiedDelivery certifiedDelivery = new CertifiedDelivery();
					certifiedDelivery.setEmail(recipientRequest.getEmail());
					certifiedDelivery.setName(recipientRequest.getFullName());
					certifiedDelivery.setRoutingOrder(recipientRequest.getSigningOrder().toString());

					
					RecipientEmailNotification configEmail = new RecipientEmailNotification();
					String secretNote = StringUtils.isEmptyString(recipientRequest.getNote())
							? null : recipientRequest.getNote();
					certifiedDelivery.setNote(secretNote);
					
					String accessCode = StringUtils.isEmptyString(recipientRequest.getSecretCode())
							? null : recipientRequest.getSecretCode();
					certifiedDelivery.accessCode(accessCode);
					
					certifiedDelivery.recipientId(idRecipient.toString());
					viewers.add(certifiedDelivery);

					recipientRequest.setProviderRecipientId(idRecipient.toString());
					idRecipient++;
					continue;
				}

				if (recipientRequest.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_SIGN)) {
					Signer signer = new Signer();
					signer.setEmail(recipientRequest.getEmail());
					signer.setName(recipientRequest.getFullName());
					signer.setRoutingOrder(recipientRequest.getSigningOrder().toString());

					
					RecipientEmailNotification configEmail = new RecipientEmailNotification();
					String secretNote = StringUtils.isEmptyString(recipientRequest.getNote())
							? null : recipientRequest.getNote();
					signer.setNote(secretNote);
					
					String accessCode = StringUtils.isEmptyString(recipientRequest.getSecretCode())
							? null : recipientRequest.getSecretCode();
					signer.accessCode(accessCode);
					
					signer.recipientId(idRecipient.toString());
					signers.add(signer);

					recipientRequest.setProviderRecipientId(idRecipient.toString());
					idRecipient++;
					continue;
				}

				if (recipientRequest.getRecipientAction().equals(RecipientActionEnum.RECEIVES_A_COPY)) {
					CarbonCopy carbonCopy = new CarbonCopy();
					carbonCopy.setEmail(recipientRequest.getEmail());
					carbonCopy.setName(recipientRequest.getFullName());
					carbonCopy.setRoutingOrder(recipientRequest.getSigningOrder().toString());
					
					RecipientEmailNotification configEmail = new RecipientEmailNotification();
					String secretNote = StringUtils.isEmptyString(recipientRequest.getNote())
							? null : recipientRequest.getNote();
					carbonCopy.setNote(secretNote);
					
					String accessCode = StringUtils.isEmptyString(recipientRequest.getSecretCode())
							? null : recipientRequest.getSecretCode();
					carbonCopy.accessCode(accessCode);
					
					carbonCopy.recipientId(idRecipient.toString());
					carbonCopies.add(carbonCopy);

					recipientRequest.setSigned(Boolean.TRUE);
					recipientRequest.setProviderRecipientId(idRecipient.toString());
					idRecipient++;
					continue;
				}

				LOG.error("El destinatario " + recipientRequest.getFullName() + " no tiene una acción válida");
				throw new Exception(
						"El destinatario " + recipientRequest.getFullName() + " no tiene una acción válida");

			}

			Recipients recipients = new Recipients();
			recipients.setCertifiedDeliveries(viewers);
			recipients.setSigners(signers);
			recipients.setCarbonCopies(carbonCopies);

			return recipients;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}


	
	private void setConfigurationsEnvelopeNotification(EnvelopeDefinition envelope, DocumentDS documentDS) {
		try {
			
			Boolean hasExpiration = documentDS.isHasExpiration();
			Boolean hasReminders = documentDS.isHasReminders();
			
			
			Notification notification = new Notification();
			
				Reminders reminders = new Reminders();
				reminders.setReminderEnabled(Constants.TRUE_STRING);

				String reminderDelay = documentDS.getDaysBeforeFirstReminder() != null
						? documentDS.getDaysBeforeFirstReminder().toString()
						: Constants.ONE_STRING;
				reminders.setReminderDelay(reminderDelay);

				String reminderFrequency = documentDS.getDaysBetweenReminders() != null
						? documentDS.getDaysBetweenReminders().toString()
						: Constants.THREE_STRING;
				reminders.setReminderFrequency(reminderFrequency);

				notification.setReminders(reminders);

				
				Expirations expirations = new Expirations();
				expirations.setExpireEnabled(Constants.TRUE_STRING);
				
				String expireAfter = documentDS.getDaysValidity() != null
						? documentDS.getDaysValidity().toString()
						: Constants.THIRTY_STRING;
				expirations.setExpireAfter(expireAfter);
				
				
				notification.setExpirations(expirations);

	        envelope.setNotification(notification);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	private void saveInDatabase(EnvelopeSummary resultDocuSign, DocumentDS documentRequest) throws Exception {
		try {

			DocumentDS documentDS = new DocumentDS();
			documentDS.setIdUser(session.getIdUsuarioSession());
			documentDS.setIdRequisition(documentRequest.getIdRequisition());
			documentDS.setDocumentName(documentRequest.getDocumentName());
			documentDS.setDigitalSignatureProvider(DigitalSignatureProviderEnum.DOCUSIGN);
			documentDS.setProviderDocumentId(resultDocuSign.getEnvelopeId());
			documentDS.setFilePath(documentRequest.getFilePath());
			documentDS.setOnlySigner(documentRequest.isOnlySigner());
			documentDS.setEmailSubject(documentRequest.getEmailSubject());
			documentDS.setEmailMessage(documentRequest.getEmailMessage());
			documentDS.setStatusDigitalSignature(DigitalSignatureStatusEnum.SENT);
			documentDS.setCreatedAt(new Date());
			documentDS.setUpdatedAt(new Date());

			Integer documentId = documentable.save(documentDS);

			for (Recipient recipientRequest : documentRequest.getRecipients()) {

				Recipient newRecipient = new Recipient();
				newRecipient.setIdDocument(documentId);
				newRecipient.setRecipientAction(recipientRequest.getRecipientAction());
				newRecipient.setProviderRecipientId(recipientRequest.getProviderRecipientId());
				newRecipient.setSigningOrder(recipientRequest.getSigningOrder());
				newRecipient.setFullName(recipientRequest.getFullName());
				newRecipient.setEmail(recipientRequest.getEmail());
				newRecipient.setRfc("");
				newRecipient.setSecretCode(recipientRequest.getSecretCode());
				newRecipient.setNote(recipientRequest.getNote());
				newRecipient.setWidgetId(null);
//				newRecipient.setSigned(Boolean.FALSE);
				if(recipientRequest.getRecipientAction().equals(RecipientActionEnum.RECEIVES_A_COPY)) {
					newRecipient.setSigned(Boolean.TRUE);
				}
				if(recipientRequest.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_SIGN)) {
					newRecipient.setSigned(Boolean.FALSE);
				}
				if(recipientRequest.getRecipientAction().equals(RecipientActionEnum.NEEDS_TO_VIEW)) {
					newRecipient.setSigned(Boolean.FALSE);
				}
				newRecipient.setCreatedAt(new Date());
				newRecipient.setUpdatedAt(new Date());

				recipientable.save(newRecipient);
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}

	public static String encodeToBase64(String filePath) {
		String base64 = null;
		try {
			Path path = Paths.get(filePath);
			byte[] fileBytes = Files.readAllBytes(path);
			base64 = Base64.getEncoder().encodeToString(fileBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return base64;
	}
	

	public void downloadDocument(DocumentDS documentDS, final HttpServletResponse response)
			throws Exception {
		try {
			
			File zipFile = getDocumentsDocuSign(documentDS);
			
			FileInputStream fileInputStream = new FileInputStream(zipFile);
			IOUtils.copy(fileInputStream, response.getOutputStream());
			fileInputStream.close();
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	
	private File getDocumentsDocuSign(DocumentDS documentDS) throws Exception {
		try {
			
			String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
			ApiClient apiClient = new ApiClient(urlApi);

			String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
			apiClient.setOAuthBasePath(oAuthBasePath);

			OAuthToken oAuthToken = getAuthToken(apiClient);

			String accessToken = oAuthToken.getAccessToken();
			UserInfo userInfo = apiClient.getUserInfo(accessToken);
			String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();
			
			apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);
			
			EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
			byte[] resultDocuSign = envelopesApi.getDocument(accountId, 
					documentDS.getProviderDocumentId(), Constants.CONFIG_DS_DOCUSIGN_DOWNLOAD_CERTIFICATE);
			
			String fileName = documentDS.getDocumentName() + Constants.STRING_CERTIFICATE;
			StringBuilder folderPath = buildPath(documentDS.getIdRequisition(), "Solicitudes");
			String filePath = folderPath + File.separator + fileName + Constants.PDF_FILE;
			
			Path pathPDF = Paths.get(filePath);
			Files.write(pathPDF, resultDocuSign);
			
			File certificateFile = new File(filePath);
			File signedDocument = new File(documentDS.getFilePath());
			File conservationCertificate = pscWorldBusiness.getConservationCertificate(documentDS);
			
			File[] files = { signedDocument, certificateFile, conservationCertificate };
			File tempFile = File.createTempFile(Constants.TEMP_FILE_NAME, Constants.TEMP_FILE_EXTENSION);
			
			return ApiRestUtils.createZipArchive(tempFile.getPath(), files);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	
	
	public File generateConservationCertificate(DocumentDS documentDS) throws Exception {
		try {
			
			InfoDocument infoDocument = statusDocuSignDocument(documentDS);
			if (!infoDocument.getStatusDigitalSignature().equalsIgnoreCase(DocuSignStatusEnum.COMPLETED.getValue())) {
				LOG.error("No se puede generar un certificado para un documento sin estatus COMPLETED");
				return null;
			}
			DocumentDS documentDB = documentable.findDocumentByIdRequisition(documentDS.getIdRequisition());
			
			String urlApi = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_URL_API);
			ApiClient apiClient = new ApiClient(urlApi);

			String oAuthBasePath = this.configurable.findByName(Constants.CONFIG_DS_DOCUSIGN_OAUTH_BASE);
			apiClient.setOAuthBasePath(oAuthBasePath);

			OAuthToken oAuthToken = getAuthToken(apiClient);

			String accessToken = oAuthToken.getAccessToken();
			UserInfo userInfo = apiClient.getUserInfo(accessToken);
			String accountId = userInfo.getAccounts().stream().findFirst().get().getAccountId();
			
			apiClient.addDefaultHeader(Constants.HEADER_AUTHORIZACION_KEY, Constants.TOKEN_BEARER_PREFIX + accessToken);
			
			EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
			byte[] resultDocuSign = envelopesApi.getDocument(accountId, 
					documentDB.getProviderDocumentId(), Constants.CONFIG_DS_DOCUSIGN_DOWNLOAD_COMBINED);
			
			String fileName = documentDB.getDocumentName();
			StringBuilder folderPath = buildPath(documentDB.getIdRequisition(), "Solicitudes");
			String filePath = folderPath + File.separator + fileName + Constants.PDF_FILE;
			
			Path pathPDF = Paths.get(filePath);
			Files.write(pathPDF, resultDocuSign);
			
			File pdfFile = new File(filePath);

			String fileCertificatePath = folderPath + File.separator + fileName + Constants.ASN1_FILE;

			return pscWorldBusiness.generateConservationCertificate(pdfFile, documentDB.getIdRequisition(), 
							fileCertificatePath);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}


	public StringBuilder buildPath(final Integer idObject, final String objectName) throws BusinessException {
		final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
		pathBuilder.append(objectName);
		pathBuilder.append(File.separator).append(this.userSession.getIdFlow());
		pathBuilder.append(DirUtil.obtenRutaSolicitud(idObject)).append(File.separator);
		pathBuilder.append(idObject.toString());
		this.validateDirectory(pathBuilder.toString());

		return pathBuilder;
	}


	 public String getBasePath() throws BusinessException {
	        try {
	            return this.configurable.findByName("ROOT_PATH");
	        } catch (DatabaseException databaseException) {
	            LOG.error(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	            throw new BusinessException(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	        }
	    }
	 
	 public void validateDirectory(final String path) {
	        final File requisitionPath = new File(path);
	        if (!requisitionPath.exists())
	            requisitionPath.mkdirs();
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
	 
		
		public void saveDocuSign(DocumentDS documentRequest) throws Exception {
			try {

				// creando la rutua
				documentRequest = documentable.findDocumentByIdRequisition(documentRequest.getIdRequisition());
				String rutaAbsoluta = documentRequest.getFilePath();
				Path rutaAbsolutaPath = Paths.get(rutaAbsoluta);
				String rutaSinExtension = quitarExtension(rutaAbsolutaPath);
				System.out.println(rutaSinExtension);
				String rutaArchivoSalida = rutaSinExtension + Constants.DOCUSIGN;
				File file = new File(rutaArchivoSalida);
				FileOutputStream outputStream = new FileOutputStream(file);

				File zipFile = getDocumentsDocuSign(documentRequest);

				FileInputStream fileInputStream = new FileInputStream(zipFile);
				IOUtils.copy(fileInputStream, outputStream);
				outputStream.flush();
				outputStream.close();
				fileInputStream.close();

				// guardando en la base de datos los archivos
				Requisition req = new Requisition();
				req.setDocumentDS(documentRequest);
				req.setIdRequisition(documentRequest.getIdRequisition());
				FileUploadInfo fle = new FileUploadInfo();
				File filex = new File(rutaSinExtension + Constants.DOCUSIGN);
				String fileName1 = filex.getName();
				fle.setDocumentName(fileName1);
				fle.setIdRequisition(documentRequest.getIdRequisition());
				fle.setName(fileName1);
				req.setSupplierApprovalDocument(fle);
				this.requisitionBusiness.sendToDocumentFinalDS(req);

			} catch (Exception exception) {
				exception.printStackTrace();
				throw exception;
			}
		}

}
