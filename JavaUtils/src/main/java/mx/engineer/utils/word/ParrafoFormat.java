package mx.engineer.utils.word;

import java.math.BigInteger;

import org.docx4j.jaxb.Context;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Br;
import org.docx4j.wml.Color;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Jc;
import org.docx4j.wml.JcEnumeration;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.R.Tab;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;
import org.docx4j.wml.U;
import org.docx4j.wml.UnderlineEnumeration;

public class ParrafoFormat {
	
	public static final String BR = "<br>";
	public static final String N = "/n";
	public static final String R = "/r";
	public static final String B = "<b>";
	public static final String BN = "</b>";
	public static final String U = "<u>";
	public static final String UN = "</u>";
	public static final String CR = "\\r?\\n";
	private static final String COLOR = "000000";
	private static final String BLANK = "";
	private static final int END = 4;
	private  ObjectFactory factory = Context.getWmlObjectFactory();
	private P parrafo = this.factory.createP();
	private int index;
	private String colorFont = COLOR; 
	private String PRESERVE = "preserve"; 
	private JcEnumeration align = JcEnumeration.BOTH; 
	private R properties ; 
	public static final String NOTDEFSPECIAL = "NotDefSpecial";
	
	private static final String FONT = "Arial";
	
	
	public ParrafoFormat() {
		this.parrafo.setPPr(this.addPPr());
	}
	
	public ParrafoFormat(String color) {
		this.setColorFont(color);
		this.parrafo.setPPr(this.addPPr());
	}
	
	public ParrafoFormat(JcEnumeration en) {
		this.setAlign(en);
		this.parrafo.setPPr(this.addPPr());
	}
	
	public ParrafoFormat(String color,JcEnumeration en) {
		this.setColorFont(color);
		this.setAlign(en);
		this.parrafo.setPPr(this.addPPr());
	}
	
	public void creaCr() {
		final R run = this.factory.createR();
		R.Cr cr=new R.Cr();
		run.getContent().add(cr);
		parrafo.getContent().add(run);
	}
	
	public void creaCrIndex() {
		final R run = this.factory.createR();
		R.Cr cr=new R.Cr();
		run.getContent().add(cr);
		parrafo.getContent().add(index++,run);
	}
	
	public void creaBr() {
		final R run = this.factory.createR();
		final Br br = this.factory.createBr(); 
		run.getContent().add(br);
		parrafo.getContent().add(run);
	}
	
	private R creaRun(final String contenido, final  boolean bold, final UnderlineEnumeration underline) {
		final Text text = this.factory.createText();
		text.setValue(contenido);
		text.setSpace(PRESERVE);
		final R run = this.factory.createR();
		run.getContent().add(text);
		run.setRPr(this.addRPr(bold, underline));
		return run;
	}
	
	public static R creaRunClean(final String contenido,final P p ,final int index,final R prop){
		ParrafoFormat parr=new ParrafoFormat();
		R run =parr.creaRun(contenido);
		run.setRPr(prop.getRPr());
		run.setRsidDel(prop.getRsidDel());
		run.setRsidR(prop.getRsidR());
		run.setRsidRPr(prop.getRsidRPr());
		run.setParent(p);
		p.getContent().add(index, run);
		return run;
	}
	
	public static int creaRun(final String contenido,final P p ,final int index,boolean borrador, final R prop){
		ParrafoFormat parr=new ParrafoFormat();
		if(borrador)parr.setColorFont("0000FF");
		parr.setParrafo(p);
		parr.setIndex(index);
		parr.setProperties(prop);
		String lines[] = contenido.split(CR);
		for (int i=0;i<lines.length;i++) {
			parr.verifyTextIndex(lines[i], false, UnderlineEnumeration.NONE);
			if(i!=(lines.length-1))
				parr.creaCrIndex();
		}
		
 		return parr.getIndex();
	}
	
	
	public R creaRun(final String contenido) {
		final Text text = this.factory.createText();
		text.setValue(contenido);
		text.setSpace(PRESERVE);
		final R run = this.factory.createR();
		run.getContent().add(text);
		return run;
	}
	
	private RPr addRPr(final  boolean bold, final UnderlineEnumeration underline) {
		final RPr rpr = new RPr();
		rpr.setB(this.setBoolean(bold));
		rpr.setColor(this.setColor(colorFont));
		rpr.setU(this.setU(underline));
		rpr.setRFonts(getProperties().getRPr()==null?null:getProperties().getRPr().getRFonts());
		rpr.setSz(getProperties().getRPr()==null?null:getProperties().getRPr().getSz());
		rpr.setSzCs(getProperties().getRPr()==null?null:getProperties().getRPr().getSzCs());
		return rpr;
	}
	
	private HpsMeasure addSize() {
		final HpsMeasure size = new HpsMeasure();
	    size.setVal(BigInteger.valueOf(21));
	    return size;
	}
	
