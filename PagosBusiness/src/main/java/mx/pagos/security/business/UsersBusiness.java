package mx.pagos.security.business;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.security.UserStatusEnum;
import mx.pagos.admc.exceptions.back.DESEncryptionException;
import mx.pagos.admc.util.back.Base64Coder;
import mx.pagos.admc.util.back.DESEncryption;
import mx.pagos.admc.util.back.SecurityService;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.exceptions.BlockedUserException;
import mx.pagos.security.exceptions.InvalidCredentialsException;
import mx.pagos.security.interfaces.Userable;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.shared.PasswordGenerator;

/**
 * Clase que maneja la información de los usuarios, así como la validación de las credenciales al momento de loguearse
 * para ingresar al sistema.
 *
 * @see Userable
 * @see UserSession
 * @see User
 * @see UserStatusEnum
 */
@Service("UsersBusiness")
public class UsersBusiness extends AbstractExportable {
	private static final String MESSAGE_BLOCLED_USER_ERROR =
			"Usuario bloquedo por intentos fallidos. Comuniquese con "
					+ "el administrador de la aplicación para desbloquear su usuario";
	private static final Integer ZERO_RETRIES_WHEN_LOGIN = 0;
	private static final String ERROR_TO_VALIDATE_PASSWORD_MESSAGE = "Hubo un error al intentar validar la contraseña";
	private static final String INVALID_CREDENTIALS_MESSAGE = "Credenciales del usuario inválidas";
	private static final String ERROR_LOGIN_AD = "Usuario no registrado, favor de comunicarse con el administrador.";
	private static final String ERROR_USUARIOS = "Error al obtener los usuarios";
	private static final String ERROR_VALIDATING_CREDENTIALS = "Hubo un error al validar las credenciales del usuario";
	private static final String ERROR_FIND_USERS_BY_AREA_MESSAGE = "Hubo un error al obtener usuarios por área";
	private static final String ERROR_IS_PROFILE_USER_FILTERED = 
			"Hubo un problema al validar si el estatus de la pantalla está filtrado";
	private static final String ERROR_FIND_LOGIN_TYPE = 
			"Hubo un problema al buscar el tipo de login para la aplicación";
	private static final String MESSAGE_EXPORTING_USERS_ERROR =
			"Hubo un problema al exportar el catálogo Usuarios";
	private static final String MESSAGE_FIND_ALL_USERS_CATALOG_PAGED_ERROR = 
			"Hubo un problema al buscar usuarios paginados";
	private static final String MESSAGE_FIND_TOTAL_PAGES_USERS_ERROR = 
			"Hubo un problema al buscar número de pagínas de usuarios";
	private static final Logger LOG = Logger.getLogger(UsersBusiness.class);

	@Autowired
	private Userable userable;

	@Autowired
	private Configurable configurable;

	@Autowired
	private ConfigurationsBusiness configuration;

	//    @Autowired
	//    private Ldapable ldapable;

	public String findLoginType () throws BusinessException {
		try {
			return this.userable.findLoginType();
		} catch (DatabaseException databaseException) {
			throw new BusinessException(ERROR_FIND_LOGIN_TYPE, databaseException);
		}
	}

	public User validateUserCredentials(final String username, final String password) throws BusinessException,
	BlockedUserException {
		User user = new User();
		try {
			final String userNameEncripted = this.encryptUsername(username);
			this.validateIsNoBlockedUser(userNameEncripted);
			user = this.findByUsernameForLogin(userNameEncripted);
			this.validateCredentials(userNameEncripted, this.signOn(password, user.getSalt(), user.getStatus()));
			this.userable.registerRetry(user.getIdUser(), ZERO_RETRIES_WHEN_LOGIN);
			LOG.info("Inicio de sesión del usuario: " + user.getFullName() + ", con Id: " + user.getIdUser());
			return user;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error(ERROR_VALIDATING_CREDENTIALS + ":" + username, databaseException);
			throw new BusinessException(ERROR_VALIDATING_CREDENTIALS, databaseException);
		} catch (InvalidCredentialsException | EmptyResultException invalidCredentialsException) {
			this.registerInvalidAttempt(user, invalidCredentialsException);
			throw new BusinessException(INVALID_CREDENTIALS_MESSAGE, invalidCredentialsException);
		}
	}
	
	
	
