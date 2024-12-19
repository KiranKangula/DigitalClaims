package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.GuidewirePage;
import pages.HomePage;
import pages.LoginPage;
import pages.OSPCClaimOverview;
import utils.ConfigReader;

import java.time.Duration;

public class OSPCClaims {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private GuidewirePage guidewirecc;
	private OSPCClaimOverview OSPCClaimOverview;
	ConfigReader prop= Hooks.getConfigReader();
	public OSPCClaims() throws Exception {
		driver = Hooks.driver;
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		homePage = new HomePage(driver, wait);
		guidewirecc = new GuidewirePage(driver, wait);
		OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
	}

	@Given("User is on my Claim screen")
	public void userIsOnMyClaimScreen() {
		Assert.assertTrue(homePage.verifyOnHomePage());
	}


	@When("User clicks on {string}")
	public void userClicksOnClaimNumber(String claimnumber) {
		homePage.clickClaim(claimnumber);
	}

	@Then("User should be navigated to My Claims Overview screen")
	public void userShouldBeNavigatedToMyClaimsOverviewScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnClaimOverview());
	}

	@And("User should be navigated to Benefit Payments screen")
	public void userShouldBeNavigatedToBenefitPaymentsScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnBenefitpayments());
	}

	@And("User should be navigated to Healthcare benefit screen")
	public void userShouldBeNavigatedToHealthcareBenefitScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnHealthcarebenefits());
	}

	@And("User should be navigated to Non Health care benefit screen")
	public void userShouldBeNavigatedToNonHealthCareBenefitScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnNonHealthcarebenefits());
	}

	@And("User should be navigated to Appeals screen")
	public void userShouldBeNavigatedToAppealsScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnAppeals());
	}

	@And("User should be navigated to Submit Document screen")
	public void userShouldBeNavigatedToSubmitDocumentScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnSubmitDocuments());

	}

	@And("User should be able to select WSIB form {string}")
	public void userShouldBeAbleToSelectWSIBForm(String wsibform) {
		Assert.assertTrue(OSPCClaimOverview.selectWSIBForm(wsibform));

	}

	@And("Select confirmation checkbox and Click on Submit button")
	public void selectConfirmationCheckboxAndClickOnSubmitButton() throws InterruptedException {
		Assert.assertTrue(OSPCClaimOverview.SubmitDocument());
	}

	@And("User should be able to upload a document with {string}")
	public void userShouldBeAbleToUploadADocumentWithFilename(String filename) {
		Assert.assertTrue(OSPCClaimOverview.uploadDocuments(filename));
	}

	@And("Verify the Document History is updated with {string}")
	public void verifyTheDocumentHistoryIsUpdatedWith(String filename) {
		Assert.assertTrue(OSPCClaimOverview.DocumentHistory(filename));
	}

	@And("User clicks on Send us a message link and Lands on Send us a message screen")
	public void userClicksOnSendUsAMessageLink() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnSendMessageScreen());
	}

	@And("User selects {string} and enter {string}")
	public void userSelectsAndEnter(String subject, String message) {

		Assert.assertTrue(OSPCClaimOverview.sendusamessage(subject, message));

	}

	@And("User clicks on Send button and Lands on Message sent screen")
	public void userClicksOnSendButtonAndLandsOnMessageSentScreen() {
		Assert.assertTrue(OSPCClaimOverview.sendandconfirm());
	}

	@And("Return to Mail and verify the sent message")
	public void returnToMailAndVerifyTheSentMessage() {
		Assert.assertTrue(OSPCClaimOverview.mailscreen());

	}

	@When("User click on Add a Claim button")
	public void userClickOnAddAClaimButton() {
		Assert.assertTrue(OSPCClaimOverview.addClaim());

	}

	@Then("User should be able to add claim with {string}, {string} and {string}")
	public void userShouldBeAbleToAddClaimWithAnd(String Claimnumber, String YOB, String PIN) {
		
		Assert.assertTrue(OSPCClaimOverview.AddaClaimOSPC(Claimnumber,YOB, PIN, guidewirecc));
	}

	@And("Switch back to OSPC and verify if the {string} is removed")
	public void switchBackToOSPCAndVerifyIfTheIsRemoved(String claimnumber) throws InterruptedException {
		Assert.assertTrue(OSPCClaimOverview.claimRemovalConfirmationOSPC(claimnumber));
	}
}