	private RFonts setRFonts(final String fontfuente) {
		final RFonts font = new RFonts();
		font.setCs(fontfuente);
		font.setAscii(FONT);
		font.setEastAsia(NOTDEFSPECIAL);
		return font;
	}
	
	private PPr addPPr() {
		final PPr ppr = new PPr();
		ppr.setAdjustRightInd(this.setBoolean(false));
		ppr.setAutoSpaceDE(this.setBoolean(false));
		ppr.setAutoSpaceDN(this.setBoolean(false));
		ppr.setJc(this.setJc(align));
		return ppr;
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
	
	public final P creaParrafo(final String contenido) {
		this.prepareContenido(contenido);
		return this.parrafo;
	}
	
	
	private void prepareContenido(final String contenido) {
		String lines[] = contenido.split(CR);
		for (int i=0;i<lines.length;i++) {
			this.verify(lines[i], false, UnderlineEnumeration.NONE);
			if(i!=(lines.length-1))
				this.creaCr();
		}
	}

	private void verify(final String base, final  boolean bold, final UnderlineEnumeration underline) {
		final int ib = base.indexOf(B);
		final int iu = base.indexOf(U);
		final int ibn = base.indexOf(BN) + END;
		final int iun = base.indexOf(UN) + END;
		
		if (ib >= 0 || iu >= 0) {
			if (iu < 0 || (ib >= 0 && ib < iu)) {
				this.verify(base.substring(0, ib), bold, underline);
				this.textBold(base.substring(ib, ibn), underline);
				this.verify(base.substring(ibn), bold, underline);
			}else if (ib < 0 || (iu >= 0 && iu < ib)) {
				this.verify(base.substring(0, iu), bold, underline);
				this.textU(base.substring(iu, iun), bold);
				this.verify(base.substring(iun), bold, underline);
			}
		} else {
			this.parrafo.getContent().add(this.creaRun(base, bold, underline));
		}
	}
	
	private void verifyTextIndex(final String base, final  boolean bold, final UnderlineEnumeration underline) {
		final int ib = base.indexOf(B);
		final int iu = base.indexOf(U);
		final int ibn = base.indexOf(BN) + END;
		final int iun = base.indexOf(UN) + END;
		
		if (ib >= 0 || iu >= 0) {
			if (iu < 0 || (ib >= 0 && ib < iu)) {
				this.verifyTextIndex(base.substring(0, ib), bold, underline);
				this.textBoldIndex(base.substring(ib, ibn), underline);
				this.verifyTextIndex(base.substring(ibn), bold, underline);
			}else if (ib < 0 || (iu >= 0 && iu < ib)) {
				this.verifyTextIndex(base.substring(0, iu), bold, underline);
				this.textUIndex(base.substring(iu, iun), bold);
				this.verifyTextIndex(base.substring(iun), bold, underline);
			}
		} else {
			if(base.isEmpty())return;
			
			R run =this.creaRun(base, bold, underline);
			run.setParent(this.parrafo);
			
			this.parrafo.getContent().add(index++,run);
		}
	}
	
	private void textBoldIndex(final String base, final UnderlineEnumeration underline) {
		this.verifyTextIndex(base.replace(B, BLANK).replace(BN, BLANK), true, underline);
	}
	
	private void textUIndex(final String base, final  boolean bold) {
		this.verifyTextIndex(base.replace(U, BLANK).replace(UN, BLANK), bold, UnderlineEnumeration.SINGLE);
	}
	
	private void textBold(final String base, final UnderlineEnumeration underline) {
		this.verify(base.replace(B, BLANK).replace(BN, BLANK), true, underline);
	}
	
	private void textU(final String base, final  boolean bold) {
		this.verify(base.replace(U, BLANK).replace(UN, BLANK), bold, UnderlineEnumeration.SINGLE);
	}

	public String getColorFont() {
		return colorFont;
	}

	public void setColorFont(String colorFont) {
		this.colorFont = colorFont;
	}

	public JcEnumeration getAlign() {
		return align;
	}

	public void setAlign(JcEnumeration align) {
		this.align = align;
	}
	

	public P getParrafo() {
		return parrafo;
	}

	public void setParrafo(P parrafo) {
		this.parrafo = parrafo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public R getProperties() {
		if(properties==null){
			properties=new R();
		}
		return properties;
	}

	public void setProperties(R properties) {
		this.properties = properties;
	}
	
	public static R creaRunTab(final P p ,final int index,final R prop){
		ParrafoFormat parr=new ParrafoFormat();
		R run =parr.creaTab();
		run.setRPr(prop.getRPr());
		run.setRsidDel(prop.getRsidDel());
		run.setRsidR(prop.getRsidR());
		run.setRsidRPr(prop.getRsidRPr());
		run.setParent(p);
		p.getContent().add(index, run);
		return run;
	}
	
	public R creaTab() {
		final Tab tab=this.factory.createRTab();
		final R run = this.factory.createR();
		run.getContent().add(tab);
		return run;
	}
	
}
