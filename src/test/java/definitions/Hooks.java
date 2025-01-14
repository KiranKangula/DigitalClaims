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
    private static ConfigReader configReader;
    public static List<Map<String, String>> data;
     public static int currentIndex=0;
    public static ThreadLocal<Map<String, String>> currentRow = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
    public static String sheetName;
    private static String scenarioName;
    @Before(order = 0)
    public void setup(Scenario scenario)
    {
        String featureName = scenario.getUri().getPath().split("/")
                [scenario.getUri().getPath().split("/").length-1].replace(".feature", "");
        String propertyFilePath= "src/test/resources/config/"+ featureName+ ".properties";
        configReader = new ConfigReader(propertyFilePath);
        sheetName = scenario.getSourceTagNames()
                .stream()
                .filter(tag -> tag.startsWith("@SheetName_"))
                .map(tag -> tag.replace("@SheetName_", ""))
                .findFirst()
                .orElse(null);
        //scenarioName.set(scenario.getName());
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        scenarioName = scenario.getName();
    }

    public static String getScenarioName() {
        return scenarioName;
    }

    @Before(order = 1)
    public void loadData(Scenario scenario) throws IOException {
        String excelfilepath = "src/test/resources/data/"+ configReader.getProperty("env") + "/testData.xlsx";
        String sheetname = getSheetName(scenario);
        if(sheetname==null || sheetname.isEmpty()){
            scenario.log("Sheet name could not be determined for this scenario");
            return;
        }
        data = ExcelUtils.readExcelData(sheetname);
        data.forEach(row-> System.out.println(row));
        currentIndex = 0; // Reset currentIndex before each test run
    }

    @Before(order = 2)
    public void setScenarioData(Scenario scenario) {
        if (data != null && !data.isEmpty()) {
            if (currentRow == null) {
                currentRow = ThreadLocal.withInitial(() -> null);
            }

            if (currentIndex < data.size()) {
                currentRow.set(data.get(currentIndex));
                scenario.log("Setting the scenario: "+ currentRow.get());
                currentIndex++;
            } else {
                scenario.log("No more rows available in the test data for this scenario.");
            }
        } else {
            scenario.log("Test data is not loaded or is empty.");
        }
    }

    public static String getSheetName(Scenario scenario) {

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

    public static WebDriver getDriver() {
        return driver.get();
    }

    @Before
    public void openBrowser() throws Exception {
        WebDriver webDriver;
        String browser = configReader.getProperty("browser");
        boolean isHeadless = configReader.getProperty("headless").contentEquals("true");

        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-dev-shm-usage", "--no-sandbox");
                if (isHeadless) {
                    chromeOptions.addArguments("--headless", "--window-size=1440,768", "--disable-gpu");
                }
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setExperimentalOption("useAutomationExtension", false);
                edgeOptions.setAcceptInsecureCerts(true);
                edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--disable-dev-shm-usage", "--no-sandbox");
                if (isHeadless) {
                    edgeOptions.addArguments("--headless", "--window-size=1440,768", "--disable-gpu");
                }
                webDriver = new EdgeDriver(edgeOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }

        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(20, TimeUnit.SECONDS);
        webDriver.manage().deleteAllCookies();
        if (!isHeadless) {
            webDriver.manage().window().maximize();
        }
        webDriver.get(configReader.getProperty("baseUrl"));
        driver.set(webDriver);
    }

    @After
    public void closeDriver() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove();
        }
    }

    @AfterStep
    public void takeScreenshot(Scenario scenario) {

                if(scenario.isFailed() || configReader.getProperty("screenshots").contentEquals("true")) {
            try {
                final byte[] screenshot = ((TakesScreenshot)driver.get()).getScreenshotAs(OutputType.BYTES);
                String screenshotname = scenario.getName().replaceAll(" ","_")+".png";

                scenario.attach(screenshot, "image/png", screenshotname);
            } catch (WebDriverException noSupportScreenshot) {
                System.err.println(noSupportScreenshot.getMessage());
            }
        }
    }
}
