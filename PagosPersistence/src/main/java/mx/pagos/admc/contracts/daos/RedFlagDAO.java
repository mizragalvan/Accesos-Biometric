package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.RedFlagable;
import mx.pagos.admc.contracts.structures.RedFlag;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class RedFlagDAO implements RedFlagable {

	private static final String FROM_REDFLAGS = QueryConstants.FROM + TableConstants.TABLE_REDFLAGS;
	private static final String RED = " r.";
	private static final String US = " u.";

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;


	@Override
	public Integer save(final RedFlag redFlag) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createInsertCommentNamedParameters(redFlag);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertCommentQuery(), namedParameters, keyHolder,
					new String[] { "IdRedFlag" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar redFlag", dataAccessException);
		}
	}

	@Override
	public List<RedFlag> findByIdRequisition(Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdRequisition(idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindByIdRequisitionQuery(), namedParameters,
					new BeanPropertyRowMapper<RedFlag>(RedFlag.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener: redflats", dataAccessException);
		}
	}

	private MapSqlParameterSource createInsertCommentNamedParameters(final RedFlag redFlag) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, redFlag.getIdRequisition());
		namedParameters.addValue(TableConstants.COMMENT_TEXT, redFlag.getCommentText());
		namedParameters.addValue(TableConstants.ID_USER, redFlag.getIdUser());
		return namedParameters;
	}

	private String buildInsertCommentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_REDFLAGS + QueryConstants.LEFT_BRACES);
		this.buildInsertFieldsQuery(query);
		query.append(QueryConstants.RIGHT_BRACES + QueryConstants.VALUES_TAG);
		query.append(TableConstants.ID_REQUISITION + QueryConstants.COMMA_TAG);
		query.append(TableConstants.COMMENT_TEXT + QueryConstants.COMMA_TAG);
		query.append(TableConstants.ID_USER).append(QueryConstants.RIGHT_BRACES);
		return query.toString();
	}

	private void buildInsertFieldsQuery(final StringBuilder query) {
		query.append(TableConstants.ID_REQUISITION + QueryConstants.COMMA);
		query.append(TableConstants.COMMENT_TEXT + QueryConstants.COMMA);
		query.append(TableConstants.ID_USER).append(QueryConstants.SPACE);
	}

	private MapSqlParameterSource createFindByIdRequisition(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		return namedParameters;
	}

	private String buildFindByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.SELECT);
		query.append(RED + TableConstants.ID_REDFLAG + QueryConstants.COMMA);
		query.append(RED + TableConstants.ID_REQUISITION + QueryConstants.COMMA);
		query.append(RED + TableConstants.COMMENT_TEXT + QueryConstants.COMMA);
		query.append(RED + TableConstants.CREATION_DATE + QueryConstants.COMMA);
		query.append(RED + TableConstants.ID_USER + QueryConstants.COMMA);
		query.append(" (u.Name + ' ' + u.FirstLastName + isnull(' ' + u.SecondLastName, '')) AS UserName ");
		query.append(FROM_REDFLAGS + "AS r ");
		query.append(" INNER JOIN " + TableConstants.TABLE_USER + QueryConstants.SPACE + " AS u ");
		query.append(" ON u.IdUser = r.IdUser ");
		query.append("WHERE IdRequisition = :IdRequisition ");
		query.append(" ORDER BY CreationDate DESC");
		return query.toString();
	}

}
