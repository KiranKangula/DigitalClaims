package definitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DirectDeposit.OSPCDirectDeposit;
import pages.Guidewire.GWClaimParticipantsPage;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import pages.OSPC.OSPCClaimOverview;
import utils.ConfigReader;
import utils.ExternalValidation;

import java.time.Duration;

import static utils.BaseDriverClass.getDateFormat;

public class DirectDeposit {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private GWClaimParticipantsPage gwParticipants;
	private pages.OSPC.OSPCClaimOverview OSPCClaimOverview;
	private pages.DirectDeposit.OSPCDirectDeposit OSPCDirectDeposit;
	ConfigReader prop= Hooks.getConfigReader();
	ConfigReader common = Hooks.getCommonReader();
	public static String Claimnumber;
	public DirectDeposit() throws Exception {
        driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		homePage = new HomePage(driver, wait);
		gwParticipants = new GWClaimParticipantsPage(driver, wait);
		OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
		OSPCDirectDeposit = new OSPCDirectDeposit(driver, wait);
	}

	@When("User clicks on Direct Deposit")
	public void userClicksOnDirectDeposit() throws InterruptedException {
		Hooks.log("Clicking on Direct Deposit link.");
		Assert.assertTrue(OSPCDirectDeposit.LandOnDirectDeposit());
	}

	@Then("Verify your identity to set up and manage direct deposit")
	public void clickAddDirectDepositInformationAndSelectAnd() {
		Hooks.log("Verify your identity to set up and manage direct deposit: ");
		Assert.assertTrue(OSPCDirectDeposit.AddDirectDepositInformation());
	}

	@And("Enter banking information and Confirm details")
	public void enterAndConfirmDetails() {
		String Transit = Hooks.currentRow.get().get("TransitNumber");
		String Institution= Hooks.currentRow.get().get("InstitutionNumber");
		String accountNumber= Hooks.currentRow.get().get("AccountNumber");

		Hooks.log("Entering Direct Deposit details: Transit=" + Transit + ", Institution=" + Institution);
		Assert.assertTrue(OSPCDirectDeposit.AddandValidateDDInformation(Transit, Institution,accountNumber ));
		 Claimnumber = OSPCDirectDeposit.getClaimNumberfromOSPC();
		 Hooks.log("Captured Claim Number: " + Claimnumber);
	}

	@And("Remove the Direct Deposit from the Claim")
	public void removeTheDirectDepositFromTheClaim() {
		Hooks.log("Removing Direct Deposit from the Claim in GWCC.");
		Assert.assertTrue(gwParticipants.removeDirectDeposit());
	}

	@And("Verify the Direct Deposit is not displayed in OSPC")
	public void verifyTheDirectDepositIsNotDisplayedInOSPC() {
		Hooks.log("Verifying Direct Deposit removal in OSPC.");
		Assert.assertTrue(OSPCDirectDeposit.verifyDirectDepositRemovalinOSPC());
	}

	@Then("Click Edit direct Deposit information and Select {string} and {string}")
	public void clickEditDirectDepositInformationAndSelectAnd(String Month, String Year) {
		String newDate= getDateFormat(Hooks.currentRow.get().get("DOB"), "d/MMMM/yyyy", "dd/M/yyyy") ;
		Month= newDate.split("/")[1];
		Year= newDate.split("/")[2];
		Hooks.log("Editing Direct Deposit information for DOB Month: " + Month + ", Year: " + Year);
		Assert.assertTrue(OSPCDirectDeposit.editDirectDepositInformation(Month, Year));
	}

	@And("Verify the Direct Deposit is displayed in ClaimCenter")
	public void verifyTheDirectDepositIsDisplayedInClaimCenter() {
		// Write code here that turns the phrase above into concrete actions
		if (!ExternalValidation.shouldRun(
				"Verify the Direct Deposit is displayed in ClaimCenter")) {
			return;
		}
		Assert.assertTrue(gwParticipants.addDirectDeposit());


	}
}
