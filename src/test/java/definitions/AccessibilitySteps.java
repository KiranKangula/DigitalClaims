package definitions;

import com.deque.html.axecore.results.Results;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.AccessibilityUtils;

public class AccessibilitySteps {

    private Results lastScanResults;

    private WebDriver getDriver() {
        return Hooks.getDriver();
    }

    @When("User runs accessibility scan on {string}")
    public void userRunsAccessibilityScanOn(String pageName) {
        lastScanResults = AccessibilityUtils.runAccessibilityScan(getDriver(), pageName);

        // Log results into Cucumber report
        String summary = AccessibilityUtils.buildViolationSummary(lastScanResults);
        Hooks.scenario.get().log(summary);
    }

    @Then("the page should have no critical WCAG violations")
    public void thePageShouldHaveNoCriticalWCAGViolations() {
        String summary = AccessibilityUtils.buildViolationSummary(lastScanResults);
        Assert.assertFalse(
                AccessibilityUtils.hasCriticalViolations(lastScanResults),
                "Critical/Serious WCAG violations found:\n" + summary
        );
    }

    @Then("the page should have no WCAG violations")
    public void thePageShouldHaveNoWCAGViolations() {
        String summary = AccessibilityUtils.buildViolationSummary(lastScanResults);
        Assert.assertEquals(
                lastScanResults.getViolations().size(), 0,
                "WCAG violations found:\n" + summary
        );
    }

    @And("User logs all WCAG results without failing")
    public void userLogsAllWCAGResultsWithoutFailing() {
        // Soft check — logs everything, never fails the scenario
        String summary = AccessibilityUtils.buildViolationSummary(lastScanResults);
        Hooks.scenario.get().log(summary);
        System.out.println(summary);
    }
}