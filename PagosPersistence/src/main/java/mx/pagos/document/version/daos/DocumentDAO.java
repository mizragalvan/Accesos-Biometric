package mx.pagos.document.version.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.document.version.daos.constants.TableVersionConstants;
import mx.pagos.document.versioning.interfaces.Documentable;
import mx.pagos.document.versioning.structures.Document;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * @author Mizraim
 */

@Repository
public class DocumentDAO implements Documentable {
    private static final String WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT = "WHERE IdDocument = :IdDocument";
    @Autowired
     private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public Integer saveOrUpdate(final Document document) throws DatabaseException {
        return document.getIdDocument() == null ? this.insert(document) :
            this.update(document);
    }

    private Integer update(final Document document) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.insertNamedParameters(document);
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return document.getIdDocument();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private Integer insert(final Document document) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.insertNamedParameters(document);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, 
                    new String[]{"IdDocument"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changeDocumentStatus(final Integer idDocument, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createChangeDocumentStatusNamedParameters(
                    idDocument, status);
            this.namedjdbcTemplate.update(this.changeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Document findByIdDocument(final Integer idDocument) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFiandByIdNamedParameters(idDocument);
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<Document>(Document.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void deleteById(final Integer idDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFiandByIdNamedParameters(idDocument);
            this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource createFiandByIdNamedParameters(
            final Integer idDocument) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableVersionConstants.ID_DOCUMENT, idDocument);
        return namedParameters;
    }
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM DOCUMENT ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }
    
    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE DOCUMENT SET CurrentVersion = :CurrentVersion ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }
    
    private String buildInsertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO DOCUMENT(CurrentVersion) ");
        query.append("VALUES (:CurrentVersion)");
        return query.toString();
    }
    
    private MapSqlParameterSource insertNamedParameters(final Document document) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableVersionConstants.ID_DOCUMENT, document.getIdDocument());
        namedParameters.addValue(TableVersionConstants.CURRENT_VERSION, document.getCurrentVersion());
        return namedParameters;
    }
    
    private String changeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE DOCUMENT SET Status = :Status ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }
    
    private MapSqlParameterSource createChangeDocumentStatusNamedParameters(
            final Integer idDocument, final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableVersionConstants.ID_DOCUMENT, idDocument);
        namedParameters.addValue(TableVersionConstants.STATUS, status.toString());
        return namedParameters;
    }

    private String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdDocument, CurrentVersion, Status FROM DOCUMENT ");
        query.append(WHERE_ID_DOCUMENT_EQUALS_ID_DOCUMENT);
        return query.toString();
    }
}
