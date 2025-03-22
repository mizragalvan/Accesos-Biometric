package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Area {
	private Integer idArea;
	private String name;
	private RecordStatusEnum status;
	private Integer pageNumber;
	private Integer totalRows;
	
    public final Integer getIdArea() {
		return this.idArea;
	}
	
	public final void setIdArea(final Integer idAreaParameter) {
		this.idArea = idAreaParameter;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}
	
	public final RecordStatusEnum getStatus() {
		return this.status;
	}
	
	public final void setStatus(final RecordStatusEnum statusParameter) {
		this.status = statusParameter;
	}

    public final Integer getPageNumber() {
        return this.pageNumber;
    }

    public final void setPageNumber(final Integer pageNumberParameter) {
        this.pageNumber = pageNumberParameter;
    }

    public final Integer getTotalRows() {
        return this.totalRows;
    }

    public final void setTotalRows(final Integer totalRowsParameter) {
        this.totalRows = totalRowsParameter;
    }
}
