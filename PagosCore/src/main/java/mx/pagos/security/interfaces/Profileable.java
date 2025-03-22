package mx.pagos.security.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Company;
import mx.pagos.admc.contracts.structures.Flow;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.Menu;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.ProfileScreenFlow;

/**
 *
 * Interfaz que contiene los métodos con los que se interactuará con los DAOs de perfiles de usuario
 *
 */
public interface Profileable {

	Integer saveOrUpdate(final Profile profile) throws DatabaseException;

	void changeProfileStatus(final Integer idProfile, final RecordStatusEnum status) throws DatabaseException;
	
	Profile findById(final Integer idProfile)  throws DatabaseException;
	
	List<Profile> findAll() throws DatabaseException;
	
	List<Profile> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
	
	void saveNewMenuProfile(final Integer idProfile, final String menuItem) throws DatabaseException;
	
	List<Menu> findMenuProfileByIdProfileList(final List<Profile> profileList) throws DatabaseException;
	
	void deleteMenuProfileByIdProfile(final Integer idProfile) throws DatabaseException;
	
	void saveProfileScreenFlow(ProfileScreenFlow profileScreenFlow) throws DatabaseException;
    
    List<ProfileScreenFlow> findProfileScreenTrayByProfilesAndIdFlow(final List<Profile> profiles, Integer idFlow)
            throws DatabaseException;        
    
    void deleteProfileScreenFlowByIdProfile(final Integer idProfile) throws DatabaseException;

	void deleteById(Integer idProfile)throws DatabaseException;

	List<Menu> findMenuProfileByIdProfile(Integer idProfile)throws DatabaseException;

    List<Flow> findFlowsByProfiles(List<Profile> profilesList) throws DatabaseException;
    
    List<Unit> findUnitsByProfiles(List<Company> idFlow) throws DatabaseException;
    
    List<Company> findCompaniesByProfiles() throws DatabaseException;
    
    Boolean validateProfileName(String profileName) throws DatabaseException; 
    
    List<Profile> findByName(String profileName) throws DatabaseException;
    
    List<Profile> findAllProfilesCatalogPaged(String name, Integer pagesNumber, Integer itemsNumber)
            throws DatabaseException;
    
    Long countTotalItemsToShowOfProfiles(String name) throws DatabaseException;
}
