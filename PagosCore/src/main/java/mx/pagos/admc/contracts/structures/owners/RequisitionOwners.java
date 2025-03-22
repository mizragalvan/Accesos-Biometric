package mx.pagos.admc.contracts.structures.owners;

import java.util.ArrayList;
import java.util.List;

import mx.pagos.admc.contracts.structures.FileUploadInfo;
import mx.pagos.admc.contracts.structures.FlowScreenAction;

public class RequisitionOwners {
    private Integer idRequisitionOwners;
    private Integer idRequisitionOwnersVersion;
    private Integer idLawyer;
    private Integer idCustomer;
    private Integer idCategory;
    private Integer idCurrency;
    private Integer idBusinessman;
    private Integer idOrganizationEntity; 
    private Integer idCategoryCheckDocumentation;
    private Integer idDictaminationTemplate;
    private Integer idDeciderLawyer;
    private Integer idDocumentType;
    private Integer idPublicBroker;
    private Integer idPublicNotary;
    private Integer idLawyerVobo;
    private Integer creditResolutionNumber;
    private Integer amount;
    private Integer deedNumber;
    private Integer idFlow;
    private Integer idUserDictamenVoBo;
    private Integer idUserProjectReviewVoBo;
    private Integer idUserSignVoBo;
    private String idSession;
    private String categoryName;
    private String currencyName;
    private String organizationEntityName;
    private String jurisdiction;
    private String creditTypeProduct;
    private String documentType;
    private String publicBroker;
    private String publicNotary;
    private String endDate;
    private String startDate;
    private String lawyerVobo;
    private String terms;
    private String customerCompanyName;
    private String customerRfc;
    private String userProjectReviewName;
    private String lawyerAssigmentName;
    private Integer totalDocumentsProjects;
    private Integer totalDocumentsGuarantees;
    private Boolean isPublicNotaryDelivered;
    private Boolean isVerifiedReceivedContract;
    private Boolean isPrivateFormalization;
    private Boolean isPublicNotaryRatified;
    private Boolean isPublicRedordEnmrolled;
    private Boolean isFinishProcess;
    private FileUploadInfo dictumTemplateInfo;
    private CommentOwner commentOwner = new CommentOwner();
    private List<FileUploadInfo> loadProjectDocumentsList = new ArrayList<FileUploadInfo>();
    private List<FileUploadInfo> documentationDocumentsList = new ArrayList<FileUploadInfo>();
    private List<FileUploadInfo> loadGuaranteesDocumentsList = new ArrayList<FileUploadInfo>();
    private List<FileUploadInfo> dictaminationDocumentsList = new ArrayList<FileUploadInfo>();
    private List<FileUploadInfo> digitalizationInscriptionList = new ArrayList<FileUploadInfo>();
    private List<Integer> checkListDocumentationIds = new ArrayList<Integer>();
    private FlowScreenAction flowScreenAction;
    private Boolean isStandarized;
    private String dateFirstProject;
    private Boolean isExpiredAttended;
    private String applicationDate;
    private Boolean isRequisitionCancelled;
    private Boolean isContractCancelled;
    private Integer beforeDaysExpirationAlert;
    private Integer afterDaysExpirationAlert;
    private String dateValue;
    private String businesmanName;
    private String businessManEMail;
    
	public final Integer getIdRequisitionOwners() {
		return this.idRequisitionOwners;
	}
	
	public final void setIdRequisitionOwners(final Integer idRequisitionOwnersParameter) {
		this.idRequisitionOwners = idRequisitionOwnersParameter;
	}
	
	public final Integer getIdLawyer() {
		return this.idLawyer;
	}
	
	public final void setIdLawyer(final Integer idLawyerParameter) {
		this.idLawyer = idLawyerParameter;
	}
	
	public final Integer getIdCustomer() {
		return this.idCustomer;
	}
	
	public final void setIdCustomer(final Integer idCustomerParameter) {
		this.idCustomer = idCustomerParameter;
	}
	
