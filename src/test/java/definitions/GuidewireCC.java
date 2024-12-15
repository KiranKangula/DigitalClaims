package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.GuidewirePage;
import pages.HomePage;
import pages.LoginPage;
import pages.OSPCClaimOverview;
import utils.ConfigReader;

import java.time.Duration;

public class GuidewireCC {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private GuidewirePage guidewirecc;
    ConfigReader prop=new ConfigReader();

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
}
