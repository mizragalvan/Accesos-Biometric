package mx.pagos.admc.service.generals;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.AnexoBusiness;
import mx.pagos.admc.contracts.business.RedFlagBusiness;
import mx.pagos.admc.contracts.structures.FolioByPlantilla;
import mx.pagos.admc.contracts.structures.RedFlag;
import mx.pagos.admc.contracts.structures.TagField;
import mx.pagos.admc.contracts.structures.TypeByAnexo;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.security.structures.UserSession;

@Controller
public class AnexoService {
	private static final Logger LOG = Logger.getLogger(AnexoService.class);
	
	@Autowired
    private AnexoBusiness anexoBusiness;
	
	@Autowired
	private UserSession session;
    
    @RequestMapping(value = UrlConstants.SAVE_TYPE_ANEXO, method = RequestMethod.POST)
    @ResponseBody
    public final TypeByAnexo saveOrUpdate(@RequestBody final TypeByAnexo typeByAnexo, final HttpServletResponse response) {
        try {
            return this.anexoBusiness.save(typeByAnexo);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
            return null;
        }
    }
    
    @RequestMapping (value = UrlConstants.FIND_TYPES_ANEXOS, method = RequestMethod.POST)
    @ResponseBody
    public final List<TypeByAnexo> findTypesByAnexosAndPerson(@RequestBody final TypeByAnexo typeByAnexo, final HttpServletResponse response) {
        try {
        	return this.anexoBusiness.findTypesByAnexosAndPerson(typeByAnexo);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
            return null;
        }
     
    }
    
    @RequestMapping(value = UrlConstants.FIND_TAGS_ANEXOS, method = RequestMethod.POST)
    @ResponseBody
    public final List<TagField> findTagsAnexos(final HttpServletResponse response) {
      try {
    	  return this.anexoBusiness.findTagsAnexos();
      } catch (Exception e) {
    	  LOG.error(e.getMessage(), e);
          response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
          response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
          return null;	  
	 }
    }
    
    @RequestMapping (value = UrlConstants.FIND_REQUISITION_BY_IDDOCUMENTTYPE, method = RequestMethod.POST)
    @ResponseBody
    public final List<FolioByPlantilla> findRequisitionByIdDocumentType(@RequestBody final TypeByAnexo typeByAnexo, final HttpServletResponse response) {
        try {
        	return this.anexoBusiness.findFolioByTypeDocument(typeByAnexo);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
            return null;
        }
        
    }
    
    @RequestMapping (value = UrlConstants.DELETE_TYPE_ANEXO, method = RequestMethod.POST)
    @ResponseBody
    public final TypeByAnexo deleteTypeByAnexo(@RequestBody final TypeByAnexo typeByAnexo, final HttpServletResponse response) {
        try {
        	return this.anexoBusiness.delete(typeByAnexo);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
            return null;
        }
        
    } 
}