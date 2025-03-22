package mx.engineer.utils.word;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.docx4j.XmlUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.Body;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.RPr;
import org.docx4j.wml.Text;

public class VariablePrepare {
	
	/**
	 * @param wmlPackage
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static void prepare(WordprocessingMLPackage wmlPackage) throws Exception {
	
		WordprocessingMLPackage.FilterSettings filterSettings = new WordprocessingMLPackage.FilterSettings();
		filterSettings.setRemoveProofErrors(true);
		filterSettings.setRemoveContentControls(true);
		filterSettings.setRemoveRsids(true);
		wmlPackage.filter(filterSettings);
		
		org.docx4j.wml.Document wmlDocumentEl = wmlPackage.getMainDocumentPart().getContents();
		Body body =  wmlDocumentEl.getBody();
						
		SingleTraversalUtilVisitorCallback paragraphVisitor 
			= new SingleTraversalUtilVisitorCallback(
					new TraversalUtilParagraphVisitor());
		paragraphVisitor.walkJAXBElements(body);
	}
	
    private final static QName _RT_QNAME = new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "t");
    
	public static void main(String[] args) throws Exception {

		String inputfilepath = System.getProperty("user.dir") + "/absoluteAnchor.docx";
		
		WordprocessingMLPackage wmlPackage = WordprocessingMLPackage.load(new java.io.File(inputfilepath));
		
		P p = (P)wmlPackage.getMainDocumentPart().getContent().get(2);
		
		System.out.println(XmlUtils.marshaltoString(p, true, true));
		joinupRuns(p);
		System.out.println(XmlUtils.marshaltoString(p, true, true));
	}
	
	
	public static void joinupRuns(P p) {

		List<Object> existingContents = p.getContent();
		List<Object> newContents = new ArrayList<Object>();
		
		R currentR = null;
		String currentRPrString = null;
		
		// First join up runs with same run properties
		for (Object o : existingContents) {
			
			if (o instanceof R) {
				
				if (currentR==null) { // first object, or after something not a run
					currentR=(R)o;
					if (currentR.getRPr()!=null) {
						currentRPrString = XmlUtils.marshaltoString(currentR.getRPr());
					}
					newContents.add(currentR);
				} else {
					RPr other = ((R)o).getRPr();
					
					boolean makeNewRun = true; // unless proven otherwise
					
					if (currentRPrString==null && other==null) makeNewRun=false;
					if (currentRPrString!=null && other!=null) {
						// Simple minded notion of equality
						if ( XmlUtils.marshaltoString(other).equals(currentRPrString) )  makeNewRun=false; 
					}
					
					if (makeNewRun) {
						currentR=(R)o;
						if (currentR.getRPr()==null) {
							currentRPrString = null;
						} else {
							currentRPrString = XmlUtils.marshaltoString(currentR.getRPr());
						}
						newContents.add(currentR);
					} else {
						currentR.getContent().addAll( ((R)o).getContent() );
					}
				}
				
			} else {
				newContents.add(o);
				currentR = null;
				currentRPrString = null;
			}
			
		}
				
		for (Object o : newContents) {
			
			if (o instanceof R) {
				
				List<Object> newRunContents = new ArrayList<Object>();	
				JAXBElement<?> currentT = null;
				for ( Object rc : ((R)o).getContent() ) {
					
					if (rc instanceof JAXBElement&& ((JAXBElement<?>)rc).getName().equals(_RT_QNAME)) {
						
						if (currentT==null) { // first object, or after something not a w:t
							currentT=(JAXBElement<?>)rc;
							newRunContents.add(currentT);
						} else {
							Text currentText = (Text)XmlUtils.unwrap(currentT);
							String val = currentText.getValue();
							
							currentText.setValue(val + ((Text)XmlUtils.unwrap(rc)).getValue() );								
						}
						
						// <w:t xml:space="preserve">
						if (((Text)XmlUtils.unwrap(rc)).getSpace()!=null
								&& ((Text)XmlUtils.unwrap(rc)).getSpace().equals("preserve")) { // any of them
							((Text)XmlUtils.unwrap(currentT)).setSpace("preserve");
						}
						
					} else {
						// not text .. just add it and move on
						newRunContents.add(rc);
						currentT = null;
					}
				
				}
				
				((R)o).getContent().clear();
				((R)o).getContent().addAll(newRunContents);
				
			}
		
		}
		
		// Now replace w:p contents
		p.getContent().clear();
		p.getContent().addAll(newContents);
		
	}

	public static class TraversalUtilParagraphVisitor extends TraversalUtilVisitor<P> {
		
		@Override
		public void apply(P p, Object parent, List<Object> siblings) {
			joinupRuns(p);
		}
	
	}
}
