package mx.pagos.document.versioning.structures;

public class DocumentBySection {
    private Integer idRequisitionOwnersVersion;
	private Integer idRequisitionOwners;
	private Integer idDocument;
	private String sectionType;
	private String documentName;
	
	public final Integer getIdRequisitionOwners() {
		return this.idRequisitionOwners;
	}
	
	public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
		this.idRequisitionOwners = idRequisitionOwnersParameter;
	}
	
	public final Integer getIdDocument() {
		return this.idDocument;
	}
	
	public final void setIdDocument(final Integer idDocumentParameter) {
		this.idDocument = idDocumentParameter;
	}
	
	public final String getSectionType() {
		return this.sectionType;
	}
	
	public final void setSectionType(final String sectionTypeParameter) {
		this.sectionType = sectionTypeParameter;
	}
	
	public final String getDocumentName() {
		return this.documentName;
	}
	
	public final void setDocumentName(final String documentNameParameter) {
		this.documentName = documentNameParameter;
	}

    public final Integer getIdRequisitionOwnersVersion() {
        return this.idRequisitionOwnersVersion;
    }

    public final void setIdRequisitionOwnersVersion(final Integer idRequisitionOwnersVersionParameter) {
        this.idRequisitionOwnersVersion = idRequisitionOwnersVersionParameter;
    }
}
