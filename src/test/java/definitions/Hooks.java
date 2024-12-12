package definitions;

import java.util.concurrent.TimeUnit;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;

public class Hooks {
	public static WebDriver driver;
    ConfigReader configReader = new ConfigReader();
    @Before
    public void openBrowser() throws Exception {

      if(configReader.getProperty("browser").contentEquals("chrome")) {
          WebDriverManager.chromedriver().setup();
          ChromeOptions options = new ChromeOptions();
          if(configReader.getProperty("headless").contentEquals("true")) {
              options.addArguments("--headless", "--window-size=1440,768", "--disable-gpu");
          }
              driver = new ChromeDriver(options);
          driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
          driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
          driver.manage().deleteAllCookies();
          driver.manage().window().maximize();
          driver.get(configReader.getProperty("baseUrl"));
      }

    }

    @After
    public void embedScreenshot(Scenario scenario) {

//        if(scenario.isFailed()) {
//            try {
//                byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
//                scenario.embed(screenshot, "image/png");
//            } catch (WebDriverException noSupportScreenshot) {
//                System.err.println(noSupportScreenshot.getMessage());
//            }
//        }
        driver.quit();
    }
}
