package utils;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ITestNGMethod;
import java.util.Arrays;
import java.util.List;

public class TestListener implements ITestListener {

    private static final List<String> EXCLUDED_CLASSES = Arrays.asList(
            "testRunner.OSPCTests",
            "testRunner.UNCD_Tests",
            "testRunner.WT_Tests"
    );

    private static final int MAX_RETRY_COUNT = 2; // Number of retries

    @Override
    public void onTestFailure(ITestResult result) {
        String className = result.getTestClass().getRealClass().getName();
        System.out.println("Test failed: " + result.getName() + " in class: " + className);

        // Only consider retrying if the class is not in the excluded list
        if (!EXCLUDED_CLASSES.contains(className)) {
            if (shouldRetry(result)) {
                System.out.println("Retry attempt " + (getRetryCount(result)) + " for " + result.getName());
                result.getTestContext().getFailedTests().addResult(result);
            } else {
                System.out.println("Maximum retry count reached for " + result.getName() + ". No more retries.");
            }
        } else {
            System.out.println("Excluding test " + result.getName() + " from retries based on class " + className);
        }
    }

    // Helper methods for retry logic, moved here to fix private access error
    private boolean shouldRetry(ITestResult result) {
        int retryCount = getRetryCount(result);
        if (retryCount < MAX_RETRY_COUNT) {
            setRetryCount(result, retryCount + 1);
            return true;
        }
        return false;
    }

    private int getRetryCount(ITestResult result) {
        Integer count = (Integer) result.getAttribute("retryCount");
        return (count != null) ? count : 0;
    }

    private void setRetryCount(ITestResult result, int count) {
        result.setAttribute("retryCount", count);
    }

    // Other ITestListener methods (onTestStart, onTestSuccess, etc.)
    @Override
    public void onStart(ITestContext context) {}
    @Override
    public void onTestStart(ITestResult result) {}
    @Override
    public void onTestSuccess(ITestResult result) {}
    @Override
    public void onTestSkipped(ITestResult result) {}
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}
    @Override
    public void onFinish(ITestContext context) {}
}
