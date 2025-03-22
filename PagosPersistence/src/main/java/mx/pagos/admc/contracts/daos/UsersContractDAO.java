package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.UsersContractable;
import mx.pagos.admc.contracts.structures.UserLawyerEvaluator;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.User;

@Repository
public class UsersContractDAO implements UsersContractable {
    @Autowired
    private DatabaseUtils databaseUtils;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	private static final Logger LOG = Logger.getLogger(UsersContractDAO.class);

    public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
        this.databaseUtils = databaseUtilsParameter;
    }
    
	@Override
	public List<UserLawyerEvaluator> findAviableLawyersOrEvaluators(Integer idFlow,
			Boolean isLawyer, Boolean isEvaluator) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("idFlow", idFlow);
            namedParameters.addValue("isLawyer", isLawyer);
            namedParameters.addValue("isEvaluator", isEvaluator);
            return this.namedjdbcTemplate.query(this.buildFindAviableLawyersOrEvaluatorQuery(),
                    namedParameters, new BeanPropertyRowMapper<UserLawyerEvaluator>(UserLawyerEvaluator.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
	public UserLawyerEvaluator findAviableLawyerOrEvaluator(Integer idFlow, final Boolean isLawyer, final Boolean isEvaluator) throws DatabaseException, EmptyResultException {
		try{
			return this.findAviableLawyersOrEvaluators(idFlow, isLawyer, isEvaluator).get(0);
		}catch(IndexOutOfBoundsException ex){
			throw new EmptyResultException(ex);
		}		
	}
	
	@Override
	public Boolean findIsTrayUserFiltered(
	        final Integer idUser, final FlowPurchasingEnum status, final Integer idFlow) throws DatabaseException {
	    try {
	    	LOG.info("***** CARGA DE BANDEJA ****  "+status+" ::  USUARIO ["+idUser+"] - FLUJO ["+idFlow+")] ****");	        
	        final MapSqlParameterSource namedParameters =
	                this.createFindIsTrayUserFilteredNamedParameters(idUser, status, idFlow);
	        return this.namedjdbcTemplate.queryForObject(this.buildFindIsTrayUserFilteredQuery(),
	                namedParameters, Boolean.class);
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

    private String buildFindAviableLawyersOrEvaluatorQuery() {
    	final StringBuilder query = new StringBuilder();
    	query.append("SELECT LAWYEREVALUATOR.FirstLastName, LAWYEREVALUATOR.SecondLastName, LAWYEREVALUATOR.Name, "); 
    	query.append("LAWYEREVALUATOR.IdUser, LAWYEREVALUATOR.holidaysLeft, LAWYEREVALUATOR.CONTADOR, ");
    	query.append("(SELECT COUNT(*) FROM REQUISITION WHERE REQUISITION.IdLawyer = LAWYEREVALUATOR.IdUser) as TotalReqByLawyer ");
    	query.append("FROM ( ");
    	query.append("SELECT distinct USERS.IdUser, PROFILESCREENFLOW.IdFlow, USERS.FirstLastName, USERS.SecondLastName, con.CONTADOR, USERS.Name, ");
    	query.append("(SELECT TOP 1 DATEDIFF(DD,GETDATE(),StartDate) FROM HOLIDAYUSER ");
    	query.append("where HOLIDAYUSER.IdUser = USERS.IdUser AND GETDATE() < HOLIDAYUSER.StartDate ORDER BY HOLIDAYUSER.StartDate) holidaysLeft ");
    	query.append("FROM USERS ");
    	query.append("INNER JOIN PROFILEUSER ON PROFILEUSER.IdUser = USERS.IdUser "); 
    	query.append("INNER JOIN PROFILESCREENFLOW ON PROFILESCREENFLOW.IdProfile = PROFILEUSER.IdProfile ");
    	query.append("LEFT JOIN HOLIDAYUSER ON HOLIDAYUSER.IdUser = USERS.IdUser ");
    	query.append("LEFT JOIN ( ");
    	query.append("SELECT COUNT(1) AS CONTADOR, IdLawyer ");
    	query.append("FROM REQUISITION  ");
    	query.append("WHERE EXISTS ( ");
    	query.append(this.databaseUtils.arrayToTableFunc("SELECT CONF.Value FROM CONFIGURATION CONF WHERE CONF.Name = 'LAWYER_NOT_AVAILABLE_STATUS'"));
    	query.append("WHERE StrVal = REQUISITION.Status	OR :isEvaluator = 1 ");
    	query.append(")	 ");
    	query.append("GROUP BY IdLawyer ");
    	query.append(") con on con.IdLawyer = USERS.IdUser ");
    	query.append("WHERE (GETDATE() NOT BETWEEN HOLIDAYUSER.StartDate AND HOLIDAYUSER.EndDate OR HOLIDAYUSER.StartDate IS NULL) "); 
    	query.append("AND PROFILESCREENFLOW.IdFlow = :idFlow  ");
    	query.append("AND ((USERS.IsLawyer = 1 AND :isLawyer = 1) OR (USERS.IsEvaluator = 1 AND :isEvaluator = 1)) ");
    	query.append("AND USERS.Status = 'ACTIVE' ");
    	query.append(") LAWYEREVALUATOR ");
    	query.append("ORDER BY LAWYEREVALUATOR.CONTADOR, LAWYEREVALUATOR.holidaysLeft ");
    	return query.toString();
    }
    
    private String buildFindIsTrayUserFilteredQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT MIN(IsUserFiltered) AS IsUserFiltered FROM PROFILESCREENFLOW INNER JOIN ");
        query.append("SCREEN ON SCREEN.FactoryName = PROFILESCREENFLOW.FactoryName INNER JOIN ");
        query.append("PROFILEUSER ON PROFILESCREENFLOW.IdProfile = PROFILEUSER.IdProfile ");
        query.append("WHERE IdFlow = :IdFlow AND FlowStatus = :Status AND PROFILEUSER.IdUser = :IdUser");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindIsTrayUserFilteredNamedParameters(
            final Integer idUser, final FlowPurchasingEnum status, final Integer idFlow) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_USER, idUser);
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }

    @Override
    public List<User> findDeciderLawyer(final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.ID_FLOW, idFlow);
            return this.namedjdbcTemplate.query(this.findDeciderLawyerQuery(), parameterSource,
                        new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findDeciderLawyerQuery() {
        final StringBuilder builder = new StringBuilder();  
        builder.append("SELECT DISTINCT USERS.IdUser, ");
        builder.append("CONCAT(USERS.Name,' ',USERS.FirstLastName,' ',USERS.SecondLastName) AS Name, ");
        builder.append("(SELECT COUNT(*) FROM REQUISITION WHERE REQUISITION.IdLawyer = USERS.IdUser AND REQUISITION.Status not in('REQUISITION_CLOSE', 'CLOSED', 'CANCELLED', 'CANCELED_CONTRACT')) AS TotalReqByLawyer ");
        builder.append("FROM USERS ");
        builder.append("INNER JOIN PROFILEUSER ON USERS.IdUser = PROFILEUSER.IdUser ");
        builder.append("INNER JOIN PROFILESCREENFLOW ON PROFILEUSER.IdProfile = PROFILESCREENFLOW.IdProfile ");
        builder.append("WHERE USERS.IsDecider = 'true' AND PROFILESCREENFLOW.IdFlow = :IdFlow ");
        return builder.toString();
    }
}
