package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_HealthCareDetails extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;

	@FindBy(id = "identifyHealthcareRecieved") private WebElement HealthcareDetailsHeading;

	private static final String radiobuttonHealth_Radio = "radiobuttonHealth";
	@FindBy(id = "injuryLocation-wrapper") private WebElement injuryLocation;
	private static final String dropdownReceivedDate = "dropdownDobContainer";
//	@FindBy(id = "dropdownReceivedDateDay-wrapper") private WebElement dropdownReceivedDateDay;
//	@FindBy(id = "dropdownReceivedDateMonth-wrapper") private WebElement dropdownReceivedDateMonth;
//	@FindBy(id = "dropdownReceivedDateYear-wrapper") private WebElement dropdownReceivedDateYear;
	private static final String dropdownHCReceivedDate = "healthCareReceivedDatePicker";
//	@FindBy(id = "dropdownHCReceivedDateDay-wrapper") private WebElement dropdownHCReceivedDateDay;
//	@FindBy(id = "dropdownHCReceivedDateMonth-wrapper") private WebElement dropdownHCReceivedDateMonth;
//	@FindBy(id = "dropdownHCReceivedDateYear-wrapper") private WebElement dropdownHCReceivedDateYear;



	public DIeForm7_HealthCareDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean selectHealthCareDetails(String healthCare) throws InterruptedException {
		waitUntilElementVisible(HealthcareDetailsHeading);

		selectRadioButtonOption(radiobuttonHealth_Radio, healthCare.split("\\|")[0]);
		Select(injuryLocation, healthCare.split("\\|")[1]);
		DatePicker(dropdownReceivedDate, healthCare.split("\\|")[2]);
		DatePicker(dropdownHCReceivedDate, healthCare.split("\\|")[3]);

		Click(NextButton);
		return true;
	}
}