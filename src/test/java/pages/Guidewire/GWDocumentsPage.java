package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.*;

public class GWDocumentsPage extends BaseDriverClass {
    ConfigReader prop = Hooks.getConfigReader();

    @FindBy(id = "Claim:MenuLinks:Claim_ClaimDocumentsGroup")
    private WebElement ClaimDocumentsLink;
    @FindBy(xpath = "//*[contains(@id, 'DocumentsMemosScreen:ClaimDocumentSearchDV:Hidden')]")
    private WebElement HiddenDropdown;
    @FindBy(xpath = "//*[contains(@id, 'DocumentsMemosScreen:ClaimDocumentSearchDV:RedactionRequired')]")
    private WebElement RedactionRequiredDropdown;
    @FindBy(xpath = "//*[contains(@id, 'DocumentsMemosScreen:ClaimDocumentSearchDV:NoAccess')]")
    private WebElement NoAccessDropdown;
    @FindBy(xpath = "//*[contains(@id, 'DocumentsMemosScreen:ClaimDocumentSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search_link')]")
    private WebElement Search_link;
    @FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:StartDate")
    private WebElement SearchStartDate;
    @FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:EndDate")
    private WebElement SearchEndDate;
    @FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:Author")
    private WebElement Author;
    @FindBy(id = "ClaimDocuments:DocumentsMemosScreen:DocumentsLV")
    private WebElement DocumentsTable;
    @FindBy(id = "WSIB_Form8_FNOLWizard:Documents")
    private WebElement UnnumberedDocLink;

    @FindBy(id = "WSIB_Form8_FNOLWizard:IndependentWizardStepSet:DocumentsMemosScreen:DocumentsLV")
    private WebElement NIHLUNCDTable;

    @FindBy(xpath = "//span[contains(@id, 'WSIB_Form8_FNOLWizard:IndependentWizardStepSet:DocumentsMemosScreen:DocumentsLV:_ListPaging_container')]")
    private List<WebElement> NIHLUNCDPagers;



    public GWDocumentsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean verifyLandedDocument(String author, String description) throws InterruptedException {
        List<String> descriptions = new ArrayList<>();
        descriptions.add(description);
        return verifyLandedDocuments(author, descriptions);
    }

