package definitions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.an.E;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import utils.ConfigReader;
import utils.ExcelUtils;


public class Hooks {
	public static WebDriver driver;
    private static ConfigReader configReader;
    public static List<Map<String, String>> data;
    public static ThreadLocal<Map<String, String>> currentRow;
    public static int currentIndex=0;
    @Before
    public void setup(Scenario scenario)
    {
        String featureName = scenario.getUri().getPath().split("/")
                [scenario.getUri().getPath().split("/").length-1].replace(".feature", "");
        String propertyFilePath= "src/test/resources/config/"+ featureName+ ".properties";
        configReader = new ConfigReader(propertyFilePath);
    }

    @Before
    public void loadData(Scenario scenario) throws IOException {
        String excelfilepath = "src/test/resources/data/"+ configReader.getProperty("env") + "/testData.xlsx";
        String sheetname = getSheetName(scenario);
        if(sheetname==null || sheetname.isEmpty()){
            scenario.log("Sheet name could not be determined for this scenario");
            return;
        }
        data = ExcelUtils.readExcelData(sheetname);
        //data.forEach(row-> scenario.log(String.join(", ", row)));
    }

    @Before
    public void setScenarioData(Scenario scenario){
        if(data != null && !data.isEmpty()){
            if(currentIndex<data.size())
            {
                currentRow.set(data.get(currentIndex));
                currentIndex++;
            }

        }
    }

    private String getSheetName(Scenario scenario) {

        return scenario.getSourceTagNames()
                .stream()
                .filter(tag-> tag.startsWith("@SheetName_"))
                .map(tag-> tag.replace("@SheetName_", ""))
                .findFirst()
                .orElse(null);
    }

    public static ConfigReader getConfigReader(){
        return configReader;
    }

    @Before
    public void openBrowser() throws Exception {

        if(configReader.getProperty("browser").contentEquals("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();

            // Configure ChromeOptions to bypass 403 errors and improve compatibility
            options.setExperimentalOption("useAutomationExtension", Optional.of(false));
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--remote-allow-origins=*");
            // Enable headless mode only when required
            if(configReader.getProperty("headless").contentEquals("true")) {
                options.addArguments("--headless", "--window-size=1440,768", "--disable-gpu");
            }

            driver = new ChromeDriver(options);

            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
            driver.manage().deleteAllCookies();

            // Maximize window if not headless
            if (!configReader.getProperty("headless").contentEquals("true")) {
                driver.manage().window().maximize();
            }

            driver.get(configReader.getProperty("baseUrl"));
        }
        if(configReader.getProperty("browser").contentEquals("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.setExperimentalOption("useAutomationExtension", Optional.of(false));
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            options.addArguments("--remote-allow-origins=*");

            if(configReader.getProperty("headless").contentEquals("true")) {
                options.addArguments("--headless", "--window-size=1440,768", "--disable-gpu");
            }
            driver = new EdgeDriver(options);
            driver.get(configReader.getProperty("baseUrl"));
        }

    }

    @After
    public void closeDriver() {
        driver.quit();
    }

    @AfterStep
    public void takeScreenshot(Scenario scenario) {

                if(scenario.isFailed() || configReader.getProperty("screenshots").contentEquals("true")) {
            try {
                final byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
                String screenshotname = scenario.getName().replaceAll(" ","_")+".png";

                scenario.attach(screenshot, "image/png", screenshotname);
            } catch (WebDriverException noSupportScreenshot) {
                System.err.println(noSupportScreenshot.getMessage());
            }
        }
    }
}
