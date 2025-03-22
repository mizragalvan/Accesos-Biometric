package mx.engineer.utils.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.docx4j.convert.out.pdf.viaXSLFO.PdfSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;

import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;



@SuppressWarnings("deprecation")
public class PDFUtils {
	private static final String PDF_EXTENSION = ".pdf";

	public static File convertDocxToPDF(final File docxFile) throws IOException {
		final InputStream inputStream = new FileInputStream(docxFile);
		final XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
		final PdfOptions pdfOptions = PdfOptions.create();
		final OutputStream outputStream = new FileOutputStream(builtOutputPathFile(docxFile, PDF_EXTENSION));
		PdfConverter.getInstance().convert(xwpfDocument, outputStream, pdfOptions);
		return builtOutputPathFile(docxFile, PDF_EXTENSION);
	}

	public static void convertDocxToPDF_v2(String docPath, String pdfPath) throws FileNotFoundException, Docx4JException {
		InputStream is = new FileInputStream(new File(docPath));
		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(is);
		List<SectionWrapper> sections = wordMLPackage.getDocumentModel().getSections();
		for (int i = 0; i < sections.size(); i++) {
			wordMLPackage.getDocumentModel().getSections().get(i).getPageDimensions();
		}
		Mapper fontMapper = new IdentityPlusMapper();
		PhysicalFont font = PhysicalFonts.getPhysicalFonts().get("Calibri");// set your desired font
		fontMapper.getFontMappings().put("Algerian", font);
		try {
			wordMLPackage.setFontMapper(fontMapper);
			
			PdfSettings pdfSettings = new PdfSettings();
			org.docx4j.convert.out.pdf.PdfConversion conversion = new org.docx4j.convert.out.pdf.viaXSLFO.Conversion(wordMLPackage);

			// To turn off logger

			List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
			loggers.add(LogManager.getRootLogger());
			for (Logger logger : loggers) {
				logger.setLevel(Level.OFF);
			}
			OutputStream out = new FileOutputStream(new File(pdfPath));
			conversion.output(out, pdfSettings);
			System.out.println("DONE!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void convertDocxToPDFDocuSign(final File docxInputFile, final File pdfOutputFile) throws Exception {
		try {

			InputStream docxInputStream = new FileInputStream(docxInputFile);
			OutputStream outputStream = new FileOutputStream(pdfOutputFile);
			IConverter converter = LocalConverter.builder().build();
			converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
			outputStream.close();
//			return pdfOutputFile;

		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;
		}
	}
	public static File convertDocxToPdfLibreOffice(final String OFFICE_HOME, final File docxInputFile, final File pdfOutputFile)
			throws IOException, OfficeException {
		
		LocalOfficeManager officeManager = LocalOfficeManager.builder().officeHome(OFFICE_HOME).install().build();
		try {
			officeManager.start();
			JodConverter.convert(docxInputFile).to(pdfOutputFile).execute();
			officeManager.stop();
			
			return pdfOutputFile;

		} catch (OfficeException officeException) {
			officeException.printStackTrace();
			throw officeException;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;

		} finally {
			OfficeUtils.stopQuietly(officeManager);
		}
	}

	private static File builtOutputPathFile(final File file, final String extension) {
		return new File(FilenameUtils.removeExtension(file.getPath()) + extension);
	}

	public static void convertToPDF(String docPath, String pdfPath) throws IOException, FileNotFoundException {
		InputStream in = new FileInputStream(new File(docPath));
		XWPFDocument document = new XWPFDocument(in);
		PdfOptions options = PdfOptions.create();
		OutputStream out = new FileOutputStream(new File(pdfPath));
		PdfConverter.getInstance().convert(document, out, options);
		document.close();
		out.close();
	}

	public static void main(String[] args) throws FileNotFoundException {
		String folder = "/Contratos/Solicitudes/1/0-25M/0-5K/28/";
		String archivo = "GERDAU_CORSA___JOSE_CORTES___Transporte_Chatarra___Folio_28___VFQRC";
		String wordPath = folder + archivo + ".docx";
		String pdfPath = folder + archivo + ".pdf";
		final File docxFile = new File(wordPath);
		try {
			System.out.println("wordPath :: "+wordPath);
			System.out.println("pdfPath  :: "+pdfPath);
			// convertDocxToPDF(docxFile);
//			convertToPDF(wordPath, pdfPath);
			convertDocxToPDF_v2(wordPath, pdfPath);
		} catch (IOException e) {
			System.out.println("Error al generar el contrato: " + e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	//se implemento para docusign
	public static File convertDocxToPDF(final String OFFICE_HOME, final File docxInputFile, final File pdfOutputFile)
			throws IOException, OfficeException {
		
		LocalOfficeManager officeManager = LocalOfficeManager.builder().officeHome(OFFICE_HOME).install().build();
		try {
			officeManager.start();
			JodConverter.convert(docxInputFile).to(pdfOutputFile).execute();
			officeManager.stop();
			
			return pdfOutputFile;

		} catch (OfficeException officeException) {
			officeException.printStackTrace();
			throw officeException;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw exception;

		} finally {
			OfficeUtils.stopQuietly(officeManager);
		}
	}
	public static File convertDocxToPdfMsOffice(final File docxInputFile, final File pdfOutputFile) throws Exception {
	try {

		InputStream docxInputStream = new FileInputStream(docxInputFile);
		OutputStream outputStream = new FileOutputStream(pdfOutputFile);
		IConverter converter = LocalConverter.builder().build();
		converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
		outputStream.close();
		return pdfOutputFile;

	} catch (Exception exception) {
		exception.printStackTrace();
		throw exception;
	}
}
	

}