	public List<User> findGerente(final int idUser) throws BusinessException,
	BlockedUserException {
		try {

			return this.userable.findGerente(idUser);
        } catch (DatabaseException databaseException) {
			LOG.error(ERROR_VALIDATING_CREDENTIALS + ":" +"idUser"+ idUser, databaseException);
			throw new BusinessException(ERROR_VALIDATING_CREDENTIALS, databaseException);
        }
	}

	private void validateIsNoBlockedUser(final String userNameEncripted)
			throws BlockedUserException, DatabaseException {
		if (this.userable.isBlockedUser(userNameEncripted))
			throw new BlockedUserException(MESSAGE_BLOCLED_USER_ERROR);
	}

	public void logCloseSession(final UserSession userSessionParameter) {
		if (null == userSessionParameter.getUsuario()) {
			LOG.info("Cierre de sesión del usuario: ");
		} else if (null == userSessionParameter.getUsuario().getIdUser()) {
			LOG.info("Cierre de sesión del usuario: ");
		} else {
			LOG.info("Cierre de sesión del usuario: " + userSessionParameter.getUsuario().getFullName()
					+ ", con el Id: " + userSessionParameter.getUsuario().getIdUser());
		}
	}

	public User validateUserCredentialsAD(final String username) throws BusinessException, BlockedUserException {
		User user = new User();
		try {
			final String[] splitedUsername = username.split("\\\\");
			final String userNameEncripted = this.encryptUsername(splitedUsername[splitedUsername.length - 1]);
			user = this.findByUsername(userNameEncripted);
			return user;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error("Hubo un error al validar las credenciales del usuario:" + username, databaseException);
			throw new BusinessException("Error al validar las credenciales del usuario", databaseException);
		} catch (EmptyResultException invalidCredentialsException) {
			this.registerInvalidAttempt(user, invalidCredentialsException);
			throw new BusinessException(ERROR_LOGIN_AD, invalidCredentialsException);
		}
	}

	private void registerInvalidAttempt(final User user, final Exception invalidCredentialsException)
			throws BusinessException, BlockedUserException {
		LOG.error(invalidCredentialsException);
		this.registerAttemptWhenInvalidCredentials(invalidCredentialsException, user);
	}

	private String encryptUsername(final String username) throws DESEncryptionException {
		final DESEncryption encryptor = new DESEncryption();
		return encryptor.encrypt(username);
	}

	private void validateCredentials(final String username, final String password) throws DatabaseException,
	InvalidCredentialsException {
		if (this.isInvalidCredentials(username, password))
			throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
	}
	private void validateGerenCredentials(final String username, final String password) throws DatabaseException,
	InvalidCredentialsException {
		if (this.isInvalidCredentials(username, password))
			throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
	}

	private Boolean isInvalidCredentials(final String username, final String password) throws DatabaseException {
		return this.userable.getCountCorrespondPasswordByUsername(username, password) != NumbersEnum.ONE.getNumber();
	}

	public User isEmailRegistered(final String emailForRecovering) throws BusinessException {
		try {
			return this.userable.findUserByEmail(emailForRecovering);
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al verificar la existencia del email: " + emailForRecovering, databaseException);
			throw new BusinessException("Error al verificar la existencia del email", databaseException);
		}  catch (EmptyResultException emptyResultException) {
			throw new BusinessException("El email ingresado no existe en el sistema", emptyResultException);
		}
	}

	public User findByUsername(final String username) throws DatabaseException, EmptyResultException {
		return this.userable.findByUsername(username);
	}

