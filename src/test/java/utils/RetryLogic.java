package utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import java.io.FileInputStream;
import java.util.Properties;

public class RetryLogic implements IRetryAnalyzer {
    private int retryCount = 0;
    private static int MAX_RETRY_COUNT;

    static {
        try {
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("src/test/resources/config/Common.properties")) {
                props.load(fis);
            }
            MAX_RETRY_COUNT = Integer.parseInt(props.getProperty("test.retry.count", "0"));
            System.out.println("[RETRY LOGIC] Max retry count: " + MAX_RETRY_COUNT);
        } catch (Exception e) {
            MAX_RETRY_COUNT = 0;
        }
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }

    public boolean isLastAttempt() {
        return retryCount >= MAX_RETRY_COUNT;
    }

    public int getAttemptNumber() {
        return retryCount + 1;
    }
}
