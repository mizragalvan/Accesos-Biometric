package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.CustomerBusiness;
import mx.pagos.admc.contracts.structures.Customer;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class CustomerService {
    @Autowired
    private CustomerBusiness customerBusiness;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @Autowired
    private UserSession session; 

    @RequestMapping(value = UrlConstants.CUSTOMER_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Customer customer, final HttpServletResponse response) {
        Integer idCustomer = 0;
        try {
            idCustomer = this.customerBusiness.saveOrUpdate(customer);
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Guardardo de Cliente con número de Id: "  +  idCustomer, 
                    this.session, LogCategoryEnum.INSERT));
            return idCustomer;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idCustomer;
    }

    @RequestMapping (value = UrlConstants.CUSTOMER_FIN_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Customer> findAll(final HttpServletResponse response) {
        final ConsultaList<Customer> customerList = new ConsultaList<Customer>();
        try {
            customerList.setList(this.customerBusiness.findAll());
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los registros de Clientes", this.session, LogCategoryEnum.QUERY));
            return customerList;            
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return customerList;
    }
    
    @RequestMapping(value = UrlConstants.CUSTOMER_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeStatus(@RequestBody final Customer customer, final HttpServletResponse response) {
        try {
            this.customerBusiness.changeStatus(customer.getIdCustomer(), customer.getStatus());
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Cambio de estatus de Cliente con Id: "
                    + customer.getIdCustomer() + " y con un estatus actual: " +
                    customer.getStatus(), this.session, LogCategoryEnum.UPDATE));
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.CUSTOMER_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Customer> findByStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        final ConsultaList<Customer> customerList = new ConsultaList<Customer>();
        try {
            customerList.setList(this.customerBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Clientes por estatus: " + 
                    status, this.session, LogCategoryEnum.QUERY));
            return customerList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return customerList;
    }

    @RequestMapping (value = UrlConstants.CUSTOMER_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Customer findById(@RequestBody final String idCurrency, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Cliente con el id: " + 
                    idCurrency, this.session, LogCategoryEnum.QUERY));
            return this.customerBusiness.findById(Integer.valueOf(idCurrency));
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Customer();
    }
    
    @RequestMapping (value = UrlConstants.CUSTOMER_FIND_BY_COMPANY_NAME_OR_RFC, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Customer> findByCompanyNameOrRfc(@RequestBody final Customer customer,
            final HttpServletResponse response) {
        final ConsultaList<Customer> customerList = new ConsultaList<Customer>();
        try {
            customerList.setList(this.customerBusiness.findByCompanyNameOrRfc(customer));
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Cliente con el Nombre: " + 
                    customer.getCompanyName() + " y RFC: " + customer.getRfc(), this.session, LogCategoryEnum.QUERY));
            return customerList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return customerList;
    }
    
    @RequestMapping (value = UrlConstants.CUSTOMER_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Customer> findAllCustomerCatalogPaged(@RequestBody final Customer customer, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los clientes", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Customer> customerList = new ConsultaList<>();
            customerList.setList(this.customerBusiness.findCustomerCatalogPaged(customer));
            return customerList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Customer>();
    }

    @RequestMapping (value = UrlConstants.CUSTOMER_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final Customer returnTotalRowsOfCustomer(
            @RequestBody final Customer customer, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de clientes", this.session, LogCategoryEnum.QUERY));
            return this.customerBusiness.returnTotalPagesShowCustomer(customer.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Customer();
    }
}
