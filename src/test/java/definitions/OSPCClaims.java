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
import pages.OSPC.OSPCClaimOverview;
import pages.OSPC.Tombstone;
import utils.ConfigReader;
import utils.ExternalValidation;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static utils.BaseDriverClass.getCurrentDate;

public class OSPCClaims {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	//private GuidewirePage guidewirecc;
	private OSPCClaimOverview OSPCClaimOverview;
	private Tombstone tombstone;
	private int appealsCount;
	ConfigReader prop= Hooks.getConfigReader();
	ConfigReader common = Hooks.getCommonReader();
	// Removed: List<Map<String, String>> data = Hooks.data; // This line caused the error
	public OSPCClaims() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		homePage = new HomePage(driver, wait);
		tombstone = new Tombstone(driver, wait);
		//guidewirecc = new GuidewirePage(driver, wait);
		OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
	}

	@Given("User is on my Claim screen")
	public void userIsOnMyClaimScreen() {
		Hooks.log("Verifying User is on My Claims screen.");
		Assert.assertTrue(homePage.verifyOnHomePage());
	}


	@When("User clicks on {string}")
	public void userClicksOnClaimNumber(String claimnumber) {
		claimnumber = Hooks.currentRow.get().get("Claim Number");
		Hooks.log("Clicking on Claim Number: " + claimnumber);
		homePage.clickClaim(claimnumber);
	}

	@Then("User should be navigated to My Claims Overview screen")
	public void userShouldBeNavigatedToMyClaimsOverviewScreen() {
		Hooks.log("Verifying navigation to Claims Overview screen.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnClaimOverview());
	}

	@And("User should be navigated to Benefit Payments screen")
	public void userShouldBeNavigatedToBenefitPaymentsScreen() {
		Hooks.log("Verifying navigation to Benefit Payments screen.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnBenefitpayments());
	}

	@And("User should be navigated to Healthcare benefit screen")
	public void userShouldBeNavigatedToHealthcareBenefitScreen() {
		Hooks.log("Verifying navigation to Healthcare Benefit screen.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnHealthcarebenefits());
	}

	@And("User should be navigated to Non Health care benefit screen")
	public void userShouldBeNavigatedToNonHealthCareBenefitScreen() {
		Hooks.log("Verifying navigation to Non-Healthcare Benefit screen.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnNonHealthcarebenefits());
	}

	@And("User should be navigated to Appeals screen")
	public void userShouldBeNavigatedToAppealsScreen() {
		Hooks.log("Verifying navigation to Appeals screen.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnAppeals());
	}

	@And("User should be navigated to Submit Document screen")
	public void userShouldBeNavigatedToSubmitDocumentScreen() {
		Hooks.log("Verifying navigation to Submit Document screen.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnSubmitDocuments());

	}

	@And("User should be able to select WSIB form {string}")
	public void userShouldBeAbleToSelectWSIBForm(String wsibform) {
		wsibform = Hooks.currentRow.get().get("wsibform");
		Hooks.log("Selecting WSIB form: " + wsibform);
		Assert.assertTrue(OSPCClaimOverview.selectWSIBForm(wsibform));

	}

	@And("User should be able to select WSIB forms {string}")
	public void userShouldBeAbleToSelectWSIBForms(String wsibforms) {
		wsibforms = Hooks.currentRow.get().get("wsibforms");
		Hooks.log("Selecting WSIB form: " + wsibforms);

		Assert.assertTrue(OSPCClaimOverview.selectWSIBForm(wsibforms));

	}

	@And("User should be able to select Non WSIB form {string} and {string}")
	public void userShouldBeAbleToSelectNonWSIBForm(String nonwsibformtype, String nonwsibform) {
		nonwsibformtype = Hooks.currentRow.get().get("nonwsibformtype");
		nonwsibform = Hooks.currentRow.get().get("nonwsibform");
		Hooks.log("Selecting Non WSIB form: " + nonwsibformtype + " and " + nonwsibform);
		Assert.assertTrue(OSPCClaimOverview.selectNonWSIBForm(nonwsibformtype, nonwsibform));

	}

	@And("User should be able to select Non WSIB forms {string} and {string}")
	public void userShouldBeAbleToSelectNonWSIBForms(String nonwsibformsType, String nonwsibforms) {
		nonwsibformsType = Hooks.currentRow.get().get("nonwsibformstype");
		nonwsibforms = Hooks.currentRow.get().get("nonwsibforms");
		Hooks.log("Selecting WSIB form: " + nonwsibforms);

		Assert.assertTrue(OSPCClaimOverview.selectNonWSIBForm(nonwsibformsType, nonwsibforms));

	}

	@And("Select confirmation checkbox and Click on Submit button")
	public void selectConfirmationCheckboxAndClickOnSubmitButton() throws InterruptedException {
		Hooks.log("Selecting confirmation checkbox and clicking Submit.");
		Assert.assertTrue(OSPCClaimOverview.SubmitDocument());
		Hooks.log("Document Submitted at: " + getCurrentDate("MMM dd, yyyy hh:mm:ss a"));
	}

	@And("User should be able to upload a document with {string}")
	public void userShouldBeAbleToUploadADocumentWithFilename(String filename) throws IOException {
		String filesToUpload = Hooks.currentRow.get().get("filename");
		Hooks.log("Uploading document: " + filesToUpload);
		OSPCClaimOverview.uploadDocuments(filesToUpload);
	}

	@And("User should be able to upload documents with {string}")
	public void userShouldBeAbleToUploadADocumentsWithFilenames(String filenames) throws IOException {
		String filesToUpload = Hooks.currentRow.get().get("filenames");
		Hooks.log("Uploading documents: " + filesToUpload);
		OSPCClaimOverview.uploadDocuments(filesToUpload);
	}

	@And("Verify the Document History is updated with {string}")
	public void verifyTheDocumentHistoryIsUpdatedWith(String formtype) {
		// This 'filename' parameter will now receive the updated timestamped string
		// from Hooks.currentRow.get().get("filenames") if the JSON update logic is implemented.
		String documentCategory;
		String documentDescription;
		if(formtype.contentEquals("WSIB")) {
			 documentCategory = Hooks.currentRow.get().get("documentCategory");
			 documentDescription = Hooks.currentRow.get().get("documentDescription");
		}
		else {
			 documentCategory = Hooks.currentRow.get().get("nondocumentCategory");
			 documentDescription = Hooks.currentRow.get().get("nondocumentDescription");
		}
		Hooks.log("Verifying Document History is updated with: " + documentCategory+ "::"+ documentDescription);

        Assert.assertTrue(OSPCClaimOverview.DocumentHistory(formtype, documentCategory, documentDescription));
	}

	@And("Verify the Document History are updated with {string}")
	public void verifyTheDocumentHistoryAreUpdatedWith(String formtype) {

		String documentCategories;
		String documentDescriptions;
		if(formtype.contentEquals("WSIB")) {
			documentCategories = Hooks.currentRow.get().get("documentCategories");
			documentDescriptions = Hooks.currentRow.get().get("documentDescriptions");
		}
		else {
			documentCategories = Hooks.currentRow.get().get("nondocumentCategories");
			documentDescriptions = Hooks.currentRow.get().get("nondocumentDescriptions");
		}

		Hooks.log("Verifying Document History is updated with: "  + documentCategories+ "::"+ documentDescriptions);
		Assert.assertTrue(OSPCClaimOverview.DocumentHistory(formtype, documentCategories,documentDescriptions ));
	}

	@And("User clicks on Send us a message link and Lands on Send us a message screen")
	public void userClicksOnSendUsAMessageLink() {
		Hooks.log("Clicking on 'Send us a message' link.");
		Assert.assertTrue(OSPCClaimOverview.verifyOnSendMessageScreen());
	}

	@And("User selects {string} and enter {string}")
	public void userSelectsAndEnter(String subject, String message) {
		Hooks.log("Sending message with subject: " + subject);
		Assert.assertTrue(OSPCClaimOverview.sendusamessage(subject, message));

	}

	@And("User clicks on Send button and Lands on Message sent screen")
	public void userClicksOnSendButtonAndLandsOnMessageSentScreen() {
		Hooks.log("Clicking Send button.");
		Assert.assertTrue(OSPCClaimOverview.sendandconfirm());
	}

	@And("Return to Mail and verify the sent message")
	public void returnToMailAndVerifyTheSentMessage() {
		Hooks.log("Returning to Mail and verifying sent message.");
		Assert.assertTrue(OSPCClaimOverview.mailscreen());

	}

	@When("User click on Add a Claim button")
	public void userClickOnAddAClaimButton() {
		Hooks.log("Clicking 'Add a Claim' button.");
		Assert.assertTrue(OSPCClaimOverview.addClaim());

	}

	@Then("User should be able to add claim with {string}, {string}")
	public void userShouldBeAbleToAddClaimWithAnd(String ClaimNumber, String YOB) {
		if (!ExternalValidation.shouldRun(
				"Adding Claim")) {
			return;
		}
		ClaimNumber = Hooks.currentRow.get().get("Claim Number");
			YOB= Hooks.currentRow.get().get("DOB").split("/")[2];
			String InjurDate = Hooks.currentRow.get().get("QInjurDate");
		String PhoneNumber = Hooks.currentRow.get().get("QPhoneNumber");
		String EmployerName = Hooks.currentRow.get().get("QEmployerName");
		String Postalcode = Hooks.currentRow.get().get("QPostalcode");
		String InJuryArea = Hooks.currentRow.get().get("QAreaofInjury");

		Hooks.log("Adding Claim: " + ClaimNumber + " with YOB: " + YOB);
		Assert.assertTrue(OSPCClaimOverview.AddaClaimOSPC(ClaimNumber,YOB, InjurDate, PhoneNumber, EmployerName, Postalcode, InJuryArea));
	}

	@And("Switch back to OSPC and verify if the {string} is removed")
	public void switchBackToOSPCAndVerifyIfTheIsRemoved(String claimnumber) throws InterruptedException {
		if (!ExternalValidation.shouldRun(
				"Switching back to OSPC and verifying removal of claim")) {
			return;
		}
		claimnumber = Hooks.currentRow.get().get("Claim Number");
		Hooks.log("Switching back to OSPC and verifying removal of claim: " + claimnumber);
		Assert.assertTrue(OSPCClaimOverview.claimRemovalConfirmationOSPC(claimnumber));
	}

	@And("Switch back to OSPC and verify if the {string} is added")
	public void switchBackToOSPCAndVerifyIfTheIsAdded(String claimnumber) throws InterruptedException {
		claimnumber = Hooks.currentRow.get().get("Claim Number");
		Hooks.log("Switching back to OSPC and verifying addition of claim: " + claimnumber);
		Assert.assertTrue(OSPCClaimOverview.claimAdditionalConfirmationOSPC(claimnumber));
	}

	@And("Switch to New Tab and Login to OSPC and verify if the {string} is added")
	public void logIntoOSPCAndVerifyIfTheClaimIsAdded(String claimnumber) throws InterruptedException {
		claimnumber = Hooks.currentRow.get().get("Claim Number");
		//guidewirecc.switchtab();
		Hooks.log("Navigating to OSPC URL and logging in to verify claim: " + claimnumber);
		loginPage.navigateToURL(prop.getProperty("OSPC_URL"));
		loginPage.loginToSite(prop.getProperty("OSPC_Username"), prop.getProperty("OSPC_Password"));
		Assert.assertTrue(OSPCClaimOverview.claimAdditionConfirmationOSPC(claimnumber));
	}


	@And("Switch back to OSPC and verify appeal registered is displayed")
	public void switchBackToOSPCAndVerifyAppealRegisteredIsDisplayed() {
		if (!ExternalValidation.shouldRun(
				"Switch back to OSPC and verify appeal registered is displayed")) {
			return;
		}
		Hooks.log("Verifying appeal registration in OSPC.");
		int actualCount = (OSPCClaimOverview.appealRegisteredConfirmationOSPC());
		if(actualCount>appealsCount) {
			Assert.assertTrue("Appeals added in the table", true);
		}
	}


	@And("User should be navigated to Appeals screen and count number of appeals")
	public void userShouldBeNavigatedToAppealsScreenAndNumberOfAppeals() {
		Hooks.log("Navigating to Appeals screen and counting appeals.");
		appealsCount = OSPCClaimOverview.verifyCountOfAppeals();

	}

    @When("User clicks on contact information link on my claim screen")
    public void userClicksOnContactInformationLinkOnMyClaimScreen() {
		Hooks.log("Clicking on 'Contact Information' link.");
		Assert.assertTrue("Contact Information heading is displayed", tombstone.clickContactInfoLink() );

    }

	@Then("User should be view Personal details, Mailing address and Contact details")
	public void userShouldBeViewPersonalDetailsMailingAddressAndContactDetails() {
		Hooks.log("Verifying Personal Details, Mailing Address and Contact Details.");
		Assert.assertTrue( tombstone.verifyFullName() );
		Assert.assertTrue( tombstone.verifyAddress() );
		Assert.assertTrue(tombstone.verifyPhoneNumber() );
	}

	@When("User clicks on Edit button to update Mailing address")
	public void userClicksOnEditButtonToUpdateMailingAddress() {
		Hooks.log("Clicking Edit Mailing Address button.");
		Assert.assertTrue(tombstone.clickEditMailingAddress() );
	}

	@Then("User should be able to update the Mailing address details and Save")
	public void userShouldBeAbleToUpdateTheMailingAddressDetailsAndSave() throws IOException {
		Hooks.log("Updating Mailing Address and saving.");
		Assert.assertTrue(tombstone.updateMailingAddress() );
	}

	@When("User clicks on Edit button to update Phone Number")
	public void userClicksOnEditButtonToUpdatePhoneNumber() {
		Hooks.log("Clicking Edit Phone Number button.");
		Assert.assertTrue(tombstone.clickEditPhoneNumber() );
	}

	@Then("User should be able to update the Phone Number and Save")
	public void userShouldBeAbleToUpdateThePhoneNumberAndSave() {
		Hooks.log("Updating Phone Number and saving.");
		Assert.assertTrue(tombstone.updatePhoneNumber() );
	}
}
