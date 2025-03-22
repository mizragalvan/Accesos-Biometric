package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.owners.PublicFigureBusiness;
import mx.pagos.admc.contracts.structures.owners.PublicFigure;
import mx.pagos.admc.enums.LogCategoryEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.enums.owners.PublicFigureTypeEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.logs.business.BinnacleBusiness;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.LoggingUtils;

@Controller
public class PublicFigureService {

    @Autowired
    private PublicFigureBusiness publicFigureBusiness;
    
    @Autowired
    private BinnacleBusiness binnacleBusiness;

    @Autowired
    private UserSession session; 

    @RequestMapping(value = UrlConstants.PUBLICFIGURE_SAVEORUPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final PublicFigure publicFigure, 
            final HttpServletResponse response) {
        Integer idPublicFigure = 0;     
        try {
            idPublicFigure = this.publicFigureBusiness.saveOrUpdate(publicFigure);
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Guardardo de figura pública con el id: "
                    + idPublicFigure, this.session, LogCategoryEnum.INSERT));
            return idPublicFigure;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idPublicFigure;
    }

    @RequestMapping(value = UrlConstants.PUBLICFIGURE_CHANGE_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeStatus(@RequestBody final PublicFigure publicFigure, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se hará un cambio de estatus de la "
                    + "figuara publica con el id: " + publicFigure.getIdPublicFigure(),
                    this.session, LogCategoryEnum.UPDATE));
            this.publicFigureBusiness.changeStatus(publicFigure.getIdPublicFigure(), publicFigure.getStatus());        
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }

    @RequestMapping (value = UrlConstants.PUBLICFIGURE_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<PublicFigure> findAll(final HttpServletResponse response) {
        final ConsultaList<PublicFigure> listReturn = new ConsultaList<PublicFigure>();
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se hará una busqueda de todas las "
                    + "figuras públicas", this.session, LogCategoryEnum.QUERY));
            listReturn.setList(this.publicFigureBusiness.findAll());
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return listReturn;
    }

    @RequestMapping (value = UrlConstants.PUBLICFIGURE_FIND_BY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<PublicFigure> findByRecordStatus(@RequestBody final String status,
            final HttpServletResponse response) {
        final ConsultaList<PublicFigure> listReturn = new ConsultaList<PublicFigure>();
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se hará una busqueda de todas las "
                    + "figuras públicas por el estatus: " + status, this.session, LogCategoryEnum.QUERY));
            listReturn.setList(this.publicFigureBusiness.findByStatus(RecordStatusEnum.valueOf(status)));
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return listReturn;
    }
    
    @RequestMapping (value = UrlConstants.PUBLICFIGURE_FIND_BY_TYPE, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<PublicFigure> findByType(@RequestBody final String type,
            final HttpServletResponse response) {
        final ConsultaList<PublicFigure> listReturn = new ConsultaList<PublicFigure>();
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se hará una busqueda de todas las "
                    + "figuras públicas por tipo: " + type, this.session, LogCategoryEnum.QUERY));
            listReturn.setList(this.publicFigureBusiness.findByType(PublicFigureTypeEnum.valueOf(type)));
            return listReturn;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return listReturn;
    }

    @RequestMapping (value = UrlConstants.PUBLICFIGURE_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final PublicFigure findById(@RequestBody final Integer idPublicFigure, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging("Se hará una busqueda de "
                    + " la figura pública con el id: " + idPublicFigure, this.session, LogCategoryEnum.QUERY));
            return this.publicFigureBusiness.findById(idPublicFigure);
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new PublicFigure();
    }
    
    @RequestMapping (value = UrlConstants.PUBLICFIGURE_FIND_ALL_CATALOG_PAGED, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<PublicFigure> findAllPublicFigureCatalogPaged(
            @RequestBody final PublicFigure publicFigure, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos las figuras públicas", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<PublicFigure> publicFigureList = new ConsultaList<>();
            publicFigureList.setList(this.publicFigureBusiness.findPublicFigureCatalogPaged(publicFigure));
            return publicFigureList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<PublicFigure>();
    }

    @RequestMapping (value = UrlConstants.PUBLICFIGURE_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final PublicFigure returnTotalRowsOfPublicFigure(
            @RequestBody final PublicFigure publicFigure, final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de figuras públicas", 
                    this.session, LogCategoryEnum.QUERY));
            return this.publicFigureBusiness.returnTotalPagesShowPublicFigure(
                    publicFigure.getStatus(), publicFigure.getType());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new PublicFigure();
    }
}
