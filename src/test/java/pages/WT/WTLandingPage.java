package pages.WT;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class WTLandingPage extends BaseDriverClass {

	@FindBy(id = "lastName") private WebElement lastName;
	@FindBy(id = "claimNumber") private WebElement claimNumber;
	@FindBy(id = "day") private WebElement DateofBirthDay;
	@FindBy(id = "month") private WebElement DateofBirthMonth;
	@FindBy(id = "year") private WebElement DateofBirthYear;
	@FindBy(xpath = "//*[@type='submit']") private WebElement NextButton;

	public WTLandingPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnClaimantInformationPage() throws InterruptedException {
		waitUntilElementVisible(lastName);
		return lastName.isDisplayed();
	}

	public void ValidateClaimantInformation(String claimnumber, String lastname, String dateOfbirth) {
		claimNumber.sendKeys(claimnumber);
		lastName.sendKeys(lastname);
		String[] dob = dateOfbirth.split("/");
		DateofBirthDay.sendKeys(dob[0]);
		DateofBirthMonth.sendKeys(dob[1]);
		DateofBirthYear.sendKeys(dob[2]);
		NextButton.click();

	}
}