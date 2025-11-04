package utils;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.ITestNGMethod;
import java.util.Arrays;
import java.util.List;

public class RetryInvokedListener implements IInvokedMethodListener {

    private static final List<String> EXCLUDED_CLASSES = Arrays.asList(
            "testRunner.OSPCTests",
            "testRunner.UNCD_Tests",
            "testRunner.WT_Tests"
    );

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        // This logic only applies to failed test methods
        if (result.getStatus() == ITestResult.FAILURE) {
            ITestNGMethod testNGMethod = result.getMethod();
            String className = testNGMethod.getRealClass().getName();

            System.out.println("Checking for retry in class: " + className);

            if (!EXCLUDED_CLASSES.contains(className)) {
                if (RetryLogic.shouldRetry(result)) {
                    // Re-add the failed test to the list of tests to run
                    result.getTestContext().getFailedTests().addResult(result);
                    // Mark the current result as skipped so it doesn't appear as a hard failure yet
                    result.setStatus(ITestResult.SKIP);
                }
            } else {
                System.out.println("Excluding " + className + " from retries.");
            }
        }
    }

    // The beforeInvocation method is not needed for this logic, but must be implemented
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        // Not used
    }
}
