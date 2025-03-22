package mx.pagos.admc.contracts.daos;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Qr;
import mx.pagos.admc.contracts.structures.QuickResponse;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class QrDAO implements Qr {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	private DatabaseUtils databaseUtils;

	private static final String NULL = "Null";
	private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
	private static final String SINGLE_QUOTE = "'";


	public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
		this.databaseUtils = databaseUtilsParameter;
	}

	@Override
	public Integer saveQr(final QuickResponse quickResponse, String qrStr) {
		Integer resp = 0;
		try {
			final MapSqlParameterSource namedParameters = this.createInsertQRNamedParameters(quickResponse, qrStr);
			final KeyHolder keyHolder = new GeneratedKeyHolder();					
			this.namedjdbcTemplate.update(this.buildInsertQRQuery(), namedParameters, keyHolder,
				new String[] { "IdRequisitionQR" });
			//return quickResponse.getIdRequisition();
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			return resp;
		}		
	}

	private MapSqlParameterSource createInsertQRNamedParameters(final QuickResponse qr, String qrStr) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.QR, qrStr);
		namedParameters.addValue(TableConstants.SALT, qr.getSalt());
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);
		final String formatedTodayDate = toDateTimeFormat.format(new Date());
		namedParameters.addValue(TableConstants.DATE_CREATION, formatedTodayDate);
		namedParameters.addValue(TableConstants.ID_REQUISITION, qr.getIdRequisition());
		return namedParameters;
	}

	private String buildInsertQRQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_REQUISITION_QR + QueryConstants.LEFT_BRACES);
		this.buildInsertFieldsQuery(query);
		query.append(QueryConstants.RIGHT_BRACES + QueryConstants.VALUES_TAG);
		query.append(TableConstants.QR + QueryConstants.COMMA_TAG);
		query.append(TableConstants.SALT + QueryConstants.COMMA_TAG);
		query.append(TableConstants.DATE_CREATION + QueryConstants.COMMA_TAG);
		query.append(TableConstants.ID_REQUISITION).append(QueryConstants.RIGHT_BRACES);
		return query.toString();
	}

	private void buildInsertFieldsQuery(final StringBuilder query) {
		query.append(TableConstants.QR + QueryConstants.COMMA);
		query.append(TableConstants.SALT + QueryConstants.COMMA);
		query.append(TableConstants.DATE_CREATION + QueryConstants.COMMA);
		query.append(TableConstants.ID_REQUISITION).append(QueryConstants.SPACE);
	}

	@Override
	public QuickResponse findBySalt(final String salt) throws DatabaseException, EmptyResultException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindBySaltParameters(salt);
			String sql = this.buildFindQRBySaltQuery();
			return this.namedjdbcTemplate.queryForObject(sql, namedParameters,
					new BeanPropertyRowMapper<QuickResponse>(QuickResponse.class));
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createFindBySaltParameters(final String salt) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.SALT, salt);
		return namedParameters;
	}

	private String buildFindQRBySaltQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdRequisitionQR, Qr, salt, DateCreation, IdRequisition, status ");
		query.append("FROM REQUISITION_QR where salt=:salt COLLATE Latin1_General_CS_AS");
		return query.toString();
	}

}
