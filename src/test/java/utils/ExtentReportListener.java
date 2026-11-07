package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import definitions.Hooks;
import io.cucumber.testng.PickleWrapper;
import org.openqa.selenium.WebDriver;
import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * Based on the user's uploaded ExtentReportListener.java.
 *
 * Fresh run: SanityTest_<date>/Execution_<environment>_<timestamp>.html/.json.
 * report.rerun=true: replace executed scenario records in the latest completed
 * report for the environment, then rebuild that report's original HTML.
 * HTML presentation: runner -> readable scenario hierarchy.
 * JSON persistence: flat, uniquely named scenario records.
 *
 * Archive reconstruction deliberately DOES NOT deserialize a complete Extent
 * Test or Log. Extent's public factories create nodes, logs, labels and screenshot
 * media. Jackson updates only explicitly selected scalar/date/map fields.
 * This avoids ExceptionInfo/Category/Author/Device constructor deserialization.
 * Exception stack traces are restored as escaped text with the saved log status;
 * rich exception-dashboard metadata is not reconstructed.
 *
 * Target: ExtentReports 5.1.1, Cucumber-TestNG 7.20.1, TestNG 7.9.0.
 * NOT compiled or executed in the assistant environment. Validate in the project.
 * Run one execution at a time; resume only the latest completed fresh execution
 * for the environment. Keep feature URIs/example-row lines and timezone unchanged.
 */
