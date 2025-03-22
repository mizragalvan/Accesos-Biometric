package mx.pagos.admc.contracts.structures;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import mx.pagos.admc.contracts.structures.digitalsignature.BaseDS;
import mx.pagos.admc.contracts.structures.digitalsignature.Recipient;
import mx.pagos.admc.enums.DigitalSignatureProviderEnum;
import mx.pagos.admc.enums.DigitalSignatureStatusEnum;
import mx.pagos.admc.enums.TimeRangeEnum;
import mx.pagos.admc.enums.WeekdayEnum;
 
public class DocumentDS extends BaseDS implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer idDocument;
	private Integer idUser;
	private Integer idRequisition;
	private String documentName;
	private DigitalSignatureProviderEnum digitalSignatureProvider;
	private String providerDocumentId;
	private String filePath;
	private boolean onlySigner;
	private String emailSubject;
	private String emailMessage;
	private DigitalSignatureStatusEnum statusDigitalSignature;
	private boolean status;
	private String fullName;
	private String extension;
	private String uniqueId;

	// Data transfer attributes
	private File file;
	private List<Recipient> recipients;
	private boolean signingOrder;
	private boolean manualSigningOrder;
    private String comment;
    private boolean hasReminders;
    private Integer daysBeforeFirstReminder;
    private Integer daysBetweenReminders;
    private List<WeekdayEnum> weekdays;
    private List<TimeRangeEnum> timeRanges;
    private boolean hasExpiration;
    private Integer daysValidity;

 
	public DocumentDS() {
		super();
	}
	public DocumentDS(String responseCode, String responseMessage) {
		super(responseCode, responseMessage);
	}
	
	
	public Integer getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public DigitalSignatureProviderEnum getDigitalSignatureProvider() {
		return digitalSignatureProvider;
	}
	public void setDigitalSignatureProvider(DigitalSignatureProviderEnum digitalSignatureProvider) {
		this.digitalSignatureProvider = digitalSignatureProvider;
	}
	public String getProviderDocumentId() {
		return providerDocumentId;
	}
	public void setProviderDocumentId(String providerDocumentId) {
		this.providerDocumentId = providerDocumentId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public boolean isOnlySigner() {
		return onlySigner;
	}
	public void setOnlySigner(boolean onlySigner) {
		this.onlySigner = onlySigner;
	}
	public String getEmailSubject() {
		return emailSubject;
	}
	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	public String getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}
	public DigitalSignatureStatusEnum getStatusDigitalSignature() {
		return statusDigitalSignature;
	}
	public void setStatusDigitalSignature(DigitalSignatureStatusEnum statusDigitalSignature) {
		this.statusDigitalSignature = statusDigitalSignature;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public List<Recipient> getRecipients() {
		return recipients;
	}
	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}
	public boolean isSigningOrder() {
		return signingOrder;
	}
	public void setSigningOrder(boolean signingOrder) {
		this.signingOrder = signingOrder;
	}
	public boolean isManualSigningOrder() {
		return manualSigningOrder;
	}
	public void setManualSigningOrder(boolean manualSigningOrder) {
		this.manualSigningOrder = manualSigningOrder;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isHasReminders() {
		return hasReminders;
	}
	public void setHasReminders(boolean hasReminders) {
		this.hasReminders = hasReminders;
	}
	public Integer getDaysBeforeFirstReminder() {
		return daysBeforeFirstReminder;
	}
	public void setDaysBeforeFirstReminder(Integer daysBeforeFirstReminder) {
		this.daysBeforeFirstReminder = daysBeforeFirstReminder;
	}
	public Integer getDaysBetweenReminders() {
		return daysBetweenReminders;
	}
	public void setDaysBetweenReminders(Integer daysBetweenReminders) {
		this.daysBetweenReminders = daysBetweenReminders;
	}
	public List<WeekdayEnum> getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(List<WeekdayEnum> weekdays) {
		this.weekdays = weekdays;
	}
	public List<TimeRangeEnum> getTimeRanges() {
		return timeRanges;
	}
	public void setTimeRanges(List<TimeRangeEnum> timeRanges) {
		this.timeRanges = timeRanges;
	}
	public boolean isHasExpiration() {
		return hasExpiration;
	}
	public void setHasExpiration(boolean hasExpiration) {
		this.hasExpiration = hasExpiration;
	}
	public Integer getDaysValidity() {
		return daysValidity;
	}
	public void setDaysValidity(Integer daysValidity) {
		this.daysValidity = daysValidity;
	}
	
}
