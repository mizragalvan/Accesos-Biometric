package mx.pagos.document.versioning.interfaces;

import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.document.versioning.structures.Document;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface Documentable {
    Integer saveOrUpdate(Document document) throws DatabaseException;
    
    void changeDocumentStatus(Integer idDocument, RecordStatusEnum status) throws DatabaseException;
    
    Document findByIdDocument(Integer idDocument) throws DatabaseException, EmptyResultException;

    void deleteById(Integer idDocument) throws DatabaseException;
}
