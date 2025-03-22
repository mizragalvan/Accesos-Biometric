package mx.pagos.admc.contracts.business;

import java.util.ArrayList;
import java.util.List;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.LegalRepresentativeable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.FinancialEntity;
import mx.pagos.admc.contracts.structures.LegalRepresentative;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LegalRepresentativeBusiness")
public class LegalRepresentativeBusiness extends AbstractExportable {

    private static final Logger LOG = Logger.getLogger(LegalRepresentativeBusiness.class);

    private static final String MESSAGE_SAVING_LEGAL_REPRESENTATIVE_FINANCIAL_ENTITIES_ERROR =
            "Hubo un problema al guardar las entidades del representante legal";
    private static final String MESSAGE_RETRIVING_FINANCIAL_ENTITIES_BY_LEGAL_REPRESENTATIVE_ERROR =
            "Hubo un problema al recuperar las entidades del representante legal";
    private static final String MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_BY_FINANCIAL_ENTITY_AND_DGA_ERROR =
            "Error al obtener Representantes Legales por DGA y Entidad";
    private static final String MESSAGE_RETRIVING_POWERS_BY_LEGAL_AND_FINANCIAL_ERROR =
            "Hubo un problema al obtener poderes por Representante Legal";
    private static final String MESSAGE_EXPORTING_LEGAL_REPRESENTATIVE_ERROR =
            "Hubo un problema al exportar el catálogo de representantes legales";
    private static final String MESSAGE_RETRIVING_REQUISITION_FINANCIAL_ENTITIES_BY_LEGAL_REPRESENTATIVE_ERROR =
            "Hubo un problema al recuperar los representantes legales de las entidades de la solicitud";
    private static final String MESSAGE_FIND_ALL_LEGAL_REPRESENTATIVE_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar representantes legales paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_LEGAL_REPRESENTATIVE_ERROR = 
            "Hubo un problema al buscar número de pagínas de representantes legales";

    @Autowired
    private LegalRepresentativeable legalRepresentativeable;
    
    @Autowired
    private ConfigurationsBusiness configuration;
    
    public final Integer saveOrUpdate(final LegalRepresentative legalRepresentative) throws BusinessException {
        try {
            final Integer idLegal = this.legalRepresentativeable.saveOrUpdate(legalRepresentative);
            this.legalRepresentativeable.deleteFinantialEntitiesByIdLegalRepresentative(idLegal);
            for (FinancialEntity financialEntity: legalRepresentative.getFinancialEntitiesList()) {
                this.saveLegalRepresentativePowersByFinancialEntity(
                		idLegal, financialEntity);
                this.legalRepresentativeable.saveLegalRepresentativeFinancialEntity(idLegal, 
                		financialEntity.getIdFinancialEntity());
            }
            return idLegal;
        } catch (DatabaseException databaseException) {
            LOG.error("Error al guardar los datos del Representante Legal", databaseException);
            throw new BusinessException("Error al guardar datos del Representante Legal", databaseException);
        }
    }

    private void saveLegalRepresentativePowersByFinancialEntity(final Integer idLegalRepresentative,
            final FinancialEntity financialEntity) throws DatabaseException {
        financialEntity.getPower().setIdLegalRepresentative(idLegalRepresentative);
        financialEntity.getPower().setIdFinancialEntity(financialEntity.getIdFinancialEntity());
        this.saveOrUpdatePower(financialEntity.getPower());
    }
    
    private void saveOrUpdatePower(final Power power) throws DatabaseException {
        if (power.getIdPower() != null)
            this.legalRepresentativeable.updatePower(power);
        else
            this.legalRepresentativeable.savePower(power);
    }

