package mx.pagos.admc.contracts.interfaces.digitalsignature;

import mx.pagos.admc.contracts.structures.digitalsignature.UserInformation;
import mx.pagos.general.exceptions.DatabaseException;

public interface UserInformatable {
	
	void save(UserInformation userInformation) throws DatabaseException;

	void update(UserInformation userInformation) throws DatabaseException;

	UserInformation findByIdUser(Integer idUser) throws DatabaseException;

}
