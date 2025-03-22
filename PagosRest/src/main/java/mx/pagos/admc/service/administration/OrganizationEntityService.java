package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.owners.OrganizationEntityBusiness;
import mx.pagos.admc.contracts.structures.owners.OrganizationEntity;
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
public class OrganizationEntityService {
private static final Logger LOG = Logger.getLogger(OrganizationEntityService.class);
    
    @Autowired
    private OrganizationEntityBusiness organizationEntityBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @RequestMapping(value = UrlConstants.ORGANIZATION_ENTITY_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final OrganizationEntity organizationEntity, 
            final HttpServletResponse response) {
        Integer idFinancialEntity = 0;
        try {
            idFinancialEntity = this.organizationEntityBusiness.saveOrUpdate(organizationEntity);
            LOG.debug("Guardado de Entidad de la Organización exitoso " + idFinancialEntity);
            return idFinancialEntity;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idFinancialEntity;
    }
    
    @RequestMapping(value = UrlConstants.ORGANIZATION_ENTITY_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeFinancialEntityStatus(@RequestBody final OrganizationEntity vo, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se cambiara el estatus de Entidad de la Organización con id " +
                    vo.getIdOrganizationEntity());
            this.organizationEntityBusiness.changeStatus(vo.getIdOrganizationEntity(), RecordStatusEnum.valueOf(
                            vo.getStatus().toString()));
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }
    
    @RequestMapping (value = UrlConstants.ORGANIZATION_ENTITY_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<OrganizationEntity> findAll(final HttpServletResponse response) {
        final ConsultaList<OrganizationEntity> organizationEntityList = new ConsultaList<>();
        try {
            LOG.debug("Se va a obtener la lista de Entidades de la Organización activas");
            organizationEntityList.setList(this.organizationEntityBusiness.findAll());
            return organizationEntityList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return organizationEntityList;
    }
    
    @RequestMapping (value = UrlConstants.ORGANIZATION_ENTITY_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<OrganizationEntity> findByRecordStatus(@RequestBody final String status, 
            final HttpServletResponse response) {
        final ConsultaList<OrganizationEntity> organizationEntityList = new ConsultaList<>();
        try {
            LOG.debug("Se va a obtener Entidades de la Organización por estatus" + status);
            organizationEntityList.setList(this.organizationEntityBusiness.findByStatus(
                    RecordStatusEnum.valueOf(status)));
            return organizationEntityList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return organizationEntityList;
    }
    
    @RequestMapping (value = UrlConstants.ORGANIZATION_ENTITY_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final OrganizationEntity findByIdFinancialEntity(@RequestBody final Integer idOrganizationEntity, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Entidad de la Organización por id" + idOrganizationEntity);
            return this.organizationEntityBusiness.findById(idOrganizationEntity);
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new OrganizationEntity();
    }
    
    @RequestMapping (value = UrlConstants.ORGANIZATION_ENTITY_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<OrganizationEntity> findAllOrganizationEntityCatalogPaged(
            @RequestBody final OrganizationEntity entity, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todas las entidades", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<OrganizationEntity> organizationEntityList = new ConsultaList<>();
            organizationEntityList.setList(this.organizationEntityBusiness.findOrganizationEntityCatalogPaged(entity));
            return organizationEntityList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<OrganizationEntity>();
    }

    @RequestMapping (value = UrlConstants.ORGANIZATION_ENTITY_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final OrganizationEntity returnTotalRowsOfOrganizationEntity(
            @RequestBody final OrganizationEntity entity, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de entidades", this.session, LogCategoryEnum.QUERY));
            return this.organizationEntityBusiness.returnTotalPagesShowOrganizationEntity(entity.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new OrganizationEntity();
    }
}
