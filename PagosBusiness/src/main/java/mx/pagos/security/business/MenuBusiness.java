package mx.pagos.security.business;

import java.util.List;

import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.interfaces.Menuable;
import mx.pagos.security.structures.Menu;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuBusiness {
    private static final Logger LOG = Logger.getLogger(MenuBusiness.class);
    
    @Autowired
	private Menuable menuable;
	
	public final List<Menu> findAll() throws BusinessException {
		try {
			return this.menuable.findAll();
        } catch (DatabaseException dataBaseException) {
            LOG.error("Error al obtener datos", dataBaseException);
            throw new BusinessException("Ocurrió un error al obtener datos", dataBaseException);
        }
	}
}
