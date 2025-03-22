package mx.pagos.admc.contracts.structures;

public class LdapConnectionParameters {

	private String url;
	private String base;
	private String userDn;
	private String password;
	
	public final String getUrl() {
		return this.url;
	}
	
	public final void setUrl(final String urlParameter) {
		this.url = urlParameter;
	}
	
	public final String getBase() {
		return this.base;
	}
	
	public final void setBase(final String baseParameter) {
		this.base = baseParameter;
	}
	
	public final String getUserDn() {
		return this.userDn;
	}
	
	public final void setUserDn(final String userDnParameter) {
		this.userDn = userDnParameter;
	}
	
	public final String getPassword() {
		return this.password;
	}
	
	public final void setPassword(final String passwordParameter) {
		this.password = passwordParameter;
	}
}
