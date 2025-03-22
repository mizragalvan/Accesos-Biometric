package mx.pagos.admc.contracts.structures.digitalsignature;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentsResultEviSign {

	@JsonProperty("evidenceId")
	private String evidenceId;

	@JsonProperty("lookupKey")
	private String lookupKey;

	@JsonProperty("subject")
	private String subject;

	@JsonProperty("document")
	private String document;

	@JsonProperty("state")
	private String state;

	@JsonProperty("outcome")
	private String outcome;

	@JsonProperty("creationDate")
	private Date creationDate;

	@JsonProperty("lastStateChangeDate")
	private Date lastStateChangeDate;

	@JsonProperty("submittedOn")
	private Date submittedOn;

	@JsonProperty("processedOn")
	private Date processedOn;

	@JsonProperty("sentOn")
	private Date sentOn;

	@JsonProperty("closedOn")
	private Date closedOn;

	@JsonProperty("signedOn")
	private Date signedOn;

	@JsonProperty("affidavits")
	private List<Affidavit> affidavits;

	@JsonProperty("attachments")
	private List<Attachment> attachments;

	@JsonProperty("expiredOn")
	private Date expiredOn;

	@JsonProperty("signingParties")
	private List<SigningParty> signingParties;

	@JsonProperty("interestedParties")
	private List<InterestedParty> interestedParties;

	@JsonProperty("issuer")
	private String issuer;

	@JsonProperty("timeToLive")
	private Integer timeToLive;

	@JsonProperty("onlineRetentionPeriod")
	private Integer onlineRetentionPeriod;

	@JsonProperty("notaryRetentionPeriod")
	private Integer notaryRetentionPeriod;

	@JsonProperty("sourceChannel")
	private String sourceChannel;

	@JsonProperty("affidavitKinds")
	private List<String> affidavitKinds;

	
	public String getEvidenceId() {
		return evidenceId;
	}

	public void setEvidenceId(String evidenceId) {
		this.evidenceId = evidenceId;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastStateChangeDate() {
		return lastStateChangeDate;
	}

	public void setLastStateChangeDate(Date lastStateChangeDate) {
		this.lastStateChangeDate = lastStateChangeDate;
	}

	public Date getSubmittedOn() {
		return submittedOn;
	}

	public void setSubmittedOn(Date submittedOn) {
		this.submittedOn = submittedOn;
	}

	public Date getProcessedOn() {
		return processedOn;
	}

	public void setProcessedOn(Date processedOn) {
		this.processedOn = processedOn;
	}

	public Date getSentOn() {
		return sentOn;
	}

	public void setSentOn(Date sentOn) {
		this.sentOn = sentOn;
	}

	public Date getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}

	public Date getSignedOn() {
		return signedOn;
	}

	public void setSignedOn(Date signedOn) {
		this.signedOn = signedOn;
	}

	public List<Affidavit> getAffidavits() {
		return affidavits;
	}

	public void setAffidavits(List<Affidavit> affidavits) {
		this.affidavits = affidavits;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Date getExpiredOn() {
		return expiredOn;
	}

	public void setExpiredOn(Date expiredOn) {
		this.expiredOn = expiredOn;
	}

	public List<SigningParty> getSigningParties() {
		return signingParties;
	}

	public void setSigningParties(List<SigningParty> signingParties) {
		this.signingParties = signingParties;
	}

	public List<InterestedParty> getInterestedParties() {
		return interestedParties;
	}

	public void setInterestedParties(List<InterestedParty> interestedParties) {
		this.interestedParties = interestedParties;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public Integer getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(Integer timeToLive) {
		this.timeToLive = timeToLive;
	}

	public Integer getOnlineRetentionPeriod() {
		return onlineRetentionPeriod;
	}

	public void setOnlineRetentionPeriod(Integer onlineRetentionPeriod) {
		this.onlineRetentionPeriod = onlineRetentionPeriod;
	}

	public Integer getNotaryRetentionPeriod() {
		return notaryRetentionPeriod;
	}

	public void setNotaryRetentionPeriod(Integer notaryRetentionPeriod) {
		this.notaryRetentionPeriod = notaryRetentionPeriod;
	}

	public String getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public List<String> getAffidavitKinds() {
		return affidavitKinds;
	}

	public void setAffidavitKinds(List<String> affidavitKinds) {
		this.affidavitKinds = affidavitKinds;
	}
	
}
