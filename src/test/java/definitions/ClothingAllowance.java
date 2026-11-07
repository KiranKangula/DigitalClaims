package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CAA.Clothingallowance;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import pages.WT.TravelExpense;
import pages.WT.WTLandingPage;
import utils.ConfigReader;

import java.time.Duration;

public class ClothingAllowance {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private WTLandingPage WTlandingpage;
	private Clothingallowance clothingallowance;
	ConfigReader prop= Hooks.getConfigReader();
	ConfigReader common = Hooks.getCommonReader();
	public ClothingAllowance() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		WTlandingpage = new WTLandingPage(driver, wait);
		clothingallowance = new Clothingallowance(driver, wait);
		homePage = new HomePage(driver, wait);
	}


//	@When("User enters claimant information such as {string}, {string}, {string}")
//	public void userEntersClaimantInformationSuchAs(String claimnumber, String lastname, String dateOfbirth) {
//		claimnumber = Hooks.currentRow.get().get("claimnumber");
//		lastname = Hooks.currentRow.get().get("lastname");
//		dateOfbirth = Hooks.currentRow.get().get("dateofbirth");
//		WTlandingpage.ValidateClaimantInformation(claimnumber, lastname, dateOfbirth);
//	}


//	@And("User should be able to review and Submit the application")
//	public void userShouldBeAbleToReviewAndSubmitTheApplication() {
//		String email = Hooks.currentRow.get().get("email");
//		Assert.assertTrue(clothingallowance.reviewAndSubmit( email));
//	}

	@Then("Enter data in Clothing allowance application screen")
	public void enterDataInClothingAllowanceApplicationScreen() throws InterruptedException {
		String phonenumber = Hooks.currentRow.get().get("phonenumber");
		String ppdNel= Hooks.currentRow.get().get("ppdNel");
		String receivedCa = Hooks.currentRow.get().get("receivedCa");
		String otherClaimNumber = Hooks.currentRow.get().get("otherClaimNumber");
		String isEmployed = Hooks.currentRow.get().get("isEmployed");
		String usesDeviceAtWork = Hooks.currentRow.get().get("usesDeviceAtWork");
		String employerPaysForUniform = Hooks.currentRow.get().get("employerPaysForUniform");
		String uniformOverClothing = Hooks.currentRow.get().get("uniformOverClothing");
		String damage = Hooks.currentRow.get().get("damage");
		String date = Hooks.currentRow.get().get("startDate");
		String hoursPerDay = Hooks.currentRow.get().get("hoursPerDay");
		String daysPerWeek = Hooks.currentRow.get().get("daysPerWeek");
		String UpperBody = Hooks.currentRow.get().get("UpperBody");
		String LowerBody = Hooks.currentRow.get().get("LowerBody");
		String OtherDevice = Hooks.currentRow.get().get("OtherDevice");
		String metalParts = Hooks.currentRow.get().get("metalParts");
		String repairOrReplaced = Hooks.currentRow.get().get("repairOrReplaced");
		Hooks.log("Entering data in Clothing Allowance application screen for phone: " + phonenumber);
		Assert.assertTrue(clothingallowance.enterClothingAllowanceDetails(phonenumber, ppdNel, receivedCa, otherClaimNumber, isEmployed, usesDeviceAtWork, employerPaysForUniform, uniformOverClothing, damage, date, hoursPerDay, daysPerWeek, UpperBody, LowerBody, OtherDevice, metalParts, repairOrReplaced));

	}
}
