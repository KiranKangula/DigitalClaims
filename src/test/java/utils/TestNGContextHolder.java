package utils;

import org.testng.ITestContext;

public class TestNGContextHolder {
    private static ITestContext context;

    public static void setContext(ITestContext ctx) {
        context = ctx;
    }

    public static ITestContext getContext() {
        return context;
    }
}