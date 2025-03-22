package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.ProfileScreenFlow;


public interface ProfileScreenFlowable {

	List<ProfileScreenFlow> findFlowScreenActionByProfile(ProfileScreenFlow bean)
			throws DatabaseException;

	void saveProfileScreenFlow(ProfileScreenFlow profileScreen)
			throws DatabaseException;

	void deleteProfileScreenFlowByIdProfile(Integer idProfile)
			throws DatabaseException;
	
}
