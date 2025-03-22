package mx.pagos.admc.contracts.structures;

public class UserInProgressRequisitionFilter {
    private Integer idUser;
    private Integer idRequisition;
    private String companyName;
    private String documentTypeName;
    private String statusName;
    private String name;
    private String startDate;
    private String endDate;
    
    public final Integer getIdUser() {
        return this.idUser;
    }
    
    public final void setIdUser(final Integer idUserParameter) {
        this.idUser = idUserParameter;
    }
    
    public final Integer getIdRequisition() {
        return this.idRequisition;
    }
    
    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
    }
    
    public final String getCompanyName() {
        return this.companyName;
    }
    
    public final void setCompanyName(final String companyNameParameter) {
        this.companyName = companyNameParameter;
    }
    
    public final String getDocumentTypeName() {
        return this.documentTypeName;
    }
    
    public final void setDocumentTypeName(final String documentTypeNameParameter) {
        this.documentTypeName = documentTypeNameParameter;
    }
    
    public final String getStatusName() {
        return this.statusName;
    }
    
    public final void setStatusName(final String statusNameParameter) {
        this.statusName = statusNameParameter;
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

    public final String getName() {
        return this.name;
    }

    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
}
