package mx.pagos.admc.util.shared;

public class UrlConstants {

	
	/*
	 * For Purchases
	 */
	//Controller PUBLICCONTROLLER
	public static final String LOGIN = "controller/login.go";
	public static final String LOGINAD = "loginAD.loginAd";
	public static final String DO_LOGIN = "do.login";
	public static final String DO2_LOGIN = "do2.login";
	public static final String VALIDATE_SESSION = "controller/validateSession.do";
	public static final String VALIDATE_GERENTE = "controller/validateGerente.do";
	public static final String IS_INITIAL_SYSTEM_CONFIGURATION = "isInitialSystemConfiguration.do";
	public static final String SAVE_INITIAL_SYSTEM_CONFIGURATION = "saveInitialSystemConfiguration.do";
	public static final String CLOSE_SESSION = "controller/closeSession.do";
	public static final String RECOVERY_PASSWORD = "controller/recoveryPassword.do";
	public static final String FEED_SESSION = "feedSession.do";
	public static final String FIND_LOGIN_TYPE = "findLoginType.do";
	public static final String FIND_VERSION_SYSTEM = "findVersionSystem.do";

	//Controller AREACONTROLLER
	public static final String SAVEORUPDATE = "controller/area/Saveorupdate.do";
	public static final String TEST = "controller/TEST.go";
    public static final String AREA_SAVE_OR_UPDATE = "controller/area/saveorupdate.do";
    public static final String CHANGE_AREA_STATUS = "controller/area/changeAreaStatus.do";
    public static final String AREA_FIND_ALL = "controller/area/findAll.do";
    public static final String AREA_FIND_ACTIVE = "controller/area/findActive.do";
    public static final String FIND_AREAS_BY_STATUS = "controller/area/findByStatus.do";
    public static final String FIND_BY_ID = "controller/area/findById.do";
    public static final String FIND_AREAS_BY_ID_REQUISITION = "controller/area/findAreasByIdRequisition.do";
    public static final String FIND_ALL_AREAS_CATALOG_PAGED = "controller/area/findAreasCatalogPaged.do";
    public static final String TOTAL_PAGES_SHOW_AREAS = "controller/area/returnTotalPagesShowAreas.do";
    public static final String FIND_APPROVAL_AREAS_DOCUMENTS = "controller/area/findApprovalAreasDocuments.do";

    //Controller PublicFigureController
    public static final String PUBLICFIGURE_SAVEORUPDATE = "controller/publicFigure/saveOrUpdate.do";
    public static final String PUBLICFIGURE_CHANGE_STATUS = "controller/publicFigure/changeStatus.do";
    public static final String PUBLICFIGURE_FIND_ALL = "controller/publicFigure/findAll.do";
    public static final String PUBLICFIGURE_FIND_BY_STATUS = "controller/publicFigure/findByStatus.do";
    public static final String PUBLICFIGURE_FIND_BY_ID = "controller/publicFigure/findById.do";
    public static final String PUBLICFIGURE_FIND_BY_TYPE = "controller/publicFigure/findAreasByIdRequisition.do";
    public static final String PUBLICFIGURE_FIND_ALL_CATALOG_PAGED = 
            "controller/publicFigure/findAllPublicFigureCatalogPaged.do";
    public static final String PUBLICFIGURE_FIND_TOTAL_PAGES = 
            "controller/publicFigure/returnTotalRowsOfPublicFigure.do";
    
    //Controller DgaController
    public static final String DGA_SAVEORUPDATE = "controller/dga/Saveorupdate.do";
    public static final String DGA_CHANGE_STATUS = "controller/dga/changeDgaStatus.do";
    public static final String DGA_FIND_ALL = "controller/dga/findAll.do";
    public static final String DGA_FIND_ACTIVE = "controller/dga/findActive.do";
    public static final String DGA_FIND_BY_STATUS = "controller/dga/findByStatus.do";
    public static final String DGA_FIND_BY_ID = "controller/dga/findById.do";
    public static final String DGA_FIND_ALL_CATALOG_PAGED = "controller/dga/findAllDgaCatalogPaged.do";
    public static final String DGA_FIND_TOTAL_PAGES = "controller/dga/returnTotalRowsOfDga.do";
    
  //DocumentPersonalityFieldsService
    public static final String FIND_DOCUMENT_FIELDS_BY_DOCUMENT = 
            "controller/documentPersonalityFields/finDocumentFieldsByDocument.do";
    public static final String SECTION_TO_SHOW_BY_DOCUMENT = 
            "controller/documentPersonalityFields/sectionsToShowByDocument.do";
    public static final String FIND_ALL_DOCUMENT_FIELDS = 
            "controller/documentPersonalityFields/finAllDocumentFields.do";
    public static final String SAVE_DOCUMENT_FIELDS_BY_DOCUMENT = 
            "controller/documentPersonalityFields/saveDocumentFieldsByDocument.do";

    //Controller DocumentTypeController
    public static final String DOCTYPE_SAVEORUPDATE = "controller/documentType/Saveorupdate.do";
    public static final String DOCTYPE_CHANGE_STATUS = "controller/documentType/changeDocTypeStatus.do";
    public static final String DOCTYPE_FIND_ALL = "controller/documentType/findByIdDocument.do";
    public static final String DOCTYPE_FIND_BY_STATUS_AND_DOCUMENTTYPE =
            "controller/documentType/findDocumentTypeByStatusAndDocumentType.do";
    public static final String DOCTYPE_FIND_BY_IDAREA = "controller/documentType/findAll.do";
    public static final String DOCTYPE_FIND_BY_STATUS = "controller/documentType/findByRecordStatus.do";
    public static final String DOCTYPE_FIND_BY_ID = "controller/documentType/findById.do";
    public static final String DOCUMENT_TYPE_NAME_EXISTS = "controller/documentType/nameExists.do";
    public static final String DOCTYPE_FIND_ACTIVE = "controller/documentType/findActive.do";
    
    public static final String DOWNLOAD_DOCUMENTTYPE = "controller/requisition/downloadDocumentType.do";
    public static final String DOWNLOAD_DOCUMENTTYPE_NATURAL = "controller/requisition/downloadDocumentTypeNatural.do";
    public static final String DOCTYPE_FIND_ALL_CATALOG_PAGED = 
            "controller/requisition/findAllDocumentTypeCatalogPaged.do";
    public static final String DOCTYPE_FIND_TOTAL_PAGES = "controller/requisition/returnTotalRowsOfDocumentType.do";
    
    //Controller ProfileController
    public static final String PROFILE_SAVEORUPDATE = "controller/profile/Saveorupdate.do";
    public static final String PROFILE_CHANGE_STATUS = "controller/profile/changeStatus.do";
    public static final String PROFILE_FIND_ALL = "controller/profile/findAll.do";
    public static final String PROFILE_FIND_BY_STATUS = "controller/profile/findByRecordStatus.do";
    public static final String PROFILE_FIND_BY_ID = "controller/profile/findById.do";
    public static final String PROFILE_SAVENEWMENUPROFILE = "controller/profile/saveNewMenuProfile.do";
    public static final String PROFILE_FINDMENUPROFILEBYIDPROFILE = "controller/profile/findMenuProfileByIdProfile.do";
    public static final String PROFILE_SAVENEWPROFILESCREEN = "controller/profile/saveNewProfileScreen.do";
    public static final String PROFILE_FINDPROFILESCREENTRAYBYIDPROFILE =
            "controller/profile/findProfileScreenTrayByIdProfile.do";
    public static final String PROFILE_FINDMENUBYID = "controller/profile/findMenuProfileById.do";

    //Controller GuaranteesController
    public static final String GUARANTEES_SAVEORUPDATE = "controller/guarantees/Saveorupdate.do";
    public static final String GUARANTEES_CHANGE_STATUS = "controller/guarantees/changeStatus.do";
    public static final String GUARANTEES_FIND_ALL = "controller/guarantees/findAll.do";
    public static final String GUARANTEES_FIND_BY_STATUS = "controller/guarantees/findByStatus.do";
    public static final String FIND_CHECK_DOCUMENT_BY_GUARANTEE = 
            "controller/guarantees/findCheckDocumentListByIdGuarantee.do";
    public static final String FIND_GUARANTEE_CHECK_DOCUMENT_BY_STATUS = 
            "controller/guarantees/findGuaranteeCheckDocuementation.do";
    public static final String GUARANTEES_FIND_BY_ID = "controller/guarantees/findById.do";
    public static final String GUARANTEES_FIND_ALL_CATALOG_PAGED = 
            "controller/guarantees/findAllGuaranteesCatalogPaged.do";
    public static final String GUARANTEES_FIND_TOTAL_PAGES = "controller/guarantees/returnTotalRowsOfGuarantees.do";
    public static final String DOWNLOAD_GUARANTEES = "controller/requisition/downloadGuarantees.do";

	//CommentController
	public static final String COMMENT_SAVE_OR_UPDATE = "controller/comment/saveorupdate.do";
	public static final String FIND_BY_IDREQ_AND_FLOWSTATUS =
	        "controller/comment/findByIdRequisitionAndFlowStatus.do";
	public static final String FIND_BY_ID_COMMENT =
	        "controller/comment/findByIdComment.do";

	//CatalogApplicableConfidentialityLawsController
	public static final String APPLICABLE_CONFIDENTIALITY_LAWS_FIND_ALL = 
			"controller/financialEntity/applicableConfidentialityLawsFindAll.do";
	public static final String APPLICABLE_CONFIDENTIALITY_LAWS_FIND_ACTIVE = 
			"controller/financialEntity/applicableConfidentialityLawsFindActive.do";
	public static final String APPLICABLE_CONFIDENTIALITY_LAWS_FIND_BY_RECORD_STATUS = 
			"controller/financialEntity/applicableConfidentialityLawsFindByRecordStatus.do";
	public static final String APPLICABLE_CONFIDENTIALITY_LAWS_FIND_BY_ID_FINANCIAL_ENTITY = 
			"controller/financialEntity/applicableConfidentialityLawsFindByIdFinancialEntity.do";
	public static final String APPLICABLE_CONFIDENTIALITY_LAWS_FIND_BY_ID = 
			"controller/financialEntity/applicableConfidentialityLawsFindById.do";
	public static final String APPLICABLE_CONFIDENTIALITY_LAWS_SAVE_OR_UPDATE = 
			"controller/financialEntity/applicableConfidentialityLawsSaveOrUpdate.do";
	public static final String CHANGE_APPLICABLE_CONFIDENTIALITY_LAWS_STATUS = 
			"controller/financialEntity/changeApplicableConfidentialityLawsStatus.do";
	public static final String CONFINDENTIALITY_LAW_ALL_CATALOG_PAGED = 
	        "controller/financialEntity/findAllConfidentialityLawCatalogPaged.do";
	public static final String CONFINDENTIALITY_LAW_FIND_TOTAL_PAGES = 
	        "controller/financialEntity/returnTotalRowsOfConfidentialityLaw.do";
	
