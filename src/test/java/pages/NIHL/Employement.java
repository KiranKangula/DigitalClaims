package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class Employement extends BaseDriverClass {

	@FindBy(id = "btnNewClaim") private WebElement NewClaimButton;

	@FindBy(xpath = "//*[@id='yes']") private WebElement RetiredRadioYes;
	@FindBy(xpath = "//*[@name='retirementDate']") private WebElement RetirementDate;
	@FindBy(xpath = "//*[@id='no']") private WebElement RetiredRadioNo;
	@FindBy(xpath = "//*[@id='isEmployedyes']") private WebElement EmployedRadioYes;
	@FindBy(xpath = "//*[@name='employerName']") private WebElement EmployerName;
	@FindBy(xpath = "//*[@name='employerAddress']") private WebElement EmployerAddress;
	@FindBy(xpath = "//*[@name='employerPhoneNumber']") private WebElement EmployerPhoneNumber;
	@FindBy(xpath = "//*[@id='isHazardousNoiseyes']") private WebElement HazardousNoiseRadioYes;
	@FindBy(xpath = "//*[@id='isHazardousNoiseno']") private WebElement HazardousNoiseRadioNo;
	@FindBy(xpath = "//*[@id='isEmployedno']") private WebElement EmployedRadioNo;

	@FindBy(xpath = "//*[@id='selfEmployedQuestionyes']") private WebElement SelfEmployedRadioYes;
	@FindBy(xpath = "//*[@id='hasInsuranceyes']") private WebElement InsuranceRadioYes;
	@FindBy(xpath = "//*[@name='businessName']") private WebElement BusinessName;
	@FindBy(xpath = "//*[@name='yourBusinessAddress']") private WebElement BusinessAddress;
	@FindBy(xpath = "//*[@name='yourBusinessFrom']") private WebElement BusinessFrom;
	@FindBy(xpath = "//*[@name='yourBusinessTo']") private WebElement BusinessTo;
	@FindBy(xpath = "//*[@id='hasInsuranceno']") private WebElement InsuranceRadioNo;

	@FindBy(xpath = "//*[@id='selfEmployedQuestionno']") private WebElement SelfEmployedRadioNo;


	@FindBy(xpath = "//*[@id='noisyMachineryOffworkYes']") private WebElement noisyMachineryOffworkYes;
	@FindBy(xpath = "//*[@name='usedNoisyMachinery']") private WebElement usedNoisyMachinery;
	@FindBy(xpath = "//*[@id='noisyMachineryOffworkNo']") private WebElement noisyMachineryOffworkNo;

	@FindBy(xpath = "//*[@aria-label='Add']") private WebElement AddButton;
	@FindBy(xpath = "//*[@id='stillInBusinessYes']") private WebElement stillInBusinessYes;
	@FindBy(xpath = "//*[@id='stillInBusinessNo']") private WebElement stillInBusinessNo;
	@FindBy(xpath = "//*[@name='jobTitle']") private WebElement JobTitle;
	@FindBy(xpath = "//*[@name='employedFrom']") private WebElement EmployedFrom;
	@FindBy(xpath = "//*[@name='employedTo']") private WebElement EmployedTo;
	@FindBy(xpath = "//*[@id='toolsEquipmentUsed']") private WebElement ToolsEquipmentUsed;
	@FindBy(xpath = "//*[@formcontrolname='hoursUsed']") private WebElement HoursUsed;
	@FindBy(xpath = "//*[@aria-label='Done']") private WebElement DoneButton;
	@FindBy(xpath = "(//button[@role='button' and contains(., 'Next')])[3]") private WebElement NextButton;


	public Employement(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnNIHLLandingPage() throws InterruptedException {
		waitUntilElementVisible(NewClaimButton);
		return NewClaimButton.isDisplayed();
	}


	public void EnterEmploymentDetails(String areyouretired, String retirementDate, String employedRadio, String employerName, String employerAddress, String employerPhone, String hazardousNoiseRadio, String selfEmployedRadio, String insuranceRadio, String businessName, String businessAddress, String businessFrom, String businessTo, String noisyMachineryOffwork, String usedNoisyMachinery, String stillInBusiness, String jobTitle, String employedFrom, String employedTo, String toolsEquipmentUsed, String hoursUsed) throws InterruptedException {

		if (areyouretired.equalsIgnoreCase("Yes")) {
			Click(RetiredRadioYes);
			Enter(RetirementDate, retirementDate);
		} else {
			Click(RetiredRadioNo);
			if (employedRadio.equalsIgnoreCase("Yes")) {
				Click(EmployedRadioYes);
				Enter(EmployerName, employerName);
				Enter(EmployerAddress, employerAddress);
				Enter(EmployerPhoneNumber, employerPhone);
				if (hazardousNoiseRadio.equalsIgnoreCase("Yes")) {
					Click(HazardousNoiseRadioYes);
				} else {
					Click(HazardousNoiseRadioNo);
				}
			} else {
				Click(EmployedRadioNo);
			}

		}


		if (selfEmployedRadio.equalsIgnoreCase("Yes")) {
			Click(SelfEmployedRadioYes);
			if (insuranceRadio.equalsIgnoreCase("Yes")) {
				Click(InsuranceRadioYes);
				Enter(BusinessName, businessName);
				Enter(BusinessAddress, businessAddress);
				Enter(BusinessFrom, businessFrom);
				Click(BusinessName);
				Enter(BusinessTo, businessTo);
			} else {
				Click(InsuranceRadioNo);
			}

		} else {
			Click(SelfEmployedRadioNo);
		}

		if (noisyMachineryOffwork.equalsIgnoreCase("Yes")) {
			Click(noisyMachineryOffworkYes);
			Enter(this.usedNoisyMachinery, usedNoisyMachinery);
		} else {
			Click(noisyMachineryOffworkNo);
		}

		Click(AddButton);
		if (stillInBusiness.equalsIgnoreCase("Yes")) {
			Click(stillInBusinessYes);
		} else {
			Click(stillInBusinessNo);
		}
		Enter(JobTitle, jobTitle);
		Enter(EmployedFrom, employedFrom);
		Click(JobTitle);
		Enter(EmployedTo, employedTo);
		Enter(ToolsEquipmentUsed, toolsEquipmentUsed);
		Enter(HoursUsed, hoursUsed);
		Click(DoneButton);
		Sleep(2000);
		Click(NextButton);

	}
}