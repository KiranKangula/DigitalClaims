package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.By;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DIeForm7_SupportingDocuments extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();
	private final WebDriver pageDriver;
	private final WebDriverWait pageWait;
	private String historyState = "History has not been inspected.";

	// Existing declarations retained for compatibility.
	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']")
	private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']")
	private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]")
	private WebElement NextButton;
	@FindBy(xpath = "//h1[contains(text(),'Supporting documents')]")
	private WebElement SupportingDocumentsHeading;
	@FindBy(xpath = "//h1[contains(text(),'Additional comments')]")
	private WebElement AdditionalCommentsHeading;
	@FindBy(id = "additionalCommentsInput")
	private WebElement AdditionalComments;

	// Form 7 supporting-document states from the supplied HTML.
	@FindBy(id = "attachReportButton")
	private List<WebElement> AttachButtons;
	@FindBy(id = "documentUploadContainer")
	private List<WebElement> UploadContainers;
	@FindBy(id = "fileupload")
	private List<WebElement> FileInputs;
	@FindBy(css = "#documentUploadContainer .jut__FileUploadField__valueText")
	private WebElement SelectedFileText;
	@FindBy(id = "aboutDocument")
	private List<WebElement> AboutDocumentHeadings;
	@FindBy(id = "progressBarPercentage")
	private WebElement UploadPercentage;
	@FindBy(id = "ddlWhoCompleted-wrapper")
	private WebElement DocumentTypeWrapper;
	@FindBy(id = "ddlWhoCompleted")
	private WebElement DocumentTypeInput;
	@FindBy(css = "#ddlWhoCompleted-wrapper .jut__GenericSelectControl__singleValue")
	private List<WebElement> SelectedDocumentTypes;
	// Open-menu HTML was not included. These cover ARIA and normal react-select options.
	@FindBy(css = "#ddlWhoCompleted-wrapper [role='option'], "
			+ "#ddlWhoCompleted-wrapper [id*='-option-'], "
			+ "[id^='react-select-'][id*='-option-']")
	private List<WebElement> DocumentTypeOptions;
	@FindBy(id = "chkTerm")
	private WebElement ConfirmationInput;
	@FindBy(css = "label[for='chkTerm']")
	private WebElement ConfirmationLabel;
	@FindBy(id = "btnSubmit")
	private WebElement SubmitDocumentButton;
	@FindBy(css = "#dynamicIntakeFooter a[aria-label='Go to the next page.']")
	private WebElement SupportingNextButton;

	@FindBy(css = "#divAttachDocContainer [class*='document-history_tableStyling']")
	private List<WebElement> HistoryContainers;
	@FindBy(id = "SubmittedDocumentsDataTable")
	private List<WebElement> HistoryTables;
	@FindBy(css = "#SubmittedDocumentsDataTable .rt-tbody .rt-tr-group")
	private List<WebElement> DocumentRows;
	@FindBy(css = "#SubmittedDocumentsDataTable .jut__Pagination__resultOfMessage")
	private List<WebElement> HistoryCounters;
	@FindBy(css = "#SubmittedDocumentsDataTable button.jut__Pagination__paginationSelect")
	private List<WebElement> PageSizeButtons;
	@FindBy(css = "#SubmittedDocumentsDataTable a[data-code='All']")
	private List<WebElement> ShowAllOptions;
	@FindBy(css = "#SubmittedDocumentsDataTable .-loading.-active")
	private List<WebElement> ActiveHistoryLoaders;
	@FindBy(css = "#associatedClaimNumber [role='alert'], "
			+ "#associatedClaimNumber #displayErrorMsg, "
			+ "#associatedClaimNumber #documentErrorContainer")
	private List<WebElement> ErrorMessages;

	public DIeForm7_SupportingDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		this.pageDriver = driver;
		this.pageWait = wait;
		PageFactory.initElements(driver, this);
	}

	/** Existing skip behaviour and comments are intentionally unchanged. */
	public boolean attachSupportingDocuments() {
		waitUntilElementVisible(SupportingDocumentsHeading);
		Click(NextButton);
		Sleep(2000);
		waitUntilElementVisible(AdditionalCommentsHeading);
		Enter(AdditionalComments, "This is additional comments added");
		Click(NextButton);
		return true;
	}

	/** Uploads and verifies one document at a time, then follows the existing comments flow. */
	public List<String> attachSupportingDocuments(String filenames, String documentTypes,
	                                              String folder, String comments) {
		List<Path> files = resolveFiles(filenames, folder);
		List<String> types = splitPipe(documentTypes, "SupportingDocumentTypes");
		if (types.size() != 1 && types.size() != files.size()) {
			throw new IllegalArgumentException("Supply one SupportingDocumentTypes label for all files "
					+ "or one label per SupportingFiles entry.");
		}
		pageWait.until(ExpectedConditions.visibilityOf(SupportingDocumentsHeading));
		enableUploaderIfNeeded();

		List<String> verifiedFiles = new ArrayList<>();
		Set<String> verifiedThisRun = new HashSet<>();
		for (int index = 0; index < files.size(); index++) {
			Path file = files.get(index);
			String filename = file.getFileName().toString();
			String type = types.size() == 1 ? types.get(0) : types.get(index);
			long fileStarted = System.nanoTime();
			long stageStarted = fileStarted;

			waitForResetUploader();
			HistorySnapshot before = readCompleteHistory(verifiedThisRun);
			stageStarted = logStage(filename, "uploader ready and baseline captured", stageStarted);
			LocalDate selectedOn = LocalDate.now();
			assertNoDocumentErrors();
			FileInputs.get(0).sendKeys(file.toString());
			newWait(setting("eform7.supporting.upload.timeout.seconds", 120),
					"File upload did not reach 100% for " + filename)
					.until(driver -> {
						assertNoDocumentErrors();
						try {
							String selected = SelectedFileText.getText().trim()
									.replaceFirst("(?i)^Uploaded file:\\s*", "");
							return filename.equals(selected)
									&& "100%".equals(UploadPercentage.getText().trim());
						} catch (NoSuchElementException | StaleElementReferenceException e) {
							return false;
						}
					});
			stageStarted = logStage(filename, "upload reached 100%", stageStarted);

			selectDocumentType(type);
			stageStarted = logStage(filename, "document type selected", stageStarted);
			if (!ConfirmationInput.isSelected()) {
				clickNative(ConfirmationLabel);
			}
			pageWait.until(driver -> ConfirmationInput.isSelected());
			assertNoDocumentErrors();
			clickNative(SubmitDocumentButton); // Exactly one Submit per file.
			stageStarted = logStage(filename, "confirmed and submitted", stageStarted);

			HistoryRow landed = waitForNewDocument(filename, before.ids, selectedOn, verifiedThisRun);
			verifiedThisRun.add(landed.id);
			verifiedFiles.add(filename);
			logStage(filename, "new submitted row verified", stageStarted);
			logStage(filename, "TOTAL", fileStarted);
		}

		// Match the existing method: Supporting documents -> Additional comments -> next screen.
		clickNative(SupportingNextButton);
		waitUntilElementVisible(AdditionalCommentsHeading);
		Enter(AdditionalComments, comments == null || comments.trim().isEmpty()
				? "This is additional comments added" : comments);
		Click(NextButton);
		return Collections.unmodifiableList(verifiedFiles);
	}

	private void enableUploaderIfNeeded() {
		pageWait.until(driver -> uploaderExists() || visibleAttachButton() != null);
		if (!uploaderExists()) {
			WebElement attach = visibleAttachButton();
			if (attach == null) {
				throw new IllegalStateException("Form 7 Attach button disappeared before the uploader opened.");
			}
			clickNative(attach);
			Hooks.log("[FORM 7 DOCUMENTS] Opened the initial uploader using Attach.");
		}
		pageWait.until(driver -> uploaderExists());
		// Do not use the custom visible='false' attribute on divAttachDocContainer;
		// it is present in both the initial and expanded supplied HTML.
	}

	private boolean uploaderExists() {
		try {
			return !FileInputs.isEmpty() && !UploadContainers.isEmpty()
					&& UploadContainers.get(0).isDisplayed() && FileInputs.get(0).isEnabled();
		} catch (StaleElementReferenceException e) {
			return false;
		}
	}

	private WebElement visibleAttachButton() {
		for (WebElement button : AttachButtons) {
			if (button.isDisplayed() && button.isEnabled()) { return button; }
		}
		return null;
	}

	private void waitForResetUploader() {
		newWait(setting("eform7.supporting.ui.timeout.seconds", 15),
				"Uploader did not reset to 'Drag and drop document'. A pending document may remain.")
				.until(driver -> {
					assertNoDocumentErrors();
					try {
						if (!uploaderExists()
								|| !"drag and drop document".equals(normalize(SelectedFileText.getText()))) {
							return false;
						}
						for (WebElement heading : AboutDocumentHeadings) {
							if (heading.isDisplayed()) { return false; }
						}
						return true;
					} catch (NoSuchElementException | StaleElementReferenceException e) {
						return false;
					}
				});
	}

	private void selectDocumentType(String label) {
		if (selectedTypeMatches(label)) { return; }
		clickNative(DocumentTypeWrapper);
		pageWait.until(ExpectedConditions.visibilityOf(DocumentTypeInput));
		DocumentTypeInput.clear();
		DocumentTypeInput.sendKeys(label);
		WebElement option;
		try {
			option = newWait(setting("eform7.supporting.ui.timeout.seconds", 15),
					"No exact visible document-type option: " + label).until(driver -> {
				try {
					WebElement match = null;
					for (WebElement candidate : DocumentTypeOptions) {
						if (candidate.isDisplayed()
								&& normalize(label).equals(normalize(candidate.getText()))) {
							if (match != null) {
								throw new IllegalStateException("Multiple document-type options match: " + label);
							}
							match = candidate;
						}
					}
					return match;
				} catch (NoSuchElementException | StaleElementReferenceException e) {
					return null;
				}
			});
		} catch (TimeoutException e) {
			throw new TimeoutException("Cannot find document-type option '" + label
					+ "'. If the label is correct, provide the dropdown HTML while OPEN; "
					+ "the supplied files contain only closed dropdowns.", e);
		}
		option.click();
		newWait(setting("eform7.supporting.ui.timeout.seconds", 15),
				"Option was clicked but the selected singleValue does not equal: " + label)
				.until(driver -> selectedTypeMatches(label));
	}

	private boolean selectedTypeMatches(String label) {
		try {
			for (WebElement selected : SelectedDocumentTypes) {
				if (selected.isDisplayed()
						&& normalize(label).equals(normalize(selected.getText()))) { return true; }
			}
			return false;
		} catch (StaleElementReferenceException e) {
			return false;
		}
	}

	private HistorySnapshot readCompleteHistory(Set<String> verifiedThisRun) {
		return newWait(setting("eform7.supporting.ui.timeout.seconds", 15),
				"Could not capture a complete Form 7 history baseline")
				.withMessage(() -> "Incomplete Form 7 baseline: " + historyState)
				.until(driver -> {
					assertNoDocumentErrors();
					try {
						HistorySnapshot snapshot = historySnapshot();
						if (snapshot == null) { return null; }
						// Do not accept a temporary empty render after a previous successful upload.
						if (!snapshot.ids.containsAll(verifiedThisRun)) {
							historyState += "; previously verified rows are temporarily missing";
							return null;
						}
						return snapshot;
					} catch (NoSuchElementException | StaleElementReferenceException e) {
						historyState = "History is re-rendering: " + e.getClass().getSimpleName();
						return null;
					}
				});
	}

	private HistoryRow waitForNewDocument(String filename, Set<String> baseline,
	                                      LocalDate selectedOn, Set<String> verifiedThisRun) {
		final Set<String> previousIds = new HashSet<>(baseline);
		return newWait(setting("eform7.supporting.history.timeout.seconds", 120),
				"No new submitted document row for " + filename)
				.withMessage(() -> "No new row for " + filename + ". " + historyState
						+ ". Submit is not automatically repeated.")
				.until(driver -> {
					assertNoDocumentErrors();
					try {
						HistorySnapshot snapshot = historySnapshot();
						if (snapshot == null || !snapshot.ids.containsAll(verifiedThisRun)) { return null; }
						for (HistoryRow row : snapshot.rows) {
							if (filename.equals(row.filename) && !previousIds.contains(row.id)
									&& !row.date.isBefore(selectedOn)
									&& !row.date.isAfter(LocalDate.now())) { return row; }
						}
						return null;
					} catch (NoSuchElementException | StaleElementReferenceException e) {
						historyState = "History is re-rendering: " + e.getClass().getSimpleName();
						return null;
					}
				});
	}

	private HistorySnapshot historySnapshot() {
		for (WebElement loader : ActiveHistoryLoaders) {
			if (loader.isDisplayed()) { historyState = "Table loading indicator is active"; return null; }
		}
		HistorySnapshot snapshot = new HistorySnapshot();
		if (HistoryTables.isEmpty()) {
			// Confirm the EXACT empty-holder state shown in images 3, 4, 5, and 7.
			// An empty holder may have zero height, so do not require isDisplayed().
			for (WebElement holder : HistoryContainers) {
				if (holder.findElements(By.xpath("./*")).isEmpty()
						&& holder.getText().trim().isEmpty()
						&& uploaderExists() && SupportingDocumentsHeading.isDisplayed()) {
					historyState = "Confirmed empty Form 7 history holder; no table exists yet";
					return snapshot;
				}
			}
			historyState = "History table absent, but the supplied empty-holder state was not found";
			return null;
		}
		DateTimeFormatter dates = new java.time.format.DateTimeFormatterBuilder()
				.parseCaseInsensitive().appendPattern("dd MMM uuuu").toFormatter(Locale.ENGLISH);
		for (WebElement group : DocumentRows) {
			if (!group.isDisplayed()) { continue; }
			List<WebElement> cells = group.findElements(By.cssSelector(".rt-td"));
			if (cells.size() < 2 || cells.get(0).getText().trim().isEmpty()) { continue; }
			WebElement name = cells.get(0).findElement(By.cssSelector("span[id^='name_']"));
			String id = name.getAttribute("id");
			if (id == null || id.isEmpty() || !snapshot.ids.add(id)) {
				throw new IllegalStateException("Missing or duplicate Form 7 document-row ID: " + id);
			}
			snapshot.rows.add(new HistoryRow(id, name.getText().trim(),
					LocalDate.parse(cells.get(1).getText().trim(), dates)));
		}
		Integer total = displayedHistoryTotal();
		historyState = "Displayed total=" + total + "; parsed rows=" + snapshot.rows.size();
		if (total == null) { return null; }
		if (total.intValue() == snapshot.rows.size()) { return snapshot; }
		if (total > 10 && snapshot.rows.size() < total) {
			// Non-blocking pagination interaction: no nested WebDriverWait in this poll.
			expandHistoryIfNeeded();
		}
		return null;
	}

	private Integer displayedHistoryTotal() {
		for (WebElement counter : HistoryCounters) {
			if (!counter.isDisplayed()) { continue; }
			Matcher matcher = Pattern.compile("(\\d+)\\s*$").matcher(
					counter.getText().replace(",", "").replace('\u00a0', ' ').trim());
			if (!matcher.find()) {
				throw new IllegalStateException("Cannot read Form 7 history count: " + counter.getText());
			}
			return Integer.parseInt(matcher.group(1));
		}
		return null;
	}

	private void expandHistoryIfNeeded() {
		for (WebElement option : ShowAllOptions) {
			if (option.isDisplayed()) { option.click(); return; }
		}
		for (WebElement button : PageSizeButtons) {
			if (!button.isDisplayed()) { continue; }
			String text = normalize(button.getText());
			if ("all".equals(text) || "show all".equals(text)
					|| "true".equalsIgnoreCase(button.getAttribute("aria-expanded"))) { return; }
			if (button.isEnabled()) { button.click(); }
			return;
		}
	}

	private void assertNoDocumentErrors() {
		for (WebElement error : ErrorMessages) {
			if (error.isDisplayed() && !"true".equalsIgnoreCase(error.getAttribute("aria-hidden"))) {
				String text = error.getText().trim();
				if (!text.isEmpty()) { throw new AssertionError("Form 7 document error: " + text); }
			}
		}
	}

	private void clickNative(WebElement element) {
		pageWait.until(ExpectedConditions.elementToBeClickable(element)).click();
	}

	private WebDriverWait newWait(int seconds, String message) {
		WebDriverWait wait = new WebDriverWait(pageDriver, Duration.ofSeconds(seconds));
		wait.pollingEvery(Duration.ofMillis(setting("eform7.supporting.poll.millis", 500)));
		wait.withMessage(message);
		return wait;
	}

	private List<Path> resolveFiles(String filenames, String folder) {
		List<String> names = splitPipe(filenames, "SupportingFiles");
		if (names.isEmpty()) { throw new IllegalArgumentException("The Form 7 upload test requires SupportingFiles."); }
		Path base = Paths.get(folder == null || folder.trim().isEmpty()
				? "src/test/resources/DocUploadFiles" : folder.trim()).toAbsolutePath().normalize();
		Set<String> allowed = new HashSet<>(Arrays.asList("docx", "jpeg", "jpg", "tiff", "tif", "pdf"));
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
				throw new IllegalArgumentException("File type is not accepted by the supplied Form 7 input: " + basename);
			}
			files.add(file);
		}
		return files;
	}

	private List<String> splitPipe(String value, String key) {
		List<String> result = new ArrayList<>();
		if (value == null || value.trim().isEmpty()) { return result; }
		for (String token : value.split("\\|", -1)) {
			String item = token.trim();
			if (item.isEmpty()) { throw new IllegalArgumentException("Empty item in " + key + ": " + value); }
			result.add(item);
		}
		return result;
	}

	private int setting(String key, int fallback) {
		String value = prop.getProperty(key);
		if (value == null || value.trim().isEmpty()) { return fallback; }
		try {
			int parsed = Integer.parseInt(value.trim());
			if (parsed <= 0) { throw new NumberFormatException("not positive"); }
			return parsed;
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(key + " must be a positive integer.", e);
		}
	}

	private String normalize(String text) {
		return text.replace('\u00a0', ' ').trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
	}

	private long logStage(String filename, String stage, long started) {
		long now = System.nanoTime();
		Hooks.log(String.format(Locale.ROOT, "[FORM 7 TIMING] %s | %s | %.2f seconds",
				filename, stage, (now - started) / 1_000_000_000.0));
		return now;
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
