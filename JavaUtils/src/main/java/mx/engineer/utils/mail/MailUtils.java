package mx.engineer.utils.mail;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import mx.engineer.utils.mail.structures.EmailServerData;
import mx.engineer.utils.mail.structures.MailAuthenticator;

public class MailUtils {
	private String emailUser;
	private String onlyUserEmail;
	private String emailPassword;
	private String host;
	private String port;
	private Boolean isTls;
	private Boolean isAuthentication;
	private Boolean isSsl;
	private String sslTrust;
	private Boolean isAuthPlain;
	private MimeMessage message = new MimeMessage(this.session);
	private Session session;
	private BodyPart bodyMessage = new MimeBodyPart();
	private MimeMultipart multipart = new MimeMultipart();
	private Transport transport;

	public MailUtils(final EmailServerData emailServerData) {

		if("".equals(emailServerData.getUserEmail().trim())) {
			this.onlyUserEmail = emailServerData.getEmailUser();
		} else {
			this.onlyUserEmail = emailServerData.getUserEmail();
		}
		this.emailUser = emailServerData.getEmailUser();
		this.emailPassword = emailServerData.getEmailPassword();
		this.host = emailServerData.getHost();
		this.port = emailServerData.getPort();
	}

	public final void sendEmail() throws MessagingException {
		this.session = this.getConnectionSession();
		this.transport = this.session.getTransport();
		this.transport.connect(this.onlyUserEmail, this.emailPassword);
		this.message.setContent(this.multipart);
		this.message.saveChanges();
		this.transport.sendMessage(this.message, this.message.getAllRecipients());
	}

	private Session getConnectionSession() {	
		final Properties properties = this.getMailProperties();
		final MailAuthenticator auth = new MailAuthenticator(this.onlyUserEmail, this.emailPassword);
		return Session.getInstance(properties, auth);
	}

	private Properties getMailProperties() {
		final Properties properties = new Properties();

		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.starttls.enable", this.isTls.toString());
		properties.put("mail.smtp.host", this.host);
		properties.put("mail.smtp.user", this.onlyUserEmail);
		properties.put("mail.smtp.from", this.emailUser);
		properties.put("smtp.from", this.emailUser);
		properties.put("mail.smtp.port", this.port);
		properties.put("mail.smtp.auth", this.isAuthentication.toString());
		properties.put("mail.smtp.ssl.enable", this.isSsl.toString());
		properties.put("mail.smtp.socketFactory.port", "");
		properties.put("mail.smtp.auth.plain.disable", this.isAuthPlain.toString());

		return properties;
	}

	public final void setToAddresses(final String[] addressesArray) throws MessagingException {
		final InternetAddress from = new InternetAddress(this.emailUser);
		this.message.setFrom(from);
		this.message.addRecipients(Message.RecipientType.TO, this.getReceipientsAddress(addressesArray));
		this.message.setReplyTo(new Address[] { new InternetAddress(this.emailUser) });
	}

	private InternetAddress[] getReceipientsAddress(final String[] address) throws AddressException {
		final InternetAddress[] recipients = new InternetAddress[address.length];
		for (int i = 0; i < address.length; i++)
			recipients[i] = new InternetAddress(address[i]);
		return recipients;
	}

	public final void setSubject(final String subject) throws MessagingException {
		this.message.setSubject(subject);
	}

	public final void setMessageBodyTextFormat(final String textMessage) throws MessagingException {
		this.bodyMessage.setText(textMessage);
		this.multipart.addBodyPart(this.bodyMessage);
	}

	public final void setMessageBodyHtmlFormat(final String htmlMessage) throws MessagingException {		
		this.bodyMessage.setContent(htmlMessage, "text/html; charset=UTF-8");

		this.multipart.addBodyPart(bodyMessage);
		this.bodyMessage = new MimeBodyPart();
		DataSource fds = new FileDataSource("C:\\Contratos\\EmailTemplates\\LogoContratos.png");
		this.bodyMessage.setDataHandler(new DataHandler(fds));
		this.bodyMessage.setHeader("Content-ID", "<image>");

		this.multipart.addBodyPart(this.bodyMessage);
	}

	public final void setAttachments(final String[] records) throws MessagingException {
		final List<BodyPart> listAttachments = new LinkedList<BodyPart>();
		this.processAttachments(records, listAttachments);
		this.addAttachFile(listAttachments);
	}

	private void processAttachments(final String[] attachmentsObject, final List<BodyPart> listAttachments) 
			throws MessagingException {
		for (int i = 0; i <= attachmentsObject.length - 1; i++) { 
			final BodyPart attachedProperties = new MimeBodyPart(); 
			final File file = new File(attachmentsObject[i]);
			final DataSource source = new FileDataSource(attachmentsObject[i]);
			attachedProperties.setDataHandler(new DataHandler(source));
			attachedProperties.setFileName(file.getName());
			listAttachments.add(attachedProperties);
		}	
	}

	private void addAttachFile(final List<BodyPart> listAttachments) throws MessagingException {
		final Iterator<BodyPart> it = listAttachments.iterator();
		while (it.hasNext()) {
			final BodyPart attach = (BodyPart) it.next();
			this.multipart.addBodyPart(attach);
		}
	}

	public final MimeMessage getMessage() {
		return this.message;
	}

	public final MimeMultipart getMultipart() {
		return this.multipart;
	}

	public final Session getSession() {
		return this.session;
	}

	public final Transport getTransport() {
		return this.transport;
	}

	public final void setIsTls(final Boolean isTlsParameter) {
		this.isTls = isTlsParameter;
	}

	public final void setIsAuthentication(final Boolean isAuthenticationParameter) {
		this.isAuthentication = isAuthenticationParameter;
	}

	public final void setIsSsl(final Boolean isSslParameter) {
		this.isSsl = isSslParameter;
	}

	public final String getSslTrust() {
		return this.sslTrust;
	}

	public final void setSslTrust(final String sslTrustParameter) {
		this.sslTrust = sslTrustParameter;
	}

	/**
	 * @return the isAuthPlain
	 */
	public final Boolean getIsAuthPlain() {
		return isAuthPlain;
	}

	/**
	 * @param isAuthPlain the isAuthPlain to set
	 */
	public final void setIsAuthPlain(Boolean isAuthPlain) {
		this.isAuthPlain = isAuthPlain;
	}
}
