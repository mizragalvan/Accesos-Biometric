package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.ObligationStatusEnum;

public class Obligation {
    private Integer idObligation;
    private Integer idRequisition;
    private String startDate;
    private String endDate;
    private String obligationText;
    private Double amount;
    private ObligationStatusEnum status;
    
    public final Integer getIdObligation() {
        return this.idObligation;
    }
    
    public final void setIdObligation(final Integer idObligationParameter) {
        this.idObligation = idObligationParameter;
    }
    
    public final Integer getIdRequisition() {
        return this.idRequisition;
    }
    
    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
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
    
    public final String getObligationText() {
        return this.obligationText;
    }

    public final void setObligationText(final String obligationTextParameter) {
        this.obligationText = obligationTextParameter;
    }

    public final Double getAmount() {
        return this.amount;
    }

    public final void setAmount(final Double amountParameter) {
        this.amount = amountParameter;
    }

    public final ObligationStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final ObligationStatusEnum statusParameter) {
        this.status = statusParameter;
    }
}
