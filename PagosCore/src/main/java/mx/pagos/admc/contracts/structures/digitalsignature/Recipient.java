package mx.pagos.admc.contracts.structures.digitalsignature;

import mx.pagos.admc.enums.digitalsignature.RecipientActionEnum;

public class Recipient extends BaseDS {

	private Integer idRecipient;
	private Integer idDocument;
	private RecipientActionEnum recipientAction;
	private String providerRecipientId;
	private Integer signingOrder;
	private String rfc;
	private String fullName;
	private String recipientType;
	private String email;
	private String secretCode;
	private String note;
	private String widgetId;
	private String linkToDocument;
	private Boolean signed;
	private Integer idContact;
	private Boolean isRequired;
	private Boolean isViewer;
	//EviSign
	private String role;
	private String signatureChallengeResponse;
	private String signatureChallenge;
	private String signingMethod;
	
	public Recipient() {
		super();
	}
	
	public Recipient(String responseCode, String responseMessage) {
		super(responseCode, responseMessage);
	}
	
	public Integer getIdRecipient() {
		return idRecipient;
	}
	public void setIdRecipient(Integer idRecipient) {
		this.idRecipient = idRecipient;
	}
	public Integer getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}
	public RecipientActionEnum getRecipientAction() {
		return recipientAction;
	}
	public void setRecipientAction(RecipientActionEnum recipientAction) {
		this.recipientAction = recipientAction;
	}
	public String getProviderRecipientId() {
		return providerRecipientId;
	}
	public void setProviderRecipientId(String providerRecipientId) {
		this.providerRecipientId = providerRecipientId;
	}
	public Integer getSigningOrder() {
		return signingOrder;
	}
	public void setSigningOrder(Integer signingOrder) {
		this.signingOrder = signingOrder;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getRecipientType() {
		return recipientType;
	}
	public void setRecipientType(String recipientType) {
		this.recipientType = recipientType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSecretCode() {
		return secretCode;
	}
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}
	public String getLinkToDocument() {
		return linkToDocument;
	}
	public void setLinkToDocument(String linkToDocument) {
		this.linkToDocument = linkToDocument;
	}
	public Boolean getSigned() {
		return signed;
	}
	public void setSigned(Boolean signed) {
		this.signed = signed;
	}
	public Integer getIdContact() {
		return idContact;
	}
	public void setIdContact(Integer idContact) {
		this.idContact = idContact;
	}
	public Boolean getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Boolean isRequired) {
		this.isRequired = isRequired;
	}
	public Boolean getIsViewer() {
		return isViewer;
	}
	public void setIsViewer(Boolean isViewer) {
		this.isViewer = isViewer;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSignatureChallengeResponse() {
		return signatureChallengeResponse;
	}

	public void setSignatureChallengeResponse(String signatureChallengeResponse) {
		this.signatureChallengeResponse = signatureChallengeResponse;
	}

	public String getSignatureChallenge() {
		return signatureChallenge;
	}

	public void setSignatureChallenge(String signatureChallenge) {
		this.signatureChallenge = signatureChallenge;
	}

	public String getSigningMethod() {
		return signingMethod;
	}

	public void setSigningMethod(String signingMethod) {
		this.signingMethod = signingMethod;
	}

}