	//FinancialEntityController
	public static final String FINANCIAL_SAVE_OR_UPDATE = "controller/financialEntity/saveOrUpdate.do";
	public static final String FIND_FINANCIAL_BY_REQUISITION = 
	        "controller/financialEntity/financialEntityByRequisition.do";
	public static final String CHANGE_FINANCIAL_STATUS =
	        "controller/financialEntity/changeFinancialEntityStatus.do";
	public static final String FINANCIAL_FIND_ALL = "controller/financialEntity/findAll.do";
	public static final String FINANCIAL_FIND_DATA_FINANCIAL_ENTITY =
    		"controller/legalRepresentative/findDataFinantialEntity.do";
	public static final String FINANCIAL_FIND_ACTIVE = "controller/financialEntity/findActive.do";
    public static final String FINANCIAL_FIND_BY_RECORD_STATUS = "controller/financialEntity/findByRecordStatus.do";
    public static final String FINANCIAL_FIND_BY_ID = "controller/financialEntity/findByIdFinancialEntity.do";
    public static final String FINANCIAL_FIND_ALL_CATALOG_PAGED = 
            "controller/financialEntity/findAllFinancialEntityCatalogPaged.do";
    public static final String FINANCIAL_FIND_TOTAL_PAGES = 
            "controller/financialEntity/returnTotalRowsOfFinancialEntity.do";
    public static final String FINANCIAL_FIND_DATA_FINANCIAL_ENTITY_REQUISITION  =
    		"controller/financialEntity/findDataFinantialEntityRequisition.do";

    //OrganizationEntityController
    public static final String ORGANIZATION_ENTITY_SAVE_OR_UPDATE = "controller/OrganizationEntity/saveOrUpdate.do";
    public static final String ORGANIZATION_ENTITY_CHANGE_STATUS =
            "controller/OrganizationEntity/changeStatus.do";
    public static final String ORGANIZATION_ENTITY_FIND_ALL = "controller/OrganizationEntity/findAll.do";
    public static final String ORGANIZATION_ENTITY_FIND_BY_STATUS = "controller/OrganizationEntity/findByStatus.do";
    public static final String ORGANIZATION_ENTITY_FIND_BY_ID = "controller/OrganizationEntity/findById.do";
    public static final String ORGANIZATION_ENTITY_FIND_ALL_CATALOG_PAGED = 
            "controller/organizationEntity/findAllOrganizationEntityCatalogPaged.do";
    public static final String ORGANIZATION_ENTITY_FIND_TOTAL_PAGES = 
            "controller/organizationEntity/returnTotalRowsOfOrganizationEntity.do";
    
    //CategoryController


    public static final String CATEGORY_SAVE_OR_UPDATE = "controller/category/saveOrUpdate.do";
    public static final String CHANGE_CATEGORY_STATUS = "controller/category/changeCategoryStatus.do";
    public static final String CATEGORY_FIND_ALL = "controller/category/findAll.do";
    public static final String CATEGORY_FIND_BY_RECORD_STATUS = "controller/category/findByRecordStatus.do";
    public static final String CATEGORY_FIND_BY_ID = "controller/category/findById.do";
    public static final String FIND_CHECK_DOCUMENTATION_BY_CATEGORY = 
            "controller/category/findCheckDocumentationIdsByCategory.do";
    public static final String CATEGORY_FIND_ALL_CATALOG_PAGES = "controller/category/findAllCategoryCatalogPaged.do";
    public static final String CATEGORY_FIND_TOTAL_PAGES = "controller/category/returnTotalRowsOfCategory.do";
    
    //ConfigurationService
    public static final String CONFIGURATION_FIND_ALL = "controller/configuration/findAll.do";
    public static final String UPDATE_CONFIGURATION = "controller/configuration/updateConfiguration.do";
    public static final String UPDATE_PUR_REQ_GENERAL_REPORT_CONFIGURATIONS =
            "controller/configuration/updateUpdatePurReqGeneralReportConfigurations.do";
    public static final String FIND_CONFIGURATION_BY_CATEGORY = 
            "controller/configuration/findConfigurationsByCategory.do";
    public static final String FIND_PUR_GENERAL_REPORT_MAILS =
            "controller/configuration/findPurchasingGeneralReportMails.do";
    public static final String FIND_PUR_GENERAL_REPORT_LATEST_MONTHS =
            "controller/configuration/findPurGeneralReportLastestMonths.do";
    public static final String FIND_AUTOSAVE_TIME_SECONDS_CONFIGURATION =
            "controller/configurations/findAutoSaveTimeSecons.do";
    
    //RequisitionController
    public static final String SAVE_REQUISITION_IN_PROGRESS = "controller/requisition/saveRequisitionInProgress.do";
    public static final String SAVE_REQUISITION_IN_PROGRESS_PART_1_AND_2 = "controller/requisition/saveRequisitionInProgressPart1And2.do";
    public static final String SAVE_REQUISITION_IN_PROGRESS_PART_5 = "controller/requisition/saveRequisitionInProgressPart5.do";  
    public static final String SAVE_REQUISITION_IN_PROGRESS_PART_6 = "controller/requisition/saveRequisitionInProgressPart6.do";  
    public static final String SAVE_REQUISITION_IN_PROGRESS_PART_7 = "controller/requisition/saveRequisitionInProgressPart7.do";
    public static final String SAVE_REQUISITION_DRATF_PART2 = "controller/requisition/saveRequisitionDraftPart2.do";
    public static final String SAVE_REQUISITION_DRATF_PROEM = "controller/requisition/saveRequisitionDraftProem.do";
    public static final String SAVE_REQUISITION_DRATF_CLAUSULES = "controller/requisition/saveRequisitionDraftClausules.do"; 
    public static final String SAVE_REQUISITION_DRATF_PROPERTY = "controller/requisition/saveRequisitionDraftProperty.do";  
    public static final String REQUISITION_DETAIL = "controller/requisition/detailRequisition.do";  
    public static final String SAVE_REQUISITION_IN_PROGRESS_PART_3="controller/requisition/saveRequisitionInProgressPart3.do";
    public static final String SAVE_REQUISITION_IN_PROGRESS_PART_4="controller/requisition/saveRequisitionInProgressPart4.do";
    public static final String FIND_WHOLE_REQUISITION = "controller/requisition/findWholeRequisition.do";
    public static final String SEND_REQUISITION = "controller/requisition/saveOrUpdate.do";
    public static final String SEND_REQUISITION_ANGULAR = "controller/requisition/saveOrUpdateAngular.do";
    public static final String SEND_REQUISITION_ANGULAR_NOTIFICATION = "controller/requisition/saveOrUpdateAngularNotification.do";
    public static final String FIND_REQUISITIONS_TO_CREATE_ONE_FROM_ANOTHER = 
    		"controller/requisition/findRequisitionsToCreateOneFromAnother.do";
    public static final String FIND_ALL_REQUISITIONS_GENERAL = 
    		"controller/requisition/findAllRequisitions.do";
    public static final String FIND_DOCUMENTS_ATTACHMENT = "controller/requisition/findDocumentsAttachment.do";
    public static final String FIND_DOCUMENTS_ATTACHMENT_VERSION_DETAIL = 
    		"controller/requisition/findDocumentsAttachmentVersionDetail.do";
    public static final String CHANGE_REQUISITION_ESTATUS = "controller/requisition/changeRequisitionStatus.do";
    public static final String CHANGE_REQUISITION_STATUS_TO_CANLCELLED = 
    		"controller/requisition/changeRequisitionStatusToCancelled.do";
    public static final String REQUISITION_FIND_BY_ID = "controller/requisition/findById.do";
    public static final String DS_GET_USER = "controller/requisition/getUser.do";
    public static final String REQUISITION_FIND_DRAFT_BY_ID = "controller/requisition/findDraftById.do";
    public static final String REQUISITION_FIND_BY_ID_IN_PROGRESS = "controller/requisition/findByIdInProgress.do";
    public static final String REQUISITION_VERSION_FIND_BY_ID = "controller/requisition/findRequisitionVersionById.do";
    public static final String FIND_WHOLE_REQUISITION_VERSION_BY_ID = "controller/requisition/findWholeRequisitionVersionById.do";
    public static final String FIND_SECOND_WHOLE_REQUISITION_VERSION_BY_ID = "controller/requisition/findSecondWholeRequisitionVersionById.do";
    public static final String REQUISITION_FIND_BY_RECORDSTATUS = "controller/requisition/findByRecordStatus.do";
    public static final String DOWNLOAD_DRAFT_REQUISITION = "controller/requisition/downloadDraftRequisition2.do";
    public static final String DOWNLOAD_DRAFT_CONTRACT = "controller/requisition/downloadDraftContract.do";
    public static final String DOWNLOAD_CURRENT_VERSION = "controller/requisition/downloadCurrentVersion.do";
    public static final String FIND_REQUISITION_FINANCIAL_ENTITY =
            "controller/requisition/findRequisitionFinancialEntityByIdRequisition.do";
    public static final String FIND_REQUISITION_FINANCIAL_ACTIVE_ENTITY =
            "controller/requisition/findRequisitionFinancialEntityActivateByIdRequisition.do";
    public static final String FIND_REQUISITION_FINANCIAL_ENTITY_WITNESS =
    		"controller/requisition/findRequisitionFinancialEntityWitnesByIdRequisition.do";
    public static final String FIND_REQUISITION_AUTHORIZATION_DGAS =
            "controller/requisition/findRequisitionAuthorizationDgas.do";
    public static final String FIND_REQUISITION_AUTHORIZATION_DGAS_ACTIVE =
            "controller/requisition/findRequisitionAuthorizationDgasActive.do";
    public static final String FIND_REQUISITION_APPROVAL_AREAS_NAME = 
            "controller/requisition/findApprovalAreasNames.do";
    public static final String FIND_REQUISITION_APPROVAL_AREAS =
            "controller/requisition/findRequisitionApprovalAreas.do";
    public static final String FIND_REQUISITION_APPROVAL_AREAS_ACTIVE =
            "controller/requisition/findRequisitionApprovalAreasActive.do";
    public static final String SEND_REQUISITION_TO_LAWYER = "controller/requisition/sendRequisitionToLawyer.do";
    public static final String REJECT_TO_LAWYER = "controller/requisition/rejectRequisitionToLawyer.do";
    public static final String SEND_REQUISITION_TO_LAWYER_ASSIGMENT =
            "controller/requisition/sendRequisitionToLawyerAssigment.do";
    public static final String SAVE_REQUISITION_DOCUMENT_REVIEW_LAWYER =
            "controller/requisition/saveRequisitionDocumentReviewLawyer.do";
    public static final String SEND_REQUISITION_DOCUMENT_REVIEW_LAWYER =
    "controller/requisition/sendRequisitionDocumentReviewLawyer.do";
    public static final String REJECT_REQUISITION_DOCUMENT_REVIEW_LAWYER =
    		"controller/requisition/rejectRequisitionDocumentReviewLawyer.do";
    public static final String SEND_REVIEW_DRAFT = "controller/requisition/sendReviewDraft.do";
    public static final String SEND_REVIEW_DRAFT_ANGULAR = "controller/requisition/sendReviewDraftAngular.do";
    public static final String SEND_REVIEW_DRAFT_ANGULAR_NOTIFICATION = "controller/requisition/sendReviewDraftAngularNotification.do";
    public static final String SAVE_DRAFT_INFO = "controller/requisition/saveDraftInfo.do";
    public static final String CHANGE_REVIEW_DRAFT = "controller/requisition/changeReviewDraft.do";
    public static final String SEND_DRAFT_CONTRACT_NEGOTIATOR = "controller/requisition/sendDraftContractNegotiator.do";
    public static final String SEND_DRAFT_CONTRACT_NEGOTIATOR_NOTIFICATION = "controller/requisition/sendDraftContractNegotiatorNotification.do";
    public static final String REJECT_DRAFT_CONTRACT_NEGOTIATOR =
    		"controller/requisition/rejectDraftContractNegotiator.do";
    public static final String REQUEST_MODIFICATION_DRAFT =
    		"controller/requisition/requestModificationDraft.do";
    public static final String REQUEST_MODIFICATION_DRAFT_NOTIFICATION =
    		"controller/requisition/requestModificationDraftNotification.do";
    public static final String SEND_DRAFT_CONTRACT_USER = "controller/requisition/sendDraftContractUser.do";
    public static final String REJECT_DRAFT_CONTRACT_USER = "controller/requisition/rejectDraftContractUser.do";
    public static final String SAVE_SUPPLIER_VO_BO = "controller/requisition/saveSupplierVoBo.do";
    public static final String DELETE_SUPPLIER_APPROVAL_DOCUMENT = 
            "controller/requisition/deleteSupplierApprovalDocument.do";
    public static final String SEND_TO_VO_BO = "controller/requisition/sendToVoBo.do";
    public static final String SEND_TO_DOCUMENT_FINAL = "controller/requisition/sendToDocumentFinal.do";
    public static final String REJECT_TO_VO_BO = "controller/requisition/rejectToVoBo.do";
    public static final String SEND_APPROVED_CONTRACT = "controller/requisition/sendApprovedContract.do";
    public static final String SEND_SIGNING_CONTRACT = "controller/requisition/sendSigningContract.do";
    public static final String SAVE_SIGN_CONTRACT = "controller/requisition/saveSignContract.do";
    public static final String SAVE_SIGN_CONTRACT_NOTIFICATION = "controller/requisition/saveSignContractNotification.do";

