package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Holidayable;
import mx.pagos.admc.contracts.structures.Holiday;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class HolidayDAO implements Holidayable {
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Override
    public void save(final Holiday holiday) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(holiday);
            this.namedjdbcTemplate.update(this.buildInsertHolidayQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void deleteAll() throws DatabaseException {
        try {
            this.namedjdbcTemplate.update(this.builDeleteAllHolidaysQuery(), new MapSqlParameterSource());
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Holiday> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(), new BeanPropertyRowMapper<>(Holiday.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Date> findAllDates() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.queryForList(this.buildFindAllDatesQuery(),
                    new MapSqlParameterSource(), Date.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public Holiday findByDate(final String date) {
        final MapSqlParameterSource namedParameters = this.createfindByDatenamedParameters(date);
        return this.namedjdbcTemplate.queryForObject(this.buildFindByDate(), namedParameters,
                new BeanPropertyRowMapper<>(Holiday.class));
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.countAllQuery(), new MapSqlParameterSource(), Integer.class);
    }

    private String buildInsertHolidayQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO HOLIDAY (Date, Description) VALUES (:Date, :Description)");
        return query.toString();
    }
    
    private String builDeleteAllHolidaysQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM HOLIDAY");
        return query.toString();
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT Date, Description FROM HOLIDAY ");
    }
    
    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
    
    private String buildFindByDate() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE Date = :Date");
        return query.toString();
    }
    
    private MapSqlParameterSource createfindByDatenamedParameters(
            final String date) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.DATE, date, Types.VARCHAR);
        return namedParameters;
    }
    
    private String countAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(Date) FROM HOLIDAY");
        return query.toString();
    }
    
    private String buildFindAllDatesQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Date FROM HOLIDAY");
        return query.toString();
    }
}
