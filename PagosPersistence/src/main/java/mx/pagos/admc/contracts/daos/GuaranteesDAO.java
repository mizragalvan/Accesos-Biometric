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
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Guaranteesable;
import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.contracts.structures.Guarantees;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class GuaranteesDAO implements Guaranteesable {
    private static final String UPDATE_SET = "UPDATE " + TableConstants.TABLE_GUARANTEE + "SET ";
    private static final String FROM = "FROM " + TableConstants.TABLE_GUARANTEE;
    private static final String WHERE_ID_EQUALS_ID_PARAMETER =
            "WHERE " + TableConstants.ID_GUARANTEE + " = :" + TableConstants.ID_GUARANTEE;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer saveOrUpdate(final Guarantees guarantees) throws DatabaseException {
        return guarantees.getIdGuarantee() == null ? this.insert(guarantees) : this.update(guarantees);
    }
    
    private Integer insert(final Guarantees guarantees) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.NAME, guarantees.getName());
            namedParameters.addValue(TableConstants.PATH, guarantees.getPath());
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, 
                    new String[]{"IdGuarantee"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar", dataAccessException);
        }
    }

    private String buildInsertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_GUARANTEE + QueryConstants.LEFT_BRACES);
        query.append(TableConstants.NAME + QueryConstants.COMMA);
        query.append(TableConstants.PATH + QueryConstants.COMMA);
        query.append(TableConstants.STATUS + QueryConstants.RIGHT_BRACES);        
        query.append(QueryConstants.VALUES);
        query.append(QueryConstants.TAG + TableConstants.NAME + QueryConstants.COMMA);
        query.append(QueryConstants.TAG + TableConstants.PATH + QueryConstants.COMMA); 
        query.append(TableConstants.ACTIVE + QueryConstants.RIGHT_BRACES);       
        return query.toString();
    }
    
    private Integer update(final Guarantees guarantees) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_GUARANTEE, guarantees.getIdGuarantee());
            namedParameters.addValue(TableConstants.NAME, guarantees.getName());
            namedParameters.addValue(TableConstants.PATH, guarantees.getPath());
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return guarantees.getIdGuarantee();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar", dataAccessException);
        }
    }

    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_SET);
        query.append(TableConstants.NAME + QueryConstants.EQUAL_TAG + TableConstants.NAME + QueryConstants.COMMA);
        query.append(TableConstants.PATH + QueryConstants.EQUAL_TAG + TableConstants.PATH + QueryConstants.SPACE);
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }
    
    @Override
    public void changeGuaranteesStatus(final Integer id, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_GUARANTEE, id);
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            this.namedjdbcTemplate.update(this.buildChangeStatusDgaQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar el estatus", dataAccessException);
        }
    }
    
    private String buildChangeStatusDgaQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_SET);
        query.append(TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS + QueryConstants.SPACE);
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }

    @Override
    public List<Guarantees> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildSelectAllQuery(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<Guarantees>(Guarantees.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener todos las garantias", dataAccessException);
        }
    }

    private String buildSelectAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildFindAll(query);
        return query.toString();
    }

    private void buildFindAll(final StringBuilder query) {
        query.append(QueryConstants.SELECT); 
        query.append(TableConstants.ID_GUARANTEE + QueryConstants.COMMA);
        query.append(TableConstants.NAME + QueryConstants.COMMA);
        query.append(TableConstants.PATH + QueryConstants.COMMA);
        query.append(TableConstants.STATUS + QueryConstants.SPACE);        
        query.append(FROM);
    }
    
    @Override
    public Guarantees findById(final Integer id) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_GUARANTEE, id);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(),
                    namedParameters, new BeanPropertyRowMapper<Guarantees>(Guarantees.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al recuperar", exception);
        }
    }
    
    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }


    @Override
    public List<Guarantees> findByStatus(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.buildSelectByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Guarantees>(Guarantees.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener por estatus", dataAccessException);
        }
    }
    
    private String buildSelectByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append(QueryConstants.WHERE + TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }
    
    public void deleteById(final Integer id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_GUARANTEE, id);
        this.namedjdbcTemplate.update(this.buildDeleteQuery(), namedParameters);
    }
    
    private String buildDeleteQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.DELETE_FROM + TableConstants.TABLE_GUARANTEE);
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }
    
    private String buildCountQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(IdGuarantee) ");
        query.append("FROM GUARANTEE");
        return query.toString();
    }
    
    public Integer builtFindAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountQuery(), new MapSqlParameterSource(),
                Integer.class);
    }

    @Override
    public List<CheckDocument> findCheckDocumentListByIdGuarantee(final Integer idGuarantee) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_GUARANTEE, idGuarantee);
            return this.namedjdbcTemplate.query(this.findCheckDocumentListByIdGuaranteeQuery(), source, 
                    new BeanPropertyRowMapper<CheckDocument>(CheckDocument.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findCheckDocumentListByIdGuaranteeQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT(CHECKDOCUMENT.IdCheckDocument), Name, Status, Guarantee, IdGuarantee ");
        builder.append("FROM CHECKDOCUMENT LEFT JOIN REQOWNERSGUARANTEECHECKDOC ");
        builder.append("ON CHECKDOCUMENT.IdCheckDocument = REQOWNERSGUARANTEECHECKDOC.IdCheckDocument ");
        builder.append("WHERE CHECKDOCUMENT.IdGuarantee = :IdGuarantee ");
        return builder.toString();
    }

    @Override
    public List<Guarantees> findAllGuaranteesCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllGuaranteesCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<Guarantees>(Guarantees.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllGuaranteesCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildFindAll(builder);
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
    public Long countTotalItemsToShowOfGuarantees(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllGuaranteesCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
