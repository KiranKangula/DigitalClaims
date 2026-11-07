package utils;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class AccessibilityUtils {

    /**
     * Runs a full WCAG accessibility scan on the current page.
     * Tags: wcag2a, wcag2aa, wcag21aa cover WCAG 2.0/2.1 Level A & AA.
     *
     * @param driver   active WebDriver instance
     * @param pageName a label for reporting purposes
     * @return AxeResults containing violations, passes, incomplete
     */
    public static Results runAccessibilityScan(WebDriver driver, String pageName) {
        Results axeResults = new AxeBuilder()
                .withTags(List.of("wcag2a", "wcag2aa", "wcag21aa", "wcag22aa", "best-practice"))
                .analyze(driver);

        System.out.println("=== Accessibility Scan: " + pageName + " ===");
        System.out.println("URL: " + axeResults.getUrl());
        System.out.println("WCAG Violations Found: " + axeResults.getViolations().size());

        return axeResults;
    }

    /**
     * Builds a human-readable summary of violations for reporting.
     */
    public static String buildViolationSummary(Results results) {
        List<Rule> violations = results.getViolations();
        if (violations.isEmpty()) {
            return "✅ No WCAG violations found on: " + results.getUrl();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("❌ WCAG Violations on: ").append(results.getUrl()).append("\n\n");

        for (Rule violation : violations) {
            sb.append("--------------------------------------------------\n");
            sb.append("Rule ID    : ").append(violation.getId()).append("\n");
            sb.append("Impact     : ").append(violation.getImpact()).append("\n");
            sb.append("Description: ").append(violation.getDescription()).append("\n");
            sb.append("Help URL   : ").append(violation.getHelpUrl()).append("\n");
            sb.append("Affected Nodes:\n");
            violation.getNodes().forEach(node -> {
                sb.append("  - HTML  : ").append(node.getHtml()).append("\n");
                node.getFailureSummary().lines().forEach(line ->
                        sb.append("    Issue : ").append(line.trim()).append("\n")
                );
            });
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns true if there are any critical/serious violations.
     */
    public static boolean hasCriticalViolations(Results results) {
        return results.getViolations().stream()
                .anyMatch(v -> "critical".equals(v.getImpact()) || "serious".equals(v.getImpact()));
    }
}