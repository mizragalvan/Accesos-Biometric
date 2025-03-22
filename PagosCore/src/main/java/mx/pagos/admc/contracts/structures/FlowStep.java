package mx.pagos.admc.contracts.structures;

public class FlowStep {
    private Integer idFlow;
    private Integer step;
    private String stepImageEnabled;
    private String stepImageDisabled;
    
    public final Integer getIdFlow() {
        return this.idFlow;
    }
    
    public final void setIdFlow(final Integer idFlowParameter) {
        this.idFlow = idFlowParameter;
    }
    
    public final Integer getStep() {
        return this.step;
    }
    
    public final void setStep(final Integer stepParameter) {
        this.step = stepParameter;
    }
    
    public final String getStepImageEnabled() {
        return this.stepImageEnabled;
    }
    
    public final void setStepImageEnabled(final String stepImageEnabledParameter) {
        this.stepImageEnabled = stepImageEnabledParameter;
    }
    
    public final String getStepImageDisabled() {
        return this.stepImageDisabled;
    }
    
    public final void setStepImageDisabled(final String stepImageDisabledParameter) {
        this.stepImageDisabled = stepImageDisabledParameter;
    }
}
