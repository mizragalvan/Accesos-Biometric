package mx.pagos.document.versioning.structures;

import java.util.Date;

public class Version {
    private Integer idVersion;
    private String documentPath;
    private Integer versionNumber;
    private Integer idDocument;
    private String fileName;
    private Integer idUser;
	private Date createDate;

    public final Integer getIdVersion() {
        return this.idVersion;
    }

    public final void setIdVersion(final Integer idVersionParameter) {
        this.idVersion = idVersionParameter;
    }

    public final String getDocumentPath() {
        return this.documentPath;
    }

    public final void setDocumentPath(final String documentPathParameter) {
        this.documentPath = documentPathParameter;
    }

    public final Integer getVersionNumber() {
        return this.versionNumber;
    }

    public final void setVersionNumber(final Integer versionParameter) {
        this.versionNumber = versionParameter;
    }

    public final Integer getIdDocument() {
        return this.idDocument;
    }

    public final void setIdDocument(final Integer idDocumentParameter) {
        this.idDocument = idDocumentParameter;
    }

	public final String getFileName() {
		return this.fileName;
	}

	public final void setFileName(final String fileNameParameter) {
		this.fileName = fileNameParameter;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
