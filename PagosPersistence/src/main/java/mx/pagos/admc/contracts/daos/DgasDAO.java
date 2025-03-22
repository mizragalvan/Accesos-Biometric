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
import mx.pagos.admc.contracts.interfaces.Dgable;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

/**
 * 
 * @author Mizraim
 */
@Repository
public class DgasDAO implements Dgable {
    private static final String UPDATE_SET = "UPDATE " + TableConstants.TABLE_DGA + "SET ";
    private static final String FROM = "FROM " + TableConstants.TABLE_DGA;
    private static final String WHERE_ID_EQUALS_ID_PARAMETER =
            "WHERE " + TableConstants.ID_DGA + " = :" + TableConstants.ID_DGA;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer saveOrUpdate(final Dga dga) throws DatabaseException {
        return dga.getIdDga() == null ? this.insert(dga) : this.update(dga);
    }

    private Integer insert(final Dga dga) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.NAME, dga.getName());
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, new String[]{"IdDga"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar", dataAccessException);
        }
    }

    private String buildInsertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO " + TableConstants.TABLE_DGA);
        query.append("(Name, Status)");
        query.append("VALUES ");
        query.append("(:Name, 'ACTIVE')");
        return query.toString();
    }
    
    private Integer update(final Dga dga) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_DGA, dga.getIdDga());
            namedParameters.addValue(TableConstants.NAME, dga.getName());
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return dga.getIdDga();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar", dataAccessException);
        }
    }

    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_SET);
        query.append("Name = :Name ");
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }

    @Override
    public void changeDgaStatus(final Integer id, final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_DGA, id);
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            this.namedjdbcTemplate.update(this.buildChangeStatusDgaQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar el estatus", dataAccessException);
        }
    }

    private String buildChangeStatusDgaQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_SET);
        query.append("Status = :Status ");
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }

    @Override
    public List<Dga> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildSelectAllQuery(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<Dga>(Dga.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener todos los DGAs", dataAccessException);
        }
    }
    
    private String buildSelectAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAll(query);
        return query.toString();
    }
    
    private void buildSelectAll(final StringBuilder query) {
        query.append("SELECT IdDga, Name, Status ");
        query.append(FROM);
    }
    
    @Override
    public Dga findById(final Integer id) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_DGA, id);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(),
                    namedParameters, new BeanPropertyRowMapper<Dga>(Dga.class));
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
    public List<Dga> findByStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createCountByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildSelectByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Dga>(Dga.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener por estatus", dataAccessException);
        }
    }
    
    private String buildSelectByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append("WHERE Status = :Status");
        return query.toString();
    }
    
    public void deleteById(final Integer id) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_DGA, id);
        this.namedjdbcTemplate.update(this.buildDeleteQuery(), namedParameters);
    }

    private String buildDeleteQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE " + FROM);
        query.append(WHERE_ID_EQUALS_ID_PARAMETER);
        return query.toString();
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }

    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.SELECT_COUNT + TableConstants.ID_DGA + QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.FROM + TableConstants.TABLE_DGA);
        return query.toString();
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createCountByStatusNamedParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(), namedParameters, Integer.class);
    }

    private String buildCountByStatusQuery() {
        final StringBuilder query = new StringBuilder(this.buildCountAllQuery());
        query.append(QueryConstants.WHERE + TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }

    private MapSqlParameterSource createCountByStatusNamedParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }

    @Override
    public List<Dga> findAllDgaCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllDgaCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, new BeanPropertyRowMapper<Dga>(Dga.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllDgaCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildSelectAll(builder);
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
    public Long countTotalItemsToShowOfDga(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllDgaCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildExistsDGA(String name) {
        final StringBuilder query = new StringBuilder();
        query.append(buildCountAllQuery());
        query.append(QueryConstants.SPACE);
        query.append(QueryConstants.WHERE);
        query.append(QueryConstants.SPACE);
        query.append(TableConstants.NAME);
        query.append(QueryConstants.EQUAL);
        query.append(":Name");
        return query.toString();
    }

	@Override
	public boolean ExistsDGAByName(String name) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.NAME, name);			
			return this.namedjdbcTemplate.queryForObject(buildExistsDGA(name), source,Boolean.class) ;
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
}
