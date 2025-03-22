package mx.engineer.utils.word;

import java.math.BigInteger;
import java.util.List;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTTblCellMar;
import org.docx4j.wml.CTTblLayoutType;
import org.docx4j.wml.Color;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STBorder;
import org.docx4j.wml.STTblLayoutType;
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner.TcBorders;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

public class CreaTablaSeleccion {

	 private static final int CT = 2552;
	 private static final int CTA = 567;
	 private static final String TW = "779";
	 private static final String M = "70";
	 private static final String TYPE = "dxa";
	 private static final String SPACE = "   ";
	 private static final String COLOR = "000000";
	 private static final String NOTDEFSPECIAL = "NotDefSpecial";
	 private static final String FONT = "Arial";
		
	 private  ObjectFactory factory = Context.getWmlObjectFactory();
	 private String leftMargin = M;
	 private String rightMargin = M;
	 private String tblWidth = TW;
	 private int colTable = CTA;
	 private int colText = CT;

	 private int filas=2;
	 private int columnas=3;
	    
	 private Tbl table;
	 private List<String> lista ;
	 private Boolean format = false;
	 private String colorFont = COLOR;
	 
	 public CreaTablaSeleccion(final List<String> list) {
		 this.lista = list;
	}
	 
	public final  Tr createRow(){
	    	 Tr tr = factory.createTr();
	    	 return tr;
	}
	    
	private  TblWidth createTblWidth(final String width,final String tipo){
	   TblWidth tblind=new TblWidth();
	   tblind.setW(new BigInteger(width));
	   tblind.setType(tipo);
	   return tblind;
	}
	
	
	private  Tbl getTbl(){
		if(table==null){
			table = factory.createTbl();
			table.setTblPr(createTblPr());
			table.getTblPr().getTblCellMar().setLeft(createTblWidth(leftMargin,TYPE));
			table.getTblPr().getTblCellMar().setRight(createTblWidth(rightMargin,TYPE));
			table.getTblPr().setTblInd(createTblWidth(tblWidth,TYPE));
		}
		return table;
	}
	    
	private  TblPr createTblPr(){
		TblPr properties=new TblPr();
		properties.setTblCellMar(new CTTblCellMar());
		properties.setTblLayout(new CTTblLayoutType());
		properties.getTblLayout().setType(STTblLayoutType.FIXED);
		return properties;
	}
	    
	public  Tbl creaTabla(){
		int countTextos=0;
		for(int i=0;i<filas;i++){
			Tr tr =createRow();
			for(int j=0;j<columnas;j++){
				agregaCelda(tr, countTextos);
				countTextos++;
			}
			getTbl().getContent().add(tr);
			if(i<(filas-1)){
				getTbl().getContent().add(agregaFilaBlank());
			}
		}
		return getTbl();
	}
	
	private Tr agregaFilaBlank(){
		Tr trBlank =createRow();
		addTableCell(trBlank, SPACE,colTable,false);
		return trBlank;
	}
	
	private void agregaCelda(Tr tr,int countTextos){
		if(countTextos<lista.size()){
			addTableCell(tr, SPACE,colTable,true);
			addTableCell(tr, lista.get(countTextos),colText,false);
		}else
			addTableCell(tr, SPACE,colTable,false);
	}
	
	private P creataParrafo(final String contenido) {
		if (this.format) {
			final ParrafoFormat pf = new ParrafoFormat(colorFont, JcEnumeration.CENTER);
			return  pf.creaParrafo(contenido);
		} else {
			final P parrafo = this.factory.createP();
			final R run = this.factory.createR();
			final Text text = this.factory.createText();
			text.setValue(contenido);
			run.getContent().add(text);
			parrafo.getContent().add(run);
			run.setRPr(this.addRPr(true, UnderlineEnumeration.NONE));
			parrafo.setPPr(this.addPPr());
			return parrafo;
		}
		
	}
	
