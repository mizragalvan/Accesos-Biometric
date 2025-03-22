package mx.pagos.admc.contracts.structures;

public class FinishedContractPurchases {

	private Integer idRequisition;
	private String supplierName;
	private Integer idDocumentTypeName;
	private String documentTypeName;
	private String dateOfAdmission;
	private String signatureDate;
	private String startDate;
	private String endDate;
	private String status;
	
	public final Integer getIdRequisition() {
		return this.idRequisition;
	}
	
	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}
	
	public final String getSupplierName() {
		return this.supplierName;
	}
	
	public final void setSupplierName(final String supplierNameParameter) {
		this.supplierName = supplierNameParameter;
	}
	
	public final String getDateOfAdmission() {
		return this.dateOfAdmission;
	}
	
	public final void setDateOfAdmission(final String dateOfAdmissionParameter) {
		this.dateOfAdmission = dateOfAdmissionParameter;
	}
	
	public final String getSignatureDate() {
		return this.signatureDate;
	}
	
	public final void setSignatureDate(final String signatureDateParameter) {
		this.signatureDate = signatureDateParameter;
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

	public final Integer getIdDocumentTypeName() {
		return this.idDocumentTypeName;
	}

	public final void setIdDocumentTypeName(final Integer idDocumentTypeNameParameter) {
		this.idDocumentTypeName = idDocumentTypeNameParameter;
	}

	public final String getDocumentTypeName() {
		return this.documentTypeName;
	}

	public final void setDocumentTypeName(final String documentTypeNameParameter) {
		this.documentTypeName = documentTypeNameParameter;
	}

	public final String getStatus() {
		return this.status;
	}

	public final void setStatus(final String statusParameter) {
		this.status = statusParameter;
	}
}
