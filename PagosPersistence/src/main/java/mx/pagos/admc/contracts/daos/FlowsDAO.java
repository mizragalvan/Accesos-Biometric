package mx.pagos.admc.contracts.daos;

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
import mx.pagos.admc.contracts.interfaces.Flowable;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class FlowsDAO implements Flowable {
    private static final String WHERE_ID_FLOW_EQUALS_ID_FLOW_PARAM = QueryConstants.WHERE + TableConstants.ID_FLOW +
            QueryConstants.EQUAL_TAG + TableConstants.ID_FLOW;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public Integer saveOrUpdate(final Flow flow) throws DatabaseException {
        return flow.getIdFlow() == null ? this.insertFlow(flow) : this.updateFlow(flow);
    }

    @Override
    public void changeFlowStatus(final Integer idFlow, final RecordStatusEnum status) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
            namedParameters.addValue(TableConstants.STATUS, status.toString());       
            this.namedjdbcTemplate.update(this.changeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar el estatus del Flujo", dataAccessException);
        }
    }

    @Override
    public List<Flow> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<Flow>(Flow.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar Flujos", dataAccessException);
        }
    }

    @Override
    public Flow findByFlowName(final String flowName) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.NAME, flowName);
            return this.namedjdbcTemplate.queryForObject(this.findByNameQuery(), namedParameters, 
                    new BeanPropertyRowMapper<Flow>(Flow.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al buscar por nombre", exception);
        }
    }

    @Override
    public List<Flow> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.findByRecordStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Flow>(Flow.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar por estatus", dataAccessException);
        }
    }

    @Override
    public Flow findById(final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_FLOW, idFlow);      
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(),
                    namedParameters, new BeanPropertyRowMapper<Flow>(Flow.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al recuperar el Flujo", exception);
        }
    }
    
    @Override
    public Boolean isManagerialFlow(final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
            return this.namedjdbcTemplate.queryForObject(this.buildIsManagerialFlowQuery(),
                    namedParameters, Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildIsManagerialFlowQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) AS isManagerialFlow ");
        query.append("FROM FLOW WHERE IdFlow = :IdFlow AND Type = 'MANAGERIAL'");
        return query.toString();
    }

    public void deleteById(final Integer idFlow) {
        final StringBuilder queryDelete = new StringBuilder();
        queryDelete.append("DELETE " + QueryConstants.FROM + TableConstants.TABLE_FLOW + 
                WHERE_ID_FLOW_EQUALS_ID_FLOW_PARAM);
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
    }

    public Integer builtFindAll() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }

    public Integer countRecordsByStatusQuery() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, RecordStatusEnum.ACTIVE.toString());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByStatus(), namedParameters, Integer.class);
    }

    private Integer updateFlow(final Flow flow) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(flow);
            this.namedjdbcTemplate.update(this.updateQuery().toString(), namedParameters);
            return flow.getIdFlow();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al Actualizar el Flujo", dataAccessException);
        }
    }

    private Integer insertFlow(final Flow flow) throws DatabaseException {     
        try { 
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            this.setNamedParameters(flow, namedParameters);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.insertQuery().toString(), namedParameters, keyHolder, 
                    new String[]{"IdFlow"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar el Flujo", dataAccessException);
        }
    }

    private StringBuilder updateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.UPDATE + TableConstants.TABLE_FLOW + QueryConstants.SET);
        query.append(TableConstants.NAME + QueryConstants.EQUAL_TAG + TableConstants.NAME + QueryConstants.SPACE);
        query.append(WHERE_ID_FLOW_EQUALS_ID_FLOW_PARAM);
        return query;
    }

    private StringBuilder insertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_FLOW);
        query.append(QueryConstants.LEFT_BRACES + TableConstants.NAME + QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.VALUES); 
        query.append(QueryConstants.TAG + TableConstants.NAME + QueryConstants.RIGHT_BRACES);
        return query;
    }

    private void setNamedParameters(final Flow flow, final MapSqlParameterSource namedParameters) {
        namedParameters.addValue(TableConstants.NAME, flow.getName());
    }

    private String changeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.UPDATE + TableConstants.TABLE_FLOW + QueryConstants.SET);
        query.append(TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS + QueryConstants.SPACE);
        query.append(WHERE_ID_FLOW_EQUALS_ID_FLOW_PARAM);
        return query.toString();
    }

    private String findAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(QueryConstants.FROM + TableConstants.TABLE_FLOW);
        return query.toString();
    }

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append(QueryConstants.SELECT + TableConstants.ID_FLOW + QueryConstants.COMMA + TableConstants.NAME + 
                QueryConstants.COMMA + TableConstants.STATUS + QueryConstants.SPACE);
    }

    private String findByRecordStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(QueryConstants.FROM + TableConstants.TABLE_FLOW + QueryConstants.WHERE + 
                TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }

    private  String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(QueryConstants.FROM + TableConstants.TABLE_FLOW + WHERE_ID_FLOW_EQUALS_ID_FLOW_PARAM);
        return query.toString();
    }
    
    private String findByNameQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(QueryConstants.FROM + TableConstants.TABLE_FLOW + QueryConstants.WHERE + 
                TableConstants.NAME + QueryConstants.EQUAL_TAG + TableConstants.NAME);
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
        query.append(QueryConstants.WHERE + TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }

    private void buildCountQuery(final StringBuilder query) {
        query.append(QueryConstants.SELECT_COUNT + TableConstants.ID_FLOW + QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.FROM + TableConstants.TABLE_FLOW);
    }

    @Override
    public List<String> findStepListByIdFlow(final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createStepAndIdFlowParameters(idFlow);
            return this.namedjdbcTemplate.queryForList(this.buildFindStepListFlow(), namedParameters, String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource createStepAndIdFlowParameters(final Integer idFlow) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        return namedParameters;
    }
    
    private String buildFindStepListFlow() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT StepImageEnabled ");
        query.append("FROM FLOWSTEP ");
        query.append("WHERE IdFlow = :IdFlow ");
        query.append("ORDER BY Step ");
        return query.toString();
    }

    @Override
    public List<Flow> findPurchasingFlows() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findPurchasingFlowsQuery(), 
                    new BeanPropertyRowMapper<Flow>(Flow.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findPurchasingFlowsQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT IdFlow, Name, Status, Type AS FlowType FROM FLOW ");
        builder.append("WHERE Type = 'PURCHASING' AND Status = 'ACTIVE' ");
        return builder.toString();
    }
}
