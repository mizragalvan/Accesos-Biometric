package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.MenuGroup;
import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.contracts.structures.ScreenMenuGroup;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface ScreenMenuGroupable {
	
	Integer saveOrUpdate(final ScreenMenuGroup screenMenuGroup) throws DatabaseException;

	RecordStatusEnum changeScreenMenuGroupStatus(final Integer idScreenMenuGroup, final RecordStatusEnum status) 
			throws DatabaseException;
	
	List<ScreenMenuGroup> findAll() throws DatabaseException;
	
	List<ScreenMenuGroup> findByScreen(final Screen screen) throws DatabaseException;
	
	List<ScreenMenuGroup> findByMenuGroup(final MenuGroup menuGroup) throws DatabaseException;
	
	List<ScreenMenuGroup> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;

}
