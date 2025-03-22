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

public class Requisition {
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
	private Unit unit;
	private String applicationDate;
	private Integer idDocumentType;
	private String documentTypeName;
	private String documentName;
	private Integer templateIdDocument;
	private String authorizationDocumentName;
	private Integer authorizationDocumentIdDoc;
	private String serviceDescription;
	private String technicalDetails;
	private Boolean attatchmentDeliverables = false;
	private Boolean attchmtServiceLevelsMeasuring = false;
	private Boolean attatchmentPenalty = false;
	private Boolean attatchmentScalingMatrix = false;
	private Boolean attatchmentCompensation = false;
	private Boolean attchmtBusinessMonitoringPlan = false;
	private String businessReasonMonitoringPlan;
	private Boolean attchmtImssInfoDeliveryReqrmts = false;
	private Boolean attatchmentInformationSecurity = false;
	private Boolean attatchmentOthers = false;
	private String attatchmentOthersName;
	private FlowPurchasingEnum status;
	private Map<Integer, Integer> specialClausesMap = new HashMap<Integer, Integer>();
	private List<Integer> approvalAreasList = new ArrayList<Integer>();
	private List<String> approvalAreasActiveList = new ArrayList<String>();
	private List<Integer> authorizationDgasList = new ArrayList<Integer>();
	private List<Dga> authorizationDgasActiveList = new ArrayList<Dga>();
	private List<Integer> financialEntityList = new ArrayList<Integer>();
	private List<FinancialEntity> dataFinancialEntityList = new ArrayList<FinancialEntity>();
	private List<Customs> customsList = new ArrayList<Customs>();
	private List<LegalRepresentative> legalRepresentativesList = new ArrayList<LegalRepresentative>();
	private List<Integer> usersToVoboList = new ArrayList<Integer>();
	private List<User> usersToVoboUserList = new ArrayList<User>();
	private FlowScreenAction flowScreenActionParams = new FlowScreenAction();
	private List<String> financialEntityWitnessesList = new ArrayList<String>();
	private ValidityEnum validity;
	private Boolean automaticRenewal;
	private Integer renewalPeriods;
	private String validityStartDate;
	private String validityEndDate;
	private String validityClause;
	private Boolean isRequiredHumanResources;
	private Boolean isImssCeduleGiven;
	private Integer imssCeduleNotGivenIdDocument;
	private Boolean isSupplierSingleRegistered;
	private Boolean isSupplierPenalties;
	private String supplierPenaltiesText;
	private Boolean isAdvanceBailNeeded;
	private Double advanceBailAmount;
	private Integer advanceBailNotNeededIdDocument;
	private Boolean isFulfillmentBailNeeded;
	private Double fulfillmentBailAmount;
	private Integer fulfillmentBailNotNeededIdDocument;
	private Boolean isFidelityBailNeeded;
	private Integer fidelityBailNotNeededIdDocument;
	private Boolean isContingencyBailNeeded;
	private Integer contingencyBailNotNeededIdDocument;
	private Boolean isCivilRespInsuranceBailNeeded;
	private Integer civilResponsabilityInsuranceBailNotNeededIdDocument;
	private Boolean isContractTermAuthorized;
	private Boolean isSpecialProvisionNegotiated;
	private String specialProvisionNegotiated;
	private Boolean isWarrantyDeposit;
	private Boolean isStaffUnderGroupDirecction;
	private String contractObjectClause;
	private String ClausulaFormaPago;
	private String considerationClause;
	private String clabe;
	private String contractDurationDate;
	private String considerationInitialReport;
	private String considerationMonthlyReport;
	private String considerationExtraordinaryReport;
	private Double initialPaymentPercentage;
	private String initialPaymentPeriod;
	private Double monthlyPaymentPercentage;
	private String monthlyPaymentPeriod;
	private Double extraordinaryPaymentPercentage;
	private String extraordinaryPaymentPeriod;
	private String domainName;
	private String brandName;
	private String subcontractorLegalRepresentativeName;
	private String contractApplicant;
	private String contract;
	private String extensionsNumber;
	private String extensionYears;
	private String extensionPeriod;
	private String extensionValidity;
	private String extensionForcedYears;
	private String extensionVoluntaryYears;
	private String rentInitialQuantity;
	private String extensionFirstYearRent;
	private String maintenanceInitialQuantity;
	private String naturalPersonTenantDeclarations;
	private String moralOrNaturalPersonDeclarations;
	private String contractDate;
	private String surface;
	private String propertyDeedTitleNumber;
	private String propertyDeedTitleDate;
	private String propertyDeedTitleNotary;
	private String propertyDeedTitleNotaryNumber;
	private String propertyDeedTitleNotaryState;
	private String propertyDeedTitleCommercialFolio;
	private String propertyDeedTitleRegistrationDate;
	private String subcontactedPersonality;
	private String directSupplierPersonality;
	private String solicitudDescripcionNegociacion;
	private String solicitudDescripcionLargaNegociacion;
	private String numeroAdendum;
	private ContratistaEnum contractType;
	private String eventName;
	private String eventDatetime;
	private String clausulesToModify;
	private String contractValidity;
	private String maintenanceClause;
	private String signDate;
	private Comment comment;
	private String supplierSignSendDate;
	private String witnessesSignSendDate;
	private String supplierSignReturnDate;
	private String witnessesSignReturnDate;
	private Boolean isOrigSignCntrDelvrdSupplier;
	private Boolean isCpySignCntrDelvrdSupplier;
	private String signedContractSendDateSupplier;
	private Boolean isOrigSignCntrDelvrdWitnesses;
	private Boolean isCpySignCntrDelvrdWitnesses;
	private String signedContractSendDateWitnesses;
	private Boolean isOrigSignCntrDelvrdRegistry;
	private Boolean isCpySignCntrDelvrdRegistry;
	private Boolean isOrigSignCntrDelvrdLegal;
	private Boolean isCpySignCntrDelvrdLegal;
	private String signedContractSendDateRegistry;
	private String signedContractSendDateLegal;
	private List<FileUploadInfo> documentsAttachment = new ArrayList<FileUploadInfo>();
	private FileUploadInfo authorizationDocument;
	private FileUploadInfo imssCeduleNotGivenDocument;
	private FileUploadInfo advanceBailNotNeededDocument;
	private FileUploadInfo fulfillmentBailNotNeededDocument;
	private FileUploadInfo fidelityBailNotNeededDocument;
	private FileUploadInfo contingencyBailNotNeededDocument;
	private FileUploadInfo civilResponsabilityInsuranceBailNotNeededDocument;
	private FileUploadInfo hiddenVicesBailDocument;
	private List<VoBoDocumentFile> documentsAreasVoBoList;
	private Integer supplierApprovalIdDocument;
	private FileUploadInfo supplierApprovalDocument;
	private List<FileUploadInfo> digitalizationDocument = new ArrayList<FileUploadInfo>();
	private Supplier supplier;
	private List<Integer> legalReprentativeIdList = new ArrayList<Integer>();
	private String professionalLicense;
	private String expeditionDateProfessionalLicense;
	private String specialty;
	private String specialtyCeduleNumber;
	private String expeditionDateSpecialtyLicense;
	private String sanitaryLicense;
	private String socialReasonOfTheContract;
	private List<Obligation> obligationsList;
	private SemaphoreEnum semaphore;
	private String rfc;
	private String flowStatus;
	private String starDate;
	private String endDate;
	private Boolean retry;
	private String emailApplicant;
	private String emailLawyer;
	private String fullNameApplicant;
	private String enumDocumentType;
	private String proemName;
	private String contractObject;
	private String contractExtendClause;
	private String contractEndClause;
	private String forcedYears;
	private String voluntaryYears;
	private String rentClause;
	private String supplierCompanyPurpose;
	private String supplierCompanyType;
	private String supplierAddress;
	private String fiscalAddress;
	private Boolean isExpiredAttended;
	private String property;
	private Double contingencyBailAmount;
	private String proyect;
	private String proyectAddress;
	private String developer;
	private String endDateClause;
	private Double extensionAmount;
	private String endDateDeclaration;
	private String ocupationDate;
	private List<Scaling> scalingListSupplier = new ArrayList<>();
	private List<Scaling> scalingListFinancialEntity = new ArrayList<>();
	private String areaTender;
	private String dateValue;
	private String supplierDeedNumber;
	private String supplierDeedConstitutionDate;
	private String supplierDeedNotary;
	private String supplierNotaryNumber;
	private String supplierNotaryState;
	private String supplierDeedComercialFolio;
	private String supplierDeedInscriptionDateFolio;
	private String publicNotaryDeed;
	private String activeActor;
	private String passiveActor;
	private String deedOrCommercialRegister;
	private String officialId;
	private String officialIdNumber;
	private Integer gracePeriodMonths;
	private String contractualPenaltyMonths;
	private String propertyAddress;
	private String propertyDeliveryDate;
	private String settlementObligations;
	private String propertyDateVacated;
	private String propertyDeed;
	private String datePropertyDeed;
	private String notaryDeed;
	private String numberNotaryDeed;
	private String stateNotaryDeed;
	private String folioDeedOrCommercialRegister;
	private String registrationDateFolio;
	private String conventionalPenaltyAmount;
	private String monthlyRentAmount;
	private String extensionNumber;
	private String publicDeedNumberCopy;
	private String monthlyMaintenanceAmount;
	private String paExtensionForcedYears;
	private String paExtensionVoluntaryYears;
	private String aaExtensionForcedYears;
	private String aaExtensionVoluntaryYears;
	private String serviceStartDate;
	private String contractToModify;
	private String dateContractToModify;
	private String contractObjetToModify;
	private String retroactiveDate;
	private String clauseToModifyContent;
	private String fidelityBailAmount;
	private String civilResponsabilityBailAmount;
	private Boolean isHiddenVices;
	private String hiddenVicesAmount;
	private Integer hiddenVicesBailIdDoc;
	private FinancialEntity financialEntity;
	private String endDateContractToEnd;
	private String descripcionClausulaModificada;
	private String outsourcedMail;
	private String outsourcedPhoneNumber;
	private String outsourcedAtention;
	private String outsourcedAddress;
	private String contratoaTerminar;
	private String nombreClausulaAdicionar;
	private String nombreAnexoAdicionar;

	private String anticipatedEndDate;
	private String jurisdictionState;
	private Integer financialEntitiesSelectionLimit;
	private String healthLicenseGrantsAuthority;
	private Integer discountAgreedService;
	private String appraisersPfName;
	private String supplierCompanyName;
	private String cancellationDate;
	private String startExtensionDate;
	private String endExtensionDate;
	private String startFirstExtensionDate;
	private String endFirstExtensionDate;
	private String scheduleService;
	private String descripcionEquipos;
	private String contactoMatenimiento;
	private CurrencyCodeEnum monthlyRentCurrency;
	private String numeroConvenio;
	private String volume;
	private String turnDate;
	private String bookNumber;
	private String towerNameProperty;
	private String customsAgentPatentNumber;
	private String customsState;
	private String financialEntityBranchAddress;
	private Integer contractValidityMonths;
	private Double conventionalPenaltyPercentage;
	private Integer depositsRealizedMonthsNumber;
	private String signState;
	private String condosNumber;

	private String initialAdvanceAmount;
	private String totalAmountForServicesRendered;
	private String financialEntityLegalRepresentativePosition;
	private String supplierLegalRepresentativePosition;
	private String billingCycle;
	private String paymentCycle;
	private String standardizedKeyBankingFinancialEntity;
	private String transferDay;
	private String daysNoticeForEarlyTermination;
	private String nominativeCheckDeliveryDay;
	private String businessDaysAcceptRejectPurchaseOrder;
	private String calendarDayOfDeliveryDate;
	private String businessDaysToModifyCancelOrders;
	private String calendarDaysToWithdrawContract;
	private String daysForAnomalyDeliveryReport;
	private String daysDeadlineForPayment;
	private String financialEntityDirection;
	private String financialEntityAddress;
	private String spaceServiceGranted;
	private String inscriptionCommercialFolioState;
	private String civilLawState;
	private String serviceLocationState;
	private String constructionStagesEndDate;
	private String worksEndDate;
	private String constructionStagesStartDate;
	private String worksStartDate;
	private String subsidiaries;
	private String paymentMethodSubscribers;
	private String deliveryCost;
	private String amountOfInsuranceForDamageOrLoss;
	private String depositAmount;
	private String totalNetAmountOfWorkDone;
	private String supplierIMMS;
	private String compensationMonthsRent;
	private String bankingInstitution;
	private String deliveryMonthNominativeCheck;
	private String transferMonth;
	private String supplierNationality;
	private String squareName;
	private String representativeSocietyName;
	private String personNameSendDailySalesReports;
	private String personMailSendDailySalesReports;
	private String fractionationName;
	private Integer employeesNumber;
	private Integer frameContractNumberTelecomServices;
	private String accountNumberFinancial;
	private String stepsBuildNumber;
	private Integer premisesInTheSquareNumber;
	private String equivalentDepositsMonthsNumber;
	private String supplierObligations;
	private String currencyCountry;
	private String propertyDeliveryPeriod;
	private String totalPaymentPercentajeAmountTotal;
	private String rentEquivalent;
	private String currencyType;
	private String megacableServiceSupplierProvided;
	private String providerServiceMegacableProvided;
	private String megacableObligationsPaymentPercentageExchange;
	private String sellerObligationsPaymentPercentageExchange;
	private String surveying;
	private String strokeStreet;
	private String digitalization;
	private String networkCopy;
	private String networkGPON;
	private String fiberCopy;
	private String surveyingCost;
	private String strokeStreetCost;
	private String digitalizationCost;
	private String networkCopyCost;
	private String networkGPONCost;
	private String fiberCopyCost;
	private String stateCivilLaw;
	private String considerationAmount;
	private String surfaceStoreMerchandise;
	private String propertyBusinessLine;
	private String serviceSchedule;
	private String deliveryCalendarDays;
	private String reimbursementTerminationCalendarDays;
	private String financialEntityAtention;
	private String cityJurisdiction;
	private String publicDeedDate;
	private String registerCommercialFolioDate;
	private Integer updateRequisitionBy;
	private String updateRequisitionDate;
	private User updateByUser;
	private String versionNumber;
	private List<Version> attachmentListDocument = new ArrayList<>();
	private String closedDate;
	private String supplierAtention;
	private String supplierPhone;
	private String supplierAccountNumber;
	private FileUploadInfo templateUploadInfo;
	private String estacionamientosInmueble;
	private String fiadorFechaEscrituraPublica;
	private String fechaTerminacionContratoaFinalizar;
	private String fiadorNumeroEscrituraPublica;
	private String nombreFiador;
	private String penalizacionCantidadRentaMensualLetra;
	private String penalizacionComisionRentaMensual;
	private String penalizacionRentaMensualInmueble;
	private String fiadorNumeroNotariaPublica;
	private String porcentajeIvaContraprestacion;
	// MetroCarrier Fields
	private String negotiatorRepresentativeName;
	private String metrocarrierSquareAddress;
	private String frameworkContractSingDate;
	private String billingContactName;
	private String billingPosition;
	private String billingEmail;
	private String billingPhone;
	private String billingExtension;
	private String billingFax;
	private String technitianContactName;
	private String technitianPosition;
	private String technitianEmail;
	private String technitianPhone;
	private String technitianExtension;
	private String technitianFax;
	private String facturation;
	private String actReceptionService;
	private String ethernetPrivateLine;
	private String vpn;
	private String dedicatedInternet;
	private String digitalPrivateLine;
	private String infrastructure;
	private String videoPrivateLine;
	private String trunk;
	private String otherServices;
	private String cableTv;
	private String businessInternet;
	private String businessTelephony;
	private String intranet;
	private String tecnologySolutions;
	private String googleServiceCloud;
	private String serviceSellerName;
	private String promissoryNoteAmount;
	private Integer idCompany;
	private boolean contractRisk;
	private boolean voBocontractRisk;
	private List<ModifiedClausules> modifiedClausulesList = new ArrayList<>();
	private List<Tracto> tractoList = new ArrayList<>();
	private List<RollOff> rollOffList = new ArrayList<>();
	//
	private List<Version> contractVersionHistory = new ArrayList<>();
	private String cesionDerechos;
	private String prcentajePenaConvencional;
	private String importePolizaSeguro;
	private String obligacionesEspecificas;
	// Digital Signature
	private DocumentDS documentDS;
		
	
	// lista de los representantes legales y testigos seleccionados para la solicitud
	private List<SupplierPersonByRequisition> supplierLegaList;

