package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

//import org.testng.annotations.DataProvider;


@CucumberOptions(
        features = "src/test/resources/features",
        tags = "@WT",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty", "html:target/TestResults/WT_Results.html"},
        publish = true
)
public class WT_Tests extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
