package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.Categoriable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.interfaces.owners.RequisitionOwnersable;
import mx.pagos.admc.contracts.structures.Category;
import mx.pagos.admc.contracts.structures.owners.CheckDocumentation;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("CategoriesBusiness")
public class CategoriesBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(CategoriesBusiness.class);
    private static final String FIND_CHECK_DOCUMENTATION_IDS_ERROR = 
            "Hubo un error al buscar los checklist por categoría";
    private static final String MESSAGE_FIND_ALL_WITH_CHECK_DOCUMENTATION_ERROR =
            "Hubo un problema al buscar las categoría con sus checks de documentos";
    private static final String MESSAGE_FIND_ALL_CATEGORY_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar categorias de documentos paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_CATEGORY_ERROR = 
            "Hubo un problema al buscar número de pagínas de categorias de documentos";

    @Autowired
    private Categoriable categoriable;

    @Autowired
    private RequisitionOwnersable requisitionOwnersable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public Integer saveOrUpdate(final Category category) throws BusinessException {
        try {
            final Integer idCategory = this.categoriable.saveOrUpdate(category);
            final List<CheckDocumentation> checkDocumentationList = 
                    this.findRequisitionOwnerCheckDocumentAndDelete(idCategory);
            this.categoriable.deleteCategoryCheckDocumentation(idCategory);
            for (final Integer idCheckDocumentation : category.getIdCheckDocumentationList()) {
                final Integer id = this.categoriable.saveCategoryCheckDocumentation(idCheckDocumentation, idCategory);
                this.saveCheckDocumentation(idCategory, checkDocumentationList, idCheckDocumentation, id);
            }
            return idCategory;
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar los datos de la Categoría", dataBaseException);
            throw new BusinessException("Error al guardar datos de Categoría", dataBaseException);
        }
    }

    private void saveCheckDocumentation(final Integer idCategory, final List<CheckDocumentation> checkDocumentationList,
            final Integer ... idCheckDocumentation) throws DatabaseException {
        for (final CheckDocumentation checkDoc : checkDocumentationList)
            this.validCheckDocumentation(idCategory, checkDoc, idCheckDocumentation);
    }

    private void validCheckDocumentation(final Integer idCategory, final CheckDocumentation checkDoc,
            final Integer... idCheckDocumentation) throws DatabaseException {
        if (checkDoc.getIdCategory().equals(idCategory) &&
                checkDoc.getIdCheckDocumentation().equals(idCheckDocumentation[0]))
            this.requisitionOwnersable.insertCheckList(checkDoc.getIdRequisitionOwners(), idCheckDocumentation[1]);
    }

    private List<CheckDocumentation> findRequisitionOwnerCheckDocumentAndDelete(final Integer idCategory) 
            throws DatabaseException {
        final List<CheckDocumentation> checkDocumentationList = 
                this.categoriable.findOwnerCheckDocumentationByCategory(idCategory);
        for (final CheckDocumentation checkDocumentation : checkDocumentationList)
            this.categoriable.deleteRequisitionOwnerCheckDocumentation(
                    checkDocumentation.getIdCheckDocumentation(), checkDocumentation.getIdCategory());
        return checkDocumentationList;
    }

    public final void changeCategoryStatus(final Integer idCategory, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.categoriable.changeCategoryStatus(idCategory, RecordStatusEnum.INACTIVE);	
            else
                this.categoriable.changeCategoryStatus(idCategory, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar el estatus de la Categoría", dataBaseException);
            throw new BusinessException("Error al cambiar el estatus de Categoría", dataBaseException);
        }
    }

    public final List<Category> findAll() throws BusinessException {
        try {
            return this.categoriable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Categorías", dataBaseException);
            throw new BusinessException("Error al obtener datos de la Categoría", dataBaseException);
        }
    }

    public final List<Category> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.categoriable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Categorías por estatus", dataBaseException);
            throw new BusinessException("Error al obtener estatus de la Categoría", dataBaseException);
        }
    }

    public final Category findById(final Integer idCategory) throws BusinessException {
        try {
            return this.categoriable.findById(idCategory);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Categoría por Id", databaseException);
            throw new BusinessException("Error al obtener Categoria por id", databaseException);
        }
    }

    public final List<Integer> findCheckDocumentationIdsByCategory(final Integer idCategory) throws BusinessException {
        try {
            return this.categoriable.findCheckDocumentationIdsByCategory(idCategory);
        } catch (DatabaseException databaseException) {
            LOG.error(FIND_CHECK_DOCUMENTATION_IDS_ERROR, databaseException);
            throw new BusinessException(FIND_CHECK_DOCUMENTATION_IDS_ERROR, databaseException);
        }
    }
    
    public final List<Category> findAllCategoriesWithCheckDocumentation() throws BusinessException {
        try {
            return this.categoriable.findAllCategoriesWithCheckDocumentation();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_WITH_CHECK_DOCUMENTATION_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_WITH_CHECK_DOCUMENTATION_ERROR, databaseException);
        }
    }
    
    public final List<Category> findCategoryCatalogPaged(final Category category) throws BusinessException {
        try {
            return this.categoriable.findAllCategoryCatalogPaged(category.getStatus(), category.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_CATEGORY_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_CATEGORY_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Category returnTotalPagesShowDga(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.categoriable.countTotalItemsToShowOfCategory(status);
            final Category category = new Category();
            category.setNumberPage(this.configuration.totalPages(totalPages));
            category.setTotalRows(totalPages.intValue());
            return category;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_CATEGORY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_CATEGORY_ERROR, databaseException);
        }
    }

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
	    final List<Category> categoryList = this.findAllCategoriesWithCheckDocumentation();
	    return this.getExportCategoryMatrix(categoryList);
	}

	private String[][] getExportCategoryMatrix(final List<Category> categoryListParameter) {
        final Integer columnsNumber = 4;
        final String[][] dataMatrix = new String[categoryListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdCategory";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "IdCheckDocumentation";
        Integer index = 1;
        for (Category category : categoryListParameter) {
            dataMatrix[index][0] = category.getIdCategory().toString();
            dataMatrix[index][1] = category.getName();
            dataMatrix[index][2] = category.getStatus().toString();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] =
                    StringUtils.getObjectStringValue(category.getIdCheckDocumentation());
            index++;
        }
        return dataMatrix;
	}
}