	public String getObligacionesEspecificas() {
		return obligacionesEspecificas;
	}

	public void setObligacionesEspecificas(String obligacionesEspecificas) {
		this.obligacionesEspecificas = obligacionesEspecificas;
	}

	public String getCesionDerechos() {
		return cesionDerechos;
	}

	public void setCesionDerechos(String cesionDerechos) {
		this.cesionDerechos = cesionDerechos;
	}

	public String getPrcentajePenaConvencional() {
		return prcentajePenaConvencional;
	}

	public void setPrcentajePenaConvencional(String prcentajePenaConvencional) {
		this.prcentajePenaConvencional = prcentajePenaConvencional;
	}

	public String getImportePolizaSeguro() {
		return importePolizaSeguro;
	}

	public void setImportePolizaSeguro(String importePolizaSeguro) {
		this.importePolizaSeguro = importePolizaSeguro;
	}

	/** Lawyer name */
	private String lawyerName;

	public final Integer getIdRequisition() {
		return this.idRequisition;
	}

	public final void setIdRequisition(final Integer idRequisitionParameter) {
		this.idRequisition = idRequisitionParameter;
	}

	public final Integer getIdFlow() {
		return this.idFlow;
	}

	public final void setIdFlow(final Integer idFlowParameter) {
		this.idFlow = idFlowParameter;
	}

	public final Integer getIdApplicant() {
		return this.idApplicant;
	}

	public final void setIdApplicant(final Integer idApplicantParameter) {
		this.idApplicant = idApplicantParameter;
	}

	public final Integer getIdSupplier() {
		return this.idSupplier;
	}

	public final void setIdSupplier(final Integer idSupplierParameter) {
		this.idSupplier = idSupplierParameter;
	}

	public final String getSupplierName() {
		return this.supplierName;
	}

