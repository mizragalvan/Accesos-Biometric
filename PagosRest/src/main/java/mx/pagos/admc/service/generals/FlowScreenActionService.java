package mx.pagos.admc.service.generals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.FlowScreenActionBusiness;
import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FlowScreenActionService {
    private static final Logger LOG = Logger.getLogger(FlowScreenActionService.class);
    
	@Autowired
	private FlowScreenActionBusiness flowBuss;
    
	 @RequestMapping (value = UrlConstants.SEARCH_FLOWSCREENACTIONBYFLOW, method = RequestMethod.POST)
	 @ResponseBody
	 public final ConsultaList<FlowScreenAction> findFlowScreenActionByFlow(
	         @RequestBody final FlowScreenAction flowScreenAction, final HttpServletRequest request,
	         final HttpServletResponse  response) {
		 
		    LOG.info("FlowScreenActionService :: findFlowScreenActionByFlow");
	        try {
	             final ConsultaList<FlowScreenAction> listReturn = new ConsultaList<FlowScreenAction>();
	             listReturn.setList(this.flowBuss.findFlowScreenActionByFlow(flowScreenAction));
	             return listReturn;
	        } catch (BusinessException businessException) {
	            response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
	            response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
	            LOG.info(" BusinessException :: FlowScreenActionService :: findFlowScreenActionByFlow");
	            LOG.info(businessException.getMessage(), businessException);
	        }
	        return new ConsultaList<FlowScreenAction>();
	 }
}