    public static final String SAVE_CONTRACT_TO_DIGITIZE = "controller/requisition/saveContractToDigitize.do";
    public static final String SAVE_DOCUMENTATION_MISSING = "controller/requisition/saveDocumentationMissing.do";
    public static final String SEND_DOCUMENTATION_MISSING = "controller/requisition/sendDocumentationMissing.do";
    public static final String FIND_HISTORY_DOCUMENTS_VERSIONS =
    		"controller/requisition/findHistoryDocumentsVersions.do";
    public static final String FIND_FLOW_SCREEN = "controller/requisition/findFlowScreen.do";
    public static final String DOWNLOAD_DOCUMENT_FROM_HISTORY = "controller/requisition/downloadDocumentFromHistory.do";
    public static final String DOWNLOAD_REQDOC_FROM_HISTORY = "controller/requisition/downloadReqDocumentFromHistory.do";
    public static final String DOWNLOAD_DOCUMENT_REQUIRED = "controller/requisition/downloadDocumentRequired.do";
    public static final String DOCUMENT_REQUIRED_PDF = "controller/requisition/documentRequiredpdf.do";
    public static final String DOWNLOAD_PREVIEW = "controller/requisition/downloadPreview.do";
    public static final String REQUISITION_LEGAL_REPRESENTATIVES =
            "controller/requisition/findRequisitionLegalRepresentatives.do";
    public static final String REQUISITION_LEGAL_REPRESENTATIVES_ACTIVE =
            "controller/requisition/findRequisitionLegalRepresentativesActive.do";
    public static final String FIND_DIGITALIZATION_DOCUMENT_VERSION =
            "controller/requisition/findDigitalizationDocumentVersion.do";
    public static final String FIND_REQUISITION_LEGAL_REPRESENTATIVES =
            "controller/requisition/findRequisitionLegalRepresentative.do";
    public static final String FIND_REQUISITION_VERSION_HISTORY =
    		"controller/requisition/findRequisitionVersionHistory.do";
    public static final String DOWNLOAD_DOC_REQUIRED =
    		"controller/requisition/downloadDocRequired.do";
    public static final String SAVE_REQUISITION_TEMPLATE = "controller/requisition/saveTemplate.do";
    public static final String DELETE_REQUISITION_TEMPLATE = "controller/requisition/deleteTemplate.do";
    public static final String FIND_REQUISITION_TEMPLATE = "controller/requisition/findTemplate.do";
    public static final String DELETE_DIGITALIZATION = "controller/requisition/deleteDigitalizationByIdDocument.do";
    public static final String DOWNLOAD_REQUISITION_DOCUMENT = "controller/requisition/downloadDocument.do";
    public static final String FIND_REQUISITION_OBLIGATIONS = "controller/requisition/findObligations.do";
    public static final String FIND_LAST_VERSION_DOCUMENT_BY_ID =
            "controller/requisition/findLastVersionDocumentById.do";
    public static final String FIND_IN_PROGRESS_REQUISITIONS =
            "controller/requisition/findInProgressRequisitions.do";
    public static final String FIND_PAGINATED_TRAY_REQUISITIONS_BY_STATUS =
            "controller/requisition/findPaginatedTrayBystatus.do";
    public static final String FIND_PAGINATED_TRAY_REQUISITIONS_BY_STATUS_GRAFICA =
            "controller/requisition/findPaginatedTrayBystatusGrafica.do";
    public static final String FIND_PAGINATED_TRAY_REQUISITIONS_BY_DATE =
            "controller/requisition/findPaginatedTrayPorFechas.do";
    public static final String FIND_REQUISITION_BY_ID_FLOW = "controller/requisition/findRequisitionByFlow.do";
    public static final String SAVE_REQUISITION_EDITED_DATA = "controller/requisition/saveRequisitionEditedData.do";
    public static final String FIND_REQUISITION_BY_PARAMETERS = "controller/requisition/findRequisitionByParameters.do";
    public static final String GENERATE_EXCEL_REPORT = "controller/requisition/generateExcelReport.do";
    public static final String FIND_REQUISITION_CLOSED = "controller/requisition/findRequisitionClosed.do";
    public static final String CHANGE_ATTEND_STATUS = "controller/requisition/changeAttendStatus.do";
    public static final String FIND_SCALING_MATRIX_BY_ID_REQUISITION_AND_SCALING_TYPE =
            "controller/requisition/findScalingMatrixByIdRequisitionAndScalingType.do";
    public static final String FIND_SCALING_MATRIX_VERSION_BY_ID_REQUISITION_VERSION_AND_SCALING_TYPE =
            "controller/requisition/findScalingMatrixVersionByIdRequisitionVersionAndScalingType.do";
    public static final String FIND_APPROVAL_AREAS_VERSION = "controller/requisitionVersion/findApprovalAreas.do";
    public static final String FIND_APPROVAL_AREAS_VOBO_VERSION =
            "controller/requisitionVersion/findApprovalAreasVoBo.do";
    public static final String FIND_ATTACHMENT_VERSION = "controller/requisitionVersion/findAttchement.do";
    public static final String FIND_AUTHORIZATION_DGA_VERSION = "controller/requisitionVersion/findAuthorizationDGA.do";
    public static final String FIND_FINANTIAL_ENTITY_VERSION = "controller/requisitionVersion/findFinantialEntity.do"; 
    public static final String FIND_FINANTIAL_ENTITY_WITNESS_VERSION = 
            "controller/requisitionVersion/findFinantialEntityWitness.do";
    public static final String FIND_LEGAL_REPRESENTATIVE_VERSION = 
            "controller/requisitionVersion/findLegalRepresentative.do";
    public static final String FIND_USER_VOBO_VERSION = "controller/requisitionVersion/findUsersVoBo.do";
    public static final String FIND_PAGINATED_REQUISITIONS_MANAGEMENT =
            "controller/requisition/findPaginatedRequisitionsManagement.do";
    public static final String TOTAL_PAGES_SHOW_PAGINATED_REQUISITIONS_MANAGEMENT =
            "controller/requisition/returnTotalPagesShowRequisitionsManagement.do";
    public static final String TOTAL_DATA_SHOW_PAGINATED_REQUISITIONS_MANAGEMENT =
            "controller/requisition/returnTotalDataShowRequisitionsManagement.do";
    public static final String FIND_PAGINATED_REQUISITIONS_CLOSED =
            "controller/requisition/findPaginatedRequisitionsClosed.do";
    public static final String FIND_PAGINATED_CONTRACTS = "controller/requisition/findPaginatedContracts.do";
    public static final String TOTAL_PAGES_SHOW_PAGINATED_REQUISITIONS_CLOSED =
            "controller/requisition/returnTotalPagesShowRequisitionsClosed.do";
    public static final String TOTAL_PAGES_SHOW_PAGINATED_OF_CONTRACTS =
            "controller/requisition/returnTotalPagesShowOfContracts.do";
    public static final String VERIFY_CURRENT_DRAF = "controller/requisition/findTemplate.do";
    public static final String VERIFY_DRAF_DOCUMENT = "controller/requisition/verifyDrafDocument.do";
    public static final String VERIFY_CONTRACT_DOCUMENT = "controller/requisition/verifyContractDocument.do";
    public static final String COMPARE_WORD_FILES = "controller/requisition/wordCompare.do";
    public static final String DOWNLOAD_WORD_COMPARATION_FILE = "controller/requisition/downloadWordComparationFile.do";
    public static final String SAVE_CONTRACT_CANCELLATION_COMMENT = 
            "controller/requisition/saveContractCancellationComment.do";
    public static final String SAVE_CONTRACT_CANCELLATION_COMMENT_NOTIFICATION = "controller/requisition/saveContractCancellationCommentNotification.do";
    public static final String FIND_CONTRACT_CANCELLATION_COMMENT = 
            "controller/requisition/findContractCancellationComment.do";
    public static final String TOTAL_PAGES_SHOW_TRAY = "controller/requisition/totalPagesForTray.do";
    public static final String CREATE_REPLACES_MAP = "controller/requisition/createReplacesMap.do";
    public static final String FIND_APPLICANT_INPROGRESS_REQUISITIONS =
            "controller/requisition/findApplicantInProgressRequisitions.do";
    public static final String TOTAL_PAGES_APPLICANT_IN_PROGRESS_REQUISITIONS =
            "controller/requisition/countTotalPagesApplicantInProgressRequisitions.do";
    public static final String FIND_LAWYER_INPROGRESS_REQUISITIONS =
            "controller/requisition/findLawyerInProgressRequisitions.do";
    public static final String TOTAL_PAGES_LAWYER_IN_PROGRESS_REQUISITIONS =
            "controller/requisition/countTotalPagesLawyerInProgressRequisitions.do";
    
