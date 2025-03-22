package mx.pagos.admc.contracts.daos;

import static mx.pagos.admc.contracts.constants.TableConstants.ID_FLOW;
import static mx.pagos.admc.contracts.constants.TableConstants.ISUSERFILTERED;
import static mx.pagos.admc.contracts.constants.TableConstants.TABLE_PROFILESCREENFLOW;
import static mx.pagos.general.constants.QueryConstants.ASTERISK;
import static mx.pagos.general.constants.QueryConstants.COMMA;
import static mx.pagos.general.constants.QueryConstants.COMMA_TAG;
import static mx.pagos.general.constants.QueryConstants.DELETE_FROM;
import static mx.pagos.general.constants.QueryConstants.EQUAL_TAG;
import static mx.pagos.general.constants.QueryConstants.FROM;
import static mx.pagos.general.constants.QueryConstants.INSERT_INTO;
import static mx.pagos.general.constants.QueryConstants.LEFT_BRACES;
import static mx.pagos.general.constants.QueryConstants.RIGHT_BRACES;
import static mx.pagos.general.constants.QueryConstants.SELECT;
import static mx.pagos.general.constants.QueryConstants.VALUES_TAG;
import static mx.pagos.general.constants.QueryConstants.WHERE;
import static mx.pagos.security.constants.TableConstantsSecurity.FACTORY_NAME;
import static mx.pagos.security.constants.TableConstantsSecurity.ID_PROFILE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.interfaces.ProfileScreenFlowable;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.ProfileScreenFlow;

@Repository
public class ProfileScreenFlowDAO implements ProfileScreenFlowable {

	private static final Logger log = LoggerFactory.getLogger(ProfileScreenFlowDAO.class);

	@Autowired
	private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Override
	public List<ProfileScreenFlow> findFlowScreenActionByProfile(ProfileScreenFlow bean) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(bean);
			return this.namedjdbcTemplate.query(this.buildFindByIdQuery(), namedParameters,
					new BeanPropertyRowMapper<ProfileScreenFlow>(ProfileScreenFlow.class));
		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? cause
					: dataAccessException;
			throw new DatabaseException("Error al obtener la informacion", exception);
		}
	}

	@Override
	public void saveProfileScreenFlow(final ProfileScreenFlow profileScreen) throws DatabaseException {
		log.info("Se inicia insersion de bandeja");
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(profileScreen);
			this.namedjdbcTemplate.update(this.buildInsertScreenProfileQuery(), namedParameters);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new DatabaseException("Error al agregar el menu al perfil", e);
		}
	}

	@Override
	public void deleteProfileScreenFlowByIdProfile(final Integer idProfile) throws DatabaseException {
		log.info("Se inicia eliminacion de pantallas del flujo de perfiles por id de perfil");
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(ID_PROFILE, idProfile);
			this.namedjdbcTemplate.update(this.buildDeleteScreenProfileByIdProfileQuery(), namedParameters);
		} catch (DataAccessException e) {
			log.error(e.getMessage());
			throw new DatabaseException("Error al eliminar los perfiles por usuario", e);
		}
	}

	private String buildSelectAllQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(SELECT).append(ASTERISK);
		query.append(FROM).append(TABLE_PROFILESCREENFLOW);
		return query.toString();
	}

	private String buildFindByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(this.buildSelectAllQuery());
		query.append(WHERE).append(ID_PROFILE).append(EQUAL_TAG).append(ID_PROFILE);
		return query.toString();
	}

	private String buildInsertScreenProfileQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(INSERT_INTO).append(TABLE_PROFILESCREENFLOW).append(LEFT_BRACES);
		query.append(ID_PROFILE).append(COMMA);
		query.append(FACTORY_NAME).append(COMMA);
		query.append(ID_FLOW).append(COMMA);
		query.append(ISUSERFILTERED).append(RIGHT_BRACES).append(VALUES_TAG);
		query.append(ID_PROFILE).append(COMMA_TAG);
		query.append(FACTORY_NAME).append(COMMA_TAG);
		query.append(ID_FLOW).append(COMMA_TAG);
		query.append(ISUSERFILTERED).append(RIGHT_BRACES);
		return query.toString();
	}

	private String buildDeleteScreenProfileByIdProfileQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM PROFILESCREENFLOW WHERE IdProfile = :IdProfile");
		return query.toString();
	}

}