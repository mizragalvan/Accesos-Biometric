package mx.solsersistem.utils.test.mail;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import mx.solsersistem.utils.mail.MailUtils;
import mx.solsersistem.utils.mail.structures.EmailServerData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MailUtilsTest {
	private static final EmailServerData CONNECTIONDATARECEIVED = new EmailServerData();
	private static final String HOST = "smtpout.secureserver.net";
	private static final String USER = "junitsmail@solsersistem.net";
	private static final String PASSWORD = "JSolser2015";
	private static final String PORT = "465";
	private MailUtils mailsUtils = new MailUtils(CONNECTIONDATARECEIVED);
	private String subject = "Pueba de envio de correos con JUNIT";
    private String[] address = new String[2];
    private MimeMessage message;
	private String messageText = "Desarrollo de funcion/clase para envio de correos.";
	private String messageHtml = "<H1>Desarrollo de funcion/clase para envio de correos</H1><br>"
					+ "<b>Desarrollo de funcion/clase para envio de correos</b><br>"
					+ "<a>Desarrollo de funcion/clase para envio de correos</a>";
	private String[] attachments = new String[2];

	@BeforeClass
	public static void setupClass() {
		configureConnection();
	}
	
	private static void configureConnection() {
		CONNECTIONDATARECEIVED.setHost(HOST);
		CONNECTIONDATARECEIVED.setEmailUser(USER);
		CONNECTIONDATARECEIVED.setEmailPassword(PASSWORD);
		CONNECTIONDATARECEIVED.setPort(PORT);
	}
	
	@Before
	public final void setup() throws MessagingException {
		this.setInitialConfigurations();
	}

	private void setInitialConfigurations() throws MessagingException {
		this.address[0] = "testuserjunits2@gmail.com";
	    this.address[1] = "testuserjunits3@gmail.com";
	    this.attachments[0] = "TestFiles/adjunto_img.png";
	    this.attachments[1] = "TestFiles/adjunto_pdf.pdf";
	    this.mailsUtils.setToAddresses(this.address);
		this.mailsUtils.setSubject(this.subject);
	}

	@Test
	public final void whenSetAddressThenGetRecipients() throws MessagingException {
		this.message = (MimeMessage) this.mailsUtils.getMessage();
        Assert.assertEquals("El mensaje deberia tener el mismo remitente", USER, this.message.getFrom()[0].toString());
        Assert.assertNotNull("El mensaje deberia tener los destinatarios", 
        		this.message.getRecipients(MimeMessage.RecipientType.TO));
        Assert.assertNotNull("El mensaje no deberia aparecer vacio", this.mailsUtils.getMessage());
	}
	
	@Test
	public final void whenSetSubjectThenGetMessageSubject() throws MessagingException {		
		this.message = (MimeMessage) this.mailsUtils.getMessage();
        Assert.assertEquals("Asunto deberia ser el mismo", this.subject, this.message.getSubject());
        Assert.assertNotNull("Asunto del mensaje vacio", this.mailsUtils.getMessage());
	}
	
	@Test
	public final void whenSetMessageTextThenGetMessageBody() throws MessagingException, IOException {
		this.mailsUtils.setMessageBodyTextFormat(this.messageText);
		Assert.assertNotNull("El texto del contenido no deberia estar vacio", this.mailsUtils.getMultipart());
		Assert.assertEquals("El body Text debio corresponder al esperado", this.messageText,
				(String) this.mailsUtils.getMultipart().getBodyPart(0).getContent());
	}
	
	@Test
	public final void whenSetMessageHtmlThenGetMessageBody() throws MessagingException, IOException {
		this.mailsUtils.setMessageBodyHtmlFormat(this.messageHtml);
		Assert.assertNotNull("El html del contenido no deberia estar vacio", this.mailsUtils.getMultipart());
		Assert.assertEquals("El body Html debio corresponder al esperado", this.messageHtml,
				(String) this.mailsUtils.getMultipart().getBodyPart(0).getContent());
	}
	
	@Test
	public final void whenSetFilesThenGetAttachments() throws MessagingException, IOException {	    
	    this.mailsUtils.setAttachments(this.attachments);
	    Assert.assertEquals("No coincide el numero de adjuntos agregados", 
	    		this.mailsUtils.getMultipart().getCount(), this.attachments.length);
		Assert.assertNotEquals("Ningun adjunto agregado", this.mailsUtils.getMultipart().getCount(), 0);
	}

	/*@Test
	public final void whenGetMessageTextWithAttachnetsThenSendMail() throws MessagingException, IOException {
		this.mailsUtils.setAttachments(this.attachments);
		this.mailsUtils.setMessageBodyTextFormat(this.messageText);
	    this.mailsUtils.sendEmail();
		Assert.assertNotNull("No se obtuvo las propiedades de session para envio de texto/adjuntos", 
				this.mailsUtils.getSession());
		Assert.assertTrue("Error en la conexion para el envio de texto/adjuntos", 
				this.mailsUtils.getTransport().isConnected());
		Assert.assertNotNull("No se realizo el envio de texto/adjuntos", this.mailsUtils.getTransport());
	}

	@Test
	public final void whenGetMessageHtmlWithAttachnetsThenSendMail() throws MessagingException {
		this.mailsUtils.setAttachments(this.attachments);
		this.mailsUtils.setMessageBodyHtmlFormat(this.messageHtml);
		this.mailsUtils.sendEmail();
        Assert.assertNotNull("No se obtuvo las propiedades de sesion para envio de Html/Adjuntos", 
        		this.mailsUtils.getSession());
        Assert.assertTrue("Error en la conexion para el envio de Html/Adjuntos", 
        		this.mailsUtils.getTransport().isConnected());
		Assert.assertNotNull("No se realizo el envio de Html/Adjuntos", this.mailsUtils.getTransport());
	}*/
}
