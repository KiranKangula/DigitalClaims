package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.time.Duration;

public class DIeForm6_WorkHistory extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(id = "inputJobTitle") private WebElement jobTitle;
	@FindBy(id = "dropdownStartDate-wrapper") private WebElement startDateDay;
	@FindBy(id = "dropdownStartMonth-wrapper") private WebElement startDateMonth;
	@FindBy(id = "dropdownStartYear-wrapper") private WebElement startDateYear;
	@FindBy(id = "inputEmploymentDurationYears") private WebElement employmentDurationYears;
	@FindBy(id = "inputMonths") private WebElement employmentDurationMonths;
	@FindBy(xpath = "//*[@id=\"AddContactButton\"]/span/span") private WebElement addContactButton;
	@FindBy(id = "inputFirstName") private WebElement SupervisorFirstName;
	@FindBy(id = "inputLastName") private WebElement SupervisorLastName;
	@FindBy(id = "inputPhoneNumeber") private WebElement SupervisorPhoneNumber;
	@FindBy(id = "btnSave") private WebElement saveButton;
	@FindBy(xpath = "//*[@id=\"radiobuttonCompanySize\"]/div[1]/label") private WebElement companySizeYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonCompanySize\"]/div[2]/label") private WebElement companySizeNo;
	@FindBy(xpath = "//*[@id=\"radiobuttonThirdPartyUnion\"]/div[1]/label") private WebElement thirdPartyUnionYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonThirdPartyUnion\"]/div[2]/label") private WebElement thirdPartyUnionNo;
	@FindBy(id = "inputNameOfTheUnion") private WebElement nameOfTheUnion;
	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(xpath = "//*[@id=\"radiobuttonRelative\"]/div[1]/label") private WebElement relativeYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonRelative\"]/div[2]/label") private WebElement relativeNo;
	@FindBy(id = "businessNature") private WebElement businessNature;
	@FindBy(xpath = "//*[@id=\"radiobuttonCommitte\"]/div[1]/label") private WebElement safetyCommitteeYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonCommitte\"]/div[2]/label") private WebElement safetyCommitteeNo;
	@FindBy(xpath = "//*[@id=\"radiobuttonRepresentative\"]/div[1]/label") private WebElement safetyRepresentativeYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonRepresentative\"]/div[2]/label") private WebElement safetyRepresentativeNo;
	@FindBy(xpath = "\t//*[@id=\"attachReportButton\"]/span/span") private WebElement attachReportButton;
	@FindBy(id = "dropdownExposureStartDate-wrapper") private WebElement exposureStartDateDay;
	@FindBy(id = "dropdownExposureStartMonth-wrapper") private WebElement exposureStartDateMonth;
	@FindBy(id = "dropdownExposureStartYear-wrapper") private WebElement exposureStartDateYear;





	public DIeForm6_WorkHistory(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean enterJobDetails(String jobTitle, String startDateDay, String startDateMonth, String startDateYear, String empDurationYears,
								   String empDurationMonths, String supervisorFirstName, String supervisorLastName, String supervisorPhoneNumber,
								   String companySize, String thirdPartyUnion, String nameOfTheUnion) {

		waitUntilElementVisible(this.jobTitle);
		this.jobTitle.sendKeys(jobTitle);
		this.startDateDay.click();
		ClickusingActions(startDateDay);
		this.startDateMonth.click();
		ClickusingActions(startDateMonth);
		this.startDateYear.click();
		ClickusingActions(startDateYear);
		jsMoveSendKeys(this.employmentDurationYears, empDurationYears);
		jsMoveSendKeys(this.employmentDurationMonths, empDurationMonths);
		this.addContactButton.click();
		this.SupervisorFirstName.sendKeys(supervisorFirstName);
		this.SupervisorLastName.sendKeys(supervisorLastName);
		jsMoveSendKeys(this.SupervisorPhoneNumber, supervisorPhoneNumber);
		this.saveButton.click();
		if(companySize.equalsIgnoreCase("yes")) {
			this.companySizeYes.click();
		} else {
			this.companySizeNo.click();
		}
		if(thirdPartyUnion.equalsIgnoreCase("yes")) {
			this.thirdPartyUnionYes.click();
			waitUntilElementVisible(this.nameOfTheUnion);
			this.nameOfTheUnion.sendKeys(nameOfTheUnion);
		} else {
			this.thirdPartyUnionNo.click();
		}
		this.nextButton.click();
		waitUntilElementVisible(this.relativeYes);
		return this.relativeYes.isDisplayed();
	}

	public void enterJobDetails(String jobTitle, String startDate, String WhatTypeofWork, String companySize) {

		waitUntilElementVisible(this.jobTitle);
		this.jobTitle.sendKeys(jobTitle);
		exposureStartDateDay.click();
		ClickusingActions(startDate.split("\\|")[0]);
		exposureStartDateMonth.click();
		ClickusingActions(startDate.split("\\|")[1]);
		exposureStartDateYear.click();
		ClickusingActions(startDate.split("\\|")[2]);
		businessNature.sendKeys(WhatTypeofWork);

		if(companySize.equalsIgnoreCase("yes")) {
			this.companySizeYes.click();
		} else {
			this.companySizeNo.click();
		}

		this.nextButton.click();
	}

	public void enterWorkDetails(String relative, String typesofEmployment) {
		if(relative.equalsIgnoreCase("yes")) {
			this.relativeYes.click();
		} else {
			this.relativeNo.click();
		}
		String dynamicXPath = "//*[@id='radiobuttonFollowupQuestion']/div/label/span[contains(text(),'" + typesofEmployment + "')]";
		WebElement typeofemp = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		typeofemp.click();
		this.nextButton.click();
	}

	public void enterJointHealthAndSafetyCommittee(String safetyCommittee, String safetyRepresentative, String nameOfTheUnion) {
		waitUntilElementVisible(safetyCommitteeYes);
		if(safetyCommittee.equalsIgnoreCase("yes")) {
			safetyCommitteeYes.click();
		} else {
			safetyCommitteeNo.click();
		}

		if(safetyRepresentative.equalsIgnoreCase("yes")) {
			safetyRepresentativeYes.click();
		} else {
			safetyRepresentativeNo.click();
		}

		this.nameOfTheUnion.sendKeys(nameOfTheUnion);
		nextButton.click();
	}
}
