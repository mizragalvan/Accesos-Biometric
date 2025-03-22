package mx.solsersistem.utils.test.word;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mx.solsersistem.utils.word.WordComparationException;
import mx.solsersistem.utils.word.WordComparationUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class WordComparationUtilsTest {
    
    private static final String BASE_DOCX_PATH = "TestFiles/WordComparation/PlantillaUno.docx";
    private static final String BASE_DOC_PATH = "TestFiles/WordComparation/PlantillaUno.doc";
    private static final String COMPARED_DOCX_PATH = "TestFiles/WordComparation/PlantillaDos.docx";
    private static final String COMPARED_DOC_PATH = "TestFiles/WordComparation/PlantillaDos.doc";
    private static final String OUTPUTH_PATH = "TestFiles/WordComparation/";
    private static List<File> comparedFiles;
    
    @Before
    public final void setupTest() {
        comparedFiles = new ArrayList<>();
    }
    
    @After
    public final void deleteCreatedFiles() {
        for (File fileToDelete : comparedFiles)
            fileToDelete.delete();
    }

    @Test
    public final void whenBothFilesAreDocxThenComparationIsDone() throws WordComparationException {
        final File comparedFile = 
                WordComparationUtils.compare(new File(BASE_DOCX_PATH), new File(COMPARED_DOCX_PATH), OUTPUTH_PATH);
        comparedFiles.add(comparedFile);
    }
    
    @Test(expected = WordComparationException.class)
    public final void whenAFileIsDocThenWordComparationExceptionIsThrown() throws WordComparationException {
        final File comparedFile = 
                WordComparationUtils.compare(new File(BASE_DOC_PATH), new File(COMPARED_DOCX_PATH), OUTPUTH_PATH);
        comparedFiles.add(comparedFile);
    }
    
    @Test(expected = WordComparationException.class)
    public final void whenAFileIsNotFoundThenWordComparationExceptionIsThrown() throws WordComparationException {
        final File comparedFile = 
                WordComparationUtils.compare(new File(OUTPUTH_PATH), new File(COMPARED_DOCX_PATH), OUTPUTH_PATH);
        comparedFiles.add(comparedFile);
    }
    
    @Ignore
    @Test
    public final void whenBaseFileIsDocThenConvertToDocxAndComparationIsDone() throws WordComparationException {
        final File comparedFile = 
                WordComparationUtils.compare(new File(BASE_DOC_PATH), new File(COMPARED_DOCX_PATH), OUTPUTH_PATH);
        comparedFiles.add(comparedFile);
    }
    
    @Ignore
    @Test
    public final void whenComparedsFileIsDocThenConvertToDocxAndComparationIsDone() throws WordComparationException {
        final File comparedFile = 
                WordComparationUtils.compare(new File(BASE_DOCX_PATH), new File(COMPARED_DOC_PATH), OUTPUTH_PATH);
        comparedFiles.add(comparedFile);
    }
}
