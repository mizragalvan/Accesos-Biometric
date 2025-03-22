package mx.pagos.logs.daos;

import java.sql.Types;
import java.util.Collections;
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

import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.logs.daos.constants.TableConstants;
import mx.pagos.logs.interfaces.Binnacleable;
import mx.pagos.logs.structures.Binnacle;

/**
 * @author Mizraim
 */
@Repository
public class BinnacleDAO implements Binnacleable {
    private static final String ORDER_BY_REGISTER_DATE = "ORDER BY RegisterDate DESC ";
    private static final String WHERE_ID_USER_EQUALS_ID_USER = "WHERE USERS.IdUser = :IdUser ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer save(final Binnacle binnacle) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(binnacle);
            namedParameters.registerSqlType("LogCategory", Types.VARCHAR);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertBinnacleQuery(), namedParameters, keyHolder,
                    new String[]{"IdBinnacle"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Binnacle> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<Binnacle>(Binnacle.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Binnacle> findByIdUser(final Integer idUser) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdUserNamedParameters(idUser);
            return this.namedjdbcTemplate.query(this.findByIdUserQuery(), namedParameters,
                    new BeanPropertyRowMapper<Binnacle>(Binnacle.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Binnacle> findByDate(final String startDate, final String endDate) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByDateNamedParameters(startDate, endDate);
            return this.namedjdbcTemplate.query(this.findByDateQuery(), namedParameters,
                    new BeanPropertyRowMapper<Binnacle>(Binnacle.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public Binnacle findByIdBinnacle(final Integer idBinnacle) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idBinnacle);
            return this.namedjdbcTemplate.queryForObject(this.findByIdBinnacleQuery(), namedParameters,
                    new BeanPropertyRowMapper<Binnacle>(Binnacle.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Binnacle> findByIdFlow(final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdFlowNamedParameters(idFlow);
            return this.namedjdbcTemplate.query(this.buildFindByIdFlowQuery(), namedParameters,
                    new BeanPropertyRowMapper<Binnacle>(Binnacle.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Binnacle> findByLogCategory(final LogCategoryEnum logCategory) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByLogCategoryNamedParameters(logCategory);
            return this.namedjdbcTemplate.query(this.buildFindByLogCategoryQuery(),
                    namedParameters, new BeanPropertyRowMapper<Binnacle>(Binnacle.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    public Integer countByCategory(final LogCategoryEnum logCategory) {
        final MapSqlParameterSource namedParameters = this.createFindByLogCategoryNamedParameters(logCategory);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByCategoryQuery(),
                namedParameters, Integer.class);
    }

    private String buildInsertBinnacleQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO BINNACLE(IdUser, IdFlow, Action, LogCategory) ");
        query.append("VALUES (:IdUser, :IdFlow, :Action, :LogCategory)");
        return query.toString();
    }
    
    private String findAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(ORDER_BY_REGISTER_DATE);
        return query.toString();
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdBinnacle, BINNACLE.IdUser, ");
        query.append("CONCAT(USERS.Name, ' ', FirstLastName, ' ', SecondLastName) AS UserFullName, ");
        query.append("BINNACLE.IdFlow, RegisterDate, Action, LogCategory, FLOW.Name AS FlowName ");
        query.append("FROM BINNACLE LEFT JOIN USERS ON BINNACLE.IdUser = USERS.IdUser ");
        query.append("LEFT JOIN FLOW ON BINNACLE.IdFlow = FLOW.IdFlow ");
    }

    private String findByIdBinnacleQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE IdBinnacle = :IdBinnacle ");
        query.append(ORDER_BY_REGISTER_DATE);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idBinnacle) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_BINNACLE, idBinnacle);
        return namedParameters;
    }
    
    private String findByIdUserQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_USER_EQUALS_ID_USER);
        query.append(ORDER_BY_REGISTER_DATE);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdUserNamedParameters(final Integer idUser) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_USER, idUser);
        return namedParameters;
    }
    
    private String findByDateQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE  CAST(RegisterDate AS DATE) BETWEEN :startDate AND :endDate");
        return query.toString();
    }   
    
    private MapSqlParameterSource createFindByDateNamedParameters(final String startDate, final String endDate) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("startDate", startDate);
        namedParameters.addValue("endDate", endDate);
        return namedParameters;
    }
    
    public void deleteById(final Integer idBinnacle) {
        final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idBinnacle);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM BINNACLE WHERE IdBinnacle = :IdBinnacle");
        return query.toString();
    }
    
