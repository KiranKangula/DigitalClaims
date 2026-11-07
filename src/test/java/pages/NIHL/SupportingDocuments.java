package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SupportingDocuments extends BaseDriverClass {

	@FindBy(xpath = "//app-supporting-documents//button[@aria-label='Next']")
	private WebElement NextButton;

	@FindBy(xpath = "//app-supporting-documents//a[@aria-label='Attach audiogram, if available']")
	private WebElement AttachedAudiogramLink;

	@FindBy(xpath = "//app-supporting-documents//input[@type='file' and @title='uploadAudiogram']")
	private WebElement AttachedAudiogramFile;

	@FindBy(xpath = "//app-supporting-documents//input[@type='file' and @title='uploadProofOfEmployment']")
	private List<WebElement> AttachedEmploymentFiles;

	@FindBy(xpath = "//app-supporting-documents//app-field-error-display")
	private List<WebElement> UploadErrors;

	@FindBy(xpath = "//app-review//h2[@aria-label='Review']")
	private WebElement ReviewHeading;

	public SupportingDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	// Existing callers can continue to skip both optional upload sections.
	public void EnterSupportingDocuments() throws InterruptedException {
		EnterSupportingDocuments("", "", null);
	}

	public void EnterSupportingDocuments(String healthCareFiles,
	                                     String employmentFiles,
	                                     String uploadFolder)
			throws InterruptedException {

		waitUntilElementVisible(AttachedAudiogramLink);
		Sleep(5000);

		// Validate all configured paths before starting any uploads.
		List<Path> healthFiles = getUploadFiles(healthCareFiles, uploadFolder);
		List<Path> employmentDocuments = getUploadFiles(employmentFiles, uploadFolder);

		if (healthFiles.size() > 10) {
			throw new IllegalArgumentException("You can upload up to 10 audiograms.");
		}

		// Employment files map to employer upload controls in page order.
		// Capture runtime IDs because controls may disappear after uploading.
		List<String> employerIds = new ArrayList<>();
		Set<String> uniqueIds = new HashSet<>();

		if (employmentDocuments.size() > AttachedEmploymentFiles.size()) {
			throw new IllegalArgumentException(
					"Configured " + employmentDocuments.size()
							+ " employment files, but the page contains only "
							+ AttachedEmploymentFiles.size()
							+ " employer upload controls. Only one file per employer is allowed.");
		}

		for (int i = 0; i < employmentDocuments.size(); i++) {
			String id = AttachedEmploymentFiles.get(i).getAttribute("id");
			if (id == null || id.trim().isEmpty() || !uniqueIds.add(id)) {
				throw new IllegalStateException(
						"Employment upload controls must have unique IDs.");
			}
			employerIds.add(id);
		}

		verifyNoUploadErrors();

		// No multiple attribute: send one audiogram per selection.
		for (Path file : healthFiles) {
			AttachedAudiogramFile.sendKeys(file.toString());
			Sleep(5000);
			verifyNoUploadErrors();
		}

		// One employment document per employer.
		for (int i = 0; i < employmentDocuments.size(); i++) {
			getEmploymentFileInput(employerIds.get(i))
					.sendKeys(employmentDocuments.get(i).toString());
			Sleep(5000);
			verifyNoUploadErrors();
		}

		verifyNoUploadErrors();
		Click(NextButton);
		waitUntilElementVisible(ReviewHeading);
		Sleep(5000);
	}

	private List<Path> getUploadFiles(String configuredFiles, String uploadFolder) {
		List<Path> files = new ArrayList<>();

		// Missing or blank fields preserve the existing skip behaviour.
		if (configuredFiles == null || configuredFiles.trim().isEmpty()) {
			return files;
		}

		String folder = uploadFolder == null || uploadFolder.trim().isEmpty()
				? "src/test/resources/DocUploadFiles"
				: uploadFolder.trim();

		Path baseFolder = Paths.get(folder).toAbsolutePath().normalize();
		Set<String> fileNames = new HashSet<>();

		for (String value : configuredFiles.split("\\|", -1)) {
			String name = value.trim();
			if (name.isEmpty()) {
				throw new IllegalArgumentException(
						"Empty filename in upload data: " + configuredFiles);
			}

			// Absolute paths also work; relative paths use upload.folder.
			Path file = baseFolder.resolve(name).toAbsolutePath().normalize();
			if (!Files.isRegularFile(file) || !Files.isReadable(file)) {
				throw new IllegalArgumentException(
						"Upload file does not exist or is not readable: " + file);
			}
			if (!fileNames.add(file.getFileName().toString())) {
				throw new IllegalArgumentException(
						"Duplicate filename in the same section: " + file.getFileName());
			}
			files.add(file);
		}
		return files;
	}

	private WebElement getEmploymentFileInput(String employerId) {
		// Re-read the @FindBy list instead of hard-coding an employer ID.
		for (WebElement input : AttachedEmploymentFiles) {
			if (employerId.equals(input.getAttribute("id"))) {
				return input;
			}
		}
		throw new IllegalStateException(
				"Employment upload control not found for employer ID: " + employerId);
	}

	private void verifyNoUploadErrors() {
		for (WebElement error : UploadErrors) {
			// Inactive components may have aria-labels but no rendered text.
			String message = error.getText().trim();
			if (error.isDisplayed() && !message.isEmpty()) {
				throw new AssertionError("Supporting document upload error: " + message);
			}
		}
	}
}
