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
import mx.pagos.admc.contracts.interfaces.owners.PublicFigurable;
import mx.pagos.admc.contracts.structures.owners.PublicFigure;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.owners.PublicFigureTypeEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Repository
public class PublicFigureDAO implements PublicFigurable {
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status";
    private static final String WHERE_ID_PUBLIC_FIGURE_EQUALS_ID_PUBLIC_FIGURE =
            "WHERE IdPublicFigure = :IdPublicFigure";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    @Autowired
    private DatabaseUtils databaseUtils;
    

    
    @Override
    public Integer saveOrUpdate(final PublicFigure publicFigure) throws DatabaseException {
        return publicFigure.getIdPublicFigure() == null ? this.insert(publicFigure) : this.update(publicFigure);
    }

    private Integer insert(final PublicFigure publicFigure) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.publicFigureParameters(publicFigure);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, 
                    new String[]{"IdPublicFigure"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private MapSqlParameterSource publicFigureParameters(final PublicFigure publicFigure) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_PUBLIC_FIGURE, publicFigure.getIdPublicFigure());
        namedParameters.addValue(TableConstants.NAME, publicFigure.getName());
        namedParameters.addValue(TableConstants.TYPE, publicFigure.getType().toString());
        return namedParameters;
    }

    private String buildInsertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO PUBLICFIGURE (Name, Type) VALUES (:Name, :Type)");
        return query.toString();
    }

    private Integer update(final PublicFigure publicFigure) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.publicFigureParameters(publicFigure);
            this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
            return publicFigure.getIdPublicFigure();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildUpdateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE PUBLICFIGURE SET Name = :Name, Type = :Type ");
        query.append(WHERE_ID_PUBLIC_FIGURE_EQUALS_ID_PUBLIC_FIGURE);
        return query.toString();
    }
    
    @Override
    public void changeStatus(final Integer idPublicFigure, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(new PublicFigure(idPublicFigure, status));
            namedParameters.registerSqlType(TableConstants.STATUS, Types.VARCHAR);
            this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildChangeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE PUBLICFIGURE SET Status = :Status ");
        query.append(WHERE_ID_PUBLIC_FIGURE_EQUALS_ID_PUBLIC_FIGURE);
        return query.toString();
    }
    
    @Override
    public PublicFigure findById(final Integer idPublicFigure) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idPublicFigure);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(PublicFigure.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdPublicFigure, Name, Type, Status FROM PUBLICFIGURE ");
    }

    private String buildFindByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_PUBLIC_FIGURE_EQUALS_ID_PUBLIC_FIGURE);
        return query.toString();
    }

    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idPublicFigure) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_PUBLIC_FIGURE, idPublicFigure);
        return namedParameters;
    }
    
    @Override
    public List<PublicFigure> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
                    new BeanPropertyRowMapper<>(PublicFigure.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
    
    @Override
    public List<PublicFigure> findByStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(PublicFigure.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
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
    
    @Override
    public List<PublicFigure> findByType(final PublicFigureTypeEnum type) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByTypeNamedParameters(type);
            return this.namedjdbcTemplate.query(this.buildFindByTypeQuery(), namedParameters,
                    new BeanPropertyRowMapper<>(PublicFigure.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildFindByTypeQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append("WHERE Type = :Type");
        return query.toString();
    }

    private MapSqlParameterSource createFindByTypeNamedParameters(
            final PublicFigureTypeEnum type) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.TYPE, type.toString());
        return namedParameters;
    }

    @Override
    public List<PublicFigure> findAllPublicFigureCatalogPaged(final RecordStatusEnum status, 
            final PublicFigureTypeEnum type, final Integer pagesNumber, final Integer itemsNumber) 
                    throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status, type);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_PUBLIC_FIGURE,
                    this.findAllPublicFigureCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<PublicFigure>(PublicFigure.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllPublicFigureCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildSelectAllQuery(builder);
        builder.append("WHERE (:Status IS NULL OR Status = :Status) ");
        builder.append("AND (:Type IS NULL OR Type = :Type) ");
        builder.append("ORDER BY Name ASC ");
        return builder.toString();
    }

    private MapSqlParameterSource statusParameter(final RecordStatusEnum status, final PublicFigureTypeEnum type) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, status == null ? null : status.toString());
        source.addValue(TableConstants.TYPE, type == null ? null : type.toString());
        return source;
    }

    @Override
    public Long countTotalItemsToShowOfPublicFigure(final RecordStatusEnum status,
            final PublicFigureTypeEnum type) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status, type);
            final String countItems = this.databaseUtils.countTotalRows(this.findAllPublicFigureCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