	public final Integer getIdCategory() {
		return this.idCategory;
	}
	
	public final void setIdCategory(final Integer idCategoryParameter) {
		this.idCategory = idCategoryParameter;
	}
	
	public final Integer getIdCurrency() {
		return this.idCurrency;
	}
	
	public final void setIdCurrency(final Integer idCurrencyParameter) {
		this.idCurrency = idCurrencyParameter;
	}
	
	public final Integer getIdBusinessman() {
		return this.idBusinessman;
	}
	
	public final void setIdBusinessman(final Integer idBusinessmanParameter) {
		this.idBusinessman = idBusinessmanParameter;
	}
	
	
    public final Integer getIdOrganizationEntity() {
        return this.idOrganizationEntity;
    }

    public final void setIdOrganizationEntity(final Integer idOrganizationEntityParameter) {
        this.idOrganizationEntity = idOrganizationEntityParameter;
    }

    public final String getCurrencyName() {
        return this.currencyName;
    }

    public final void setCurrencyName(final String currencyNameParameter) {
        this.currencyName = currencyNameParameter;
    }

    public final String getCategoryName() {
        return this.categoryName;
    }

    public final void setCategoryName(final String documentTypeNameParameter) {
        this.categoryName = documentTypeNameParameter;
    }

    public final Integer getIdCategoryCheckDocumentation() {
        return this.idCategoryCheckDocumentation;
    }

    public final void setIdCategoryCheckDocumentation(final Integer idCategoryCheckDocumentationParameter) {
        this.idCategoryCheckDocumentation = idCategoryCheckDocumentationParameter;
    }

    public final Integer getIdDeciderLawyer() {
        return this.idDeciderLawyer;
    }

    public final void setIdDeciderLawyer(final Integer idDeciderLawyerParameter) {
        this.idDeciderLawyer = idDeciderLawyerParameter;
    }
    
    public final CommentOwner getCommentOwner() {
        return this.commentOwner;
    }
    
    public final void setCommentOwner(final CommentOwner commentOwnerParameter) {
        this.commentOwner = commentOwnerParameter;
    }

    public final FileUploadInfo getDictumTemplateInfo() {
        return this.dictumTemplateInfo;
    }

    public final void setDictumTemplateInfo(final FileUploadInfo dictumTemplateInfoParameter) {
        this.dictumTemplateInfo = dictumTemplateInfoParameter;
    }

    public final String getIdSession() {
        return this.idSession;
    }

    public final void setIdSession(final String idSessionParameter) {
        this.idSession = idSessionParameter;
    }

    public final Integer getIdDictaminationTemplate() {
        return this.idDictaminationTemplate;
    }

    public final void setIdDictaminationTemplate(final Integer idDictaminationTemplateParameter) {
        this.idDictaminationTemplate = idDictaminationTemplateParameter;
    }
    
    public final List<FileUploadInfo> getLoadProjectDocumentsList() {
		return this.loadProjectDocumentsList;
	}

	public final void setLoadProjectDocumentsList(final List<FileUploadInfo> loadProjectDocumentsListParameter) {
		this.loadProjectDocumentsList = loadProjectDocumentsListParameter;
	}

	public final List<FileUploadInfo> getDocumentationDocumentsList() {
		return this.documentationDocumentsList;
	}

    public final void setDocumentationDocumentsList(final List<FileUploadInfo> documentationDocumentsListParameter) {
		this.documentationDocumentsList = documentationDocumentsListParameter;
	}

	public final List<FileUploadInfo> getLoadGuaranteesDocumentsList() {
		return this.loadGuaranteesDocumentsList;
	}

    public final void setLoadGuaranteesDocumentsList(final List<FileUploadInfo> loadGuaranteesDocumentsListParameter) {
		this.loadGuaranteesDocumentsList = loadGuaranteesDocumentsListParameter;
	}

	public final List<Integer> getCheckListDocumentationIds() {
		return this.checkListDocumentationIds;
	}

