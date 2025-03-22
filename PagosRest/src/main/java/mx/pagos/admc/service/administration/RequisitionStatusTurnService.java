package mx.pagos.admc.service.administration;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.RequisitionStatusTurnBusiness;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;

@Controller
public class RequisitionStatusTurnService {

	private static final Logger LOG = Logger.getLogger(RequisitionStatusTurnService.class);

	@Autowired
	private RequisitionStatusTurnBusiness requisitionStatusBusiness;

	@RequestMapping(value = UrlConstants.GET_REQUISITION_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean getRequisitionStatus(@RequestBody final RequisitionStatusTurn requisition,
			final HttpServletResponse response) {
		LOG.info("NotificationService :: sendNotificationBySTEP");
		try {
			return this.requisitionStatusBusiness.validByIdRequisitionAndStatus(requisition.getIdRequisition(),
					FlowPurchasingEnum.APROVED_BY_JURISTIC);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info("BusinessException :: NotificationService :: sendNotificationBySTEP");
			LOG.info(businessException.getMessage(), businessException);
			return Boolean.FALSE;
		}
	}

}
