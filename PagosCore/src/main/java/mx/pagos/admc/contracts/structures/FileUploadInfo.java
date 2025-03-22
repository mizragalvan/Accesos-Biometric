package mx.pagos.admc.contracts.structures;


public class FileUploadInfo {

	 private String filePath;
	 private String field;
	 private String name;
	 private String documentName;
	 private String ctype;
	 private Integer idFile;
	 private boolean fileNew;
	 private int returnExist;
	 private Integer idRequisition;
	 private String commentText;
	 private boolean documentofina=false;

	public boolean isDocumentofina() {
		return documentofina;
	}

	public void setDocumentofina(boolean documentofina) {
		this.documentofina = documentofina;
	}

	public final String getFilePath() {
		return this.filePath;
	}
	
	public final void setFilePath(final String filePathParameter) {
		this.filePath = filePathParameter;
	}
	
	public final String getField() {
		return this.field;
	}
	
	public final void setField(final String fieldParameter) {
		this.field = fieldParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final String getCtype() {
		return this.ctype;
	}
	
	public final void setCtype(final String ctypeParameter) {
		this.ctype = ctypeParameter;
	}
	
	public final Integer getIdFile() {
		return this.idFile;
	}
	
	public final void setIdFile(final Integer idFileParameter) {
		this.idFile = idFileParameter;
	}
	
	public final boolean isFileNew() {
		return this.fileNew;
	}
	
	public final void setFileNew(final boolean fileNewParameter) {
		this.fileNew = fileNewParameter;
	}
	
	public final int getReturnExist() {
		return this.returnExist;
	}
	
	public final void setReturnExist(final int returnExistParameter) {
		this.returnExist = returnExistParameter;
	}
	
	public final Integer getIdRequisition() {
		return this.idRequisition;
	}
	
	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final String getDocumentName() {
		return this.documentName;
	}

	public final void setDocumentName(final String documentNameParameter) {
		this.documentName = documentNameParameter;
	}

    public final String getCommentText() {
        return this.commentText;
    }

    public final void setCommentText(final String commentTextParameter) {
        this.commentText = commentTextParameter;
    }
}
