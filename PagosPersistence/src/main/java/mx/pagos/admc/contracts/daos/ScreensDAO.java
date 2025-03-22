package mx.pagos.admc.contracts.daos;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Screenable;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Mizraim
 * 
 * @see Screen
 * @see Screenable
 * @see DatabaseException
 * @see RecordStatusEnum
 * @see NamedParameterJdbcTemplate
 * 
 */
@Repository
public class ScreensDAO implements Screenable {
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status";
    private static final String WHERE_TRAY_EQUALS_TRAY = "WHERE FactoryNameTray = :FactoryNameTray";
    private static final String FROM_SCREEN = "FROM SCREEN ";
    private static final String WHERE_FACTORY_NAME_EQUALS_FACTORY_NAME = "WHERE FactoryName = :FactoryName";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public void insertScreen(final Screen screen) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createInsertOrUpdateNamedParameters(screen);
            this.namedjdbcTemplate.update(this.buildInsertScreenQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void updateScreen(final Screen screen) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createInsertOrUpdateNamedParameters(screen);
            this.namedjdbcTemplate.update(this.buildUpdateScreenQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changeScreenStatus(final String factoryName, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createChangeStatusNamedParameters(factoryName, status);
            this.namedjdbcTemplate.update(this.buildChangeStatusNamedParametersQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Screen> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
                    new BeanPropertyRowMapper<Screen>(Screen.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Screen findByFactoryName(final String factoryName) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByFactoryNameNamedParameters(factoryName);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByFactoryNameQuery(), namedParameters,
                    new BeanPropertyRowMapper<Screen>(Screen.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException(exception);
        }
    }

    @Override
    public Screen findByFactoryNameTray(final String factoryNameTray) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindByFactoryNameTrayNamedParameters(factoryNameTray);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByFactorynameTrayQuery(), namedParameters,
                    new BeanPropertyRowMapper<Screen>(Screen.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException(exception);
        }
    }

    @Override
    public List<Screen> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByRecordStatusParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByRecordStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Screen>(Screen.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void deleteByFactoryName(final String factoryName) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByFactoryNameNamedParameters(factoryName);
            this.namedjdbcTemplate.update(this.buildDeleteByFactoryNameQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public String findNameByFlowStatus(final FlowPurchasingEnum flowStatus) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByFlowStatusNamedParameters(flowStatus.
                    toString());
            return this.namedjdbcTemplate.queryForObject(this.buildSelectByFlowStatusQuery(), namedParameters,
                    String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private MapSqlParameterSource createFindByFlowStatusNamedParameters(final String flowStatus) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FlowStatus", flowStatus.toString());
        return namedParameters;
    }
    
    private String buildSelectByFlowStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Name FROM SCREEN WHERE FlowStatus = :FlowStatus");
        return query.toString();
    }
    
    private String buildFindByRecordStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByRecordStatusParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
    
    private String buildFindByFactorynameTrayQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_TRAY_EQUALS_TRAY);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByFactoryNameTrayNamedParameters(final String factoryNameTray) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FactoryNameTray", factoryNameTray);
        return namedParameters;
    }
    
    private String buildChangeStatusNamedParametersQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE SCREEN SET Status = :Status ");
        query.append(WHERE_FACTORY_NAME_EQUALS_FACTORY_NAME);
        return query.toString();
    }
    
    private MapSqlParameterSource createChangeStatusNamedParameters(
            final String factoryName, final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("FactoryName", factoryName);
        namedParameters.addValue("Status", status.toString());
        return namedParameters;
    }
    
    private String buildInsertScreenQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO SCREEN (");
        this.buildAllFieldsQuery(query);
        query.append(") VALUES (:FactoryName, :FactoryNameTray, :Name, :FlowStatus)");
        return query.toString();
    }
    
    private String buildUpdateScreenQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE SCREEN SET FactoryName =:FactoryName, FactoryNameTray = :FactoryNameTray,");
        query.append("Name = :Name, FlowStatus = :FlowStatus ");
        query.append(WHERE_FACTORY_NAME_EQUALS_FACTORY_NAME);
        return query.toString();
    }
    
    private void buildAllFieldsQuery(final StringBuilder query) {
        query.append("FactoryName, FactoryNameTray, Name, FlowStatus ");
    }
    
    private MapSqlParameterSource createInsertOrUpdateNamedParameters(final Screen screen) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.FACTORY_NAME, screen.getFactoryName());
        namedParameters.addValue(TableConstants.FACTORY_NAME_TRAY, screen.getFactoryNameTray());
        namedParameters.addValue(TableConstants.NAME, screen.getName());
        namedParameters.addValue(TableConstants.FLOW_STATUS, screen.getFlowStatus().toString());
        return namedParameters;
    }
    
    private String buildFindByFactoryNameQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_FACTORY_NAME_EQUALS_FACTORY_NAME);
        return query.toString();
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT ");
        this.buildAllFieldsQuery(query);
        query.append(", Status ");
        query.append(FROM_SCREEN);
    }

    private String buildDeleteByFactoryNameQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM SCREEN ");
        query.append(WHERE_FACTORY_NAME_EQUALS_FACTORY_NAME);
        return query.toString();
    }

    private MapSqlParameterSource createFindByFactoryNameNamedParameters(final String factoryName) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.FACTORY_NAME, factoryName);
        return namedParameters;
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }

    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(FactoryName) FROM SCREEN ");
        return query.toString();
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByRecordStatusParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountBystatusQuery(), namedParameters, Integer.class);
    }

    private String buildCountBystatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildCountAllQuery());
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    @Override
    public String findStageByStatusAndTurn(final FlowPurchasingEnum flowStatus, final Integer turn)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindStageByStatusAndTurnNamedParameters(flowStatus, turn);
            return this.namedjdbcTemplate.queryForObject(this.buildFindStageByStatusAndTurnQuery(),
                    namedParameters, String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource createFindStageByStatusAndTurnNamedParameters(
            final FlowPurchasingEnum flowStatus, final Integer turn) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.FLOW_STATUS, flowStatus.toString());
        namedParameters.addValue(TableConstants.TURN, turn);
        return namedParameters;
    }

    private String buildFindStageByStatusAndTurnQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT CASE WHEN :Turn = 0 THEN StageFirstTime ELSE StageAfterFirstTime END ");
        query.append("FROM SCREEN WHERE FlowStatus = :FlowStatus ");
        return query.toString();
    }
}
