package mx.pagos.admc.contracts.daos.owners;

import java.sql.Types;
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
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.owners.OrganizationEntitiable;
import mx.pagos.admc.contracts.structures.owners.OrganizationEntity;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class OrganizationEntityDAO implements OrganizationEntitiable {
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status";
    private static final String WHERE_ID_EQUALS_ID = "WHERE IdOrganizationEntity = :IdOrganizationEntity";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    @Autowired
    private DatabaseUtils databaseUtils;
    


    @Override
    public Integer saveOrUpdate(final OrganizationEntity organizationEntity)
            throws DatabaseException {
        return organizationEntity.getIdOrganizationEntity() == null ?
                this.insertOrganizationEntity(organizationEntity) : this.updateOrganizationEntity(organizationEntity);
    }

    private Integer insertOrganizationEntity(final OrganizationEntity organizationEntity) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(organizationEntity);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertOrganizationEntityQuery(), namedParameters, keyHolder, 
                    new String[]{"IdOrganizationEntity"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private Integer updateOrganizationEntity(final OrganizationEntity organizationEntity) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(organizationEntity);
            this.namedjdbcTemplate.update(this.buildUpdateOrganizationEntityQuery(), namedParameters);
            return organizationEntity.getIdOrganizationEntity();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changeStatus(final Integer idOrganizationEntity, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    this.createChangeStatusNamedParameters(idOrganizationEntity, status);
            this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public OrganizationEntity findById(final Integer idOrganizationEntity)
            throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idOrganizationEntity);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(OrganizationEntity.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<OrganizationEntity> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
                    new BeanPropertyRowMapper<>(OrganizationEntity.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<OrganizationEntity> findByStatus(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(OrganizationEntity.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteById(final Integer idOrganizationEntity) {
        final OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setIdOrganizationEntity(idOrganizationEntity);
        final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(organizationEntity);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(), namedParameters, Integer.class);
    }

    private String buildInsertOrganizationEntityQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ORGANIZATIONENTITY (Name) VALUES (:Name)");
        return query.toString();
    }
    
    private String buildUpdateOrganizationEntityQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE ORGANIZATIONENTITY SET Name = :Name ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private String buildChangeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE ORGANIZATIONENTITY SET Status = :Status ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private BeanPropertySqlParameterSource createChangeStatusNamedParameters(
            final Integer idOrganizationEntity, final RecordStatusEnum status) {
        final OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setIdOrganizationEntity(idOrganizationEntity);
        organizationEntity.setStatus(status);
        final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(organizationEntity);
        namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
        return namedParameters;
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdOrganizationEntity, Name, Status ");
        query.append("FROM ORGANIZATIONENTITY ");
    }
    
    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idOrganizationEntity) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_ORGANIZATION_ENTITY, idOrganizationEntity);
        return namedParameters;
    }
    
    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
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
    
    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ORGANIZATIONENTITY ");
        query.append(WHERE_ID_EQUALS_ID);
        return query.toString();
    }
    
    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) FROM ORGANIZATIONENTITY ");
        return query.toString();
    }
    
    private String buildCountByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(this.buildCountAllQuery());
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }

    @Override
    public List<OrganizationEntity> findAllOrganizationEntityCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllOrganizationEntityCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<OrganizationEntity>(OrganizationEntity.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllOrganizationEntityCatalogPagedQuery() {
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
    public Long countTotalItemsToShowOfOrganizationEntity(
            final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String countItems = 
                    this.databaseUtils.countTotalRows(this.findAllOrganizationEntityCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
