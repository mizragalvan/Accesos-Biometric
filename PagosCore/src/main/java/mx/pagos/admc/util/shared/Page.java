package mx.pagos.admc.util.shared;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<T> items;
	private T filter;
	private Integer totalItems;
	private Integer totalPages;
	private Integer pageSize;
	private Integer actualPage;
	
	
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public T getFilter() {
		return filter;
	}
	public void setFilter(T filter) {
		this.filter = filter;
	}
	public Integer getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getActualPage() {
		return actualPage;
	}
	public void setActualPage(Integer actualPage) {
		this.actualPage = actualPage;
	}


}