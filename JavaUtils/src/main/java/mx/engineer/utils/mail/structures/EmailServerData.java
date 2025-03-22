package mx.engineer.utils.mail.structures;

public class EmailServerData {
    private String host;
    private String emailUser;
    private String userEmail;
    private String emailPassword;
    private String port;

    public final String getHost() {
        return this.host;
    }

    public final void setHost(final String hostParameter) {
        this.host = hostParameter;
    }

    public final String getEmailUser() {
        return this.emailUser;
    }

    public final void setEmailUser(final String emailUserParameter) {
        this.emailUser = emailUserParameter;
    }

    public final String getUserEmail() {
        return this.userEmail;
    }

    public final void setUserEmail(final String userEmailParameter) {
        this.userEmail = userEmailParameter;
    }

    public final String getEmailPassword() {
        return this.emailPassword;
    }

    public final void setEmailPassword(final String emailPasswordParameter) {
        this.emailPassword = emailPasswordParameter;
    }

    public final String getPort() {
        return this.port;
    }

    public final void setPort(final String portParameter) {
        this.port = portParameter;
    }
}
