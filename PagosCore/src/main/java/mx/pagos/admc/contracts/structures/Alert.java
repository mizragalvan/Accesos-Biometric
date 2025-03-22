package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.enums.FlowPurchasingEnum;

public class Alert {
	private Integer idAlert;
	private Integer idFlow;
	private String status;
	private String name;
	private Integer turn;
	private Integer time;
	private String idStatus;
	private String nameFlow;
	private String expirationMailText; 
	private List<AlertConfigurationDay> alertConfigurationDaysList = new ArrayList<>();
	private Integer idRequisition;
	private String serviceDescription;
	private String requisitionStatus;
	private String commercialName;
	private String emailsList;
	private String mailContent;
	private String applicantMail;
	private Boolean isUserSubdirectorEmailSend;
	private Integer idUser;
	private Integer idUnderDirector;
	private String underDirectorEmail;
	private List<Integer> documentTypesList = new ArrayList<>();
	private Integer idRequisitionOwners;
	private String customerCompanyName;
	private String businessManMail;
	private Boolean isManagerial;
	
	public Alert() {

	}
	
	public Alert(final Integer idAlerts) {
		this.idAlert = idAlerts;
	}
	
	public Alert(final String nameParameter) {
		this.name = nameParameter;
	}

	public Alert(final Integer idFlowParameter, final FlowPurchasingEnum statusParameter) {
		this.idFlow = idFlowParameter;
		this.status = statusParameter.name();
	}
	
	public Alert(final Integer idFlowParameter, final String statusParameter) {
		this.idFlow = idFlowParameter;
		this.status = statusParameter;
	}
	
	public Alert(final Integer idFlowParameter, final String nameFlowParameter, final String statusParameter,
	        final String idStatusParameter) {
		this.idFlow = idFlowParameter;
		this.nameFlow = nameFlowParameter;
		this.status = statusParameter;
		this.idStatus = idStatusParameter;
	}

	public Alert(final FlowPurchasingEnum statusParameter) {
		this.status = statusParameter.name();
	}

    public final Integer getIdAlert() {
		return this.idAlert;
	}
	
	public final void setIdAlert(final Integer idAlertParameter) {
		this.idAlert = idAlertParameter;
	}
	
	public final Integer getIdFlow() {
		return this.idFlow;
	}
	
	public final void setIdFlow(final Integer idFlowParameter) {
		this.idFlow = idFlowParameter;
	}
	
	public final String getStatus() {
		return this.status;
	}
	
	public final void setStatus(final String statusParameter) {
		this.status = statusParameter;
	}
	
	public final Integer getTurn() {
		return this.turn;
	}
	
	public final void setTurn(final Integer turnParameter) {
		this.turn = turnParameter;
	}
	
	public final Integer getTime() {
		return this.time;
	}
	
	public final void setTime(final Integer timeParameter) {
		this.time = timeParameter;
	}

	public final String getName() {
		return this.name;
	}
	
	public final void setName(final String nameParameter) {
		this.name = nameParameter;
	}

	public final String getNameFlow() {
		return this.nameFlow;
	}

	public final void setNameFlow(final String nameFlowParameter) {
		this.nameFlow = nameFlowParameter;
	}

	public final String getExpirationMailText() {
        return this.expirationMailText;
    }

    public final void setExpirationMailText(final String expirationMailTextParameter) {
        this.expirationMailText = expirationMailTextParameter;
    }

    public final String getIdStatus() {
		return this.idStatus;
	}

	public final void setIdStatus(final String idStatusParameter) {
		this.idStatus = idStatusParameter;
	}

    public final List<AlertConfigurationDay> getAlertConfigurationDaysList() {
        return this.alertConfigurationDaysList;
    }

    public final void setAlertConfigurationDaysList(
            final List<AlertConfigurationDay> alertConfigurationDaysListParameter) {
        this.alertConfigurationDaysList = alertConfigurationDaysListParameter;
    }

    public final Integer getIdRequisition() {
        return this.idRequisition;
    }

    public final void setIdRequisition(final Integer idRequisitionParameter) {
        this.idRequisition = idRequisitionParameter;
    }

    public final String getServiceDescription() {
        return this.serviceDescription;
    }

    public final void setServiceDescription(final String serviceDescriptionParameter) {
        this.serviceDescription = serviceDescriptionParameter;
    }

    public final String getRequisitionStatus() {
        return this.requisitionStatus;
    }

    public final void setRequisitionStatus(final String requisitionStatusParameter) {
        this.requisitionStatus = requisitionStatusParameter;
    }

    public final String getCommercialName() {
        return this.commercialName;
    }

    public final void setCommercialName(final String supplierCommercialNameParameter) {
        this.commercialName = supplierCommercialNameParameter;
    }

    public final String getEmailsList() {
        return this.emailsList;
    }

    public final void setEmailsList(final String emailListParameter) {
        this.emailsList = emailListParameter;
    }

    public final String getMailContent() {
        return this.mailContent;
    }

    public final void setMailContent(final String mailContentParameter) {
        this.mailContent = mailContentParameter;
    }

    public final String getApplicantMail() {
        return this.applicantMail;
    }

    public final void setApplicantMail(final String applicantMailParameter) {
        this.applicantMail = applicantMailParameter;
    }

    public final Boolean getIsUserSubdirectorEmailSend() {
        return this.isUserSubdirectorEmailSend;
    }

    public final void setIsUserSubdirectorEmailSend(final Boolean isUserSubdirectorEmailSendParameter) {
        this.isUserSubdirectorEmailSend = isUserSubdirectorEmailSendParameter;
    }

    public final Integer getIdUser() {
        return this.idUser;
    }

    public final void setIdUser(final Integer idUserParameter) {
        this.idUser = idUserParameter;
    }

    public final Integer getIdUnderDirector() {
        return this.idUnderDirector;
    }

    public final void setIdUnderDirector(final Integer idUnderDirectorParameter) {
        this.idUnderDirector = idUnderDirectorParameter;
    }

    public final String getUnderDirectorEmail() {
        return this.underDirectorEmail;
    }

    public final void setUnderDirectorEmail(final String underDirectorEmailParameter) {
        this.underDirectorEmail = underDirectorEmailParameter;
    }

    public final List<Integer> getDocumentTypesList() {
        return this.documentTypesList;
    }

    public final void setDocumentTypesList(final List<Integer> documentTypesListParameter) {
        this.documentTypesList = documentTypesListParameter;
    }

    public final Integer getIdRequisitionOwners() {
        return this.idRequisitionOwners;
    }

    public final void setIdRequisitionOwners(final Integer idRequisitionOwnerParameters) {
        this.idRequisitionOwners = idRequisitionOwnerParameters;
    }

    public final String getCustomerCompanyName() {
        return this.customerCompanyName;
    }

    public final void setCustomerCompanyName(final String customerCompanyNamParametere) {
        this.customerCompanyName = customerCompanyNamParametere;
    }

    public final String getBusinessManMail() {
        return this.businessManMail;
    }

    public final void setBusinessManMail(final String businessManMailParameter) {
        this.businessManMail = businessManMailParameter;
    }

    public final Boolean getIsManagerial() {
        return this.isManagerial;
    }

    public final void setIsManagerial(final Boolean isManagerialParameter) {
        this.isManagerial = isManagerialParameter;
    }
}
