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
import pages.WT.TravelExpense;
import pages.WT.WTLandingPage;
import utils.ConfigReader;

import java.time.Duration;

public class WorkerTravel {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private WTLandingPage WTlandingpage;
	private TravelExpense travelExpense;
	ConfigReader prop= Hooks.getConfigReader();
	public WorkerTravel() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		WTlandingpage = new WTLandingPage(driver, wait);
		travelExpense = new TravelExpense(driver, wait);
		homePage = new HomePage(driver, wait);
	}

	@Given("User is on landing page")
	public void userIsOnLandingPage() throws InterruptedException {
		Assert.assertTrue(WTlandingpage.verifyOnClaimantInformationPage());

	}

	@When("User enters claimant information such as {string}, {string}, {string}")
	public void userEntersClaimantInformationSuchAs(String claimnumber, String lastname, String dateOfbirth) {
		claimnumber = Hooks.currentRow.get().get("claimnumber");
		lastname = Hooks.currentRow.get().get("lastname");
		dateOfbirth = Hooks.currentRow.get().get("dateofbirth");
		WTlandingpage.ValidateClaimantInformation(claimnumber, lastname, dateOfbirth);
	}

	@Then("Enter data in Travel Expense screen")
	public void enterDataInTravelExpenseScreen() throws InterruptedException {
		String expenseType = Hooks.currentRow.get().get("expenseType");
		String Reason = Hooks.currentRow.get().get("Reason");
		String Expenses = Hooks.currentRow.get().get("Expenses");
		String TotalKM = Hooks.currentRow.get().get("TotalKM");
		String PubTransit = Hooks.currentRow.get().get("PubTransit");
		String Parking = Hooks.currentRow.get().get("Parking");
		String Others = Hooks.currentRow.get().get("Others");
		String AdditionalInfo = Hooks.currentRow.get().get("AdditionalInfo");
		Assert.assertTrue(travelExpense.enterExpenseDetails( expenseType, Reason, Expenses, TotalKM, PubTransit, Parking, Others, AdditionalInfo));

	}

	@And("Enter data in Other Expenses screen")
	public void enterDataInOtherExpensesScreen() {
		String OtherExpenseType = Hooks.currentRow.get().get("OtherExpenseType");
		String OtherExpenseDescription = Hooks.currentRow.get().get("OtherExpenseDescription");
		String OtherExpenseAmount = Hooks.currentRow.get().get("OtherExpenseAmount");
		String OtherAdditionalInfo = Hooks.currentRow.get().get("OtherAdditionalInfo");

		Assert.assertTrue(travelExpense.otherExpenseDetails( OtherExpenseType, OtherExpenseDescription, OtherExpenseAmount, OtherAdditionalInfo));
	}

	@And("User should be able to review and Submit the application")
	public void userShouldBeAbleToReviewAndSubmitTheApplication() {
		String email = Hooks.currentRow.get().get("email");
		Assert.assertTrue(travelExpense.reviewAndSubmit( email));
	}
}
