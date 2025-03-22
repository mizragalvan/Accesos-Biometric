package mx.pagos.admc.contracts.structures;

public class UserInProgressRequisition {
    private Integer idRequisition;
    private String companyName;
    private String documentTypeName;
    private String statusName;
    private String turnDate;
    private String expectedAttentionDate;
    private Integer idTemplateDocument;
    private String name;
    
    
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
    
    public final String getTurnDate() {
        return this.turnDate;
    }
    
    public final void setTurnDate(final String turnDateParameter) {
        this.turnDate = turnDateParameter;
    }
    
    public final String getExpectedAttentionDate() {
        return this.expectedAttentionDate;
    }
    
    public final void setExpectedAttentionDate(final String expectedAttentionDateParameter) {
        this.expectedAttentionDate = expectedAttentionDateParameter;
    }

	public final Integer getIdTemplateDocument() {
		return this.idTemplateDocument;
	}

	public final void setIdTemplateDocument(final Integer idTemplateDocumentParameter) {
		this.idTemplateDocument = idTemplateDocumentParameter;
	}

    public final String getName() {
        return this.name;
    }

    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
}
