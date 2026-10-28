package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ExternalValidation {

    // Loaded once per JVM, including a new IntelliJ rerun.
    private static final boolean ENABLED = loadEnabledFlag();

    private ExternalValidation() {
    }

    public static boolean shouldRun(String stepName) {
        if (!ENABLED) {
            System.out.println(
                    "[EXTERNAL VALIDATION BYPASSED] " + stepName
                            + " | externalValidation=false"
            );
        }
        return ENABLED;
    }

    private static boolean loadEnabledFlag() {
        Properties properties = new Properties();

        // Reads src/test/resources/common.properties from the classpath.
        try (InputStream input =
                     ExternalValidation.class.getClassLoader()
                             .getResourceAsStream("config/Common.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "config/Common.properties was not found on the test classpath"
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to read config/Common.properties", e
            );
        }

        String value = properties.getProperty("externalValidation");

        if (value == null) {
            throw new IllegalStateException(
                    "Missing externalValidation in config/Common.properties"
            );
        }

        value = value.trim();

        if (!"true".equalsIgnoreCase(value)
                && !"false".equalsIgnoreCase(value)) {
            throw new IllegalStateException(
                    "externalValidation must be true or false; found: " + value
            );
        }

        return Boolean.parseBoolean(value);
    }
}