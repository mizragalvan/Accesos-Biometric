package mx.engineer.utils.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public final class ExcelUtils {
    
    private static final String UNDERSCORE = "_";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy.MM.dd.HH.mm.ss";
    private static final String XLSX_EXTENSION = ".xlsx";

    private ExcelUtils() { }
    
    public static File createXlsxFromArray(final String path, final String reportName, final String[][] dataMatrix)
            throws IOException {
        final XSSFWorkbook workbook = new XSSFWorkbook();
        final XSSFSheet sheet = workbook.createSheet(reportName);
        iterateRows(dataMatrix, sheet);
        final String date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(new Date());
        validatePath(path);
        final File excelFile = new File(path + File.separator + reportName + UNDERSCORE + date + XLSX_EXTENSION);
        final FileOutputStream outputStream = new FileOutputStream(excelFile);
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
        return excelFile;
    }

    private static void iterateRows(final String[][] dataMatrix, final XSSFSheet sheet) {
        Integer rowNumber = 0;
        for (String[] rowData : dataMatrix) {
            final Row row = sheet.createRow(rowNumber++);
            insertColumnData(rowData, row);
        }
    }

    private static void insertColumnData(final String[] rowData, final Row row) {
        Integer columnNumber = 0;
        for (String columnData : rowData) {
            final Cell cell = row.createCell(columnNumber++);
            cell.setCellValue(columnData);
        }
    }
    
    private static void validatePath(String path) throws IOException {
    	if (!Files.exists(Paths.get(path)))
        	Files.createDirectories(Paths.get(path));
    }
    
    public static File createEmptyUniqueXlsx(final String path, final String fileName) throws ExcelException {
        try {
            final XSSFWorkbook workbook = new XSSFWorkbook();
            final String date = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(new Date());
            final File excelFile = new File(path + File.separator + fileName + UNDERSCORE + date + XLSX_EXTENSION);
            final FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
            return excelFile;
        } catch (IOException ioException) {
            throw new ExcelException(ioException);
        }
    }
    
}
