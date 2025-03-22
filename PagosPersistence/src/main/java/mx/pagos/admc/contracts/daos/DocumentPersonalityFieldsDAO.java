package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DocumentPersonalityFieldable;
import mx.pagos.admc.contracts.structures.DocumentPersonalityFields;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class DocumentPersonalityFieldsDAO implements DocumentPersonalityFieldable {
    private static final String WHERE = "WHERE IdDocumentType = :IdDocumentType AND IdPersonality = :IdPersonality ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public List<DocumentPersonalityFields> findFieldsByDocumentType(
            final Integer idDocumentType, final Integer idPersonality) throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findFieldsByDocumentTypeQuery(), 
                    this.documentPersonalityParameters(idDocumentType, idPersonality), 
                    new BeanPropertyRowMapper<DocumentPersonalityFields>(DocumentPersonalityFields.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource documentPersonalityParameters(
            final Integer idDocumentType, final Integer idPersonality) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.ID_DOCUMENT_TYPE, idDocumentType);
        source.addValue(TableConstants.ID_PERSONALITY, idPersonality);
        return source;
    }

    private String findFieldsByDocumentTypeQuery() {
        final StringBuilder query = new StringBuilder();
        this.selectAllQuery(query);
        query.append(WHERE);
        return query.toString();
    }
    
    @Override
    public void saveDocumentTypeField(
            final DocumentPersonalityFields documentPersonalityFields) throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.saveDocumentTypeFieldQuery(),
                    new BeanPropertySqlParameterSource(documentPersonalityFields));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }        
    }
    
    private String saveDocumentTypeFieldQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO DOCUMENTPERSONALITYFIELDS(IdDocumentType, FieldName, IdPersonality) ");
        query.append("VALUES(:IdDocumentType, :FieldName, :IdPersonality)");
        return query.toString();
    }

    @Override
    public void deleteDocumentTypeField(final Integer idDocumentType) throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.deleteDocumentTypeFieldQuery(), 
                    new MapSqlParameterSource(TableConstants.ID_DOCUMENT_TYPE, idDocumentType));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }        
    }
    
    private String deleteDocumentTypeFieldQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM DOCUMENTPERSONALITYFIELDS ");
        query.append("WHERE IdDocumentType = :IdDocumentType ");
        return query.toString();
    }

    @Override
    public List<DocumentPersonalityFields> findAllDocumentTypeFields() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllDocumentTypeFieldsQuery(), 
                    new BeanPropertyRowMapper<DocumentPersonalityFields>(DocumentPersonalityFields.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }    
    }
    
    private String findAllDocumentTypeFieldsQuery() {
        final StringBuilder query = new StringBuilder();
        this.selectAllQuery(query);
        return query.toString();
    }

    private void selectAllQuery(final StringBuilder query) {
        query.append("SELECT DPF.FieldName, SectionName ");
        this.whereClause(query);
    }

    private void whereClause(final StringBuilder query) {
        query.append("FROM DOCUMENTPERSONALITYFIELDS AS DPF INNER JOIN FIELDS ON ");
        query.append("DPF.FieldName = FIELDS.FieldName ");
    }

    @Override
    public List<String> sectionsToShowByDocument(final Integer idDocumentType, 
            final Integer idPersonality) throws DatabaseException {
        try {
            return this.namedjdbcTemplate.queryForList(this.sectionsToShowByDocument(), 
                    this.documentPersonalityParameters(idDocumentType, idPersonality), String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String sectionsToShowByDocument() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT SectionName ");
        this.whereClause(builder);
        builder.append(WHERE);
        return builder.toString();
    }
}