    public boolean verifyLandedDocuments(String author, List<String> descriptions) throws InterruptedException {
        Click(ClaimDocumentsLink);
        new Select(HiddenDropdown).selectByValue("Yes");
        new Select(RedactionRequiredDropdown).selectByValue("Yes");
        new Select(NoAccessDropdown).selectByValue("Yes");
        jsSendKeys(SearchStartDate, getCurrentDate("dd/MM/yyyy"));
        jsSendKeys(SearchEndDate, getCurrentDate("dd/MM/yyyy"));
        jsSendKeys(Author, author);
        Click(Search_link);
        Sleep(5000);
        int maxAttempts = 2;
        if (prop.getProperty("DocuploadVerification").equalsIgnoreCase("Yes")) {
            maxAttempts = Integer.parseInt(prop.getProperty("DocuploadLandingTime"));
        }
        boolean isOspcAuthor = "OSPC".equalsIgnoreCase(author != null ? author.trim() : "");
        // Prepare expected document counts / lookup
        Map<String, Integer> expectedIncrements = new HashMap<>();
        for (String desc : descriptions) {
            String normalizedDesc = desc.toLowerCase().trim();
            expectedIncrements.put(normalizedDesc, expectedIncrements.getOrDefault(normalizedDesc, 0) + 1);
        }
        // Measure initial counts only when author is NOT OSPC
        Map<String, Integer> initialCounts = new HashMap<>();
        if (!isOspcAuthor) {
            initialCounts = getDocumentCountsFromTable(expectedIncrements.keySet());
            for (String desc : expectedIncrements.keySet()) {
                Hooks.log("Initial count for '" + desc + "': " + initialCounts.get(desc));
            }
        } else {
            Hooks.log("Author is OSPC - skipping initial baseline counts. Checking all visible rows directly.");
        }
        boolean allLanded = false;
        for (int i = 0; i < maxAttempts; i++) {
            if (isOspcAuthor) {
                // Check all rows currently visible on screen
                allLanded = verifyAllVisibleRowsContainDocuments(descriptions);
            } else {
                // Count-based verification for non-OSPC
                Map<String, Integer> currentCounts = getDocumentCountsFromTable(expectedIncrements.keySet());
                allLanded = true;
                for (String desc : expectedIncrements.keySet()) {
                    int requiredCount = initialCounts.getOrDefault(desc, 0) + expectedIncrements.get(desc);
                    if (currentCounts.getOrDefault(desc, 0) < requiredCount) {
                        allLanded = false;
                        Hooks.log("Waiting for '" + desc + "'. Current: " + currentCounts.get(desc) + ", Required: " + requiredCount);
                        break;
                    }
                }
            }
            if (allLanded) {
                Hooks.log("All expected documents successfully verified in GWCC.");
                break;
            } else {
                Sleep(5000);
                Hooks.log("Waiting for documents to land... Attempt: " + (i + 1));
                Click(Search_link);
                Sleep(5000);
            }
        }
        return allLanded;
    }
    /**
     * Scans all rows currently visible on the screen to verify
     * that each expected document description is present.
     */
    private boolean verifyAllVisibleRowsContainDocuments(List<String> expectedDescriptions) {
        // List of descriptions remaining to find (case-insensitive)
        List<String> remainingToFind = new ArrayList<>();
        for (String desc : expectedDescriptions) {
            remainingToFind.add(desc.toLowerCase().trim());
        }
        // Get all rows displayed on screen (supports React table, standard table, or Jut table rows)
        List<WebElement> rows = getDriver().findElements(By.cssSelector("div[id*='ClaimDocuments'] .rt-tr-group, table[id*='ClaimDocuments'] tr, .rt-tbody .rt-tr-group"));
        for (WebElement row : rows) {
            // Skip empty or pad rows if React Table
            String rowClass = row.getAttribute("class");
            if (rowClass != null && rowClass.contains("-padRow")) {
                continue;
            }
            String rowText = row.getText().toLowerCase();
            Iterator<String> iterator = remainingToFind.iterator();
            while (iterator.hasNext()) {
                String targetDesc = iterator.next();
                if (rowText.contains(targetDesc)) {
                    iterator.remove(); // Matched one occurrence of this document
                    break; // Move to next row so each row matches at most one item
                }
            }
            // If all expected documents have been matched on the screen, pass immediately
            if (remainingToFind.isEmpty()) {
                return true;
            }
        }
        Hooks.log("OSPC check: Still waiting on document(s) to appear on screen: " + remainingToFind);
        return false;
    }

    private Map<String, Integer> getDocumentCountsFromTable(java.util.Set<String> targetDescriptions) {
        Map<String, Integer> counts = new HashMap<>();
        for (String desc : targetDescriptions) {
            counts.put(desc, 0);
        }

        boolean hasNextPage = true;
        while (hasNextPage) {
            By docTableLocator = By.id("ClaimDocuments:DocumentsMemosScreen:DocumentsLV");
            try {
                WebElement docTable = getDriver().findElement(docTableLocator);
                List<WebElement> rows = docTable.findElements(By.tagName("tr"));
                String today = getCurrentDate("dd/MMM/yyyy").toLowerCase();

                for (WebElement row : rows) {
                    // Skip header rows
                    if (row.getAttribute("class").contains("lvhead")) {
                        continue;
                    }
                    
                    try {
                        String rowDescription = row.findElement(By.xpath(".//td[5]")).getText().toLowerCase();
                        String rowFinalDate = row.findElement(By.xpath(".//td[21]")).getText().toLowerCase();

                        if (rowFinalDate.trim().contains(today)) {
                            for (String targetDesc : targetDescriptions) {
                                if (rowDescription.contains(targetDesc.trim().toLowerCase())) {
                                    counts.put(targetDesc, counts.get(targetDesc) + 1);
                                }
                            }
                        }
                    } catch (NoSuchElementException | StaleElementReferenceException e) {
                        // Skip rows that don't have expected columns
                    }
                }

                // Handle Pagination: Look for the Next button specifically for this List View
                try {
                    // Based on provided HTML: find 'Next' link inside the paging container
                    List<WebElement> nextButtons = getDriver().findElements(By.xpath("//span[contains(@id, 'DocumentsLV:_ListPaging_container')]//a[text()='Next']"));
                    if (!nextButtons.isEmpty() && nextButtons.get(0).isDisplayed()) {
                        Click(nextButtons.get(0));
                        Sleep(3000); // Wait for Guidewire AJAX refresh
                    } else {
                        hasNextPage = false;
                    }
                } catch (NoSuchElementException e) {
                    hasNextPage = false; // No pagination controls found
                }

            } catch (TimeoutException e) {
                Hooks.log("Document table not found or timed out.");
                hasNextPage = false;
            }
        }
        return counts;
    }

