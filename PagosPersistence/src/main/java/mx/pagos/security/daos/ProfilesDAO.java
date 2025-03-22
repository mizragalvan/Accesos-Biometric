package mx.pagos.security.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import mx.pagos.admc.contracts.structures.Company;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.constants.TableConstantsSecurity;
import mx.pagos.security.interfaces.Profileable;
import mx.pagos.security.structures.Menu;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.ProfileScreenFlow;

/**
 * Clase que contiene las consultas para accesar a la tabla de perfiles de
 * usuario. Permite guardar, recuperar y cambiar estatus de los perfiles;
 * Guardar y recuperar el menu del perfil; Guardar y recuperar la bandeja de
 * entrada por perfil.
 * 
 * @see Profile
 * @see Profileable
 * @see DatabaseException
 * @see NamedParameterJdbcTemplate
 */
@Repository
public class ProfilesDAO implements Profileable {
	private static final Logger Log = LoggerFactory.getLogger(ProfilesDAO.class);
	private static final String FACTORY_NAME = "FactoryName";
	private static final String ID_PROFILE = "IdProfile";

	@Autowired
	private NamedParameterJdbcTemplate namedjdbcTemplate;
	@Autowired
	private DatabaseUtils databaseUtils;

	@Override
	public Integer saveOrUpdate(final Profile profile) throws DatabaseException {
		return profile.getIdProfile() == null ? this.insertProfile(profile) : this.updateProfile(profile);
	}

