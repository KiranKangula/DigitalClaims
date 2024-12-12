package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;
    private final String propertyFilePath = "src/test/resources/ospcconfig.properties"; // Adjust path accordingly

    public ConfigReader() {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(propertyFilePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
