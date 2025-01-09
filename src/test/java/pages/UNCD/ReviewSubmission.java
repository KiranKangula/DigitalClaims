package pages.UNCD;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class ReviewSubmission extends BaseDriverClass {

	@FindBy(xpath = "(//*[@type='checkbox'])[1]") private WebElement emailconfirmation;
	@FindBy(xpath = "(//*[@type='checkbox'])[2]") private WebElement declaration;
	@FindBy(xpath = "//*[@type='submit']") private WebElement SubmitButton;
	@FindBy(xpath = "//button[@class='link']") private WebElement printLink;
	@FindBy(id = "region-review") private WebElement reviewHeader;

	public ReviewSubmission(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean submitDocument() {
		waitUntilElementVisible(reviewHeader);
		emailconfirmation.click();
		declaration.click();
		SubmitButton.click();
		waitUntilElementVisible(printLink);
		return printLink.isDisplayed();

	}

	public boolean verifyOnConfirmationScreen() {
		waitUntilElementVisible(printLink);
		return printLink.isDisplayed();

	}
}