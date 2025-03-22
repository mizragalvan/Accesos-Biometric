package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Category;
import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Categoriable {
	
	Integer saveOrUpdate(final Category category) throws DatabaseException;

	void changeCategoryStatus(final Integer idCategory, final RecordStatusEnum status) 
			throws DatabaseException;
	
	List<Category> findAll() throws DatabaseException;
	
	Category findById(Integer idCategory) throws DatabaseException;
	
	List<Category> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
	
    Integer saveCategoryCheckDocumentation(Integer idCheckDocumentation, Integer idCategory) throws DatabaseException;
	
	void deleteCategoryCheckDocumentation(Integer idCategory) throws DatabaseException;
	
	List<Integer> findCheckDocumentationIdsByCategory(Integer idCategory) throws DatabaseException;
	
    List<CheckDocumentation> findOwnerCheckDocumentationByCategory(Integer idCategory) throws DatabaseException;
    
    void deleteRequisitionOwnerCheckDocumentation(Integer idCheckDocumentation, Integer idCategory) 
            throws DatabaseException;
    
    List<Category> findAllCategoriesWithCheckDocumentation() throws DatabaseException;
    
    List<Category> findAllCategoryCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber)
            throws DatabaseException;
    
    Long countTotalItemsToShowOfCategory(RecordStatusEnum status) throws DatabaseException;
}
