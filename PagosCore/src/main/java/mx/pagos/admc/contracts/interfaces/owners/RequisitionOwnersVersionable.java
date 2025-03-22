package mx.pagos.admc.contracts.interfaces.owners;

import java.util.List;

import mx.pagos.admc.contracts.structures.owners.CategoryCheckDocumentation;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.SectionTypeEnum;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.general.exceptions.DatabaseException;

public interface RequisitionOwnersVersionable {

	Integer saveRequisitionOwnersVersion(Integer idRequisition) throws DatabaseException;

	void saveRequisitionRequisitionOwnersVersion(Integer idRequisition, Integer idRequisitionVersion) 
			throws DatabaseException;

	void saveRequisitionOwnersDigitalizationsVersion(Integer idRequisition, Integer idRequisitionVersion) 
			throws DatabaseException;

	void saveRequisitionOwnersAttachmentVersion(Integer idRequisition, Integer idRequisitionVersion)
			throws DatabaseException;

	void saveRequisitionOwnersCheckDocumentationVersion(final Integer idRequisitionVersion,
            final Integer idCategory, final Integer idCheckDocumentation) throws DatabaseException;

	void saveRequisitionOwnersGuaranteeCheckDocumentVersion(Integer idRequisition,
			Integer idRequisitionVersion) throws DatabaseException;

	void saveRequisitionOwnersStatusVersion(Integer idRequisition, Integer idRequisitionVersion) 
			throws DatabaseException;

	void saveRequisitionOwnersStatusTurnVersion(Integer idRequisition, Integer idRequisitionVersion) 
			throws DatabaseException;
	
    RequisitionOwners findRequisitionOwnerVersionByIdVersion(Integer idRequisitionOwnerVersion, Integer versionNumber)
	        throws DatabaseException;
    
    List<DocumentBySection> findAttachmentByVersion(Integer idRequisitionOwnerVersion, SectionTypeEnum sectionType) 
            throws DatabaseException;
    
    List<Integer> findCheckDocumentationByVersion(Integer idRequisitionOwnerVersion) throws DatabaseException;
    
    List<CategoryCheckDocumentation> findCategoryCheckDocumentationForVersion(Integer idRequisitionOwners)
            throws DatabaseException;
}
