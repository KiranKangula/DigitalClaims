package definitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.GuidewireClaimCenter.GuidewirePage;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static definitions.Hooks.getDriver;
import static pages.UNCD.AddClaimantInfo.firstname;
import static pages.UNCD.AddClaimantInfo.lastname;

public class GuidewireCC {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private GuidewirePage guidewirecc;
    ConfigReader prop= Hooks.getConfigReader();
    List<Map<String, String>> data = Hooks.data;
    public GuidewireCC() throws Exception {
        driver = getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
        loginPage = new LoginPage(driver, wait);
        homePage = new HomePage(driver, wait);
        guidewirecc = new GuidewirePage(driver, wait);
    }

    @Then("Switch to New tab and Login to ClaimCenter")
    public void switchToNewTabAndLoginToClaimCenter() {
        guidewirecc.switchtab();
        if(prop.getProperty("env").equalsIgnoreCase("BAT")) {
            guidewirecc.loginToGWCC(prop.getProperty("GWCC_Username"), prop.getProperty("GWCC_Password"));
        }
        else {
            getDriver().switchTo().defaultContent();
            getDriver().switchTo().frame("top_frame");
        }
    }

    @And("Search for {string}")
    public void searchForClaimnumber(String claimnumber) {
        String claim = Hooks.currentRow.get().get("claimnumber");
        Assert.assertTrue(guidewirecc.searchClaim(claim));
    }

    @And("Search for UNCD {string}")
    public void searchForUNCDClaimnumber(String claimnumber) {
        Assert.assertTrue(guidewirecc.searchClaim(claimnumber));
    }

    @Then("User should be able to see the message displayed from OSPC")
    public void userShouldBeAbleToSeeTheMessageDisplayedFromOSPC() {
        Assert.assertTrue(guidewirecc.verifySentMessage());
    }

    @And("Navigate to Portal Authorization screen")
    public void navigateToPortalAuthorizationScreen() {
        Assert.assertTrue(guidewirecc.navigatePortalAuthScreen());
    }

    @And("Remove the {string} from profile")
    public void removeTheFromProfile(String claimnumber) {
        claimnumber = Hooks.currentRow.get().get("Claim Number");
        Assert.assertTrue(guidewirecc.removeClaimfromPortalAuth(claimnumber));
    }

    @And("Search for Claim number")
    public void searchForClaimNumber() {
        Assert.assertTrue(guidewirecc.searchClaim(DirectDeposit.Claimnumber));
    }

    @And("Verify the document is landed in GWCC with {string}, {string}")
    public void verifyTheDocumentIsDisplayedIsLandedInGWCCWith(String Author, String Description) throws InterruptedException {
         Author=Hooks.currentRow.get().get("Author");
         Description=Hooks.currentRow.get().get("Description");
        Assert.assertTrue(guidewirecc.verifyLandedDocument(Author,Description));
    }

    @And("Verify the EDOB document is landed in GWCC with {string}, {string}")
    public void verifyTheEDOBDocumentIsDisplayedIsLandedInGWCCWith(String Author, String Description) throws InterruptedException {
        Assert.assertTrue(guidewirecc.verifyLandedDocument(Author,Description));
    }

    @Given("User is on GWlogin page")
    public void userIsOnGWloginPage() {
        Assert.assertTrue(guidewirecc.verifyonGWCC());
    }

    @When("User submit username and password")
    public void userSubmitUsernameAndPassword() {
        if(!prop.getProperty("env").equalsIgnoreCase("PPD")) {
            guidewirecc.loginToGWCC(prop.getProperty("GWCC_Username"), prop.getProperty("GWCC_Password"));
        }
    }

    @Then("User should be able to login successfully into GWCC")
    public void userShouldBeAbleToLoginSuccessfullyIntoGWCC() {
        Assert.assertTrue(guidewirecc.GWCCLoginsuccess());
    }

    @Given("User Logged into ClaimCenter")
    public void userLoggedIntoClaimCenter() {
        Assert.assertTrue(guidewirecc.GWCCLoginsuccess());
    }

