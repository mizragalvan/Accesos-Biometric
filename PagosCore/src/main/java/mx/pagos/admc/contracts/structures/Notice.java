package mx.pagos.admc.contracts.structures;


public class Notice {
	private Integer idNotice;
	private String noticeType;
	private String creationDate;
	private String dueDate;
	private Integer numberPage;
	private Integer totalRows;
	
	public final Integer getIdNotice() {
		return this.idNotice;
	}
	
	public final void setIdNotice(final Integer idNoticeParameter) {
		this.idNotice = idNoticeParameter;
	}
	
	public final String getNoticeType() {
		return this.noticeType;
	}
	
	public final void setNoticeType(final String noticeTypeParameter) {
		this.noticeType = noticeTypeParameter;
	}
	
	public final String getCreationDate() {
		return this.creationDate;
	}
	
	public final void setCreationDate(final String upDateParameter) {
		this.creationDate = upDateParameter;
	}
	
	public final String getDueDate() {
		return this.dueDate;
	}
	
	public final void setDueDate(final String dueDateParameter) {
		this.dueDate = dueDateParameter;
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
