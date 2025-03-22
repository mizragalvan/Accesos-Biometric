/**
 * 
 */
package mx.engineer.utils.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDocument;
import com.lowagie.text.pdf.PdfPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;


/**
 * @author Mizraim
 */
public class AddFooterQr {
	private static final String FORMAT_IMAGE = "PNG";
	private static final String ENCODE = "UTF-8";
	public static final String FOLIO = "Folio Número: ";

	private static final int QR_SIZE = 80;
	private static final int TRES = 3;
	private static final int CERO = 0;
	private static final String EXTENSION_PDF_FILE = ".pdf";
	private static final String QUICK_RESPONSE = "QRC";
	
	
	public void addQRToExistingPDF(String existingPdfFilePath, String newPdfFilePath, String imageFilePath, String folio,
			boolean isOtherDocumentType)
			throws IOException, DocumentException {
		// elimina la extensión ".pdf" del final del path
		String pdfFilePath = existingPdfFilePath;
		if (pdfFilePath.toLowerCase().endsWith(EXTENSION_PDF_FILE)) {
			pdfFilePath = pdfFilePath.substring(0, pdfFilePath.length() - 4);
		}
		// crea un nuevo documento PDF con el contenido adicional
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document,
				new FileOutputStream(pdfFilePath + QUICK_RESPONSE + EXTENSION_PDF_FILE));
		document.open();
		// agrega imagen al nuevo documento
		Image image = Image.getInstance(imageFilePath);
		image.scaleToFit(80, 30);
		image.setAbsolutePosition(10, 10);
		document.add(image);
		// agrega texto al encabezado si es otro tipo de documento
		if (isOtherDocumentType) {
			Paragraph folioParagraph = new Paragraph(FOLIO + folio);
			folioParagraph.getFont().setSize(12);
			document.add(folioParagraph);
		}
		// cierra el documento nuevo
		document.close();
		// abrir el PDF existente y combinarlo con el nuevo contenido
		PdfReader existingPdfReader = new PdfReader(existingPdfFilePath);
		PdfStamper stamper = new PdfStamper(existingPdfReader,
				new FileOutputStream(pdfFilePath + QUICK_RESPONSE + EXTENSION_PDF_FILE));
		int totalPages = existingPdfReader.getNumberOfPages();
		// agrega el contenido nuevo en todas las páginas existentes
		for (int i = 1; i <= totalPages; i++) {
			PdfContentByte pageContent = stamper.getOverContent(i);
			// ajusta la posición
			image.setAbsolutePosition(10, 10);
			pageContent.addImage(image);

			if (isOtherDocumentType) {
				ColumnText.showTextAligned(pageContent, Element.ALIGN_LEFT,
						new Phrase(FOLIO + folio),
						100, 10, 0);
			}
		}
		// cierra el PdfStamper
		stamper.close();
	}
	
	public void addQRFooter(File requisition, String imageName, String imageNameComplete, String newFileName,
			String folio, Boolean tipoDocumentoOtros) throws IOException {
		XWPFDocument doc = null;
		FileInputStream fis = null;
		FileInputStream fis2 = null;
		FileOutputStream fis3 = null;
		if (null == folio) {
			throw new IOException();
		}
		try {
			fis = new FileInputStream(requisition);
			doc = new XWPFDocument(fis);
			XWPFParagraph paragraph = doc.createParagraph();
			XWPFRun run = paragraph.createRun();
			paragraph = doc.createParagraph();
			CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
			XWPFHeaderFooterPolicy headerFooterPolicy = doc.getHeaderFooterPolicy();
			if (null == headerFooterPolicy) {
				headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);
			}
			XWPFHeader header = headerFooterPolicy.createHeader(XWPFHeaderFooterPolicy.DEFAULT);
			paragraph = header.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.RIGHT);
			run = paragraph.createRun();
			if(tipoDocumentoOtros) {
				run.setText(FOLIO + folio);
			}
			XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
			paragraph = footer.createParagraph();
			paragraph.setAlignment(ParagraphAlignment.LEFT);			
			run = paragraph.createRun();
			fis2 = new FileInputStream(new File(imageNameComplete));
			run.addPicture(fis2, XWPFDocument.PICTURE_TYPE_PNG, imageName, Units.toEMU(QR_SIZE / TRES),
					Units.toEMU(QR_SIZE / TRES));
			fis3 = new FileOutputStream(newFileName);
			doc.write(fis3);
		} catch (InvalidFormatException e) {
			throw new IOException();
		} finally {
				if (null != doc) {
					doc.close();
				}
				if (null != fis) {
					fis.close();
				}
				if (null != fis2) {
					fis2.close();
				}
				if (null != fis3) {
					fis3.close();
				}
//				Files.deleteIfExists(Paths.get(imageNameComplete));
		}
	}

	/**
	 * Genera imagen.
	 * 
	 * @param qr      cadena codigo
	 * @param bandera para color; default=blue
	 * @param nic     nombre de imagen completa
	 * @param width   ancho
	 * @param height  largo
	 * @throws WriterException exepcion propagar
	 * @throws IOException     exepcion a propagar
	 */
	public void generateSaveQr(String qr, Boolean bandera, String nic, int width, int height)
			throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
		hintMap.put(EncodeHintType.CHARACTER_SET, ENCODE);
		hintMap.put(EncodeHintType.MARGIN, CERO); /* default = 4 */
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		BitMatrix bitMatrix = qrCodeWriter.encode(qr, BarcodeFormat.QR_CODE, width, height, hintMap);
		Path path = FileSystems.getDefault().getPath(nic);
		if (bandera) {
			MatrixToImageConfig adn = new MatrixToImageConfig(mx.engineer.utils.general.ColorsEnum.RED.getArgb(),
					MatrixToImageConfig.WHITE);
			MatrixToImageWriter.writeToPath(bitMatrix, FORMAT_IMAGE, path, adn);
		} else {
			MatrixToImageConfig adn = new MatrixToImageConfig(mx.engineer.utils.general.ColorsEnum.BLUE.getArgb(),
					MatrixToImageConfig.WHITE);
			MatrixToImageWriter.writeToPath(bitMatrix, FORMAT_IMAGE, path, adn);
		}
	}
}