package mx.pagos.admc.contracts.daos;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

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

import mx.engineer.utils.database.JdbcTemplateUtils;
import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.DatabaseUtils;
import mx.pagos.admc.contracts.interfaces.Supplierable;
import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.document.version.daos.constants.TableVersionConstants;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * @author Mizraim
 * 
 * @see Supplier
 * @see Supplierable
 * @see DatabaseException
 * @see RecordStatusEnum
 *
 */
@Repository
public class SuppliersDAO implements Supplierable {

	private static final String COMMA = ", ";
	private static final String NULL = "Null";
	private static final String SELECT_COUNT = "SELECT COUNT(IdSupplier) ";
	private static final String WHERE_STATUS_EQUALS_STATUS = "WHERE SUPPLIER.Status = :Status";
	private static final String UPDATE_SUPPLIER = "UPDATE SUPPLIER SET ";
	private static final String FROM_SUPPLIER = "FROM SUPPLIER ";
	private static final String WHERE_ID_SUPPLIER_EQUALS_ID_SUPPLIER = " WHERE IdSupplier = :IdSupplier";
	@Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;

	@Autowired
	private DatabaseUtils databaseUtils;

	@Override
	public Integer saveOrUpdate(final Supplier supplier) throws DatabaseException {
		return supplier.getIdSupplier() == null ? this.insertSupplier(supplier) : this.updateSupplier(supplier);
	}

