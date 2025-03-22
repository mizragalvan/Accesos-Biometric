package mx.pagos.admc.contracts.structures;

import java.util.Date;
import java.util.List;

import mx.pagos.security.structures.User;

public class FiltrosGrafica {
	private List<User> users;
	private List<Area> areas;
	private List<Supplier> suppliers;
	private List<FinancialEntity> companies;

	private int idUser;
	private int idArea;
	private int idSupplier;
	private int idCompany;
	private int idRequisition;

	private boolean onlyFinished = false;
	private Date startDate;
	private Date endDate;
	private int pageIndex;
	private int pageSize;
	private String idsAreas;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public List<FinancialEntity> getCompanies() {
		return companies;
	}

	public void setCompanies(List<FinancialEntity> companies) {
		this.companies = companies;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public int getIdArea() {
		return idArea;
	}

	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}

	public int getIdSupplier() {
		return idSupplier;
	}

	public void setIdSupplier(int idSupplier) {
		this.idSupplier = idSupplier;
	}

	public int getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(int idCompany) {
		this.idCompany = idCompany;
	}

	public boolean isOnlyFinished() {
		return onlyFinished;
	}

	public void setOnlyFinished(boolean onlyFinished) {
		this.onlyFinished = onlyFinished;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * @return the idsAreas
	 */
	public String getIdsAreas() {
		return idsAreas;
	}
	/**
	 * @param idsAreas the idsAreas to set
	 */
	public void setIdsAreas(String idsAreas) {
		this.idsAreas = idsAreas;
	}

	@Override
	public String toString() {
		return "FiltrosGrafica [users=" + users + ", areas=" + areas + ", suppliers=" + suppliers + ", companies="
				+ companies + ", idUser=" + idUser + ", idArea=" + idArea + ", idSupplier=" + idSupplier
				+ ", idCompany=" + idCompany + ", onlyFinished=" + onlyFinished + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", idsAreas="
				+ idsAreas + "]";
	}

	public int getIdRequisition() {
		return idRequisition;
	}

	public void setIdRequisition(int idRequisition) {
		this.idRequisition = idRequisition;
	}
	
	

}
