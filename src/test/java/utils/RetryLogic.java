package utils;

import org.testng.ITestResult;

public class RetryLogic {
    private static final int MAX_RETRY_COUNT = 2; // Number of retries

    public static boolean shouldRetry(ITestResult result) {
        int retryCount = getRetryCount(result);
        if (retryCount < MAX_RETRY_COUNT) {
            setRetryCount(result, retryCount + 1);
            System.out.println("Retrying test: " + result.getName() + " | Attempt: " + (retryCount + 1));
            return true;
        }
        return false;
    }

    private static int getRetryCount(ITestResult result) {
        Integer count = (Integer) result.getAttribute("retryCount");
        return (count != null) ? count : 0;
    }

    private static void setRetryCount(ITestResult result, int count) {
        result.setAttribute("retryCount", count);
    }
}
