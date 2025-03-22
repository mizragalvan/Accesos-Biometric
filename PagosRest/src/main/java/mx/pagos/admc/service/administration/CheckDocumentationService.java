package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.CheckDocumentationBusiness;
import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
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
public class CheckDocumentationService {

    @Autowired
    private CheckDocumentationBusiness checkDocumentationBusiness;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @Autowired
    private UserSession session; 

    @RequestMapping(value = UrlConstants.FIND_CHECKLIST_DOCUMENTATION_BY_CATEGORY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocumentation> findCheckDocumentationByCategory(
            @RequestBody final String idCategory, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Busqueda de Checklist de documentos " + 
                    "por id de categoría número: "  + idCategory, this.session, LogCategoryEnum.QUERY));
            final ConsultaList<CheckDocumentation> checkDocumentationList = new ConsultaList<>();
            checkDocumentationList.setList(
                    this.checkDocumentationBusiness.findCheckDocumentationByCategory(Integer.valueOf(idCategory)));
            return checkDocumentationList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return new ConsultaList<CheckDocumentation>();
    }

    @RequestMapping(value = UrlConstants.CHECKLIST_DOCUMENTATION_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final CheckDocumentation checkDocumentation, 
            final HttpServletResponse response) {
        Integer idCheckDocumentation = 0;
        try {
            idCheckDocumentation = this.checkDocumentationBusiness.saveOrUpdate(checkDocumentation);
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Guardardo de Checklist número de Id: "  +
                    idCheckDocumentation + "", this.session, LogCategoryEnum.INSERT));
            return idCheckDocumentation;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idCheckDocumentation;
    }

    @RequestMapping(value = UrlConstants.CHECKLIST_DOCUMENTATION_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeStatus(@RequestBody final CheckDocumentation checkDocumentation, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Cambio de estatus de Checklist con Id: "
                    + checkDocumentation.getIdCheckDocumentation() + " y con un estatus actual: " +
                    checkDocumentation.getStatus(), this.session, LogCategoryEnum.UPDATE));
            this.checkDocumentationBusiness.changeCheckDocumentationStatus(
                    checkDocumentation.getIdCheckDocumentation(), checkDocumentation.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.CHECKLIST_DOCUMENTATION_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocumentation> findAll(final HttpServletResponse response) {
        try {
            final ConsultaList<CheckDocumentation> checkDocumentationList = new ConsultaList<CheckDocumentation>();
            checkDocumentationList.setList(this.checkDocumentationBusiness.findAll());
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los registros Checklist", this.session, LogCategoryEnum.QUERY));
            return checkDocumentationList;            
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<CheckDocumentation>();
    }

    @RequestMapping (value = UrlConstants.CHECKLIST_DOCUMENTATION_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocumentation> findByStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Checklist por estatus: " + 
                    status, this.session, LogCategoryEnum.QUERY));
            final ConsultaList<CheckDocumentation> currencyList = new ConsultaList<CheckDocumentation>();
            currencyList.setList(this.checkDocumentationBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return currencyList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<CheckDocumentation>();
    }

    @RequestMapping (value = UrlConstants.CHECKLIST_DOCUMENTATION_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final CheckDocumentation findById(@RequestBody final Integer idCheckDocumentation, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Checklist con el id: " + 
                    idCheckDocumentation, this.session, LogCategoryEnum.QUERY));
            return this.checkDocumentationBusiness.findById(idCheckDocumentation);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new CheckDocumentation();
    }
    
    @RequestMapping (value = UrlConstants.CHECKLIST_DOCUMENTATION_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocumentation> findAllCheckDocumentationCatalogPaged(
            @RequestBody final CheckDocumentation checkDocumentation, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los Checklist", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<CheckDocumentation> checkDocumentationList = new ConsultaList<>();
            checkDocumentationList.setList(
                    this.checkDocumentationBusiness.findCheckDocumentationCatalogPaged(checkDocumentation));
            return checkDocumentationList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<CheckDocumentation>();
    }

    @RequestMapping (value = UrlConstants.CHECKLIST_DOCUMENTATION_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final CheckDocumentation returnTotalRowsOfCheckDocumentation(
            @RequestBody final CheckDocumentation checkDocumentation, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de Checklist", this.session, LogCategoryEnum.QUERY));
            return this.checkDocumentationBusiness.returnTotalPagesShowCheckDocumentation(
                    checkDocumentation.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new CheckDocumentation();
    }
}
