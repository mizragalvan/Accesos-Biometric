package mx.pagos.admc.contracts.business;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.DocumentTypeable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.admc.contracts.structures.DocumentType;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.DocumentTypeEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("DocumentTypeBusiness")
public class DocumentTypeBusiness extends AbstractExportable {
    private static final String MESSAGE_NAME_EXISTS_ERROR =
            "Hubo un priblema al determinar si el nombre del tipo de documento ya existe registrado";
    private static final String MESSAGE_EXPORTING_DOCUMENT_TYPE_ERROR =
            "Hubo un problema al exportar el catálogo de tipos de documento";
    private static final String MESSAGE_FIND_BY_AREA_STATUS_AND_DOCUMENT_TYPE_ERROR =
            "Hubo un problema al buscar por área, status y tipo de documento";
    private static final String DOT = ".";
    private static final String INVALID_CHARACTERS = "[\\\\/><\\|\\s\"'{}()\\[\\]:]+";
    private static final String UNDERSCORE = "_";
    private static final String MESSAGE_FIND_ALL_DOCUMENT_TYPE_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar tipos de documentos";
    private static final String MESSAGE_FIND_TOTAL_PAGES_DOCUMENT_TYPE_ERROR = 
            "Hubo un problema al buscar número de tipos de documentos";
    private static final Logger LOG = Logger.getLogger(DocumentTypeBusiness.class);
    
    @Autowired
    private DocumentTypeable documentTypeable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    @Autowired
    private UserSession session;
    
    public final Integer saveOrUpdate(final DocumentType documentType) throws BusinessException {
        try {
            this.saveTemplatesFile(documentType);
            final int idDocumentType = this.documentTypeable.saveOrUpdate(documentType);
            return idDocumentType;
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar datos de Tipo de Documento", dataBaseException);
            throw new BusinessException("Error al guardar los datos del Tipo de Documento", dataBaseException);
        }
    }

    private void saveTemplatesFile(final DocumentType documentType) throws BusinessException, DatabaseException {
        final DocumentType currentDocumentType = this.findDocumentTypeIfExists(documentType.getIdDocumentType());
        if (documentType.getIsNewTemplate()) {
            documentType.setTemplatePath(documentType.getTemplatePath().replaceAll(INVALID_CHARACTERS, UNDERSCORE));
            final Path originPath = this.createFilePath(new File(this.createUserTemporalPath()),
                        documentType.getTemplatePath());
            documentType.setTemplatePath(this.moveTemplateFileIfExists(documentType.getName(),
                    documentType.getTemplatePath(), originPath));
        } else {
            documentType.setTemplatePath(currentDocumentType.getTemplatePath());
        }
        if (documentType.getIsNewNaturalPersonTemplate()) {
            documentType.setTemplatePathNaturalPerson(
                    documentType.getTemplatePathNaturalPerson().replaceAll(INVALID_CHARACTERS, UNDERSCORE));
            final Path originPath = this.createFilePath(new File(this.createUserTemporalPath()),
                        documentType.getTemplatePathNaturalPerson());
            documentType.setTemplatePathNaturalPerson(this.moveTemplateFileIfExists(documentType.getName() + "_PF",
                    documentType.getTemplatePathNaturalPerson(), originPath));
        } else {
            documentType.setTemplatePathNaturalPerson(currentDocumentType.getTemplatePathNaturalPerson());
        }
    }
    
    private DocumentType findDocumentTypeIfExists(final Integer idDocumentType) {
        try {
            return this.documentTypeable.findById(idDocumentType);
        } catch (DatabaseException databaseException) {
            return new DocumentType();
        }
    }
    
    private String moveTemplateFileIfExists(final String documentTypeName, final String templatePath,
            final Path originPath) throws BusinessException {
        final String cleanedName = documentTypeName.replaceAll(INVALID_CHARACTERS, UNDERSCORE);
        final Path targetDocumentPath = this.createFilePath(new File(this.getDocumentTypeTemplatesPath()),
                cleanedName + DOT + FilenameUtils.getExtension(templatePath));
        try {
            Files.copy(originPath, targetDocumentPath, StandardCopyOption.REPLACE_EXISTING);
            return this.createFileTargetDocumentPath(templatePath, cleanedName);
        } catch (IOException ioException) {
        	LOG.error("=============================  ERROR  ======================================="
        			+ "\nEl archivo subido dejó de existir. Intente nuevamente");
            throw new BusinessException("El archivo subido dejó de existir. Intente nuevamente", ioException);
        }
    }
    
    private String createUserTemporalPath() throws BusinessException {
        return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_TMP
                + File.separator + this.session.getIdUsuarioSession();
    }
    
