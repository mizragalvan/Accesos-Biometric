package mx.pagos.admc.contracts.structures;

public class RequisitionDraftPart2 {
	private Integer idRequisition;
	private Integer idDocumentType;
	private String ActorActivo;
	private String ActorPasivo;
	private Integer UpdateRequisitionBy;

	public Integer getIdRequisition() {
		return idRequisition;
	}

	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}

	public Integer getIdDocumentType() {
		return idDocumentType;
	}

	public void setIdDocumentType(Integer idDocumenttype) {
		this.idDocumentType = idDocumenttype;
	}

	public String getActorActivo() {
		return ActorActivo;
	}

	public void setActorActivo(String actorActivo) {
		ActorActivo = actorActivo;
	}

	public String getActorPasivo() {
		return ActorPasivo;
	}

	public void setActorPasivo(String actorPasivo) {
		ActorPasivo = actorPasivo;
	}

	public Integer getUpdateRequisitionBy() {
		return UpdateRequisitionBy;
	}

	public void setUpdateRequisitionBy(Integer updateRequisitionBy) {
		UpdateRequisitionBy = updateRequisitionBy;
	}

}
