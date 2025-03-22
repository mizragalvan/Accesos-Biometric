package mx.engineer.utils.mail.structures;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
	private String user;
	private String password;
	
	public MailAuthenticator(final String userMail, final String passwordMail) {
		this.user = userMail;
		this.password = passwordMail;
		this.getPasswordAuthentication();
	}
	
	public final PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.user, this.password);
	}
}