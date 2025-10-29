package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_LosstimeEarningsDetails extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;
	//Lost time and earnings details
	@FindBy(xpath = "//h1[contains(text(),'Lost time and earnings details')]") private WebElement LOEDetailsHeading;
	private static final String LOE_Radio = "radiobutton";
	//Return-to-work contact details
	@FindBy(xpath = "//h1[contains(text(),'Return-to-work contact details')]") private WebElement RTWDetailsHeading;
	private static final String TypeofWork_Radio = "radiotypeOfReturnedWork";
	private static final String WageLoss_Radio = "radiortwisWageLoss";

	public DIeForm7_LosstimeEarningsDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean selectLossTimeAndEarningsDetails(String loe, String typeofWork, String wageLoss) {
		waitUntilElementVisible(LOEDetailsHeading);

		selectRadioButtonOption(LOE_Radio, loe);
		NextButton.click();

		waitUntilElementVisible(RTWDetailsHeading);

		selectRadioButtonOption(TypeofWork_Radio, typeofWork);
		selectRadioButtonOption(WageLoss_Radio, wageLoss);

		NextButton.click();
		return true;
	}
}