package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

//import org.testng.annotations.DataProvider;


@CucumberOptions(
        features = "src/test/resources/features/OSPC.feature",
        tags = "@eForm6_PI or @eForm6_illness or @eForm6_Mental or @eForm6_NIHL",
        glue = "definitions",
        monochrome = true,
        plugin = {"pretty", "html:target/TestResults/eForm6_Results.html"},
        publish = true
)
public class DIEform6_Tests extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }

}
