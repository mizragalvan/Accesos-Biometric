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
import mx.pagos.admc.contracts.interfaces.CheckDocumentationable;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class CheckDocumentationDAO implements CheckDocumentationable {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    

    public Integer saveOrUpdate(final CheckDocumentation checkDocumentation) throws DatabaseException {
        return checkDocumentation.getIdCheckDocumentation() == null ?
                this.insertCheckDocumentation(checkDocumentation) : this.updateCheckDocumentation(checkDocumentation);
    }

    private Integer insertCheckDocumentation(final CheckDocumentation checkDocumentation) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(checkDocumentation);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, 
                    new String[]{"IdCheckDocumentation"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException); 
        }
    }

    private Integer updateCheckDocumentation(final CheckDocumentation checkDocumentation) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(checkDocumentation);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return checkDocumentation.getIdCheckDocumentation();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException); 
        }
    }

    @Override
    public List<CheckDocumentation> findCheckDocumentationByCategory(final Integer idCategory) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_CATEGORY, idCategory);
            return this.namedjdbcTemplate.query(this.findCheckDocumentationByCategoryQuery(), parameterSource, 
                    new BeanPropertyRowMapper<CheckDocumentation>(CheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException); 
        }
    }


    @Override
    public void changeStatus(final Integer idCheckDocumentation, final RecordStatusEnum status) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CHECK_DOCUMENTATION, idCheckDocumentation);
            source.addValue(TableConstants.STATUS, status.toString());
            this.namedjdbcTemplate.update(this.changeStatusQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String changeStatusQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UPDATE CHECKDOCUMENTATION SET ");
        builder.append("Status = :Status ");
        builder.append("WHERE IdCheckDocumentation = :IdCheckDocumentation ");
        return builder.toString();
    }

    @Override
    public List<CheckDocumentation> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(),
                    new BeanPropertyRowMapper<CheckDocumentation>(CheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findAllQuery() {
        final StringBuilder builder = new StringBuilder();
        this.findAllFieldsQuery(builder);
        return builder.toString();
    }
    
    private void findAllFieldsQuery(final StringBuilder builder) {
        builder.append("SELECT IdCheckDocumentation, Name, Status ");
        builder.append("FROM CHECKDOCUMENTATION ");
    }
    
    @Override
    public List<CheckDocumentation> findByStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.findByStatusQuery(), source, 
                    new BeanPropertyRowMapper<CheckDocumentation>(CheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findByStatusQuery() {
        final StringBuilder builder = new StringBuilder();
        this.findAllFieldsQuery(builder);
        builder.append("WHERE Status = :Status ");
        return builder.toString();
    }

    public void deleteById(final Integer idCheckDocumentation) {
        final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idCheckDocumentation);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }

    private String buildInsertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO CHECKDOCUMENTATION (Name) VALUES (:Name)");
        return query.toString();
    }

    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE CHECKDOCUMENTATION SET Name = :Name WHERE IdCheckDocumentation = :IdCheckDocumentation");
        return query.toString();
    }

    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idCheckDocumentation) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CHECK_DOCUMENTATION, idCheckDocumentation);
        return namedParameters;
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM CHECKDOCUMENTATION WHERE IdCheckDocumentation = :IdCheckDocumentation");
        return query.toString();
    }

    private String findCheckDocumentationByCategoryQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdCategoryCheckDocumentation, CHECKDOCUMENTATION.IdCheckDocumentation, ");
        builder.append("CHECKDOCUMENTATION.Name, CHECKDOCUMENTATION.Status FROM CATEGOYCHECKDOCUMENTATION ");
        builder.append("INNER JOIN CHECKDOCUMENTATION ON ");
        builder.append("CATEGOYCHECKDOCUMENTATION.IdCheckDocumentation = CHECKDOCUMENTATION.IdCheckDocumentation ");
        builder.append("WHERE CATEGOYCHECKDOCUMENTATION.IdCategory = :IdCategory "); 
        builder.append("AND CHECKDOCUMENTATION.STATUS = 'ACTIVE' ");
        return builder.toString();
    }

    @Override
    public CheckDocumentation findById(final Integer idCheckDocumentation) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_CHECK_DOCUMENTATION, idCheckDocumentation);
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(), source, 
                    new BeanPropertyRowMapper<CheckDocumentation>(CheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findByIdQuery() {
        final StringBuilder builder = new StringBuilder();
        this.findAllFieldsQuery(builder);
        builder.append("WHERE IdCheckDocumentation = :IdCheckDocumentation");
        return builder.toString();
    }

    @Override
    public List<CheckDocumentation> findAllCheckDocumentationCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_PUBLIC_FIGURE,
                    this.findAllCheckDocumentationCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<CheckDocumentation>(CheckDocumentation.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllCheckDocumentationCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.findAllFieldsQuery(builder);
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
    public Long countTotalItemsToShowOfCheckDocumentation(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = 
                    this.databaseUtils.countTotalRows(this.findAllCheckDocumentationCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
