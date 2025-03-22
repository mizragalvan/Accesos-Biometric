package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.FlowPurchasingEnum;

public class StatusBranch {
    private Integer idRequisitionOwners;
    private FlowPurchasingEnum status;
    private String holdForBranch;
    
    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }

    public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
        this.idRequisitionOwners = idRequisitionOwnersParameter;
    }

    public final FlowPurchasingEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final FlowPurchasingEnum statusParameter) {
        this.status = statusParameter;
    }
    
    public final String getHoldForBranch() {
        return this.holdForBranch;
    }
    
    public final void setHoldForBranch(final String holdForBranchParameter) {
        this.holdForBranch = holdForBranchParameter;
    }
}
