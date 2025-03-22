package mx.pagos.security.daos;

import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
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
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.LoginEnum;
import mx.pagos.admc.enums.security.UserStatusEnum;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.general.constants.QueryConstants;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.constants.TableConstantsSecurity;
import mx.pagos.security.interfaces.Userable;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.User;

/**
 * Clase que que maneja los usuarios del sistema permitiendo alta, bajas, cambios y obtención de información.
 *
 * @see User
 */
@Repository
public class UsersDAO implements Userable {
    private static final String SPACE_CHAR = "' '";
    private static final String ID_USER = "IdUser";
    private static final String WHERE_ID_USER_EQUALS_ID_USER = "WHERE IdUser = :IdUser ";
    private static final String ERROR_RECUPERAR_USUARIO = "Error al recuperar el usuario por nombre de usuario";
    private static final String NULL = "Null";
    private static final String ORDER_BY_NAME = "ORDER BY U.Name ";
    @Autowired
    private NamedParameterJdbcTemplate namedjdbcTemplate;
    private static final Logger LOG = Logger.getLogger(UsersDAO.class);
    
    @Autowired
    private DatabaseUtils databaseUtils;

    public void setDatabaseUtils(final DatabaseUtils databaseUtilsParameter) {
        this.databaseUtils = databaseUtilsParameter;
    }

    @Override
    public Integer saveOrUpdate(final User user) throws DatabaseException {
        return user.getIdUser() == null ? this.insertUser(user) : this.updateUser(user);
    }

