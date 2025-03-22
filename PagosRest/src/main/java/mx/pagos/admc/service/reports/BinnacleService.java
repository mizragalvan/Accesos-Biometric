package mx.pagos.admc.service.reports;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.*;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.logs.structures.Binnacle;

@Controller
public class BinnacleService {
    private static final Logger LOG = Logger.getLogger(BinnacleService.class);

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @RequestMapping(value = UrlConstants.BINNACLE_SAVE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer save(@RequestBody final Binnacle binnacle, final HttpServletResponse response) {
        Integer idBinnacle = 0;
        try {
            idBinnacle = this.binnacleBusiness.save(binnacle);
            LOG.debug("Guardado de Entidad LOG exitoso " + binnacle);
            return idBinnacle;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idBinnacle;
    }

    @RequestMapping(value = UrlConstants.BINNACLE_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se obtedrá la lista de LOGs");
            final ConsultaList<Binnacle> binnacleList = new ConsultaList<>();
            binnacleList.setList(this.binnacleBusiness.findAll());
            return binnacleList;        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
        return new ConsultaList<Binnacle>();
    }

    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_USER, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findByUser(@RequestBody final Integer idUser,
            final HttpServletResponse response) {
        try {
            LOG.debug("Se obtedrá la lista de LOGs por Usuario " + idUser.toString());
            final ConsultaList<Binnacle> binnacleList = new ConsultaList<>();
            binnacleList.setList(this.binnacleBusiness.findByIdUser(idUser));
            return binnacleList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Binnacle>();
    }

    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_DATE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findByDate(@RequestBody final ConsultaList<Binnacle> parameters, 
            final HttpServletResponse response) {
        try {
            final ConsultaList<Binnacle> listResponse = new ConsultaList<>();
            LOG.info("Se obtedrá LOGs por rango de fecha de " +
                    parameters.getParam1() + " a " + parameters.getParam2());
            listResponse.setList(this.binnacleBusiness.findByDate(parameters.getParam1(), parameters.getParam1()));
            return listResponse;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage()); 
            return null;
        }
    }

    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Binnacle findById(@RequestBody final Integer idBinnacle, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se obtedrá LOG por Id" + idBinnacle.toString());
            return this.binnacleBusiness.findByIdBinnacle(idBinnacle);
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Binnacle();
    }
    
    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_ID_FLOW, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findByIdFlow(@RequestBody final Integer idFlow, 
            final HttpServletResponse response) {
        final ConsultaList<Binnacle> listResponse = new ConsultaList<>();
        listResponse.setList(new ArrayList<Binnacle>());
        try {
            LOG.debug("Se obtedrá LOG por Id flujo" + idFlow.toString());
            listResponse.setList(this.binnacleBusiness.findByIdFlow(idFlow));
            return listResponse;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return listResponse;
    }
    
    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_ID_CATEGORY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findByLogCategory(@RequestBody final String logCategoryString, 
            final HttpServletResponse response) {
        final ConsultaList<Binnacle> listResponse = new ConsultaList<>();
        listResponse.setList(new ArrayList<Binnacle>());
        try {
            LOG.debug("Se obtedrá LOG por categoría: " + logCategoryString);
            listResponse.setList(this.binnacleBusiness.findByLogCategory(LogCategoryEnum.valueOf(logCategoryString)));
            return listResponse;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return listResponse;
    }

    @RequestMapping (value = UrlConstants.BINNACLE_DELETE_BY_DATES_RANGE, method = RequestMethod.POST)
    @ResponseBody
    public final void deleteByDatesRange(@RequestBody final ConsultaList<Binnacle> binnacle,
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.deleteByDatesRange(binnacle.getParam1(), 
                    binnacle.getParam2(), binnacle.getList().get(0).getLogList());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
    }
    
    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_LOG_CATEGORY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findByLogCategoryTypes(@RequestBody final Binnacle binnacle,
            final HttpServletResponse response) {
        final ConsultaList<Binnacle> binnacleList = new ConsultaList<>();
        try {
            binnacleList.setList(this.binnacleBusiness.findByLogCategoryListDatesAndUser(binnacle));
            return binnacleList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return binnacleList;
    }
    
    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_LOG_CATEGORY_PAGINATED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Binnacle> findByLogCategoryTypesPaginated(
    		@RequestBody final ConsultaList<Binnacle> parameters, final HttpServletResponse response) {
    	final ConsultaList<Binnacle> binnacleList = new ConsultaList<>();
    	try {
    		binnacleList.setList(this.binnacleBusiness.findByLogCategoryTypesPaginated(
    				parameters.getList().get(0), parameters.getParam4()));
    		return binnacleList;
    	} catch (BusinessException businessException) {
    		response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
    		response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
    	}
    	return binnacleList;
    }
    
    @RequestMapping (value = UrlConstants.BINNACLE_FIND_BY_LOG_CATEGORY_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final Integer findByLogCategoryTypesPaginatedTotalPages(
    		@RequestBody final Binnacle binnacle, final HttpServletResponse response) {
        try {
        	return this.binnacleBusiness.findByLogCategoryTypesPaginatedTotalPages(binnacle);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
            return null;
        }
    }
}
