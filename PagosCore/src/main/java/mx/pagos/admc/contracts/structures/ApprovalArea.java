package mx.pagos.admc.contracts.structures;

public class ApprovalArea {
    private Integer idArea;
    private String documentPath;
    private Integer voboIdDocument;
    private String name;
    
    public final Integer getIdArea() {
        return this.idArea;
    }
    
    public final void setIdArea(final Integer idAreaParameter) {
        this.idArea = idAreaParameter;
    }
    
    public final String getDocumentPath() {
        return this.documentPath;
    }
    
    public final void setDocumentPath(final String documentPathParameter) {
        this.documentPath = documentPathParameter;
    }
    
    public final Integer getVoboIdDocument() {
        return this.voboIdDocument;
    }
    
    public final void setVoboIdDocument(final Integer voboIdDocumentParameter) {
        this.voboIdDocument = voboIdDocumentParameter;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
}
