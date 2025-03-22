package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.general.exceptions.DatabaseException;

public interface AutomaticRenewable {
    void automaticRenewContracts() throws DatabaseException;
    
    List<Integer> findRenewableRequisitions() throws DatabaseException;
    
    void updateToRenewedStatus(List<Integer> idRequisitionsList) throws DatabaseException;

    void automaticUpdateToRenewedStatus() throws DatabaseException;

    List<Requisition> findRenewedRequisitions() throws DatabaseException;
}
