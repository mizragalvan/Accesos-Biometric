package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.CheckDocumentationable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("CheckDocumentationBusiness")
public class CheckDocumentationBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(CheckDocumentationBusiness.class);
    private static final String ERROR_MESSAGE_FIND_CHECK_DOCUMENTATION_BY_CATEGORY = 
            "Hubo un error al buscar Checklist de documentos por categoría";
    private static final String SAVE_OR_UPDATE_MESSAGE_ERROR = "Hubo un problema al guardar los datos del Checklist";
    private static final String CHANGE_STATUS_MESSAGE_ERROR = "Hubo un problema al cambiar el estatus del Checklist";
    private static final String FIND_ALL_MESSAGE_ERROR = "Hubo un problema al buscar todos los Checklists";
    private static final String FIND_BY_STATUS_MESSAGE_ERROR = "Hubo un problema al buscar los Checklists por estatus";
    private static final String FIND_BY_ID_MESSAGE_ERROR = "Hubo un problea al buscar Checklist por id";
    private static final String MESSAGE_EXPORTING_CHECK_DOCUMENTATION_ERROR =
            "Hubo un problema al exportar el catálogo de check de documentación";
    private static final String MESSAGE_FIND_ALL_CHECK_DOCUMENTATION_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar Checklist paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_CHECK_DOCUMENTATION_ERROR = 
            "Hubo un problema al buscar número de pagínas de Checklist";
    
    @Autowired
    private CheckDocumentationable checkDocumentationable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public final Integer saveOrUpdate(final CheckDocumentation checkDocumentation) throws BusinessException {
        try {
            return this.checkDocumentationable.saveOrUpdate(checkDocumentation);
        } catch (DatabaseException dataBaseException) {
            LOG.error(SAVE_OR_UPDATE_MESSAGE_ERROR, dataBaseException);
            throw new BusinessException(SAVE_OR_UPDATE_MESSAGE_ERROR, dataBaseException);
        }
    }

    public final void changeCheckDocumentationStatus(final Integer idCheckDocumentation, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.checkDocumentationable.changeStatus(idCheckDocumentation, RecordStatusEnum.INACTIVE);  
            else
                this.checkDocumentationable.changeStatus(idCheckDocumentation, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error(CHANGE_STATUS_MESSAGE_ERROR, dataBaseException);
            throw new BusinessException(CHANGE_STATUS_MESSAGE_ERROR, dataBaseException);
        }
    }

    public final List<CheckDocumentation> findAll() throws BusinessException {
        try {
            return this.checkDocumentationable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error(FIND_ALL_MESSAGE_ERROR, dataBaseException);
            throw new BusinessException(FIND_ALL_MESSAGE_ERROR, dataBaseException);
        }
    }

    public final List<CheckDocumentation> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.checkDocumentationable.findByStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error(FIND_BY_STATUS_MESSAGE_ERROR, dataBaseException);
            throw new BusinessException(FIND_BY_STATUS_MESSAGE_ERROR, dataBaseException);
        }
    }

    public final CheckDocumentation findById(final Integer idCheckDocumentation) throws BusinessException {
        try {
            return this.checkDocumentationable.findById(idCheckDocumentation);
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_BY_ID_MESSAGE_ERROR, databaseException);
            throw new BusinessException(FIND_BY_ID_MESSAGE_ERROR, databaseException);
        }
    }
    
    public final List<CheckDocumentation> findCheckDocumentationByCategory(final Integer idCategory) 
            throws BusinessException {
        try {
            return this.checkDocumentationable.findCheckDocumentationByCategory(idCategory);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_CHECK_DOCUMENTATION_BY_CATEGORY, databaseException);
            throw new BusinessException(ERROR_MESSAGE_FIND_CHECK_DOCUMENTATION_BY_CATEGORY, databaseException);
        }
    }
    
    public final List<CheckDocumentation> findCheckDocumentationCatalogPaged(
            final CheckDocumentation checkDocumentation) throws BusinessException {
        try {
            return this.checkDocumentationable.findAllCheckDocumentationCatalogPaged(checkDocumentation.getStatus(), 
                    checkDocumentation.getNumberPage(), Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_CHECK_DOCUMENTATION_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_CHECK_DOCUMENTATION_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final CheckDocumentation returnTotalPagesShowCheckDocumentation(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.checkDocumentationable.countTotalItemsToShowOfCheckDocumentation(status);
            final CheckDocumentation checkDocumentation = new CheckDocumentation();
            checkDocumentation.setNumberPage(this.configuration.totalPages(totalPages));
            checkDocumentation.setTotalRows(totalPages.intValue());
            return checkDocumentation;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_CHECK_DOCUMENTATION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_CHECK_DOCUMENTATION_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<CheckDocumentation> checkDocumentationList = this.checkDocumentationable.findAll();
	        return this.getExportCheckDocumentationMatrix(checkDocumentationList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_CHECK_DOCUMENTATION_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportCheckDocumentationMatrix(
			final List<CheckDocumentation> checkDocumentationListParameter) {
        final Integer columnsNumber = 3;
        final String[][] dataMatrix = new String[checkDocumentationListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdCheckDocumentation";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        Integer index = 1;
        
        for (CheckDocumentation checkDocumentation : checkDocumentationListParameter) {
            dataMatrix[index][0] = checkDocumentation.getIdCheckDocumentation().toString();
            dataMatrix[index][1] = checkDocumentation.getName();
            dataMatrix[index][2] = checkDocumentation.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
