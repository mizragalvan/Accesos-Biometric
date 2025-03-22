package mx.pagos.admc.core.interfaces;

import java.util.List;

import mx.pagos.admc.core.enums.Category;
import mx.pagos.admc.core.structures.Configuration;
import mx.pagos.general.exceptions.DatabaseException;

public interface Configurable {
	void update(Configuration config) throws DatabaseException;
	
	void updateValue(Configuration config) throws DatabaseException;
	
	String findByName(String name) throws DatabaseException;
	
	List<Configuration> findByNameList(List<String> name) throws DatabaseException;
	
	List<Configuration> findAll() throws DatabaseException;
	
	List<Configuration> findConfigurationByCategory(Category categoryType) throws DatabaseException;
}