    //LegalRepresentativeController
    public static final String LEGAL_SAVE_OR_UPDATE = "controller/legalRepresentative/saveOrUpdate.do";
    public static final String CHANGE_LEGAL_STATUS =
            "controller/legalRepresentative/changeLegalRepresentativeStatus.do";
    public static final String LEGAL_FIND_ALL = "controller/legalRepresentative/findAll.do";
    public static final String LEGAL_FIND_BY_RECORD_STATUS = "controller/legalRepresentative/findByRecordStatus.do";
    public static final String LEGAL_FIND_DGA_AND_FINANCIAL_ENTITY =
            "controller/legalRepresentative/findByDgaAndFinancialEntity.do";
    public static final String LEGAL_FIND_FINANCIAL_ENTITIES_BY_ID_LEGAL = 
            "controller/legalRepresentative/findFinancialEntitiesByIdLegalRepresentative.do";
    public static final String LEGAL_FIND_BY_ID = "controller/legalRepresentative/findByIdLegalRepresentative.do";
    public static final String LEGAL_FIND_TOTAL_PAGES = 
            "controller/legalRepresentative/returnTotalRowsOfLegalRepresentative.do";
    public static final String LEGAL_FIND_ALL_CATALOG_PAGED = 
            "controller/legalRepresentative/findAllLegalRepresentativeCatalogPaged.do";

    //PowerController
    public static final String POWER_SAVE_OR_UPDATE = "controller/power/saveOrUpdate.do";
    public static final String CHANGE_POWER_STATUS = "controller/power/changePowerStatus.do";
    public static final String POWER_FIND_ALL = "controller/power/findAll.do";
    public static final String POWER_FIND_BY_RECORD_STATUS = "controller/power/findByRecordStatus.do";
    public static final String POWER_FIND_BY_ID = "controller/power/findByIdPower.do";
    public static final String POWERS_FIND_BY_ID_FINANCIAL_ENTITY = "controller/power/findByIdFinancialEntity.do";
    public static final String POWERS_FIND_BY_ID_FINANCIAL_ENTITY_AND_ID_LEGAL_REPRESENTATIVE = 
    		"controller/power/findByIdFinancialEntityAndIdLegalRepresentative.do";
    public static final String POWERS_FIND_ALL_CATALOG_PAGED = "controller/power/findAllPowerCatalogPaged.do";
    public static final String POWERS_FIND_TOTAL_PAGES = "controller/power/returnTotalRowsOfPower.do";

    //BinnacleController
    public static final String BINNACLE_SAVE = "controller/binnacle/saveOrUpdate.do";
    public static final String BINNACLE_FIND_ALL = "controller/binnacle/findAll.do";
    public static final String BINNACLE_FIND_BY_USER = "controller/binnacle/findByUser.do";
    public static final String BINNACLE_FIND_BY_LEVEL = "controller/binnacle/findByLevel.do";
    public static final String BINNACLE_FIND_BY_DATE = "controller/binnacle/findByDate.do";
    public static final String BINNACLE_FIND_BY_ID = "controller/binnacle/findByIdBinnacle.do";
    public static final String BINNACLE_FIND_BY_ID_FLOW = "controller/binnacle/findByIdFlow.do";
    public static final String BINNACLE_FIND_BY_ID_CATEGORY = "controller/binnacle/findByCategory.do";
    public static final String BINNACLE_DELETE_BY_DATES_RANGE = "controller/binnacle/deleteByDatesRange.do";
    public static final String BINNACLE_FIND_BY_LOG_CATEGORY = "controller/binnacle/findByLogCategoryTypes.do";
    public static final String BINNACLE_FIND_BY_LOG_CATEGORY_PAGINATED = 
    		"controller/binnacle/findByLogCategoryTypesPaginated.do";
    public static final String BINNACLE_FIND_BY_LOG_CATEGORY_TOTAL_PAGES = 
    		"controller/binnacle/findByLogCategoryTypesTotalPages.do";

    //SupplierController
	public static final String SEARCH_PROVIDERS = "controller/supplier/searchProviders.do";
	public static final String SAVE_SUPPLIER_PERSONS = "controller/supplier/saveSupplierPersons.do";
	public static final String SEARCH_SUPPLIER_PERSONS_BY_IDSUPPLIER_AND_TYPE =
	        "controller/supplier/findSupplierPersonsByIdSupplierAndType.do";
	public static final String FIND_SUPPLIER_LAWYERS_BY_SUPPLIER =
	        "controller/supplier/findLegalRepresentativesByIdSupplier.do";
	public static final String FIND_SUPPLIER_WITNESSES_BY_SUPPLIER =
	        "controller/supplier/findWitnessesByIdSupplierAndType.do";
	public static final String SEARCH_PROVIDERS_BY_RFC = "controller/supplier/searchProvidersByRfc.do";
	public static final String FIND_REQUIRED_DOCUMENT = "controller/supplier/findRequiredDocumentsByIdSupplier.do";
	public static final String UPDATE_SUPPLIER_DRAFT_FIELDS = "controller/supplier/updateDraftSupplierFields.do";
	public static final String SAVE_OR_UPDATE_SUPPLIER = "controller/supplier/saveOrUpdate.do";
	public static final String SUPPLIER_FIND_ALL = "controller/supplier/findAll.do";
	public static final String SUPPLIER_CHANGE_STATUS = "controller/supplier/changeStatus.do";
	public static final String SUPPLIER_PERSON_CHANGE_STATUS = "controller/supplierPerson/changeStatusPerson.do";
	public static final String SUPPLIER_FIND_BY_STATUS = "controller/supplier/findByStatus.do";
	public static final String SUPPLIER_UPDATE_REP_LEGAL = "controller/supplier/updateRepLegal.do";
	public static final String SUPPLIER_UPDATE_REP_LEGAL_ANGULAR = "controller/supplier/updateRepLegalAngular.do";
	public static final String SEARCH_APPLICATION_REVIEW = "controller/searchApplicationReview.do";
	public static final String SEARCH_LAWYER_ASSIGNMENT = "controller/searchLawyerAssignMent.do";
	public static final String SEARCH_ADD_USERS_TO_VOBO = "controller/searchAddUsersToVoBo.do";
	public static final String SEARCH_ADDED_USERS_TO_VOBO = "controller/searchAddedUsersToVoBo.do";
	public static final String SUPPLIER_FIND_ALL_CATALOG_PAGED = "controller/supplier/findAllSupplierCatalogPaged.do";
	public static final String SUPPLIER_FIND_TOTAL_PAGES = "controller/supplier/returnTotalRowsOfSupplier.do";
	
    //UploadService
    public static final String UPLOAD_SERVICE = "controller/document/uploadService.do";
    public static final String UPLOAD_SERVICE_ANGULAR = "controller/document/uploadServiceAngular.do";
    public static final String DELETE_SERVICE = "controller/document/deleteFileService.do";
    public static final String DOWNLOAD_SERVICE = "controller/document/downloadFileService.do";
    
    public static final String GENERATE_QR = "controller/document/generateQR.do";
    public static final String DOWNLOAD_QR = "controller/document/downloadQR.do";
    public static final String DOWNLOAD_DOC_QR = "controller/document/downloadDocQR.do";
    public static final String GET_QR = "controller/document/getQR.do";
    
    public static final String SEARCH_PROVIDERS_BY_ID = "controller/document/searchProvidersById.do";
    public static final String DOWNLOAD_REQUIRED_DOCUMENT = "controller/document/downloadRequiredDocument.do";
    public static final String VERIFY_CURRENT_DOCUMENT = "controller/document/verifydocument.do";
    public static final String VERIFY_DOCUMENT_PATH = "controller/document/verifyDocumentPath.do";
    public static final String IS_COMPANY_NAME_EXIST = "controller/document/isCompanyNameExist.do";
    public static final String EXIST_RFC = "controller/document/existRFC.do";
    
    //ProfilingService
    public static final String SEARCH_FLOWS_BY_USER_PROFILES = "controller/profiling/findFlowsByUserProfiles.do";
    public static final String SEARCH_UNIT_BY_USER_PROFILES = "controller/profiling/findUnitsByUserProfiles.do";
    public static final String SEARCH_COMPANY_BY_USER_PROFILES = "controller/profiling/findCompaniesByUserProfiles.do";

    public static final String VALIDATE_PROFILE_NAME = "controller/profiling/validateProfileName.do";
    public static final String FIND_PROFILE_BY_NAME = "controller/profiling/findProfilesByName.do";
    public static final String FIND_PROFILE_CATALOG_PAGED = "controller/profiling/findAllProfileCatalogPaged.do";
    public static final String FIND_PROFILE_TOTAL_ROWS = "controller/profiling/returnTotalRowsOfProfile.do";

