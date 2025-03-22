package mx.pagos.admc.contracts.structures.owners;

public class FinishedContractOwners {

	private Integer idRequisitionOwners;
	private String customerCompanyName;
	private Integer idCategory;
	private String categoryName;
	private String applicationDate;
	private String signatureDate;
	private String startDate;
	private String endDate;
	
	public final Integer getIdRequisitionOwners() {
		return this.idRequisitionOwners;
	}
	
	public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
		this.idRequisitionOwners = idRequisitionOwnersParameter;
	}
	
	public final String getCustomerCompanyName() {
		return this.customerCompanyName;
	}
	
	public final void setCustomerCompanyName(final String customerCompanyNameParameter) {
		this.customerCompanyName = customerCompanyNameParameter;
	}
	
	public final Integer getIdCategory() {
		return this.idCategory;
	}
	
	public final void setIdCategory(final Integer idCategoryParameter) {
		this.idCategory = idCategoryParameter;
	}
	
	public final String getCategoryName() {
		return this.categoryName;
	}
	
	public final void setCategoryName(final String categoryNameParameter) {
		this.categoryName = categoryNameParameter;
	}
	
	public final String getApplicationDate() {
		return this.applicationDate;
	}
	
	public final void setApplicationDate(final String applicationDateParameter) {
		this.applicationDate = applicationDateParameter;
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
}
