package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.RequiredDocumentable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.RequiredDocument;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

/**
 * 
 * Lógica de negocio para manejar los documentos requeridos
 * 
 * @author Mizraim
 * 
 * @see RequiredDocumentDocument
 * @see RequiredDocumentable
 * @see BusinessException
 * @see RecordStatusEnum
 *
 */
@Service("RequiredDocumentBusiness")
public class RequiredDocumentBusiness extends AbstractExportable {
	private static final String MESSAGE_RETRIEVING_REQUIRED_DOCUMENT_ERROR =
			"Ocurrió un problema al encontrar el documento requerido";
	private static final String MESSAGE_RETRIEVING_REQUIRED_DOCUMENTS_ERROR =
			"Ocurrió un error al obtener los documentos requeridos";
	private static final String MESSAGE_CHANGE_REQUIRED_DOCUMENT_STATUS_ERROR =
			"Ocurrió un problema al cambiar el estatus del documento requerido";
	private static final String MESSAGE_PROBLEM_SAVING_REQUIRED_DOCUMENT =
			"Ocurrió un problema al guardar el documento requerido";
	private static final String MESSAGE_PROBLEM_SAVING_PERSONALITY_REQUIRED_DOCUMENT =
			"Ocurrió un problema al guardar el documento requerido por tipo de personalidad";
	private static final String MESSAGE_PROBLEM_DELETE_PERSONALITY_REQUIRED_DOCUMENT =
			"Ocurrió un problema al eliminar el documento requerido por tipo de personalidad";
	private static final String MESSAGE_FIND_ALL_WITH_PERSONALITIES_ERROR =
			"Hubo un problema al buscar documentos requeridos con su tipo de personalidad";
	private static final String MESSAGE_FIND_ALL_REQUIRED_DOCUMENT_CATALOG_PAGED_ERROR = 
			"Hubo un problema al buscar documentos requeridos paginados";
	private static final String MESSAGE_FIND_TOTAL_PAGES_REQUIRED_DOCUMENT_ERROR = 
			"Hubo un problema al buscar número de pagínas de documentos requeridos";

	private static final Logger LOG = Logger.getLogger(RequiredDocumentBusiness.class);

	@Autowired
	private RequiredDocumentable requiredDocumentable;

	@Autowired
	private ConfigurationsBusiness configuration;

