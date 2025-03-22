package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.ManagerialDocumentTypeable;
import mx.pagos.admc.contracts.structures.ManagerialDocumentType;
import mx.pagos.admc.enums.ManagerialDocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class ManagerialDocumentTypeDAO implements ManagerialDocumentTypeable {
    private static final String FROM_MANAGERIALDOCUMENTTYPE = "FROM MANAGERIALDOCUMENTTYPE ";
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status ";
    private static final String UPDATE_SET = "UPDATE MANAGERIALDOCUMENTTYPE SET ";
    private static final String WHERE_ID_EQUALS_ID = "WHERE IdManagerialDocumentType = :IdManagerialDocumentType ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer save(final ManagerialDocumentType managerialDocumentType) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource nameSource = 
                    new BeanPropertySqlParameterSource(managerialDocumentType);
            nameSource.registerSqlType(TableConstants.DOCUMENT_TYPE, Types.VARCHAR);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.insertManagerialDocumentQuery(), nameSource, keyHolder, 
                    new String[]{"IdManagerialDocumentType"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String insertManagerialDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO MANAGERIALDOCUMENTTYPE(Name, Url, DocumentType) ");
        query.append("VALUES(:Name, :Url, :DocumentType) ");
        return query.toString();
    }
    
    @Override
    public Integer update(final ManagerialDocumentType managerialDocumentType) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource nameSource = 
                    new BeanPropertySqlParameterSource(managerialDocumentType); 
            nameSource.registerSqlType(TableConstants.DOCUMENT_TYPE, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.updateManagerialDocumentTypeQuery(), nameSource);
            return managerialDocumentType.getIdManagerialDocumentType();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String updateManagerialDocumentTypeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_SET);
        query.append("Name = :Name, Url = :Url, DocumentType = :DocumentType ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    @Override
    public List<ManagerialDocumentType> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(), 
                    new BeanPropertyRowMapper<>(ManagerialDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.allFieldsQuery(query);
        return query.toString();
    }

    private void allFieldsQuery(final StringBuilder query) {
        query.append("SELECT IdManagerialDocumentType, Name, Url, Status, DocumentType ");
        query.append(FROM_MANAGERIALDOCUMENTTYPE);
    }
    
    @Override
    public List<ManagerialDocumentType> findByStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.findByStatusQuery(), parameterSource, 
                    new BeanPropertyRowMapper<>(ManagerialDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.allFieldsQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }

    @Override
    public ManagerialDocumentType findById(final Integer idManagerialDocumenType) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_MANAGERIAL_DOCUMENT, idManagerialDocumenType);
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(), parameterSource,
                    new BeanPropertyRowMapper<>(ManagerialDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.allFieldsQuery(query);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    @Override
    public void changeStatus(final Integer idManagerialDocumenType, final RecordStatusEnum status) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(TableConstants.ID_MANAGERIAL_DOCUMENT, idManagerialDocumenType);
            paramMap.addValue(TableConstants.STATUS, status.toString());
            this.namedjdbcTemplate.update(this.changeStatusQuery(), paramMap);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String changeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_SET);
        query.append("Status = :Status ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    public void deleteById(final Integer idManagerialDocumentType) throws DatabaseException {
        try {
            final MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(TableConstants.ID_MANAGERIAL_DOCUMENT, idManagerialDocumentType);
            this.namedjdbcTemplate.update(this.deleteByIdQuery(), paramMap);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String deleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM MANAGERIALDOCUMENTTYPE ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    public Integer countAllRecords() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }
    
    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        return query.toString();
    }

    private void buildCountQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdManagerialDocumentType) ");
        query.append(FROM_MANAGERIALDOCUMENTTYPE);
    }
    
    public Integer countRecordsByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.STATUS, status.toString());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByStatusQuery(), parameterSource, Integer.class);
    }
    
    private String countRecordsByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }

    @Override
    public List<ManagerialDocumentType> findManagerialDocumentByDocumentType(
            final ManagerialDocumentTypeEnum documentType) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.MANAGERIAL_DOCUMENT_TYPE, documentType.toString());
            return this.namedjdbcTemplate.query(this.findManagerialDocumentByDocumentType(), parameterSource, 
                    new BeanPropertyRowMapper<ManagerialDocumentType>(ManagerialDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findManagerialDocumentByDocumentType() {
        final StringBuilder builder = new StringBuilder();
        this.allFieldsQuery(builder);
        builder.append("WHERE DocumentType = :DocumentType AND Status = 'ACTIVE' ");
        return builder.toString();
    }

    @Override
    public List<ManagerialDocumentType> findAllManagerialDocumentTypeCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllManagerialDocumentTypeCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<ManagerialDocumentType>(ManagerialDocumentType.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllManagerialDocumentTypeCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.allFieldsQuery(builder);
        builder.append("WHERE :Status IS NULL OR Status = :Status ");
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }

    private MapSqlParameterSource statusParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        return source;
    }

    @Override
    public Long countTotalItemsToShowOfManagerialDocumentType(
            final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = 
                    this.databaseUtils.countTotalRows(this.findAllManagerialDocumentTypeCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
