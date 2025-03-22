package mx.engineer.utils.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.filefilter.AgeFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public final class FileUtils {
    private static FileFilter filesfilter = new FileFilter() {
        
        @Override
        public boolean accept(final File pathname) {
            return pathname.isFile();
        }
    };
    
    private FileUtils() { }
    
    public static List<String> getFolderFilesAsStringList(final String path) {
        final List<String> resultFileNamesList = new ArrayList<>();
        final File folder = new File(path);
        for (File file : folder.listFiles(filesfilter))
            resultFileNamesList.add(file.getAbsolutePath());
        return resultFileNamesList;
    }
    
    public static List<File> getAllFoldersFiles(final String path) {
        final List<File> resultFileNamesList = new ArrayList<>();
        final File folder = new File(path);
        getAllFoldersFiles(folder, resultFileNamesList);
        return resultFileNamesList;
    }
    
    private static void getAllFoldersFiles(final File path, final List<File> resultFilesList) {
        if (path.isFile())
            resultFilesList.add(path);
        else {
            for (File file : path.listFiles())
                getAllFoldersFiles(file, resultFilesList);
        }
    }
    
    public static void deleteAllFoldersOldFiles(final String path, final Integer olderThanDays) {
        final Calendar filesToDeleteDate = Calendar.getInstance();
        filesToDeleteDate.set(Calendar.HOUR_OF_DAY, 0);
        filesToDeleteDate.set(Calendar.MINUTE, 0);
        filesToDeleteDate.set(Calendar.SECOND, 0);
        filesToDeleteDate.set(Calendar.MILLISECOND, 0);
        filesToDeleteDate.add(Calendar.DATE, olderThanDays * -1);
        final File baseFolder = new File(path);
        @SuppressWarnings("unchecked")
        final Iterator<File> filesToDelete = (Iterator<File>) org.apache.commons.io.FileUtils.iterateFiles(baseFolder,
                new AgeFileFilter(filesToDeleteDate.getTime()), TrueFileFilter.TRUE);
        while (filesToDelete.hasNext()) {
            filesToDelete.next().delete();
        }
    }
    
    public static void deleteAllFoldersFilesOlderThanDate(final String path, final Date filesToDeleteDate) {
        final File baseFolder = new File(path);
        @SuppressWarnings("unchecked")
        final Iterator<File> filesToDelete = (Iterator<File>) org.apache.commons.io.FileUtils.iterateFiles(baseFolder,
                new AgeFileFilter(filesToDeleteDate.getTime()), TrueFileFilter.TRUE);
        while (filesToDelete.hasNext()) {
            filesToDelete.next().delete();
        }
    }
    
    public static ArrayList<File> orderFilesByDateOlderToNewer(final List<File> filesList) {
        final ArrayList<File> orderedFiles = new ArrayList<>(filesList);
        for (int j = 0; j < filesList.size(); j++) {
            for (int i = j + 1; i < filesList.size(); i++) {
                swapFilesWhenJOlderThanI(orderedFiles, j, i);
            }
        }
        return orderedFiles;
    }
    
    private static void swapFilesWhenJOlderThanI(final ArrayList<File> orderedFiles, int j, int i) {
        if (orderedFiles.get(j).lastModified() > orderedFiles.get(i).lastModified()) {
            swapFiles(orderedFiles, j, i);
        }
    }
    
    public static ArrayList<File> orderFilesByDateNewerToOlder(final List<File> filesList) {
        final ArrayList<File> orderedFiles = new ArrayList<>(filesList);
        for (int j = 0; j < filesList.size(); j++) {
            for (int i = j + 1; i < filesList.size(); i++) {
                swapFilesWhenIOlderThanJ(orderedFiles, j, i);
            }
        }
        return orderedFiles;
    }
    
    private static void swapFilesWhenIOlderThanJ(final ArrayList<File> orderedFiles, int j, int i) {
        if (orderedFiles.get(j).lastModified() < orderedFiles.get(i).lastModified()) {
            swapFiles(orderedFiles, j, i);
        }
    }

    private static void swapFiles(final ArrayList<File> orderedFiles, int j, int i) {
        final File temp = orderedFiles.get(j);
        orderedFiles.set(j, orderedFiles.get(i));
        orderedFiles.set(i, temp);
    }
}
