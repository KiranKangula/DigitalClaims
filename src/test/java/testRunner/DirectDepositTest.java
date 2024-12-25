package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

//import org.testng.annotations.DataProvider;


@CucumberOptions(
        features = "src/test/resources/features/OSPC.feature",
        tags = "@PrerequisiteTest or @DependentTest",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty", "html:target/TestResults/DirectDepositResults.html"},
        publish = true
)
public class DirectDepositTest extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
