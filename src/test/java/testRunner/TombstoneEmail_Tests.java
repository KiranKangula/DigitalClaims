package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeSuite;
import utils.ConfigReader;
import utils.ExtentReportListener;
import utils.ExtentTestNameListener;
import utils.RetryInvokedListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Listeners({ExtentReportListener.class, ExtentTestNameListener.class, RetryInvokedListener.class})
@CucumberOptions(
        features = "src/test/resources/features/OSPC.feature",
        tags = "@Mailingaddress",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty","json:target/cucumber_TombstoneMailingAddr.json"},
        publish = false
)
public class TombstoneEmail_Tests extends AbstractTestNGCucumberTests {
    private static ConfigReader configReader;

    @BeforeSuite
    public void setupCucumberPlugin() {
        // Fetch the environment set by ExtentReportListener or default to SIT
        String environment = System.getProperty("env", "BAT");

        // Initialize ConfigReader at class level so it can be used across tests
        configReader = new ConfigReader("src/test/resources/config/" + environment + "/OSPC.properties");

        String dateFolder = new SimpleDateFormat("dd_MMM_yyyy").format(new Date());
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        String resultsPath = "target/TestResults/Tombstone/" + dateFolder + "/" + environment;

        File folder = new File(resultsPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        System.setProperty("cucumber.plugin", "html:" + resultsPath + "/TombStoneResultsMailingAddress_" + timestamp + ".html");
        System.setProperty("cucumber.testng.testname", "true");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}