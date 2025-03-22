package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Attachment;
import mx.pagos.admc.contracts.structures.Clause;
import mx.pagos.admc.contracts.structures.ContractCancellationComment;
import mx.pagos.admc.contracts.structures.Customs;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.DocumentDS;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.FinantialEntityWitness;
import mx.pagos.admc.contracts.structures.Instrument;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Obligation;
import mx.pagos.admc.contracts.structures.RequiredDocumentBySupplier;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionAngular;
import mx.pagos.admc.contracts.structures.RequisitionAttachment;
import mx.pagos.admc.contracts.structures.RequisitionDocuSign;
import mx.pagos.admc.contracts.structures.RequisitionDraftPart2;
import mx.pagos.admc.contracts.structures.RequisitionStatusTurn;
import mx.pagos.admc.contracts.structures.RequisitionsPartFour;
import mx.pagos.admc.contracts.structures.RequisitionsPartOneAndTwo;
import mx.pagos.admc.contracts.structures.RequisitionsPartThree;
import mx.pagos.admc.contracts.structures.Scaling;
import mx.pagos.admc.contracts.structures.SupplierPerson;
import mx.pagos.admc.contracts.structures.SupplierPersonByRequisition;
import mx.pagos.admc.contracts.structures.TrayFilter;
import mx.pagos.admc.contracts.structures.TrayRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisition;
import mx.pagos.admc.contracts.structures.UserInProgressRequisitionFilter;
import mx.pagos.admc.contracts.structures.dtos.RequisitionDTO;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.ScalingTypeEnum;
import mx.pagos.admc.enums.SupplierPersonTypeEnum;
import mx.pagos.admc.util.shared.ConsultaList;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.User;

/**
 * 
 * @author Mizraim
 * 
 * Interfaz que contiene los métodos con los que se interactuará con los DAOs de Solicitud
 * 
 * @see DatabaseException
 * @see FlowPurchasingEnum
 *
 */
public interface Requisitable {
    Integer saveOrUpdate(Requisition requisition) throws DatabaseException;

    void changeRequisitionStatus(Integer idRequisition, FlowPurchasingEnum status) throws DatabaseException;

    Requisition findById(Integer idRequisition) throws DatabaseException, EmptyResultException;
    
    DocumentDS getUser(Integer idRequisition) throws DatabaseException;

    Requisition findByIdInProgress(Integer idRequisition) throws DatabaseException, EmptyResultException;

    List<Integer> findRequisitionsToCreateOneFromAnother(String idRequisitionParameter, 
    		String documentTypeParameter, String supplierParameter) throws DatabaseException;
    List<RequisitionDTO> findAllRequisitions(RequisitionAngular requisition) throws DatabaseException;
    
    List<Requisition> findByFlowPurchasingStatus(FlowPurchasingEnum status, final Integer idFlow)
            throws DatabaseException;

    List<Integer> findRequisitionFinancialEntityByIdRequisition(final Integer idRequisition) throws DatabaseException;

    List<String> findRequisitionFinancialEntityActiveByIdRequisition(final Integer idRequisition)
            throws DatabaseException;

    List<FinantialEntityWitness> findRequisitionFinancialEntityByIdRequisitionWitness(
    		final Integer idRequisition) throws DatabaseException;

    void saveRequisitionFinancialEntity(FinancialEntity entity) throws DatabaseException;

    void deleteRequisitionFinancialEntity(Integer idRequisition) throws DatabaseException;

    void saveRequisitionApprovalArea(Integer idRequisition, Integer idArea, Integer voboIdDocument)
            throws DatabaseException;

    void saveRequisitionAuthorizationDga(Integer idRequisition, Integer idDga) throws DatabaseException;

    void deleteRequisitionApprovalAreas(Integer idRequisition) throws DatabaseException;

    void deleteRequisitionAuthorizationDgas(Integer idRequisition) throws DatabaseException;

    List<Integer> findRequisitionApprovalAreas(Integer idRequisition) throws DatabaseException;
    
    List<String> findRequisitionApprovalAreasActive(Integer idRequisition) throws DatabaseException;

    List<Integer> findRequisitionAuthorizationDgas(Integer idRequisition) throws DatabaseException;

    List<String> findRequisitionAuthorizationDgasActive(Integer idRequisition) throws DatabaseException;
    
    List<Dga> findAutorizationDga(Integer idRequisition) throws DatabaseException;
    
    List<String> findApprovalAreas(Integer idRequisition) throws DatabaseException;

