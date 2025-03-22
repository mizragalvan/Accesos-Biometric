package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.contracts.structures.Guarantees;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Guaranteesable {
	
	Integer saveOrUpdate(final Guarantees guarantees) throws DatabaseException;
	
	void changeGuaranteesStatus(Integer idguarantess, RecordStatusEnum status) throws DatabaseException;
	
	List<Guarantees> findAll() throws DatabaseException;
	
	Guarantees findById(Integer idguarantess) throws DatabaseException;
	
	List<Guarantees> findByStatus(RecordStatusEnum statusEnum) throws DatabaseException;
	
	List<CheckDocument> findCheckDocumentListByIdGuarantee(Integer idGuarantee) throws DatabaseException;
	
	List<Guarantees> findAllGuaranteesCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber)
            throws DatabaseException;
    
    Long countTotalItemsToShowOfGuarantees(RecordStatusEnum status) throws DatabaseException;
}