	public Boolean usernameExists(final String username) throws BusinessException {
		try {
			final String userNameEncripted = this.encryptUsername(username);
			this.userable.findByUsername(userNameEncripted);
			return true;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error("Hubo un error al verificar la existencia del usuario: " + username, databaseException);
			throw new BusinessException("Error al verificar la existencia del email", databaseException);
		}  catch (EmptyResultException emptyResultException) {
			return false;
		}
	}

	public User findByUsernameForLogin(final String username)
			throws DatabaseException, EmptyResultException, DESEncryptionException {
		return this.userable.findByUsernameForLogin(username);
	}

	public String generatePasswordForRecovering(final User user) throws BusinessException {
		try {
			final String newPassword = PasswordGenerator.getPassword();
			user.setPassword(newPassword);
			this.generatePass(user);
			this.userable.updatePassword(user);
			return newPassword;
		} catch (DatabaseException databaseException) {
			LOG.error("Error al cambiar la contraseña del usuario " + user.getUsername() + " por recuperación",
					databaseException);
			throw new BusinessException("Error al cambiar la contraseña recuperada", databaseException);
		}
	}

	private void encryptUser(final User user) throws DESEncryptionException {
		final DESEncryption encryptor = new DESEncryption();
		user.setUsername(encryptor.encrypt(user.getUsername()));
	}

	private void updatePassword(final User user) throws DatabaseException {
		if (!user.getPassword().equals(Constants.PASSWORD)) {
			this.generatePass(user);
			this.userable.updatePassword(user);
		}
	}

	private void generatePass(final User user) {
		user.setSalt(SecurityService.generateSalt());
		user.setPassword(SecurityService.encrypt(user.getPassword(), Base64Coder.encodeString(user.getSalt())));
	}

	public Integer saveOrUpdate(final User user) throws BusinessException {
		try {
			this.encryptUser(user);
			if (!user.getIsActiveDirectoryUser()) {
				if (user.getIdUser() == null)
					this.generatePass(user);
				else
					this.updatePassword(user);
			}
			final Integer idUser = this.userable.saveOrUpdate(user);
			this.saveProfilesByIdUser(idUser, user.getProfileList());
			
			/* Guarda lista de areas */
			if (user.getAreasReporte() != null) {
				this.saveAreasByIdUser(idUser, user.getAreasReporte());
				LOG.info("AREAS ID: " + user.getAreasReporte().toString());				
			}
			
			return idUser;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error("Hubo un error al guardar el usuario", databaseException);
			throw new BusinessException("Error al guardar el usuario", databaseException);
		}
	}

	public List<User> findAll() throws BusinessException {
		try {
			final List<User> users = this.userable.findAll();
			final DESEncryption encryptor = new DESEncryption();
			for (User tmp:users)
				tmp.setUsername(encryptor.decrypt(tmp.getUsername()));
			return users;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error("Hubo un error al obtener los usuarios", databaseException);
			throw new BusinessException(ERROR_USUARIOS, databaseException);
		}
	}

	public List<User> findByStatus(final UserStatusEnum status) throws BusinessException {
		try {
			return this.userable.findByStatus(status);
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al obtener los usuarios por estatus", databaseException);
			throw new BusinessException(ERROR_USUARIOS, databaseException);
		}
	}

	public void changeUserStatus(final Integer idUser, final UserStatusEnum status)
			throws BusinessException {
		try {
			this.userable.changeUserStatus(idUser, status);
			this.userable.registerRetry(idUser, ZERO_RETRIES_WHEN_LOGIN);
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al cambiar el estatus del usuario: " + idUser.toString(), databaseException);
			throw new BusinessException("Error al cambiar el estatus del usuario", databaseException);
		}
	}

