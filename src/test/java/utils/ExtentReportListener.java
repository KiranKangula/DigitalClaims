package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import definitions.Hooks;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentReportListener implements ITestListener {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

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
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        String scenarioName = Hooks.getScenarioName();
        if (scenarioName != null) {
            test.get().getModel().setName(scenarioName);
            test.get().getModel().setDescription("Scenario: " + scenarioName);
        }
        test.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String scenarioName = Hooks.getScenarioName();
        if (scenarioName != null) {
            test.get().getModel().setName(scenarioName);
            test.get().getModel().setDescription("Scenario: " + scenarioName);
        }
        test.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String scenarioName = Hooks.getScenarioName();
        if (scenarioName != null) {
            test.get().getModel().setName(scenarioName);
            test.get().getModel().setDescription("Scenario: " + scenarioName);
        }
        test.get().skip("Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}