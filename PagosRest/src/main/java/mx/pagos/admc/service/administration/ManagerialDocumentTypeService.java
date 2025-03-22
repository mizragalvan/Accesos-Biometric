package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.ManagerialDocumentTypeBusiness;
import mx.pagos.admc.contracts.structures.ManagerialDocumentType;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.ManagerialDocumentTypeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class ManagerialDocumentTypeService {

    @Autowired
    private ManagerialDocumentTypeBusiness managerialDocumentTypeBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @RequestMapping(value = UrlConstants.MANAGERIAL_DOCTYPE_SAVEORUPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final ManagerialDocumentType managerialDocumentType,
            final HttpServletResponse response) {
        Integer idManagerialDocumentType = 0;
        try {
            idManagerialDocumentType = 
                    this.managerialDocumentTypeBusiness.saveOrpdateManagerialDocumentType(managerialDocumentType);
            return idManagerialDocumentType;                   
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return idManagerialDocumentType;
    }

    @RequestMapping(value = UrlConstants.MANAGERIAL_DOCTYPE_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeManagerialDocumentTypeStatus(@RequestBody final ManagerialDocumentType 
            managerialDocumentType, final HttpServletResponse response) {
        try {
            this.managerialDocumentTypeBusiness.changeManagerialDocumentTypeStatus(
                    managerialDocumentType.getIdManagerialDocumentType(), managerialDocumentType.getStatus());        
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }

    @RequestMapping (value = UrlConstants.MANAGERIAL_DOCTYPE_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<ManagerialDocumentType> findAll(final HttpServletResponse response) {
        try {
            final ConsultaList<ManagerialDocumentType> documentTypeList = new ConsultaList<ManagerialDocumentType>();
            documentTypeList.setList(this.managerialDocumentTypeBusiness.findAll());
            return documentTypeList;            
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<ManagerialDocumentType>();
    }

    @RequestMapping (value = UrlConstants.MANAGERIAL_DOCTYPE_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<ManagerialDocumentType> findByStatus(@RequestBody final String status, 
            final HttpServletResponse response) {
        try {
            final ConsultaList<ManagerialDocumentType> managerialList = new ConsultaList<ManagerialDocumentType>();
            managerialList.setList(this.managerialDocumentTypeBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            return managerialList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<ManagerialDocumentType>();
    } 

    @RequestMapping (value = UrlConstants.MANAGERIAL_DOCTYPE_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final ManagerialDocumentType findById(@RequestBody final String idManagerialDocumentType, 
            final HttpServletResponse response) {
        try {
            return this.managerialDocumentTypeBusiness.findById(Integer.valueOf(idManagerialDocumentType));
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ManagerialDocumentType();
    }

    @RequestMapping (value = UrlConstants.MANAGERIAL_DOCTYPE_FIND_BY_DOCUMENT_TYPE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<ManagerialDocumentType> findByDocumentType(@RequestBody final String documentType,
            final HttpServletResponse response) {
        try {
            final ConsultaList<ManagerialDocumentType> documentTypeList =  new ConsultaList<>();
            documentTypeList.setList(this.managerialDocumentTypeBusiness.findByDocumentType(
                    ManagerialDocumentTypeEnum.valueOf(documentType)));
            return documentTypeList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<ManagerialDocumentType>();
    }
    
    @RequestMapping (value = UrlConstants.MANAGERIAL_DOCTYPE_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<ManagerialDocumentType> findAllManagerialDocumentTypeCatalogPaged(
            @RequestBody final ManagerialDocumentType documentType, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los tipos de documentos empresarial", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<ManagerialDocumentType> managerialDocumentTypeList = new ConsultaList<>();
            managerialDocumentTypeList.setList(
                    this.managerialDocumentTypeBusiness.findManagerialDocumentTypeCatalogPaged(documentType));
            return managerialDocumentTypeList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<ManagerialDocumentType>();
    }

    @RequestMapping (value = UrlConstants.MANAGERIAL_DOCTYPE_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final ManagerialDocumentType returnTotalRowsOfManagerialDocumentType(
            @RequestBody final ManagerialDocumentType documentType, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de tipos de documentos empresarial", 
                    this.session, LogCategoryEnum.QUERY));
            return this.managerialDocumentTypeBusiness.returnTotalPagesShowManagerialDocumentType(
                    documentType.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ManagerialDocumentType();
    }
}
