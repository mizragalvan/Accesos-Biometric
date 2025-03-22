package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.FlowPurchasingEnum;

public class RequisitionStatusTurn {
    private Integer idRequisition;
    private FlowPurchasingEnum status;
    private Integer turn;
    private String turnDate;
    private Integer attentionDays;
    private String stage;
    private boolean active;
    
    public final Integer getIdRequisition() {
        return this.idRequisition;
    }
    
    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
    }
    
    public final FlowPurchasingEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final FlowPurchasingEnum statusParameter) {
        this.status = statusParameter;
    }
    
    public final Integer getTurn() {
        return this.turn;
    }
    
    public final void setTurn(final Integer turnParameter) {
        this.turn = turnParameter;
    }
    
    public final String getTurnDate() {
        return this.turnDate;
    }

    public final void setTurnDate(final String dateParameter) {
        this.turnDate = dateParameter;
    }

    public final Integer getAttentionDays() {
        return this.attentionDays;
    }
    
    public final void setAttentionDays(final Integer attentionDaysParameter) {
        this.attentionDays = attentionDaysParameter;
    }

    public final String getStage() {
        return this.stage;
    }

    public final void setStage(final String stageParameter) {
        this.stage = stageParameter;
    }

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
    
}
