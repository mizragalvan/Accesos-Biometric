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
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.FinancialEntityable;
import mx.pagos.admc.contracts.structures.FinancialEntitieCombination;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

@Repository
public class FinancialEntityDAO implements FinancialEntityable {

	private static final String WHERE_RFE_ID_REQUISITION = "WHERE RFE.IdRequisition =:IdRequisition ";
	private static final String SELECT_COUNT_LEFT_BRACES = "SELECT COUNT(";
	private static final String WHERE = "WHERE ";
	private static final String SPACE = " ";
	private static final String UPDATE_FINANCIALENTITY_SET = "UPDATE FINANCIALENTITY SET ";
	private static final String COMMA = ", ";
	private static final String EQUAL_TAG = " = :";
	private static final String FROM_FINANCIALENTITY = "FROM " + TableConstants.TABLE_FINANCIALENTITY + SPACE;
	private static final String WHERE_ID_FINANCIALENTITY_EQUALS_ID_FINANCIALENTITY_PARAM = WHERE +
			TableConstants.ID_FINANCIALENTITY + EQUAL_TAG + TableConstants.ID_FINANCIALENTITY;
	private static final String LIKE = "%";
	private static final String NULL = "Null";

	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	private DatabaseUtils databaseUtils;

	@Override
	public Integer saveOrUpdate(final FinancialEntity financialEntity)
			throws DatabaseException {
		return financialEntity.getIdFinancialEntity() == null ? 
				this.insertFinancialEntity(financialEntity) : this.updateFinancialEntity(financialEntity);
	}

