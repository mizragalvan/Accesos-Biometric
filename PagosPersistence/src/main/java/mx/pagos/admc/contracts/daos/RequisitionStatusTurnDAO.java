package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.RequisitionTrackable;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class RequisitionStatusTurnDAO implements RequisitionTrackable {

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Override
	public boolean getActiveByIdRequistionStatus(final Integer idRequisition, final FlowPurchasingEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindActiveParameters(idRequisition, status);
			List<Boolean> resultado = this.namedjdbcTemplate.queryForList(this.buildFindActiveQuery(), namedParameters, Boolean.class);
			return (resultado != null && !resultado.isEmpty()) ? resultado.get(0) : true;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void updateRequisitionStatusTurnAttentionDaysAndActive(
			final RequisitionStatusTurn requisitionStatusTurn) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createUpdateActiveParameters(requisitionStatusTurn);
			namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
			this.namedjdbcTemplate.update(this.buildUpdateRequisitionStatusTurnAttentionDaysAndStageQuery(),
					namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	
	@Override
	public boolean getByIdRequistionStatus(final Integer idRequisition, final FlowPurchasingEnum status)throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindActiveParameters(idRequisition, status);
			List<Boolean> resultado = this.namedjdbcTemplate.queryForList(this.buildFindIdRequisitionAndStatusQuery(), namedParameters, Boolean.class);
			return (resultado != null && !resultado.isEmpty()) ? true : false;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	private MapSqlParameterSource createFindActiveParameters(final Integer idRequisition,
			final FlowPurchasingEnum status) {
		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue(TableConstants.ID_REQUISITION, idRequisition);
		parameterSource.addValue(TableConstants.STATUS, status.toString());
		return parameterSource;
	}

	private String buildFindActiveQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT Active FROM REQUISITIONSTATUSTURN ");
		query.append("WHERE IdRequisition = :IdRequisition AND Status = :Status ");
		query.append("ORDER BY Turn DESC");
		return query.toString();
	}
	
	private MapSqlParameterSource createUpdateActiveParameters(final RequisitionStatusTurn requisition) {
		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue(TableConstants.ID_REQUISITION, requisition.getIdRequisition().toString());
		parameterSource.addValue(TableConstants.STATUS, requisition.getStatus().toString());
		parameterSource.addValue(TableConstants.TURN, requisition.getTurn().toString());
		parameterSource.addValue(TableConstants.ATTENTION_DAYS, requisition.getAttentionDays().toString());
		parameterSource.addValue(TableConstants.TURN_DATE, new Date());
		return parameterSource;
	}

	private String buildUpdateRequisitionStatusTurnAttentionDaysAndStageQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUISITIONSTATUSTURN ");
		query.append("SET AttentionDays = :AttentionDays, TurnDate = :TurnDate, Active = 1");
		query.append("WHERE IdRequisition = :IdRequisition AND Status = :Status AND Turn = :Turn");
		return query.toString();
	}

	private String buildFindIdRequisitionAndStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdRequisition FROM REQUISITIONSTATUSTURN ");
		query.append("WHERE IdRequisition = :IdRequisition AND Status = :Status ");
		return query.toString();
	}
	
}
