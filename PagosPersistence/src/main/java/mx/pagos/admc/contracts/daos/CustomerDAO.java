package mx.pagos.admc.contracts.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.Customerable;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.structures.Customer;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class CustomerDAO implements Customerable {
    private static final String WHERE_STATUS_STATUS = "WHERE Status = :Status ";
    private static final String FROM_CUSTOMER = "FROM CUSTOMER ";
    private static final String WHERE_ID_EQUALS_ID = "WHERE IdCustomer = :IdCustomer ";
    private static final String UPDATE_CUSTOMER = "UPDATE CUSTOMER SET ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer save(final Customer customer) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(customer);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.saveCustomerQuery(), parameters, keyHolder, 
                    new String[]{"IdCustomer"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String saveCustomerQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO CUSTOMER(CompanyName, Rfc) ");
        builder.append("VALUES(:CompanyName, :Rfc) ");
        return builder.toString();
    }

    @Override
    public Integer update(final Customer customer) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(customer);
            this.namedjdbcTemplate.update(this.updateCustomerQuery(), namedParameters);
            return customer.getIdCustomer();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String updateCustomerQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_CUSTOMER);
        builder.append("CompanyName = :CompanyName, Rfc = :Rfc ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }

    @Override
    public List<Customer> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllCustomerQuery(),
                    new BeanPropertyRowMapper<>(Customer.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findAllCustomerQuery() {
        final StringBuilder builder = new StringBuilder();
        this.selectAllFields(builder);
        builder.append(FROM_CUSTOMER);
        return builder.toString();
    }

    private void selectAllFields(final StringBuilder builder) {
        builder.append("SELECT IdCustomer, CompanyName, Rfc, Status ");
    }
    
    @Override
    public void changeStatus(final Integer idCustomer, final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource(); 
            parameterSource.addValue(TableConstants.ID_CUSTOMER, idCustomer);
            parameterSource.addValue(TableConstants.STATUS, status.toString());
            this.namedjdbcTemplate.update(this.changeCustomerStatusQuery(), parameterSource);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String changeCustomerStatusQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append(UPDATE_CUSTOMER);
        builder.append("Status = :Status ");
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }
    
    @Override
    public List<Customer> findByStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.findCustomersByStatusQuery(), parameterSource, 
                    new BeanPropertyRowMapper<>(Customer.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findCustomersByStatusQuery() {
        final StringBuilder builder = new StringBuilder();
        this.selectAllFields(builder);
        builder.append(FROM_CUSTOMER);
        builder.append(WHERE_STATUS_STATUS);
        return builder.toString();
    }
    
    @Override
    public Customer findById(final Integer idCustomer) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterJdbcTemplate = new MapSqlParameterSource();
            parameterJdbcTemplate.addValue(TableConstants.ID_CUSTOMER, idCustomer);
            return this.namedjdbcTemplate.queryForObject(this.findByIdCustomerQuery(), parameterJdbcTemplate, 
                    new BeanPropertyRowMapper<>(Customer.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String findByIdCustomerQuery() {
        final StringBuilder builder = new StringBuilder();
        this.selectAllFields(builder);
        builder.append(FROM_CUSTOMER);
        builder.append(WHERE_ID_EQUALS_ID);
        return builder.toString();
    }
    
    @Override
    public List<Customer> findByCompanyNameOrRfc(final Customer customer) throws DatabaseException {
        try {
            final MapSqlParameterSource parameterSource = new MapSqlParameterSource(); 
            parameterSource.addValue(TableConstants.COMPANY_NAME, QueryConstants.ANY_CHARACTER +
                    (customer.getCompanyName() == null ? QueryConstants.EMPTY : customer.getCompanyName()) +
                    QueryConstants.ANY_CHARACTER);
            parameterSource.addValue(TableConstants.RFC, QueryConstants.ANY_CHARACTER +
                    (customer.getRfc() == null ? QueryConstants.EMPTY : customer.getRfc()) +
                    QueryConstants.ANY_CHARACTER);
            return this.namedjdbcTemplate.query(this.findByCompanyNameOrRfcQuery(), parameterSource, 
                    new BeanPropertyRowMapper<>(Customer.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findByCompanyNameOrRfcQuery() {
        final StringBuilder builder = new StringBuilder();
        this.selectAllFields(builder);
        builder.append(FROM_CUSTOMER);
        builder.append("WHERE (CompanyName LIKE :CompanyName AND Rfc LIKE :Rfc) AND Status = 'ACTIVE'");
        return builder.toString();
    }
    
    public void deleteById(final Integer idCustomer) throws DatabaseException {
        try {
            final MapSqlParameterSource paramMap = new MapSqlParameterSource();
            paramMap.addValue(TableConstants.ID_CUSTOMER, idCustomer);
            this.namedjdbcTemplate.update(this.deleteByIdQuery(), paramMap);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String deleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM CUSTOMER ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    public Integer countAllRecords() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }
    
    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        return query.toString();
    }

    private void buildCountQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdCustomer) ");
        query.append(FROM_CUSTOMER);
    }
    
    public Integer countRecordsByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue(TableConstants.STATUS, status.toString());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByStatusQuery(), parameterSource, Integer.class);
    }
    
    private String countRecordsByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append(WHERE_STATUS_STATUS);
        return query.toString();
    }

    @Override
    public List<Customer> findAllCustomerCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllCustomerCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<Customer>(Customer.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllCustomerCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.selectAllFields(builder);
        builder.append(FROM_CUSTOMER);
        builder.append("WHERE :Status IS NULL OR Status = :Status ");
        builder.append("ORDER BY CompanyName ASC ");
        return builder.toString();
    }

    private MapSqlParameterSource statusParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        return source;
    }

    @Override
    public Long countTotalItemsToShowOfCustomer(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllCustomerCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
