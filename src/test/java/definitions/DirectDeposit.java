package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.sql.DriverManager.getDriver;

public class DirectDeposit {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private GuidewirePage guidewirecc;
	private OSPCClaimOverview OSPCClaimOverview;
	private OSPCDirectDeposit OSPCDirectDeposit;
	ConfigReader prop= Hooks.getConfigReader();
	public static String Claimnumber;
	public DirectDeposit() throws Exception {
        driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		homePage = new HomePage(driver, wait);
		guidewirecc = new GuidewirePage(driver, wait);
		OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
		OSPCDirectDeposit = new OSPCDirectDeposit(driver, wait);
	}

	@When("User clicks on Direct Deposit")
	public void userClicksOnDirectDeposit() {
		Assert.assertTrue(OSPCDirectDeposit.LandOnDirectDeposit());
	}

	@Then("Click Add direct Deposit information and Select {string} and {string}")
	public void clickAddDirectDepositInformationAndSelectAnd(String Month, String Year) {
		Assert.assertTrue(OSPCDirectDeposit.AddDirectDepositInformation(Month, Year));
	}

	@And("Enter {string}, {string}, {string} and Confirm details")
	public void enterAndConfirmDetails(String Transit, String Institution, String accountNumber) {
		Assert.assertTrue(OSPCDirectDeposit.AddandValidateDDInformation(Transit, Institution,accountNumber ));
		 Claimnumber = OSPCDirectDeposit.getClaimNumberfromOSPC();
	}

	@And("Remove the Direct Deposit from the Claim")
	public void removeTheDirectDepositFromTheClaim() {
		Assert.assertTrue(guidewirecc.removeDirectDeposit());
	}

	@And("Verify the Direct Deposit is not displayed in OSPC")
	public void verifyTheDirectDepositIsNotDisplayedInOSPC() {
		Assert.assertTrue(OSPCDirectDeposit.verifyDirectDepositRemovalinOSPC());
	}

	@Then("Click Edit direct Deposit information and Select {string} and {string}")
	public void clickEditDirectDepositInformationAndSelectAnd(String Month, String Year) {
		Assert.assertTrue(OSPCDirectDeposit.editDirectDepositInformation(Month, Year));
	}
}


