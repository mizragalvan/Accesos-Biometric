package mx.pagos.admc.service.administration;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.FinancialEntityBusiness;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */

@Controller
public class FinancialEntityService {
    private static final Logger LOG = Logger.getLogger(FinancialEntityService.class);
    
    @Autowired
    private FinancialEntityBusiness finacialEntityBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @RequestMapping(value = UrlConstants.FINANCIAL_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final FinancialEntity financialEntity, 
            final HttpServletResponse response) {
        Integer idFinancialEntity = 0;
        try {
            idFinancialEntity = this.finacialEntityBusiness.saveOrUpdate(financialEntity);
            LOG.debug("Guardado de Entidad exitoso " + idFinancialEntity);
            return idFinancialEntity;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idFinancialEntity;
    }
   
    @RequestMapping(value = UrlConstants.FIND_FINANCIAL_BY_REQUISITION, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> financialEntityByRequisition(
            @RequestBody final Integer idRequisition, final HttpServletResponse response) {
        try {
            final ConsultaList<FinancialEntity> entityList = new ConsultaList<>();
            entityList.setList(this.finacialEntityBusiness.findFinancialEntityByIdRequisition(idRequisition));
            return entityList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return new ConsultaList<FinancialEntity>();
    }
    
    @RequestMapping(value = UrlConstants.CHANGE_FINANCIAL_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeFinancialEntityStatus(@RequestBody final FinancialEntity financialEntity, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por idFinancialEntity " + financialEntity.getIdFinancialEntity() + 
                    " y Estatus " + financialEntity.getStatus());
            this.finacialEntityBusiness.changeFinancialEntityStatus(financialEntity.getIdFinancialEntity(), 
                    financialEntity.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }
    
    @RequestMapping (value = UrlConstants.FINANCIAL_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Entidades activas");
            final ConsultaList<FinancialEntity> returnList = new ConsultaList<FinancialEntity>();
            returnList.setList(this.finacialEntityBusiness.findAll());
            return returnList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<FinancialEntity>();
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping (value = UrlConstants.FINANCIAL_FIND_DATA_FINANCIAL_ENTITY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findDataFinantialEntity(@RequestBody final ParametersHolder vo,
    		final HttpServletResponse response) {
    	try {
    		final List<Integer> idFinancialEntityList = 
    				(List<Integer>) vo.getParameterValue("dataFinancialEntitiesList");
    		final ConsultaList<FinancialEntity> financialEntityConsultaList = new ConsultaList<FinancialEntity>();
    		financialEntityConsultaList.setList(this.finacialEntityBusiness.findDataFinantialEntity(
    				idFinancialEntityList));
    		return financialEntityConsultaList;
    	} catch (BusinessException businessException) {
    		LOG.error(businessException.getMessage(), businessException);
    		response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
    		response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
    	}
    	return new ConsultaList<FinancialEntity>();
    }
    
	@RequestMapping 
	(value = UrlConstants.FINANCIAL_FIND_DATA_FINANCIAL_ENTITY_REQUISITION, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findDataFinantialEntityRequisition(
    		@RequestBody final Integer idRequisition, final HttpServletResponse response) {
    	try {
    		final ConsultaList<FinancialEntity> financialEntityConsultaList = new ConsultaList<FinancialEntity>();
    		financialEntityConsultaList.setList(this.finacialEntityBusiness.findDataFinantialEntityRequisition(
    				idRequisition));
    		return financialEntityConsultaList;
    	} catch (BusinessException businessException) {
    		LOG.error(businessException.getMessage(), businessException);
    		response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
    		response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
    	}
    	return new ConsultaList<FinancialEntity>();
    }
    
    @RequestMapping (value = UrlConstants.FINANCIAL_FIND_ACTIVE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findActive(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Entidades");
            final ConsultaList<FinancialEntity> returnList = new ConsultaList<FinancialEntity>();
            returnList.setList(this.finacialEntityBusiness.findByRecordStatus(RecordStatusEnum.ACTIVE));
            return returnList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<FinancialEntity>();
    }
    
    @RequestMapping (value = UrlConstants.FINANCIAL_FIND_BY_RECORD_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findByRecordStatus(@RequestBody final String status, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Entidades por estatus" + status);
            final ConsultaList<FinancialEntity> returnList = new ConsultaList<FinancialEntity>();
            returnList.setList(this.finacialEntityBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return returnList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<FinancialEntity>();
    }
    
    @RequestMapping (value = UrlConstants.FINANCIAL_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final FinancialEntity findByIdFinancialEntity(@RequestBody final FinancialEntity financialEntity, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Entidad por id" + financialEntity.getIdFinancialEntity());
            return this.finacialEntityBusiness.findByIdFinancialEntity(financialEntity.getIdFinancialEntity());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new FinancialEntity();
    }
    
    @RequestMapping (value = UrlConstants.FINANCIAL_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findAllFinancialEntityCatalogPaged(
            @RequestBody final FinancialEntity financialEntity, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todas las entidades", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<FinancialEntity> financialEntityList = new ConsultaList<>();
            financialEntityList.setList(
                    this.finacialEntityBusiness.findFinancialEntityPaged(financialEntity));
            return financialEntityList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<FinancialEntity>();
    }

    @RequestMapping (value = UrlConstants.FINANCIAL_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final FinancialEntity returnTotalRowsOfFinancialEntity(
            @RequestBody final FinancialEntity financialEntity, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de entidades", 
                    this.session, LogCategoryEnum.QUERY));
            return this.finacialEntityBusiness.returnTotalPagesShowFinancialEntity(financialEntity);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new FinancialEntity();
    }
}