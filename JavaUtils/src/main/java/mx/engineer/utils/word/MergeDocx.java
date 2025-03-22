package mx.engineer.utils.word;

import java.io.File;
import java.util.List;

import jakarta.xml.bind.JAXBException;

import org.docx4j.dml.CTBlip;
import org.docx4j.jaxb.XPathBinderAssociationIsPartialException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;

public class MergeDocx {
	
	private static final String BLIP="//a:blip";
	private static final String BODY="//w:body";
	private static final String WORD="/word/";
	
	    
		public static void main(String[] args) {
		    try {
		    	
		    	File base = new File("C:\\Contratos\\CatalogoTipoDocumentos\\Prestación_de_Servicios_Entidades.docx");
		    	File add = new File("C:\\Contratos\\CatalogoTipoDocumentos\\Test.docx");
		    	File out = new File("C:\\Contratos\\CatalogoTipoDocumentos\\Test3.docx");
		    	MergeDocx.merge2Docx(base,add,out);
			} catch ( Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
	}
		  
		  public static void merge2Docx(final File base,final File add,final File out) throws Docx4JException, JAXBException{
			    WordprocessingMLPackage f = WordprocessingMLPackage.load(base);
			    WordprocessingMLPackage s = WordprocessingMLPackage.load(add);
			    addBody(s, f);
			    addBlip(s, f);
			    f.save(out);
		  }
		  
		  private static void addBody(final WordprocessingMLPackage s,final WordprocessingMLPackage f) throws XPathBinderAssociationIsPartialException, JAXBException{
			    for(Object b : s.getMainDocumentPart().getJAXBNodesViaXPath(BODY, false)){
			        List<Object> filhos = ((org.docx4j.wml.Body)b).getContent();
			        for(Object k : filhos)
			            f.getMainDocumentPart().addObject(k);
			    }
		  }
		  
		  
		  private static void addBlip(final WordprocessingMLPackage s,final WordprocessingMLPackage f) throws XPathBinderAssociationIsPartialException, JAXBException, InvalidFormatException{
			    for(Object el : s.getMainDocumentPart().getJAXBNodesViaXPath(BLIP, false)){
			            CTBlip blip = (CTBlip) el;
			            RelationshipsPart parts = s.getMainDocumentPart().getRelationshipsPart();
			            Relationship rel = parts.getRelationshipByID(blip.getEmbed());
			            RelationshipsPart docRels = f.getMainDocumentPart().getRelationshipsPart();
			            rel.setId(null);
			            docRels.addRelationship(rel);
			            blip.setEmbed(rel.getId());
			            f.getMainDocumentPart().addTargetPart(s.getParts().getParts().get(new PartName(WORD+rel.getTarget())));
			    }
		  }
}