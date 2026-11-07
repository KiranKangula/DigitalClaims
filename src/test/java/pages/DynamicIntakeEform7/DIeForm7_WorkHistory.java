package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_WorkHistory extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;

	@FindBy(id = "pwiEmploymentDetails") private WebElement EmploymentDetailsHeading;
	@FindBy(id = "jobTitle") private WebElement jobTitle;
	private String hireDatePicker = "hireDatePicker";
//	@FindBy(id = "dropdownDobDate-wrapper") private WebElement dropdownDobDate;
//	@FindBy(id = "dropdownDobMonth-wrapper") private WebElement dropdownDobMonth;
//	@FindBy(id = "dropdownDobYear-wrapper") private WebElement dropdownDobYear;

	@FindBy(id = "employmentLengthYear") private WebElement employmentLengthYear;
	@FindBy(id = "employmentLengthMonth") private WebElement employmentLengthMonth;

	@FindBy(id = "pwiAddContact") private WebElement pwiAddContact;
	@FindBy(id = "myCard") private WebElement SupervisorCard;
	@FindBy(id = "InputfirstName") private WebElement SupervisorfirstName;
	@FindBy(id = "InputlastName") private WebElement SupervisorlastName;
	@FindBy(id = "InputLanguageofService-wrapper") private WebElement SupervisorLanguage;
	@FindBy(id = "InputprimaryPhone") private WebElement SupervisorphoneNumber;
	@FindBy(id = "pwiEmploymentSave") private WebElement SupervisorSaveButton;
	//Employee job details - union information
	@FindBy(id = "radiobuttonEmployee") private WebElement radiobuttonEmployeeW;
	private static final String radiobuttonEmployee_radio = "radiobuttonEmployee";
	private static final String radiobuttonUnionorLocal_radio = "radiobuttonUnionorLocal";
	//Employee job details - relationship to employer
	@FindBy(id = "IsSpouseRelavtive") private WebElement IsSpouseRelavtiveW;
	@FindBy(xpath = "//*[@id=\"dropdownEmploymentTypeList-wrapper\"]/div/div[1]") private WebElement EmployerRelation;
	private static final String radiobutton_radio = "radiobutton";
	private static final String EmployerRelation_radio = "EmployerRelation";

	public DIeForm7_WorkHistory(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean selectWorkHistoryDetails(String jobTitle, String empStartDate, String empYears, String empMonths, String supervisorContact, String employees, String union, String isSpouseRelavtive, String employerRelation) throws InterruptedException {

		waitUntilElementVisible(EmploymentDetailsHeading);
		Enter(this.jobTitle, jobTitle);
		DatePicker(hireDatePicker, empStartDate);
		Sleep(2000);
		jsMoveSendKeys(this.employmentLengthYear,empYears);
		jsMoveSendKeys(this.employmentLengthMonth, empMonths);

			Click(pwiAddContact);
			waitUntilElementVisible(SupervisorCard);
			Enter(SupervisorfirstName, supervisorContact.split("\\|")[0]);
			Enter(SupervisorlastName, supervisorContact.split("\\|")[1]);
			Select(SupervisorLanguage, supervisorContact.split("\\|")[3]);
			jsMoveSendKeys(SupervisorphoneNumber, supervisorContact.split("\\|")[2]);
			Click(SupervisorSaveButton);
		Click(NextButton);
		waitUntilElementVisible(radiobuttonEmployeeW);
		selectRadioButtonOption(radiobuttonEmployee_radio, employees);
		selectRadioButtonOption(radiobuttonUnionorLocal_radio, union);
		Click(NextButton);
		waitUntilElementVisible(IsSpouseRelavtiveW);
		selectRadioButtonOption(radiobutton_radio, isSpouseRelavtive);
		selectDropdownValue(EmployerRelation,employerRelation );
		//selectRadioButtonOption(EmployerRelation_radio, employerRelation);

		Click(NextButton);
		return  true;

	}
}