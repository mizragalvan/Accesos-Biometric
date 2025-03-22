package mx.pagos.document.version.business;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.structures.Requisition;
import mx.pagos.admc.contracts.structures.dtos.VersionDTO;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.document.versioning.interfaces.Documentable;
import mx.pagos.document.versioning.interfaces.Versionable;
import mx.pagos.document.versioning.structures.Document;
import mx.pagos.document.versioning.structures.Version;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;
import mx.pagos.security.structures.UserSession;

/**
 * @author Mizraim
 *
 */

@Service
public class DocumentVersionBusiness {

	private static final String MESSAGE_DOCUMENT_NOT_EXISTS_ERROR = "El documento ha dejado de existir";
	private static final String MESSAGE_RETRIEVING_DOCUMENT_ERROR = "Hubo un error al recuperar el documento";
	private static final String MESSAGE_RETRIEVING_VERSION_ERROR = "Hubo un problema al obtener versión";
	private static final String MESSAGE_RETRIEVING_VERSIONS_ERROR = "Hubo un problema al obtener las versiones por documento";
	private static final String MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR = "Hubo un error al obtener la versión actual";
	private static final String MESSAGE_CHANGE_STATUS_ERROR = "Hubo un problema al cambiar el estatus del Documento";
	private static final String MESSAGE_SAVING_DOCUMENT_ERROR = "Hubo un problema al guardar los datos del documento";
	private static final String MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR = "La versión ha dejado de existir";
	private static final String MESSAGE_DELETING_DOCUMENT_ERROR = "Hubo un problema al eliminar el documento";
	private static final String MESSAGE_DELETING_FILE_ERROR = "Permisos insuficientes para borrar los archivos relacionados al documento";
	private static final String MESSAGE_VERSION_NOT_EXISTS_ERROR = "La versión seleccionada ha dejado de existir";
	private static final String MESSAGE_EMPTY_LIST = "No existen registros";

	private static final Logger LOG = Logger.getLogger(DocumentVersionBusiness.class);

	@Autowired
	private Documentable documentable;

	@Autowired
	private Versionable versionable;

	@Autowired
	private UserSession session;