public class ExtentReportListener
        implements ITestListener, ISuiteListener, IExecutionListener {

    private static final Path ENV_CACHE_FILE = Paths.get("target", ".last_run_env");
    private static final String RESULT_NODE =
            ExtentReportListener.class.getName() + ".node";

    private static ExtentReports extent;
    private static Path reportHtml;
    private static Path reportJson;
    private static Path reportPointer;
    private static String reportEnvironment;

    @Override
    public void onStart(ISuite suite) {
        String environment = nonBlank(suite.getParameter("env"));
        if (environment == null) {
            environment = nonBlank(System.getProperty("env"));
        }
        if (environment == null && Files.exists(ENV_CACHE_FILE)) {
            try {
                environment = nonBlank(Files.readString(
                        ENV_CACHE_FILE, StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new IllegalStateException("Cannot read cached environment", e);
            }
        }
        if (environment == null) {
            throw new IllegalStateException(
                    "No environment found in XML, -Denv, or target/.last_run_env. "
                            + "Run the original environment XML first.");
        }
        synchronized (ExtentReportListener.class) {
            if (extent != null && !environment.equals(reportEnvironment)) {
                throw new IllegalStateException(
                        "This listener supports one environment per execution. "
                                + "Already initialized for " + reportEnvironment
                                + ", but another suite requested " + environment);
            }
            System.setProperty("env", environment);
            try {
                Files.createDirectories(ENV_CACHE_FILE.getParent());
                Files.writeString(ENV_CACHE_FILE, environment, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new IllegalStateException("Cannot save environment cache", e);
            }
            if (extent == null) {
                initReport(environment, suite);
            }
        }
    }

    private static void initReport(String environment, ISuite suite) {
        try {
            Properties common = new Properties();
            try (InputStream input = ExtentReportListener.class.getClassLoader()
                    .getResourceAsStream("config/Common.properties")) {
                if (input == null) {
                    throw new IllegalStateException(
                            "config/Common.properties is missing from the classpath");
                }
                common.load(input);
            }
            String mode = common.getProperty("report.rerun", "false").trim();
            if (!"true".equalsIgnoreCase(mode) && !"false".equalsIgnoreCase(mode)) {
                throw new IllegalStateException("report.rerun must be true or false");
            }
            boolean rerun = isRerunExecution(suite);
            Path output = Paths.get("test-output").toAbsolutePath().normalize();
            Files.createDirectories(output);
            String safeEnvironment = environment.replaceAll("[^A-Za-z0-9_-]", "_");
            reportPointer = output.resolve(".last-extent-" + safeEnvironment + ".properties");

            if (rerun) {
                if (!Files.isRegularFile(reportPointer)) {
                    throw new IllegalStateException(
                            "No saved original report for " + environment
                                    + ". Set report.rerun=false and complete a fresh run first.");
                }
                Properties saved = new Properties();
                try (InputStream input = Files.newInputStream(reportPointer)) {
                    saved.load(input);
                }
                if (!environment.equals(saved.getProperty("environment"))) {
                    throw new IllegalStateException("Saved report environment mismatch");
                }
                String savedHtml = nonBlank(saved.getProperty("html"));
                String savedJson = nonBlank(saved.getProperty("json"));
                if (savedHtml == null || savedJson == null) {
                    throw new IllegalStateException("Saved report pointer is incomplete");
                }
                reportHtml = Paths.get(savedHtml);
                reportJson = Paths.get(savedJson);
                if (!Files.isRegularFile(reportHtml)
                        || !Files.isRegularFile(reportJson)
                        || Files.size(reportJson) == 0) {
                    throw new IllegalStateException(
                            "The original HTML or JSON archive is missing/empty. "
                                    + "Do not delete the original report files between runs.");
                }
            } else {
                Date now = new Date();
                String dateFolder = new SimpleDateFormat("dd-MMM-yyyy").format(now);
                String timestamp = new SimpleDateFormat("HHmmss_a").format(now);
                Path folder = output.resolve("SanityTest_" + dateFolder);
                Path jsonFolder = folder.resolve("JSON");
        // Creates the date folder and its JSON subfolder if necessary.
                Files.createDirectories(jsonFolder);
                String stem = "Execution_" + environment + "_" + timestamp;
        // HTML remains directly inside the date folder.
                reportHtml = folder.resolve(stem + ".html");
        // JSON is stored inside the JSON subfolder.
                reportJson = jsonFolder.resolve(stem + ".json");
                if (Files.exists(reportHtml) || Files.exists(reportJson)) {
                    throw new IllegalStateException(
                            "Timestamped report already exists; start a new run later: "
                                    + reportHtml);
                }
            }

            ExtentReports report = new ExtentReports();
            if (rerun) {
                restoreFlatArchive(report, reportJson);
            }
            report.attachReporter(new JsonFormatter(reportJson.toString()));
            report.setSystemInfo("Environment", environment);
            reportEnvironment = environment;
            extent = report;
            System.out.println("[REPORTER] " + (rerun ? "Updating original: " : "New report: ")
                    + reportHtml);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot initialize report persistence", e);
        }
    }
    private static boolean isRerunExecution(ISuite suite) {
        String execution = nonBlank(
                suite.getParameter("report.execution")
        );

        if ("fresh".equalsIgnoreCase(execution)) {
            System.out.println(
                    "[REPORTER] Original XML marker found: creating a new report."
            );
            return false;
        }

        if (execution == null || "rerun".equalsIgnoreCase(execution)) {
            System.out.println(
                    "[REPORTER] Rerun mode: updating the saved original report."
            );
            return true;
        }

        throw new IllegalStateException(
                "report.execution must be fresh or rerun; found: " + execution
        );
    }
    @Override
    public void onTestStart(ITestResult result) {
        Hooks.extentTest.set(nodeFor(result));
    }

    private static synchronized ExtentTest nodeFor(ITestResult result) {
        ExtentTest existing = (ExtentTest) result.getAttribute(RESULT_NODE);
        if (existing != null) {
            return existing;
        }
        if (extent == null) {
            throw new IllegalStateException("Report has not been initialized");
        }
        String name = reportTestName(result);
        extent.removeTest(name);
        ExtentTest node = extent.createTest(name);
        node.assignCategory(result.getTestClass().getRealClass().getSimpleName());
        result.setAttribute(RESULT_NODE, node);
        return node;
    }

    private static String reportTestName(ITestResult result) {
        String runner = result.getTestClass().getRealClass().getName();
        Object[] parameters = result.getParameters();
        if (parameters != null && parameters.length > 0
                && parameters[0] instanceof PickleWrapper) {
            PickleWrapper wrapper = (PickleWrapper) parameters[0];
            return wrapper.getPickle().getName() + " [" + runner + " | "
                    + wrapper.getPickle().getUri() + ":"
                    + wrapper.getPickle().getLine() + "]";
        }
        if (parameters != null && parameters.length > 0) {
            throw new IllegalStateException(
                    "A stable result key is required for non-Cucumber parameterized tests: "
                            + runner + "." + result.getMethod().getMethodName());
        }
        return runner + "." + result.getMethod().getMethodName();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        nodeFor(result).pass("Scenario passed (latest attempt).");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest node = nodeFor(result);
        if (result.getThrowable() != null) {
            node.fail(result.getThrowable());
        } else {
            node.fail("Scenario failed.");
        }
        captureScreenshot(node, Status.FAIL);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest node = nodeFor(result);
        if (result.wasRetried()) {
            node.warning("Attempt will be retried; the next attempt replaces this entry.");
        } else if (result.getThrowable() != null) {
            node.skip(result.getThrowable());
        } else {
            node.skip("Scenario skipped.");
        }
    }

    @Override
    public void onExecutionFinish() {
        synchronized (ExtentReportListener.class) {
            if (extent == null) {
                return;
            }
            try {
                extent.flush();
                if (!Files.isRegularFile(reportJson) || Files.size(reportJson) == 0) {
                    throw new IllegalStateException("JSON report archive was not written");
                }
                System.out.println("[REPORTER] Building HTML from JSON: " + reportJson);
                writeHierarchicalHtml(reportJson, reportHtml, reportEnvironment);

                Properties saved = new Properties();
                saved.setProperty("environment", reportEnvironment);
                saved.setProperty("html", reportHtml.toString());
                saved.setProperty("json", reportJson.toString());
                try (OutputStream output = Files.newOutputStream(reportPointer)) {
                    saved.store(output, "Most recent completed report for this environment");
                }
                System.out.println("[REPORTER] Saved: " + reportHtml);
            } catch (Exception | LinkageError e) {
                System.err.println("[REPORTER] Report finalization failed");
                System.err.println("[REPORTER] JSON path: " + reportJson);
                System.err.println("[REPORTER] HTML path: " + reportHtml);
                e.printStackTrace(System.err);
                throw new IllegalStateException("Cannot generate or persist report", e);
            }
        }
    }

    private static void restoreFlatArchive(ExtentReports report, Path archive)
            throws IOException {
        ObjectMapper mapper = archiveMapper();
        for (ObjectNode saved : readFlatArchive(mapper, archive)) {
            ExtentTest node = report.createTest(requiredName(saved));
            hydrateArchivedFields(mapper, saved, node);
            assertArchivedScenario(saved, node.getModel());
        }
    }

    private static void writeHierarchicalHtml(Path json, Path html, String environment)
            throws IOException {
        ObjectMapper mapper = archiveMapper();
        List<ObjectNode> records = readFlatArchive(mapper, json);
        ExtentReports presentation = new ExtentReports();
        Map<String, ExtentTest> runners = new LinkedHashMap<>();
        List<Test> renderedScenarios = new ArrayList<>();
        for (ObjectNode saved : records) {
            DisplayName display = displayNameFor(requiredName(saved));
            ExtentTest runner = runners.get(display.runner);
            if (runner == null) {
                String simpleName = display.runner.substring(display.runner.lastIndexOf('.') + 1);
                runner = presentation.createTest(simpleName);
                runners.put(display.runner, runner);
            }
            ExtentTest child = runner.createNode(display.scenario);
            hydrateArchivedFields(mapper, saved, child);
            assertArchivedScenario(saved, child.getModel());
            renderedScenarios.add(child.getModel());
        }
        for (ExtentTest runner : runners.values()) {
            summarizeRunner(runner.getModel());
        }

        Path destination = html.toAbsolutePath().normalize();
        Files.createDirectories(destination.getParent());
        Path temporary = Files.createTempFile(destination.getParent(), ".extent-render-", ".html");
        try {
            ExtentSparkReporter spark = new ExtentSparkReporter(temporary.toString());
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Automation Execution Report - " + environment);
            spark.config().setReportName("Test Execution Results - " + environment);
            presentation.attachReporter(spark);
            presentation.setSystemInfo("Environment", environment);
            presentation.flush();

            for (int i = 0; i < records.size(); i++) {
                assertArchivedScenario(records.get(i), renderedScenarios.get(i));
            }
            for (ExtentTest runner : runners.values()) {
                for (Test child : runner.getModel().getChildren()) {
                    if (child.getStatus() == Status.FAIL
                            && runner.getModel().getStatus() != Status.FAIL) {
                        throw new IllegalStateException(
                                "Refusing to publish a passing runner with a failed scenario: "
                                        + runner.getModel().getName());
                    }
                }
            }
            if (!Files.isRegularFile(temporary) || Files.size(temporary) == 0) {
                throw new IllegalStateException("Spark did not write the HTML report");
            }
            Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    /**
     * Create compound objects through Extent's APIs, NOT Jackson model binding.
     * Category/Author/Device are restored by assign...(), screenshots by the media
     * builder, exceptions by escaped text with the ORIGINAL saved log severity.
     * Only selected timestamp/sequence/map fields use readerForUpdating().
     */
    private static void hydrateArchivedFields(
            ObjectMapper mapper, ObjectNode saved, ExtentTest node) throws IOException {
        Test model = node.getModel();
        if (model.getLogs() == null || !model.getLogs().isEmpty()) {
            throw new IllegalStateException("Archive restoration requires a fresh, empty node");
        }
        JsonNode rootMedia = saved.get("media");
        if (rootMedia != null && !rootMedia.isNull()
                && (!rootMedia.isArray() || !rootMedia.isEmpty())) {
            throw new IOException(
                    "Non-empty scenario-level media needs an explicit restoration rule: "
                            + requiredName(saved)
                            + ". This reader restores screenshot media attached to logs.");
        }
        JsonNode generated = saved.get("generatedLog");
        if (generated != null && !generated.isNull()
                && (!generated.isArray() || !generated.isEmpty())) {
            throw new IOException(
                    "Non-empty generatedLog needs an explicit restoration rule: "
                            + requiredName(saved));
        }
        for (JsonNode sourceLog : saved.path("logs")) {
            Status status = requiredStatus(sourceLog, "archived log");
            String details = sourceLog.path("details").asText("");
            JsonNode exception = sourceLog.get("exception");
            if (exception != null && !exception.isNull()) {
                if (!exception.isObject()) {
                    throw new IOException("Expected an archived exception object");
                }
                String trace = nonBlank(exception.path("stackTrace").asText(null));
                if (trace == null) {
                    trace = exception.path("name").asText("Archived exception");
                }
                details += "<pre>" + escapeReportText(trace) + "</pre>";
            }

            int before = model.getLogs().size();
            replayArchivedLog(node, status, details, sourceLog.get("media"));
            if (model.getLogs().size() != before + 1) {
                throw new IllegalStateException("An archived event did not produce exactly one log");
            }
            // No exception, media, Category, Author or Device objects reach Jackson.
            ObjectNode timing = selectedFields(
                    mapper, sourceLog, "timestamp", "seq", "infoMap");
            mapper.readerForUpdating(model.getLogs().get(before))
                    .readValue(timing.toString());
        }

        // Public APIs construct Extent label objects and register them normally.
        String[] categories = archivedLabelNames(saved, "categorySet");
        String[] authors = archivedLabelNames(saved, "authorSet");
        String[] devices = archivedLabelNames(saved, "deviceSet");
        if (categories.length > 0) {
            node.assignCategory(categories);
        }
        if (authors.length > 0) {
            node.assignAuthor(authors);
        }
        if (devices.length > 0) {
            node.assignDevice(devices);
        }

        // Whitelist only safe fields. Never bind the whole saved Test/Log model.
        ObjectNode timing = selectedFields(
                mapper, saved, "useNaturalConf", "startTime", "endTime", "infoMap");
        mapper.readerForUpdating(model).readValue(timing.toString());
        model.setStatus(requiredStatus(saved, "scenario"));
    }

    private static void replayArchivedLog(
            ExtentTest node, Status status, String details, JsonNode media) throws IOException {
        if (media == null || media.isNull()) {
            node.log(status, details);
            return;
        }
        if (!media.isObject()) {
            throw new IOException("Expected an archived log-media object");
        }
        String base64 = nonBlank(media.path("base64").asText(null));
        String path = nonBlank(media.path("path").asText(null));
        if (base64 != null) {
            // JsonFormatter may save a complete data URI. The builder wants its
            // base64 payload, not a second data:image/png;base64 prefix.
            if (base64.startsWith("data:")) {
                if (!base64.startsWith("data:image/png;base64,")) {
                    throw new IOException("Only PNG screenshot data URIs are supported here");
                }
                base64 = base64.substring(base64.indexOf(',') + 1);
            }
            node.log(status, details,
                    MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
        } else if (path != null) {
            node.log(status, details,
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
        } else {
            throw new IOException(
                    "Archived screenshot media has neither base64 nor path; refusing to drop it");
        }
    }

    private static String[] archivedLabelNames(JsonNode saved, String field) throws IOException {
        JsonNode labels = saved.get(field);
        if (labels == null || labels.isNull()) {
            return new String[0];
        }
        if (!labels.isArray()) {
            throw new IOException("Expected an array for " + field);
        }
        List<String> names = new ArrayList<>();
        for (JsonNode label : labels) {
            String name = nonBlank(label.path("name").asText(null));
            if (name == null) {
                throw new IOException("Missing archived label name in " + field);
            }
            names.add(name);
        }
        return names.toArray(new String[0]);
    }

    private static ObjectNode selectedFields(
            ObjectMapper mapper, JsonNode source, String... names) {
        ObjectNode result = mapper.createObjectNode();
        for (String name : names) {
            JsonNode value = source.get(name);
            if (value != null && !value.isNull()) {
                result.set(name, value.deepCopy());
            }
        }
        return result;
    }

    private static String escapeReportText(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static List<ObjectNode> readFlatArchive(ObjectMapper mapper, Path archive)
            throws IOException {
        JsonNode root;
        try (Reader reader = Files.newBufferedReader(archive, StandardCharsets.UTF_8)) {
            root = mapper.readTree(reader);
        }
        if (root == null || !root.isArray()) {
            throw new IllegalStateException("Expected a flat JSON array: " + archive);
        }
        List<ObjectNode> result = new ArrayList<>();
        Map<String, Boolean> identities = new LinkedHashMap<>();
        for (JsonNode entry : root) {
            if (!entry.isObject()) {
                throw new IllegalStateException("Archive contains a non-object record");
            }
            ObjectNode record = (ObjectNode) entry;
            String name = requiredName(record);
            if (identities.put(name, Boolean.TRUE) != null) {
                throw new IllegalStateException("Duplicate scenario identity: " + name);
            }
            requiredStatus(record, "scenario");
            JsonNode children = record.get("children");
            if (children != null && (!children.isArray() || !children.isEmpty())) {
                throw new IllegalStateException("Expected flat records, not grouped JSON: " + name);
            }
            if (!record.path("logs").isArray()) {
                throw new IllegalStateException("Missing log array for " + name);
            }
            for (JsonNode log : record.path("logs")) {
                requiredStatus(log, "log in " + name);
            }
            result.add(record);
        }
        return result;
    }

    private static void assertArchivedScenario(ObjectNode saved, Test model) {
        String name = requiredName(saved);
        Status expected = requiredStatus(saved, "scenario");
        if (model.getStatus() != expected) {
            throw new IllegalStateException(
                    "Status changed while rebuilding " + name
                            + ": JSON=" + expected + ", model=" + model.getStatus());
        }
        JsonNode logs = saved.path("logs");
        if (model.getLogs() == null || model.getLogs().size() != logs.size()) {
            throw new IllegalStateException("Log count changed while rebuilding " + name);
        }
        for (int i = 0; i < logs.size(); i++) {
            Status expectedLog = requiredStatus(logs.get(i), "log in " + name);
            if (model.getLogs().get(i).getStatus() != expectedLog) {
                throw new IllegalStateException(
                        "Log severity changed while rebuilding " + name + " at index " + i);
            }
        }
    }

    private static ObjectMapper archiveMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SimpleModule dates = new SimpleModule();
        dates.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser parser, DeserializationContext context)
                    throws IOException {
                if (parser.currentToken().isNumeric()) {
                    return new Date(parser.getLongValue());
                }
                String text = parser.getValueAsString();
                if (text == null) {
                    throw new IOException("Missing/unsupported archive timestamp");
                }
                text = text.replace('\u202f', ' ').replace('\u00a0', ' ').trim();
                String[] patterns = {
                        "MMM d, yyyy, h:mm:ss a", "MMM d, yyyy h:mm:ss a",
                        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd'T'HH:mm:ssXXX"
                };
                for (String pattern : patterns) {
                    SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);
                    format.setLenient(false);
                    ParsePosition position = new ParsePosition(0);
                    Date value = format.parse(text, position);
                    if (value != null && position.getIndex() == text.length()) {
                        return value;
                    }
                }
                throw new IOException("Unsupported timestamp in Extent archive: " + text);
            }
        });
        mapper.registerModule(dates);
        return mapper;
    }

    private static String requiredName(JsonNode record) {
        String name = nonBlank(record.path("name").asText(null));
        if (name == null) {
            throw new IllegalStateException("Scenario name is missing from archive");
        }
        return name;
    }

    private static Status requiredStatus(JsonNode record, String description) {
        String value = nonBlank(record.path("status").asText(null));
        if (value == null) {
            throw new IllegalStateException("Missing status for " + description);
        }
        try {
            return Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown status for " + description + ": " + value, e);
        }
    }

    private static DisplayName displayNameFor(String storedName) {
        int marker = storedName.lastIndexOf(" [");
        int separator = marker < 0 ? -1 : storedName.indexOf(" | ", marker + 2);
        if (marker >= 0 && separator > marker + 2 && storedName.endsWith("]")) {
            return new DisplayName(
                    storedName.substring(marker + 2, separator), storedName.substring(0, marker));
        }
        int lastDot = storedName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < storedName.length() - 1) {
            return new DisplayName(storedName.substring(0, lastDot), storedName.substring(lastDot + 1));
        }
        throw new IllegalStateException("Unrecognized persisted scenario identity: " + storedName);
    }

    private static void summarizeRunner(Test runner) {
        Status summary = Status.PASS;
        Date start = null;
        Date end = null;
        for (Test scenario : runner.getChildren()) {
            Status status = scenario.getStatus();
            if (status == Status.FAIL) {
                summary = Status.FAIL;
            } else if (status == Status.SKIP && summary != Status.FAIL) {
                summary = Status.SKIP;
            } else if (status == Status.WARNING && summary != Status.FAIL && summary != Status.SKIP) {
                summary = Status.WARNING;
            }
            Date childStart = scenario.getStartTime();
            Date childEnd = scenario.getEndTime();
            if (childStart != null && (start == null || childStart.before(start))) {
                start = childStart;
            }
            if (childEnd != null && (end == null || childEnd.after(end))) {
                end = childEnd;
            }
        }
        runner.setStatus(summary);
        if (start != null) {
            runner.setStartTime(start);
        }
        if (end != null) {
            runner.setEndTime(end);
        }
    }

    private static final class DisplayName {
        private final String runner;
        private final String scenario;
        private DisplayName(String runner, String scenario) {
            this.runner = runner;
            this.scenario = scenario;
        }
    }

    private static String nonBlank(String value) {
        return value == null || value.trim().isEmpty() ? null : value.trim();
    }

    private void captureScreenshot(ExtentTest node, Status status) {
        WebDriver driver = Hooks.getDriver();
        if (driver != null) {
            try {
                String screenshotPath = ScreenshotUtil.getScreenshot(
                        driver, "Report_" + System.currentTimeMillis());
                node.log(status, "Screenshot",
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                node.warning("Screenshot attachment failed: " + e.getMessage());
            }
        }
    }

    /**
     * Offline regeneration: no test execution or JSON/report-pointer changes.
     * Use the module's TEST classpath and project root as the working directory.
     * Arguments: <input-json> <output-html> <environment>.
     * First select a different output filename, such as *_checked.html.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 3 || nonBlank(args[2]) == null) {
            throw new IllegalArgumentException(
                    "Usage: ExtentReportListener <input-json> <output-html> <environment>");
        }
        Path json = Paths.get(args[0]).toAbsolutePath().normalize();
        Path html = Paths.get(args[1]).toAbsolutePath().normalize();
        if (json.equals(html)) {
            throw new IllegalArgumentException("Input JSON and output HTML must differ");
        }
        writeHierarchicalHtml(json, html, args[2].trim());
        System.out.println("[REPORTER] Rebuilt HTML from unchanged JSON: " + html);
    }
}
