package mx.pagos.admc.contracts.structures.digitalsignature;

public class UserInformation extends BaseDS{

	private Integer idUser;
	private String fullName;
	private String email;
	private String rfc;
	
	public UserInformation() {
		super();
	}

	public UserInformation(String responseCode, String responseMessage) {
		super(responseCode, responseMessage);
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	
	
}
