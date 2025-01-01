package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm6_ReviewandSubmit extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(xpath = "//*[@id=\"CheckboxContainer\"]/div/div/div[1]/div") private WebElement DeclarationCheckbox;
	@FindBy(xpath = "//*[@id=\"radioTimeLost\"]/div[1]/label") private WebElement AddClaimtoProfile;
	@FindBy(xpath = "//*[@id=\"radioTimeLost\"]/div[2]/label") private WebElement AddClaimtoProfileNo;
	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement submitReportButton;
	@FindBy(xpath = "//*[@id=\"acnHeadingH1\"]") private WebElement confirmationMessage;
	@FindBy(xpath = "//*[@id=\"returnToDashboardButton\"]/span/span") private WebElement returnToDashboardButton;


	public DIeForm6_ReviewandSubmit(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean verifyReviewAndSubmitReport() {

		waitUntilElementVisible(this.DeclarationCheckbox);
		this.DeclarationCheckbox.click();
		nextButton.click();
		waitUntilElementVisible(this.AddClaimtoProfile);
		return AddClaimtoProfile.isDisplayed();
	}

	public void verifyReturnToDashBoard() {

		waitUntilElementVisible(this.returnToDashboardButton);
		returnToDashboardButton.click();
	}

	public boolean verifyReviewAndSubmitExposure() {

		waitUntilElementVisible(this.DeclarationCheckbox);
		this.DeclarationCheckbox.click();
		submitReportButton.click();
		waitUntilElementVisible(this.confirmationMessage);
		return confirmationMessage.isDisplayed();
	}

	public boolean selectAddClaimtoProfile(String addClaimtoProfile) throws InterruptedException {

		if (addClaimtoProfile.equalsIgnoreCase("Yes")) {
			AddClaimtoProfile.click();
		} else {
			AddClaimtoProfileNo.click();
		}
		submitReportButton.click();
		Thread.sleep(5000);
		waitUntilElementVisible(this.confirmationMessage);
		return confirmationMessage.isDisplayed();
	}
}
