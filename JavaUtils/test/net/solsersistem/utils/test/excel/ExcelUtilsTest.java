package mx.solsersistem.utils.test.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import mx.solsersistem.utils.excel.ExcelUtils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

public class ExcelUtilsTest {
    private static final String USER = "User";
    private static final String ONE = "1";
    private static final String LOW = "LOW";
    private File excelFile;
    
    @Test
    public final void whenCreateExcelFromArrayThenExcelFileIscreated() throws IOException {
        this.excelFile =
                ExcelUtils.createXlsxFromArray("TestFiles/Excel/Generated", "Report", this.createDataMatrix());
        Assert.assertTrue("Error al generar el excel", this.excelFile.exists());
        Assert.assertEquals("Error al validar datos del excel. Celda 0, 0", USER, this.getExcelCellData(0, 0));
        Assert.assertEquals("Error al validar datos del excel. Celda 1, 1", ONE, this.getExcelCellData(1, 1));
        Assert.assertEquals("Error al validar datos del excel. Celda 2, 2", LOW, this.getExcelCellData(2, 2));
        this.excelFile.delete();
    }
    
    private String[][] createDataMatrix() {
        final String[][] datamatrix = new String[][]{{USER, "Number", "Exp"}, {"A", ONE, "HIGH"}, {"B", "2", LOW}};
        return datamatrix;
    }
    
    private String getExcelCellData(final Integer row, final Integer column) throws IOException {
        final FileInputStream excelFileInputStream = new FileInputStream(this.excelFile);
        final XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
        final XSSFSheet sheet = workbook.getSheetAt(0);
        final String cellValue = sheet.getRow(row).getCell(column).getStringCellValue();
        workbook.close();
        return cellValue;
    }
}
