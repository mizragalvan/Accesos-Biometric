package mx.engineer.utils.word;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.docx4j.jaxb.Context;
import org.docx4j.sharedtypes.STOnOff;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTBorder;
import org.docx4j.wml.CTTblLook;
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
import org.docx4j.wml.Tbl;
import org.docx4j.wml.TblGrid;
import org.docx4j.wml.TblGridCol;
import org.docx4j.wml.TblPr;
import org.docx4j.wml.TblWidth;
import org.docx4j.wml.Tc;
import org.docx4j.wml.TcPr;
import org.docx4j.wml.TcPrInner.GridSpan;
import org.docx4j.wml.TcPrInner.TcBorders;
import org.docx4j.wml.Text;
import org.docx4j.wml.Tr;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

public class CreaTablaFirmas {

	public static final String ARRENDATARIO = "Arrendatario";
	public static final String ARRENDADOR = "Arrendador";
	public static final String REP = "Representantes";
	public static final String TESTIGOS = "Testigos";
	public static final String TITLETESTIGOS = "Titulo";
	public static final String BLANK = "";
	public static final String NOTDEFSPECIAL = "NotDefSpecial";
	
	private static final String FONT = "Arial";
	private static final String COLOR = "000000";
	private static final String TYPE = "dxa";
	private static final String VAL = "04A0";
	private static final String TBLWIDTH = "9524";
	private static final String DEFAULTSPAN = "1";
	private static final String MAXSPAN = "3";
	private static final String COLFIRMA = "4365";
	private static final String COLCENTER = "794";
	private static final int DEFAULTSPACE = 1;
	public static final String CR = "\\r?\\n";
	
	 private int spaceFirmas = DEFAULTSPACE;
	 private int spaceTestigos = DEFAULTSPACE;
	 private String colorFont = COLOR;
	 private  ObjectFactory factory = Context.getWmlObjectFactory();
	 private Tbl table;
	 private LinkedHashMap<String, List<String>> listas;
	 private Boolean format = false;
	 
	 private List<String[]> banco;
	 private List<String[]> proveedor;
	 
	 private String tagToReplace;
	 
	 public CreaTablaFirmas(final LinkedHashMap<String, List<String>> data) {
		 this.listas = data;
	 }
	 
	 public CreaTablaFirmas(final LinkedHashMap<String, List<String>> data, final String Color) {
		 this.listas = data;
		 this.setColorFont(Color);
	 }
	 
	public final Tbl creaTablaC() {
		this.createFirmas(this.spaceFirmas);
		if (this.listas.get(TESTIGOS).size() > 0) {
    		this.createRenglon(this.listas.get(TITLETESTIGOS), this.spaceTestigos);
    		this.createFirmas(new ArrayList<>(this.listas.get(TESTIGOS)), this.spaceTestigos);
		}
		return this.getTbl();
	}
	
	public final Tbl creaTablaFBorrador() {
		this.setColorFont("#0000FF");
		return this.creaTablaF();
	}
	
	public final Tbl creaTablaCBorrador() {
		this.setColorFont("#0000FF");
		return this.creaTablaC();
	}
	
	public final Tbl creaTablaF() {
		this.createFirmas(this.listas.get(REP), spaceFirmas);
		if (this.listas.get(TESTIGOS).size() > 0) {
    		this.createRenglon(this.listas.get(TITLETESTIGOS), spaceTestigos);
    		this.createFirmas(this.listas.get(TESTIGOS), spaceTestigos);
		}
		return this.getTbl();
	}
	
	private void createRenglon(final List<String> contenido, final Integer br) {
		final Tr tr = this.createRow();
		tr.getContent().add(this.createTcRenglon(contenido, MAXSPAN, br));
		this.getTbl().getContent().add(tr);
	}
	
	private void createFirmas(final int br) {
		int rows = getNumRows();
		for(int i=0;i<rows;i++){
			final Tr tr = this.createRow();
			final Tr tr2 = this.createRow();
			
			this.createTcHeader(tr,COLFIRMA, this.banco.size()>i?this.banco.get(i)[0]:BLANK, br);
			tr.getContent().add(this.createTcBlank(COLCENTER, false));
			this.createTcHeader(tr,COLFIRMA, this.proveedor.size()>i?this.proveedor.get(i)[0]:null, br);

			this.createTc(tr2,COLFIRMA, this.banco.size()>i?true:false,this.banco.size()>i?this.banco.get(i)[1]:BLANK, DEFAULTSPAN, br);
			tr2.getContent().add(this.createTcBlank(COLCENTER, false));
			this.createTc(tr2,COLFIRMA, true,  this.proveedor.size()>i?this.proveedor.get(i)[1]:null, DEFAULTSPAN, 0);
			
			this.getTbl().getContent().add(tr);
			this.getTbl().getContent().add(tr2);
		}
	}
	
