package mx.pagos.security.structures;

public class ProfileScreenFlow {
	private Integer idProfile;
	private String factoryName;
	private String factoryNameTray;
	private String flowStatus;
	private Integer idFlow;
	private Boolean isUserFiltered;
	
	public ProfileScreenFlow() {
		// TODO Auto-generated constructor stub
	}
	
	public ProfileScreenFlow(final Integer idProfileParameter) {
	    this.idProfile = idProfileParameter;
	}
	
	public ProfileScreenFlow(final String factoryNameParameter, final Boolean isUserFilteredParameter) {
		this.factoryName = factoryNameParameter;
		this.isUserFiltered = isUserFilteredParameter;
	}
	
    public final Integer getIdProfile() {
        return this.idProfile;
    }
    
    public final void setIdProfile(final Integer idProfileParameter) {
        this.idProfile = idProfileParameter;
    }
    
    public final String getFactoryName() {
        return this.factoryName;
    }
    
    public final void setFactoryName(final String factoryNameParameter) {
        this.factoryName = factoryNameParameter;
    }
    
    public final String getFactoryNameTray() {
        return this.factoryNameTray;
    }

    public final void setFactoryNameTray(final String factoryNameTrayParameter) {
        this.factoryNameTray = factoryNameTrayParameter;
    }
    
    public final String getFlowStatus() {
        return this.flowStatus;
    }

    public final void setFlowStatus(final String flowStatusParameter) {
        this.flowStatus = flowStatusParameter;
    }

    public final Integer getIdFlow() {
        return this.idFlow;
    }
    
    public final void setIdFlow(final Integer idFlowParameter) {
        this.idFlow = idFlowParameter;
    }
    
    public final Boolean getIsUserFiltered() {
        return this.isUserFiltered;
    }
    
    public final void setIsUserFiltered(final Boolean isUserFilteredParameter) {
        this.isUserFiltered = isUserFilteredParameter;
    }
}
