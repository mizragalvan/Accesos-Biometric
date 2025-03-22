package mx.pagos.admc.contracts.structures;

import java.time.LocalDate;

import mx.pagos.admc.core.enums.ContratistaEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.SemaphoreEnum;

public class TrayRequisition {
	private Integer idRequisition;
	private String documentTypeName;
	private String supplierName;
	/** Nombre del abogado asignado a la solicitud **/
	private String lawyerName;
	private String comment;
	private String applicationDate;
	private SemaphoreEnum semaphore;
	private Boolean retry;
	private FlowPurchasingEnum status;
	private String turnDate;

	private ContratistaEnum contractType;
	private String empresa;
	private String unidad;
	private String area;
	private Integer turn;
	private String turnString;
	private Integer attentiondays;
	private LocalDate stageStartDate;
	private String email;

	public final Integer getIdRequisition() {
		return this.idRequisition;
	}

	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final String getDocumentTypeName() {
		return this.documentTypeName;
	}

	public final void setDocumentTypeName(final String documentTypeNameParameter) {
		this.documentTypeName = documentTypeNameParameter;
	}

	public final String getSupplierName() {
		return this.supplierName;
	}

	public final void setSupplierName(final String supplierNameParameter) {
		this.supplierName = supplierNameParameter;
	}

	public final String getComment() {
		return this.comment;
	}

	public final void setComment(final String commentParameter) {
		this.comment = commentParameter;
	}

	public final String getApplicationDate() {
		return this.applicationDate;
	}

	public final void setApplicationDate(final String applicationDateParameter) {
		this.applicationDate = applicationDateParameter;
	}

	public final SemaphoreEnum getSemaphore() {
		return this.semaphore;
	}

	public final void setSemaphore(final SemaphoreEnum semaphoreParameter) {
		this.semaphore = semaphoreParameter;
	}

	public final Boolean getRetry() {
		return this.retry;
	}

	public final void setRetry(final Boolean retryParameter) {
		this.retry = retryParameter;
	}

	public final FlowPurchasingEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final FlowPurchasingEnum statusParameter) {
		this.status = statusParameter;
	}

	public final String getTurnDate() {
		return turnDate;
	}

	public final void setTurnDate(String turnDate) {
		this.turnDate = turnDate;
	}

	/**
	 * @return the lawyerName
	 */
	public String getLawyerName() {
		return lawyerName;
	}

	/**
	 * @param lawyerName the lawyerName to set
	 */
	public void setLawyerName(String lawyerName) {
		this.lawyerName = lawyerName;
	}

	/**
	 * @return the empresa
	 */
	public String getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa the empresa to set
	 */
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the unidad
	 */
	public String getUnidad() {
		return unidad;
	}

	/**
	 * @param unidad the unidad to set
	 */
	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the contratista
	 */
	public ContratistaEnum getContractType1() {
		return contractType;
	}

	/**
	 * @return the contratista
	 */
	public String getContractType() {
		if (contractType == null) {
			return null;
		} else {
			return contractType.toString();
		}
	}

	/**
	 * @param contratista the contratista to set
	 */
	public void setContractType(ContratistaEnum contractType) {
		this.contractType = contractType;
	}

	public void setContractType(String contractType) {
		if (null == contractType) {
			this.contractType = null;
		} else {
			this.contractType = ContratistaEnum.valueOf(contractType);
		}
	}

	public Integer getTurn() {
		return turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public String getTurnString() {
		return turnString;
	}

	public void setTurnString(String turnString) {
		this.turnString = turnString;
	}

	public Integer getAttentiondays() {
		return attentiondays;
	}

	public void setAttentiondays(Integer attentiondays) {
		this.attentiondays = attentiondays;
	}

	public LocalDate getStageStartDate() {
		return stageStartDate;
	}

	public void setStageStartDate(LocalDate stageStartDate) {
		this.stageStartDate = stageStartDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