    private String getDocumentTypeTemplatesPath() throws BusinessException {
        return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString()) + Constants.PATH_DOCTYPE;
    }
    
    private Path createFilePath(final File userTemporalFilesPath, final String fileName) {
        return FileSystems.getDefault().getPath(userTemporalFilesPath.getAbsolutePath() + File.separator + fileName);
    }
    
    private String createFileTargetDocumentPath(final String fileName, final String newFileName)
            throws BusinessException {
        return this.getDocumentTypeTemplatesPath() + File.separator + newFileName + DOT
                + FilenameUtils.getExtension(fileName);
    }

    public final void changeDocumentTypeStatus(final Integer idDocumentType, final RecordStatusEnum status) 
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE) 
                this.documentTypeable.changeDocumentTypeStatus(idDocumentType, RecordStatusEnum.INACTIVE);	
            else
                this.documentTypeable.changeDocumentTypeStatus(idDocumentType, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar estatus de Tipo de Documento", dataBaseException);
            throw new BusinessException("Error al cambiar estatus del Tipo de Documento", dataBaseException);
        }
    }

    public final List<DocumentType> findAll() throws BusinessException {
        try {
            return this.documentTypeable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Tipos de Documento", dataBaseException);
            throw new BusinessException("Error al obtener datos del Tipo de Documento", dataBaseException);
        }
    }

    public final List<DocumentType> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.documentTypeable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Tipos de Documento por estatus", dataBaseException);
            throw new BusinessException("Error al obtener estatus del Tipo de Documento", dataBaseException);
        }
    }

    public final DocumentType findById(final Integer idDocumentType) throws BusinessException {
        try {
            return this.documentTypeable.findById(idDocumentType);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Tipo de Documento por Id", databaseException);            
            throw new BusinessException("Error al obtener Documento por id", databaseException);
        }
    }
    public final CatDocumentType findByIdDocumentCat(final Integer idDocumentType) throws BusinessException {
        try {
            return this.documentTypeable.findByIdCatDocument(idDocumentType);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Tipo de Documento por Id", databaseException);            
            throw new BusinessException("Error al obtener Documento por id", databaseException);
        }
    }
    
    public final Boolean nameExists(final String name) throws BusinessException {
        try {
            return this.documentTypeable.nameExists(name);
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_NAME_EXISTS_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_NAME_EXISTS_ERROR, dataBaseException);
        }
    }
    
    public final List<DocumentType> findDocumentByTypeStatusAndDocumentType(final DocumentType documentType) 
            throws BusinessException {
        try {
            return this.documentTypeable.findDocumentTypeStatusAndDocumentType(documentType,
                    documentType.getDocumentTypeEnum());
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_BY_AREA_STATUS_AND_DOCUMENT_TYPE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_BY_AREA_STATUS_AND_DOCUMENT_TYPE_ERROR, databaseException);
        }
    }
    
    
    public final List<DocumentType> findDocumentTypeCatalogPaged(final DocumentType documentType) 
            throws BusinessException {
        try {
        	LOG.info("DocumentTypeBusiness -> findDocumentTypeCatalogPaged :" + documentType.getName());
            return this.documentTypeable.findAllDocumentTypeCatalogPaged(documentType, 
                    documentType.getDocumentTypeEnum(), documentType.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_DOCUMENT_TYPE_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_DOCUMENT_TYPE_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final DocumentType returnTotalPagesShowDocumentType(final DocumentType documentType, 
            final DocumentTypeEnum documentTypeEnum) throws NumberFormatException, BusinessException {
        try {
        	LOG.info("DocumentTypeBusiness -> returnTotalPagesShowDocumentType : " + documentType.getName());
            final Long totalPages = this.documentTypeable.countTotalItemsToShowOfDocumentType(documentType, documentTypeEnum);
            final DocumentType documentTypeRes = new DocumentType();
            documentTypeRes.setNumberPage(this.configuration.totalPages(totalPages));
            documentTypeRes.setTotalRows(totalPages.intValue());
            return documentTypeRes;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_DOCUMENT_TYPE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_DOCUMENT_TYPE_ERROR, databaseException);
        }
    }

	@Override
	public final String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<DocumentType> documentTypeList = this.documentTypeable.findAll();
	        return this.getExportDocumentTypeMatrix(documentTypeList);
	    } catch (DatabaseException dataBaseException) {
	      LOG.error(MESSAGE_EXPORTING_DOCUMENT_TYPE_ERROR, dataBaseException);
	      throw new BusinessException(MESSAGE_EXPORTING_DOCUMENT_TYPE_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportDocumentTypeMatrix(final List<DocumentType> documentTypeListParameter) {
        final Integer columnsNumber = 7;
        final String[][] dataMatrix = new String[documentTypeListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdDocumentType";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "TemplatePath";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "Status";
        dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "DocumentTypeEnum";
        Integer index = 1;
        
        for (DocumentType documentType : documentTypeListParameter) {
            dataMatrix[index][0] = documentType.getIdDocumentType().toString();
            dataMatrix[index][1] = documentType.getName();
            dataMatrix[index][2] = documentType.getTemplatePath();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = 
            		StringUtils.getObjectStringValue(documentType.getStatus());
            dataMatrix[index][NumbersEnum.FOUR.getNumber()] = 
            		StringUtils.getObjectStringValue(documentType.getDocumentTypeEnum());
            index++;
        }
        
        return dataMatrix;
	}
}
