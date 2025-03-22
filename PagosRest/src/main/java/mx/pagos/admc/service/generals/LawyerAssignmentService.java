package mx.pagos.admc.service.generals;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.pagos.admc.contracts.business.RequisitionBusiness;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

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
public class LawyerAssignmentService {

	private static final Logger LOG = Logger.getLogger(LawyerAssignmentService.class);
	
	 @Autowired
	 private RequisitionBusiness requisitionBusiness;
	
	 @RequestMapping (value = UrlConstants.SEARCH_LAWYER_ASSIGNMENT, method = RequestMethod.POST)
	 @ResponseBody
	 public final Requisition searchLawyerAssignment(@RequestBody final Requisition vo, 
			 		final HttpServletRequest request, final HttpServletResponse  response) {
		 
		 LOG.info(" @Controller -> LawyerAssignmentService :: searchLawyerAssignment - > Lawyer");
		 
		 try {
			 Requisition beanResp = new Requisition();
			 beanResp = this.requisitionBusiness.findById(vo.getIdRequisition());
			 return beanResp;
			 
		 } catch (BusinessException businessException) {
			 
			 response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
		     response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());

		     LOG.info(" @Controller -> BusinessException :: LawyerAssignmentService :: searchLawyerAssignment");
		     LOG.info(businessException.getMessage());
		     
		     return null;
		 }
	 }
}
