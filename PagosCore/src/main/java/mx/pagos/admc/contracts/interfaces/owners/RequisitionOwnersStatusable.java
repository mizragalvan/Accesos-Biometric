package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.StatusBranch;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface RequisitionOwnersStatusable {
    void insertStatus(StatusBranch statusBranch) throws DatabaseException;

    Boolean statusExists(StatusBranch statusBranch) throws DatabaseException;

    void deleteStatus(StatusBranch statusBranch) throws DatabaseException;

    Boolean isBranchToBeEnded(StatusBranch statusBranch) throws DatabaseException;

    void deleteStatusByBranch(StatusBranch statusBranch) throws DatabaseException;

    List<StatusBranch> findStatusById(Integer idRequisitionOwners) throws DatabaseException;

    Boolean isStatusAsigned(StatusBranch statusBranch) throws DatabaseException;

    void saveRequisitionStatusTurn(Integer idRequisitionOwners, FlowPurchasingEnum status) throws DatabaseException;

    Integer findCurrentTurnByIdRequisitionAndStatus(Integer idRequisitionOwners,
            FlowPurchasingEnum status) throws DatabaseException;

    void saveRequisitionStatusTurnAttentionDays(RequisitionStatusTurn requisitionStatusTurn) throws DatabaseException;

    void deleteAllStatusByIdRequisition(Integer idRequisitionOwners) throws DatabaseException;
}
