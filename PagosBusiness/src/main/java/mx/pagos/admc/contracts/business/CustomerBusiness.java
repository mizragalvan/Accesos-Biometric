package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Customerable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Customer;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("CustomerBusiness")
public class CustomerBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(CustomerBusiness.class);
    private static final String ERROR_MESSAGE_SAVE_OR_UPDATE = "Hubo un error al guardar los datos del cliente";
    private static final String ERROR_MESSAGE_FIND_ALL = "Hubo un error al buscar todos los clientes";
    private static final String ERROR_MESSAGE_CHANGE_STATUS = "Hubo un error al cambiar el estatus del cliente";
    private static final String ERROR_MESSAGE_FIND_BY_ID = "Hubo un error al buscar cliente por id";
    private static final String ERROR_MESSAGE_FIND_BY_STATUS = "Hubo un error al buscar clientes por estatus";
    private static final String ERROR_MESSAGE_FIND_BY_COMPANY_NAME_OR_RFC =
            "Hubo un error al buscar clientes por nombre o RFC";
    private static final String MESSAGE_EXPORTING_CUSTOMER_ERROR =
            "Hubo un problema al exportar el catálogo de clientes";
    private static final String MESSAGE_FIND_ALL_CUSTOMER_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar clientes paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_CUSTOMER_ERROR = 
            "Hubo un problema al buscar número de pagínas de clientes";
    
    @Autowired
    private Customerable customerable;
    
    @Autowired
    private ConfigurationsBusiness configuration;

    public final Integer saveOrUpdate(final Customer customer) throws BusinessException {
        try {
            if (customer.getIdCustomer() == null)
                return this.customerable.save(customer);
            else
                return this.customerable.update(customer);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_SAVE_OR_UPDATE, databaseException);
            throw new BusinessException(ERROR_MESSAGE_SAVE_OR_UPDATE, databaseException);
        }
    }
    
    public final List<Customer> findAll() throws BusinessException {
        try {
            return this.customerable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_ALL, databaseException);
            throw new BusinessException(ERROR_MESSAGE_FIND_ALL, databaseException);
        }
    }
    
    public final void changeStatus(final Integer idCustomer, final RecordStatusEnum status) throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.customerable.changeStatus(idCustomer, RecordStatusEnum.INACTIVE);
            else
                this.customerable.changeStatus(idCustomer, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_CHANGE_STATUS, databaseException);
            throw new BusinessException(ERROR_MESSAGE_CHANGE_STATUS, databaseException);
        }
    }
    
    public final List<Customer> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.customerable.findByStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_BY_STATUS, databaseException);
            throw new BusinessException(ERROR_MESSAGE_FIND_BY_STATUS, databaseException);
        }
    }
    
    public final Customer findById(final Integer idCustomer) throws BusinessException {
        try {
            return this.customerable.findById(idCustomer);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_BY_ID, databaseException);
            throw new BusinessException(ERROR_MESSAGE_FIND_BY_ID, databaseException);
        }
    }
    
    public final List<Customer> findByCompanyNameOrRfc(final Customer customer) throws BusinessException {
        try {
            return this.customerable.findByCompanyNameOrRfc(customer);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_BY_COMPANY_NAME_OR_RFC, databaseException);
            throw new BusinessException(ERROR_MESSAGE_FIND_BY_COMPANY_NAME_OR_RFC, databaseException);
        }
    }
    
    public final List<Customer> findCustomerCatalogPaged(final Customer customer) throws BusinessException {
        try {
            return this.customerable.findAllCustomerCatalogPaged(customer.getStatus(), customer.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_CUSTOMER_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_CUSTOMER_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Customer returnTotalPagesShowCustomer(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.customerable.countTotalItemsToShowOfCustomer(status);
            final Customer customer = new Customer();
            customer.setNumberPage(this.configuration.totalPages(totalPages));
            customer.setTotalRows(totalPages.intValue());
            return customer;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_CUSTOMER_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_CUSTOMER_ERROR, databaseException);
        }
    }

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Customer> customerList = this.customerable.findAll();
	        return this.getExportCustomerMatrix(customerList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_CUSTOMER_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportCustomerMatrix(final List<Customer> customerListParameter) {
        final Integer columnsNumber = 4;
        final String[][] dataMatrix = new String[customerListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdCustomer";
        dataMatrix[0][1] = "CompanyName";
        dataMatrix[0][2] = "Rfc";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "Status";
        Integer index = 1;
        
        for (Customer customer : customerListParameter) {
            dataMatrix[index][0] = customer.getIdCustomer().toString();
            dataMatrix[index][1] = customer.getCompanyName();
            dataMatrix[index][2] = customer.getRfc();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = customer.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
