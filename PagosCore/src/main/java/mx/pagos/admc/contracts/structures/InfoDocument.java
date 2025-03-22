package mx.pagos.admc.contracts.structures;

import java.io.File;

public class InfoDocument {
	private Integer idRequisition;
	private Integer idDocument;
	private Integer idUser;
	private String documntName;
	private String digitalSignatureProvider;
	private Integer providerDocumentId;
	private String filePath;
	private Boolean onlySigner;
	private String emailSubject;
	private String emailMessage;
	private String statusDigitalSignature;
	private String createdAt;
	private String updatedAt;

	
	
	
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
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
	public String getDocumntName() {
		return documntName;
	}
	public void setDocumntName(String documntName) {
		this.documntName = documntName;
	}
	public String getDigitalSignatureProvider() {
		return digitalSignatureProvider;
	}
	public void setDigitalSignatureProvider(String digitalSignatureProvider) {
		this.digitalSignatureProvider = digitalSignatureProvider;
	}
	public Integer getProviderDocumentId() {
		return providerDocumentId;
	}
	public void setProviderDocumentId(Integer providerDocumentId) {
		this.providerDocumentId = providerDocumentId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Boolean getOnlySigner() {
		return onlySigner;
	}
	public void setOnlySigner(Boolean onlySigner) {
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
	public String getStatusDigitalSignature() {
		return statusDigitalSignature;
	}
	public void setStatusDigitalSignature(String statusDigitalSignature) {
		this.statusDigitalSignature = statusDigitalSignature;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	
	
	
}