    //PersonalitiesService
    public static final String PERSONALITY_FIND_BY_STATUS = "controller/personality/findByStatus.do";
    public static final String PERSONALITY_FIND_REQUIRED_DOCUMENT =
            "controller/personality/findRequiredDocumentByPersonality.do";
    public static final String PERSONALITY_FIND_ACTIVE = "controller/personality/findActive.do";
    
    //Flow
    public static final String FIND_FLOW_BY_SESSION = "controller/findFlowSession.do";
    public static final String FIND_FLOW_STEP_BY_SESSION = "controller/findFlowStepSession.do";
    public static final String FIND_FLOW_BY_STATUS = "controller/flow/findByStatus.do";
    public static final String FIND_PURCHASING_FLOWS = "controller/flow/findPurchasingFlows.do";
    
    //AlertsSErvice
	public static final String SEARCH_FLOWS = "controller/searchFlows.do";
    public static final String IS_MANAGERIAL_FLOW = "controller/flow/isManagerialFlow.do";	
	public static final String SEARCH_STATUSBYFLOW = "controller/findStatusByFlow.do";
	public static final String SEARCH_ALERTSBYFLOWANDSTATUS = "controller/findAlertbyFlowStatus.do";
    public static final String ALERTS_SAVE_OR_UPDATE = "controller/Alerts/saveOrUpdate.do";
    public static final String ALERTS_DELETE = "controller/Alerts/deleteAlert.do";
    public static final String FIND_ALERT_BY_ID = "controller/Alerts/findAlertById.do";
    public static final String FIND_ALERT_CONFLICTS = "controller/Alerts/findAlertConflicts.do";
    public static final String SEND_NOTIFICATION_TO_MANAGER_SYSTEM = 
            "controller/Alerts/senNotificationToManagerSystem.do";
    
    //Lawyer
    public static final String SEARCH_AVAILABLE_LAWYERS = "controller/Alerts/searchAviableLawyers.do";
    public static final String SEARCH_AVAILABLE_EVALUATORS = "controller/Alerts/searchEvaluators.do";
    public static final String SEARCH_AVAILABLE_LAWYERS_BY_FLOW = "controller/Alerts/searchAviableLawyersByFlow.do";
    public static final String SEARCH_AVAILABLE_EVALUATORS_BY_FLOW = "controller/Alerts/searchEvaluatorsByFlow.do";

    //UsersService
    public static final String FIND_USER_BY_ID = "controller/user/findUserById.do";
    public static final String FIND_LOGGED_USER = "controller/user/findLoggedUser.do";
    public static final String SEARCH_USERSBYNAME = "controller/searchUsersByName.do";
    public static final String SEARCH_LAWYER = "controller/searchLawyerByIdUser.do";
    public static final String SEARCH_USER_MAIL = "controller/searchUserMail.do";
    public static final String SEARCH_LAWYER_NAME = "controller/searchLawyerName.do";
    public static final String SAVE_CHANGES_LAWYER = "controller/saveChangesLawyer.do";
    public static final String SEARCH_ALLUSERS = "controller/searchAllUsers.do";
    public static final String CHANGEUSERSTATUS = "controller/changeUserStatus.do";
    public static final String PROFILE_FIND_BY_IDUSER = "controller/profile/findByIdUser.do";
    public static final String SAVEORUPDATEUSER = "controller/profile/saveOrUpdateUser.do";
    public static final String FIND_USERS_BY_STATUS = "controller/user/findByStatus.do";
    public static final String USER_EMAIL_EXISTS = "controller/user/userEmailExists.do";
    public static final String USERNAME_EXISTS = "controller/user/usernameExists.do";
    public static final String FIND_USERS_BY_AREA = "controller/user/findUserByArea.do";
    public static final String IS_USER_FILTERED = "controller/user/isUserFiltered.do";
    public static final String VALIDATE_PASSWORD = "controller/user/validatePassword.do";
    public static final String CHANGE_PASSWORD = "controller/user/changePassword.do";
    public static final String FIND_USER_ACTIVE_DIRECTORY = "controller/user/findUserActiveDirectory.do";
    public static final String FIND_DECIDER_LAWYER_BY_FLOW = "controller/user/findDeciderUser.do";
    public static final String FIND_ALL_USERS_PAGED = "controller/user/findAllUsersCatalogPaged.do";
    public static final String FIND_TOTAL_PAGES_FOR_CATALOG_OF_USER = "controller/user/returnTotalRowsOfUser.do";
    
    //PositionService
    public static final String POSITION_SAVE_OR_UPDATE = "controller/position/saveOrUpdate.do";
    public static final String POSITION_FIND_BY_RECORD_STATUS = "controller/position/findByRecordStatus.do";
    public static final String CHANGE_POSITION_STATUS = "controller/position/changePositionStatus.do";
    public static final String POSITION_FIND_BY_ID = "controller/position/findPositionByIdPosition.do";
    public static final String POSITIONS_SEARCHALL = "controller/searchPositions.do";
    public static final String POSITIONS_FIND_ALL_PAGED = "controller/findAllPositionCatalogPaged.do";
    public static final String POSITIONS_FIND_TOTAL_ROWS = "controller/returnTotalRowsOfCatalogPosition.do";
    
    //NoticeService
    public static final String NOTICE_FIND_AVAILABLE = "controller/notice/findNoticeByAvailables.do";
    public static final String NOTICE_SAVE_OR_UPDATE = "controller/notice/saveOrUpdate.do";
    public static final String NOTICE_FIND_BY_ID = "controller/notice/findByNoticeId.do";
    public static final String NOTICE_FIND_BY_ID_INFO = "controller/notice/findByNoticeIdInfo.do";
    public static final String NOTICE_DELETE_BY_ID = "controller/notice/deleteNoticeById.do";
    public static final String NOTICE_FIND_ALL_AVAILABLE_PAGED = "controller/notice/findAllAvailableNoticesPaged.do";
    public static final String NOTICE_RETURN_TOTAL_PAGES_TO_SHOW = "controller/notice/returnTotalPagesToShowNotices.do";

    //MenuService
    public static final String MENU_FIND_ALL = "controller/menu/findAll.do";

    //FlowScreenActionService
	public static final String SEARCH_FLOWSCREENACTIONBYFLOW = "controller/findFlowScreenActionByFlow.do";

	//findFlowScreenActionByProfile
	public static final String FIND_FLOWSCREENACTIONBYPROFILE = "controller/findFlowScreenActionByProfile.do";
	
	//LogSevice
    public static final String LOG_SAVE_ERROR = "controller/log/saveLog.do";
    public static final String GET_FOLDER_LOG_FILES = "controller/log/getFolderLogFiles.do";
    public static final String DOWNLOAD_LOG_FILE = "controller/log/downloadLogFile.do";
    public static final String DELETE_OLDER_LOG_FILES_THAN_DATE = "controller/log/deleteOlderLogFilesThanDate.do";
	
    //CheckDocumentService
    public static final String CHECK_DOCUMENT_SAVE_OR_UPDATE = "controller/checkDocument/saveOrUpdate.do";
    public static final String CHECK_DOCUMENT_CHANGE_STATUS = "controller/checkDocument/changeStatus.do";
    public static final String CHECK_DOCUMENT_FIND_BY_STATUS = "controller/checkDocument/findByStatus.do";
    public static final String CHECK_DOCUMENT_FIN_ALL = "controller/checkDocument/findAll.do";
    public static final String CHECK_DOCUMENT_FIND_BY_ID = "controller/checkDocument/findById.do";
    public static final String CHECK_DOCUMENT_FIND_BY_GUARANTEE_AND_STATUS =
            "controller/checkDocument/findByGuaranteeAndStatus.do";
    
    //CurrencyService
    public static final String CURRENCY_SAVE_OR_UPDATE = "controller/currency/saveOrUpdate.do";
    public static final String CURRENCY_CHANGE_STATUS = "controller/currency/changeStatus.do";
    public static final String CURRENCY_FIND_BY_STATUS = "controller/currency/findByStatus.do";
    public static final String CURRENCY_FIN_ALL = "controller/currency/findAll.do";
    public static final String CURRENCY_FIND_BY_ID = "controller/currency/";
    public static final String CURRENCY_FIND_ALL_CATALOG_PAGED = 
            "controller/currency/findAllCurrencyCatalogPaged.do";
    public static final String CURRENCY_FIND_NUMBERS_PAGE = "controller/currency/returnTotalRowsOfCurrency.do";

    //ReportsService
    public static final String REPORT_REQUISITIONS_BY_FLOWS = "controller/report/findRequisitionsCountByFlows.do";
    public static final String REPORT_REQUISITIONS_BY_USER = "controller/report/findRequisitionsCountByUser.do";
    public static final String REPORT_COUNT_REQUISITIONS_BY_USER_GRAPHIC = 
    		"controller/report/findCountRequisitionsCountByUserGraphic.do";
    public static final String REPORT_REPEATING_ROWS_REQUISITIONS_BY_USER_GRAPHIC = 
    		"controller/report/findRepeatingRowsRequisitionsCountByUserGraphic.do";
    public static final String REPORT_DATA_REQUISITIONS_BY_USER_GRAPHIC = 
    		"controller/report/findDataRowsRequisitionsCountByUserGraphic.do";
    public static final String REPORT_REQUISITION_OBLIGATIONS = "controller/report/findRequisitionObligations.do";
    public static final String FIND_GENERAL_REPORT = "controller/report/findGeneralReport.do";
    public static final String REPORT_REQUISITION_OBLIGATIONS_GRAPHIC = 
    		"controller/report/findRequisitionObligationsForGrapchic.do";
    public static final String CREATE_REQUISITIONS_BY_USER_EXCEL_REPORT
    = "controller/report/createRequisitionsByUserExcelReport.do";
    public static final String CREATE_OBLIGATIONS_EXCEL_REPORT = "controller/report/createObligationsExcelReport.do";
    public static final String CREATE_GENERAL_EXCEL_REPORT = "controller/report/createGeneralExcelReport.do";
    public static final String CREATE_REQUISITIONS_BY_FLOWS_EXCEL_REPORT
    = "controller/report/createRequisitionsByFlowExcelReport.do";
    public static final String FIND_GENERAL_REPORT_FILES = "controller/report/findGeneralReportsFiles.do";
    public static final String DOWNLOAD_GENERAL_REPORT_FILES = "controller/report/downloadGeneralReportsFiles.do";
    public static final String FIND_COUNT_OBLIGATIONS_REPORT_GRAPCHIC = 
    		"controller/report/findCountObligationsReportGraphic.do";
    public static final String FIND_REPEATING_ROWS_REPORT_GRAPCHIC = 
    		"controller/report/findRepeatingRowsReportGraphic.do";
    public static final String FIND_DATA_OBLIGATIONS_REPORT_GRAPCHIC = 
    		"controller/report/findDataObligationsReportGraphic.do";
    public static final String FIND_FINISHED_CONTRACTS_PURCHASES_LIST = 
    		"controller/report/findFinishedContractsPurchasesList.do";
    public static final String FIND_FINISHED_CONTRACTS_OWNERS_LIST = 
    		"controller/report/findFinishedContractsOwnersList.do";

