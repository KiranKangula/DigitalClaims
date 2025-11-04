package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportListener implements ITestListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
    private static Map<String, ExtentTest> testMap = new HashMap<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("test-output/ExtentReport.html");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setDocumentTitle("Test Report");
        htmlReporter.config().setReportName("Test Report");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestContext().getName();
        ExtentTest parent;

        // Use a map to track the top-level TestNG <test> name from testng.xml
        if (testMap.containsKey(testName)) {
            parent = testMap.get(testName);
        } else {
            parent = extent.createTest(testName);
            testMap.put(testName, parent);
        }
        parentTest.set(parent);

        // Get the scenario name from the Hooks class
        String scenarioName = Hooks.getScenarioName();
        if (scenarioName != null) {
            // Create a child node for the specific Cucumber scenario
            ExtentTest childTest = parentTest.get().createNode(scenarioName);
            Hooks.extentTest.set(childTest);
        } else {
            // Fallback for non-Cucumber tests (e.g., if you run TestNG tests directly)
            ExtentTest childTest = parentTest.get().createNode(result.getMethod().getMethodName());
            Hooks.extentTest.set(childTest);
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest currentTest = Hooks.extentTest.get();
        if (currentTest != null) {
            currentTest.pass("PASSED");
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest currentTest = Hooks.extentTest.get();
        if (currentTest != null) {
            WebDriver driver = Hooks.getDriver();
            if (driver != null) {
                try {
                    String screenshotName = Hooks.getScenarioName() != null ? Hooks.getScenarioName() : result.getMethod().getMethodName();
                    String screenshotPath = ScreenshotUtil.getScreenshot(driver, screenshotName);
                    currentTest.fail("FAILED:" + "\n" + result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
                } catch (IOException e) {
                    currentTest.fail("FAILED:" + "\n" + result.getThrowable());
                    currentTest.warning("Failed to attach screenshot: " + e.getMessage());
                }
            } else {
                currentTest.fail("FAILED:" + "\n" + result.getThrowable());
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest currentTest = Hooks.extentTest.get();
        if (currentTest != null) {
            currentTest.skip("Test skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
