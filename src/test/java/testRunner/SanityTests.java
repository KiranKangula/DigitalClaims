package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;


@CucumberOptions(
        features = "src/test/resources/features",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty", "html:target/TestResults/Sanity_Results.html"}
)

@Listeners(utils.TestListener.class)
public class SanityTests extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