	private Integer insertFinancialEntity(final FinancialEntity financialEntity) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(financialEntity);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertFinancialEntityQuery(), namedParameters, keyHolder,
					new String[]{"IdFinancialEntity"});
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al insertar la Entidad", dataAccessException);
		}
	}

	private String buildInsertFinancialEntityQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO FINANCIALENTITY (");
		query.append("Name, Status, LongName, Domicile, Rfc, Telefono, Correo, Atencion, Constitutive, ");
		query.append("ConstitutiveEnglish, Treatment, AccountNumber, BankBranch, BankingInstitution, EntidadFinanceiraRegistro, EntidadFinanceiraRegistroIngles) ");
		query.append("VALUES(:Name, 'ACTIVE', :LongName, :Domicile, :Rfc, :Telefono, :Correo, :Atencion, ");
		query.append(":Constitutive, :ConstitutiveEnglish, :Treatment, :AccountNumber, :BankBranch, ");
		query.append(":BankingInstitution, :ConstitutiveRegistred, :ConstitutiveRegistredIngles)");
		return query.toString();
	}

	private Integer updateFinancialEntity(final FinancialEntity financialEntity) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(financialEntity);
			this.namedjdbcTemplate.update(this.buildUpdateFinancialEntityQuery(), namedParameters);
			return financialEntity.getIdFinancialEntity();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al actualizar la Entidad", dataAccessException);
		}
	}

	private String buildUpdateFinancialEntityQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_FINANCIALENTITY_SET);
		query.append(TableConstants.NAME + EQUAL_TAG + TableConstants.NAME + COMMA);
		query.append(TableConstants.LONG_NAME + EQUAL_TAG + TableConstants.LONG_NAME + COMMA);
		query.append(TableConstants.DOMICILE + EQUAL_TAG + TableConstants.DOMICILE + COMMA);
		query.append("Rfc = :Rfc, Telefono = :Telefono, Correo = :Correo, Atencion = :Atencion, ");
		query.append("Constitutive = :Constitutive, ConstitutiveEnglish = :ConstitutiveEnglish, ");
		query.append("Treatment = :Treatment, AccountNumber = :AccountNumber, ");
		query.append("BankBranch = :BankBranch, BankingInstitution = :BankingInstitution, ");
		query.append("EntidadFinanceiraRegistro = :ConstitutiveRegistred, EntidadFinanceiraRegistroIngles = :ConstitutiveRegistredIngles ");
		query.append(WHERE_ID_FINANCIALENTITY_EQUALS_ID_FINANCIALENTITY_PARAM);
		return query.toString();
	}

	@Override
	public void changeFinancialEntityStatus(final Integer idFinancialEntity, 
			final RecordStatusEnum status) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = 
					this.createChangeFinancialEntityStatusNamedParameters(idFinancialEntity, status);
			this.namedjdbcTemplate.update(this.buildChangeFinancialEntityStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al actualizar el estatus de la Entidad", dataAccessException);
		}
	}

	private MapSqlParameterSource createChangeFinancialEntityStatusNamedParameters(final Integer idFinancialEntity,
			final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		this.addStatusNamedParameter(namedParameters, status);
		this.addIdFinancialEntityNamedParameter(namedParameters, idFinancialEntity);
		return namedParameters;
	}

	private String buildChangeFinancialEntityStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_FINANCIALENTITY_SET + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS + SPACE);
		query.append(WHERE_ID_FINANCIALENTITY_EQUALS_ID_FINANCIALENTITY_PARAM);
		return query.toString();
	}

	@Override
	public String findDefaultCombinationName() throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			final StringBuilder query = new StringBuilder();
			query.append("SELECT Value FROM CONFIGURATION WHERE Name = 'FINANCIAL_ENTITIES_DEFAULT_COMBINATION_NAME' ");
			return this.namedjdbcTemplate.queryForObject(query.toString(), namedParameters, String.class);
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new DatabaseException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Boolean findIsCombination(final Integer idRequisition, final  String combinationName) 
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = 
					this.createfindIsCombinationNamedParameters(idRequisition, combinationName);
			return this.namedjdbcTemplate.queryForObject(this.buildfindIsCombinationQuery(), namedParameters,
					Boolean.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<FinancialEntity> findFinancialEntityByIdRequisition(final Integer idRequisition) 
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = 
					this.createfindFinancialEntityByIdRequisitionNamedParameters(idRequisition);
			return this.namedjdbcTemplate.query(this.buildfindFinancialEntityByIdRequisitionQuery(), 
					namedParameters, new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error obtener las entidades por solicitud", 
					dataAccessException);
		}
	}

	@Override
	public List<FinancialEntitieCombination> findFinancialEntityCombinationDistinctByCombinationName()
			throws DatabaseException {
		try {
			final StringBuilder query = new StringBuilder();
			this.buildfindFinancialEntityCombinationDistinctByCombinationNameQuery(query);
			return this.namedjdbcTemplate.query(query.toString(), new MapSqlParameterSource(),
					new BeanPropertyRowMapper<FinancialEntitieCombination>(FinancialEntitieCombination.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error obtener todas las combinaciones de Entidades", 
					dataAccessException);
		}
	}

	@Override
	public List<FinancialEntity> findAll() throws DatabaseException {
		try {
			final StringBuilder query = new StringBuilder();
			this.buildSelectAllQuery(query);
			return this.namedjdbcTemplate.query(query.toString(), new MapSqlParameterSource(),
					new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error obtener todas las Entidades", dataAccessException);
		}
	}

	private void buildSelectAllQuery(final StringBuilder query) {
		query.append("SELECT " + TableConstants.ID_FINANCIALENTITY + COMMA);
		query.append(TableConstants.NAME + COMMA + TableConstants.STATUS + COMMA + TableConstants.LONG_NAME + COMMA);
		query.append(TableConstants.DOMICILE + SPACE + ",Rfc,Telefono, Telefono AS Phone, Correo, Correo AS Email, Atencion, Atencion AS Attention, Constitutive, ");
		query.append("ConstitutiveEnglish, Treatment, AccountNumber, BankBranch, BankingInstitution, EntidadFinanceiraRegistro AS ConstitutiveRegistred, EntidadFinanceiraRegistroIngles AS ConstitutiveRegistredIngles ");
		query.append(FROM_FINANCIALENTITY);
	}

	private void buildfindFinancialEntityCombinationDistinctByCombinationNameQuery(final StringBuilder query) {
		query.append("SELECT DISTINCT CombinationName FROM FINANCIALENTITIESCOMBINATIONS ");
	}

	@Override
	public List<FinancialEntity> findByRecordStatus(final RecordStatusEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
			return this.namedjdbcTemplate.query(this.buildSelectByStatusQuery(), namedParameters,
					new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error obtener las Entidades por estatus", dataAccessException);
		}
	}

	private String buildSelectByStatusQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS);
		return query.toString();
	}

	private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		this.addStatusNamedParameter(namedParameters, status);
		return namedParameters;
	}

	private void addStatusNamedParameter(final MapSqlParameterSource namedParameters,
			final RecordStatusEnum status) {
		namedParameters.addValue(TableConstants.STATUS, status.toString());
	}

	@Override
	public FinancialEntity findByIdFinancialEntity(final Integer idFinancialEntity) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = 
					this.createFindByFinancialEntityIdNamedParameters(idFinancialEntity);
			return this.namedjdbcTemplate.queryForObject(this.buildFindFinancialEntityByIdQuery(),
					namedParameters, new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? 
					cause : dataAccessException;
			throw new DatabaseException("Error al recuperar la Entidad", exception);
		}
	}

	private MapSqlParameterSource createfindFinancialEntityByIdRequisitionNamedParameters(final Integer idRequisition) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		return namedParameters;
	}

	private MapSqlParameterSource createfindIsCombinationNamedParameters(final Integer idRequisition, 
			final String combinationName) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		namedParameters.addValue(TableConstants.COMBINATION_NAME, combinationName);
		return namedParameters;
	}

	private MapSqlParameterSource createFindByFinancialEntityIdNamedParameters(final Integer idFinancialEntity) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		this.addIdFinancialEntityNamedParameter(namedParameters, idFinancialEntity);
		return namedParameters;
	}

	private void addIdFinancialEntityNamedParameter(final MapSqlParameterSource namedParameters,
			final Integer idFinancialEntity) {
		namedParameters.addValue(TableConstants.ID_FINANCIALENTITY, idFinancialEntity);
	}

	private String buildFindFinancialEntityByIdQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_ID_FINANCIALENTITY_EQUALS_ID_FINANCIALENTITY_PARAM);
		return query.toString();
	}

	private String buildfindFinancialEntityByIdRequisitionQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT  * FROM (SELECT ");
		query.append("RFE.IdFinancialEntity, "); 
		query.append("ROW_NUMBER() OVER (PARTITION BY RFE.IdFinancialEntity ORDER BY RFE.IdFinancialEntity) AS FINANCIAL_ENTITY, ");
		query.append("FE.Name, ");
		query.append("FE.Status, "); 
		query.append("FE.LongName, "); 
		query.append("FE.Domicile, "); 
		query.append("RFE.Phone AS Telefono, "); 
		query.append("RFE.Email AS Correo, "); 
		query.append("RFE.Attention AS Atencion, "); 
		query.append("RFE.rfc, "); 
		query.append("FE.Constitutive, "); 
		query.append("FE.ConstitutiveEnglish, "); 
		query.append("FE.EntidadFinanceiraRegistro AS ConstitutiveRegistred, ");
		query.append("FE.EntidadFinanceiraRegistroIngles AS ConstitutiveRegistredIngles, ");		
		query.append("FE.AccountNumber, "); 
		query.append("FE.BankBranch, "); 
		query.append("FE.BankingInstitution, ");
		query.append("FE.Treatment, ");
		query.append("LR.Name AS LegalRepresentativeName, ");
		query.append("LR.IdLegalRepresentative "); 
		query.append("FROM REQUISITIONFINANCIALENTITY RFE ");  
		query.append("INNER JOIN FINANCIALENTITY FE ON FE.IdFinancialEntity = RFE.IdFinancialEntity "); 
		query.append("LEFT JOIN LEGALREPEFINANCIALENTITIES AS LRF ON FE.IdFinancialEntity = LRF.IdFinancialEntity ");
		query.append("LEFT JOIN LEGALREPRESENTATIVE AS LR ON LRF.IdLegalRepresentative = LR.IdLegalRepresentative AND  LR.Status = 'ACTIVE' "); 
		query.append("LEFT JOIN REQLEGALREPRESENTATIVE AS RLR ON LRF.IdLegalRepresentative = RLR.IdLegalRepresentative AND RLR.IdRequisition = :IdRequisition ");
		query.append(WHERE_RFE_ID_REQUISITION); 
		query.append("AND FE.Status = 'ACTIVE' "); 
		query.append(") AS RESULT_FINANCIAL_ENTITIES "); 
		query.append("WHERE RESULT_FINANCIAL_ENTITIES.FINANCIAL_ENTITY = 1");
		System.out.println("QUERTY Entidades: " + query.toString());
		return query.toString();
	}

	private String buildfindIsCombinationQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT 1 - CASE WHEN COUNT(1) > 0 THEN 1 ELSE 0 END AS IsCombination ");
		query.append("FROM REQUISITIONFINANCIALENTITY RFE LEFT JOIN FINANCIALENTITIESCOMBINATIONS FEC ");
		query.append("ON RFE.IdFinancialEntity = FEC.IdFinancialEntity AND FEC.CombinationName =:CombinationName ");
		query.append(WHERE_RFE_ID_REQUISITION);
		query.append("AND FEC.IdFinancialEntity IS NULL ");
		return query.toString();
	}

	public  void deleteFinancialEntity(final Integer idFinancialEntity) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_FINANCIALENTITY, idFinancialEntity);
		this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
	}

	private String buildDeleteByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM FINANCIALENTITY WHERE IdFinancialEntity = :");
		query.append(TableConstants.ID_FINANCIALENTITY);
		return query.toString();
	}

	public Integer countAll() {
		return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
				new MapSqlParameterSource(), Integer.class);
	}

	private String buildCountAllQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectCountQuery(query);
		return query.toString();
	}

	private void buildSelectCountQuery(final StringBuilder query) {
		query.append(SELECT_COUNT_LEFT_BRACES + TableConstants.ID_FINANCIALENTITY + ")");
		query.append(SPACE + FROM_FINANCIALENTITY);
	}

	public Integer countByStatus(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
		return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(),
				namedParameters, Integer.class);
	}

	private String buildCountByStatusQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectCountQuery(query);
		this.buildWhereStatusQuery(query);
		return query.toString();
	}

	private void buildWhereStatusQuery(final StringBuilder query) {
		query.append(WHERE + TableConstants.STATUS + EQUAL_TAG + TableConstants.STATUS);
	}

	@Override
	public List<FinancialEntity> findAllFinancialEntity() 
			throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindAllFinancialEntityQuery(), 
					new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindAllFinancialEntityQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT FINANCIALENTITY.IdFinancialEntity, Name, Status, LongName, Domicile, ");
		builder.append("IdConfidentialityLaw FROM FINANCIALENTITY ");
		return builder.toString();
	}

	@Override
	public void updateDraftFields(final FinancialEntity financialEntity) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(financialEntity);
			this.namedjdbcTemplate.update(this.updateDraftFieldsQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String updateDraftFieldsQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append(UPDATE_FINANCIALENTITY_SET);
		builder.append("BankingInstitution = :BankingInstitution, AccountNumber = :AccountNumber ");
		builder.append(WHERE_ID_FINANCIALENTITY_EQUALS_ID_FINANCIALENTITY_PARAM);
		return builder.toString();
	}

	@Override
	public List<FinancialEntity> findAllFinancialEntityCatalogPaged(final FinancialEntity financialEntity, 
			final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.statusParameter(financialEntity);
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_FINANCIALENTITY, 
					this.findAllFinancialEntityCatalogPagedQuery(), pagesNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, source, 
					new BeanPropertyRowMapper<FinancialEntity>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findAllFinancialEntityCatalogPagedQuery() {
		final StringBuilder builder = new StringBuilder();
		this.buildSelectAllQuery(builder);
		builder.append("WHERE ((:Status IS NULL) OR (Status = :Status)) ");
		builder.append("AND ((:NameNull IS NULL) OR (Name LIKE :Name)) ");
		builder.append("AND ((:LongNameNull IS NULL) OR (LongName LIKE :LongName)) ");
		builder.append("AND ((:DomicileNull IS NULL) OR (Domicile LIKE :Domicile)) ");
		builder.append("ORDER BY Name ASC ");
		return builder.toString();
	}

	private MapSqlParameterSource statusParameter(final FinancialEntity financialEntity) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue(TableConstants.STATUS, financialEntity.getStatus() == null ? null : financialEntity.getStatus().toString());
		source.addValue(TableConstants.NAME + NULL, financialEntity.getName());
		source.addValue(TableConstants.NAME, LIKE + financialEntity.getName() + LIKE);
		source.addValue(TableConstants.LONG_NAME + NULL, financialEntity.getLongName());
		source.addValue(TableConstants.LONG_NAME, LIKE + financialEntity.getLongName() + LIKE);
		source.addValue(TableConstants.DOMICILE + NULL, financialEntity.getDomicile());
		source.addValue(TableConstants.DOMICILE, LIKE + financialEntity.getDomicile() + LIKE);

		return source;
	}

	@Override
	public Long countTotalItemsToShowOfFinancialEntity(final FinancialEntity financialEntity) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.statusParameter(financialEntity);
			final String countItems = 
					this.databaseUtils.countTotalRows(this.findAllFinancialEntityCatalogPagedQuery());
			return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<FinancialEntity> findDataFinancialentityByRequisitionQuery(
			final Integer idRequisition) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
			return this.namedjdbcTemplate.query(this.buildFindDataFinancialentityByRequisitionEntQuery(),
					namedParameters, 
					new BeanPropertyRowMapper<>(FinancialEntity.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildFindDataFinancialentityByRequisitionEntQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT FE.IdFinancialEntity, FE.Name, FER.Phone, FER.Email, ");
		query.append("FER.Attention, FER.Rfc, FE.AccountNumber, FE.BankBranch, FE.BankingInstitution ");
		query.append("FROM REQUISITIONFINANCIALENTITY FER ");
		query.append("INNER JOIN FINANCIALENTITY FE ON FER.IdFinancialEntity = FE.IdFinancialEntity ");
		query.append("WHERE IdRequisition = :IdRequisition ");
		return query.toString();
	}
}