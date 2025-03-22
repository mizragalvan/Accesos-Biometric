package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.FinishedContractPurchasesable;
import mx.pagos.admc.contracts.structures.FinishedContractPurchases;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinishedContractPurchasesBusiness {

	private static final Logger LOG = Logger.getLogger(FinishedContractPurchasesBusiness.class);
    private static final String MESSAGE_FINISHED_CONTRACTS_PURCHASES_ERROR =
    		"Hubo un error al obtener los contratos finalizados en compras";
	
    @Autowired
    private FinishedContractPurchasesable finishedContractable;
    
    public List<FinishedContractPurchases> findFinishedContractsPurchases(
    		final FinishedContractPurchases vo) throws BusinessException {
    	try {
    		return this.finishedContractable.findFinishedContractsPurchases(vo);
    	} catch (DatabaseException dataBaseException) {
    		LOG.error(MESSAGE_FINISHED_CONTRACTS_PURCHASES_ERROR, dataBaseException);
    		throw new BusinessException(MESSAGE_FINISHED_CONTRACTS_PURCHASES_ERROR, dataBaseException);
    	}
    }
}
