package mx.pagos.admc.core.business;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.mail.MailUtils;
import mx.engineer.utils.mail.structures.EmailContent;
import mx.engineer.utils.mail.structures.EmailServerData;
import mx.pagos.admc.contracts.structures.RequisitionAngular;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.general.exceptions.DatabaseException;

/**
 * @author Mizraim
 * Clase para el envío de emails de notificación.
 */
@Service
public class EmailsBusiness {
	private static final Logger LOG = Logger.getLogger(EmailsBusiness.class);

	@Autowired
	private Configurable configurable;

	public final void sendEmail(final EmailContent emailContent, final String[] toAddresses) {
		try {

			LinkedHashSet<String> linkedHashSet = new LinkedHashSet<> ( Arrays.asList(toAddresses) );
			String[] toAdrees2 = linkedHashSet.toArray(new String [] {});

			LOG.info("\n============================================== \nINCIA EL ENVÍO DEL CORREO");
			final MailUtils mailUtils = new MailUtils(this.getEmailServerAndSecurityData());
			mailUtils.setSubject(emailContent.getSubject());
			mailUtils.setMessageBodyHtmlFormat(this.setEmailContent(emailContent, "EmailTemplate.html"));
			mailUtils.setToAddresses(toAdrees2);
			mailUtils.setIsAuthentication(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTHENTICATION")));
			mailUtils.setIsSsl(Boolean.parseBoolean(this.configurable.findByName("SMTP_SSL")));
			mailUtils.setIsTls(Boolean.parseBoolean(this.configurable.findByName("SMTP_TLS")));
			mailUtils.setSslTrust(this.configurable.findByName("SMTP_SSL_TRUST"));
			mailUtils.setIsAuthPlain(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTH_PLAIN")));
			mailUtils.sendEmail();
			LOG.info("REMITENTES ::");
			for (int i = 0; i < toAdrees2.length; i++)
				LOG.info("> "+toAdrees2[i]);
			LOG.info("CORREOS ENVIADOS SATISFACTORIAMENTE!! \n============================================== \n");
		} catch (DatabaseException databaseException) {
			LOG.error("*****************************************************\nError al obtener las configuraciones para envío de correo. Exception:"
					+ databaseException.getMessage(), databaseException);
		} catch (MessagingException | IOException messagingException) {
			LOG.error("*****************************************************\nERROR AL ENVIAR EL CORREO.\n Exception: " + messagingException.getMessage(), messagingException);
		} catch (Exception exception) {
			LOG.error("*****************************************************\nError desconocido al enviar el correo. Exception: " + exception.getMessage(), exception);
		}
	}
	
	public final void sendEmailAlert(String email, StringBuilder idsStringBuilder, String bandeja) {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject("Notificación contratos fuera de tiempo");
		emailContent.setFieldOne("FOLIO(S): " + idsStringBuilder.toString());
		emailContent.setFieldTwo("Atentamente:");
		emailContent.setFieldTwoDescription("Área Legal & Compliance.");
		emailContent.setSendDateDescription("FECHA:");
		emailContent.setSendDate(LocalDate.now());
		emailContent.setContent("Estimado usuario, usted tiene uno o varios contratos que han excedido el tiempo de "
				+ "respuesta y resolución en bandeja de: «" + bandeja + "», el plazo para concluir con el proceso es de "
						+ " 5 días hábiles a partir de que le fue entregado. Solicitamos devolver un tanto del contrato firmado a la brevedad posible.");
		emailContent.setBrand("EMAIL_NOTIFICATION_BRAND");
		try {
			LOG.debug("Inicia el envío de correo");
			final MailUtils mailUtils = new MailUtils(this.getEmailServerAndSecurityData());
			mailUtils.setSubject(emailContent.getSubject());
			mailUtils.setMessageBodyHtmlFormat(this.setEmailContent(emailContent, "EmailTemplate_FueraDeTiempo.html"));
			mailUtils.setToAddresses(new String[] {email});
			mailUtils.setIsAuthentication(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTHENTICATION")));
			mailUtils.setIsSsl(Boolean.parseBoolean(this.configurable.findByName("SMTP_SSL")));
			mailUtils.setIsTls(Boolean.parseBoolean(this.configurable.findByName("SMTP_TLS")));
			mailUtils.setSslTrust(this.configurable.findByName("SMTP_SSL_TRUST"));
			mailUtils.setIsAuthPlain(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTH_PLAIN")));
			mailUtils.sendEmail();
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener las configuraciones para envío de correo. Exception:"
					+ databaseException.getMessage(), databaseException);
		} catch (MessagingException | IOException messagingException) {
			LOG.error("Error al enviar el correo. Exception: " + messagingException.getMessage(), messagingException);
		} catch (Exception exception) {
			LOG.error("Error desconocido al enviar el correo. Exception: " + exception.getMessage(), exception);
		}
	}
	
	public final void sendMailAssignment(final ConsultaList req, final String toAddresses) {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject("Notificación asignación de contrato");
		emailContent.setFieldOne("Atentamente:");
		emailContent.setFieldOneDescription("Área Legal & Compliance.");
		emailContent.setSendDateDescription("FECHA:");
		emailContent.setSendDate(LocalDate.now());
		emailContent.setContent("Estimado Usuario, su solicitud con número de folio: " + req.getParam5()  + ", "
				+ "ha sido asignada al abogado(a): «" + req.getParam1() + 
				"» para revisión y en 4 días hábiles recibirá respuesta conforme a la política de contratos vigente, "
				+ "esperamos que su solicitud de contrato contenga toda la información requerida para poder dar respuesta del primer draft de contrato "
				+ "en el tiempo establecido.");
		try {
			LOG.debug("Inicia el envío de correo");
			final MailUtils mailUtils = new MailUtils(this.getEmailServerAndSecurityData());
			mailUtils.setSubject(emailContent.getSubject());
			mailUtils.setMessageBodyHtmlFormat(this.setEmailContent(emailContent, "EmailTemplate_AsignacionYRevision.html"));
			mailUtils.setToAddresses(new String[] {toAddresses});
			mailUtils.setIsAuthentication(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTHENTICATION")));
			mailUtils.setIsSsl(Boolean.parseBoolean(this.configurable.findByName("SMTP_SSL")));
			mailUtils.setIsTls(Boolean.parseBoolean(this.configurable.findByName("SMTP_TLS")));
			mailUtils.setSslTrust(this.configurable.findByName("SMTP_SSL_TRUST"));
			mailUtils.setIsAuthPlain(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTH_PLAIN")));
			mailUtils.sendEmail();
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener las configuraciones para envío de correo. Exception:"
					+ databaseException.getMessage(), databaseException);
		} catch (MessagingException | IOException messagingException) {
			LOG.error("Error al enviar el correo. Exception: " + messagingException.getMessage(), messagingException);
		} catch (Exception exception) {
			LOG.error("Error desconocido al enviar el correo. Exception: " + exception.getMessage(), exception);
		}
	}

	public final void sendEmailPassword(final EmailContent emailContent, final String[] toAddresses) {
		try {
			LOG.debug("Inicia el envío de correo");
			final MailUtils mailUtils = new MailUtils(this.getEmailServerAndSecurityData());
			mailUtils.setSubject(emailContent.getSubject());
			mailUtils.setMessageBodyHtmlFormat(this.setEmailContent(emailContent, "EmailTemplate_recuperaPass.html"));
			mailUtils.setToAddresses(toAddresses);
			mailUtils.setIsAuthentication(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTHENTICATION")));
			mailUtils.setIsSsl(Boolean.parseBoolean(this.configurable.findByName("SMTP_SSL")));
			mailUtils.setIsTls(Boolean.parseBoolean(this.configurable.findByName("SMTP_TLS")));
			mailUtils.setSslTrust(this.configurable.findByName("SMTP_SSL_TRUST"));
			mailUtils.setIsAuthPlain(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTH_PLAIN")));
			mailUtils.sendEmail();
			for (int i = 0; i < toAddresses.length; i++)
				LOG.info(toAddresses[i]);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener las configuraciones para envío de correo. Exception:"
					+ databaseException.getMessage(), databaseException);
		} catch (MessagingException | IOException messagingException) {
			LOG.error("Error al enviar el correo. Exception: " + messagingException.getMessage(), messagingException);
		} catch (Exception exception) {
			LOG.error("Error desconocido al enviar el correo. Exception: " + exception.getMessage(), exception);
		}
	}

	public final void sendEmailNotifyUser(final RequisitionAngular req, final String toAddresses) {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject("Notificación envío de contrato revisado");
		emailContent.setFieldOne("Atentamente:");
		emailContent.setFieldOneDescription("Área Legal & Compliance.");
		emailContent.setSendDateDescription("FECHA:");
		emailContent.setSendDate(LocalDate.now());
		emailContent.setContent("Estimado usuario, su solicitud con número de folio: " + req.getIdRequisition() + ","
				+ " ha sido revisada y enviada por el abogado(a): «" + req.getNameLawyer() +"». De acuerdo con lo "
						+ "previsto en la política de contratos vigente, cuenta con un plazo 5 días hábiles "
						+ "para enviar comentarios del contrato.");
		try {
			LOG.debug("Inicia el envío de correo");
			final MailUtils mailUtils = new MailUtils(this.getEmailServerAndSecurityData());
			mailUtils.setSubject(emailContent.getSubject());
			mailUtils.setMessageBodyHtmlFormat(this.setEmailContent(emailContent, "EmailTemplate_AsignacionYRevision.html"));
			mailUtils.setToAddresses(new String[] {toAddresses});
			mailUtils.setIsAuthentication(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTHENTICATION")));
			mailUtils.setIsSsl(Boolean.parseBoolean(this.configurable.findByName("SMTP_SSL")));
			mailUtils.setIsTls(Boolean.parseBoolean(this.configurable.findByName("SMTP_TLS")));
			mailUtils.setSslTrust(this.configurable.findByName("SMTP_SSL_TRUST"));
			mailUtils.setIsAuthPlain(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTH_PLAIN")));
			mailUtils.sendEmail();
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener las configuraciones para envío de correo. Exception:"
					+ databaseException.getMessage(), databaseException);
		} catch (MessagingException | IOException messagingException) {
			LOG.error("Error al enviar el correo. Exception: " + messagingException.getMessage(), messagingException);
		} catch (Exception exception) {
			LOG.error("Error desconocido al enviar el correo. Exception: " + exception.getMessage(), exception);
		}
	}
	

	public final void sendEmailNotify(final EmailContent emailContent, final String toAddresses) {
		try {
			LOG.debug("Inicia el envío de correo");
			final MailUtils mailUtils = new MailUtils(this.getEmailServerAndSecurityData());
			mailUtils.setSubject(emailContent.getSubject());
			mailUtils.setMessageBodyHtmlFormat(this.setEmailContent(emailContent, "EmailTemplate_AlertaCorreo.html"));
			mailUtils.setToAddresses(new String[] {toAddresses});
			mailUtils.setIsAuthentication(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTHENTICATION")));
			mailUtils.setIsSsl(Boolean.parseBoolean(this.configurable.findByName("SMTP_SSL")));
			mailUtils.setIsTls(Boolean.parseBoolean(this.configurable.findByName("SMTP_TLS")));
			mailUtils.setSslTrust(this.configurable.findByName("SMTP_SSL_TRUST"));
			mailUtils.setIsAuthPlain(Boolean.parseBoolean(this.configurable.findByName("SMTP_AUTH_PLAIN")));
			mailUtils.sendEmail();
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener las configuraciones para envío de correo. Exception:"
					+ databaseException.getMessage(), databaseException);
		} catch (MessagingException | IOException messagingException) {
			LOG.error("Error al enviar el correo. Exception: " + messagingException.getMessage(), messagingException);
		} catch (Exception exception) {
			LOG.error("Error desconocido al enviar el correo. Exception: " + exception.getMessage(), exception);
		}
	}

	private EmailServerData getEmailServerAndSecurityData() throws DatabaseException {
		final EmailServerData emailServerData = new EmailServerData();
		emailServerData.setHost(this.configurable.findByName("SMTP_HOST"));
		emailServerData.setPort(this.configurable.findByName("SMTP_PORT"));
		emailServerData.setEmailUser(this.configurable.findByName("SMTP_EMAIL"));
		emailServerData.setUserEmail(this.configurable.findByName("SMTP_USER"));
		emailServerData.setEmailPassword(this.configurable.findByName("SMTP_PASSWORD"));
		return emailServerData;
	}

	private String setEmailContent(final EmailContent emailContent, String template) throws DatabaseException, FileNotFoundException,
	IOException {
		String emailTemplateContent = IOUtils.toString(new FileInputStream(this.configurable.
				findByName(ConfigurationEnum.EMAIL_TEMPLATES_PATH.toString()) + template), "utf-8");
		emailTemplateContent = emailTemplateContent.replace("[*Asunto*]", emailContent.getSubject());
		emailTemplateContent = emailTemplateContent.replace("[*DatoUno*]", emailContent.getFieldOne());
		emailTemplateContent = emailTemplateContent.replace("[*DatoUnoDescripcion*]", emailContent.
				getFieldOneDescription());
		emailTemplateContent = emailTemplateContent.replace("[*DatoDos*]", emailContent.getFieldTwo());
		emailTemplateContent = emailTemplateContent.replace("[*DatoDosDescripcion*]", emailContent.
				getFieldTwoDescription());
		emailTemplateContent = emailTemplateContent.replace("[*DatoTres*]", emailContent.getFieldThree());
		emailTemplateContent = emailTemplateContent.replace("[*DatoTresDescripcion*]", emailContent.
				getFieldThreeDescription());
		emailTemplateContent = emailTemplateContent.replace("[*DatoCuatro*]", emailContent.getFieldFour());
		emailTemplateContent = emailTemplateContent.replace("[*DatoCuatroDescripcion*]", emailContent.
				getFieldFourDescription());
		emailTemplateContent = emailTemplateContent.replace("[*fechaEmailDesc*]", emailContent.getSendDateDescription());
		emailTemplateContent = emailTemplateContent.replace("[*fechaEmail*]", (emailContent.getSendDate()==null ? "" : emailContent.getSendDate().toString()));
		emailTemplateContent = emailTemplateContent.replace("[*Contenido*]", emailContent.getContent());
		emailTemplateContent = emailTemplateContent.replace("[*MarcaPieCorreo*]", this.configurable.
				findByName("EMAIL_NOTIFICATION_BRAND"));

		return emailTemplateContent;
	}
}
