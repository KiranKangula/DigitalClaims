package pages.OSPC;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BaseDriverClass;

public class LoginPage extends BaseDriverClass {

	@FindBy(xpath = "//input[@id='email' or @id='signInName']") private WebElement usernameTextField;
	@FindBy(xpath = "//input[@id='password']") private WebElement passwordTextField;
	@FindBy(xpath = "//button[@id='next']") private WebElement loginButton;
	@FindBy(xpath = "(//input[contains(@id, 'extension_oaMfaMethod')])[2]") private WebElement mfaMethod_phone;
	@FindBy(xpath = "//*[@id='continue']") private WebElement mfaNextButton;
	@FindBy(xpath = "//*[@id='mfaPhoneMethod_phone']") private WebElement mfaCallMeButton;
	@FindBy(xpath = "//*[@id='mfaRememberMe_true']") private WebElement mfaRememberMe_true;
	@FindBy(xpath = "//*[@id='continue']") private WebElement mfaSubmitButton;


	public LoginPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public void navigateToURL(String url) {
		getDriver().get(url);
	}

	public void loginToSite(String username, String password) {
		waitUntilElementVisible(usernameTextField);
		Enter(usernameTextField, username);
		Enter(passwordTextField, password);
		Click(loginButton);
		Sleep(3000);
		if (isDisplayed(mfaMethod_phone))
		{
			Click(mfaMethod_phone);
			Click(mfaNextButton);
			Click(mfaCallMeButton);
			Click(mfaNextButton);
			waitUntilElementVisible(mfaRememberMe_true);
			Click(mfaRememberMe_true);
			Click(mfaSubmitButton);
		}
		else {
			Hooks.log(" MFA is not enabled...");
		}

	}

	public Boolean verifyOnLoginPage() {
		waitUntilElementVisible(usernameTextField);
		return usernameTextField.isDisplayed();
	}
}
