package mx.pagos.admc.contracts.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.pagos.admc.contracts.interfaces.CatalogDocumentType;
import mx.pagos.admc.contracts.interfaces.export.AbstractExportable;
import mx.pagos.admc.contracts.structures.CatDocumentType;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;

@Service("CatalogDocTypeBusiness")
public class CatalogDocTypeBusiness extends AbstractExportable{

	@Autowired
	private CatalogDocumentType documenTypeDAO;
	
	 public final List<CatDocumentType> findAll() throws BusinessException {
	        try {
	           // LOG.debug("Se obtendra una lista de areas");
	            return this.documenTypeDAO.findAll();
	        } catch (DatabaseException dataBaseException) {
	            //LOG.error(dataBaseException.getMessage(), dataBaseException);
	            throw new BusinessException("Error al obtener datos del Catalogo de Documentos", dataBaseException);
	        }
	    }
	
	
	@Override
	public String[][] getCatalogAsMatrix() throws BusinessException {
		// TODO Auto-generated method stub
		return null;
	}

}
