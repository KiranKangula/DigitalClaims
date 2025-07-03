package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Map;


public class ExtentReportListener implements ITestListener {
    private static ExtentReports extent;
    //private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
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
        String scenarioName = Hooks.getScenarioName();
        String testName = scenarioName != null ? scenarioName : result.getMethod().getMethodName();

        if (!testMap.containsKey(testName)) {
            ExtentTest extentTest = extent.createTest(testName);
            if (scenarioName != null) {
                extentTest.getModel().setDescription("Scenario: " + scenarioName);
            }
            testMap.put(testName, extentTest);
            Hooks.extentTest.set(extentTest);
        } else {
            Hooks.extentTest.set(testMap.get(testName));
        }
    }


    @Override
    public void onTestSuccess(ITestResult result) {
//        String scenarioName = Hooks.getScenarioName();
//        if (scenarioName != null) {
//            Hooks.extentTest.get().getModel().setName(scenarioName);
//            Hooks.extentTest.get().getModel().setDescription("Scenario: " + scenarioName);
//        }
        Hooks.extentTest.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String scenarioName = Hooks.getScenarioName();
        if (scenarioName != null) {
            Hooks.extentTest.get().getModel().setName(scenarioName);
            Hooks.extentTest.get().getModel().setDescription("Scenario: " + scenarioName);
        }
        Hooks.extentTest.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String scenarioName = Hooks.getScenarioName();
        if (scenarioName != null) {
            Hooks.extentTest.get().getModel().setName(scenarioName);
            Hooks.extentTest.get().getModel().setDescription("Scenario: " + scenarioName);
        }
        Hooks.extentTest.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    public static ExtentReports getExtent() {
        return extent;
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver webDriver) {
        driver.set(webDriver);
    }
}