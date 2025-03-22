package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.FlowStepable;
import mx.pagos.admc.contracts.structures.FlowStep;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class FlowStepDAO implements FlowStepable {
    private static final String WHERE_CLAUSE = "WHERE IdFlow = :IdFlow";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Override
    public void saveStepImage(final FlowStep flowStep) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createInsertFlowStepNamedParameters(flowStep);
            this.namedjdbcTemplate.update(this.buildInsertFlowStepQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }        
    }

    private MapSqlParameterSource createInsertFlowStepNamedParameters(final FlowStep flowStep) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, flowStep.getIdFlow());
        namedParameters.addValue(TableConstants.STEP, flowStep.getStep());
        namedParameters.addValue(TableConstants.STEP_IMAGE_ENABLED, flowStep.getStepImageEnabled());
        namedParameters.addValue(TableConstants.STEP_IMAGE_DISABLED, flowStep.getStepImageDisabled());
        return namedParameters;
    }
    
    public String buildInsertFlowStepQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO FLOWSTEP(IdFlow, Step, StepImageEnabled, StepImageDisabled) ");
        query.append("VALUES(:IdFlow, :Step, :StepImageEnabled, :StepImageDisabled)");
        return query.toString();
    }
    
    @Override
    public void deleteStepImage(final Integer idFlow, final Integer step) throws DatabaseException {
        final MapSqlParameterSource namedParameters = this.createStepAndIdFlowParameters(idFlow, step);
        this.namedjdbcTemplate.update(this.buildDeleteFlowStepImageQuery().toString(), namedParameters);
    }

    private MapSqlParameterSource createStepAndIdFlowParameters(final Integer idFlow, final Integer step) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        namedParameters.addValue(TableConstants.STEP, step);
        return namedParameters;
    }

    private String buildDeleteFlowStepImageQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM FLOWSTEP ");
        query.append("WHERE IdFlow = :IdFlow AND Step = :Step");
        return query.toString();
    }
    
    @Override
    public List<String> flowStep(final Integer step, final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createStepAndIdFlowParameters(idFlow, step);
            return this.namedjdbcTemplate.queryForList(this.buildFindFlowStep(), namedParameters, String.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildFindFlowStep() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT CASE ");
        query.append("WHEN Step <= :Step THEN StepImageEnabled ");
        query.append("ELSE StepImagedisabled ");
        query.append("END AS FlowStep FROM FLOWSTEP ");
        query.append(WHERE_CLAUSE);
        return query.toString();
    }
    
    public Integer countRecordsByIdFlow(final Integer idFlow) {
        final MapSqlParameterSource naMapSqlParameterSource = new MapSqlParameterSource();
        naMapSqlParameterSource.addValue(TableConstants.ID_FLOW, idFlow);
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), naMapSqlParameterSource,
                Integer.class);
    }
    
    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(idFlow) ");
        query.append("FROM FLOWSTEP ");
        query.append(WHERE_CLAUSE);
        return query.toString();
    }
}
