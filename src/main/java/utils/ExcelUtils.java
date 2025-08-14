package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtils {

    /**
     * Reads test data from an Excel sheet and returns it as a 2D Object array.
     *
     * @param filePath  Full path to the Excel file
     * @param sheetName Name of the sheet to read data from
     * @return 2D Object array containing the test data (rows x columns)
     */
    public static Object[][] getTestData(String filePath, String sheetName) {
        Object[][] data = null;

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) { // Open Excel workbook

            Sheet sheet = workbook.getSheet(sheetName); // Get sheet by name
            int rowCount = sheet.getPhysicalNumberOfRows(); // Total number of rows
            int colCount = sheet.getRow(0).getLastCellNum(); // Number of columns based on first row

            // Initialize array excluding header row
            data = new Object[rowCount - 1][colCount];

            // Loop through all rows (skip header)
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    // Read cell value as string and store in array
                    data[i - 1][j] = row.getCell(j).toString();
                }
            }

        } catch (IOException e) {
            // Print stack trace if file reading fails
            e.printStackTrace();
        }

        return data; // Return populated 2D array
    }
}