	public final void setSupplierName(final String supplierNameParameter) {
		this.supplierName = supplierNameParameter;
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

	public final Integer getIdLawyer() {
		return this.idLawyer;
	}

	public final void setIdLawyer(final Integer idLawyerParameter) {
		this.idLawyer = idLawyerParameter;
	}

	public final Integer getIdEvaluator() {
		return this.idEvaluator;
	}

	public final void setIdEvaluator(final Integer idEvaluatorParameter) {
		this.idEvaluator = idEvaluatorParameter;
	}

	public final Integer getIdAreaTender() {
		return this.idAreaTender;
	}

	public final void setIdAreaTender(final Integer idAreaTenderParameter) {
		this.idAreaTender = idAreaTenderParameter;
	}

	public final String getApplicationDate() {
		return this.applicationDate;
	}

	public final void setApplicationDate(final String applicationDateParameter) {
		this.applicationDate = applicationDateParameter;
	}

	public final Integer getIdDocumentType() {
		return this.idDocumentType;
	}

	public final void setIdDocumentType(final Integer idDocumentTypeParameter) {
		this.idDocumentType = idDocumentTypeParameter;
	}

	public CatDocumentType getCatDocumentType() {
		return catDocumentType;
	}

	public void setCatDocumentType(CatDocumentType catDocumentType) {
		this.catDocumentType = catDocumentType;
	}

	public Integer getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(Integer idDocumentParameter) {
		this.idDocument = idDocumentParameter;
	}

	public final String getDocumentTypeName() {
		return this.documentTypeName;
	}

	public final void setDocumentTypeName(final String documentTypeNameParameter) {
		this.documentTypeName = documentTypeNameParameter;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentNameParameter) {
		this.documentName = documentNameParameter;
	}

	public final Integer getTemplateIdDocument() {
		return this.templateIdDocument;
	}

	public final void setTemplateIdDocument(final Integer templateIdDocumentParameter) {
		this.templateIdDocument = templateIdDocumentParameter;
	}

	public final String getAuthorizationDocumentName() {
		return this.authorizationDocumentName;
	}

	public final void setAuthorizationDocumentName(final String authorizationDocumentNameParameter) {
		this.authorizationDocumentName = authorizationDocumentNameParameter;
	}

	public final Integer getAuthorizationDocumentIdDoc() {
		return this.authorizationDocumentIdDoc;
	}

	public final void setAuthorizationDocumentIdDoc(final Integer authorizationDocumentIdDocParameter) {
		this.authorizationDocumentIdDoc = authorizationDocumentIdDocParameter;
	}

	public final String getServiceDescription() {
		return this.serviceDescription;
	}

	public final void setServiceDescription(final String serviceDescriptionParameter) {
		this.serviceDescription = serviceDescriptionParameter;
	}

	public final String getTechnicalDetails() {
		return this.technicalDetails;
	}

	public final void setTechnicalDetails(final String technicalDetailsParameter) {
		this.technicalDetails = technicalDetailsParameter;
	}

	public final Boolean getAttatchmentDeliverables() {
		return this.attatchmentDeliverables;
	}

	public final void setAttatchmentDeliverables(final Boolean attatchmentDeliverablesParameter) {
		this.attatchmentDeliverables = attatchmentDeliverablesParameter;
	}

	public final Boolean getAttchmtServiceLevelsMeasuring() {
		return this.attchmtServiceLevelsMeasuring;
	}

	public final void setAttchmtServiceLevelsMeasuring(final Boolean attatchmentServiceLevelsMeasuringParameter) {
		this.attchmtServiceLevelsMeasuring = attatchmentServiceLevelsMeasuringParameter;
	}

	public final Boolean getAttatchmentPenalty() {
		return this.attatchmentPenalty;
	}

	public final void setAttatchmentPenalty(final Boolean attatchmentPenaltyParameters) {
		this.attatchmentPenalty = attatchmentPenaltyParameters;
	}

	public final Boolean getAttatchmentScalingMatrix() {
		return this.attatchmentScalingMatrix;
	}

	public final void setAttatchmentScalingMatrix(final Boolean attatchmentScalingMatrixParameter) {
		this.attatchmentScalingMatrix = attatchmentScalingMatrixParameter;
	}

	public final Boolean getAttatchmentCompensation() {
		return this.attatchmentCompensation;
	}

	public final void setAttatchmentCompensation(final Boolean attatchmentCompensationParameter) {
		this.attatchmentCompensation = attatchmentCompensationParameter;
	}

	public final Boolean getAttchmtBusinessMonitoringPlan() {
		return this.attchmtBusinessMonitoringPlan;
	}

	public final void setAttchmtBusinessMonitoringPlan(final Boolean attatchmentBusinessMonitoringPlanParameter) {
		this.attchmtBusinessMonitoringPlan = attatchmentBusinessMonitoringPlanParameter;
	}

	public final Boolean getAttchmtImssInfoDeliveryReqrmts() {
		return this.attchmtImssInfoDeliveryReqrmts;
	}

	public final void setAttchmtImssInfoDeliveryReqrmts(
			final Boolean attatchmentImssInformationDeliveryRequirementsParameter) {
		this.attchmtImssInfoDeliveryReqrmts = attatchmentImssInformationDeliveryRequirementsParameter;
	}

	public final Boolean getAttatchmentInformationSecurity() {
		return this.attatchmentInformationSecurity;
	}

	public final void setAttatchmentInformationSecurity(final Boolean attatchmentInformationSecurityParameter) {
		this.attatchmentInformationSecurity = attatchmentInformationSecurityParameter;
	}

	public final Boolean getAttatchmentOthers() {
		return this.attatchmentOthers;
	}

	public final void setAttatchmentOthers(final Boolean attatchmentOthersParameter) {
		this.attatchmentOthers = attatchmentOthersParameter;
	}

	public final String getAttatchmentOthersName() {
		return this.attatchmentOthersName;
	}

	public final void setAttatchmentOthersName(final String attatchmentOthersNameParameter) {
		this.attatchmentOthersName = attatchmentOthersNameParameter;
	}

	public final FlowPurchasingEnum getStatus() {
		return this.status;
	}

	public final void setStatus(final FlowPurchasingEnum statusParameter) {
		this.status = statusParameter;
	}

	public final List<Integer> getApprovalAreasList() {
		return this.approvalAreasList;
	}

	public final void setApprovalAreasList(final List<Integer> approvalAreasListParameter) {
		this.approvalAreasList = approvalAreasListParameter;
	}

	public List<String> getApprovalAreasActiveList() {
		return approvalAreasActiveList;
	}

	public void setApprovalAreasActiveList(List<String> approvalAreasActiveList) {
		this.approvalAreasActiveList = approvalAreasActiveList;
	}

	public final List<Integer> getAuthorizationDgasList() {
		return this.authorizationDgasList;
	}

	public final void setAuthorizationDgasList(final List<Integer> authorizationDgasListParameter) {
		this.authorizationDgasList = authorizationDgasListParameter;
	}

	public List<Dga> getAuthorizationDgasActiveList() {
		return authorizationDgasActiveList;
	}

	public void setAuthorizationDgasActiveList(List<Dga> authorizationDgasActiveList) {
		this.authorizationDgasActiveList = authorizationDgasActiveList;
	}

	public final List<Integer> getFinancialEntityList() {
		return this.financialEntityList;
	}

	public final void setFinancialEntityList(final List<Integer> financialEntityListParameter) {
		this.financialEntityList = financialEntityListParameter;
	}

	public final List<LegalRepresentative> getLegalRepresentativesList() {
		return this.legalRepresentativesList;
	}

	public final void setLegalRepresentativesList(final List<LegalRepresentative> legalRepresentativesListParam) {
		this.legalRepresentativesList = legalRepresentativesListParam;
	}

	public final List<Integer> getUsersToVoboList() {
		return this.usersToVoboList;
	}

	public final void setUsersToVoboList(final List<Integer> usersToVoboListParameter) {
		this.usersToVoboList = usersToVoboListParameter;
	}

	public List<User> getUsersToVoboUserList() {
		return usersToVoboUserList;
	}

	public void setUsersToVoboUserList(List<User> usersToVoboUserList) {
		this.usersToVoboUserList = usersToVoboUserList;
	}

	public final List<String> getFinancialEntityWitnessesList() {
		return this.financialEntityWitnessesList;
	}

	public final void setFinancialEntityWitnessesList(final List<String> financialEntityWitnessesListParameter) {
		this.financialEntityWitnessesList = financialEntityWitnessesListParameter;
	}

	public final ValidityEnum getValidity() {
		return this.validity;
	}

	public final void setValidity(final ValidityEnum validityParameter) {
		this.validity = validityParameter;
	}

	public final Boolean getAutomaticRenewal() {
		return this.automaticRenewal;
	}

	public final void setAutomaticRenewal(final Boolean automaticRenewalParameter) {
		this.automaticRenewal = automaticRenewalParameter;
	}

	public final Integer getRenewalPeriods() {
		return this.renewalPeriods;
	}

	public final void setRenewalPeriods(final Integer renewalPeriodsParameter) {
		this.renewalPeriods = renewalPeriodsParameter;
	}

	public final String getValidityStartDate() {
		return this.validityStartDate;
	}

	public final void setValidityStartDate(final String validityStartDateParameter) {
		this.validityStartDate = validityStartDateParameter;
	}

	public final String getValidityEndDate() {
		return this.validityEndDate;
	}

	public final void setValidityEndDate(final String validityEndDateParameter) {
		this.validityEndDate = validityEndDateParameter;
	}

	public final String getValidityClause() {
		return this.validityClause;
	}

	public final void setValidityClause(final String validityClauseParameter) {
		this.validityClause = validityClauseParameter;
	}

	public final Boolean getIsRequiredHumanResources() {
		return this.isRequiredHumanResources;
	}

	public final void setIsRequiredHumanResources(final Boolean isRequiredHumanResourcesParameter) {
		this.isRequiredHumanResources = isRequiredHumanResourcesParameter;
	}

	public final Boolean getIsImssCeduleGiven() {
		return this.isImssCeduleGiven;
	}

	public final void setIsImssCeduleGiven(final Boolean isImssCeduleParameter) {
		this.isImssCeduleGiven = isImssCeduleParameter;
	}

	public final Integer getImssCeduleNotGivenIdDocument() {
		return this.imssCeduleNotGivenIdDocument;
	}

	public final void setImssCeduleNotGivenIdDocument(final Integer imssCeduleNotGivenIdDocumentParameter) {
		this.imssCeduleNotGivenIdDocument = imssCeduleNotGivenIdDocumentParameter;
	}

	public final Boolean getIsSupplierSingleRegistered() {
		return this.isSupplierSingleRegistered;
	}

	public final void setIsSupplierSingleRegistered(final Boolean isSupplierSingleRegisteredParameter) {
		this.isSupplierSingleRegistered = isSupplierSingleRegisteredParameter;
	}

	public final Boolean getIsSupplierPenalties() {
		return this.isSupplierPenalties;
	}

	public final void setIsSupplierPenalties(final Boolean isSupplierPenaltiesParameter) {
		this.isSupplierPenalties = isSupplierPenaltiesParameter;
	}

	public final Boolean getIsAdvanceBailNeeded() {
		return this.isAdvanceBailNeeded;
	}

	public final void setIsAdvanceBailNeeded(final Boolean isAdvanceBailNeededParameter) {
		this.isAdvanceBailNeeded = isAdvanceBailNeededParameter;
	}

	public final Double getAdvanceBailAmount() {
		return this.advanceBailAmount;
	}

	public final void setAdvanceBailAmount(final Double advanceBailAmountParameter) {
		this.advanceBailAmount = advanceBailAmountParameter;
	}

	public final Integer getAdvanceBailNotNeededIdDocument() {
		return this.advanceBailNotNeededIdDocument;
	}

	public final void setAdvanceBailNotNeededIdDocument(final Integer advanceBailNotNeededIdDocumentParameter) {
		this.advanceBailNotNeededIdDocument = advanceBailNotNeededIdDocumentParameter;
	}

	public final Boolean getIsFulfillmentBailNeeded() {
		return this.isFulfillmentBailNeeded;
	}

	public final void setIsFulfillmentBailNeeded(final Boolean isFulfillmentBailNeededParameter) {
		this.isFulfillmentBailNeeded = isFulfillmentBailNeededParameter;
	}

	public final Double getFulfillmentBailAmount() {
		return this.fulfillmentBailAmount;
	}

	public final void setFulfillmentBailAmount(final Double fulfillmentBailAmountParameter) {
		this.fulfillmentBailAmount = fulfillmentBailAmountParameter;
	}

	public final Integer getFulfillmentBailNeedNoIdDoc() {
		return this.fulfillmentBailNotNeededIdDocument;
	}

	public final void setFulfillmentBailNeedNoIdDoc(final Integer fulfillmentBailNotNeededIdDocumentParameter) {
		this.fulfillmentBailNotNeededIdDocument = fulfillmentBailNotNeededIdDocumentParameter;
	}

	public final Boolean getIsFidelityBailNeeded() {
		return this.isFidelityBailNeeded;
	}

	public final void setIsFidelityBailNeeded(final Boolean isFidelityBailNeededParameter) {
		this.isFidelityBailNeeded = isFidelityBailNeededParameter;
	}

	public final Integer getFidelityBailNeedNoIdDoc() {
		return this.fidelityBailNotNeededIdDocument;
	}

	public final void setFidelityBailNeedNoIdDoc(final Integer fidelityBailNotNeededIdDocumentParameter) {
		this.fidelityBailNotNeededIdDocument = fidelityBailNotNeededIdDocumentParameter;
	}

	public final Boolean getIsContingencyBailNeeded() {
		return this.isContingencyBailNeeded;
	}

	public final void setIsContingencyBailNeeded(final Boolean isContingencyBailNeededParameter) {
		this.isContingencyBailNeeded = isContingencyBailNeededParameter;
	}

	public final Integer getContingencyBailNeedNoIdDoc() {
		return this.contingencyBailNotNeededIdDocument;
	}

	public final void setContingencyBailNeedNoIdDoc(final Integer contingencyBailNotNeededIdDocumentParameter) {
		this.contingencyBailNotNeededIdDocument = contingencyBailNotNeededIdDocumentParameter;
	}

	public final Boolean getIsCivilRespInsuranceBailNeeded() {
		return this.isCivilRespInsuranceBailNeeded;
	}

	public final void setIsCivilRespInsuranceBailNeeded(
			final Boolean isCivilResponsabilityInsuranceBailNeededParameter) {
		this.isCivilRespInsuranceBailNeeded = isCivilResponsabilityInsuranceBailNeededParameter;
	}

	public final Integer getCivilRespInsurBailNeedNoIdDoc() {
		return this.civilResponsabilityInsuranceBailNotNeededIdDocument;
	}

	public final void setCivilRespInsurBailNeedNoIdDoc(
			final Integer civilResponsabilityInsuranceBailNotNeededIdDocumentParameter) {
		this.civilResponsabilityInsuranceBailNotNeededIdDocument = civilResponsabilityInsuranceBailNotNeededIdDocumentParameter;
	}

	public final Boolean getIsContractTermAuthorized() {
		return this.isContractTermAuthorized;
	}

	public final void setIsContractTermAuthorized(final Boolean isContractTermAuthorizedParameter) {
		this.isContractTermAuthorized = isContractTermAuthorizedParameter;
	}

	public final Boolean getIsSpecialProvisionNegotiated() {
		return this.isSpecialProvisionNegotiated;
	}

	public final void setIsSpecialProvisionNegotiated(final Boolean isSpecialProvisionNegotiatedParameter) {
		this.isSpecialProvisionNegotiated = isSpecialProvisionNegotiatedParameter;
	}

	public final Boolean getIsWarrantyDeposit() {
		return this.isWarrantyDeposit;
	}

	public final void setIsWarrantyDeposit(final Boolean isSecurityDepositParameter) {
		this.isWarrantyDeposit = isSecurityDepositParameter;
	}

	public final Boolean getIsStaffUnderGroupDirecction() {
		return this.isStaffUnderGroupDirecction;
	}

	public final void setIsStaffUnderGroupDirecction(final Boolean isStaffUnderGroupDirecctionParameter) {
		this.isStaffUnderGroupDirecction = isStaffUnderGroupDirecctionParameter;
	}

	public final Map<Integer, Integer> getSpecialClausesMap() {
		return this.specialClausesMap;
	}

	public final void setSpecialClausesMap(final Map<Integer, Integer> specialClausesMapParameter) {
		this.specialClausesMap = specialClausesMapParameter;
	}

	public final FlowScreenAction getFlowScreenActionParams() {
		return this.flowScreenActionParams;
	}

	public final void setFlowScreenActionParams(final FlowScreenAction flowScreenActionParamsParameter) {
		this.flowScreenActionParams = flowScreenActionParamsParameter;
	}

	public final String getContractObjectClause() {
		return this.contractObjectClause;
	}

	public final void setContractObjectClause(final String contractObjectClauseParameter) {
		this.contractObjectClause = contractObjectClauseParameter;
	}

	public final String getConsiderationClause() {
		return this.considerationClause;
	}

	public final void setConsiderationClause(final String considerationClauseParameter) {
		this.considerationClause = considerationClauseParameter;
	}

	public final String getClabe() {
		return this.clabe;
	}

	public final void setClabe(final String clabeParameter) {
		this.clabe = clabeParameter;
	}

	public final String getContractDurationDate() {
		return this.contractDurationDate;
	}

	public final void setContractDurationDate(final String contractDurationDateParameter) {
		this.contractDurationDate = contractDurationDateParameter;
	}

	public final String getConsiderationInitialReport() {
		return this.considerationInitialReport;
	}

	public final void setConsiderationInitialReport(final String considerationInitialReportParameter) {
		this.considerationInitialReport = considerationInitialReportParameter;
	}

	public final String getConsiderationMonthlyReport() {
		return this.considerationMonthlyReport;
	}

	public final void setConsiderationMonthlyReport(final String considerationMonthlyReportParameter) {
		this.considerationMonthlyReport = considerationMonthlyReportParameter;
	}

	public final String getConsiderExtraordinaryReport() {
		return this.considerationExtraordinaryReport;
	}

	public final void setConsiderExtraordinaryReport(final String considerationExtraordinaryReportParameter) {
		this.considerationExtraordinaryReport = considerationExtraordinaryReportParameter;
	}

	public final Double getInitialPaymentPercentage() {
		return this.initialPaymentPercentage;
	}

	public final void setInitialPaymentPercentage(final Double initialPaymentPercentageParameter) {
		this.initialPaymentPercentage = initialPaymentPercentageParameter;
	}

	public final String getInitialPaymentPeriod() {
		return this.initialPaymentPeriod;
	}

	public final void setInitialPaymentPeriod(final String initialPaymentPeriodParameter) {
		this.initialPaymentPeriod = initialPaymentPeriodParameter;
	}

	public final Double getMonthlyPaymentPercentage() {
		return this.monthlyPaymentPercentage;
	}

	public final void setMonthlyPaymentPercentage(final Double monthlyPaymentPercentageParameter) {
		this.monthlyPaymentPercentage = monthlyPaymentPercentageParameter;
	}

	public final String getMonthlyPaymentPeriod() {
		return this.monthlyPaymentPeriod;
	}

	public final void setMonthlyPaymentPeriod(final String monthlyPaymentPeriodParameter) {
		this.monthlyPaymentPeriod = monthlyPaymentPeriodParameter;
	}

	public final Double getExtraordinaryPaymentPercentage() {
		return this.extraordinaryPaymentPercentage;
	}

	public final void setExtraordinaryPaymentPercentage(final Double extraordinaryPaymentPercentageParameter) {
		this.extraordinaryPaymentPercentage = extraordinaryPaymentPercentageParameter;
	}

	public final String getExtraordinaryPaymentPeriod() {
		return this.extraordinaryPaymentPeriod;
	}

	public final void setExtraordinaryPaymentPeriod(final String extraordinaryPaymentPeriodParameter) {
		this.extraordinaryPaymentPeriod = extraordinaryPaymentPeriodParameter;
	}

	public final String getDomainName() {
		return this.domainName;
	}

	public final void setDomainName(final String domainNameParameter) {
		this.domainName = domainNameParameter;
	}

	public final String getBrandName() {
		return this.brandName;
	}

	public final void setBrandName(final String brandNameParameter) {
		this.brandName = brandNameParameter;
	}

	public final String getSubcontractorLegalRepName() {
		return this.subcontractorLegalRepresentativeName;
	}

	public final void setSubcontractorLegalRepName(final String subcontractorLegalRepresentativeNameParameter) {
		this.subcontractorLegalRepresentativeName = subcontractorLegalRepresentativeNameParameter;
	}

	public final String getExtensionsNumber() {
		return this.extensionsNumber;
	}

	public final void setExtensionsNumber(final String extensionsNumberParameter) {
		this.extensionsNumber = extensionsNumberParameter;
	}

	public final String getExtensionYears() {
		return this.extensionYears;
	}

	public final void setExtensionYears(final String extensionYearsParameter) {
		this.extensionYears = extensionYearsParameter;
	}

	public final String getExtensionPeriod() {
		return this.extensionPeriod;
	}

	public final void setExtensionPeriod(final String extensionPeriodParameter) {
		this.extensionPeriod = extensionPeriodParameter;
	}

	public final String getExtensionValidity() {
		return this.extensionValidity;
	}

	public final void setExtensionValidity(final String extensionValidityParameter) {
		this.extensionValidity = extensionValidityParameter;
	}

	public final String getExtensionForcedYears() {
		return this.extensionForcedYears;
	}

	public final void setExtensionForcedYears(final String extensionForcedYearsParameter) {
		this.extensionForcedYears = extensionForcedYearsParameter;
	}

	public final String getExtensionVoluntaryYears() {
		return this.extensionVoluntaryYears;
	}

	public final void setExtensionVoluntaryYears(final String extensionVoluntaryYearsParameter) {
		this.extensionVoluntaryYears = extensionVoluntaryYearsParameter;
	}

	public final String getRentInitialQuantity() {
		return this.rentInitialQuantity;
	}

	public final void setRentInitialQuantity(final String rentInitialQuantityParameter) {
		this.rentInitialQuantity = rentInitialQuantityParameter;
	}

	public final String getExtensionFirstYearRent() {
		return this.extensionFirstYearRent;
	}

	public final void setExtensionFirstYearRent(final String extensionFirstYearRentParameter) {
		this.extensionFirstYearRent = extensionFirstYearRentParameter;
	}

	public final String getMaintenanceInitialQuantity() {
		return this.maintenanceInitialQuantity;
	}

	public final void setMaintenanceInitialQuantity(final String maintenanceInitialQuantityParameter) {
		this.maintenanceInitialQuantity = maintenanceInitialQuantityParameter;
	}

	public final String getNatPersonTenantDeclarations() {
		return this.naturalPersonTenantDeclarations;
	}

	public final void setNatPersonTenantDeclarations(final String naturalPersonTenantDeclarationsParameter) {
		this.naturalPersonTenantDeclarations = naturalPersonTenantDeclarationsParameter;
	}

	public final String getMoralOrNatPersonDeclarations() {
		return this.moralOrNaturalPersonDeclarations;
	}

	public final void setMoralOrNatPersonDeclarations(final String moralOrNaturalPersonDeclarationsParameter) {
		this.moralOrNaturalPersonDeclarations = moralOrNaturalPersonDeclarationsParameter;
	}

	public final String getContractDate() {
		return this.contractDate;
	}

	public final void setContractDate(final String contractDateParameter) {
		this.contractDate = contractDateParameter;
	}

	public final String getSurface() {
		return this.surface;
	}

	public final void setSurface(final String surfaceParameter) {
		this.surface = surfaceParameter;
	}

	public final String getPropertyDeedTitleNumber() {
		return this.propertyDeedTitleNumber;
	}

	public final void setPropertyDeedTitleNumber(final String propertyDeedTitleNumberParameter) {
		this.propertyDeedTitleNumber = propertyDeedTitleNumberParameter;
	}

	public final String getPropertyDeedTitleDate() {
		return this.propertyDeedTitleDate;
	}

	public final void setPropertyDeedTitleDate(final String propertyDeedTitleDateParameter) {
		this.propertyDeedTitleDate = propertyDeedTitleDateParameter;
	}

	public final String getPropertyDeedTitleNotary() {
		return this.propertyDeedTitleNotary;
	}

	public final void setPropertyDeedTitleNotary(final String propertyDeedTitleNotaryParameter) {
		this.propertyDeedTitleNotary = propertyDeedTitleNotaryParameter;
	}

	public final String getPropertyDeedTitleNotaryNumber() {
		return this.propertyDeedTitleNotaryNumber;
	}

	public final void setPropertyDeedTitleNotaryNumber(final String propertyDeedTitleNotaryNumberParameter) {
		this.propertyDeedTitleNotaryNumber = propertyDeedTitleNotaryNumberParameter;
	}

	public final String getPropertyDeedTitleNotaryState() {
		return this.propertyDeedTitleNotaryState;
	}

	public final void setPropertyDeedTitleNotaryState(final String propertyDeedTitleNotaryStateParameter) {
		this.propertyDeedTitleNotaryState = propertyDeedTitleNotaryStateParameter;
	}

	public final String getPropDeedTitleCommercialFolio() {
		return this.propertyDeedTitleCommercialFolio;
	}

	public final void setPropDeedTitleCommercialFolio(final String propertyDeedTitleCommercialFolioParameter) {
		this.propertyDeedTitleCommercialFolio = propertyDeedTitleCommercialFolioParameter;
	}

	public final String getPropDeedTitleRegistrationDate() {
		return this.propertyDeedTitleRegistrationDate;
	}

	public final void setPropDeedTitleRegistrationDate(final String propertyDeedTitleRegistrationDateParameter) {
		this.propertyDeedTitleRegistrationDate = propertyDeedTitleRegistrationDateParameter;
	}

	public final String getSubcontactedPersonality() {
		return this.subcontactedPersonality;
	}

	public final void setSubcontactedPersonality(final String subcontactedPersonalityParameter) {
		this.subcontactedPersonality = subcontactedPersonalityParameter;
	}

	public final String getDirectSupplierPersonality() {
		return this.directSupplierPersonality;
	}

	public final void setDirectSupplierPersonality(final String directSupplierPersonalityParameter) {
		this.directSupplierPersonality = directSupplierPersonalityParameter;
	}

	/**
	 * @return the contratista
	 */
	public ContratistaEnum getContractType1() {
		return contractType;
	}

	/**
	 * @return the contratista
	 */
	public String getContractType() {
		if (contractType == null) {
			return null;
		} else {
			return contractType.toString();
		}
	}

	/**
	 * @param contratista the contratista to set
	 */
	public void setContractType(ContratistaEnum contractType) {
		this.contractType = contractType;
	}

	public void setContractType(String contractType) {
		if (null == contractType) {
			this.contractType = null;
		} else {
			this.contractType = ContratistaEnum.valueOf(contractType);
		}
	}

	public final String getEventName() {
		return this.eventName;
	}

	public final void setEventName(final String eventNameParameter) {
		this.eventName = eventNameParameter;
	}

	public final String getEventDatetime() {
		return this.eventDatetime;
	}

	public final void setEventDatetime(final String eventDatetimeParameter) {
		this.eventDatetime = eventDatetimeParameter;
	}

	public final String getClausulesToModify() {
		return this.clausulesToModify;
	}

	public final void setClausulesToModify(final String clausulesToModifyParameter) {
		this.clausulesToModify = clausulesToModifyParameter;
	}

	public final String getContractValidity() {
		return this.contractValidity;
	}

	public final void setContractValidity(final String contractValidityParameter) {
		this.contractValidity = contractValidityParameter;
	}

	public final String getMaintenanceClause() {
		return this.maintenanceClause;
	}

	public final void setMaintenanceClause(final String maintenanceClauseParameter) {
		this.maintenanceClause = maintenanceClauseParameter;
	}

	public final String getSignDate() {
		return this.signDate;
	}

	public final void setSignDate(final String signDateParameter) {
		this.signDate = signDateParameter;
	}

	public final Comment getComment() {
		return this.comment;
	}

	public final void setComment(final Comment commentParameter) {
		this.comment = commentParameter;
	}

	public final String getSupplierSignSendDate() {
		return this.supplierSignSendDate;
	}

	public final void setSupplierSignSendDate(final String supplierSignDateParameter) {
		this.supplierSignSendDate = supplierSignDateParameter;
	}

	public final String getWitnessesSignSendDate() {
		return this.witnessesSignSendDate;
	}

	public final void setWitnessesSignSendDate(final String witnessesSignDateParameter) {
		this.witnessesSignSendDate = witnessesSignDateParameter;
	}

	public final String getSupplierSignReturnDate() {
		return this.supplierSignReturnDate;
	}

	public final void setSupplierSignReturnDate(final String supplierSignReturnDateParameter) {
		this.supplierSignReturnDate = supplierSignReturnDateParameter;
	}

	public final String getWitnessesSignReturnDate() {
		return this.witnessesSignReturnDate;
	}

	public final void setWitnessesSignReturnDate(final String witnessesSignReturnDateParameter) {
		this.witnessesSignReturnDate = witnessesSignReturnDateParameter;
	}

	public final Boolean getIsOrigSignCntrDelvrdSupplier() {
		return this.isOrigSignCntrDelvrdSupplier;
	}

	public final void setIsOrigSignCntrDelvrdSupplier(
			final Boolean isOriginalSignedContractDeliveredSupplierParameter) {
		this.isOrigSignCntrDelvrdSupplier = isOriginalSignedContractDeliveredSupplierParameter;
	}

	public final Boolean getIsCpySignCntrDelvrdSupplier() {
		return this.isCpySignCntrDelvrdSupplier;
	}

	public final void setIsCpySignCntrDelvrdSupplier(final Boolean isCopySignedContractDeliveredSupplierParameter) {
		this.isCpySignCntrDelvrdSupplier = isCopySignedContractDeliveredSupplierParameter;
	}

	public final String getSignedContractSendDateSupplier() {
		return this.signedContractSendDateSupplier;
	}

	public final void setSignedContractSendDateSupplier(final String signedContractSendDateSupplierParameter) {
		this.signedContractSendDateSupplier = signedContractSendDateSupplierParameter;
	}

	public final Boolean getIsOrigSignCntrDelvrdWitnesses() {
		return this.isOrigSignCntrDelvrdWitnesses;
	}

	public final void setIsOrigSignCntrDelvrdWitnesses(
			final Boolean isOriginalSignedContractDeliveredWitnessesParameter) {
		this.isOrigSignCntrDelvrdWitnesses = isOriginalSignedContractDeliveredWitnessesParameter;
	}

	public final Boolean getIsCpySignCntrDelvrdWitnesses() {
		return this.isCpySignCntrDelvrdWitnesses;
	}

	public final void setIsCpySignCntrDelvrdWitnesses(final Boolean isCopySignedContractDeliveredWitnessesParameter) {
		this.isCpySignCntrDelvrdWitnesses = isCopySignedContractDeliveredWitnessesParameter;
	}

	public final String getSigndContractSendDateWitnesses() {
		return this.signedContractSendDateWitnesses;
	}

	public final void setSigndContractSendDateWitnesses(final String signedContractSendDateWitnessesParameter) {
		this.signedContractSendDateWitnesses = signedContractSendDateWitnessesParameter;
	}

	public final Boolean getIsOrigSignCntrDelvrdRegistry() {
		return this.isOrigSignCntrDelvrdRegistry;
	}

	public final void setIsOrigSignCntrDelvrdRegistry(
			final Boolean isOriginalSignedContractDeliveredRegistryParameter) {
		this.isOrigSignCntrDelvrdRegistry = isOriginalSignedContractDeliveredRegistryParameter;
	}

	public final Boolean getIsCpySignCntrDelvrdRegistry() {
		return this.isCpySignCntrDelvrdRegistry;
	}

	public final void setIsCpySignCntrDelvrdRegistry(final Boolean isCopySignedContractDeliveredRegistryParameter) {
		this.isCpySignCntrDelvrdRegistry = isCopySignedContractDeliveredRegistryParameter;
	}

	public final String getSignedContractSendDateRegistry() {
		return this.signedContractSendDateRegistry;
	}

	public final void setSignedContractSendDateRegistry(final String signedContractSendDateRegistryParameter) {
		this.signedContractSendDateRegistry = signedContractSendDateRegistryParameter;
	}

	public final String getBusinessReasonMonitoringPlan() {
		return this.businessReasonMonitoringPlan;
	}

	public final void setBusinessReasonMonitoringPlan(final String businessReasonMonitoringPlanParameter) {
		this.businessReasonMonitoringPlan = businessReasonMonitoringPlanParameter;
	}

	public final String getSupplierPenaltiesText() {
		return this.supplierPenaltiesText;
	}

	public final void setSupplierPenaltiesText(final String supplierPenaltiesTextParameter) {
		this.supplierPenaltiesText = supplierPenaltiesTextParameter;
	}

	public final String getSpecialProvisionNegotiated() {
		return this.specialProvisionNegotiated;
	}

	public final void setSpecialProvisionNegotiated(final String specialProvisionNegotiatedParameter) {
		this.specialProvisionNegotiated = specialProvisionNegotiatedParameter;
	}

	public final List<FileUploadInfo> getDocumentsAttachmentList() {
		return this.documentsAttachment;
	}

	public final void setDocumentsAttachmentList(final List<FileUploadInfo> documentsAttachmentParameter) {
		this.documentsAttachment = documentsAttachmentParameter;
	}

	public final FileUploadInfo getAuthorizationDocument() {
		return this.authorizationDocument;
	}

	public final void setAuthorizationDocument(final FileUploadInfo authorizationDocumentParameter) {
		this.authorizationDocument = authorizationDocumentParameter;
	}

	public final List<FileUploadInfo> getDocumentsAttachment() {
		return this.documentsAttachment;
	}

	public final void setDocumentsAttachment(final List<FileUploadInfo> documentsAttachmentParameter) {
		this.documentsAttachment = documentsAttachmentParameter;
	}

	public final FileUploadInfo getImssCeduleNotGivenDocument() {
		return this.imssCeduleNotGivenDocument;
	}

	public final void setImssCeduleNotGivenDocument(final FileUploadInfo imssCeduleNotGivenDocumentParameter) {
		this.imssCeduleNotGivenDocument = imssCeduleNotGivenDocumentParameter;
	}

	public final FileUploadInfo getAdvanceBailNotNeededDocument() {
		return this.advanceBailNotNeededDocument;
	}

	public final void setAdvanceBailNotNeededDocument(final FileUploadInfo advanceBailNotNeededDocumentParameter) {
		this.advanceBailNotNeededDocument = advanceBailNotNeededDocumentParameter;
	}

	public final FileUploadInfo getFulfillmentBailNotNeededDocument() {
		return this.fulfillmentBailNotNeededDocument;
	}

	public final void setFulfillmentBailNotNeededDocument(
			final FileUploadInfo fulfillmentBailNotNeededDocumentParameter) {
		this.fulfillmentBailNotNeededDocument = fulfillmentBailNotNeededDocumentParameter;
	}

	public final FileUploadInfo getFidelityBailNotNeededDocument() {
		return this.fidelityBailNotNeededDocument;
	}

	public final void setFidelityBailNotNeededDocument(final FileUploadInfo fidelityBailNotNeededDocumentParameter) {
		this.fidelityBailNotNeededDocument = fidelityBailNotNeededDocumentParameter;
	}

	public final FileUploadInfo getContingencyBailNotNeededDocument() {
		return this.contingencyBailNotNeededDocument;
	}

	public final void setContingencyBailNotNeededDocument(
			final FileUploadInfo contingencyBailNotNeededDocumentParameter) {
		this.contingencyBailNotNeededDocument = contingencyBailNotNeededDocumentParameter;
	}

	public final FileUploadInfo getCivilResponsabilityInsuranceBailNotNeededDocument() {
		return this.civilResponsabilityInsuranceBailNotNeededDocument;
	}

	public final void setCivilResponsabilityInsuranceBailNotNeededDocument(
			final FileUploadInfo civilResponsabilityInsuranceBailNotNeededDocumentParameter) {
		this.civilResponsabilityInsuranceBailNotNeededDocument = civilResponsabilityInsuranceBailNotNeededDocumentParameter;
	}

	public final List<VoBoDocumentFile> getDocumentsAreasVoBoList() {
		return this.documentsAreasVoBoList;
	}

	public final void setDocumentsAreasVoBoList(final List<VoBoDocumentFile> documentsAreasVoBoListParameter) {
		this.documentsAreasVoBoList = documentsAreasVoBoListParameter;
	}

	public final Integer getSupplierApprovalIdDocument() {
		return this.supplierApprovalIdDocument;
	}

	public final void setSupplierApprovalIdDocument(final Integer supplierApprovalIdDocumentParameter) {
		this.supplierApprovalIdDocument = supplierApprovalIdDocumentParameter;
	}

	public final FileUploadInfo getSupplierApprovalDocument() {
		return this.supplierApprovalDocument;
	}

	public final void setSupplierApprovalDocument(final FileUploadInfo supplierApprovalDocumentParameter) {
		this.supplierApprovalDocument = supplierApprovalDocumentParameter;
	}

	public final List<FileUploadInfo> getDigitalizationDocument() {
		return this.digitalizationDocument;
	}

	public final void setDigitalizationDocument(final List<FileUploadInfo> digitalizationDocumentParameter) {
		this.digitalizationDocument = digitalizationDocumentParameter;
	}

	public final Supplier getSupplier() {
		return this.supplier;
	}

	public final void setSupplier(final Supplier supplierParameter) {
		this.supplier = supplierParameter;
	}

	public final List<Integer> getLegalReprentativeIdList() {
		return this.legalReprentativeIdList;
	}

	public final void setLegalReprentativeIdList(final List<Integer> legalReprentativeIdListParameter) {
		this.legalReprentativeIdList = legalReprentativeIdListParameter;
	}

	public final String getProfessionalLicense() {
		return this.professionalLicense;
	}

	public final void setProfessionalLicense(final String professionalLicenseParameter) {
		this.professionalLicense = professionalLicenseParameter;
	}

	public final String getExpeditionDateProfLicense() {
		return this.expeditionDateProfessionalLicense;
	}

	public final void setExpeditionDateProfLicense(final String expeditionDateProfessionalLicenseParameter) {
		this.expeditionDateProfessionalLicense = expeditionDateProfessionalLicenseParameter;
	}

	public final String getSpecialty() {
		return this.specialty;
	}

	public final void setSpecialty(final String specialtyParameter) {
		this.specialty = specialtyParameter;
	}

	public final String getSpecialtyCeduleNumber() {
		return this.specialtyCeduleNumber;
	}

	public final void setSpecialtyCeduleNumber(final String specialtyCeduleNumberParameter) {
		this.specialtyCeduleNumber = specialtyCeduleNumberParameter;
	}

	public final String getExpeditionDateSpecialtyLicense() {
		return this.expeditionDateSpecialtyLicense;
	}

	public final void setExpeditionDateSpecialtyLicense(final String expeditionDateSpecialtyLicenseParameter) {
		this.expeditionDateSpecialtyLicense = expeditionDateSpecialtyLicenseParameter;
	}

	public final String getSanitaryLicense() {
		return this.sanitaryLicense;
	}

	public final void setSanitaryLicense(final String sanitaryLicenseParameter) {
		this.sanitaryLicense = sanitaryLicenseParameter;
	}

	public final String getSocialReasonOfTheContract() {
		return this.socialReasonOfTheContract;
	}

	public final void setSocialReasonOfTheContract(final String socialReasonOfTheContractParameter) {
		this.socialReasonOfTheContract = socialReasonOfTheContractParameter;
	}

	public final List<Obligation> getObligationsList() {
		return this.obligationsList;
	}

	public final void setObligationsList(final List<Obligation> obligationsListParameter) {
		this.obligationsList = obligationsListParameter;
	}

	public final SemaphoreEnum getSemaphore() {
		return this.semaphore;
	}

	public final void setSemaphore(final SemaphoreEnum semaphoreParameter) {
		this.semaphore = semaphoreParameter;
	}

	public final String getRfc() {
		return this.rfc;
	}

	public final void setRfc(final String rfcParameter) {
		this.rfc = rfcParameter;
	}

	public final String getFlowStatus() {
		return this.flowStatus;
	}

	public final void setFlowStatus(final String flowStatusParameter) {
		this.flowStatus = flowStatusParameter;
	}

	public final String getStarDate() {
		return this.starDate;
	}

	public final void setStarDate(final String starDateParameter) {
		this.starDate = starDateParameter;
	}

	public final String getEndDate() {
		return this.endDate;
	}

	public final void setEndDate(final String endDateParameter) {
		this.endDate = endDateParameter;
	}

	public final Boolean getRetry() {
		return this.retry;
	}

	public final void setRetry(final Boolean retryParameter) {
		this.retry = retryParameter;
	}

	public final String getEmailApplicant() {
		return this.emailApplicant;
	}

	public final void setEmailApplicant(final String emailApplicantParameter) {
		this.emailApplicant = emailApplicantParameter;
	}

	public final String getEmailLawyer() {
		return this.emailLawyer;
	}

	public final void setEmailLawyer(final String emailLawyerParameter) {
		this.emailLawyer = emailLawyerParameter;
	}

	public final String getFullNameApplicant() {
		return this.fullNameApplicant;
	}

	public final void setFullNameApplicant(final String fullNameApplicantParameter) {
		this.fullNameApplicant = fullNameApplicantParameter;
	}

	public final String getEnumDocumentType() {
		return this.enumDocumentType;
	}

	public final void setEnumDocumentType(final String enumDocumentTypeParameter) {
		this.enumDocumentType = enumDocumentTypeParameter;
	}

	public final String getProemName() {
		return this.proemName;
	}

	public final void setProemName(final String proemNameParameter) {
		this.proemName = proemNameParameter;
	}

	public final String getContractObject() {
		return this.contractObject;
	}

	public final void setContractObject(final String contractObjectParameter) {
		this.contractObject = contractObjectParameter;
	}

	public final String getContractExtendClause() {
		return this.contractExtendClause;
	}

	public final void setContractExtendClause(final String contractExtendClauseParameter) {
		this.contractExtendClause = contractExtendClauseParameter;
	}

	public final String getContractEndClause() {
		return this.contractEndClause;
	}

	public final void setContractEndClause(final String contractEndClauseParameter) {
		this.contractEndClause = contractEndClauseParameter;
	}

	public final String getForcedYears() {
		return this.forcedYears;
	}

	public final void setForcedYears(final String forcedYearsParameter) {
		this.forcedYears = forcedYearsParameter;
	}

	public final String getVoluntaryYears() {
		return this.voluntaryYears;
	}

	public final void setVoluntaryYears(final String voluntaryYearsParameter) {
		this.voluntaryYears = voluntaryYearsParameter;
	}

	public final String getRentClause() {
		return this.rentClause;
	}

	public final void setRentClause(final String rentClauseParameter) {
		this.rentClause = rentClauseParameter;
	}

	public final String getSupplierCompanyPurpose() {
		return this.supplierCompanyPurpose;
	}

	public final void setSupplierCompanyPurpose(final String supplierCompanyPurposeParameter) {
		this.supplierCompanyPurpose = supplierCompanyPurposeParameter;
	}

	public final String getSupplierCompanyType() {
		return this.supplierCompanyType;
	}

	public final void setSupplierCompanyType(final String supplierCompanyTypeParameter) {
		this.supplierCompanyType = supplierCompanyTypeParameter;
	}

	public final String getSupplierAddress() {
		return this.supplierAddress;
	}

	public final void setSupplierAddress(final String supplierAddressParameter) {
		this.supplierAddress = supplierAddressParameter;
	}

	public final String getFiscalAddress() {
		return this.fiscalAddress;
	}

	public final void setFiscalAddress(final String supplierFiscalAddressParameter) {
		this.fiscalAddress = supplierFiscalAddressParameter;
	}

	public final Boolean getIsExpiredAttended() {
		return this.isExpiredAttended;
	}

	public final void setIsExpiredAttended(final Boolean isExpiredAttendedParameter) {
		this.isExpiredAttended = isExpiredAttendedParameter;
	}

	public final String getProperty() {
		return this.property;
	}

	public final String getPropertyUpper() {
		if (this.property != null)
			return this.property.toUpperCase();
		return this.property;
	}

	public final void setProperty(final String propertyParameter) {
		this.property = propertyParameter;
	}

	public final Double getContingencyBailAmount() {
		return this.contingencyBailAmount;
	}

	public final void setContingencyBailAmount(final Double contingencyBailAmountParameter) {
		this.contingencyBailAmount = contingencyBailAmountParameter;
	}

	public final String getProyect() {
		return this.proyect;
	}

	public final void setProyect(final String proyectParameter) {
		this.proyect = proyectParameter;
	}

	public final String getProyectAddress() {
		return this.proyectAddress;
	}

	public final void setProyectAddress(final String proyectAddressParameter) {
		this.proyectAddress = proyectAddressParameter;
	}

	public final String getDeveloper() {
		return this.developer;
	}

	public final void setDeveloper(final String developerParameter) {
		this.developer = developerParameter;
	}

	public final String getEndDateClause() {
		return this.endDateClause;
	}

	public final void setEndDateClause(final String endDateClauseParameter) {
		this.endDateClause = endDateClauseParameter;
	}

	public final Double getExtensionAmount() {
		return this.extensionAmount;
	}

	public final void setExtensionAmount(final Double extensionAmountParameter) {
		this.extensionAmount = extensionAmountParameter;
	}

	public final String getEndDateDeclaration() {
		return this.endDateDeclaration;
	}

	public final void setEndDateDeclaration(final String endDateDeclarationParameter) {
		this.endDateDeclaration = endDateDeclarationParameter;
	}

	public final String getOcupationDate() {
		return this.ocupationDate;
	}

	public final void setOcupationDate(final String ocupationDateParameter) {
		this.ocupationDate = ocupationDateParameter;
	}

	public final List<Scaling> getScalingListSupplier() {
		return this.scalingListSupplier;
	}

	public final void setScalingListSupplier(final List<Scaling> scalingListParameter) {
		this.scalingListSupplier = scalingListParameter;
	}

	public final String getAreaTender() {
		return this.areaTender;
	}

	public final void setAreaTender(final String areaTenderParameter) {
		this.areaTender = areaTenderParameter;
	}

	public final String getDateValue() {
		return this.dateValue;
	}

	public final void setDateValue(final String dateValueParameter) {
		this.dateValue = dateValueParameter;
	}

	public final String getSupplierDeedNumber() {
		return this.supplierDeedNumber;
	}

	public final void setSupplierDeedNumber(final String supplierDeedNumberParameter) {
		this.supplierDeedNumber = supplierDeedNumberParameter;
	}

	public final String getSupplierDeedConstitutionDate() {
		return this.supplierDeedConstitutionDate;
	}

	public final void setSupplierDeedConstitutionDate(final String supplierDeedConstitutionDateParameter) {
		this.supplierDeedConstitutionDate = supplierDeedConstitutionDateParameter;
	}

	public final String getSupplierDeedNotary() {
		return this.supplierDeedNotary;
	}

	public final void setSupplierDeedNotary(final String supplierDeedNotaryParameter) {
		this.supplierDeedNotary = supplierDeedNotaryParameter;
	}

	public final String getSupplierNotaryNumber() {
		return this.supplierNotaryNumber;
	}

	public final void setSupplierNotaryNumber(final String supplierNotaryNumberParameter) {
		this.supplierNotaryNumber = supplierNotaryNumberParameter;
	}

	public final String getSupplierNotaryState() {
		return this.supplierNotaryState;
	}

	public final void setSupplierNotaryState(final String supplierNotaryStateParameter) {
		this.supplierNotaryState = supplierNotaryStateParameter;
	}

	public final String getSupplierDeedComercialFolio() {
		return this.supplierDeedComercialFolio;
	}

	public final void setSupplierDeedComercialFolio(final String supplierDeedComercialFolioParameter) {
		this.supplierDeedComercialFolio = supplierDeedComercialFolioParameter;
	}

	public final String getSupplierDeedInscrptDateFolio() {
		return this.supplierDeedInscriptionDateFolio;
	}

	public final void setSupplierDeedInscrptDateFolio(final String supplierDeedInscriptionFolioParameter) {
		this.supplierDeedInscriptionDateFolio = supplierDeedInscriptionFolioParameter;
	}

	public final String getPublicNotaryDeed() {
		return this.publicNotaryDeed;
	}

	public final void setPublicNotaryDeed(final String publicNotaryDeedParameter) {
		this.publicNotaryDeed = publicNotaryDeedParameter;
	}

	public final String getActiveActor() {
		return this.activeActor;
	}

	public final void setActiveActor(final String activeActorParameter) {
		this.activeActor = activeActorParameter;
	}

	public final String getActiveActorUpper() {
		if (this.activeActor != null)
			return this.activeActor.toUpperCase();
		return this.activeActor;
	}

	public final String getPassiveActor() {
		return this.passiveActor;
	}

	public final String getPassiveActorUpper() {
		if (this.passiveActor != null)
			return this.passiveActor.toUpperCase();
		return this.passiveActor;
	}

	public final void setPassiveActor(final String passiveActorParameter) {
		this.passiveActor = passiveActorParameter;
	}

	public final String getDeedOrCommercialRegister() {
		return this.deedOrCommercialRegister;
	}

	public final void setDeedOrCommercialRegister(final String deedOrCommercialRegisterParameter) {
		this.deedOrCommercialRegister = deedOrCommercialRegisterParameter;
	}

	public final String getOfficialId() {
		return this.officialId;
	}

	public final void setOfficialId(final String officialIdParameter) {
		this.officialId = officialIdParameter;
	}

	public final String getOfficialIdNumber() {
		return this.officialIdNumber;
	}

	public final void setOfficialIdNumber(final String officialIdNumberParameter) {
		this.officialIdNumber = officialIdNumberParameter;
	}

	public final Integer getGracePeriodMonths() {
		return this.gracePeriodMonths;
	}

	public final void setGracePeriodMonths(final Integer gracePeriodMonthsParameter) {
		this.gracePeriodMonths = gracePeriodMonthsParameter;
	}

	public final String getContractualPenaltyMonths() {
		return this.contractualPenaltyMonths;
	}

	public final void setContractualPenaltyMonths(final String contractualPenaltyMonthsParameter) {
		this.contractualPenaltyMonths = contractualPenaltyMonthsParameter;
	}

	public final String getPropertyAddress() {
		return this.propertyAddress;
	}

	public final void setPropertyAddress(final String propertyAddressParameter) {
		this.propertyAddress = propertyAddressParameter;
	}

	public final String getPropertyDeliveryDate() {
		return this.propertyDeliveryDate;
	}

	public final void setPropertyDeliveryDate(final String propertyDeliveryDateParameter) {
		this.propertyDeliveryDate = propertyDeliveryDateParameter;
	}

	public final String getPropertyDeed() {
		return this.propertyDeed;
	}

	public final void setPropertyDeed(final String propertyDeedParameter) {
		this.propertyDeed = propertyDeedParameter;
	}

	public final String getDatePropertyDeed() {
		return this.datePropertyDeed;
	}

	public final void setDatePropertyDeed(final String datePropertyDeedParameter) {
		this.datePropertyDeed = datePropertyDeedParameter;
	}

	public final String getNotaryDeed() {
		return this.notaryDeed;
	}

	public final void setNotaryDeed(final String notaryDeedParameter) {
		this.notaryDeed = notaryDeedParameter;
	}

	public final String getNumberNotaryDeed() {
		return this.numberNotaryDeed;
	}

	public final void setNumberNotaryDeed(final String numberNotaryDeedParameter) {
		this.numberNotaryDeed = numberNotaryDeedParameter;
	}

	public final String getStateNotaryDeed() {
		return this.stateNotaryDeed;
	}

	public final void setStateNotaryDeed(final String stateNotaryDeedParameter) {
		this.stateNotaryDeed = stateNotaryDeedParameter;
	}

	public final String getFolioDeedOrCommercialRegister() {
		return this.folioDeedOrCommercialRegister;
	}

	public final void setFolioDeedOrCommercialRegister(final String folioDeedOrCommercialRegisterParameter) {
		this.folioDeedOrCommercialRegister = folioDeedOrCommercialRegisterParameter;
	}

	public final String getRegistrationDateFolio() {
		return this.registrationDateFolio;
	}

	public final void setRegistrationDateFolio(final String registrationDateFolioParameter) {
		this.registrationDateFolio = registrationDateFolioParameter;
	}

	public final String getConventionalPenaltyAmount() {
		return this.conventionalPenaltyAmount;
	}

	public final void setConventionalPenaltyAmount(final String conventionalPenaltyAmountParameter) {
		this.conventionalPenaltyAmount = conventionalPenaltyAmountParameter;
	}

	public final String getMonthlyRentAmount() {
		return this.monthlyRentAmount;
	}

	public final void setMonthlyRentAmount(final String monthlyRentAmountParameter) {
		this.monthlyRentAmount = monthlyRentAmountParameter;
	}

	public final List<Scaling> getScalingListFinancialEntity() {
		return this.scalingListFinancialEntity;
	}

	public final void setScalingListFinancialEntity(final List<Scaling> scalingListFinancialEntityParameter) {
		this.scalingListFinancialEntity = scalingListFinancialEntityParameter;
	}

	public final String getExtensionNumber() {
		return this.extensionNumber;
	}

	public final void setExtensionNumber(final String extensionNumberParameter) {
		this.extensionNumber = extensionNumberParameter;
	}

	public final String getPublicDeedNumberCopy() {
		return this.publicDeedNumberCopy;
	}

	public final void setPublicDeedNumberCopy(final String publicDeedNumberCopyParameter) {
		this.publicDeedNumberCopy = publicDeedNumberCopyParameter;
	}

	public final String getMonthlyMaintenanceAmount() {
		return this.monthlyMaintenanceAmount;
	}

	public final void setMonthlyMaintenanceAmount(final String monthlyMaintenanceAmountParameter) {
		this.monthlyMaintenanceAmount = monthlyMaintenanceAmountParameter;
	}

	public final String getPaExtensionForcedYears() {
		return this.paExtensionForcedYears;
	}

	public final void setPaExtensionForcedYears(final String paExtensionForcedYearsParameter) {
		this.paExtensionForcedYears = paExtensionForcedYearsParameter;
	}

	public final String getPaExtensionVoluntaryYears() {
		return this.paExtensionVoluntaryYears;
	}

	public final void setPaExtensionVoluntaryYears(final String pAExtensionVoluntaryYearsParameter) {
		this.paExtensionVoluntaryYears = pAExtensionVoluntaryYearsParameter;
	}

	public final String getAaExtensionForcedYears() {
		return this.aaExtensionForcedYears;
	}

	public final void setAaExtensionForcedYears(final String aaExtensionForcedYearsParameter) {
		this.aaExtensionForcedYears = aaExtensionForcedYearsParameter;
	}

	public final String getAaExtensionVoluntaryYears() {
		return this.aaExtensionVoluntaryYears;
	}

	public final void setAaExtensionVoluntaryYears(final String aaExtensionVoluntaryYearsParameter) {
		this.aaExtensionVoluntaryYears = aaExtensionVoluntaryYearsParameter;
	}

	public final String getServiceStartDate() {
		return this.serviceStartDate;
	}

	public final void setServiceStartDate(final String serviceStartDateParameter) {
		this.serviceStartDate = serviceStartDateParameter;
	}

	public final String getContractToModify() {
		return this.contractToModify;
	}

	public final void setContractToModify(final String contractToModifyParameter) {
		this.contractToModify = contractToModifyParameter;
	}

	public final String getDateContractToModify() {
		return this.dateContractToModify;
	}

	public final void setDateContractToModify(final String dateContractToModifyParameter) {
		this.dateContractToModify = dateContractToModifyParameter;
	}

	public final String getContractObjetToModify() {
		return this.contractObjetToModify;
	}

	public final void setContractObjetToModify(final String contractObjetToModifyParameter) {
		this.contractObjetToModify = contractObjetToModifyParameter;
	}

	public final String getRetroactiveDate() {
		return this.retroactiveDate;
	}

	public final void setRetroactiveDate(final String retroactiveDateParameter) {
		this.retroactiveDate = retroactiveDateParameter;
	}

	public final String getClauseToModifyContent() {
		return this.clauseToModifyContent;
	}

	public final void setClauseToModifyContent(final String clauseToModifyContentParameter) {
		this.clauseToModifyContent = clauseToModifyContentParameter;
	}

	public final String getFidelityBailAmount() {
		return this.fidelityBailAmount;
	}

	public final void setFidelityBailAmount(final String fidelityBailAmountParameter) {
		this.fidelityBailAmount = fidelityBailAmountParameter;
	}

	public final String getCivilResponsabilityBailAmount() {
		return this.civilResponsabilityBailAmount;
	}

	public final void setCivilResponsabilityBailAmount(final String civilResponsabilityBailAmountParameter) {
		this.civilResponsabilityBailAmount = civilResponsabilityBailAmountParameter;
	}

	public final Boolean getIsHiddenVices() {
		return this.isHiddenVices;
	}

	public final void setIsHiddenVices(final Boolean isHiddenVicesParameter) {
		this.isHiddenVices = isHiddenVicesParameter;
	}

	public final String getHiddenVicesAmount() {
		return this.hiddenVicesAmount;
	}

	public final void setHiddenVicesAmount(final String hiddenVicesAmountParameter) {
		this.hiddenVicesAmount = hiddenVicesAmountParameter;
	}

	public final FileUploadInfo getHiddenVicesBailDocument() {
		return this.hiddenVicesBailDocument;
	}

	public final void setHiddenVicesBailDocument(final FileUploadInfo hiddenVicesBailDocumentParameter) {
		this.hiddenVicesBailDocument = hiddenVicesBailDocumentParameter;
	}

	public final Integer getHiddenVicesBailIdDoc() {
		return this.hiddenVicesBailIdDoc;
	}

	public final void setHiddenVicesBailIdDoc(final Integer hiddenVicesBailIdDocParameter) {
		this.hiddenVicesBailIdDoc = hiddenVicesBailIdDocParameter;
	}

	public final FinancialEntity getFinancialEntity() {
		return this.financialEntity;
	}

	public final void setFinancialEntity(final FinancialEntity financialEntityParameter) {
		this.financialEntity = financialEntityParameter;
	}

	public final String getEndDateContractToEnd() {
		return this.endDateContractToEnd;
	}

	public final void setEndDateContractToEnd(final String endDateContractToEndParameter) {
		this.endDateContractToEnd = endDateContractToEndParameter;
	}

	public final String getOutsourcedMail() {
		return this.outsourcedMail;
	}

	public final void setOutsourcedMail(final String outsourcedMailParameter) {
		this.outsourcedMail = outsourcedMailParameter;
	}

	public final String getOutsourcedPhoneNumber() {
		return this.outsourcedPhoneNumber;
	}

	public final void setOutsourcedPhoneNumber(final String outsourcedPhoneNumberParameter) {
		this.outsourcedPhoneNumber = outsourcedPhoneNumberParameter;
	}

	public final String getOutsourcedAtention() {
		return this.outsourcedAtention;
	}

	public final void setOutsourcedAtention(final String outsourcedAtentionParameter) {
		this.outsourcedAtention = outsourcedAtentionParameter;
	}

	public final String getAnticipatedEndDate() {
		return this.anticipatedEndDate;
	}

	public final void setAnticipatedEndDate(final String anticipatedEndDateParameter) {
		this.anticipatedEndDate = anticipatedEndDateParameter;
	}

	public final String getJurisdictionState() {
		return this.jurisdictionState;
	}

	public final void setJurisdictionState(final String jurisdictionStateParameter) {
		this.jurisdictionState = jurisdictionStateParameter;
	}

	public final Boolean getIsOrigSignCntrDelvrdLegal() {
		return this.isOrigSignCntrDelvrdLegal;
	}

	public final void setIsOrigSignCntrDelvrdLegal(final Boolean isOrigSignCntrDelvrdLegalParameter) {
		this.isOrigSignCntrDelvrdLegal = isOrigSignCntrDelvrdLegalParameter;
	}

	public final Boolean getIsCpySignCntrDelvrdLegal() {
		return this.isCpySignCntrDelvrdLegal;
	}

	public final void setIsCpySignCntrDelvrdLegal(final Boolean isCpySignCntrDelvrdLegalParameter) {
		this.isCpySignCntrDelvrdLegal = isCpySignCntrDelvrdLegalParameter;
	}

	public final String getSignedContractSendDateLegal() {
		return this.signedContractSendDateLegal;
	}

	public final void setSignedContractSendDateLegal(final String signedContractSendDateLegalParameter) {
		this.signedContractSendDateLegal = signedContractSendDateLegalParameter;
	}

	public final String getOutsourcedAddress() {
		return this.outsourcedAddress;
	}

	public final void setOutsourcedAddress(final String outsourcedAddressParameter) {
		this.outsourcedAddress = outsourcedAddressParameter;
	}

	public final Integer getFinancialEntitiesSelectionLimit() {
		return this.financialEntitiesSelectionLimit;
	}

	public final void setFinancialEntitiesSelectionLimit(final Integer financialEntitiesSelectionLimitParameter) {
		this.financialEntitiesSelectionLimit = financialEntitiesSelectionLimitParameter;
	}

	public final String getHealthLicenseGrantsAuthority() {
		return this.healthLicenseGrantsAuthority;
	}

	public final void setHealthLicenseGrantsAuthority(final String healthLicenseGrantsAuthorityParameter) {
		this.healthLicenseGrantsAuthority = healthLicenseGrantsAuthorityParameter;
	}

	public final Integer getDiscountAgreedService() {
		return this.discountAgreedService;
	}

	public final void setDiscountAgreedService(final Integer discountSgreedServiceParameter) {
		this.discountAgreedService = discountSgreedServiceParameter;
	}

	public final String getAppraisersPfName() {
		return this.appraisersPfName;
	}

	public final void setAppraisersPfName(final String appraisersPfNameParameter) {
		this.appraisersPfName = appraisersPfNameParameter;
	}

	public final String getSupplierCompanyName() {
		return this.supplierCompanyName;
	}

	public final void setSupplierCompanyName(final String supplierCompanyNameParameter) {
		this.supplierCompanyName = supplierCompanyNameParameter;
	}

	public final String getCancellationDate() {
		return this.cancellationDate;
	}

	public final void setCancellationDate(final String cancellationDateParameter) {
		this.cancellationDate = cancellationDateParameter;
	}

	public final List<FinancialEntity> getDataFinancialEntityList() {
		return this.dataFinancialEntityList;
	}

	public final void setDataFinancialEntityList(final List<FinancialEntity> dataFinancialEntityListParameter) {
		this.dataFinancialEntityList = dataFinancialEntityListParameter;
	}

	public final String getSettlementObligations() {
		return this.settlementObligations;
	}

	public final void setSettlementObligations(final String settlementObligationsParameter) {
		this.settlementObligations = settlementObligationsParameter;
	}

	public final String getPropertyDateVacated() {
		return this.propertyDateVacated;
	}

	public final void setPropertyDateVacated(final String propertyDateVacatedParameter) {
		this.propertyDateVacated = propertyDateVacatedParameter;
	}

	public final String getStartExtensionDate() {
		return this.startExtensionDate;
	}

	public final void setStartExtensionDate(final String startExtensionDateParameter) {
		this.startExtensionDate = startExtensionDateParameter;
	}

	public final String getEndExtensionDate() {
		return this.endExtensionDate;
	}

	public final void setEndExtensionDate(final String endExtensionDateParameter) {
		this.endExtensionDate = endExtensionDateParameter;
	}

	public final String getStartFirstExtensionDate() {
		return this.startFirstExtensionDate;
	}

	public final void setStartFirstExtensionDate(final String startFirstExtensionDateParameter) {
		this.startFirstExtensionDate = startFirstExtensionDateParameter;
	}

	public final String getEndFirstExtensionDate() {
		return this.endFirstExtensionDate;
	}

	public final void setEndFirstExtensionDate(final String endFirstExtensionDateParameter) {
		this.endFirstExtensionDate = endFirstExtensionDateParameter;
	}

	public final CurrencyCodeEnum getMonthlyRentCurrency() {
		return this.monthlyRentCurrency;
	}

	public final void setMonthlyRentCurrency(final CurrencyCodeEnum monthlyRentCurrencyParameter) {
		this.monthlyRentCurrency = monthlyRentCurrencyParameter;
	}

	public final String getVolume() {
		return this.volume;
	}

	public final void setVolume(final String volumeParameter) {
		this.volume = volumeParameter;
	}

	public final String getBookNumber() {
		return bookNumber;
	}

	public final void setBookNumber(String bookNumberParameter) {
		this.bookNumber = bookNumberParameter;
	}

	public final String getTowerNameProperty() {
		return this.towerNameProperty;
	}

	public void setTowerNameProperty(String towerNamePropertyParameter) {
		this.towerNameProperty = towerNamePropertyParameter;
	}

	public final String getCustomsAgentPatentNumber() {
		return this.customsAgentPatentNumber;
	}

	public final void setCustomsAgentPatentNumber(final String customsAgentPatentNumberParameter) {
		this.customsAgentPatentNumber = customsAgentPatentNumberParameter;
	}

	public final String getCustomsState() {
		return this.customsState;
	}

	public final void setCustomsState(final String customsStateParameter) {
		this.customsState = customsStateParameter;
	}

	public final String getFinancialEntityBranchAddress() {
		return this.financialEntityBranchAddress;
	}

	public final void setFinancialEntityBranchAddress(final String financialEntityBranchAddressParameter) {
		this.financialEntityBranchAddress = financialEntityBranchAddressParameter;
	}

	public final Integer getContractValidityMonths() {
		return this.contractValidityMonths;
	}

	public final void setContractValidityMonths(final Integer contractValidityMonthsParameter) {
		this.contractValidityMonths = contractValidityMonthsParameter;
	}

	public final Double getConventionalPenaltyPercentage() {
		return this.conventionalPenaltyPercentage;
	}

	public final void setConventionalPenaltyPercentage(final Double conventionalPenaltyPercentageParameter) {
		this.conventionalPenaltyPercentage = conventionalPenaltyPercentageParameter;
	}

	public final Integer getDepositsRealizedMonthsNumber() {
		return this.depositsRealizedMonthsNumber;
	}

	public final void setDepositsRealizedMonthsNumber(final Integer depositsRealizedMonthsNumberParameter) {
		this.depositsRealizedMonthsNumber = depositsRealizedMonthsNumberParameter;
	}

	public final String getSignState() {
		return this.signState;
	}

	public final void setSignState(final String signStateParameter) {
		this.signState = signStateParameter;
	}

	public final String getCondosNumber() {
		return this.condosNumber;
	}

	public final void setCondosNumber(final String condosNumberParameter) {
		this.condosNumber = condosNumberParameter;
	}

	public final String getInitialAdvanceAmount() {
		return this.initialAdvanceAmount;
	}

	public final void setInitialAdvanceAmount(final String initialAdvanceAmountParameter) {
		this.initialAdvanceAmount = initialAdvanceAmountParameter;
	}

	public final String getTotalAmountForServicesRendered() {
		return this.totalAmountForServicesRendered;
	}

	public final void setTotalAmountForServicesRendered(final String totalAmountForServicesRenderedParameter) {
		this.totalAmountForServicesRendered = totalAmountForServicesRenderedParameter;
	}

	public final String getFinancialEntityLegalRepresentativePosition() {
		return this.financialEntityLegalRepresentativePosition;
	}

	public final void setFinancialEntityLegalRepresentativePosition(
			final String financialEntityLegalRepresentativePositionParameter) {
		this.financialEntityLegalRepresentativePosition = financialEntityLegalRepresentativePositionParameter;
	}

	public final String getSupplierLegalRepresentativePosition() {
		return this.supplierLegalRepresentativePosition;
	}

	public final void setSupplierLegalRepresentativePosition(
			final String supplierLegalRepresentativePositionParameter) {
		this.supplierLegalRepresentativePosition = supplierLegalRepresentativePositionParameter;
	}

	public final String getBillingCycle() {
		return this.billingCycle;
	}

	public final void setBillingCycle(final String billingCycleParameter) {
		this.billingCycle = billingCycleParameter;
	}

	public final String getPaymentCycle() {
		return this.paymentCycle;
	}

	public final void setPaymentCycle(final String paymentCycleParameter) {
		this.paymentCycle = paymentCycleParameter;
	}

	public final String getStandardizedKeyBankingFinancialEntity() {
		return this.standardizedKeyBankingFinancialEntity;
	}

	public final void setStandardizedKeyBankingFinancialEntity(
			final String standardizedKeyBankingFinancialEntityParameter) {
		this.standardizedKeyBankingFinancialEntity = standardizedKeyBankingFinancialEntityParameter;
	}

	public final String getTransferDay() {
		return this.transferDay;
	}

	public final void setTransferDay(final String transferDayParameter) {
		this.transferDay = transferDayParameter;
	}

	public final String getDaysNoticeForEarlyTermination() {
		return this.daysNoticeForEarlyTermination;
	}

	public final void setDaysNoticeForEarlyTermination(final String daysNoticeForEarlyTerminationParameter) {
		this.daysNoticeForEarlyTermination = daysNoticeForEarlyTerminationParameter;
	}

	public final String getNominativeCheckDeliveryDay() {
		return this.nominativeCheckDeliveryDay;
	}

	public final void setNominativeCheckDeliveryDay(final String nominativeCheckDeliveryDayParameter) {
		this.nominativeCheckDeliveryDay = nominativeCheckDeliveryDayParameter;
	}

	public final String getBusinessDaysAcceptRejectPurchaseOrder() {
		return this.businessDaysAcceptRejectPurchaseOrder;
	}

	public final void setBusinessDaysAcceptRejectPurchaseOrder(
			final String businessDaysAcceptRejectPurchaseOrderParameter) {
		this.businessDaysAcceptRejectPurchaseOrder = businessDaysAcceptRejectPurchaseOrderParameter;
	}

	public final String getCalendarDayOfDeliveryDate() {
		return this.calendarDayOfDeliveryDate;
	}

	public final void setCalendarDayOfDeliveryDate(final String calendarDayOfDeliveryDateParameter) {
		this.calendarDayOfDeliveryDate = calendarDayOfDeliveryDateParameter;
	}

	public final String getBusinessDaysToModifyCancelOrders() {
		return this.businessDaysToModifyCancelOrders;
	}

	public final void setBusinessDaysToModifyCancelOrders(final String businessDaysToModifyCancelOrdersParameter) {
		this.businessDaysToModifyCancelOrders = businessDaysToModifyCancelOrdersParameter;
	}

	public final String getCalendarDaysToWithdrawContract() {
		return this.calendarDaysToWithdrawContract;
	}

	public final void setCalendarDaysToWithdrawContract(final String calendarDaysToWithdrawContractParameter) {
		this.calendarDaysToWithdrawContract = calendarDaysToWithdrawContractParameter;
	}

	public final String getDaysForAnomalyDeliveryReport() {
		return this.daysForAnomalyDeliveryReport;
	}

	public final void setDaysForAnomalyDeliveryReport(final String daysForAnomalyDeliveryReportParameter) {
		this.daysForAnomalyDeliveryReport = daysForAnomalyDeliveryReportParameter;
	}

	public final String getDaysDeadlineForPayment() {
		return this.daysDeadlineForPayment;
	}

	public final void setDaysDeadlineForPayment(final String daysDeadlineForPaymentParameter) {
		this.daysDeadlineForPayment = daysDeadlineForPaymentParameter;
	}

	public final String getFinancialEntityDirection() {
		return this.financialEntityDirection;
	}

	public final void setFinancialEntityDirection(final String financialEntityDirectionParameter) {
		this.financialEntityDirection = financialEntityDirectionParameter;
	}

	public final String getFinancialEntityAddress() {
		return this.financialEntityAddress;
	}

	public final void setFinancialEntityAddress(final String financialEntityAddressParameter) {
		this.financialEntityAddress = financialEntityAddressParameter;
	}

	public final String getSpaceServiceGranted() {
		return this.spaceServiceGranted;
	}

	public final void setSpaceServiceGranted(final String spaceServiceGrantedParameter) {
		this.spaceServiceGranted = spaceServiceGrantedParameter;
	}

	public final String getInscriptionCommercialFolioState() {
		return this.inscriptionCommercialFolioState;
	}

	public final void setInscriptionCommercialFolioState(final String inscriptionCommercialFolioStateParameter) {
		this.inscriptionCommercialFolioState = inscriptionCommercialFolioStateParameter;
	}

	public final String getCivilLawState() {
		return this.civilLawState;
	}

	public final void setCivilLawState(final String civilLawStateParameter) {
		this.civilLawState = civilLawStateParameter;
	}

	public final String getServiceLocationState() {
		return this.serviceLocationState;
	}

	public final void setServiceLocationState(final String serviceLocationStateParameter) {
		this.serviceLocationState = serviceLocationStateParameter;
	}

	public final String getConstructionStagesEndDate() {
		return this.constructionStagesEndDate;
	}

	public final void setConstructionStagesEndDate(final String constructionStagesEndDateParameter) {
		this.constructionStagesEndDate = constructionStagesEndDateParameter;
	}

	public final String getWorksEndDate() {
		return this.worksEndDate;
	}

	public final void setWorksEndDate(final String worksEndDateParameter) {
		this.worksEndDate = worksEndDateParameter;
	}

	public final String getConstructionStagesStartDate() {
		return this.constructionStagesStartDate;
	}

	public final void setConstructionStagesStartDate(final String constructionStagesStartDateParameter) {
		this.constructionStagesStartDate = constructionStagesStartDateParameter;
	}

	public final String getWorksStartDate() {
		return this.worksStartDate;
	}

	public final void setWorksStartDate(final String worksStartDateParameter) {
		this.worksStartDate = worksStartDateParameter;
	}

	public final String getSubsidiaries() {
		return this.subsidiaries;
	}

	public final void setSubsidiaries(final String subsidiariesParameter) {
		this.subsidiaries = subsidiariesParameter;
	}

	public final String getPaymentMethodSubscribers() {
		return this.paymentMethodSubscribers;
	}

	public final void setPaymentMethodSubscribers(final String paymentMethodSubscribersParameter) {
		this.paymentMethodSubscribers = paymentMethodSubscribersParameter;
	}

	public final String getDeliveryCost() {
		return this.deliveryCost;
	}

	public final void setDeliveryCost(final String deliveryCostParameter) {
		this.deliveryCost = deliveryCostParameter;
	}

	public final String getAmountOfInsuranceForDamageOrLoss() {
		return this.amountOfInsuranceForDamageOrLoss;
	}

	public final void setAmountOfInsuranceForDamageOrLoss(final String amountOfInsuranceForDamageOrLossParameter) {
		this.amountOfInsuranceForDamageOrLoss = amountOfInsuranceForDamageOrLossParameter;
	}

	public final String getDepositAmount() {
		return this.depositAmount;
	}

	public final void setDepositAmount(final String depositAmountParameter) {
		this.depositAmount = depositAmountParameter;
	}

	public final String getTotalNetAmountOfWorkDone() {
		return this.totalNetAmountOfWorkDone;
	}

	public final void setTotalNetAmountOfWorkDone(final String totalNetAmountOfWorkDoneParameter) {
		this.totalNetAmountOfWorkDone = totalNetAmountOfWorkDoneParameter;
	}

	public final String getSupplierIMMS() {
		return this.supplierIMMS;
	}

	public final void setSupplierIMMS(final String supplierIMMSParameter) {
		this.supplierIMMS = supplierIMMSParameter;
	}

	public final String getCompensationMonthsRent() {
		return this.compensationMonthsRent;
	}

	public final void setCompensationMonthsRent(final String compensationMonthsRentParameter) {
		this.compensationMonthsRent = compensationMonthsRentParameter;
	}

	public final String getBankingInstitution() {
		return this.bankingInstitution;
	}

	public final void setBankingInstitution(final String bankingInstitutionParameter) {
		this.bankingInstitution = bankingInstitutionParameter;
	}

	public final String getDeliveryMonthNominativeCheck() {
		return this.deliveryMonthNominativeCheck;
	}

	public final void setDeliveryMonthNominativeCheck(final String deliveryMonthNominativeCheckParameter) {
		this.deliveryMonthNominativeCheck = deliveryMonthNominativeCheckParameter;
	}

	public final String getTransferMonth() {
		return this.transferMonth;
	}

	public final void setTransferMonth(final String transferMonthParameter) {
		this.transferMonth = transferMonthParameter;
	}

	public final String getSupplierNationality() {
		return this.supplierNationality;
	}

	public final void setSupplierNationality(final String supplierNationalityParameter) {
		this.supplierNationality = supplierNationalityParameter;
	}

	public final String getSquareName() {
		return this.squareName;
	}

	public final void setSquareName(final String squareNameParameter) {
		this.squareName = squareNameParameter;
	}

	public final String getRepresentativeSocietyName() {
		return this.representativeSocietyName;
	}

	public final void setRepresentativeSocietyName(final String representativeSocietyNameParameter) {
		this.representativeSocietyName = representativeSocietyNameParameter;
	}

	public final String getPersonNameSendDailySalesReports() {
		return this.personNameSendDailySalesReports;
	}

	public final void setPersonNameSendDailySalesReports(final String personNameSendDailySalesReportsParameter) {
		this.personNameSendDailySalesReports = personNameSendDailySalesReportsParameter;
	}

	public final String getPersonMailSendDailySalesReports() {
		return this.personMailSendDailySalesReports;
	}

	public final void setPersonMailSendDailySalesReports(final String personMailSendDailySalesReportsParameter) {
		this.personMailSendDailySalesReports = personMailSendDailySalesReportsParameter;
	}

	public final String getFractionationName() {
		return this.fractionationName;
	}

	public final void setFractionationName(final String fractionationNameParameter) {
		this.fractionationName = fractionationNameParameter;
	}

	public final Integer getEmployeesNumber() {
		return this.employeesNumber;
	}

	public final void setEmployeesNumber(final Integer employeesNumberParameter) {
		this.employeesNumber = employeesNumberParameter;
	}

	public final Integer getFrameContractNumberTelecomServices() {
		return this.frameContractNumberTelecomServices;
	}

	public final void setFrameContractNumberTelecomServices(final Integer frameContractNumberTelecomServicesParameter) {
		this.frameContractNumberTelecomServices = frameContractNumberTelecomServicesParameter;
	}

	public final String getAccountNumberFinancial() {
		return this.accountNumberFinancial;
	}

	public final void setAccountNumberFinancial(final String accountNumberFinancialParameter) {
		this.accountNumberFinancial = accountNumberFinancialParameter;
	}

	public final String getStepsBuildNumber() {
		return this.stepsBuildNumber;
	}

	public final void setStepsBuildNumber(final String stepsBuildNumberParameter) {
		this.stepsBuildNumber = stepsBuildNumberParameter;
	}

	public final Integer getPremisesInTheSquareNumber() {
		return this.premisesInTheSquareNumber;
	}

	public final void setPremisesInTheSquareNumber(final Integer premisesInTheSquareNumberParameter) {
		this.premisesInTheSquareNumber = premisesInTheSquareNumberParameter;
	}

	public final String getEquivalentDepositsMonthsNumber() {
		return this.equivalentDepositsMonthsNumber;
	}

	public final void setEquivalentDepositsMonthsNumber(final String equivalentDepositsMonthsNumberParameter) {
		this.equivalentDepositsMonthsNumber = equivalentDepositsMonthsNumberParameter;
	}

	public final String getSupplierObligations() {
		return this.supplierObligations;
	}

	public final void setSupplierObligations(final String supplierObligationsParameter) {
		this.supplierObligations = supplierObligationsParameter;
	}

	public final String getCurrencyCountry() {
		return this.currencyCountry;
	}

	public final void setCurrencyCountry(final String currencyCountryParameter) {
		this.currencyCountry = currencyCountryParameter;
	}

	public final String getPropertyDeliveryPeriod() {
		return this.propertyDeliveryPeriod;
	}

	public final void setPropertyDeliveryPeriod(final String propertyDeliveryPeriodParameter) {
		this.propertyDeliveryPeriod = propertyDeliveryPeriodParameter;
	}

	public final String getTotalPaymentPercentajeAmountTotal() {
		return this.totalPaymentPercentajeAmountTotal;
	}

	public final void setTotalPaymentPercentajeAmountTotal(final String totalPaymentPercentajeAmountTotalParameter) {
		this.totalPaymentPercentajeAmountTotal = totalPaymentPercentajeAmountTotalParameter;
	}

	public final String getRentEquivalent() {
		return this.rentEquivalent;
	}

	public final void setRentEquivalent(final String rentEquivalentParameter) {
		this.rentEquivalent = rentEquivalentParameter;
	}

	public final String getCurrencyType() {
		return this.currencyType;
	}

	public final void setCurrencyType(final String currencyTypeParameter) {
		this.currencyType = currencyTypeParameter;
	}

	public final String getMegacableServiceSupplierProvided() {
		return this.megacableServiceSupplierProvided;
	}

	public final void setMegacableServiceSupplierProvided(final String megacableServiceSupplierProvidedParameter) {
		this.megacableServiceSupplierProvided = megacableServiceSupplierProvidedParameter;
	}

	public final String getProviderServiceMegacableProvided() {
		return this.providerServiceMegacableProvided;
	}

	public final void setProviderServiceMegacableProvided(final String providerServiceMegacableProvidedParameter) {
		this.providerServiceMegacableProvided = providerServiceMegacableProvidedParameter;
	}

	public final String getMegacableObligationsPaymentPercentageExchange() {
		return this.megacableObligationsPaymentPercentageExchange;
	}

	public final void setMegacableObligationsPaymentPercentageExchange(
			final String megacableObligationsPaymentPercentageExchangeParameter) {
		this.megacableObligationsPaymentPercentageExchange = megacableObligationsPaymentPercentageExchangeParameter;
	}

	public final String getSellerObligationsPaymentPercentageExchange() {
		return this.sellerObligationsPaymentPercentageExchange;
	}

	public final void setSellerObligationsPaymentPercentageExchange(
			final String sellerObligationsPaymentPercentageExchangeParameter) {
		this.sellerObligationsPaymentPercentageExchange = sellerObligationsPaymentPercentageExchangeParameter;
	}

	public final String getSurveying() {
		return this.surveying;
	}

	public final void setSurveying(final String surveyingParameter) {
		this.surveying = surveyingParameter;
	}

	public final String getStrokeStreet() {
		return this.strokeStreet;
	}

	public final void setStrokeStreet(final String strokeStreetParameter) {
		this.strokeStreet = strokeStreetParameter;
	}

	public final String getDigitalization() {
		return this.digitalization;
	}

	public final void setDigitalization(final String digitalizationParameter) {
		this.digitalization = digitalizationParameter;
	}

	public final String getNetworkCopy() {
		return this.networkCopy;
	}

	public final void setNetworkCopy(final String networkCopyParameter) {
		this.networkCopy = networkCopyParameter;
	}

	public final String getNetworkGPON() {
		return this.networkGPON;
	}

	public final void setNetworkGPON(final String networkGPONParameter) {
		this.networkGPON = networkGPONParameter;
	}

	public final String getFiberCopy() {
		return this.fiberCopy;
	}

	public final void setFiberCopy(final String fiberCopyParameter) {
		this.fiberCopy = fiberCopyParameter;
	}

	public final String getSurveyingCost() {
		return this.surveyingCost;
	}

	public final void setSurveyingCost(final String surveyingCostParameter) {
		this.surveyingCost = surveyingCostParameter;
	}

	public final String getStrokeStreetCost() {
		return this.strokeStreetCost;
	}

	public final void setStrokeStreetCost(final String strokeStreetCostParameter) {
		this.strokeStreetCost = strokeStreetCostParameter;
	}

	public final String getDigitalizationCost() {
		return this.digitalizationCost;
	}

	public final void setDigitalizationCost(final String digitalizationCostParameter) {
		this.digitalizationCost = digitalizationCostParameter;
	}

	public final String getNetworkCopyCost() {
		return this.networkCopyCost;
	}

	public final void setNetworkCopyCost(final String networkCopyCostParameter) {
		this.networkCopyCost = networkCopyCostParameter;
	}

	public final String getNetworkGPONCost() {
		return this.networkGPONCost;
	}

	public final void setNetworkGPONCost(final String networkGPONCostParameter) {
		this.networkGPONCost = networkGPONCostParameter;
	}

	public final String getFiberCopyCost() {
		return this.fiberCopyCost;
	}

	public final void setFiberCopyCost(final String fiberCopyCostParameter) {
		this.fiberCopyCost = fiberCopyCostParameter;
	}

	public final String getStateCivilLaw() {
		return this.stateCivilLaw;
	}

	public final void setStateCivilLaw(final String stateCivilLawParameter) {
		this.stateCivilLaw = stateCivilLawParameter;
	}

	public final String getConsiderationAmount() {
		return this.considerationAmount;
	}

	public final void setConsiderationAmount(final String considerationAmountParameter) {
		if(null == considerationAmountParameter) {
			this.considerationAmount = "";
		}else {
			this.considerationAmount = considerationAmountParameter;
		}
		
	}

	public final String getSurfaceStoreMerchandise() {
		return this.surfaceStoreMerchandise;
	}

	public final void setSurfaceStoreMerchandise(final String surfaceStoreMerchandiseParameter) {
		this.surfaceStoreMerchandise = surfaceStoreMerchandiseParameter;
	}

	public final String getPropertyBusinessLine() {
		return this.propertyBusinessLine;
	}

	public final void setPropertyBusinessLine(final String propertyBusinessLineParameter) {
		this.propertyBusinessLine = propertyBusinessLineParameter;
	}

	public final String getServiceSchedule() {
		return this.serviceSchedule;
	}

	public final void setServiceSchedule(final String serviceScheduleParameter) {
		this.serviceSchedule = serviceScheduleParameter;
	}

	public final String getDeliveryCalendarDays() {
		return this.deliveryCalendarDays;
	}

	public final void setDeliveryCalendarDays(final String deliveryCalendarDaysParameter) {
		this.deliveryCalendarDays = deliveryCalendarDaysParameter;
	}

	public final String getReimbursementTerminationCalendarDays() {
		return this.reimbursementTerminationCalendarDays;
	}

	public final void setReimbursementTerminationCalendarDays(
			final String reimbursementTerminationCalendarDaysParameter) {
		this.reimbursementTerminationCalendarDays = reimbursementTerminationCalendarDaysParameter;
	}

	public final String getScheduleService() {
		return this.scheduleService;
	}

	public final void setScheduleService(final String scheduleServiceParameter) {
		this.scheduleService = scheduleServiceParameter;
	}

	public final String getFinancialEntityAtention() {
		return this.financialEntityAtention;
	}

	public final void setFinancialEntityAtention(final String financialEntityAtentionParameter) {
		this.financialEntityAtention = financialEntityAtentionParameter;
	}

	public final String getCityJurisdiction() {
		return this.cityJurisdiction;
	}

	public final void setCityJurisdiction(final String cityJurisdictionParameter) {
		this.cityJurisdiction = cityJurisdictionParameter;
	}

	public final User getApplicant() {
		return this.applicant;
	}

	public final void setApplicant(final User applicantParameter) {
		this.applicant = applicantParameter;
	}

	public final String getPublicDeedDate() {
		return this.publicDeedDate;
	}

	public final void setPublicDeedDate(final String publicDeedDateParameter) {
		this.publicDeedDate = publicDeedDateParameter;
	}

	public final String getRegisterCommercialFolioDate() {
		return this.registerCommercialFolioDate;
	}

	public final void setRegisterCommercialFolioDate(final String registerCommercialFolioDateParameter) {
		this.registerCommercialFolioDate = registerCommercialFolioDateParameter;
	}

	public final Integer getUpdateRequisitionBy() {
		return this.updateRequisitionBy;
	}

	public final void setUpdateRequisitionBy(final Integer updateRequisitionByParameter) {
		this.updateRequisitionBy = updateRequisitionByParameter;
	}

	public final String getUpdateRequisitionDate() {
		return this.updateRequisitionDate;
	}

	public final void setUpdateRequisitionDate(final String updateRequisitionDateParameter) {
		this.updateRequisitionDate = updateRequisitionDateParameter;
	}

	public final User getUpdateByUser() {
		return this.updateByUser;
	}

	public final void setUpdateByUser(final User updateByUserParameter) {
		this.updateByUser = updateByUserParameter;
	}

	public final String getVersionNumber() {
		return this.versionNumber;
	}

	public final void setVersionNumber(final String versionNumberParameter) {
		this.versionNumber = versionNumberParameter;
	}

	public final List<Version> getAttachmentListDocument() {
		return this.attachmentListDocument;
	}

	public final void setAttachmentListDocument(final List<Version> attachmentListDocumentParameter) {
		this.attachmentListDocument = attachmentListDocumentParameter;
	}

	public final String getClosedDate() {
		return this.closedDate;
	}

	public final void setClosedDate(final String closedDateParameter) {
		this.closedDate = closedDateParameter;
	}

	public final String getSupplierAtention() {
		return supplierAtention;
	}

	public final void setSupplierAtention(final String supplierAtentionParameter) {
		this.supplierAtention = supplierAtentionParameter;
	}

	public final String getSupplierPhone() {
		return this.supplierPhone;
	}

	public final void setSupplierPhone(final String supplierPhoneParameter) {
		this.supplierPhone = supplierPhoneParameter;
	}

	public final String getSupplierAccountNumber() {
		return this.supplierAccountNumber;
	}

	public final void setSupplierAccountNumber(final String supplierAccountNumberParameter) {
		this.supplierAccountNumber = supplierAccountNumberParameter;
	}

	public User getLawyer() {
		return lawyer;
	}

	public void setLawyer(User lawyer) {
		this.lawyer = lawyer;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public final String getNegotiatorRepresentativeName() {
		return this.negotiatorRepresentativeName;
	}

	public final void setNegotiatorRepresentativeName(final String negotiatorRepresentativeNameParameter) {
		this.negotiatorRepresentativeName = negotiatorRepresentativeNameParameter;
	}

	public final String getMetrocarrierSquareAddress() {
		return this.metrocarrierSquareAddress;
	}

	public final void setMetrocarrierSquareAddress(final String metrocarrierSquareAddressParameter) {
		this.metrocarrierSquareAddress = metrocarrierSquareAddressParameter;
	}

	public final String getFrameworkContractSingDate() {
		return this.frameworkContractSingDate;
	}

	public final void setFrameworkContractSingDate(final String frameworkContractSingDateParameter) {
		this.frameworkContractSingDate = frameworkContractSingDateParameter;
	}

	public final String getBillingContactName() {
		return this.billingContactName;
	}

	public final void setBillingContactName(final String billingContactNameParameter) {
		this.billingContactName = billingContactNameParameter;
	}

	public final String getBillingPosition() {
		return this.billingPosition;
	}

	public final void setBillingPosition(final String billingPositionParameter) {
		this.billingPosition = billingPositionParameter;
	}

	public final String getBillingEmail() {
		return this.billingEmail;
	}

	public final void setBillingEmail(final String billingEmailParameter) {
		this.billingEmail = billingEmailParameter;
	}

	public final String getBillingPhone() {
		return this.billingPhone;
	}

	public final void setBillingPhone(final String billingPhoneParameter) {
		this.billingPhone = billingPhoneParameter;
	}

	public final String getBillingExtension() {
		return this.billingExtension;
	}

	public final void setBillingExtension(final String billingExtensionParameter) {
		this.billingExtension = billingExtensionParameter;
	}

	public final String getBillingFax() {
		return this.billingFax;
	}

	public final void setBillingFax(final String billingFaxParameter) {
		this.billingFax = billingFaxParameter;
	}

	public final String getTechnitianContactName() {
		return this.technitianContactName;
	}

	public final void setTechnitianContactName(final String technitianContactNameParameter) {
		this.technitianContactName = technitianContactNameParameter;
	}

	public final String getTechnitianPosition() {
		return this.technitianPosition;
	}

	public final void setTechnitianPosition(final String technitianPositionParameter) {
		this.technitianPosition = technitianPositionParameter;
	}

	public final String getTechnitianEmail() {
		return this.technitianEmail;
	}

	public final void setTechnitianEmail(final String technitianEmailParameter) {
		this.technitianEmail = technitianEmailParameter;
	}

	public final String getTechnitianPhone() {
		return this.technitianPhone;
	}

	public final void setTechnitianPhone(final String technitianPhoneParameter) {
		this.technitianPhone = technitianPhoneParameter;
	}

	public final String getTechnitianExtension() {
		return this.technitianExtension;
	}

	public final void setTechnitianExtension(final String technitianExtensionParameter) {
		this.technitianExtension = technitianExtensionParameter;
	}

	public final String getTechnitianFax() {
		return this.technitianFax;
	}

	public final void setTechnitianFax(final String technitianFaxParameter) {
		this.technitianFax = technitianFaxParameter;
	}

	public final String getFacturation() {
		return facturation;
	}

	public final void setFacturation(String facturation) {
		this.facturation = facturation;
	}

	public final String getActReceptionService() {
		return actReceptionService;
	}

	public final void setActReceptionService(String actReceptionService) {
		this.actReceptionService = actReceptionService;
	}

	public final String getEthernetPrivateLine() {
		return ethernetPrivateLine;
	}

	public final void setEthernetPrivateLine(String ethernetPrivateLine) {
		this.ethernetPrivateLine = ethernetPrivateLine;
	}

	public final String getVpn() {
		return vpn;
	}

	public final void setVpn(String vpn) {
		this.vpn = vpn;
	}

	public final String getDedicatedInternet() {
		return dedicatedInternet;
	}

	public final void setDedicatedInternet(String dedicatedInternet) {
		this.dedicatedInternet = dedicatedInternet;
	}

	public final String getDigitalPrivateLine() {
		return digitalPrivateLine;
	}

	public final void setDigitalPrivateLine(String digitalPrivateLine) {
		this.digitalPrivateLine = digitalPrivateLine;
	}

	public final String getInfrastructure() {
		return infrastructure;
	}

	public final void setInfrastructure(String infrastructure) {
		this.infrastructure = infrastructure;
	}

	public final String getVideoPrivateLine() {
		return videoPrivateLine;
	}

	public final void setVideoPrivateLine(String videoPrivateLine) {
		this.videoPrivateLine = videoPrivateLine;
	}

	public final String getTrunk() {
		return trunk;
	}

	public final void setTrunk(String trunk) {
		this.trunk = trunk;
	}

	public final String getOtherServices() {
		return otherServices;
	}

	public final void setOtherServices(String otherServices) {
		this.otherServices = otherServices;
	}

	public final String getCableTv() {
		return cableTv;
	}

	public final void setCableTv(String cableTv) {
		this.cableTv = cableTv;
	}

	public final String getBusinessInternet() {
		return businessInternet;
	}

	public final void setBusinessInternet(String businessInternet) {
		this.businessInternet = businessInternet;
	}

	public final String getBusinessTelephony() {
		return businessTelephony;
	}

	public final void setBusinessTelephony(String businessTelephony) {
		this.businessTelephony = businessTelephony;
	}

	public final String getIntranet() {
		return intranet;
	}

	public final void setIntranet(String intranet) {
		this.intranet = intranet;
	}

	public final String getTecnologySolutions() {
		return tecnologySolutions;
	}

	public final void setTecnologySolutions(String tecnologySolutions) {
		this.tecnologySolutions = tecnologySolutions;
	}

	public final String getGoogleServiceCloud() {
		return googleServiceCloud;
	}

	public final void setGoogleServiceCloud(String googleServiceCloud) {
		this.googleServiceCloud = googleServiceCloud;
	}

	public final String getServiceSellerName() {
		return this.serviceSellerName;
	}

	public final void setServiceSellerName(final String serviceSellerNameParameter) {
		this.serviceSellerName = serviceSellerNameParameter;
	}

	public final String getPromissoryNoteAmount() {
		return this.promissoryNoteAmount;
	}

	public final void setPromissoryNoteAmount(final String promissoryNoteAmountParameter) {
		this.promissoryNoteAmount = promissoryNoteAmountParameter;
	}

	public final String getTurnDate() {
		return this.turnDate;
	}

	public final void setTurnDate(final String turnDateParameter) {
		this.turnDate = turnDateParameter;
	}

	public final FileUploadInfo getTemplateUploadInfo() {
		return this.templateUploadInfo;
	}

	public final void setTemplateUploadInfo(final FileUploadInfo templateUploadInfoParameter) {
		this.templateUploadInfo = templateUploadInfoParameter;
	}

	/**
	 * @return the lawyerName
	 */
	public final String getLawyerName() {
		return lawyerName;
	}

	/**
	 * @param lawyerName the lawyerName to set
	 */
	public final void setLawyerName(String lawyerName) {
		this.lawyerName = lawyerName;
	}

	/**
	 * @return the contractApplicant
	 */
	public String getContractApplicant() {
		return contractApplicant;
	}

	/**
	 * @param contractApplicant the contractApplicant to set
	 */
	public void setContractApplicant(String contractApplicant) {
		this.contractApplicant = contractApplicant;
	}

	/**
	 * @return the contractor
	 */
	public String getContract() {
		return contract;
	}

	/**
	 * @param contractor the contractor to set
	 */
	public void setContract(String contract) {
		this.contract = contract;
	}

	/**
	 * @return the idArea
	 */
	public Integer getIdArea() {
		return idArea;
	}

	/**
	 * @param idArea the idArea to set
	 */
	public void setIdArea(Integer idArea) {
		this.idArea = idArea;
	}

	/**
	 * @return the idUnit
	 */
	public Integer getIdUnit() {
		return idUnit;
	}

	/**
	 * @param idUnit the idUnit to set
	 */
	public void setIdUnit(Integer idUnit) {
		this.idUnit = idUnit;
	}

	/**
	 * @return the idCompany
	 */
	public Integer getIdCompany() {
		return idCompany;
	}

	/**
	 * @param idCompany the idCompany to set
	 */
	public void setIdCompany(Integer idCompany) {
		this.idCompany = idCompany;
	}

	/**
	 * @return the clausulaFormaPago
	 */
	public String getClausulaFormaPago() {
		return ClausulaFormaPago;
	}

	/**
	 * @param clausulaFormaPago the clausulaFormaPago to set
	 */
	public void setClausulaFormaPago(String clausulaFormaPago) {
		ClausulaFormaPago = clausulaFormaPago;
	}

	public boolean isContractRisk() {
		return contractRisk;
	}

	public void setContractRisk(boolean contractRisk) {
		this.contractRisk = contractRisk;
	}

	public boolean isVoBocontractRisk() {
		return voBocontractRisk;
	}

	public void setVoBocontractRisk(boolean voBocontractRisk) {
		this.voBocontractRisk = voBocontractRisk;
	}

	public List<Version> getContractVersionHistory() {
		return contractVersionHistory;
	}

	public void setContractVersionHistory(List<Version> contractVersionHistory) {
		this.contractVersionHistory = contractVersionHistory;
	}

	public String getEstacionamientosInmueble() {
		return estacionamientosInmueble;
	}

	public void setEstacionamientosInmueble(String estacionamientosInmueble) {
		this.estacionamientosInmueble = estacionamientosInmueble;
	}

	public String getFiadorFechaEscrituraPublica() {
		return fiadorFechaEscrituraPublica;
	}

	public void setFiadorFechaEscrituraPublica(String fiadorFechaEscrituraPublica) {
		this.fiadorFechaEscrituraPublica = fiadorFechaEscrituraPublica;
	}

	public String getFiadorNumeroEscrituraPublica() {
		return fiadorNumeroEscrituraPublica;
	}

	public void setFiadorNumeroEscrituraPublica(String fiadorNumeroEscrituraPublica) {
		this.fiadorNumeroEscrituraPublica = fiadorNumeroEscrituraPublica;
	}

	public String getNombreFiador() {
		return nombreFiador;
	}

	public void setNombreFiador(String nombreFiador) {
		this.nombreFiador = nombreFiador;
	}

	public String getPenalizacionCantidadRentaMensualLetra() {
		return penalizacionCantidadRentaMensualLetra;
	}

	public void setPenalizacionCantidadRentaMensualLetra(String penalizacionCantidadRentaMensualLetra) {
		this.penalizacionCantidadRentaMensualLetra = penalizacionCantidadRentaMensualLetra;
	}

	public String getPenalizacionComisionRentaMensual() {
		return penalizacionComisionRentaMensual;
	}

	public void setPenalizacionComisionRentaMensual(String penalizacionComisionRentaMensual) {
		this.penalizacionComisionRentaMensual = penalizacionComisionRentaMensual;
	}

	public String getPenalizacionRentaMensualInmueble() {
		return penalizacionRentaMensualInmueble;
	}

	public void setPenalizacionRentaMensualInmueble(String penalizacionRentaMensualInmueble) {
		this.penalizacionRentaMensualInmueble = penalizacionRentaMensualInmueble;
	}

	public String getFiadorNumeroNotariaPublica() {
		return fiadorNumeroNotariaPublica;
	}

	public void setFiadorNumeroNotariaPublica(String fiadorNumeroNotariaPublica) {
		this.fiadorNumeroNotariaPublica = fiadorNumeroNotariaPublica;
	}

	public String getPorcentajeIvaContraprestacion() {
		return porcentajeIvaContraprestacion;
	}

	public void setPorcentajeIvaContraprestacion(String porcentajeIvaContraprestacion) {
		this.porcentajeIvaContraprestacion = porcentajeIvaContraprestacion;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getDescripcionEquipos() {
		return descripcionEquipos;
	}

	public void setDescripcionEquipos(String descripcionEquipos) {
		this.descripcionEquipos = descripcionEquipos;
	}

	public String getContactoMatenimiento() {
		return contactoMatenimiento;
	}

	public void setContactoMatenimiento(String contactoMatenimiento) {
		this.contactoMatenimiento = contactoMatenimiento;
	}

	public String getNumeroConvenio() {
		return numeroConvenio;
	}

	public void setNumeroConvenio(String numeroConvenio) {
		this.numeroConvenio = numeroConvenio;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getSolicitudDescripcionNegociacion() {
		return solicitudDescripcionNegociacion;
	}

	public void setSolicitudDescripcionNegociacion(String solicitudDescripcionNegociacion) {
		this.solicitudDescripcionNegociacion = solicitudDescripcionNegociacion;
	}

	public String getSolicitudDescripcionLargaNegociacion() {
		return solicitudDescripcionLargaNegociacion;
	}

	public void setSolicitudDescripcionLargaNegociacion(String solicitudDescripcionLargaNegociacion) {
		this.solicitudDescripcionLargaNegociacion = solicitudDescripcionLargaNegociacion;
	}

	public String getNumeroAdendum() {
		return numeroAdendum;
	}

	public void setNumeroAdendum(String numeroAdendum) {
		this.numeroAdendum = numeroAdendum;
	}

	public String getContratoaTerminar() {
		return contratoaTerminar;
	}

	public void setContratoaTerminar(String contratoaTerminar) {
		this.contratoaTerminar = contratoaTerminar;
	}

	public String getNombreClausulaAdicionar() {
		return nombreClausulaAdicionar;
	}

	public void setNombreClausulaAdicionar(String nombreClausulaAdicionar) {
		this.nombreClausulaAdicionar = nombreClausulaAdicionar;
	}

	public String getNombreAnexoAdicionar() {
		return nombreAnexoAdicionar;
	}

	public void setNombreAnexoAdicionar(String nombreAnexoAdicionar) {
		this.nombreAnexoAdicionar = nombreAnexoAdicionar;
	}

	public String getDescripcionClausulaModificada() {
		return descripcionClausulaModificada;
	}

	public void setDescripcionClausulaModificada(String descripcionClausulaModificada) {
		this.descripcionClausulaModificada = descripcionClausulaModificada;
	}

	public String getFechaTerminacionContratoaFinalizar() {
		return fechaTerminacionContratoaFinalizar;
	}

	public void setFechaTerminacionContratoaFinalizar(String fechaTerminacionContratoaFinalizar) {
		this.fechaTerminacionContratoaFinalizar = fechaTerminacionContratoaFinalizar;
	}

	public List<Customs> getCustomsList() {
		return customsList;
	}

	public void setCustomsList(List<Customs> customsList) {
		this.customsList = customsList;
	}

	public List<ModifiedClausules> getModifiedClausulesList() {
		return modifiedClausulesList;
	}

	public void setModifiedClausulesList(List<ModifiedClausules> modifiedClausulesList) {
		this.modifiedClausulesList = modifiedClausulesList;
	}

	public List<Tracto> getTractoList() {
		return tractoList;
	}

	public void setTractoList(List<Tracto> tractoList) {
		this.tractoList = tractoList;
	}

	public List<RollOff> getRollOffList() {
		return rollOffList;
	}

	public void setRollOffList(List<RollOff> rollOffList) {
		this.rollOffList = rollOffList;
	}

	public List<SupplierPersonByRequisition> getSupplierLegaList() {
		return supplierLegaList;
	}

	public void setSupplierLegaList(List<SupplierPersonByRequisition> supplierLegaList) {
		this.supplierLegaList = supplierLegaList;
	}

	public DocumentDS getDocumentDS() {
		return documentDS;
	}

	public void setDocumentDS(DocumentDS documentDS) {
		this.documentDS = documentDS;
	}

}
