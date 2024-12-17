package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
//import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/GWClaimCenter.feature",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty", "html:target/TestResults/cucumber-reports.html"},
        publish = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
//    @Override
//    @DataProvider(parallel = true)
//    public Object[][] scenarios() {
//        return super.scenarios();
//    }
}
