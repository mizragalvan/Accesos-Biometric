package mx.pagos.admc.service.generals;

import java.util.ArrayList;

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
import mx.pagos.admc.contracts.business.FlowsBusiness;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.business.EmailsBusiness;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.security.UserStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.business.UsersBusiness;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.User;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */
@Controller
public class UsersService {
	@Autowired
	private UsersBusiness usersBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private FlowsBusiness flowBuss;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@Autowired
	private EmailsBusiness emailsBusiness;

	@Autowired
	private ConfigurationsBusiness configurationsBusiness;

	private static final Logger LOG = Logger.getLogger(UsersService.class);

	@RequestMapping (value = UrlConstants.SAVEORUPDATEUSER, method = RequestMethod.POST)
	@ResponseBody
	public final void saveOrUpdate(@RequestBody final User user, final HttpServletRequest request,
			final HttpServletResponse response) {
		try {
			final User decryptedUser = this.userWithoutEncripted(user);
			this.usersBusiness.saveOrUpdate(user);
			this.sendEmailWhenUserRegistration(request, decryptedUser);
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se dio de alta el usuario "
					+ decryptedUser.getUsername() + " y se le notifico vía email", this.session, LogCategoryEnum.SAVE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
	}

	private void sendEmailWhenUserRegistration(final HttpServletRequest request, final User decryptedUser)
			throws BusinessException {
		if (decryptedUser.getIdUser() == null)
			this.emailsBusiness.sendEmail(this.createEmailContent(request, decryptedUser),
					new String[]{decryptedUser.getEmail()});
	}

	private User userWithoutEncripted(final User user) {
		final User decryptedUser = new User();
		decryptedUser.setIdUser(user.getIdUser());
		decryptedUser.setUsername(user.getUsername());
		decryptedUser.setPassword(user.getPassword());
		decryptedUser.setEmail(user.getEmail());
		return decryptedUser;
	}

	private EmailContent createEmailContent(final HttpServletRequest request, final User user)
			throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject(this.configurationsBusiness.findByName("ADD_USER_SUBJECTEMAIL"));
		emailContent.setFieldOne("URL:");
		emailContent.setFieldOneDescription(this.createUrlApplication(request));
		emailContent.setFieldTwo("Usuario:");
		emailContent.setFieldTwoDescription(user.getUsername());
		emailContent.setFieldThree("Contraseña:");
		emailContent.setFieldThreeDescription(user.getPassword());
		emailContent.setContent("Has sido dado de alta en el Sistema de Administración de Gerdau Corsa - Contratos. Ingresa con los"
				+ " datos arriba mencionados.");
		return emailContent;
	}

	private String createUrlApplication(final HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/AdmContract";
	}

