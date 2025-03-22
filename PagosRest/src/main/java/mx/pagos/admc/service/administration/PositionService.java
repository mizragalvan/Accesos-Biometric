package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.PositionBusiness;
import mx.pagos.admc.contracts.structures.Positions;
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

@Controller("PositionService")
public class PositionService {
    private static final Logger LOG = Logger.getLogger(PositionService.class);

    @Autowired
    private PositionBusiness positionBusiness;

    @RequestMapping (value = UrlConstants.POSITIONS_SEARCHALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Positions> searchPositions(final HttpServletResponse  response) {

        LOG.info("PositionService :: searchPositions");
        try {
            final ConsultaList<Positions> listReturn = new ConsultaList<Positions>();
            listReturn.setList(this.positionBusiness.findAll());
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
            LOG.info(" BusinessException :: PositionService :: searchPositions");
            LOG.info(businessException.getMessage(), businessException);
        }
        return new ConsultaList<Positions>();
    }
    
    @RequestMapping(value = UrlConstants.POSITION_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Positions position, final HttpServletResponse response) {
        Integer idPosition = 0;
        try {
            idPosition = this.positionBusiness.saveOrUpdate(position);
            LOG.debug("Guardado de Puesto exitoso " + idPosition);
            return idPosition;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idPosition;
    }
    
    @RequestMapping(value = UrlConstants.CHANGE_POSITION_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changePositionStatus(@RequestBody final Positions position, final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por id del puesto: " + position.getIdPosition() + 
                    " y Estatus " + position.getStatus());
            this.positionBusiness.changePositionStatus(position.getIdPosition(), position.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }
    
    @RequestMapping (value = UrlConstants.POSITION_FIND_BY_RECORD_STATUS, method = RequestMethod.POST)
    @ResponseBody
   public final ConsultaList<Positions> findByRecordStatus(@RequestBody final String status, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Puestos por estatus" + status);
            final ConsultaList<Positions> returnList = new ConsultaList<Positions>();
            returnList.setList(this.positionBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return returnList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Positions>();
    }
    
    @RequestMapping (value = UrlConstants.POSITION_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Positions findPositionByIdPosition(@RequestBody final Positions position, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener puesto por id" + position.getIdPosition());
            return this.positionBusiness.findById(position.getIdPosition());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Positions();
    }
    
    @RequestMapping (value = UrlConstants.POSITIONS_FIND_ALL_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Positions> findAllPositionCatalogPaged(@RequestBody final Positions position, 
            final HttpServletResponse response) {
        try {
            final ConsultaList<Positions> positionList = new ConsultaList<>();
            positionList.setList(this.positionBusiness.findPositionCatalogPaged(position));
            return positionList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<>();
    }
    
    @RequestMapping (value = UrlConstants.POSITIONS_FIND_TOTAL_ROWS, method = RequestMethod.POST)
    @ResponseBody
    public final Positions returnTotalRowsOfCatalogPosition(@RequestBody final Positions position, 
            final HttpServletResponse response) {
        try {
            return this.positionBusiness.returnTotalPagesShowPosition(position.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Positions();
    }
}
