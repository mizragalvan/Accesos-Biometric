package mx.pagos.admc.examples;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public final class EnvelopeHelpers {

    public static final String ENVELOPE_STATUS_SENT = "sent";
    public static final String ENVELOPE_STATUS_CREATED = "created";
    public static final String SIGNER_STATUS_CREATED = "Created";
    public static final String DELIVERY_METHOD_EMAIL = "Email";
    public static final String SIGNER_ROLE_NAME = "signer";
    public static final String CC_ROLE_NAME = "cc";
    public static final String WORKFLOW_STEP_ACTION_PAUSE = "pause_before";
    public static final String WORKFLOW_TRIGGER_ROUTING_ORDER = "routing_order";
    public static final String WORKFLOW_STATUS_IN_PROGRESS = "in_progress";


    private EnvelopeHelpers() {}

    /**
     * Loads a file content and copy it into a byte array.
     * @param path the absolute path within the class path
     * @return the new byte array that has been loaded from the file
     * @throws IOException in case of I/O errors
     */
    public static byte[] readFile(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToByteArray(resource.getInputStream());
    }

    /**
     * Loads a template of a html document. Usually returned object is kept
     * wherever and it is used when it is needed to generate the html document.
     * Html document is generated by the method.
     * {@link #createHtmlFromTemplate(Template, String, Object)}
     * @param path the path to template file
     * @return a {@link Template} object
     * @throws IOException in case of I/O errors
     */
    static Template loadHtmlTemplate(String path) throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setLocale(Locale.US);
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
        ClassPathResource resource = new ClassPathResource(path);
        String source = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        return new Template(path, source, cfg);
    }

    /**
     * Generates a html document from the template. Placeholder looks like
     * ${objectName.fieldName}
     * @param template the template object
     * @param objectName name of object to find placeholder related to a value
     * @param value object which fields are used as a values in the template
     * @return array of bytes representing created html document
     * @throws IOException in case of template parsing error
     */
    static String createHtmlFromTemplate(Template template, String objectName, Object value) throws IOException {
        Map<String, Object> input = new HashMap<>();
        input.put(objectName, value);
        StringWriter stringWriter = new StringWriter();
        try {
            template.process(input, stringWriter);
            return stringWriter.toString();
        } catch (TemplateException exception) {
            throw new ExampleException("Can't process html template " + template.getName(), exception);
        }
    }

    /**
     * Generates a html document from the template file. Placeholder looks like
     * ${objectName.fieldName}. It is a convenient method which loads template
     * from a file using {@link #loadHtmlTemplate(String)} and creates a html
     * page from the loaded template using {@link #createHtmlFromTemplate(Template, String, Object)}
     * @param path the path to template file
     * @param objectName name of object to find placeholder related to a value
     * @param value object which fields are used as a values in the template
     * @return array of bytes representing created html document
     * @throws IOException in case of I/O errors or template parsing error
     */
    public static byte[] createHtmlFromTemplateFileInByte(String path, String objectName, Object value) throws IOException {
        Template template = loadHtmlTemplate(path);
        return createHtmlFromTemplate(template, objectName, value).getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Generates a html document from the template file. Placeholder looks like
     * ${objectName.fieldName}. It is a convenient method which loads template
     * from a file using {@link #loadHtmlTemplate(String)} and creates a html
     * page from the loaded template using {@link #createHtmlFromTemplate(Template, String, Object)}
     * @param path the path to template file
     * @param objectName name of object to find placeholder related to a value
     * @param value object which fields are used as a values in the template
     * @return array of bytes representing created html document
     * @throws IOException in case of I/O errors or template parsing error
     */
    public static String createHtmlFromTemplateFile(String path, String objectName, Object value) throws IOException {
        Template template = loadHtmlTemplate(path);
        return createHtmlFromTemplate(template, objectName, value);
    }

    /**
     * Loads document from a file and creates a document object that represents
     * loaded document.
     * @param fileName name of the file to load document; the extension of the
     * loading file determines an extension of the created document
     * @param docName the name of the document; it may be differ from the file
     * @param docId identifier of the created document
     * @return the {@link Document} object
     * @throws IOException if document cannot be loaded due to some reason
     */
    public static Document createDocumentFromFile(String fileName, String docName, String docId) throws IOException {
        byte[] buffer = readFile(fileName);
        String extention = FilenameUtils.getExtension(fileName);
        return createDocument(buffer, docName, extention, docId);
    }

    /**
     * Creates a document object from the raw data.
     * @param data the raw data
     * @param documentName the name of the document; it may be differ from the file
     * @param fileExtention the extension of the creating file
     * @param documentId identifier of the created document
     * @return the {@link Document} object
     */
    public static Document createDocument(byte[] data, String documentName, String fileExtention, String documentId) {
        Document document = new Document();
        document.setDocumentBase64(Base64.getEncoder().encodeToString(data));
        document.setName(documentName);
        document.setFileExtension(fileExtention);
        document.setDocumentId(documentId);
        return document;
    }

    /**
     * Create SignHere (see {@link SignHere}) field (also known as tabs) on the
     * documents using anchor (autoPlace) positioning.
     * @param anchorString the anchor string; the DocuSign platform searches
     * throughout your envelope's documents for matching anchor strings
     * @param yOffsetPixels the y offset of anchor in pixels
     * @param xOffsetPixels the x offset of anchor in pixels
     * @return the {@link SignHere} object
     */
    public static SignHere createSignHere(String anchorString, int yOffsetPixels, int xOffsetPixels) {
        SignHere signHere = new SignHere();
        signHere.setAnchorString(anchorString);
        signHere.setAnchorUnits("pixels");
        signHere.setAnchorYOffset(String.valueOf(yOffsetPixels));
        signHere.setAnchorXOffset(String.valueOf(xOffsetPixels));
        return signHere;
    }

    /**
     * Create Tabs object containing a single SignHere (see {@link SignHere})
     * field (also known as tabs) on the documents using anchor (autoPlace) positioning.
     * @param anchorString the anchor string; the DocuSign platform searches
     * throughout your envelope's documents for matching anchor strings
     * @param yOffsetPixels the y offset of anchor in pixels
     * @param xOffsetPixels the x offset of anchor in pixels
     * @return the {@link Tabs} object containing single SignHere object
     */
    public static Tabs createSingleSignerTab(String anchorString, int yOffsetPixels, int xOffsetPixels) {
        SignHere signHere = createSignHere(anchorString, yOffsetPixels, xOffsetPixels);
        return createSignerTabs(signHere);
    }

    /**
     * Creates {@link SignHere} fields (also known as tabs) on the document.
     * @param signs the array of SignHere (see {@link SignHere})
     * @return the {@link Tabs} object containing passed SignHere objects
     */
    public static Tabs createSignerTabs(SignHere... signs) {
        Tabs signerTabs = new Tabs();
        signerTabs.setSignHereTabs(Arrays.asList(signs));
        return signerTabs;
    }

    /**
     * Creates a {@link Recipients} object and add signer and cc to it.
     * @param signer the signer object
     * @param cc the cc object
     * @return the {@link Recipients} object
     */
    public static Recipients createRecipients(Signer signer, CarbonCopy cc) {
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        recipients.setCarbonCopies(Arrays.asList(cc));
        return recipients;
    }

    public static Recipients createSingleRecipient(Signer signer) {
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        return recipients;
    }

    public static Recipients createTwoSigners(Signer signer, Signer signer2) {
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer, signer2));
        return recipients;
    }
}

