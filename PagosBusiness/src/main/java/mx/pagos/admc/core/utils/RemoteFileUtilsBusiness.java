package mx.pagos.admc.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import mx.engineer.utils.file.RemoteFileUtils;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RemoteFileUtilsBusiness {
    
    private static final String MESSAGE_FILE_WRITTING_ERROR = "Hubo un problema al escribir el archivo en el servidor";
    private static final String MESSAGE_FILE_NOT_FOUND_ERROR = "El archivo especificado no existe";
    private static final Logger LOG = Logger.getLogger(RemoteFileUtilsBusiness.class);
    
    public final File getRemoteFile(final String filePath, final String targetPath) throws BusinessException {
        try {
            return RemoteFileUtils.getRemoteFile(filePath, targetPath);
        } catch (FileNotFoundException fileNotFoundException) {
            LOG.error(MESSAGE_FILE_NOT_FOUND_ERROR, fileNotFoundException);
            throw new BusinessException(MESSAGE_FILE_NOT_FOUND_ERROR, fileNotFoundException);
        } catch (IOException ioException) {
            LOG.error(MESSAGE_FILE_WRITTING_ERROR, ioException);
            throw new BusinessException(MESSAGE_FILE_WRITTING_ERROR, ioException);
        }
    }
}
