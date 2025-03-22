package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.ApprovalArea;
import mx.pagos.admc.contracts.structures.Dga;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.RequisitionVersion;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.User;

/**
 * 
 * @author Mizraim
 * 
 * Interfaz que contiene los métodos con los que se interactuará con los DAOs del versionador de Solicitudes
 * 
 * @see DatabaseException
 * @see FlowPurchasingEnum
 *
 */
public interface RequisitionVersionable {

    Integer saveRequisitionVersion(Integer idRequisition) throws DatabaseException;
    
    void saveRequisitionRequisitionVersion(Integer idRequisition, Integer idRequisitionVersion)
            throws DatabaseException;
    
    void saveRequisitionApprovalAreasVersion(Integer idRequisition, Integer idRequisitionVersion)
            throws DatabaseException;
    
    void saveRequisitionAttatchmentsVersion(Integer idRequisition, Integer idRequisitionVersion)
           throws DatabaseException;
    
    void saveRequisitionAuthorizationDgasVersion(Integer idRequisition, Integer idRequisitionVersion)
            throws DatabaseException;
    
    void saveRequisitionFinantialEntitiesVersion(Integer idRequisition, Integer idRequisitionVersion)
            throws DatabaseException;
    
    void saveRequisitionFinantialEntityWitnessesVersion(Integer idRequisition, Integer idRequisitionVersion)
            throws DatabaseException;
    
    void saveRequisitionLegalRepresentativesVersion(Integer idRequisition, Integer idRequisitionVersion)
            throws DatabaseException;
    
    void saveRequisitionUsersVoboVersion(Integer idRequisition, Integer idRequisitionVersion) throws DatabaseException;
    
    List<VersionDTO> findRequisitionVersions(Integer idRequisition) throws DatabaseException; 
    
    Requisition findById(Integer idRequisitionVersion) throws DatabaseException, EmptyResultException;
    
    List<Integer> findApprovalAreas(Integer idRequisitionVersion) throws DatabaseException;

    RequisitionVersion findIdRequisitionVersion(Integer idRequisition) throws DatabaseException;
     
    List<String> findApprovalAreasActive(Integer idRequisitionVersion) throws DatabaseException;
    
    List<Version> findAttatchments(Integer idRequisitionVersion) throws DatabaseException;
    
    List<Dga> findAuthorizationDgas(Integer idRequisitionVersion) throws DatabaseException;
    
    List<FinancialEntity> findFinantialEntities(Integer idRequisitionVersion) throws DatabaseException;
    
    List<String> findFinantialEntitiesWitnesses(Integer idRequisitionVersion) throws DatabaseException;
    
    List<LegalRepresentative> findLegalRepresentatives(Integer idRequisitionVersion) throws DatabaseException;
    
    List<User> findUsersVobo(Integer idRequisitionVersion) throws DatabaseException;

    List<ApprovalArea> findApprovalAreasVoBo(Integer idRequisitionVersion) throws DatabaseException;
    
    List<String> findApprovalAreasName(Integer idRequisitionVersion) throws DatabaseException;

	void saveScalingVersion(Integer idRequisition, Integer idRequisitionVersion) throws DatabaseException;
	
	// DEBUGGING PLEASE DO NOT REMOVE - LuisV
	String debug(String task) throws DatabaseException;
	// END DEBUGGING
}
