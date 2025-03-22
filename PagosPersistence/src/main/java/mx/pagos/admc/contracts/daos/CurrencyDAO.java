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
import mx.pagos.admc.contracts.interfaces.Currenciable;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.structures.Currency;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class CurrencyDAO implements Currenciable {
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status";
    private static final String WHERE_ID_CURRENCY_EQUALS_ID_CURENCY = "WHERE IdCurrency = :IdCurrency";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer saveOrUpdate(final Currency currency) throws DatabaseException {
        return currency.getIdCurrency() == null ? this.insertCurrency(currency) : this.updateCurrency(currency);
    }

    private Integer insertCurrency(final Currency currency) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(currency);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertCurrencyQuery(), namedParameters, keyHolder, 
                    new String[]{"IdCurrency"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private Integer updateCurrency(final Currency currency) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(currency);
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return currency.getIdCurrency();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Currency findById(final Integer idCurrency) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idCurrency);
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(),
                    namedParameters, new BeanPropertyRowMapper<>(Currency.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Currency> findByStatus(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(Currency.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<Currency> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
                    new BeanPropertyRowMapper<>(Currency.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changeStatus(final Integer idCurrency, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createChangeStatusNamedParameters(idCurrency, status);
            this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    public void deleteById(final Integer idCurrency) {
        final MapSqlParameterSource namedParematers = this.createFindByIdNamedParameters(idCurrency);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParematers);
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(), namedParameters, Integer.class);
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }

    private String buildInsertCurrencyQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO CURRENCY (Name) VALUES (:Name)");
        return query.toString();
    }
    
    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE CURRENCY SET Name = :Name ");
        query.append(WHERE_ID_CURRENCY_EQUALS_ID_CURENCY);
        return query.toString();
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdCurrency, Name, Status FROM CURRENCY ");
    }
    
    private String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_CURRENCY_EQUALS_ID_CURENCY);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idCurrency) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CURRENCY, idCurrency);
        return namedParameters;
    }
    
    private String buildFindByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
    
    private String buildChangeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE CURRENCY SET Status = :Status ");
        query.append(WHERE_ID_CURRENCY_EQUALS_ID_CURENCY);
        return query.toString();
    }
    
    private MapSqlParameterSource createChangeStatusNamedParameters(
            final Integer idCurrency, final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_CURRENCY, idCurrency);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM CURRENCY ");
        query.append(WHERE_ID_CURRENCY_EQUALS_ID_CURENCY);
        return query.toString();
    }
    
    private void buildSelectCountAllQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdCurrency) FROM CURRENCY ");
    }
    
    private String buildCountByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountAllQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountAllQuery(query);
        return query.toString();
    }

    @Override
    public List<Currency> findAllCurrencyCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllCurrencyCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<Currency>(Currency.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllCurrencyCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildSelectAllQuery(builder);
        builder.append("WHERE :Status IS NULL OR Status = :Status ");
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }

    private MapSqlParameterSource statusParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        return source;
    }
    
    @Override
    public Long countTotalItemsToShowOfPower(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllCurrencyCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
