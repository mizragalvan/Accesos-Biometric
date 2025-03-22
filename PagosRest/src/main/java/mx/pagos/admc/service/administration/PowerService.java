package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.PowersBusiness;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */

@Controller
public class PowerService {
    private static final Logger LOG = Logger.getLogger(PowerService.class);

    @Autowired
    private PowersBusiness powerBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @RequestMapping(value = UrlConstants.POWER_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Power power, final HttpServletResponse response) {
        Integer idPower = 0;     
        try {
            idPower = this.powerBusiness.saveOrUpdate(power);
            LOG.debug("Guardado de Poder exitoso " + idPower);
            return idPower;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idPower;
    }

    @RequestMapping(value = UrlConstants.CHANGE_POWER_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changePowerStatus(@RequestBody final Power power, final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por id de Poder " + power.getIdPower() + " y Estatus " + power.getStatus());
            this.powerBusiness.changePowerStatus(power.getIdPower(), power.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.POWER_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Power> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener una lista de Poderes");
            final ConsultaList<Power> powerList = new ConsultaList<Power>();
            powerList.setList(this.powerBusiness.findAll());
            return powerList;           
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Power>();
    }

    @RequestMapping (value = UrlConstants.POWER_FIND_BY_RECORD_STATUS , method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Power> findByRecordStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por estatus" + status);
            final ConsultaList<Power> powerList = new ConsultaList<Power>();
            powerList.setList(this.powerBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return powerList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Power>();
    }

    @RequestMapping (value = UrlConstants.POWER_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Power findByIdPower(@RequestBody final Power power, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por id" + power.getIdPower());
            return this.powerBusiness.findByIdPower(power.getIdPower());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Power();
    }
    
    @RequestMapping (value = UrlConstants.POWERS_FIND_BY_ID_FINANCIAL_ENTITY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Power> findByIdFinancialEntity(@RequestBody final Integer idFinancialEntity,
            final HttpServletResponse response) {
        try {
            final ConsultaList<Power> powerList = new ConsultaList<Power>();
            powerList.setList(this.powerBusiness.findByIdFinancialEntity(idFinancialEntity));
            return powerList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Power>();
    }
    
    @RequestMapping (value = UrlConstants.POWERS_FIND_BY_ID_FINANCIAL_ENTITY_AND_ID_LEGAL_REPRESENTATIVE, 
    		method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Power> findByIdFinancialEntityAndIdLegalRepresentative(
    		@RequestBody final ConsultaList<Power> params, final HttpServletResponse response) {
        try {
            final ConsultaList<Power> powerList = new ConsultaList<Power>();
            powerList.setList(this.powerBusiness.findByIdFinancialEntityAndIdLegalRepresentative(
            		Integer.parseInt(params.getParam1()), Integer.parseInt(params.getParam2())));
            return powerList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Power>();
    }
    
    @RequestMapping (value = UrlConstants.POWERS_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Power> findAllPowerCatalogPaged(@RequestBody final Power power, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los poderes", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Power> powerList = new ConsultaList<>();
            powerList.setList(this.powerBusiness.findPowerCatalogPaged(power));
            return powerList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Power>();
    }

    @RequestMapping (value = UrlConstants.POWERS_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final Power returnTotalRowsOfPower(@RequestBody final Power power, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de poderes", 
                    this.session, LogCategoryEnum.QUERY));
            return this.powerBusiness.returnTotalPagesShowPower(power.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Power();
    }
}
