package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.ManagerialDocumentTypeable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.ManagerialDocumentType;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.ManagerialDocumentTypeEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("ManagerialDocumentTypeBusiness")
public class ManagerialDocumentTypeBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(ManagerialDocumentTypeBusiness.class);
    private static final String SAVE_OR_UPDATE_MESSAGE_ERROR = 
            "Hubo un error al tratar de guardar los datos del tipo de documento"; 
    private static final String CHANGE_STATUS_MESSAGE_ERROR = 
            "Hubo un error al cambiar el estatus del tipo de documento";
    private static final String FIND_ALL_MESSAGER_ERROR = "Hubo un error al buscar todos los tipos de documentos";
    private static final String FIND_BY_STATUS_MESSAGE_ERROR = 
            "Hubo un error al buscar tipos de documentos por estatus";
    private static final String FIND_BY_ID_MESSAGE_ERROR = "Hubo un error al buscar por id";
    private static final String FIND_BY_DOCUMENT_TYPE = "Hubo un error  al buscar documentos por tipo de documento";
    private static final String MESSAGE_EXPORTING_MANAGERIAL_DOCUMENT_TYPE_ERROR =
            "Hubo un problema al exportar el catálogo de tipo de documento de gestión";
    private static final String MESSAGE_FIND_ALL_MANAGERIAL_DOCUMENT_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar tipos de documentos paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_MANAGERIAL_DOCUMENT_ERROR = 
            "Hubo un problema al buscar número de pagínas de tipos de documentos";

    @Autowired
    private ManagerialDocumentTypeable managerialDocumentTypeable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public final Integer saveOrpdateManagerialDocumentType(final ManagerialDocumentType managerialDocumentType) 
            throws BusinessException {
        try {
            if (managerialDocumentType.getIdManagerialDocumentType() == null)
                return this.managerialDocumentTypeable.save(managerialDocumentType);
            else
                return this.managerialDocumentTypeable.update(managerialDocumentType);
        } catch (DatabaseException databaseException) {
            LOG.error(SAVE_OR_UPDATE_MESSAGE_ERROR, databaseException);
            throw new BusinessException(SAVE_OR_UPDATE_MESSAGE_ERROR, databaseException);
        }
    }
    
    public final void changeManagerialDocumentTypeStatus(final Integer idManagerialDocumentType, 
            final RecordStatusEnum status) throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.managerialDocumentTypeable.changeStatus(idManagerialDocumentType, RecordStatusEnum.INACTIVE);
            else
                this.managerialDocumentTypeable.changeStatus(idManagerialDocumentType, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error(CHANGE_STATUS_MESSAGE_ERROR, databaseException);
            throw new BusinessException(CHANGE_STATUS_MESSAGE_ERROR, databaseException);
        }
    }
    
    public final List<ManagerialDocumentType> findAll() throws BusinessException {
        try {
            return this.managerialDocumentTypeable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_ALL_MESSAGER_ERROR, databaseException);
            throw new BusinessException(FIND_ALL_MESSAGER_ERROR, databaseException);
        }
    }
    
    public final List<ManagerialDocumentType> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.managerialDocumentTypeable.findByStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_BY_STATUS_MESSAGE_ERROR, databaseException);
            throw new BusinessException(FIND_BY_STATUS_MESSAGE_ERROR, databaseException);
        }
    }
    
    public final ManagerialDocumentType findById(final Integer idManagerialDocumetType) throws BusinessException {
        try {
            return this.managerialDocumentTypeable.findById(idManagerialDocumetType);
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_BY_ID_MESSAGE_ERROR, databaseException);
            throw new BusinessException(FIND_BY_ID_MESSAGE_ERROR, databaseException);
        }
    }
    
    public final List<ManagerialDocumentType> findByDocumentType(final ManagerialDocumentTypeEnum documentType) 
            throws BusinessException {
        try {
            return this.managerialDocumentTypeable.findManagerialDocumentByDocumentType(documentType);
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_BY_DOCUMENT_TYPE, databaseException);
            throw new BusinessException(FIND_BY_DOCUMENT_TYPE, databaseException);
        }
    }
    
    public final List<ManagerialDocumentType> findManagerialDocumentTypeCatalogPaged(
            final ManagerialDocumentType managerialDocumentType) throws BusinessException {
        try {
            return this.managerialDocumentTypeable.findAllManagerialDocumentTypeCatalogPaged(
                    managerialDocumentType.getStatus(), managerialDocumentType.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_MANAGERIAL_DOCUMENT_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_MANAGERIAL_DOCUMENT_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final ManagerialDocumentType returnTotalPagesShowManagerialDocumentType(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = 
                    this.managerialDocumentTypeable.countTotalItemsToShowOfManagerialDocumentType(status);
            final ManagerialDocumentType documentType = new ManagerialDocumentType();
            documentType.setNumberPage(this.configuration.totalPages(totalPages));
            documentType.setTotalRows(totalPages.intValue());
            return documentType;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_MANAGERIAL_DOCUMENT_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_MANAGERIAL_DOCUMENT_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<ManagerialDocumentType> managerialDocumentTypeList = 
					this.managerialDocumentTypeable.findAll();
	        return this.getExportManagerialDocumentTypeMatrix(managerialDocumentTypeList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_MANAGERIAL_DOCUMENT_TYPE_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportManagerialDocumentTypeMatrix(
			final List<ManagerialDocumentType> managerialDocumentTypeListParameter) {
        final Integer columnsNumber = 5;
        final String[][] dataMatrix = new String[managerialDocumentTypeListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdManagerialDocumentType";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Url";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "Status";
        dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "DocumentType";
        Integer index = 1;
        
        for (ManagerialDocumentType managerialDocumentType : managerialDocumentTypeListParameter) {
            dataMatrix[index][0] = managerialDocumentType.getIdManagerialDocumentType().toString();
            dataMatrix[index][1] = managerialDocumentType.getName();
            dataMatrix[index][2] = managerialDocumentType.getUrl();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = managerialDocumentType.getStatus().toString();
            dataMatrix[index][NumbersEnum.FOUR.getNumber()] = 
            		StringUtils.getObjectStringValue(managerialDocumentType.getDocumentType());
            index++;
        }
        
        return dataMatrix;
	}
}
