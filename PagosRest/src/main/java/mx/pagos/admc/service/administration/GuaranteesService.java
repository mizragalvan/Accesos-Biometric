package mx.pagos.admc.service.administration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.GuaranteesBusiness;
import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.contracts.structures.Guarantees;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

/**
 * @author Mizraim
 */
@Controller
public class GuaranteesService {
    private static final Logger LOG = Logger.getLogger(GuaranteesService.class);
    private static final String SCAPED_QUOTES = "\"";

    @Autowired
    private GuaranteesBusiness guaranteesBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @RequestMapping(value = UrlConstants.GUARANTEES_SAVEORUPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Guarantees guarantees, final HttpServletResponse response) {
        Integer idGuarantee = 0;
        try {
            idGuarantee = this.guaranteesBusiness.saveOrUpdate(guarantees);
            LOG.debug("Guardado de garantia exitoso" + idGuarantee);
            return idGuarantee;                   
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
        return idGuarantee;
    }
    
    @RequestMapping(value = UrlConstants.GUARANTEES_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeGuaranteesStatus(@RequestBody final Guarantees guarantees,
            final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por garantia " +  guarantees.getIdGuarantee() + " y estatus "
                    + guarantees.getStatus());
            this.guaranteesBusiness.changeGuaranteesStatus(guarantees.getIdGuarantee(), guarantees.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
    }
    
    @RequestMapping (value = UrlConstants.GUARANTEES_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Guarantees> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de garantias");
            final ConsultaList<Guarantees> guaranteeList = new ConsultaList<>();
            guaranteeList.setList(this.guaranteesBusiness.findAll());
            return guaranteeList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Guarantees>();
    }
    
    @RequestMapping (value = UrlConstants.GUARANTEES_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Guarantees> findByStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por estatus " + status);
            final ConsultaList<Guarantees> guaranteeList = new ConsultaList<>();
            guaranteeList.setList(this.guaranteesBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            return guaranteeList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Guarantees>();
    }
    
    @RequestMapping (value = UrlConstants.FIND_CHECK_DOCUMENT_BY_GUARANTEE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<CheckDocument> findCheckDocumentListByIdGuarantee(@RequestBody final Integer idGuarantee,
            final HttpServletResponse response) {
        final ConsultaList<CheckDocument> guaranteeList = new ConsultaList<>();
        try {
            guaranteeList.setList(this.guaranteesBusiness.findCheckDocumentListByIdGuarantee(idGuarantee));
            return guaranteeList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return guaranteeList;
    }
    
    @RequestMapping (value = UrlConstants.FIND_GUARANTEE_CHECK_DOCUMENT_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Guarantees> findGuaranteeCheckDocuementation(@RequestBody final String status,
            final HttpServletResponse response) {
        final ConsultaList<Guarantees> guaranteeList = new ConsultaList<>();
        try {
            guaranteeList.setList(this.guaranteesBusiness.findGuaranteeCheckDocuementation(
                    RecordStatusEnum.valueOf(status)));
            return guaranteeList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return guaranteeList;
    }
    
    @RequestMapping (value = UrlConstants.GUARANTEES_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Guarantees findById(@RequestBody final Guarantees guarantees, final HttpServletResponse response) {
        try {
            LOG.debug("Se va a consultar por id" + guarantees.getIdGuarantee());
            return this.guaranteesBusiness.findById(guarantees.getIdGuarantee());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Guarantees();
    }
    
    @RequestMapping (value = UrlConstants.GUARANTEES_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Guarantees> findAllGuaranteesCatalogPaged(@RequestBody final Guarantees guarantees, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos las garantías", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Guarantees> guaranteesList = new ConsultaList<>();
            guaranteesList.setList(this.guaranteesBusiness.findGuaranteesCatalogPaged(guarantees));
            return guaranteesList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Guarantees>();
    }

    @RequestMapping (value = UrlConstants.GUARANTEES_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final Guarantees returnTotalRowsOfGuarantees(
            @RequestBody final Guarantees guarantees, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de garantías", this.session, LogCategoryEnum.QUERY));
            return this.guaranteesBusiness.returnTotalPagesShowGuarantees(guarantees.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Guarantees();
    }
    
    @RequestMapping (value = UrlConstants.DOWNLOAD_GUARANTEES, method = RequestMethod.GET)
    public final void downloadGuarantees(final HttpServletRequest request,
            final HttpServletResponse response) {
        try {
            final String parameterDocumentPath = String.valueOf(request.getParameter("path"));
            this.sendFileToDownload(response, parameterDocumentPath);
        } catch (IOException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }
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
