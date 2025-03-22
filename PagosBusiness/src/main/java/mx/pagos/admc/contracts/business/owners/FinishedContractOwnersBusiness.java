package mx.pagos.admc.contracts.business.owners;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.owners.FinishedContractOwnersable;
import mx.pagos.admc.contracts.structures.owners.FinishedContractOwners;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class FinishedContractOwnersBusiness {

	private static final Logger LOG = Logger.getLogger(FinishedContractOwnersBusiness.class);
    private static final String MESSAGE_FINISHED_CONTRACTS_OWNERS_ERROR =
    		"Hubo un error al obtener los contratos finalizados en empresarial";
	
    @Autowired
    private FinishedContractOwnersable finishedContractOwnersable;
    
    public List<FinishedContractOwners> findFinishedContractsOwners(
    		final FinishedContractOwners vo) throws BusinessException {
    	try {
    		return this.finishedContractOwnersable.findFinishedContractsOwners(vo);
    	} catch (DatabaseException dataBaseException) {
    		LOG.error(MESSAGE_FINISHED_CONTRACTS_OWNERS_ERROR, dataBaseException);
    		throw new BusinessException(MESSAGE_FINISHED_CONTRACTS_OWNERS_ERROR, dataBaseException);
    	}
    }
}
