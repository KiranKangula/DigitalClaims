package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties = new Properties();
    //private String propertiesFilePath;

    public ConfigReader(String fileName) {

        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    public void setProperty(String key, String value, String propertiesFilePath) throws Exception {
        properties.setProperty(key, value);
        try (FileOutputStream outputStream = new FileOutputStream(propertiesFilePath)) {
            properties.store(outputStream, null);
        }

    }


}
