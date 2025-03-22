package mx.pagos.admc.contracts.structures.dtos;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mizraim
 */
public class VersionDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer idVersion;
    private String documentPath;
    private Integer versionNumber;
    private Integer idDocument;
    private String fileName;
    private Integer idUser;
    private String userName;
	private Date createDate;
	private String createDateString;
	private boolean delete;
	
	public VersionDTO () {}
	
	public Integer getIdVersion() {
		return idVersion;
	}

	public void setIdVersion(Integer idVersion) {
		this.idVersion = idVersion;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Integer getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCreateDateString() {
		return createDateString;
	}

	public void setCreateDateString(String createDateString) {
		this.createDateString = createDateString;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	
	
}
