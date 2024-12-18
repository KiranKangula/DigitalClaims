package definitions;

import io.cucumber.datatable.DataTable;
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
import java.util.List;
import java.util.Map;

public class GuidewireCC {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private GuidewirePage guidewirecc;
    ConfigReader prop= Hooks.getConfigReader();

    public GuidewireCC() throws Exception {
        driver = Hooks.driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
        loginPage = new LoginPage(driver, wait);
        homePage = new HomePage(driver, wait);
        guidewirecc = new GuidewirePage(driver, wait);
    }

    @Then("Switch to New tab and Login to ClaimCenter")
    public void switchToNewTabAndLoginToClaimCenter() {
        guidewirecc.switchtab();
        guidewirecc.loginToGWCC(prop.getProperty("GWCC_Username"), prop.getProperty("GWCC_Password") );
    }

    @And("Search for {string}")
    public void searchForClaimnumber(String claimnumber) {
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
        Assert.assertTrue(guidewirecc.removeClaimfromPortalAuth(claimnumber));
    }

    @And("Search for Claim number")
    public void searchForClaimNumber() {
        Assert.assertTrue(guidewirecc.searchClaim(DirectDeposit.Claimnumber));
    }

    @And("Verify the document is landed in GWCC with {string}, {string}")
    public void verifyTheDocumentIsDisplayedIsLandedInGWCCWith(String Author, String Description) throws InterruptedException {
        Assert.assertTrue(guidewirecc.verifyLandedDocument(Author,Description));
    }

    @Given("User is on GWlogin page")
    public void userIsOnGWloginPage() {
        Assert.assertTrue(guidewirecc.verifyonGWCC());
    }

    @When("User submit username and password")
    public void userSubmitUsernameAndPassword() {
        guidewirecc.loginToGWCC(prop.getProperty("GWCC_Username"), prop.getProperty("GWCC_Password") );
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
    public void userEntersNewWorkerInformationAsBelow() {

    }
}
