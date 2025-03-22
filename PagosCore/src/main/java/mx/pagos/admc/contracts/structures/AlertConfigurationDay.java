package mx.pagos.admc.contracts.structures;

public class AlertConfigurationDay {
    private Integer idAlertConfigDay;
    private Integer idAlert;
    private Integer alertFromDay;
    private String emailsList;
    private Boolean isMailSendDaily;
    private Boolean isApplicantEmailSend;
    private Boolean isUserSubdirectorEmailSend;
    private String mailContent;
    private String day;
    private Integer idRecordGrid;
    
    public final Integer getIdAlertConfigDay() {
        return this.idAlertConfigDay;
    }
    
    public final void setIdAlertConfigDay(final Integer idAlertConfigDayParameter) {
        this.idAlertConfigDay = idAlertConfigDayParameter;
    }
    
    public final Integer getIdAlert() {
        return this.idAlert;
    }
    
    public final void setIdAlert(final Integer idAlertParameter) {
        this.idAlert = idAlertParameter;
    }
    
    public final Integer getAlertFromDay() {
        return this.alertFromDay;
    }
    
    public final void setAlertFromDay(final Integer alertFromDayParameter) {
        this.alertFromDay = alertFromDayParameter;
    }
    
    public final String getEmailsList() {
        return this.emailsList;
    }
    
    public final void setEmailsList(final String emailsListParameter) {
        this.emailsList = emailsListParameter;
    }
    
    public final Boolean getIsMailSendDaily() {
        return this.isMailSendDaily;
    }
    
    public final void setIsMailSendDaily(final Boolean isMailSendDailyParameter) {
        this.isMailSendDaily = isMailSendDailyParameter;
    }
    
    public final Boolean getIsApplicantEmailSend() {
        return this.isApplicantEmailSend;
    }
    
    public final void setIsApplicantEmailSend(final Boolean isApplicantEmailSendParameter) {
        this.isApplicantEmailSend = isApplicantEmailSendParameter;
    }
    
    public final Boolean getIsUserSubdirectorEmailSend() {
        return this.isUserSubdirectorEmailSend;
    }
    
    public final void setIsUserSubdirectorEmailSend(final Boolean isUserSubdirectorEmailSendParameter) {
        this.isUserSubdirectorEmailSend = isUserSubdirectorEmailSendParameter;
    }
    
    public final String getMailContent() {
        return this.mailContent;
    }
    
    public final void setMailContent(final String mailContentParameter) {
        this.mailContent = mailContentParameter;
    }

    public final String getDay() {
        return this.day;
    }

    public final void setDay(final String dayParameter) {
        this.day = dayParameter;
    }

    public final Integer getIdRecordGrid() {
        return this.idRecordGrid;
    }

    public final void setIdRecordGrid(final Integer idRecordGridParameter) {
        this.idRecordGrid = idRecordGridParameter;
    }
}
