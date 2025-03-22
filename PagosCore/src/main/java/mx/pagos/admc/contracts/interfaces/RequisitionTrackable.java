package mx.pagos.admc.contracts.interfaces;

import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface RequisitionTrackable {
	
	boolean getActiveByIdRequistionStatus(final Integer idRequisition, final FlowPurchasingEnum status) throws DatabaseException;
	void updateRequisitionStatusTurnAttentionDaysAndActive(final RequisitionStatusTurn requisitionStatusTurn) throws DatabaseException;
	boolean getByIdRequistionStatus(final Integer idRequisition, final FlowPurchasingEnum status)throws DatabaseException;
	
}
