package mx.pagos.admc.service.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.engineer.utils.mail.structures.EmailContent;
import mx.pagos.admc.contracts.business.InitialSystemConfigurationBusiness;
import mx.pagos.admc.contracts.structures.InitialSystemConfiguration;
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.business.EmailsBusiness;
import mx.pagos.admc.core.structures.Configuration;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.exceptions.back.DESEncryptionException;
import mx.pagos.admc.util.back.DESEncryption;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.business.JwtUtil;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.exceptions.BlockedUserException;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class LoginService {

	private static final String SESSION_INACTIVE = "Sesión inactiva.";
	private static final String SESSION_ACTIVE = "Sesión activa.";
	private static final Logger LOG = Logger.getLogger(LoginService.class); 
	private static final String INVALID_CREDENTIALS_MESSAGE = "Credenciales del usuario inválidas";

	@Autowired
	private UsersBusiness userBusiness;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private EmailsBusiness emailsBusiness;

	@Autowired
	private ConfigurationsBusiness configurationBusiness;

	@Autowired
	private InitialSystemConfigurationBusiness initialSystemConfigurationBusiness;

	@RequestMapping (value = UrlConstants.IS_INITIAL_SYSTEM_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean isInitialSystemConfiguration(final HttpServletRequest request, 
			final HttpServletResponse response) {
		try {
			return this.initialSystemConfigurationBusiness.isFirstApplicationRun();
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return false;
	}


	@RequestMapping (value = UrlConstants.SAVE_INITIAL_SYSTEM_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody
	public final void saveInitialSystemConfiguration(
			@RequestBody final InitialSystemConfiguration initialSystemConfiguration, 
			final HttpServletResponse  response) {
		try {
			this.initialSystemConfigurationBusiness.saveInitialSystemConfiguration(initialSystemConfiguration);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping(value = UrlConstants.FIND_LOGIN_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public final Configuration findLoginType(final HttpServletRequest request, final HttpServletResponse response) 
			throws BusinessException {
		try {
			final Configuration bean = new Configuration();
			bean.setValue(this.userBusiness.findLoginType());

			//	    		if (Constants.LOGIN_ACTIVE_DIRECTORY.equals(bean.getValue())) {
			//	    			this.userBusiness.createConectionWithLdap();
			//	    		}

			return bean;
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.FIND_VERSION_SYSTEM, method = RequestMethod.POST)
	@ResponseBody
	public final String findVersionSystem(final HttpServletRequest request, final HttpServletResponse response) 
			throws BusinessException {
		try {
			return this.configurationBusiness.findByName("VERSION_SYSTEM");
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	@RequestMapping(value = UrlConstants.LOGIN, method = RequestMethod.POST)
	@ResponseBody
	public User login(@RequestBody User user, final HttpServletResponse response) throws BusinessException {
		
		LOG.info("\n////////////////////////////////////////\n"
				+ "EL USUARIO -"+user.getUsername()+"- ESTA USANDO EL NAVEGADOR :: "+user.getBrowser()
				+"\n\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
		
		this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Intento de Logueo del Usuario: " +
				user.getUsername(), this.session, LogCategoryEnum.TRY));
		try {
			user = this.userBusiness.validateUserCredentials(user.getUsername(), user.getPassword());
			this.setUserProfileAndSession(user);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Logueo del Usuario",
					this.session, LogCategoryEnum.LOGIN));
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_OK);
		} catch (BlockedUserException businessException) {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Usuario: " + user.getUsername() +
					". Bloqueado.", this.session, LogCategoryEnum.BLOCK));
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return null;
	}

	
	private void setUserProfileAndSession(final User user) throws BusinessException {
		user.setPassword(Constants.BLANK);
		user.setUsername(Constants.BLANK);
		user.setProfileList(this.userBusiness.findProfilesByidUser(user.getIdUser()));
		this.session.setIdUsuarioSession(user.getIdUser());
		this.session.setUsuario(user);
	}

	//    @RequestMapping(value = UrlConstants.LOGINAD, method = RequestMethod.POST)
	//    @ResponseBody
	//    public User loginAD(final HttpServletRequest request, final HttpServletResponse response)
	//            throws BusinessException {
	//        try {
	//        	final User user;
	//            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	//            if (authentication != null && authentication.isAuthenticated()) {
	//                user = this.userBusiness.validateUserCredentialsAD(authentication.getName());
	//                this.setUserProfileAndSession(user);
	//                return user;
	//            } else {
	//            	throw new BusinessException(INVALID_CREDENTIALS_MESSAGE);
	//            }
	//        } catch (BusinessException | BlockedUserException businessException) {
	//            LOG.error(businessException.getMessage(), businessException);
	//            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	//            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
	//        }
	//        return new User();
	//    }

	@RequestMapping(value = UrlConstants.VALIDATE_SESSION, method = RequestMethod.POST)
	@ResponseBody
	public Boolean validateSession(final HttpServletRequest request, final HttpServletResponse response)
			throws BusinessException {
				
		try {
			LOG.info("DATOS SESION  :: "+this.session.toString());
			
			if (this.session == null || this.session.getIdUsuarioSession() == null || this.session.getUsuario() == null) {				
				LOG.info("VALIDANDO SESION ······················ "+SESSION_INACTIVE);
				request.getSession().invalidate();
				response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
				response.setHeader(Constants.HEADER_MESSAGE, SESSION_INACTIVE);
				return false;
			} else {
				LOG.info("VALIDANDO SESION ······················ "+SESSION_ACTIVE);				
				response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_OK);				
				response.setHeader(Constants.HEADER_MESSAGE, SESSION_ACTIVE);
				
				return true;
			}
		} catch (Exception exception) {
			LOG.info("VALIDANDO SESION ······················ERROR ");
			LOG.error(exception.getMessage(), exception);
			request.getSession().invalidate();
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, exception.getMessage());
			return false;
		}
	}
	@RequestMapping(value = UrlConstants.VALIDATE_GERENTE, method = RequestMethod.POST)
	@ResponseBody
	public ConsultaList<User> validateGerente(@RequestBody User user, final HttpServletResponse response)
			throws BusinessException {
		 ConsultaList<User> userlist= new ConsultaList<>();

		try {		
			if (this.session.getUsuario().getIdUser() != null || this.session == null || this.session.getIdUsuarioSession() == null || this.session.getUsuario() == null) {
				userlist.setList(this.userBusiness.findGerente(this.session.getUsuario().getIdUser()));
				LOG.info("EL idUser es : "+this.session.getUsuario().getIdUser());
			}
		} catch (Exception businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			userlist.setList(new ArrayList<User>() );
		}
		return userlist;
	}

	@RequestMapping(value = UrlConstants.RECOVERY_PASSWORD, method = RequestMethod.POST)
	@ResponseBody
	public Boolean recoverPassword(@RequestBody final String recoveryEmail, final HttpServletResponse response) {
		try {
			final DESEncryption encryptor = new DESEncryption();
			final User userFromRecoveryEmail = this.userBusiness.isEmailRegistered(recoveryEmail);
			this.emailsBusiness.sendEmailPassword(this.createEmailContent(recoveryEmail,
					encryptor.decrypt(userFromRecoveryEmail.getUsername()), this.userBusiness.
					generatePasswordForRecovering(userFromRecoveryEmail)), this.getTo(recoveryEmail));
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_OK);
			return true;
		} catch (BusinessException businessException) {
			LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		} catch (DESEncryptionException dESEncryptionException) {
			LOG.error(dESEncryptionException.getMessage(), dESEncryptionException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, "Error al desencriptar el usuario propietario del email"
					+ " para la recuperación de contraseña");
			return false;
		}
	}

	private String[] getTo(final String recoveryEmail) {
		return new String[]{recoveryEmail};
	}

	private EmailContent createEmailContent(final String recoveryEmail, final String username,
			final String newPassword) throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject(this.configurationBusiness.findByName("RECOVERY_PASSWORD_SUBJECTEMAIL"));
		emailContent.setFieldOne("Usuario:");
		emailContent.setFieldOneDescription(username);
		emailContent.setFieldTwo("Nueva Contraseña:");
		emailContent.setFieldTwoDescription(newPassword);
		emailContent.setContent("Estos son tus datos de recuperación de contraseña. Ahora puedes ingresar nuevamente"
				+ " al Sistema de Administración de Contratos con la nueva contraseña generada.");
		emailContent.setBrand(this.configurationBusiness.findByName("EMAIL_NOTIFICATION_BRAND"));
		return emailContent;
	}

	@RequestMapping(value = UrlConstants.CLOSE_SESSION, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean closeSession (final HttpServletRequest request, final HttpServletResponse response)
			throws BusinessException {
		this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Cierre de sesión", this.session, LogCategoryEnum.LOGOUT));
		this.userBusiness.logCloseSession(this.session);
		request.getSession().invalidate();
		return true;
	}

	@RequestMapping(value = UrlConstants.FEED_SESSION, method = RequestMethod.POST)
	@ResponseBody
	public void feedSession(@RequestBody final Integer minutesInSessionParameter, final HttpServletResponse response) 
			throws BusinessException {
		try {
			this.session.setMinutesInSession(minutesInSessionParameter);
		} catch (Exception exception) {
			LOG.error(exception.getMessage(), exception);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, SESSION_INACTIVE);
		}
	}

}
