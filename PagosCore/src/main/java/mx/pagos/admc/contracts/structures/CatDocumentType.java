package mx.pagos.admc.contracts.structures;

import mx.pagos.admc.enums.RecordStatusEnum;

public class CatDocumentType {
	private Integer idDocument;
	private String name;
	private RecordStatusEnum status;
	private Integer pageNumber;
	private Integer totalRows;
	
	public Integer getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(Integer idDcoument) {
		this.idDocument = idDcoument;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RecordStatusEnum getStatus() {
		return status;
	}
	public void setStatus(RecordStatusEnum status) {
		this.status = status;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}
	
}
