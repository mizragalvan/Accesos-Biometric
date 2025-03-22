package mx.pagos.admc.contracts.structures;

import java.util.List;

public class RequisitionsPartThree {
	private Integer idRequisition;
	private Supplier supplier;
	private Integer idSupplier;
	private List<SupplierPersonByRequisition> supplierPersoList;
	
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}
	public Integer getIdSupplier() {
		return idSupplier;
	}
	public void setIdSupplier(Integer idSupplier) {
		this.idSupplier =idSupplier;
	}
	public List<SupplierPersonByRequisition> getSupplierPersoList() {
		return supplierPersoList;
	}
	public void setSupplierPersoList(List<SupplierPersonByRequisition> supplierPersoList) {
		this.supplierPersoList = supplierPersoList;
	}

}
