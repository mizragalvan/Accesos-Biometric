package mx.pagos.document.versioning.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Document {
    private Integer idDocument;
    private Integer currentVersion;
    private RecordStatusEnum status;
    
    public final Integer getIdDocument() {
        return this.idDocument;
    }
    
    public final void setIdDocument(final Integer idDocumentParameter) {
        this.idDocument = idDocumentParameter;
    }
    
    public final Integer getCurrentVersion() {
        return this.currentVersion;
    }
    
    public final void setCurrentVersion(final Integer currentVersionParameter) {
        this.currentVersion = currentVersionParameter;
    }
    
    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }
}
