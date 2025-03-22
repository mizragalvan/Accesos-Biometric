package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.DocumentPersonalityFieldsBusiness;
import mx.pagos.admc.contracts.structures.DocumentPersonalityFields;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class DocumentPersonalityFieldsService {

    @Autowired
    private DocumentPersonalityFieldsBusiness documentPersonalityFieldsBusiness;

    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @RequestMapping (value = UrlConstants.FIND_DOCUMENT_FIELDS_BY_DOCUMENT, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentPersonalityFields> finDocumentFieldsByDocument(
            @RequestBody final DocumentPersonalityFields documentField, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de campos variables para el documento: " + documentField.getIdDocumentType(), 
                    this.session, LogCategoryEnum.QUERY));
            final ConsultaList<DocumentPersonalityFields> documentFieldList = new ConsultaList<>();
            documentFieldList.setList(this.documentPersonalityFieldsBusiness.findDocumentFieldsByDocumentType(
                    documentField.getIdDocumentType(), documentField.getIdPersonality()));
            return documentFieldList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<DocumentPersonalityFields>();
    }

    @RequestMapping (value = UrlConstants.SECTION_TO_SHOW_BY_DOCUMENT, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<String> sectionsToShowByDocument(
            @RequestBody final DocumentPersonalityFields documentField, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de secciones variables para el documento: " + documentField.getIdDocumentType(), 
                    this.session, LogCategoryEnum.QUERY));
            final ConsultaList<String> documentFieldList = new ConsultaList<>();
            documentFieldList.setList(this.documentPersonalityFieldsBusiness.sectionsToShowByDocument(
                    documentField.getIdDocumentType(), documentField.getIdPersonality()));
            return documentFieldList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<String>();
    }

    @RequestMapping (value = UrlConstants.FIND_ALL_DOCUMENT_FIELDS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentPersonalityFields> finAllDocumentFields(final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los campos variables", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<DocumentPersonalityFields> documentFieldList = new ConsultaList<>();
            documentFieldList.setList(this.documentPersonalityFieldsBusiness.findAllDocumentFields());
            return documentFieldList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<DocumentPersonalityFields>();
    }

    @RequestMapping (value = UrlConstants.SAVE_DOCUMENT_FIELDS_BY_DOCUMENT, method = RequestMethod.POST)
    @ResponseBody
    public final void saveDocumentFieldsByDocument(@RequestBody final 
            ConsultaList<DocumentPersonalityFields> documentFieldList, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Guardado de campos variables para el documento: " + 
            documentFieldList.getList().get(0).getIdDocumentType(), this.session, LogCategoryEnum.QUERY));
            this.documentPersonalityFieldsBusiness.saveDocumentTypeFiedsList(documentFieldList.getList());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
    }
}
