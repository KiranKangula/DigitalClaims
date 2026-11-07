package pages.OSPC;

import definitions.Hooks;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import pages.GuidewireClaimCenter.GuidewirePage;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class OSPCClaimOverview extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();
	public static String randommessage;
	private WebDriverWait wait; // For explicit waits
	@FindBy(xpath = "//*[@id='mainHeading']") private WebElement myclaimoverview;
	@FindBy(xpath = "//*[@id=\"benefitPaymentLink\"]/a/span")
	private WebElement benefitpaymentslink;
	@FindBy(id = "benefitPaymentHeading")
	private WebElement benefitpaymentsheading;
	@FindBy(xpath = "//*[@id=\"healthCareBenefitslink\"]/a/span")
	private WebElement healthcarebenefitslink;
	@FindBy(id = "mainHeading")
	private WebElement healthcarebenefitsheading;
	@FindBy(xpath = "//*[@id=\"nonHealthCareBenefitslink\"]/a/span")
	private WebElement nonhealthcarebenefitslink;
	@FindBy(id = "pageHeadingLabel")
	private WebElement nonhealthcarebenefitsheading;
	@FindBy(xpath = "//*[@id=\"appealsLink\"]/a/span")
	private WebElement appealslink;
	@FindBy(id = "mainHeading")
	private WebElement appealsheading;
	@FindBy(xpath = "//*[@id=\"DistributionPopUpNo\"]/div")
	private WebElement ModelWindow;
	@FindBy(xpath = "//*[@id=\"claimDocumentsLink\"]/a/span")
	private WebElement submitdocumentslink;
	@FindBy(xpath = "//h1[contains(text(),'Submit document(s)')]")
	private WebElement submitdocumentsheading;
	@FindBy(xpath = "//*[@id=\"docSource0\"]/div[1]/label")
	private WebElement OSPCWsibFormYes;
	@FindBy(xpath = "//*[@id=\"termOfUseCheckbox\"]/div/div/div[1]/span")
	private WebElement OSPCConfirmCheckbox;
	@FindBy(id = "wsibformselection-wrapper")
	private WebElement OSPCWSIBformDropdown;
	@FindBy(xpath = "//span[(text()='Submit')]")
	private WebElement OSPCDocumentSubmit;
	@FindBy(xpath = "//h2[contains(text(),'Confirmation')]")
	private WebElement submitdocumentsconfirmation;
	@FindBy(xpath = "//span[contains(text(),'Close')]")
	private WebElement CloseButton;
	@FindBy(xpath = "//*[@id=\"claimDocumentHistoryLink\"]/a/span") private WebElement DocumentHistoryLink;
	@FindBy(id = "submittedDocumentsHeading") private WebElement submittedDocumentsHeading;
	@FindBy(xpath = "//*[@id=\"advanceSearchAccordionCard_209711427\"]/div/div/div[2]/h4") private WebElement SearchOptionExpand;

	@FindBy(xpath = "//*[@id=\"documentCategoryDropdown-wrapper\"]/div/div[1]/div[1]") private WebElement DocumentCategoryFilter;
	@FindBy(id = "documentDescriptionLabelInput") private WebElement DocumentDescriptionFilter;
	@FindBy(id = "fromDate") private WebElement PostedDateFromFilter;
	@FindBy(id = "toDate") private WebElement PostedDateToFilter;
	@FindBy(id = "SearchButton") private WebElement SearchButtonFilter;

	@FindBy(xpath = "//*[@id='SubmittedDocumentsDataTable']/div/div[1]/div[2]/div[1]/div/div[1]") private WebElement DocumentCategory;
	@FindBy(xpath = "//*[@id='SubmittedDocumentsDataTable']/div/div[1]/div[2]/div[1]/div/div[2]") private WebElement DocumentDescription;
	@FindBy(xpath = "//*[@id='SubmittedDocumentsDataTable']/div/div[1]/div[2]/div[1]/div/div[3]") private WebElement DocumentDate;

	@FindBy(css = "button.jut__Pagination__paginationSelect") private WebElement dropdownBtnLocator;
	@FindBy(css = "a.jut__Pagination__paginationMenuItem[data-code='All']") private WebElement showAllItem;
	@FindBy(css = "a.jut__Pagination__paginationMenuItem[data-code='100']") private WebElement hundredItem;


	@FindBy(xpath = "\t//*[@id=\"sendUsMessageLink\"]/a/span") private WebElement SendusmessageLink;
	@FindBy(id = "secondaryHeading")
	private WebElement SendusmessageHeader;
	@FindBy(id = "ddlSubject-wrapper")
	private WebElement SubjectDropdown;
	@FindBy(id = "txtBody")
	private WebElement txtBody;
	@FindBy(xpath = "//*[@id=\"btnSend\"]/span/span")
	private WebElement SendButton;
	@FindBy(xpath = "//*[@id=\"returnToMailLink\"]/span")
	private WebElement ReturntoMail;
	@FindBy(id = "mailListPageHeading")
	private WebElement mailListPageHeading;
	@FindBy(xpath = "//*[@id=\"mailListDatatable\"]/div/div[1]/div[2]/div[1]/div/div[4]/div")
	private WebElement SentMessageBody;
	@FindBy(xpath = "//*[@id=\"addClaimButton\"]/span/span")
	private WebElement AddClaimButton;
	@FindBy(id = "claimNumberInput")
	private WebElement ClaimNumberInput;
	@FindBy(id = "yearOfBirthInput")
	private WebElement yearOfBirthInput;
	@FindBy(id = "personalIdentificationInput")
	private WebElement personalIdentificationInput;
	@FindBy(xpath = "//*[@id=\"termOfUseCheckBox\"]/div/div/div/div[1]/span")
	private WebElement termOfUseCheckBox;
	@FindBy(id = "LinknextButton") private WebElement NextButton;
	@FindBy(id = "InjuryDate") private WebElement InjuryDateQuestion;
	@FindBy(id = "InjuryArea") private WebElement injuryAreaContainer;
	@FindBy(id = "PostalCode") private WebElement PostalCodeQuestion;
	@FindBy(id = "EmployerName_MGICNBB FPKIAMJKRR ITT") private WebElement EmployerNameQuestion;
	@FindBy(id = "PhoneNumber") private WebElement PhoneNumberQuestion;

	@FindBy(id = "nextButton") private WebElement SubmitButton;
	@FindBy(id = "pageHeadingLabel")  private WebElement pageHeadingLabel;
	@FindBy(id = "verifyCard")  private WebElement verifyCard;
	@FindBy(xpath = "//*[@id=\"Link\"]/span") private WebElement returnToClaimsHomelLink;
	@FindBy(id = "switchClaimLink")  private WebElement switchClaimLink;

	@FindBy(id = "viewClaimLink")
	private WebElement viewClaimLink;

	@FindBy(css = ".rt-tbody")
	private WebElement tableBody;
	@FindBy(id = "isDuplicateClaimMsg")
	private WebElement isDuplicateClaimMsg;
	@FindBy(id = "trackAppealHeading")
	private WebElement trackAppealHeading;
	@FindBy(xpath = "//button[contains(@id, 'paginationSelectDropdownMenu-statusAppealDatatable')]")
	private WebElement paginationSelectDropdownMenu;
	@FindBy(xpath = "//a[@data-code='All']")
	private WebElement ShowAll;


	public OSPCClaimOverview(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		this.wait = wait; // Initialize the wait field
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnClaimOverview() {
		waitUntilElementVisible(myclaimoverview);
		if (ModelWindow.isDisplayed()) {
			Click(ModelWindow);
		}
		Assert.assertEquals(myclaimoverview.getText(), "My claims overview");
		return myclaimoverview.isDisplayed();
	}

	public Boolean verifyOnBenefitpayments() {
		jsClick(benefitpaymentslink);
		waitUntilElementVisible(benefitpaymentsheading);
		Assert.assertEquals(benefitpaymentsheading.getText(), "Benefit payments");
		return benefitpaymentsheading.isDisplayed();
	}

	public Boolean verifyOnHealthcarebenefits() {
		jsClick(healthcarebenefitslink);
		waitUntilElementVisible(healthcarebenefitsheading);
		Assert.assertEquals(healthcarebenefitsheading.getText(), "Health care benefits");
		return healthcarebenefitsheading.isDisplayed();
	}

	public Boolean verifyOnNonHealthcarebenefits() {
		jsClick(nonhealthcarebenefitslink);
		waitUntilElementVisible(nonhealthcarebenefitsheading);
		Assert.assertEquals(nonhealthcarebenefitsheading.getText(), "Non-health care benefits");
		return nonhealthcarebenefitsheading.isDisplayed();
	}

	public Boolean verifyOnAppeals() {
		jsClick(appealslink);
		waitUntilElementVisible(appealsheading);
		Assert.assertEquals(appealsheading.getText(), "Appeals");
		return appealsheading.isDisplayed();
	}

	public int verifyCountOfAppeals() {
		jsClick(appealslink);
		waitUntilElementVisible(appealsheading);
		Assert.assertEquals(appealsheading.getText(), "Appeals");

		if (isDisplayed(trackAppealHeading)) {
			Click(paginationSelectDropdownMenu);
			waitUntilElementVisible(ShowAll);
			Click(ShowAll);
			List<WebElement> rows = getDriver().findElements(By.cssSelector(".rt-tbody .rt-tr-group"));
			System.out.println("Number of appeals: " + rows.size());
			return rows.size();
		} else {
			System.out.println("No appeals found.");
			return 0;
		}

	}

	public Boolean verifyOnSubmitDocuments() {
		jsClick(submitdocumentslink);
		waitUntilElementVisible(submitdocumentsheading);
		Assert.assertEquals(submitdocumentsheading.getText(), "Submit document(s)");
		return submitdocumentsheading.isDisplayed();
	}

	public List<String> uploadDocuments(String filenamesString) throws IOException {
		List<String> originalFilenames = Arrays.stream(filenamesString.split(","))
				.map(String::trim)
				.collect(Collectors.toList());

		List<String> timestampedAbsolutePaths = new ArrayList<>();
		List<String> timestampedFilenames = new ArrayList<>();

		// Generate a unique timestamp for the batch
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));

		for (int i = 0; i < originalFilenames.size(); i++) {
			String originalFilename = originalFilenames.get(i);
			Path originalFilePath = Paths.get("src/test/resources/DocUploadFiles", originalFilename);

			// Extract filename without extension and its extension
			String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
			String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

			// Create new filename with timestamp and index to ensure uniqueness within the batch
			String newFilename = fileNameWithoutExtension + "_" + timestamp + "_" + i + fileExtension;
			Path newFilePath = Paths.get("src/test/resources/DocUploadFiles", newFilename);

			// Copy the original file to the new timestamped file path, overwriting if exists
			Files.copy(originalFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Copied original file '" + originalFilename + "' to '" + newFilename + "'");

			timestampedAbsolutePaths.add(newFilePath.toAbsolutePath().toString());
			timestampedFilenames.add(newFilename);
		}

		WebElement input = getDriver().findElement(By.cssSelector("input[type='file']"));
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.display = 'block';", input);

		// Join the timestamped absolute paths with newline characters for multiple file upload
		String filePaths = String.join("\n", timestampedAbsolutePaths);
		input.sendKeys(filePaths);

		waitUntilElementVisible(OSPCWsibFormYes);
		// Return the list of timestamped filenames for further use (e.g., updating GWCC.json)
		return timestampedFilenames;
	}

	public Boolean selectWSIBForm(String wsibFormsString) {
		List<String> wsibFormSelections = Arrays.stream(wsibFormsString.split("\\|"))
				.map(String::trim)
				.collect(Collectors.toList());

		// Find all the file item containers.
		// This assumes the order of these containers in the DOM matches the order of files uploaded.
		List<WebElement> fileItemContainers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".fileItemContainer")));

		if (fileItemContainers.size() != wsibFormSelections.size()) {
			throw new AssertionError("Mismatch between number of uploaded files and WSIB form selections provided in test data.");
		}

		for (int i = 0; i < fileItemContainers.size(); i++) {
			String currentWsibFormSelection = wsibFormSelections.get(i);
			WebElement currentFileContainer = fileItemContainers.get(i);

			// Construct the ID for the radio button group based on its index
			String radioGroupId = "docSource" + i;

			// Locate the "Yes" and "No" radio buttons within the current file's container
			WebElement yesRadioButton = currentFileContainer.findElement(By.id(radioGroupId + "_yes"));
			WebElement noRadioButton = currentFileContainer.findElement(By.id(radioGroupId + "_no"));

			// Determine whether to select "Yes" or "No"
			// We consider it a WSIB form if the selection string is not empty, "No", or "N/A"
			if (!currentWsibFormSelection.isEmpty() &&
					!currentWsibFormSelection.equalsIgnoreCase("No") &&
					!currentWsibFormSelection.equalsIgnoreCase("N/A")) {

				// Select "Yes"
				if (!yesRadioButton.isSelected()) {
					Click(yesRadioButton);
					System.out.println("Selected 'Yes' for file at index " + i + " (WSIB Form: " + currentWsibFormSelection + ")");
				}

				// IMPORTANT: If selecting "Yes" makes a *dropdown* appear to select the specific WSIB form type,
				// you would add the logic here to interact with that dropdown.
				// Example (uncomment and adapt if needed):

                WebElement wsibFormTypeDropdown = currentFileContainer.findElement(By.xpath(".//*[@id='wsibformselection-wrapper']"));
                Select(wsibFormTypeDropdown, currentWsibFormSelection);
                System.out.println("Selected WSIB form type: " + currentWsibFormSelection);


			} else {
				// Select "No"
				if (!noRadioButton.isSelected()) {
					Click(noRadioButton);
					System.out.println("Selected 'No' for file at index " + i);
				}
			}
		}
      return true;
    }



	public Boolean SubmitDocument() throws InterruptedException {
		Click(OSPCConfirmCheckbox);
		Click(OSPCDocumentSubmit);
		try{
			if(CloseButton.isDisplayed()) {
				Click(CloseButton);
				Sleep(10000);
				Click(OSPCConfirmCheckbox);
				Click(OSPCDocumentSubmit);
			}
		}
		catch(NoSuchElementException ignored)
		{}
		waitUntilElementVisible(submitdocumentsconfirmation);
		return submitdocumentsconfirmation.isDisplayed();

	}

	/**
	 * Validates document history for single and multiple file uploads.
	 * Uses pipe delimiter ('|') for descriptions and categories to safely allow
	 * commas inside descriptions (e.g., "Dental report, treatment plan | Doctor note").
	 * Captures initial baseline counts across pagination before polling.
	 *
	 * @param filenamesString     Comma-separated filenames for cleanup (e.g., "report.pdf, note.png")
	 * @param documentCategories  Pipe-separated (or comma-separated) categories matching the files
	 * @param documentDescription Pipe-separated descriptions ('|') matching the files
	 */
	public Boolean DocumentHistory(String filenamesString, String documentCategories, String documentDescription) {
		// 1. Navigate to page
		Click(DocumentHistoryLink);
		waitUntilElementVisible(submittedDocumentsHeading);
		// 2. Parse expected files using pipe delimiter ('|')
		List<String> expectedDescriptions = (documentDescription != null && !documentDescription.trim().isEmpty())
				? Arrays.stream(documentDescription.split("\\|"))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.toList()
				: Collections.emptyList();
		// Parse categories using '|' if present, otherwise fallback to ','
		List<String> categoriesList = Collections.emptyList();
		if (documentCategories != null && !documentCategories.trim().isEmpty()) {
			String catDelimiter = documentCategories.contains("|") ? "\\|" : ",";
			categoriesList = Arrays.stream(documentCategories.split(catDelimiter))
					.map(String::trim)
					.filter(s -> !s.isEmpty())
					.toList();
		}
		// Map each submitted document to a DocumentKey and count occurrences in this run
		Map<DocumentKey, Integer> newlySubmittedCounts = new LinkedHashMap<>();
		for (int i = 0; i < expectedDescriptions.size(); i++) {
			String desc = expectedDescriptions.get(i);
			String cat = "";
			if (!categoriesList.isEmpty()) {
				cat = (i < categoriesList.size()) ? categoriesList.get(i) : categoriesList.get(0);
			}
			DocumentKey key = new DocumentKey(desc, cat);
			newlySubmittedCounts.put(key, newlySubmittedCounts.getOrDefault(key, 0) + 1);
		}
		// Determine UI filter inputs (populate only if uniform across all submitted items)
		boolean isSingleCategory = !categoriesList.isEmpty() && new HashSet<>(categoriesList).size() == 1;
		String filterCategory = isSingleCategory ? categoriesList.get(0) : "";
		boolean isSingleDescription = (new HashSet<>(expectedDescriptions).size() == 1);
		String filterDescription = isSingleDescription ? expectedDescriptions.get(0) : "";
		String todayFilterDate = getCurrentDate("yyyy-MM-dd"); // For input filters
		String todayDisplayDate = getCurrentDate("dd MMM yyyy"); // E.g., "06 Oct 2026"
		// 3. Apply initial filter to establish baseline count
		applySearchFilters(filterCategory, filterDescription, todayFilterDate);
		// 4. Capture INITIAL baseline counts across all pages
		System.out.println("Reading baseline document counts across all pages...");
		Map<DocumentKey, Integer> initialCounts = getMatchingDocumentCountsAcrossPages(newlySubmittedCounts.keySet(), todayDisplayDate);
		// Target count needed to pass = initial baseline count + newly submitted count
		Map<DocumentKey, Integer> targetCounts = new LinkedHashMap<>();
		for (Map.Entry<DocumentKey, Integer> entry : newlySubmittedCounts.entrySet()) {
			DocumentKey key = entry.getKey();
			int initial = initialCounts.getOrDefault(key, 0);
			int needed = initial + entry.getValue();
			targetCounts.put(key, needed);
			System.out.println(String.format("Baseline for %s -> Existing count: %d, Newly submitted: %d, Target needed: %d",
					key, initial, entry.getValue(), needed));
		}
		// 5. Polling setup: up to 12 minutes (24 attempts x 30s)
		int maxAttempts = 24;
		int pollIntervalMillis = 30000;
		for (int attempt = 1; attempt <= maxAttempts; attempt++) {
			System.out.println("Attempt " + attempt + " of " + maxAttempts + " checking for new documents...");
			try {
				// Read current counts across all pages
				Map<DocumentKey, Integer> currentCounts = getMatchingDocumentCountsAcrossPages(newlySubmittedCounts.keySet(), todayDisplayDate);
				// Verify if all document requirements have reached their target count
				boolean allMet = true;
				for (Map.Entry<DocumentKey, Integer> entry : targetCounts.entrySet()) {
					DocumentKey key = entry.getKey();
					int required = entry.getValue();
					int current = currentCounts.getOrDefault(key, 0);
					if (current < required) {
						System.out.println(String.format("Waiting on %s: current count = %d, required = %d", key, current, required));
						allMet = false;
					}
				}
				if (allMet) {
					System.out.println("SUCCESS: All new document(s) have appeared in the table.");
					//cleanupUploadedFiles(filenamesString);
					return true;
				}
			} catch (Exception e) {
				System.err.println("Error reading document table on attempt " + attempt + ": " + e.getMessage());
			}
			// Wait before refreshing and checking again
			if (attempt < maxAttempts) {
				Sleep(pollIntervalMillis);
				getDriver().navigate().refresh();
				waitUntilElementVisible(submittedDocumentsHeading);
				applySearchFilters(filterCategory, filterDescription, todayFilterDate);
			}
		}
		System.err.println("FAILED: Timeout waiting for new document(s) to appear.");
		//cleanupUploadedFiles(filenamesString);
		return false;
	}
	/**
	 * Iterates through all pages in the table, accumulates matching counts for today's date,
	 * and resets back to Page 1 before returning.
	 */
	private Map<DocumentKey, Integer> getMatchingDocumentCountsAcrossPages(Set<DocumentKey> keysToWatch, String todayDisplayDate) {
		Map<DocumentKey, Integer> totalCounts = new HashMap<>();
		for (DocumentKey key : keysToWatch) {
			totalCounts.put(key, 0);
		}
		int currentPage = 1;
		int maxPagesToScan = 20; // Guard against infinite pagination loops
		while (currentPage <= maxPagesToScan) {
			// 1. Scan rows on current page
			countRowsOnCurrentPage(keysToWatch, todayDisplayDate, totalCounts);
			// 2. Check if a next page exists and is clickable
			if (hasNextPage()) {
				goToNextPage();
				currentPage++;
				Sleep(1000); // Allow table body to refresh
			} else {
				break;
			}
		}
		// 3. Reset back to page 1 if we traversed past it
		if (currentPage > 1) {
			resetToFirstPage();
		}
		return totalCounts;
	}
	/**
	 * Helper to scan and accumulate counts on the active table page.
	 * Uses exact case-insensitive matching for full description and category.
	 */
	private void countRowsOnCurrentPage(Set<DocumentKey> keysToWatch, String todayDisplayDate, Map<DocumentKey, Integer> counts) {
		List<WebElement> rows = getDriver().findElements(By.cssSelector("div[id^='SubmittedDocumentsDataTable'] .rt-tr-group"));
		for (WebElement row : rows) {
			try {
				// Skip empty/pad rows
				String rowClass = row.getAttribute("class");
				if (rowClass != null && rowClass.contains("-padRow")) {
					continue;
				}
				// Col 2: Category, Col 3: Description, Col 4: Date posted
				String actualCategory = row.findElement(By.cssSelector(".rt-td:nth-child(2)")).getText().trim();
				String actualDescription = row.findElement(By.cssSelector(".rt-td:nth-child(3)")).getText().trim();
				String actualDate = row.findElement(By.cssSelector(".rt-td:nth-child(4)")).getText().trim();
				if (actualDescription.isEmpty()) {
					continue;
				}
				if (actualDate.equalsIgnoreCase(todayDisplayDate)) {
					for (DocumentKey key : keysToWatch) {
						boolean descMatches = key.description.equalsIgnoreCase(actualDescription);
						boolean catMatches = key.category.isEmpty() || key.category.equalsIgnoreCase(actualCategory);
						if (descMatches && catMatches) {
							counts.put(key, counts.get(key) + 1);
							break; // Each row matches at most one key
						}
					}
				}
			} catch (NoSuchElementException ignored) {
				// Row empty or temporarily detached
			}
		}
	}
	/**
	 * Checks if the Jut "Next" button is present and not disabled.
	 */
	private boolean hasNextPage() {
		try {
			List<WebElement> nextButtons = getDriver().findElements(By.xpath(
					"//div[contains(@class, 'jut__Pagination__paginationNav')]//button[.//span[text()='Next']]"
			));
			if (nextButtons.isEmpty()) {
				return false;
			}
			WebElement nextBtn = nextButtons.get(0);
			String btnClass = nextBtn.getAttribute("class");
			String disabledAttr = nextBtn.getAttribute("disabled");
			String ariaDisabled = nextBtn.getAttribute("aria-disabled");
			boolean isDisabled = disabledAttr != null
					|| "true".equalsIgnoreCase(ariaDisabled)
					|| (btnClass != null && btnClass.toLowerCase().contains("disabled"));
			return nextBtn.isDisplayed() && !isDisabled;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * Clicks the Jut "Next" button.
	 */
	private void goToNextPage() {
		try {
			WebElement nextBtn = getDriver().findElement(By.xpath(
					"//div[contains(@class, 'jut__Pagination__paginationNav')]//button[.//span[text()='Next']]"
			));
			nextBtn.click();
		} catch (Exception e) {
			System.err.println("Warning: Failed to click Next page: " + e.getMessage());
		}
	}
	/**
	 * Returns directly to Page 1 by clicking the '1' page button in Jut pagination.
	 */
	private void resetToFirstPage() {
		try {
			List<WebElement> page1Buttons = getDriver().findElements(By.xpath(
					"//div[contains(@class, 'jut__Pagination__pageButtonsWrapper')]//button[.//span[text()='1']]"
			));
			if (!page1Buttons.isEmpty()) {
				WebElement page1Btn = page1Buttons.get(0);
				String btnClass = page1Btn.getAttribute("class");
				if (btnClass == null || !btnClass.contains("jut__Pagination__selectedPageButton")) {
					page1Btn.click();
					Sleep(800);
				}
			}
		} catch (Exception e) {
			System.err.println("Warning: Unable to reset to Page 1: " + e.getMessage());
		}
	}
	/**
	 * Sets the table page size to "Show All" (or "100") to minimize pagination overhead.
	 */
	private void selectShowAllPageSize() {
		try {
			if (isDisplayed(dropdownBtnLocator)) {
				Click(dropdownBtnLocator);
				Sleep(400);
				if (isDisplayed(showAllItem)) {
					Click(showAllItem);
				} else if (isDisplayed(hundredItem)) {
					Click(hundredItem);
				}
				Sleep(1200); // Allow table to re-render all rows
			}
		} catch (Exception e) {
			System.err.println("Notice: Could not expand page size to Show All: " + e.getMessage());
		}
	}
	/**
	 * Helper to expand and apply search filters.
	 */
	private void applySearchFilters(String category, String description, String filterDate) {
		try {
			if (isDisplayed(SearchOptionExpand)) {
				Click(SearchOptionExpand);
			}
			// 1. Only select Category if uniform across all files
			if (category != null && !category.trim().isEmpty()) {
				Select(DocumentCategoryFilter, category.trim());
			}
			// 2. Only enter Description if uniform across all files
			if (description != null && !description.trim().isEmpty()) {
				Enter(DocumentDescriptionFilter, description.trim());
			}
			// 3. Always enter today's date in From & To to reduce noise
			if (isDisplayed(PostedDateFromFilter)) {
				Enter(PostedDateFromFilter, filterDate);
			}
			if (isDisplayed(PostedDateToFilter)) {
				Enter(PostedDateToFilter, filterDate);
			}
			// 4. Click Search
			Click(SearchButtonFilter);
			Sleep(1500);
			// 5. Expand page size to 'Show All' if available
			selectShowAllPageSize();
		} catch (Exception e) {
			System.err.println("Warning: Error applying search filters: " + e.getMessage());
		}
	}
	/**
	 * Key representation of an expected document (Description + Category).
	 * Implements case-insensitive equals and hashCode for tracking in Maps/Sets.
	 */
	private static class DocumentKey {
		final String description;
		final String category;
		DocumentKey(String description, String category) {
			this.description = description != null ? description.trim() : "";
			this.category = category != null ? category.trim() : "";
		}
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			DocumentKey that = (DocumentKey) o;
			return description.equalsIgnoreCase(that.description) &&
					category.equalsIgnoreCase(that.category);
		}
		@Override
		public int hashCode() {
			return Objects.hash(description.toLowerCase(), category.toLowerCase());
		}
		@Override
		public String toString() {
			return "[" + description + " (" + (category.isEmpty() ? "Any Category" : category) + ")]";
		}
	}
	/**
	 * Deletes the temporary files created during the upload process.
	 */
	private void cleanupUploadedFiles(String filenamesString) {
		Arrays.stream(filenamesString.split(","))
				.map(String::trim)
				.forEach(filename -> {
					try {
						Files.deleteIfExists(Paths.get("src/test/resources/DocUploadFiles", filename));
						System.out.println("Deleted temporary file: " + filename);
					} catch (IOException e) {
						System.err.println("Could not delete temporary file: " + filename + ". Error: " + e.getMessage());
					}
				});
	}


	public boolean verifyOnSendMessageScreen() {
		jsClick(SendusmessageLink);
		waitUntilElementVisible(SendusmessageHeader);
		Assert.assertEquals(SendusmessageHeader.getText(), "Send us a message");
		return SendusmessageHeader.isDisplayed();
	}

	public boolean sendusamessage(String subject, String message) {
		if (message.equals("<dynamic>")) {
			randommessage = generateRandomText(5); // Replace "<dynamic>" with random text
		}
		Click(SubjectDropdown);
		String dynamicXPath = "//*[contains(text(),'" + subject + "')]";
		WebElement SubjectElement = getDriver().findElement(By.xpath(dynamicXPath));
		waitUntilElementVisible(SubjectElement);
		Click(SubjectElement);
		Enter(txtBody, randommessage);
		return SubjectDropdown.getText().contentEquals(subject);
	}

	public boolean sendandconfirm() {
		Click(SendButton);
		waitUntilElementVisible(ReturntoMail);
		//Assert.assertEquals(ReturntoMail.getText(), "Message sent");
		return ReturntoMail.isDisplayed();
	}

	public boolean mailscreen() {
		Click(ReturntoMail);
		waitUntilElementVisible(mailListPageHeading);
		return SentMessageBody.getText().contentEquals(randommessage);
	}

	public boolean addClaim() {
		Click(AddClaimButton);
		waitUntilElementVisible(pageHeadingLabel);
		return pageHeadingLabel.isDisplayed();
	}

	public void selectInjuryAreaIfDisplayed(String injuryArea) {
		if (isDisplayed(injuryAreaContainer)) {
			// Locate the dynamic label element based on the text passed
			WebElement option = getDriver().findElement(
					By.xpath("//div[@id='InjuryArea']//label[normalize-space()='" + injuryArea + "']")
			);
			Click(option);
			System.out.println("Selected Injury Area: " + injuryArea);
		}
	}

//	public boolean AddaClaimOSPC(String claimnumber, String yob, String pin, GuidewirePage guidewirecc) {
//
//		ClaimNumberInput.sendKeys(claimnumber);
//		yearOfBirthInput.sendKeys(yob);
//		personalIdentificationInput.sendKeys(pin);
//		termOfUseCheckBox.click();
//		SubmitButton.click();
//		try {
//			if (isDuplicateClaimMsg.isDisplayed()) {
//				ArrayList<String> tabs = guidewirecc.switchtab();
//				guidewirecc.loginToGWCC(prop.getProperty("GWCC_Username"), prop.getProperty("GWCC_Password"));
//				guidewirecc.navigatePortalAuthScreen();
//				guidewirecc.removeClaimfromPortalAuth(claimnumber);
//				guidewirecc.GWCClogout();
//				getDriver().close();
//				getDriver().switchTo().window(tabs.get(0));
//				termOfUseCheckBox.click();
//				SubmitButton.click();
//			}
//		}
//		catch(NoSuchElementException ignored)
//		{}
//
//		waitUntilElementVisible(secondaryHeading);
//		returnToClaimsHomelLink.click();
//
//		// Locate all rows in the table
//		List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));
//
//		// Iterate through each row to search for the claim number
//		for (WebElement row : rows) {
//			// Locate the Claim Number cell (assuming it's the first column)
//			WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
//			String cNumber = claimCell.getText();
//
//			// Check if the claim number matches the desired value
//			if (cNumber.equals(claimnumber)) {
//				System.out.println("Claim number " + claimnumber + " found.");
//				return true;
//            }
//			else {
//				System.out.println("Claim number " + claimnumber + " not found.");
//				return false;
//			}
//		}
//			return false;
//	}

		public boolean AddaClaimOSPC(String claimnumber, String yob, String InjuryDate,
									 String PhoneNumber, String EmployerName, String PostalCode, String injuryArea) {

		Enter(ClaimNumberInput, claimnumber);
		Enter(yearOfBirthInput, yob);
		Click(NextButton);
		Enter(InjuryDateQuestion, InjuryDate);
			// Dynamic selection replacing Click(InjuryFaceQuestion)
			selectInjuryAreaIfDisplayed(injuryArea);

		if(isDisplayed(PhoneNumberQuestion))
		{
			Enter(PhoneNumberQuestion, PhoneNumber);
		}
			if(isDisplayed(EmployerNameQuestion))
			{
				Enter(EmployerNameQuestion, EmployerName);
			}
			if(isDisplayed(PostalCodeQuestion))
			{
				Enter(PostalCodeQuestion, PostalCode);
			}

			Click(NextButton);
		waitUntilElementVisible(verifyCard);
		Click(returnToClaimsHomelLink);
		Click(switchClaimLink);
		// Locate all rows in the table
		List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));

		// Iterate through each row to search for the claim number
		for (WebElement row : rows) {
			// Locate the Claim Number cell (assuming it's the first column)
			WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
			String cNumber = claimCell.getText();

			// Check if the claim number matches the desired value
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return true;
            }
			else {
				System.out.println("Claim number " + claimnumber + " not found.");
				return false;
			}
		}
			return false;
	}

	public boolean claimRemovalConfirmationOSPC(String claimnumber) throws InterruptedException {
		Set<String> windowHandles = getDriver().getWindowHandles();
		List<String> tabs = new ArrayList<>(windowHandles);
		getDriver().close();
		getDriver().switchTo().window(tabs.get(0));
		getDriver().navigate().refresh();
		//Sleep(5000);
		if(isDisplayed(tableBody)) {
			waitUntilElementVisible(tableBody);
			// Locate all rows in the table
			List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));

				// Iterate through each row to search for the claim number
				for (WebElement row : rows) {
					// Locate the Claim Number cell (assuming it's the first column)
					WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
					String cNumber = claimCell.getText();

					// Check if the claim number matches the desired value
					if (cNumber.equals(claimnumber)) {
						System.out.println("Claim number " + claimnumber + " found.");
						return false;
					} else {
						System.out.println("Claim number " + claimnumber + " not found.");
						return true;
					}
				}
					}
		else {
			System.out.println("No Claims Exists in the table");
			return true;
		}
		return false;
	}

	public boolean claimAdditionalConfirmationOSPC(String claimnumber) throws InterruptedException {
		List<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
		getDriver().close();
		getDriver().switchTo().window(tabs.get(0));
		getDriver().navigate().refresh();
		Sleep(5000);
		//waitUntilElementVisible(tableBody);

		for (WebElement row : tableBody.findElements(By.cssSelector("div.rt-tr"))) {
			String cNumber = row.findElement(By.cssSelector("div.rt-td a.bluelink")).getText();
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return true;
			}
		}
		System.out.println("Claim number " + claimnumber + " not found.");
		return false;
	}


	public boolean claimAdditionConfirmationOSPC(String claimnumber) throws InterruptedException {

		//Sleep(5000);
		waitUntilElementVisible(tableBody);

		// Locate all rows in the table
		List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));

		// Iterate through each row to search for the claim number
		for (WebElement row : rows) {
			// Locate the Claim Number cell (assuming it's the first column)
			WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
			String cNumber = claimCell.getText();

			// Check if the claim number matches the desired value
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return true;
			}
			else {
				System.out.println("Claim number " + claimnumber + " not found.");
				return false;
			}
		}
		return false;
	}

	public int appealRegisteredConfirmationOSPC() {
		Set<String> windowHandles = getDriver().getWindowHandles();
		List<String> tabs = new ArrayList<>(windowHandles);
		getDriver().close();
		getDriver().switchTo().window(tabs.get(0));
		getDriver().navigate().refresh();
		waitUntilElementVisible(trackAppealHeading);
		return verifyCountOfAppeals();
	}


	public boolean selectNonWSIBForm(String nonwsibformtype, String nonwsibform) {
		List<String> categories = Arrays.stream(nonwsibformtype.split("\\|"))
				.map(String::trim)
				.collect(Collectors.toList());
		List<String> documents = Arrays.stream(nonwsibform.split("\\|"))
				.map(String::trim)
				.collect(Collectors.toList());

		List<WebElement> fileItemContainers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".fileItemContainer")));

		if (fileItemContainers.size() != categories.size() || fileItemContainers.size() != documents.size()) {
			throw new AssertionError("Mismatch between number of uploaded files and Non-WSIB form selections provided.");
		}

		for (int i = 0; i < fileItemContainers.size(); i++) {
			WebElement currentFileContainer = fileItemContainers.get(i);
			String currentCategory = categories.get(i);
			String currentDocument = documents.get(i);

			String radioGroupId = "docSource" + i;
			WebElement noRadioButton = currentFileContainer.findElement(By.id(radioGroupId + "_no"));

			// Select "No" for "Is this a WSIB form?"
			if (!noRadioButton.isSelected()) {
				Click(noRadioButton);
				System.out.println("Selected 'No' for file at index " + i);
			}

			// Select Category dropdown (Dropdown 1)
			WebElement categoryDropdown = currentFileContainer.findElement(By.xpath(".//*[contains(@id, 'formcategoryselection-wrapper')]"));
			Select(categoryDropdown, currentCategory);
			System.out.println("Selected Category: '" + currentCategory + "' for file index " + i);

			// Select Document type dropdown (Dropdown 2)
			WebElement docTypeDropdown = currentFileContainer.findElement(By.xpath(".//*[contains(@id, 'nonwsibformselection-wrapper')]"));
			Select(docTypeDropdown, currentDocument);
			System.out.println("Selected Document Name: '" + currentDocument + "' for file index " + i);
		}
		return true;
	}
}
