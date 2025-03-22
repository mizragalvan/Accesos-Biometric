package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.general.exceptions.DatabaseException;

public interface CatalogDocumentType {

	List<CatDocumentType> findAll() throws DatabaseException;;
}
