package mx.pagos.admc.contracts.daos;

import static mx.pagos.admc.contracts.constants.TableConstants.ID_FLOW;
import static mx.pagos.admc.contracts.constants.TableConstants.STATUS;
import static mx.pagos.admc.contracts.constants.TableConstants.TABLE_FLOWSCREENACTION;
import static mx.pagos.admc.contracts.constants.TableConstants.TABLE_SCREEN;
import static mx.pagos.general.constants.QueryConstants.DISTINCT;
import static mx.pagos.general.constants.QueryConstants.EQUAL_TAG;
import static mx.pagos.general.constants.QueryConstants.FROM;
import static mx.pagos.general.constants.QueryConstants.INNER_JOIN;
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
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.FlowScreenActionable;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.contracts.structures.StatusBranch;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * @author Mizraim
 * 
 * @see FlowScreenAction
 * @see FlowScreenActionable
 * @see DatabaseException
 * @see NamedParameterJdbcTemplate
 * 
 */
@Repository
public class FlowScreenActionDAO implements FlowScreenActionable {

	private static final String WHERE_ID_FLOW = "WHERE IdFlow = :IdFlow ";
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	
	@Override
	public Integer saveFlowScreenAction(final FlowScreenAction flowScreenAction) throws DatabaseException {
		try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(flowScreenAction);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildInsertFlowScreenActionQuery(), namedParameters);
            return flowScreenAction.getIdFlow();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