	public User findByUserId(final Integer idUser) throws BusinessException {
		try {
			LOG.info("findByUserId -> idUser: " + idUser);
			final DESEncryption encryptor = new DESEncryption();
			final User user = this.userable.findByUserId(idUser);
			user.setUsername(encryptor.decrypt(user.getUsername()));			
			user.setAreasReporte(this.userable.findAreasRepoByUserId(idUser));			
			LOG.info("USUARIO :: Nombre(" + user.getFullName()+") - Usuario ("+user.getUsername()+") ID ("+idUser+")");			
			LOG.info("TOTAL DE REGISTROS PARA REPORTE: " + user.getAreasReporte().size());
			if(!user.getAreasReporte().isEmpty())				
				LOG.info("ÁREAS ASIGNADAS PARA REPORTE :: " + user.getAreasReporte().toString());							
			return user;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error("Hubo un error al obtener los usuarios por estatus por id", databaseException);
			throw new BusinessException("Error al obtener el usuario", databaseException);
		} catch (EmptyResultException emptyResultException) {
			throw new BusinessException("El usuario ha dejado de existir", emptyResultException);
		} catch (NullPointerException nullException) {
			throw new BusinessException("El id del usuario es nulo", nullException);
		}
	}

	private String signOn(final String pass, final String salt, final UserStatusEnum status) throws BusinessException {
		LOG.info("Login:Validando Datos Password");
		if (status == UserStatusEnum.ACTIVE)
			return SecurityService.encrypt(pass, Base64Coder.encodeString(salt));
		throw new BusinessException(INVALID_CREDENTIALS_MESSAGE);
	}

	private void registerAttemptWhenInvalidCredentials(final Exception exception, final User user)
			throws BusinessException, BlockedUserException {
		if (exception instanceof InvalidCredentialsException)
			this.registerFailedAccessAttempt(user);
	}

