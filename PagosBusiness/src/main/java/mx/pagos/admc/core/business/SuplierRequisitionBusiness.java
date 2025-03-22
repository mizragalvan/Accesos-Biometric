package mx.pagos.admc.core.business;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.RequisitionGarantiable;
import mx.pagos.admc.contracts.interfaces.SupplierRequisitionble;
import mx.pagos.admc.contracts.interfaces.Supplierable;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.SupplierRequisition;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;


/**
 * 
 * Lógica de negocio para manejar los tipos de persona Requision
 * 
 * @author Mizraim
 * 
 * @see SupplierRequisition
 * @see BusinessException
 * @see RecordStatusEnum
 *
 */
@Service
public class SuplierRequisitionBusiness {
	
    private static final String MESSAGE_PROBLEM_SAVING_SUPLIERREQUISITION =
            "Ocurrió un problema al guardar persona requisición";
    private static final String MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR =
            "Ocurrió un eror al recuperar los datos de  persona requisición";

    private static final Logger LOG = Logger.getLogger(SuplierRequisitionBusiness.class);

    @Autowired
    @Lazy
    private SupplierRequisitionble supplierRequisitionble;
    
    @Autowired
    @Lazy
    private Supplierable supplierable;
    
    @Autowired
    @Lazy
    private RequisitionGarantiable requisitionGarantiable;

    public  Integer saveOrUpdate(final SupplierRequisition supplierRequisition) throws BusinessException {
        try {
        	final Integer idSupplierRequisition = this.supplierRequisitionble.save(supplierRequisition);
   		 	return idSupplierRequisition;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_PROBLEM_SAVING_SUPLIERREQUISITION, databaseException);
            throw new BusinessException(MESSAGE_PROBLEM_SAVING_SUPLIERREQUISITION, databaseException);
        }
    }

    public  List<Requisition> findGarantesByIdSupplierAndIdRequisition(Integer idSupplier, Integer idRequisition) throws BusinessException {
        try {
        	List<Requisition> lista = this.supplierRequisitionble.findGarantesByIdSupplierAndIdRequisition(idSupplier,idRequisition);
//            for (Requisition sr : lista) {
//        		sr.setSupplier(this.supplierable.findById(idSupplier));
//        		sr.setGarantias(this.requisitionGarantiable.findGarantiasByIdSupplierRequisition(sr.getIdSupplierRequisition()));
//			}
            return lista;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
//        } catch (EmptyResultException databaseException) {
//            LOG.error(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
//            throw new BusinessException(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
        }
    }
    
    
    public  void deleteGarantesByIdRequisitionAndIdRequisition(Integer idSupplier, Integer idRequisition) throws BusinessException {
        try {
            this.supplierRequisitionble.deleteGarantesByIdRequisitionAndIdRequisition(idSupplier, idRequisition);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
        }
    }
    
    public   List<Requisition> findGarantesByIdRequisition(Integer idRequisition) throws BusinessException {
        try {
        	List<Requisition> lista = this.supplierRequisitionble.findGarantesByIdRequisitionOrderByIdSupplierRequisitionAsc(idRequisition);
        	for (Requisition sr : lista) {
        		sr.setSupplier(this.supplierable.findById(sr.getIdSupplier()));
			}
        	
//        	Comparator<Requisition> idComparator = new Comparator<Requisition>() {
//                @Override
//                public int compare(Requisition e1, Requisition e2) {
//                    return Integer.compare(e1.getIdSupplierRequisition().intValue(), e2.getIdSupplierRequisition().intValue());                
//                }
//            };
//            Collections.sort(lista, idComparator);
            
        	return lista;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
        }catch (EmptyResultException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_SUPLIERREQUISITION_ERROR, databaseException);
        }
    }
    
    
}
