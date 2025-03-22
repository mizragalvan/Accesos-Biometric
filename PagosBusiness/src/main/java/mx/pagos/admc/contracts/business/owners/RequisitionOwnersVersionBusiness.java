package mx.pagos.admc.contracts.business.owners;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersVersionable;
import mx.pagos.admc.contracts.structures.owners.CategoryCheckDocumentation;
import mx.pagos.admc.contracts.structures.owners.RequisitionOwners;
import mx.pagos.admc.enums.SectionTypeEnum;
import mx.pagos.document.versioning.structures.DocumentBySection;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service
public class RequisitionOwnersVersionBusiness {

    private static final Logger LOG = Logger.getLogger(RequisitionOwnersVersionBusiness.class);

    private static final String MESSAGE_SAVING_REQUISITION_VERSION_ERROR =
            "Hubo un error al guardar la versión de la solicitud";
    private static final String MESSAGE_FIND_REQUISITION_VERSION_BY_VERSION_ERROR =
            "Hubo un error al buscar la solicitud por la versión";
    private static final String MESSAGE_FIND_ATTACHMENTS_BY_VERSION_ERROR =
            "Hubo un error al buscar los anexos por la versión";
    private static final String MESSAGE_FIND_CHECKDOCUMENTATION_BY_VERSION_ERROR =
            "Hubo un error al buscar los checklist de categorías por la versión";
    private static final String MESSAGE_SAVE_CHECKDOCUMENTATION_BY_VERSION_ERROR =
            "Hubo un error al guardar los checklist de categorías por la versión";

    @Autowired
    private RequisitionOwnersVersionable requisitionVersionOwnersable;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { BusinessException.class })
    public Integer saveRequisitionOwnersVersion(final Integer idRequisition) throws BusinessException {
        try {
            final Integer idRequisitionVersion = 
                    this.requisitionVersionOwnersable.saveRequisitionOwnersVersion(idRequisition);
            this.requisitionVersionOwnersable.saveRequisitionRequisitionOwnersVersion(
                    idRequisition, idRequisitionVersion);
            this.requisitionVersionOwnersable.saveRequisitionOwnersDigitalizationsVersion(
                    idRequisition, idRequisitionVersion);
            this.requisitionVersionOwnersable.saveRequisitionOwnersAttachmentVersion(
                    idRequisition, idRequisitionVersion);
            this.saveRequisitionOwnersCheckDocumentationVersion(idRequisition, idRequisitionVersion);
            this.requisitionVersionOwnersable.saveRequisitionOwnersGuaranteeCheckDocumentVersion(
                    idRequisition, idRequisitionVersion);
            this.requisitionVersionOwnersable.saveRequisitionOwnersStatusVersion(
                    idRequisition, idRequisitionVersion);
            this.requisitionVersionOwnersable.saveRequisitionOwnersStatusTurnVersion(
                    idRequisition, idRequisitionVersion);
            return idRequisitionVersion;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_REQUISITION_VERSION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVING_REQUISITION_VERSION_ERROR, databaseException);
        }
    }

    private void saveRequisitionOwnersCheckDocumentationVersion(final Integer idRequisition, 
            final Integer idRequisitionVersion) throws BusinessException {
        try {
            final List<CategoryCheckDocumentation> list = 
                    this.requisitionVersionOwnersable.findCategoryCheckDocumentationForVersion(idRequisition);
            for (final CategoryCheckDocumentation categoryCheck : list)
                this.requisitionVersionOwnersable.
                saveRequisitionOwnersCheckDocumentationVersion(idRequisitionVersion, 
                        categoryCheck.getIdCategory(), categoryCheck.getIdCheckDocumentation());
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVE_CHECKDOCUMENTATION_BY_VERSION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVE_CHECKDOCUMENTATION_BY_VERSION_ERROR, databaseException);
        }
    }

    public RequisitionOwners findRequisitionOwnerVersionByIdVersion(final Integer idRequisitionOwnerVersion, 
            final Integer versionNumber) throws BusinessException {
        try {
            return this.requisitionVersionOwnersable.findRequisitionOwnerVersionByIdVersion(
                    idRequisitionOwnerVersion, versionNumber);
        } catch (DatabaseException  databaseException) {
            LOG.error(MESSAGE_FIND_REQUISITION_VERSION_BY_VERSION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_REQUISITION_VERSION_BY_VERSION_ERROR, databaseException);
        }
    }

    public List<DocumentBySection> findAttachmentsByVersion(final Integer idRequisitionOwnerVersion, 
            final SectionTypeEnum sectionType) throws BusinessException {
        try {
            return this.requisitionVersionOwnersable.findAttachmentByVersion(idRequisitionOwnerVersion, sectionType);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ATTACHMENTS_BY_VERSION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ATTACHMENTS_BY_VERSION_ERROR, databaseException);
        }
    }

    public List<Integer> findCheckDocumentationByVersion(final Integer idRequisitionOwnerVersion)
            throws BusinessException {
        try {
            return this.requisitionVersionOwnersable.findCheckDocumentationByVersion(idRequisitionOwnerVersion);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_CHECKDOCUMENTATION_BY_VERSION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_CHECKDOCUMENTATION_BY_VERSION_ERROR, databaseException);
        }
    }
}
