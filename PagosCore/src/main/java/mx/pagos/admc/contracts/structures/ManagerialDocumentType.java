package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.ManagerialDocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;

public class ManagerialDocumentType {
    private Integer idManagerialDocumentType;
    private String name;
    private String url;
    private ManagerialDocumentTypeEnum documentType;
    private RecordStatusEnum status;
    private Integer numberPage;
    private Integer totalRows;
    
    public final Integer getIdManagerialDocumentType() {
        return this.idManagerialDocumentType;
    }

    public final void setIdManagerialDocumentType(final Integer idManagerialDocumentTypeParameter) {
        this.idManagerialDocumentType = idManagerialDocumentTypeParameter;
    }

    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final String getUrl() {
        return this.url;
    }
    
    public final void setUrl(final String urlParameter) {
        this.url = urlParameter;
    }
    
    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
    }

    public final ManagerialDocumentTypeEnum getDocumentType() {
        return this.documentType;
    }

    public final void setDocumentType(final ManagerialDocumentTypeEnum documentTypeParameter) {
        this.documentType = documentTypeParameter;
    }
    
    public final Integer getNumberPage() {
        return this.numberPage;
    }

    public final void setNumberPage(final Integer numberPageParameter) {
        this.numberPage = numberPageParameter;
    }

    public final Integer getTotalRows() {
        return this.totalRows;
    }

    public final void setTotalRows(final Integer totalRowsParameter) {
        this.totalRows = totalRowsParameter;
    }
}
