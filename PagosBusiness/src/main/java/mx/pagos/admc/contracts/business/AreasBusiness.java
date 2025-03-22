package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Areable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Area;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("AreasBusiness")
public class AreasBusiness extends AbstractExportable {
    private static final String MESSAGE_EXPORTING_AREAS_ERROR = "Hubo un problema al exportar el catálogo áreas";
    private static final String MESSAGE_FIND_ALL_AREAS_CATALOG_PAGED_ERROR = "Hubo un problema al buscar áreas";

    private static final Logger LOG = Logger.getLogger(AreasBusiness.class);
    
    @Autowired
    private Areable areable;

    @Autowired
    private ConfigurationsBusiness configuration;
    
    public final Integer saveOrUpdate(final Area area) throws BusinessException {
        try {
            LOG.debug("Se guardará un area");
            return this.areable.saveOrUpdate(area);
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al guardar datos del Área", dataBaseException);
        }
    }

    public final void changeAreaStatus(final Integer idArea, final RecordStatusEnum status) throws BusinessException {
        try {
            LOG.debug("Se cambiará el estatus");
            if (status == RecordStatusEnum.ACTIVE)
                this.areable.changeAreaStatus(idArea, RecordStatusEnum.INACTIVE);	
             else
                this.areable.changeAreaStatus(idArea, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al cambiar el estatus del Área", dataBaseException);
        }
    }    

    public final List<Area> findAll() throws BusinessException {
        try {
            LOG.debug("Se obtendra una lista de areas");
            return this.areable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al obtener datos del Área", dataBaseException);
        }
    }

    public final List<Area> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
            throws BusinessException {
        try {
            LOG.debug("Se obtendra una lista de áreas por estatus");
            return this.areable.findByRecordStatus(recordStatusEnum);
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al obtener estaus del Área", dataBaseException);
        }
    }
    
    public final List<Area> findByIdRequisition(final Integer idRequisition)
    		throws BusinessException {
    	try {
    		return this.areable.findByIdRequisition(idRequisition);
    	} catch (DatabaseException dataBaseException) {
    		LOG.error(dataBaseException.getMessage(), dataBaseException);
    		throw new BusinessException("Error al obtener áreas por solicitud ", dataBaseException);
    	}
    }

    public final Area findById(final Integer idArea) throws BusinessException {
        try {
            LOG.debug("Se obtendra una lista de áreas por id");
            return this.areable.findById(idArea);
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al obtener Áreas por id", dataBaseException);
        }
    }
    
    public final List<Area> findAreasCatalogPaged(final Area area) throws BusinessException {
        try {
            return this.areable.findAllAreasCatalogPaged(area.getStatus(), area.getPageNumber(), Integer.parseInt(
                    this.configuration.findByName(ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_AREAS_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_AREAS_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    public final Area returnTotalPagesShowAreas(final RecordStatusEnum status)
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.areable.countTotalItemsToShowOfAreas(status);
            final Area area = new Area();
            area.setPageNumber(this.configuration.totalPages(totalPages));
            area.setTotalRows(totalPages.intValue());
            return area;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_AREAS_CATALOG_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_AREAS_CATALOG_PAGED_ERROR, databaseException);
        }
    }
    
    @Override
	public final String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Area> areasList = this.areable.findAll();
	        return this.getExportAreasMatrix(areasList);
	    } catch (DatabaseException dataBaseException) {
	      LOG.error(MESSAGE_EXPORTING_AREAS_ERROR, dataBaseException);
	      throw new BusinessException(MESSAGE_EXPORTING_AREAS_ERROR, dataBaseException);
	    }
	}
    
    private String[][] getExportAreasMatrix(final List<Area> areasList) {
        final Integer columnsNumber = 3;
        final String[][] dataMatrix = new String[areasList.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdArea";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        Integer index = 1;
        for (Area area : areasList) {
            dataMatrix[index][0] = area.getIdArea().toString();
            dataMatrix[index][1] = area.getName();
            dataMatrix[index][2] = area.getStatus().toString();
            index++;
        }
        return dataMatrix;
    }
}
