package utils;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;

public class SuiteListener implements ISuiteListener, ITestListener {

    @Override
    public void onStart(ITestContext context) {
        TestNGContextHolder.setContext(context);
        System.out.println("ExcludedClasses parameter: " + context.getSuite().getParameter("excludedClasses"));
    }

    @Override
    public void onFinish(ITestContext context) {
        // Clear the context after the suite finishes
        TestNGContextHolder.setContext(null);
    }
}