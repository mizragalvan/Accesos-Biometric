package mx.pagos.util.mail.structure;

import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EmailOffice365 {

	Properties properties;
	Session session;
	MimeMessage mimeMessage;

	String USERNAME = "gcontratos@gerdau.com";
	String PASSWORD = "uDRRPaAD";
	String HOSTNAME = "smtp.office365.com";
	String STARTTLS_PORT = "587";
	boolean STARTTLS = true;
	boolean AUTH = true;
	String FromAddress = "gcontratos@gerdau.com";

	public static void main(String args[]) throws MessagingException {
		String EmailSubject = "Subject:Text Subject";
		String EmailBody = "Text Message Body: Hello World";
		String ToAddress = "munguua@gmail.com";
		EmailOffice365 office365TextMsgSend = new EmailOffice365();
		office365TextMsgSend.sendGmail(EmailSubject, EmailBody, ToAddress);
	}

	/*
	 *    props = new Properties();
		  props.put("mail.smtp.starttls.enable", "true");
		  props.put("mail.smtp.port", "587");
		  props.put("mail.smtp.host", "m.outlook.com");
		  props.put("mail.smtp.auth", "true");        
	 */

	public void sendGmail(String EmailSubject, String EmailBody, String ToAddress) {
		try {
			properties = new Properties();
			properties.put("mail.smtp.host", HOSTNAME);
			// Setting STARTTLS_PORT
			properties.put("mail.smtp.port", STARTTLS_PORT);
			// AUTH enabled
			properties.put("mail.smtp.auth", AUTH);
			// STARTTLS enabled
			properties.put("mail.smtp.starttls.enable", STARTTLS);

			// Authenticating
			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(USERNAME, PASSWORD);
				}
			};

			// creating session
			session = Session.getInstance(properties, auth);

			// create mimemessage
			mimeMessage = new MimeMessage(session);

			// from address should exist in the domain
			mimeMessage.setFrom(new InternetAddress(FromAddress));
			mimeMessage.addRecipient(RecipientType.TO, new InternetAddress(ToAddress));
			mimeMessage.setSubject(EmailSubject);

			// setting text message body
			mimeMessage.setText(EmailBody);

			// setting HTML message body
			// mimeMessage.setContent(EmailBody,"text/html; charset=utf-8");

			// sending mail
			Transport.send(mimeMessage);
			System.out.println("Mail Send Successfully");

		} catch (Exception e) {
			System.err.println("ERROR :: "+e.getMessage()+"\n"+e.getCause()+"\n");
			e.printStackTrace();
		}
	}

}