   void saveRequisitionLegalRepresentative(Integer idRequisition,
           Integer idLegalRepresentative) throws DatabaseException;

   List<LegalRepresentative> findRequisitionLegalRepresentatives(Integer idRequisition) throws DatabaseException;


   List<String> findRequisitionLegalRepresentativesActive(Integer idRequisition)throws DatabaseException;

   void deleteRequisitionLegalRepresentatives(Integer idRequisition) throws DatabaseException;

   List<User> findUsersToVoBo(Integer idRequisition) throws DatabaseException;

   void saveUserToVoBo(Integer idRequisition, Integer idUser) throws DatabaseException;

   void deleteUsersToVoBo(Integer idRequisition) throws DatabaseException;
   
   void saveUserVobo(Integer idRequisition, Integer idUser) throws DatabaseException;

   void saveRequisitionFinantialEntityWitness(Integer idRequisition, String witnessName) throws DatabaseException;

   List<String> findRequisitionFinantialEntityWitnesses(Integer idRequisition) throws DatabaseException;

   void deleteRequisitionFinantialEntityWitnesses(Integer idRequisition) throws DatabaseException;

   void saveRequisitionLawyer(Integer idRequisition, Integer idLawyer) throws DatabaseException;

   void saveRequisitionAttatchmentsFields(Requisition requisition) throws DatabaseException;

   void saveContractDraftFields(Requisition requisition) throws DatabaseException;

   void saveRequisitionEvaluator(Requisition requisition) throws DatabaseException;

   void saveProviderAndWitnessesSignDates(Requisition requisition) throws DatabaseException;

    void saveRequisitionLegalRepresentativeSignDate(Integer idRequisition,
            LegalRepresentative legalRepresentative) throws DatabaseException;

    void saveRequisitionSignedContractData(Requisition requisition) throws DatabaseException;

    void saveRequisitionLegalRepresentativeSignedContractData(Integer idRequisition,
            LegalRepresentative legalRepresentative) throws DatabaseException;

	Integer saveRequisitionAttatchment(RequisitionAttachment requisitionAttatchment)throws DatabaseException;

	void deleteRequisitionAttatchmentByIdRequisition(Integer idRequisition)throws DatabaseException;

	void deleteRequisitionAttatchmentByIdDocument(Integer idDocument)throws DatabaseException;

	List<Integer> findRequisitionAttachmentByIdRequisition(Integer idRequisition) throws DatabaseException;

	Integer findHistoryDocumentsVersions(Integer idSupplier, Integer idRequiredDocument)
			throws DatabaseException;

    void saveDigitalizationIdDocument(Integer idRequisition, Integer digitalizationIdDocument) throws DatabaseException;

    List<Version> findDigitalizationDocuments(Integer idRequisition) throws DatabaseException;

    void deleteDigitalizationDocuments(Integer idRequisition) throws DatabaseException;

    List<ApprovalArea> findRequisitionApprovalAreasVoBo(Integer idRequisition) throws DatabaseException;

    void saveSupplierApprovalIdDocument(Integer idRequisition, Integer supplierApprovalIdDocument)
            throws DatabaseException;

    void saveTemplateIdDocument(Integer idRequisition, Integer templateIdDocument) throws DatabaseException;

    void saveSupplierApprovalDocument(Integer idRequisition, Integer templateIdDocument) throws DatabaseException;

    void deleteDigitalizationByIdDocument(Integer idDocument) throws DatabaseException;

    Integer saveObligation(Obligation obligation) throws DatabaseException;

    List<Obligation> findObligationsByIdRequisition(Integer idRequisition) throws DatabaseException;

    void deleteObligationsByIdRequisition(Integer idRequisition) throws DatabaseException;

    List<TrayRequisition> findPaginatedTrayRequisitions(TrayFilter trayFilter, Integer pageNumber,
            Integer itemsNumber, final String search) throws DatabaseException;

    Boolean findIsAllUsersVobo(Integer idRequisition) throws DatabaseException;

    void saveRequisitionStatusTurn(Integer idRequisition, FlowPurchasingEnum status) throws DatabaseException;

    List<Requisition> findRequisitionByFlow(Integer idFlow) throws DatabaseException;
    
    void saveApplicant(Integer idRequisition, Integer idApplicant) throws DatabaseException;
    
    void saveLawyer(Integer idRequisition, Integer idLawyer) throws DatabaseException;
    
    List<Requisition> findRequisitionByManyParameters(Requisition requisition) throws DatabaseException;
    
