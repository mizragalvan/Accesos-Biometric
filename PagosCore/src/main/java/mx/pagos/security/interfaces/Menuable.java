package mx.pagos.security.interfaces;

import java.util.List;

import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.Menu;

public interface Menuable {
    
    String save(Menu menu) throws DatabaseException;

	List<Menu> findAll() throws DatabaseException;

	void deleteByFactoryName(String factoryName) throws DatabaseException;

}
