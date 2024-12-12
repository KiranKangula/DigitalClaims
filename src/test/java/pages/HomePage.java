package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class HomePage extends BaseDriverClass {
	ConfigReader prop=new ConfigReader();

	@FindBy(xpath = "//*[@id='divTitle']/h1")
	@CacheLookup
	private WebElement homepageHeader;

	@FindBy(xpath = "//*[contains(text(),'')]")
	@CacheLookup
	private WebElement claimnumber;

	public HomePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnHomePage() {
		waitUntilElementVisible(homepageHeader);
		return homepageHeader.isDisplayed();

	}

	public void clickClaim() {
		waitUntilElementVisible(claimnumber);
		claimnumber.click();
	}


}