    List<Requisition> findClosedRequisitionUnattended(Integer beforeExpirationDate, Integer afteExpirationDate) 
            throws DatabaseException;
    
    void saveRequisitionStatusTurnAttentionDaysAndStage(
            RequisitionStatusTurn requisitionStatusTurn) throws DatabaseException;

    List<RequisitionStatusTurn> findRequisitionStatusTurnsByIdRequisition(Integer idRequisition)
            throws DatabaseException;

    Integer findCurrentTurnByIdRequisition(Integer idRequisition) throws DatabaseException;
    
    List<Requisition> findRequisitionClosed(Requisition requisition, Integer beforeExpirationDay, 
            Integer afterExpirationDay) throws DatabaseException;
    
    void changeAttendStatus(final Integer idRequisition, final Boolean isExpiredAttended) throws DatabaseException;
    
    void saveScalingMatrix(final Scaling scaling) throws DatabaseException;
    
    void deleteScalingMatrixByIdRequisition(final Integer idRequisition, final ScalingTypeEnum scalingType) 
    		throws DatabaseException;
    
    List<Scaling> findScalingMatrixVersionByIdRequisitionVersion(final Integer idRequisitionVersion, 
    		final ScalingTypeEnum scalingType) throws DatabaseException;
    
    List<Scaling> findScalingMatrixByIdRequisition(final Integer idRequisition, final ScalingTypeEnum scalingType) 
    		throws DatabaseException;

    List<Requisition> findPaginatedRequisitionsManagement(Requisition requisition,
            Integer pageNumber, Integer itemsNumber) throws DatabaseException;

    void cleanUsersVobo(Integer idRequisition) throws DatabaseException;
    
    void deleteRequisitionAttachmentVersion(Integer idDocument) throws DatabaseException;
    
    Long countTotalRowsRequisitionsManagement(final Requisition requisition) throws DatabaseException;
    
    Long countTotalRowsOfContracts(final Requisition requisition) throws DatabaseException;

    List<Requisition> findPaginatedRequisitionsClosed(Requisition requisition, Integer beforeExpirationDays,
            Integer afterExpirationDate, Integer pageNumber, Integer itemsNumber) throws DatabaseException;
    
    List<Requisition> findPaginatedContracts(Requisition requisition, Integer pageNumber, Integer itemsNumber) 
            throws DatabaseException;

    Long countTotalRowsRequisitionsClosed(Requisition requisition, Integer beforeExpirationDays,
            Integer afterExpirationDate) throws DatabaseException;
    
    Integer saveComment(final ContractCancellationComment contractCancellationComment) throws DatabaseException;
    
    void saveContractCancellationCommentDocument(Integer idCancellationComment, Integer idDocument, String documentName)
            throws DatabaseException;
    
    ContractCancellationComment findContractCancellationComment(final Integer idRequisition) throws DatabaseException;
    
    List<DocumentBySection> findContractCancelationCommentDocument(final Integer idRequisition) 
            throws DatabaseException;
    
    Long countTotalRowsForTray(TrayFilter trayFilter, String search) throws DatabaseException;
    
    void saveFreezeInformationOfContractDetail(Integer idRequisition, String contractDetailJson) 
    		throws DatabaseException;
    
    String findContractDetailByIdRequisition(Integer idRequisition) throws DatabaseException;

	Boolean isRequisitionCancelled(Integer idRequisition) throws DatabaseException;
	
	void deleteAuthorizationDocument(Integer idRequisition) throws DatabaseException;

    void saveRequisitionStage(Integer idRequsition, String stage) throws DatabaseException;

    String findRequisitionStage(Integer idRequisition) throws DatabaseException, EmptyResultException;

	void deleteImssCeduleFile(Integer idRequisition) throws DatabaseException;
	
	Requisition findRequisitionBailsByIdRequisition(Integer idRequisition) throws DatabaseException;

    List<Requisition> findInprogressRequisitions(TrayFilter trayFilter) throws DatabaseException;

    List<FinancialEntity> findActiveFinancialEntitiesByIdRequisition(Integer idRequisition) throws DatabaseException;

    List<LegalRepresentative> findActiveLegalRepByRequisitionAndFinancialEnt(Integer idRequisition,
            Integer idFinancialEntity) throws DatabaseException;
    
    List<RequiredDocumentBySupplier> findRequiredDocumentsBySupplier(Integer idSupplier) throws DatabaseException;
    
    void deleteSupplierApprovalDocument(Integer idRequisition) throws DatabaseException;

