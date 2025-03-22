package mx.pagos.security.interfaces;

import java.util.List;

import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.LoginEnum;
import mx.pagos.admc.enums.security.UserStatusEnum;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.Profile;
import mx.pagos.security.structures.User;

public interface Userable {

	String findLoginType() throws DatabaseException;

	User findByUserId(final Integer idUser) throws DatabaseException, EmptyResultException; 

	Integer saveOrUpdate(final User user) throws DatabaseException;

	void changeUserStatus(final Integer idUser, final UserStatusEnum status) throws DatabaseException;

	List<User> findAll() throws DatabaseException;

	List<User> findByStatus(final UserStatusEnum status) throws DatabaseException;

	Integer getCountCorrespondPasswordByUsername(final String username, final String password) throws DatabaseException;

	User findByUsername(final String username) throws DatabaseException, EmptyResultException;
	
	List<User> findGerente(int requisition) throws DatabaseException;

	User findByUsernameForLogin(final String username) throws DatabaseException, EmptyResultException;

	void saveProfileByIdUser(final Integer idUser, Integer idProfile) throws DatabaseException;

	void deleteProfilesListByIdUser(final Integer idUser) throws DatabaseException;

	List<Profile> findProfilesByIdUser(final Integer idUser) throws DatabaseException;

	List<User> findUsersByName(String name,String apaterno,String amaterno) throws DatabaseException;
	
	List<User> findLawyersByIdUser() throws DatabaseException;
	
	List<User> findUserMailAddress(String req) throws DatabaseException;
	
	String findLawyerNameByIdRequisition(String req) throws DatabaseException;
	
	Boolean saveChangesLawyer(final ConsultaList req) throws DatabaseException;

	List<User> findUsersByNameOrLastNameAndIdRequisition(final String name, Integer idRequisition, final Integer idFlow)
			throws DatabaseException;

	Integer updatePassword(final User user) throws DatabaseException;

	User findUserByEmail(final String email) throws DatabaseException, EmptyResultException;

	List<User> findUsersByArea(final Integer idArea) throws DatabaseException;

	Boolean isProfileUserFiltered(final Integer idUser, final Integer idFlow, final FlowPurchasingEnum status) 
			throws DatabaseException;

	void registerRetry(final Integer idUser, final Integer retryNumber) throws DatabaseException;

	void registerLoginStatus(final Integer idUser, final LoginEnum statusLogin) throws DatabaseException;

	String findUrlLdap() throws DatabaseException, EmptyResultException;

	String findBaseLdap() throws DatabaseException, EmptyResultException;

	String findUserLdap() throws DatabaseException, EmptyResultException;

	String findPasswordLdap() throws DatabaseException, EmptyResultException;

	Boolean isBlockedUser(String username) throws DatabaseException;

	List<User> findAllUsersCatalogPaged(String name,String apaterno,String amaterno, Integer pagesNumber, Integer itemsNumber)
			throws DatabaseException;

	Long countTotalItemsToShowOfUser(String name,String apaterno,String amaterno) throws DatabaseException;

	List<Integer> findAreasRepoByUserId(final Integer idUser) throws DatabaseException, EmptyResultException; 

	void saveAreasRepoByIdUser(final Integer idUser, Integer idArea) throws DatabaseException;

	void deleteAreasListByIdUser(final Integer idUser) throws DatabaseException;
}
