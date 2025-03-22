package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.FlowPurchasingEnum;

public class FlowScreenAction {

	private Integer idFlow;
	private String factoryName;
    private String actionName;
	private FlowPurchasingEnum status;
	private Integer step;
	
	public FlowScreenAction() {
	}
	
	public FlowScreenAction(final Integer idFlowParameter) {
		this.idFlow = idFlowParameter;
	}
	
	public final Integer getIdFlow() {
		return this.idFlow;
	}
	
	public final void setIdFlow(final Integer idFlowParameter) {
		this.idFlow = idFlowParameter;
	}
	
	public final String getFactoryName() {
		return this.factoryName;
	}
	
	public final void setFactoryName(final String factoryNameParameter) {
		this.factoryName = factoryNameParameter;
	}
	
	public final String getActionName() {
		return this.actionName;
	}
	
	public final void setActionName(final String actionNameParameter) {
		this.actionName = actionNameParameter;
	}

    public final FlowPurchasingEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final FlowPurchasingEnum statusParameter) {
        this.status = statusParameter;
    }

    public final Integer getStep() {
        return this.step;
    }

    public final void setStep(final Integer stepParameter) {
        this.step = stepParameter;
    }
}