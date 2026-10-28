package pages.UNCD;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

///same used for Upload Report - UNCD documents
public class SubmitDocuments extends BaseDriverClass {

	@FindBy(xpath = "//*[@aria-labelledby='formtype0']/label/span/input[@value='yes']") private WebElement formtype0Yes;
	@FindBy(xpath = "//*[@aria-labelledby='formtype0']/label/span/input[@value='no']") private WebElement formtype0No;
	@FindBy(id = "edobformselection0-select") private WebElement edobformselection0Select;
	@FindBy(id = "catogoryselection0-select") private WebElement catogoryselection0Select;
	@FindBy(id = "subformselection0-select") private WebElement subformselection0Select;
	@FindBy(id = "subformselection0-listbox") private WebElement subformselection0listbox;
	@FindBy(xpath = "//*[@type='submit']") private WebElement NextButton;
	@FindBy(id = "formtype0") private WebElement isthisaWSIBform;

	@FindBy(id = "uncdformselection0-select") private WebElement uncdformselection0Select;
	@FindBy(id = "region-support") private WebElement regionSupportHeader;
	@FindBy(xpath = "//*[@aria-labelledby='support-doctype-label']/label/span/input[@value='yes']") private WebElement supportdoctypeYes;
	@FindBy(xpath = "//*[@aria-labelledby='support-doctype-label']/label/span/input[@value='no']") private WebElement supportdoctypeNo;
	@FindBy(id = "alert-dialog-title") private WebElement alertdialogtitle;
	@FindBy(xpath = "//button[text()='Fermer' or text()= 'Close']") private WebElement alertCloseButton;

	private WebDriverWait wait;
	public SubmitDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		this.wait = wait;
		PageFactory.initElements(driver, this);
	}

	public void navigateToURL(String url) {
		getDriver().get(url);
	}


	public List<String> uploadDocument(String filenamesString) throws IOException {
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

		waitUntilElementVisible(isthisaWSIBform);
		// Return the list of timestamped filenames for further use (e.g., updating GWCC.json)
		return timestampedFilenames;
	}

	public Boolean selectWSIBForm(String wsibFormsString) {
		List<String> wsibFormSelections = Arrays.stream(wsibFormsString.split("\\|"))
				.map(String::trim)
				.collect(Collectors.toList());

		// Find all the file item containers.
		// This assumes the order of these containers in the DOM matches the order of files uploaded.
		List<WebElement> fileItemContainers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".MuiGrid-root.MuiGrid-container.MuiGrid-direction-xs-row.css-akeo4y")));

		if (fileItemContainers.size() != wsibFormSelections.size()) {
			throw new AssertionError("Mismatch between number of uploaded files and WSIB form selections provided in test data.");
		}

		for (int i = 0; i < fileItemContainers.size(); i++) {
			String currentWsibFormSelection = wsibFormSelections.get(i);
			WebElement currentFileContainer = fileItemContainers.get(i);

			// Construct the ID for the radio button group based on its index
			String radioGroupId = "formtype" + i;

			// Locate the "Yes" and "No" radio buttons within the current file's container
			WebElement yesRadioButton = currentFileContainer.findElement(By.xpath(".//*[@aria-labelledby='" + radioGroupId + "']/label/span/input[@value='yes']"));
			WebElement noRadioButton = currentFileContainer.findElement(By.xpath(".//*[@aria-labelledby='" + radioGroupId + "']/label/span/input[@value='no']"));

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

                WebElement wsibFormTypeDropdown = currentFileContainer.findElement(By.xpath(".//*[@id='edobformselection" + i + "-select']"));
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
		Click(NextButton);
		try{
			if(alertdialogtitle.isDisplayed()){
				System.out.println("Alert dialog displayed");
				Click(alertCloseButton);
				Sleep(5000);
				Click(NextButton);
			}
		}
		catch(Exception e){
			System.out.println("Alert dialog not displayed");
		}
      return true;
    }

	public boolean selectNonWSIBForm(String nonwsibformtype, String nonwsibform) {
		List<String> categories = Arrays.stream(nonwsibformtype.split("\\|"))
				.map(String::trim)
				.collect(Collectors.toList());
		List<String> documents = Arrays.stream(nonwsibform.split("\\|"))
				.map(String::trim)
				.collect(Collectors.toList());

		List<WebElement> fileItemContainers = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".MuiGrid-root.MuiGrid-container.MuiGrid-direction-xs-row.css-akeo4y")));

		if (fileItemContainers.size() != categories.size() || fileItemContainers.size() != documents.size()) {
			throw new AssertionError("Mismatch between number of uploaded files and Non-WSIB form selections provided.");
		}

		for (int i = 0; i < fileItemContainers.size(); i++) {
			WebElement currentFileContainer = fileItemContainers.get(i);
			String currentCategory = categories.get(i);
			String currentDocument = documents.get(i);

			String radioGroupId = "formtype" + i;
			WebElement noRadioButton = currentFileContainer.findElement(By.xpath(".//*[@aria-labelledby='" + radioGroupId + "']/label/span/input[@value='no']"));

			// Select "No" for "Is this a WSIB form?"
			if (!noRadioButton.isSelected()) {
				Click(noRadioButton);
				System.out.println("Selected 'No' for file at index " + i);
			}

			// Select Category dropdown (Dropdown 1)
			WebElement categoryDropdown = currentFileContainer.findElement(By.xpath(".//*[@id='catogoryselection" + i + "-select']"));
			Select(categoryDropdown, currentCategory);
			System.out.println("Selected Category: '" + currentCategory + "' for file index " + i);

			// Select Document type dropdown (Dropdown 2)
			WebElement docTypeDropdown = currentFileContainer.findElement(By.xpath(".//*[@id='subformselection" + i + "-select']"));
			Select(docTypeDropdown, currentDocument);
			System.out.println("Selected Document Name: '" + currentDocument + "' for file index " + i);
		}
		Click(NextButton);
		try{
			if(alertdialogtitle.isDisplayed()){
				System.out.println("Alert dialog displayed");
				Click(alertCloseButton);
				Sleep(5000);
				Click(NextButton);
			}
		}
		catch(Exception e){
			System.out.println("Alert dialog not displayed");
		}
		return true;
	}

	public void selectUNCDForm(String category) {
		waitUntilElementVisible(uncdformselection0Select);
		Click(uncdformselection0Select);
		Enter(uncdformselection0Select, category);
		Click(NextButton);
		try{
			if(alertdialogtitle.isDisplayed()){
				System.out.println("Alert dialog displayed");
				Click(alertCloseButton);
				Sleep(5000);
				Click(NextButton);
			}
		}
		catch(Exception e){
			System.out.println("Alert dialog not displayed");
		}

	}

	public void selectSupportingDocuments(String supportingdocument) {
		waitUntilElementVisible(regionSupportHeader);
		if (supportingdocument.equalsIgnoreCase("yes")) {
			Click(supportdoctypeYes);
		} else {
			Click(supportdoctypeNo);
		}
		Click(NextButton);
	}
	/**
	 * Deletes the temporary files created during the upload process.
	 */
	public void cleanupUploadedFiles(String filenamesString) { // Changed to public
		Arrays.stream(filenamesString.split(","))
				.map(String::trim)
				.forEach(filename -> {
					try {
						Files.deleteIfExists(Paths.get("src/test/resources/DocUploadFiles", filename));
						Hooks.log("Deleted temporary file: " + filename);
					} catch (IOException e) {
						System.err.println("Could not delete temporary file: " + filename + ". Error: " + e.getMessage());
					}
				});
	}

}