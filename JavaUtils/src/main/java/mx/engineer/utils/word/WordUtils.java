package mx.engineer.utils.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class WordUtils {
    private static final String BOLD_CLOSE_TAG = "</b>";
    private static final String BOLD_OPEN_TAG = "<b>";
    private static final String CAR_RETURN = "\n";
    private static final String COLOR_3300FF = "3300FF";
    private static final String TAG_OPENING = "[&";
    private static final String TAG_CLOSING = "&]";
    private static final String EMPTY = "";
    private static final String EXTENSION_DOC = ".doc";
    private static final String EXTENSION_DOCX = ".docx";
    private String key;
    private Boolean colorMark = false;
    
    public final void setColorMark(final boolean colorMarkParameter) {
        this.colorMark = colorMarkParameter;
    }

    private String builtOutputPathFile(final File file, final String outputPathFile, final String fileType) {
        return outputPathFile + File.separator + FilenameUtils.removeExtension(file.getName()) + "-" + 
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + fileType;
    }

    public final File fillWordDocTemplate(final File fileTemplate, final Map <String, String> mapValues, 
            final String outputPathFile) throws IOException {
        final HWPFDocument hwpfDocument = new HWPFDocument(new FileInputStream(fileTemplate.getPath()));
        this.getMapList(mapValues, hwpfDocument.getRange());
        final String newOutputPathFile = this.builtOutputPathFile(fileTemplate, outputPathFile, EXTENSION_DOC);
        final FileOutputStream fileOutputStream =
                new FileOutputStream(newOutputPathFile);
        hwpfDocument.write(fileOutputStream);
        fileOutputStream.close();
        return new File(newOutputPathFile);
    }

    private void getMapList(final Map<String, String> mapValues, final Range range) {
        for (Entry<String, String> entryValue : mapValues.entrySet()) {
            this.key = entryValue.getKey().replace(TAG_OPENING, "[%").replace(TAG_CLOSING, "%]");
            range.replaceText(entryValue.getKey(), this.key);
            this.getCharacterRuns(range);
            range.replaceText(this.key, entryValue.getValue());
        }
    }

    private void getCharacterRuns(final Range range) {
        final Integer color = 9;
        for (int index = 0; index < range.numCharacterRuns(); index++)
            if (range.getCharacterRun(index).text().contains(this.key)) {
                this.changeTextColor(range, color, index);
            }
    }

    private void changeTextColor(final Range range, final Integer color, final int index) {
        if (this.colorMark)
            range.getCharacterRun(index).setColor(color);
    }

    public final File fillWordDocxTemplate(final File fileTemplate, final Map <String, String> mapValues, 
            final String outputPathFile) throws IOException {
        final XWPFDocument xwpfDocument = new XWPFDocument(new FileInputStream(fileTemplate.getPath()));
        this.getEntryMapList(mapValues, xwpfDocument);
        final String newOutputPathFile = this.builtOutputPathFile(fileTemplate, outputPathFile, EXTENSION_DOCX);
        final FileOutputStream fileOutputStream =
                new FileOutputStream(newOutputPathFile);
        xwpfDocument.write(fileOutputStream);
        fileOutputStream.close();
        return new File(newOutputPathFile);
    }

    private void getEntryMapList(final Map<String, String> mapValues, final XWPFDocument xwpfDocument) {
        for (Entry<String, String> entryValue : mapValues.entrySet()) {
            this.getTableList(entryValue, xwpfDocument);
            this.getParagraphList(entryValue, xwpfDocument);
        }
    }

    private void getTableList(final Entry<String, String> entryValue, final XWPFDocument xwpfDocument) {
        for (XWPFTable xwpfTable : xwpfDocument.getTables())
            this.getTableRowList(entryValue, xwpfTable);
    }

    private void getTableRowList(final Entry<String, String> entryValue, final XWPFTable xwpfTable) {
        for (XWPFTableRow xwpfTableRow : xwpfTable.getRows())
            this.getTableCellList(entryValue, xwpfTableRow);
    }

    private void getTableCellList(final Entry<String, String> entryValue, final XWPFTableRow xwpfTableRow) {
        for (XWPFTableCell xwpfTableCell : xwpfTableRow.getTableCells())
            this.getParagraphList(entryValue, xwpfTableCell);
    }

    private void getParagraphList(final Entry<String, String> entryValue, final XWPFTableCell xwpfTableCell) {
        for (XWPFParagraph xwpfParagraph : xwpfTableCell.getParagraphs())
            this.compareValueToParagraphValues(entryValue, xwpfParagraph);
    }

    private void getParagraphList(final Entry<String, String> entryValue, final XWPFDocument xwpfDocument) {
        for (XWPFParagraph xwpfParagraph : xwpfDocument.getParagraphs())
            this.compareValueToParagraphValues(entryValue, xwpfParagraph);
    }

    private void compareValueToParagraphValues(final Entry<String, String> entryValue,
            final XWPFParagraph xwpfParagraph) {
        if (xwpfParagraph.getParagraphText().contains(entryValue.getKey())) {
            this.getRunList(entryValue, xwpfParagraph);
        }
    }

    private void getRunList(final Entry<String, String> entryValue, final XWPFParagraph xwpfParagraph) {
        if (xwpfParagraph.getText().contains(entryValue.getKey())) {
            final List<XWPFRun> xwpfRunList = xwpfParagraph.getRuns();
            this.key = entryValue.getKey().replace(TAG_OPENING, EMPTY).replace(TAG_CLOSING, EMPTY);
            this.getRuns(entryValue, xwpfRunList);
        }
    }

    private void getRuns(final Entry<String, String> entryValue, final List<XWPFRun> xwpfRunList) {
        for (int index = 0; index < xwpfRunList.size(); index++) {
            this.replaceKey(entryValue, xwpfRunList, index);
        }
    }
    
    private void replaceKey(final Entry<String, String> entryValue,
            final List<XWPFRun> xwpfRunList, final int index) {
        if (this.containsKey(index, xwpfRunList)) {
            this.replaceTag(entryValue, xwpfRunList, index);
        }
    }

    private void replaceTag(final Entry<String, String> entryValue, final List<XWPFRun> xwpfRunList, final int index) {
        xwpfRunList.get(index - 2).setText(xwpfRunList.get(index - 2).toString().replace(TAG_OPENING, EMPTY), 0);
        final XWPFRun closingRun = xwpfRunList.get(index);
    	this.replaceAndFormatTag(entryValue.getValue(), xwpfRunList.get(index - 1));
        closingRun.setText(closingRun.toString().replace(TAG_CLOSING, EMPTY), 0);
    }
    
    private void replaceAndFormatTag(final String value, final XWPFRun runParameter) {
        final String[] lines = value.split(CAR_RETURN);
        XWPFRun run = runParameter;
        final XWPFParagraph xwpfParagraph = (XWPFParagraph) run.getParent();
        int runIndex = this.getRunIndex(run, xwpfParagraph);
        for (String line : lines) {
            XWPFRun carriageRun = run;
            run.setText(line, 0);
            run.setBold(false);
            if (line.contains(BOLD_OPEN_TAG)) {
                run.setText(line.substring(0, line.indexOf(BOLD_OPEN_TAG)), 0);
                run.setBold(false);
                carriageRun = this.createBoldRun(xwpfParagraph, ++runIndex, line);
                runIndex = this.processPostBold(xwpfParagraph, runIndex, line);
                if (this.isStringAfterBoldRun(line))
                    carriageRun = xwpfParagraph.getRuns().get(runIndex);
            }
            this.addCarriageReturn(lines, carriageRun);
            this.setRunColorMark(run);
            run = xwpfParagraph.insertNewRun(++runIndex);
        }
    }

    private XWPFRun createBoldRun(final XWPFParagraph xwpfParagraph, final int runIndex, final String line) {
        final XWPFRun boldRun = xwpfParagraph.insertNewRun(runIndex);
        boldRun.setText(line.substring(line.indexOf(BOLD_OPEN_TAG) + BOLD_OPEN_TAG.length(),
                line.indexOf(BOLD_CLOSE_TAG)), 0);
        boldRun.setBold(true);
        this.setRunColorMark(boldRun);
        return boldRun;
    }

    private boolean isStringAfterBoldRun(final String line) {
        return line.substring(line.indexOf(BOLD_CLOSE_TAG) + BOLD_CLOSE_TAG.length()).length() > 0;
    }

    private int processPostBold(final XWPFParagraph xwpfParagraph, final int runIndexParameter, final String line) {
        int runIndex = runIndexParameter;
        if (this.isStringAfterBoldRun(line)) {
            final XWPFRun postBoldRun = xwpfParagraph.insertNewRun(++runIndex);
            postBoldRun.setText(line.substring(line.indexOf(BOLD_CLOSE_TAG) + BOLD_CLOSE_TAG.length()), 0);
            this.setRunColorMark(postBoldRun);
            
        }
        return runIndex;
    }
    
    private void addCarriageReturn(final String[] lines, final XWPFRun carriageRun) {
        if (lines.length > 1) {
            carriageRun.addCarriageReturn();
        }
    }

    private int getRunIndex(final XWPFRun run, final XWPFParagraph xwpfParagraph) {
        for (int index = 0; index < xwpfParagraph.getRuns().size(); index++) {
            final XWPFRun currentRun = xwpfParagraph.getRuns().get(index);
            if (currentRun == run) {
                return index;
            }
        }
        return 0;
    }
    
    private void setRunColorMark(final XWPFRun run) {
        if (this.colorMark)
            run.setColor(COLOR_3300FF);
    }

    private Boolean containsKey(final Integer index, final List<XWPFRun> xwpfRunList) {
        if (index > 1) {
            final String runs = xwpfRunList.get(index - 2).toString() + xwpfRunList.get(index - 1).toString()
                    + xwpfRunList.get(index).toString();
            return runs.contains(TAG_OPENING + this.key + TAG_CLOSING) ? true : false;
        }
        return false;
    }
    
    
    
}

