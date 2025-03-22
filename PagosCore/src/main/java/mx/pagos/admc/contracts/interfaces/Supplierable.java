package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Personality;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface Supplierable {

	Integer saveOrUpdate(Supplier supplier) throws DatabaseException;

	void changeSupplierStatus(Integer idSupplier, RecordStatusEnum status) throws DatabaseException;

	List<Supplier> findAll() throws DatabaseException;

	List<Supplier> findByRecordStatus(RecordStatusEnum status) throws DatabaseException;

	Supplier findById(final Integer idSupplier) throws DatabaseException, EmptyResultException;

	List<Supplier> findByNameAndRfc(String name, String rfc) throws DatabaseException;

	Supplier findByRfc(String rfc) throws DatabaseException, EmptyResultException;

	void saveRequiredDocument(Integer idSupplier, Integer idRequiredDocuent, Integer idDocument) 
			throws DatabaseException;   

	List<RequiredDocument> findRequiredDocumentsByIdSupplier(Integer idSupplier) throws DatabaseException;

	void deleteSupplierRequiredDocument(Integer idSupplier, final Integer idRequiredDocument) throws DatabaseException;

	void updateDraftSupplierFields(Supplier supplier) throws DatabaseException;

	Boolean isCompanyNameExist(String supplierCompanyName) throws DatabaseException;

	Boolean existRFC(String rfc) throws DatabaseException;

	List<Supplier> findAllSupplierCatalogPaged(final Supplier supplier, Integer pagesNumber, Integer itemsNumber)
			throws DatabaseException;

	Long countTotalItemsToShowOfSupplier(final Supplier supplier) throws DatabaseException;

	Personality findPersonality(Integer idSupplier) throws DatabaseException;
}