	@Override
	public void deleteFlowScreenAction(final Integer idFlow) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource(TableConstants.ID_FLOW, idFlow);
        this.namedjdbcTemplate.update(this.buildDeleteFlowScreenActionQuery().toString(), namedParameters);
	}
    
	@Override
	public String findNextStatus(final FlowScreenAction flowScreenAction) throws DatabaseException, 
		EmptyResultException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(flowScreenAction);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            return this.namedjdbcTemplate.queryForObject(this.buildFindNextStatusQuery(), namedParameters,
                    String.class);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
	public List<StatusBranch> findNextMultipleStatus(final FlowScreenAction flowScreenAction)
	        throws DatabaseException {
	    try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(flowScreenAction);
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            return this.namedjdbcTemplate.query(this.buildFindNextMultipleStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(StatusBranch.class));
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

	private String buildFindNextMultipleStatusQuery() {
	    final StringBuilder query = new StringBuilder();
        query.append("SELECT Status, HoldForBranch FROM FLOWSCREENACTION ");
        query.append(WHERE_ID_FLOW);
        query.append("AND FactoryName = :FactoryName AND ActionName = :ActionName ");
        return query.toString();
    }

    private String buildfindFlowScreenActionByFlow() {
	    final StringBuilder query = new StringBuilder();
	    query.append("SELECT DISTINCT FactoryName, Step FROM FLOWSCREENACTION ");
	    query.append(WHERE_ID_FLOW);
	    query.append("ORDER BY Step , FactoryName DESC ");
	    return query.toString();
	}
	
	private String buildFindNextStatusQuery() {
	    final StringBuilder query = new StringBuilder();
	    query.append("SELECT Status FROM FLOWSCREENACTION ");
	    query.append(WHERE_ID_FLOW);
	    query.append("AND FactoryName = :FactoryName ");
	    query.append("AND ActionName = :ActionName ");
	    return query.toString();
	}
	
    @Override
    public Integer findRequisitionStep(final Integer idRequisition) throws DatabaseException {
        try {
            final MapSqlParameterSource naMapSqlParameterSource = new MapSqlParameterSource();
            naMapSqlParameterSource.addValue(TableConstants.ID_REQUISITION, idRequisition);
            return this.namedjdbcTemplate.queryForObject(this.buildFindRequisitionStepQuery(), 
                    naMapSqlParameterSource, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildFindRequisitionStepQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT FLOWSCREENACTION.Step ");
        query.append("FROM REQUISITION INNER JOIN SCREEN ON REQUISITION.Status = SCREEN.FlowStatus ");
        query.append("INNER JOIN FLOWSCREENACTION ON SCREEN.FactoryName = FLOWSCREENACTION.FactoryName ");
        query.append("AND REQUISITION.IdFlow = FLOWSCREENACTION.IdFlow ");
        query.append("WHERE REQUISITION.IdRequisition = :IdRequisition");
        return query.toString();
    }
	
    @Override
    public Integer findRequisitionOwnerStep(final Integer idRequisitionOwner) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_REQUISITION_OWNERS, idRequisitionOwner);
            return this.namedjdbcTemplate.queryForObject(this.findRequisitionOwnerStep(), source, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findRequisitionOwnerStep() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SELECT DISTINCT FLOWSCREENACTION.Step FROM REQUISITIONOWNERS ");
        builder.append("INNER JOIN REQUISITIONOWNERSSTATUS ");
        builder.append("ON REQUISITIONOWNERS.IdRequisitionOwners = REQUISITIONOWNERSSTATUS.IdRequisitionOwners ");
        builder.append("INNER JOIN SCREEN ON REQUISITIONOWNERSSTATUS.Status = SCREEN.FlowStatus ");
        builder.append("INNER JOIN FLOWSCREENACTION ON SCREEN.FactoryName = FLOWSCREENACTION.FactoryName ");
        builder.append("AND REQUISITIONOWNERS.IdFlow = FLOWSCREENACTION.IdFlow ");
        builder.append("WHERE REQUISITIONOWNERSSTATUS.IdRequisitionOwners = :IdRequisitionOwners ");
        builder.append("AND REQUISITIONOWNERSSTATUS.HoldForBranch IS NULL ");
        return builder.toString();
    }
    
	private String buildInsertFlowScreenActionQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO FLOWSCREENACTION ( ");
        query.append("IdFlow, Step, ");
        query.append("FactoryName, ActionName, Status ");
        query.append(") VALUES (:IdFlow, :Step, :FactoryName, :ActionName, :Status) ");
        return query.toString();
	}
	
	private String buildDeleteFlowScreenActionQuery() {
	    final StringBuilder queryDelete = new StringBuilder();
	    queryDelete.append("DELETE FROM FLOWSCREENACTION ");
	    queryDelete.append(WHERE_ID_FLOW);
	    return queryDelete.toString();
	}
	
	@Override
	public List<FlowScreenAction> findFlowScreenActionByFlow(final FlowScreenAction flowScreenAction) 
	        throws EmptyResultDataAccessException, DatabaseException {
	    try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(flowScreenAction);
            return this.namedjdbcTemplate.query(this.buildfindFlowScreenActionByFlow(), 
                    namedParameters, new BeanPropertyRowMapper<FlowScreenAction>(FlowScreenAction.class));
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	  @Override
	public List<FlowScreenAction> findStatusByFlow(final Integer idFlow) throws DatabaseException {
    	try {
             final BeanPropertySqlParameterSource namedParameters =
                     new BeanPropertySqlParameterSource(new Flow(idFlow));
    		 return this.namedjdbcTemplate.query(this.buildStatusQuery(), namedParameters,
    		         new BeanPropertyRowMapper<FlowScreenAction>(FlowScreenAction.class));
    	} catch (DataAccessException dataAccessException) {
    	    throw new DatabaseException(dataAccessException);
    	}
	}
	  
	  private String buildStatusQuery() {
	      final StringBuilder query = new StringBuilder();
	      query.append(SELECT).append(DISTINCT).append(STATUS).append(FROM).append(TABLE_FLOWSCREENACTION);
	      query.append(WHERE).append(ID_FLOW).append(EQUAL_TAG).append(ID_FLOW);
	      return query.toString();
	  }
	  
    @Override
    public List<Screen> findStatusNameByFlow(final Integer idFlow) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(new Flow(idFlow));
            return this.namedjdbcTemplate.query(this.buildStatusNameByFlowQuery(), namedParameters,
                    new BeanPropertyRowMapper<Screen>(Screen.class));
    	  } catch (DataAccessException dataAccessException) {
    		  throw new DatabaseException(dataAccessException);
    	  }
    }
    
    private String buildStatusNameByFlowQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(SELECT).append(DISTINCT).append("S.Name ,S.FlowStatus ");
        query.append(FROM).append(TABLE_SCREEN).append(" S ");
        query.append(INNER_JOIN).append(TABLE_FLOWSCREENACTION).append(" FSA ON S.FactoryName=FSA.FactoryName ");
        query.append(WHERE).append(ID_FLOW).append(EQUAL_TAG).append(ID_FLOW);
        return query.toString();
    }
}