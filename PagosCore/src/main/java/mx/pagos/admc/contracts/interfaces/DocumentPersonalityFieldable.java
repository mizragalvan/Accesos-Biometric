package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.DocumentPersonalityFields;
import mx.pagos.general.exceptions.DatabaseException;

public interface DocumentPersonalityFieldable {

    List<DocumentPersonalityFields> findFieldsByDocumentType(Integer idDocumentType, Integer idPersonality) 
            throws DatabaseException;
    
    void saveDocumentTypeField(DocumentPersonalityFields documentPersonalityFields) throws DatabaseException;
    
    void deleteDocumentTypeField(Integer idDocumentType) throws DatabaseException;
    
    List<DocumentPersonalityFields> findAllDocumentTypeFields() throws DatabaseException;
    
    List<String> sectionsToShowByDocument(Integer idDocumentType, Integer idPersonality) throws DatabaseException;
    
}
