package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.GuaranteeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;

public class CheckDocument {
    private Integer idCheckDocument;
    private String name;
    private GuaranteeEnum guarantee;
    private RecordStatusEnum status;
    private Integer idGuarantee;
    private Integer idRequisitionOwner;
    
    public final Integer getIdCheckDocument() {
        return this.idCheckDocument;
    }
    
    public final void setIdCheckDocument(final Integer idCheckDocumentParameter) {
        this.idCheckDocument = idCheckDocumentParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final GuaranteeEnum getGuarantee() {
        return this.guarantee;
    }

    public final void setGuarantee(final GuaranteeEnum guaranteeParameter) {
        this.guarantee = guaranteeParameter;
    }

    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }

    public final Integer getIdGuarantee() {
        return this.idGuarantee;
    }

    public final void setIdGuarantee(final Integer idGuaranteeParameter) {
        this.idGuarantee = idGuaranteeParameter;
    }

    public final Integer getIdRequisitionOwner() {
        return this.idRequisitionOwner;
    }

    public final void setIdRequisitionOwner(final Integer idRequisitionOwnerParameter) {
        this.idRequisitionOwner = idRequisitionOwnerParameter;
    }
}
