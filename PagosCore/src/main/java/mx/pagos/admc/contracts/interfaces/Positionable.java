package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Positions;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Positionable {
	
	Integer saveOrUpdate(final Positions positions) throws DatabaseException;
	
	void changePositionStatus(final Integer idPosition, final RecordStatusEnum status) throws DatabaseException;

	List<Positions> findAll() throws DatabaseException;

	List<Positions> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;

	Positions findPositionByIdPosition(Integer idPosition) throws DatabaseException;
	
	List<Positions> findAllPositionCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber) 
            throws DatabaseException;
    
    Long countTotalItemsToShowOfPosition(final RecordStatusEnum status) throws DatabaseException;
}
