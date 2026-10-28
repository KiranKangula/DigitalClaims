package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

@CucumberOptions(
        features = "src/test/resources/features/UNCD_API.feature",
        tags = "@Token or @UNCD_API",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty"},
        publish = false
)
public class UNCDAPITests extends AbstractTestNGCucumberTests {
    public UNCDAPITests() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        System.setProperty("cucumber.plugin", "html:target/TestResults/UNCD_API/UNCD_APIResults_" + timestamp + ".html");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}