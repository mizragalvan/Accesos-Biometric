package mx.pagos.admc.contracts.business.owners;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.interfaces.owners.OrganizationEntitiable;
import mx.pagos.admc.contracts.structures.owners.OrganizationEntity;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.general.exceptions.EmptyResultException;

@Service("OrganizationEntityBusiness")
public class OrganizationEntityBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(OrganizationEntityBusiness.class);

    private static final String MESSAGE_SAVING_ORGANIZATION_ENTITY_ERROR = "Hubo un problema al guardar la entidad";
    private static final String MESSAGE_SAVING_ORGANIZATION_ENTITY_STATUS_ERROR =
            "Hubo un problema al guardar el estatus de la entidad";
    private static final String MESSAGE_OGANIZATION_ENTITY_NOT_EXISTS_ERROR = "La entidad ha dejado de existir";
    private static final String MESSAGE_RETRIVING_ORGANIZATION_ENTITY_BY_ID_ERROR =
            "Hubo un problema al recuperar la entidad";
    private static final String MESSAGE_RETRIVING_ALL_ORGANIZATION_ENTITY_ERROR =
            "Hubo un problema al recuperar todas las entidades";
    private static final String MESSAGE_RETRIVING_ORGANIZATION_ENTITY_BY_ESTATUS_ERROR =
            "Hubo un problema al recuperar las entidades por estatus";
    private static final String MESSAGE_EXPORTING_ORGANIZATION_ENTITY_ERROR =
            "Hubo un problema al exportar el catálogo de entidades de la organización";
    private static final String MESSAGE_FIND_ALL_ORGANIZATION_ENTITY_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar entidades paginadas";
    private static final String MESSAGE_FIND_TOTAL_PAGES_ORGANIZATION_ENTITY_ERROR = 
            "Hubo un problema al buscar número de pagínas de entidades";
    
    @Autowired
    private OrganizationEntitiable organizationEntitiable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public Integer saveOrUpdate(final OrganizationEntity organizationEntity) throws BusinessException {
        try {
            return this.organizationEntitiable.saveOrUpdate(organizationEntity);
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_SAVING_ORGANIZATION_ENTITY_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_SAVING_ORGANIZATION_ENTITY_ERROR, dataBaseException);
        }
    }
    
    public void changeStatus(final Integer idOrganizationEntity, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.organizationEntitiable.changeStatus(idOrganizationEntity, RecordStatusEnum.INACTIVE);
            else
                this.organizationEntitiable.changeStatus(idOrganizationEntity, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_SAVING_ORGANIZATION_ENTITY_STATUS_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_SAVING_ORGANIZATION_ENTITY_STATUS_ERROR, dataBaseException);
        }
    }
    
    public OrganizationEntity findById(final Integer idOrganizationEntity) throws BusinessException {
        try {
            return this.organizationEntitiable.findById(idOrganizationEntity);
        } catch (EmptyResultException emptyResultException) {
            LOG.error(MESSAGE_OGANIZATION_ENTITY_NOT_EXISTS_ERROR, emptyResultException);
            throw new BusinessException(MESSAGE_OGANIZATION_ENTITY_NOT_EXISTS_ERROR, emptyResultException);
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_RETRIVING_ORGANIZATION_ENTITY_BY_ID_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_RETRIVING_ORGANIZATION_ENTITY_BY_ID_ERROR, dataBaseException);
        }
    }
    
    public List<OrganizationEntity> findAll() throws BusinessException {
        try {
            return this.organizationEntitiable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_RETRIVING_ALL_ORGANIZATION_ENTITY_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_RETRIVING_ALL_ORGANIZATION_ENTITY_ERROR, dataBaseException);
        }
    }
    
    public List<OrganizationEntity> findByStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.organizationEntitiable.findByStatus(status);
        } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_RETRIVING_ORGANIZATION_ENTITY_BY_ESTATUS_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_RETRIVING_ORGANIZATION_ENTITY_BY_ESTATUS_ERROR, dataBaseException);
        }
    }
    
    public List<OrganizationEntity> findOrganizationEntityCatalogPaged(final OrganizationEntity organizationEntity) 
            throws BusinessException {
        try {
            return this.organizationEntitiable.findAllOrganizationEntityCatalogPaged(organizationEntity.getStatus(), 
                    organizationEntity.getNumberPage(), Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_ORGANIZATION_ENTITY_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_ORGANIZATION_ENTITY_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public OrganizationEntity returnTotalPagesShowOrganizationEntity(final RecordStatusEnum status) 
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.organizationEntitiable.countTotalItemsToShowOfOrganizationEntity(status);
            final OrganizationEntity organizationEntity = new OrganizationEntity();
            organizationEntity.setNumberPage(this.configuration.totalPages(totalPages));
            organizationEntity.setTotalRows(totalPages.intValue());
            return organizationEntity;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_ORGANIZATION_ENTITY_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_ORGANIZATION_ENTITY_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<OrganizationEntity> organizationEntityList = this.organizationEntitiable.findAll();
	        return this.getExportOrganizationEntityMatrix(organizationEntityList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_ORGANIZATION_ENTITY_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportOrganizationEntityMatrix(
			final List<OrganizationEntity> organizationEntityListParameter) {
        final Integer columnsNumber = 3;
        final String[][] dataMatrix = new String[organizationEntityListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdOrganizationEntity";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        Integer index = 1;
        
        for (OrganizationEntity organizationEntity : organizationEntityListParameter) {
            dataMatrix[index][0] = organizationEntity.getIdOrganizationEntity().toString();
            dataMatrix[index][1] = organizationEntity.getName();
            dataMatrix[index][2] = organizationEntity.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
