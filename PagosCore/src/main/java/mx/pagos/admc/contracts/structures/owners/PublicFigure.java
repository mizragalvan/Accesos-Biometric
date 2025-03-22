package mx.pagos.admc.contracts.structures.owners;

import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.owners.PublicFigureTypeEnum;

public class PublicFigure {
    private Integer idPublicFigure;
    private String name;
    private PublicFigureTypeEnum type;
    private RecordStatusEnum status;
    private Integer numberPage;
    private Integer totalRows;
    
    public PublicFigure() { }
    
    public PublicFigure(final Integer idPublicFigureParameter, final RecordStatusEnum statusParameter) {
        this.idPublicFigure = idPublicFigureParameter;
        this.status = statusParameter;
    }

    public final Integer getIdPublicFigure() {
        return this.idPublicFigure;
    }
    
    public final void setIdPublicFigure(final Integer idPublicFigureParameter) {
        this.idPublicFigure = idPublicFigureParameter;
    }
    
    public final String getName() {
        return this.name;
    }
    
    public final void setName(final String nameParameter) {
        this.name = nameParameter;
    }
    
    public final PublicFigureTypeEnum getType() {
        return this.type;
    }
    
    public final void setType(final PublicFigureTypeEnum typeParameter) {
        this.type = typeParameter;
    }
    
    public final RecordStatusEnum getStatus() {
        return this.status;
    }
    
    public final void setStatus(final RecordStatusEnum statusParameter) {
        this.status = statusParameter;
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
