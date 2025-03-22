package mx.pagos.security.business;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.pagos.admc.contracts.interfaces.ProfileScreenFlowable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Company;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.enums.ApplicationEnum;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.interfaces.Profileable;
import mx.pagos.security.structures.Menu;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.ProfileScreenFlow;

/**
 * Clase que contiene las reglas de negocio de perfiles de usuario. Permite
 * guardar, recuperar y cambiar estatus de los perfiles; Guardar y recuperar el
 * menu del perfil; Guardar y recuperar la bandeja de entrada por perfil.
 * 
 * @see Profile
 * @see Profileable
 * @see BusinessException
 */
@Service("ProfilesBusiness")
public class ProfilesBusiness extends AbstractExportable {
	private static final String MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR = "Hubo un error al recuperar los flujos asignados a los perfiles del usuario";
	private static final String MESSAGE_PROFILE_NAME_DUPLICATE_ERROR = "Hubo un error al validar el nombre del perfil";
	private static final String MESSAGE_FIND_PROFILE_BY_NAME_ERROR = "Hubo un error al buscar perfiles por el nombre";
	private static final String MESSAGE_EXPORTING_PROFILE_ERROR = "Hubo un problema al exportar el catálogo Perfiles";
	private static final String MESSAGE_FIND_ALL_PROFILE_CATALOG_PAGED_ERROR = "Hubo un problema al buscar perfiles paginados";
	private static final String MESSAGE_FIND_TOTAL_PAGES_PROFILE_ERROR = "Hubo un problema al buscar número de pagínas de perfiles";

	private static final Logger log = LoggerFactory.getLogger(ProfilesBusiness.class);

	@Autowired
	private Profileable profileable;

	@Autowired
	private ProfileScreenFlowable profileScreenable;

	@Autowired
	private ConfigurationsBusiness configuration;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer saveOrUpdate(final Profile profile) throws BusinessException {
		try {
			log.info("Se recibe guardado o actualizado de perfiles");
			if (profile.getIdProfile() != null) {
				log.info("Se eliminan perfiles para una actualizacion");
				this.profileable.deleteMenuProfileByIdProfile(profile.getIdProfile());
				this.profileScreenable.deleteProfileScreenFlowByIdProfile(profile.getIdProfile());
			}
			profile.setIdProfile(this.profileable.saveOrUpdate(profile));
			for (Menu tmp : profile.getListaMenu()) {
				log.info("Insersion del perfil " + profile.getIdProfile() + " con el menu " + tmp.getFactoryName());
				this.profileable.saveNewMenuProfile(profile.getIdProfile(), tmp.getFactoryName());
			}
			for (ProfileScreenFlow tmp : profile.getListaScreen()) {
				log.info("Insersion de bandeja: " + tmp.getFactoryName());
				tmp.setIdProfile(profile.getIdProfile());
				tmp.setIdFlow(profile.getIdFlow());
				this.profileScreenable.saveProfileScreenFlow(tmp);
			}
			return profile.getIdProfile();
		} catch (DatabaseException e) {
			log.info("Error en insersion del perfil " + profile.getIdProfile() + " error: " + e.getMessage());
			throw new BusinessException("Error al guardar datos del perfil", e);
		}
	}

	public void changeStatus(final Integer idProfile, final RecordStatusEnum status) throws BusinessException {
		try {
			if (status == RecordStatusEnum.ACTIVE)
				this.profileable.changeProfileStatus(idProfile, RecordStatusEnum.INACTIVE);
			else
				this.profileable.changeProfileStatus(idProfile, RecordStatusEnum.ACTIVE);
		} catch (DatabaseException dataBaseException) {
			log.error("Error al cambiar el estatus del perfil", dataBaseException);
			throw new BusinessException("Ocurrió un error al cambiar el estatus del perfil", dataBaseException);
		}
	}

	public List<Profile> findAll() throws BusinessException {
		try {
			return this.profileable.findAll();
		} catch (DatabaseException dataBaseException) {
			log.error("Error al obtener datos de los perfiles", dataBaseException);
			throw new BusinessException("Ocurrió un error al obtener datos de los perfiles", dataBaseException);
		}
	}

