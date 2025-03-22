package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.admc.contracts.structures.DocumentType;
import mx.pagos.admc.enums.DocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface DocumentTypeable {
    Integer saveOrUpdate(final DocumentType documentType) throws DatabaseException;

    void changeDocumentTypeStatus(final Integer idDocumentType, final RecordStatusEnum status) 
            throws DatabaseException;

    DocumentType findById(Integer idDocumentType) throws DatabaseException;
    CatDocumentType findByIdCatDocument(Integer idDocumentType) throws DatabaseException;

    List<DocumentType> findAll() throws DatabaseException;

    List<DocumentType> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;

    Boolean nameExists(String name) throws DatabaseException;

    List<DocumentType> findDocumentTypeStatusAndDocumentType(
    		DocumentType documentType, DocumentTypeEnum documentTypeEnum) throws DatabaseException;
    
    List<DocumentType> findAllDocumentTypeCatalogPaged(DocumentType documentType,  DocumentTypeEnum documentTypeEnum, 
            Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfDocumentType(DocumentType documentType, DocumentTypeEnum documentTypeEnum) 
            throws DatabaseException;
}