package mx.pagos.util.mail.structure;

public class SmtpServerConfigurations {
    private String mailDirectory;
    private Boolean transportLayerSecurity;
    private Boolean authentication;
    private String protocol;
    private String host;
    private String port;
    private String user;
    private String password;

    public final String getMailDirectory() {
        return this.mailDirectory;
    }
    
    public final void setMailDirectory(final String mailDirectoryParameter) {
        this.mailDirectory = mailDirectoryParameter;
    }

    public final Boolean getTransportLayerSecurity() {
        return this.transportLayerSecurity;
    }

    public final void setTransportLayerSecurity(final Boolean transportLayerSecurityParameter) {
        this.transportLayerSecurity = transportLayerSecurityParameter;
    }

    public final Boolean getAuthentication() {
        return this.authentication;
    }

    public final void setAuthentication(final Boolean authenticationParameter) {
        this.authentication = authenticationParameter;
    }
    
    public final String getProtocol() {
        return this.protocol;
    }

    public final void setProtocol(final String protocolParameter) {
        this.protocol = protocolParameter;
    }
    
    public final String getHost() {
        return this.host;
    }
    
    public final void setHost(final String hostParameter) {
        this.host = hostParameter;
    }
    
    public final String getPort() {
        return this.port;
    }
    
    public final void setPort(final String portParameter) {
        this.port = portParameter;
    }
    
    public final String getUser() {
        return this.user;
    }
    
    public final void setUser(final String userParameter) {
        this.user = userParameter;
    }
    
    public final String getPassword() {
        return this.password;
    }
    
    public final void setPassword(final String passwordParameter) {
        this.password = passwordParameter;
    }
}
