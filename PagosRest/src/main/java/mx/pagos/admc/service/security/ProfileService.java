package mx.pagos.admc.service.security;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.structures.Company;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.business.ProfilesBusiness;
import mx.pagos.security.structures.Menu;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.ProfileScreenFlow;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */
@Controller
public class ProfileService {
	
	private static final Logger log = LoggerFactory.getLogger(ProfileService.class);


	@Autowired
	private ProfilesBusiness profilesBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private BinnacleBusiness binnacleBusiness;

	@RequestMapping(value = UrlConstants.PROFILE_SAVEORUPDATE, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdate(@RequestBody final Profile profile, final HttpServletResponse response) {
		Integer idProfile = 0;
		try {
			log.debug("Se recibe actualizacion o guardado de perfil: " + idProfile);
			idProfile = this.profilesBusiness.saveOrUpdate(profile);
			return idProfile;                   
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: saveOrUpdate");
			log.error(businessException.getMessage());
		}
		return idProfile;
	}

	@RequestMapping(value = UrlConstants.PROFILE_CHANGE_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeStatus(@RequestBody final Profile profile, final HttpServletResponse response) {
		try {
			log.debug("Se hará un filtro por perfil " +  profile.getIdProfile() + " y Estatus " + profile.getStatus());
			this.profilesBusiness.changeStatus(profile.getIdProfile(), profile.getStatus());        
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: changeStatus");
			log.error(businessException.getMessage());
		}
	}

	@RequestMapping (value = UrlConstants.PROFILE_FIND_ALL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Profile> findAll(final HttpServletResponse response) {
		try {
			log.debug("Se va a obtener la lista de Perfiles");
			final ConsultaList<Profile> list = new ConsultaList<Profile>();
			list.setList(this.profilesBusiness.findAll());
			return list;            
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findAll");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Profile>();
	}


	@RequestMapping (value = UrlConstants.PROFILE_FIND_BY_STATUS , method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Profile> findByRecordStatus(@RequestBody final Profile profile,
			final HttpServletResponse response) {
		try {
			log.debug("Se va a consultar por estatus" + profile.getStatus());
			final ConsultaList<Profile> list = new ConsultaList<Profile>();
			list.setList(this.profilesBusiness.findByRecordStatus(profile.getStatus()));
			return list;            
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findByRecordStatus");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Profile>();
	}

	@RequestMapping (value = UrlConstants.PROFILE_FIND_BY_ID , method = RequestMethod.POST)
	@ResponseBody
	public final Profile findById(@RequestBody final Profile profile, final HttpServletResponse response) {
		try {
			log.debug("Se va a consultar por id" + profile.getIdProfile());
			return this.profilesBusiness.findById(profile.getIdProfile());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());     
			log.error(" BusinessException :: ProfileService :: findById");
			log.error(businessException.getMessage());
		}
		return new Profile();
	}

	@RequestMapping(value = UrlConstants.PROFILE_SAVENEWMENUPROFILE, method = RequestMethod.POST)
	@ResponseBody
	public final void saveNewMenuProfile(@RequestBody final ConsultaList<String> profileList, 
			final HttpServletResponse response) {
		try {
			log.debug("Se guardará un menú de perfiles " +  profileList.getParam1());
			this.profilesBusiness.saveNewMenuProfile(Integer.valueOf(profileList.getParam1()), profileList.getList()); 
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: saveNewMenuProfile");
			log.error(businessException.getMessage());
		}
	}

	@RequestMapping (value = UrlConstants.PROFILE_FINDMENUPROFILEBYIDPROFILE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Menu> findMenuProfileByIdProfile(@RequestBody final ParametersHolder parametersHolder,
			final HttpServletResponse response) {
		try {
			log.debug("Se va a consultar el menu de acuerdo a los perfiles");
			final ConsultaList<Menu> findMenuList = new ConsultaList<Menu>();
			// Se separo el paso de guardar el flujo seleccionado en la session
			findMenuList.setList(this.profilesBusiness.findMenuProfileByIdProfileList(this.session.getUsuario().getProfileList(), false));
			return findMenuList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findMenuProfileByIdProfile");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Menu>();
	}

	@RequestMapping (value = UrlConstants.PROFILE_FINDMENUBYID, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Menu> findMenuProfileByIdProfile(@RequestBody final Profile vo,
			final HttpServletRequest request, final HttpServletResponse response) {
		try {
			log.info("ProfileService :: findMenuProfileByIdProfile");
			final ConsultaList<Menu> list = new ConsultaList<Menu>();
			list.setList(this.profilesBusiness.findMenuProfileByIdProfile(vo.getIdProfile()));
			return list;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.info(" BusinessException :: ProfileService :: findMenuProfileByIdProfile");
			log.info(businessException.getMessage());
		}
		return new ConsultaList<Menu>();
	}


	private void setIdFlowToSession(final String idFlow) {
		if (idFlow != null && !idFlow.isEmpty()) {
			this.session.setIdFlow(Integer.parseInt(idFlow));
		}
	}

	@RequestMapping(value = UrlConstants.PROFILE_SAVENEWPROFILESCREEN, method = RequestMethod.POST)
	@ResponseBody
	public final void saveNewProfileScreen(@RequestBody final ConsultaList<ProfileScreenFlow> profileScreenParameters,
			final HttpServletResponse response) {
		try {
			log.debug("Se guardará una pantalla de perfil " +  profileScreenParameters.getParam1());
			this.profilesBusiness.saveProfileScreen(profileScreenParameters.getList());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: saveNewProfileScreen");
			log.error(businessException.getMessage());
		}
	}

	@RequestMapping (value = UrlConstants.PROFILE_FINDPROFILESCREENTRAYBYIDPROFILE, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<ProfileScreenFlow> findProfileScreenTrayByIdProfile(final HttpServletResponse response) {
		try {
			final ConsultaList<ProfileScreenFlow> findProfileScreenList = new ConsultaList<ProfileScreenFlow>();
			log.debug("Se va a consultar pantalla por perfil");
			findProfileScreenList.setList(this.profilesBusiness.findProfileScreenTrayByProfilesAndIdFlow(
					this.session.getUsuario().getProfileList(), this.session.getIdFlow()));
			return findProfileScreenList;
		} catch (BusinessException | NullPointerException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findProfileScreenTrayByIdProfile");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<ProfileScreenFlow>();
	}

	@RequestMapping (value = UrlConstants.SEARCH_FLOWS_BY_USER_PROFILES, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Flow> findFlowsByProfiles(final HttpServletResponse response) {
		try {
			final ConsultaList<Flow> flowsList = new ConsultaList<Flow>();
			flowsList.setList(this.profilesBusiness.findFlowsByProfiles(this.session.getUsuario().getProfileList()));
			if(flowsList!=null && flowsList.getList()!=null && !flowsList.getList().isEmpty()) {
				flowsList.setList(flowsList.getList().stream()
						  .sorted(Comparator.comparing(Flow::getIdFlow))
						  .collect(Collectors.toList()));
				this.setIdFlowToSession(flowsList.getList().get(0).getIdFlow().toString());
			}
			return flowsList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findFlowsByProfiles");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Flow>();
	}
	
	@RequestMapping (value = UrlConstants.SEARCH_UNIT_BY_USER_PROFILES, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Unit> findUnitsByProfiles(@RequestBody final List<Company> idFlow, final HttpServletResponse response) {
		try {
			final ConsultaList<Unit> flowsList = new ConsultaList<Unit>();
			flowsList.setList(this.profilesBusiness.findUnitsByProfiles(idFlow));
			// if(flowsList!=null && flowsList.getList()!=null && flowsList.getList().size() == 1) {
				// this.setIdFlowToSession(flowsList.getList().get(0).getIdFlow().toString());
			// }
			return flowsList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findUnitsByUserProfiles");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Unit>();
	}

	@RequestMapping (value = UrlConstants.SEARCH_COMPANY_BY_USER_PROFILES, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Company> findCompaniesByProfiles(final HttpServletResponse response) {
		try {
			final ConsultaList<Company> flowsList = new ConsultaList<Company>();
			flowsList.setList(this.profilesBusiness.findCompaniesByProfiles());
			// if(flowsList!=null && flowsList.getList()!=null && flowsList.getList().size() == 1) {
				// this.setIdFlowToSession(flowsList.getList().get(0).getIdFlow().toString());
			// }
			return flowsList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error(" BusinessException :: ProfileService :: findUnitsByUserProfiles");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Company>();
	}
	
	
	
	@RequestMapping (value = UrlConstants.VALIDATE_PROFILE_NAME, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean validateProfileName(@RequestBody final String profileName, 
			final HttpServletResponse response) {
		try {
			return this.profilesBusiness.validateProfileName(profileName);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return false;
	}

	@RequestMapping (value = UrlConstants.FIND_PROFILE_BY_NAME, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Profile> findProfilesByName(@RequestBody final Profile profile, 
			final HttpServletResponse response) {
		final ConsultaList<Profile> profileList = new ConsultaList<>();
		try {
			profileList.setList(this.profilesBusiness.findProfilesByName(profile.getName()));
			return profileList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}
		return profileList;
	}

	@RequestMapping (value = UrlConstants.FIND_PROFILE_CATALOG_PAGED, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Profile> findAllProfileCatalogPaged(@RequestBody final Profile profile, 
			final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta de todos los perfiles", this.session, LogCategoryEnum.QUERY));
			final ConsultaList<Profile> profileList = new ConsultaList<>();
			profileList.setList(this.profilesBusiness.findProfileCatalogPaged(profile));
			return profileList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new ConsultaList<Profile>();
	}

	@RequestMapping (value = UrlConstants.FIND_PROFILE_TOTAL_ROWS, method = RequestMethod.POST)
	@ResponseBody
	public final Profile returnTotalRowsOfProfile(@RequestBody final Profile profile, 
			final HttpServletResponse response) {
		try {
			this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
					"Consulta del número de paginas de catálogo de perfiles", 
					this.session, LogCategoryEnum.QUERY));
			return this.profilesBusiness.returnTotalPagesShowProfile(profile.getName());
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
		}
		return new Profile();
	}

	@RequestMapping (value = UrlConstants.OBTENER_MENUS_POR_PERFIL, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Menu> obtenerMenuPorPerfil(final HttpServletResponse response) {
		try {
			log.debug("Se va a consultar el menu de acuerdo a los perfiles");
			final ConsultaList<Menu> findMenuList = new ConsultaList<Menu>();
			
			// Integer idUser = 
			findMenuList.setList(this.profilesBusiness.findMenuProfileByIdProfileList(this.session.getUsuario().getProfileList(), true));
			return findMenuList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			log.error("BusinessException :: ProfileService :: findMenuProfileByIdProfile");
			log.error(businessException.getMessage());
		}
		return new ConsultaList<Menu>();
	}

	@RequestMapping(value = UrlConstants.GUARDAR_IDENTIFICADOR_DEL_FLUJO, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean establecerFlujoSeleccionado(@RequestBody final Integer idFlow, final HttpServletResponse response) {
		try {
			if(validarFlujoSeleccionado(idFlow)) {
				this.setIdFlowToSession(idFlow != null ? idFlow.toString() : null);
				response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_OK);
			} else {
				response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
				return false;
			}

		} catch (BusinessException e) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			log.error("BusinessException :: ProfileService :: establecerFlujoSeleccionado");
			log.error(e.getMessage());
			return false;
		}
		return true;
	}

	private boolean validarFlujoSeleccionado (Integer idFlow) throws BusinessException {
		if(idFlow == null) {
			return false;
		}

		List<Flow> flujos = this.profilesBusiness.findFlowsByProfiles(this.session.getUsuario().getProfileList());
		if(flujos == null || flujos.isEmpty()) {
			return false;
		}

		flujos = flujos.stream().filter(f-> f.getIdFlow().toString().equals(idFlow.toString())).collect(Collectors.toList());
		if(flujos == null || flujos.isEmpty()) {
			return false;
		}
		return true;
	}
	
	

}
