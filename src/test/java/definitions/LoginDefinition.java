package definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.WebDriverWait;

import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import pages.UNCD.GetStarted;
import utils.ConfigReader;

import java.io.FileOutputStream;
import java.time.Duration;

public class LoginDefinition {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private GetStarted getStarted;
	ConfigReader prop= Hooks.getConfigReader();
	ConfigReader common = Hooks.getCommonReader();
	public LoginDefinition() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		getStarted = new GetStarted(driver, wait);
		homePage = new HomePage(driver, wait);
	}

	@Given("^User is on login page$")
	public void loginPage() {
		Hooks.log("Verifying User is on Login Page.");
		Assert.assertTrue(loginPage.verifyOnLoginPage());
	}

	@When("^User submit email and password$")
	public void submitEmailPassword() {
		Hooks.log("Submitting Email and Password.");
		loginPage.loginToSite(prop.getProperty("OSPC_username"), prop.getProperty("OSPC_Password"));
	}

	@Then("^User should be able to login successfully and new page open$")
	public void successLogin() {
		Hooks.log("Verifying successful login.");
		Assert.assertTrue(homePage.verifyOnHomePage());
	}

	@Then("User should be able to login successfully and retrieve the token")
	public void userShouldBeAbleToLoginSuccessfullyAndRetrieveTheToken() throws Exception {
		Hooks.log("Verifying login and retrieving OAuth token.");
		Assert.assertTrue(homePage.verifyOnHomePage());
		// Retrieve browser logs
		LogEntries logEntries = driver.manage().logs().get("browser");

		String token = null;

		// Iterate through the logs to find the token
		for (LogEntry entry : logEntries) {
			String message = entry.getMessage();

			// Check if the log entry contains the token

			if (message.contains("Oauth token for request:")) {
				token = message.split("Oauth token for request:")[1].replaceAll("\"", " " ).trim();
				break;
			}
		}
		if (token == null) {
			Hooks.log("Error: Bearer token not found in console logs.");
			throw new RuntimeException("Bearer token not found in console logs.");
		}
		Hooks.log("Bearer Token retrieved successfully.");
		// Path to the properties file
		String propertiesFilePath = "src/test/resources/config/"+System.getProperty("env")+ "/OSPC_API.properties";
		// Update the BearerToken value
		prop.setProperty("BearerToken", token, propertiesFilePath);
	}

	@Then("User should be able to login successfully and retrieve the token for UNCD")
	public void userShouldBeAbleToLoginSuccessfullyAndRetrieveTheTokenUNCD() throws Exception {
		Hooks.log("Verifying Get Started page and retrieving token from session storage.");
		Assert.assertTrue(getStarted.verifyOnGetStartedPage());

		// Use JavascriptExecutor to retrieve all keys from sessionStorage
		JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		String[] keys = ((String) jsExecutor.executeScript("return Object.keys(sessionStorage).toString();")).split(",");

		String secret = null;

		// Iterate through all keys to find the desired value
		for (String key : keys) {
			String value = (String) jsExecutor.executeScript("return sessionStorage.getItem(arguments[0]);", key);

			if (value != null && !value.isEmpty()) {
				try {
					org.json.JSONObject jsonObject = new org.json.JSONObject(value);
					if ("AccessToken".equals(jsonObject.optString("credentialType"))) {
						secret = jsonObject.optString("secret");
						break;
					}
				} catch (org.json.JSONException e) {
					// Ignore non-JSON values
				}
			}
		}

		if (secret == null || secret.isEmpty()) {
			Hooks.log("Error: Secret with credentialType:IdToken not found in sessionStorage.");
			throw new RuntimeException("Secret with credentialType:IdToken not found in sessionStorage.");
		}

		Hooks.log("Retrieved Secret successfully.");
		// Path to the properties file
		String propertiesFilePath = "src/test/resources/config/"+System.getProperty("env")+"/UNCD_API.properties";
		// Update the BearerToken value
		prop.setProperty("BearerToken", secret, propertiesFilePath);
	}


	@Then("Regenerate the Token for Claim Submission")
	public void regenerateTheTokenForClaimSubmission() throws Exception {
		Hooks.log("Regenerating Token for Claim Submission.");
		loginPage();
		submitEmailPassword();
		userShouldBeAbleToLoginSuccessfullyAndRetrieveTheToken();


	}

	@Then("User should be able to login successfully to OSBC portal")
	public void userShouldBeAbleToLoginSuccessfullyToOSBCPortal() {
		Hooks.log("Verifying successful login to OSBC portal.");
		Assert.assertTrue(homePage.verifyOSBCUnifiedLandingPage());
	}
}