	private int getNumRows(){
		
		if(this.banco==null)return this.proveedor==null?0: this.proveedor.size();
		if(this.proveedor==null)return this.banco==null?0: this.banco.size();
		
		if(this.banco.size() > this.proveedor.size()){
			return this.banco.size();
		}else{
			return this.proveedor.size();
		}
	}
	
	
	private void createFirmas(final List<String> completa, final int br) {
		if (completa.size() >= 2) {
			final List<String> temp = completa.subList(0, 2);
			this.getTbl().getContent().add(this.createFilaFirmas(temp, br));
			this.borraElementosUsados(completa, temp.size());
			this.createFirmas(completa, br);
		} else if (completa.size() > 0) {
			this.getTbl().getContent().add(this.createFila(completa, br));
		}
	}

    private void borraElementosUsados(final List<String> completa, final Integer tempSize) {
        for (int size = 0; size < tempSize; size++)
            completa.remove(0);
    }
	
	private  Tr createFila(final List<String> contenidos, final int br) {
		final Tr trRep = this.createRow();
		trRep.getContent().add(this.createTc(COLFIRMA, true, contenidos.get(0), DEFAULTSPAN, br));
		return trRep;
	}
	
	private  Tr createFilaFirmas(final List<String> contenidos, final int br) {
			final Tr trRep = this.createRow();
			trRep.getContent().add(this.createTc(COLFIRMA, this.setBorderTop(contenidos.get(0)),
			        contenidos.get(0), DEFAULTSPAN, br));
			trRep.getContent().add(this.createTcBlank(COLCENTER, false));
			trRep.getContent().add(this.createTc(COLFIRMA, this.setBorderTop(contenidos.get(1)),
			        contenidos.get(1), DEFAULTSPAN, 0));
			return trRep;
	}
	
	private boolean setBorderTop(final String contenidos) {
        return contenidos.trim().length() > 0;
    }
	
	public  Tc createTcHeader(final String width, final boolean border, final String contenidos) {
		final Tc tableCell = this.factory.createTc();
		if(contenidos!=null){
			String[] aux=contenidos.split(CR);
			for (String contenido:aux) {
				tableCell.getContent().add(this.creataParrafo(contenido));
			}
			tableCell.getContent().add(this.creataParrafoBr());
			tableCell.getContent().add(this.creataParrafoBr());
			this.setCellWidth(tableCell, width);
			if (border)this.setBorderTop(tableCell);
		}
		return tableCell;
	}
	
	private  void createTcHeader(final Tr tr, final String width, final String contenidos, final int br) {
		if(contenidos!=null){
			final Tc tableCell = this.factory.createTc();
			String[] aux=contenidos.split(CR);
			for (String contenido:aux) {
				tableCell.getContent().add(this.creataParrafo(contenido));
			}
			for (int i = 0; i < br; i++) {
			    tableCell.getContent().add(this.creataParrafoBr());
			}
			this.setCellWidth(tableCell, width);
			tr.getContent().add(tableCell);
		}
	}
	
	private  Tc createTc(String width,boolean border,String contenido,String span) {
		final Tc tableCell = this.factory.createTc();
		tableCell.getContent().add(this.creataParrafo(contenido));
		this.setCellWidth(tableCell, width);
		if (border)this.setBorderTop(tableCell);
		this.setSpan(tableCell, span);
		return tableCell;
	}
	
	
	private  Tc createTc(String width,boolean border,String contenido,String span,int br) {
		final Tc tableCell = this.createTc(width, border, contenido, span);
		for (int i = 0; i < br; i++) {
			tableCell.getContent().add(this.creataParrafoBr());
		}
		return tableCell;
	}
	
	private  void createTc(Tr tr, String width,boolean border,String contenido,String span,int br) {
		if(contenido!=null){
			final Tc tableCell = this.createTc(width, border, contenido, span);
			for (int i = 0; i < br; i++) {
				tableCell.getContent().add(this.creataParrafoBr());
			}
			tr.getContent().add(tableCell);
		}
	}
	
	
	
	private  Tc createTcRenglon(final List<String> contenidos, final String span, final Integer br) {
		final Tc tableCell = this.factory.createTc();
		for (String contenido:contenidos) {
			tableCell.getContent().add(this.creataParrafo(contenido));
		}
		for (int i = 0; i < br; i++) {
            tableCell.getContent().add(this.creataParrafoBr());
        }
		
		this.setSpan(tableCell, span);
		return tableCell;
	}

	private  Tc createTcBlank(final String width, final boolean border) {
		final Tc tableCell = this.factory.createTc();
		tableCell.getContent().add(this.creataParrafo(BLANK));
		this.setCellWidth(tableCell, width);
		if (border)this.setBorderTop(tableCell);
		return tableCell;
	}
	
