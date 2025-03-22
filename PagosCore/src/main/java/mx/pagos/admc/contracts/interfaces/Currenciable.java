package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Currency;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface Currenciable {
    Integer saveOrUpdate(Currency currency) throws DatabaseException;
    
    Currency findById(Integer idCurrency) throws DatabaseException, EmptyResultException;
    
    List<Currency> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    List<Currency> findAll() throws DatabaseException;
    
    void changeStatus(Integer idCurrency, RecordStatusEnum status) throws DatabaseException;
    
    List<Currency> findAllCurrencyCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber)
            throws DatabaseException;
    
    Long countTotalItemsToShowOfPower(RecordStatusEnum status) throws DatabaseException;
}
