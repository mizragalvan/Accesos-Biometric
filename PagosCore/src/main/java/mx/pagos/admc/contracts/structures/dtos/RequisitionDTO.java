package mx.pagos.admc.contracts.structures.dtos;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.pagos.admc.contracts.structures.Area;
import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.admc.contracts.structures.Comment;
import mx.pagos.admc.contracts.structures.Customs;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.DocumentType;
import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.FlowScreenAction;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.ModifiedClausules;
import mx.pagos.admc.contracts.structures.Obligation;
import mx.pagos.admc.contracts.structures.RequiredDocumentBySupplier;
import mx.pagos.admc.contracts.structures.RollOff;
import mx.pagos.admc.contracts.structures.Scaling;
import mx.pagos.admc.contracts.structures.Supplier;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.contracts.structures.Tracto;
import mx.pagos.admc.contracts.structures.Unit;
import mx.pagos.admc.contracts.structures.VoBoDocumentFile;
import mx.pagos.admc.core.enums.ContratistaEnum;
import mx.pagos.admc.enums.CommentType;
import mx.pagos.admc.enums.CurrencyCodeEnum;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.SemaphoreEnum;
import mx.pagos.admc.enums.ValidityEnum;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.security.structures.User;
/**
 * @author Mizraim
 *
 */
  public class RequisitionDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1501722362992031120L;
	
	public User getApplicant() {
		return applicant;
	}




	public void setApplicant(User applicant) {
		this.applicant = applicant;
	}




	public User getLawyer() {
		return lawyer;
	}




	public void setLawyer(User lawyer) {
		this.lawyer = lawyer;
	}




	public Integer getIdDocument() {
		return idDocument;
	}




	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}




	public DocumentType getDocumentType() {
		return documentType;
	}




	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}




	public CatDocumentType getCatDocumentType() {
		return catDocumentType;
	}




	public void setCatDocumentType(CatDocumentType catDocumentType) {
		this.catDocumentType = catDocumentType;
	}




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




	public Integer getIdSupplier() {
		return idSupplier;
	}




	public void setIdSupplier(Integer idSupplier) {
		this.idSupplier = idSupplier;
	}




	public String getSupplierName() {
		return supplierName;
	}




	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}




	public List<SupplierPerson> getSupplierLegalRepresentativesList() {
		return SupplierLegalRepresentativesList;
	}




	public void setSupplierLegalRepresentativesList(List<SupplierPerson> supplierLegalRepresentativesList) {
		SupplierLegalRepresentativesList = supplierLegalRepresentativesList;
	}




	public List<RequiredDocumentBySupplier> getRequiredDocumentBySupplier() {
		return requiredDocumentBySupplier;
	}




	public void setRequiredDocumentBySupplier(List<RequiredDocumentBySupplier> requiredDocumentBySupplier) {
		this.requiredDocumentBySupplier = requiredDocumentBySupplier;
	}




	public Integer getIdLawyer() {
		return idLawyer;
	}




	public void setIdLawyer(Integer idLawyer) {
		this.idLawyer = idLawyer;
	}




	public Integer getIdEvaluator() {
		return idEvaluator;
	}




	public void setIdEvaluator(Integer idEvaluator) {
		this.idEvaluator = idEvaluator;
	}




	public Integer getIdAreaTender() {
		return idAreaTender;
	}




	public void setIdAreaTender(Integer idAreaTender) {
		this.idAreaTender = idAreaTender;
	}




	public Integer getIdArea() {
		return idArea;
	}




	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}




	public Area getArea() {
		return area;
	}




	public void setArea(Area area) {
		this.area = area;
	}




	public Integer getIdUnit() {
		return idUnit;
	}




	public void setIdUnit(Integer idUnit) {
		this.idUnit = idUnit;
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




	public FlowPurchasingEnum getStatus() {
		return status;
	}




	public void setStatus(FlowPurchasingEnum status) {
		this.status = status;
	}




	public ValidityEnum getValidity() {
		return validity;
	}




	public void setValidity(ValidityEnum validity) {
		this.validity = validity;
	}




	public Boolean getAutomaticRenewal() {
		return automaticRenewal;
	}




	public void setAutomaticRenewal(Boolean automaticRenewal) {
		this.automaticRenewal = automaticRenewal;
	}




	public Integer getRenewalPeriods() {
		return renewalPeriods;
	}




	public void setRenewalPeriods(Integer renewalPeriods) {
		this.renewalPeriods = renewalPeriods;
	}




	public String getValidityStartDate() {
		return validityStartDate;
	}




	public void setValidityStartDate(String validityStartDate) {
		this.validityStartDate = validityStartDate;
	}




	public String getValidityEndDate() {
		return validityEndDate;
	}




	public void setValidityEndDate(String validityEndDate) {
		this.validityEndDate = validityEndDate;
	}




	public String getEventName() {
		return eventName;
	}




	public void setEventName(String eventName) {
		this.eventName = eventName;
	}




	public String getEventDatetime() {
		return eventDatetime;
	}




	public void setEventDatetime(String eventDatetime) {
		this.eventDatetime = eventDatetime;
	}




	public String getSignDate() {
		return signDate;
	}




	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}




	public String getSupplierSignSendDate() {
		return supplierSignSendDate;
	}




	public void setSupplierSignSendDate(String supplierSignSendDate) {
		this.supplierSignSendDate = supplierSignSendDate;
	}




	public String getWitnessesSignSendDate() {
		return witnessesSignSendDate;
	}




	public void setWitnessesSignSendDate(String witnessesSignSendDate) {
		this.witnessesSignSendDate = witnessesSignSendDate;
	}




	public String getSupplierSignReturnDate() {
		return supplierSignReturnDate;
	}




	public void setSupplierSignReturnDate(String supplierSignReturnDate) {
		this.supplierSignReturnDate = supplierSignReturnDate;
	}




	public String getWitnessesSignReturnDate() {
		return witnessesSignReturnDate;
	}




	public void setWitnessesSignReturnDate(String witnessesSignReturnDate) {
		this.witnessesSignReturnDate = witnessesSignReturnDate;
	}




	public String getSignedContractSendDateSupplier() {
		return signedContractSendDateSupplier;
	}




	public void setSignedContractSendDateSupplier(String signedContractSendDateSupplier) {
		this.signedContractSendDateSupplier = signedContractSendDateSupplier;
	}




	public String getSignedContractSendDateWitnesses() {
		return signedContractSendDateWitnesses;
	}




	public void setSignedContractSendDateWitnesses(String signedContractSendDateWitnesses) {
		this.signedContractSendDateWitnesses = signedContractSendDateWitnesses;
	}




	public String getSignedContractSendDateRegistry() {
		return signedContractSendDateRegistry;
	}




	public void setSignedContractSendDateRegistry(String signedContractSendDateRegistry) {
		this.signedContractSendDateRegistry = signedContractSendDateRegistry;
	}




	public String getSignedContractSendDateLegal() {
		return signedContractSendDateLegal;
	}




	public void setSignedContractSendDateLegal(String signedContractSendDateLegal) {
		this.signedContractSendDateLegal = signedContractSendDateLegal;
	}




	public Integer getSupplierApprovalIdDocument() {
		return supplierApprovalIdDocument;
	}




	public void setSupplierApprovalIdDocument(Integer supplierApprovalIdDocument) {
		this.supplierApprovalIdDocument = supplierApprovalIdDocument;
	}




	public Supplier getSupplier() {
		return supplier;
	}




	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}




	public String getFlowStatus() {
		return flowStatus;
	}




	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
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




	public String getEnumDocumentType() {
		return enumDocumentType;
	}




	public void setEnumDocumentType(String enumDocumentType) {
		this.enumDocumentType = enumDocumentType;
	}




	public String getSupplierDeedNumber() {
		return supplierDeedNumber;
	}




	public void setSupplierDeedNumber(String supplierDeedNumber) {
		this.supplierDeedNumber = supplierDeedNumber;
	}




	public String getSupplierDeedConstitutionDate() {
		return supplierDeedConstitutionDate;
	}




	public void setSupplierDeedConstitutionDate(String supplierDeedConstitutionDate) {
		this.supplierDeedConstitutionDate = supplierDeedConstitutionDate;
	}




	public String getSupplierDeedNotary() {
		return supplierDeedNotary;
	}




	public void setSupplierDeedNotary(String supplierDeedNotary) {
		this.supplierDeedNotary = supplierDeedNotary;
	}




	public String getSupplierNotaryNumber() {
		return supplierNotaryNumber;
	}




	public void setSupplierNotaryNumber(String supplierNotaryNumber) {
		this.supplierNotaryNumber = supplierNotaryNumber;
	}




	public String getSupplierNotaryState() {
		return supplierNotaryState;
	}




	public void setSupplierNotaryState(String supplierNotaryState) {
		this.supplierNotaryState = supplierNotaryState;
	}




	public String getSupplierDeedComercialFolio() {
		return supplierDeedComercialFolio;
	}




	public void setSupplierDeedComercialFolio(String supplierDeedComercialFolio) {
		this.supplierDeedComercialFolio = supplierDeedComercialFolio;
	}




	public String getSupplierDeedInscriptionDateFolio() {
		return supplierDeedInscriptionDateFolio;
	}




	public void setSupplierDeedInscriptionDateFolio(String supplierDeedInscriptionDateFolio) {
		this.supplierDeedInscriptionDateFolio = supplierDeedInscriptionDateFolio;
	}




	public String getEndDateContractToEnd() {
		return endDateContractToEnd;
	}




	public void setEndDateContractToEnd(String endDateContractToEnd) {
		this.endDateContractToEnd = endDateContractToEnd;
	}




	public String getSignState() {
		return signState;
	}




	public void setSignState(String signState) {
		this.signState = signState;
	}




	public String getCondosNumber() {
		return condosNumber;
	}




	public void setCondosNumber(String condosNumber) {
		this.condosNumber = condosNumber;
	}




	public String getSupplierIMMS() {
		return supplierIMMS;
	}




	public void setSupplierIMMS(String supplierIMMS) {
		this.supplierIMMS = supplierIMMS;
	}




	public String getSupplierNationality() {
		return supplierNationality;
	}




	public void setSupplierNationality(String supplierNationality) {
		this.supplierNationality = supplierNationality;
	}




	public String getSquareName() {
		return squareName;
	}




	public void setSquareName(String squareName) {
		this.squareName = squareName;
	}




	public String getRepresentativeSocietyName() {
		return representativeSocietyName;
	}




	public void setRepresentativeSocietyName(String representativeSocietyName) {
		this.representativeSocietyName = representativeSocietyName;
	}




	public String getPersonNameSendDailySalesReports() {
		return personNameSendDailySalesReports;
	}




	public void setPersonNameSendDailySalesReports(String personNameSendDailySalesReports) {
		this.personNameSendDailySalesReports = personNameSendDailySalesReports;
	}




	public String getPersonMailSendDailySalesReports() {
		return personMailSendDailySalesReports;
	}




	public void setPersonMailSendDailySalesReports(String personMailSendDailySalesReports) {
		this.personMailSendDailySalesReports = personMailSendDailySalesReports;
	}




	public String getSupplierObligations() {
		return supplierObligations;
	}




	public void setSupplierObligations(String supplierObligations) {
		this.supplierObligations = supplierObligations;
	}




	public String getCurrencyCountry() {
		return currencyCountry;
	}




	public void setCurrencyCountry(String currencyCountry) {
		this.currencyCountry = currencyCountry;
	}




	public String getPublicDeedDate() {
		return publicDeedDate;
	}




	public void setPublicDeedDate(String publicDeedDate) {
		this.publicDeedDate = publicDeedDate;
	}




	public String getClosedDate() {
		return closedDate;
	}




	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}




	public String getSupplierAtention() {
		return supplierAtention;
	}




	public void setSupplierAtention(String supplierAtention) {
		this.supplierAtention = supplierAtention;
	}




	public String getSupplierPhone() {
		return supplierPhone;
	}




	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}




	public String getSupplierAccountNumber() {
		return supplierAccountNumber;
	}




	public void setSupplierAccountNumber(String supplierAccountNumber) {
		this.supplierAccountNumber = supplierAccountNumber;
	}




	public String getNegotiatorRepresentativeName() {
		return negotiatorRepresentativeName;
	}




	public void setNegotiatorRepresentativeName(String negotiatorRepresentativeName) {
		this.negotiatorRepresentativeName = negotiatorRepresentativeName;
	}




	public Integer getIdCompany() {
		return idCompany;
	}




	public void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}




	public String getObligacionesEspecificas() {
		return obligacionesEspecificas;
	}




	public void setObligacionesEspecificas(String obligacionesEspecificas) {
		this.obligacionesEspecificas = obligacionesEspecificas;
	}




	private User applicant;
	private User lawyer;
	private Integer idDocument;
	private DocumentType documentType;
	private CatDocumentType catDocumentType;
	private Integer idRequisition;
	private Integer idFlow;
	private Integer idApplicant;
	private Integer idSupplier;
	private String supplierName;
	private List<SupplierPerson> SupplierLegalRepresentativesList;
	private List<RequiredDocumentBySupplier> requiredDocumentBySupplier;
	private Integer idLawyer;
	private Integer idEvaluator;
	private Integer idAreaTender;
	private Integer idArea;
	private Area area;
	private Integer idUnit;
	private String applicationDate;
	private Integer idDocumentType;
	private String documentTypeName;
	private String documentName;


	private FlowPurchasingEnum status;

	private ValidityEnum validity;
	private Boolean automaticRenewal;
	private Integer renewalPeriods;
	private String validityStartDate;
	private String validityEndDate;





	private String eventName;
	private String eventDatetime;

	private String signDate;
	private String supplierSignSendDate;
	private String witnessesSignSendDate;
	private String supplierSignReturnDate;
	private String witnessesSignReturnDate;

	private String signedContractSendDateSupplier;

	private String signedContractSendDateWitnesses;
	
	private String signedContractSendDateRegistry;
	private String signedContractSendDateLegal;
	private Integer supplierApprovalIdDocument;
	private Supplier supplier;
	private String flowStatus;
	private String starDate;
	private String endDate;
	private String fullNameApplicant;
	private String enumDocumentType;

	private String supplierDeedNumber;
	private String supplierDeedConstitutionDate;
	private String supplierDeedNotary;
	private String supplierNotaryNumber;
	private String supplierNotaryState;
	private String supplierDeedComercialFolio;
	private String supplierDeedInscriptionDateFolio;


	private String endDateContractToEnd;


	private String signState;
	private String condosNumber;


	private String supplierIMMS;

	private String supplierNationality;
	private String squareName;
	private String representativeSocietyName;
	private String personNameSendDailySalesReports;
	private String personMailSendDailySalesReports;

	private String supplierObligations;
	private String currencyCountry;

	private String publicDeedDate;

	private String closedDate;
	private String supplierAtention;
	private String supplierPhone;
	private String supplierAccountNumber;

	// MetroCarrier Fields
	private String negotiatorRepresentativeName;


	private Integer idCompany;

	private String obligacionesEspecificas;
	
	private String CompanyName;
	private String rfc;
	private String lawyerName;
	private String supplierCompanyName;
	private String fullName;
	private String areaName;
	private String createDate;
	
	
	
	public String getCreateDate() {
		return createDate;
	}




	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}




	public String getLastDateModify() {
		return LastDateModify;
	}




	public void setLastDateModify(String lastDateModify) {
		LastDateModify = lastDateModify;
	}




	private String LastDateModify;
	
	
	
	




	public String getAreaName() {
		return areaName;
	}




	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}




	public String getFullName() {
		return fullName;
	}




	public void setFullName(String fullName) {
		this.fullName = fullName;
	}




	public String getSupplierCompanyName() {
		return supplierCompanyName;
	}




	public void setSupplierCompanyName(String supplierCompanyName) {
		this.supplierCompanyName = supplierCompanyName;
	}




	public String getLawyerName() {
		return lawyerName;
	}




	public void setLawyerName(String lawyerName) {
		this.lawyerName = lawyerName;
	}




	public String getRfc() {
		return rfc;
	}




	public void setRfc(String rfc) {
		this.rfc = rfc;
	}




	public String getCompanyName() {
		return CompanyName;
	}




	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}




	public RequisitionDTO() {}
	  
  }