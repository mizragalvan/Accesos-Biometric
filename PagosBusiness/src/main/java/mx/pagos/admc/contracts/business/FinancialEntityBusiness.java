package mx.pagos.admc.contracts.business;

import java.util.ArrayList;
import java.util.List;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.FinancialEntityable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.FinancialEntitieCombination;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FinancialEntityBusiness")
public class FinancialEntityBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(FinancialEntityBusiness.class);
    private static final String MESSAGE_UPDATE_FINANCIAL_ENTITY_FIELDS_ERROR =
            "Hubo un problema al actualizar campos de entidades";
    private static final String MESSAGE_FIND_ALL_FINANCIAL_ENTITY_WITH_FOREIGN_KEY_ERROR =
            "Hubo un problema al buscar entidades con sus artículos de ley y leyes de confidencialidad";
    private static final String MESSAGE_FIND_ALL_FINANCIAL_ENTITY_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar entidades paginadas";
    private static final String MESSAGE_FIND_TOTAL_PAGES_FINANCIAL_ENTITY_ERROR = 
            "Hubo un problema al buscar número de pagínas de entidades";
    private static final String MESSAGE_RETRIVING_DATA_FINANCIAL_ENTITIES = 
    		"Error al obtener datos de las entidades seleccionadas";
    
    @Autowired
    private FinancialEntityable financialEntityAble;
    
    @Autowired
    private ConfigurationsBusiness configuration;

    public final Integer saveOrUpdate(final FinancialEntity financialEntity) throws BusinessException  {
        try {
            final Integer idFinancialEntity = this.financialEntityAble.saveOrUpdate(financialEntity);
            return idFinancialEntity;
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar datos de Entidad", dataBaseException);
            throw new BusinessException("Error en guardar o actualizar entidad", dataBaseException);
        }
    }

    public final void changeFinancialEntityStatus(final Integer idFinancialEntity, final RecordStatusEnum status) 
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.financialEntityAble.changeFinancialEntityStatus(idFinancialEntity, RecordStatusEnum.INACTIVE);
            else
                this.financialEntityAble.changeFinancialEntityStatus(idFinancialEntity, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar estatus de Entidad", dataBaseException);
            throw new BusinessException("Error al cambiar el estatus entidad", dataBaseException);
        }
    }

    public final List<FinancialEntity> findAll() throws BusinessException {
        try {
            return this.financialEntityAble.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Entidades", dataBaseException);
            throw new BusinessException("Error al obtener datos de entidad", dataBaseException);
        }
    }

    public final List<FinancialEntity> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.financialEntityAble.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Entidades por estatus", dataBaseException);
            throw new BusinessException("Error al obtener estatus de entidad", dataBaseException);
        }
    }

    public final FinancialEntity findByIdFinancialEntity(final Integer idFinancialEntity)
            throws BusinessException {
        try {
            return this.financialEntityAble.findByIdFinancialEntity(idFinancialEntity);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Entidad por Id", dataBaseException);
            throw new BusinessException("Error al obtener entidad por ID", dataBaseException);
        }
    }
    
    public final List<FinancialEntitieCombination> findFinancialEntityCombinationDistinctByCombinationName() 
    		throws BusinessException {
        try {
            return this.financialEntityAble.findFinancialEntityCombinationDistinctByCombinationName();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener las combinaciones de entidades", dataBaseException);
            throw new BusinessException("Error al obtener las combinaciones de entidades ",
            		dataBaseException);
        }
    }

	public final Boolean findIsCombination(final Integer idRequisition, final String combinationName)
			throws BusinessException {
        try {
            return this.financialEntityAble.findIsCombination(idRequisition, combinationName);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al verificar combinación de entidades", dataBaseException);
            throw new BusinessException("Error al verificar combinación de entidades ",
            		dataBaseException);
        }
	}

	public final List<FinancialEntity> findFinancialEntityByIdRequisition(final Integer idRequisition) 
			throws BusinessException {
        try {
            return this.financialEntityAble.findFinancialEntityByIdRequisition(idRequisition);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener las entidades por solicitud", dataBaseException);
            throw new BusinessException("Error al obtener las entidades por solicitud ",
            		dataBaseException);
        }
	}

	public final String findDefaultCombinationName() throws BusinessException  {
        try {
            return this.financialEntityAble.findDefaultCombinationName();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener la combinación por defecto de entidades", dataBaseException);
            throw new BusinessException("Error al obtener la combinación por defecto de entidades ",
            		dataBaseException);
        }
	}
        
    public final void updateDraftFields(final FinancialEntity entity) throws BusinessException {
        try {
            this.financialEntityAble.updateDraftFields(entity);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_UPDATE_FINANCIAL_ENTITY_FIELDS_ERROR, databaseException);
            throw new BusinessException(MESSAGE_UPDATE_FINANCIAL_ENTITY_FIELDS_ERROR, databaseException);
        }
    }
    
    public final List<FinancialEntity> findAllFinancialEntityWithForeigKeys() throws BusinessException {
        try {
            return this.financialEntityAble.findAllFinancialEntity();
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_FINANCIAL_ENTITY_WITH_FOREIGN_KEY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_FINANCIAL_ENTITY_WITH_FOREIGN_KEY_ERROR, databaseException);
        }
    }

    public final List<FinancialEntity> findFinancialEntityPaged(final FinancialEntity financialEntity) 
            throws BusinessException {
        try {
            return this.financialEntityAble.findAllFinancialEntityCatalogPaged(financialEntity, 
                    financialEntity.getNumberPage(), Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_FINANCIAL_ENTITY_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_FINANCIAL_ENTITY_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final FinancialEntity returnTotalPagesShowFinancialEntity(final FinancialEntity financialEntity)
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.financialEntityAble.countTotalItemsToShowOfFinancialEntity(financialEntity);
            final FinancialEntity entity = new FinancialEntity();
            entity.setNumberPage(this.configuration.totalPages(totalPages));
            entity.setTotalRows(totalPages.intValue());
            return entity;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_FINANCIAL_ENTITY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_FINANCIAL_ENTITY_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
	    final List<FinancialEntity> financialEntityList = this.findAllFinancialEntityWithForeigKeys();
	    return this.getExportFinancialEntityMatrix(financialEntityList);
	}

	private String[][] getExportFinancialEntityMatrix(final List<FinancialEntity> financialEntityListParameter) {
        final Integer columnsNumber = 7;
        final String[][] dataMatrix = new String[financialEntityListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdFinancialEntity";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "LongName";
        dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "Domicile";
        dataMatrix[0][NumbersEnum.FIVE.getNumber()] = "IdArticleOfLaw";
        dataMatrix[0][NumbersEnum.SIX.getNumber()] = "IdConfidentialityLaw";
        Integer index = 1;
        for (FinancialEntity financialEntity : financialEntityListParameter) {
            dataMatrix[index][0] = financialEntity.getIdFinancialEntity().toString();
            dataMatrix[index][1] = financialEntity.getName();
            dataMatrix[index][2] = financialEntity.getStatus().toString();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = 
            		StringUtils.getObjectStringValue(financialEntity.getLongName());
            dataMatrix[index][NumbersEnum.FOUR.getNumber()] = 
            		StringUtils.getObjectStringValue(financialEntity.getDomicile());
            dataMatrix[index][NumbersEnum.FIVE.getNumber()] =
                    StringUtils.getObjectStringValue(financialEntity.getIdArticleOfLaw());
            dataMatrix[index][NumbersEnum.SIX.getNumber()] =
                    StringUtils.getObjectStringValue(financialEntity.getIdConfidentialityLaw());
            index++;
        }
        return dataMatrix;
	}

    public List<FinancialEntity> findDataFinantialEntity(final List<Integer> financialEntitiesList) 
    		throws BusinessException {
    	try {
    		final List<FinancialEntity> dataFinancialEntitiesList = new ArrayList<>();
    		for (Integer idfinancialEntity: financialEntitiesList) {
    			dataFinancialEntitiesList.add(this.financialEntityAble.findByIdFinancialEntity(
    					idfinancialEntity));
    		}
    		return dataFinancialEntitiesList;
    	} catch (DatabaseException databaseException) {
    		LOG.error(MESSAGE_RETRIVING_DATA_FINANCIAL_ENTITIES, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_DATA_FINANCIAL_ENTITIES,
                    databaseException);
    	}
    }
    
    public List<FinancialEntity> findDataFinantialEntityRequisition(final Integer idRequisition) 
    		throws BusinessException {
    	try {
    		return this.financialEntityAble.findDataFinancialentityByRequisitionQuery(idRequisition);
    	} catch (DatabaseException databaseException) {
    		LOG.error(MESSAGE_RETRIVING_DATA_FINANCIAL_ENTITIES, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_DATA_FINANCIAL_ENTITIES,
                    databaseException);
    	}
    }
    
}
