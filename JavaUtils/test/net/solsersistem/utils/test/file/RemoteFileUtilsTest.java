package mx.solsersistem.utils.test.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import mx.solsersistem.utils.file.RemoteFileUtils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RemoteFileUtilsTest {
    private static final String TARGET_PATH = "TestFiles" + File.separator + "RemoteFiles";
    private List<File> filestoDeleteList;
    
    @Before
    public final void setupTest() {
        this.filestoDeleteList = new ArrayList<>();
    }
    
    @After
    public final void cleanResult() {
        for (File fileToDelete : this.filestoDeleteList)
            fileToDelete.delete();
    }
    
    @Ignore
    @Test
    public final void whenGetRemoteFileHttpsThenReturnDownloadedFile() throws MalformedURLException, IOException {
        final File downloadedFile =
                RemoteFileUtils.getRemoteFile("https://www.um.es/atica/documentos/HTML_Juan_Jose_Lopez.doc",
                TARGET_PATH);
        this.filestoDeleteList.add(downloadedFile);
        Assert.assertTrue("Error al descargar el archivo por https", downloadedFile.exists());
    }
    
    @Ignore
    @Test
    public final void whenGetRemoteFileHttpThenReturnDownloadedFile() throws MalformedURLException, IOException {
        final File downloadedFile =
                RemoteFileUtils.getRemoteFile("http://www.gitsinformatica.com/descargas/android.doc",
                TARGET_PATH);
        this.filestoDeleteList.add(downloadedFile);
        Assert.assertTrue("Error al descargar el archivo por http", downloadedFile.exists());
    }
    
    @Ignore
    @Test(expected = FileNotFoundException.class)
    public final void whenGetRemoteFileDoesntExistsThenFileNotFoundExceptionIsThrown()
            throws MalformedURLException, IOException {
        this.filestoDeleteList.add(
                RemoteFileUtils.getRemoteFile("https://www.google.com.mx/HTML_Juan_Jose_Lopez.doc2",
                        TARGET_PATH));
    }
    
    @Ignore
    @Test
    public final void whenGetRemoteFileSharedThenReturnDownloadedFile() throws MalformedURLException, IOException {
        final File downloadedFile =
                RemoteFileUtils.getRemoteFile("\\\\10.0.1.65\\Shared\\settings.xml",
                TARGET_PATH);
        this.filestoDeleteList.add(downloadedFile);
        Assert.assertTrue("Error al descargar el archivo desde una ruta compartida", downloadedFile.exists());
    }
    
    @Test(expected = FileNotFoundException.class)
    public final void whenGetRemoteFileSharedDoesntExistsThenFileNotFoundExceptionIsThrown()
            throws MalformedURLException, IOException {
        RemoteFileUtils.getRemoteFile("\\\\10.0.1.65\\Shared\\settings.xml2", TARGET_PATH);
    }
}
