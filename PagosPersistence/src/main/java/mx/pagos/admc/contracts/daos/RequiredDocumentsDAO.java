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
import mx.pagos.admc.contracts.interfaces.RequiredDocumentable;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * @author Mizraim
 * 
 * @see RequiredDocument
 * @see RequiredDocumentable
 * @see DatabaseException
 * @See RecordStatusEnum
 *
 */
@Repository
public class RequiredDocumentsDAO implements RequiredDocumentable {
	private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE REQUIREDDOCUMENT.Status = :Status";
	private static final String WHERE_ID_EQUALS_ID = "WHERE IdRequiredDocument = :IdRequiredDocument";
	private static final String LIKE = "%";
	private static final String NULL = "Null";
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	private DatabaseUtils databaseUtils;

	@Override
	public Integer saveOrUpdate(final RequiredDocument requiredDocument) throws DatabaseException {
		return requiredDocument.getIdRequiredDocument() == null ?
				this.insertRequiredDocument(requiredDocument) : this.updateRequiredDocument(requiredDocument);
	}

	private Integer insertRequiredDocument(final RequiredDocument requiredDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters =
					this.createInsertRequiredDocumentNamedParameters(requiredDocument);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertRequiredDocumentQuery(), namedParameters, keyHolder, 
					new String[]{"IdRequiredDocument"});
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer updateRequiredDocument(final RequiredDocument requiredDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters =
					this.createUpdateRequiredDocumentNamedParameters(requiredDocument);
			this.namedjdbcTemplate.update(this.buildUpdateRequiredDocumentQuery(), namedParameters);
			return requiredDocument.getIdRequiredDocument();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void changeRequiredDocumentStatus(final Integer idRequiredDocument, final RecordStatusEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters =
					this.createChangeRequiredDocumentStatusNamedParameters(idRequiredDocument, status);
			this.namedjdbcTemplate.update(this.buildChangeRequiredDocumentStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<RequiredDocument> findAll() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
					new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public RequiredDocument findByIdRequiredDocument(final Integer idRequiredDocument)
			throws DatabaseException, EmptyResultException {
		try {
			final MapSqlParameterSource namedParameters =
					this.createFindByIdRequiredDocumentNamedParameters(idRequiredDocument);
			return this.namedjdbcTemplate.queryForObject(this.buildFindByIdRequiredDocumentQuery(), namedParameters,
					new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<RequiredDocument> findByStatus(final RecordStatusEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
			return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
					new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	public void deleteByIdRequiredDocument(final Integer idRequiredDocument) {
		final MapSqlParameterSource namedParameters =
				this.createFindByIdRequiredDocumentNamedParameters(idRequiredDocument);
		this.namedjdbcTemplate.update(this.buildDeleteByIdRequiredDocumentQuery(), namedParameters);
	}

	public Integer countAll() {
		return this.namedjdbcTemplate.queryForObject(this.countAllRequiredDocumentQuery(),
				new MapSqlParameterSource(), Integer.class);
	}

	private String countAllRequiredDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		this.buildSelectCountAllQuery(builder);
		this.joinsRequiredDocument(builder);
		return builder.toString();
	}

	private String buildCountByStatusQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectCountAllQuery(query);
		this.joinsRequiredDocument(query);
		query.append(WHERE_STATUS_EQUALS_STATUS);
		return query.toString();
	}

	private void joinsRequiredDocument(final StringBuilder query) {
		query.append("LEFT JOIN PERSONALITYREQUIREDDOCUMENT ON ");
		query.append("REQUIREDDOCUMENT.IdRequiredDocument = PERSONALITYREQUIREDDOCUMENT.IdRequiredDocument ");
		query.append("LEFT JOIN PERSONALITY ON PERSONALITYREQUIREDDOCUMENT.IdPersonality = PERSONALITY.IdPersonality ");
	}

	private String buildInsertRequiredDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO REQUIREDDOCUMENT (Name, isRequired) VALUES (:Name, :isRequired)");
		return query.toString();
	}

	private MapSqlParameterSource createInsertRequiredDocumentNamedParameters(final RequiredDocument requiredDocument) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.NAME, requiredDocument.getName());
		namedParameters.addValue(TableConstants.ISREQUIRED, requiredDocument.getIsRequired());
		return namedParameters;
	}

	private String buildUpdateRequiredDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUIREDDOCUMENT SET Name = :Name, isRequired = :isRequired ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createUpdateRequiredDocumentNamedParameters(final RequiredDocument requiredDocument) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUIRED_DOCUMENT, requiredDocument.getIdRequiredDocument());
		namedParameters.addValue(TableConstants.NAME, requiredDocument.getName());
		namedParameters.addValue(TableConstants.ISREQUIRED, requiredDocument.getIsRequired());
		return namedParameters;
	}

	private String buildChangeRequiredDocumentStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE REQUIREDDOCUMENT SET Status = :Status ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createChangeRequiredDocumentStatusNamedParameters(
			final Integer idRequiredDocument, final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
		namedParameters.addValue(TableConstants.STATUS, status.toString());
		return namedParameters;
	}

	private String buildFindAllQuery() {
		final StringBuilder query = new StringBuilder();
		this.findAllFieldsQuery(query);
		return query.toString();
	}