	public List<Profile> findByRecordStatus(final RecordStatusEnum recordStatusEnum) throws BusinessException {
		try {
			return this.profileable.findByRecordStatus(recordStatusEnum);
		} catch (DatabaseException databaseException) {
			log.error("Error al obtener datos de los perfiles por estatus", databaseException);
			throw new BusinessException("Error al obtener los perfiles por estatus", databaseException);
		}
	}

	public Profile findById(final Integer idParemeter) throws BusinessException {
		try {
			return this.profileable.findById(idParemeter);
		} catch (DatabaseException databaseException) {
			log.error("Error al obtener el perfil por Id", databaseException);
			throw new BusinessException("Ocurrió un error al obtener el perfil por Id", databaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveNewMenuProfile(final Integer idProfile, final List<String> menuItemsList) throws BusinessException {
		try {
			this.profileable.deleteMenuProfileByIdProfile(idProfile);
			for (String menuItem : menuItemsList)
				this.profileable.saveNewMenuProfile(idProfile, menuItem);
		} catch (DatabaseException databaseException) {
			log.error("Error al agregar el menu por perfil", databaseException);
			throw new BusinessException("Ocurrió un error al agregar el menu por perfil", databaseException);
		}
	}

	public List<Menu> findMenuProfileByIdProfileList(final List<Profile> profileList, Boolean isAngular)
			throws BusinessException {
		try {

			return checkList(this.profileable.findMenuProfileByIdProfileList(profileList), isAngular);
		} catch (DatabaseException databaseException) {
			log.error("Error al obtener el menu por perfil", databaseException);
			throw new BusinessException("Ocurrió un error al obtener el menu por perfil", databaseException);
		}
	}

	/**
	 * Metodo que filtra menus.
	 * 
	 * @param findMenuProfileByIdProfileList la lista de BD
	 * @param isAngular                      bandera de application
	 * @return la lista filtrada
	 */
	private List<Menu> checkList(List<Menu> findMenuProfileByIdProfileList, boolean isAngular) {
		List<Menu> list = new ArrayList<Menu>();
		if (isAngular) {
			findMenuProfileByIdProfileList.forEach((m) -> {
				if ((m.getApplication1().equals(ApplicationEnum.ANGULAR) || m.getApplication1().equals(ApplicationEnum.DEFAULT))
						&& m.getMenuLevel().equals("0")) {
					list.add(m);
				}
			});
			list.forEach((p) -> {
				p.setSubMenu(new ArrayList<Menu>());
				findMenuProfileByIdProfileList.forEach((m) -> {
					if ((m.getApplication1().equals(ApplicationEnum.ANGULAR)
							|| m.getApplication1().equals(ApplicationEnum.DEFAULT)) && m.getMenuLevel().equals("1")
							&& p.getFactoryName().equals(m.getFactoryNameParent())) {
						p.getSubMenu().add(m);
					}
				});
			});
		} else {
			findMenuProfileByIdProfileList.forEach((m) -> {
				if (m.getApplication1() == null || m.getApplication1().equals(ApplicationEnum.GWT)) {
					list.add(m);
				}

			});

		}
		return list;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void saveProfileScreen(final List<ProfileScreenFlow> profileScreenFlows) throws BusinessException {
		try {
			this.profileable.deleteProfileScreenFlowByIdProfile(profileScreenFlows.get(0).getIdProfile());
			for (ProfileScreenFlow profileScreenFlow : profileScreenFlows) {
				this.profileable.saveProfileScreenFlow(profileScreenFlow);
			}
		} catch (DatabaseException databaseException) {
			log.error("Error al agregar las pantallas por perfil", databaseException);
			throw new BusinessException("Ocurrió un error al agregar las pantallas por perfil", databaseException);
		}
	}

	public List<ProfileScreenFlow> findProfileScreenTrayByProfilesAndIdFlow(final List<Profile> profilesList,
			final Integer idFlow) throws BusinessException {
		try {
			return this.profileable.findProfileScreenTrayByProfilesAndIdFlow(profilesList, idFlow);
		} catch (DatabaseException databaseException) {
			log.error("Error al obtener la bandeja por perfil", databaseException);
			throw new BusinessException("Ocurrió un error al obtener la bandeja por pefil", databaseException);
		}
	}

	public List<Flow> findFlowsByProfiles(final List<Profile> profilesList) throws BusinessException {
		try {
			return this.profileable.findFlowsByProfiles(profilesList);
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR, databaseException);
		}
	}

	public List<Unit> findUnitsByProfiles(final List<Company> idFlow) throws BusinessException {
		try {
			return this.profileable.findUnitsByProfiles(idFlow);
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR, databaseException);
		}
	}

	public List<Company> findCompaniesByProfiles() throws BusinessException {
		try {
			return this.profileable.findCompaniesByProfiles();
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_FLOWS_BY_PROFILES_ERROR, databaseException);
		}
	}

	public List<Menu> findMenuProfileByIdProfile(final Integer idProfile) throws BusinessException {
		try {
			return this.profileable.findMenuProfileByIdProfile(idProfile);
		} catch (DatabaseException dataBaseException) {
			log.error("Error al obtener datos", dataBaseException);
			throw new BusinessException("Ocurrió un error al obtener datos", dataBaseException);
		}
	}

	public Boolean validateProfileName(final String profileName) throws BusinessException {
		try {
			return this.profileable.validateProfileName(profileName);
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_PROFILE_NAME_DUPLICATE_ERROR, databaseException);
			throw new BusinessException(MESSAGE_PROFILE_NAME_DUPLICATE_ERROR, databaseException);
		}
	}

