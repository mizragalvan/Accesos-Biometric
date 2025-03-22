package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.FinishedContractPurchasesable;
import mx.pagos.admc.contracts.structures.FinishedContractPurchases;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class FinishedContractPurchasesDAO implements FinishedContractPurchasesable {

	private static final String PERCENTAGE = "%";
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Override
	public List<FinishedContractPurchases> findFinishedContractsPurchases(
			final FinishedContractPurchases vo) throws DatabaseException {
	    try {
			final MapSqlParameterSource namedParameters = 
					this.createFindFinishedContractsPurchasesParameters(vo);
			return this.namedjdbcTemplate.query(this.buildFindFinishedContractsPurchasesQuery(), 
					namedParameters, new BeanPropertyRowMapper<>(FinishedContractPurchases.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	private String buildFindFinishedContractsPurchasesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT R.IdRequisition, S.CompanyName supplierName, DT.Name documentTypeName, ");
		query.append("R.ApplicationDate dateOfAdmission, R.SignDate signatureDate, R.Status ");
		query.append("FROM REQUISITION R ");
		query.append("INNER JOIN SUPPLIER S ON R.IdSupplier = S.IdSupplier ");
		query.append("INNER JOIN DOCUMENTTYPE DT ON R.IdDocumentType = DT.IdDocumentType ");
		query.append("WHERE (:StartDate IS NULL OR :EndDate IS NULL ");
		query.append("OR CAST(R.ApplicationDate AS DATE) BETWEEN :StartDate AND :EndDate ");
		query.append("OR R.SignDate BETWEEN :StartDate AND :EndDate) ");
		query.append("AND (:IdRequisition IS NULL OR R.IdRequisition = :IdRequisition) ");
		query.append("AND (:IdDocumentType IS NULL OR R.IdDocumentType = :IdDocumentType) ");
        query.append("AND (UPPER(S.CommercialName) LIKE UPPER(:SupplierName) OR UPPER(S.CompanyName) ");
        query.append("LIKE UPPER(:SupplierName)) ");
		query.append("AND R.Status IN ('REQUISITION_CLOSE', 'CANCELED_CONTRACT') ");
		query.append("ORDER BY IdRequisition DESC");
		return query.toString();
	}
	
    private MapSqlParameterSource createFindFinishedContractsPurchasesParameters(
    		final FinishedContractPurchases finishedContractPurchases) {
    	
    	final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
    	final String supplierName = finishedContractPurchases.getSupplierName() == null ? ""
    			: finishedContractPurchases.getSupplierName();
    	
    	namedParameters.addValue(TableConstants.ID_REQUISITION, finishedContractPurchases.getIdRequisition());
    	namedParameters.addValue(TableConstants.ID_DOCUMENT_TYPE, finishedContractPurchases.getIdDocumentTypeName());
    	namedParameters.addValue(TableConstants.SUPPLIER_NAME, PERCENTAGE + supplierName + PERCENTAGE);
    	namedParameters.registerSqlType(TableConstants.START_DATE, Types.VARCHAR);
    	namedParameters.addValue(TableConstants.START_DATE, finishedContractPurchases.getStartDate());
    	namedParameters.registerSqlType(TableConstants.END_DATE, Types.VARCHAR);
    	namedParameters.addValue(TableConstants.END_DATE, finishedContractPurchases.getEndDate());
    	
    	return namedParameters;
    }
}
