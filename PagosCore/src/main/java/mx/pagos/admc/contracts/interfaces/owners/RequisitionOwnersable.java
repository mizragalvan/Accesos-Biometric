package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.ContractCancellationComment;
import mx.pagos.admc.contracts.structures.TrayFilter;
import mx.pagos.admc.contracts.structures.TrayRequisition;
import mx.pagos.admc.contracts.structures.owners.OwnersVersion;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.SectionTypeEnum;
import mx.pagos.admc.util.shared.ParametersHolder;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

public interface RequisitionOwnersable {

	Integer saveOrUpdate(RequisitionOwners requisition) throws DatabaseException;
	
    RequisitionOwners findRequisitionOwnersById(Integer idRequisitionOwners) throws DatabaseException, 
    EmptyResultException;
    
    List<Integer> findCheckDocumentationByIdRequisitionOwner(Integer idRequisitionOwner) 
            throws DatabaseException;

    void saveDeciderLawyer(Integer idRequisitionOwner, Integer idDeciderLawyer) throws DatabaseException;
    
    void saveLawyer(Integer idRequisitionOwner, Integer idLawyer) throws DatabaseException;

    Integer saveRequisitionAttatchmentOwners(Integer idRequisitionOwner, Integer idDocument, String sectionType, 
    		String documentName) throws DatabaseException;
    
    void deleteRequisitionAttatchmentOwnersByIdRequisitionAndSectionType(Integer idRequisition, String sectionType)
    		throws DatabaseException;
    
    void updateJurisdictionByIdRequisition(Integer idRequisition, String jurisdiction)
    		throws DatabaseException;

    void deleteCheckListDocumentsByIdRequisition(Integer idRequisition)throws DatabaseException;

    void insertCheckList(Integer idRequisition, Integer idParameter)throws DatabaseException;

    List<Integer> findRequisitionOwnersAttachmentByIdRequisitionOwners(Integer idRequisitionOwner, 
            SectionTypeEnum sectionType) throws DatabaseException;
    
    List<DocumentBySection> findDocumentsAttachmentBySectionType(Integer idRequisitionOwner, 
    		SectionTypeEnum sectionType) throws DatabaseException;
    
    List<DocumentBySection> findDigitalizationBySectionType(Integer idRequisitionOwner, SectionTypeEnum sectionType)
            throws DatabaseException;
    
    Boolean haveDictaminationDocumentsOfRequisitionOwner(Integer idRequisitionOwner) throws DatabaseException;
    
    void saveIdDictaminationTemplate(Integer idRequisitionOwner, Integer idDictaminationTemplate) 
            throws DatabaseException;

    void saveSignNotification(RequisitionOwners requisitionOwners) throws DatabaseException;
    
    void saveRequisitionOwnersDigitalizations(Integer idRequisitionOwner, Integer idDocument,
            SectionTypeEnum sectionType, String documentName) throws DatabaseException;
    
    void deleteRequisitionOwnersDigitalizationsByIdRequisitionAndSectionType(Integer idRequisitionOwner, 
            SectionTypeEnum sectionType) throws DatabaseException;

    List<TrayRequisition> findRequisitionsForTray(TrayFilter trayFilterInteger, Integer pageNumber,
            Integer itemsNumber) throws DatabaseException;
    
    List<Version> findDictaminationVersionsByIdRequisitionOwner(Integer idRequisitionOwner) throws DatabaseException;
    
    List<RequisitionOwners> findRequisitionDocumentsContracsAndGuaranteesByParameters(ParametersHolder parameters) 
            throws DatabaseException;
    
    void saveUserDitamenVoBo(Integer idRequisitionOwner, Integer idUserDictamenVobo) throws DatabaseException;
    
    void saveUserProjectReviewVoBo(Integer idRequisitionOwnwer, Integer idUserProjectReviewVoBo) 
            throws DatabaseException;
    
    void saveUserNotificationSigning(Integer idRequisitionOwnwer, Integer idUserSignVoBo) throws DatabaseException;
    
    RequisitionOwners findRequisitionMultidocuments(final Integer idRequisitionOwner) throws DatabaseException;
    
    void deleteRequisitionOwnerGuaranteeCheckDocument(Integer idRequisitionOwner) throws DatabaseException;
    
    void saveRequisitionOwnerGuaranteeCheckDocument(Integer idRequisitionOwner, Integer idCheckDocument) 
            throws DatabaseException;
    
    List<OwnersVersion> findOwnersVersionByIdRequisition(Integer idRequisitionOwner) throws DatabaseException;

	void saveLoadingProject(RequisitionOwners requisitionParameter) throws DatabaseException;
	
	void updateIsExpiredAttended(Integer idRequisitionOwners, Boolean isExpiredAttended) throws DatabaseException;

    Integer saveContractCancellationComment(ContractCancellationComment cancellationComment) throws DatabaseException;

    ContractCancellationComment findContractCancellationComment(Integer idRequisitionOwners) throws DatabaseException;

    void saveContractCancellationCommentDocument(Integer idOwnersCancellationComment, Integer idDocument,
            String documentName) throws DatabaseException;

    List<DocumentBySection> findContractCancelationCommentDocument(
            Integer idRequisitionOwners) throws DatabaseException;

    List<RequisitionOwners> findPaginatedContractsForManagement(RequisitionOwners requisitionOwners, Integer pageNumber,
            Integer itemsNumber) throws DatabaseException;

    Long countPaginatedContractsForManagement(RequisitionOwners requisitionOwners) throws DatabaseException;

    List<RequisitionOwners> findPaginatedRequisitionsForManagement(RequisitionOwners requisitionOwners,
            Integer pageNumber, Integer itemsNumber) throws DatabaseException;

    Long countPaginatedRequisitionsForManagement(RequisitionOwners requisitionOwners) throws DatabaseException;

    void updateManagementInfo(RequisitionOwners requisitionOwners) throws DatabaseException;

    List<RequisitionOwners> findPaginatedContractsToExpire(RequisitionOwners requisitionOwners,
            final Integer pageNumber, final Integer itemsNumber) throws DatabaseException;

    Long countContractsToExpire(RequisitionOwners requisitionOwners) throws DatabaseException;

	void saveFreezeInformationOfContractDetailOwners(Integer idRequisitionOwnersParameter,
			String contractDetailOwnersJson) throws DatabaseException;

	String findOwnersContractDetail(Integer idRequisitionOwnersParameter) throws DatabaseException;

	Long countTotalRowsForTray(TrayFilter trayFilter) throws DatabaseException;
	List<TrayRequisition> findAllRequisitionsForTray(final TrayFilter trayFilter) throws DatabaseException;
}
