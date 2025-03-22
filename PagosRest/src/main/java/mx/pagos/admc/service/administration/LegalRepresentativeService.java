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

import mx.pagos.admc.contracts.business.LegalRepresentativeBusiness;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
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

@Controller
public class LegalRepresentativeService {
    private static final Logger LOG = Logger.getLogger(LegalRepresentativeService.class);

    @Autowired
    private LegalRepresentativeBusiness legalRepresentativeBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @RequestMapping(value = UrlConstants.LEGAL_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final LegalRepresentative legalRepresentative, 
            final HttpServletResponse response) {
        Integer idLegal = 0;     
        try {
            idLegal = this.legalRepresentativeBusiness.saveOrUpdate(legalRepresentative);
            LOG.debug("Guardado de Representante Legal exitoso " + idLegal);
            return idLegal;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idLegal;
    }

    @RequestMapping(value = UrlConstants.CHANGE_LEGAL_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeLegalRepresentativeStatus(@RequestBody final LegalRepresentative legalRepresentative, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por idlegalRepresentative " + legalRepresentative.getIdLegalRepresentative() +
                    " y Estatus " + legalRepresentative.getStatus());
            this.legalRepresentativeBusiness.changeLegalRepresentativeStatus(legalRepresentative.
                    getIdLegalRepresentative(), legalRepresentative.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.LEGAL_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<LegalRepresentative> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Representantes Legales");
            final ConsultaList<LegalRepresentative> returnList = new ConsultaList<LegalRepresentative>();
            returnList.setList(this.legalRepresentativeBusiness.findAll());
            return returnList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());    
        }
        return new ConsultaList<LegalRepresentative>();
    }

    @RequestMapping (value = UrlConstants.LEGAL_FIND_BY_RECORD_STATUS , method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<LegalRepresentative> findByRecordStatus(@RequestBody final String 
            status, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por estatus" + status);
            final ConsultaList<LegalRepresentative> returnList = new ConsultaList<LegalRepresentative>();
            returnList.setList(this.legalRepresentativeBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return returnList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<LegalRepresentative>();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping (value = UrlConstants.LEGAL_FIND_DGA_AND_FINANCIAL_ENTITY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<LegalRepresentative> findByDgaAndArea(@RequestBody final ParametersHolder parameters,
            final HttpServletResponse response) {
        try {
            final ConsultaList<LegalRepresentative> returnList = new ConsultaList<LegalRepresentative>();
            returnList.setList(this.legalRepresentativeBusiness.findLegalRepresentativeByFinancialEntity(
                    (List<Integer>) parameters.getParameterValue("financialEntitiesList")));
            return returnList;         
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<LegalRepresentative>();
    }

    @RequestMapping (value = UrlConstants.LEGAL_FIND_FINANCIAL_ENTITIES_BY_ID_LEGAL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<FinancialEntity> findFinantialEntitiesByIdLegalRepresentative(
            @RequestBody final Integer idLegalRepresentative, final HttpServletResponse response) {
        try {
            final ConsultaList<FinancialEntity> returnList = new ConsultaList<>();
            returnList.setList(
            this.legalRepresentativeBusiness.findFinantialEntitiesByIdLegalRepresentative(idLegalRepresentative));
            return returnList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<FinancialEntity>();
    }

    @RequestMapping (value = UrlConstants.LEGAL_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final LegalRepresentative findByIdLegalRepresentative(
            @RequestBody final LegalRepresentative legalRepresentative, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por id" + legalRepresentative.getIdLegalRepresentative());
            return this.legalRepresentativeBusiness.
                    findByIdLegalRepresentative(legalRepresentative.getIdLegalRepresentative());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new LegalRepresentative();
    }
    
    @RequestMapping (value = UrlConstants.LEGAL_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<LegalRepresentative> findAllLegalRepresentativeCatalogPaged(
            @RequestBody final LegalRepresentative legalRepresentative, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los representantes legales", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<LegalRepresentative> legalRepresentativeList = new ConsultaList<>();
            legalRepresentativeList.setList(
                    this.legalRepresentativeBusiness.findLegalRepresentativeCatalogPaged(legalRepresentative));
            return legalRepresentativeList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<LegalRepresentative>();
    }

    @RequestMapping (value = UrlConstants.LEGAL_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final LegalRepresentative returnTotalRowsOfLegalRepresentative(
            @RequestBody final LegalRepresentative legalRepresentative, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de dgas", this.session, LogCategoryEnum.QUERY));
            return this.legalRepresentativeBusiness.returnTotalPagesShowLegalRepresentative(
                    legalRepresentative);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new LegalRepresentative();
    }
}