	public final Integer save(final Integer idDocument, final File documentPath) throws BusinessException {
		try {
			return this.versionDocument(idDocument, documentPath);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_DOCUMENT_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_RETRIEVING_VERSION_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_RETRIEVING_VERSION_ERROR, emptyResultException);
		}
	}

	private Integer versionDocument(final Integer idDocument, final File documentPath)
			throws DatabaseException, EmptyResultException {
		final Document document = new Document();
		if (idDocument == null) {
			document.setCurrentVersion(1);
			document.setIdDocument(this.documentable.saveOrUpdate(document));
			this.versionable.save(document.getIdDocument(), documentPath, this.session.getIdUsuarioSession());
		} else {
			document.setIdDocument(idDocument);
			final Integer idVersion = this.versionable.save(idDocument, documentPath,
					this.session.getIdUsuarioSession());
			document.setCurrentVersion(this.versionable.findByIdVersion(idVersion).getVersionNumber());
			this.documentable.saveOrUpdate(document);
		}
		return document.getIdDocument();
	}

	public final void changeDocumentStatus(final Integer idDocument, final RecordStatusEnum status)
			throws BusinessException {
		try {
			if (status == RecordStatusEnum.ACTIVE)
				this.documentable.changeDocumentStatus(idDocument, RecordStatusEnum.INACTIVE);
			else
				this.documentable.changeDocumentStatus(idDocument, RecordStatusEnum.ACTIVE);
		} catch (DatabaseException dataBaseException) {
			LOG.error(MESSAGE_CHANGE_STATUS_ERROR, dataBaseException);
			throw new BusinessException(MESSAGE_CHANGE_STATUS_ERROR, dataBaseException);
		}
	}

	public final Version findCurrentVersion(final Integer idDocument) throws BusinessException {
		try {
			final Integer currentVersion = this.documentable.findByIdDocument(idDocument).getCurrentVersion();
			return this.versionable.findDocumentVersion(idDocument, currentVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR, emptyResultException);
		}
	}
	  public  Version findVersion(final Integer idDocument) throws BusinessException {
	        try {
	            final Integer currentVersion = this.documentable.findByIdDocument(idDocument).getCurrentVersion();
	            return this.versionable.findDocumentVersion(idDocument, currentVersion);
	        } catch (DatabaseException databaseException) {
	            LOG.error(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
	            throw new BusinessException(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
	        } catch (EmptyResultException emptyResultException) {
	        	LOG.error(MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR, emptyResultException);
	            throw new BusinessException(MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR, emptyResultException);
			}
	    }

	public final List<Version> findVersionByIdDocument(final Integer idDocument) throws BusinessException {
		try {
			return this.versionable.findByIdDocument(idDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_VERSIONS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_VERSIONS_ERROR, databaseException);
		}
	}

	public final Version findDocumentVersion(final Integer idDocument, final Integer versionNumber)
			throws BusinessException {
		try {
			return this.versionable.findDocumentVersion(idDocument, versionNumber);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_VERSION_NOT_EXISTS_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_VERSION_NOT_EXISTS_ERROR, emptyResultException);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_VERSION_ERROR, databaseException);
		}
	}

	public final Document findByIdDocument(final Integer idDocument) throws BusinessException {
		try {
			return this.documentable.findByIdDocument(idDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_DOCUMENT_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_DOCUMENT_NOT_EXISTS_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_DOCUMENT_NOT_EXISTS_ERROR, emptyResultException);
		}
	}

	public final void deleteByIdDocument(final Integer idDocument) throws BusinessException {
		try {
			final List<Version> documentVersions = this.versionable.findByIdDocument(idDocument);
			this.deleteVersionsFiles(documentVersions);
			this.versionable.deleteByIdDocument(idDocument);
			this.documentable.deleteById(idDocument);
		} catch (SecurityException securityException) {
			LOG.error(MESSAGE_DELETING_FILE_ERROR, securityException);
			throw new BusinessException(MESSAGE_DELETING_FILE_ERROR, securityException);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_DELETING_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_DELETING_DOCUMENT_ERROR, databaseException);
		}
	}

	private void deleteVersionsFiles(final List<Version> documentVersions) {
		File fileToDelete;
		for (Version version : documentVersions) {
			fileToDelete = new File(version.getDocumentPath());
			fileToDelete.delete();
		}
	}

	public final VersionDTO findCurrentVersionDTO(final Integer idDocument) throws BusinessException {
		try {
			final Integer currentVersion = this.documentable.findByIdDocument(idDocument).getCurrentVersion();
			return this.versionable.findDocumentVersionDTO(idDocument, currentVersion);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR, emptyResultException);
			throw new BusinessException(MESSAGE_CURRENT_VERSION_NOT_EXISTS_ERROR, emptyResultException);
		}
	}

	public final void saveContractVersion(final Integer idRequisition, final Integer idDocument)
			throws BusinessException {
		try {
			this.versionable.saveContractVersion(idRequisition, idDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_SAVING_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_SAVING_DOCUMENT_ERROR, databaseException);
		}
	}

	public final List<VersionDTO> findContractVersionDTO(final Integer idRequisition) throws BusinessException {
		try {
			List<VersionDTO> versions = this.versionable.findContractVersionDTO(idRequisition);
			for (VersionDTO versionDto : versions) {
				versionDto.setFileName(FilenameUtils.getName(versionDto.getDocumentPath()));
			}
			return versions;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_CURRENT_VERSION_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error(MESSAGE_EMPTY_LIST, emptyResultException);
			throw new BusinessException(MESSAGE_EMPTY_LIST, emptyResultException);
		}
	}

}
