package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.apache.log4j.Logger;
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
import mx.pagos.admc.contracts.interfaces.Areable;
import mx.pagos.admc.contracts.sqlserver.utils.SqlServerUtils;
import mx.pagos.admc.contracts.structures.Area;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class AreasDAO implements Areable {
    private static final String SELECT_COUNT = "SELECT COUNT(";
    private static final String WHERE = " WHERE ";
    private static final String SPACE = " ";
    private static final String UPDATE_AREA_SET = "UPDATE AREA SET ";
    private static final String INSERT_AREA = "INSERT INTO ";
    private static final String EQUAL_TAG = " = :";
    private static final String FROM_AREA = " FROM " + TableConstants.TABLE_AREA + SPACE;
    private static final String WHERE_ID_AREA_EQUALS_ID_AREA_PARAM = WHERE + TableConstants.ID_AREA +
            EQUAL_TAG + TableConstants.ID_AREA;
    private static final String RIGTH_PARENTHESIS_TAG = ")";
    private static final String POINTS = ":";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    private static final Logger LOG = Logger.getLogger(AreasDAO.class);
    
    @Autowired
    private SqlServerUtils databaseUtils;
//    private DatabaseUtils databaseUtils;



    @Override
    public Integer saveOrUpdate(final Area area) throws DatabaseException {
        return area.getIdArea() == null ? this.insertArea(area) : this.updateArea(area);
    }

    @Override
    public void changeAreaStatus(final Integer idArea, final RecordStatusEnum status) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_AREA, idArea);
            namedParameters.addValue(TableConstants.STATUS, status.toString());       
            this.namedjdbcTemplate.update(this.changeAreaStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar el estatus del Área", dataAccessException);
        }
    }

    @Override
    public List<Area> findAll() throws DatabaseException { 
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<Area>(Area.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar áreas", dataAccessException);
        }
    }

    @Override
    public List<Area> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.findByRecordStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Area>(Area.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar por estatus", dataAccessException);
        }
    }
    
    @Override
    public List<Area> findByIdRequisition(final Integer idRequisition) throws DatabaseException {
    	try {
    		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
    		return this.namedjdbcTemplate.query(this.findByIdRequisitionQuery(), namedParameters,
    				new BeanPropertyRowMapper<Area>(Area.class));
    	} catch (DataAccessException dataAccessException) {
    		throw new DatabaseException("Error al recuperar las áreas por solicitud", dataAccessException);
    	}
    }

    @Override
    public Area findById(final Integer idArea) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_AREA, idArea);
            LOG.info("---------------------< QUERY - findById  idArea :: "+idArea+" >-----------------------------------------------");
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(),
                    namedParameters, new BeanPropertyRowMapper<Area>(Area.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al recuperar el proveedor", exception);
        }
    }

    public void deleteQuery(final Integer idArea) {
        final StringBuilder queryDelete = new StringBuilder();
        queryDelete.append("DELETE " + FROM_AREA + WHERE_ID_AREA_EQUALS_ID_AREA_PARAM);
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_AREA, idArea);
        this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
    }

    public Integer builtFindAll() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }

    public Integer countRecordsByStatusQuery() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS  , RecordStatusEnum.ACTIVE.toString());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByStatus(), namedParameters, Integer.class);
    }
    
    private Integer updateArea(final Area area) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(area);
            this.namedjdbcTemplate.update(this.updateAreaQuery().toString(), namedParameters);
            return area.getIdArea();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al Actualizar el Área", dataAccessException);
        }
    }

    private Integer insertArea(final Area area) throws DatabaseException {     
        try { 
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            this.setUserNamedParameters(area, namedParameters);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.insertAreaQuery().toString(), namedParameters, keyHolder, 
                    new String[]{"IdArea"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar el Área", dataAccessException);
        }
    }

    private StringBuilder updateAreaQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_AREA_SET);
        query.append(TableConstants.NAME + EQUAL_TAG + TableConstants.NAME);
        query.append(WHERE_ID_AREA_EQUALS_ID_AREA_PARAM);
        return query;
    }

    private StringBuilder insertAreaQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(INSERT_AREA + TableConstants.TABLE_AREA);
        query.append("(" + TableConstants.NAME + RIGTH_PARENTHESIS_TAG);
        query.append(" VALUES ("); 
        query.append(POINTS + TableConstants.NAME + RIGTH_PARENTHESIS_TAG);
        return query;
    }
    
    private void setUserNamedParameters(final Area area, final MapSqlParameterSource namedParameters) {
        namedParameters.addValue(TableConstants.NAME, area.getName());
    }

    private String changeAreaStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_AREA_SET);
        query.append(TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS);
        query.append(WHERE_ID_AREA_EQUALS_ID_AREA_PARAM);
        return query.toString();
    }

    private String findAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(" FROM AREA ");
        return query.toString();
    }

    private void buildSelectAllQuery(final StringBuilder query) {
    	query.append("SELECT IdArea,Name,Status");
    }

    private String findByRecordStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(FROM_AREA + WHERE + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }
    
    private String findByIdRequisitionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT RAA.IdArea, A.Name, A.Status ");
        query.append("FROM REQUISITIONAPPROVALAREA RAA ");
        query.append("INNER JOIN AREA A ON RAA.IdArea = A.IdArea ");
        query.append("WHERE RAA.IdRequisition =:IdRequisition ");
        return query.toString();
    }

    private String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(FROM_AREA + WHERE_ID_AREA_EQUALS_ID_AREA_PARAM);
        LOG.info(query.toString());
        return query.toString();
    }

    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        return query.toString();
    }

    private String countRecordsByStatus() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append(WHERE + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }
    
    private void buildCountQuery(final StringBuilder query) {
        query.append(SELECT_COUNT + TableConstants.ID_AREA + RIGTH_PARENTHESIS_TAG);
        query.append(FROM_AREA);
    }

    @Override
    public List<Area> findAllAreasCatalogPaged(final RecordStatusEnum status, final Integer pagesNumber, 
            final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_AREA,
                    this.findAllAreasCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<Area>(Area.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource statusParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        return source;
    }
    
    private String findAllAreasCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdArea, Name, Status ");
        builder.append("FROM AREA ");
        builder.append("WHERE :Status IS NULL OR Status = :Status ");
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }

    @Override
    public Long countTotalItemsToShowOfAreas(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllAreasCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}