	public final void setCheckListDocumentationIds(final List<Integer> checkListDocumentationIdsParameter) {
		this.checkListDocumentationIds = checkListDocumentationIdsParameter;
	}
	
	public final List<FileUploadInfo> getDictaminationDocumentsList() {
        return this.dictaminationDocumentsList;
    }

	public final void setDictaminationDocumentsList(final
	        List<FileUploadInfo> dictaminationDocumentsListParameter) {
	    this.dictaminationDocumentsList = dictaminationDocumentsListParameter;
	}

    public final String getJurisdiction() {
		return this.jurisdiction;
	}

	public final void setJurisdiction(final String jurisdictionParameter) {
		this.jurisdiction = jurisdictionParameter;
	}

    public final String getOrganizationEntityName() {
        return this.organizationEntityName;
    }

    public final void setOrganizationEntityName(final String organizationEntityNameParameter) {
        this.organizationEntityName = organizationEntityNameParameter;
    }

    public final List<FileUploadInfo> getDigitalizationInscriptionList() {
        return this.digitalizationInscriptionList;
    }

    public final void setDigitalizationInscriptionList(
            final List<FileUploadInfo> digitalizationInscriptionListParameter) {
        this.digitalizationInscriptionList = digitalizationInscriptionListParameter;
    }

    public final Boolean getIsFinishProcess() {
        return this.isFinishProcess;
    }

    public final void setIsFinishProcess(final Boolean isFinishProcessParameter) {
        this.isFinishProcess = isFinishProcessParameter;
    }

    public final Integer getIdDocumentType() {
        return this.idDocumentType;
    }

    public final void setIdDocumentType(final Integer idDocumentTypeParamter) {
        this.idDocumentType = idDocumentTypeParamter;
    }

    public final Integer getIdPublicBroker() {
        return this.idPublicBroker;
    }

    public final void setIdPublicBroker(final Integer idPublicBrokerPameter) {
        this.idPublicBroker = idPublicBrokerPameter;
    }

    public final Integer getIdPublicNotary() {
        return this.idPublicNotary;
    }

    public final void setIdPublicNotary(final Integer idPublicNotaryParameter) {
        this.idPublicNotary = idPublicNotaryParameter;
    }
    
    public final Integer getIdLawyerVobo() {
        return this.idLawyerVobo;
    }

    public final void setIdLawyerVobo(final Integer idLawyerVoboParameter) {
        this.idLawyerVobo = idLawyerVoboParameter;
    }
    
    public final Integer getCreditResolutionNumber() {
        return this.creditResolutionNumber;
    }

    public final void setCreditResolutionNumber(final Integer creditResolutionNumberParameter) {
        this.creditResolutionNumber = creditResolutionNumberParameter;
    }

    public final Integer getAmount() {
        return this.amount;
    }

    public final void setAmount(final Integer amountParameter) {
        this.amount = amountParameter;
    }

    public final Integer getDeedNumber() {
        return this.deedNumber;
    }

    public final void setDeedNumber(final Integer deedNumberParameter) {
        this.deedNumber = deedNumberParameter;
    }

    public final String getCreditTypeProduct() {
        return this.creditTypeProduct;
    }

    public final void setCreditTypeProduct(final String creditTypeProductParameter) {
        this.creditTypeProduct = creditTypeProductParameter;
    }

    public final String getDocumentType() {
        return this.documentType;
    }

    public final void setDocumentType(final String documentTypeParameter) {
        this.documentType = documentTypeParameter;
    }

    public final String getPublicBroker() {
        return this.publicBroker;
    }

    public final void setPublicBroker(final String publicBrokerParameter) {
        this.publicBroker = publicBrokerParameter;
    }

    public final String getPublicNotary() {
        return this.publicNotary;
    }

    public final void setPublicNotary(final String publicNotaryParameter) {
        this.publicNotary = publicNotaryParameter;
    }

    public final String getEndDate() {
        return this.endDate;
    }

