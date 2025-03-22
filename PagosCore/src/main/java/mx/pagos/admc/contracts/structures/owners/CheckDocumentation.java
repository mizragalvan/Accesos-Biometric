package mx.pagos.admc.contracts.structures.owners;

import mx.pagos.admc.enums.RecordStatusEnum;

public class CheckDocumentation {
    private Integer idCheckDocumentation;
    private Integer idCategoryCheckDocumentation;
    private Integer idCategory;
    private String name;
    private Integer idRequisitionOwners;
    private RecordStatusEnum status;
    private Integer numberPage;
    private Integer totalRows;
    
    public final Integer getIdCheckDocumentation() {
        return this.idCheckDocumentation;
    }
    
    public final void setIdCheckDocumentation(final Integer idCheckDocumentParameter) {
        this.idCheckDocumentation = idCheckDocumentParameter;
    }
    
    public final Integer getIdCategoryCheckDocumentation() {
        return this.idCategoryCheckDocumentation;
    }
    
    public final void setIdCategoryCheckDocumentation(final Integer idCategoryCheckDocumentationParameter) {
        this.idCategoryCheckDocumentation = idCategoryCheckDocumentationParameter;
    }
    
    public final Integer getIdCategory() {
        return this.idCategory;
    }
    
    public final void setIdCategory(final Integer idCategoryParameter) {
        this.idCategory = idCategoryParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }

    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }

    public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
        this.idRequisitionOwners = idRequisitionOwnersParameter;
    }
    
    public final Integer getNumberPage() {
        return this.numberPage;
    }

    public final void setNumberPage(final Integer numberPageParameter) {
        this.numberPage = numberPageParameter;
    }

    public final Integer getTotalRows() {
        return this.totalRows;
    }

    public final void setTotalRows(final Integer totalRowsParameter) {
        this.totalRows = totalRowsParameter;
    }
}