    public Integer builtFindAll() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }
    
    public Integer countRecordsByIdUserQuery(final Binnacle binnacle) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_USER, binnacle.getIdUser());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByIdUser(), namedParameters, Integer.class);
    }
    
    public Integer countRecordsByDateQuery(final String startDate, final String endDate) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("startD", startDate);
        namedParameters.addValue("endD", endDate);
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByDate(), namedParameters, Integer.class);
    }
    
    private void buildCountQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdBinnacle) FROM BINNACLE ");
    }
    
    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        return query.toString();
    }
    
    private String countRecordsByIdUser() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append("WHERE IdUser = :IdUser");
        return query.toString();
    }
    
    private String countRecordsByDate() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append("WHERE CAST(RegisterDate AS DATE) BETWEEN :startD AND :endD");
        return query.toString();
    }
    
    private String buildFindByIdFlowQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE BINNACLE.IdFlow = :IdFlow");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdFlowNamedParameters(final Integer idFlow) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        return namedParameters;
    }
    
    private String buildFindByLogCategoryQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE LogCategory = :LogCategory");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByLogCategoryNamedParameters(final LogCategoryEnum logCategory) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.LOG_CATEGORY, logCategory.toString());
        return namedParameters;
    }
    
    private String buildCountByCategoryQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(IdBinnacle) FROM BINNACLE WHERE LogCategory = :LogCategory");
        return query.toString();
    }

    @Override
    public void deleteByRangeDates(final String dateFrom, final String dateTo, final List<String> list)
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("dateFrom", dateFrom, Types.VARCHAR);
            source.addValue("dateTo", dateTo, Types.VARCHAR);
            source.addValues(Collections.singletonMap("logCategory", list));
            this.namedjdbcTemplate.update(this.deleteByRangeDatesQuery(), source);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    final String deleteByRangeDatesQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM BINNACLE ");
        builder.append("WHERE ").append(databaseUtils.converToShortDate("RegisterDate"));
        builder.append(" BETWEEN :dateFrom AND :dateTo AND LogCategory IN(:logCategory) ");
        return builder.toString();
    }

	@Override
	public Long findByLogCategoryTypesPaginatedTotalPages(Binnacle binnacle) throws DatabaseException {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("startDate", binnacle.getStartDate(), Types.VARCHAR);
        source.addValue("endDate", binnacle.getEndDate(), Types.VARCHAR);
        source.addValue("idUser", binnacle.getIdUser());
        source.addValues(Collections.singletonMap(TableConstants.ID_BINNACLE, binnacle.getLogList()));
        final String countRows = this.databaseUtils.countTotalRows(this.findByLogCategoryListQuery());
        return this.namedjdbcTemplate.queryForObject(countRows, source, Long.class);
	}
    
    @Override
	public List<Binnacle> findByLogCategoryTypesPaginated(final Binnacle binnacle, 
			final Integer pageNumber, final Integer itemsNumber) throws DatabaseException {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("startDate", binnacle.getStartDate(), Types.VARCHAR);
        source.addValue("endDate", binnacle.getEndDate(), Types.VARCHAR);
        source.addValue("idUser", binnacle.getIdUser());
        source.addValues(Collections.singletonMap(TableConstants.ID_BINNACLE, binnacle.getLogList()));
        
        final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_BINNACLE,
        		this.findByLogCategoryListQuery(), pageNumber, itemsNumber);
        return this.namedjdbcTemplate.query(paginatedQuery, source, new BeanPropertyRowMapper<>(Binnacle.class));
	}

    
    @Override
    public List<Binnacle> findByLogCategoryListDatesAndUser(final Binnacle binnacle)
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue("startDate", binnacle.getStartDate(), Types.VARCHAR);
            source.addValue("endDate", binnacle.getEndDate(), Types.VARCHAR);
            source.addValue("idUser", binnacle.getIdUser());
            source.addValues(Collections.singletonMap(TableConstants.ID_BINNACLE, binnacle.getLogList()));
            return this.namedjdbcTemplate.query(this.findByLogCategoryListQuery(), source, 
                    new BeanPropertyRowMapper<>(Binnacle.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findByLogCategoryListQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdBinnacle, BINNACLE.IdUser, BINNACLE.IdFlow, Action, RegisterDate, LogCategory, ");
        builder.append("CONCAT(USERS.Name,' ',FirstLastName,' ',SecondLastName) AS UserFullName ");
        builder.append("FROM BINNACLE LEFT JOIN USERS ON BINNACLE.IdUser = USERS.IdUser ");
        builder.append("LEFT JOIN FLOW ON BINNACLE.IdFlow = FLOW.IdFlow ");
        builder.append("WHERE (:idUser IS NULL OR BINNACLE.IdUser = :idUser) ");
        builder.append("AND ((:startDate IS NULL OR :endDate IS NULL) ");
        builder.append("OR CAST(RegisterDate AS DATE) BETWEEN :startDate AND :endDate) ");
        builder.append("AND LogCategory IN(:IdBinnacle) ");
        return builder.toString();
    }
}

