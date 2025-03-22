package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.HolidaysUserable;
import mx.pagos.admc.contracts.structures.HolidayUser;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class HolidayUserDAO implements HolidaysUserable {

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	
	@Override
    public Integer save(final HolidayUser holidayUser) throws DatabaseException {
		try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(holidayUser);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertUserQuery(), namedParameters, keyHolder, 
                    new String[]{"IdHolidayUser"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }		
	}
	
	@Override
    public void deleteHolidayUser(final Integer idHolidayUser) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdHolidayUser(idHolidayUser);
	        this.namedjdbcTemplate.update(this.buildDeletePersonalityByIdQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}
	
	@Override
	public List<HolidayUser> findByIdUser(final Integer idUser) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters = this.createFindByIdUserNamedParameters(idUser);
	        return this.namedjdbcTemplate.query(this.buildFindByIdUserQuery(), namedParameters,
	                new BeanPropertyRowMapper<>(HolidayUser.class));
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	@Override
	public void deleteHolidaysUserByIdUser(final Integer idUser) throws DatabaseException {
	    try {
	        final MapSqlParameterSource namedParameters = this.createFindByIdUserNamedParameters(idUser);
	        this.namedjdbcTemplate.update(this.buildDeleteHolidaysUserByIdUserQuery(), namedParameters);
	    } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}

    public HolidayUser findById(final Integer idHolidayUser) throws EmptyResultException, DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdHolidayUser(idHolidayUser);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<HolidayUser>(HolidayUser.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
	private String buildInsertUserQuery() {
	    final StringBuilder query = new StringBuilder();
	    query.append("INSERT INTO HOLIDAYUSER(IdUser,StartDate,EndDate) VALUES (:IdUser, :StartDate,:EndDate)");
	    return query.toString();
	}
	
	private String buildDeletePersonalityByIdQuery() {
	    final StringBuilder query = new StringBuilder();
	    query.append("DELETE FROM HOLIDAYUSER WHERE IdHolidayUser = :IdHolidayUser");
	    return query.toString();
	}
	
	private String buildFindByIdUserQuery() {
	    final StringBuilder query = new StringBuilder();
	    this.buildSelectAllQuery(query);
	    query.append("WHERE IdUser = :IdUser");
	    return query.toString();
	}
	
	private MapSqlParameterSource createFindByIdUserNamedParameters(final Integer idUser) {
	    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue(TableConstants.ID_USER, idUser);
	    return namedParameters;
	}
	
	private String buildDeleteHolidaysUserByIdUserQuery() {
	    final StringBuilder query = new StringBuilder();
	    query.append("DELETE FROM HOLIDAYUSER WHERE IdUser = :IdUser");
	    return query.toString();
	}
	
	public String buildFindByIdQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append("WHERE IdHolidayUser = :IdHolidayUser");
		return query.toString();
	}

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdHolidayUser, IdUser, StartDate, EndDate ");
		query.append("FROM HOLIDAYUSER ");
    }
	
	private MapSqlParameterSource createFindByIdHolidayUser(
	        final Integer idHolidayUser) {
	    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue(TableConstants.ID_HOLIDAY_USER, idHolidayUser);
	    return namedParameters;
	}

    @Override
    public List<HolidayUser> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
                    new BeanPropertyRowMapper<>(HolidayUser.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
}
