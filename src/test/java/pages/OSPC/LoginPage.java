package pages.OSPC;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.BaseDriverClass;

public class LoginPage extends BaseDriverClass {

	@FindBy(xpath = "//input[@id='email']") private WebElement usernameTextField;
	@FindBy(xpath = "//input[@id='password']") private WebElement passwordTextField;
	@FindBy(xpath = "//button[@id='next']") private WebElement loginButton;

	public LoginPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public void navigateToURL(String url) {
		getDriver().get(url);
	}

	public void loginToSite(String username, String password) {
		waitUntilElementVisible(usernameTextField);
		usernameTextField.sendKeys(username);
		passwordTextField.sendKeys(password);
		loginButton.click();
	}

	public Boolean verifyOnLoginPage() {
		waitUntilElementVisible(usernameTextField);
		return usernameTextField.isDisplayed();
	}
}