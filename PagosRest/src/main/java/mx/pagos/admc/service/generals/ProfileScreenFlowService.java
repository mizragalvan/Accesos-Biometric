package mx.pagos.admc.service.generals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.ProfileScreenFlowBusiness;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.security.structures.ProfileScreenFlow;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileScreenFlowService {
    private static final Logger LOG = Logger.getLogger(ProfileScreenFlowService.class);
    
	@Autowired
	private ProfileScreenFlowBusiness flowBuss;
    
	 @RequestMapping (value = UrlConstants.FIND_FLOWSCREENACTIONBYPROFILE, method = RequestMethod.POST)
	 @ResponseBody
	 public final ConsultaList<ProfileScreenFlow> findFlowScreenActionByProfile(@RequestBody final ProfileScreenFlow bean,final HttpServletRequest request, final HttpServletResponse  response) {
		 
		    LOG.info("ProfileScreenFlowService :: findFlowScreenActionByProfile");
	        try {
	             final ConsultaList<ProfileScreenFlow> listReturn = new ConsultaList<ProfileScreenFlow>();
	             listReturn.setList(this.flowBuss.findFlowScreenActionByProfile(bean));
	             return listReturn;
	        } catch (BusinessException businessException) {
	            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
	            LOG.info(" BusinessException :: ProfileScreenFlowService :: findFlowScreenActionByProfile");
	            LOG.info(businessException.getMessage(), businessException);
	        }
	        return new ConsultaList<ProfileScreenFlow>();
	 }
}
