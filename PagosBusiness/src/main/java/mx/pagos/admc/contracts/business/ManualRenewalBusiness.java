package mx.pagos.admc.contracts.business;

import mx.pagos.admc.contracts.interfaces.ManualRenewable;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ManualRenewalBusiness {
    private static final String MESSAGE_REQUISITION_MANUAL_RENEW_ERROR =
            "Hubo un problema al renovar el contrato";
    
    private static final Logger LOG = Logger.getLogger(ManualRenewalBusiness.class);
    
    @Autowired
    private ManualRenewable manualRenewable;
    
    public final Integer renewContract(final Integer idRequisition, final Integer idFlow)
            throws BusinessException {
        try {
            final FlowPurchasingEnum status = this.manualRenewable.getRenewalStatus(idFlow);
            return this.manualRenewable.renewContract(idRequisition, status);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_REQUISITION_MANUAL_RENEW_ERROR, databaseException);
            throw new BusinessException(MESSAGE_REQUISITION_MANUAL_RENEW_ERROR, databaseException);
        }
    }
}
