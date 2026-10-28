package definitions;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.openqa.selenium.*;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import io.cucumber.java.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.ITestResult;
import org.testng.Reporter;
import utils.ConfigReader;
import utils.JsonUtils;
import utils.NetworkDiagnostics;
import utils.RetryLogic;

public class Hooks {
    private static ThreadLocal<ConfigReader> configReader = new ThreadLocal<>();
    private static ThreadLocal<ConfigReader> commonPropReader = new ThreadLocal<>();
    public static ThreadLocal<List<Map<String, String>>> data = new ThreadLocal<>();
    public static ThreadLocal<Integer> currentIndex = ThreadLocal.withInitial(() -> 0);
    private static ThreadLocal<String> lastScenarioId = ThreadLocal.withInitial(() -> "");
    public static ThreadLocal<Map<String, String>> currentRow = ThreadLocal.withInitial(() -> null);
    private static ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(() -> null);
    public static ThreadLocal<String> sheetName = new ThreadLocal<>();
    private static ThreadLocal<String> scenarioName = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    public static ThreadLocal<Scenario> scenario = new ThreadLocal<>();
    public static ThreadLocal<String> scenarioType = new ThreadLocal<>();
    private static ConcurrentHashMap<String, AtomicInteger> scenarioCounter = new ConcurrentHashMap<>();

    // Cucumber creates a Hooks instance per scenario. Capture state is thread-local.
    private String lastNetworkSummary = "";
    private boolean uiCheckWarningLogged;

    @Before(order = 0)
    public void setup(Scenario scenario) {
        Hooks.scenario.set(scenario);
        lastNetworkSummary = "";
        uiCheckWarningLogged = false;
        NetworkDiagnostics.stop();
        String featureName = scenario.getUri().getPath().split("/")
                [scenario.getUri().getPath().split("/").length - 1].replace(".feature", "");
        String env = System.getProperty("env");
        if (env == null) {
            env = "SIT2";
            System.setProperty("env", "SIT2");
        }
        String propertyFilePath = "src/test/resources/config/" + env + "/" + featureName + ".properties";
        configReader.set(new ConfigReader(propertyFilePath));
        commonPropReader.set(new ConfigReader("src/test/resources/config/Common.properties"));
        String extractedSheetName = scenario.getSourceTagNames().stream()
                .filter(tag -> tag.startsWith("@SheetName_"))
                .map(tag -> tag.replace("@SheetName_", ""))
                .findFirst().orElse(null);
        sheetName.set(extractedSheetName);
        if (scenario.getSourceTagNames().stream().anyMatch(tag -> tag.startsWith("@NonWSIB"))) {
            scenarioType.set("NonWSIB");
        } else if (scenario.getSourceTagNames().stream().anyMatch(tag -> tag.startsWith("@WSIBForm"))) {
            scenarioType.set("WSIB");
        } else {
            scenarioType.set("Unknown");
        }
    }

    public static String getScenarioName() { return scenarioName.get(); }

    @Before(order = 1)
    public void loadData(Scenario scenario) throws IOException {
        String sheetname = getSheetName(scenario);
        if (sheetname == null || sheetname.isEmpty()) {
            scenario.log("Sheet name (JSON filename) could not be determined for this scenario");
            return;
        }
        String currentScenarioId = scenario.getUri().toString() + "_" + scenario.getName();
        if (data.get() == null || !currentScenarioId.equals(lastScenarioId.get())) {
            data.set(JsonUtils.readJsonData(sheetname));
            AtomicInteger counter = scenarioCounter.computeIfAbsent(currentScenarioId, k -> new AtomicInteger(0));
            currentIndex.set(counter.getAndIncrement());
            lastScenarioId.set(currentScenarioId);
        }
    }

    @Before(order = 2)
    public void setScenarioData(Scenario scenario) {
        List<Map<String, String>> testData = data.get();
        if (testData != null && !testData.isEmpty()) {
            int index = currentIndex.get();
            if (index < testData.size()) {
                currentRow.set(testData.get(index));
                currentIndex.set(index + 1);
            }
        }
    }

