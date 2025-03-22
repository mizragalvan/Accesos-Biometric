package mx.pagos.admc.contracts.interfaces.digitalsignature;

import java.util.List;

import mx.pagos.admc.contracts.structures.digitalsignature.ContactDS;
import mx.pagos.admc.util.shared.Page;
import mx.pagos.general.exceptions.DatabaseException;

public interface Contactable {
	void save(ContactDS contactDS) throws DatabaseException;

	void update(ContactDS contactDS) throws DatabaseException;

	ContactDS findById(Integer idContact) throws DatabaseException;
	
	void deleteById(Integer idContact) throws DatabaseException;

	List<ContactDS> getAllContacts(Integer idUser) throws DatabaseException;

	Page<ContactDS> getContacts(Integer idUser, Page<ContactDS> request) throws DatabaseException;

}