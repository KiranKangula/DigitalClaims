package definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import utils.ConfigReader;

import java.time.Duration;

public class LoginDefinition {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	ConfigReader prop= Hooks.getConfigReader();
	public LoginDefinition() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		homePage = new HomePage(driver, wait);
	}

	@Given("^User is on login page$")
	public void loginPage() {
		Assert.assertTrue(loginPage.verifyOnLoginPage());
	}

	@When("^User submit email and password$")
	public void submitEmailPassword() {
		loginPage.loginToSite(prop.getProperty("OSPC_username"), prop.getProperty("OSPC_Password"));
	}

	@Then("^User should be able to login successfully and new page open$")
	public void successLogin() {
		Assert.assertTrue(homePage.verifyOnHomePage());
	}	
}
