package utils;

import com.aventstack.extentreports.service.ExtentService;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestNameListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        // Force the adapter to use TestNG Test names
        System.setProperty("cucumber.testng.testname", "true");
        // Disable feature-based hierarchy to keep it flat or based on TestNG
        System.setProperty("cucumber.report.hierarchy", "false");
    }

    @Override
    public void onTestStart(ITestResult result) {
        // This ensures that the Test Name in Extent reflects the simple name of the Runner class
        String className = result.getTestClass().getRealClass().getSimpleName();
        ExtentService.getInstance().setSystemInfo("Current Test Runner", className);
    }
}
