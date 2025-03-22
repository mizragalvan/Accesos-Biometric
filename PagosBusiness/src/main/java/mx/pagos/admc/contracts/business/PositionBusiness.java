package mx.pagos.admc.contracts.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.Positionable;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.Positions;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("PositionBusiness")
public class PositionBusiness extends AbstractExportable {
    private static final Logger LOG = Logger.getLogger(PositionBusiness.class);
    private static final String ERROR_MESSAGE_SAVE_POSITION = "Error al guardar puesto";
    private static final String ERROR_MESSAGE_CHANGE_STATUS = "Error al cambiar el estatus del puesto";
    private static final String ERROR_MESSAGE_FIND_BY_STATUS = "Error al obtener puestos por estatus";
    private static final String MESSAGE_EXPORTING_POSITION_ERROR =
    		"Hubo un problema al exportar el catálogo de posición";
    private static final String MESSAGE_FIND_ALL_POSITION_PAGED_ERROR =
            "Hubo un problema al buscar todos los puestos";
    private static final String MESSAGE_FIND_ALL_NUMBER_PAGES_ERROR =
            "Hubo un problema al buscar el total de paginas";

    @Autowired
    private Positionable positionable;
    
    @Autowired
    private ConfigurationsBusiness configuration;

    public final Integer saveOrUpdate(final Positions position) throws BusinessException {
        try {
            return this.positionable.saveOrUpdate(position);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_SAVE_POSITION);
            throw new BusinessException(ERROR_MESSAGE_SAVE_POSITION, databaseException);
        }
    }

    public final void changePositionStatus(final Integer idPosition, final RecordStatusEnum status) 
            throws BusinessException {
        try {
            if (status == RecordStatusEnum.ACTIVE)
                this.positionable.changePositionStatus(idPosition, RecordStatusEnum.INACTIVE);
            else
                this.positionable.changePositionStatus(idPosition, RecordStatusEnum.ACTIVE);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_CHANGE_STATUS);
            throw new BusinessException(ERROR_MESSAGE_CHANGE_STATUS, databaseException);
        }    
    }

    public final List<Positions> findByRecordStatus(final RecordStatusEnum status) throws BusinessException {
        try {
            return this.positionable.findByRecordStatus(status);
        } catch (DatabaseException databaseException) {
            LOG.error(ERROR_MESSAGE_FIND_BY_STATUS);
            throw new BusinessException(ERROR_MESSAGE_FIND_BY_STATUS, databaseException);
        }
    }

    public final List<Positions> findAll() throws BusinessException {
        try {
            LOG.debug("Se obtendra una lista de Puestos");
            return this.positionable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al obtener información", dataBaseException);
        }
    }

    public final Positions findById(final Integer idPositions) throws BusinessException {
        try {
            LOG.debug("Se obtendra Positions por id");
            return this.positionable.findPositionByIdPosition(idPositions);
        } catch (DatabaseException dataBaseException) {
            LOG.error(dataBaseException.getMessage(), dataBaseException);
            throw new BusinessException("Error al obtener puesto por id", dataBaseException);
        }
    }
    
    public final List<Positions> findPositionCatalogPaged(final Positions position) throws BusinessException {
        try {
            return this.positionable.findAllPositionCatalogPaged(position.getStatus(), position.getNumberPage(), 
                    Integer.parseInt(this.configuration.findByName(
                            ConfigurationEnum.NUMBERS_ITEM_BY_CATALOG_TO_SHOW.toString())));
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_POSITION_PAGED_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_POSITION_PAGED_ERROR, databaseException);
        }
    }
    
    public final Positions returnTotalPagesShowPosition(final RecordStatusEnum status)
            throws NumberFormatException, BusinessException {
        try {
            final Long totalPages = this.positionable.countTotalItemsToShowOfPosition(status);
            final Positions position = new Positions();
            position.setNumberPage(this.configuration.totalPages(totalPages));
            position.setTotalRows(totalPages.intValue());
            return position;
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_FIND_ALL_NUMBER_PAGES_ERROR, databaseException);
            throw new BusinessException(MESSAGE_FIND_ALL_NUMBER_PAGES_ERROR, databaseException);
        }
    }
    
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		try {
			final List<Positions> positionsList = this.positionable.findAll();
	        return this.getExportPositionsMatrix(positionsList);
	    } catch (DatabaseException dataBaseException) {
	      throw new BusinessException(MESSAGE_EXPORTING_POSITION_ERROR, dataBaseException);
	    }
	}

	private String[][] getExportPositionsMatrix(final List<Positions> positionsListParameter) {
        final Integer columnsNumber = 3;
        final String[][] dataMatrix = new String[positionsListParameter.size() + 1][columnsNumber];
        dataMatrix[0][0] = "IdPosition";
        dataMatrix[0][1] = "Name";
        dataMatrix[0][2] = "Status";
        Integer index = 1;
        
        for (Positions positions : positionsListParameter) {
            dataMatrix[index][0] = positions.getIdPosition().toString();
            dataMatrix[index][1] = positions.getName();
            dataMatrix[index][2] = positions.getStatus().toString();
            index++;
        }
        
        return dataMatrix;
	}
}
