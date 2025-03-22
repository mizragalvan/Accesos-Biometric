package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Powerable {
	Integer saveOrUpdate(final Power power) throws DatabaseException;

	void changePowerStatus(final Integer idPower, final RecordStatusEnum status) throws DatabaseException;
	
	List<Power> findAll() throws DatabaseException;
	
	List<Power> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
	
	Power findByIdPower(final Integer idPower) throws DatabaseException;

	List<Power> findByIdFinancialEntity(Integer idFinancialEntity) throws DatabaseException;

	List<Power> findByIdFinancialEntityAndIdLegalRepresentative(Integer idFinancialEntity,
			Integer idLegalRepresentative) throws DatabaseException;
	
	List<Power> findAllPowerCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber)
	        throws DatabaseException;
    
    Long countTotalItemsToShowOfPower(RecordStatusEnum status) throws DatabaseException;
}
