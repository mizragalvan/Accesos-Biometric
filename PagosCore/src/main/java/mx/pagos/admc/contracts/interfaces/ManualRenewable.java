package mx.pagos.admc.contracts.interfaces;

import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface ManualRenewable {

    Integer renewContract(Integer idRequisition, FlowPurchasingEnum status) throws DatabaseException;

    FlowPurchasingEnum getRenewalStatus(Integer idFlow) throws DatabaseException;
}
