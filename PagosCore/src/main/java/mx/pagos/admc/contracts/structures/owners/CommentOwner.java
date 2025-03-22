package mx.pagos.admc.contracts.structures.owners;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;

public class CommentOwner {
    private Integer idCommentOwner;
    private Integer idRequisitionOwners;
    private String commentText;
    private String createDate;
    private FlowPurchasingEnum flowStatus;
    private CommentType commentType;
    private List<FileUploadInfo> commentOwnerDocumentsList = new ArrayList<FileUploadInfo>();
    
    public final Integer getIdCommentOwner() {
        return this.idCommentOwner;
    }
    
    public final void setIdCommentOwner(final Integer idCommentOwnerParameter) {
        this.idCommentOwner = idCommentOwnerParameter;
    }
    
    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }
    
    public final void setIdRequisitionOwners(final Integer idRequisitionOwnerParameter) {
        this.idRequisitionOwners = idRequisitionOwnerParameter;
    }
    
    public final String getCommentText() {
        return this.commentText;
    }
    
    public final void setCommentText(final String commentTextParameter) {
        this.commentText = commentTextParameter;
    }
    
    public final String getCreateDate() {
        return this.createDate;
    }

    public final void setCreateDate(final String creationDateParameter) {
        this.createDate = creationDateParameter;
    }

    public final FlowPurchasingEnum getFlowStatus() {
        return this.flowStatus;
    }
    
    public final void setFlowStatus(final FlowPurchasingEnum flowStatusParameter) {
        this.flowStatus = flowStatusParameter;
    }
    
    public final CommentType getCommentType() {
        return this.commentType;
    }
    
    public final void setCommentType(final CommentType commentTypeParameter) {
        this.commentType = commentTypeParameter;
    }
    
    public final List<FileUploadInfo> getCommentOwnerDocumentsList() {
        return this.commentOwnerDocumentsList;
    }

    public final void setCommentOwnerDocumentsList(final List<FileUploadInfo> commentOwnerDocumentsListParameter) {
        this.commentOwnerDocumentsList = commentOwnerDocumentsListParameter;
    }
}
