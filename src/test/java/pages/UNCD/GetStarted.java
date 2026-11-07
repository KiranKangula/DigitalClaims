package pages.UNCD;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class GetStarted extends BaseDriverClass {

	@FindBy(xpath = "//*[@value='edob']") private WebElement edob;
	@FindBy(xpath = "//*[@value='uncd']") private WebElement uncd;
	@FindBy(id = "region-info") private WebElement GetStartedHeader;

	@FindBy(xpath = "//*[@type='submit']") private WebElement NextButton;

	public GetStarted(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public void navigateToURL(String url) {
		getDriver().get(url);
	}

	public Boolean verifyOnGetStartedPage() throws InterruptedException {
		waitUntilElementVisible(GetStartedHeader);
		return GetStartedHeader.isDisplayed();
	}

	public void SelectNumberedDocument() {
		Click(NextButton);
	}

	public void SelectUnNumberedDocument() {
		Click(uncd);
		Click(NextButton);
	}
}