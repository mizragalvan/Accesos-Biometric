package mx.pagos.admc.contracts.structures;

/**
 * @author Mizraim
 */
public class RedFlag {
	private Integer idRedFlag;
    private Integer idRequisition;
    private String commentText;
    private String creationDate;
    private Integer idUser;
    private String userName;
    
    public RedFlag () {
    }

	public Integer getIdRedFlag() {
		return idRedFlag;
	}

	public void setIdRedFlag(Integer idRedFlag) {
		this.idRedFlag = idRedFlag;
	}

	public Integer getIdRequisition() {
		return idRequisition;
	}

	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
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
    
}
