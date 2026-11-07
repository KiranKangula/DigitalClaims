package utils;

import definitions.Hooks;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ExcelUtils {
    private static String BASE_PATH = "src/test/resources/data/";
    private static final Lock fileLock = new ReentrantLock();

    public static void backupExcelFile(String sheetName) throws IOException {
        String filePath = BASE_PATH + System.getProperty("env") + "/testData.xlsx";
        String backupFilePath = BASE_PATH + System.getProperty("env") + "/backup_testData.xlsx";
        File sourceFile = new File(filePath);
        File backupFile = new File(backupFilePath);

        fileLock.lock();
        try {
            Files.copy(sourceFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            fileLock.unlock();
        }
    }

    /**
     * Reads data from Excel and returns it as a list of maps.
     *
     * @param sheetName Sheet name in the Excel file
     * @return List of maps, where each map represents a row with column names as keys
     * @throws IOException If an I/O error occurs
     */
    public static List<Map<String, String>> readExcelData(String sheetName) throws IOException {
        backupExcelFile(sheetName);
        String filePath = BASE_PATH + System.getProperty("env") + "/testData.xlsx";
        List<Map<String, String>> data = new ArrayList<>();


        fileLock.lock();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            Iterator<Row> rowIterator = sheet.iterator();

            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>();
                
                // Store physical row index to ensure updates happen on the correct row
                rowData.put("excel_row_num", String.valueOf(row.getRowNum()));
                
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = getCellValueAsString(cell);
                    rowData.put(headers.get(i), cellValue);
                }
                data.add(rowData);
            }
        } finally {
            fileLock.unlock();
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
                    return String.format("%.0f", cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getRichStringCellValue().getString();
                    case NUMERIC:
                        return String.format("%.0f", cell.getNumericCellValue());
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    default:
                        return "";
                }
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }

    /**
     * Saves data into Excel by appending a row with dynamic fields and values.
     *
     * @param sheetName Sheet name in the Excel file
     * @param rowData   Data to save as a map of field names and values
     * @throws IOException If an I/O error occurs
     */
    public static void saveExcelData(String sheetName, Map<String, String> rowData) throws IOException {
        backupExcelFile(sheetName);
        String filePath = BASE_PATH + System.getProperty("env") + "/testData.xlsx";
        Workbook workbook;
        Sheet sheet;
        fileLock.lock();
        try {
            File file = new File(filePath);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(filePath)) {
                    workbook = new XSSFWorkbook(fis);
                    sheet = workbook.getSheet(sheetName);
                    if (sheet == null) {
                        sheet = workbook.createSheet(sheetName);
                    }
                }
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet(sheetName);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                headerRow = sheet.createRow(0);
                int headerIndex = 0;
                for (String key : rowData.keySet()) {
                    if(key.equals("excel_row_num")) continue;
                    Cell headerCell = headerRow.createCell(headerIndex++);
                    headerCell.setCellValue(key);
                }
            }

            List<String> headers = new ArrayList<>();
            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue());
            }
            for (String key : rowData.keySet()) {
                if(key.equals("excel_row_num")) continue;
                if (!headers.contains(key)) {
                    Cell newHeaderCell = headerRow.createCell(headers.size());
                    newHeaderCell.setCellValue(key);
                    headers.add(key);
                }
            }

            // Determine which row to write to
            int currentRowIndex = -1;
            if (rowData.containsKey("excel_row_num")) {
                currentRowIndex = Integer.parseInt(rowData.get("excel_row_num"));
            }

            Row row;
            if (currentRowIndex != -1) {
                row = sheet.getRow(currentRowIndex);
                if (row == null) row = sheet.createRow(currentRowIndex);
            } else {
                int lastRowNum = sheet.getLastRowNum() + 1;
                row = sheet.createRow(lastRowNum);
            }

            for (int i = 0; i < headers.size(); i++) {
                String header = headers.get(i);
                String value = rowData.getOrDefault(header, "");
                Cell cell = row.createCell(i);
                cell.setCellValue(value);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } finally {
            fileLock.unlock();
        }
    }


    public static void updateExcelData(String sheetName, Map<String, String> updatedRowData) throws IOException {
        String filePath = BASE_PATH + System.getProperty("env") + "/testData.xlsx";
        
        fileLock.lock();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);

            // Directly use the physical row index stored during read
            int currentRowIndex = -1;
            if (updatedRowData.containsKey("excel_row_num")) {
                currentRowIndex = Integer.parseInt(updatedRowData.get("excel_row_num"));
            }

            if (currentRowIndex != -1) {
                Row row = sheet.getRow(currentRowIndex);
                if (row == null) row = sheet.createRow(currentRowIndex);
                
                for (Map.Entry<String, String> entry : updatedRowData.entrySet()) {
                    if (entry.getKey().equals("excel_row_num")) continue;
                    
                    int columnIndex = getColumnIndex(sheet, entry.getKey());
                    if (columnIndex == -1) {
                        // Add new column if it doesn't exist
                        Row headerRow = sheet.getRow(0);
                        columnIndex = headerRow.getLastCellNum();
                        headerRow.createCell(columnIndex).setCellValue(entry.getKey());
                    }
                    Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    cell.setCellValue(entry.getValue());
                }
                
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }
            }
        } finally {
            fileLock.unlock();
        }
    }

    private static int getColumnIndex(Sheet sheet, String columnName) {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                if (cell.getStringCellValue().equals(columnName)) {
                    return cell.getColumnIndex();
                }
            }
        }
        return -1; // Column not found
    }

    public static int getRowCount(String sheetName) {
        String filePath = BASE_PATH + System.getProperty("env") + "/testData.xlsx";
        File file = new File(filePath);
        if (!file.exists()) return 0;

        fileLock.lock();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            return (sheet == null) ? 0 : sheet.getLastRowNum();
        } catch (Exception e) {
            return 0;
        } finally {
            fileLock.unlock();
        }
    }
}
