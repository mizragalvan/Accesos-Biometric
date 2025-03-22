package mx.engineer.utils.word;
//package mx.solsersistem.utils.word;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//

//import org.docx4j.XmlUtils;
//import org.docx4j.jaxb.Context;
//import org.docx4j.model.table.TblFactory;
//import org.docx4j.openpackaging.exceptions.Docx4JException;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
//import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
//import org.docx4j.openpackaging.parts.WordprocessingML.StyleDefinitionsPart;
//import org.docx4j.wml.BooleanDefaultTrue;
//import org.docx4j.wml.CTBorder;
//import org.docx4j.wml.CTCnf;
//import org.docx4j.wml.CTShd;
//import org.docx4j.wml.Color;
//import org.docx4j.wml.HpsMeasure;
//import org.docx4j.wml.JcEnumeration;
//import org.docx4j.wml.ObjectFactory;
//import org.docx4j.wml.P;
//import org.docx4j.wml.PPr;
//import org.docx4j.wml.R;
//import org.docx4j.wml.RFonts;
//import org.docx4j.wml.RPr;
//import org.docx4j.wml.STBorder;
//import org.docx4j.wml.STVerticalJc;
//import org.docx4j.wml.Style;
//import org.docx4j.wml.Styles;
//import org.docx4j.wml.Tbl;
//import org.docx4j.wml.TblBorders;
//import org.docx4j.wml.TblPr;
//import org.docx4j.wml.Tc;
//import org.docx4j.wml.TcPr;
//import org.docx4j.wml.Text;
//import org.docx4j.wml.Tr;
//
//public class DocXTest {
//
////	private final static String image = "/Users/Coolcold/Pictures/advancedanime/5jb1zl5.png";
////	private final static String input = "/Users/Coolcold/Downloads/TablaFirmas.docx";
//	private final static String output = "C:\\Contratos\\CatalogoTipoDocumentos\\TEST - copia.docx";
//	private final static String input = "C:\\Contratos\\CatalogoTipoDocumentos\\TEST.docx";
//
//	public static void main(final String[] args) throws Exception {
//
//		WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(input));
//
//		/*------------------------------------*/
////		MainDocumentPart mp = wordMLPackage.getMainDocumentPart();
////		Styles styles = mp.getStyleDefinitionsPart().getJaxbElement();
////		ObjectFactory factory = Context.getWmlObjectFactory();
////		for (Style s : styles.getStyle()) {
////			if (s.getName().getVal().equals("Normal")) {
////				
////				RPr rpr = s.getRPr();
////				if (rpr == null) {
////					rpr = factory.createRPr();
////					s.setRPr(rpr);
////				}
////
////				RFonts rf = rpr.getRFonts();
////				if (rf == null) {
////					rf = factory.createRFonts();
////					rpr.setRFonts(rf);
////				}
////				// This is where you set your font name.
////				rf.setAscii("Verdana");
////			}
////		}
//
//		/*------------------------------------*/
//
//		VariablePrepare.prepare(wordMLPackage);
//
//		// Courier New
//
//		DocXUtil.replacePlaceholderText(wordMLPackage, "Nmero", "[&FolioSolicitud&]", true);
//		DocXUtil.replacePlaceholderText(wordMLPackage, "trminos", "[&SolicitudDescripcionServicioMayusculas&]", true);
//
////		DocXUtil.replacePlaceholderText(wordMLPackage, "Prueba 3 ", "[***]", true);
////		DocXUtil.replacePlaceholderText(wordMLPackage, "Prueba 4 ", "[&NombreProemio&]", true);
////		DocXUtil.replacePlaceholderText(wordMLPackage, "Prueba 5 ", "[&NombresLargosEntidadesFinancieras&]", true);
////		DocXUtil.replacePlaceholderText(wordMLPackage, "<b>Prue<u>ba</u> 7</b>", "[&ActorActivoMayusculas&]");
////		DocXUtil.replacePlaceholderTextBorrador(wordMLPackage, "<b>Prue<u>ba</u> 6</b> ",
////				"[&RepresentantesLegalesSolicitud&]");
////		DocXUtil.replacePlaceholderTextBorrador(wordMLPackage, "Prueba 8 ", "[&ProveedorNombreCompaÃ±ia&]");
////		DocXUtil.replacePlaceholderTextBorrador(wordMLPackage, "Prueba 9 ", "[&ActorPasivoMayusculas&]");
//
////		pruebaTablaFirmasV2(wordMLPackage);
////		pruebaTablaFirmas(wordMLPackage);
////		pruebaTabla(wordMLPackage);
////		pruebaImagenes(wordMLPackage);
////		pruebaParrafo(wordMLPackage);		
//
//		wordMLPackage.save(new File(output));
//
////		WordprocessingMLPackage test = WordprocessingMLPackage.load(new File(ver));
////	        WordprocessingMLPackage test = WordprocessingMLPackage.load(new File(output));
////	        leeBloques(test);
//	}
//
////	private static void pruebaImagenes(WordprocessingMLPackage wordMLPackage) throws Exception {
////
////		DocXUtil.replacePlaceholderRun(wordMLPackage, DocXUtil.createImageRun(wordMLPackage, image), "[&Prueba4&]");
////
////	}
//
//	private static void pruebaTablaFirmas(WordprocessingMLPackage wordMLPackage) throws Docx4JException {
//
//		List<String> rep2 = new ArrayList<>();
//		rep2.add("Firma 01");
//		rep2.add("Firma 02");
//
//		List<String> testigosFormato = new ArrayList<>();
//		testigosFormato.add("<b>Tes</b>tigo 01");
//		testigosFormato.add("<b><u>Testigo 02</u></b>");
//		testigosFormato.add("<u>Testigo 03</u>");
//		testigosFormato.add("Testigo 04");
//		testigosFormato.add("T<b>es<u>t</u>i<u>g</u>o</b> 05");
//		testigosFormato.add("Testigo 06");
//
//		List<String> titulo2 = new ArrayList<>();
//		titulo2.add("Testigos:");
//
//		LinkedHashMap<String, List<String>> data2 = new LinkedHashMap<>();
//		data2.put(CreaTablaFirmas.REP, rep2);
//		data2.put(CreaTablaFirmas.TESTIGOS, testigosFormato);
//		data2.put(CreaTablaFirmas.TITLETESTIGOS, titulo2);
//		CreaTablaFirmas firmas2 = new CreaTablaFirmas(data2);
//		firmas2.setFormat(true);
//		firmas2.setColorFont("0000FF");
//		DocXUtil.replacePlaceholderTablaF(wordMLPackage, firmas2, "[&#tablaFirmas2#&]");
//		/// Reemplaza Texto
//		DocXUtil.replacePlaceholderText(wordMLPackage, "Jesus Ferruzca Luna", "#[Arrendador]#");
//	}
//
//	private static void pruebaTablaFirmasV2(WordprocessingMLPackage wordMLPackage) throws Docx4JException {
//
//		List<String[]> banco = new ArrayList<>();
//		String[] a1 = new String[2];
//		a1[0] = "El â€œBancoâ€� \n Scotiabank Inverlat, S.A.,\n InstituciÃ³n de Banca MÃºltiple,\n Grupo Financiero Scotiabank Inverlat";
//		a1[1] = "[DGA o Director del Ã�rea Usuaria] 1";
//		banco.add(a1);
//		String[] a2 = new String[2];
//		a2[0] = "La â€œCasa de Bolsaâ€�  \n Scotia Inverlat Casa de Bolsa, S.A. de C.V.,\n Grupo Financiero Scotiabank Inverlat";
//		a2[1] = "[DGA o Director del Ã�rea Usuaria] 2";
//		banco.add(a2);
//		String[] a3 = new String[2];
//		a3[0] = "â€œScotia Fondosâ€� \n Scotia Fondos, S.A. de C.V., Sociedad \n Operadora de Sociedades de InversiÃ³n, \n Grupo Financiero Scotiabank Inverlat";
//		a3[1] = "[DGA o Director del Ã�rea Usuaria] 3";
//		banco.add(a3);
//
//		List<String[]> proveedor = new ArrayList<>();
//		String[] arre1 = new String[2];
//		arre1[0] = "â€œArrendadorâ€� \n  Jesus Ferruzca Luna";
//		arre1[1] = "Jesus Ferruzca Luna";
//		proveedor.add(arre1);
//
//		List<String> testigosFormato = new ArrayList<>();
//		testigosFormato.add("<b>Tes</b>tigo 01");
//		testigosFormato.add("<b><u>Testigo 02</u></b>");
//		testigosFormato.add("<u>Testigo 03</u>");
//		testigosFormato.add("Testigo 04");
//		testigosFormato.add("T<b>es<u>t</u>i<u>g</u>o</b> 05");
//		testigosFormato.add("Testigo 06");
//
//		List<String> titulo2 = new ArrayList<>();
//		titulo2.add("Testigos:");
//
//		LinkedHashMap<String, List<String>> data2 = new LinkedHashMap<>();
//		data2.put(CreaTablaFirmas.TESTIGOS, testigosFormato);
//		data2.put(CreaTablaFirmas.TITLETESTIGOS, titulo2);
//		CreaTablaFirmas firmas2 = new CreaTablaFirmas(data2);
//		firmas2.setFormat(true);
//		firmas2.setBanco(banco);
//		firmas2.setProveedor(proveedor);
//		DocXUtil.replacePlaceholderTablaC(wordMLPackage, firmas2, "[&#tablaFirmas#&]");
//	}
//
//	private static void pruebaTablaSeleccion(WordprocessingMLPackage wordMLPackage) throws Docx4JException {
//		List<String> lista = new ArrayList<>();
//		lista.add("Sus empleados");
//		lista.add("CondÃ³minos");
//		lista.add("Inquilinos");
//		lista.add("Sus clientes");
//		lista.add("PÃºblico en general");
//
//		CreaTablaSeleccion tabla = new CreaTablaSeleccion(lista);
//		tabla.setColorFont("0000FF");
//		DocXUtil.replacePlaceholderTabla(wordMLPackage, tabla, "[&#TablaPrueba#&]");
//
//	}
//
//	private static void pruebaTabla(WordprocessingMLPackage wordMLPackage) throws Docx4JException {
////        List<String> lista=new ArrayList<>();
////		  lista.add("Sus empleados");
////		  lista.add("CondÃ³minos");
////		  lista.add("Inquilinos");
////		  lista.add("Sus clientes");
////		  lista.add("PÃºblico en general");
////		
////		CreaTablaSeleccion tabla=new CreaTablaSeleccion(lista);
////		tabla.setColorFont("0000FF");
//		ObjectFactory factory = Context.getWmlObjectFactory();
//
//		CTBorder border = new CTBorder();
//		border.setColor("auto");
//		border.setSz(new BigInteger("4"));
//		border.setSpace(new BigInteger("0"));
//		border.setVal(STBorder.SINGLE);
//
//		TblBorders borders = new TblBorders();
//		borders.setBottom(border);
//		borders.setLeft(border);
//		borders.setRight(border);
//		borders.setTop(border);
//		borders.setInsideH(border);
//		borders.setInsideV(border);
//		List<String> head = new ArrayList<String>();
//		head.add("Marca");
//		head.add("Modelo");
//		head.add("Placas Federales");
//		head.add("Chofer");
//		head.add("Prov. GPS");
//		head.add("No. Póliza Seguro Tracto");
//		List<Tracto> listaTracto = crearLista();
//		int writableWidthTwips = wordMLPackage.getDocumentModel().getSections().get(0).getPageDimensions()
//				.getWritableWidthTwips();
//		int columnNumber = head.size();
//		Tbl tabla = TblFactory.createTable(0, columnNumber, writableWidthTwips / columnNumber);
//		tabla.setTblPr(new TblPr());
//		tabla.getTblPr().setTblBorders(borders);
////				List<Object> rows = tabla.getContent();
////				for (Object row : rows) {
////				    Tr tr = (Tr) row;
////				    List<Object> cells = tr.getContent();
////				    for(Object cell : cells) {
////				        Tc td = (Tc) cell;
////				        td.getContent().add(p);
////				    }
////				}
//		Tr thead = factory.createTr();
//		for (int i = 0; i < head.size(); i++) {
//			addTCHead(thead, factory, head.get(i), wordMLPackage);
//		}
//
//		tabla.getContent().add(thead);
//
//		for (int i = 0; i < listaTracto.size(); i++) {
//			Tr content = factory.createTr();
//
//			for (int j = 0; j < head.size(); j++) {
//				switch (j) {
//				case 0:
//					addTC(content, factory, listaTracto.get(i).getBrand(), wordMLPackage);
//					break;
//				case 1:
//					addTC(content, factory, listaTracto.get(i).getModel(), wordMLPackage);
//					break;
//				case 2:
//					addTC(content, factory, listaTracto.get(i).getFederalPlates(), wordMLPackage);
//					break;
//				case 3:
//					addTC(content, factory, listaTracto.get(i).getDriver(), wordMLPackage);
//					break;
//				case 4:
//					addTC(content, factory, listaTracto.get(i).getGpsProvider(), wordMLPackage);
//					break;
//				case 5:
//					addTC(content, factory, listaTracto.get(i).getTractoInsurancePolicyNumber(), wordMLPackage);
//					break;
//				default:
//					break;
//				}
//
//			}
//
//			tabla.getContent().add(content);
//		}
//
//		DocXUtil.replacePlaceholderTabla(wordMLPackage, tabla, "[&TablaTracto&]");
//
//		DocXUtil.replacePlaceholderTabla(wordMLPackage, tabla, "[&#TablaPrueba#&]");
//
//	}
//
//	private static void addTC(Tr tr, ObjectFactory factory, String text, WordprocessingMLPackage wordMLPackage) {
//		Tc tc = factory.createTc();
//		tc.getEGBlockLevelElts().add(wordMLPackage.getMainDocumentPart().createParagraphOfText(text));
//		tr.getEGContentCellContent().add(tc);
//
//	}
//
//	private static void addTCHead(Tr tr, ObjectFactory factory, String text, WordprocessingMLPackage wordMLPackage) {
//		Tc tc = factory.createTc();
//		P p = factory.createP();
//		R r = factory.createR();
//		Text t = factory.createText();
//		t.setValue(text);
//		r.getContent().add(t);
//		p.getContent().add(r);
//		RPr rpr = factory.createRPr();
//		setHeaderStyle(factory, rpr, tc, r, "012061", "white");
//		setFontFamily(rpr, "Arial");
//		setFontSize(rpr, "20");
//		tc.getEGBlockLevelElts().add(p);
//
//		tr.getEGContentCellContent().add(tc);
//	}
//
//	private static List<Tracto> crearLista() {
//		List<Tracto> lista = new ArrayList<Tracto>();
//		for (int i = 0; i < 5; i++) {
//			Tracto tracto = new Tracto();
//			tracto.setBrand("Marcaaaaaaaaaa " + (i + 1));
//			tracto.setDriver("Choferrrrrrrrrrr " + (i + 1));
//			tracto.setFederalPlates("Placas federalessssssssssssssss " + (i + 1));
//			tracto.setGpsProvider("Proveedro GPSSSSSSSSSSSSSSSS " + (i + 1));
//			tracto.setModel("Modeloooooooooooo " + (i + 1));
//			tracto.setTractoInsurancePolicyNumber("No. Poliza Seguro Tractooooooooooooooooooooo " + (i + 1));
//			lista.add(tracto);
//		}
//		return lista;
//	}
//
//	public static void leeBloques(final WordprocessingMLPackage template) {
//		final List<Object> texts = DocXUtil.getAllElementFromObject(template.getMainDocumentPart(), P.class);
//		for (Object o : texts) {
//			final P run = (P) o;
//			System.out.println("*************************");
//			System.out.println(XmlUtils.marshaltoString(run));
//		}
//	}
//
//	private static void pruebaParrafo(final WordprocessingMLPackage template) {
//		BufferedReader br = null;
//		StringBuilder text = new StringBuilder();
//		String ruta = "/Users/Coolcold/Downloads/texto_formato.txt";
//		try {
//			String sCurrentLine;
//
//			br = new BufferedReader(new InputStreamReader(new FileInputStream(ruta), "UTF-8"));
//			while ((sCurrentLine = br.readLine()) != null) {
//				text.append(sCurrentLine + "\r\n");
//			}
//
//			final ParrafoFormat pf = new ParrafoFormat("0000FF");
//			P parrafo = pf.creaParrafo(text.toString());
//			DocXUtil.replacePlaceholderParrafo(template, parrafo, "[&Prueba&]");
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (br != null)
//					br.close();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//
//	}
//
////	private void setStyle() {
//////		 DocxStyle style = new DocxStyle();
//////	        style.setBold(true);
//////	        style.setItalic(true);
//////	        style.setUnderline(true);
//////	        style.setFontSize("40");
//////	        style.setFontColor("FF0000");
//////	        style.setFontFamily("Book Antiqua");
//////	        style.setTop(300);
//////	        style.setBackground("CCFFCC");        
//////	        style.setVerticalAlignment(STVerticalJc.CENTER);
//////	        style.setHorizAlignment(JcEnumeration.CENTER);
//////	        style.setBorderTop(true);
//////	        style.setBorderBottom(true);
//////	        style.setNoWrap(true);
////
////	}
//	private static void setHeaderStyle(ObjectFactory factory, RPr rpr, Tc tc, R r, String backgroundColor,
//			String textColor) {
//		BooleanDefaultTrue b = new BooleanDefaultTrue();
//		rpr.setB(b);
//		rpr.setCaps(b);
//		Color color = factory.createColor();
//		color.setVal(textColor);
//		rpr.setColor(color);
//		r.setRPr(rpr);
//		TcPr tableCellProperties = tc.getTcPr();
//		if (tableCellProperties == null) {
//			tableCellProperties = new TcPr();
//			tc.setTcPr(tableCellProperties);
//		}
//		CTShd shd = new CTShd();
//		shd.setFill(backgroundColor);
//		tableCellProperties.setShd(shd);
//	}
//
//	private static void setFontSize(RPr runProperties, String fontSize) {
//		if (fontSize != null && !fontSize.isEmpty()) {
//			HpsMeasure size = new HpsMeasure();
//			size.setVal(new BigInteger(fontSize));
//			runProperties.setSz(size);
//			runProperties.setSzCs(size);
//		}
//	}
//
//	private static void setFontFamily(RPr runProperties, String fontFamily) {
//		if (fontFamily != null) {
//			RFonts rf = runProperties.getRFonts();
//			if (rf == null) {
//				rf = new RFonts();
//				runProperties.setRFonts(rf);
//			}
//			rf.setAscii(fontFamily);
//		}
//	}
//}
