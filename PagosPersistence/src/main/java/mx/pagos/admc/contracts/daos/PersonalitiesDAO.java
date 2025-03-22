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
import mx.pagos.admc.contracts.interfaces.Personalitable;
import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * @author Mizraim
 * 
 * @see Personality
 * @see Personalitable
 * @see DatabaseException
 * @See RecordStatusEnum
 *
 */
@Repository
public class PersonalitiesDAO implements Personalitable {
    
    private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE Status = :Status";
    private static final String WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY = " WHERE IdPersonality = :IdPersonality";
    
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    
    @Override
    public Integer saveOrUpdate(final Personality personality) throws DatabaseException {
        return personality.getIdPersonality() == null ?
                this.insertPersonality(personality) : this.updatePersonality(personality);
        
    }

    private Integer insertPersonality(final Personality personality) throws DatabaseException {
        try {
          final MapSqlParameterSource namedParameters = this.createInsertPersonalityNamedParameters(personality);
          final KeyHolder keyHolder = new GeneratedKeyHolder();
          this.namedjdbcTemplate.update(this.buildInsertPersonalityQuery(), namedParameters, keyHolder, 
                  new String[]{"IdPersonality"});
          return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private Integer updatePersonality(final Personality personality) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createUpdatePersonalityNamedParameters(personality);
            this.namedjdbcTemplate.update(this.buildUpdatePersonalityQuery(), namedParameters);
            return personality.getIdPersonality();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changePersonalityStatus(final Integer idPersonality, final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createChangePersonalityStatusNamedParameters(idPersonality, status);
            this.namedjdbcTemplate.update(this.buildChangePersonalityStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Personality> findAll() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
                    new BeanPropertyRowMapper<Personality>(Personality.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Personality findByIdPersonality(final Integer idPersonality)
            throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idPersonality);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByIdPersonalityQuery(), namedParameters,
                    new BeanPropertyRowMapper<Personality>(Personality.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    @Override
    public List<Personality> findByStatus(final RecordStatusEnum status)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
                    new BeanPropertyRowMapper<Personality>(Personality.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<RequiredDocument> findRequiredDocumentByPersonality(final Integer idPersonality) 
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_PERSONALITY, idPersonality);
            return this.namedjdbcTemplate.query(this.buildFindRequiredDocumentQuery(), namedParameters,
                    new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));            
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void saveRequiredDocumentByPersonality(final Integer idPersonality, final Integer idRequiredDocument) 
            throws DatabaseException {
        final MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue(TableConstants.ID_PERSONALITY, idPersonality);
        namedParameter.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
        this.namedjdbcTemplate.update(this.buildInsertRequiredDocumentQuery(), namedParameter);
    }

    @Override
    public void deleteRequiredDocumentByPersonality(final Integer idPersonality) throws DatabaseException {
        try {
            final StringBuilder queryDelete = new StringBuilder();
            queryDelete.append("DELETE FROM PERSONALITYREQUIREDDOCUMENT ");
            queryDelete.append(WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY);
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_PERSONALITY, idPersonality);
            this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    public void deleteByIdPersonality(final Integer idPersonality) throws DatabaseException {
        final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idPersonality);
        this.namedjdbcTemplate.update(this.buildDeletePersonalityByIdQuery(), namedParameters);
    }
    
    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }
    
    public Integer countByStatus(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(), namedParameters, Integer.class);
    }
    
    public Integer countByIdPersonality(final Integer idPersonality) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_PERSONALITY, idPersonality);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByIdQuery(), namedParameters, Integer.class);
    }

    private String buildInsertRequiredDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO PERSONALITYREQUIREDDOCUMENT(IdPersonality, IdRequiredDocument) ");
        query.append("VALUES(:IdPersonality, :IdRequiredDocument)");
        return query.toString();
    }
    
    private String buildInsertPersonalityQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("INSERT INTO PERSONALITY (Name, PersonalityEnum) VALUES (:Name, :PersonalityEnum)");
        return query.toString();
    }

    private MapSqlParameterSource createInsertPersonalityNamedParameters(final Personality personality) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.NAME, personality.getName());
        namedParameters.addValue(TableConstants.PERSONALITY_ENUM, personality.getPersonalityEnum().toString());
        return namedParameters;
    }
    
    private String buildUpdatePersonalityQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE PERSONALITY SET Name = :Name, PersonalityEnum = :PersonalityEnum ");
        query.append(WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY);
        return query.toString();
    }
    
    private MapSqlParameterSource createUpdatePersonalityNamedParameters(final Personality personality) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_PERSONALITY, personality.getIdPersonality());
        namedParameters.addValue(TableConstants.NAME, personality.getName());
        namedParameters.addValue(TableConstants.PERSONALITY_ENUM, personality.getPersonalityEnum().toString());
        return namedParameters;
    }
    
    private String buildChangePersonalityStatusQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE PERSONALITY SET Status = :Status ");
        query.append(WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY);
        return query.toString();
    }
    
    private MapSqlParameterSource createChangePersonalityStatusNamedParameters(final Integer idPersonality,
            final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_PERSONALITY, idPersonality);
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private String buildFindAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        return query.toString();
    }
    
    private String buildFindByIdPersonalityQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY);
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByIdNamedParameters(final Integer idPersonality) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_PERSONALITY, idPersonality);
        return namedParameters;
    }
    
    private String buildFindByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private String buildFindRequiredDocumentQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT RD.IdRequiredDocument, Name, RD.isRequired ");
        query.append("FROM REQUIREDDOCUMENT RD INNER JOIN PERSONALITYREQUIREDDOCUMENT PRD ");
        query.append("ON RD.IdRequiredDocument = PRD.IdRequiredDocument ");
        query.append("WHERE IdPersonality = :IdPersonality AND RD.Status = 'ACTIVE' ");
        return query.toString();
    }
    
    private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.STATUS, status.toString());
        return namedParameters;
    }
    
    private void buildSelectAllQuery(final StringBuilder query) {
        query.append("SELECT IdPersonality, Name, Status, PersonalityEnum FROM PERSONALITY ");
    }
    
    private String buildDeletePersonalityByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM PERSONALITY ");
        query.append(WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY);
        return query.toString();
    }
    
    private void buildSelectCountQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdPersonality) FROM PERSONALITY ");
    }
    
    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountQuery(query);
        return query.toString();
    }
    
    private String buildCountByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountQuery(query);
        query.append(WHERE_STATUS_EQUALS_STATUS);
        return query.toString();
    }
    
    private String buildCountByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(IdRequiredDocument) FROM PERSONALITYREQUIREDDOCUMENT");
        query.append(WHERE_ID_PERSONALITY_EQUALS_ID_PERSONALITY);
        return query.toString();
    }

}
