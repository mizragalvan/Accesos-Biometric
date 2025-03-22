package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.AutomaticRenewable;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutomaticRenewalBusiness {
    private static final Logger LOG = Logger.getLogger(AutomaticRenewalBusiness.class);

    private static final String MESSAGE_AUTOMATIC_RENEW_REQUISITIONS_ERROR =
            "Hubo un problema al renovar automáticamente los contratos";
    private static final String MESSAGE_FIND_RENEWED_REQUISITIONS_ERROR =
            "Hubo un problema al recuperar los contratos autorenovados";
    private static final String MESSAGE_UPDATE_RENEWED_REQUISITIONS_STATUS_ERROR =
            "Hubo un problema al actualizar el estatus de las solicitudes renovadas";
    
    @Autowired
    private AutomaticRenewable automaticRenewable;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
    public void automaticRenewRequisitions() throws BusinessException {
        try {
            this.automaticRenewable.automaticRenewContracts();
            this.automaticRenewable.automaticUpdateToRenewedStatus();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_AUTOMATIC_RENEW_REQUISITIONS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_AUTOMATIC_RENEW_REQUISITIONS_ERROR, databaseException);
        }
    }
    
    public List<Requisition> findRenewedRequisitions() throws BusinessException {
        try {
            return this.automaticRenewable.findRenewedRequisitions();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_RENEWED_REQUISITIONS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_RENEWED_REQUISITIONS_ERROR, databaseException);
        }
    }
    
    public void updateToRenewedStatus(final List<Integer> idRequisitionsList) throws BusinessException {
        try {
            this.automaticRenewable.updateToRenewedStatus(idRequisitionsList);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_UPDATE_RENEWED_REQUISITIONS_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_UPDATE_RENEWED_REQUISITIONS_STATUS_ERROR, databaseException);
        }
    }
}
