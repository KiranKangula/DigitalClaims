package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

//import org.testng.annotations.DataProvider;


@CucumberOptions(
        features = "src/test/resources/features/OSPC.feature",
        tags = "@PrerequisiteTest or @DependentTest",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty"},
        publish = true
)
public class DirectDepositTest extends AbstractTestNGCucumberTests {
    public DirectDepositTest() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        System.setProperty("cucumber.plugin", "html:target/TestResults/DirectDeposit/DirectDepositResults_" + timestamp + ".html");
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
