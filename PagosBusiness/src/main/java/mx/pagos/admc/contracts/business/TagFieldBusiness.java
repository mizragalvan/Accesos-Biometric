package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.TagFieldable;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagFieldBusiness {
    private static final String MESSAGE_RETRIEVING_TAG_FIELDS_ERROR =
            "Hubo un problema al recuperar todas las campos por etiqueta";
    private static final String MESSAGE_RETRIEVING_TAG_FIELDS_BY_TABLE_ERROR =
            "Hubo un problema al recuperar todas las campos por etiqueta por tipo de objeto";

    private static final Logger LOG = Logger.getLogger(TagFieldBusiness.class);


    @Autowired
    private TagFieldable tagFieldable;
    
    public final List<TagField> findAll() throws BusinessException {
        try {
            return this.tagFieldable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_TAG_FIELDS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_TAG_FIELDS_ERROR, databaseException);
        }
    }
    
    public final List<TagField> findByTable(final String table) throws BusinessException {
        try {
            return this.tagFieldable.findByTable(table);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_TAG_FIELDS_BY_TABLE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_TAG_FIELDS_BY_TABLE_ERROR, databaseException);
        }
    }
}
