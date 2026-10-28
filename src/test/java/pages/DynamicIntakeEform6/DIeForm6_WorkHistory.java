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
	@FindBy(id = "start-date-picker") private WebElement startDate;
//	@FindBy(id = "dropdownStartMonth-wrapper") private WebElement startDateMonth;
//	@FindBy(id = "dropdownStartYear-wrapper") private WebElement startDateYear;
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
	@FindBy(xpath = "//*[@id=\"dropdownEmploymentType\"]") private WebElement EmploymentType;

	@FindBy(id = "businessNature") private WebElement businessNature;
	@FindBy(xpath = "//*[@id=\"radiobuttonCommitte\"]/div[1]/label") private WebElement safetyCommitteeYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonCommitte\"]/div[2]/label") private WebElement safetyCommitteeNo;
	@FindBy(xpath = "//*[@id=\"radiobuttonRepresentative\"]/div[1]/label") private WebElement safetyRepresentativeYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonRepresentative\"]/div[2]/label") private WebElement safetyRepresentativeNo;
	@FindBy(xpath = "\t//*[@id=\"attachReportButton\"]/span/span") private WebElement attachReportButton;
	@FindBy(id = "exposure-start-date-picker") private WebElement exposureStartDate;






	public DIeForm6_WorkHistory(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean enterJobDetails(String jobTitle, String startDate, String empDurationYears,
								   String empDurationMonths, String supervisorFirstName, String supervisorLastName, String supervisorPhoneNumber,
								   String companySize, String thirdPartyUnion, String nameOfTheUnion) {

		waitUntilElementVisible(this.jobTitle);
		Enter(this.jobTitle, jobTitle);
		Click(this.startDate);
		Enter(this.startDate, startDate);
		jsMoveSendKeys(this.employmentDurationYears, empDurationYears);
		jsMoveSendKeys(this.employmentDurationMonths, empDurationMonths);
		Click(this.addContactButton);
		Enter(this.SupervisorFirstName, supervisorFirstName);
		Enter(this.SupervisorLastName, supervisorLastName);
		jsMoveSendKeys(this.SupervisorPhoneNumber, supervisorPhoneNumber);
		Click(this.saveButton);
		if(companySize.equalsIgnoreCase("yes")) {
			Click(this.companySizeYes);
		} else {
			Click(this.companySizeNo);
		}
		if(thirdPartyUnion.equalsIgnoreCase("yes")) {
			Click(this.thirdPartyUnionYes);
			waitUntilElementVisible(this.nameOfTheUnion);
			Enter(this.nameOfTheUnion, nameOfTheUnion);
		} else {
			Click(this.thirdPartyUnionNo);
		}
		Click(this.nextButton);
		waitUntilElementVisible(this.relativeYes);
		return this.relativeYes.isDisplayed();
	}

	public void enterJobDetails(String jobTitle, String startDate, String WhatTypeofWork, String companySize) {
		waitUntilElementVisible(this.jobTitle);
		Enter(this.jobTitle, jobTitle);
		Enter(exposureStartDate, startDate);
		Enter(businessNature, WhatTypeofWork);

		if(companySize.equalsIgnoreCase("yes")) {
			Click(this.companySizeYes);
		} else {
			Click(this.companySizeNo);
		}

		Click(this.nextButton);
	}

	public void enterWorkDetails(String relative, String typesofEmployment) {
		if(relative.equalsIgnoreCase("yes")) {
			Click(this.relativeYes);
		} else {
			Click(this.relativeNo);
		}
		selectDropdownValue(EmploymentType, typesofEmployment);
		Click(this.nextButton);
	}

	public void enterJointHealthAndSafetyCommittee(String safetyCommittee, String safetyRepresentative, String nameOfTheUnion) {
		waitUntilElementVisible(safetyCommitteeYes);
		if(safetyCommittee.equalsIgnoreCase("yes")) {
			Click(safetyCommitteeYes);
		} else {
			Click(safetyCommitteeNo);
		}

		if(safetyRepresentative.equalsIgnoreCase("yes")) {
			Click(safetyRepresentativeYes);
		} else {
			Click(safetyRepresentativeNo);
		}

		Enter(this.nameOfTheUnion, nameOfTheUnion);
		Click(nextButton);
	}
}