    public boolean verifyLandedUNCDDocument(String author, String description) throws InterruptedException {
        int times = 2;
        Click(UnnumberedDocLink);
        new Select(HiddenDropdown).selectByValue("Yes");
        new Select(RedactionRequiredDropdown).selectByValue("Yes");
        new Select(NoAccessDropdown).selectByValue("Yes");
        Click(Search_link);
        Sleep(5000);

        boolean recordFound = false;
        if (prop.getProperty("DocuploadVerification").equalsIgnoreCase("Yes")) {
            times = Integer.parseInt(prop.getProperty("DocuploadLandingTime"));
        }

        By uncdTableLocator = By.id("WSIB_Form8_FNOLWizard:IndependentWizardStepSet:DocumentsMemosScreen:DocumentsLV");
        for (int i = 0; i < times; i++) {
            try {
                WebElement uncdTable = waitUntilElementVisible(uncdTableLocator);
                List<WebElement> rows = uncdTable.findElements(By.tagName("tr"));
                for (WebElement row : rows) {
                    String rowAuthor = row.findElement(By.xpath("./td[15]")).getText().toLowerCase();
                    String rowDescription = row.findElement(By.xpath("./td[5]")).getText().toLowerCase();
                    String rowFinalDate = row.findElement(By.xpath("./td[21]")).getText().toLowerCase();
                    if (rowDescription.contains(description.toLowerCase().trim()) && 
                        rowAuthor.contains(author.toLowerCase().trim()) && 
                        rowFinalDate.trim().contains(getCurrentDate("dd/MMM/yyyy").toLowerCase())) {
                        recordFound = true;
                        break;
                    }
                }
                if (recordFound) {
                    Hooks.log("UNCD document successfully verified in GWCC.");
                    break;
                } else {
                    Sleep(5000);
                    Hooks.log("Waiting for the latest UNCD document to land... Attempt: " + (i + 1));
                    Click(Search_link);
                    Sleep(5000);
                }
            } catch (StaleElementReferenceException e) {
                Hooks.log("Stale element encountered, retrying loop...");
            }
        }
        return recordFound;
    }

    public static final class NIHLDocumentExpectation {
        private final String author;
        private final String description;
        private final boolean exact;
        public NIHLDocumentExpectation(String author, String description, boolean exact) {
            if (author == null || author.trim().isEmpty()
                    || description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("NIHL expected author and description are required.");
            }
            this.author = author.trim();
            this.description = description.trim();
            this.exact = exact;
        }
        @Override
        public String toString() {
            return "{author='" + author + "', description='" + description
                    + "', match=" + (exact ? "exact" : "contains") + "}";
        }
    }

