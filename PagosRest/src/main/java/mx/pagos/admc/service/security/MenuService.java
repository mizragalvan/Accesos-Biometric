package mx.pagos.admc.service.security;

import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.security.business.MenuBusiness;
import mx.pagos.security.structures.Menu;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MenuService {    
    private static final Logger LOG = Logger.getLogger(MenuService.class);
    
    @Autowired
    private MenuBusiness menuBusiness;
    
    @RequestMapping (value = UrlConstants.MENU_FIND_ALL, method = RequestMethod.POST)
    @ResponseBody
    public final ConsultaList<Menu> findAll(final HttpServletResponse response) {
        try {
            LOG.debug("Se va a obtener la lista de Menus");
            
            final ConsultaList<Menu> list=new ConsultaList<Menu>();
            list.setList(this.menuBusiness.findAll());
            return list;            
        } catch (BusinessException businessException) {
            LOG.error(businessException.getMessage(), businessException);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());          
        }
        return new ConsultaList<Menu>();
    }
    
    
 
}
