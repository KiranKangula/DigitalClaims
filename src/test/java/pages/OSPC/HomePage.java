package pages.OSPC;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.time.Duration;

public class HomePage extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='divTitle']/h1") private WebElement homepageHeader;
	@FindBy(xpath = "//*[contains(@aria-label,'Go to Report an injury or illness (Form 7)')]") private WebElement GOButton;
	@FindBy(id = "reportsGoButton") private WebElement reportsGoButton;

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
		WebElement claimElement = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		claimElement.click();
	}


	public boolean verifyOSBCOnHomePage() {
		waitUntilElementVisible(GOButton);
		return GOButton.isDisplayed();
	}

	public boolean verifyOSBCUnifiedLandingPage() {
		waitUntilElementVisible(reportsGoButton);
		return reportsGoButton.isDisplayed();
	}
}
