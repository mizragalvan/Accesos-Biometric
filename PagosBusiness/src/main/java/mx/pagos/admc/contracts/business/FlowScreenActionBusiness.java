package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.FlowScreenActionable;
import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.contracts.structures.StatusBranch;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowScreenActionBusiness {

    private static final String MESSAGE_FIND_FLOW_SCREENACTION_ERROR =
            "Hubo un problema al recuperar los estatus por acciuón por flujo";
    private static final String MESSAGE_FIND_STATUS_BY_FLOW_ERROR =
            "Hubo un problema al recuperar los estatus por flujo";
    private static final String MESSAGE_FIND_STATUS_NAME_BY_FLOW =
            "Hubo un problema al recuperar los nombres de los estatus por flujo";
    private static final String MESSAGE_FIND_NEXT_MULTIPLE_STATUS_ERROR =
            "Error al recuperar el(los) siguiente(s) estatus del flujo";

    private static final Logger LOG = Logger.getLogger(FlowScreenActionBusiness.class);
    
    @Autowired
    private FlowScreenActionable flowScreenDao;

    public final List<FlowScreenAction> findFlowScreenActionByFlow(final FlowScreenAction flowScreenAction)
            throws BusinessException {
        try {
            return this.flowScreenDao.findFlowScreenActionByFlow(flowScreenAction); 
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_FIND_FLOW_SCREENACTION_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_FIND_FLOW_SCREENACTION_ERROR,
                    dataBaseException);
        }
    }
    
    public final List<FlowScreenAction> findStatusByFlow(final Integer idFlow) throws BusinessException {
    	try {
    		return this.flowScreenDao.findStatusByFlow(idFlow);
    	} catch (DatabaseException dataBaseException) {
    		LOG.error(MESSAGE_FIND_STATUS_BY_FLOW_ERROR, dataBaseException);
    		throw new BusinessException(MESSAGE_FIND_STATUS_BY_FLOW_ERROR, dataBaseException);
    	}
    }
    
    public final List<Screen> findStatusNameByFlow(final Integer idFlow) throws BusinessException {
    	try {
    		return this.flowScreenDao.findStatusNameByFlow(idFlow); 
    	} catch (DatabaseException dataBaseException) {
    		LOG.error(MESSAGE_FIND_STATUS_NAME_BY_FLOW, dataBaseException);
    		throw new BusinessException(MESSAGE_FIND_STATUS_NAME_BY_FLOW, dataBaseException);
    	}
    }
    
    public final List<StatusBranch> findNextMultipleStatus(final FlowScreenAction flowScreenAction)
            throws BusinessException {
        try {
            return this.flowScreenDao.findNextMultipleStatus(flowScreenAction);
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_FIND_NEXT_MULTIPLE_STATUS_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_FIND_NEXT_MULTIPLE_STATUS_ERROR, dataBaseException);
        }
    }
}