    public static String getSheetName(Scenario scenario) {
        return scenario.getSourceTagNames().stream()
                .filter(tag -> tag.startsWith("@SheetName_"))
                .map(tag -> tag.replace("@SheetName_", ""))
                .findFirst().orElse(null);
    }
    public static ConfigReader getConfigReader() {
        if (configReader.get() == null) throw new IllegalStateException("ConfigReader is not initialized.");
        return configReader.get();
    }
    public static ConfigReader getCommonReader() { return commonPropReader.get(); }
    public static WebDriver getDriver() { return driver.get(); }
    public static String getScenarioType() { return scenarioType.get(); }
    public static void log(String message) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + message);
        if (scenario.get() != null) scenario.get().log(message);
        if (extentTest.get() != null) extentTest.get().info(message);
    }

    @Before
    public void openBrowser(Scenario scenario) throws Exception {
        if (scenario.getSourceTagNames().contains("@API")) {
            scenario.log("Skipping browser setup for @API tests.");
            return;
        }
        WebDriver webDriver;
        String browser = configReader.get().getProperty("browser").trim().toLowerCase();
        String headlessSysProp = System.getProperty("headless");
        boolean isHeadless = false;
        if (headlessSysProp != null) {
            isHeadless = headlessSysProp.trim().equalsIgnoreCase("true");
        } else if (commonPropReader.get().getProperty("headless") != null) {
            isHeadless = commonPropReader.get().getProperty("headless").trim().equalsIgnoreCase("true");
        }
        if (System.getProperty("os.name").toLowerCase().contains("linux") && System.getenv("DISPLAY") == null) {
            if (!isHeadless) {
                log("Linux CI detected (no DISPLAY), forcing Headless Mode: true");
                isHeadless = true;
            }
        }
        log("Browser: " + browser + " | Headless Mode: " + isHeadless);
        switch (browser) {
            case "chrome":
                try {
                    WebDriverManager.chromedriver().setup();
                } catch (Exception e) {
                    log("WebDriverManager failed for Chrome. Checking for local driver.");
                    String localPath = System.getProperty("os.name").toLowerCase().contains("win")
                            ? "src/test/resources/config/chromedriver.exe" : "src/test/resources/config/chromedriver";
                    if (new File(localPath).exists()) System.setProperty("webdriver.chrome.driver", localPath);
                    else throw e;
                }
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
                if (NetworkDiagnostics.enabled(commonPropReader.get())) {
                    LoggingPreferences logging = new LoggingPreferences();
                    logging.enable(LogType.PERFORMANCE, Level.ALL);
                    chromeOptions.setCapability("goog:loggingPrefs", logging);
                }
                if (isHeadless) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--window-size=1920,1080");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--disable-setuid-sandbox");
                    chromeOptions.addArguments("--disable-software-rasterizer");
                }
                webDriver = new ChromeDriver(chromeOptions);
                driver.set(webDriver); // Also permits cleanup if later setup fails.
                ((JavascriptExecutor) webDriver).executeScript(
                        "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
                break;
            case "edge":
                try {
                    WebDriverManager.edgedriver().setup();
                } catch (Exception e) {
                    log("WebDriverManager failed for Edge. Checking for local driver.");
                    String localPath = System.getProperty("os.name").toLowerCase().contains("win")
                            ? "src/test/resources/config/msedgedriver.exe" : "src/test/resources/config/msedgedriver";
                    if (new File(localPath).exists()) System.setProperty("webdriver.edge.driver", localPath);
                    else throw e;
                }
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setAcceptInsecureCerts(true);
                edgeOptions.setExperimentalOption("useAutomationExtension", false);
                edgeOptions.addArguments("--remote-allow-origins=*");
                edgeOptions.addArguments("--no-sandbox");
                edgeOptions.addArguments("--disable-dev-shm-usage");
                edgeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                if (NetworkDiagnostics.enabled(commonPropReader.get())) {
                    LoggingPreferences logging = new LoggingPreferences();
                    logging.enable(LogType.PERFORMANCE, Level.ALL);
                    edgeOptions.setCapability("ms:loggingPrefs", logging);
                }
                if (isHeadless) {
                    edgeOptions.addArguments("--headless=new");
                    edgeOptions.addArguments("--window-size=1920,1080");
                    edgeOptions.addArguments("--disable-gpu");
                }
                webDriver = new EdgeDriver(edgeOptions);
                driver.set(webDriver);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        webDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        webDriver.manage().deleteAllCookies();
        if (!isHeadless) webDriver.manage().window().maximize();
        String baseUrl = configReader.get().getProperty("baseUrl");
        NetworkDiagnostics.start(webDriver, commonPropReader.get(), baseUrl);
        log("Navigating to URL: " + baseUrl);
        webDriver.get(baseUrl);
    }

    /** After-step hooks run in descending order: this follows the original screenshot hook. */
    @AfterStep(order = 0)
    public void checkApplicationAndNetworkErrors(Scenario scenario) {
        if (driver.get() == null || !NetworkDiagnostics.enabled(commonPropReader.get())) return;
        NetworkDiagnostics.collect();
        boolean genericError = genericErrorVisible();
        if (scenario.isFailed() || genericError) attachNetworkEvidence(scenario);
        if (genericError && !scenario.isFailed()) {
            captureApplicationErrorScreenshot(scenario);
            if ("true".equalsIgnoreCase(commonSetting("network.capture.failOnUiError", "true"))) {
                // Only visible UI errors deliberately fail a scenario; a nearby failed API
                // alone does not. Preserve any failure already raised by the test.
                throw new AssertionError("Application displayed the configured generic error message. "
                        + "See browser-api-diagnostics; captured requests are not proof of root cause.");
            }
        }
    }

    private boolean genericErrorVisible() {
        String selectors = commonSetting("network.capture.uiErrorSelector", "h1,h2,h3,[role='alert'],[role='dialog']");
        String text = commonSetting("network.capture.uiErrorText", "something went wrong");
        try {
            Object visible = ((JavascriptExecutor) driver.get()).executeScript(
                    "var wanted = arguments[1].toLowerCase().replace(/\\s+/g, ' ').trim();"
                            + "return Array.from(document.querySelectorAll(arguments[0])).some(function(el) {"
                            + "var style = window.getComputedStyle(el);"
                            + "if (!el.getClientRects().length || style.display === 'none' || "
                            + "style.visibility === 'hidden' || style.opacity === '0') return false;"
                            + "var text = (el.innerText || '').toLowerCase().replace(/\\s+/g, ' ').trim();"
                            + "return text.length < 2000 && text.indexOf(wanted) !== -1;"
                            + "});", selectors, text);
            return Boolean.TRUE.equals(visible);
        } catch (RuntimeException e) {
            if (!uiCheckWarningLogged) {
                log("[NETWORK] UI error check unavailable (" + e.getClass().getSimpleName()
                        + "). Existing test assertions remain authoritative.");
                uiCheckWarningLogged = true;
            }
            return false;
        }
    }

    private void attachNetworkEvidence(Scenario scenario) {
        try {
            String summary = NetworkDiagnostics.summary();
            if (summary.equals(lastNetworkSummary)) return;
            scenario.attach(summary, "text/plain", "browser-api-diagnostics");
            ExtentTest test = extentTest.get();
            if (test != null) test.info("<details><summary>Browser API diagnostics</summary><pre>"
                    + escapeHtml(summary) + "</pre></details>");
            System.out.println("[NETWORK] Attached browser API diagnostics to scenario reports.");
            lastNetworkSummary = summary;
        } catch (Exception e) {
            System.err.println("[NETWORK] Unable to attach diagnostics (" + e.getClass().getSimpleName() + ").");
        }
    }
    private void captureApplicationErrorScreenshot(Scenario scenario) {
        try {
            byte[] image = ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(image, "image/png", "application-error");
            ExtentTest test = extentTest.get();
            if (test != null) test.info("Application error screenshot",
                    MediaEntityBuilder.createScreenCaptureFromBase64String(
                            Base64.getEncoder().encodeToString(image)).build());
        } catch (Exception e) {
            System.err.println("[NETWORK] Application error screenshot unavailable ("
                    + e.getClass().getSimpleName() + ").");
        }
    }
    private static String commonSetting(String key, String fallback) {
        ConfigReader common = commonPropReader.get();
        String value = common == null ? null : common.getProperty(key);
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }
    private static String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    @After
    public void closeDriver() {
        WebDriver webDriver = driver.get();
        try {
            if (webDriver != null && NetworkDiagnostics.enabled(commonPropReader.get())) {
                NetworkDiagnostics.collect();
                Scenario current = scenario.get();
                if (current != null && current.isFailed()) attachNetworkEvidence(current);
            }
        } finally {
            NetworkDiagnostics.stop();
            try {
                if (webDriver != null) webDriver.quit();
            } finally {
                driver.remove();
                configReader.remove();
                commonPropReader.remove();
                data.remove();
                currentIndex.remove();
                lastScenarioId.remove();
                currentRow.remove();
                sheetName.remove();
                scenarioType.remove();
                scenario.remove();
                extentTest.remove();
            }
        }
    }

    // Original screenshot/retry reporting retained.
    @AfterStep
    public void logStepResult(Scenario scenario) {
        ExtentTest test = extentTest.get();
        if (test == null) return;
        String screenshotsProp = commonPropReader.get() != null
                ? commonPropReader.get().getProperty("screenshots") : null;
        boolean isScreenshotEnabledEveryStep = screenshotsProp != null
                && screenshotsProp.trim().equalsIgnoreCase("true");
        if (scenario.isFailed() || isScreenshotEnabledEveryStep) {
            WebDriver webDriver = driver.get();
            if (webDriver != null) {
                try {
                    final byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
                    scenario.attach(screenshot, "image/png", "step_screenshot");
                    Status status = Status.PASS;
                    String message = "Step Screenshot";
                    if (scenario.isFailed()) {
                        ITestResult result = Reporter.getCurrentTestResult();
                        boolean willRetry = false;
                        if (result != null && result.getMethod().getRetryAnalyzer(result) instanceof RetryLogic) {
                            RetryLogic logic = (RetryLogic) result.getMethod().getRetryAnalyzer(result);
                            if (!logic.isLastAttempt()) willRetry = true;
                        }
                        if (willRetry) {
                            status = Status.WARNING;
                            message = "Step Failed (Intermediate Attempt)";
                        } else {
                            status = Status.FAIL;
                            message = "Step Failed (Final Attempt)";
                        }
                    }
                    String screenshotBase64 = "data:image/png;base64,"
                            + Base64.getEncoder().encodeToString(screenshot);
                    test.log(status, message,
                            MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
                } catch (Exception e) {
                    log("Error while taking screenshot: " + e.getMessage());
                }
            }
        }
    }
}