	public final Integer saveOrUpdate(final RequiredDocument requiredDocument) throws BusinessException {
		try {
			final Integer idRequiredDocument = this.requiredDocumentable.findIdByName(requiredDocument.getName());
			if (requiredDocument.getIdRequiredDocument() != null || idRequiredDocument == null)
				return this.requiredDocumentable.saveOrUpdate(requiredDocument);
			return idRequiredDocument;
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_PROBLEM_SAVING_REQUIRED_DOCUMENT, databaseException);
			throw new BusinessException(MESSAGE_PROBLEM_SAVING_REQUIRED_DOCUMENT, databaseException);
		}
	}

	public final void changeRequiredDocumentStatus(final Integer idRequiredDocument, final RecordStatusEnum status)
			throws BusinessException {
		try {
			final RecordStatusEnum changedStatus =
					status.equals(RecordStatusEnum.ACTIVE) ? RecordStatusEnum.INACTIVE : RecordStatusEnum.ACTIVE;
			this.requiredDocumentable.changeRequiredDocumentStatus(idRequiredDocument, changedStatus);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_CHANGE_REQUIRED_DOCUMENT_STATUS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_CHANGE_REQUIRED_DOCUMENT_STATUS_ERROR,
					databaseException);
		}
	}

	public final List<RequiredDocument> findAll() throws BusinessException {
		try {
			return this.requiredDocumentable.findAll();
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUIRED_DOCUMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUIRED_DOCUMENTS_ERROR, databaseException);
		}
	}

	public final RequiredDocument findByIdRequiredDocument(final Integer idRequiredDocument) throws BusinessException {
		try {
			return this.requiredDocumentable.findByIdRequiredDocument(idRequiredDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUIRED_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUIRED_DOCUMENT_ERROR, databaseException);
		} catch (EmptyResultException emptyResultException) {
			LOG.error("El documento requerido ha dejado de existir. ID: " + idRequiredDocument.toString(),
					emptyResultException);
			throw new BusinessException("El documento requerido ha dejado de existir", emptyResultException);
		}
	}

	public final List<RequiredDocument> findByStatus(final RecordStatusEnum status) throws BusinessException {
		try {
			return this.requiredDocumentable.findByStatus(status);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_RETRIEVING_REQUIRED_DOCUMENTS_ERROR, databaseException);
			throw new BusinessException(MESSAGE_RETRIEVING_REQUIRED_DOCUMENTS_ERROR, databaseException);
		}
	}

	public final void savePersonalityRequiredDocument(final Integer idPersonality, final Integer idRequiredDocument)
			throws BusinessException {
		try {
			this.requiredDocumentable.savePersonalityRequiredDocument(idPersonality, idRequiredDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_PROBLEM_SAVING_PERSONALITY_REQUIRED_DOCUMENT, databaseException);
			throw new BusinessException(MESSAGE_PROBLEM_SAVING_PERSONALITY_REQUIRED_DOCUMENT, databaseException);
		}
	}

	public final void deletePersonalityRequiredDocument(final Integer idRequiredDocument) throws BusinessException {
		try {
			this.requiredDocumentable.deletePersonalityRequiredDocument(idRequiredDocument);
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_PROBLEM_DELETE_PERSONALITY_REQUIRED_DOCUMENT, databaseException);
			throw new BusinessException(MESSAGE_PROBLEM_DELETE_PERSONALITY_REQUIRED_DOCUMENT, databaseException);
		}
	}

	public final List<RequiredDocument> findAllRequiredDocumentWithPersonalities() throws BusinessException {
		try {
			return this.requiredDocumentable.findAllRequiredDocumentWithPersonalities();
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_ALL_WITH_PERSONALITIES_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_ALL_WITH_PERSONALITIES_ERROR, databaseException);
		}
	}


	public List<RequiredDocument> findRequiredDocumentCatalogPaged(final RequiredDocument requiredDocument) 
			throws BusinessException {
		try {
			return this.requiredDocumentable.findAllRequiredDocumentCatalogPaged(requiredDocument, 
					requiredDocument.getNumberPage(), Integer.parseInt(this.configuration.findByName(
							ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
		} catch (DatabaseException databaseException) {
			LOG.error(MESSAGE_FIND_ALL_REQUIRED_DOCUMENT_CATALOG_PAGED_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_ALL_REQUIRED_DOCUMENT_CATALOG_PAGED_ERROR, databaseException);
		}
	}

	public RequiredDocument returnTotalPagesShowRequiredDocument(final RequiredDocument requiredDocument)
			throws NumberFormatException, BusinessException {
		try {
			final Long totalPages = this.requiredDocumentable.countTotalItemsToShowOfRequiredDocument(requiredDocument);
			final RequiredDocument requiredDocumentRes = new RequiredDocument();
			requiredDocumentRes.setNumberPage(this.configuration.totalPages(totalPages));
			requiredDocumentRes.setTotalRows(totalPages.intValue());
			return requiredDocumentRes;
		} catch (DatabaseException | NumberFormatException databaseException) {
			LOG.error(MESSAGE_FIND_TOTAL_PAGES_REQUIRED_DOCUMENT_ERROR, databaseException);
			throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_REQUIRED_DOCUMENT_ERROR, databaseException);
		}
	}

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		final List<RequiredDocument> requiredDocumentList = this.findAllRequiredDocumentWithPersonalities();
		return this.getExportRequiredDocumentMatrix(requiredDocumentList);
	}

	private String[][] getExportRequiredDocumentMatrix(final List<RequiredDocument> requiredDocumentListParameter) {
		final Integer columnsNumber = 4;
		final String[][] dataMatrix = new String[requiredDocumentListParameter.size() + 1][columnsNumber];
		dataMatrix[0][0] = "IdRequiredDocument";
		dataMatrix[0][1] = "Name";
		dataMatrix[0][2] = "Status";
		dataMatrix[0][NumbersEnum.THREE.getNumber()] = "IdPersonality";
		Integer index = 1;

		for (RequiredDocument declaration : requiredDocumentListParameter) {
			dataMatrix[index][0] = declaration.getIdRequiredDocument().toString();
			dataMatrix[index][1] = declaration.getName();
			dataMatrix[index][2] = StringUtils.getObjectStringValue(declaration.getStatus());
			dataMatrix[index][NumbersEnum.THREE.getNumber()] = 
					StringUtils.getObjectStringValue(declaration.getIdPersonality());
			index++;
		}

		return dataMatrix;
	}
}