    public final void setEndDate(final String endDateParameter) {
        this.endDate = endDateParameter;
    }

    public final String getStartDate() {
        return this.startDate;
    }

    public final void setStartDate(final String startDateParameter) {
        this.startDate = startDateParameter;
    }

    public final Boolean getIsPublicNotaryDelivered() {
        return this.isPublicNotaryDelivered;
    }

    public final void setIsPublicNotaryDelivered(final Boolean isPublicNotaryDeliveredParameter) {
        this.isPublicNotaryDelivered = isPublicNotaryDeliveredParameter;
    }

    public final Boolean getIsVerifiedReceivedContract() {
        return this.isVerifiedReceivedContract;
    }

    public final void setIsVerifiedReceivedContract(final Boolean isVerifiedReceivedContractParameter) {
        this.isVerifiedReceivedContract = isVerifiedReceivedContractParameter;
    }

    public final Boolean getIsPrivateFormalization() {
        return this.isPrivateFormalization;
    }

    public final void setIsPrivateFormalization(final Boolean isPrivateFormalizationParameter) {
        this.isPrivateFormalization = isPrivateFormalizationParameter;
    }

    public final Boolean getIsPublicNotaryRatified() {
        return this.isPublicNotaryRatified;
    }

    public final void setIsPublicNotaryRatified(final Boolean isPublicNotaryRatifiedParameter) {
        this.isPublicNotaryRatified = isPublicNotaryRatifiedParameter;
    }

    public final Boolean getIsPublicRedordEnmrolled() {
        return this.isPublicRedordEnmrolled;
    }

    public final void setIsPublicRedordEnmrolled(final Boolean isPublicRedordEnmrolledParameter) {
        this.isPublicRedordEnmrolled = isPublicRedordEnmrolledParameter;
    }
    
    public final String getLawyerVobo() {
        return this.lawyerVobo;
    }

    public final void setLawyerVobo(final String lawyerVoboParameter) {
        this.lawyerVobo = lawyerVoboParameter;
    }

    public final String getTerms() {
        return this.terms;
    }
    
    public final void setTerms(final String termsParameter) {
        this.terms = termsParameter;
    }

    public final Integer getTotalDocumentsProjects() {
        return this.totalDocumentsProjects;
    }

    public final void setTotalDocumentsProjects(final Integer totalDocumentsProjectsParameter) {
        this.totalDocumentsProjects = totalDocumentsProjectsParameter;
    }

    public final Integer getTotalDocumentsGuarantees() {
        return this.totalDocumentsGuarantees;
    }

    public final void setTotalDocumentsGuarantees(final Integer totalDocumentsGuaranteesParameter) {
        this.totalDocumentsGuarantees = totalDocumentsGuaranteesParameter;
    }

    public final Integer getIdFlow() {
        return this.idFlow;
    }

    public final void setIdFlow(final Integer idFlowParameter) {
        this.idFlow = idFlowParameter;
    }

    public final FlowScreenAction getFlowScreenAction() {
        return this.flowScreenAction;
    }

    public final void setFlowScreenAction(final FlowScreenAction flowScreenActionParameter) {
        this.flowScreenAction = flowScreenActionParameter;
    }

    public final Integer getIdUserDictamenVoBo() {
        return this.idUserDictamenVoBo;
    }

    public final void setIdUserDictamenVoBo(final Integer idUserDictamenVoBoParameter) {
        this.idUserDictamenVoBo = idUserDictamenVoBoParameter;
    }

    public final Integer getIdUserProjectReviewVoBo() {
        return this.idUserProjectReviewVoBo;
    }

    public final void setIdUserProjectReviewVoBo(final Integer idUserProjectReviewVoBoParameter) {
        this.idUserProjectReviewVoBo = idUserProjectReviewVoBoParameter;
    }

    public final Integer getIdUserSignVoBo() {
        return this.idUserSignVoBo;
    }

