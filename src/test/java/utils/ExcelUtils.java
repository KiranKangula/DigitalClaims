package utils;

import definitions.Hooks;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class ExcelUtils {
    private static String BASE_PATH = "src/test/resources/data/";
    static ConfigReader prop = Hooks.getConfigReader();

    /**
     * Reads data from Excel and returns it as a list of maps.
     *
     * @param env       Environment (folder name)
     * @param sheetName Sheet name in the Excel file
     * @return List of maps, where each map represents a row with column names as keys
     * @throws IOException If an I/O error occurs
     */
    public static List<Map<String, String>> readExcelData(String sheetName) throws IOException {
        String filePath = BASE_PATH + prop.getProperty("env") + "/testData.xlsx";
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();

            // Read header row to get column names
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            // Read the rest of the rows and map values to headers
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>(); // Corrected to use Map
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = getCellValueAsString(cell);
                    rowData.put(headers.get(i), cellValue); // Use put method to map header to value
                }
                data.add(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }


/**
 * Saves data into Excel by appending a row with dynamic fields and values.
 *
 * @param env       Environment (folder name)
 * @param sheetName Sheet name in the Excel file
 * @param rowData   Data to save as a map of field names and values
 * @throws IOException If an I/O error occurs
 */
public static void saveExcelData(String sheetName, Map<String, String> rowData) throws IOException {
    String filePath = BASE_PATH + prop.getProperty("env") + "/testData.xlsx";
    Workbook workbook;
    Sheet sheet;

    // Check if the file exists; if not, create a new workbook and sheet
    File file = new File(filePath);
    if (file.exists()) {
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName); // Create sheet if missing
        }
    } else {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }

    // Initialize headers
    Row headerRow = sheet.getRow(0);
    if (headerRow == null) {
        headerRow = sheet.createRow(0); // Create header row
        int headerIndex = 0;
        for (String key : rowData.keySet()) {
            Cell headerCell = headerRow.createCell(headerIndex++);
            headerCell.setCellValue(key); // Add headers
        }
    }

    // Ensure headers match rowData keys
    List<String> headers = new ArrayList<>();
    for (Cell cell : headerRow) {
        headers.add(cell.getStringCellValue());
    }
    for (String key : rowData.keySet()) {
        if (!headers.contains(key)) {
            Cell newHeaderCell = headerRow.createCell(headers.size());
            newHeaderCell.setCellValue(key);
            headers.add(key);
        }
    }

    // Add new row at the end
    int lastRowNum = sheet.getLastRowNum() + 1;
    Row newRow = sheet.createRow(lastRowNum);

    // Write data into cells based on headers
    for (int i = 0; i < headers.size(); i++) {
        String header = headers.get(i);
        String value = rowData.getOrDefault(header, "");
        Cell cell = newRow.createCell(i);
        cell.setCellValue(value);
    }

    // Save changes
    FileOutputStream fos = new FileOutputStream(filePath);
    workbook.write(fos);
    workbook.close();
}

}

/// Reading data Example

//String env = "QA";
//String sheetName = "TestData";
//List<Map<String, String>> data = ExcelUtils.readExcelData(env, sheetName);
//
//for (Map<String, String> row : data) {
//        System.out.println("Username: " + row.get("Username"));
//        System.out.println("Password: " + row.get("Password"));
//        }