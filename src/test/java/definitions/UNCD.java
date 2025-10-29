package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import pages.UNCD.AddClaimantInfo;
import pages.UNCD.GetStarted;
import pages.UNCD.ReviewSubmission;
import pages.UNCD.SubmitDocuments;
import utils.ConfigReader;

import java.time.Duration;

public class UNCD {
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
		Assert.assertTrue(getStarted.verifyOnGetStartedPage());
	}

	@When("User clicks on Submitting a document for an existing claim")
	public void userClicksOnSubmittingADocumentForAnExistingClaim() {
		getStarted.SelectNumberedDocument();
	}

	@Then("User enters claim information such as {string}, {string}, {string}")
	public void userEntersClaimInformation(String claimnumber, String lastname, String dateofbirth) {
		addClaimantInfo.AddClaimantInformation(claimnumber, lastname, dateofbirth);
	}

	@And("User should be able to upload a UNCD document with {string}")
	public void userShouldBeAbleToUploadAUNCDDocumentWith(String filename) {
			Assert.assertTrue(submitDocument.uploadDocument(filename));
	}

	@And("User should be able to select UNCD WSIB form {string}")
	public void userShouldBeAbleToSelectUNCDWSIBForm(String wsibform) throws InterruptedException {
		submitDocument.selectWSIBForm(wsibform);

	}

	@And("Select delaration and Email consent checkbox and Click on Submit button")
	public void selectDelarationAndEmailConsentCheckboxAndClickOnSubmitButton() {
		Assert.assertTrue(reviewSubmission.submitDocument());

	}

	@And("Verify the confirmation screen is displayed")
	public void verifyTheConfirmationScreenIsDisplayed() {
		Assert.assertTrue(reviewSubmission.verifyOnConfirmationScreen());
	}

	@And("User should be able to select UNCD Non WSIB form {string}, {string}")
	public void userShouldBeAbleToSelectUNCDNonWSIBForm(String category, String subform) throws InterruptedException {
		submitDocument.selectNonWSIBForm(category, subform);

	}

	@When("User clicks on Reporting a new injury or exposure")
	public void userClicksOnReportingANewInjuryOrExposure() {
		getStarted.SelectUnNumberedDocument();
	}

	@Then("User enters claim information such as {string}, {string}, {string}, {string}")
	public void userEntersClaimInformationSuchAs(String firstname, String lastname, String dateofbirth, String dateofinjury) {
		addClaimantInfo.AddClaimantInformationUNCD(firstname, lastname, dateofbirth, dateofinjury);


	}

	@And("User should be able to select UNCD form {string}")
	public void userShouldBeAbleToSelectUNCDForm(String category) {
		submitDocument.selectUNCDForm(category);


	}

	@And("User should be able to select Supporting Documents as {string}")
	public void userShouldBeAbleToSelectSupportingDocumentsAs(String supportingdocument) {
	submitDocument.selectSupportingDocuments(supportingdocument);
	}
}
