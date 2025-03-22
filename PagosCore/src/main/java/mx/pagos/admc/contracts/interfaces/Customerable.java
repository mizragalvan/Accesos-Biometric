package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Customer;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface Customerable {

    Integer save(Customer customer) throws DatabaseException;
    
    Integer update(Customer customer) throws DatabaseException;
    
    List<Customer> findAll() throws DatabaseException;
    
    void changeStatus(Integer idCustomer, RecordStatusEnum status) throws DatabaseException;
    
    List<Customer> findByStatus(RecordStatusEnum status) throws DatabaseException;
    
    Customer findById(Integer idCustomer) throws DatabaseException;

    List<Customer> findByCompanyNameOrRfc(Customer customer) throws DatabaseException;
    
    List<Customer> findAllCustomerCatalogPaged(RecordStatusEnum status, Integer pagesNumber, Integer itemsNumber)
            throws DatabaseException;
    
    Long countTotalItemsToShowOfCustomer(RecordStatusEnum status) throws DatabaseException;
}
