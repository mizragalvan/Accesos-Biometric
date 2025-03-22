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

import mx.pagos.admc.contracts.business.RedFlagBusiness;
import mx.pagos.admc.contracts.structures.RedFlag;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.security.structures.UserSession;

@Controller
public class RedFlagService {
	private static final Logger LOG = Logger.getLogger(RedFlagService.class);
	
	@Autowired
    private RedFlagBusiness redBusiness;
	@Autowired
	private UserSession session;
    
    @RequestMapping(value = UrlConstants.SAVE_REDFLAG, method = RequestMethod.POST)
    @ResponseBody
    public final List<RedFlag> saveOrUpdate(@RequestBody final RedFlag redFlag, final HttpServletResponse response) {
        try {
            return this.redBusiness.save(redFlag, this.session.getUsuario().getIdUser());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
        }
		return null;
    }
    
    @RequestMapping (value = UrlConstants.FIND_REDFLAG, method = RequestMethod.POST)
    @ResponseBody
    public final List<RedFlag> findByIdRequisition(@RequestBody final RedFlag redFlag, final HttpServletResponse response) {
        try {
        	return this.redBusiness.findByIdRequisition(redFlag != null ? redFlag.getIdRequisition() : 0);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
            response.setHeader(Constants.HEADER_MESSAGE, e.getMessage());
            return null;
        }
        
    }    
    

}
