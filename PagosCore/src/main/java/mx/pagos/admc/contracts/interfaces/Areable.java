package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Area;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Areable {
	Integer saveOrUpdate(final Area area) throws DatabaseException;

	void changeAreaStatus(final Integer idArea, final RecordStatusEnum status) throws DatabaseException;
	
	List<Area> findAll() throws DatabaseException;
	
	Area findById(final Integer idArea) throws DatabaseException;
	
	List<Area> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;

	List<Area> findByIdRequisition(Integer idRequisition) throws DatabaseException;
	
	List<Area> findAllAreasCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber) 
	        throws DatabaseException;
	
	Long countTotalItemsToShowOfAreas(final RecordStatusEnum status) throws DatabaseException;
}
