package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Alert;
import mx.pagos.admc.contracts.structures.AlertConfigurationDay;
import mx.pagos.admc.contracts.structures.AlertDocumentType;
import mx.pagos.admc.contracts.structures.AlertFlowStep;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface Alertable {
	Integer saveOrUpdate(final Alert alert) throws DatabaseException;
	
	Integer insertAlert(final Alert alert) throws DatabaseException;
	    
	Integer updateAlert(final Alert alert) throws DatabaseException;
	
	void deleteAlert(final Alert alert) throws DatabaseException;

	List<Alert> findAll() throws DatabaseException;

	List<Alert> findbyFlowStatus(final Alert alert) throws DatabaseException;

	Alert findById(Integer idAlert) throws DatabaseException, EmptyResultException;

    Integer saveAlertconfigurationDay(AlertConfigurationDay alertConfigurationDay) throws DatabaseException;

    List<AlertConfigurationDay> findAlertConfigurationDaysByIdAlert(Integer idAlert) throws DatabaseException;

    void deleteAlertConfigurationDaysByIdAlert(Integer idAlert) throws DatabaseException;
    
    List<AlertFlowStep> getEmailsToAlertsByStep(Integer idRequisition, FlowPurchasingEnum flowStatus)
            throws DatabaseException;

    List<AlertFlowStep> getOwnersEmailsToAlertByStep(Integer idRequisitionOwner, FlowPurchasingEnum flowStatus) 
            throws DatabaseException;
    
    Integer findValidityDaysByRequisitionFlowStatusTurn(Integer idRequisition,
            FlowPurchasingEnum status, Integer turn) throws DatabaseException;
    
    Integer findValidityDaysByFlowStatusTurn(Integer idFlow,
            FlowPurchasingEnum status, Integer turn)throws DatabaseException;
    
    List<Alert> findAlerts() throws DatabaseException;
    
    List<Alert> findUnderDirectorMailSendAlert(List<Integer> idRequisitionList) throws DatabaseException;

    void saveDocumentType(Integer idAlert, Integer idDocumentType) throws DatabaseException;

    void deleteDocumentTypesByIdAlert(Integer idAlert) throws DatabaseException;

    List<Integer> findActiveDocumentTypesByIdAlert(Integer idAlert) throws DatabaseException;
    
    List<Alert> findOwnersServiceLevelsAlerts() throws DatabaseException;
    
    List<Alert> findRequisitionOwnersUnderDirectorMail(List<Integer> idRequisitionOwnersList) throws DatabaseException;

    List<AlertDocumentType> findAlertConflicts(Alert alert) throws DatabaseException;
    
    List<RequisitionOwners> findContractsToExpireForAlerts(RequisitionOwners requisitionOwners)
            throws DatabaseException;
}
