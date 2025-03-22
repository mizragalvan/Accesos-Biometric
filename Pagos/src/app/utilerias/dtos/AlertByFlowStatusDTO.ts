export class FlowStatus {
    idAlert: string;
    idFlow: number;
    status: string;
    name: string;
    turn: number;
    time: number;
    idStatus: string;
    nameFlow: string;
    expirationMailText: string;
    alertConfigurationDaysList: AlertConfigDay[];
    idRequisition: string;
    serviceDescription: string;
    requisitionStatus: string;
    commercialName: string;
    emailsList: string;
    mailContent: string;
    applicantMail: string;
    isUserSubdirectorEmailSend: string;
    idUser: string;
    idUnderDirector: string;
    underDirectorEmail: "";
    documentTypesList: [];
    idRequisitionOwners: string;
    customerCompanyName: string;
    businessManMail: string;
    isManagerial: string;

    constructor() {
        this.idAlert = "";
        this.idFlow = 0;
        this.status = "";
        this.name = "";
        this.turn = 0;
        this.time = 0;
        this.idStatus = "";
        this.nameFlow = "";
        this.expirationMailText = "";
        this.alertConfigurationDaysList = [];
        this.idRequisition = "";
        this.serviceDescription = "";
        this.requisitionStatus = "";
        this.commercialName = "";
        this.emailsList = "";
        this.mailContent = "";
        this.applicantMail = "";
        this.isUserSubdirectorEmailSend = "";
        this.idUser = "";
        this.idUnderDirector = "";
        this.underDirectorEmail = "";
        this.documentTypesList = [];
        this.idRequisitionOwners = "";
        this.customerCompanyName = "";
        this.businessManMail = "";
        this.isManagerial = "";
    }
}

export class AlertConfigDay {
    alertFromDay: number;
    day: string;
    emailsList: string;
    idAlert: ""
    idAlertConfigDay: ""
    idRecordGrid: ""
    isApplicantEmailSend: boolean;
    isMailSendDaily: boolean
    isUserSubdirectorEmailSend: boolean;
    mailContent: string;

    constructor() {
      this.alertFromDay = 0;
      this.day = "";
      this.emailsList = "";
      this.idAlert = "";
      this.idAlertConfigDay = "";
      this.idRecordGrid = "";
      this.isApplicantEmailSend = false;
      this.isMailSendDaily = false;
      this.isUserSubdirectorEmailSend = false;
      this.mailContent = "";
    }
}
