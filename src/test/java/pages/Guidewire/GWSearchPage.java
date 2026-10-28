package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.JsonUtils;

import javax.swing.text.Utilities;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GWSearchPage extends BaseDriverClass {
    @FindBy(id = "TabBar:SearchTab")
    private WebElement SearchTab;
    @FindBy(id = "DesktopActivities")
    private WebElement DesktopActivities;
    @FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimNumber")
    private WebElement ClaimSearchField;
    @FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search_link")
    private WebElement SearchButton;
    @FindBy(xpath = "//div[contains(@id, 'ResultsLV:0:ClaimNumber')] | //a[contains(@id, 'ResultsLV:0:ClaimNumber')] | //div[contains(@id, 'ClaimSearch')]//table//tr[1]//td[contains(@class, 'gw-action')]")
    private WebElement SelectClaimfromSearch;
    @FindBy(id = "ClaimSummary:ClaimSummaryScreen:ttlBar")
    private WebElement SummaryTitlebar;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ttlBar")
    private WebElement ClaimRegistrationTitlebar;
    @FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:FirstName")
    private WebElement SearchFirstName;
    @FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:LastName")
    private WebElement SearchLastName;
    @FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:DateOfBirth")
    private WebElement SearchDOB;
    @FindBy(id = "Claim:ClaimInfoBar:originatingChannel_link")
    private WebElement OriginatingChannel;

    @FindBy(xpath = "//table[contains(@id, 'ClaimSearch') and contains(@id, 'ResultsLV')]//a[substring(@id, string-length(@id) - string-length(':ClaimNumber') + 1) = ':ClaimNumber']")
    private List<WebElement> NIHLClaimResultLinks;

    @FindBy(id = "WSIB_Form8_FNOLWizard:Documents")
    private WebElement NIHLUnnumberedDocumentsLink;

    public GWSearchPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean searchClaim(String claimnumber) {
        Click(SearchTab);
        Enter(ClaimSearchField, claimnumber);
        Click(SearchButton);
        Click(SelectClaimfromSearch);
        return SummaryTitlebar.isDisplayed();
    }

    public boolean searchReference(String referenceNumber) {
        Click(SearchTab);
        Enter(ClaimSearchField, referenceNumber);
        Click(SearchButton);
        Click(SelectClaimfromSearch);
        return ClaimRegistrationTitlebar.isDisplayed();
    }

    public boolean GWCCLoginsuccess() {
        waitUntilElementVisible(DesktopActivities);
        return DesktopActivities.isDisplayed();
    }

    public boolean searchClaimant(
            String firstname,
            String lastname,
            String dateofbirth) throws InterruptedException {

        // Validate before interacting with the application.
        if (firstname == null || firstname.isBlank()
                || lastname == null || lastname.isBlank()
                || dateofbirth == null || dateofbirth.isBlank()) {

            throw new IllegalArgumentException(
                    "First name, last name, and date of birth are required."
            );
        }

        boolean extendedVerification = "Yes".equalsIgnoreCase(
                Hooks.getConfigReader().getProperty("DocuploadVerification")
        );

        int maxAttempts = extendedVerification ? 50 : 2;
        Duration resultTimeout = Duration.ofSeconds(5);
        long startedAt = System.nanoTime();

        dateofbirth  = convertDate(dateofbirth, "DD/MM/YYYY");

        WebDriverWait resultWait = new WebDriverWait(
                Hooks.getDriver(), resultTimeout
        );
        resultWait.pollingEvery(Duration.ofMillis(300));

        Hooks.log(
                "[GWCC SEARCH] Starting claimant search."
                        + " Maximum attempts: " + maxAttempts
                        + "; result wait per attempt: "
                        + resultTimeout.toSeconds() + " seconds."
        );

        Hooks.log("[GWCC SEARCH] Opening Search and entering criteria.");

        Click(SearchTab);
        Enter(SearchFirstName, firstname.trim());
        Enter(SearchLastName, lastname.trim());
        Enter(SearchDOB, dateofbirth.trim());

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            Hooks.log(
                    "[GWCC SEARCH] Attempt " + attempt + "/" + maxAttempts
                            + ": submitting search."
            );

            // Submit again on every attempt.
            // Submission errors propagate instead of being hidden as "not found".
            Click(SearchButton);

            try {
                String claimNumber = resultWait.until(driver -> {
                    try {
                        if (!SelectClaimfromSearch.isDisplayed()
                                || !SelectClaimfromSearch.isEnabled()) {
                            return null;
                        }

                        String number =
                                SelectClaimfromSearch.getText().trim();

                        if (number.isEmpty()) {
                            return null;
                        }

                        // Use a direct click here to avoid nesting the waits
                        // and retries inside your custom Click() helper.
                        SelectClaimfromSearch.click();

                        // Success is returned only after the click succeeds.
                        return number;

                    } catch (NoSuchElementException
                             | StaleElementReferenceException
                             | ElementNotInteractableException e) {

                        // The result may not exist yet, may be refreshing,
                        // or may be temporarily unavailable for clicking.
                        return null;
                    }
                });

                long elapsedSeconds = Duration.ofNanos(
                        System.nanoTime() - startedAt
                ).toSeconds();

                Hooks.log(
                        "[GWCC SEARCH] SUCCESS: clicked claim " + claimNumber
                                + " on attempt " + attempt + "/" + maxAttempts
                                + ". Total elapsed: " + elapsedSeconds + " seconds."
                );

                return true;

            } catch (TimeoutException e) {

                Hooks.log(
                        "[GWCC SEARCH] Attempt " + attempt + "/" + maxAttempts
                                + ": no usable claim result could be clicked within "
                                + resultTimeout.toSeconds() + " seconds."
                );

                if (attempt < maxAttempts) {
                    Hooks.log(
                            "[GWCC SEARCH] Retrying: submitting the search again."
                    );
                }
            }
        }

        long elapsedSeconds = Duration.ofNanos(
                System.nanoTime() - startedAt
        ).toSeconds();

        Hooks.log(
                "[GWCC SEARCH] FAILED: no claim result was successfully clicked"
                        + " after " + maxAttempts + " attempts."
                        + " Total elapsed: " + elapsedSeconds + " seconds."
        );

        return false;
    }

    public boolean searchDIClaim(String firstname, String lastname, String dateofbirth) throws InterruptedException {
        Hooks.log("Searching DI Claim: " + firstname + " " + lastname + ", DOB: " + dateofbirth);
        Click(SearchTab);
        Enter(SearchFirstName, firstname);
        Enter(SearchLastName, lastname);
        jsSendKeys(SearchDOB, dateofbirth);
        for (int loop = 0; loop < 50; loop++) {
            Click(SearchButton);
            Sleep(2000);
            if (isDisplayed(SelectClaimfromSearch)) {
                if (SelectClaimfromSearch.getText().charAt(0) == '3') {
                    Hooks.log("Claim found in GWCC: " + SelectClaimfromSearch.getText());
                    Map<String, String> currentRowData = Hooks.currentRow.get();
                    currentRowData.put("Claim Number", SelectClaimfromSearch.getText());
                    JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());
                    Hooks.currentRow.set(currentRowData);
                    Click(SelectClaimfromSearch);
                    break;
                } else {
                    Hooks.log("Waiting for Auto Registration to Complete... current value: " + SelectClaimfromSearch.getText());
                }
            } else {
                Hooks.log("Search Results not available Yet, trying again... Attempt: " + loop);
                Sleep(5000);
                Click(SearchTab);
            }
        }
        waitUntilElementVisible(OriginatingChannel);
        return OriginatingChannel.isDisplayed();
    }

    public boolean searchNIHLClaimant(String firstName, String lastName, String dob,
                                      int timeoutSeconds, int pollSeconds)
            throws InterruptedException {
        if (firstName == null || firstName.trim().isEmpty()
                || lastName == null || lastName.trim().isEmpty()
                || dob == null || dob.trim().isEmpty()) {
            throw new IllegalArgumentException("NIHL claimant name and DOB are required.");
        }
        if (timeoutSeconds <= 0 || pollSeconds <= 0) {
            throw new IllegalArgumentException("NIHL timeout and poll seconds must be positive.");
        }
        if (Hooks.currentRow.get() != null) {
            Hooks.currentRow.get().remove("NIHLGWCCReference");
        }
        final String expectedName = nihlSearchText(firstName + " " + lastName);
        final java.time.LocalDate expectedDob = java.time.LocalDate.parse(dob.trim(),
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/uuuu"));
        long started = System.nanoTime();
        long budget = Duration.ofSeconds(timeoutSeconds).toNanos();
        int attempt = 0;
        while (System.nanoTime() - started < budget) {
            attempt++;
            Hooks.log("[NIHL GWCC SEARCH] Attempt " + attempt + "; elapsed="
                    + Duration.ofNanos(System.nanoTime() - started).getSeconds()
                    + "s; budget=" + timeoutSeconds + "s.");
            Click(SearchTab);
            ClaimSearchField.clear();
            Enter(SearchFirstName, firstName.trim());
            Enter(SearchLastName, lastName.trim());
            jsSendKeys(SearchDOB, dob.trim());
            Click(SearchButton);
            long remaining = budget - (System.nanoTime() - started);
            if (remaining <= 0) {
                break;
            }
            WebDriverWait resultWait = new WebDriverWait(Hooks.getDriver(),
                    Duration.ofNanos(Math.min(remaining, Duration.ofSeconds(5).toNanos())));
            resultWait.pollingEvery(Duration.ofMillis(300));
            WebElement selected;
            try {
                selected = resultWait.until(driver -> {
                    try {
                        List<WebElement> matches = new ArrayList<>();
                        for (WebElement link : NIHLClaimResultLinks) {
                            if (!link.isDisplayed() || !link.isEnabled()) {
                                continue;
                            }
                            WebElement row = link.findElement(By.xpath("ancestor::tr[1]"));
                            String actualName = row.findElement(
                                    By.cssSelector("span[id$=':Claimant']")).getText();
                            String actualDobText = row.findElement(
                                    By.cssSelector("span[id$=':WorkerDateOfBirth']")).getText().trim();
                            java.time.LocalDate actualDob = java.time.LocalDate.parse(actualDobText,
                                    new java.time.format.DateTimeFormatterBuilder()
                                            .parseCaseInsensitive().appendPattern("dd/MMM/uuuu")
                                            .toFormatter(Locale.ENGLISH));
                            if (expectedName.equals(nihlSearchText(actualName))
                                    && expectedDob.equals(actualDob)
                                    && !link.getText().trim().isEmpty()) {
                                matches.add(link);
                            }
                        }
                        if (matches.size() > 1) {
                            throw new IllegalStateException("Multiple GWCC records match this NIHL claimant. "
                                    + "Use a submission/reference identifier; refusing to select the first record.");
                        }
                        return matches.isEmpty() ? null : matches.get(0);
                    } catch (StaleElementReferenceException e) {
                        return null;
                    } catch (NoSuchElementException e) {
                        throw new IllegalStateException(
                                "NIHL search results do not match the supplied classic table layout.", e);
                    }
                });
            } catch (TimeoutException e) {
                Hooks.log("[NIHL GWCC SEARCH] Matching claimant is not visible yet.");
                nihlSearchPause(started, budget, pollSeconds);
                continue;
            }
            String reference;
            try {
                reference = selected.getText().trim();
                selected.click();
            } catch (StaleElementReferenceException | ElementNotInteractableException e) {
                Hooks.log("[NIHL GWCC SEARCH] Result changed before the click; retrying search.");
                nihlSearchPause(started, budget, pollSeconds);
                continue;
            }
            remaining = budget - (System.nanoTime() - started);
            if (remaining <= 0) {
                return false;
            }
            try {
                new WebDriverWait(Hooks.getDriver(), Duration.ofNanos(
                        Math.min(remaining, Duration.ofSeconds(15).toNanos())))
                        .until(org.openqa.selenium.support.ui.ExpectedConditions
                                .visibilityOf(NIHLUnnumberedDocumentsLink));
            } catch (TimeoutException e) {
                Hooks.log("[NIHL GWCC SEARCH] Clicked reference " + reference
                        + ", but the UNCD Documents link did not become visible. "
                        + "Another screen may have opened; no landing success is reported.");
                return false;
            }
            if (Hooks.currentRow.get() != null) {
                Hooks.currentRow.get().put("NIHLGWCCReference", reference);
            }
            Hooks.log("[NIHL GWCC SEARCH] SUCCESS: matched identity and opened UNCD reference "
                    + reference + ". Elapsed="
                    + Duration.ofNanos(System.nanoTime() - started).getSeconds() + "s.");
            return true;
        }
        Hooks.log("[NIHL GWCC SEARCH] TIMEOUT: matching NIHL claimant was not opened.");
        return false;
    }

    private String nihlSearchText(String text) {
        return text.replace('\u00a0', ' ').trim().replaceAll("\\s+", " ")
                .toLowerCase(Locale.ROOT);
    }

    private void nihlSearchPause(long started, long budget, int pollSeconds)
            throws InterruptedException {
        long remaining = budget - (System.nanoTime() - started);
        if (remaining > 0) {
            long delay = Math.min(Duration.ofSeconds(pollSeconds).toMillis(),
                    Duration.ofNanos(remaining).toMillis());
            Sleep((int) Math.max(1L, Math.min(delay, Integer.MAX_VALUE)));
        }
    }

    }
