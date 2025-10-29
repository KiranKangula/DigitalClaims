package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_ReviewandSubmit extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;
	@FindBy(xpath = "//*[contains(text(),'Submit')]") private WebElement SubmitButton;

	@FindBy(xpath = "//h1[contains(text(),'Review and submit')]") private WebElement ReviewAndSubmitHeading;

	@FindBy(id = "reviewDeclaratioInputTitle") private WebElement reviewDeclaratioInputTitle;
	@FindBy(id = "reviewDeclaratioInputPhone") private WebElement reviewDeclaratioInputPhone;
	private static final String Declaration_checkbox = "CheckboxContainer2";
	@FindBy(xpath = "//h1[contains(text(),'Confirmation')]") private WebElement ConfirmationHeading;
	@FindBy(id = "returnToDashboardButton") private WebElement returnToDashboardButton;

	public DIeForm7_ReviewandSubmit(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean submitEform7Report() {
		waitUntilElementVisible(ReviewAndSubmitHeading);
		reviewDeclaratioInputTitle.sendKeys("Automation Test");
		jsMoveSendKeys(reviewDeclaratioInputPhone, "6478558585");
		selectRadioButtonOption(Declaration_checkbox, "By checking this box");
		SubmitButton.click();
		waitUntilElementVisible(ConfirmationHeading);
		returnToDashboardButton.click();
		return true;
	}
}