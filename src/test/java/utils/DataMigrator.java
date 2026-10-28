package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DataMigrator {
    private static final String BASE_PATH = "src/test/resources/data/";
    private static final String[] ENVS = {"SIT", "BAT", "PPD"};

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        for (String env : ENVS) {
            File excelFile = new File(BASE_PATH + env + "/testData.xlsx");
            if (!excelFile.exists()) continue;

            System.out.println("Migrating data for environment: " + env);
            try (FileInputStream fis = new FileInputStream(excelFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    String sheetName = sheet.getSheetName();
                    List<Map<String, String>> data = new ArrayList<>();

                    Row headerRow = sheet.getRow(0);
                    if (headerRow == null) continue;

                    List<String> headers = new ArrayList<>();
                    for (Cell cell : headerRow) headers.add(cell.getStringCellValue());

                    for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                        Row row = sheet.getRow(j);
                        if (row == null) continue;
                        Map<String, String> rowData = new LinkedHashMap<>();
                        for (int k = 0; k < headers.size(); k++) {
                            Cell cell = row.getCell(k, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            rowData.put(headers.get(k), cell.toString());
                        }
                        data.add(rowData);
                    }

                    File jsonFile = new File(BASE_PATH + env + "/" + sheetName + ".json");
                    mapper.writeValue(jsonFile, data);
                    System.out.println("  - Created: " + jsonFile.getName());
                }
            }
        }
        System.out.println("Migration Complete!");
    }
}