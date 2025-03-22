package mx.pagos.admc.core.utils;

import java.io.File;

import mx.pagos.admc.core.interfaces.Configurable;
import mx.pagos.general.exceptions.BusinessException;
import mx.pagos.general.exceptions.DatabaseException;
import mx.pagos.security.structures.UserSession;
import mx.pagos.util.DirUtil;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectoryUtils {
    
    private static final String MESSAGE_RETRIEVING_BASE_PATH_ERROR =
            "Hubo un problema al recuperar la ruta base para guardar los archivos";
    private static final Logger LOG = Logger.getLogger(DirectoryUtils.class);
    @Autowired
    private Configurable configurable;
    
    @Autowired
    private UserSession userSession;
    
    public final File getRequisitionPath(final Integer idRequisition) throws BusinessException {
        final StringBuilder requisitionPathBuilder = this.buildPath(idRequisition, "Solicitudes");
        return new File(requisitionPathBuilder.toString());
    }
    
    public final File getSupplierPath(final Integer idRequisition) throws BusinessException {
        final StringBuilder supplierPathBuilder = this.buildSupplierPath(idRequisition, "Proveedores");
        return new File(supplierPathBuilder.toString());
    }
    
    public final File getClientPath(final Integer idRequisition) throws BusinessException {
        final StringBuilder requisitionPathBuilder = this.buildPath(idRequisition, "Clientes");
        return new File(requisitionPathBuilder.toString());
    }
    public final File getArchivoPathFinal(final Integer idRequisition) throws BusinessException {
        final StringBuilder requisitionPathBuilder = this.buildPath(idRequisition, "ArchivosFinales");
        return new File(requisitionPathBuilder.toString());
    }

    public final File getPath(final Integer id, final String folderName) throws BusinessException {
        final StringBuilder pathBuilder = this.buildPath(id, folderName);
        return new File(pathBuilder.toString());
    }
    
    private StringBuilder buildPath(final Integer idObject, final String objectName)
            throws BusinessException {
        final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
        pathBuilder.append(File.separator).append(objectName).append(File.separator);
        pathBuilder.append(File.separator).append(this.userSession.getIdFlow()).append(File.separator);
        pathBuilder.append(DirUtil.obtenRutaSolicitud(idObject)).append(File.separator);
        pathBuilder.append(idObject.toString());
        this.validateDirectory(pathBuilder.toString());
        return pathBuilder;
    }
    
    private StringBuilder buildSupplierPath(final Integer idObject, final String objectName) throws BusinessException {
        final StringBuilder pathBuilder = new StringBuilder(this.getBasePath());
        pathBuilder.append(File.separator).append(objectName).append(File.separator);
        pathBuilder.append(idObject.toString());
        this.validateDirectory(pathBuilder.toString());
        return pathBuilder;
    }

    private void validateDirectory(final String path) {
        final File requisitionPath = new File(path);
        if (!requisitionPath.exists())
            requisitionPath.mkdirs();
    }
    
    private String getBasePath() throws BusinessException {
        try {
            return this.configurable.findByName("ROOT_PATH");
        } catch (DatabaseException databaseException) {
            LOG.error(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
            throw new BusinessException(MESSAGE_RETRIEVING_BASE_PATH_ERROR, databaseException);
        }
    }
}
