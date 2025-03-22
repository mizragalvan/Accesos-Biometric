package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.DocumentPersonalityFieldable;
import mx.pagos.admc.contracts.structures.DocumentPersonalityFields;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("DocumentPersonalityFieldsBusiness")
public class DocumentPersonalityFieldsBusiness {
    private static final Logger LOG = Logger.getLogger(DocumentPersonalityFieldsBusiness.class);
    private static final String FIND_DOCUMENT_FIELDS_BY_DOCUMENTTYPE_ERROR = 
            "Hubo un problema al buscar los campos variables por tipo de documento";
    private static final String FIND_DOCUMENT_FIELDS_ERROR = "Hubo un problema al buscar todos los campos variables";
    private static final String SAVE_DOCUMENT_FIELDS_ERROR = "Hubo un problema al guardar un campo variable";
    private static final String DELETE_DOCUMENT_FIELDS_ERROR = "Hubo un problema al eliminar un campo variable";
            
    @Autowired
    private DocumentPersonalityFieldable documentPersonalityFieldable;
    
    public final List<DocumentPersonalityFields> findDocumentFieldsByDocumentType(
            final Integer idDocumentType, final Integer idPersonality) throws BusinessException {
        try {
            return this.documentPersonalityFieldable.findFieldsByDocumentType(idDocumentType, idPersonality);
        } catch (DatabaseException  databaseException) {
            LOG.error(FIND_DOCUMENT_FIELDS_BY_DOCUMENTTYPE_ERROR, databaseException);
            throw new BusinessException(FIND_DOCUMENT_FIELDS_BY_DOCUMENTTYPE_ERROR, databaseException);
        }
    }

    public final List<String> sectionsToShowByDocument(final Integer idDocumentType, final Integer idPersonality) 
            throws BusinessException {
        try {
            return this.documentPersonalityFieldable.sectionsToShowByDocument(idDocumentType, idPersonality);
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_DOCUMENT_FIELDS_BY_DOCUMENTTYPE_ERROR, databaseException);
            throw new BusinessException(FIND_DOCUMENT_FIELDS_BY_DOCUMENTTYPE_ERROR, databaseException);
        }
    }
    
    public final List<DocumentPersonalityFields> findAllDocumentFields() throws BusinessException {
        try {
            return this.documentPersonalityFieldable.findAllDocumentTypeFields();
        } catch (DatabaseException  databaseException) {
            LOG.error(FIND_DOCUMENT_FIELDS_ERROR, databaseException);
            throw new BusinessException(FIND_DOCUMENT_FIELDS_ERROR, databaseException);
        }
    }
    
    public final void saveDocumentTypeField(final DocumentPersonalityFields documentPersonalityFields) 
            throws BusinessException {
        try {
            this.documentPersonalityFieldable.saveDocumentTypeField(documentPersonalityFields);
        } catch (DatabaseException databaseException) {
            LOG.error(SAVE_DOCUMENT_FIELDS_ERROR, databaseException);
            throw new BusinessException(SAVE_DOCUMENT_FIELDS_ERROR, databaseException);
        }
    }

    public final void deleteDocumentTypeField(final Integer idDocumentType) throws BusinessException {
        try {
            this.documentPersonalityFieldable.deleteDocumentTypeField(idDocumentType);
        } catch (DatabaseException databaseException) {
            LOG.error(DELETE_DOCUMENT_FIELDS_ERROR, databaseException);
            throw new BusinessException(DELETE_DOCUMENT_FIELDS_ERROR, databaseException);
        }
    }
    
    public final void saveDocumentTypeFiedsList(final List<DocumentPersonalityFields> fieldsList) 
            throws BusinessException {
        this.deleteDocumentTypeField(fieldsList.get(0).getIdDocumentType());
        for (final DocumentPersonalityFields documentPersonalityFields : fieldsList) 
            this.saveDocumentTypeField(documentPersonalityFields);
    }
}
