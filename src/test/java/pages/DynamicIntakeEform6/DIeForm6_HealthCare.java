package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm6_HealthCare extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadios\"]/div[1]/label/span") private WebElement healthCareYes;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadios\"]/div[2]/label/span") private WebElement healthCareNo;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadios\"]/div[3]/label/span") private WebElement healthCareUnsure;
	@FindBy(id = "sourceOfInfection-wrapper") private WebElement fromWhere;
	@FindBy(id = "healthcare-received-date-picker") private WebElement healthCareReceivedDate;

	@FindBy(id = "employer-reported-date-picker") private WebElement employerReportedDate;

	@FindBy(xpath = "//*[@id=\"AddReporterButton\"]/span/span") private WebElement addReporterButton;
	@FindBy(id = "inputFirstName") private WebElement ReporterFirstName;
	@FindBy(id = "inputLastName") private WebElement ReporterLastName;
	@FindBy(id = "inputPosition") private WebElement ReporterPosition;
	@FindBy(id = "inputPhoneNumeber") private WebElement ReporterPhoneNumber;
	@FindBy(id = "btnSave") private WebElement saveButtonReporter;
	@FindBy(xpath = "//*[@id=\"AddProviderContactButton\"]/span/span") private WebElement addProviderContactButton;
	@FindBy(id = "orgContactInputFirstName") private WebElement ProviderFirstName;
	@FindBy(id = "orgContactInputLastName") private WebElement ProviderLastName;
	@FindBy(id = "orgContactInputPhoneNumeber") private WebElement ProviderPhoneNumber;
	@FindBy(id = "orgContactBtnSave") private WebElement saveButtonProvider;
	@FindBy(xpath = "//*[@id=\"AddProviderOrgButton\"]/span/span") private WebElement addProviderOrgButton;
	@FindBy(id = "orgInputCompanyName") private WebElement ProviderOrgName;
	@FindBy(id = "orgInputPhoneNumeber") private WebElement ProviderOrgPhoneNumber;
	@FindBy(id = "inputAddressLine1") private WebElement ProviderOrgAddressLine1;
	@FindBy(id = "inputCity") private WebElement ProviderOrgCity;
	@FindBy(id = "dropdownProvince-wrapper") private WebElement ProviderOrgProvinceDropdown;
	@FindBy(id = "inputPostalCode") private WebElement ProviderOrgPostalCode;
	@FindBy(id = "dropdownCountry-wrapper") private WebElement ProviderOrgCountryDropdown;
	@FindBy(id = "orgBtnSave") private WebElement saveButtonProviderOrg;
	@FindBy(id = "expected-healthcare-date-picker") private WebElement whendoyouexpect;

	@FindBy(xpath = "//*[@id=\"divNoPhoneContainer\"]/div/div/div[1]/span") private WebElement NoDatePlannedYetCheckbox;




	public DIeForm6_HealthCare(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void enterHealthCareDetails(String healthCareReceived, String whereHealthCareReceived, String healthCareReceivedDate, String emplReportedDate,
									   String reporter, String provider, String providerOrg, String providerOrgAddress, String whendoyouexpect)  {

		if (healthCareReceived.equalsIgnoreCase("Yes")) {
			Click(healthCareYes);
			Click(fromWhere);
			DynamicTextClick(whereHealthCareReceived);
			Click(this.healthCareReceivedDate);
			Enter(this.healthCareReceivedDate, healthCareReceivedDate);
			Click(this.employerReportedDate);
			Enter(this.employerReportedDate, emplReportedDate);
			Click(addReporterButton);
			Enter(ReporterFirstName, reporter.split("\\|")[0]);
			Enter(ReporterLastName, reporter.split("\\|")[1]);
			Enter(ReporterPosition, reporter.split("\\|")[2]);
			jsMoveSendKeys(ReporterPhoneNumber, reporter.split("\\|")[3]);
			Click(saveButtonReporter);
			Sleep(3000);
			if(!provider.isEmpty()) {
				Click(addProviderContactButton);
				Enter(ProviderFirstName, provider.split("\\|")[0]);
				Enter(ProviderLastName, provider.split("\\|")[1]);
				jsMoveSendKeys(ProviderPhoneNumber, provider.split("\\|")[2]);
				Click(saveButtonProvider);
			}
			else if(!providerOrg.isEmpty()) {
				Click(addProviderOrgButton);
				Enter(ProviderOrgName, providerOrg.split("\\|")[0]);
				jsMoveSendKeys(ProviderOrgPhoneNumber, providerOrg.split("\\|")[1]);
				Enter(ProviderOrgAddressLine1, providerOrgAddress.split("\\|")[0]);
				Enter(ProviderOrgCity, providerOrgAddress.split("\\|")[1]);
				Click(ProviderOrgProvinceDropdown);
				ClickusingActions(providerOrgAddress.split("\\|")[2]);
				Enter(ProviderOrgPostalCode, providerOrgAddress.split("\\|")[3]); // This is a sendKeys, not a click
				Click(ProviderOrgCountryDropdown);
				ClickusingActions(providerOrgAddress.split("\\|")[4]);
				Click(saveButtonProviderOrg);
			}


		} else if (healthCareReceived.equalsIgnoreCase("No")) {
			Click(healthCareNo);

		} else {
			Click(healthCareUnsure);
			if(whendoyouexpect.isEmpty()) {
				Click(NoDatePlannedYetCheckbox);
			}else {
				Click(this.whendoyouexpect);
				Enter(this.whendoyouexpect, whendoyouexpect);
			}

		}
		Sleep(3000);
		Click(nextButton);
	}
}