	public List<Profile> findProfilesByName(final String profileName) throws BusinessException {
		try {
			return this.profileable.findByName(profileName);
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_FIND_PROFILE_BY_NAME_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_PROFILE_BY_NAME_ERROR, databaseException);
		}
	}

	public List<Profile> findProfileCatalogPaged(final Profile profile) throws BusinessException {
		try {
			return this.profileable.findAllProfilesCatalogPaged(profile.getName(), profile.getNumberPage(), Integer
					.parseInt(this.configuration.findByName(ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
		} catch (DatabaseException databaseException) {
			log.error(MESSAGE_FIND_ALL_PROFILE_CATALOG_PAGED_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_ALL_PROFILE_CATALOG_PAGED_ERROR, databaseException);
		}
	}

	public Profile returnTotalPagesShowProfile(final String name) throws NumberFormatException, BusinessException {
		try {
			final Long totalPages = this.profileable.countTotalItemsToShowOfProfiles(name);
			final Profile profile = new Profile();
			profile.setNumberPage(this.configuration.totalPages(totalPages));
			profile.setTotalRows(totalPages.intValue());
			return profile;
		} catch (DatabaseException | NumberFormatException databaseException) {
			log.error(MESSAGE_FIND_TOTAL_PAGES_PROFILE_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_PROFILE_ERROR, databaseException);
		}
	}

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Profile> profileList = this.profileable.findAll();
			return this.getExportProfileMatrix(profileList);
		} catch (DatabaseException dataBaseException) {
			log.error(MESSAGE_EXPORTING_PROFILE_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_EXPORTING_PROFILE_ERROR, dataBaseException);
		}
	}

	private String[][] getExportProfileMatrix(final List<Profile> profileList) {
		final Integer columnsNumber = 3;
		final String[][] dataMatrix = new String[profileList.size() + 1][columnsNumber];
		dataMatrix[0][0] = "IdProfile";
		dataMatrix[0][1] = "Name";
		dataMatrix[0][2] = "Status";
		Integer index = 1;
		for (Profile profile : profileList) {
			dataMatrix[index][0] = profile.getIdProfile().toString();
			dataMatrix[index][1] = profile.getName();
			dataMatrix[index][2] = profile.getStatus().name();
			index++;
		}
		return dataMatrix;
	}

	public boolean validateProfileByToScreens(final List<Profile> profilesList, final Integer idFlow,
			FlowPurchasingEnum flowStatus) throws BusinessException {
		try {
			List<ProfileScreenFlow> screens = this.profileable.findProfileScreenTrayByProfilesAndIdFlow(profilesList, idFlow);
			if (screens == null || screens.isEmpty()) {
				return false;
			}

			screens = screens.stream().filter(s -> s.getFlowStatus().equals(flowStatus.toString()))
					.collect(Collectors.toList());
			if (screens == null || screens.isEmpty()) {
				return false;
			}

			return true;
		} catch (DatabaseException databaseException) {
			log.error("Error al obtener la bandeja por perfil", databaseException);
			throw new BusinessException("Ocurrió un error al obtener la bandeja por pefil", databaseException);
		}
	}
}
