package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class NIHLLandingPage extends BaseDriverClass {

	@FindBy(id = "btnNewClaim") private WebElement NewClaimButton;
	@FindBy(id = "gradual") private WebElement GradualOption;
	@FindBy(id = "sudden") private WebElement SuddenOption;
	@FindBy(id = "email") private WebElement Email;
	@FindBy(id = "dob_day") private WebElement DateofBirthDay;
	@FindBy(id = "dob_month") private WebElement DateofBirthMonth;
	@FindBy(id = "dob_year") private WebElement DateofBirthYear;
	@FindBy(id = "continue") private WebElement StartClaimButton;

	@FindBy(xpath = "//*[@aria-label='Continue a claim']") private WebElement ContinueClaimButton;
	@FindBy(id = "refNo") private WebElement ReferenceNumber;
	@FindBy(id = "continue") private WebElement ContinueButton;


	public NIHLLandingPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnNIHLLandingPage() throws InterruptedException {
		waitUntilElementVisible(NewClaimButton);
		return NewClaimButton.isDisplayed();
	}

	public boolean ClickGradualRadioButton() {
		Click(NewClaimButton);
		waitUntilElementVisible(GradualOption);
		Click(GradualOption);
		return Email.isDisplayed();

	}

	public void EnterUserDetails(String email, String dob) {
		Enter(Email, email);
		String[] dob1 = dob.split("/");
		GWSelect(DateofBirthDay, dob1[0]);
		GWSelect(DateofBirthMonth, dob1[1]);
		GWSelect(DateofBirthYear, dob1[2]);
		Click(StartClaimButton);
	}
}