    //ScreenService
    public static final String FIND_SCREEN_BY_STATUS = "controller/screen/findScreenByStatus.do";
    
    //HolidaysUserService
    public static final String FIND_HOLIDAYS_BY_USER = "controller/holidayUser/findHolidaysByUser.do";
    public static final String SAVE_HOLIDAYS_USER = "controller/holidayUser/saveHolidaysUser.do";
    
    //HolidayService
    public static final String FIND_ALL_HOLIDAYS = "controller/holiday/findAll.do";
    public static final String SAVE_HOLIDAYS = "controller/holiday/saveHolidays.do";
    
    //ManagerialDocumentType
    public static final String MANAGERIAL_DOCTYPE_SAVEORUPDATE = "controller/managerialDocumentType/saveOrUpdate.do";
    public static final String MANAGERIAL_DOCTYPE_CHANGE_STATUS = "controller/managerialDocumentType/changeStatus.do";
    public static final String MANAGERIAL_DOCTYPE_FIND_ALL = "controller/managerialDocumentType/findAll.do";
    public static final String MANAGERIAL_DOCTYPE_FIND_BY_STATUS = "controller/managerialDocumentType/findByStatus.do";
    public static final String MANAGERIAL_DOCTYPE_FIND_BY_ID = "controller/managerialDocumentType/findById.do";
    public static final String MANAGERIAL_DOCTYPE_FIND_BY_DOCUMENT_TYPE =  
            "controller/managerialDocumentType/findByDocumentType.do";
    public static final String MANAGERIAL_DOCTYPE_FIND_ALL_CATALOG_PAGED =  
            "controller/managerialDocumentType/findAllManagerialDocumentTypeCatalogPaged.do";
    public static final String MANAGERIAL_DOCTYPE_FIND_TOTAL_PAGES =  
            "controller/managerialDocumentType/returnTotalRowsOfManagerialDocumentType.do";
	
    //Required Documents
    public static final String REQUIRED_DOCUMENT_SAVE_OR_UPDATE = "controller/requiredDocument/saveOrUpdate.do";
    public static final String REQUIRED_DOCUMENT_CHANGE_STATUS = "controller/requiredDocument/changeStatus.do";
    public static final String REQUIRED_DOCUMENT_FIND_ALL = "controller/requiredDocument/findAll.do";
    public static final String REQUIRED_DOCUMENT_FIND_BY_ID = "controller/requiredDocument/findById.do";
    public static final String REQUIRED_DOCUMENT_FIND_BY_STATUS = "controller/requiredDocument/findByStatus.do";
    public static final String REQUIRED_DOCUMENT_FIND_ALL_CATALOG_PAGED =
            "controller/requiredDocument/findAllRequiredDocumentCatalogPaged.do";
    public static final String REQUIRED_DOCUMENT_TOTAL_PAGES = 
            "controller/requiredDocument/returnTotalRowsOfRequiredDocument.do";
    
    public static final String FIND_CONTRACT_DETAIL = "controller/requisition/findContractDetail.do";
    public static final String DELETE_AUTHORIZATION_DOCUMENT = "controller/requisition/deleteAuthorizationDocument.do";
    public static final String DELETE_IMSS_CEDULE = "controller/requisition/deleteImssCeduleFile.do";
    
    //Draft Contract
    public static final String FIND_DECLARATIONS_BY_ID_REQUISITION_DOCUMENT_TYPE_AND_PERSONALITY = 
    		"controller/requisition/findDeclarationsByIdRequisitionDocumentTypeAndPersonality.do";
    public static final String UPDATE_STATUS_DECLARATIONBYREQUISITION_BY_ID_DECLARATION_BY_REQUISITION = 
    		"controller/requisition/updateStatusDeclarationByRequisitionByIdDeclarationByRequisition.do";
    public static final String UPDATE_STATUS_DECLARSUBPARBYREQUISITION_BY_ID_DECLARSUBPARBYREQUISITION = 
    		"controller/requisition/updateStatusDeclarSubparByRequisitionByIdDeclarsubparByRequisition.do";
    
    public static final String FIND_DECLARATIONS_AND_SUBPARAGRAPHS_WITH_CHANGES = 
    		"controller/requisition/findDeclarationsAndSubparagraphsWithChanges.do";
    public static final String FIND_REQUISITIONS_THAT_ARE_USING_DECLARATION = 
    		"controller/requisition/findRequisitionsThatAreUsingDeclaration.do";
    public static final String FIND_REQUISITIONS_THAT_ARE_USING_SUBPARAGRAPH = 
    		"controller/requisition/findRequisitionsThatAreUsingSubparagraph.do";
    public static final String FIND_DECLARATION_FROM_CATALOG_COPY = 
    		"controller/requisition/findDeclarationFromCatalogCopy.do";
    public static final String FIND_SUBPARAGRAPH_FROM_CATALOG_COPY = 
    		"controller/requisition/findSubparagraphFromCatalogCopy.do";
    public static final String UPDATE_DECLARATION_FROM_CATALOG_COPY_NOT_APPLY_CHANGE = 
    		"controller/requisition/updateDeclarationFromCatalogCopyNotApplyChange.do";
    public static final String UPDATE_SUBPARAGRAPH_FROM_CATALOG_COPY_NOT_APPLY_CHANGE = 
    		"controller/requisition/updateSubparagraphFromCatalogCopyNotApplyChange.do";
    public static final String UPDATE_DECLARATION_FROM_CATALOG_COPY_APPLY_CHANGE = 
    		"controller/requisition/updateDeclarationFromCatalogCopyApplyChange.do";
    public static final String UPDATE_SUBPARAGRAPH_FROM_CATALOG_COPY_APPLY_CHANGE = 
    		"controller/requisition/updateSubparagraphFromCatalogCopyApplyChange.do";
    public static final String UPDATE_SUBPARAGRAPH_FROM_CATALOG_COPY_APPLY_CHANGE_ALL = 
    		"controller/requisition/updateSubparagraphFromCatalogCopyApplyChangeAll.do";
    public static final String UPDATE_DECLARATION_FROM_CATALOG_COPY_APPLY_CHANGE_ALL = 
    		"controller/requisition/updateDeclarationFromCatalogCopyApplyChangeAll.do";
    
    // for TrayPendingRequisition
    public static final String DELETE_PENDING_REQUISITIONS =
    		"controller/requisition/deletePendingRequisitions.do";
    
    /*
	 * For Owners
	 */
    //RequisitionOwners
    public static final String FIND_REQUISITION_OWNERS_BY_ID = 
    		"controller/requisitionOwners/findRequisitionOwnersById.do";
    public static final String FIND_CHECKLISTDOCUMENTATION_BY_ID = 
            "controller/requisitionOwners/findCheckListDocumentationById.do";
    public static final String FIND_REQUISITION_OWNER_DOCUMENTS_ATTACHMENT = 
            "controller/requisitionOwners/findDocumentsAttachment.do";
    public static final String FIND_REQUISITION_OWNER_DOCUMENTS_ATTACHMENT_BY_SECTION_TYPE = 
    		"controller/requisitionOwners/findDocumentsAttachmentBySectionType.do";
    public static final String FIND_REQUISITION_OWNER_DIGITALIZATION_BY_SECTION_TYPE = 
            "controller/requisitionOwners/findDigitalizationsBySectionType.do";
    public static final String SERVICE_TEST = "controller/requisitionOwners/save.do";
    public static final String GENERAL_DOWNLOAD_SERVICE = "controller/document/generalDownloadService.do";
    public static final String DOWNLOAD_REMOTE_FILE_SERVICE = "controller/document/downloadRemoteFileService.do";
    public static final String REQUISITION_OWNERS_SAVE_OR_UPDATE = 
    		"controller/requisitionOwners/requisitionOwnersSaveOrUpdate.do";
    public static final String SAVE_LOADING_PROJECT = 
    		"controller/requisitionOwners/saveLoadingProject.do";
    public static final String HAVE_DICTAMINATION_DOCUMENTS = 
            "controller/requisitionOwners/haveDictaminationDocumentsOfRequisitionOwner.do";
    public static final String REJECT_PROJECT_REVIEW = "controller/requisitionOnwers/rejectProjectReview.do";
    public static final String SAVE_USER_PROJECT_REVIEW = "controller/requisitionOnwers/saveUserProjectReview.do";
    public static final String SAVE_DICTAMINATION = "controller/requisitionOwners/saveDictamination.do";
    public static final String SAVE_LAWYER = "controller/requisitionOwners/saveLawyer.do";
    public static final String REJECT_LAWYER_ASSIGMENT = "controller/requisitionOwners/rejectLawyerAssigment.do";
    public static final String SAVE_DECIDER_LAWYER = "controller/requisitionOwners/saveDeciderLawyer.do";
    public static final String SEND_TO_LAWYER = "controller/requisitionOwners/sendToLawyer.do";
    public static final String SAVE_USER_DICTAMEN_VOBO = "controller/requisitionOwners/saveUserDictamentVobo.do";
    public static final String REJECT_DOCUMENT_REVIEW = "controller/requisitionOwners/rejectDocumentReview.do";
    public static final String REJECT_DICTAMINATION = "controller/requisitionOwners/rejectDictamination.do";
    public static final String SAVE_SIGNING_DOCUMENTS = "controller/requisitionOwners/signingDocuments.do";
    public static final String SAVE_LOAD_DIGITALIZATION = "controller/requisitionOwners/saveLoadDigitalization.do";
    public static final String SAVE_SIGN_NOTIFICATION = "controller/requisitionOwners/saveSignNotification.do";
    public static final String SAVE_SIGN_NOTIFICATION_REVIEW = 
            "controller/requisitionOwners/saveSignNotificationReview.do";
    public static final String FIND_DICTAMINATION_VERSIONS = 
            "controller/requisitionOwners/findDictaminationVersionsByIdRequisition.do";
    public static final String FIND_REQUISITIONOWNERS_DOCUMENTS_GUARANTEES_AND_CONTRACTS = 
            "controller/requisitionOwners/findRequisitionOwnerDocumentsGuaranteesAndContracts.do";
    public static final String REQUISITIONOWNERS_FIND_COMMENTS_BY_ID_REQUISITIONOWNERS_FLOW_STATUS_AND_COMMENT_TYPE = 
            "controller/requisitionOwners/findCommentOwnerByIdRequisitionOwnerFlowStatusAndCommentType.do";
    public static final String REQUISITIONOWNERS_FIND_COMMENT_DOCUMENT = 
            "controller/requisitionOwners/findCommentDocument.do";
    public static final String SAVE_REQUISITIONOWNERS_GUARANTEE_CHECK_DOCUMENT = 
            "controller/requisitionOwners/saveRequisitionOwnerGuaranteeCheckDocument.do";
    public static final String FIND_REQUISITIONOWNERS_VERSIONS = 
            "controller/requisitionOwners/findOwnersVersionsByIdRequisitionOwner.do";
    public static final String FIND_REQUISITIONOWNERS_BY_VERSION = 
            "controller/requisitionOwners/findRequisitionOwnersByVersion.do";
    public static final String FIND_ATTACHMENTS_BY_VERSION = "controller/requisitionOwners/findAttachmentsByVersion.do";
    public static final String FIND_CHECK_DOCUMENTATION_BY_VERSION = 
            "controller/requisitionOwners/findCheckDocumentationByVersion.do";
    public static final String CANCEL_REQUISITION_OWNERS_CONTRACT =
            "controller/requisitionOwners/cancelContract.do";
    public static final String FIND_PAGINATED_OWNERS_CONTRACTS =
            "controller/requisitionOwners/findPaginatedContracts.do";
    public static final String TOTAL_PAGES_SHOW_PAGINATED_OWNERS_CONTRACTS =
            "controller/requisitionOwners/findTotalPagesPaginatedContracts.do";
    public static final String FIND_PAGINATED_OWNERS_REQUISITIONS =
            "controller/requisitionOwners/findPaginatedRequisitions.do";
    public static final String TOTAL_PAGES_SHOW_PAGINATED_OWNERS_REQUISITIONS =
            "controller/requisitionOwners/findTotalPagesPaginatedRequistions.do";
    public static final String UPDATE_MANAGEMENT_INFO = "controller/requisitionOwners/updateManagementInfo.do";
    public static final String CANCEL_REQUISITION_OWNERS_REQUISITION_BY_ID =
            "controller/requisitionOwners/cancelRequisitionById.do";
    public static final String FIND_PAGINATED_OWNERS_CONTRACTS_TO_EXPIRE =
            "controller/requisitionOwners/findPaginatedContractsToExpire.do";
    public static final String TOTAL_PAGES_PAGINATED_OWNERS_CONTRACTS_TO_EXPIRE =
            "controller/requisitionOwners/findTotalPagesPaginatedContractsToExpire.do";
    public static final String UPDATE_IS_EXPIRED_ATTENDED = "controller/requisitionOwners/updateIsExpiredAttended.do";
    public static final String FIND_OWNERS_CONTRACT_CANCELLATION_COMMENT =
            "controller/requisitionOwners/findContractsCancelationComment.do";
    public static final String FIND_OWNERS_CONTRACT_DETAIL = 
    		"controller/requisitionOwners/findOwnersContractDetail.do";
    
