package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.SupplierRequisition;
import mx.pagos.general.exceptions.DatabaseException;


public interface SupplierRequisitionble {

	Integer save(SupplierRequisition supplierRequisition) throws DatabaseException;
	
	Integer update(SupplierRequisition supplierRequisition) throws DatabaseException;
	
	List<Requisition>  findGarantesByIdSupplierAndIdRequisition(Integer idSupplier, Integer idRequisition) throws DatabaseException;
	
	List<Requisition>  findGarantesByIdRequisitionOrderByIdSupplierRequisitionAsc(Integer idRequisition) throws DatabaseException;
	
//	List<SupplierRequisition>  findGarantesByIdSupplierAndIdRequisition(Integer idSupplier, Integer idRequisition) throws DatabaseException;
//	
//	List<SupplierRequisition>  findGarantesByIdRequisitionOrderByIdSupplierRequisitionAsc(Integer idRequisition) throws DatabaseException;

	void deleteGarantesByIdRequisitionAndIdRequisition(Integer idSupplier, Integer idRequisition) throws DatabaseException;

	
}
