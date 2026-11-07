package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DIeForm6_SupportingDocuments extends BaseDriverClass {
	private final ConfigReader prop = Hooks.getConfigReader();
	private final WebDriver pageDriver;
	private final WebDriverWait pageWait;

	@FindBy(css = "#ddlWhoCompleted-wrapper .jut__GenericSelectControl__control")
	private WebElement documentTypeControl;

	@FindBy(css = "#SubmittedDocumentsDataTable .jut__DataTable__noDataMessage")
	private List<WebElement> emptyHistoryMessages;
	private String historyReadState = "History has not been read yet.";

	@FindBy(css = "#intakeBodySection a[aria-label='Next step']")
	private WebElement nextButton;
	@FindBy(id = "additionalCommentsInput")
	private WebElement additionalCommentsInput;
	@FindBy(id = "acnDescriptionP")
	private WebElement supportingHeading;
	@FindBy(id = "fileupload")
	private WebElement fileInput;
	@FindBy(css = "#documentUploadContainer .jut__FileUploadField__valueText")
	private WebElement selectedFileText;
	@FindBy(id = "progressBarPercentage")
	private WebElement uploadPercentage;
	@FindBy(id = "ddlWhoCompleted")
	private WebElement documentTypeInput;
	@FindBy(id = "ddlWhoCompleted-wrapper")
	private WebElement documentTypeWrapper;
	// The closed dropdown HTML does not include its options. Support normal ARIA
	// options and react-select's generated option IDs; require an exact visible label.
	@FindBy(css = "#ddlWhoCompleted-wrapper [role='option'], "
			+ "#ddlWhoCompleted-wrapper [id*='-option-'], "
			+ "[id^='react-select-'][id*='-option-']")
	private List<WebElement> documentTypeOptions;
	@FindBy(id = "chkTerm")
	private WebElement confirmationInput;
	@FindBy(css = "label[for='chkTerm']")
	private WebElement confirmationLabel;
	@FindBy(id = "btnSubmit")
	private WebElement submitDocumentButton;
	@FindBy(id = "divTitleDocHistory")
	private WebElement submittedDocumentsHeading;
	@FindBy(id = "SubmittedDocumentsDataTable")
	private WebElement submittedDocumentsTable;
	@FindBy(css = "#SubmittedDocumentsDataTable .rt-tbody .rt-tr-group")
	private List<WebElement> submittedRows;
	@FindBy(css = "#SubmittedDocumentsDataTable button.jut__Pagination__paginationSelect")
	private List<WebElement> pageSizeButtons;
	@FindBy(css = "#SubmittedDocumentsDataTable a.jut__Pagination__paginationMenuItem[data-code='All']")
	private List<WebElement> showAllOptions;
	@FindBy(css = "#SubmittedDocumentsDataTable .jut__Pagination__resultOfMessage")
	private List<WebElement> resultCounters;
	@FindBy(css = "#SubmittedDocumentsDataTable .-loading.-active")
	private List<WebElement> activeTableLoaders;
	@FindBy(css = "#intakeBodySection [role='alert']")
	private List<WebElement> validationAlerts;

	public DIeForm6_SupportingDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		this.pageDriver = driver;
		this.pageWait = wait;
		PageFactory.initElements(driver, this);
	}

	/** Existing callers still skip optional documents; no JSON upload data is read. */
	public void enterSupportingDocuments() {
		Sleep(2000);
		Click(nextButton);
	}

	private boolean isDocumentTypeSelected(String label) {
		String expected = normalize(label);

		try {
			if (!documentTypeControl.isDisplayed()) {
				return false;
			}

			if (expected.equals(normalize(documentTypeControl.getText()))) {
				return true;
			}

			for (WebElement element : documentTypeControl.findElements(
					By.xpath(".//*[not(self::input)]"))) {

				if (element.isDisplayed()
						&& expected.equals(normalize(element.getText()))) {
					return true;
				}
			}

			return false;

		} catch (NoSuchElementException
		         | StaleElementReferenceException e) {
			return false;
		}
	}

	private String currentDocumentTypeText() {
		try {
			return documentTypeControl.getText().trim();

		} catch (NoSuchElementException
		         | StaleElementReferenceException e) {
			return "<control unavailable or re-rendering>";
		}
	}

	private boolean hasConfirmedEmptyHistory() {
		for (WebElement message : emptyHistoryMessages) {
			try {
				if (message.isDisplayed()
						&& "you have not submitted any documents yet"
						.equals(normalize(message.getText()))) {

					return true;
				}

			} catch (StaleElementReferenceException e) {
				// The table may be transitioning from empty to populated.
				return false;
			}
		}

		return false;
	}

	/** Strict no-document test: start a fresh report and assert the history is empty. */
	public void continueWithoutSupportingDocuments() {
		waitForSupportingScreen();
		long stageStarted = System.nanoTime();
		HistorySnapshot before = readCompleteHistory();

		if (!before.rows.isEmpty()) {
			throw new AssertionError("The no-document test found existing submitted documents "
					+ "on this report. Start a fresh Form 6 application.");
		}
		Hooks.log("[FORM 6 DOCUMENTS] No submitted documents listed; continuing without uploads.");
		continueToComments();
	}

	/** One file/type or pipe-separated files/types; one type may be reused for all files. */
	public List<String> enterSupportingDocuments(String filenames, String documentTypes,
	                                             String uploadFolder) {
		List<Path> files = resolveFiles(filenames, uploadFolder);
		List<String> types = parsePipe(documentTypes, "SupportingDocumentTypes");
		if (types.size() != 1 && types.size() != files.size()) {
			throw new IllegalArgumentException("Provide one SupportingDocumentTypes label for all files "
					+ "or one label per SupportingFiles entry.");
		}
		waitForSupportingScreen();
		List<String> verified = new ArrayList<>();
		for (int i = 0; i < files.size(); i++) {
			Path file = files.get(i);
			String filename = file.getFileName().toString();
			String type = types.size() == 1 ? types.get(0) : types.get(i);
			// Capture IDs BEFORE this file is selected/submitted. An older row with
			// the same filename cannot satisfy the current submission.
			HistorySnapshot before = readCompleteHistory();
			LocalDate selectedOn = LocalDate.now();
			assertNoVisibleErrors();
			Hooks.log("[FORM 6 DOCUMENTS] Uploading " + filename + "; document type=" + type);
			pageWait.until(ExpectedConditions.presenceOfElementLocated(By.id("fileupload")));
			fileInput.sendKeys(file.toString());
			new WebDriverWait(pageDriver, Duration.ofSeconds(setting(
					"eform6.supporting.upload.timeout.seconds", 120)))
					.until(driver -> {
						assertNoVisibleErrors();
						try {
							String selected = selectedFileText.getText().trim()
									.replaceFirst("(?i)^Uploaded file:\\s*", "");
							return filename.equals(selected)
									&& "100%".equals(uploadPercentage.getText().trim());
						} catch (NoSuchElementException | StaleElementReferenceException e) {
							return false;
						}
					});
			selectDocumentType(type);
			if (!confirmationInput.isSelected()) {
				Click(confirmationLabel);
			}
			pageWait.until(driver -> confirmationInput.isSelected());
			assertNoVisibleErrors();
			pageWait.until(ExpectedConditions.elementToBeClickable(submitDocumentButton));
			// Submit ONCE. History polling must not re-submit and create duplicates.
			Click(submitDocumentButton);
			waitForNewHistoryRow(filename, before.ids, selectedOn);
			verified.add(filename);
			Hooks.log("[FORM 6 DOCUMENTS] VERIFIED new submitted-history row for " + filename);
		}
		continueToComments();
		return verified;
	}

	public void enterAdditionalComments(String additionalComments) {
		waitUntilElementVisible(this.additionalCommentsInput);
		Enter(this.additionalCommentsInput, additionalComments);
		Click(nextButton);
	}

	private void waitForSupportingScreen() {
		pageWait.until(driver -> {
			try {
				return supportingHeading.isDisplayed()
						&& "Submit supporting documents".equals(supportingHeading.getText().trim());
			} catch (NoSuchElementException | StaleElementReferenceException e) {
				return false;
			}
		});
	}

	private void continueToComments() {
		Click(nextButton);
		waitUntilElementVisible(additionalCommentsInput);
	}

	private Integer displayedHistoryTotal() {
		for (WebElement counter : resultCounters) {
			if (!counter.isDisplayed()) {
				continue;
			}

			Matcher matcher = Pattern.compile("(\\d+)\\s*$").matcher(
					counter.getText()
							.replace(",", "")
							.replace('\u00a0', ' ')
							.trim());

			if (!matcher.find()) {
				throw new IllegalStateException(
						"Cannot read history result count: "
								+ counter.getText());
			}

			return Integer.parseInt(matcher.group(1));
		}

		return null;
	}

	private void selectDocumentType(String label) {
		if (label == null || label.trim().isEmpty()) {
			throw new IllegalArgumentException(
					"A document-type label is required.");
		}

		// The application may retain the previous file's document type.
		if (isDocumentTypeSelected(label)) {
			Hooks.log("[FORM 6 DOCUMENTS] Document type already selected: "
					+ label);
			return;
		}

		Click(documentTypeWrapper);
		documentTypeInput.clear();
		documentTypeInput.sendKeys(label);

		WebElement option;

		try {
			option = pageWait.until(driver -> {
				try {
					WebElement match = null;

					for (WebElement candidate : documentTypeOptions) {
						if (candidate.isDisplayed()
								&& normalize(candidate.getText())
								.equals(normalize(label))) {

							if (match != null) {
								throw new IllegalStateException(
										"Multiple visible options match: " + label);
							}

							match = candidate;
						}
					}

					return match;

				} catch (NoSuchElementException
				         | StaleElementReferenceException e) {
					return null;
				}
			});

		} catch (TimeoutException e) {
			throw new TimeoutException(
					"Document-type OPTION was not found: '" + label
							+ "'. Check the configured label and open-menu selectors.",
					e);
		}

		option.click();

		try {
			pageWait.until(driver -> isDocumentTypeSelected(label));

		} catch (TimeoutException e) {
			throw new TimeoutException(
					"Clicked document-type option '" + label
							+ "', but could not verify its selected display."
							+ " Current control text: ["
							+ currentDocumentTypeText() + "]",
					e);
		}

		Hooks.log("[FORM 6 DOCUMENTS] Document type selected and verified: "
				+ label);
	}

	private long logUploadStage(
			String filename, String stage, long startedAt) {

		long now = System.nanoTime();

		Hooks.log(String.format(
				Locale.ROOT,
				"[FORM 6 TIMING] %s | %s | %.2f seconds",
				filename,
				stage,
				(now - startedAt) / 1_000_000_000.0));

		return now;
	}

	private void waitForNewHistoryRow(String filename, Set<String> baselineIds, LocalDate selectedOn) {
		final Set<String> baseline = new HashSet<>(baselineIds);
		try {
			new WebDriverWait(pageDriver, Duration.ofSeconds(setting(
					"eform6.supporting.history.timeout.seconds", 300)))
					.pollingEvery(Duration.ofMillis(500))
					.until(driver -> {
						assertNoVisibleErrors();
						try {
							ensureShowAll();
							HistorySnapshot snapshot = snapshotIfComplete();
							if (snapshot == null) { return false; }
							for (HistoryRow row : snapshot.rows) {
								if (filename.equals(row.filename) && !baseline.contains(row.id)
										&& !row.date.isBefore(selectedOn)
										&& !row.date.isAfter(LocalDate.now())) {
									return true;
								}
							}
							return false;
						} catch (NoSuchElementException | StaleElementReferenceException e) {
							return false;
						}
					});
		} catch (TimeoutException e) {
			throw new AssertionError("No NEW submitted-history row was verified for '" + filename
					+ "' within the configured timeout. The document was not automatically resubmitted.", e);
		}
	}

	private HistorySnapshot readCompleteHistory() {
		historyReadState = "Waiting for submitted-document history.";

		pageWait.until(
				ExpectedConditions.visibilityOf(submittedDocumentsHeading));

		pageWait.until(
				ExpectedConditions.visibilityOf(submittedDocumentsTable));

		ensureShowAll();

		WebDriverWait initialHistoryWait = new WebDriverWait(
				pageDriver, Duration.ofSeconds(30));

		return initialHistoryWait
				.withMessage(() ->
						"Form 6 initial document history is incomplete. "
								+ historyReadState
								+ ". Expected a complete result count or an explicit "
								+ "empty-history message.")
				.until(driver -> {
					try {
						return snapshotIfComplete();

					} catch (NoSuchElementException
					         | StaleElementReferenceException e) {

						historyReadState =
								"History is rendering: "
										+ e.getClass().getSimpleName();

						return null;
					}
				});
	}

	private void ensureShowAll() {
		Integer total = displayedHistoryTotal();
		if (total == null || total <= 10) {
			return;
		}
		for (WebElement button : pageSizeButtons) {
			if (!button.isDisplayed()) { continue; }
			String selection = normalize(button.getText());
			if ("all".equals(selection) || "show all".equals(selection)) { return; }
			Click(button);
			WebElement all = pageWait.until(driver -> {
				for (WebElement option : showAllOptions) {
					if (option.isDisplayed()) { return option; }
				}
				return null;
			});
			all.click();
			pageWait.until(driver -> {
				for (WebElement current : pageSizeButtons) {
					String text = normalize(current.getText());
					if (current.isDisplayed() && ("all".equals(text) || "show all".equals(text))) {
						return true;
					}
				}
				return false;
			});
			return;
		}
	}

	private HistorySnapshot snapshotIfComplete() {
		for (WebElement loader : activeTableLoaders) {
			if (loader.isDisplayed()) { return null; }
		}
		HistorySnapshot snapshot = new HistorySnapshot();
		DateTimeFormatter format = new java.time.format.DateTimeFormatterBuilder()
				.parseCaseInsensitive().appendPattern("dd MMM uuuu").toFormatter(Locale.ENGLISH);
		for (WebElement group : submittedRows) {
			if (!group.isDisplayed()) { continue; }
			List<WebElement> cells = group.findElements(By.cssSelector(".rt-td"));
			if (cells.size() < 2 || cells.get(0).getText().trim().isEmpty()) { continue; }
			WebElement name = cells.get(0).findElement(By.cssSelector("span[id^='name_']"));
			String id = name.getAttribute("id");
			if (id == null || id.trim().isEmpty()) {
				throw new IllegalStateException("Submitted-document row has no record ID.");
			}
			String filename = name.getText().trim();
			LocalDate date = LocalDate.parse(cells.get(1).getText().trim(), format);
			if (!snapshot.ids.add(id)) {
				throw new IllegalStateException("Duplicate document-row ID in submitted history: " + id);
			}
			snapshot.rows.add(new HistoryRow(id, filename, date));
		}
		Integer total = null;
		for (WebElement counter : resultCounters) {
			if (counter.isDisplayed()) {
				Matcher match = Pattern.compile("(\\d+)\\s*$").matcher(
						counter.getText().replace(",", "").replace('\u00a0', ' ').trim());
				if (!match.find()) {
					throw new IllegalStateException("Cannot read submitted-document result count: " + counter.getText());
				}
				total = Integer.parseInt(match.group(1));
				break;
			}
		}
		// Without a total, do not call an empty/partial table a complete baseline.
		boolean confirmedEmpty = hasConfirmedEmptyHistory();
		historyReadState =
				"Result-counter total=" + total
						+ "; parsed document rows=" + snapshot.rows.size()
						+ "; explicit empty message=" + confirmedEmpty;
// Populated table—or an empty table that provides a zero counter.
// Require the parsed rows to agree with the displayed total.
		if (total != null) {
			return total.intValue() == snapshot.rows.size()
					? snapshot
					: null;
		}
// Confirmed initial empty state from the supplied HTML.
// No counter or pagination is rendered in this state.
		if (snapshot.rows.isEmpty() && confirmedEmpty) {
			return snapshot;
		}
// Neither a complete counted table nor a confirmed empty table.
		return null;
	}

	private void assertNoVisibleErrors() {
		for (WebElement alert : validationAlerts) {
			if (alert.isDisplayed() && !"true".equalsIgnoreCase(alert.getAttribute("aria-hidden"))) {
				String message = alert.getText().trim();
				if (!message.isEmpty()) {
					throw new AssertionError("Form 6 supporting-document error: " + message);
				}
			}
		}
	}

	private List<Path> resolveFiles(String value, String folder) {
		List<String> names = parsePipe(value, "SupportingFiles");
		if (names.isEmpty()) {
			throw new IllegalArgumentException("The upload test needs at least one SupportingFiles entry. "
					+ "Use the no-document step for the skip test.");
		}
		Path base = Paths.get(folder == null || folder.trim().isEmpty()
				? "src/test/resources/DocUploadFiles" : folder.trim()).toAbsolutePath().normalize();
		Set<String> allowed = new HashSet<>(Arrays.asList("docx", "jpeg", "jpg", "tiff", "tif", "pdf", "png"));
		Set<String> basenames = new HashSet<>();
		List<Path> files = new ArrayList<>();
		for (String name : names) {
			Path file = base.resolve(name).toAbsolutePath().normalize();
			if (!Files.isRegularFile(file) || !Files.isReadable(file)) {
				throw new IllegalArgumentException("Upload file does not exist or is not readable: " + file);
			}
			String basename = file.getFileName().toString();
			int dot = basename.lastIndexOf('.');
			String extension = dot < 0 ? "" : basename.substring(dot + 1).toLowerCase(Locale.ROOT);
			if (!allowed.contains(extension)) {
				throw new IllegalArgumentException("Unsupported positive-test file type: " + basename);
			}
			if (!basenames.add(basename.toLowerCase(Locale.ROOT))) {
				throw new IllegalArgumentException("Use unique file basenames within one upload list: " + basename);
			}
			files.add(file);
		}
		return files;
	}

	private List<String> parsePipe(String value, String key) {
		List<String> result = new ArrayList<>();
		if (value == null || value.trim().isEmpty()) { return result; }
		for (String token : value.split("\\|", -1)) {
			String item = token.trim();
			if (item.isEmpty() || item.startsWith("REPLACE_")) {
				throw new IllegalArgumentException("Provide actual, non-empty " + key + " values: " + value);
			}
			result.add(item);
		}
		return result;
	}

	private int setting(String name, int fallback) {
		String value = prop.getProperty(name);
		if (value == null || value.trim().isEmpty()) { return fallback; }
		try {
			int parsed = Integer.parseInt(value.trim());
			if (parsed <= 0) { throw new NumberFormatException("not positive"); }
			return parsed;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(name + " must be a positive integer.", e);
		}
	}

	private String normalize(String value) {
		return value.replace('\u00a0', ' ').trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
	}

	private static final class HistorySnapshot {
		private final Set<String> ids = new LinkedHashSet<>();
		private final List<HistoryRow> rows = new ArrayList<>();
	}
	private static final class HistoryRow {
		private final String id;
		private final String filename;
		private final LocalDate date;
		private HistoryRow(String id, String filename, LocalDate date) {
			this.id = id;
			this.filename = filename;
			this.date = date;
		}
	}
}
