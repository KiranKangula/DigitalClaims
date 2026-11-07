package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import utils.ExtentReportListener;
import utils.ExtentTestNameListener;

import java.text.SimpleDateFormat;
import java.util.Date;


@Listeners({ExtentReportListener.class, ExtentTestNameListener.class})
@CucumberOptions(
        features = "src/test/resources/features/OSPC.feature",
        tags = "@@WSIBFormMulti",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty","json:target/cucumber.json"},
        publish = false
)

public class SanityTests extends AbstractTestNGCucumberTests {
    public SanityTests() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        System.setProperty("cucumber.plugin", "html:target/TestResults/Sanity/SanityResults_" + timestamp + ".html");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
