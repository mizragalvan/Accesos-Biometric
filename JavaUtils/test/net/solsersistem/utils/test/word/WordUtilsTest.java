package mx.solsersistem.utils.test.word;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import mx.solsersistem.utils.word.WordUtils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;

public class WordUtilsTest {
	private Map <String, String> mapValues = this.getValuesMap();
	private String outPathFile = "TestFiles"; 
	private WordUtils wordUtils = new WordUtils();
	private File fileDoc = new File("TestFiles/1._SOLICITUD_INSTRUMENTO_LEGAL.doc");
	private File fileDocx = new File("TestFiles/1._SOLICITUD_INSTRUMENTO_LEGAL.docx");
	private File file = null;
	
	@Test
	public final void whenGetDocTemplateFieldsThenFilleDocument() throws IOException {
	    this.wordUtils.setColorMark(true);
		this.file = this.wordUtils.fillWordDocTemplate(this.fileDoc, this.mapValues, this.outPathFile);
		Assert.assertTrue("Tu documento .doc ha sido creado", this.file.exists());
		Assert.assertTrue("El archivo .doc ha sido eliminado", this.file.delete());
	}

	@Test
	public final void whenGetDocxTemplateFieldsThenFilledDocument() throws IOException, InvalidFormatException {
	    this.wordUtils.setColorMark(true);
		this.file = this.wordUtils.fillWordDocxTemplate(this.fileDocx, this.mapValues, this.outPathFile);
		Assert.assertTrue("Tu documento .docx ha sido creado", this.file.exists());
		Assert.assertTrue("El archivo .docx ha sido eliminado", this.file.delete());
	}
	
	@Test(expected = FileNotFoundException.class)
	public final void whenDocTemplateDoesntExistThenThrownFileNotFoundException() throws IOException {
		final File fileDocDoesntExist = new File("TestFiles/Activity.doc");
		this.wordUtils.fillWordDocTemplate(fileDocDoesntExist, this.mapValues, this.outPathFile);
	}
	
	@Test(expected = FileNotFoundException.class)
	public final void whenDocxTemplateDoesntExisThenThrownFileNotFoundException() throws IOException,
	InvalidFormatException {
		final File fileDocxDoesntExist = new File("TestFiles/Activity.docx");
		this.wordUtils.fillWordDocxTemplate(fileDocxDoesntExist, this.mapValues, this.outPathFile);
	}
	
	private Map<String, String> getValuesMap() {
		final Map<String, String> templateValuesMap = new HashMap<>();
		templateValuesMap.put("[&NombreSolicitante&]", "Luis Alberto");
		templateValuesMap.put("[&TelefonoSolicitante&]", "1234567890");
		return templateValuesMap;
	}
}
