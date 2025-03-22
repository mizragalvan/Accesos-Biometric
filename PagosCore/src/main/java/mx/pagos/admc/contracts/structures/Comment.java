package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;

public class Comment {

	private Integer idComment;
	private Integer idRequisition;
	private String commentText;
	private String creationDate;
	private FlowPurchasingEnum flowStatus;
	private String codRetPopup;
	private CommentType commentType;
	private Integer idUser;
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public final Integer getIdComment() {
		return this.idComment;
	}

	public final void setIdComment(final Integer idCommentParameter) {
		this.idComment = idCommentParameter;
	}

	public final Integer getIdRequisition() {
		return this.idRequisition;
	}

	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final String getCommentText() {
		return this.commentText;
	}

	public final void setCommentText(final String commentTextParameter) {
		this.commentText = commentTextParameter;
	}

	public final String getCreationDate() {
		return this.creationDate;
	}

	public final void setCreationDate(final String createdDateParameter) {
		this.creationDate = createdDateParameter;
	}

	public final FlowPurchasingEnum getFlowStatus() {
		return this.flowStatus;
	}

	public final void setFlowStatus(final FlowPurchasingEnum flowStatusParameter) {
		this.flowStatus = flowStatusParameter;
	}

	public final String getCodRetPopup() {
		return this.codRetPopup;
	}

	public final void setCodRetPopup(final String codRetPopupParameter) {
		this.codRetPopup = codRetPopupParameter;
	}

	public final CommentType getCommentType() {
		return this.commentType;
	}

	public final void setCommentType(final CommentType commentTypeParameter) {
		this.commentType = commentTypeParameter;
	}

	public final Integer getIdUser() {
		return this.idUser;
	}

	public final void setIdUser(final Integer idUserParameter) {
		this.idUser = idUserParameter;
	}
}