	public final void changeLegalRepresentativeStatus(final Integer idLegalRepresentative, 
            final RecordStatusEnum status) throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.legalRepresentativeable.changeLegalRepresentativeStatus(idLegalRepresentative, 
                        RecordStatusEnum.INACTIVE);
            else 
                this.legalRepresentativeable.changeLegalRepresentativeStatus(idLegalRepresentative, 
                        RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al cambiar el estatus del Representante Legal", databaseException);
            throw new BusinessException("Error al cambiar el estatus", databaseException);
        }
    }

    public final List<LegalRepresentative> findAll() throws BusinessException {
        try {
            return this.legalRepresentativeable.findAll();
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Representantes Legales", databaseException);
            throw new BusinessException("Error al obtener datos del Representante Legal", databaseException);
        }
    }

    public final List<LegalRepresentative> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.legalRepresentativeable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Representantes Legales por estatus", databaseException);
            throw new BusinessException("Error al obtener estatus del Representante Legal", databaseException);
        }
    }

    public final List<LegalRepresentative> findLegalRepresentativeByFinancialEntity( 
    		final List<Integer> financialEntitiesList) throws BusinessException {
        try {
            List<LegalRepresentative> legalRepresentativesList = new ArrayList<>();
            if (this.isListFilled(financialEntitiesList))
                legalRepresentativesList = this.legalRepresentativeable.findByDgaAndFinantialEntity(
                		financialEntitiesList);
            return legalRepresentativesList;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_BY_FINANCIAL_ENTITY_AND_DGA_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_LEGAL_REPRESENTATIVES_BY_FINANCIAL_ENTITY_AND_DGA_ERROR,
                    databaseException);
        }
    }

    private boolean isListFilled(final List<Integer> financialEntitiesList) {
        return financialEntitiesList.size() > 0;
    }

    public final Power findPowersByIdLegalRepresentativeAndIdFinancialEntity(final Integer idlegalrepresentative,
            final Integer idFinancialEntity) throws BusinessException {
        try {
            return this.legalRepresentativeable.findPowersByIdLegalRepresentativeAndIdFinancialEntity(
                    idlegalrepresentative, idFinancialEntity);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_POWERS_BY_LEGAL_AND_FINANCIAL_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_POWERS_BY_LEGAL_AND_FINANCIAL_ERROR, databaseException);
        }
    }

    public final LegalRepresentative findByIdLegalRepresentative(final Integer idLegalRepresentative)
            throws BusinessException {
        try {
            return this.legalRepresentativeable.findByIdLegalRepresentative(idLegalRepresentative);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Representante Legal por Id", databaseException);
            throw new BusinessException("Error al obtener representantes legales", databaseException);
        }
    }

    public final void saveLegalRepresentativeFinancialEntity(final Integer idLegalRepresentative,
            final List<Integer> idFinancialEntitiesList) throws BusinessException {
        try {
            this.legalRepresentativeable.deleteFinantialEntitiesByIdLegalRepresentative(idLegalRepresentative);
            for (Integer idFinancialEntity : idFinancialEntitiesList)
                this.legalRepresentativeable.saveLegalRepresentativeFinancialEntity(
                        idLegalRepresentative, idFinancialEntity);
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_SAVING_LEGAL_REPRESENTATIVE_FINANCIAL_ENTITIES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_SAVING_LEGAL_REPRESENTATIVE_FINANCIAL_ENTITIES_ERROR,
                    databaseException);
        }
    }
    
    public final List<FinancialEntity> findFinantialEntitiesByIdLegalRepresentative(
            final Integer idLegalRepresentative) throws BusinessException {
        try {
            final List<FinancialEntity> financialEntitiesList =
                    this.legalRepresentativeable.findFinantialEntitiesByIdLegalRepresentative(idLegalRepresentative);
            this.setFinancialEntityLegalRepresentativesPowers(idLegalRepresentative, financialEntitiesList);
            return financialEntitiesList;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_FINANCIAL_ENTITIES_BY_LEGAL_REPRESENTATIVE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_FINANCIAL_ENTITIES_BY_LEGAL_REPRESENTATIVE_ERROR,
                    databaseException);
        }
    }

    private void setFinancialEntityLegalRepresentativesPowers(final Integer idLegalRepresentative,
            final List<FinancialEntity> financialEntitiesList)throws BusinessException {
        for (FinancialEntity financialEntity : financialEntitiesList) 
            financialEntity.setPower(
                    this.findPowersByIdLegalRepresentativeAndIdFinancialEntity(idLegalRepresentative,
                            financialEntity.getIdFinancialEntity()));
    }
    
    public final List<FinancialEntity> findRequisitionFinantialEntitiesByIdLegalRepresentative(
            final Integer idRequisition, final Integer idLegalRepresentative) throws BusinessException {
        try {
            final List<FinancialEntity> financialEntitiesList =
                    this.legalRepresentativeable.findRequisitionFinantialEntitiesByIdLegalRepresentative(
                            idRequisition, idLegalRepresentative);
            this.setFinancialEntityLegalRepresentativesPowers(idLegalRepresentative, financialEntitiesList);
            return financialEntitiesList;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIVING_REQUISITION_FINANCIAL_ENTITIES_BY_LEGAL_REPRESENTATIVE_ERROR,
                    databaseException);
            throw new BusinessException(MESSAGE_RETRIVING_REQUISITION_FINANCIAL_ENTITIES_BY_LEGAL_REPRESENTATIVE_ERROR,
                    databaseException);
        }
    }

    public final List<LegalRepresentative> findLegalRepresentativeCatalogPaged(
            final LegalRepresentative legalRepresentative) throws BusinessException {
        try {
            return this.legalRepresentativeable.findAllLegalRepresentativeCatalogPaged(legalRepresentative, 
                    legalRepresentative.getNumberPage(), Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_LEGAL_REPRESENTATIVE_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_LEGAL_REPRESENTATIVE_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final LegalRepresentative returnTotalPagesShowLegalRepresentative(
            final LegalRepresentative legalRepresentative) throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = 
                    this.legalRepresentativeable.countTotalItemsToShowOfLegalRepresentative(legalRepresentative);
            final LegalRepresentative legal = new LegalRepresentative();
            legal.setNumberPage(this.configuration.totalPages(totalPages));
            legal.setTotalRows(totalPages.intValue());
            return legal;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_LEGAL_REPRESENTATIVE_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_LEGAL_REPRESENTATIVE_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<LegalRepresentative> legalRepresentativeList = 
					this.legalRepresentativeable.findAll();
	        return this.getExportLegalRepresentativeMatrix(legalRepresentativeList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_LEGAL_REPRESENTATIVE_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportLegalRepresentativeMatrix(
			final List<LegalRepresentative> legalRepresentativeListParameter) {
        final Integer columnsNumber = 5;
        final String[][] dataMatrix = new String[legalRepresentativeListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdLegalRepresentative";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "IdDga";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "dgaName";
        dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "Status";
        Integer index = 1;
        
        for (LegalRepresentative legalRepresentative : legalRepresentativeListParameter) {
            dataMatrix[index][0] = legalRepresentative.getIdLegalRepresentative().toString();
            dataMatrix[index][1] = legalRepresentative.getName();
            dataMatrix[index][2] = StringUtils.getObjectStringValue(legalRepresentative.getIdDga());
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = 
            		StringUtils.getObjectStringValue(legalRepresentative.getDgaName());
            dataMatrix[index][NumbersEnum.FOUR.getNumber()] = 
            		StringUtils.getObjectStringValue(legalRepresentative.getStatus());
            index++;
        }
        
        return dataMatrix;
	}
}

