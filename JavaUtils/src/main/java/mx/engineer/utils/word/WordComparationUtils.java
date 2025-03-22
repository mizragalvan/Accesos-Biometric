package mx.engineer.utils.word;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;
import org.docx4j.XmlUtils;
import org.docx4j.diff.Differencer;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.exceptions.InvalidOperationException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.relationships.Namespaces;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.Body;

public final class WordComparationUtils {
    private static final String DOC_EXTENSION = "doc";
    
    private WordComparationUtils() { }
    
    public static File compare(final File baseFile, final File comparedFile, final String outputPath)
            throws WordComparationException {
        try {
            final File docxBaseFile = getDocxFile(baseFile, outputPath);
            final File docxComparedFile = getDocxFile(comparedFile, outputPath);
            return compareText(docxBaseFile, docxComparedFile, outputPath);
        } catch (JAXBException | Docx4JException | IOException | OpenXML4JException | XmlException exception) {
            throw new WordComparationException(exception);
        }
    }

    private static File getDocxFile(final File file, final String outputPath) throws IOException,
                    OpenXML4JException, XmlException, Docx4JException, WordComparationException {
        final File docxFile = file;
        if (DOC_EXTENSION.equals(FilenameUtils.getExtension(file.getName()).toLowerCase()))
            throw new WordComparationException("Ambos archivos deben ser docx para poder compararse");
        return docxFile;
    }

    private static File compareText(final File baseFile, final File comparedFile, final String outputPath)
            throws JAXBException, Docx4JException {
        final WordprocessingMLPackage oldPackage = WordprocessingMLPackage.load(baseFile);
        final WordprocessingMLPackage newerPackage = WordprocessingMLPackage.load(comparedFile);
        getDifferences(oldPackage, newerPackage);
        final File outputFile = cerateOutputFile(outputPath);
        oldPackage.save(outputFile);
        return outputFile;
     }

    private static void getDifferences(final WordprocessingMLPackage oldPackage,
            final WordprocessingMLPackage newerPackage) throws Docx4JException, JAXBException, jakarta.xml.bind.JAXBException {
        final Body oldBody = oldPackage.getMainDocumentPart().getContents().getBody();
        final Body newerBody = newerPackage.getMainDocumentPart().getContents().getBody();
        final StringWriter stringWriter = new StringWriter();
        final Differencer differencer = new Differencer();
        differencer.diff(newerBody, oldBody, new StreamResult(stringWriter), "System", null,
           newerPackage.getMainDocumentPart().getRelationshipsPart(),
            oldPackage.getMainDocumentPart().getRelationshipsPart());
        final Body newBody = (Body) XmlUtils.unmarshalString(stringWriter.toString(), Context.jc, Body.class);
        
        oldPackage.getMainDocumentPart().getContents().setBody(newBody);
        handleRelationships(differencer, oldPackage.getMainDocumentPart().getRelationshipsPart());
    }

    private static File cerateOutputFile(final String outputPath) {
        final File outputFile = new File(outputPath + getGeneratedDocxName());
        return outputFile;
    }
    
    private static String getGeneratedDocxName() {
        final String now = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return now + ".docx";
    }
     
    private static void handleRelationships(final Differencer differencer, final RelationshipsPart rp)
            throws InvalidFormatException {
        deletePreexistingRelationships(rp);
        addComposedRelationships(differencer, rp);
    }

    private static void addComposedRelationships(final Differencer differencer, final RelationshipsPart rp) {
        final Map<Relationship, Part> newRels = differencer.getComposedRels();
        final Set<Relationship> set = newRels.keySet();
        for (Relationship nr : set) {
            try {
                rp.addRelationship(nr);
            } catch (InvalidOperationException invalidOperationException) { }
        }
    }

    private static void deletePreexistingRelationships(final RelationshipsPart rp) {
        for (Relationship r : getRelationshipsToRemove(rp)) {
            rp.removeRelationship(r);
        }
    }

    private static List<Relationship> getRelationshipsToRemove(final RelationshipsPart rp) {
        final List<Relationship> relsToRemove = new ArrayList<Relationship>();
        for (Relationship r : rp.getRelationships().getRelationship()) {
            if (r.getType().equals(Namespaces.IMAGE)) {
                relsToRemove.add(r);
            }
        }
        return relsToRemove;
    }
}
