package testRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import org.junit.runner.RunWith;
import org.testng.annotations.DataProvider;

//@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "definitions",
        monochrome = true,
        plugin= {"pretty", "html:target/cucumber-reports.html"},
        publish = true
)

public class TestRunner   extends AbstractTestNGCucumberTests{
//    @Override
//    @DataProvider(parallel = true)
//    public Object[][] scenarios(){
//    return super.scenarios();
//}

}
