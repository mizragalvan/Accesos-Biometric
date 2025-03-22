package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.CheckDocumentable;
import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.enums.GuaranteeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class CheckDocumentDAO implements CheckDocumentable {
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status";
    private static final String WHERE_ID_CHECK_DOCUMENT_EQUALS_ID_CHECK_DOCUMENT =
            "WHERE IdCheckDocument = :IdCheckDocument";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public Integer saveOrUpdate(final CheckDocument checkDocument) throws DatabaseException {
        return checkDocument.getIdCheckDocument() == null ? this.insertCheckDocument(checkDocument) :
            this.updateCheckDocument(checkDocument);
    }

    private Integer insertCheckDocument(final CheckDocument checkDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.NAME, checkDocument.getName());
            source.addValue("Guarantee", checkDocument.getGuarantee().toString());
            source.addValue(TableConstants.ID_GUARANTEE, checkDocument.getIdGuarantee());
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertCheckDocumentQuery(), source, keyHolder, 
                    new String[]{"IdCheckDocument"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private Integer updateCheckDocument(final CheckDocument checkDocument) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("IdCheckDocument", checkDocument.getIdCheckDocument());
            source.addValue(TableConstants.NAME, checkDocument.getName());
            source.addValue("Guarantee", checkDocument.getGuarantee().toString());
            this.namedjdbcTemplate.update(this.buildUpdateCheckDocumentQuery(), source);
            return checkDocument.getIdCheckDocument();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public CheckDocument findById(final Integer idCheckDocument) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idCheckDocument);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<CheckDocument>(CheckDocument.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        }  catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<CheckDocument> findByStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<CheckDocument>(CheckDocument.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<CheckDocument> findByGuaranteeAndStatus(final RecordStatusEnum status,
            final GuaranteeEnum guarantee) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.STATUS, status == null ? null : status.name());
            source.addValue("Guarantee", guarantee == null ? null : guarantee.name());
            return this.namedjdbcTemplate.query(this.buildFindByGuaranteeAndStatusQuery(), source,
                    new BeanPropertyRowMapper<CheckDocument>(CheckDocument.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<CheckDocument> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(),
                    new BeanPropertyRowMapper<CheckDocument>(CheckDocument.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changeStatus(final Integer idCheckDocument, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameter = this.buildChangeStatusNamedParameter(idCheckDocument, status);
            this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameter);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    public void deleteById(final Integer idCheckDocument) {
        final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idCheckDocument);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountbyStatusQuery(), namedParameters, Integer.class);
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }

    private String buildInsertCheckDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO CHECKDOCUMENT (Name, Guarantee, IdGuarantee) ");
        query.append("VALUES (:Name, :Guarantee, :IdGuarantee) ");
        return query.toString();
    }
    
    private String buildUpdateCheckDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE CHECKDOCUMENT SET Name = :Name, Guarantee = :Guarantee ");
        query.append(WHERE_ID_CHECK_DOCUMENT_EQUALS_ID_CHECK_DOCUMENT);
        return query.toString();
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdCheckDocument, Guarantee, Name, Status FROM CHECKDOCUMENT ");
    }
    
    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_CHECK_DOCUMENT_EQUALS_ID_CHECK_DOCUMENT);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idCheckdocument) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CHECK_DOCUMENT, idCheckdocument);
        return namedParameters;
    }
    
    private String buildFindByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private String buildFindByGuaranteeAndStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE (:Status IS NULL OR Status = :Status) AND (:Guarantee IS NULL OR Guarantee = :Guarantee) ");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String findAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
    
    private String buildChangeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE CHECKDOCUMENT SET Status = :Status ");
        query.append(WHERE_ID_CHECK_DOCUMENT_EQUALS_ID_CHECK_DOCUMENT);
        return query.toString();
    }
    
    private MapSqlParameterSource buildChangeStatusNamedParameter(final Integer idCheckDocument,
            final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CHECK_DOCUMENT, idCheckDocument);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM CHECKDOCUMENT ");
        query.append(WHERE_ID_CHECK_DOCUMENT_EQUALS_ID_CHECK_DOCUMENT);
        return query.toString();
    }
    
    private void buildSelectCountAllQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdCheckDocument) FROM CHECKDOCUMENT ");
    }
    
    private String buildCountbyStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountAllQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountAllQuery(query);
        return query.toString();
    }
}
