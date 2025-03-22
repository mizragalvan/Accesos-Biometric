package mx.pagos.admc.service.administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.DocumentTypeBusiness;
import mx.pagos.admc.contracts.structures.DocumentType;
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

import org.apache.commons.io.IOUtils;
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
public class DocumentTypeService {

    private static final String ID_DOCUMENT_TYPE = "idDocumentType";
    private static final Logger LOG = Logger.getLogger(DocumentTypeService.class); 
    private static final String SCAPED_QUOTES = "\"";

    @Autowired
    private DocumentTypeBusiness documentTypeBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @RequestMapping(value = UrlConstants.DOCTYPE_SAVEORUPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final DocumentType documentType, final HttpServletResponse response)
    		throws IOException {
        Integer idDocumentType = 0;
        try {
            idDocumentType = this.documentTypeBusiness.saveOrUpdate(documentType);
            LOG.debug("Guardado de tipo de documento exitoso" + idDocumentType);
            return idDocumentType;                   
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return idDocumentType;
    }

    @RequestMapping(value = UrlConstants.DOCTYPE_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeDocumentTypeStatus(@RequestBody final DocumentType documentType, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por tipo de documento " +  documentType.getIdDocumentType() + " y Estatus "
                    + documentType.getStatus());
            this.documentTypeBusiness.changeDocumentTypeStatus(documentType.getIdDocumentType(), 
                    documentType.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }

    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentType> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Documentos");
            final ConsultaList<DocumentType> documentTypeList = new ConsultaList<DocumentType>();
            documentTypeList.setList(this.documentTypeBusiness.findAll());
            return documentTypeList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<DocumentType>();
    }

    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_BY_STATUS_AND_DOCUMENTTYPE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentType> findDocumentTypeByStatusAndDocumentType(
            @RequestBody final DocumentType documentType, final HttpServletResponse response) {
        try {
            final ConsultaList<DocumentType> documentTypeList = new ConsultaList<DocumentType>();
            documentTypeList.setList(
                    this.documentTypeBusiness.findDocumentByTypeStatusAndDocumentType(documentType));
            return documentTypeList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<DocumentType>();
    }

    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentType> findByRecordStatus(
            @RequestBody final ConsultaList<DocumentType> documentTypeList, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por estatus" + documentTypeList.getParam1());
            documentTypeList.setList(this.documentTypeBusiness.findByRecordStatus(
                    RecordStatusEnum.valueOf(documentTypeList.getParam1())));
            return documentTypeList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<DocumentType>();
    }
    
    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_ACTIVE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentType> findActive(final HttpServletResponse response) {
        final ConsultaList<DocumentType> documentTypeList = new ConsultaList<DocumentType>();
        try {
            documentTypeList.setList(this.documentTypeBusiness.findByRecordStatus(RecordStatusEnum.ACTIVE));
            return documentTypeList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return documentTypeList;
    } 

    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final DocumentType findById(@RequestBody final DocumentType documentType, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por id" + documentType.getIdDocumentType());
            return this.documentTypeBusiness.findById(documentType.getIdDocumentType());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new DocumentType();
    }

    @RequestMapping (value = UrlConstants.DOWNLOAD_DOCUMENTTYPE, method = RequestMethod.GET)
    public final void downloadDocumentType(final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
        	final String idDocumentParameter = String.valueOf(request.getParameter(ID_DOCUMENT_TYPE));
            final DocumentType documenType = this.documentTypeBusiness.findById(Integer.valueOf(idDocumentParameter));
            this.sendFileToDownload(response, documenType.getTemplatePath());
        } catch (IOException | NumberFormatException | BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }
    
    @RequestMapping (value = UrlConstants.DOWNLOAD_DOCUMENTTYPE_NATURAL, method = RequestMethod.GET)
    public final void downloadDocumentTypeNatural(final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            final String idDocumentParameter = String.valueOf(request.getParameter(ID_DOCUMENT_TYPE));
            final DocumentType documenType = this.documentTypeBusiness.findById(Integer.valueOf(idDocumentParameter));
            this.sendFileToDownload(response, documenType.getTemplatePathNaturalPerson());
        } catch (IOException | NumberFormatException | BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }
    
    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<DocumentType> findAllDocumentTypeCatalogPaged(
            @RequestBody final DocumentType documentType, final HttpServletResponse response) {
        try {
        	LOG.info("DocumentTypeService -> findAllDocumentTypeCatalogPaged: Consulta de todos los tipos de documentos");
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos los tipos de documentos", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<DocumentType> documentTypeList = new ConsultaList<>();
            documentTypeList.setList(this.documentTypeBusiness.findDocumentTypeCatalogPaged(documentType));
            return documentTypeList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<DocumentType>();
    }

    @RequestMapping (value = UrlConstants.DOCTYPE_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final DocumentType returnTotalRowsOfDocumentType(
            @RequestBody final DocumentType documentType, final HttpServletResponse response) {
        try {
        	LOG.info("DocumentTypeService -> returnTotalRowsOfDocumentType: Consulta del número de paginas de catálogo de tipos de documentos");
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de tipos de documentos", 
                    this.session, LogCategoryEnum.QUERY));
            return this.documentTypeBusiness.returnTotalPagesShowDocumentType(
                    documentType, documentType.getDocumentTypeEnum());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new DocumentType();
    }
    
    @RequestMapping (value = UrlConstants.DOCUMENT_TYPE_NAME_EXISTS, method = RequestMethod.POST)
    @ResponseBody
    public final Boolean nameExists(@RequestBody final ParametersHolder parameters,
            final HttpServletResponse response) {
        try {
            return this.documentTypeBusiness.nameExists((String) parameters.getParameterValue("Name"));
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return false;
    }

    private void sendFileToDownload(final HttpServletResponse response, final String parameterDocumentPath)
            throws FileNotFoundException, IOException {
        final File file = new File(parameterDocumentPath);
        this.setReponseData(response, file.getName(), file);
        final FileInputStream fileInputStream = new FileInputStream(file);
        final ServletOutputStream servletOutputStream = response.getOutputStream();
        IOUtils.copy(fileInputStream, servletOutputStream);
        fileInputStream.close();
        servletOutputStream.flush();
        servletOutputStream.close();
    }

    private void setReponseData(final HttpServletResponse response, final String name, final File file) {
        final int buffer = 1024 * 100;
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + SCAPED_QUOTES + name + SCAPED_QUOTES);
        response.setContentLength(Long.valueOf(file.length()).intValue());
        response.setBufferSize(buffer);
    }

}
