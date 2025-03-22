package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.CheckDocumentable;
import mx.pagos.admc.contracts.structures.CheckDocument;
import mx.pagos.admc.enums.GuaranteeEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CheckDocumentBusiness {
    private static final String MESSAGE_SAVING_CHECK_DOCUMENT_ERROR =
            "Hubo un problema al guardar el documento de checklist";
    private static final String MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_ID_ERROR =
            "Hubo un problema al recuperar el documento de checklist";
    private static final String MESSAGE_CHECK_DOCUMENT_NOT_EXISTS_ERROR =
            "El documento del checklist ha dejado de existir";
    private static final String MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_STATUS_ERROR =
            "Hubo un problema al obtener los documentos de checklist por estatus";
    private static final String MESSAGE_RETRIVING_CHECK_DOCUMENT_ERROR =
            "Hubo un problema al obtener todos los documentos de checklist";
    private static final String MESSAGE_CHANGE_CHECK_DOCUMENT_STATUS_ERROR =
            "Hubo un problema al cambiar el estatus del documento de checklist";
    
    private static final Logger LOG = Logger.getLogger(CheckDocumentBusiness.class);
    
    @Autowired
    private CheckDocumentable checkDocumentable;
    
    public final Integer saveOrUpdate(final CheckDocument checkDocument) throws BusinessException {
        try {
            return this.checkDocumentable.saveOrUpdate(checkDocument);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_CHECK_DOCUMENT_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVING_CHECK_DOCUMENT_ERROR, databaseException);
        }
    }
    
    public final CheckDocument findById(final Integer idCheckDocument) throws BusinessException {
        try {
            return this.checkDocumentable.findById(idCheckDocument);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_ID_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_ID_ERROR, databaseException);
        } catch (EmptyResultException emptyResultException) {
            LOG.error(MESSAGE_CHECK_DOCUMENT_NOT_EXISTS_ERROR, emptyResultException);
            throw new BusinessException(MESSAGE_CHECK_DOCUMENT_NOT_EXISTS_ERROR, emptyResultException);
        }
    }
    
    public final List<CheckDocument> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.checkDocumentable.findByStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_STATUS_ERROR, databaseException);
        }
    }
    
    public final List<CheckDocument> findAll() throws BusinessException {
        try {
            return this.checkDocumentable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_CHECK_DOCUMENT_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_CHECK_DOCUMENT_ERROR, databaseException);
        }
    }
    
    public final void changeStatus(final Integer idCheckDocument, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (RecordStatusEnum.ACTIVE == status)
                this.checkDocumentable.changeStatus(idCheckDocument, RecordStatusEnum.INACTIVE);
            else
                this.checkDocumentable.changeStatus(idCheckDocument, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_CHANGE_CHECK_DOCUMENT_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_CHANGE_CHECK_DOCUMENT_STATUS_ERROR, databaseException);
        }
    }
    
    public final List<CheckDocument> findByGuaranteeAndStatus(final RecordStatusEnum status,
            final GuaranteeEnum guarantee) throws BusinessException {
        try {
            return this.checkDocumentable.findByGuaranteeAndStatus(status, guarantee);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_STATUS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_CHECK_DOCUMENT_BY_STATUS_ERROR, databaseException);
        }
    }
}
