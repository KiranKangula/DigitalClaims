package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import utils.*;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Listeners({ExtentReportListener.class, ExtentTestNameListener.class, RetryInvokedListener.class})
@CucumberOptions(
        features = "src/test/resources/features",
        tags = "@eForm6",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty", "json:target/cucumber_DI6.json"},
        publish = false
)
public class DIEform6_Tests extends AbstractTestNGCucumberTests {

    // Remove the constructor, as it runs too early for System.getProperty("env")
    // public DIEform6_Tests() {
    //     // This code runs before ISuiteListener.onStart, so System.getProperty("env") will be null
    // }

    @BeforeSuite
    public void setupCucumberPlugin() {
        // Ensure 'env' is set by ExtentReportListener.onStart(ISuite) before this method runs.
        // Provide a default value if for some reason it's still null, though it shouldn't be.
        String environment = System.getProperty("env", "SIT");

        // Generate folder name with current date in DD_MMM_YYYY format
        String dateFolder = new SimpleDateFormat("dd_MMM_yyyy").format(new Date());
        // Generate timestamp for unique file names, explicitly including AM/PM marker
        String timestamp = new SimpleDateFormat("hhmmss_a").format(new Date()); // Changed HHmmss to hhmmss_a
        // Define the results path
        String resultsPath = "target/TestResults/DIEform6/" + dateFolder + "/" + environment;

        // Create the folder if it doesn't exist
        new File(resultsPath).mkdirs();

        // Set the Cucumber plugin property to save results in the appropriate folder
        System.setProperty("cucumber.plugin", "html:" + resultsPath + "/DIEform6Results_" + timestamp + ".html");
        System.out.println("[CUCUMBER PLUGIN] Set to: " + System.getProperty("cucumber.plugin"));
    }

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        Object[][] scenarios = super.scenarios();
        List<Object[]> multipliedScenarios = new ArrayList<>();

        for (Object[] scenario : scenarios) {
            PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
            List<String> tags = pickleWrapper.getPickle().getTags();

            // Find the sheet name from tags
            String sheetName = tags.stream()
                    .filter(tag -> tag.startsWith("@SheetName_"))
                    .map(tag -> tag.replace("@SheetName_", ""))
                    .findFirst()
                    .orElse(null);

            if (sheetName != null) {
                int rowCount = JsonUtils.getRowCount(sheetName);
                if (rowCount > 0) {
                    // Repeat the scenario for each data row found in JSON
                    for (int i = 0; i < rowCount; i++) {
                        multipliedScenarios.add(scenario);
                    }
                } else {
                    multipliedScenarios.add(scenario);
                }
            } else {
                multipliedScenarios.add(scenario);
            }
        }

        return multipliedScenarios.toArray(new Object[0][0]);
    }
}
