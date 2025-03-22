package mx.pagos.admc.contracts.structures;

public class InitialSystemConfiguration {

	private String urlLdap;
	private String baseLdap;
	private String userLdap;
	private String passwordLdap;
	private String smtpAuthentication;
	private String smtpEmail;
	private String smtpHost;
	private String smtpPassword;
	private String smtpPort;
	private String smtpProtocol;
	private String smtpSsl;
	private String smtpTls;
	private String administratorUserName;
	private String securityAdministratorUserName;
	
	public final String getUrlLdap() {
		return this.urlLdap;
	}
	
	public final void setUrlLdap(final String urlLdapParameter) {
		this.urlLdap = urlLdapParameter;
	}
	
	public final String getBaseLdap() {
		return this.baseLdap;
	}
	
	public final void setBaseLdap(final String baseLdapParameter) {
		this.baseLdap = baseLdapParameter;
	}
	
	public final String getUserLdap() {
		return this.userLdap;
	}
	
	public final void setUserLdap(final String userLdapParameter) {
		this.userLdap = userLdapParameter;
	}
	
	public final String getPasswordLdap() {
		return this.passwordLdap;
	}
	
	public final void setPasswordLdap(final String passwordLdapParameter) {
		this.passwordLdap = passwordLdapParameter;
	}
	
	public final String getSmtpAuthentication() {
		return this.smtpAuthentication;
	}
	
	public final void setSmtpAuthentication(final String smtpAuthenticationParameter) {
		this.smtpAuthentication = smtpAuthenticationParameter;
	}
	
	public final String getSmtpEmail() {
		return this.smtpEmail;
	}
	
	public final void setSmtpEmail(final String smtpEmailParameter) {
		this.smtpEmail = smtpEmailParameter;
	}
	
	public final String getSmtpHost() {
		return this.smtpHost;
	}
	
	public final void setSmtpHost(final String smtpHostParameter) {
		this.smtpHost = smtpHostParameter;
	}
	
	public final String getSmtpPassword() {
		return this.smtpPassword;
	}
	
	public final void setSmtpPassword(final String smtpPasswordParameter) {
		this.smtpPassword = smtpPasswordParameter;
	}
	
	public final String getSmtpPort() {
		return this.smtpPort;
	}
	
	public final void setSmtpPort(final String smtpPortParameter) {
		this.smtpPort = smtpPortParameter;
	}
	
	public final String getSmtpProtocol() {
		return this.smtpProtocol;
	}
	
	public final void setSmtpProtocol(final String smtpProtocolParameter) {
		this.smtpProtocol = smtpProtocolParameter;
	}
	
	public final String getSmtpSsl() {
		return this.smtpSsl;
	}
	
	public final void setSmtpSsl(final String smtpSslParameter) {
		this.smtpSsl = smtpSslParameter;
	}
	
	public final String getSmtpTls() {
		return this.smtpTls;
	}
	
	public final void setSmtpTls(final String smtpTlsParameter) {
		this.smtpTls = smtpTlsParameter;
	}
	
	public final String getAdministratorUserName() {
		return this.administratorUserName;
	}
	
	public final void setAdministratorUserName(final String administratorUserNameParameter) {
		this.administratorUserName = administratorUserNameParameter;
	}

    public final String getSecurityAdministratorUserName() {
        return this.securityAdministratorUserName;
    }

    public final void setSecurityAdministratorUserName(final String securityAdministratorUserNameParameter) {
        this.securityAdministratorUserName = securityAdministratorUserNameParameter;
    }
}
