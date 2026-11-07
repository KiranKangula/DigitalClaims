package pages.DynamicIntakeEform7;

import definitions.Hooks;
import io.cucumber.java.en.And;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DynamicIntakeEform7.DIeForm7_SupportingDocuments;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/** Add this as a NEW glue class; do not replace your existing EForm7 definitions. */
public class EForm7SupportingDocumentSteps {

    @And("Upload and verify Form {int} supporting documents")
    public void uploadAndVerifyForm7SupportingDocuments() throws Exception {
        Map<String, String> row = Hooks.currentRow.get();
        if (row == null) {
            throw new IllegalStateException("Form 7 scenario data is not initialized.");
        }
        String files = required(row, "SupportingFiles");
        String types = required(row, "SupportingDocumentTypes");
        ConfigReader config = Hooks.getConfigReader();
        DIeForm7_SupportingDocuments page = supportingDocumentsPage();
        List<String> verified = page.attachSupportingDocuments(
                files, types, config.getProperty("upload.folder"), row.get("AdditionalComments"));
        row.put("VerifiedSupportingFiles", String.join("|", verified));
        Hooks.log("[FORM 7 DOCUMENTS] Verified original filenames: " + String.join(" | ", verified));
    }

    @And("Continue Form 7 without supporting documents")
    public void continueForm7WithoutSupportingDocuments() throws Exception {
        Assert.assertTrue("Could not continue through Form 7 supporting documents/comments.",
                supportingDocumentsPage().attachSupportingDocuments());
    }

    private DIeForm7_SupportingDocuments supportingDocumentsPage() throws Exception {
        // Resolve the driver when the step runs, not before the scenario's browser hook.
        WebDriver driver = Hooks.getDriver();
        if (driver == null) {
            throw new IllegalStateException("The scenario WebDriver is not initialized.");
        }
        ConfigReader common = Hooks.getCommonReader();
        String value = common.getProperty("timeout");
        long timeout = value == null || value.trim().isEmpty() ? 10 : Long.parseLong(value.trim());
        if (timeout <= 0) {
            throw new IllegalArgumentException("The common timeout must be positive.");
        }
        return new DIeForm7_SupportingDocuments(driver,
                new WebDriverWait(driver, Duration.ofSeconds(timeout)));
    }

    private String required(Map<String, String> row, String key) {
        String value = row.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("The Form 7 upload test requires " + key + " in its JSON row.");
        }
        return value;
    }
}
