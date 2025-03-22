package mx.pagos.admc.contracts.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import mx.pagos.admc.contracts.interfaces.LegalRepresentativeable;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class LegalRepresentativeDAO implements LegalRepresentativeable {
    private static final String WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM = QueryConstants.WHERE + 
            TableConstants.ID_LEGALREPRESENTATIVE + QueryConstants.EQUAL_TAG + TableConstants.ID_LEGALREPRESENTATIVE;
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

    @Autowired
    private DatabaseUtils databaseUtils;
    
    @Override
    public Integer saveOrUpdate(final LegalRepresentative legalRepresentative) throws DatabaseException {
        return legalRepresentative.getIdLegalRepresentative() == null ? this.insert(legalRepresentative) :
            this.update(legalRepresentative);
    }
    
    private Integer insert(final LegalRepresentative legalRepresentative) throws DatabaseException {     
        try { 
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(legalRepresentative);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.insertQuery(), namedParameters, keyHolder, 
                    new String[]{"IdLegalRepresentative"});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar Reprensentante legal", dataAccessException);
        }
    }
    
    private Integer update(final LegalRepresentative legalRepresentative) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters =
                    new BeanPropertySqlParameterSource(legalRepresentative);
            this.namedjdbcTemplate.update(this.updateQuery(), namedParameters);
            return legalRepresentative.getIdLegalRepresentative();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al Actualizar Representante Legal", dataAccessException);
        }
    }
    
    @Override
    public void changeLegalRepresentativeStatus(final Integer idLegalRepresentative,
            final  RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createFindByIdLegalRepresentativeNamedParameters(idLegalRepresentative);
            namedParameters.addValue(TableConstants.STATUS, status.toString());       
            this.namedjdbcTemplate.update(this.changeStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al cambiar el estatus del Representante Legal", dataAccessException);
        }
    }

    @Override
    public List<LegalRepresentative> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.findAllQuery(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));   
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar Representantes Legales", dataAccessException);
        }
    }

    @Override
    public List<LegalRepresentative> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.query(this.findByRecordStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al recuperar por estatus", dataAccessException);
        }
    }

    @Override
    public List<LegalRepresentative> findByDgaAndFinantialEntity( 
    		final List<Integer> finantialEntitiesList) throws DatabaseException {   
        try {
            final MapSqlParameterSource namedParameters = 
            		this.createFindByDgaAndFinantialEntityNamedParameters(finantialEntitiesList);
            return this.namedjdbcTemplate.query(this.findLegalRepresentativeByFinantialEntityQuery(), namedParameters, 
                    new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al buscar por DGA y Entidad", dataAccessException);
        }
    }

    private MapSqlParameterSource createFindByDgaAndFinantialEntityNamedParameters( 
    		final List<Integer> finantialEntitiesList) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        final Map<String, List<Integer>> valuesMap = new HashMap<>();
        valuesMap.put(TableConstants.ID_FINANCIALENTITY, finantialEntitiesList);
        namedParameters.addValues(valuesMap);
        return namedParameters;
    }

    @Override
    public Power findPowersByIdLegalRepresentativeAndIdFinancialEntity(final Integer idLegalRepresentative,
            final Integer idFinancialEntity) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createSaveLegalRepresentativeFinancialEntityNamedParameters(
                            idLegalRepresentative, idFinancialEntity);
            return this.namedjdbcTemplate.queryForObject(this.buildFindPowersByIdLegalRepresentativeQuery(),
                    namedParameters, new BeanPropertyRowMapper<Power>(Power.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return new Power();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public LegalRepresentative findByIdLegalRepresentative(final Integer idLegalRepresentative) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdLegalRepresentativeNamedParameters(
                    idLegalRepresentative);
            return this.namedjdbcTemplate.queryForObject(this.findByIdQuery(),
                    namedParameters, new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));
        } catch (DataAccessException dataAccessException) {
            final Throwable cause = dataAccessException.getMostSpecificCause();
            final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
                    cause : dataAccessException;
            throw new DatabaseException("Error al recuperar el Representante Legal", exception);
        }
    }

    private MapSqlParameterSource createFindByIdLegalRepresentativeNamedParameters(
            final Integer idLegalRepresentative) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, idLegalRepresentative);
        return namedParameters;
    }

    @Override
    public void savePower(final Power power) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(power);
            this.namedjdbcTemplate.update(this.updatePowerQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void deleteLegalRepresentativePower(final Integer idLegalRepresentative) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createFindByIdLegalRepresentativeNamedParameters(idLegalRepresentative);
            this.namedjdbcTemplate.update(this.buildDeleteLegalRepresentativePowersQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public void saveLegalRepresentativeFinancialEntity(final Integer idLegalRepresentative,
            final Integer idFinancialEntity) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createSaveLegalRepresentativeFinancialEntityNamedParameters(
                            idLegalRepresentative, idFinancialEntity);
            this.namedjdbcTemplate.update(this.saveLegalRepresentativeFinancialEntityQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<FinancialEntity> findFinantialEntitiesByIdLegalRepresentative(final Integer idLegalRepresentative)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindByIdLegalRepresentativeNamedParameters(idLegalRepresentative);
            return this.namedjdbcTemplate.query(this.buildFindFinantialEntitiesByIdLegalRepresentativeQuery(),
                    namedParameters, new BeanPropertyRowMapper<>(FinancialEntity.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    

    @Override
    public List<FinancialEntity> findRequisitionFinantialEntitiesByIdLegalRepresentative(
            final Integer idRequisition, final Integer idLegalRepresentative) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
            namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, idLegalRepresentative);
            return this.namedjdbcTemplate.query(
                    this.buildFindRequisitionFinantialEntitiesByIdLegalRepresentativeQuery(),
                    namedParameters, new BeanPropertyRowMapper<>(FinancialEntity.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildFindRequisitionFinantialEntitiesByIdLegalRepresentativeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT FE.* FROM REQUISITIONFINANCIALENTITY REQFE  ");
        query.append("INNER JOIN REQLEGALREPRESENTATIVE REQLR ON REQFE.IdRequisition = REQLR.IdRequisition ");
        query.append("INNER JOIN FINANCIALENTITY FE ON REQFE.IdFinancialEntity = FE.IdFinancialEntity ");
        query.append("INNER JOIN LEGALREPEFINANCIALENTITIES LGLFE ON FE.IdFinancialEntity = LGLFE.IdFinancialEntity ");
        query.append("AND REQLR.IdLegalRepresentative = LGLFE.IdLegalRepresentative ");
        query.append("WHERE LGLFE.IdLegalRepresentative = :IdLegalRepresentative ");
        query.append("AND FE.Status = 'ACTIVE' AND REQLR.IdRequisition = :IdRequisition ");
        return query.toString();
    }

    @Override
    public void deleteFinantialEntitiesByIdLegalRepresentative(final Integer idLegalRepresentative)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createFindByIdLegalRepresentativeNamedParameters(idLegalRepresentative);
            this.namedjdbcTemplate.update(this.buildDeleteFinantialEntitiesByIdLegalRepresentativeQuery(),
                    namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    private String buildDeleteFinantialEntitiesByIdLegalRepresentativeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM LEGALREPEFINANCIALENTITIES ");
        query.append(WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        return query.toString();
    }

    private String saveLegalRepresentativeFinancialEntityQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO LEGALREPEFINANCIALENTITIES (IdLegalRepresentative, IdFinancialEntity) ");
        query.append("VALUES (:IdLegalRepresentative, :IdFinancialEntity) ");
        return query.toString();
    }
    
    private String buildFindFinantialEntitiesByIdLegalRepresentativeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT FINANCIALENTITY.IdFinancialEntity, FINANCIALENTITY.Name ");
        query.append("FROM LEGALREPEFINANCIALENTITIES INNER JOIN FINANCIALENTITY ");
        query.append("ON LEGALREPEFINANCIALENTITIES.IdFinancialEntity = ");
        query.append("FINANCIALENTITY.IdFinancialEntity ");
        query.append(WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        return query.toString();
    }

    private String buildDeleteLegalRepresentativePowersQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM LEGALREPRESENTATIVEPOWER WHERE IdLegalRepresentative = :IdLegalRepresentative");
        return query.toString();
    }

    private MapSqlParameterSource createSaveLegalRepresentativeFinancialEntityNamedParameters(
            final Integer idLegalRepresentative, final Integer idFinancialEntity) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_LEGALREPRESENTATIVE, idLegalRepresentative);
        namedParameters.addValue(TableConstants.ID_FINANCIALENTITY, idFinancialEntity);
        return namedParameters;
    }

    public void deleteById(final Integer idLegalRepresentative) {
        final StringBuilder queryDelete = new StringBuilder();
        queryDelete.append(QueryConstants.DELETE_FROM + TableConstants.TABLE_LEGALREPRESENTATIVE + 
                WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        final MapSqlParameterSource namedParameters = this.createFindByIdLegalRepresentativeNamedParameters(
                idLegalRepresentative);
        this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
    }

    public Integer builtFindAll() {
        return this.namedjdbcTemplate.queryForObject(this.countAllRecordsQuery(), new MapSqlParameterSource(),
                Integer.class);
    }

    public Integer countRecordsByStatusQuery() {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, RecordStatusEnum.ACTIVE.toString());
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByStatus(), namedParameters, Integer.class);
    }

    public Integer countRecordsByIdLegalRepresentativeQuery(final Integer idLegal) {
        final MapSqlParameterSource namedParameters = this.createFindByIdLegalRepresentativeNamedParameters(
                idLegal);
        return this.namedjdbcTemplate.queryForObject(this.countRecordsByIdLegalQuery(), 
                namedParameters, Integer.class);
    }
    
    private String insertQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO LEGALREPRESENTATIVE (IdDga, Name) ");
        query.append("VALUES (:IdDga, :Name)");
        return query.toString();
    }

    private String updateQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE LEGALREPRESENTATIVE SET ");
        query.append("IdDga = :IdDga, Name = :Name ");
        query.append(WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        return query.toString();
    }
     
    private String changeStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.UPDATE + TableConstants.TABLE_LEGALREPRESENTATIVE + QueryConstants.SET);
        query.append(TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS + QueryConstants.SPACE);
        query.append(WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        return query.toString();
    }

    private String findAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }

    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT LR.IdLegalRepresentative, LR.Name,");
        query.append("LR.IdDga, D.Name dgaName, LR.Status ");
        query.append("FROM LEGALREPRESENTATIVE LR ");
        query.append("LEFT JOIN DGA D ON LR.IdDga = D.IdDga ");
    }

    private String findByRecordStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(QueryConstants.WHERE + "LR.Status" + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }

    private  String findByIdQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        return query.toString();
    }

    private  String saveLegalRepresentativePowerQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE POWER SET ");
        query.append("PublicDeedNumber = :PublicDeedNumber,  PublicNotaryName = :PublicNotaryName, ");
        query.append("PublicNotaryNumber = :PublicNotaryNumber, PublicNotaryState = :PublicNotaryState, ");
        query.append("PublicCommercialFolio = :PublicCommercialFolio, PublicDeedDate = :PublicDeedDate, ");
        query.append("PublicCommercialFolioInscriptionDate = :PublicCommercialFolioInscriptionDate, ");
        query.append("PublicCommercialFolioInscriptionState = :PublicCommercialFolioInscriptionState, ");
        query.append("IdFinancialEntity = :IdFinancialEntity ");
        query.append("WHERE IdLegalRepresentative = :IdLegalRepresentative AND IdPower = :IdPower ");
        return query.toString();
    }

    private String findLegalRepresentativeByFinantialEntityQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT LR.IdLegalRepresentative, LR.Name, "); 
        query.append("LR.Status, LRFE.Name financialEntityName ");
        query.append("FROM LEGALREPRESENTATIVE LR ");
        query.append("INNER JOIN ( ");
        query.append("SELECT DISTINCT IdLegalRepresentative, FINANCIALENTITY.Name ");
        query.append("FROM LEGALREPEFINANCIALENTITIES ");
        query.append("INNER JOIN FINANCIALENTITY ");
        query.append("ON LEGALREPEFINANCIALENTITIES.IdFinancialEntity = FINANCIALENTITY.IdFinancialEntity ");
        query.append("WHERE LEGALREPEFINANCIALENTITIES.IdFinancialEntity IN (:IdFinancialEntity) ");
        query.append("AND FINANCIALENTITY.Status = 'ACTIVE' ");
        query.append(") LRFE ON LRFE.IdLegalRepresentative = LR.IdLegalRepresentative ");
        query.append("AND LR.Status = 'ACTIVE' ");
        return query.toString();
    }

    private String buildFindPowersByIdLegalRepresentativeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdPower, IdFinancialEntity, PublicDeedNumber, PublicNotaryName, PublicNotaryNumber, ");
        query.append("PublicNotaryState, PublicCommercialFolio, PublicCommercialFolioInscriptionDate, ");
        query.append("PublicCommercialFolioInscriptionState, PublicDeedDate, IdLegalRepresentative ");
        query.append("FROM POWER WHERE ");
        
        query.append("IdLegalRepresentative = :IdLegalRepresentative AND IdFinancialEntity = :IdFinancialEntity ");
        return query.toString();
    }

    private String countAllRecordsQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        return query.toString();
    }

    private String countRecordsByStatus() {
        final StringBuilder query = new StringBuilder();
        this.buildCountQuery(query);
        query.append(QueryConstants.WHERE + TableConstants.STATUS + QueryConstants.EQUAL_TAG + TableConstants.STATUS);
        return query.toString();
    }

    private String countRecordsByIdLegalQuery() {
        final StringBuilder query = new StringBuilder();
        query.append(QueryConstants.SELECT_COUNT + TableConstants.ID_LEGALREPRESENTATIVE + QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.FROM + TableConstants.TABLE_LEGALREPRESENTATIVE_POWER + QueryConstants.SPACE);
        query.append(WHERE_ID_LEGAL_EQUALS_ID_LEGAL_PARAM);
        return query.toString();
    }

    private void buildCountQuery(final StringBuilder query) {
        query.append(QueryConstants.SELECT_COUNT + TableConstants.ID_LEGALREPRESENTATIVE + QueryConstants.RIGHT_BRACES);
        query.append(QueryConstants.FROM + TableConstants.TABLE_LEGALREPRESENTATIVE);
    }

    @Override
    public List<LegalRepresentative> findAllLegalRepresentativeCatalogPaged(
            final LegalRepresentative legalRepresentative, final Integer pagesNumber, 
            final Integer itemsNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(legalRepresentative);
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_LEGALREPRESENTATIVE, 
                    this.findAllLegalRepresentativeCatalogPagedQuery(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, source, 
                    new BeanPropertyRowMapper<LegalRepresentative>(LegalRepresentative.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String findAllLegalRepresentativeCatalogPagedQuery() {
        final StringBuilder builder = new StringBuilder();
        this.buildSelectAllQuery(builder);
        builder.append("WHERE (:Status IS NULL OR LR.Status = :Status) ");
        builder.append("AND (:NameNULL IS NULL OR LR.Name LIKE :Name) ");
        builder.append("ORDER BY LR.Name ASC ");
        return builder.toString();
    }

    private MapSqlParameterSource statusParameter(final LegalRepresentative legalRepresentative) {
        final MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue(TableConstants.STATUS, legalRepresentative.getStatus() == null ? null : 
            legalRepresentative.getStatus().toString());
        source.addValue(TableConstants.NAME + "NULL", legalRepresentative.getName());
        source.addValue(TableConstants.NAME, "%" + legalRepresentative.getName() + "%");
        return source;
    }

    @Override
    public Long countTotalItemsToShowOfLegalRepresentative(final LegalRepresentative legalRepresentative)
            throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(legalRepresentative);
            final String countItems = 
                    this.databaseUtils.countTotalRows(this.findAllLegalRepresentativeCatalogPagedQuery());
            return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void updatePower(final Power power) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(power);
            this.namedjdbcTemplate.update(this.saveLegalRepresentativePowerQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String updatePowerQuery() {
        final StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO POWER(IdFinancialEntity, PublicDeedNumber, PublicNotaryName, PublicNotaryNumber, ");
        builder.append("PublicNotaryState, PublicCommercialFolio, PublicCommercialFolioInscriptionDate, ");
        builder.append("PublicCommercialFolioInscriptionState, PublicDeedDate, IdLegalRepresentative) ");
        builder.append("VALUES(:IdFinancialEntity, :PublicDeedNumber, :PublicNotaryName, :PublicNotaryNumber, ");
        builder.append(":PublicNotaryState, :PublicCommercialFolio, :PublicCommercialFolioInscriptionDate, ");
        builder.append(":PublicCommercialFolioInscriptionState, :PublicDeedDate, :IdLegalRepresentative) ");
        return builder.toString();
    }
}