	private PPr addPPr() {
		final PPr ppr = new PPr();
		ppr.setAdjustRightInd(this.setBoolean(false));
		ppr.setAutoSpaceDE(this.setBoolean(false));
		ppr.setAutoSpaceDN(this.setBoolean(false));
		ppr.setJc(this.setJc(JcEnumeration.CENTER));
		return ppr;
	}
	
	private RPr addRPr(final  boolean bold, 
			 final UnderlineEnumeration underline) {
		final RPr rpr = new RPr();
		rpr.setRFonts(this.setRFonts(FONT));
		rpr.setB(this.setBoolean(bold));
		rpr.setColor(this.setColor(colorFont));
		rpr.setU(this.setU(underline));
		return rpr;
	}
	
	private  void addTableCell(Tr tableRow, String content, int width,boolean borders) {
		Tc tableCell = factory.createTc();
		tableCell.getContent().add(creataParrafo(content));
		tableRow.getContent().add(tableCell);
		setCellWidth(tableCell, width);
		addBorders(tableCell,borders);
	}
	
	private  TcBorders addTcBorders(){
		TcBorders borders = new TcBorders();
		borders.setBottom(addCTBorder());
		borders.setLeft(addCTBorder());
		borders.setRight(addCTBorder());
		borders.setTop(addCTBorder());
		borders.setInsideH(addCTBorder());
		borders.setInsideV(addCTBorder());
		return borders;
	}
	
	private  CTBorder addCTBorder(){
		CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);
		return border;
	}
	
	private  void addBorders(Tc tableCell,boolean borders){
		if (borders) {
			verifyTcPr(tableCell);
			tableCell.getTcPr().setTcBorders(addTcBorders());
		}
	}
	
	private  void setCellWidth(Tc tableCell, int width) {
		if (width > 0) {
			verifyTcPr(tableCell);
			tableCell.getTcPr().setTcW(new TblWidth());
			tableCell.getTcPr().getTcW().setW(BigInteger.valueOf(width));
		}
	}
	
	private  void verifyTcPr(Tc tableCell){
		if(tableCell.getTcPr()==null){
			tableCell.setTcPr(new TcPr());
		}
	}


	public String getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(String leftMargin) {
		this.leftMargin = leftMargin;
	}

	public String getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(String rightMargin) {
		this.rightMargin = rightMargin;
	}

	public String getTblWidth() {
		return tblWidth;
	}

	public void setTblWidth(String tblWidth) {
		this.tblWidth = tblWidth;
	}

	public int getColTable() {
		return colTable;
	}

	public void setColTable(int colTable) {
		this.colTable = colTable;
	}

	public int getColText() {
		return colText;
	}

	public void setColText(int colText) {
		this.colText = colText;
	}

	public int getFilas() {
		return filas;
	}

	public void setFilas(int filas) {
		this.filas = filas;
	}

	public int getColumnas() {
		return columnas;
	}

	public void setColumnas(int columnas) {
		this.columnas = columnas;
	}
	
	
	private BooleanDefaultTrue setBoolean(final boolean b) {
		final BooleanDefaultTrue bool = new BooleanDefaultTrue();
		bool.setVal(b);
		return bool;
	}
	
	private Jc setJc(final JcEnumeration en) {
		final Jc jc = new Jc();
		jc.setVal(en);
		return jc;
	}
	
	private Color setColor(final String color) {
		final Color c = new Color();
		c.setVal(color);
		return c;
	}
	
	private U setU(final UnderlineEnumeration d) {
		final U u = new U();
		u.setVal(d);
		return u;
	}
	
	private RFonts setRFonts(final String fontfuente) {
		final RFonts font = new RFonts();
		font.setCs(fontfuente);
		font.setEastAsia(NOTDEFSPECIAL);
		return font;
	}

	public String getColorFont() {
		return colorFont;
	}

	public void setColorFont(String colorFont) {
		this.colorFont = colorFont;
	}
	
	
}
