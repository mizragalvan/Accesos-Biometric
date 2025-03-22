package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Dgable {
	Integer saveOrUpdate(final Dga dga) throws DatabaseException;

	void changeDgaStatus(final Integer idDga, final RecordStatusEnum status) throws DatabaseException;
	
	List<Dga> findAll() throws DatabaseException;
	
	Dga findById(final Integer idDga) throws DatabaseException;
	
	List<Dga> findByStatus(final RecordStatusEnum status) throws DatabaseException;
	
	List<Dga> findAllDgaCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber)
            throws DatabaseException;
    
    Long countTotalItemsToShowOfDga(RecordStatusEnum status) throws DatabaseException;
    
    boolean ExistsDGAByName(final String name) throws DatabaseException;
}
