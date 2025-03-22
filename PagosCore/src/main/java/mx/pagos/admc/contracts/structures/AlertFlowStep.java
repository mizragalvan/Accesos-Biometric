package mx.pagos.admc.contracts.structures;

public class AlertFlowStep {
    private Integer idRequisition;
    private Integer idRequisitionOwners;
    private String categoryName;
    private String customerCompanyName;
    private String documentTypeName;
    private String commercialName;
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

    public final String getCommercialName() {
        return this.commercialName;
    }

    public final void setCommercialName(final String commercialNameParameter) {
        this.commercialName = commercialNameParameter;
    }

    public final String getEmail() {
        return this.email;
    }

    public final void setEmail(final String emailParameter) {
        this.email = emailParameter;
    }

    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }

    public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
        this.idRequisitionOwners = idRequisitionOwnersParameter;
    }

    public final String getCategoryName() {
        return this.categoryName;
    }

    public final void setCategoryName(final String categoryNameParameter) {
        this.categoryName = categoryNameParameter;
    }

    public final String getCustomerCompanyName() {
        return this.customerCompanyName;
    }

    public final void setCustomerCompanyName(final String customerCompanyNameParameter) {
        this.customerCompanyName = customerCompanyNameParameter;
    }
}
