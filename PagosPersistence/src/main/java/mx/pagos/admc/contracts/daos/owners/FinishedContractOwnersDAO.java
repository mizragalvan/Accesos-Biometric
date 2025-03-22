package mx.pagos.admc.contracts.daos.owners;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.owners.FinishedContractOwnersable;
import mx.pagos.admc.contracts.structures.owners.FinishedContractOwners;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class FinishedContractOwnersDAO implements FinishedContractOwnersable {

	private static final String PERCENTAGE = "%";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
	public List<FinishedContractOwners> findFinishedContractsOwners(
			final FinishedContractOwners vo) throws DatabaseException {
	    try {
			final MapSqlParameterSource namedParameters = 
					this.createFindFinishedContractsOwnersParameters(vo);
			return this.namedjdbcTemplate.query(this.buildFindFinishedContractsOwnersQuery(), 
					namedParameters, new BeanPropertyRowMapper<>(FinishedContractOwners.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	private String buildFindFinishedContractsOwnersQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT RO.IdRequisitionOwners, RO.CustomerCompanyName, C.Name categoryName, ");
		query.append("RO.ApplicationDate, RO.EndDate signatureDate ");
		query.append("FROM REQUISITIONOWNERS RO ");
		query.append("INNER JOIN CATEGORY C ON RO.IdCategory = C.IdCategory ");
		query.append("INNER JOIN REQUISITIONOWNERSSTATUS ROS ");
		query.append("ON RO.IdRequisitionOwners = ROS.IdRequisitionOwners ");
		query.append("WHERE (:StartDate IS NULL OR :EndDate IS NULL ");
		query.append("OR RO.ApplicationDate BETWEEN :StartDate AND :EndDate ");
		query.append("OR RO.EndDate BETWEEN :StartDate AND :EndDate) ");
		query.append("AND (:IdRequisitionOwners IS NULL OR RO.IdRequisitionOwners = :IdRequisitionOwners) ");
		query.append("AND (:IdCategory IS NULL OR RO.IdCategory = :IdCategory) ");
		query.append("AND (UPPER(RO.CustomerCompanyName) LIKE UPPER(:CustomerCompanyName)) ");
		query.append("AND ROS.Status = 'ENTERPRISE_REQUISITION_CLOSE'; ");
		return query.toString();
	}
	
    private MapSqlParameterSource createFindFinishedContractsOwnersParameters(
    		final FinishedContractOwners finishedContractPurchases) {
    	
    	final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    	final String customerCompanyName = finishedContractPurchases.getCustomerCompanyName() == null ? ""
    			: finishedContractPurchases.getCustomerCompanyName();
    	
    	namedParameters.addValue(TableConstants.ID_REQUISITION_OWNERS, 
    			finishedContractPurchases.getIdRequisitionOwners());
    	namedParameters.addValue(TableConstants.ID_CATEGORY, finishedContractPurchases.getIdCategory());
    	namedParameters.addValue(TableConstants.CUSTOMER_COMPANY_NAME, PERCENTAGE + customerCompanyName + PERCENTAGE);
    	namedParameters.registerSqlType(TableConstants.START_DATE, Types.VARCHAR);
    	namedParameters.addValue(TableConstants.START_DATE, finishedContractPurchases.getStartDate());
    	namedParameters.registerSqlType(TableConstants.END_DATE, Types.VARCHAR);
    	namedParameters.addValue(TableConstants.END_DATE, finishedContractPurchases.getEndDate());
    	
    	return namedParameters;
    }
}
