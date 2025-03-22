package mx.pagos.admc.contracts.structures.owners;

import mx.pagos.admc.enums.RecordStatusEnum;

public class GuaranteeCheckList {
    private Integer idGuaranteeChecklist;
    private Integer idGuarantee;
    private String checklistCategory;
    private String checklistDescription;
    private RecordStatusEnum status;
    
    public final Integer getIdGuaranteeChecklist() {
        return this.idGuaranteeChecklist;
    }
    
    public final void setIdGuaranteeChecklist(final Integer idGuaranteeChecklistParameter) {
        this.idGuaranteeChecklist = idGuaranteeChecklistParameter;
    }
    
    public final Integer getIdGuarantee() {
        return this.idGuarantee;
    }
    
    public final void setIdGuarantee(final Integer idGuaranteeParameter) {
        this.idGuarantee = idGuaranteeParameter;
    }
    
    public final String getChecklistCategory() {
        return this.checklistCategory;
    }
    
    public final void setChecklistCategory(final String checklistCategoryParameter) {
        this.checklistCategory = checklistCategoryParameter;
    }
    
    public final String getChecklistDescription() {
        return this.checklistDescription;
    }
    
    public final void setChecklistDescription(final String checklistDescriptionParameter) {
        this.checklistDescription = checklistDescriptionParameter;
    }

    public final RecordStatusEnum getStatus() {
        return this.status;
    }

    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }
}
