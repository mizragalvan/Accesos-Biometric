package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.Flowable;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowsBusiness {
    private static final Logger LOG = Logger.getLogger(FlowsBusiness.class);
    private static final String MESSAGE_RETRIVING_IS_MANAGERIAL_FLOW_ERROR =
            "Hubo un problema al determinar el tipo d flujo";
    private static final String MESSAGE_FIND_PURCHASING_FLOWS_ERROR =
            "Hubo un problema al buscar todos los flujos de tipo compras";

    @Autowired
    private Flowable flowable;

    public final Integer saveOrUpdate(final Flow flow) throws BusinessException {
        try {
            return this.flowable.saveOrUpdate(flow);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar los datos del Flujo", dataBaseException);
            throw new BusinessException("Error al guardar datos del flujo", dataBaseException);
        }
    }

    public final void changeFlowStatus(final Integer idFlow, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.flowable.changeFlowStatus(idFlow, RecordStatusEnum.INACTIVE);	
            else
                this.flowable.changeFlowStatus(idFlow, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar el estatus del Flujo", dataBaseException);
            throw new BusinessException("Error al cambiar el estatus del flujo", dataBaseException);
        }
    }

    public final List<Flow> findAll() throws BusinessException {
        try {
            return this.flowable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Flujos", dataBaseException);
            throw new BusinessException("Error al obtener datos del flujo", dataBaseException);
        }
    }

    public final Flow findByFlowName(final String flowName) throws BusinessException {
        try {
            return this.flowable.findByFlowName(flowName);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Flujo por nombre", databaseException);
            throw new BusinessException("Error al obtener el flujo", databaseException);
        }
    }

    public final List<Flow> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.flowable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Flujos por estatus", dataBaseException);
            throw new BusinessException("Error al obtener estatus del flujo", dataBaseException);
        }
    }	

    public final Flow findById(final Integer idFlow) throws BusinessException {
        try {
            return this.flowable.findById(idFlow);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Flujo por Id", databaseException);
            throw new BusinessException("Error al obtener el flujo por id", databaseException);
        }
    }
    
    public final List<String> findStepListByIdFlow(final Integer idFlow) throws BusinessException {
        try {
            return this.flowable.findStepListByIdFlow(idFlow);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener pasos del flujo por Id", databaseException);
            throw new BusinessException("Error al obtener pasos del flujo por id", databaseException);
        }
    }
    
    public final Boolean isManagerialFlow(final Integer idFlow) throws BusinessException {
        try {
            return this.flowable.isManagerialFlow(idFlow);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_IS_MANAGERIAL_FLOW_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_IS_MANAGERIAL_FLOW_ERROR, databaseException);
        }
    }
    
    public final List<Flow> findPurchasingFlows() throws BusinessException {
        try {
            return this.flowable.findPurchasingFlows();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_PURCHASING_FLOWS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_PURCHASING_FLOWS_ERROR, databaseException);
        }
    }
}
