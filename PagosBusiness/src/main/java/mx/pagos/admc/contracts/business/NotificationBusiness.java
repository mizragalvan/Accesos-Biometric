package mx.pagos.admc.contracts.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Notifiable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Notification;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.NotificacionTypeEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;

@Service("NotificationBusiness")
public class NotificationBusiness extends AbstractExportable {

	private static final Logger LOG = Logger.getLogger(NotificationBusiness.class);
	private static final String ERROR = "Error al obtener las Notificaciones";
	private static final String DATE_FORMAT = "dd/MM/YYYY";

	@Autowired
	Notifiable notifiable;

	@Autowired
	Configurable configurable;

	@Autowired
	private RequisitionBusiness requisitionBusiness;

	@Autowired
	RequisitionStatusTurnBusiness requisitionStatusTurnBusiness;

	@Autowired
	private SimpUserRegistry simpUserRegistry;

	@Autowired
	private SessionRepository<? extends Session> webSession;

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		return null;
	}

	public final List<Notification> findNotificationsByIdUser(Integer idUser) throws BusinessException {
		try {
			return this.notifiable.findNotificationsByIdUser(idUser);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener Notificaciones vigentes", databaseException);
			throw new BusinessException(ERROR, databaseException);
		}
	}

	public final void updateStatusNotificacionById(Integer idNotification) throws BusinessException {
		try {
			LOG.info("=========== updateStatusNotificacionById() - CONSULTA NOTIFICACIÓN ===========");
			this.notifiable.updateNotification(idNotification, Boolean.TRUE);
		} catch (DatabaseException databaseException) {
			LOG.error("Error al actualizar la notificación", databaseException);
			throw new BusinessException(ERROR, databaseException);
		}
	}

	public final Notification createNotificacionByRequisition(RequisitionStatusTurn requisition)
			throws BusinessException {
		try {	
			LOG.info("=========== createNotificacionByRequisition ===========");
			requisition = this.requisitionStatusTurnBusiness.validActiveByIdRequisitionAndStatus(requisition);
			LOG.info("REQUISITION: " + requisition.getIdRequisition());
			if (requisition != null) {
				return this.createNotificationByRequisition(requisition.getIdRequisition(),
						this.getNotificacionTypeByFlowPurchasing(requisition.getStatus()),
						this.AddDaysToDate(new Date(), requisition.getAttentionDays()), null);
			}
		} catch (Exception e) {
			LOG.error("Error al crear la notificación por paso: " + e.getMessage());
			throw new BusinessException(ERROR, e);
		}
		return null;
	}

	public final Notification createNotificationByRequisition(Integer idRequisition, NotificacionTypeEnum type,
			Date date, Integer idUserRem) throws BusinessException {
		try {
			LOG.info("=========== NOTAFICACIÓN  POR REQUISICIÓN ===========");
			Integer idUser = null;
			if(idUserRem == null) 
				idUser = this.getIdUserByStatus(type, idRequisition);
			else
				idUser = idUserRem;

			Notification notification = new Notification();
			notification.setIdUser(idUser);
			notification.setMessage(replaceVariables(type, idRequisition.toString(), date));
			notification.setCreateDate(new Date());
			notification.setExpirationDate(date != null ? date : new Date());
			Integer idNotification = this.notifiable.saveNotification(notification);
			notification.setIdNotification(idNotification);

			String token = this.getTokenUser(notification.getIdUser());
//			if (token != null) {
				LOG.info(" REQ :: ["+idRequisition+"] - USR ::"+ notification.getIdUser()+"\n MSJ :: "+notification.getMessage());
				notification.setIdUserNotificaction(token);
				return notification;
//			} else {
//				LOG.error("ERROR AL ENVIAR NOTIFICACIÓN");
//				return null;
//			}
		} catch (DatabaseException databaseException) {
			LOG.error("Error al obtener Notificaciones vigentes", databaseException);
			throw new BusinessException(ERROR, databaseException);
		}
	}

	private String getTokenUser(Integer idUser) {
		try {
			for (SimpUser simpleUsser : simpUserRegistry.getUsers()) {
				UserSession ses = webSession.findById(simpleUsser.getName()).getAttribute("scopedTarget.UserSession");
				if (ses != null && ses.getIdUsuarioSession() != null) {
					if (ses.getIdUsuarioSession().toString().equals(idUser.toString())) {
						return simpleUsser.getName();
					}
				}
			}
			return null;
		}catch (Exception e) {
			LOG.info("NO SE ENCONTRO AL USUARIO CONECTADO ::("+idUser+")");
			return null;
		}
	}

	private String replaceVariables(NotificacionTypeEnum type, String idRequisition, Date date) {
		String cadena = getMessageByType(type);
		cadena = cadena.replace("%folio%", idRequisition);
		//cadena = cadena.replace("%fechaAlerta%", date.toString());
		return cadena;

	}

	private String formatDate(Date date) {
		if (date != null) {
			final SimpleDateFormat toDateTimeFormat = new SimpleDateFormat(DATE_FORMAT);
			return toDateTimeFormat.format(date);
		}
		return "";
	}

	private Integer getIdUserByStatus(NotificacionTypeEnum status, Integer folio) throws BusinessException {
		switch (status) {
		case SEND_DRAFT_GENERATION:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case SEND_NEGOTIATOR_CONTRACT:
			return this.requisitionBusiness.getIdApplicantByIdRequisition(folio);
		case SEND_LOAD_SUPPLIER_AREAS_APPROVAL:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case SEND_APROVED_BY_JURISTIC:
			return this.requisitionBusiness.getIdJuridico();
		case SEND_PRINT_CONTRACT:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case SEND_SACC_SIGN_CONTRACT:
			return this.requisitionBusiness.getIdApplicantByIdRequisition(folio);
		case SEND_SACC_SCAN_CONTRACT:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);

		case MODIFY_CONTRATO:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case CANCEL_CONTRACT_BY_APPLICANT:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case FINISH_CONTRACT_PROCESS:
			return this.requisitionBusiness.getIdApplicantByIdRequisition(folio);

		case START_DRAFT_GENERATION:
			return this.requisitionBusiness.getIdApplicantByIdRequisition(folio);
		case START_NEGOTIATOR_CONTRACT:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case START_LOAD_SUPPLIER_AREAS_APPROVAL:
			return this.requisitionBusiness.getIdApplicantByIdRequisition(folio);
		case START_APROVED_BY_JURISTIC:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case START_PRINT_CONTRACT:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case START_SACC_SIGN_CONTRACT:
			return this.requisitionBusiness.getIdLawyerByIdRequisition(folio);
		case START_SACC_SCAN_CONTRACT:
			return this.requisitionBusiness.getIdApplicantByIdRequisition(folio);

		default:
			break;
		}
		return null;
	}

	private NotificacionTypeEnum getNotificacionTypeByFlowPurchasing(FlowPurchasingEnum status) {
		switch (status) {
		case DRAFT_GENERATION:
			return NotificacionTypeEnum.START_DRAFT_GENERATION;
		case NEGOTIATOR_CONTRACT_REVIEW:
			return NotificacionTypeEnum.START_NEGOTIATOR_CONTRACT;
		case LOAD_SUPPLIER_AREAS_APPROVAL:
			return NotificacionTypeEnum.START_LOAD_SUPPLIER_AREAS_APPROVAL;
		case APROVED_BY_JURISTIC:
			return NotificacionTypeEnum.START_APROVED_BY_JURISTIC;
		case PRINT_CONTRACT:
			return NotificacionTypeEnum.START_PRINT_CONTRACT;
		case SACC_SIGN_CONTRACT:
			return NotificacionTypeEnum.START_SACC_SIGN_CONTRACT;
		case SACC_SCAN_CONTRACT:
			return NotificacionTypeEnum.START_SACC_SCAN_CONTRACT;
		default:
			break;
		}
		return null;
	}

	private Date AddDaysToDate(Date init, Integer days) {
		Calendar c = Calendar.getInstance();
		c.setTime(init);
		c.add(Calendar.DAY_OF_MONTH, days);
		return c.getTime();
	}

	private String getMessageByType (NotificacionTypeEnum type) {
		String name = type.toString();
		String message = null;
		try {
			message = this.configurable.findByName(name);
		} catch (Exception e) {
			message = null;
		}
		return message !=null ? message : type.getLabel();

	}

}
