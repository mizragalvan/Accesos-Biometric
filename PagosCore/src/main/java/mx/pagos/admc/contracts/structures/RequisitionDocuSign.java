package mx.pagos.admc.contracts.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.pagos.admc.core.enums.ContratistaEnum;
import mx.pagos.admc.enums.CurrencyCodeEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.SemaphoreEnum;
import mx.pagos.admc.enums.ValidityEnum;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.security.structures.User;

public class RequisitionDocuSign {
	private Integer idRequisition;
	private Integer idFlow;
	private Integer idApplicant;
	private Integer idArea;
	private Integer idUser;
	private Integer idPosition;
	private String correo;
	private Area area;
	private String applicationDate;
	private Integer idDocumentType;
	private String documentTypeName;
	private String documentName;
	private Integer templateIdDocument;
	private String rfc;
	private String starDate;
	private String endDate;
	private String fullNameApplicant;
	private String status;
	private String statusLogin;
	
	
	public Integer getIdRequisition() {
		return idRequisition;
	}
	public void setIdRequisition(Integer idRequisition) {
		this.idRequisition = idRequisition;
	}
	public Integer getIdFlow() {
		return idFlow;
	}
	public void setIdFlow(Integer idFlow) {
		this.idFlow = idFlow;
	}
	public Integer getIdApplicant() {
		return idApplicant;
	}
	public void setIdApplicant(Integer idApplicant) {
		this.idApplicant = idApplicant;
	}
	public Integer getIdArea() {
		return idArea;
	}
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public Integer getIdPosition() {
		return idPosition;
	}
	public void setIdPosition(Integer idPosition) {
		this.idPosition = idPosition;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	public String getApplicationDate() {
		return applicationDate;
	}
	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}
	public Integer getIdDocumentType() {
		return idDocumentType;
	}
	public void setIdDocumentType(Integer idDocumentType) {
		this.idDocumentType = idDocumentType;
	}
	public String getDocumentTypeName() {
		return documentTypeName;
	}
	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public Integer getTemplateIdDocument() {
		return templateIdDocument;
	}
	public void setTemplateIdDocument(Integer templateIdDocument) {
		this.templateIdDocument = templateIdDocument;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getStarDate() {
		return starDate;
	}
	public void setStarDate(String starDate) {
		this.starDate = starDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getFullNameApplicant() {
		return fullNameApplicant;
	}
	public void setFullNameApplicant(String fullNameApplicant) {
		this.fullNameApplicant = fullNameApplicant;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusLogin() {
		return statusLogin;
	}
	public void setStatusLogin(String statusLogin) {
		this.statusLogin = statusLogin;
	}
	
	
}
