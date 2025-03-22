package mx.pagos.admc.core.business.digitalsignature;

import java.io.File;

import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.DirUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;


@Service
public class RutaPDFBusiness {
	
	private static final Logger LOG = Logger.getLogger(RutaPDFBusiness.class);
	private static final String MESSAGE_RETRIEVING_BASE_PATH_ERROR =
            "Hubo un problema al recuperar la ruta base para guardar los archivos";
	
	@Autowired
	Configurable configurable;
	
	@Autowired
    private UserSession userSession;
	
	
	
	public StringBuilder buildPath(final Integer idObject, final String objectName)
            throws BusinessException {
        final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
        pathBuilder.append(File.separator).append(objectName).append(File.separator);
        pathBuilder.append(File.separator).append(this.userSession.getIdFlow()).append(File.separator);
        pathBuilder.append(DirUtil.obtenRutaSolicitud(idObject)).append(File.separator);
        pathBuilder.append(idObject.toString());
        this.validateDirectory(pathBuilder.toString());
        return pathBuilder;
    }
	 public String getBasePath() throws BusinessException {
	        try {
	            return this.configurable.findByName("ROOT_PATH");
	        } catch (DatabaseException databaseException) {
	            LOG.error(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	            throw new BusinessException(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
	        }
	    }
	 public void validateDirectory(final String path) {
	        final File requisitionPath = new File(path);
	        if (!requisitionPath.exists())
	            requisitionPath.mkdirs();
	    }

}