	private Integer insertProfile(final Profile profile) throws DatabaseException {
		Log.info("Se inicia insersion de perfil");
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(TableConstantsSecurity.NAME,
					profile.getName());
			final KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedjdbcTemplate.update(this.buildInsertQuery(), namedParameters, keyHolder, new String[] { "IdProfile" });
			return keyHolder.getKey().intValue();
		} catch (DataAccessException e) {
			Log.error(e.getMessage());
			throw new DatabaseException(e);
		}
	}

	private Integer updateProfile(final Profile profile) throws DatabaseException {
		try {
			Log.info("Se inicia actualizacion de perfil");
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(profile);
			this.namedjdbcTemplate.update(this.buildUpdateQuery(), namedParameters);
			return profile.getIdProfile();
		} catch (DataAccessException e) {
			Log.error(e.getMessage());
			throw new DatabaseException(e);
		}
	}

	@Override
	public void changeProfileStatus(final Integer idProfile, final RecordStatusEnum status) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createChangeStatusNamedParameters(idProfile, status);
			this.namedjdbcTemplate.update(this.buildChangeStatusQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Profile findById(final Integer idProfile) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(ID_PROFILE, idProfile);
			return this.namedjdbcTemplate.queryForObject(this.buildFindByIdQuery(), namedParameters,
					new BeanPropertyRowMapper<Profile>(Profile.class));
		} catch (DataAccessException dataAccessException) {
			final Throwable cause = dataAccessException.getMostSpecificCause();
			final Throwable exception = EmptyResultDataAccessException.class.equals(cause.getClass()) ? cause
					: dataAccessException;
			throw new DatabaseException(exception);
		}
	}

	@Override
	public List<Profile> findAll() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildSelectAllQuery(),
					new BeanPropertyRowMapper<Profile>(Profile.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Profile> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(TableConstantsSecurity.STATUS,
					status.toString());
			return this.namedjdbcTemplate.query(this.buildFindByStatusQuery(), namedParameters,
					new BeanPropertyRowMapper<Profile>(Profile.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void saveNewMenuProfile(final Integer idProfile, final String menuItem) throws DatabaseException {
		try {
			Log.info("Se inicia la insersion: " + idProfile + " con " + menuItem);
			final MapSqlParameterSource namedParameters = this.createAddMenuProfileNamedParameters(idProfile, menuItem);
			this.namedjdbcTemplate.update(this.buildInsertMenuProfileQuery(), namedParameters);
		} catch (DataAccessException e) {
			Log.error(e.getMessage());
			throw new DatabaseException(e);
		}
	}

	@Override
	public void deleteMenuProfileByIdProfile(final Integer idProfile) throws DatabaseException {
		Log.info("Se inicia eliminacion de referencias de menu de perfiles por id de perfil");
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(ID_PROFILE, idProfile);
			this.namedjdbcTemplate.update(this.buildDeleteMenuProfileByIdProfileQuery(), namedParameters);
		} catch (DataAccessException e) {
			Log.error(e.getMessage());
			throw new DatabaseException(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Menu> findMenuProfileByIdProfileList(final List<Profile> profileList) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdProfileAnfDlowListParameters(profileList);
			return this.namedjdbcTemplate.query(this.buildFindMenuProfileByIdProfile(), namedParameters,
					new BeanPropertyRowMapper<Menu>(Menu.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Menu> findMenuProfileByIdProfile(final Integer idProfile) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(ID_PROFILE, idProfile);
			return this.namedjdbcTemplate.query(this.buildFindMenuProfileById(), namedParameters,
					new BeanPropertyRowMapper<Menu>(Menu.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException("Error al obtener el menu por perfil", dataAccessException);
		}
	}

	private String buildFindMenuProfileById() {
		final StringBuilder query = new StringBuilder();
		query.append(" SELECT M.FactoryName,M.FactoryNameParent,M.MenuLevel FROM MENUPROFILE MP");
		query.append(" INNER JOIN MENU M ON M.FactoryName=MP.FactoryName ");
		query.append(" WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	@Override
	public void saveProfileScreenFlow(final ProfileScreenFlow profileScreenFlow) throws DatabaseException {
		try {
			final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(profileScreenFlow);
			this.namedjdbcTemplate.update(this.buildSaveNewProfileScreenFlowQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<ProfileScreenFlow> findProfileScreenTrayByProfilesAndIdFlow(final List<Profile> profilesList,
			final Integer idFlow) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByIdProfileAndIdFlowParameters(profilesList, idFlow);
			return this.namedjdbcTemplate.query(this.buildFindProfileScreenTrayByIdProfileQuery(), namedParameters,
					new BeanPropertyRowMapper<ProfileScreenFlow>(ProfileScreenFlow.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public void deleteProfileScreenFlowByIdProfile(final Integer idProfile) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(ID_PROFILE, idProfile);
			this.namedjdbcTemplate.update(this.buildDeleteProfileScreenByIdProfileQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Flow> findFlowsByProfiles(final List<Profile> profilesList) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindFlowsByProfilesNamedParameters(profilesList);
			return this.namedjdbcTemplate.query(this.buildFindFlowsByProfilesQuery(), namedParameters,
					new BeanPropertyRowMapper<Flow>(Flow.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Unit> findUnitsByProfiles(List<Company> idFlow) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindUnitsByProfilesNamedParameters(idFlow);
			return this.namedjdbcTemplate.query(this.buildFindUnitsByProfilesQuery(), namedParameters,
					new BeanPropertyRowMapper<Unit>(Unit.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public List<Company> findCompaniesByProfiles() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.query(this.buildFindCompaniesByProfilesQuery(),
					new BeanPropertyRowMapper<Company>(Company.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	public Integer countAllProfiles() throws DatabaseException {
		try {
			return this.namedjdbcTemplate.queryForObject(this.buildCountAllProfilesQuery(), new MapSqlParameterSource(),
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	public Integer countByRecordStatus(final RecordStatusEnum status) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
			return this.namedjdbcTemplate.queryForObject(this.buildCountByRecordStatusQuery(), namedParameters,
					Integer.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildCountByRecordStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT (IdProfile) FROM PROFILE WHERE Status = :Status");
		return query.toString();
	}

	private String buildCountAllProfilesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(IdProfile) FROM PROFILE");
		return query.toString();
	}

	public Integer countProfileScreenByIdProfileAndFactoryName(final Integer idProfile, final String factoryName) {
		final MapSqlParameterSource namedParameters = this.createFindProfileScreenByIdProfileAndFactoryName(idProfile,
				factoryName);
		return this.namedjdbcTemplate.queryForObject(this.buildCountProfileScreenByIdProfileAndFactoryName(),
				namedParameters, Integer.class);
	}

	private MapSqlParameterSource createFindByIdProfileAnfDlowListParameters(final List<Profile> profileList) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		final Map<String, List<Integer>> profileMap = new HashMap<String, List<Integer>>();
		final List<Integer> profileIdsList = new ArrayList<Integer>();
		for (Profile profile : profileList)
			profileIdsList.add(profile.getIdProfile());
		profileMap.put(TableConstantsSecurity.ID_PROFILE, profileIdsList);
		namedParameters.addValues(profileMap);
		return namedParameters;
	}

	private String buildFindMenuProfileByIdProfile() {
		final StringBuilder query = new StringBuilder();
		query.append(
				"SELECT DISTINCT MENU.FactoryName, MenuLevel, FactoryNameParent, Application, Factory, Tipo as Type, Image as Imagen, Nombre, Collapse, Orden FROM MENU INNER JOIN MENUPROFILE");
		query.append(" ON(MENU.FactoryName = MENUPROFILE.FactoryName) ");
		query.append(" WHERE MENUPROFILE.IdProfile IN (:IdProfile)");
		query.append(" ORDER BY MENU.Orden ASC");
		return query.toString();
	}

	private String buildDeleteProfileScreenByIdProfileQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM PROFILESCREENFLOW WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	private String buildSaveNewProfileScreenFlowQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("INSERT INTO PROFILESCREENFLOW (IdProfile, FactoryName, IdFlow, IsUserFiltered) ");
		query.append("VALUES (:IdProfile, :FactoryName, :IdFlow, :IsUserFiltered)");
		return query.toString();
	}

	private String buildInsertMenuProfileQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.INSERT_INTO)
				.append(TableConstantsSecurity.TABLE_MENU_PROFILE + QueryConstants.LEFT_BRACES);
		query.append(ID_PROFILE + QueryConstants.COMMA);
		query.append(FACTORY_NAME + QueryConstants.RIGHT_BRACES + QueryConstants.VALUES_TAG);
		query.append(ID_PROFILE + QueryConstants.COMMA_TAG);
		query.append(FACTORY_NAME + QueryConstants.RIGHT_BRACES);
		return query.toString();
	}

	private String buildDeleteMenuProfileByIdProfileQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM MENUPROFILE WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	private MapSqlParameterSource createAddMenuProfileNamedParameters(final Integer idProfile, final String menuItem) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(ID_PROFILE, idProfile);
		namedParameters.addValue(TableConstantsSecurity.FACTORY_NAME, menuItem);
		return namedParameters;
	}

	private String buildChangeStatusQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE PROFILE SET Status = :Status WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	private MapSqlParameterSource createChangeStatusNamedParameters(final Integer idProfile,
			final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(ID_PROFILE, idProfile);
		namedParameters.addValue(TableConstantsSecurity.STATUS, status.toString());
		return namedParameters;
	}

	private String buildFindByStatusQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAll(query);
		query.append("WHERE Status = :Status");
		return query.toString();
	}

	private String buildUpdateQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("UPDATE PROFILE SET Name = :Name WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	private String buildFindByIdQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAll(query);
		query.append("WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	private void buildSelectAll(final StringBuilder query) {
		query.append("SELECT IdProfile, Name, Status FROM PROFILE ");
	}

	private String buildSelectAllQuery() {
		final StringBuilder query = new StringBuilder();
		this.buildSelectAll(query);
		return query.toString();
	}

	private MapSqlParameterSource createFindByStatusNamedParameters(final RecordStatusEnum status) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstantsSecurity.STATUS, status.toString());
		return namedParameters;
	}

	@Override
	public void deleteById(final Integer idProfile) throws DatabaseException {
		try {
			final MapSqlParameterSource namedParameters = new MapSqlParameterSource(ID_PROFILE, idProfile);
			this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String buildDeleteByIdQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("DELETE FROM PROFILE WHERE IdProfile = :IdProfile");
		return query.toString();
	}

	private String buildInsertQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(QueryConstants.INSERT_INTO + TableConstantsSecurity.TABLE_PROFILE + QueryConstants.LEFT_BRACES);
		query.append(TableConstantsSecurity.NAME + QueryConstants.SPACE);
		query.append(QueryConstants.RIGHT_BRACES + QueryConstants.VALUES_TAG);
		query.append(TableConstantsSecurity.NAME);
		query.append(QueryConstants.RIGHT_BRACES);
		return query.toString();
	}

	private String buildCountProfileScreenByIdProfileAndFactoryName() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(idProfile) FROM PROFILESCREENFLOW ");
		query.append("WHERE IdProfile = :IdProfile AND FactoryName = :FactoryName");
		return query.toString();
	}

	private MapSqlParameterSource createFindProfileScreenByIdProfileAndFactoryName(final Integer idProfile,
			final String factoryName) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue(TableConstantsSecurity.ID_PROFILE, idProfile);
		namedParameters.addValue(TableConstantsSecurity.FACTORY_NAME, factoryName);
		return namedParameters;
	}

	private MapSqlParameterSource createFindByIdProfileAndIdFlowParameters(final List<Profile> profilesList,
			final Integer idFlow) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		final List<Integer> profileIds = new ArrayList<Integer>();
		final Map<String, List<Integer>> profileMap = new HashMap<>();
		for (Profile profile : profilesList)
			profileIds.add(profile.getIdProfile());
		profileMap.put(TableConstantsSecurity.ID_PROFILE, profileIds);
		namedParameters.addValues(profileMap);
		namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
		return namedParameters;
	}

	private String buildFindProfileScreenTrayByIdProfileQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT SCREEN.FactoryNameTray, SCREEN.FlowStatus FROM PROFILESCREENFLOW ");
		query.append("INNER JOIN SCREEN ON PROFILESCREENFLOW.FactoryName = SCREEN.FactoryName ");
		query.append("WHERE PROFILESCREENFLOW.IdProfile IN (:IdProfile) AND PROFILESCREENFLOW.IdFlow = :IdFlow");
		return query.toString();
	}

	private String buildFindFlowsByProfilesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT FLOW.IdFlow, Name, Status, Type flowType FROM PROFILESCREENFLOW INNER JOIN ");
		query.append("FLOW ON FLOW.IdFlow = PROFILESCREENFLOW.IdFlow WHERE IdProfile IN (:IdProfile) ");
		query.append("AND FLOW.Status = 'ACTIVE'");
		return query.toString();
	}

	private String buildFindUnitsByProfilesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT unit.idUnit, unit.name, unit.status FROM unit INNER JOIN ");
		query.append(
				"FINANCIALENTITY ON FINANCIALENTITY.IdFinancialEntity = unit.IdCompany WHERE unit.IdCompany IN (:IdCompany) ");
		query.append("AND unit.status = 'ACTIVE'");
		query.append(" ORDER BY unit.name ");
		return query.toString();
	}

	private String buildFindCompaniesByProfilesQuery() {
		final StringBuilder query = new StringBuilder();
		query.append(
				" SELECT DISTINCT FINANCIALENTITY.IdFinancialEntity as IdCompany, FINANCIALENTITY.Name, FINANCIALENTITY.Status FROM FINANCIALENTITY ");
		query.append(" WHERE ");
		query.append(" FINANCIALENTITY.Status = 'ACTIVE'");
		return query.toString();
	}

	private MapSqlParameterSource createFindFlowsByProfilesNamedParameters(final List<Profile> profilesList) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		final List<Integer> profileIds = new ArrayList<Integer>();
		final Map<String, List<Integer>> profileMap = new HashMap<>();
		for (Profile profile : profilesList)
			profileIds.add(profile.getIdProfile());
		profileMap.put(TableConstantsSecurity.ID_PROFILE, profileIds);
		Log.info("profile:: " + profileIds);
		namedParameters.addValues(profileMap);
		return namedParameters;
	}

	private MapSqlParameterSource createFindUnitsByProfilesNamedParameters(final List<Company> profilesList) {
		final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		final List<Integer> profileIds = new ArrayList<Integer>();
		final Map<String, List<Integer>> profileMap = new HashMap<>();
		for (Company profile : profilesList)
			profileIds.add(profile.getIdCompany());
		profileMap.put(TableConstantsSecurity.ID_COMPANY, profileIds);
		Log.info("\n======================================== FLOW:: " + profileIds
				+ " =================================================\n");
		namedParameters.addValues(profileMap);
		return namedParameters;
	}

	@Override
	public Boolean validateProfileName(final String profileName) throws DatabaseException {
		try {
			final MapSqlParameterSource sourceParameters = new MapSqlParameterSource();
			sourceParameters.addValue("NAME", profileName);
			return this.namedjdbcTemplate.queryForObject(this.validateProfileNameQuery(), sourceParameters, Boolean.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private String validateProfileNameQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT CASE WHEN COUNT(Name) > 0 THEN 1 ELSE 0 END FROM PROFILE WHERE Name = :NAME");
		return query.toString();
	}

	@Override
	public List<Profile> findByName(final String profileName) throws DatabaseException {
		try {
			final MapSqlParameterSource source = this.profileNameParameters(profileName);
			return this.namedjdbcTemplate.query(this.findByNameQuery(), source,
					new BeanPropertyRowMapper<Profile>(Profile.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	private MapSqlParameterSource profileNameParameters(final String profileName) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		source.addValue(TableConstantsSecurity.NAME + "NULL", profileName);
		source.addValue(TableConstantsSecurity.NAME, "%" + profileName + "%");
		return source;
	}

	private String findByNameQuery() {
		final StringBuilder builder = new StringBuilder();
		this.buildSelectAll(builder);
		builder.append("WHERE (:NameNULL IS NULL) OR (Name LIKE :Name) ");
		builder.append("ORDER BY Name ASC ");
		return builder.toString();
	}

	@Override
	public List<Profile> findAllProfilesCatalogPaged(final String name, final Integer pagesNumber,
			final Integer itemsNumber) throws DatabaseException {
		try {
			final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_PROFILE,
					this.findByNameQuery(), pagesNumber, itemsNumber);
			return this.namedjdbcTemplate.query(paginatedQuery, this.profileNameParameters(name),
					new BeanPropertyRowMapper<Profile>(Profile.class));
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

	@Override
	public Long countTotalItemsToShowOfProfiles(final String name) throws DatabaseException {
		try {
			final String countItems = this.databaseUtils.countTotalRows(this.findByNameQuery());
			return this.namedjdbcTemplate.queryForObject(countItems, this.profileNameParameters(name), Long.class);
		} catch (DataAccessException dataAccessException) {
			throw new DatabaseException(dataAccessException);
		}
	}

}
