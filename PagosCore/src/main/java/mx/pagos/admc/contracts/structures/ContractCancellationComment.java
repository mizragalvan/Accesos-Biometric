package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.document.versioning.structures.DocumentBySection;

public class ContractCancellationComment {
    private Integer idCancellationComent;
    private Integer idRequisition;
    private String comment;
    private String creationDate;
    private FlowPurchasingEnum requisitionCancelStatus;
    private List<FileUploadInfo> fileInfoList = new ArrayList<>();
    private List<DocumentBySection> documentList = new ArrayList<>();
    
    public final Integer getIdCancellationComent() {
        return this.idCancellationComent;
    }
    
    public final void setIdCancellationComent(final Integer idCancellationComentParameter) {
        this.idCancellationComent = idCancellationComentParameter;
    }
    
    public final String getComment() {
        return this.comment;
    }
    
    public final void setComment(final String cancellationCommentParameter) {
        this.comment = cancellationCommentParameter;
    }
    
    public final String getCreationDate() {
        return this.creationDate;
    }
    
    public final void setCreationDate(final String creationDateParameter) {
        this.creationDate = creationDateParameter;
    }

    public final List<FileUploadInfo> getFileInfoList() {
        return this.fileInfoList;
    }

    public final void setFileInfoList(final List<FileUploadInfo> fileInfoListParameter) {
        this.fileInfoList = fileInfoListParameter;
    }

    public final Integer getIdRequisition() {
        return this.idRequisition;
    }

    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
    }

    public final FlowPurchasingEnum getRequisitionCancelStatus() {
        return this.requisitionCancelStatus;
    }

    public final void setRequisitionCancelStatus(final FlowPurchasingEnum requisitionCancelStatusParameter) {
        this.requisitionCancelStatus = requisitionCancelStatusParameter;
    }

    public final List<DocumentBySection> getDocumentList() {
        return this.documentList;
    }

    public final void setDocumentList(List<DocumentBySection> documentListParameter) {
        this.documentList = documentListParameter;
    }
}
