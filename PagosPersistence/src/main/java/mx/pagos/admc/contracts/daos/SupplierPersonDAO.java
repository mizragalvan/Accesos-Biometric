package mx.pagos.admc.contracts.daos;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.pagos.admc.contracts.constants.TableConstants;
import mx.pagos.admc.contracts.interfaces.SupplierPersonable;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.general.exceptions.DatabaseException;

/**
 * 
 * @author Mizraim
 * 
 * @see SupplierPerson
 * @see SupplierPersonable
 * @see DatabaseException
 *
 */
@Repository
public class SupplierPersonDAO implements SupplierPersonable {

	private static final String DATABASE_DATE_FORMAT = "yyyy-MM-dd";
	private static final String WHERE_ID_SUPPLIER_AND_SUPPLIER_PERSON_TYPE =
			"WHERE IdSupplier = :IdSupplier AND SupplierPersonType = :SupplierPersonType";
	private static final String UPDATE_SUPPLIER_PERSON_SET = "UPDATE SUPPLIERPERSON SET ";
	private static final String WHERE_ID_EQUALS_ID = "WHERE IdSupplierPerson = :IdSupplierPerson ";
	private static final String SINGLE_QUOTE = "'";
	private static final String WHERE_ID_SUPPLIER_AND_TYPE_AND_NAME =
			"WHERE IdSupplier = :IdSupplier AND SupplierPersonType = :SupplierPersonType AND Name = :Name";
    private static final String AND_ACTIVE = " AND Active = :Active";			
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
	private static final String UPDATE_SUPPLIER_PERSON = "UPDATE SUPPLIERPERSON SET ";
	private static final String WHERE_ID_SUPPLIER_PERSON_EQUALS_ID_SUPPLIER = " WHERE IdSupplierPerson = :IdSupplierPerson";


	@Override
	public Integer save(final SupplierPerson supplierPerson) throws DatabaseException {
		try {
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertQuery(), this.createInsertNamedParameters(supplierPerson), 
					keyHolder, new String[]{"IdSupplierPerson"});
			return keyHolder.getKey().intValue();
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<SupplierPerson> findSupplierPersonsByIdSupplierAndType(
			final Integer idSupplier, final SupplierPersonTypeEnum supplierPersontype) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters =
					this.createFindSupplierPersonsByIdSupplierAndTypeNAmedParameters(idSupplier, supplierPersontype);
			return this.namedjdbcTemplate.query(this.buildFindSupplierPersonsByIdSupplierAndTypeQuery(),
					namedParameters, new BeanPropertyRowMapper<SupplierPerson>(SupplierPerson.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteSupplierPersonByIdSupplierAndType(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters =
					this.createFindSupplierPersonsByIdSupplierAndTypeNAmedParameters(idSupplier, supplierPersontype);
			this.namedjdbcTemplate.update(this.buildDeleteSupplierPersonByIdSupplierAndTypeQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException); 
		}
	}

	public void deleteByIdSupplierPerson(final Integer idSupplierPerson) {
		final MapSqlParameterSource namedParameters =
				this.createFindByIdSupplierPersonNamedParameters(idSupplierPerson);
		this.namedjdbcTemplate.update(this.deleteByIdSupplierPersonQuery(), namedParameters);
	}

	public SupplierPerson findByIdSupplierPerson(final Integer idSupplierPerson) {
		final MapSqlParameterSource namedParameters =
				this.createFindByIdSupplierPersonNamedParameters(idSupplierPerson);
		return this.namedjdbcTemplate.queryForObject(this.buildFindByIdSupplierPersonQuery(), namedParameters,
				new BeanPropertyRowMapper<SupplierPerson>(SupplierPerson.class));
	}

	public Integer countSupplierPersonsByIdSupplierAndType(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype) {
		final MapSqlParameterSource namedParameters =
				this.createFindSupplierPersonsByIdSupplierAndTypeNAmedParameters(idSupplier, supplierPersontype);
		return this.namedjdbcTemplate.queryForObject(this.buildCountSupplierPersonsByIdSupplierAndTypeQuery(),
				namedParameters, Integer.class);
	}

	private String buildAllNonPrimaryKeyFields() {
		return "IdSupplier, Name, SupplierPersonType, DeedNumber, DeedDate, PublicNotaryName, DeedNotaryNumber,"
				+ " PublicNotaryState, CommercialFolio, CommercialFolioInscriptionDate, CommercialOrPropertyRegister,"
				+ " CommercialFolioInscriptionState ";
	}

	private String buildAllNonPrimaryKeyFieldsSelect() {
		return "IdSupplier, Name, SupplierPersonType, DeedNumber, DeedDate, PublicNotaryName, DeedNotaryNumber,"
				+ " PublicNotaryState, CommercialFolio, CommercialFolioInscriptionDate, CommercialOrPropertyRegister,"
				+ " CommercialFolioInscriptionState, Active ";
	}

	private void buildSelectAllQuery(final StringBuilder query) {
		query.append("SELECT IdSupplierPerson, " + this.buildAllNonPrimaryKeyFieldsSelect());
		query.append("FROM SUPPLIERPERSON ");
	}

	private String buildInsertQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO SUPPLIERPERSON (");
		query.append(this.buildAllNonPrimaryKeyFields());
		query.append(") VALUES (");
		query.append(":" + this.buildAllNonPrimaryKeyFields().replaceAll(", ", ", :"));
		query.append(")");
		return query.toString();
	}

	private BeanPropertySqlParameterSource createInsertNamedParameters(final SupplierPerson supplierPerson) {
		final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(supplierPerson);
		source.registerSqlType(TableConstants.SUPPLIER_PERSON_TYPE, Types.VARCHAR);
		return source;
	}

	private String buildFindSupplierPersonsByIdSupplierAndTypeQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_ID_SUPPLIER_AND_SUPPLIER_PERSON_TYPE);
		return query.toString();
	}

