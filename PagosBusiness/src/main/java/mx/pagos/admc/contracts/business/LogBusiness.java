package mx.pagos.admc.contracts.business;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mx.engineer.utils.file.FileUtils;
import mx.pagos.admc.contracts.structures.LogFile;
import mx.pagos.admc.core.business.ConfigurationsBusiness;
import mx.pagos.admc.enums.ConfigurationEnum;
import mx.pagos.admc.util.shared.Constants;
import mx.pagos.general.exceptions.BusinessException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogBusiness {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String MESSAGE_DELETING_LOGS_ERROR = "Hubo un problema al eliminar los archivos de log";
    private static final int LOGS_FILES_TO_KEEP = 3;

    private static final Logger LOG = Logger.getLogger(LogBusiness.class);
    
    @Autowired
    private ConfigurationsBusiness configuration;

    public final List<LogFile> getFolderLogFiles() throws BusinessException {
        LOG.debug("Se buscaran todos los archivos de la carpeta logs");
        final List<LogFile> logFileList = new ArrayList<>();
        for (final String file :
            FileUtils.getFolderFilesAsStringList(this.getLogsPath())) {
            final LogFile logFile = new LogFile();
            logFile.setFileName(new File(file).getName());
            logFile.setPath(file);
            logFileList.add(logFile);
        }
        return logFileList;
    }
    
    public final void deleteOlderLogFilesThanDate(final String olderThanDate, final String format)
            throws BusinessException {
        LOG.info("Se borrarán todos los archivos de la carpeta logs a apartir de la fecha " + olderThanDate);
        final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            final Date date = dateFormat.parse(olderThanDate);
            final ArrayList<File> orderedFilesList =
                    FileUtils.orderFilesByDateNewerToOlder(FileUtils.getAllFoldersFiles(this.getLogsPath()));
           this.deleteFilesWhenMoreThanFileToKeep(date, orderedFilesList);
        } catch (ParseException parseException) {
            LOG.error(MESSAGE_DELETING_LOGS_ERROR, parseException);
            throw new BusinessException(MESSAGE_DELETING_LOGS_ERROR, parseException);
        }
    }

    private void deleteFilesWhenMoreThanFileToKeep(final Date date,
            final ArrayList<File> orderedFilesList) throws BusinessException, ParseException {
        if (orderedFilesList.size() > LOGS_FILES_TO_KEEP) {
            final Date lastModified = new Date(orderedFilesList.get(2).lastModified());
               date.setTime(lastModified.before(date) ?
                       orderedFilesList.get(2).lastModified() : date.getTime());
               this.deleteLogFiles(orderedFilesList, date);
           }
    }
    
    private void deleteLogFiles(final ArrayList<File> orderedFilesList, final Date date) throws ParseException {
        for (File file : orderedFilesList) {
            this.extractLogDateAndDeleteFileWhenOlder(file, date);
        }
    }

    private void extractLogDateAndDeleteFileWhenOlder(final File file, final Date date) throws ParseException {
        final String logDateString = this.getLogDateString(file);
        if (logDateString.length() > 0) {
            final SimpleDateFormat simpleFormat = new SimpleDateFormat(DATE_FORMAT);
            final Date logDate = simpleFormat.parse(logDateString);
            this.deleteLogWhenOlderThanDate(file, date, logDate);
        }
    }

    private void deleteLogWhenOlderThanDate(final File file, final Date date, final Date logDate) {
        if (logDate.before(date))
            file.delete();
    }

    private String getLogDateString(final File file) {
        final Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
        final String fileName = FilenameUtils.getName(file.getName());
        final Matcher matcher = pattern.matcher(fileName);
        final String logDateString = matcher.find() ? matcher.group(0) : "";
        return logDateString;
    }

    private String getLogsPath() throws BusinessException {
        return this.configuration.findByName(ConfigurationEnum.ROOT_PATH.toString())
                + Constants.LOG + File.separator;
    }
}
