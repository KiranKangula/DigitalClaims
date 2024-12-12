package definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.HomePage;
import pages.LoginPage;
import pages.OSPCClaimOverview;
import utils.ConfigReader;

public class OSPCClaims {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private OSPCClaimOverview OSPCClaimOverview;
	ConfigReader prop=new ConfigReader();
	public OSPCClaims() throws Exception {
		driver = Hooks.driver;
		wait = new WebDriverWait(driver, 5);
		loginPage = new LoginPage(driver, wait);
		homePage = new HomePage(driver, wait);
		OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
	}

	@Given("User is on my Claim screen")
	public void userIsOnMyClaimScreen() {
		Assert.assertTrue(homePage.verifyOnHomePage());
	}


	@When("User clicks on ClaimNumber")
	public void userClicksOnClaimNumber() {
		homePage.clickClaim(prop.getProperty("claimnumber"));
	}

	@Then("User should be navigated to My Claims Overview screen")
	public void userShouldBeNavigatedToMyClaimsOverviewScreen() {
		Assert.assertTrue(OSPCClaimOverview.verifyOnClaimOverview());
	}

}
