package mx.pagos.admc.contracts.structures.dtos;

import java.io.Serializable;

import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.FlowPurchasingEnum;
/**
 * @author Mizraim
 *
 */
public class CommentDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer idComment;
	private Integer idRequisition;
	private Integer idUser;
	private String userName;
	private FlowPurchasingEnum flowStatus;
	private CommentType commentType;
	private String commentText;
	private String creationDate;
	
	public CommentDTO () {}

	public Integer getIdComment() {
		return idComment;
	}

	public void setIdComment(Integer idComment) {
		this.idComment = idComment;
	}

	public Integer getIdRequisition() {
		return idRequisition;
	}

	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public FlowPurchasingEnum getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(FlowPurchasingEnum flowStatus) {
		this.flowStatus = flowStatus;
	}

	public CommentType getCommentType() {
		return commentType;
	}

	public void setCommentType(CommentType commentType) {
		this.commentType = commentType;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
}
