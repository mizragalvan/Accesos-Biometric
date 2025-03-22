package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.AreasBusiness;
import mx.pagos.admc.contracts.structures.Area;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Mizraim
 */

@Controller
public class AreaService {
	private static final Logger LOG = Logger.getLogger(AreaService.class);
		
	@Autowired
    private AreasBusiness areasBusiness;
	
	@RequestMapping(value = UrlConstants.AREA_SAVE_OR_UPDATE, method = RequestMethod.POST)
	@ResponseBody
	public final Integer saveOrUpdate(@RequestBody final Area area,
	        final HttpServletResponse response) {
	    Integer idArea = 0;	    
		try {
		    idArea = this.areasBusiness.saveOrUpdate(area);
		    LOG.debug("Guardado de área exitoso " + idArea);
		    return idArea;
		} catch (BusinessException businessException) {
		    LOG.error(businessException.getMessage(), businessException);
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	        response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
		}	
		return idArea;
	}
	
	@RequestMapping(value = UrlConstants.CHANGE_AREA_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final void changeAreaStatus(@RequestBody final Area area, final HttpServletResponse response) {
	    try {
	        LOG.debug("Se hará un filtro por idArea " +  area.getIdArea() + " y Estatus "
                    + area.getStatus());
	        this.areasBusiness.changeAreaStatus(area.getIdArea(), area.getStatus());	    
	    } catch (BusinessException businessException) {
	        LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	    
	    }
	}
	
    @RequestMapping (value = UrlConstants.AREA_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Area> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de áreas");
            final ConsultaList<Area> listReturn = new ConsultaList<Area>();
            listReturn.setList(this.areasBusiness.findAll());
            return listReturn;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	        
        }
        return new ConsultaList<Area>();
    }
    
    @RequestMapping (value = UrlConstants.AREA_FIND_ACTIVE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Area> findActive(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de áreas activas");
            final ConsultaList<Area> listReturn = new ConsultaList<Area>();
            listReturn.setList(this.areasBusiness.findByRecordStatus(RecordStatusEnum.ACTIVE));
            return listReturn;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Area>();
    }
	
    @RequestMapping (value = UrlConstants.FIND_AREAS_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Area> findByRecordStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por estatus" + status);
            final ConsultaList<Area> listReturn = new ConsultaList<Area>();
            listReturn.setList(this.areasBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return listReturn;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	        
        }
        return new ConsultaList<Area>();
    }
    	
    @RequestMapping (value = UrlConstants.FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Area findById(@RequestBody final Area area, final HttpServletResponse response) {
        try {
            LOG.debug(this.getClass().getName()+" > findById  " + area.getIdArea());
            return this.areasBusiness.findById(area.getIdArea());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	        
        }
        return new Area();
    }
    
    @RequestMapping (value = UrlConstants.FIND_AREAS_BY_ID_REQUISITION, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Area> findByIdRequisition(@RequestBody final ConsultaList<Area> vo, 
    		final HttpServletResponse response) {
    	
    	LOG.info("AreaService :: findByIdRequisition");
    	
        try {
            final ConsultaList<Area> listReturn = new ConsultaList<Area>();
            listReturn.setList(this.areasBusiness.findByIdRequisition(vo.getParam4()));
            return listReturn;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	        
        }
        return null;
    }
    
    @RequestMapping (value = UrlConstants.FIND_ALL_AREAS_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Area> findAreasCatalogPaged(@RequestBody final Area area, 
            final HttpServletResponse response) {
        try {
            final ConsultaList<Area> listReturn = new ConsultaList<Area>();
            listReturn.setList(this.areasBusiness.findAreasCatalogPaged(area));
            return listReturn;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	        
        }
        return new ConsultaList<Area>();
    }
    
    @RequestMapping (value = UrlConstants.TOTAL_PAGES_SHOW_AREAS, method = RequestMethod.POST)
    @ResponseBody
    public final Area returnTotalPagesShowAreas(@RequestBody final Area area, 
            final HttpServletResponse response) {
        try {
            return this.areasBusiness.returnTotalPagesShowAreas(area.getStatus());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());	        
        }
        return null;
    }
}
