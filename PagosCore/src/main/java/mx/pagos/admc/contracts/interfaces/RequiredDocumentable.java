package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * Interfaz que contiene los métodos paara interactuar con los DAos de documentos requeridos
 * 
 * @author Mizraim
 * 
 * @see RequiredDocument
 * @see RecordStatusEnum
 * @see DatabaseException
 *
 */
public interface RequiredDocumentable {

	Integer saveOrUpdate(RequiredDocument requiredDocument) throws DatabaseException;

	void changeRequiredDocumentStatus(Integer idRequiredDocument, RecordStatusEnum status) throws DatabaseException;

	List<RequiredDocument> findAll() throws DatabaseException;

	RequiredDocument findByIdRequiredDocument(Integer idRequiredDocument)
			throws DatabaseException, EmptyResultException;

	List<RequiredDocument> findByStatus(RecordStatusEnum status) throws DatabaseException;

	void savePersonalityRequiredDocument(Integer idPersonality, Integer idRequiredDocument) throws DatabaseException;

	void deletePersonalityRequiredDocument(Integer idRequiredDocument) throws DatabaseException;

	List<RequiredDocument> findAllRequiredDocumentWithPersonalities() throws DatabaseException;

	List<RequiredDocument> findAllRequiredDocumentCatalogPaged(
			RequiredDocument requiredDocument, Integer pagesNumber, Integer itemsNumber) throws DatabaseException;

	Long countTotalItemsToShowOfRequiredDocument(RequiredDocument requiredDocument) throws DatabaseException;

	Integer findIdByName(String name) throws DatabaseException;
}
