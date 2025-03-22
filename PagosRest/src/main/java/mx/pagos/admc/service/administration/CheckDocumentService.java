package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.CheckDocumentBusiness;
import mx.pagos.admc.contracts.structures.CheckDocument;
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
public class CheckDocumentService {

    @Autowired
    private CheckDocumentBusiness checkDocumentBusiness;
    
    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @Autowired
    private UserSession session; 

    @RequestMapping(value = UrlConstants.CHECK_DOCUMENT_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final CheckDocument checkDocument,
            final HttpServletResponse response) {
        Integer idCheckDocument = 0;
        try {
            idCheckDocument = this.checkDocumentBusiness.saveOrUpdate(checkDocument);
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Guardardo de Checklist " + 
                    "de Documento número: "  + idCheckDocument + "", this.session, LogCategoryEnum.INSERT));
            return idCheckDocument;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idCheckDocument;
    }

    @RequestMapping(value = UrlConstants.CHECK_DOCUMENT_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeStatus(@RequestBody final CheckDocument checkDocument, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Cambio de estatus del CheckList de "
                    + "Documento con id: " + checkDocument.getIdCheckDocument() + " y con un estatus actual: " +
                    checkDocument.getStatus(), this.session, LogCategoryEnum.UPDATE));
            this.checkDocumentBusiness.changeStatus(checkDocument.getIdCheckDocument(), checkDocument.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.CHECK_DOCUMENT_FIN_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocument> findAll(final HttpServletResponse response) {
        try {
            final ConsultaList<CheckDocument> checkDocumentList = new ConsultaList<CheckDocument>();
            checkDocumentList.setList(this.checkDocumentBusiness.findAll());
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de todos los CheckList de "
                    + "Documentos registrados", this.session, LogCategoryEnum.QUERY));
            return checkDocumentList;            
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<CheckDocument>();
    }

    @RequestMapping (value = UrlConstants.CHECK_DOCUMENT_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocument> findByStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Checklist de Documentos por "
                    + "estatus: " + status, this.session, LogCategoryEnum.QUERY));
            final ConsultaList<CheckDocument> checkDocumentList = new ConsultaList<CheckDocument>();
            checkDocumentList.setList(this.checkDocumentBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            return checkDocumentList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<CheckDocument>();
    }

    @RequestMapping (value = UrlConstants.CHECK_DOCUMENT_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final CheckDocument findById(@RequestBody final CheckDocument checkDocument, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de CheckList de Documento "
                    + "con el id: " + checkDocument.getIdCheckDocument(), this.session, LogCategoryEnum.QUERY));
            return this.checkDocumentBusiness.findById(checkDocument.getIdCheckDocument());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new CheckDocument();
    }
    
    @RequestMapping (value = UrlConstants.CHECK_DOCUMENT_FIND_BY_GUARANTEE_AND_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocument> findByGuaranteeAndStatus(@RequestBody final CheckDocument checkDocument,
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Consulta de Checklist de Documentos por "
                    + "estatus: " + checkDocument.getStatus() + " y garantia: " + checkDocument.getGuarantee(),
                    this.session, LogCategoryEnum.QUERY));
            final ConsultaList<CheckDocument> checkDocumentList = new ConsultaList<CheckDocument>();
            checkDocumentList.setList(this.checkDocumentBusiness.findByGuaranteeAndStatus(checkDocument.getStatus(),
                    checkDocument.getGuarantee()));
            return checkDocumentList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<CheckDocument>();
    }
}
