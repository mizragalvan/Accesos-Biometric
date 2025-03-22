package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.ManagerialDocumentType;
import mx.pagos.admc.enums.ManagerialDocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface ManagerialDocumentTypeable {

    Integer save(ManagerialDocumentType managerialDocumentType) throws DatabaseException;
    
    Integer update(ManagerialDocumentType managerialDocumentType) throws DatabaseException;
    
    List<ManagerialDocumentType> findAll() throws DatabaseException;
    
    List<ManagerialDocumentType> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    ManagerialDocumentType findById(Integer idManagerialDocumenType) throws DatabaseException;
    
    void changeStatus(Integer idManagerialDocumenType, RecordStatusEnum status) throws DatabaseException;
    
    List<ManagerialDocumentType> findManagerialDocumentByDocumentType(ManagerialDocumentTypeEnum documentType) 
            throws DatabaseException;
    
    List<ManagerialDocumentType> findAllManagerialDocumentTypeCatalogPaged(
            RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfManagerialDocumentType(RecordStatusEnum status) throws DatabaseException;
}