	private MapSqlParameterSource createFindSupplierPersonsByIdSupplierAndTypeNAmedParameters(final Integer idSupplier,
			final SupplierPersonTypeEnum supplierPersontype) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
		namedParameters.addValue(TableConstants.SUPPLIER_PERSON_TYPE, supplierPersontype.toString());
		return namedParameters;
	}

	private String buildDeleteSupplierPersonByIdSupplierAndTypeQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM SUPPLIERPERSON ");
		query.append(WHERE_ID_SUPPLIER_AND_SUPPLIER_PERSON_TYPE);
		return query.toString();
	}

	private String deleteByIdSupplierPersonQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM SUPPLIERPERSON WHERE IdSupplierPerson = :IdSupplierPerson");
		return query.toString();
	}

	private MapSqlParameterSource createFindByIdSupplierPersonNamedParameters(final Integer idSupplierPerson) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER_PERSON, idSupplierPerson);
		return namedParameters;
	}

	private String buildFindByIdSupplierPersonQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append("WHERE IdSupplierPerson = :IdSupplierPerson");
		return query.toString();
	}

	private String buildCountSupplierPersonsByIdSupplierAndTypeQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(IdSupplierPerson) FROM SUPPLIERPERSON ");
		query.append(WHERE_ID_SUPPLIER_AND_SUPPLIER_PERSON_TYPE);
		return query.toString();
	}

	@Override
	public void saveLegalRepresentativePower(final Power power) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(power);
			this.namedjdbcTemplate.update(this.buildSaveLegalRepresentativePowerQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException); 
		}
	}

	private String buildSaveLegalRepresentativePowerQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO SUPPLIERLEGALREPPOWER (IdSupplierPerson, Name) VALUES (:IdSupplierPerson, :Name) ");
		return query.toString();
	}

	@Override
	public void deleteLegalRepresentativesPowers(final Integer idSupplier) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER, idSupplier);
			this.namedjdbcTemplate.update(this.buildDeleteLegalRepresentativesPowersQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException); 
		}
	}

	private String buildDeleteLegalRepresentativesPowersQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM SUPPLIERLEGALREPPOWER WHERE IdSupplierPerson IN ( ");
		query.append("SELECT IdSupplierPerson FROM SUPPLIERPERSON WHERE SupplierPersonType = 'LEGALREPRESENTATIVE' ");
		query.append("AND IdSupplier = :IdSupplier) ");
		return query.toString();
	}

	@Override
	public String findLegalRepresentativePower(final Integer idSupplierPerson) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
			namedParameters.addValue(TableConstants.ID_SUPPLIER_PERSON, idSupplierPerson);
			return this.namedjdbcTemplate.queryForObject(this.buildFindLegalRepresentativePowersQuery(), 
					namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException); 
		}
	}

	private String buildFindLegalRepresentativePowersQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT IdSupplierPerson, Name FROM SUPPLIERLEGALREPPOWER ");
		query.append("WHERE IdSupplierPerson = :IdSupplierPerson ");
		return query.toString();
	}

	@Override
	public List<SupplierPerson> findSupplierLegalRepresentativesPower(final Integer idSupplierPerson) 
			throws DatabaseException {
		try {
			final MapSqlParameterSource source = new MapSqlParameterSource(TableConstants.ID_SUPPLIER, idSupplierPerson);
			return this.namedjdbcTemplate.query(this.findSupplierLegalRepresentativesPowerQuery(), 
					source, new BeanPropertyRowMapper<SupplierPerson>(SupplierPerson.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String findSupplierLegalRepresentativesPowerQuery() {
		final StringBuilder builder = new StringBuilder();
		builder.append("SELECT SUPPLIERPERSON.Name, SUPPLIERLEGALREPPOWER.Name AS Power ");
		builder.append("FROM SUPPLIERPERSON INNER JOIN SUPPLIERLEGALREPPOWER ");
		builder.append("ON SUPPLIERPERSON.IdSupplierPerson = SUPPLIERLEGALREPPOWER.IdSupplierPerson ");
		builder.append("WHERE IdSupplier = :IdSupplier ");
		builder.append("ORDER BY SUPPLIERPERSON.Name ");
		return builder.toString();
	}

	@Override
	public void changePersonSupplierStatus(final SupplierPerson supplierPerson)
			throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createChangeStatusNamedParameters(supplierPerson);
			this.namedjdbcTemplate.update(this.buildChangeSupplierStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createChangeStatusNamedParameters(
			final SupplierPerson supplierPerson) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_SUPPLIER_PERSON, supplierPerson.getIdSupplierPerson());
		namedParameters.addValue(TableConstants.ACTIVE_MIN, (supplierPerson.isActive() ? 1 : 0));
		return namedParameters;
	}

	private String buildChangeSupplierStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_SUPPLIER_PERSON);
		query.append("Active = :Active ");
		query.append(WHERE_ID_SUPPLIER_PERSON_EQUALS_ID_SUPPLIER);
		return query.toString();
	}

	@Override
	public void updateSupplier (final SupplierPerson supplierPerson) throws DatabaseException {
		try {
			this.namedjdbcTemplate.update(this.buildUpdateQuery(), this.supplierPersonParameterSource(supplierPerson));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildUpdateQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(UPDATE_SUPPLIER_PERSON_SET);
		query.append(" IdSupplier = :IdSupplier, Name = :Name, SupplierPersonType = :SupplierPersonType, DeedNumber = :DeedNumber, ");
		query.append(" DeedDate = :DeedDate, PublicNotaryName =:PublicNotaryName, DeedNotaryNumber =:DeedNotaryNumber, ");
		query.append("PublicNotaryState =:PublicNotaryState, CommercialFolio =:CommercialFolio, ");
		query.append("CommercialFolioInscriptionDate =:CommercialFolioInscriptionDate, ");
		query.append("CommercialOrPropertyRegister = :CommercialOrPropertyRegister, CommercialFolioInscriptionState =:CommercialFolioInscriptionState ");
		query.append(WHERE_ID_EQUALS_ID);
		return query.toString();
	}

	private MapSqlParameterSource supplierPersonParameterSource(final SupplierPerson person) {
		final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATABASE_DATE_FORMAT);


		final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
		parameterSource.addValue(TableConstants.ID_SUPPLIER_PERSON, person.getIdSupplierPerson());
		parameterSource.addValue(TableConstants.ID_SUPPLIER, person.getIdSupplier());
		parameterSource.addValue(TableConstants.NAME, person.getName());
		parameterSource.addValue(TableConstants.SUPPLIER_PERSON_TYPE, person.getSupplierPersonType().toString());
		parameterSource.addValue(TableConstants.DEED_NUMBER, person.getDeedNumber());
		parameterSource.addValue(TableConstants.PUBLIC_NOTARY_NAME, person.getPublicNotaryName());
		parameterSource.addValue(TableConstants.DEED_NOTARY_NUMBER, person.getDeedNotaryNumber());
		parameterSource.addValue(TableConstants.PUBLIC_NOTARY_STATE, person.getPublicNotaryState());
		parameterSource.addValue(TableConstants.COMMERCIAL_FOLIO, person.getCommercialFolio());

		parameterSource.addValue(TableConstants.COMMERCIAL_PROPERTY_REGISTER, person.getCommercialOrPropertyRegister());
		parameterSource.addValue(TableConstants.COMMERCIAL_FOLIO_INST_STATE, person.getCommercialFolioInscriptionState());
		parameterSource.addValue(TableConstants.ACTIVE_MIN, (person.isActive() ? 1 : 0));

		parameterSource.addValue(TableConstants.DEED_DATE, person.getDeedDate());
		parameterSource.addValue(TableConstants.COMMERCIAL_FOLIO_INST_DATE, person.getCommercialFolioInscriptionDate());
		return parameterSource;
	}

	private String darFormatoFecha (SimpleDateFormat toDateTimeFormat, String today) {
		if(today != null && today.trim() != "") {
			return SINGLE_QUOTE + toDateTimeFormat.format(new Date()) + SINGLE_QUOTE;
		}
		return null;
	}


	@Override
	public List<SupplierPerson> findSupplierPersonByIdSupplierAndTypeAndName (
			final Integer idSupplier, final SupplierPersonTypeEnum supplierPersontype, final String name) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindSupplierPersonsByIdSupplierAndTypeNAmedParameters(idSupplier, supplierPersontype);
			namedParameters.addValue(TableConstants.NAME, name);
			return this.namedjdbcTemplate.query(this.createfindSupplierPersonQuery(),
					namedParameters, new BeanPropertyRowMapper<SupplierPerson>(SupplierPerson.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}


	private String createfindSupplierPersonQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAllQuery(query);
		query.append(WHERE_ID_SUPPLIER_AND_TYPE_AND_NAME);
		return query.toString();
	}


	@Override
	public List<SupplierPerson> findLegalRepresentativesByIdRequisition(final Integer idRequisition, final SupplierPersonTypeEnum supplierPersontype) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindLegalRepByIdRequisitionParams(idRequisition, supplierPersontype);
			return this.namedjdbcTemplate.query(this.buildFindLegalRepByIdRequisitionQueryeQuery(),
					namedParameters, new BeanPropertyRowMapper<SupplierPerson>(SupplierPerson.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource createFindLegalRepByIdRequisitionParams(final Integer idRequisition, final SupplierPersonTypeEnum type) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
		namedParameters.addValue(TableConstants.SUPPLIER_PERSON_TYPE, type.toString());
		return namedParameters;
	}

	private String buildFindLegalRepByIdRequisitionQueryeQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT SP.IdSupplierPerson AS IdSupplierPerson, SP.IdSupplier AS IdSupplier, SP.Name AS Name, ");
		query.append("SP.SupplierPersonType AS SupplierPersonType, SP.DeedNumber AS DeedNumber, SP.DeedDate AS DeedDate, ");
		query.append("SP.PublicNotaryName AS PublicNotaryName, SP.DeedNotaryNumber AS DeedNotaryNumber, ");
		query.append("SP.PublicNotaryState AS PublicNotaryState, SP.CommercialFolio AS CommercialFolio,");
		query.append("SP.CommercialFolioInscriptionDate AS CommercialFolioInscriptionDate, SP.CommercialOrPropertyRegister AS CommercialOrPropertyRegister, ");
		query.append("SP.CommercialFolioInscriptionState AS CommercialFolioInscriptionState ");
		query.append("FROM SUPPLIERPERSONBYREQUISITION AS SPR ");
		query.append("INNER JOIN SUPPLIERPERSON AS SP ON SP.IdSupplierPerson = SPR.IdSupplierPerson ");
		query.append("WHERE SPR.IdRequisition = :IdRequisition AND SP.SupplierPersonType = :SupplierPersonType ");
		return query.toString();
	}

	@Override
    public List<SupplierPerson> findSupplierPersonsByIdSupplierAndTypeAtive(
            final Integer idSupplier, final SupplierPersonTypeEnum type, final boolean active) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindSupplierPersonsByIdSupplierAndTypeNAmedParameters(idSupplier, type);
            namedParameters.addValue(TableConstants.ACTIVE_MIN, active ? 1 : 0);
            return this.namedjdbcTemplate.query(this.buildFindSupplierPersonsByIdSupplierAndTypeAndActiveQuery(),
                    namedParameters, new BeanPropertyRowMapper<SupplierPerson>(SupplierPerson.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildFindSupplierPersonsByIdSupplierAndTypeAndActiveQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectAllQuery(query);
        query.append(WHERE_ID_SUPPLIER_AND_SUPPLIER_PERSON_TYPE);
        query.append(AND_ACTIVE);
        return query.toString();
	}
}