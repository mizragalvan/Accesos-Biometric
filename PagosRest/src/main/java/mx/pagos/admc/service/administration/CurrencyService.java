package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.CurrencyBusiness;
import mx.pagos.admc.contracts.structures.Currency;
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
public class CurrencyService {
    @Autowired
    private CurrencyBusiness currencyBusiness;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @Autowired
    private UserSession session; 

    @RequestMapping(value = UrlConstants.CURRENCY_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Currency currency, final HttpServletResponse response) {
        Integer idCurrency = 0;
        try {
            idCurrency = this.currencyBusiness.saveOrUpdate(currency);
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Guardardo de Moneda con número de Id: "  +
                    idCurrency + "", this.session, LogCategoryEnum.INSERT));
            return idCurrency;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idCurrency;
    }

    @RequestMapping(value = UrlConstants.CURRENCY_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeStatus(@RequestBody final Currency currency, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Cambio de estatus de Moneda con Id: "
                    + currency.getIdCurrency() + " y con un estatus actual: " +
                    currency.getStatus(), this.session, LogCategoryEnum.UPDATE));
            this.currencyBusiness.changeStatus(currency.getIdCurrency(), currency.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.CURRENCY_FIN_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Currency> findAll(final HttpServletResponse response) {
        try {
            final ConsultaList<Currency> currencyList = new ConsultaList<Currency>();
            currencyList.setList(this.currencyBusiness.findAll());
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de todos los registros Moneda",
                    this.session, LogCategoryEnum.QUERY));
            return currencyList;            
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Currency>();
    }

    @RequestMapping (value = UrlConstants.CURRENCY_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Currency> findByStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Monedas por estatus: " + 
                    status, this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Currency> currencyList = new ConsultaList<Currency>();
            currencyList.setList(this.currencyBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            return currencyList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Currency>();
    }

    @RequestMapping (value = UrlConstants.CURRENCY_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Currency findById(@RequestBody final Currency currency, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Moneda con el id: " + 
                    currency.getIdCurrency(), this.session, LogCategoryEnum.QUERY));
            return this.currencyBusiness.findById(currency.getIdCurrency());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Currency();
    }
    
    @RequestMapping (value = UrlConstants.CURRENCY_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Currency> findAllCurrencyCatalogPaged(@RequestBody final Currency currency, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos las monedas", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Currency> currencyList = new ConsultaList<>();
            currencyList.setList(this.currencyBusiness.findCurrencyCatalogPaged(currency));
            return currencyList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Currency>();
    }

    @RequestMapping (value = UrlConstants.CURRENCY_FIND_NUMBERS_PAGE, method = RequestMethod.POST)
    @ResponseBody
    public final Currency returnTotalRowsOfCurrency(@RequestBody final Currency currency, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de monedas", 
                    this.session, LogCategoryEnum.QUERY));
            return this.currencyBusiness.returnTotalPagesShowCurrency(currency.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Currency();
    }
}
