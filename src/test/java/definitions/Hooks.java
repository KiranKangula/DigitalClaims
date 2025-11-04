package definitions;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.openqa.selenium.remote.CapabilityType;

import com.aventstack.extentreports.ExtentTest;
import io.cucumber.java.*;
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
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.ConfigReader;
import utils.ExcelUtils;
import utils.ExtentReportListener;


public class Hooks {
    private static ConfigReader configReader;
    private static ConfigReader commonPropReader;
    public static List<Map<String, String>> data;
    //private static ThreadLocal<List<Map<String, String>>> data = ThreadLocal.withInitial(() -> null);
     public static int currentIndex=0;
    public static ThreadLocal<Map<String, String>> currentRow = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
    public static String sheetName;
    private static ThreadLocal<String> scenarioName = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    public static ThreadLocal<Scenario> scenario = new ThreadLocal<>();
    @Before(order = 0)
    public void setup(Scenario scenario)
    {
       Hooks.scenario.set(scenario);
        String featureName = scenario.getUri().getPath().split("/")
                [scenario.getUri().getPath().split("/").length-1].replace(".feature", "");
        String propertyFilePath= "src/test/resources/config/"+ featureName+ ".properties";
        configReader = new ConfigReader(propertyFilePath);
        String commonPropReaderPath= "src/test/resources/config/Common.properties";
        commonPropReader = new ConfigReader(commonPropReaderPath);
        sheetName = scenario.getSourceTagNames()
                .stream()
                .filter(tag -> tag.startsWith("@SheetName_"))
                .map(tag -> tag.replace("@SheetName_", ""))
                .findFirst()
                .orElse(null);
        //scenarioName.set(scenario.getName());
    }

    public static String getScenarioName() {
        return scenarioName.get();
    }
    @Before(order = 1)
    public void loadData(Scenario scenario) throws IOException {
        String excelfilepath = "src/test/resources/data/"+ configReader.getProperty("env") + "/testData.xlsx";
        String sheetname = getSheetName(scenario);
        if(sheetname==null || sheetname.isEmpty()){
            scenario.log("Sheet name could not be determined for this scenario");
            return;
        }
        //List<Map<String, String>> testData = ExcelUtils.readExcelData(sheetname);
        data = ExcelUtils.readExcelData(sheetname);

        data.forEach(row-> System.out.println(row));
        //data.get().forEach(row -> System.out.println(row));
        //data.set(testData);
        currentIndex = 0; // Reset currentIndex before each test run
    }

    @Before(order = 2)
    public void setScenarioData(Scenario scenario) {
        //List<Map<String, String>> testData = data.get();
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
           // currentRow.set(data.get(currentIndex));
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
        if (configReader == null) {
            throw new IllegalStateException("ConfigReader is not initialized. Ensure the @Before method in Hooks is executed before accessing it.");
        }
        return configReader;
    }
    public static ConfigReader getCommonReader(){
        return commonPropReader;
    }

    public static WebDriver getDriver() {
        return driver.get();
    }
    @Before
    public void openBrowser(Scenario scenario) throws Exception {

        // Skip browser setup for scenarios tagged with @API
        if (scenario.getSourceTagNames().contains("@API")) {
            scenario.log("Skipping browser setup for @API tests.");
            return;
        }
            WebDriver webDriver;
            String browser = configReader.getProperty("browser");
            boolean isHeadless = commonPropReader.getProperty("headless").contentEquals("true");

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    LoggingPreferences logPrefs = new LoggingPreferences();
                    logPrefs.enable(LogType.BROWSER, Level.ALL);
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
                    chromeOptions.setExperimentalOption("useAutomationExtension", false);
                    chromeOptions.setAcceptInsecureCerts(true);
                    chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--disable-dev-shm-usage", "--no-sandbox");
                    if (isHeadless) {
                        chromeOptions.addArguments("--headless");
                        chromeOptions.addArguments("--window-size=1440,768");//, "--disable-gpu", "--window-size=1440,768"
                    }
                    webDriver = new ChromeDriver(chromeOptions);
                    break;
                case "edge":
                    //WebDriverManager.edgedriver().setup();
                    System.setProperty("webdriver.edge.driver", "C:\\Users\\kangulak\\msedgedriver.exe");
                    //WebDriver driver = new EdgeDriver();
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

                if(scenario.isFailed() || commonPropReader.getProperty("screenshots").contentEquals("true")) {
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
