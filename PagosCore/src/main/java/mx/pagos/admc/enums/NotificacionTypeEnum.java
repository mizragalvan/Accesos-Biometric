package mx.pagos.admc.enums;

public enum NotificacionTypeEnum {

	SEND_DRAFT_GENERATION(1, "La solicitud con folio: %folio% le ha sido asignada ."),
	SEND_NEGOTIATOR_CONTRACT(2, "La solicitud con folio: %folio% le ha sido asignada (Revisión Contrato Solicitado)."),
	SEND_LOAD_SUPPLIER_AREAS_APPROVAL(3, "La solicitud con folio: %folio% le ha sido asignada (Contratos para VoBo Jurídico)."),
	SEND_APROVED_BY_JURISTIC(4, "La solicitud con folio: %folio% le ha sido asignada (VoBo Jurídico)."),
	SEND_PRINT_CONTRACT(5, "El contrato con folio:  %folio% se encuentra listo para impresión"),
	SEND_SACC_SIGN_CONTRACT(6, "El contrato con folio: %folio% se ha enviado a firmas."),
	SEND_SACC_SCAN_CONTRACT(7, "El contrato con folio: %folio% se ha enviado a Digitalización."),

	MODIFY_CONTRATO(8, "Se le ha solicitado realizar modificaciones a la solicitud con folio: %folio%."),
	CANCEL_CONTRACT_BY_APPLICANT(9, "Se ha cancelado la solicitud con folio: %folio%."),
	FINISH_CONTRACT_PROCESS(10, "Se ha finalizado el proceso folio: %folio%."),

	START_DRAFT_GENERATION(11, "Folio: %folio% se encuentra en proceso (Solicitud de Contrato)."),
	START_NEGOTIATOR_CONTRACT(12, "La solicitud con folio: %folio% se encuentra en proceso (Revisión Contrato Solicitado)."),
	START_LOAD_SUPPLIER_AREAS_APPROVAL(13, "La solicitud con folio: %folio% se encuentra en proceso (Contratos para VoBo Jurídico)."),
	START_APROVED_BY_JURISTIC(14, "La solicitud con folio: %folio% se encuentra en proceso (VoBo Jurídico)."),
	START_PRINT_CONTRACT(15, "El contrato: %folio% se ha descargado."),
	START_SACC_SIGN_CONTRACT(16, "El contrato: %folio% se encuentra en proceso de firmas."),
	START_SACC_SCAN_CONTRACT(17, "El contrato: %folio% se encuentra en proceso de digitalización."), 

	SEND_DRAFT_GENERATION_REMITE(18, "La solicitud con folio: %folio% le ha sido asignada ."),
	MODIFY_CONTRATO_REMITE(19, "Se le ha solicitado realizar modificaciones a la solicitud con folio: %folio%."),
	SEND_LOAD_SUPPLIER_AREAS_APPROVAL_REMITE(20, "La solicitud con folio: %folio% le ha sido asignada (Contratos para VoBo Jurídico)."),
	CHANGE_STATUS_SUBJECTEMAIL(21, ""),
	SEND_DRAFT_GENERATION_LWR(22, ""),
	SEND_DRAFT_GENERATION_USR(23, ""),
	SEND_NEGOTIATOR_CONTRACT_USR(24, ""),
	SEND_APROVED_BY_JURISTIC_JRD(25, ""),
	SEND_SACC_SIGN_CONTRACT_USR(26, ""),
	SEND_SACC_SCAN_CONTRACT_USR(27, ""),
	FINISH_CONTRACT_PROCESS_USR(28, ""),
	
	REJECTION_BY_JURISTIC(29, "Se ha rechazado por jurídico, solicitud con folio: %folio%."),

	;

	private int value;
	private String label;

	private NotificacionTypeEnum (int valor, String label) {
		this.value = value;
		this.label = label;
	}

	public static NotificacionTypeEnum getNotificacionTypeEnum(int value) {
		switch (value) {
		case 1: return SEND_DRAFT_GENERATION;
		case 2: return SEND_NEGOTIATOR_CONTRACT;
		case 3: return SEND_LOAD_SUPPLIER_AREAS_APPROVAL;
		case 4: return SEND_APROVED_BY_JURISTIC;
		case 5: return SEND_PRINT_CONTRACT;
		case 6: return SEND_SACC_SIGN_CONTRACT;
		case 7: return SEND_SACC_SCAN_CONTRACT;
		case 8: return MODIFY_CONTRATO;
		case 9: return CANCEL_CONTRACT_BY_APPLICANT;
		case 10: return START_DRAFT_GENERATION;
		case 11: return START_NEGOTIATOR_CONTRACT;
		case 12: return START_LOAD_SUPPLIER_AREAS_APPROVAL;
		case 13: return START_APROVED_BY_JURISTIC;
		case 14: return START_PRINT_CONTRACT;
		case 15: return START_SACC_SIGN_CONTRACT;
		case 16: return START_SACC_SCAN_CONTRACT;
		case 18: return SEND_DRAFT_GENERATION_REMITE;
		case 19: return MODIFY_CONTRATO_REMITE;
		case 20: return SEND_LOAD_SUPPLIER_AREAS_APPROVAL_REMITE;
		case 21: return CHANGE_STATUS_SUBJECTEMAIL;
		case 22: return SEND_DRAFT_GENERATION_LWR;
		case 23: return SEND_DRAFT_GENERATION_USR;
		case 24: return SEND_NEGOTIATOR_CONTRACT_USR;
		case 25: return SEND_APROVED_BY_JURISTIC_JRD;
		case 26: return SEND_SACC_SIGN_CONTRACT_USR;
		case 27: return SEND_SACC_SCAN_CONTRACT_USR;
		case 28: return FINISH_CONTRACT_PROCESS_USR;
		}
		return null;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
