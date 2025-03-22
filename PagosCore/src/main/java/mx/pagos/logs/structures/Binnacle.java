package mx.pagos.logs.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.LogCategoryEnum;

public class Binnacle {
	private Integer idBinnacle;
	private Integer idUser;
	private String userFullName;
    private Integer idFlow;
	private String flowName;
	private String registerDate;
	private String action;
	private LogCategoryEnum logCategory;
	private List<String> logList = new ArrayList<>();
	private String startDate;
	private String endDate;
	
	public final Integer getIdBinnacle() {
		return this.idBinnacle;
	}
	
	public final void setIdBinnacle(final Integer idBinnacleParameter) {
		this.idBinnacle = idBinnacleParameter;
	}
	
	public final Integer getIdUser() {
		return this.idUser;
	}
	
	public final void setIdUser(final Integer idUserParameter) {
		this.idUser = idUserParameter;
	}
	
	public final String getUserFullName() {
        return this.userFullName;
    }

    public final void setUserFullName(final String userFullNameParameter) {
        this.userFullName = userFullNameParameter;
    }

    public final Integer getIdFlow() {
        return this.idFlow;
    }

    public final void setIdFlow(final Integer idFlowParameter) {
        this.idFlow = idFlowParameter;
    }
    
    public final String getFlowName() {
        return this.flowName;
    }

    public final void setFlowName(final String flowNameParameter) {
        this.flowName = flowNameParameter;
    }

    public final String getRegisterDate() {
		return this.registerDate;
	}
	
	public final void setRegisterDate(final String registerDateParameter) {
		this.registerDate = registerDateParameter;
	}
	
	public final String getAction() {
		return this.action;
	}
	
	public final void setAction(final String actionParameter) {
		this.action = actionParameter;
	}

    public final LogCategoryEnum getLogCategory() {
        return this.logCategory;
    }

    public final void setLogCategory(final LogCategoryEnum logCategoryParameter) {
        this.logCategory = logCategoryParameter;
    }

    public final List<String> getLogList() {
        return this.logList;
    }

    public final void setLogList(final List<String> logListParameter) {
        this.logList = logListParameter;
    }

    public final String getStartDate() {
        return this.startDate;
    }

    public final void setStartDate(final String startDateParameter) {
        this.startDate = startDateParameter;
    }

    public final String getEndDate() {
        return this.endDate;
    }

    public final void setEndDate(final String endDateParameter) {
        this.endDate = endDateParameter;
    }
}
