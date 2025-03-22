package mx.pagos.admc.contracts.business;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.engineer.utils.general.SubparagraphUtils;
import mx.engineer.utils.mail.structures.EmailContent;
import mx.pagos.admc.contracts.interfaces.Alertable;
import mx.pagos.admc.contracts.interfaces.Requisitable;
import mx.pagos.admc.contracts.structures.Alert;
import mx.pagos.admc.contracts.structures.AlertConfigurationDay;
import mx.pagos.admc.contracts.structures.AlertDocumentType;
import mx.pagos.admc.contracts.structures.AlertFlowStep;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.core.business.EmailsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.business.UsersBusiness;

@Service
public class AlertsBusiness {
	private static final String BEFORE = "before";
	private static final String MESSAGE_RECOVERY_EMAILS_ERROR = "Hubo un problema al recuperar los emails";
	private static final String MESSAGE_SAVING_ALERT_ERROR = "Error al guardar datos del Alerta";
	private static final String MESSAGE_RETRIEVING_ALL_ALERTS_ERROR = "Error al obtener lista de Alertas";
	private static final String MESSAGE_ALERT_NO_LONGER_EXISTS = "La alerta ha dejado de existir";
	private static final String MESSAGE_RETRIEVING_ALERTS_BY_FLOW_AND_STATUS_ERROR = "Error al obtener lista de Alertas por flujo y estatus";
	private static final String MESSAGE_DELETING_ALERT_ERROR = "Error al Borrar Alerta";
	private static final String MESSAGE_SAVING_DAY_ALERTS_ERROR = "Hubo un problema al guadar las alertas por día";
	private static final String MESSAGE_RETRIVING_VALIDITY_DAYS_ERROR = "Hubo un problema al recuperar la vigencia de la solicitud";
	private static final String MESSAGE_SEND_COMPLETION_ALERT_MAIL_ERROR = "Hubo un problema al enviar alerta de finalización del contrato";
	private static final String MESSAGE_SEND_ALERTS_EMAILS = "Hubo un problema al enviar alertas";
	private static final String MESSAGE_FIND_ALERT_CONFLICTS_ERROR = "Hubo un problema al validar si la configuración de la alerta conflictua con otra alerta";
	private static final String MESSAGE_FIND_CONTRACTS_TO_EXPIRE_FOR_ALERTS_ERROR = "Hubo un problema al recuperar contratos por vencer para el envío de alertas";

	private static final Logger LOG = Logger.getLogger(AlertsBusiness.class);
	private String comma = ",";

	@Autowired
	private Alertable alertable;

	@Autowired
	private Requisitable requisitable;

	@Autowired
	private ConfigurationsBusiness configuration;

	@Autowired
	private EmailsBusiness emailsBusiness;