	@RequestMapping (value = UrlConstants.SEARCH_ADD_USERS_TO_VOBO, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> searchAddUsersToVoBo(@RequestBody final ConsultaList<User> userConsultaList, 
			final HttpServletResponse response) {
		try {
			final ConsultaList<User> listResponse = new ConsultaList<User>();
			listResponse.setList(this.usersBusiness.findUsersByNameOrLastName(
					userConsultaList.getParam1(), userConsultaList.getParam4(), this.session.getIdFlow()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return this.createEmptyUserConsultaList();
		}
	}

	@RequestMapping (value = UrlConstants.FIND_USER_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final User findUserById(@RequestBody final Integer idUser, final HttpServletResponse  response) {
		try {
			LOG.info("findUserById -> idUser: " + idUser);
			return this.usersBusiness.findByUserId(idUser);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return new User();
		}
	}

	@RequestMapping (value = UrlConstants.FIND_LOGGED_USER, method = RequestMethod.POST)
	@ResponseBody
	public final User findLoggerUser(final HttpServletResponse response) {
		try {
			return this.usersBusiness.findByUserId(this.session.getUsuario().getIdUser());       	
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return new User();
		}
	}

	@RequestMapping (value = UrlConstants.FIND_FLOW_BY_SESSION, method = RequestMethod.POST)
	@ResponseBody
	public final Flow findFlowStepSession(final HttpServletRequest request, final HttpServletResponse response) {
		try {
			Flow flow = new Flow();
			flow.setName("");
			if (this.session.getIdFlow() != null) {
				flow = this.flowBuss.findById(this.session.getIdFlow());
			}
			return flow;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return new Flow();
		}
	}

	@RequestMapping (value = UrlConstants.FIND_FLOW_STEP_BY_SESSION, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<String> findFlowSession(final HttpServletRequest request,
			final HttpServletResponse  response) {
		try {
			final ConsultaList<String> stringList = new ConsultaList<>();
			stringList.setList(this.flowBuss.findStepListByIdFlow(this.session.getIdFlow()));
			return stringList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return new ConsultaList<String>();
		}
	}

	@RequestMapping (value = UrlConstants.SEARCH_USERSBYNAME, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> searchUsersByName(@RequestBody final ConsultaList<User> vo, 
			final HttpServletResponse  response) {
		try {
			final ConsultaList<User> listResponse = new ConsultaList<User>();
			listResponse.setList(this.usersBusiness.findUsersByName(vo.getParam1(),vo.getParam2(),vo.getParam3()));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return this.createEmptyUserConsultaList();
		}
	}

	@RequestMapping(value = UrlConstants.SEARCH_LAWYER, method = RequestMethod.GET)
	@ResponseBody
	public final ConsultaList<User> searchLawyerByIdUser(final HttpServletResponse response) {
		try {
			final ConsultaList<User> listResponse = new ConsultaList<User>();
			listResponse.setList(this.usersBusiness.findLawyersByIdUser());
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return this.createEmptyUserConsultaList();
		}
	}
	
	@RequestMapping(value = UrlConstants.SEARCH_USER_MAIL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> searchUserMail(@RequestBody final String req, final HttpServletResponse response) {
		try {
			final ConsultaList<User> listResponse = new ConsultaList<User>();
			listResponse.setList(this.usersBusiness.findUserMailAddress(req));
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return this.createEmptyUserConsultaList();
		}
	}
	@RequestMapping(value = UrlConstants.SEARCH_LAWYER_NAME, method = RequestMethod.POST)
	@ResponseBody
	public final String searchLawyerName(@RequestBody final String req, final HttpServletResponse response) {
		try {
			String lawyerName = new String();
			lawyerName = this.usersBusiness.findLawyerNameByIdRequisition(req);
			return lawyerName;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return null;
		}
	}
	@RequestMapping(value = UrlConstants.SAVE_CHANGES_LAWYER, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean saveChangesLawyer(@RequestBody final ConsultaList req, final HttpServletResponse response) {
		try {
			this.usersBusiness.saveChangesLawyer(req);
			this.emailsBusiness.sendMailAssignment(req, req.getParam2());
			return true;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}
	}
	@RequestMapping (value = UrlConstants.SEARCH_ALLUSERS, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> searchAllUsers(final HttpServletResponse  response) {
		try {
			final ConsultaList<User> listResponse = new ConsultaList<User>();
			listResponse.setList(this.usersBusiness.findAll());
			return listResponse;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return this.createEmptyUserConsultaList();
		}
	}

	@RequestMapping (value = UrlConstants.FIND_USERS_BY_AREA, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> findUsersByArea(@RequestBody final String idArea, 
			final HttpServletResponse response) {
		try {
			final ConsultaList<User> userList = new ConsultaList<User>();
			userList.setList(this.usersBusiness.findUsersByArea(Integer.valueOf(idArea)));
			return userList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return this.createEmptyUserConsultaList();
	}

	@RequestMapping (value = UrlConstants.CHANGEUSERSTATUS, method = RequestMethod.POST)
	public final void changeUserStatus(@RequestBody final User user, final HttpServletResponse response) {
		try {
			this.usersBusiness.changeUserStatus(user.getIdUser(), user.getStatus());
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se cambio el estatus del usuario "
					+ user.getUsername(), this.session, LogCategoryEnum.UPDATE));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
	}

	@RequestMapping (value = UrlConstants.PROFILE_FIND_BY_IDUSER, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Profile> findByIdUser(@RequestBody final User user, final HttpServletResponse response) {
		try {
			final ConsultaList<Profile> list = new ConsultaList<Profile>();
			LOG.info("findByIdUser -> consulta de usuario  -> idUser: " + user.getIdUser());
			list.setList(this.usersBusiness.findProfilesByidUser(user.getIdUser()));
			return list;            
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return this.createEmptyProfileConsultaList();
		}
	}

	private ConsultaList<User> createEmptyUserConsultaList() {
		final ConsultaList<User> consultaList = new ConsultaList<User>();
		consultaList.setList(new ArrayList<User>());
		return consultaList;
	}

	private ConsultaList<Profile> createEmptyProfileConsultaList() {
		final ConsultaList<Profile> consultaList = new ConsultaList<Profile>();
		consultaList.setList(new ArrayList<Profile>());
		return consultaList;
	}

	@RequestMapping (value = UrlConstants.FIND_USERS_BY_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public ConsultaList<User> findByStatus(@RequestBody final String status, final HttpServletResponse response) {
		try {
			final ConsultaList<User> userList = new ConsultaList<User>();
			userList.setList(this.usersBusiness.findByStatus(UserStatusEnum.valueOf(status)));
			return userList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage()); 
		}
		return new ConsultaList<User>();
	}

	@RequestMapping (value = UrlConstants.USER_EMAIL_EXISTS, method = RequestMethod.POST)
	@ResponseBody
	public Boolean userEmailExists(@RequestBody final String email, final HttpServletResponse response) {
		try {
			this.usersBusiness.isEmailRegistered(email);
			return true;
		} catch (BusinessException businessException) {
			if (!(businessException.getCause() instanceof EmptyResultException)) {
				response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
				response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage()); 
			}
			return false;
		}
	}

	@RequestMapping (value = UrlConstants.USERNAME_EXISTS, method = RequestMethod.POST)
	@ResponseBody
	public Boolean usernameExists(@RequestBody final String username, final HttpServletResponse response) {
		try {
			return this.usersBusiness.usernameExists(username);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return true;
	}

	@RequestMapping (value = UrlConstants.IS_USER_FILTERED, method = RequestMethod.POST)
	@ResponseBody
	public Boolean isProfileUserFiltered(@RequestBody final String status, 
			final HttpServletResponse response) {
		try {
			return this.usersBusiness.isProfileUserFiltered(this.session.getUsuario().getIdUser(), 
					this.session.getIdFlow(), FlowPurchasingEnum.valueOf(status));
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage()); 
		}
		return true;
	}

	@RequestMapping (value = UrlConstants.VALIDATE_PASSWORD, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean validatePassword(@RequestBody final String password, final HttpServletResponse response) {
		try {
			final Boolean validate =
					this.usersBusiness.validatePassword(password, this.session.getUsuario().getIdUser());
			return validate;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return false;
	}

	@RequestMapping (value = UrlConstants.CHANGE_PASSWORD, method = RequestMethod.POST)
	@ResponseBody
	public final boolean changePassword(@RequestBody final String password, final HttpServletResponse response) {
		try {
			LOG.info("changePassword");
			LOG.info("changePassword: " + this.usersBusiness.validarFormatoContrasena(password));
			if(this.usersBusiness.validarFormatoContrasena(password)) {
				this.usersBusiness.changePassword(password, this.session.getUsuario().getIdUser());
				return true;
			}else {
				return false;
			}
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			return false;
		}   
	}

	@RequestMapping (value = UrlConstants.FIND_ALL_USERS_PAGED, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<User> findAllUsersCatalogPaged(@RequestBody final User user, 
			final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta de todos los usuarios", this.session, LogCategoryEnum.QUERY));
			final ConsultaList<User> userList = new ConsultaList<>();
			userList.setList(this.usersBusiness.findUsersCatalogPaged(user));
			return userList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new ConsultaList<User>();
	}

	@RequestMapping (value = UrlConstants.FIND_TOTAL_PAGES_FOR_CATALOG_OF_USER, method = RequestMethod.POST)
	@ResponseBody
	public final User returnTotalRowsOfUser(@RequestBody final User user, final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta del número de paginas de catálogo de usuarios", 
					this.session, LogCategoryEnum.QUERY));
			return this.usersBusiness.returnTotalPagesShowUser(user.getName(),user.getFirstLastName(),user.getSecondLastName());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
		}
		return new User();
	}
}