	private P creataParrafo(final String contenido) {
		if (this.format) {
			final ParrafoFormat pf = new ParrafoFormat(colorFont,JcEnumeration.CENTER);
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
	
	private P creataParrafoBr() {
		final P parrafo = this.factory.createP();
		final R run = this.factory.createR();
		final R.Cr br = new R.Cr(); 
		run.getContent().add(br);
		parrafo.getContent().add(run);
		return parrafo;
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
	
	private  CTBorder addCTBorder() {
		final CTBorder border = new CTBorder();
		border.setColor("auto");
		border.setSz(new BigInteger("4"));
		border.setSpace(new BigInteger("0"));
		border.setVal(STBorder.SINGLE);
		return border;
	}
	
	private  void setBorderTop(final Tc tableCell) {
		this.verifyTcPr(tableCell);
		final TcBorders borders = new TcBorders();
		borders.setTop(this.addCTBorder());
		tableCell.getTcPr().setTcBorders(borders);	
	}
	
	private  void setCellWidth(final Tc tableCell, final String width) {
		this.verifyTcPr(tableCell);
			tableCell.getTcPr().setTcW(new TblWidth());
			tableCell.getTcPr().getTcW().setW(new BigInteger(width));
			tableCell.getTcPr().getTcW().setType(TYPE);
	}
	
	private  void setSpan(final Tc tableCell, final String span) {
		this.verifyTcPr(tableCell);
		tableCell.getTcPr().setGridSpan(new GridSpan());
		tableCell.getTcPr().getGridSpan().setVal(new BigInteger(span));
	}
	
	private  void verifyTcPr(final Tc tableCell) {
		if (tableCell.getTcPr() == null) {
			tableCell.setTcPr(new TcPr());
		}
	}

	public final  Tr createRow() {
   	 	return this.factory.createTr();
	}

	private  TblWidth createTblWidth(final String width, final String tipo) {
		final TblWidth tblind = new TblWidth();
		tblind.setW(new BigInteger(width));
		tblind.setType(tipo);
		return tblind;
	}
	
	private  CTTblLook createCTTblLook() {
		final CTTblLook look = new CTTblLook();
		look.setFirstRow(STOnOff.ONE);
		look.setLastRow(STOnOff.ZERO);
		look.setFirstColumn(STOnOff.ONE);
		look.setLastColumn(STOnOff.ZERO);
		look.setNoVBand(STOnOff.ONE);
		look.setNoHBand(STOnOff.ZERO);
		look.setVal(VAL);
		return look;
	}
	
	private  Tbl getTbl() {
		if (this.table == null) {
			this.table = this.factory.createTbl();
			this.table.setTblPr(this.createTblPr());
			this.table.setTblGrid(this.createTblGrid());
		}
		return this.table;
	}
	
	private  TblPr createTblPr() {
		final TblPr properties = new TblPr();
		properties.setTblW(this.createTblWidth(TBLWIDTH, TYPE));
		properties.setTblLook(this.createCTTblLook());
		return properties;
	}
	
	private  TblGrid createTblGrid() {
		final TblGrid grid = new TblGrid();
		grid.getGridCol().add(this.createTblGridCol(COLFIRMA));
		grid.getGridCol().add(this.createTblGridCol(COLCENTER));
		grid.getGridCol().add(this.createTblGridCol(COLFIRMA));
		return grid;
	}
	
	private  TblGridCol createTblGridCol(final String w) {
		final TblGridCol gridCol = new TblGridCol();
		gridCol.setW(new BigInteger(w));
		return gridCol;
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
	
	 public final Boolean getFormat() {
		return this.format;
	}

	/**
	 * @param format
	 * Verifica Formato dentro de Texto 
	 */
	public final void setFormat(final Boolean forma) {
		this.format = forma;
	}

	public String getColorFont() {
		return colorFont;
	}

	public void setColorFont(String colorFont) {
		this.colorFont = colorFont;
	}

	public int getSpaceFirmas() {
		return spaceFirmas;
	}

	public void setSpaceFirmas(int spaceFirmas) {
		this.spaceFirmas = spaceFirmas;
	}

	public int getSpaceTestigos() {
		return spaceTestigos;
	}

	public void setSpaceTestigos(int spaceTestigos) {
		this.spaceTestigos = spaceTestigos;
	}
	public void setBanco(List<String[]> banco) {
		this.banco = banco;
	}
	public void setProveedor(List<String[]> proveedor) {
		this.proveedor = proveedor;
	}

    public final String getTagToReplace() {
        return this.tagToReplace;
    }

    public final void setTagToReplace(final String tagToReplaceParameter) {
        this.tagToReplace = tagToReplaceParameter;
    }
}