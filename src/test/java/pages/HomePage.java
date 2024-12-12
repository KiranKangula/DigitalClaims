package pages;

import org.openqa.selenium.By;
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

	public HomePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnHomePage() {
		waitUntilElementVisible(homepageHeader);
		return homepageHeader.isDisplayed();

	}

	public void clickClaim(String claimNumber) {
		// Dynamically create the XPath using the claim number
		String dynamicXPath = "//*[contains(text(),'" + claimNumber + "')]";
		WebElement claimElement = getDriver().findElement(By.xpath(dynamicXPath));
		waitUntilElementVisible(claimElement);
		claimElement.click();
	}



}
