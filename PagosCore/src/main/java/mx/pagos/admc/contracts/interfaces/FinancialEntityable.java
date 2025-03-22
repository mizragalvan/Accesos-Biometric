package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.FinancialEntitieCombination;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface FinancialEntityable {
	Integer saveOrUpdate(final FinancialEntity financialEntity) throws DatabaseException;

	void changeFinancialEntityStatus(final Integer idFinancialEntity, final RecordStatusEnum status)
			throws DatabaseException;
	
	List<FinancialEntity> findAll() throws DatabaseException;
	
	List<FinancialEntity> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
	
	FinancialEntity findByIdFinancialEntity(final Integer idFinancialEntity) throws DatabaseException;
	
	List<FinancialEntitieCombination> findFinancialEntityCombinationDistinctByCombinationName() 
			throws DatabaseException;

	Boolean findIsCombination(Integer idRequisition, String combinationName) throws DatabaseException;

	List<FinancialEntity> findFinancialEntityByIdRequisition(Integer idRequisition) throws DatabaseException;

	String findDefaultCombinationName() throws DatabaseException;

    List<FinancialEntity> findAllFinancialEntity() throws DatabaseException;
    
    void updateDraftFields(FinancialEntity financialEntity) throws DatabaseException;
    
    List<FinancialEntity> findAllFinancialEntityCatalogPaged(
    		final FinancialEntity financialEntity, Integer pagesNumber, Integer itemsNumber) throws DatabaseException;
    
    Long countTotalItemsToShowOfFinancialEntity(FinancialEntity financialEntity) throws DatabaseException;

	List<FinancialEntity> findDataFinancialentityByRequisitionQuery(Integer idRequisition) throws DatabaseException;
}