	private void registerFailedAccessAttempt(final User user) throws BusinessException, BlockedUserException {
		try {
			user.setRetriesNumber(user.getRetriesNumber() + 1);
			LOG.info("Registrando acceso fallido para el usuario con id: " + user.getIdUser() + ". Intento número: "
					+ user.getRetriesNumber());
			final Integer maxAllowedAttempts = Integer.parseInt(this.configurable.findByName("LOGIN_RETRIES"));
			this.userable.registerRetry(user.getIdUser(), user.getRetriesNumber());
			if (user.getRetriesNumber() >= maxAllowedAttempts) {
				this.userable.changeUserStatus(user.getIdUser(),  UserStatusEnum.BLOCKED);
				throw new BlockedUserException(MESSAGE_BLOCLED_USER_ERROR);
			}
		} catch (DatabaseException databaseException) {
			throw new BusinessException("Hubo un problema al registrar el reintento de login", databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveProfilesByIdUser(final Integer idUser,
			final List<Profile> profilesList) throws BusinessException {
		try {
			this.userable.deleteProfilesListByIdUser(idUser);
			for (Profile profile : profilesList)
				this.userable.saveProfileByIdUser(idUser, profile.getIdProfile());  
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al guardar el perfil del usuario usuario:" + idUser.toString(), databaseException);
			throw new BusinessException("Error al guardar el perfil por usuario", databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveAreasByIdUser(final Integer idUser, final List<Integer> areasList) throws BusinessException {
		try {
			this.userable.deleteAreasListByIdUser(idUser);
			for (Integer area : areasList)
				this.userable.saveAreasRepoByIdUser(idUser, area);  
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al guardar el perfil del usuario usuario:" + idUser.toString(), databaseException);
			throw new BusinessException("Error al guardar el perfil por usuario", databaseException);
		}
	}
	
	public List<Profile> findProfilesByidUser(final Integer idUser) throws BusinessException {
		try {
			LOG.info("findProfilesByidUser -> consulta de usuario -> idUser: " + idUser);
			return this.userable.findProfilesByIdUser(idUser);
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al recuperar los perfiles del usuario:" + idUser.toString(), databaseException);
			throw new BusinessException("Error al recuperar los perfiles por usuario", databaseException);
		}
	}

	public List<User> findUsersByName(final String name,final String apaterno,final String amaterno) throws BusinessException {
		try {
			final List<User> users = this.userable.findUsersByName(name,apaterno,amaterno);
			final DESEncryption encryptor = new DESEncryption();
			for (User user : users)
				user.setUsername(encryptor.decrypt(user.getUsername()));
			return users;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error("Hubo un error al recuperar los usuarios por nombre: " + name, databaseException);
			throw new BusinessException("Error al recuperar los usuarios por nombre", databaseException);
		}
	}
	public List<User> findLawyersByIdUser() throws BusinessException {
		try {
			final List<User> abogados = this.userable.findLawyersByIdUser();
			return abogados;
		} catch (DatabaseException databaseException) {
			throw new BusinessException("Error al recuperar los abogados por idUser", databaseException);
		}
	}
	public List<User> findUserMailAddress(String req) throws BusinessException {
		try {
			final List<User> abogados = this.userable.findUserMailAddress(req);
			return abogados;
		} catch (DatabaseException databaseException) {
			throw new BusinessException("Error al recuperar el correo del usuario. UsersBussines - findUserMailAddress", databaseException);
		}
	}
	public String findLawyerNameByIdRequisition(String req) throws BusinessException {
		try {
			final String lawyer = this.userable.findLawyerNameByIdRequisition(req);
			return lawyer;
		} catch (DatabaseException databaseException) {
			throw new BusinessException("Error al recuperar el nombre del abogado. UsersBussines - findNameLawyerByIdRequisition", databaseException);
		}
	}
	public Boolean saveChangesLawyer(final ConsultaList req) throws BusinessException {
		try {
			this.userable.saveChangesLawyer(req);
			return true;
		} catch (DatabaseException databaseException) {
			throw new BusinessException("Error al actualizar idLawyer", databaseException);
		}
	}

	public List<User> findUsersCatalogPaged(final User user) throws BusinessException {
		try {
			final List<User> users = this.userable.findAllUsersCatalogPaged(user.getName(),user.getFirstLastName(),user.getSecondLastName(),user.getNumberPage(), 
					Integer.parseInt(this.configuration.findByName(
							ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
			final DESEncryption encryptor = new DESEncryption();
			for (User userParam : users) {
				userParam.setUsername(encryptor.decrypt(userParam.getUsername()));
			}
			return users;
		} catch (DatabaseException | DESEncryptionException databaseException) {
			LOG.error(MESSAGE_FIND_ALL_USERS_CATALOG_PAGED_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_ALL_USERS_CATALOG_PAGED_ERROR, databaseException);
		}
	}

	public User returnTotalPagesShowUser(final String name,final String apaterno,final String amaterno) throws NumberFormatException, BusinessException {
		try {
			final Long totalPages = this.userable.countTotalItemsToShowOfUser(name,apaterno,amaterno);
			final User user = new User();
			user.setNumberPage(this.configuration.totalPages(totalPages));
			user.setTotalRows(totalPages.intValue());
			return user;
		} catch (DatabaseException | NumberFormatException databaseException) {
			LOG.error(MESSAGE_FIND_TOTAL_PAGES_USERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_USERS_ERROR, databaseException);
		}
	}

	public List<User> findUsersByNameOrLastName(final String name, final Integer idRequisition, final Integer idFlow) 
			throws BusinessException {
		try {
			final List<User> users = 
					this.userable.findUsersByNameOrLastNameAndIdRequisition(name, idRequisition, idFlow);
			return users;
		} catch (DatabaseException databaseException) {
			LOG.error("Hubo un error al recuperar los usuarios por nombre o apellido: " + name, databaseException);
			throw new BusinessException("Error al recuperar los usuarios por nombre o apellido", databaseException);
		}
	}

	public List<User> findUsersByArea(final Integer idArea) throws BusinessException {
		try {
			return this.userable.findUsersByArea(idArea);
		} catch (DatabaseException databaseException) {
			LOG.error(ERROR_FIND_USERS_BY_AREA_MESSAGE, databaseException);
			throw new BusinessException(ERROR_FIND_USERS_BY_AREA_MESSAGE, databaseException);
		}
	}

	public Boolean isProfileUserFiltered(final Integer idUser, final Integer idFlow, FlowPurchasingEnum status) 
			throws BusinessException {
		try {
			return this.userable.isProfileUserFiltered(idUser, idFlow, status);
		} catch (DatabaseException databaseException) {
			LOG.error(ERROR_IS_PROFILE_USER_FILTERED, databaseException);
			throw new BusinessException(ERROR_IS_PROFILE_USER_FILTERED, databaseException);
		}
	}

	public Boolean validatePassword(final String password, final Integer idUser) throws BusinessException {
		try {
			User user = this.findByUserId(idUser);
			user =  this.findByUsernameForLogin(this.encryptUsername(user.getUsername()));
			return user.getPassword().equals(this.signOn(password, user.getSalt(), user.getStatus())) ? true : false;
		} catch (DatabaseException | EmptyResultException | DESEncryptionException databaseException) {
			LOG.error(ERROR_TO_VALIDATE_PASSWORD_MESSAGE, databaseException);
			throw new BusinessException(ERROR_TO_VALIDATE_PASSWORD_MESSAGE, databaseException);
		}
	}

	public void changePassword(final String password, final Integer idUser) throws BusinessException {
		try {
			User user = this.findByUserId(idUser);
			user =  this.findByUsernameForLogin(this.encryptUsername(user.getUsername()));
			user.setPassword(this.signOn(password, user.getSalt(), user.getStatus()));
			this.userable.updatePassword(user);
		} catch (DatabaseException | EmptyResultException | DESEncryptionException databaseException) {
			LOG.error(ERROR_TO_VALIDATE_PASSWORD_MESSAGE, databaseException);
			throw new BusinessException(ERROR_TO_VALIDATE_PASSWORD_MESSAGE, databaseException);
		}
	}

	//	public void createConectionWithLdap() throws BusinessException {
	//		try {
	//			final LdapConnectionParameters ldapConnectionParameters = new LdapConnectionParameters();
	//			ldapConnectionParameters.setUrl(this.userable.findUrlLdap());
	//			ldapConnectionParameters.setBase(this.userable.findBaseLdap());
	//			ldapConnectionParameters.setUserDn(this.userable.findUserLdap());
	//			ldapConnectionParameters.setPassword(this.userable.findPasswordLdap());
	//			this.ldapable.createConectionWithLdap(ldapConnectionParameters);
	//		} catch (DatabaseException | EmptyResultException databaseException) {
	//            LOG.error(ERROR_FIND_LDAP_CREDENTIALS, databaseException);
	//            throw new BusinessException(ERROR_FIND_LDAP_CREDENTIALS, databaseException);
	//        }
	//	}

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<User> userList = this.userable.findAll();
			return this.getExportUserMatrix(userList);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_EXPORTING_USERS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_EXPORTING_USERS_ERROR, databaseException);
		}
	}

	private String[][] getExportUserMatrix(final List<User> userList) throws BusinessException {
		final Integer columnsNumber = 18;
		final String[][] dataMatrix = new String[userList.size() + 1][columnsNumber];
		this.setHeaders(dataMatrix);
		Integer index = 1;
		for (User user : userList) {
			this.insertDataIntoMatrix(dataMatrix, index, user);
			index++;
		}
		return dataMatrix;
	}

	private void insertDataIntoMatrix(final String[][] dataMatrix, final Integer index, final User user)
			throws BusinessException {
		dataMatrix[index][0] = user.getIdUser().toString();
		dataMatrix[index][1] = StringUtils.getObjectStringValue(user.getIdUnderdirector());
		dataMatrix[index][2] = StringUtils.getObjectStringValue(user.getIdPosition());
		dataMatrix[index][NumbersEnum.THREE.getNumber()] = StringUtils.getObjectStringValue(user.getIdArea());
		dataMatrix[index][NumbersEnum.FOUR.getNumber()] = StringUtils.getObjectStringValue(user.getIdDga());
		dataMatrix[index][NumbersEnum.FIVE.getNumber()] = this.decryptValue(user.getUsername());
		dataMatrix[index][NumbersEnum.SIX.getNumber()] = user.getName();
		dataMatrix[index][NumbersEnum.SEVEN.getNumber()] = user.getFirstLastName();
		dataMatrix[index][NumbersEnum.EIGTH.getNumber()] = user.getSecondLastName();
		dataMatrix[index][NumbersEnum.NINE.getNumber()] = user.getPhoneNumber();
		dataMatrix[index][NumbersEnum.TEN.getNumber()] = user.getEmail();
		dataMatrix[index][NumbersEnum.ELEVEN.getNumber()] = user.getTerritory();
		dataMatrix[index][NumbersEnum.TWELVE.getNumber()] = StringUtils.getObjectStringValue(user.getStatus());
		dataMatrix[index][NumbersEnum.THIRTEEN.getNumber()] =
				StringUtils.getObjectStringValue(user.getIsLawyer());
		dataMatrix[index][NumbersEnum.FOURTEEN.getNumber()] =
				StringUtils.getObjectStringValue(user.getIsEvaluator());
		dataMatrix[index][NumbersEnum.FIFTEEN.getNumber()] =
				StringUtils.getObjectStringValue(user.getRetriesNumber());
		dataMatrix[index][NumbersEnum.SIXTEEN.getNumber()] =
				StringUtils.getObjectStringValue(user.getStatusLogin());
		dataMatrix[index][NumbersEnum.SEVENTEEN.getNumber()] =
				StringUtils.getObjectStringValue(user.getIsDecider());
	}

	private String decryptValue(final String value) throws BusinessException {
		try {
			if (value != null) {
				final DESEncryption encryptor = new DESEncryption();
				return encryptor.decrypt(value);
			}
			return value;
		} catch (DESEncryptionException desEncryptionException) {
			LOG.error(MESSAGE_EXPORTING_USERS_ERROR, desEncryptionException);
			throw new BusinessException(MESSAGE_EXPORTING_USERS_ERROR, desEncryptionException);
		}
	}

	private void setHeaders(final String[][] dataMatrix) {
		dataMatrix[0][0] = "IdUser";
		dataMatrix[0][1] = "IdUnderdirector";
		dataMatrix[0][2] = "IdPosition";
		dataMatrix[0][NumbersEnum.THREE.getNumber()] = "IdArea";
		dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "IdDga";
		dataMatrix[0][NumbersEnum.FIVE.getNumber()] = "Username";
		dataMatrix[0][NumbersEnum.SIX.getNumber()] = "Name";
		dataMatrix[0][NumbersEnum.SEVEN.getNumber()] = "FirstLastName";
		dataMatrix[0][NumbersEnum.EIGTH.getNumber()] = "SecondLastName";
		dataMatrix[0][NumbersEnum.NINE.getNumber()] = "PhoneNumber";
		dataMatrix[0][NumbersEnum.TEN.getNumber()] = "Email";
		dataMatrix[0][NumbersEnum.ELEVEN.getNumber()] = "Territory";
		dataMatrix[0][NumbersEnum.TWELVE.getNumber()] = "Status";
		dataMatrix[0][NumbersEnum.THIRTEEN.getNumber()] = "IsLawyer";
		dataMatrix[0][NumbersEnum.FOURTEEN.getNumber()] = "IsEvaluator";
		dataMatrix[0][NumbersEnum.FIFTEEN.getNumber()] = "RetriesNumber";
		dataMatrix[0][NumbersEnum.SIXTEEN.getNumber()] = "StatusLogin";
		dataMatrix[0][NumbersEnum.SEVENTEEN.getNumber()] = "IsDecider";
	}

	public boolean validarFormatoContrasena (String contrasena) {
		
		LOG.info("changePassword -> validarFormatoContrasena");
		
		Pattern pat = Pattern.compile(Constants.CONTRASENA_VALIDA);
		if(contrasena == null || contrasena.trim().isEmpty()) {
			return false;
		}

		contrasena = contrasena.trim();
		Matcher mat = pat.matcher(contrasena);

		LOG.info("changePassword -> validarFormatoContrasena match: " + mat.matches());
		if(mat.matches()) {
			return true;
		}

		return false;
	}
}