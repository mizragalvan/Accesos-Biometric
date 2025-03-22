package mx.pagos.security.structures;

import java.io.Serializable;
import java.util.List;

import mx.pagos.admc.enums.LoginEnum;
import mx.pagos.admc.enums.security.UserStatusEnum;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8240626397017557720L;
	private static final String SPACE = " ";
	private static final String EMPTY_VALUE = "";
	private Integer idUser;
	private Integer idPosition;
	private Integer idUnderDirector;
	private List<Profile> profileList;
	private String positionName;
	private Integer idArea;
	private String areaName;
	private Integer idDga;
	private String dgaName;
	private String username;
	private String password;
	private String salt;
	private String name;
	private String firstLastName;
	private String secondLastName;
	private String phoneNumber;
	private String email;
	private String territory;
	private String underDirectorName;
	private Boolean isLawyer;
	private Boolean isEvaluator;
	private Boolean isVoboGiven;
	private Integer retriesNumber;
	private UserStatusEnum status;
	private LoginEnum statusLogin;
	private Boolean isActiveDirectoryUser = false;
	private Boolean isDecider;
	private String underDirectorPhoneNumber;
	private Integer numberPage;

    private String browser;
    
    /** total Requisitions assigned by Lawyer **/
    private Integer totalReqByLawyer;
	private Integer totalRows;
	private List<Integer> areasReporte;
	private Integer IdProfile;
	public Integer getIdProfile() {
		return IdProfile;
	}

	public void setIdProfile(Integer idProfile) {
		IdProfile = idProfile;
	}

	public User() {

	}

	public User(final Integer idUserParameter) {
		this.idUser = idUserParameter;
	}

	public User(final Integer idUserParameter, final String usernameParameter) {
		this.idUser = idUserParameter;
		this.username = usernameParameter;
	}

	public final String getTerritory() {
		return this.territory;
	}

	public final void setTerritory(final String territoryParameter) {
		this.territory = territoryParameter;
	}

	public final Integer getIdUser() {
		return this.idUser;
	}

	public final void setIdUser(final Integer idUserParameter) {
		this.idUser = idUserParameter;
	}

	public final Integer getIdPosition() {
		return this.idPosition;
	}

	public final void setIdPosition(final Integer idPositionParameter) {
		this.idPosition = idPositionParameter;
	}

	public final List<Profile> getProfileList() {
		return this.profileList;
	}

	public final void setProfileList(final List<Profile> profileListParameter) {
		this.profileList = profileListParameter;
	}

	public final String getPositionName() {
		return this.positionName;
	}

	public final void setPositionName(final String positioNameParameter) {
		this.positionName = positioNameParameter;
	}

	public final String getUsername() {
		return this.username;
	}

	public final void setUsername(final String usernameParameter) {
		this.username = usernameParameter;
	}

	public final String getPassword() {
		return this.password;
	}

	public final void setPassword(final String passwordParameter) {
		this.password = passwordParameter;
	}

	public final String getSalt() {
		return this.salt;
	}

	public final void setSalt(final String saltParameter) {
		this.salt = saltParameter;
	}

	public final String getName() {
		return this.name;
	}

	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}

	public final String getFirstLastName() {
		return this.firstLastName;
	}

	public final void setFirstLastName(final String firstLastNameParameter) {
		this.firstLastName = firstLastNameParameter;
	}

	public final String getSecondLastName() {
		return this.secondLastName;
	}

	public final void setSecondLastName(final String secondLastNameParameter) {
		this.secondLastName = secondLastNameParameter;
	}

	public final String getPhoneNumber() {
		return this.phoneNumber;
	}

	public final void setPhoneNumber(final String phoneNumberParameter) {
		this.phoneNumber = phoneNumberParameter;
	}

	public final String getEmail() {
		return this.email;
	}

	public final void setEmail(final String emailParameter) {
		this.email = emailParameter;
	}

	public final UserStatusEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final UserStatusEnum statusParameter) {
		this.status = statusParameter;
	}

	public final Integer getIdArea() {
		return this.idArea;
	}

	public final void setIdArea(final Integer idAreaParameter) {
		this.idArea = idAreaParameter;
	}

	public final Integer getIdDga() {
		return this.idDga;
	}

	public final void setIdDga(final Integer idDgaParameter) {
		this.idDga = idDgaParameter;
	}

	public final String getAreaName() {
		return this.areaName;
	}

	public final void setAreaName(final String areaNameParameter) {
		this.areaName = areaNameParameter;
	}

	public final String getDgaName() {
		return this.dgaName;
	}

	public final void setDgaName(final String dgaNameParameter) {
		this.dgaName = dgaNameParameter;
	}

	public final String getUnderDirectorName() {
		return this.underDirectorName;
	}

	public final void setUnderDirectorName(final String underDirectorNameParameter) {
		this.underDirectorName = underDirectorNameParameter;
	}

	public final Integer getIdUnderdirector() {
		return this.idUnderDirector;
	}

	public final void setIdUnderdirector(final Integer idUnderDirectorParameter) {
		this.idUnderDirector = idUnderDirectorParameter;
	}

	public final String getFullName() {
		if (this.name != null && this.firstLastName != null)
			return this.name.concat(SPACE).concat(this.firstLastName).concat(SPACE).concat(
					(this.secondLastName != null && !this.secondLastName.isEmpty()) ? this.secondLastName : EMPTY_VALUE);
		return EMPTY_VALUE;
	}

	public final Boolean getIsLawyer() {
		return this.isLawyer;
	}

	public final void setIsLawyer(final Boolean isLawyerParameter) {
		this.isLawyer = isLawyerParameter;
	}

	public final Boolean getIsEvaluator() {
		return this.isEvaluator;
	}

	public final void setIsEvaluator(final Boolean isEvaluatorParameter) {
		this.isEvaluator = isEvaluatorParameter;
	}

	public final Integer getRetriesNumber() {
		return this.retriesNumber;
	}

	public final void setRetriesNumber(final Integer retriesNumberParameter) {
		this.retriesNumber = retriesNumberParameter;
	}

	public final Boolean getIsVoboGiven() {
		return this.isVoboGiven;
	}

	public final void setIsVoboGiven(final Boolean isVoboGivenParameter) {
		this.isVoboGiven = isVoboGivenParameter;
	}

	public final LoginEnum getStatusLogin() {
		return this.statusLogin;
	}

	public final void setStatusLogin(final LoginEnum statusLoginParameter) {
		this.statusLogin = statusLoginParameter;
	}

	public final Boolean getIsActiveDirectoryUser() {
		return this.isActiveDirectoryUser;
	}

	public final void setIsActiveDirectoryUser(final Boolean isActiveDirectoryUserParameter) {
		this.isActiveDirectoryUser = isActiveDirectoryUserParameter;
	}

    public final Boolean getIsDecider() {
        return this.isDecider;
    }

    public final void setIsDecider(final Boolean isDeciderParameter) {
        this.isDecider = isDeciderParameter;
    }

    public final String getUnderDirectorPhoneNumber() {
        return this.underDirectorPhoneNumber;
    }

    public final void setUnderDirectorPhoneNumber(final String underDirectorPhoneNumberParameter) {
        this.underDirectorPhoneNumber = underDirectorPhoneNumberParameter;
    }
    
    public final Integer getNumberPage() {
        return this.numberPage;
    }

    public final void setNumberPage(final Integer numberPageParameter) {
        this.numberPage = numberPageParameter;
    }

    public final Integer getTotalRows() {
        return this.totalRows;
    }

    public final void setTotalRows(final Integer totalRowsParameter) {
        this.totalRows = totalRowsParameter;
    }

    /**
     * @return the totalReqByLawyer
     */
    public final Integer getTotalReqByLawyer() {
        return totalReqByLawyer;
    }

    /**
     * @param totalReqByLawyer the totalReqByLawyer to set
     */
    public final void setTotalReqByLawyer(Integer totalReqByLawyer) {
        this.totalReqByLawyer = totalReqByLawyer;
    }

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}    

	/**
	 * @return the areasReporte
	 */
	public List<Integer> getAreasReporte() {
		return areasReporte;
	}

	/**
	 * @param areasReporte the areasReporte to set
	 */
	public void setAreasReporte(List<Integer> areasReporte) {
		this.areasReporte = areasReporte;
	}
}