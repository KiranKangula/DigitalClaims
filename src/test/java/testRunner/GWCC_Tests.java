package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeSuite;
import utils.ConfigReader;
import utils.JsonUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@CucumberOptions(
        features = "src/test/resources/features",
        tags = "@ClaimEW_withOSPCData",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty","json:target/cucumberGWCC.json"},
        publish = false
)
public class GWCC_Tests extends AbstractTestNGCucumberTests {
    private static ConfigReader configReader;

    @BeforeSuite
    public void setupCucumberPlugin() {
        // Fetch the environment set by ExtentReportListener or default to SIT
        String environment = System.getProperty("env", "PPD");

        // Initialize ConfigReader at class level so it can be used across tests
        configReader = new ConfigReader("src/test/resources/config/" + environment + "/GWClaimCenter.properties");

        String dateFolder = new SimpleDateFormat("dd_MMM_yyyy").format(new Date());
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        String resultsPath = "target/TestResults/GWCC/" + dateFolder + "/" + environment;

        File folder = new File(resultsPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        System.setProperty("cucumber.plugin", "html:" + resultsPath + "/GWCCResults_" + timestamp + ".html");
        System.setProperty("cucumber.testng.testname", "true");
        // Removed System.setProperty("extent.reporter.spark.out", ...) as ExtentReportListener handles Extent reports.
    }

    @Override
    @DataProvider(parallel = false)
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
