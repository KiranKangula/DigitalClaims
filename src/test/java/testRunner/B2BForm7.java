package testRunner;

import definitions.Hooks;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import utils.ConfigReader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//import org.testng.annotations.DataProvider;


@CucumberOptions(
        features = "src/test/resources/features/B2BForm7.feature",
        tags = "@Token or @B2BForm7",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty"},
        publish = true
)
public class B2BForm7 extends AbstractTestNGCucumberTests {
    public B2BForm7() {
        ConfigReader configReader = new ConfigReader("src/test/resources/config/B2BForm7.properties");
        String environment= configReader.getProperty("env");
        // Generate folder name with current date in DD_MMM_YYYY format
        String dateFolder = new SimpleDateFormat("dd_MMM_yyyy").format(new Date());
        // Generate timestamp for unique file names
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        // Define the results path
        String resultsPath = "target/TestResults/B2B/" + dateFolder+ "/" + environment;

        // Create the folder if it doesn't exist
        new File(resultsPath).mkdirs();

        // Set the Cucumber plugin property to save results in the appropriate folder
        System.setProperty("cucumber.plugin", "html:" + resultsPath + "/B2BForm7_" + timestamp + ".html");

    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
