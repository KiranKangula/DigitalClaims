package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

@CucumberOptions(
        features = "src/test/resources/features/OSBC.feature",
        tags = "@OSBC",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty"},
        publish = true
)
public class OSBCTests extends AbstractTestNGCucumberTests {
    public OSBCTests() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        System.setProperty("cucumber.plugin", "html:target/TestResults/OSBC/OSBCResults_" + timestamp + ".html");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}