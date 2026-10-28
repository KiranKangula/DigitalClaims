package definitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.ss.usermodel.DateUtil;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import pages.UNCD.AddClaimantInfo;
import pages.UNCD.GetStarted;
import pages.UNCD.ReviewSubmission;
import pages.UNCD.SubmitDocuments;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static utils.BaseDriverClass.getCurrentDate;

public class UNCD  {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private GetStarted getStarted;
	private AddClaimantInfo addClaimantInfo;
	private SubmitDocuments submitDocument;
	private ReviewSubmission reviewSubmission;
	ConfigReader prop= Hooks.getConfigReader();
	ConfigReader common = Hooks.getCommonReader();
	public UNCD() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		getStarted = new GetStarted(driver, wait);
		addClaimantInfo = new AddClaimantInfo(driver, wait);
		submitDocument = new SubmitDocuments(driver, wait);
		reviewSubmission = new ReviewSubmission(driver, wait);
		homePage = new HomePage(driver, wait);
	}

	@Given("User is on Step {int}: Get Started screen")
	public void userIsOnStepGetStartedScreen(int arg0) throws InterruptedException {
		Hooks.log("Verifying user is on Step 1: Get Started screen.");
		Assert.assertTrue(getStarted.verifyOnGetStartedPage());
	}

	@When("User clicks on Submitting a document for an existing claim")
	public void userClicksOnSubmittingADocumentForAnExistingClaim() {
		Hooks.log("Selecting 'Submitting a document for an existing claim'.");
		getStarted.SelectNumberedDocument();
	}

	@Then("User enters claim information such as {string}, {string}, {string}")
	public void userEntersClaimInformation(String claimnumber, String lastname, String dateofbirth) {
		claimnumber = Hooks.currentRow.get().get("claimnumber");
		lastname = Hooks.currentRow.get().get("lastname");
		dateofbirth = Hooks.currentRow.get().get("dateofbirth");

		Hooks.log("Entering claim information: " + claimnumber + ", " + lastname + ", " + dateofbirth);
		addClaimantInfo.AddClaimantInformation(claimnumber, lastname, dateofbirth);
	}

	@And("User should be able to upload a UNCD document with {string}")
	public void userShouldBeAbleToUploadAUNCDDocumentWith(String filename) throws IOException {
		String originalFilenames = Hooks.currentRow.get().get("filename");
		Hooks.log("Uploading document: " + originalFilenames);
		// Call uploadDocuments which now returns the list of timestamped filenames
		List<String> timestampedFilenamesList = submitDocument.uploadDocuments(originalFilenames);
		// Join the timestamped filenames into a comma-separated string
		String timestampedFilenamesString = String.join(",", timestampedFilenamesList);

		Hooks.currentRow.get().put("filename", timestampedFilenamesString);
		Hooks.log("Updated filename in current row for verification: " + timestampedFilenamesString);
		// --- END: Placeholder for JSON modification logic ---
		//submitDocument.cleanupUploadedFiles(timestampedFilenamesString);
	}

	@And("User should be able to select UNCD WSIB form {string}")
	public void userShouldBeAbleToSelectUNCDWSIBForm(String wsibform) throws InterruptedException {
		wsibform = Hooks.currentRow.get().get("wsibform");
		Hooks.log("Selecting UNCD WSIB form: " + wsibform);
		submitDocument.selectWSIBForm(wsibform);
		String filenamestring = Hooks.currentRow.get().get("filename");
		submitDocument.cleanupUploadedFiles(filenamestring);

	}

	@And("Select delaration and Email consent checkbox and Click on Submit button")
	public void selectDelarationAndEmailConsentCheckboxAndClickOnSubmitButton() {
		Hooks.log("Selecting declaration and email consent, then clicking Submit.");
		Assert.assertTrue(reviewSubmission.submitDocument());

	}

	@And("Verify the confirmation screen is displayed")
	public void verifyTheConfirmationScreenIsDisplayed() {
		Hooks.log("Verifying confirmation screen.");
		Assert.assertTrue(reviewSubmission.verifyOnConfirmationScreen());
		Hooks.log("Document Submitted at: " + getCurrentDate("MMM dd, yyyy hh:mm:ss a"));

	}

	@And("User should be able to select UNCD Non WSIB form {string}, {string}")
	public void userShouldBeAbleToSelectUNCDNonWSIBForm(String category, String subform) throws InterruptedException {
		category = Hooks.currentRow.get().get("nonwsibformcategory");
		subform = Hooks.currentRow.get().get("subform");
		Hooks.log("Selecting UNCD Non-WSIB form: Category=" + category + ", Subform=" + subform);
		submitDocument.selectNonWSIBForm(category, subform);
		String filenamestring = Hooks.currentRow.get().get("filename");
		submitDocument.cleanupUploadedFiles(filenamestring);

	}

	@When("User clicks on Reporting a new injury or exposure")
	public void userClicksOnReportingANewInjuryOrExposure() {
		Hooks.log("Selecting 'Reporting a new injury or exposure'.");
		getStarted.SelectUnNumberedDocument();
	}

	@Then("User enters claim information such as {string}, {string}, {string}, {string}")
	public void userEntersClaimInformationSuchAs(String firstname, String lastname, String dateofbirth, String dateofinjury) {
		firstname = Hooks.currentRow.get().get("firstname");
		lastname = Hooks.currentRow.get().get("lastname");
		dateofbirth = Hooks.currentRow.get().get("dateofbirth");
		dateofinjury = Hooks.currentRow.get().get("dateofinjury");

		Hooks.log("Entering new injury claim information: " + firstname + " " + lastname + ", DOB=" + dateofbirth + ", DOI=" + dateofinjury);
		addClaimantInfo.AddClaimantInformationUNCD(firstname, lastname, dateofbirth, dateofinjury);


	}

	@And("User should be able to select UNCD form {string}")
	public void userShouldBeAbleToSelectUNCDForm(String category) {
		category = Hooks.currentRow.get().get("formcategory");
		Hooks.log("Selecting UNCD form category: " + category);
		submitDocument.selectUNCDForm(category);
		String filenamestring = Hooks.currentRow.get().get("filename");
		submitDocument.cleanupUploadedFiles(filenamestring);


	}

	@And("User should be able to select Supporting Documents as {string}")
	public void userShouldBeAbleToSelectSupportingDocumentsAs(String supportingdocument) {
		Hooks.log("Selecting Supporting Documents: " + supportingdocument);
		submitDocument.selectSupportingDocuments(supportingdocument);
	}

	@And("User should be able to upload a UNCD documents with {string}")
	public void userShouldBeAbleToUploadAUNCDDocumentsWith(String arg0) throws IOException {
		String originalFilenames = Hooks.currentRow.get().get("filenames");
		Hooks.log("Uploading documents: " + originalFilenames);
		// Call uploadDocuments which now returns the list of timestamped filenames
		List<String> timestampedFilenamesList = submitDocument.uploadDocuments(originalFilenames);
		// Join the timestamped filenames into a comma-separated string
		String timestampedFilenamesString = String.join(",", timestampedFilenamesList);

		Hooks.currentRow.get().put("filenames", timestampedFilenamesString);
		Hooks.log("Updated filenames in current row for verification: " + timestampedFilenamesString);


		// --- END: Placeholder for JSON modification logic ---
	}

	@And("User should be able to select UNCD WSIB forms {string}")
	public void userShouldBeAbleToSelectUNCDWSIBForms(String wsibforms) throws InterruptedException {
		wsibforms = Hooks.currentRow.get().get("wsibforms");
		Hooks.log("Selecting UNCD WSIB form: " + wsibforms);
		submitDocument.selectWSIBForm(wsibforms);
		String filenamestring = Hooks.currentRow.get().get("filenames");
		submitDocument.cleanupUploadedFiles(filenamestring);
	}

	@And("User should be able to select UNCD Non WSIB forms {string}, {string}")
	public void userShouldBeAbleToSelectUNCDNonWSIBForms(String categorys, String subforms) {
		categorys = Hooks.currentRow.get().get("nonwsibformscategory");
		subforms = Hooks.currentRow.get().get("subforms");
		Hooks.log("Selecting UNCD Non-WSIB form: Category=" + categorys + ", Subform=" + subforms);
		submitDocument.selectNonWSIBForm(categorys, subforms);
		String filenamestring = Hooks.currentRow.get().get("filenames");
		submitDocument.cleanupUploadedFiles(filenamestring);
	}
}
