package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface CheckDocumentationable {
    
    Integer saveOrUpdate(CheckDocumentation checkDocumentation) throws DatabaseException;
    
    void changeStatus(Integer idCheckDocumentation, RecordStatusEnum status) throws DatabaseException;
    
    List<CheckDocumentation> findCheckDocumentationByCategory(Integer idCategory) throws DatabaseException;
    
    List<CheckDocumentation> findAll() throws DatabaseException;
    
    List<CheckDocumentation> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    CheckDocumentation findById(Integer idCheckDocumentation) throws DatabaseException;
    
    List<CheckDocumentation> findAllCheckDocumentationCatalogPaged(
            RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfCheckDocumentation(RecordStatusEnum status) throws DatabaseException;
}