    public boolean verifyNIHLUNCDDocuments(List<NIHLDocumentExpectation> expected,
                                           java.time.LocalDate submittedOn,
                                           int timeoutSeconds, int pollSeconds,
                                           int maxPages) throws InterruptedException {
        if (expected == null || expected.isEmpty() || expected.contains(null)) {
            throw new IllegalArgumentException("At least one NIHL report expectation is required.");
        }
        if (submittedOn == null || submittedOn.isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("NIHL submission date is missing or in the future.");
        }
        if (timeoutSeconds <= 0 || pollSeconds <= 0 || maxPages <= 0) {
            throw new IllegalArgumentException("NIHL timeouts, poll interval and page limit must be positive.");
        }
        List<NIHLDocumentExpectation> requirements = new ArrayList<>(expected);
        List<NIHLDocumentExpectation> missing = new ArrayList<>(requirements);
        long started = System.nanoTime();
        long budget = java.time.Duration.ofSeconds(timeoutSeconds).toNanos();
        int attempt = 0;
        Click(UnnumberedDocLink);
        new Select(HiddenDropdown).selectByValue("Yes");
        new Select(RedactionRequiredDropdown).selectByValue("Yes");
        new Select(NoAccessDropdown).selectByValue("Yes");
        while (System.nanoTime() - started < budget) {
            attempt++;
            Hooks.log("[NIHL GWCC DOCUMENTS] Attempt " + attempt + "; elapsed="
                    + java.time.Duration.ofNanos(System.nanoTime() - started).getSeconds()
                    + "s; budget=" + timeoutSeconds + "s.");
            try {
                Click(Search_link);
                nihlDocumentWait(started, budget).until(
                        org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(NIHLUNCDTable));
                nihlResetUNCDToFirstPage(started, budget, maxPages);
                missing = nihlScanUNCDPages(requirements, submittedOn,
                        java.time.LocalDate.now(), started, budget, maxPages);
                if (missing.isEmpty()) {
                    Hooks.log("[NIHL GWCC DOCUMENTS] SUCCESS: all expected rows found under this UNCD record. "
                            + "Elapsed=" + java.time.Duration.ofNanos(System.nanoTime() - started).getSeconds() + "s.");
                    return true;
                }
                Hooks.log("[NIHL GWCC DOCUMENTS] Still waiting for: " + missing);
            } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {
                missing = new ArrayList<>(requirements);
                Hooks.log("[NIHL GWCC DOCUMENTS] Incomplete read; retrying. "
                        + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            long remaining = budget - (System.nanoTime() - started);
            if (remaining > 0) {
                long delay = Math.min(java.time.Duration.ofSeconds(pollSeconds).toMillis(),
                        java.time.Duration.ofNanos(remaining).toMillis());
                Sleep((int) Math.max(1L, Math.min(delay, Integer.MAX_VALUE)));
            }
        }
        Hooks.log("[NIHL GWCC DOCUMENTS] TIMEOUT: pending/unverified rows=" + missing);
        return false;
    }

    private List<NIHLDocumentExpectation> nihlScanUNCDPages(
            List<NIHLDocumentExpectation> expected,
            java.time.LocalDate fromDate, java.time.LocalDate toDate,
            long started, long budget, int maxPages) {
        List<NIHLDocumentExpectation> remaining = new ArrayList<>(expected);
        Set<String> visited = new HashSet<>();
        for (int page = 1; page <= maxPages; page++) {
            if (!visited.add(nihlUNCDSignature())) {
                throw new IllegalStateException("UNCD pagination repeated a table snapshot; refusing to count it twice.");
            }
            for (WebElement row : NIHLUNCDTable.findElements(By.xpath(".//tr"))) {
                if (!row.isDisplayed()) { continue; }
                List<WebElement> cells = row.findElements(By.xpath("./td"));
                if (cells.isEmpty()) { continue; }
                String firstClass = cells.get(0).getAttribute("class");
                if (firstClass != null && firstClass.contains("lvhead")) { continue; }
                if (cells.size() < 21) {
                    if (cells.size() > 1) {
                        throw new IllegalStateException("UNCD row layout changed: expected at least 21 cells, found "
                                + cells.size());
                    }
                    continue; // Empty-results/colspan row.
                }
                String description = nihlDocumentText(cells.get(4).getText());
                String author = nihlDocumentText(cells.get(14).getText());
                java.time.LocalDate finalDate = nihlDocumentDate(cells.get(20).getText());
                if (finalDate == null || finalDate.isBefore(fromDate) || finalDate.isAfter(toDate)) { continue; }
                // Prefer exact supporting-row matches before report contains-matches.
                for (boolean exactPass : new boolean[]{true, false}) {
                    boolean matched = false;
                    Iterator<NIHLDocumentExpectation> it = remaining.iterator();
                    while (it.hasNext()) {
                        NIHLDocumentExpectation item = it.next();
                        if (item.exact != exactPass) { continue; }
                        String expectedAuthor = nihlDocumentText(item.author);
                        String expectedDescription = nihlDocumentText(item.description);
                        boolean matches = item.exact
                                ? author.equals(expectedAuthor) && description.equals(expectedDescription)
                                : author.contains(expectedAuthor) && description.contains(expectedDescription);
                        if (matches) {
                            Hooks.log("[NIHL GWCC DOCUMENTS] Found " + item + " on page " + page);
                            it.remove();
                            matched = true;
                            break;
                        }
                    }
                    if (matched) { break; } // One row satisfies at most one expected occurrence.
                }
            }
            if (remaining.isEmpty()) { return remaining; }
            WebElement next = nihlPagingControl("Next");
            if (next == null) { return remaining; }
            if (page == maxPages) {
                throw new IllegalStateException("NIHLMaxDocumentPages=" + maxPages
                        + " reached while Next is enabled. Coverage is incomplete.");
            }
            nihlMoveUNCDPage(next, started, budget);
        }
        return remaining;
    }

    private void nihlResetUNCDToFirstPage(long started, long budget, int maxPages) {
        WebElement pager = nihlVisibleUNCDPager();
        if (pager == null) { return; }
        WebElement first = nihlPagingControl("First");
        if (first != null) {
            nihlMoveUNCDPage(first, started, budget);
            return;
        }
        Set<String> visited = new HashSet<>();
        for (int move = 0; move < maxPages; move++) {
            WebElement previous = nihlPagingControl("Previous", "Prev");
            if (previous == null) {
                pager = nihlVisibleUNCDPager();
                String text = pager == null ? "" : pager.getText();
                boolean hasBackLabel = pager != null && !pager.findElements(By.xpath(
                        ".//*[normalize-space(.)='First' or normalize-space(.)='Previous' "
                                + "or normalize-space(.)='Prev']")).isEmpty();
                boolean firstRange = java.util.regex.Pattern.compile(
                                "(?i)(?:\\bpage\\s+1\\s+of\\s+\\d+\\b|\\b1\\s*[-\\u2013]\\s*\\d+\\s+of\\s+\\d+\\b)")
                        .matcher(text).find();
                if (hasBackLabel || firstRange) { return; }
                throw new IllegalStateException("Cannot establish the first NIHL document page. "
                        + "Supply its pagination HTML rather than assuming Search reset it.");
            }
            if (!visited.add(nihlUNCDSignature())) {
                throw new IllegalStateException("NIHL backward pagination did not advance.");
            }
            nihlMoveUNCDPage(previous, started, budget);
        }
        throw new IllegalStateException("Cannot reset NIHL pagination within NIHLMaxDocumentPages.");
    }

    private WebElement nihlVisibleUNCDPager() {
        WebElement visible = null;
        for (WebElement pager : NIHLUNCDPagers) {
            if (pager.isDisplayed()) {
                if (visible != null) {
                    throw new IllegalStateException("More than one NIHL document pager matched.");
                }
                visible = pager;
            }
        }
        return visible;
    }

    private WebElement nihlPagingControl(String... labels) {
        WebElement pager = nihlVisibleUNCDPager();
        if (pager == null) { return null; }
        for (WebElement control : pager.findElements(By.xpath(".//a | .//button"))) {
            for (String label : labels) {
                if (label.equalsIgnoreCase(control.getText().trim()) && nihlPagingEnabled(control)) {
                    return control;
                }
            }
        }
        return null;
    }

    private boolean nihlPagingEnabled(WebElement control) {
        if (!control.isDisplayed() || !control.isEnabled()
                || control.getAttribute("disabled") != null
                || "true".equalsIgnoreCase(control.getAttribute("aria-disabled"))) { return false; }
        String classes = control.getAttribute("class");
        if (classes != null && classes.toLowerCase(Locale.ROOT).contains("disabled")) { return false; }
        WebElement parent = control.findElement(By.xpath(".."));
        String parentClasses = parent.getAttribute("class");
        return !"true".equalsIgnoreCase(parent.getAttribute("aria-disabled"))
                && (parentClasses == null || !parentClasses.toLowerCase(Locale.ROOT).contains("disabled"));
    }

    private void nihlMoveUNCDPage(WebElement control, long started, long budget) {
        final String before = nihlUNCDSignature();
        control.click();
        nihlDocumentWait(started, budget).until(driver -> {
            try { return !before.equals(nihlUNCDSignature()); }
            catch (NoSuchElementException | StaleElementReferenceException e) { return false; }
        });
    }

    private String nihlUNCDSignature() {
        String html = NIHLUNCDTable.getAttribute("innerHTML");
        if (html == null) { throw new IllegalStateException("Cannot read the NIHL table snapshot."); }
        return html;
    }

    private WebDriverWait nihlDocumentWait(long started, long budget) {
        long remaining = budget - (System.nanoTime() - started);
        if (remaining <= 0) { throw new TimeoutException("NIHL document polling deadline reached."); }
        return new WebDriverWait(Hooks.getDriver(), java.time.Duration.ofNanos(
                Math.min(remaining, java.time.Duration.ofSeconds(10).toNanos())));
    }

    private String nihlDocumentText(String text) {
        return text.replace('\u00a0', ' ').trim().replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }

    private java.time.LocalDate nihlDocumentDate(String text) {
        java.util.regex.Matcher match = java.util.regex.Pattern.compile(
                "\\b\\d{2}/[A-Za-z]{3}/\\d{4}\\b").matcher(text);
        if (!match.find()) { return null; }
        return java.time.LocalDate.parse(match.group(),
                new java.time.format.DateTimeFormatterBuilder().parseCaseInsensitive()
                        .appendPattern("dd/MMM/uuuu").toFormatter(Locale.ENGLISH));
    }


}
