package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleWrapper;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.BeforeSuite;
import utils.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Listeners({ExtentReportListener.class, ExtentTestNameListener.class, RetryInvokedListener.class})
@CucumberOptions(
        features = "src/test/resources/features/OSPC.feature",
        tags = "@OSPCUI",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty","json:target/cucumber_OSPCUI.json"},
        publish = true
)
public class OSPCUI_Tests extends AbstractTestNGCucumberTests {
    private static ConfigReader configReader;

    @BeforeSuite
    public void setupCucumberPlugin() {
        // Fetch the environment set by ExtentReportListener or default to SIT
        String environment = System.getProperty("env", "BAT");

        // Initialize ConfigReader at class level so it can be used across tests
        configReader = new ConfigReader("src/test/resources/config/" + environment + "/OSPC.properties");

        String dateFolder = new SimpleDateFormat("dd_MMM_yyyy").format(new Date());
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        String resultsPath = "target/TestResults/OSPCUI/" + dateFolder + "/" + environment;

        File folder = new File(resultsPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Setting this property dynamically ensures Cucumber writes the HTML report to the unique daily folder
        System.setProperty("cucumber.plugin", "html:" + resultsPath + "/OSPCUIResults_" + timestamp + ".html");

        // This is the critical setting to use the Runner class name instead of Feature name
        System.setProperty("cucumber.testng.testname", "true");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        Object[][] scenarios = super.scenarios();
        List<Object[]> multipliedScenarios = new ArrayList<>();

        for (Object[] scenario : scenarios) {
            PickleWrapper pickleWrapper = (PickleWrapper) scenario[0];
            List<String> tags = pickleWrapper.getPickle().getTags();

            String sheetName = tags.stream()
                    .filter(tag -> tag.startsWith("@SheetName_"))
                    .map(tag -> tag.replace("@SheetName_", ""))
                    .findFirst()
                    .orElse(null);

            if (sheetName != null) {
                int rowCount = JsonUtils.getRowCount(sheetName);
                if (rowCount > 0) {
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
