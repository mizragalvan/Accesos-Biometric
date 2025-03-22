package mx.pagos.admc.contracts.business;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.pagos.admc.contracts.interfaces.RequisitionTrackable;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class RequisitionStatusTurnBusiness {

	private static final Logger LOG = Logger.getLogger(RequisitionStatusTurnBusiness.class);
	private static final String ERROR = "Error al obtener las Requisiciones por estatus";
	
	@Autowired
	RequisitionTrackable requisitionTrackable;

	@Autowired
	RequisitionBusiness requisitionBusiness;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public RequisitionStatusTurn validActiveByIdRequisitionAndStatus(RequisitionStatusTurn requisition) throws BusinessException {
		try {

			final Boolean active = this.requisitionTrackable.getActiveByIdRequistionStatus(requisition.getIdRequisition(), requisition.getStatus());
			if (!active) {
				Integer turn = this.requisitionBusiness.findCurrentTurnByIdRequisition(requisition.getIdRequisition());
				Integer days = this.requisitionBusiness.getRequisitionTurnAttentiondays(requisition.getIdRequisition(),
						requisition.getStatus(), turn);
				requisition.setTurn(turn != null ? turn : 0);
				requisition.setAttentionDays(days != null ? days : 0);
				this.requisitionTrackable.updateRequisitionStatusTurnAttentionDaysAndActive(requisition);
				return requisition;
			}
			return null;
		} catch (DatabaseException databaseException) {
			LOG.error(ERROR, databaseException);
			throw new BusinessException(ERROR, databaseException);
		}
	}
	
	public boolean validByIdRequisitionAndStatus(Integer idRequisition, FlowPurchasingEnum status) throws BusinessException {
		try {
			return this.requisitionTrackable.getByIdRequistionStatus(idRequisition, status);
		} catch (DatabaseException databaseException) {
			LOG.error(ERROR, databaseException);
			throw new BusinessException(ERROR, databaseException);
		}
	}

}
