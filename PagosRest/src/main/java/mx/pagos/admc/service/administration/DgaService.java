package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.DgaBusiness;
import mx.pagos.admc.contracts.structures.Dga;
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
public class DgaService {
    private static final Logger LOG = Logger.getLogger(DgaService.class);
    
    @Autowired
    private DgaBusiness dgaBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @RequestMapping(value = UrlConstants.DGA_SAVEORUPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Dga dga, final HttpServletResponse response) {
        Integer idDga = 0;
        try {
            idDga = this.dgaBusiness.saveOrUpdate(dga);
            LOG.debug("Guardado de dga exitoso" + idDga);
            return idDga;                   
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return idDga;
    }
    
    @RequestMapping(value = UrlConstants.DGA_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeDgaStatus(@RequestBody final Dga dga, final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por Dga " +  dga.getIdDga() + " y Estatus "
                    + dga.getStatus());
            this.dgaBusiness.changeDgaStatus(dga.getIdDga(), dga.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }
    
    @RequestMapping (value = UrlConstants.DGA_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Dga> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Dgas");
            final ConsultaList<Dga> listReturn = new ConsultaList<Dga>();
            listReturn.setList(this.dgaBusiness.findAll());
            return listReturn;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Dga>();
    }
    
    @RequestMapping (value = UrlConstants.DGA_FIND_ACTIVE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Dga> findActive(final HttpServletResponse response) {
        try {
            LOG.debug("Se van a obtener las Dgas activas");
            final ConsultaList<Dga> listReturn = new ConsultaList<Dga>();
            listReturn.setList(this.dgaBusiness.findByStatus(RecordStatusEnum.ACTIVE));
            return listReturn;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Dga>();
    }
    
    /**
     * @param status
     * @param response
     * @return
     */
    @RequestMapping (value = UrlConstants.DGA_FIND_BY_STATUS , method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Dga> findByStatus(@RequestBody final String status, final HttpServletResponse response) {
        try {
        	
            LOG.debug("Se va a consultar por estatus" + status);
            final ConsultaList<Dga> listReturn = new ConsultaList<Dga>();
            listReturn.setList(this.dgaBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            return listReturn;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Dga>();
    }
    
    @RequestMapping (value = UrlConstants.DGA_FIND_BY_ID , method = RequestMethod.POST)
    @ResponseBody
    public final Dga findById(@RequestBody final Dga dga, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por id" + dga.getIdDga());
            return this.dgaBusiness.findById(dga.getIdDga());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Dga();
    }
    
    @RequestMapping (value = UrlConstants.DGA_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Dga> findAllDgaCatalogPaged(@RequestBody final Dga dga, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los dgas", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Dga> dgaList = new ConsultaList<>();
            dgaList.setList(this.dgaBusiness.findDgaCatalogPaged(dga));
            return dgaList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Dga>();
    }

    @RequestMapping (value = UrlConstants.DGA_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final Dga returnTotalRowsOfDga(@RequestBody final Dga dga, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de dgas", this.session, LogCategoryEnum.QUERY));
            return this.dgaBusiness.returnTotalPagesShowDga(dga.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Dga();
    }
}
