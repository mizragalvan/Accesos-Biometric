package mx.pagos.admc.contracts.structures;

public class RequisitionAngular {
	
	private Integer idRequisition;
	private Comment comment;
	private FlowScreenAction flow;
	private Integer updateRequisitionBy;
	private Integer idFlow;
	private String nameLawyer;
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}
	public Comment getComment() {
		return comment;
	}
	public void setComment(Comment comment) {
		this.comment = comment;
	}
	public FlowScreenAction getFlow() {
		return flow;
	}
	public void setFlow(FlowScreenAction flow) {
		this.flow = flow;
	}
	
	public Integer getUpdateRequisitionBy() {
		return updateRequisitionBy;
	}
	public void setUpdateRequisitionBy(Integer updateRequisitionBy) {
		this.updateRequisitionBy = updateRequisitionBy;
	}
	public Integer getIdFlow() {
		return idFlow;
	}
	public void setIdFlow(Integer idFlow) {
		this.idFlow = idFlow;
	}
	public String getNameLawyer() {
		return nameLawyer;
	}
	public void setNameLawyer(String nameLawyer) {
		this.nameLawyer = nameLawyer;
	}

	

	
}
