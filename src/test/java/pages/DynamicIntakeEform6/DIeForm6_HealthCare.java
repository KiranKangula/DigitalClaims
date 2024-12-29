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
	@FindBy(id = "dropdownHealthCareReceivedDate-wrapper") private WebElement healthCareReceivedDateDay;
	@FindBy(id = "dropdownHealthCareReceivedMonth-wrapper") private WebElement healthCareReceivedDateMonth;
	@FindBy(id = "dropdownHealthCareReceivedYear-wrapper") private WebElement healthCareReceivedDateYear;
	@FindBy(id = "dropdownEmployerReportedDate-wrapper") private WebElement employerReportedDateDay;
	@FindBy(id = "dropdownEmployerReportedMonth-wrapper") private WebElement employerReportedDateMonth;
	@FindBy(id = "dropdownEmployerReportedYear-wrapper") private WebElement employerReportedDateYear;
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
	@FindBy(id = "dropdownExpectedDate-wrapper") private WebElement whendoyouexpectDay;
	@FindBy(id = "dropdownExpectedMonth-wrapper") private WebElement whendoyouexpectMonth;
	@FindBy(id = "dropdownExpectedYear-wrapper") private WebElement whendoyouexpectYear;
	@FindBy(xpath = "//*[@id=\"divNoPhoneContainer\"]/div/div/div[1]/span") private WebElement NoDatePlannedYetCheckbox;




	public DIeForm6_HealthCare(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void enterHealthCareDetails(String healthCareReceived, String whereHealthCareReceived, String healthCareReceivedDate, String emplReportedDate,
									   String reporter, String provider, String providerOrg, String providerOrgAddress, String whendoyouexpect) throws InterruptedException {

		if (healthCareReceived.equalsIgnoreCase("Yes")) {
			healthCareYes.click();
			fromWhere.click();
			DynamicTextClick(whereHealthCareReceived);
			healthCareReceivedDateDay.click();
			ClickusingActions(healthCareReceivedDate.split("\\|")[0]);
			healthCareReceivedDateMonth.click();
			ClickusingActions(healthCareReceivedDate.split("\\|")[1]);
			healthCareReceivedDateYear.click();
			ClickusingActions(healthCareReceivedDate.split("\\|")[2]);
			employerReportedDateDay.click();
			ClickusingActions(emplReportedDate.split("\\|")[0]);
			employerReportedDateMonth.click();
			ClickusingActions(emplReportedDate.split("\\|")[1]);
			employerReportedDateYear.click();
			ClickusingActions(emplReportedDate.split("\\|")[2]);
			addReporterButton.click();
			ReporterFirstName.sendKeys(reporter.split("\\|")[0]);
			ReporterLastName.sendKeys(reporter.split("\\|")[1]);
			ReporterPosition.sendKeys(reporter.split("\\|")[2]);
			jsMoveSendKeys(ReporterPhoneNumber, reporter.split("\\|")[3]);
			saveButtonReporter.click();
			Thread.sleep(3000);
			if(!provider.isEmpty()) {
				addProviderContactButton.click();
				ProviderFirstName.sendKeys(provider.split("\\|")[0]);
				ProviderLastName.sendKeys(provider.split("\\|")[1]);
				jsMoveSendKeys(ProviderPhoneNumber, provider.split("\\|")[2]);
				saveButtonProvider.click();
			}
			else if(!providerOrg.isEmpty()) {
				addProviderOrgButton.click();
				ProviderOrgName.sendKeys(providerOrg.split("\\|")[0]);
				jsMoveSendKeys(ProviderOrgPhoneNumber, providerOrg.split("\\|")[1]);
				ProviderOrgAddressLine1.sendKeys(providerOrgAddress.split("\\|")[0]);
				ProviderOrgCity.sendKeys(providerOrgAddress.split("\\|")[1]);
				ProviderOrgProvinceDropdown.click();
				ClickusingActions(providerOrgAddress.split("\\|")[2]);
				ProviderOrgPostalCode.sendKeys(providerOrgAddress.split("\\|")[3]);
				ProviderOrgCountryDropdown.click();
				ClickusingActions(providerOrgAddress.split("\\|")[4]);
				saveButtonProviderOrg.click();
			}


		} else if (healthCareReceived.equalsIgnoreCase("No")) {
			healthCareNo.click();

		} else {
			healthCareUnsure.click();
			if(whendoyouexpect.isEmpty()) {
				NoDatePlannedYetCheckbox.click();
			}else {
				whendoyouexpectDay.click();
				ClickusingActions(whendoyouexpect.split("\\|")[0]);
				whendoyouexpectMonth.click();
				ClickusingActions(whendoyouexpect.split("\\|")[1]);
				whendoyouexpectYear.click();
				ClickusingActions(whendoyouexpect.split("\\|")[2]);
			}

		}
		Thread.sleep(3000);
		nextButton.click();
	}
}
