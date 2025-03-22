package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FolioByPlantilla;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.admc.contracts.structures.TypeByAnexo;
import mx.pagos.general.exceptions.DatabaseException;

public interface Anexable {
	
	Integer save(TypeByAnexo anexo) throws DatabaseException;
	
	List<TypeByAnexo> findTypesByAnexosAndPerson(final Integer idDocumentType, boolean moral, int folio)throws DatabaseException;

	List<TypeByAnexo> findTypesByAnexosAndPersonOrderPage(TypeByAnexo anexo) throws DatabaseException;
	
	void deleteTypeByAnexo(final Integer idTypeByAnexo) throws DatabaseException;
	
	Integer UpdateTypesByAnexos(TypeByAnexo anexo) throws DatabaseException;

	List<FolioByPlantilla> findFolioByDocumentType(Integer idDocumentType) throws DatabaseException;

	List<TypeByAnexo> findTypesByFolio(Integer folio) throws DatabaseException;

	List<TagField> findTagsAnexos() throws DatabaseException;
}