    //CustomerService
    public static final String CUSTOMER_SAVE_OR_UPDATE = "controller/customer/saveOrUpdate.do";
    public static final String CUSTOMER_CHANGE_STATUS = "controller/customer/changeStatus.do";
    public static final String CUSTOMER_FIND_BY_STATUS = "controller/customer/findByStatus.do";
    public static final String CUSTOMER_FIN_ALL = "controller/customer/findAll.do";
    public static final String CUSTOMER_FIND_BY_ID = "controller/customer/findById.do";
    public static final String CUSTOMER_FIND_BY_COMPANY_NAME_OR_RFC = "controller/customer/findByCompanyNameOrRfc.do";
    public static final String CUSTOMER_FIND_ALL_CATALOG_PAGED = "controller/customer/findAllCustomerCatalogPaged.do";
    public static final String CUSTOMER_FIND_TOTAL_PAGES = "controller/customer/returnTotalRowsOfCustomer.do";
    
    //EnterpriseDocumentReviewController
    
    //CheckDocumentationService
    public static final String FIND_CHECKLIST_DOCUMENTATION_BY_CATEGORY = 
            "controller/checkDocumentation/findCheckDocumentationByCategory.do";
    public static final String CHECKLIST_DOCUMENTATION_SAVE_OR_UPDATE = "controller/checkDocumentation/saveOrUpdate.do";
    public static final String CHECKLIST_DOCUMENTATION_CHANGE_STATUS = "controller/checkDocumentation/changeStatus.do";
    public static final String CHECKLIST_DOCUMENTATION_FIND_BY_STATUS = "controller/checkDocumentation/findByStatus.do";
    public static final String CHECKLIST_DOCUMENTATION_FIND_BY_ID = "controller/checkDocumentation/findById.do";
    public static final String CHECKLIST_DOCUMENTATION_FIND_ALL = "controller/checkDocumentation/findAll.do";
    public static final String CHECKLIST_DOCUMENTATION_FIND_ALL_CATALOG_PAGED = 
            "controller/checkDocumentation/findAllCheckDocumentationCatalogPaged.do";
    public static final String CHECKLIST_DOCUMENTATION_FIND_TOTAL_PAGES =
            "controller/checkDocumentation/returnTotalRowsOfCheckDocumentation.do";
    
    //CatalogDeclarationsController
    public static final String DECLARATIONS_FIND_ALL = "controller/declarations/declarationsFindAll.do";
    public static final String DECLARATIONS_FIND_BY_ID = "controller/declarations/declarationsFindById.do";
    public static final String DECLARATIONS_FIND_BY_STATUS = "controller/declarations/declarationsFindByStatus.do";
    public static final String DECLARATION_CHANGE_STATUS = "controller/declarations/declarationsChangeStatus.do";
    public static final String DECLARATION_SAVE_OR_UPDATE = "controller/declarations/declarationsSaveOrUpdate.do";
    public static final String DECLARATION_FIND_ACTIVE = "controller/declarations/declarationFindActive.do";
    public static final String FIND_SUBPARAGRAPCH_BY_ID_DECLARATION = 
    		"controller/declarations/findSubparagrapchByIdDeclaration.do";
    public static final String SUBPARAGRAPCH_CHANGE_STATUS = 
    		"controller/declarations/subparagrapchChangeStatus.do";
    public static final String SUBPARAGRAPCH_CHANGE_DESCRIPTION = 
    		"controller/declarations/subparagrapchChangeDescription.do";
    public static final String FINANCIAL_ENTITIES_BY_ID_DECLARATION = 
    		"controller/declarations/financialEntitiesByIdDeclaration.do";
//    public static final String FIND_FINANCIAL_ENTITIES_DECLARATIONS_AND_SUBPARAGRAPHS_BY_ID_REQUISITION = 
//    		"controller/declarations/findFinancialEntitiesDeclarationsAndSubparagraphsByIdRequisition.do";
//    public static final String ALL_FINANCIAL_ENTITIES_DECLARATIONS_SUBPARAGRAPHS_BY_ID_REQUISITION = 
//    		"controller/declarations/allFinancialEntitiesDeclarationsSubparagraphsByIdRequisition.do";
    public static final String DECLARATION_FIND_ACTIVE_PERSONALITIES =
            "controller/declarations/findActivePersonalities.do";
    public static final String DECLARATION_FIND_ACTIVE_DOCUMENT_TYPES =
            "controller/declarations/findActiveDocumentTypes.do";
    public static final String DECLARATION_FIND_ALL_CATALOG_PAGED =
            "controller/declarations/findAllDeclarationCatalogPaged.do";
    public static final String DECLARATION_FIND_TOTAL_PAGES =
            "controller/declarations/returnTotalRowsOfDeclaration.do";
    
    //CatalogExportService
    public static final String FIND_ALL_CATALOG_EXPORT = "controller/catalogExport/findAllCatalogExport.do";
    public static final String DELETE_ALL_CATALOG_EXPORT = "controller/catalogExport/deleteAllCatalogExportRecords.do";
    public static final String SAVE_CATALOG_EXPORT_INITIALIZATION_SAVE = 
    			"controller/catalogExport/saveCatalogExportInitializationSave.do";
    public static final String EXPORT_CATALOGS = "controller/catalogExport/exportCatalogs.do";
    public static final String STOP_EXPORTATION_CATALOGS = "controller/catalogExport/stopExportationCatalogs.do";
    public static final String DELETE_ALL_CATALOGS_EXPORT_AND_FILES = 
    		"controller/catalogExport/deleteAllCatalogsExportAndFiles.do";
    public static final String DOWNLOAD_EXPORT_CATALOGS = "controller/catalogExport/downloadExportCatalogs.do";
	public static final String SAVE_AND_SEND_CONTRACT_TO_DIGITIZE = 
			"controller/requisition/saveAndSendContractToDigitize.do";
	
	// Debugging - please do not remove, usefull when adding debugging code - Luis Virueña.
	public static final String GENERAL_DEBUG_REQUEST = "controller/requisition/generalDebugging.do";
	
	public static final String OBTENER_SOLICITUDES_PENDIENTES ="controller/requisition/obtenerSolicitudesPendientesPorEnviar.do";
	public static final String DESCARGAR_BORRADOR_CONTRATO_PDF = "controller/requisition/obtenerBorradorContratoPDF.do";
	
	public static final String ENVIAR_VOBO_JURIDICO = "controller/requisition/enviarVoBoJuridico.do";
    public static final String RECHAZAR_VOBO_JURIDICO = "controller/requisition/rechazarVoBoJuridico.do";
    
    public static final String OBTENER_MENUS_POR_PERFIL =  "controller/profile/obtenerMenuPorPerfil.do";
    public static final String GUARDAR_IDENTIFICADOR_DEL_FLUJO =  "controller/profile/establecerIdentificadorFlujo.do";
    
    //NoticeService
    public static final String ACTIVE_NOTIFICATIONS_BY_USER = "controller/notification/findActiveNotificationsByUser.do";

    
    //CatalogdocumentTyoe
    public static final String DOCUMENT_TYPE_FIND_ALL ="controller/catDocumentType/findAll.do";

