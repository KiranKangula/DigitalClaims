package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_EmployeeDetails extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;
	@FindBy(id = "relatedClaimNumber") private WebElement employeeDetailsHeading;
	@FindBy(id = "pwiAdditionalInfo") private WebElement pwiAdditionalInfo;
	@FindBy(id = "pwiEmployeeFName") private WebElement pwiEmployeeFName;
	@FindBy(id = "pwiEmployeeLName") private WebElement pwiEmployeeLName;
	@FindBy(id = "primaryPhone") private WebElement primaryPhone;
	@FindBy(xpath = "//h1[contains(text(),'Employee details – Additional information')]") private WebElement AdditionalInfoHeading;
	private static final String Sex_radio = "radiobuttonNihl";
	@FindBy(id = "dropdownPronounList-wrapper") private WebElement dropdownPronounList;
	private String empDateOfBirth = "dobDatePicker";
//	@FindBy(id = "dropdownDobDate-wrapper") private WebElement dropdownDobDate;
//	@FindBy(id = "dropdownDobMonth-wrapper") private WebElement dropdownDobMonth;
//	@FindBy(id = "dropdownDobYear-wrapper") private WebElement dropdownDobYear;
	private static final String Language_radio = "radiobuttonLanguageofService";
	private static final String SIN_checkbox = "pwiAdditionalInfo";
	@FindBy(id = "inputSin") private WebElement inputSin;
	@FindBy(id = "pwiAddressLine1") private WebElement pwiAddressLine1;
	@FindBy(id = "pwiAddressCity") private WebElement pwiAddressCity;
	@FindBy(id = "dropdownProvince-wrapper") private WebElement dropdownProvince;
	@FindBy(id = "inputPostalCode") private WebElement inputPostalCode;


	public DIeForm7_EmployeeDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public boolean selectEmployeeDetails(String employeeFirstName, String employeeLastName, String phoneNumber, String sex, String pronouns,
										 String dateOfBirth, String language, String sin, String address) throws InterruptedException {

		waitUntilElementVisible(employeeDetailsHeading);
		Click(NextButton);
		Enter(pwiEmployeeFName, employeeFirstName);
		Enter(pwiEmployeeLName, employeeLastName);
		jsMoveSendKeys(primaryPhone,phoneNumber); // To scroll into view
		Click(NextButton);
		waitUntilElementVisible(AdditionalInfoHeading);
		selectRadioButtonOption(Sex_radio,sex);
		Select(dropdownPronounList,pronouns);
		DatePicker(empDateOfBirth, dateOfBirth);
//		selectDropdownValue(dropdownDobDate,dateOfBirth.split("/")[0]);
//		selectDropdownValue(dropdownDobMonth,dateOfBirth.split("/")[1]);
//		selectDropdownValue(dropdownDobYear,dateOfBirth.split("/")[2]);
		selectRadioButtonOption(Language_radio,language);
		if(sin.equalsIgnoreCase("")) {
			selectRadioButtonOption(SIN_checkbox, "I don");
		}
		else {
			Enter(inputSin, sin);
		}
		Click(NextButton);
		waitUntilElementVisible(pwiAddressLine1);
		Enter(pwiAddressLine1, address.split("\\|")[0]);
		Enter(pwiAddressCity, address.split("\\|")[1]);
		Select(dropdownProvince,address.split("\\|")[2]);
		Enter(inputPostalCode, address.split("\\|")[3]);
		Click(NextButton);
		return true;
	}
}