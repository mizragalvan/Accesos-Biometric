package mx.engineer.utils.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.model.table.TblFactory;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Body;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTShd;
import org.docx4j.wml.Color;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.Document;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblBorders;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

public class DocXUtil {

	private static int offset = 0;
	private static int numRead = 0;
	private static final String WTAB = "<w:tab ";
	private static final String WT = "<w:t ";
	private static final String WTE = ">";
	private static final String END = "</w:t>";
	private static final int VAL = 1;
	private static final Logger LOG = Logger.getLogger(DocXUtil.class);

	public static void replacePlaceholderTabla(final WordprocessingMLPackage template, final Tbl table,
			final String placeholder) {
		final List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), R.class);
		String escapePH = StringEscapeUtils.escapeXml10(placeholder);
		for (Object o : texts) {
			final R run = (R) o;
			final String str = XmlUtils.marshaltoString(run);
			if (str.contains(escapePH)) {
				final P p = (P) run.getParent();
				final Body body = (Body) p.getParent();
				body.getContent().add(body.getContent().indexOf(p), table);
				body.getContent().remove(p);
			}
		}
	}

	public static void replacePlaceholderRun(final WordprocessingMLPackage template, final R replacer,
			final String placeholder) {
		final List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), R.class);
		String escapePH = StringEscapeUtils.escapeXml10(placeholder);
		for (Object o : texts) {
			final R run = (R) o;
			final String str = XmlUtils.marshaltoString(run);
			if (str.contains(escapePH)) {
				final P p = (P) run.getParent();
				p.getContent().add(p.getContent().indexOf(run), replacer);
				p.getContent().remove(run);
			}

		}
	}

	public static void replacePlaceholderParrafo(final WordprocessingMLPackage template, final P replacer,
			final String placeholder) {
		final List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), R.class);
		String escapePH = StringEscapeUtils.escapeXml10(placeholder);
		for (Object o : texts) {
			final R run = (R) o;
			final String str = XmlUtils.marshaltoString(run);
			if (str.contains(escapePH)) {
				final P p = (P) run.getParent();
				if (p.getParent() instanceof Body) {
					final Body body = (Body) p.getParent();
					body.getContent().add(body.getContent().indexOf(p), replacer);
					body.getContent().remove(p);
				}
				if (p.getParent() instanceof Document) {
					final Document body = (Document) p.getParent();
					body.getContent().add(body.getContent().indexOf(p), replacer);
					body.getContent().remove(p);
				}
			}

		}
	}

	public static void replacePlaceholderRun(final WordprocessingMLPackage template, final P replacer,
			final String placeholder) {
		final List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), R.class);
		String escapePH = StringEscapeUtils.escapeXml10(placeholder);
		for (Object o : texts) {
			final R run = (R) o;
			final String str = XmlUtils.marshaltoString(run);
			if (str.contains(escapePH)) {
				final P p = (P) run.getParent();
				final Body body = (Body) p.getParent();
				body.getContent().add(body.getContent().indexOf(p), replacer);
				body.getContent().remove(p);
			}

		}
	}

	public static void replacePlaceholderTablaCBorrador(final WordprocessingMLPackage template,
			final CreaTablaFirmas tabla, final String placeholder) {
		replacePlaceholderTabla(template, tabla.creaTablaCBorrador(), placeholder);
	}

	public static void replacePlaceholderTabla(final WordprocessingMLPackage template, final CreaTablaSeleccion tabla,
			final String placeholder) {
		replacePlaceholderTabla(template, tabla.creaTabla(), placeholder);
	}

	public static void replacePlaceholderTablaC(final WordprocessingMLPackage template, final CreaTablaFirmas tabla,
			final String placeholder) {
		replacePlaceholderTabla(template, tabla.creaTablaC(), placeholder);
	}

	public static void replacePlaceholderTablaF(final WordprocessingMLPackage template, final CreaTablaFirmas tabla,
			final String placeholder) {
		replacePlaceholderTabla(template, tabla.creaTablaF(), placeholder);
	}

	public static void replacePlaceholderTextBorrador(final WordprocessingMLPackage template, final String name,
			final String placeholder) {
		replacePlaceholderText(template, name, placeholder, true);
	}

	public static void replacePlaceholderText(final WordprocessingMLPackage template, final String name,
			final String placeholder) {
		replacePlaceholderText(template, name, placeholder, false);
	}

	private static void agregaTab(final P p, final R run) {
		int index = p.getContent().indexOf(run);
		ParrafoFormat.creaRunTab(p, index, run);
	}

	private static void modifyParrafo(final String base, final P p, final R run, final String name,
			final String placeholder, final boolean borrador) {
		String escapePH = StringEscapeUtils.escapeXml10(placeholder);
		if (base.contains(escapePH)) {
			String trama = generaTramaBase(base);
			String ini = trama.substring(0, trama.indexOf(escapePH));
			String end = trama.substring(trama.indexOf(escapePH) + escapePH.length());
			int index = p.getContent().indexOf(run);
			ParrafoFormat.creaRunClean(StringEscapeUtils.unescapeXml(ini), p, index, run);
			index = ParrafoFormat.creaRun(StringEscapeUtils.unescapeXml(name), p, index + 1, borrador, run);
			final R lastRun = ParrafoFormat.creaRunClean(StringEscapeUtils.unescapeXml(end), p, index, run);
			p.getContent().remove(run);
			findAndReplaceText(name, placeholder, borrador, lastRun);
		} else if (!base.isEmpty() && p.getContent().indexOf(run) >= 0) {
			String trama = generaTramaBase(base);
			int index = p.getContent().indexOf(run);
			ParrafoFormat.creaRunClean(StringEscapeUtils.unescapeXml(trama), p, index, run);
		}
	}

	private static String generaTramaBase(final String base) {
		String trama = base.substring(base.indexOf(WTE) + VAL);
		trama = trama.substring(0, trama.indexOf(END));
		return trama;
	}

	public static Tbl createTractoTable(final WordprocessingMLPackage wordMLPackage, final List<String> header,
			ObjectFactory factory) {

		int columnNumber = header.size();
		int writableWidthTwips = wordMLPackage.getDocumentModel().getSections().get(0).getPageDimensions()
				.getWritableWidthTwips();
		Tbl tabla = TblFactory.createTable(0, columnNumber, writableWidthTwips / columnNumber);
		CTBorder border = createBorder();
		TblBorders borders = creteBorders(border);
		tabla.setTblPr(new TblPr());
		tabla.getTblPr().setTblBorders(borders);
		return tabla;
	}

	private static TblBorders creteBorders(CTBorder border) {
		TblBorders borders = new TblBorders();
		borders.setBottom(border);
		borders.setLeft(border);
		borders.setRight(border);
		borders.setTop(border);
		borders.setInsideH(border);
		borders.setInsideV(border);
		return borders;
	}

	private static CTBorder createBorder() {
		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);
		return border;
	}

	public static void replacePlaceholderText(final WordprocessingMLPackage template, final String name,
			final String placeholder, final boolean borrador) {
		final List<Object> texts = getAllElementFromObject(template.getMainDocumentPart(), R.class);
		for (Object o : texts) {
			final R run = (R) o;
			findAndReplaceText(name, placeholder, borrador, run);
		}
	}

	private static void findAndReplaceText(final String name, final String placeholder, final boolean borrador,
			final R run) {
		String escapePH = StringEscapeUtils.escapeXml10(placeholder);
		final String str = XmlUtils.marshaltoString(run);
		if (str.contains(escapePH)) {
			final P p = (P) run.getParent();
			for (Object x : run.getContent()) {
				String base = XmlUtils.marshaltoString(x);
				if (base.startsWith(WTAB)) {
					agregaTab(p, run);
				} else if (base.startsWith(WT)) {
					modifyParrafo(base, p, run, name, placeholder, borrador);
				}
			}
		}
	}

	public static void replacePlaceholderText(final WordprocessingMLPackage template,
			final Map<String, String> mapValues) {
		
		LOG.info("===================  INICIA  replacePlaceholderText =======================");
		
		
		for (Entry<String, String> entry : mapValues.entrySet()) {
			LOG.info("REMPLAZA TAG :: Valor("+entry.getValue()+") Tag ("+entry.getKey()+")");
			replacePlaceholderText(template, entry.getValue(), entry.getKey());
		}
		LOG.info("TERMINA REMPLAZADO DE TAGS - NORMAL");
	}

	public static void replacePlaceholderTextBorrador(final WordprocessingMLPackage template,
			final Map<String, String> mapValues) {
		
		LOG.info("===================  INICIA  replacePlaceholderTextBorrador =======================");
		
		for (Entry<String, String> entry : mapValues.entrySet()) {
			LOG.info("REMPLAZA TAG :: Valor("+entry.getValue()+") Tag ("+entry.getKey()+")");
			replacePlaceholderTextBorrador(template, entry.getValue(), entry.getKey());
		}
		LOG.info("TERMINA REMPLAZADO DE TAGS - BORRADOR");
	}

	public static List<Object> getAllElementFromObject(Object obj, Class<?> toSearch) {
		final List<Object> result = new ArrayList<Object>();
		if (obj instanceof JAXBElement)
			obj = ((JAXBElement<?>) obj).getValue();
		if (obj.getClass().equals(toSearch))
			result.add(obj);
		else if (obj instanceof ContentAccessor) {
			final List<?> children = ((ContentAccessor) obj).getContent();
			for (Object child : children) {
				result.addAll(getAllElementFromObject(child, toSearch));
			}
		}
		return result;
	}

	public static R createImageRun(final WordprocessingMLPackage wordMLPackage, final String imagePath)
			throws Exception {
		final BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage,
				getBytes(imagePath));
		final Inline inline = imagePart.createImageInline(null, "image alt", 0, 1, false);
		final ObjectFactory factory = Context.getWmlObjectFactory();
		final R run = factory.createR();
		final Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return run;
	}

	public static byte[] getBytes(String path) throws Exception {
		final File file = new File(path);
		final InputStream is = new FileInputStream(file);
		final long length = file.length();
		if (length > Integer.MAX_VALUE) {
			System.out.println("Archivo muy Grande!!");
		}
		final byte[] bytes = new byte[(int) length];

		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			System.out.println("No se pudo leer todo el archivo " + file.getName());
		}
		is.close();

		return bytes;

	}

	public static void addTC(Tr tr, ObjectFactory factory, String text, String backgroundColor, String textColor,
			String fontFamily, String fontSize, WordprocessingMLPackage wordMLPackage, Boolean isBold,
			Boolean isItalic, Boolean isUnderline) {
		Tc tc = factory.createTc();
		P p = factory.createP();
		R r = factory.createR();
		Text t = factory.createText();
		t.setValue(text);
		r.getContent().add(t);
		p.getContent().add(r);
		RPr rpr = factory.createRPr();
		setCellStyle(factory, rpr, tc, r, backgroundColor, textColor);
		setFontFamily(rpr, fontFamily);
		setFontSize(rpr, fontSize);
		
		if (isBold) {
			addBoldStyle(rpr);
		}
		if (isItalic) {
			addItalicStyle(rpr);
		}
		if (isUnderline) {
			addUnderlineStyle(rpr);
		}

		tc.getEGBlockLevelElts().add(p);
		tr.getEGContentCellContent().add(tc);

	}

	private static void setCellStyle(ObjectFactory factory, RPr rpr, Tc tc, R r, String backgroundColor,
			String textColor) {
		BooleanDefaultTrue b = new BooleanDefaultTrue();
		rpr.setB(b);
		rpr.setCaps(b);
		Color color = factory.createColor();
		color.setVal(textColor);
		rpr.setColor(color);
		r.setRPr(rpr);
		TcPr tableCellProperties = tc.getTcPr();
		if (tableCellProperties == null) {
			tableCellProperties = new TcPr();
			tc.setTcPr(tableCellProperties);
		}
		CTShd shd = new CTShd();
		shd.setFill(backgroundColor);
		tableCellProperties.setShd(shd);
	}

	private static void setFontSize(RPr runProperties, String fontSize) {
		if (fontSize != null && !fontSize.isEmpty()) {
			HpsMeasure size = new HpsMeasure();
			size.setVal(new BigInteger(fontSize));
			runProperties.setSz(size);
			runProperties.setSzCs(size);
		}
	}

	private static void setFontFamily(RPr runProperties, String fontFamily) {
		if (fontFamily != null) {
			RFonts rf = runProperties.getRFonts();
			if (rf == null) {
				rf = new RFonts();
				runProperties.setRFonts(rf);
			}
			rf.setAscii(fontFamily);
		}
	}


	
    private static void addBoldStyle(RPr runProperties) {
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        b.setVal(true);
        runProperties.setB(b);
     }

     private static void addItalicStyle(RPr runProperties) {
        BooleanDefaultTrue b = new BooleanDefaultTrue();
        b.setVal(true);
        runProperties.setI(b);
     }

     private static void addUnderlineStyle(RPr runProperties) {
        U val = new U();
        val.setVal(UnderlineEnumeration.SINGLE);
        runProperties.setU(val);
     }
}
