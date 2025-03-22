package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.RequisitionGarantia;
import mx.pagos.general.exceptions.DatabaseException;

public interface RequisitionGarantiable {

	Integer save(RequisitionGarantia requisitionGarantia) throws DatabaseException;
	
	Integer update(RequisitionGarantia requisitionGarantia) throws DatabaseException;
	
	RequisitionGarantia  findIdGarantiasByIdIdRequisitionGar(Integer IdGarantia) throws DatabaseException;
	
	List<RequisitionGarantia>  findGarantiasByIdSupplierRequisition(Integer idRequisition) throws DatabaseException;

	void deleteGarantesByIdSupplierRequisition(Integer IdSupplierRequisition) throws DatabaseException;

	
}