    private Integer insertUser(final User user) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
            final KeyHolder keyHolder = new GeneratedKeyHolder();
            this.namedjdbcTemplate.update(this.buildInsertUserQuery(), namedParameters, keyHolder, 
                    new String[]{ID_USER});
            return keyHolder.getKey().intValue();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al insertar el usuario", dataAccessException);
        }
    }

    private Integer updateUser(final User user) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
            this.namedjdbcTemplate.update(this.buildUpdateUserQuery(), namedParameters);
            return user.getIdUser();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar el usuario", dataAccessException);
        }
    }

    @Override
    public Integer updatePassword(final User user) throws DatabaseException {
        try {
            final BeanPropertySqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
            this.namedjdbcTemplate.update(this.buildUpdatePasswordQuery(), namedParameters);
            return user.getIdUser();
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public User findByUserId(final Integer idUser) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idUser);
            return this.namedjdbcTemplate.queryForObject(this.buildFindUserByIdQuery(), namedParameters,
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public void changeUserStatus(final Integer idUser, final UserStatusEnum status) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createChangeUserStatusNamedParameters(idUser, status);
            this.namedjdbcTemplate.update(this.buildChangeUserStatusQuery(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al actualizar el estatus del usuario", dataAccessException);
        }
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        try {
            final StringBuilder query = this.buildFindAllQuery();
            return this.namedjdbcTemplate.query(query.toString(), new MapSqlParameterSource(),
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener todos los usuarios", dataAccessException);
        }
    }

    @Override
    public List<User> findByStatus(final UserStatusEnum status) throws DatabaseException {
        try {
            final StringBuilder query = this.buildSelectByStatusQuery();
            final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
            return this.namedjdbcTemplate.query(query.toString(), namedParameters,
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error obtener los usuarios por estatus", dataAccessException);
        }
    }

    @Override
    public User findByUsername(final String username) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindUsernameNamedParameters(username);
            return this.namedjdbcTemplate.queryForObject(this.buildFindByUsernameQuery(), namedParameters,
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(ERROR_RECUPERAR_USUARIO, dataAccessException);
        }
    }
    @Override
	public List<User> findGerente(final int idUser) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters =this.createFindRequisitionByManyParametersprueba(idUser);

            return this.namedjdbcTemplate.query(this.buildFindAllRequisitionsQuery(), namedParameters,
                    new BeanPropertyRowMapper<User>(User.class));
		} catch (DataAccessException dataAccessException) {
			LOG.error("error al intentar hacer el query", dataAccessException);
			throw new DatabaseException("error en el query",dataAccessException);
		}
	}
    
    private MapSqlParameterSource createFindRequisitionByManyParametersprueba( final int idUser ) {
		final MapSqlParameterSource source = new MapSqlParameterSource();
		LOG.info("---------llego el idusuario al query ----------------------------------------------------------------------------");
		LOG.info("-"+idUser );
		LOG.info("--------------------------------------------------------------------------------------");
		source.addValue("idUser", idUser);
	       LOG.info("QUERY -> createFindRequisitionsToCreateOneFromAnotherParameters -PARAMETROS LIKE ");
		return source;
    }
    private String buildFindAllRequisitionsQuery() {
		final StringBuilder query = new StringBuilder();
		query.append("SELECT USERS.IdUser,USERS.IdPosition,USERS.IdArea,USERS.Username, USERS.Name,USERS.FirstLastName, USERS.SecondLastName, USERS.Status, ");
		query.append("PROFILE.Name as positionName, AREA.Name as areaName, PROFILEUSER.idProfile ");
		query.append("FROM USERS  ");
		query.append("LEFT JOIN PROFILEUSER ON PROFILEUSER.IdUser=USERS.IdUser ");
		query.append("INNER JOIN PROFILE ON PROFILE.idProfile=PROFILEUSER.idProfile ");
		query.append("INNER JOIN AREA ON AREA.IdArea = USERS.IdArea  ");
	    query.append("WHERE USERS.IdUser = :idUser");
		return query.toString();
	}

    private MapSqlParameterSource createFindUsernameNamedParameters(final String username) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstantsSecurity.USERNAME, username);
        return namedParameters;
    }

    private String buildFindByUsernameQuery() {
        final StringBuilder query = this.buildSelectUserFields();
        query.append(" FROM USERS WHERE Username = :Username");
        return query.toString();
    }

    private StringBuilder buildSelectUserFields() {
        final StringBuilder querySelect = new StringBuilder();
        querySelect.append("SELECT IdUser, IdUnderdirector, IdPosition, IdArea, IdDga, Username, Name, FirstLastName, ");
        querySelect.append("SecondLastName, PhoneNumber, Email, Territory, Status, IsLawyer, IsEvaluator, ");
        querySelect.append("RetriesNumber, StatusLogin, IsDecider ");
        return querySelect;
    }

    @Override
    public User findByUsernameForLogin(final String username) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindUsernameNamedParameters(username);
            return this.namedjdbcTemplate.queryForObject(this.buildFindLoginAllQuery(), namedParameters,
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(ERROR_RECUPERAR_USUARIO, dataAccessException);
        }
    }

    private String buildFindLoginAllQuery() {
        final StringBuilder querySelect = this.buildSelectUserFields();
        querySelect.append(", Username, Password, Salt FROM USERS WHERE Username = :Username");
        return querySelect.toString();
    }

    @Override
    public Integer getCountCorrespondPasswordByUsername(final String username, final String password)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createCountCorrespondPasswordByUsernameNamedParameters(
                    username, password);
            return this.namedjdbcTemplate.queryForObject(this.buildCountCorrespondPasswordByUsernameQuery(),
                    namedParameters, Integer.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al consultar la base de datos", dataAccessException);
        }
    }

    @Override
    public void saveProfileByIdUser(final Integer idUser, final Integer idProfile)
            throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters =
                    this.createSaveProfileByIdUserNamedParameters(idUser, idProfile);
            this.namedjdbcTemplate.update(this.buildSaveProfileByIdUser(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al guardar el perfil del usuario", dataAccessException);
        }
    }

    @Override
    public void deleteProfilesListByIdUser(final Integer idUser) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idUser);
            this.namedjdbcTemplate.update(this.buildDeleteProfilesListByIdUser(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al borrar los perfiles por usuario", dataAccessException);
        }

    }

    @Override
    public List<Profile> findProfilesByIdUser(final Integer idUser) throws DatabaseException {
        try {
        	System.out.println("findProfilesByIdUser -> idUser: " + idUser);
            final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idUser);
            return this.namedjdbcTemplate.query(this.buildFindProfilesByIdUserQuery(), namedParameters,
                    new BeanPropertyRowMapper<Profile>(Profile.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al obtener los perfiles del usuario", dataAccessException);
        }
    }

    @Override
    public List<User> findUsersByName(final String name,final String apaterno,final String amaterno) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindUsersByNameParameters(name,apaterno,amaterno);
            return this.namedjdbcTemplate.query(this.buildFindUsersByName(), namedParameters, 
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    @Override
    public List<User> findLawyersByIdUser() throws DatabaseException {
        try {
            return this.namedjdbcTemplate.query(this.buildFindLawyersByIdUser(), 
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
        	LOG.info("No se pudo consultar abogados por idUser.");
            throw new DatabaseException(dataAccessException);
        }
    }
    @Override
    public List<User> findUserMailAddress(String req) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_REQUISITION, req);
            return this.namedjdbcTemplate.query(this.buildFindUserMailAddress(), namedParameters, 
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
        	LOG.info("Error al consultar correo del uduario. UsersDAO - findUserMailAddress");
            throw new DatabaseException(dataAccessException);
        }
    }
	@Override
	public String findLawyerNameByIdRequisition(String req) throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue(TableConstants.ID_REQUISITION, req);
            return this.namedjdbcTemplate.queryForObject(this.buildFindLawyerName(), namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al consultar el nombre del abogado. UsersDAO - findNameLawyerByIdRequisition", dataAccessException);
		}
	}
    @Override
    public Boolean saveChangesLawyer(final ConsultaList req) throws DatabaseException {
        try {
        	final MapSqlParameterSource sourceParameters = new MapSqlParameterSource();
        	sourceParameters.addValue(ID_USER, req.getParam4());
        	sourceParameters.addValue(TableConstantsSecurity.ID_REQUISITION, req.getParam5());        	
            this.namedjdbcTemplate.update(this.buildSaveChangesLawyer(), sourceParameters);
            return true;
        } catch (DataAccessException dataAccessException) {
        	LOG.info("No se pudo actualizar la columna idLawyer en la bd.");
            throw new DatabaseException(dataAccessException);
        }
    }
    @Override
    public List<User> findUsersByNameOrLastNameAndIdRequisition(final String name, final Integer idRequisition, 
            final Integer idFlow) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = 
                    this.createFindUsersByNameOrLastNameParameters(name, idRequisition, idFlow);
            return this.namedjdbcTemplate.query(this.buildFindUsersByNameOrLastName(), namedParameters, 
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public List<User> findUsersByArea(final Integer idArea) throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(TableConstants.ID_AREA, idArea);
            return this.namedjdbcTemplate.query(this.buildFindUserByAreaQuery(), source, 
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Boolean isProfileUserFiltered(final Integer idUser, final Integer idFlow, 
            final FlowPurchasingEnum status)throws DatabaseException {
        try {
            final MapSqlParameterSource source = new MapSqlParameterSource();
            source.addValue(ID_USER, idUser);
            source.addValue(TableConstants.ID_FLOW, idFlow);
            source.addValue(TableConstants.STATUS, status.toString());
            return this.namedjdbcTemplate.queryForObject(this.buildIsProfileUserFilteredQuery(), source, Boolean.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    public void registerRetry(final Integer idUser, final Integer retriesNumber) throws DatabaseException {
        try {
            final MapSqlParameterSource sourceParameters = new MapSqlParameterSource();
            sourceParameters.addValue(ID_USER, idUser);
            sourceParameters.addValue("RetriesNumber", retriesNumber);
            this.namedjdbcTemplate.update(this.buildRegisterLoginRetries(), sourceParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }
    
    private String buildRegisterLoginRetries() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE USERS SET RetriesNumber = :RetriesNumber WHERE IdUser = :IdUser");
        return query.toString();
    }

    private String buildIsProfileUserFilteredQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT CASE WHEN COUNT(PROFILESCREENFLOW.IsUserFiltered) > 0 THEN 1 ELSE 0 END ");
        query.append("FROM PROFILESCREENFLOW ");
        query.append("INNER JOIN PROFILEUSER ON PROFILESCREENFLOW.IdProfile = PROFILEUSER.IdProfile ");
        query.append("INNER JOIN SCREEN ON SCREEN.FactoryName = PROFILESCREENFLOW.FactoryName ");
        query.append("WHERE PROFILEUSER.IdUser = :IdUser ");
        query.append("AND PROFILESCREENFLOW.IsUserFiltered = 0 ");
        query.append("AND PROFILESCREENFLOW.IdFlow = :IdFlow ");
        query.append("AND SCREEN.FlowStatus = :Status ");
        return query.toString();
    }

    private String buildFindUserByAreaQuery() {
        return  this.buildSelectUserFields().append(" FROM USERS WHERE IdArea = :IdArea AND Status = 'ACTIVE' ").
        		toString();
    }

    private String buildFindProfilesByIdUserQuery() {
        final StringBuilder querySelect = new StringBuilder();
        querySelect.append("SELECT PROFILE.IdProfile, Name, Status FROM PROFILEUSER INNER JOIN PROFILE ON");
        querySelect.append(" PROFILE.Idprofile = PROFILEUSER.IdProfile WHERE PROFILEUSER.IdUser = :IdUser ");
        querySelect.append("AND PROFILE.Status = 'ACTIVE'");
        return querySelect.toString();
    }

    private String buildDeleteProfilesListByIdUser() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM PROFILEUSER WHERE IdUser = :IdUser");
        return query.toString();
    }

    private String buildSaveProfileByIdUser() {
        final StringBuilder queryInsert = new StringBuilder();
        queryInsert.append("INSERT INTO PROFILEUSER(IdUser,Idprofile) VALUES(:IdUser,:IdProfile)");
        return queryInsert.toString();
    }

    private MapSqlParameterSource createSaveProfileByIdUserNamedParameters(
            final Integer idUser, final Integer idProfile) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(ID_USER, idUser);
        namedParameters.addValue("IdProfile", idProfile);
        return namedParameters;
    }

    public void deleteById(final Integer idUser) {
        final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idUser);
        this.namedjdbcTemplate.update(this.buildDeleteByIdQuery(), namedParameters);
    }

    public Integer countAll() {
        return this.namedjdbcTemplate.queryForObject(this.buildCountAllQuery(),
                new MapSqlParameterSource(), Integer.class);
    }

    public Integer countByStatus(final UserStatusEnum status) {
        final MapSqlParameterSource namedParameters = this.createFindByStatusNamedParameters(status);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByStatusQuery(),
                namedParameters, Integer.class);
    }

    public Integer countByName(final String nameParam,final String apaternoParam,final String amaternoParam) {
        final MapSqlParameterSource namedParameters = this.createFindUsersByNameParameters(nameParam,apaternoParam,amaternoParam);
        return this.namedjdbcTemplate.queryForObject(this.buildCountByNameQuery(),
                namedParameters, Integer.class);
    }

    private String buildCountAllQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountQuery(query);
        return query.toString();
    }

    private String buildCountByStatusQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCountQuery(query);
        this.buildWhereStatusQuery(query);
        return query.toString();
    }

    private String buildCountByNameQuery() {
        final StringBuilder query = new StringBuilder();
        this.buildSelectCounByNameQuery(query);
        query.append(this.buildWhereNameAndLastNameQueryForInnerJoin());
        //this.buildWhereNameQuery(query);
        return query.toString();
    }

    private void buildSelectCountQuery(final StringBuilder query) {
        query.append("SELECT COUNT(IdUser) FROM USERS ");
    }

    private void buildSelectCounByNameQuery(final StringBuilder query) {
        query.append("SELECT COUNT(name) FROM USERS U ");
    }

    private String buildDeleteByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM USERS WHERE IdUser = :IdUser");
        return query.toString();
    }

    private StringBuilder buildFindAllQuery() {
        final StringBuilder querySelect = new StringBuilder();
        querySelect.append(this.buildSelectUserFields());
        querySelect.append(" FROM USERS ");
        return querySelect;
    }


    private String buildCountCorrespondPasswordByUsernameQuery() {
        final StringBuilder querySelect = new StringBuilder();
        querySelect.append("SELECT COUNT(IdUser) FROM USERS WHERE Username = :Username AND Password = :Password");
        return querySelect.toString();
    }

    private MapSqlParameterSource createCountCorrespondPasswordByUsernameNamedParameters(
            final String username, final String password) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("Username", username);
        namedParameters.addValue("Password", password);
        return namedParameters;
    }

    private String buildUpdateUserQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE USERS SET IdPosition = :IdPosition, IdUnderdirector = :IdUnderdirector, ");
        query.append("IdArea = :IdArea, IdDga = :IdDga, Username = :Username, FirstLastName = :FirstLastName, ");
        query.append("SecondLastName = :SecondLastName, PhoneNumber = :PhoneNumber, ");
        query.append("Name = :Name, Email = :Email, Territory = :Territory , IsLawyer = :IsLawyer, IsEvaluator = ");
        query.append(":IsEvaluator, IsDecider = :IsDecider ");
        query.append(WHERE_ID_USER_EQUALS_ID_USER);
        return query.toString();
    }

    private String buildUpdatePasswordQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE USERS SET Password = :Password, Salt = :Salt ");
        query.append(WHERE_ID_USER_EQUALS_ID_USER);
        return query.toString();
    }

    private String buildInsertUserQuery() {
        final StringBuilder queryInsert = new StringBuilder();
        queryInsert.append("INSERT INTO USERS (IdPosition, IdUnderdirector, IdArea, IdDga, Username, Password, ");
        queryInsert.append("Salt, Name, FirstLastName, SecondLastName, PhoneNumber, Email, Territory, IsLawyer, ");
        queryInsert.append("IsEvaluator, IsDecider) VALUES(:IdPosition, :IdUnderdirector, :IdArea, :IdDga, :Username,");
        queryInsert.append(" :Password, :Salt, :Name, :FirstLastName, :SecondLastName, :PhoneNumber, :Email,");
        queryInsert.append(" :Territory, :IsLawyer, :IsEvaluator, :IsDecider) ");
        return queryInsert.toString();
    }

    private MapSqlParameterSource createFindByUserIdNamedParameters(final Integer idUser) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstantsSecurity.ID_USER, idUser);
        return namedParameters;
    }

    private MapSqlParameterSource createFindUsersByNameParameters(final String name,final String apaterno,final String amaterno) {
    	LOG.info("USERSDAO::: El nombre del usuario es ::::: " + name);
    	LOG.info("USERSDAO ::: El nombre del usuario es ::::: " + apaterno);
    	LOG.info("USERSDAO ::: El nombre del usuario es ::::: " + amaterno);
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstantsSecurity.NAME + NULL, name);
        namedParameters.addValue(TableConstantsSecurity.NAME, QueryConstants.ANY_CHARACTER + name + 
                QueryConstants.ANY_CHARACTER);
        namedParameters.addValue(TableConstantsSecurity.FIRST_LAST_NAME + NULL, apaterno);
        namedParameters.addValue(TableConstantsSecurity.FIRST_LAST_NAME, QueryConstants.ANY_CHARACTER + apaterno + 
                QueryConstants.ANY_CHARACTER);
        namedParameters.addValue(TableConstantsSecurity.SECOND_LAST_NAME + NULL, amaterno);
        namedParameters.addValue(TableConstantsSecurity.SECOND_LAST_NAME, QueryConstants.ANY_CHARACTER + amaterno + 
                QueryConstants.ANY_CHARACTER);
        return namedParameters;
    }

    private MapSqlParameterSource createFindUsersByNameOrLastNameParameters(final String name,
            final Integer idRequisition, final Integer idFlow) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstants.ID_FLOW, idFlow);
        namedParameters.addValue(TableConstants.ID_REQUISITION, idRequisition);
        namedParameters.addValue(TableConstantsSecurity.NAME, QueryConstants.ANY_CHARACTER + name + 
                QueryConstants.ANY_CHARACTER);	
        return namedParameters;
    }

    private String buildFindUsersByName() {
        final StringBuilder query = this.buildSelectAllInnerJoinUserAndAreaAndDga();
        query.append(this.buildWhereNameAndLastNameQueryForInnerJoin());
        query.append(ORDER_BY_NAME);
        return query.toString();
    }
    
    private String buildFindLawyersByIdUser() {
        final StringBuilder query = this.buildSelectAllFromUsersWhereIdUser();
        query.append(ORDER_BY_NAME);
        return query.toString();
    }
    private String buildFindUserMailAddress() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT top 1  u.Email from REQUISITION r inner join USERS u on u.IdUser=r.IdApplicant where r.IdRequisition = :IdRequisition");
        return query.toString();
    }
    private String buildFindLawyerName() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT CONCAT(Name, ' ', FirstLastName, ' ', SecondLastName) AS fullName ");
        query.append("FROM USERS u ");
        query.append("JOIN REQUISITION r ON r.IdLawyer = u.IdUser ");
        query.append("WHERE r.IdRequisition = :IdRequisition");
        return query.toString();
    }
    private String buildSaveChangesLawyer() {
        final StringBuilder query = this.buildSaveChangesLawyerQery();
        return query.toString();
    }


    private String buildFindUsersByNameOrLastName() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT DISTINCT USERS.IdUser, ");
        this.buildUserFullNameConcat(query);
        query.append(" AS Name, POSITION.Name AS PositionName, AREA.Name AS AreaName, ");
        query.append("DGA.Name AS DgaName, USERS.IsLawyer, USERS.IsEvaluator, USERS.IsDecider ");
        query.append("FROM USERS LEFT JOIN REQUISITIONUSERSVOBO ON USERS.IdUser  = REQUISITIONUSERSVOBO.IdUser AND ");
        query.append("REQUISITIONUSERSVOBO.IdRequisition = :IdRequisition INNER JOIN ");
        query.append("PROFILEUSER ON USERS.IdUser = PROFILEUSER.IdUser INNER JOIN ");
        query.append("PROFILESCREENFLOW ON PROFILEUSER.IdProfile = PROFILESCREENFLOW.IdProfile INNER JOIN ");
        query.append("SCREEN ON PROFILESCREENFLOW.FactoryName = SCREEN.FactoryName LEFT JOIN ");
        query.append("POSITION ON USERS.IdPosition = POSITION.IdPosition LEFT JOIN ");
        query.append("AREA ON USERS.IdArea = AREA.IdArea LEFT JOIN ");
        query.append("DGA ON USERS.IdDga = DGA.IdDga ");
        query.append("WHERE REQUISITIONUSERSVOBO.IdRequisition IS NULL AND PROFILESCREENFLOW.IdFlow = :IdFlow AND ");
        query.append("USERS.Status = 'ACTIVE' AND UPPER(");
        this.buildUserFullNameConcat(query);
        query.append(") LIKE UPPER(:Name)");
        return query.toString();
    }

    private void buildUserFullNameConcat(final StringBuilder query) {
        query.append(this.databaseUtils.buildConcat(
                "USERS.Name", SPACE_CHAR, "FirstLastName", SPACE_CHAR, "SecondLastName"));
    }

    private StringBuilder buildSelectAllInnerJoinUserAndAreaAndDga() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT U.IdUser, U.IdUnderdirector, POSITION.IdPosition, A.IdArea, A.Name areaName, D.IdDga, ");
        query.append("D.Name dgaName, U.Username, U.Name, U.FirstLastName, U.SecondLastName, ");
        query.append("U.PhoneNumber, U.Email, U.Territory, U.Status, POSITION.Name PositionName, ");
        query.append("U.IsLawyer, U.IsEvaluator, U.IsDecider ");
        query.append("FROM USERS U ");
        query.append("LEFT JOIN AREA A ON U.idArea = A.idArea AND A.Status = 'ACTIVE'");
        query.append("LEFT JOIN DGA D ON U.idDga = D.idDga AND D.Status = 'ACTIVE' ");
        query.append("LEFT JOIN POSITION ON U.IdPosition = POSITION.IdPosition AND POSITION.Status = 'ACTIVE' ");
        return query;
    }

    private StringBuilder buildSelectAllFromUsersWhereIdUser() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT * from USERS u WHERE u.Status='ACTIVE' AND u.IdPosition =2");
        return query;
    }
    private StringBuilder buildSaveChangesLawyerQery() {
        final StringBuilder query = new StringBuilder();
        query.append("UPDATE REQUISITION SET IdLawyer = :IdUser WHERE IdRequisition = :idRequisition");
        return query;
    }
    

    private StringBuilder buildWhereNameAndLastNameQueryForInnerJoin() {
        final StringBuilder query = new StringBuilder();
        query.append(" WHERE 1=1 ");
        query.append("AND (:NameNull IS NULL OR CONCAT (U.Name, ' ' , U.FirstLastName, ' ', U.SecondLastName) LIKE :Name) ");
//        query.append(" WHERE (:NameNull IS NULL) OR (U.Name LIKE :Name) ");
//        query.append("OR (U.FirstLastName LIKE :Name) ");
//        query.append("OR (U.SecondLastName LIKE :Name) ");
//        query.append("OR (U.SecondLastName LIKE :FirstLastName) ");
//        query.append("OR (U.FirstLastName LIKE :FirstLastName) ");
//        query.append("OR (U.SecondLastName LIKE :SecondLastName) ");
//        query.append("OR (U.FirstLastName LIKE :SecondLastName) ");
        LOG.info("buildWhereNameAndLastNameQueryForInnerJoin ::: LA CONSULTA COMPLETA ES :::::"+query);
        return query;
    }
    
    private StringBuilder buildWhereStatusActiveAndIdPosition2() {
        final StringBuilder query = new StringBuilder();
        query.append(" WHERE USERS.IdUser = :IdUser AND U.Status='ACTIVE' AND U.IdPosition =2 ; ");
        LOG.info("buildWhereStatusActiveAndIdPosition2 ::: LA CONSULTA COMPLETA ES :::::" + query);
        return query;
    }

    
    private String buildFindUserByIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT USERS.IdUser, USERS.IdUnderdirector, USERS.IdPosition, USERS.IdArea, USERS.IdDga, ");
        query.append("USERS.Name, USERS.FirstLastName, USERS.SecondLastName, USERS.Username, ");
        query.append("USERS.IsLawyer, USERS.IsEvaluator, USERS.IsDecider, ");
        query.append(this.databaseUtils.buildConcat(
                "UnderDirector.Name", SPACE_CHAR, "UnderDirector.FirstLastName",
                SPACE_CHAR, "UnderDirector.SecondLastName"));
        query.append(" AS UnderDirectorName, USERS.PhoneNumber, USERS.Email, USERS.Territory, USERS.Status, "); 
        query.append("AREA.Name AS AreaName, DGA.Name AS DgaName, POSITION.Name As PositionName, ");
        query.append("UnderDirector.PhoneNumber AS UnderDirectorPhoneNumber ");
        query.append("FROM USERS LEFT JOIN USERS UnderDirector ON USERS.IdUnderDirector = UnderDirector.IdUser ");
        query.append("LEFT JOIN AREA ON USERS.idArea = AREA.idArea ");
        query.append("LEFT JOIN DGA ON USERS.idDga = DGA.idDga ");
        query.append("LEFT JOIN POSITION ON USERS.IdPosition = POSITION.IdPosition ");
        query.append("WHERE USERS.IdUser = :IdUser");
        return query.toString();
    }

    private StringBuilder buildSelectByStatusQuery() {
        final StringBuilder query = this.buildFindAllQuery();
        this.buildWhereStatusQuery(query);
        return query;
    }

    private void buildWhereStatusQuery(final StringBuilder query) {
        query.append("WHERE Status = :Status");
    }

    private MapSqlParameterSource createChangeUserStatusNamedParameters(final Integer idUser,
            final UserStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstantsSecurity.STATUS, status.toString());
        namedParameters.addValue(TableConstantsSecurity.ID_USER, idUser);
        return namedParameters;
    }

    private String buildChangeUserStatusQuery() {
        final StringBuilder queryUpdate = new StringBuilder();
        queryUpdate.append("UPDATE USERS SET Status = :Status WHERE IdUser = :IdUser");
        return queryUpdate.toString();
    }

    private MapSqlParameterSource createFindByStatusNamedParameters(final UserStatusEnum status) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue(TableConstantsSecurity.STATUS, status.toString());
        return namedParameters;
    }

    @Override
    public User findUserByEmail(final String email) throws DatabaseException, EmptyResultException {
        try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("Email", email);
            return this.namedjdbcTemplate.queryForObject(this.buildSelectUserQueryForRecoveringPassword(),
                    namedParameters, new BeanPropertyRowMapper<User>(User.class));
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al buscar el email en la base de datos", dataAccessException);
        }
    }

	@Override
	public String findUrlLdap() throws DatabaseException, EmptyResultException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            final StringBuilder query = new StringBuilder();
            query.append("SELECT value FROM CONFIGURATION WHERE Name = 'URL_LDAP' ");
            return this.namedjdbcTemplate.queryForObject(query.toString(), namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al consultar la url LDAP LDAP para la aplicación", dataAccessException);
		}
	}

	@Override
	public String findBaseLdap() throws DatabaseException, EmptyResultException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            final StringBuilder query = new StringBuilder();
            query.append("SELECT value FROM CONFIGURATION WHERE Name = 'BASE_LDAP' ");
            return this.namedjdbcTemplate.queryForObject(query.toString(), namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al consultar la base LDAP LDAP para la aplicación", dataAccessException);
		}
	}

	@Override
	public String findUserLdap() throws DatabaseException, EmptyResultException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            final StringBuilder query = new StringBuilder();
            query.append("SELECT value FROM CONFIGURATION WHERE Name = 'USER_LDAP' ");
            return this.namedjdbcTemplate.queryForObject(query.toString(), namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al consultar el usuario LDAP para la aplicación", dataAccessException);
		}
	}

	@Override
	public String findPasswordLdap() throws DatabaseException, EmptyResultException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            final StringBuilder query = new StringBuilder();
            query.append("SELECT value FROM CONFIGURATION WHERE Name = 'PASSWORD_LDAP' ");
            return this.namedjdbcTemplate.queryForObject(query.toString(), namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al consultar el password LDAP para la aplicación", dataAccessException);
		}
	}
    
	@Override
	public String findLoginType() throws DatabaseException {
		try {
            final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            return this.namedjdbcTemplate.queryForObject(this.buildfindLoginTypeQuery(),
                    namedParameters, String.class);
		} catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al buscar el tipo de login para la aplicación", dataAccessException);
		}
	}
    
    private String buildfindLoginTypeQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT Value FROM CONFIGURATION WHERE Name = 'LOGIN_TYPE' ");
        return query.toString();
    }
	
    private String buildSelectUserQueryForRecoveringPassword() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdUser, Username, Password, Salt FROM USERS WHERE Email = :Email");
        return query.toString();
    }

	@Override
	public void registerLoginStatus(final Integer idUser, final LoginEnum statusLogin) throws DatabaseException {
	}
	
	@Override
	public Boolean isBlockedUser(final String username) throws DatabaseException {
	    final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
	    namedParameters.addValue(TableConstantsSecurity.USERNAME, username);
	    return this.namedjdbcTemplate.queryForObject(this.buildIsBlockedUserQuery(), namedParameters, Boolean.class);
	}

    private String buildIsBlockedUserQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT COUNT(1) FROM USERS WHERE Username = :Username AND Status = 'BLOCKED' ");
        return query.toString();
    }

    @Override
    public List<User> findAllUsersCatalogPaged(final String name,final String apaterno,final String amaterno, final Integer pagesNumber,
            final Integer itemsNumber) throws DatabaseException {
    	LOG.info("findAllUsersCatalogPaged ::: El nombre del usuario es ::::: " + name);
    	LOG.info("findAllUsersCatalogPaged ::: El nombre del usuario es ::::: " + apaterno);
    	LOG.info("findAllUsersCatalogPaged ::: El nombre del usuario es ::::: " + amaterno);
        try {
            final String paginatedQuery = this.databaseUtils.buildPaginatedQuery(TableConstants.ID_USER, 
                    this.buildFindUsersByName(), pagesNumber, itemsNumber);
            return this.namedjdbcTemplate.query(paginatedQuery, this.createFindUsersByNameParameters(name,apaterno,amaterno), 
                    new BeanPropertyRowMapper<User>(User.class));
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

    @Override
    public Long countTotalItemsToShowOfUser(final String name,final String apaterno,final String amaterno) throws DatabaseException {
    	LOG.info("countTotalItemsToShowOfUser ::: El nombre del usuario es ::::: " + name);
//    	LOG.info("findAllUsersCatalogPaged ::: El nombre del usuario es ::::: " + name);
        try {
            final String countItems = this.databaseUtils.countTotalRows(this.buildFindUsersByName());
            return this.namedjdbcTemplate.queryForObject(countItems, 
                    this.createFindUsersByNameParameters(name,apaterno,amaterno), Long.class);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
    }

	@Override
	public List<Integer> findAreasRepoByUserId(Integer idUser) throws DatabaseException, EmptyResultException {
        try {
        	System.out.println("DAO REPORTES -> idUser: "+ idUser);
            final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idUser);
            return this.namedjdbcTemplate.queryForList(this.buildFindAreaByUserIdQuery(), namedParameters,
                    Integer.class);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            throw new EmptyResultException(emptyResultDataAccessException);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException(dataAccessException);
        }
	}
	
    private String buildFindAreaByUserIdQuery() {
        final StringBuilder query = new StringBuilder();
        query.append("SELECT IdArea ");
        query.append("FROM AREASGERENTES ");
        query.append("WHERE IdUser = :IdUser");
        return query.toString();
    }

	@Override
	public void saveAreasRepoByIdUser(Integer idUser, Integer idArea) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createSaveAreasByIdUserNamedParameters(idArea, idUser);
            this.namedjdbcTemplate.update(this.buildSaveAreaRepoByIdUser(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al guardar el areas para el reporte del usuario", dataAccessException);
        }
	}
	
    private MapSqlParameterSource createSaveAreasByIdUserNamedParameters(
            final Integer idArea, final Integer idUser) {
        final MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("IdArea", idArea);
        namedParameters.addValue(ID_USER, idUser);
        return namedParameters;
    }
    
    private String buildSaveAreaRepoByIdUser() {
        final StringBuilder queryInsert = new StringBuilder();
        queryInsert.append("INSERT INTO AREASGERENTES (IdArea, IdUser) VALUES (:IdArea, :IdUser)");
        return queryInsert.toString();
    }

	@Override
	public void deleteAreasListByIdUser(Integer idUser) throws DatabaseException {
        try {
            final MapSqlParameterSource namedParameters = this.createFindByUserIdNamedParameters(idUser);
            this.namedjdbcTemplate.update(this.buildDeleteAreasListByIdUser(), namedParameters);
        } catch (DataAccessException dataAccessException) {
            throw new DatabaseException("Error al borrar los areas para reporte por usuario", dataAccessException);
        }
	}
	
    private String buildDeleteAreasListByIdUser() {
        final StringBuilder query = new StringBuilder();
        query.append("DELETE FROM AREASGERENTES WHERE IdUser = :IdUser");
        return query.toString();
    }
}