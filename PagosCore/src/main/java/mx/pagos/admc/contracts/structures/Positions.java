package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class Positions {
	private Integer idPosition;
	private String name;
	private RecordStatusEnum status;
	private Integer numberPage;
	private Integer totalRows;
	
	public Positions() {
		super();
	}
	
	public Positions(final Integer idPositionParameter) {
		super();
		this.idPosition = idPositionParameter;
	}
	
	public final Integer getIdPosition() {
		return this.idPosition;
	}

	public final void setIdPosition(final Integer idPositionParameter) {
		this.idPosition = idPositionParameter;
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
