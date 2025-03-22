package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.Screenable;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Mizraim
 * 
 * Clase que contiene las reglas de negocio de las pantallas.
 * Permite guardar, recuperar y cambiar estatus de la0s pantallas;
 * 
 * @see Screen
 * @see Screenable
 * @see BusinessException
 * @see RecordStatusEnum
 *
 */
@Service
public class ScreensBusiness {
    private static final String MESSAGE_RETRIVING_STAGE_ERROR = "Hubo un problema al recuperar la etapa";
    private static final Logger LOG = Logger.getLogger(ScreensBusiness.class);
    @Autowired
	private Screenable screenable;
	
	public final void saveOrUpdate(final Screen screen) throws BusinessException {
		try {
		    if (this.isInsert(screen)) 
		        this.screenable.insertScreen(screen);
		    else
		        this.screenable.updateScreen(screen);
		} catch (DatabaseException dataBaseException) {
            LOG.error("Error al guardar los datos de la Pantalla", dataBaseException);
			throw new BusinessException("Error al guardar datos de la pantalla", dataBaseException);
		}
	}
	
	private boolean isInsert(final Screen screen) throws DatabaseException {
	    try {
            this.screenable.findByFactoryName(screen.getFactoryName());
            return false;
        } catch (DatabaseException databaseException) {
            if (!databaseException.getCause().getClass().equals(EmptyResultDataAccessException.class))
                throw databaseException;
        }
        return true;
    }

    public final void changeScreenStatus(final String factoryName, final RecordStatusEnum status)
			throws BusinessException {
		try {
			if (status == RecordStatusEnum.ACTIVE)
				this.screenable.changeScreenStatus(factoryName, RecordStatusEnum.INACTIVE);	
			else
			    this.screenable.changeScreenStatus(factoryName, RecordStatusEnum.ACTIVE);
		} catch (DatabaseException dataBaseException) {
            LOG.error("Error al cambiar estatus de la Pantalla", dataBaseException);
		    throw new BusinessException("Error al cambiar el estatus de la pantalla", dataBaseException);
		}
	}
	
	public final List<Screen> findAll() throws BusinessException {
		try {
			return this.screenable.findAll();
		} catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Pantallas", dataBaseException);
			throw new BusinessException("Error al obtener datos de la pantalla", dataBaseException);
		}
	}
	
	public final Screen findByFactoryName(final String factoryName) throws BusinessException {
	    try {
	        return this.screenable.findByFactoryName(factoryName);
	    } catch (DatabaseException databaseException) {
	        LOG.error("Error al obtener Pantalla por Id", databaseException);
	        throw new BusinessException("Error al obtener pantalla por id", databaseException);
	    }
	}
	
	public final Screen findByFactoryNameTray(final String factoryNameTray) throws BusinessException {
		try {
			return this.screenable.findByFactoryNameTray(factoryNameTray);
		} catch (DatabaseException databaseException) {
            LOG.error("Error al obtener Pantalla por bandeja", databaseException);
            throw new BusinessException("Error al obtener los datos de la pantalla por bandeja", databaseException);
		}
	}
	
	public final List<Screen> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
			throws BusinessException {
		try {
			return this.screenable.findByRecordStatus(recordStatusEnum);
		} catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener Pantallas por estatus", dataBaseException);
			throw new BusinessException("Error al obtener estatus de la pantalla", dataBaseException);
		}
	}
	
	public final String findNameByFlowStatus(final FlowPurchasingEnum flowStatus) throws BusinessException {
	    try {
            return this.screenable.findNameByFlowStatus(flowStatus);
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener el nombre de la bandeja relacionado al estatus del flujo", dataBaseException);
            throw new BusinessException("Error al obtener el nombre de la bandeja en el estatus de la solicitud",
                    dataBaseException);
        }
	}
	
	public final String findStageByStatusAndTurn(final FlowPurchasingEnum flowStatus, final Integer turn)
            throws BusinessException {
	    try {
	        return this.screenable.findStageByStatusAndTurn(flowStatus, turn);
	    } catch (DatabaseException dataBaseException) {
            LOG.error(MESSAGE_RETRIVING_STAGE_ERROR, dataBaseException);
            throw new BusinessException(MESSAGE_RETRIVING_STAGE_ERROR, dataBaseException);
	    }
	}
}
