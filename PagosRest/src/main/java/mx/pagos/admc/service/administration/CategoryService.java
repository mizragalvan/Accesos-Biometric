package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.CategoriesBusiness;
import mx.pagos.admc.contracts.structures.Category;
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
public class CategoryService {
private static final Logger LOG = Logger.getLogger(CategoryService.class);
    
    @Autowired
    private CategoriesBusiness categoryBusiness;
    
    @Autowired
    private UserSession session;

    @Autowired
    private BinnacleBusiness binnacleBusiness;
    
    @RequestMapping(value = UrlConstants.CATEGORY_SAVE_OR_UPDATE, method = RequestMethod.POST)
    @ResponseBody
    public final Integer saveOrUpdate(@RequestBody final Category category, final HttpServletResponse response) {
        Integer idCategory = 0;
        try {
            idCategory = this.categoryBusiness.saveOrUpdate(category);
            LOG.debug("Guardado de Categoría exitoso " + idCategory);
            return idCategory;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
        }   
        return idCategory;
    }
    
    @RequestMapping(value = UrlConstants.CHANGE_CATEGORY_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final void changeCategoryStatus(@RequestBody final Category category, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se hará un filtro por IdCategory " + category.getIdCategory() + 
                    " y Estatus " + category.getStatus());
            this.categoryBusiness.changeCategoryStatus(category.getIdCategory(), category.getStatus());        
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());      
        }
    }
    
    @RequestMapping (value = UrlConstants.CATEGORY_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Category> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Categorías");
            final ConsultaList<Category> categoryList = new ConsultaList<Category>();
            categoryList.setList(this.categoryBusiness.findAll());
            return categoryList;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Category>();
    }
    
    @RequestMapping (value = UrlConstants.CATEGORY_FIND_BY_RECORD_STATUS, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Category> findByRecordStatus(@RequestBody final String status, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Categorías por estatus" + status);
            final ConsultaList<Category> categoryList = new ConsultaList<Category>();
            categoryList.setList(this.categoryBusiness.findByRecordStatus(RecordStatusEnum.valueOf(status)));
            return categoryList;
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Category>();
    }
    
    @RequestMapping (value = UrlConstants.CATEGORY_FIND_BY_ID, method = RequestMethod.POST)
    @ResponseBody
    public final Category findById(@RequestBody final Category category, 
            final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener Categoría por id" + category.getIdCategory());
            return this.categoryBusiness.findById(category.getIdCategory());
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Category();
    }
    
    @RequestMapping (value = UrlConstants.FIND_CHECK_DOCUMENTATION_BY_CATEGORY, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Integer> findCheckDocumentationIdsByCategory(@RequestBody final Integer idCategory, 
            final HttpServletResponse response) {
        final ConsultaList<Integer> idList = new ConsultaList<>();
        try {
            idList.setList(this.categoryBusiness.findCheckDocumentationIdsByCategory(idCategory));
            return idList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return idList;
    }
    
    @RequestMapping (value = UrlConstants.CATEGORY_FIND_ALL_CATALOG_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Category> findAllCategoryCatalogPaged(@RequestBody final Category category, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta de todos las categorías de documentos", this.session, LogCategoryEnum.QUERY));
            final ConsultaList<Category> categoryList = new ConsultaList<>();
            categoryList.setList(this.categoryBusiness.findCategoryCatalogPaged(category));
            return categoryList;
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Category>();
    }

    @RequestMapping (value = UrlConstants.CATEGORY_FIND_TOTAL_PAGES, method = RequestMethod.POST)
    @ResponseBody
    public final Category returnTotalRowsOfCategory(@RequestBody final Category category, 
            final HttpServletResponse response) {
        try {
            this.binnacleBusiness.save(LoggingUtils.createBinnacleForLogging(
                    "Consulta del número de paginas de catálogo de categorías de documentos",
                    this.session, LogCategoryEnum.QUERY));
            return this.categoryBusiness.returnTotalPagesShowDga(category.getStatus());
        } catch (BusinessException businessException) {
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new Category();
    }
}
