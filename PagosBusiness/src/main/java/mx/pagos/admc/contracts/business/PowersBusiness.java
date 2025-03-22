package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.engineer.utils.string.StringUtils;
import mx.pagos.admc.contracts.interfaces.Powerable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Power;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.NumbersEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("PowersBusiness")
public class PowersBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(PowersBusiness.class);
    private static final String MESSAGE_EXPORTING_POWER_ERROR =
            "Hubo un problema al exportar el catálogo de poderes";
    private static final String MESSAGE_FIND_ALL_POWER_CATALOG_PAGED_ERROR = 
            "Hubo un problema al buscar poderes paginados";
    private static final String MESSAGE_FIND_TOTAL_PAGES_POWER_ERROR = 
            "Hubo un problema al buscar número de pagínas de poderes";

    @Autowired
    private Powerable powerable;
    
    @Autowired
    private ConfigurationsBusiness configuration;

    public final Integer saveOrUpdate(final Power power) throws BusinessException {
        try {
            return this.powerable.saveOrUpdate(power);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar los datos del Poder", dataBaseException);
            throw new BusinessException("Error al guardar datos del Poder", dataBaseException);
        }
    }

    public final void changePowerStatus(final Integer idPower, final RecordStatusEnum status)
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.powerable.changePowerStatus(idPower, RecordStatusEnum.INACTIVE);
            else
                this.powerable.changePowerStatus(idPower, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar estatus del Poder", dataBaseException);
            throw new BusinessException("Error al cambiar el estatus del Poder", dataBaseException);
        }
    }

    public final List<Power> findAll() throws BusinessException {
        try {
            return this.powerable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Poderes", dataBaseException);
            throw new BusinessException("Error al obtener datos del Poder", dataBaseException);
        }
    }

    public final List<Power> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            return this.powerable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Poderes por estatus", dataBaseException);
            throw new BusinessException("Error al obtener estatus del Poder", dataBaseException);
        }
    }

    public final Power findByIdPower(final Integer idPower) throws BusinessException {
        try {
            return this.powerable.findByIdPower(idPower);
        } catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Poder por Id", databaseException);
            throw new BusinessException("Error al buscar por id de poderes", databaseException);
        }
    }
    
    public final List<Power> findByIdFinancialEntity(final Integer idFinancialEntity)
            throws BusinessException {
        try {
            return this.powerable.findByIdFinancialEntity(idFinancialEntity);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Poderes por entidad", dataBaseException);
            throw new BusinessException("Error al obtener Poderes por entidad ", dataBaseException);
        }
    }
    
    public final List<Power> findByIdFinancialEntityAndIdLegalRepresentative(final Integer idFinancialEntity, 
    		final Integer idLegalRepresentative)
            throws BusinessException {
        try {
            return this.powerable.findByIdFinancialEntityAndIdLegalRepresentative(idFinancialEntity, 
            		idLegalRepresentative);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Poderes por entidad y representante legal", dataBaseException);
            throw new BusinessException("Error al obtener Poderes por entidad y representante legal ", 
            		dataBaseException);
        }
    }
    
    public final List<Power> findPowerCatalogPaged(final Power power) throws BusinessException {
        try {
            return this.powerable.findAllPowerCatalogPaged(power.getStatus(), power.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_POWER_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_POWER_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Power returnTotalPagesShowPower(final RecordStatusEnum status)
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.powerable.countTotalItemsToShowOfPower(status);
            final Power power = new Power();
            power.setNumberPage(this.configuration.totalPages(totalPages));
            power.setTotalRows(totalPages.intValue());
            return power;
        } catch (DatabaseException | NumberFormatException databaseException) {
            LOG.error(MESSAGE_FIND_TOTAL_PAGES_POWER_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_TOTAL_PAGES_POWER_ERROR, databaseException);
        }
    }

	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Power> powerList = this.powerable.findAll();
	        return this.getExportPowerMatrix(powerList);
	    } catch (DatabaseException dataBaseException) {
	      LOG.error(MESSAGE_EXPORTING_POWER_ERROR, dataBaseException);
	      throw new BusinessException(MESSAGE_EXPORTING_POWER_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportPowerMatrix(final List<Power> powerListParameter) {
        final Integer columnsNumber = 5;
        final String[][] dataMatrix = new String[powerListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdPower";
        dataMatrix[0][1] = "IdFinancialEntity";
        dataMatrix[0][2] = "Name";
        dataMatrix[0][NumbersEnum.THREE.getNumber()] = "Status";
        dataMatrix[0][NumbersEnum.FOUR.getNumber()] = "FinancialName";
        Integer index = 1;
        
        for (Power power : powerListParameter) {
            dataMatrix[index][0] = power.getIdPower().toString();
            dataMatrix[index][1] = power.getIdFinancialEntity().toString();
            dataMatrix[index][2] = power.getName();
            dataMatrix[index][NumbersEnum.THREE.getNumber()] = power.getStatus().toString();
            dataMatrix[index][NumbersEnum.FOUR.getNumber()] = 
            		StringUtils.getObjectStringValue(power.getFinancialName());
            index++;
        }
        
        return dataMatrix;
	}
}
