package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Dga {
    private Integer idDga;
    private String name;
    private RecordStatusEnum status;
    private Integer numberPage;
    private Integer totalRows;

    public final Integer getIdDga() {
        return this.idDga;
    }

    public final void setIdDga(final Integer idDgaParameter) {
        this.idDga = idDgaParameter;
    }

    public final String getName() {
        return this.name;
    }

    public final void setName(final String dgaParameter) {
        this.name = dgaParameter;
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