	void changeRequisitionStatusToCancelled(Integer idRequisition) throws DatabaseException;

	void deleteFinancialEntityByRequisition(Integer idRequisition) throws DatabaseException;

	void saveFinancialEntityByRequisition(Integer idRequisition, FinancialEntity financialEntity)
			throws DatabaseException;

	void deletePendingRequisitions(List<Integer> list) throws DatabaseException;

    void updateFinancialEntityRequisitionDraftFields(Integer idRequisition, FinancialEntity financialEntity)
            throws DatabaseException;
    
   List<FinancialEntity> findFinancialEntityByRepLegalAndRequisition(Integer idRepLegal, Integer idRequisition)
           throws DatabaseException;
   
   List<UserInProgressRequisition> findApplicantInProgressRequisitions(
           UserInProgressRequisitionFilter filter, final Integer pageNumber,
           final Integer itemsNumber) throws DatabaseException;

    List<UserInProgressRequisition> findLawyerInProgressRequisitions(UserInProgressRequisitionFilter filter,
            Integer pageNumber, Integer itemsNumber) throws DatabaseException;

    Long countTotalRowsApplicantInProgressRequisitions(UserInProgressRequisitionFilter filter) throws DatabaseException;

    Long countTotalRowsLawyerInProgressRequisitions(UserInProgressRequisitionFilter filter) throws DatabaseException;
    
    List<TrayRequisition> obtenerSolicitudesPendientes (final TrayFilter trayFilter) throws DatabaseException;

	Integer saveOrUpdate(RequisitionsPartOneAndTwo requisition) throws DatabaseException;

	Integer saveOrUpdate5(Instrument requisition) throws DatabaseException;
	
	Integer saveOrUpdate6(Attachment requisition) throws DatabaseException;
	
	Integer saveOrUpdate7(Clause requisition) throws DatabaseException;
	Integer saveOrUpdateRequisitionDraftPart2(RequisitionDraftPart2 requisition)throws DatabaseException;
	Integer saveOrUpdateRequisitionDraftProem(Requisition requisition) throws DatabaseException;
	Integer saveOrUpdateRequisitionDraftClausules(Requisition requisition) throws DatabaseException;
	Integer saveOrUpdateRequisitionDraftProperty(Requisition requisition) throws DatabaseException;
	Integer saveOrUpdatePartThree(RequisitionsPartThree requisition) throws DatabaseException;
	Integer saveOrUpdatePartFour(RequisitionsPartFour requisition) throws DatabaseException;

	void actualizarEvaluadorVoBoJuridico(Integer idRequisition) throws DatabaseException;
	Integer getIdLawyerByIdRequisition(Integer idRequisition) throws DatabaseException;
	Integer getIdApplicantByIdRequisition(Integer idRequisition) throws DatabaseException;
	List<TrayRequisition> findPaginatedTrayRequisitions(final TrayFilter trayFilter) throws DatabaseException;
	List<TrayRequisition> findPaginatedTrayRequisitionsPorFechas(final TrayFilter trayFilter) throws DatabaseException;
	List<TrayRequisition> findContractsInRevision() throws DatabaseException;
	List<TrayRequisition> findContractsInSignatures() throws DatabaseException;
	void updateContractRiskByIdRequisition(final Integer idRequisition, final boolean contractRisk) throws DatabaseException;
	void updateVoBoContractRiskByIdRequisition(final Integer idRequisition, final boolean voboContractRisk) throws DatabaseException;
	FlowPurchasingEnum getStatusByIdRequisition (Integer idRequisition) throws DatabaseException;
	Integer findFirstJuristic () throws DatabaseException;
	Integer findTemplateIdDocumentByIdRequisition (final Integer idRequisition) throws DatabaseException;
	Integer findTemplateIdSupplierDocumentByIdRequisition (final Integer idRequisition) throws DatabaseException;
	String findTemplateNameDocumentByIdRequisition (final Integer idRequisition) throws DatabaseException;
	
	List<SupplierPersonByRequisition> getIdsSupplierPersonByIdRequisition (final Integer idRequisition, final Integer idSupplier) throws DatabaseException;
	List<SupplierPersonByRequisition> getIdsSupplierPersonByIdRequisitionDTO (final Integer idRequisition, final Integer idSupplier) throws DatabaseException;
	void deleteSupplierPersonByIdSupplierPerson(Integer idRequisition) throws DatabaseException;
	Integer saveSupplierPersonByIdRequisition(SupplierPersonByRequisition person) throws DatabaseException;
}
