package mx.pagos.admc.service.administration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mx.pagos.admc.contracts.business.NotificationBusiness;
import mx.pagos.admc.contracts.structures.Notification;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.admc.util.shared.UrlConstants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.security.structures.UserSession;

@Controller
public class NotificationService {
	private static final Logger LOG = Logger.getLogger(NotificationService.class);

	@Autowired
	private NotificationBusiness notificationBusiness;

	@Autowired
	private UserSession session;

	@Autowired
	private SimpMessagingTemplate template;

	@RequestMapping(value = UrlConstants.ACTIVE_NOTIFICATIONS_BY_USER, method = RequestMethod.POST)
	@ResponseBody
	public final ConsultaList<Notification> findNotificationsByIdUser(final HttpServletResponse response) {
		LOG.info("NotificationService :: findNotificationsByIdUser: " + this.session.getUsuario().getIdUser());
		try {
			final ConsultaList<Notification> noticeList = new ConsultaList<Notification>();
			noticeList.setList(
					this.notificationBusiness.findNotificationsByIdUser(this.session.getUsuario().getIdUser()));
			return noticeList;
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info("BusinessException :: NotificationService :: findNotificationsByIdUser");
			LOG.info(businessException.getMessage(), businessException);
		}
		return new ConsultaList<Notification>();
	}

	@RequestMapping(value = UrlConstants.UPDATE_NOTIFICATION_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean updateStatusNotificacion(@RequestBody final Integer idNotification,
			final HttpServletResponse response) {
		LOG.info("NotificationService :: updateStatusNotificacion");
		try {
			this.notificationBusiness.updateStatusNotificacionById(idNotification);
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info("BusinessException :: NotificationService :: updateStatusNotificacion");
			LOG.info(businessException.getMessage(), businessException);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@RequestMapping(value = UrlConstants.SEND_NOTIFICATION_BY_USER_AND_STEP, method = RequestMethod.POST)
	@ResponseBody
	public final Boolean sendNotificationBySTEP (@RequestBody final RequisitionStatusTurn requisition, final HttpServletResponse response) {
		LOG.info("NotificationService :: sendNotificationBySTEP");
		try {
			Notification notification = this.notificationBusiness.createNotificacionByRequisition(requisition);			
			if(notification!=null) {				
				this.template.convertAndSend("/topic/resp/notifications", notification);
				LOG.info("NOTIFICACIÓN  OK");
			}
		} catch (BusinessException businessException) {
			response.setHeader(Constants.HEADER_STATUS, Constants.COD_STATUS_ERROR);
			response.setHeader(Constants.HEADER_MESSAGE, businessException.getMessage());
			LOG.info("BusinessException :: NotificationService :: sendNotificationBySTEP");
			LOG.info(businessException.getMessage(), businessException);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}