    @When("User initiates Manual Registration")
    public void userInitiatesManualRegistration() {
        Assert.assertTrue(guidewirecc.KickManualRegistration());
    }

    @Then("User selects Source of Information as {string}")
    public void userSelectsSourceOfInformationAs(String source) {
        guidewirecc.selectSourceofInformation(source);
    }

    @And("User enters new worker information as below:")
    public void userEntersNewWorkerInformationAsBelow(DataTable dataTable) throws InterruptedException {
        guidewirecc.createNewWorker(dataTable);
    }

    @And("User searches for {string} with bussiness activity")
    public void userSearchesForWithBussinessActivity(String accountnumber) throws InterruptedException {
        guidewirecc.searchAccountNumber(accountnumber);
    }

    @And("User enters Accident Details as below:")
    public void userEntersAccidentDetailsAsBelow(DataTable dataTable) throws InterruptedException {
        guidewirecc.enterAccidentDetails(dataTable);
    }

    @And("User Enters Body part details as below:")
    public void userEntersBodyPartDetailsAsBelow(DataTable dataTable) throws InterruptedException {
        guidewirecc.enterBodyPartDetails(dataTable);
    }

    @And("Select Third Party accident as {string}")
    public void selectThirdPartyAccidentAs(String TPA) throws InterruptedException {
        guidewirecc.TPASelection(TPA);
    }

    @And("Select Accident Location from the dropdown")
    public void selectAccidentLocationFromTheDropdown() throws InterruptedException {
        guidewirecc.selectAccidentLocation();
    }

    @And("Select Health Care Received as {string}")
    public void selectHealthCareReceivedAs(String HCIndicator) throws InterruptedException {
        guidewirecc.selectHealthCareRecieved(HCIndicator);
    }

    @And("Select Claim Type as {string}")
    public void selectClaimTypeAs(String claimtype) throws InterruptedException {
        guidewirecc.selectClaimType(claimtype);
    }

    @And("Click on Register Claim button and verify that Claim has been registered Successfully")
    public void clickOnRegisterClaimButtonAndVerifyThatClaimHasBeenRegisteredSuccessfully() throws InterruptedException, IOException {
        String claimdata[] = guidewirecc.registerClaim();
        System.out.println("ClaimData  " + claimdata[0] + "-" + claimdata[1] + "-" + claimdata[2]);


        // Update the current row data with the new values
        Map<String, String> currentRowData = Hooks.currentRow.get();
        currentRowData.put("Claim Number", claimdata[0]);
        currentRowData.put("DOB", claimdata[1]);
        currentRowData.put("PIN", claimdata[2]);

        // Save the updated row data back to the Excel file
        String sheetName = Hooks.sheetName;
        ExcelUtils.updateExcelData(sheetName, currentRowData);

    }

    @And("Search for Reference Number")
    public void searchForReferenceNumber() {
    String referenceNumber = Hooks.currentRow.get().get("ReferenceNumber");
        Assert.assertTrue(guidewirecc.searchReference(referenceNumber));
    }

    @And("Verify intake and Worker Information and Validate worker Address")
    public void verifyIntakeAndWorkerInformationAndValidateWorkerAddress() {
        String Fname = Hooks.currentRow.get().get("FirstName");
        String Lname = Hooks.currentRow.get().get("LastName");
        String Gender = Hooks.currentRow.get().get("Gender");
        String Pronouns = Hooks.currentRow.get().get("Pronouns");
        guidewirecc.verifyIntakeWorkerInformation(Fname, Lname, Gender, Pronouns);
    }

    @And("Search with firstname and lastname & dateofbirth {string}")
    public void searchWithFirstnameAndLastnameDateofbirth(String dateofbirth) throws InterruptedException {
        Assert.assertTrue(guidewirecc.searchClaimant(firstname, lastname, dateofbirth));

    }

    @And("Verify the UNCD document is landed in GWCC with {string}, {string}")
    public void verifyTheUNCDDocumentIsLandedInGWCCWith(String Author, String Description) throws InterruptedException {
        Assert.assertTrue(guidewirecc.verifyLandedUNCDDocument(Author,Description));
    }
}
