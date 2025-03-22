package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.enums.GuaranteeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface CheckDocumentable {
    Integer saveOrUpdate(CheckDocument checkDocument) throws DatabaseException;
    
    CheckDocument findById(Integer idCheckDocument) throws DatabaseException, EmptyResultException;
    
    List<CheckDocument> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    List<CheckDocument> findByGuaranteeAndStatus(RecordStatusEnum status, GuaranteeEnum guarantee)
            throws DatabaseException;
    
    List<CheckDocument> findAll() throws DatabaseException;
    
    void changeStatus(final Integer idCheckDocument, RecordStatusEnum status) throws DatabaseException;
}
