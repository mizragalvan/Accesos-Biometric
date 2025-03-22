package mx.pagos.admc.contracts.daos;

import static mx.pagos.admc.contracts.constants.TableConstants.ID_POSITION;
import static mx.pagos.admc.contracts.constants.TableConstants.TABLE_POSITION;
import static mx.pagos.general.constants.QueryConstants.ASTERISK;
import static mx.pagos.general.constants.QueryConstants.EQUAL_TAG;
import static mx.pagos.general.constants.QueryConstants.FROM;
import static mx.pagos.general.constants.QueryConstants.SELECT;
import static mx.pagos.general.constants.QueryConstants.WHERE;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Positionable;
import mx.pagos.admc.contracts.structures.Positions;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class PositionDAO implements Positionable {
    private static final String UPDATE_POSITION = "UPDATE POSITION SET ";
    private static final String WHERE_STATUS_EQUALS = "WHERE Status = :Status";
    private static final String FROM_POSITION = "FROM POSITION ";
    @Autowired
     private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer saveOrUpdate(final Positions position) throws DatabaseException {
        return position.getIdPosition() == null ? this.insertPosition(position) : this.updatePosition(position);
    }

    private Integer insertPosition(final Positions position) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(position);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertPositionQuery(), namedParameters, keyHolder, 
                    new String[]{"IdPosition"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar el puesto", dataAccessException);
        }
    }

    private String buildInsertPositionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO POSITION(Name) ");
        query.append("VALUES(:Name)");
        return query.toString();
    }

    private Integer updatePosition(final Positions position) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(position);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildUpdatePositionQuery(), namedParameters);
            return position.getIdPosition();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar el puesto", dataAccessException);
        }
    }
    
    private String buildUpdatePositionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_POSITION);
        query.append("Name = :Name ");
        query.append("WHERE IdPosition = :IdPosition");
        return query.toString();
    }

    @Override
    public List<Positions> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildSelectAllQuery(), 
                    new BeanPropertyRowMapper<Positions>(Positions.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener todos los puestos", dataAccessException);
        }
    }

    @Override
    public Positions findPositionByIdPosition(final Integer idPosition) throws DatabaseException {
        try {
            final Positions position = new Positions(idPosition);
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(position);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,  
                    new BeanPropertyRowMapper<Positions>(Positions.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al obtener el puesto", exception);
        }
    }

    private String buildSelectAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(SELECT).append(ASTERISK);
        query.append(FROM).append(TABLE_POSITION);
        return query.toString();
    }

    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildSelectAllQuery());
        query.append(WHERE).append(ID_POSITION).append(EQUAL_TAG).append(ID_POSITION);
        return query.toString();
    }

    @Override
    public void changePositionStatus(final Integer idPosition, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.changePositionStatusNamedParameters(idPosition, status);
            this.namedjdbcTemplate.update(this.buildChangePositionStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar el estatus del puesto", dataAccessException);
        }
    }
    
    private MapSqlParameterSource changePositionStatusNamedParameters(final Integer idPosition,
            final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_POSITION, idPosition);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildChangePositionStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_POSITION);
        query.append("Status = :Status ");
        query.append(WHERE).append(ID_POSITION).append(EQUAL_TAG).append(ID_POSITION);
        return query.toString();
    }

    @Override
    public List<Positions> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.buildFindByRecordStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Positions>(Positions.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener puestos por estatus", dataAccessException);
        }
    }
    
    private String buildFindByRecordStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdPosition, Name, Status ");
        query.append(FROM_POSITION);
        query.append(WHERE_STATUS_EQUALS);
        return query.toString();
    }
    
    public  void deletePosition(final Integer idPosition) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_POSITION, idPosition);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM POSITION WHERE IdPosition = :IdPosition");
        return query.toString();
    }
    
    public Integer builtFindAll() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }
    
    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        return query.toString();
    }

    private void buildCountQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdPosition) ");
        query.append(FROM_POSITION);
    }
    
    public Integer countRecordsByStatusQuery() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, RecordStatusEnum.ACTIVE.toString());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByStatus(), namedParameters, Integer.class);
    }

    private String countRecordsByStatus() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append(WHERE_STATUS_EQUALS);
        return query.toString();
    }

    @Override
    public List<Positions> findAllPositionCatalogPaged(final RecordStatusEnum status, final Integer pagesNumber, 
            final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POSITION,
                    this.findAllPositionsCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<Positions>(Positions.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource statusParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        return source;
    }

    private String findAllPositionsCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdPosition, Name, Status ");
        builder.append(FROM_POSITION);
        builder.append("WHERE :Status IS NULL OR Status = :Status ");
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }
    
    @Override
    public Long countTotalItemsToShowOfPosition(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllPositionsCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}

