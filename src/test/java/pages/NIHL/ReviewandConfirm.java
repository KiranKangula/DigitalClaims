package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class ReviewandConfirm extends BaseDriverClass {



	@FindBy(xpath = "//*[@name='signature']") private WebElement Signature;
	@FindBy(xpath = "//*[@aria-label='print a copy of your submission']") private WebElement PrintButton;
	@FindBy(xpath = "(//button[@role='button' and contains(., 'Submit')])[1]") private WebElement SubmitButton;




	public ReviewandConfirm(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void reviewandSubmitNIHLClaim() throws InterruptedException {
		waitUntilElementVisible(Signature);
		Signature.sendKeys("Signature");
		SubmitButton.click();
		Thread.sleep(5000);

	}

	public boolean verifyClaimSubmitted() {
		waitUntilElementVisible(PrintButton);
		return PrintButton.isDisplayed();
	}
}