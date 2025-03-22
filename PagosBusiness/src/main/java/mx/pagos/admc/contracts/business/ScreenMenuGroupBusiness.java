package mx.pagos.admc.contracts.business;

import java.util.List;

import mx.pagos.admc.contracts.interfaces.ScreenMenuGroupable;
import mx.pagos.admc.contracts.structures.MenuGroup;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.contracts.structures.ScreenMenuGroup;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

public class ScreenMenuGroupBusiness {
	private ScreenMenuGroupable screenMenuGroupable;

	public ScreenMenuGroupBusiness(final ScreenMenuGroupable screenMenuGroupableParameter) {
		this.screenMenuGroupable = screenMenuGroupableParameter;
	}
	
	public final Integer saveOrUpdate(final ScreenMenuGroup screenMenuGroup) throws BusinessException {
		try {
			return this.screenMenuGroupable.saveOrUpdate(screenMenuGroup);
		} catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al guardar datos de pantalla de grupo de menú", dataBaseException);
		}
	}
	
	public final RecordStatusEnum changeScreenMenuGroupStatus(final Integer idSreenMenuGroup, 
			final RecordStatusEnum status) throws BusinessException {
		try {
			if (status == RecordStatusEnum.ACTIVE)
				return this.screenMenuGroupable.changeScreenMenuGroupStatus(idSreenMenuGroup,
				        RecordStatusEnum.INACTIVE);
            return this.screenMenuGroupable.changeScreenMenuGroupStatus(idSreenMenuGroup, RecordStatusEnum.ACTIVE);
		} catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al cambiar el es tatus de pantalla de grupo de menú", dataBaseException);
		}
	}
	
	public final List<ScreenMenuGroup> findAll() throws BusinessException {
		try {
			return this.screenMenuGroupable.findAll();
		} catch (DatabaseException dataBaseException) {
            throw new BusinessException("Error al obtener datos de pantalla de grupo de menú", dataBaseException);
		}
	}
	
	public final List<ScreenMenuGroup> findByScreen(final Screen screen) throws BusinessException {
		try {
			return this.screenMenuGroupable.findByScreen(screen);
		} catch (DatabaseException dataBaseException) {
			throw new BusinessException("Error al obtener datos del grupo por pantalla", dataBaseException);
		}
	}
	
	public final List<ScreenMenuGroup> findByMenuGroup(final MenuGroup menuGroup) throws BusinessException {
		try {
			return this.screenMenuGroupable.findByMenuGroup(menuGroup);
		} catch (DatabaseException dataBaseException) {
		   throw new BusinessException("Error al obtener datos del grupo por grupo de menú", dataBaseException);
		}
	}
		
	public final List<ScreenMenuGroup> findByRecordStatus(final RecordStatusEnum recordStatusEnum)
			throws BusinessException {
		try {
			return this.screenMenuGroupable.findByRecordStatus(recordStatusEnum);
		} catch (DatabaseException dataBaseException) {
	       throw new BusinessException("Error al obtener estatus de pantalla de grupo de menú", dataBaseException);
		}
	}	
}
