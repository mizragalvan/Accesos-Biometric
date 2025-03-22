package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.MenuGroup;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;

public interface MenuGroupable {

	Integer saveOrUpdate(final MenuGroup group) throws DatabaseException;

	RecordStatusEnum changeFlowStatus(final Integer idGroup, final RecordStatusEnum status) 
			throws DatabaseException;
	
	List<MenuGroup> findAll() throws DatabaseException;
	
	List<MenuGroup> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
}