	@Autowired
	private UsersBusiness userBusiness;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public Integer saveOrUpdate(final Alert alert) throws BusinessException {
		try {
			LOG.debug("Se guardará alerta: " + alert.getName());
			final Integer idAlert = this.alertable.saveOrUpdate(alert);
			alert.setIdAlert(idAlert);
			LOG.debug("El id de la alerta es: " + idAlert);
			this.saveAlertconfigurationDay(alert);
			this.saveDocumentTypes(alert);
			return idAlert;
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_SAVING_ALERT_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_SAVING_ALERT_ERROR, dataBaseException);
		}
	}

	public List<Alert> findAll() throws BusinessException {
		try {
			LOG.debug("Se obtendra una lista de Alertas");
			return this.alertable.findAll();
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_RETRIEVING_ALL_ALERTS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_ALL_ALERTS_ERROR, dataBaseException);
		}
	}

	public Alert findById(final Integer idAlert) throws BusinessException {
		try {
			LOG.debug("Se obtendra Alerta por id");
			final Alert alert = this.alertable.findById(idAlert);
			alert.setAlertConfigurationDaysList(this.alertable.findAlertConfigurationDaysByIdAlert(idAlert));
			alert.setDocumentTypesList(this.alertable.findActiveDocumentTypesByIdAlert(idAlert));
			return alert;
		} catch (DatabaseException dataBaseException) {
			LOG.error(dataBaseException.getMessage(), dataBaseException);
			throw new BusinessException("Error al obtener Áreas por id", dataBaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_ALERT_NO_LONGER_EXISTS, emptyResultException);
			throw new BusinessException(MESSAGE_ALERT_NO_LONGER_EXISTS, emptyResultException);
		}
	}

	public List<Alert> findbyFlowStatus(final Alert alert) throws BusinessException {
		try {
			LOG.debug("Se obtendra una lista de Alertas por flujo y estatus");
			return this.alertable.findbyFlowStatus(alert);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_RETRIEVING_ALERTS_BY_FLOW_AND_STATUS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_ALERTS_BY_FLOW_AND_STATUS_ERROR, dataBaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	public void deleteAlert(final Alert alert) throws BusinessException {
		try {
			LOG.debug("Se Borra Alerta");
			this.alertable.deleteAlertConfigurationDaysByIdAlert(alert.getIdAlert());
			this.alertable.deleteAlert(alert);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_DELETING_ALERT_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_DELETING_ALERT_ERROR, dataBaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	private void saveAlertconfigurationDay(final Alert alert) throws BusinessException {
		try {
			this.alertable.deleteAlertConfigurationDaysByIdAlert(alert.getIdAlert());
			for (AlertConfigurationDay alertConfigurationDay : alert.getAlertConfigurationDaysList()) {
				alertConfigurationDay.setIdAlert(alert.getIdAlert());
				this.alertable.saveAlertconfigurationDay(alertConfigurationDay);
			}
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_SAVING_DAY_ALERTS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_SAVING_DAY_ALERTS_ERROR, dataBaseException);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
	private void saveDocumentTypes(final Alert alert) throws BusinessException {
		try {
			this.alertable.deleteDocumentTypesByIdAlert(alert.getIdAlert());
			for (Integer idDocumentType : alert.getDocumentTypesList())
				this.alertable.saveDocumentType(alert.getIdAlert(), idDocumentType);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_SAVING_DAY_ALERTS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_SAVING_DAY_ALERTS_ERROR, dataBaseException);
		}
	}

	public List<AlertFlowStep> getEmailsToAlertsByStep(final Integer idRequisition, final FlowPurchasingEnum flowStatus)
			throws BusinessException {
		try {
			return this.alertable.getEmailsToAlertsByStep(idRequisition, flowStatus);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_RECOVERY_EMAILS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_RECOVERY_EMAILS_ERROR, dataBaseException);
		}
	}

	public List<AlertFlowStep> getOwnersEmailsToAlertByStep(final Integer idRequisitionOwners,
			final FlowPurchasingEnum flowStatus) throws BusinessException {
		try {
			return this.alertable.getOwnersEmailsToAlertByStep(idRequisitionOwners, flowStatus);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RECOVERY_EMAILS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RECOVERY_EMAILS_ERROR, databaseException);
		}
	}

	public Integer findValidityDaysByRequisitionFlowStatusTurn(final Integer idRequisitionParam,
			final FlowPurchasingEnum status, final Integer turn) throws BusinessException {
		try {
			return this.alertable.findValidityDaysByRequisitionFlowStatusTurn(idRequisitionParam, status, turn);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_RETRIVING_VALIDITY_DAYS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_RETRIVING_VALIDITY_DAYS_ERROR, dataBaseException);
		}
	}

	public Integer findValidityDaysByFlowStatusTurn(final Integer idFlow, final FlowPurchasingEnum status,
			final Integer turn) throws BusinessException {
		try {
			return this.alertable.findValidityDaysByFlowStatusTurn(idFlow, status, turn);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_RETRIVING_VALIDITY_DAYS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_RETRIVING_VALIDITY_DAYS_ERROR, dataBaseException);
		}
	}

	public void sendContractsToExpireForAlerts() throws BusinessException {
		final List<RequisitionOwners> ownersList = this.findContractsToExpireForAlerts();
		for (final RequisitionOwners owner : ownersList) {
			if (BEFORE.equals(owner.getDateValue()))
				this.emailsBusiness.sendEmail(
						this.createEmailContractOwnerContent(owner,
								this.configuration.findByName(ConfigurationEnum.ENDING_CONTRACTS_SUBJET_EMAIL.toString()),
								this.configuration.findByName("BEFORE_EXPIRATION_CONTRACT_OWNER_CONTENT_EMAIL")),
						this.getEmailsTo(owner.getBusinessManEMail()));
			else
				this.emailsBusiness.sendEmail(
						this.createEmailContractOwnerContent(owner,
								this.configuration.findByName(ConfigurationEnum.ENDING_CONTRACTS_SUBJET_EMAIL.toString()),
								this.configuration.findByName("AFTER_EXPIRATION_CONTRACT_OWNER_CONTENT_EMAIL")),
						this.getEmailsTo(owner.getBusinessManEMail()));
		}
	}

	private EmailContent createEmailContractOwnerContent(final RequisitionOwners requisition, final String subject,
			final String content) throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject(subject);
		emailContent.setFieldOne("FOLIO:");
		emailContent.setFieldOneDescription(requisition.getIdRequisitionOwners().toString());
		emailContent.setFieldTwo("Tipo de Documento:");
		emailContent.setFieldTwoDescription(requisition.getCategoryName());
		emailContent.setFieldThree("Ejecutivo:");
		emailContent.setFieldThreeDescription(requisition.getBusinesmanName());
		emailContent.setSendDateDescription("FECHA DE ENTREGA:");
		emailContent.setSendDate(this.obtenerFecha());
		emailContent.setContent(content);
		emailContent.setBrand(this.configuration.findByName(ConfigurationEnum.EMAIL_NOTIFICATION_BRAND.toString()));
		return emailContent;
	}

	public void sendcompletionAlert() throws BusinessException, ParseException {
		try {
			final List<Requisition> list = this.requisitable.findClosedRequisitionUnattended(
					-Integer.valueOf(this.configuration.findByName("BEFORE_DAYS_EXPIRATION_ALERT")),
					Integer.valueOf(this.configuration.findByName("AFTER_DAYS_EXPIRATION_ALERT")));
			for (Requisition requisition : list) {
				if (BEFORE.equals(requisition.getDateValue()))
					this.emailsBusiness.sendEmail(
							this.createEmailContent(requisition,
									this.configuration.findByName(ConfigurationEnum.ENDING_CONTRACTS_SUBJET_EMAIL.toString()),
									this.configuration.findByName("BEFORE_EXPIRATION_CONTENTMAIL")),
							this.getEmailsTo(requisition.getEmailApplicant()));
				else
					this.emailsBusiness.sendEmail(
							this.createEmailContent(requisition,
									this.configuration.findByName(ConfigurationEnum.ENDING_CONTRACTS_SUBJET_EMAIL.toString()),
									this.configuration.findByName("AFTER_EXPIRATION_CONTENTMAIL")),
							this.getEmailsTo(requisition.getEmailApplicant()));
			}
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SEND_COMPLETION_ALERT_MAIL_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SEND_COMPLETION_ALERT_MAIL_ERROR, databaseException);
		}
	}

	private EmailContent createEmailContent(final Requisition requisition, final String subject, final String content)
			throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent.setSubject(subject);
		emailContent.setFieldOne("FOLIO:");
		emailContent.setFieldOneDescription(requisition.getIdRequisition().toString());
		emailContent.setFieldTwo("Tipo de Documento:");
		emailContent
				.setFieldTwoDescription(requisition.getDocumentTypeName() == null ? "" : requisition.getDocumentTypeName());
		emailContent.setFieldThree("Negociador:");
		emailContent
				.setFieldThreeDescription(requisition.getFullNameApplicant() == null ? "" : requisition.getFullNameApplicant());
		emailContent.setFieldFour("Flujo:");
		emailContent
				.setFieldFourDescription(requisition.getStatus().toString() == null ? "" : requisition.getStatus().toString());
		emailContent.setSendDateDescription("FECHA DE ENTREGA:");
		emailContent.setSendDate(this.obtenerFecha());
		emailContent.setContent(content);
		emailContent.setBrand(this.configuration.findByName(ConfigurationEnum.EMAIL_NOTIFICATION_BRAND.toString()));
		return emailContent;
	}

	private String[] getEmailsTo(final String applicantEmail) throws BusinessException {
		final String emails = this.configuration.findByName("EXPIRATION_MAILS") + this.comma + applicantEmail;
		final String[] emailArray = emails.split(this.comma);
		return emailArray;
	}

	public void sendServiceLevelsAlerts() throws BusinessException {
		try {
			final List<Integer> idRequisitionOwnerList = new ArrayList<>();
			final List<Alert> alertList = this.alertable.findOwnersServiceLevelsAlerts();
			this.sendMailsAlerts(idRequisitionOwnerList, alertList, true);
			this.sendRequisitionOwnersUnderdirectorAlertsWhenThereAre(idRequisitionOwnerList);
		} catch (DatabaseException databaseException) {
			LOG.error(databaseException);
			throw new BusinessException(databaseException);
		}
	}

	private void sendRequisitionOwnersUnderdirectorAlertsWhenThereAre(final List<Integer> idRequisitionOwnerList)
			throws DatabaseException, BusinessException {
		if (idRequisitionOwnerList.size() > 0) {
			final List<Alert> alertDirectorMail = this.alertable
					.findRequisitionOwnersUnderDirectorMail(idRequisitionOwnerList);
			for (final Alert alert : alertDirectorMail)
				this.alertUnderDirector(alertDirectorMail, alert, true);
		}
	}

	public void sendAlerts() throws BusinessException {
		try {
			final List<Integer> idRequisitionList = new ArrayList<Integer>();
			final List<Alert> alertsList = this.alertable.findAlerts();
			this.sendMailsAlerts(idRequisitionList, alertsList, false);
			this.sendAlertWhenThereAreAlerts(idRequisitionList, alertsList);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SEND_ALERTS_EMAILS, databaseException);
			throw new BusinessException(MESSAGE_SEND_ALERTS_EMAILS, databaseException);
		}
	}

	private void sendAlertWhenThereAreAlerts(final List<Integer> idRequisitionList, final List<Alert> alertsList)
			throws DatabaseException, BusinessException {
		if (idRequisitionList.size() > 0) {
			final List<Alert> alertDirectorMail = this.alertable.findUnderDirectorMailSendAlert(idRequisitionList);
			for (Alert alert : alertsList)
				this.alertUnderDirector(alertDirectorMail, alert, false);
		}
	}

	public List<AlertDocumentType> findAlertConflicts(final Alert alert) throws BusinessException {
		try {
			return this.alertable.findAlertConflicts(alert);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_ALERT_CONFLICTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_ALERT_CONFLICTS_ERROR, databaseException);
		}
	}

	private void alertUnderDirector(final List<Alert> alertDirectorMail, final Alert alert, final Boolean isOwner)
			throws BusinessException {
		for (Alert alertDirector : alertDirectorMail)
			if (alert.getIdRequisition().equals(alertDirector.getIdRequisition()))
				this.sendEmail(alert, alertDirector.getUnderDirectorEmail(), isOwner);
	}

	private void sendMailsAlerts(final List<Integer> idRequisitionSendUnderDirectorMailList, final List<Alert> alertList,
			final Boolean isOwner) throws BusinessException {
		for (Alert alert : alertList) {
			this.validateEmailListNotNull(alert, isOwner);
			this.validateIsUnderDirectorSendMail(idRequisitionSendUnderDirectorMailList, alert);
		}
	}

	private void validateIsUnderDirectorSendMail(final List<Integer> idRequisitionSendUnderDirectorMailList,
			final Alert alert) {
		if (alert.getIsUserSubdirectorEmailSend()
				&& !idRequisitionSendUnderDirectorMailList.contains(alert.getIdRequisition()))
			idRequisitionSendUnderDirectorMailList.add(alert.getIdRequisition());
	}

	private void validateEmailListNotNull(final Alert alert, final Boolean isOwner) throws BusinessException {
		if (alert.getEmailsList() != null)
			this.sendEmail(alert, alert.getEmailsList(), isOwner);
	}

	private void sendEmail(final Alert alert, final String emailList, final Boolean isOwner) throws BusinessException {
		this.emailsBusiness.sendEmail(this.createEmailAlertContent(alert, isOwner), this.listEmails(emailList));
	}

	private String[] listEmails(final String emailList) throws BusinessException {
		final String[] list = emailList.split(this.comma);
		return list;
	}

	private EmailContent createEmailAlertContent(final Alert alert, final Boolean isOwner) throws BusinessException {
		final EmailContent emailAlertContent = new EmailContent();
		emailAlertContent.setSubject(this.configuration.findByName(ConfigurationEnum.SUBJECT_ALERT_MESSAGE.toString()));
		emailAlertContent.setFieldOne("FOLIO:");
		if (isOwner) {
			emailAlertContent.setFieldOneDescription(alert.getIdRequisitionOwners().toString());
			emailAlertContent.setFieldTwo("Cliente:");
			emailAlertContent
					.setFieldTwoDescription(alert.getCustomerCompanyName() == null ? "" : alert.getCustomerCompanyName());
		} else {
			emailAlertContent.setFieldOneDescription(alert.getIdRequisition().toString());
			emailAlertContent.setFieldTwo("Descripción del servicio:");
			emailAlertContent
					.setFieldTwoDescription(alert.getServiceDescription() == null ? "" : alert.getServiceDescription());
			emailAlertContent.setFieldThree("Proveedor:");
			emailAlertContent.setFieldThreeDescription(alert.getCommercialName() == null ? "" : alert.getCommercialName());
			emailAlertContent.setFieldFour("Flujo:");
			emailAlertContent.setFieldFourDescription(alert.getStatus() == null ? "" : alert.getStatus());
			emailAlertContent.setSendDateDescription("FECHA DE ENTREGA:");
			emailAlertContent.setSendDate(this.obtenerFecha());
		}
		emailAlertContent.setContent(alert.getMailContent() == null ? "" : alert.getMailContent());
		emailAlertContent.setBrand(this.configuration.findByName(ConfigurationEnum.EMAIL_NOTIFICATION_BRAND.toString()));
		return emailAlertContent;
	}

	public List<RequisitionOwners> findContractsToExpireForAlerts() throws BusinessException {
		try {
			final RequisitionOwners requisitionOwners = new RequisitionOwners();
			this.setExpirationDays(requisitionOwners);
			return this.alertable.findContractsToExpireForAlerts(requisitionOwners);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_CONTRACTS_TO_EXPIRE_FOR_ALERTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_CONTRACTS_TO_EXPIRE_FOR_ALERTS_ERROR, databaseException);
		}
	}

	private void setExpirationDays(final RequisitionOwners requisitionOwners) throws BusinessException {
		requisitionOwners.setBeforeDaysExpirationAlert(
				-1 * Integer.valueOf(this.configuration.findByName(ConfigurationEnum.BEFORE_DAYS_EXPIRATION_ALERT.toString())));
		requisitionOwners.setAfterDaysExpirationAlert(
				Integer.valueOf(this.configuration.findByName(ConfigurationEnum.AFTER_DAYS_EXPIRATION_ALERT.toString())));
	}

	public void sendNotificationToManagerSystem(final Integer idUser, final String notificationContent)
			throws BusinessException, UnsupportedEncodingException {
		this.emailsBusiness.sendEmail(
				this.createNotificationToManagerSystemMail(this.userBusiness.findByUserId(idUser).getFullName(),
						SubparagraphUtils.convertToUTF8(notificationContent)),
				this.configuration.findByName(ConfigurationEnum.MAIL_LIST_TO_SEND_NOTIFICATION_BY_CONTROL_PANE.toString())
						.split(this.comma));
	}

	private EmailContent createNotificationToManagerSystemMail(final String userFullName,
			final String notificationContent) throws BusinessException {
		final EmailContent emailContent = new EmailContent();
		emailContent
				.setSubject(this.configuration.findByName(ConfigurationEnum.CONTROL_PANE_NOTIFICATION_SUBJECTMAIL.toString()));
		emailContent.setFieldOne("Notificacion del Usuario:");
		emailContent.setFieldOneDescription(userFullName);
		emailContent.setContent(notificationContent);
		emailContent.setBrand(this.configuration.findByName(ConfigurationEnum.EMAIL_NOTIFICATION_BRAND.toString()));
		return emailContent;
	}

	private LocalDate obtenerFecha() {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDate fechaEnvio = currentTime.toLocalDate();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/uuuu");
		String textFechaEnvio = fechaEnvio.format(formatters);
		LocalDate parsedDate = LocalDate.parse(textFechaEnvio, formatters);

		return parsedDate;
	}
}