	private String buildFindByIdRequiredDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource createFindByIdRequiredDocumentNamedParameters(final Integer idRequiredDocument) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
		return namedParameters;
	}

	private String buildFindByStatusQuery() {
		final StringBuilder query = new StringBuilder();
		this.findAllFieldsQuery(query);
		query.append(WHERE_STATUS_EQUALS_STATUS);
		return query.toString();
	}

	private void findAllFieldsQuery(final StringBuilder query) {
		query.append("SELECT REQUIREDDOCUMENT.IdRequiredDocument, REQUIREDDOCUMENT.Name, ");
		query.append("PERSONALITYREQUIREDDOCUMENT.IdPersonality, PERSONALITY.Name AS PersonalityName, ");
		query.append("REQUIREDDOCUMENT.isRequired, REQUIREDDOCUMENT.Status FROM REQUIREDDOCUMENT ");
		this.joinsRequiredDocument(query);
	}

	private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.STATUS, status.toString());
		return namedParameters;
	}

	private void buildSelectAllQuery(final StringBuilder query) {
		query.append("SELECT IdRequiredDocument, Name, Status FROM REQUIREDDOCUMENT ");
	}

	private String buildDeleteByIdRequiredDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM REQUIREDDOCUMENT ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private void buildSelectCountAllQuery(final StringBuilder query) {
		query.append("SELECT COUNT(REQUIREDDOCUMENT.IdRequiredDocument) FROM REQUIREDDOCUMENT ");
	}

	public Integer countByStatus(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
		return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(), namedParameters, Integer.class);
	}

	@Override
	public void savePersonalityRequiredDocument(final Integer idPersonality, final Integer idRequiredDocument) 
			throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.personalityDocumentsParameters(
					idPersonality, idRequiredDocument);
			this.namedjdbcTemplate.update(this.savePersonalityRequiredDocumentQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource personalityDocumentsParameters(
			final Integer idPersonality, final Integer idRequiredDocument) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue(TableConstants.ID_PERSONALITY, idPersonality);
		source.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
		return source;
	}

	private String savePersonalityRequiredDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("INSERT INTO PERSONALITYREQUIREDDOCUMENT(IdPersonality, IdRequiredDocument) ");
		builder.append("VALUES(:IdPersonality, :IdRequiredDocument) ");
		return builder.toString();
	}

	@Override
	public void deletePersonalityRequiredDocument(final Integer idRequiredDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
			this.namedjdbcTemplate.update(this.deletePersonalityRequiredDocumentQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String deletePersonalityRequiredDocumentQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("DELETE FROM PERSONALITYREQUIREDDOCUMENT ");
		builder.append("WHERE IdRequiredDocument = :IdRequiredDocument ");
		return builder.toString();
	}

	@Override
	public List<RequiredDocument> findAllRequiredDocumentWithPersonalities() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.findAllRequiredDocumentWithPersonalitiesQuery(), 
					new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findAllRequiredDocumentWithPersonalitiesQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT REQUIREDDOCUMENT.IdRequiredDocument, Name, Status, ");
		builder.append("PERSONALITYREQUIREDDOCUMENT.IdPersonality ");
		builder.append("FROM REQUIREDDOCUMENT INNER JOIN PERSONALITYREQUIREDDOCUMENT ");
		builder.append("ON REQUIREDDOCUMENT.IdRequiredDocument = PERSONALITYREQUIREDDOCUMENT.IdRequiredDocument ");
		return builder.toString();
	}

	@Override
	public List<RequiredDocument> findAllRequiredDocumentCatalogPaged(final RequiredDocument requiredDocument, 
			final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.statusParameter(requiredDocument);
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_REQUIRED_DOCUMENT, 
					this.findAllRequiredDocumentCatalogPagedQuery(), pagesNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, source, 
					new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findAllRequiredDocumentCatalogPagedQuery() {
		final StringBuilder builder = new StringBuilder();
		this.findAllFieldsQuery(builder);
		builder.append("WHERE ((:Status IS NULL) OR (REQUIREDDOCUMENT.Status = :Status)) ");
		builder.append("AND ((:NameNull IS NULL) OR (REQUIREDDOCUMENT.Name LIKE :Name)) ");
		builder.append("ORDER BY REQUIREDDOCUMENT.Name ASC ");
		return builder.toString();
	}

	private MapSqlParameterSource statusParameter(final RequiredDocument requiredDocument) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue(TableConstants.NAME + NULL, requiredDocument.getName());
		source.addValue(TableConstants.NAME, LIKE + requiredDocument.getName() + LIKE);
		source.addValue(TableConstants.STATUS, requiredDocument.getStatus() == null ? null : requiredDocument.getStatus().toString());

		return source;
	}

	@Override
	public Long countTotalItemsToShowOfRequiredDocument(final RequiredDocument requiredDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.statusParameter(requiredDocument);
			final String countItems = 
					this.databaseUtils.countTotalRows(this.findAllRequiredDocumentCatalogPagedQuery());
			return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Integer findIdByName(final String name) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue("Name", name);
			return this.namedjdbcTemplate.queryForObject(this.buildFindIdByNameQuery(), namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			return null;
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindIdByNameQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdRequiredDocument FROM REQUIREDDOCUMENT WHERE Name = :Name");
		return query.toString();
	}
}