	private Integer insertSupplier(final Supplier supplier) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(supplier);
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertSupplierQuery(), namedParameters, keyHolder, 
					new String[]{"IdSupplier"});
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private Integer updateSupplier(final Supplier supplier) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(supplier);
			this.namedjdbcTemplate.update(this.buildUpdateSupplierQuery(), namedParameters);
			return supplier.getIdSupplier();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void changeSupplierStatus(final Integer idSupplier, final RecordStatusEnum status)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createChangeStatusNamedParameters(idSupplier, status);
			this.namedjdbcTemplate.update(this.buildChangeSupplierStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Supplier> findAll() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindAllQuery(),
					new BeanPropertyRowMapper<Supplier>(Supplier.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException); 
		}
	}

	@Override
	public List<Supplier> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createCountByStatusNamedParameters(status);
			return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
					new BeanPropertyRowMapper<Supplier>(Supplier.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Supplier findById(final Integer idSupplier) throws DatabaseException, EmptyResultException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdNamedParameters(idSupplier);
			return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
					new BeanPropertyRowMapper<Supplier>(Supplier.class));
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Supplier> findByNameAndRfc(final String name, final String rfc) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByNameAndRfcNamedParameters(name, rfc);
			return this.namedjdbcTemplate.query(this.buildFindByNameAndRfcQuery(), namedParameters,
					new BeanPropertyRowMapper<Supplier>(Supplier.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Supplier findByRfc(final String rfc) throws DatabaseException, EmptyResultException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByRfcNamedParameters(rfc);
			return this.namedjdbcTemplate.queryForObject(this.buildFindByRfcQuery(), namedParameters,
					new BeanPropertyRowMapper<Supplier>(Supplier.class));
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new EmptyResultException(emptyResultDataAccessException);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveRequiredDocument(final Integer idSupplier, final Integer idRequiredDocuent, 
			final Integer idDocument) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.requiredDocumentsParameter(
					idSupplier, idRequiredDocuent, idDocument);
			this.namedjdbcTemplate.update(this.saveRequiredDocumentQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<RequiredDocument> findRequiredDocumentsByIdSupplier(final Integer idSupplier) 
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
			return this.namedjdbcTemplate.query(this.findRequiredDocumentQuery(), namedParameters,
					new BeanPropertyRowMapper<RequiredDocument>(RequiredDocument.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteSupplierRequiredDocument(final Integer idSupplier, final Integer idRequiredDocument) 
			throws DatabaseException {
		try {
			final StringBuilder queryDelete = new StringBuilder();
			queryDelete.append("DELETE FROM SUPPLIERREQUIREDDOCUMENT ");
			queryDelete.append("WHERE IdSupplier = :IdSupplier AND IdRequiredDocument = :IdRequiredDocument ");
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
			namedParameters.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocument);
			this.namedjdbcTemplate.update(queryDelete.toString(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void updateDraftSupplierFields(final Supplier supplier) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(supplier);
			this.namedjdbcTemplate.update(this.buildUpdateDraftSupplierFieldsQuery(), source);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}        
	}

	private String buildFindByIdQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_ID_SUPPLIER_EQUALS_ID_SUPPLIER);
		return query.toString();
	}

	private MapSqlParameterSource createFindByNameAndRfcNamedParameters(final String name, final String rfc) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.NAME + NULL, name);
		namedParameters.addValue(TableConstants.RFC + NULL, rfc);
		namedParameters.addValue(TableConstants.NAME, QueryConstants.ANY_CHARACTER + name 
				+ QueryConstants.ANY_CHARACTER);
		namedParameters.addValue(TableConstants.RFC, QueryConstants.ANY_CHARACTER + rfc + QueryConstants.ANY_CHARACTER);
		return namedParameters;
	}

	private String buildFindByNameAndRfcQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		this.buildWhereNameAndRfcLikeQuery(query);
		System.out.println("QUERY BUSCAR PROVEEDOR BOTON: " + query.toString());
		return query.toString();
	}

	private void buildWhereNameAndRfcLikeQuery(final StringBuilder query) {
		query.append("WHERE ((:RfcNull IS NULL )  OR (Rfc LIKE :Rfc)) AND ");
		query.append("((:NameNull IS NULL )  OR (CommercialName LIKE :Name) OR (CompanyName LIKE :Name)) ");
		query.append("AND SUPPLIER.Status = 'ACTIVE' ");
	}

	private String buildFindAllQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		return query.toString();
	}

	private String buildFindByStatusQuery() {
		final StringBuilder query = new StringBuilder(this.buildFindAllQuery());
		query.append(WHERE_STATUS_EQUALS_STATUS);
		return query.toString();
	}

	private String buildChangeSupplierStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_SUPPLIER);
		query.append("Status = :Status ");
		query.append(WHERE_ID_SUPPLIER_EQUALS_ID_SUPPLIER);
		return query.toString();
	}

	private MapSqlParameterSource createChangeStatusNamedParameters(
			final Integer idSupplier, final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		namedParameters.addValue(TableConstants.STATUS, status.toString());
		return namedParameters;
	}

	private String buildUpdateSupplierQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_SUPPLIER);
		query.append("IdPersonality = :IdPersonality, CommercialName = :CommercialName, CompanyName = :CompanyName, ");
		query.append("Rfc = :Rfc, Imss =:Imss, BankBranch =:BankBranch, Nacionality =:Nacionality, ");
		query.append("AccountNumber = :AccountNumber, SupplierPaymentFinInstitution =:SupplierPaymentFinInstitution, ");
		query.append("SupplierCompanyPurpose=:SupplierCompanyPurpose, CompanyType=:CompanyType, ");
		query.append("NonFiscalAddress = :NonFiscalAddress, FiscalAddress = :FiscalAddress, ");
		query.append("PhoneNumber = :PhoneNumber, Email = :Email, Atention = :Atention, ");
		query.append("Street = :Street, ExteriorNumber = :ExteriorNumber, InteriorNumber = :InteriorNumber, ");
		query.append("Suburb = :Suburb, City = :City, Township = :Township, ");
		query.append("State = :State, PostalCode = :PostalCode, ");
		query.append("StreetMail = :StreetMail, ExteriorNumberMail = :ExteriorNumberMail, ");
		query.append("InteriorNumberMail = :InteriorNumberMail, SuburbMail = :SuburbMail, CityMail = :CityMail, ");
		query.append("TownshipMail = :TownshipMail, StateMail = :StateMail, PostalCodeMail = :PostalCodeMail, ");
		this.buildConstitutiveActFields(query);
		query.append(WHERE_ID_SUPPLIER_EQUALS_ID_SUPPLIER);
		return query.toString();
	}

	private void buildConstitutiveActFields(final StringBuilder query) {
		query.append("PublicDeedPropertyNotary = :PublicDeedPropertyNotary, ");
		query.append("PublicDeedNotaryNumber = :PublicDeedNotaryNumber, ");
		query.append("PublicDeedNotaryState = :PublicDeedNotaryState, CommercialFolio = :CommercialFolio, ");
		query.append("PublicDeedNumber = :PublicDeedNumber, PublicDeedTitleDate = :PublicDeedTitleDate, ");
		query.append("InscriptionFolioDate = :InscriptionFolioDate, InscriptionFolioState = :InscriptionFolioState, ");
		query.append("CommercialOrPropertyRegister = :CommercialOrPropertyRegister ");
	}

	private String buildUpdateDraftSupplierFieldsQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append(UPDATE_SUPPLIER);
		builder.append("CompanyName = :CompanyName, SupplierCompanyPurpose = :SupplierCompanyPurpose, ");
		builder.append("CompanyType = :CompanyType, Rfc = :Rfc, NonFiscalAddress =:nonFiscalAddress, BusinessReferences =:businessReferences, ");
		builder.append("FiscalAddress =:fiscalAddress, Imss =:Imss, ");
		this.buildConstitutiveActFields(builder);
		builder.append(COMMA);
		builder.append(JdbcTemplateUtils.buildUpdateFields(this.buildMailAdressFields(), COMMA)).append(" ");
		builder.append(WHERE_ID_SUPPLIER_EQUALS_ID_SUPPLIER);

		return builder.toString();
	}

	private String buildMailAdressFields() {
		final StringBuilder builder = new StringBuilder();
		builder.append("StreetMail, ExteriorNumberMail, InteriorNumberMail, SuburbMail, CityMail, TownshipMail, ");
		builder.append("StateMail, PostalCodeMail");
		return builder.toString();
	}

	private void buildSelectAllQuery(final StringBuilder query) {
		query.append("SELECT IdSupplier, ");
		this.buildSelectAllNonPrimaryKeyFields(query);
		query.append(", SUPPLIER.Status, PERSONALITY.Name AS PersonalityName ");
		query.append(FROM_SUPPLIER);
		query.append("INNER JOIN PERSONALITY ON SUPPLIER.IdPersonality = PERSONALITY.IdPersonality ");
	}

	private MapSqlParameterSource createFindByIdNamedParameters(final Integer idSupplier) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		return namedParameters;
	}

	private String buildInsertSupplierQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO SUPPLIER (");
		this.buildSelectAllNonPrimaryKeyFields(query);
		query.append(") VALUES (:IdPersonality, :CommercialName, :CompanyName, :Rfc, :Imss, ");
		query.append(":AccountNumber,:SupplierCompanyPurpose,:CompanyType,:NonFiscalAddress,:FiscalAddress, ");
		query.append(":PhoneNumber, :Email, :Atention, :PublicDeedPropertyNotary, :PublicDeedNotaryNumber, ");
		query.append(":PublicDeedNotaryState, :CommercialFolio, :PublicDeedNumber, :PublicDeedTitleDate,  ");
		query.append(":InscriptionFolioDate, :InscriptionFolioState,:BankBranch, :SupplierPaymentFinInstitution, ");
		query.append(":CommercialOrPropertyRegister, :Street, :ExteriorNumber, :InteriorNumber, :Suburb, ");
		query.append(":City, :Township, :State, :PostalCode, ");
		query.append(":StreetMail, :ExteriorNumberMail, :InteriorNumberMail, :SuburbMail, ");
		query.append(":CityMail, :TownshipMail, :StateMail, :PostalCodeMail, :Nacionality, :Position,:BusinessReferences ) ");
		return query.toString();
	}

	private void buildSelectAllNonPrimaryKeyFields(final StringBuilder query) {
		query.append("SUPPLIER.IdPersonality, CommercialName, CompanyName, Rfc, Imss, ");
		query.append("AccountNumber, SupplierCompanyPurpose, CompanyType, NonFiscalAddress, FiscalAddress, ");
		query.append("PhoneNumber, Email, Atention, PublicDeedPropertyNotary, PublicDeedNotaryNumber, ");
		query.append("PublicDeedNotaryState, CommercialFolio, PublicDeedNumber, PublicDeedTitleDate, ");
		query.append("InscriptionFolioDate, InscriptionFolioState, BankBranch, SupplierPaymentFinInstitution, ");
		query.append("CommercialOrPropertyRegister, Street, ExteriorNumber, InteriorNumber, Suburb, City, ");
		query.append("Township, State, PostalCode, StreetMail, ExteriorNumberMail, InteriorNumberMail, ");
		query.append("SuburbMail, CityMail, TownshipMail, StateMail, PostalCodeMail, Nacionality, Position,BusinessReferences ");
	}

	public void deleteById(final Integer idSupplier) {
		this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), this.createDeleteNamedParameters(idSupplier));
	}

	private String buildDeleteByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM SUPPLIER ");
		query.append(WHERE_ID_SUPPLIER_EQUALS_ID_SUPPLIER);
		return query.toString();
	}

	private MapSqlParameterSource createDeleteNamedParameters(final Integer idArea) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idArea);
		return namedParameters;
	}

	public Integer countAll() {
		return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(), new MapSqlParameterSource(),
				Integer.class);
	}

	private String buildCountAllQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectCountFromSupplier(query);
		return query.toString();
	}

	public Integer countByStatus(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = this.createCountByStatusNamedParameters(status);
		return this.namedjdbcTemplate.queryForObject(this.buildCountAllByStatusQuery(),
				namedParameters, Integer.class);
	}

	private MapSqlParameterSource createCountByStatusNamedParameters(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.STATUS, status.toString());
		return namedParameters;
	}

	public String buildCountAllByStatusQuery() {
		final StringBuilder query = new StringBuilder(this.buildCountAllQuery());
		query.append(WHERE_STATUS_EQUALS_STATUS);
		return query.toString();
	}

	public Integer countByNameAndRfc(final String name, final String rfc) {
		final MapSqlParameterSource namedParameters = this.createFindByNameAndRfcNamedParameters(name, rfc);
		return this.namedjdbcTemplate.queryForObject(this.buildCountByNameAndRfcQuery(),
				namedParameters, Integer.class);
	}

	private String buildCountByNameAndRfcQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectCountFromSupplier(query);
		this.buildWhereNameAndRfcLikeQuery(query);
		return query.toString();
	}

	private void buildSelectCountFromSupplier(final StringBuilder query) {
		query.append(SELECT_COUNT);
		query.append(FROM_SUPPLIER);
	}

	private MapSqlParameterSource requiredDocumentsParameter(final Integer idSupplier, final Integer idRequiredDocuent,
			final Integer idDocument) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		namedParameters.addValue(TableConstants.ID_REQUIRED_DOCUMENT, idRequiredDocuent);
		namedParameters.addValue(TableVersionConstants.ID_DOCUMENT, idDocument);
		return namedParameters;
	}

	private String saveRequiredDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO SUPPLIERREQUIREDDOCUMENT(IdSupplier, IdRequiredDocument, IdDocument) ");
		query.append("VALUES(:IdSupplier, :IdRequiredDocument, :IdDocument)");
		return query.toString();
	}

	private String findRequiredDocumentQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SRD.IdRequiredDocument, Name, IdDocument, RD.isRequired ");
		query.append("FROM SUPPLIERREQUIREDDOCUMENT SRD INNER JOIN REQUIREDDOCUMENT RD ");
		query.append("ON SRD.IdRequiredDocument = RD.IdRequiredDocument ");
		query.append("WHERE Status = 'ACTIVE' AND IdSupplier = :IdSupplier");
		return query.toString();
	}

	public Integer countRecordsByIdSupplierQuery(final Integer idSupplier) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		return this.namedjdbcTemplate.queryForObject(this.countRecordsByIdSupplierQuery(), 
				namedParameters, Integer.class);
	}

	private String countRecordsByIdSupplierQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(IdSupplier)");
		query.append("FROM SUPPLIERREQUIREDDOCUMENT WHERE IdSupplier = :IdSupplier");
		return query.toString();
	}

	private String buildFindByRfcQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append("WHERE Rfc = :Rfc");
		System.out.println("QUERY BUSCAR PROVEEDOR AUTOMATICO: " + query.toString());
		return query.toString();
	}

	private MapSqlParameterSource createFindByRfcNamedParameters(final String rfc) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.RFC, rfc);
		return namedParameters;
	}

	@Override
	public Boolean isCompanyNameExist(final String supplierCompanyName) throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource();
			source.addValue(TableConstants.COMPANY_NAME, supplierCompanyName);
			return this.namedjdbcTemplate.queryForObject(this.isCompanyNameExistQuery(), source, Boolean.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Boolean existRFC(final String rfc) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.createFindByRfcNamedParameters(rfc);
			return this.namedjdbcTemplate.queryForObject(this.existRfcQuery(), source, Boolean.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String isCompanyNameExistQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT CASE WHEN COUNT(IdSupplier) > 0 THEN 1 ELSE 0 END ");
		builder.append(FROM_SUPPLIER);
		builder.append("WHERE CompanyName = :CompanyName ");
		return builder.toString();
	}

	private String existRfcQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT CASE WHEN COUNT(IdSupplier) > 0 THEN 1 ELSE 0 END ");
		builder.append(FROM_SUPPLIER);
		builder.append("WHERE RFC = :Rfc ");
		return builder.toString();
	}

	@Override
	public List<Supplier> findAllSupplierCatalogPaged(final Supplier supplier, 
			final Integer pagesNumber, final Integer itemsNumber) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.statusParameter(supplier);
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_POWER, 
					this.findAllSupplierCatalogPagedQuery(), pagesNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, source, 
					new BeanPropertyRowMapper<Supplier>(Supplier.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findAllSupplierCatalogPagedQuery() {
		final StringBuilder builder = new StringBuilder();
		this.buildSelectAllQuery(builder);
		builder.append("WHERE ((:Status IS NULL OR SUPPLIER.Status = :Status) ");
		builder.append("AND (:CompanyNameNULL IS NULL OR SUPPLIER.CompanyName LIKE :CompanyName) ");
		builder.append("AND (:RfcNULL IS NULL OR SUPPLIER.Rfc LIKE :Rfc)) ");
		builder.append("ORDER BY CompanyName ASC ");
		return builder.toString();
	}

	private MapSqlParameterSource statusParameter(final Supplier supplier) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		System.out.println("CADENA PARAMETROS PROVEEDORES: [" + supplier.getCompanyName() + "], [" + supplier.getRfc() + "], [" + supplier.getStatus() + "]");
		source.addValue(TableConstants.STATUS, supplier.getStatus() == null ? null : supplier.getStatus().toString());
		source.addValue(TableConstants.COMPANY_NAME + "NULL", supplier.getCompanyName());
		source.addValue(TableConstants.COMPANY_NAME, "%" + supplier.getCompanyName() + "%");
		source.addValue(TableConstants.RFC + "NULL", supplier.getRfc());
		source.addValue(TableConstants.RFC, "%" + supplier.getRfc() + "%");
		return source;
	}

	@Override
    public Long countTotalItemsToShowOfSupplier(final Supplier supplier) throws DatabaseException {
        try {
            final MapSqlParameterSource source = this.statusParameter(supplier);
			final String countItems = this.databaseUtils.countTotalRows(this.findAllSupplierCatalogPagedQuery());
			return this.namedjdbcTemplate.queryForObject(countItems, source, Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Personality findPersonality(final Integer idSupplier) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
			return this.namedjdbcTemplate.queryForObject(this.createFindPersonalityQuery(), namedParameters,
					new BeanPropertyRowMapper<>(Personality.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String createFindPersonalityQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT PERSONALITY.IdPersonality, PERSONALITY.Name, PERSONALITY.Status, PersonalityEnum ");
		query.append("FROM SUPPLIER INNER JOIN PERSONALITY ON SUPPLIER.IdPersonality = PERSONALITY.IdPersonality ");
		query.append("WHERE SUPPLIER.IdSupplier = :IdSupplier");
		return query.toString();
	}
}
