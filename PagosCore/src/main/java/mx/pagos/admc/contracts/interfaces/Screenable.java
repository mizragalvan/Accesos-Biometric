package mx.pagos.admc.contracts.interfaces;

import java.util.List;

import mx.pagos.admc.contracts.structures.Screen;
import mx.pagos.admc.enums.FlowPurchasingEnum;
import mx.pagos.admc.enums.RecordStatusEnum;
import mx.pagos.general.exceptions.DatabaseException;


/**
 * 
 * @author Mizraim
 * Interfaz que contiene los métodos con los que se interactuará con los DAOs de pantallas
 *
 * @see DatabaseException
 * @see RecordStatusEnum
 *
 */
public interface Screenable {
	
    void insertScreen(final Screen screen) throws DatabaseException;
    
    void updateScreen(final Screen screen) throws DatabaseException;

	void changeScreenStatus(final String factoryName, final RecordStatusEnum status) throws DatabaseException;
	
	List<Screen> findAll() throws DatabaseException;
	
	Screen findByFactoryName(final String factoryName) throws DatabaseException;
	
	Screen findByFactoryNameTray(final String factoryNameTray) throws DatabaseException;
	
	List<Screen> findByRecordStatus(final RecordStatusEnum status) throws DatabaseException;
	
	void deleteByFactoryName(String factoryName) throws DatabaseException;
	
	String findNameByFlowStatus(FlowPurchasingEnum flowStatus) throws DatabaseException;

    String findStageByStatusAndTurn(FlowPurchasingEnum flowStatus, Integer turn) throws DatabaseException;
}