    public static final String UPDATE_NOTIFICATION_BY_ID = "controller/notification/updateStatusNotificacionById.do";
    public static final String SEND_NOTIFICATION_BY_USER_AND_STEP = "controller/notification/sendNotificationByUserAndStep.do";
    public static final String FIND_ALL_COMMENTS_BY_IDREQUISITION = "controller/comment/findAllCommentsByIdRequisition.do";
    public static final String FIND_COMMENTS_BY_IDREQUISITION_FLOWSTATUS_TYPE = "controller/comment/findByIdRequisitionAndFlowStatusAndCommentType.do";
    public static final String FIND_DOCUMENTS_ATTACHMENT_DTO = "controller/requisition/findDocumentsAttachmentDTO.do";
    public static final String SAVE_DOCUMENT_ATTACHMENT_DTO = "controller/requisition/saveDocumentAttachmentDTO.do";
    public static final String SEND_PRINT_CONTRACT = "controller/requisition/sendPrintContract.do";
    public static final String FIND_VERSIONS_CONTRACT_DTO = "controller/requisition/findContractVersionDTO.do";
    
    //Reportes
    public static final String DIAS_ATTENCION_POR_BANDEJA = "controller/grafica/graficaDeTiempoEnBandeja.do";
    public static final String TOTAL_SOLICITUDES_GRAFICA = "controller/grafica/graficaTotalSolicitudesEnPasos.do";
    public static final String GRAFICA_DE_STATUS = "controller/reportes/graficaStatus.do";
    public static final String GRAFICA_DE_AREA = "controller/reportes/graficaPastelArea.do";
    public static final String GRAFICA_TIEMPOS_DE_BARRAS = "controller/reportes/graficaBarrasTiempos.do";
    public static final String CONTRATOS_CERRADOS_POR_MES = "controller/reportes/contratosCerradosPorMes.do";
    public static final String LISTA_PARA_FILTROS ="controller/reportes/getListaFiltro.do";
    public static final String DATOS_GRAFICA_FILTROS ="controller/reportes/getDatosGraficaFiltros.do";
    public static final String REPORTE_EXCEL_SOLICITUDES ="controller/reportes/reporteExcelSolicitudes.do";
    public static final String REPORTE_EXCEL_TIEMPO_POLITICA_FECHAS ="controller/reportes/reporteExcelTiempoPoliticaFechas.do";
    public static final String DOCUMENT_VALIDATE_DATE = "controller/document/validateDate.do";
    public static final String GENERA_EXCEL_FINALIZADOS ="controller/reportes/generaExcelFinalizados.do";
    public static final String GENERA_EXCEL_SOLICITUDES ="controller/reportes/generaExcelSolicitudes.do";
    public static final String GENERA_EXCEL_GRAFICA_TOTAL_SOLICITUDES ="controller/reportes/generaExcelGraficaSolicitudes.do";
    public static final String GENERA_EXCEL_GRAFICA_TOTAL_SOLICITUDES_CERRADAS_ANIO ="controller/reportes/generaExcelGraficaSolicitudesCerradas.do";
    public static final String GENERA_EXCEL_AREA_SELECCIONADA_PASTEL ="controller/reportes/generaExcelAreaSeleccionada.do";
    public static final String GENERA_EXCEL_TIEMPO_POLITICA ="controller/reportes/generaExcelTiempoPolitica.do";
    public static final String DESCARGAR_REPORTE_EXCEL ="controller/reportes/descargarReporteExcel.do";
    public static final String GENERAR_REPORTE_EXCEL_GRAFICA ="controller/reportes/generarReporteExcelGrafica.do";
    public static final String GENERAR_REPORTE_EXCEL_GRAFICA_PASTEL_AREAS ="controller/reportes/generarReporteExcelGraficaPastelAreas.do";
    
    // Unit
    public static final String SAVE_OR_UPDATE_UNIT = "controller/unit/saveOrUpdateUnit.do";
    public static final String FIND_UNIT_BY_ID = "controller/unit/findUnitById.do";
    public static final String FIND_ALL_UNITS = "controller/unit/findAllUnits.do";
    public static final String DELETE_UNIT = "controller/unit/deleteUnit.do";
    
    //Contratos Celebrados
    public static final String GET_LIST_DEAL_END="controller/contratosCelebrados/obtenerContratosCelebrados.do";
    public static final String GET_LIST_ALL_DEAL_END="controller/contratosCelebrados/obtenerTotalContratosCelebrados.do";
    public static final String DOWNLOAD_CONTRACT_ZIP="controller/contratosCelebrados/downloasRar.do";
    
    public static final String GET_IDS_SUPPLIERPERSON_BY_IDREQUISITION = "controller/requisition/getIdsSupplierPersonByIdRequisition.do";
    public static final String FIND_SUPPLIER_PERSON_BY_REQUISITION = "controller/supplier/findLegalRepresentativesByIdRequisition.do"; 
    
    
    public static final String FIND_SUPPLIER_LAWYERS_BY_SUPPLIER_ACTIVE =  "controller/supplier/findLegalRepresentativesByIdSupplieraActive.do";
    public static final String FIND_SUPPLIER_WITNESSES_BY_SUPPLIER_ACTIVE =  "controller/supplier/findWitnessesByIdSupplierAndTypeActive.do";

    public static final String SAVE_REDFLAG ="controller/redFlag/addRedFlag.do";
    public static final String FIND_REDFLAG ="controller/redFlag/findReflags.do";
    
    public static final String GET_REQUISITION_STATUS ="controller/requisitionStatusTurn/getRequisitionStatus.do";
    public static final String REQUEST_ONLY_UPDATE_DRAFT = "controller/requisition/requestOnlyUpdateDraft.do";
    
    public static final String SAVE_TYPE_ANEXO ="controller/anexo/addTypeByAnexo.do";
    public static final String DELETE_TYPE_ANEXO ="controller/anexo/deleteTypeByAnexo.do";
    public static final String FIND_TYPES_ANEXOS ="controller/anexo/findTypesByAnexos.do";
    public static final String FIND_REQUISITION_BY_IDDOCUMENTTYPE ="controller/anexo/findRequisitionByIdDocumentType.do";
    public static final String FIND_TAGS_ANEXOS = "controller/anexo/findTagsAnexos.do";
 // Digital Signature
 	public static final String DS_GET_USER_INFORMATION = "controller/digitalSignature/getUserInformation.do";
 	public static final String DS_UPDATE_USER_INFORMATION = "controller/digitalSignature/updateUserInformation.do";
 	public static final String DS_GET_ALL_CONTACTS = "controller/digitalSignature/getAllContacts.do";
 	public static final String DS_GET_CONTACTS = "controller/digitalSignature/getContacts.do";
 	public static final String DS_SAVE_UPDATE_CONTACT = "controller/digitalSignature/saveUpdateContact.do";
 	public static final String DS_DELETE_CONTACT = "controller/digitalSignature/deleteContact.do";
 	public static final String DS_VALIDATE_ONLY_SIGNER = "controller/digitalSignature/validateOnlySigner.do";
 	public static final String DS_SEND_DOCUMENT = "controller/digitalSignature/sendDocument.do";
 	public static final String DS_STATUS_DOCUMENT = "controller/digitalSignature/statusDocument.do";
 	public static final String DS_STATUS_DIGITAL_SIGNATURE_DOCUMENT = "controller/digitalSignature/statusDigitalSignatureDocument.do";
 	public static final String DS_STATUS_RECIPIENT_SIGNED_DOCUMENT = "controller/digitalSignature/statusRecipienSignedDocument.do";
 	public static final String DS_DELETE_SIGNED_DOCUMENT = "controller/digitalSignature/deleteSignedDocument.do";
 	public static final String DS_POSITION_SIGNED_DOCUMENT = "controller/digitalSignature/positionSignedDocument.do";
 	public static final String DS_REENVIAR_SIGNED_DOCUMENT = "controller/digitalSignature/reenvioEnvelope.do";
 	public static final String DS_CORRECT_SIGNED_DOCUMENT = "controller/digitalSignature/correccionEnvelope.do";
 	public static final String DS_VIEW_SIGNED_DOCUMENT = "controller/digitalSignature/viewEnvelopeSigned.do";
 	public static final String DS_SET_RECIPIENT_SIGNED = "controller/digitalSignature/setRecipientSigned.do";
 	public static final String DS_GET_USER_DOCUMENTS = "controller/digitalSignature/getUserDocuments.do";
 	public static final String DS_RESEND_INVITATION_EMAIL = "controller/digitalSignature/resendInvitationEmail.do";
 	public static final String DS_VALIDATE_SECRET_CODE_RECIPIENT = "controller/digitalSignature/validateSecretCodeRecipient.do";
 	public static final String DS_VIEW_DOCUMENT = "controller/digitalSignature/viewDocument.do";
 	public static final String DS_DOWNLOAD_SENT_DOCUMENT = "controller/digitalSignature/downloadSentDocument.do";
 	public static final String DS_DOWNLOAD_SIGNED_DOCUMENT = "controller/digitalSignature/downloadSignedDocument.do";
 	public static final String DS_SAVE_SIGNED_DOCUMENT = "controller/digitalSignature/saveSignedDocument.do";
 	public static final String DS_DELETE_DOCUMENT = "controller/digitalSignature/deleteDocument.do";
 	// Digital Signature Contract
 	public static final String DS_VALIDATE_DRAFT_DOCUSIGN = "controller/digitalSignature/validateDraftDocusign.do";
 	public static final String DS_GET_DOCUMENT_BY_ID_REQUISITION = "controller/digitalSignature/getDocumentByIdRequisition.do";
 	public static final String DS_DELETE_DOCUMENT_BY_ID = "controller/digitalSignature/deleteDocumentById.do";
 	public static final String DS_VIEW_DOCUMENT_CONTRACT = "controller/digitalSignature/viewDocumentContract.do";
 	public static final String DS_GET_DOCUMENT_INFORMATION = "controller/digitalSignature/getDocumentInformation.do";
 	public static final String DS_GET_SIGNATURE_OPTION = "controller/digitalSignature/getSignatureOption.do";
 	public static final String DS_DOCUMENT_EXTENSION = "controller/digitalSignature/documentDetailExtension.do";
 	public static final String DS_SEND_DOCUMENT_TO_PROVIDER = "controller/digitalSignature/sendDocumentToProvider.do";

	// EviSign send document
	public static final String EVI_SEND_DOCUMENT = "controller/digitalSignature/sendDocumentToEviSign.do";
	public static final String GET_LAST_ID_EMPLEADO = "controller/empleado/obtenerUltimoIdEmpleado.do";
	




}