    public final void setIdUserSignVoBo(final Integer idUserSignVoBoParameter) {
        this.idUserSignVoBo = idUserSignVoBoParameter;
    }

    public final String getCustomerCompanyName() {
        return this.customerCompanyName;
    }

    public final void setCustomerCompanyName(final String customerCompanyNameParameter) {
        this.customerCompanyName = customerCompanyNameParameter;
    }

    public final String getCustomerRFC() {
        return this.customerRfc;
    }

    public final void setCustomerRFC(final String customerRfcParameter) {
        this.customerRfc = customerRfcParameter;
    }

    public final String getUserProjectReviewName() {
        return this.userProjectReviewName;
    }

    public final void setUserProjectReviewName(final String userProjectReviewNameParameter) {
        this.userProjectReviewName = userProjectReviewNameParameter;
    }

    public final String getLawyerAssigmentName() {
        return this.lawyerAssigmentName;
    }

    public final void setLawyerAssigmentName(final String lawyerAssigmentNameParameter) {
        this.lawyerAssigmentName = lawyerAssigmentNameParameter;
    }

    public final Integer getIdRequisitionOwnersVersion() {
        return this.idRequisitionOwnersVersion;
    }

    public final void setIdRequisitionOwnersVersion(final Integer idRequisitionOwnersVersionParameter) {
        this.idRequisitionOwnersVersion = idRequisitionOwnersVersionParameter;
    }

	public final Boolean getIsStandarized() {
		return this.isStandarized;
	}

	public final void setIsStandarized(final Boolean isStandarizedParameter) {
		this.isStandarized = isStandarizedParameter;
	}
	
	public final String getDateFirstProject() {
		return this.dateFirstProject;
	}
	
	public final void setDateFirstProject(final String dateFirstProjectParameter) {
		this.dateFirstProject = dateFirstProjectParameter;
	}

    public final Boolean getIsExpiredAttended() {
        return this.isExpiredAttended;
    }

    public final void setIsExpiredAttended(final Boolean isExpiredAttendedParameter) {
        this.isExpiredAttended = isExpiredAttendedParameter;
    }

    public final String getApplicationDate() {
        return this.applicationDate;
    }

    public final void setApplicationDate(final String applicationDateParameter) {
        this.applicationDate = applicationDateParameter;
    }

    public final Boolean getIsRequisitionCancelled() {
        return this.isRequisitionCancelled;
    }

    public final void setIsRequisitionCancelled(final Boolean isRequisitionCancelledParameter) {
        this.isRequisitionCancelled = isRequisitionCancelledParameter;
    }

    public final Boolean getIsContractCancelled() {
        return this.isContractCancelled;
    }

    public final void setIsContractCancelled(final Boolean isContractCancelledParameter) {
        this.isContractCancelled = isContractCancelledParameter;
    }
    
    public final Integer getBeforeDaysExpirationAlert() {
        return this.beforeDaysExpirationAlert;
    }

    public final void setBeforeDaysExpirationAlert(final Integer beforeDaysExpirationAlertParameter) {
        this.beforeDaysExpirationAlert = beforeDaysExpirationAlertParameter;
    }

    public final Integer getAfterDaysExpirationAlert() {
        return this.afterDaysExpirationAlert;
    }

    public final void setAfterDaysExpirationAlert(final Integer afterDaysExpirationAlertParameter) {
        this.afterDaysExpirationAlert = afterDaysExpirationAlertParameter;
    }

    public final String getDateValue() {
        return this.dateValue;
    }

    public final void setDateValue(final String dateValueParameter) {
        this.dateValue = dateValueParameter;
    }

    public final String getBusinesmanName() {
        return this.businesmanName;
    }

    public final void setBusinesmanName(final String businesmanNameParameter) {
        this.businesmanName = businesmanNameParameter;
    }

    public final String getBusinessManEMail() {
        return this.businessManEMail;
    }

    public final void setBusinessManEMail(final String businessManEMailParameter) {
        this.businessManEMail = businessManEMailParameter;
    }
}
