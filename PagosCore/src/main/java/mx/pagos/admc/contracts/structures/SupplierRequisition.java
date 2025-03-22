package mx.pagos.admc.contracts.structures;

import java.util.List;

import lombok.Data;

@Data
public class SupplierRequisition {

	private Integer idSupplierRequisition;
	
    private Integer idSupplier;
    
    private Integer idRequisition;
    
    private String tipoRelacion;
    
    private Supplier supplier;
    
    private List<RequisitionGarantia> garantias;
    
    private RequisitionGarantia garantia;
	
}
