package mx.solsersistem.utils.test.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.solsersistem.utils.file.FileUtils;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class FileUtilsTest {
    private static final String THIRD_FILE_NAME = "adjunto_img.png";
    private static final String SECOND_FILE_NAME = "Actividad.docx";
    private static final String FIRST_FILE_NAME = "Actividad.doc";
    private static final String FILE_001_JPG = "001.jpg";
    private static final String ROOT_PATH_VALUE = "TestFiles";
    private static final String AGED_FILES_PATH_VALUE =
            ROOT_PATH_VALUE + File.separator + "AgedFiles"  + File.separator;
    
    @Test
    public final void whenGetFolderFilesThenRetunFilesList() {
        final List<String> filesList = FileUtils.getFolderFilesAsStringList(ROOT_PATH_VALUE);
        this.validateStringListOfFilesOnly(filesList);
        Assert.assertFalse("Error: Archivo /TestFiles/TestFolder/001.jpg listado",
                this.isFileInStringList(FILE_001_JPG, filesList));
    }
    
    @Test
    public final void whenGetAllFoldersFilesthenReturnAllFilesInMainFolderAndSubfolders() {
        final List<File> filesList = FileUtils.getAllFoldersFiles(ROOT_PATH_VALUE);
        this.validateFilesListOfFilesOnly(filesList);
        Assert.assertTrue("Error a listar el archivo /TestFiles/TestFolder/001.jpg",
                this.isFileInFileList(FILE_001_JPG, filesList));
        Assert.assertTrue("Error a listar el archivo /TestFiles/TestFolder/variadordevelocidad.jpg",
                this.isFileInFileList("variadordevelocidad.jpg", filesList));
    }
    
    @Ignore
    @Test
    public final void whenDeleteAllFoldersOldFilesThenOldFilesdeleted() throws IOException {
       this.generateAgedFiles();
       final Integer olderThanDays = 2;
       FileUtils.deleteAllFoldersOldFiles(AGED_FILES_PATH_VALUE, olderThanDays);
       final File firstFile = new File(AGED_FILES_PATH_VALUE + FIRST_FILE_NAME);
       final File secondFile = new File(AGED_FILES_PATH_VALUE + SECOND_FILE_NAME);
       final File thirdFile = new File(AGED_FILES_PATH_VALUE + THIRD_FILE_NAME);
       Assert.assertTrue("El Archivo /TestFiles/AgedFiles/Actividad.doc no fué encontrado", firstFile.exists());
       Assert.assertTrue("El Archivo /TestFiles/AgedFiles/Actividad.docx no fué encontrado", secondFile.exists());
       Assert.assertFalse("El Archivo /TestFiles/AgedFiles/adjunto_img.png no fué borrado", thirdFile.exists());
       firstFile.delete();
       secondFile.delete();
       thirdFile.delete();
    }
    
    private void generateAgedFiles() throws IOException {
        final Integer threeOldDays = 3;
       this.copyToAgedFiles(new File(ROOT_PATH_VALUE + File.separator + FIRST_FILE_NAME),
                new File(AGED_FILES_PATH_VALUE + FIRST_FILE_NAME), 1);
       this.copyToAgedFiles(new File(ROOT_PATH_VALUE + File.separator + SECOND_FILE_NAME),
               new File(AGED_FILES_PATH_VALUE + SECOND_FILE_NAME), 2);
       this.copyToAgedFiles(new File(ROOT_PATH_VALUE + File.separator + THIRD_FILE_NAME),
               new File(AGED_FILES_PATH_VALUE + THIRD_FILE_NAME), threeOldDays);
    }
    
    private void copyToAgedFiles(final File sourceFile, final File targetFile, final Integer agedDays)
            throws IOException {
        org.apache.commons.io.FileUtils.copyFile(sourceFile, targetFile);
        final Calendar creationDateCalendar = Calendar.getInstance();
        creationDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
        creationDateCalendar.set(Calendar.MINUTE, 0);
        creationDateCalendar.set(Calendar.SECOND, 0);
        creationDateCalendar.set(Calendar.MILLISECOND, 0);
        creationDateCalendar.add(Calendar.DATE, agedDays * -1);
        this.setFileCreationDate(targetFile.getAbsolutePath(), creationDateCalendar.getTime());
    }

    private void setFileCreationDate(final String filePath, final Date creationDate) throws IOException {
        final BasicFileAttributeView attributes =
                Files.getFileAttributeView(Paths.get(filePath), BasicFileAttributeView.class);
        final FileTime time = FileTime.fromMillis(creationDate.getTime());
        attributes.setTimes(time, time, time);

    }
    
    private void validateStringListOfFilesOnly(final List<String> filesNamesList) {
        for (String fileName : filesNamesList) {
            final File file = new File(fileName);
            this.assertIsFile(file);
        }
    }
    
    private void validateFilesListOfFilesOnly(final List<File> filesNamesList) {
        for (File fileName : filesNamesList) {
            this.assertIsFile(fileName);
        }
    }

    private void assertIsFile(final File fileName) {
        Assert.assertEquals("Error: Se listó como archivo una carpeta", false, fileName.isDirectory());
    }
    
    private Boolean isFileInStringList(final String fileNameToFind, final List<String> fileNamesList) {
        for (String fileName : fileNamesList) {
            final File file = new File(fileName);
            if (file.getName().equals(fileNameToFind))
                return true;
        }
        return false;
    }
    
    private Boolean isFileInFileList(final String fileNameToFind, final List<File> fileNamesList) {
        for (File fileName : fileNamesList) {
            if (fileName.getName().equals(fileNameToFind))
                return true;
        }
        return false;
    }
}
