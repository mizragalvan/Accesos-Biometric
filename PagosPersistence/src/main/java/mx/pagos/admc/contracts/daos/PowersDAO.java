package mx.pagos.admc.contracts.daos;

import java.util.List;

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
import mx.pagos.admc.contracts.interfaces.Powerable;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class PowersDAO implements Powerable {
    private static final String SELECT_COUNT_LEFT_BRACES = "SELECT COUNT(";
    private static final String WHERE = "WHERE";
    private static final String SPACE = " ";
    private static final String UPDATE_POWER_SET = "UPDATE POWER SET ";
    private static final String COMMA = ", ";
    private static final String EQUAL_TAG = " = :";
    private static final String FROM_POWER = "FROM" + SPACE + TableConstants.TABLE_POWER + SPACE;
    private static final String WHERE_ID_POWER_EQUALS_ID_POWER_PARAM = WHERE + SPACE + TableConstants.ID_POWER + 
            EQUAL_TAG + TableConstants.ID_POWER;
    private static final String COMMA_TAG = ", :";
    private static final String POWER_DOT = TableConstants.TABLE_POWER.trim() + QueryConstants.DOT;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer saveOrUpdate(final Power power) throws DatabaseException {
        return power.getIdPower() == null ? this.insertPower(power) : this.updatepower(power);
    }

    private Integer updatepower(final Power power) throws DatabaseException {
        try {
            final MapSqlParameterSource nameParameter = this.createSaveOrUpdateNameParameter(power);
            this.namedjdbcTemplate.update(this.buildUpdatePowerQuery(), nameParameter);
            return power.getIdPower();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al Actualizar un Poder", dataAccessException);
        }
        
    }

    private MapSqlParameterSource createSaveOrUpdateNameParameter(final Power power) {
        final MapSqlParameterSource nameParameters = new MapSqlParameterSource();
        this.setPowerNameParameter(power, nameParameters);
        return nameParameters;
    }
    
    private String buildUpdatePowerQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_POWER_SET);
        query.append(TableConstants.NAME + EQUAL_TAG + TableConstants.NAME + QueryConstants.COMMA);
        query.append(TableConstants.ID_FINANCIALENTITY + EQUAL_TAG + TableConstants.ID_FINANCIALENTITY);
        query.append(", AlternativePower = :AlternativePower ");
        query.append(WHERE_ID_POWER_EQUALS_ID_POWER_PARAM);
        return query.toString();
    }

    private void setPowerNameParameter(final Power power, final MapSqlParameterSource nameParameters) {
        nameParameters.addValue(TableConstants.ID_POWER, power.getIdPower());
        nameParameters.addValue(TableConstants.ID_FINANCIALENTITY,
                power.getIdFinancialEntity());
        nameParameters.addValue(TableConstants.NAME, power.getName());
        nameParameters.addValue(TableConstants.ALTERNATIVE_POWER, power.getAlternativePower());
    }

    private Integer insertPower(final Power power) throws DatabaseException {
        try {
            final MapSqlParameterSource nameParameters = this.createSaveOrUpdateNameParameter(power);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertPowerQuery(power), nameParameters, keyHolder, 
                    new String[]{"IdPower"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al Insertar Poder", dataAccessException);
        }
        
    }

    private String buildInsertPowerQuery(final Power power) {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.INSERT_INTO + TableConstants.TABLE_POWER + QueryConstants.LEFT_BRACES);
        this.addAllNonPrimaryKeyFields(query);
        query.append(QueryConstants.RIGHT_BRACES + QueryConstants.VALUES);
        query.append(QueryConstants.TAG + TableConstants.ID_FINANCIALENTITY + COMMA_TAG);
        query.append(TableConstants.NAME + COMMA);
        query.append(TableConstants.ACTIVE).append(", :AlternativePower").append(QueryConstants.RIGHT_BRACES);
        return query.toString();
    }
    
    private void addAllNonPrimaryKeyFields(final StringBuilder query) {
        query.append(POWER_DOT + TableConstants.ID_FINANCIALENTITY + COMMA + POWER_DOT + TableConstants.NAME + COMMA);
        query.append(POWER_DOT + TableConstants.STATUS).append(", POWER.AlternativePower ");
    }

    @Override
    public void changePowerStatus(final Integer idPower, final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource nameParameter = this.createChangeStatusPowerNameParemeter(idPower, status);
            this.namedjdbcTemplate.update(this.buildChangeStatusPowerStatus(), nameParameter);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar status", dataAccessException);
        }
    }
    
    private MapSqlParameterSource createChangeStatusPowerNameParemeter(final Integer idPower, 
            final RecordStatusEnum status) {
        final MapSqlParameterSource nameParameter = new MapSqlParameterSource();
        nameParameter.addValue(TableConstants.STATUS, status.toString());
        nameParameter.addValue(TableConstants.ID_POWER, idPower);
        return nameParameter;
    }

    private String buildChangeStatusPowerStatus() {
        final StringBuilder query = new StringBuilder();
        query.append(UPDATE_POWER_SET + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS + SPACE);
        query.append(WHERE_ID_POWER_EQUALS_ID_POWER_PARAM);
        return query.toString();
    }

    @Override
    public List<Power> findAll() throws DatabaseException {
        try {
            final StringBuilder query = this.buildFindAllQuery();
            return this.namedjdbcTemplate.query(query.toString(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<Power>(Power.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener todo", dataAccessException);
        }
    }
    
	@Override
	public List<Power> findByIdFinancialEntityAndIdLegalRepresentative(final Integer idFinancialEntity,
			final Integer idLegalRepresentative) throws DatabaseException {
        try {
            final MapSqlParameterSource nameParameters = new MapSqlParameterSource();
            nameParameters.addValue(TableConstants.ID_FINANCIALENTITY, idFinancialEntity);
            nameParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, idLegalRepresentative);
            final StringBuilder query = new StringBuilder();
            query.append("SELECT P.IdPower, P.IdFinancialEntity, P.Name, P.Status, P.AlternativePower ");
            query.append("FROM LEGALREPRESENTATIVEPOWER LRP ");
            query.append("INNER JOIN POWER P ON P.IdPower = LRP.IdPower ");
            query.append("WHERE LRP.IdFinancialEntity =:IdFinancialEntity ");
            query.append("AND LRP.IdLegalRepresentative =:IdLegalRepresentative ");
            return this.namedjdbcTemplate.query(query.toString(), nameParameters, 
            		new BeanPropertyRowMapper<Power>(Power.class));
        } catch (DataAccessException accessException) {
            throw new DatabaseException("Error al buscar por entidad y representante legal ",
            		accessException);
        }
	}
    
	@Override
	public List<Power> findByIdFinancialEntity(final Integer idFinancialEntity) throws DatabaseException {
        try {
            final MapSqlParameterSource nameParameters = new MapSqlParameterSource();
            nameParameters.addValue(TableConstants.ID_FINANCIALENTITY, idFinancialEntity);
            final StringBuilder query = new StringBuilder();
            query.append("SELECT * FROM POWER WHERE IdFinancialEntity =:IdFinancialEntity AND STATUS = 'ACTIVE' ");
            return this.namedjdbcTemplate.query(query.toString(), nameParameters, 
            		new BeanPropertyRowMapper<Power>(Power.class));
        } catch (DataAccessException accessException) {
            throw new DatabaseException("Error al buscar por entidad", accessException);
        }
	}
    
    @Override
    public List<Power> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final StringBuilder query = new StringBuilder();
            this.buildFindByStatus(query);
            final MapSqlParameterSource nameParameters = this.createFindByStatusNameParameter(status);
            return this.namedjdbcTemplate.query(query.toString(), nameParameters,
                    new BeanPropertyRowMapper<Power>(Power.class));
        } catch (DataAccessException accessException) {
            throw new DatabaseException("Error al buscar por status", accessException);
        }
    }

    private void buildFindByStatus(final StringBuilder query) {
        this.buildSelectAllQuery(query);
        this.buildWhereSatusQuery(query);
    }

    private void buildWhereSatusQuery(final StringBuilder query) {
        query.append(WHERE + SPACE + POWER_DOT + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS);
    }

    private MapSqlParameterSource createFindByStatusNameParameter(final RecordStatusEnum status) {
        final MapSqlParameterSource nameParameter = new MapSqlParameterSource();
        nameParameter.addValue(TableConstants.STATUS, status.toString());
        return nameParameter;
    }

    @Override
    public Power findByIdPower(final Integer idPower) throws DatabaseException {
        try {
        	final MapSqlParameterSource nameParameter = new MapSqlParameterSource();
        	nameParameter.addValue(TableConstants.ID_POWER, idPower);
            //final MapSqlParameterSource nameParameter = this.createFindByIdPower(idPower);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdPower(),
                    nameParameter, new BeanPropertyRowMapper<Power>(Power.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ?
            cause : dataAccessException;
            throw new DatabaseException("Error al Encontrar por id Poderes", exception);
        }
    }
    
    private MapSqlParameterSource createFindByIdPower(final Integer idPower) {
        final MapSqlParameterSource nameParameter = new MapSqlParameterSource();
        nameParameter.addValue(TableConstants.ID_POWER, idPower);
        return nameParameter;
    }

    private String buildFindByIdPower() {
        final StringBuilder query = this.buildFindAllQuery();
        query.append(WHERE_ID_POWER_EQUALS_ID_POWER_PARAM);
        return query.toString();
    }
    
    private StringBuilder buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query;
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append(QueryConstants.SELECT + TableConstants.ID_POWER + COMMA);
        this.addAllNonPrimaryKeyFields(query);
        query.append(", FINANCIALENTITY.Name As FinancialName FROM POWER INNER JOIN FINANCIALENTITY ");
        query.append("ON POWER.IdFinancialEntity = FINANCIALENTITY.IdFinancialEntity ");
    }

    public void deletePower(final Integer idPower) {
      final MapSqlParameterSource nameParameter = this.createFindByIdPower(idPower);
      this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), nameParameter);
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.DELETE_FROM + TableConstants.TABLE_POWER);
        query.append(WHERE_ID_POWER_EQUALS_ID_POWER_PARAM);
        return query.toString();
    }

    public int countAll() {
        final MapSqlParameterSource nameParameter = new MapSqlParameterSource();
        return this.namedjdbcTemplate.queryForObject(this.buildCountQuery(), nameParameter, Integer.class);
    }

    private String buildCountQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountQuery(query);
        return query.toString();
    }

    private void buildSelectCountQuery(final StringBuilder query) {
        query.append(SELECT_COUNT_LEFT_BRACES + TableConstants.ID_POWER + QueryConstants.RIGHT_BRACES);
        query.append(FROM_POWER);
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByStatusNameParameter(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(),
                namedParameters, Integer.class);
    }

    private String buildCountByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountQuery(query);
        this.buildWhereSatusQuery(query);
        return query.toString();
    }

    @Override
    public List<Power> findAllPowerCatalogPaged(final RecordStatusEnum status, 
            final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(status);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
                    this.findAllPowerCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, new BeanPropertyRowMapper<Power>(Power.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllPowerCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildSelectAllQuery(builder);
        builder.append("WHERE :Status IS NULL OR POWER.Status = :Status ");
        builder.append("ORDER BY FinancialName ASC ");
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
            final String countItems = this.databaseUtils.countTotalRows(this.findAllPowerCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
}
