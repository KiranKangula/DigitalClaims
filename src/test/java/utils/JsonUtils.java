package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import definitions.Hooks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final String BASE_PATH = "src/test/resources/data/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Ensure that writing doesn't force alphabetical order
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, false);
    }

    /**
     * Reads data from a JSON file and returns it as a list of maps.
     * Uses LinkedHashMap to preserve the order of keys as they appear in the file.
     */
    public static List<Map<String, String>> readJsonData(String fileName) {
        String env = System.getProperty("env", "SIT2"); 
        String filePath = BASE_PATH + env + "/" + fileName + ".json";
        File jsonFile = new File(filePath);

        if (!jsonFile.exists()) {
            Hooks.log("ERROR: JSON file not found at " + filePath);
            return Collections.emptyList();
        }

        try {
            // Read as LinkedHashMap values to preserve order
            List<Map<String, Object>> rawData = objectMapper.readValue(jsonFile, new TypeReference<List<Map<String, Object>>>(){});
            List<Map<String, String>> processedData = new ArrayList<>();

            for (Map<String, Object> row : rawData) {
                Map<String, String> processedRow = new LinkedHashMap<>(); // Preserve order here
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    if (entry.getValue() == null) {
                        processedRow.put(entry.getKey(), null);
                    } else if (entry.getValue() instanceof String) {
                        processedRow.put(entry.getKey(), (String) entry.getValue());
                    } else {
                        processedRow.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
                    }
                }
                processedData.add(processedRow);
            }
            return processedData;
        } catch (IOException e) {
            Hooks.log("ERROR: Reading JSON data failed for " + fileName + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public static void writeJsonData(String fileName, List<Map<String, String>> data) {
        String env = System.getProperty("env", "SIT2");
        String filePath = BASE_PATH + env + "/" + fileName + ".json";
        File jsonFile = new File(filePath);

        try {
            // We write the list of maps (which are LinkedHashMaps) to preserve the order
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, data);
            Hooks.log("SUCCESS: Data written to " + filePath);
        } catch (IOException e) {
            Hooks.log("ERROR: Writing JSON data failed: " + e.getMessage());
        }
    }

    public static int getRowCount(String fileName) {
        return readJsonData(fileName).size();
    }
}
