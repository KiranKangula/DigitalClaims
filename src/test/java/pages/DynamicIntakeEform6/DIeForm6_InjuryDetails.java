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

public class DIeForm6_InjuryDetails extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(id = "divInjuryDetailsInput") private WebElement injuryDetailsInput;
	@FindBy(id = "injury-date-picker") private WebElement injuryOneTimeDate;
//	@FindBy(id = "dropdownInjuryOneTimeMonth-wrapper") private WebElement injuryOneTimeDateMonth;
//	@FindBy(id = "dropdownInjuryOneTimeYear-wrapper") private WebElement injuryOneTimeDateYear;
	@FindBy(id = "dropdownInjuryOneTimeHours-wrapper") private WebElement injuryOneTimeHours;
	@FindBy(id = "dropdownInjuryOneTimeMinute-wrapper") private WebElement injuryOneTimeMinutes;
	@FindBy(xpath = "//*[@id=\"radioReportedToEmployer\"]/div[1]/label") private WebElement injuryReportedtoEmpl_Yes;
	@FindBy(xpath = "//*[@id=\"radioReportedToEmployer\"]/div[2]/label") private WebElement injuryReportedtoEmpl_No;
	@FindBy(id = "reported-date-picker") private WebElement ReportedDate;

	@FindBy(xpath = "//*[@id=\"addBodyPartsBtn\"]/span/span") private WebElement addBodyPartsButton;
	@FindBy(id = "save") private WebElement saveButton;
	@FindBy(xpath = "//*[@id=\"attachReportButton\"]/span/span") private WebElement attachReportButton;
	@FindBy(xpath = "//*[@id=\"radioPriorClaim\"]/div[1]/label/span") private WebElement witnessInvolvedYes;
	@FindBy(xpath = "//*[@id=\"radioPriorClaim\"]/div[2]/label/span") private WebElement witnessInvolvedNo;
	@FindBy(xpath = "//*[@id=\"AddContactButton\"]/span/span") private WebElement AddContactButton;
	@FindBy(id = "inputFirstName") private WebElement WitnessFirstName;
	@FindBy(id = "inputLastName") private WebElement WitnessLastName;
	@FindBy(id = "inputPosition") private WebElement WitnessPosition;
	@FindBy(id = "inputPhoneNumeber") private WebElement WitnessPhoneNumber;
	@FindBy(id = "btnSave") private WebElement saveButtonWitness;
	@FindBy(xpath = "//*[@id=\"copyFromEmployerAddressButton\"]/span/span") private WebElement copyFromEmployerAddressButton;
	@FindBy(xpath = "//*[@id=\"radioExposureTo\"]/div[1]/label") private WebElement exposureToYes;
	@FindBy(xpath = "//*[@id=\"radioExposureTo\"]/div[2]/label") private WebElement exposureToNo;
	@FindBy(xpath = "//*[@id=\"radioPriorClaim\"]/div[1]/label/span") private WebElement priorClaimYes;
	@FindBy(xpath = "//*[@id=\"radioPriorClaim\"]/div[2]/label/span") private WebElement priorClaimNo;
	@FindBy(xpath = "//*[@id=\"AddClaimButton\"]/span/span") private WebElement AddPreviousClaimButton;
	@FindBy(id = "inputClaimNumber") private WebElement ClaimNumber;
	@FindBy(id = "btnSave") private WebElement saveButtonClaim;
	@FindBy(xpath = "//*[@id=\"radioAreaOfInjury\"]/div[1]/label") private WebElement sameAreaOfInjuryYes;
	@FindBy(xpath = "//*[@id=\"radioAreaOfInjury\"]/div[2]/label") private WebElement sameAreaOfInjuryNo;
	@FindBy(id = "previousInjuryInfo") private WebElement previousInjuryInfo;
	@FindBy(id = "conditionInput") private WebElement conditionInput;
	@FindBy(id = "relatedToWorkInput") private WebElement relatedToWorkInput;
	@FindBy(id = "symptomsInput") private WebElement symptomsInput;
	@FindBy(id = "injury-start-date-picker") private WebElement IllnessStartDate;
	@FindBy(id = "mental-start-date-picker") private WebElement MentalStartDate;

	@FindBy(id = "mental-end-date-picker") private WebElement MentalEndDate;

	@FindBy(xpath = "//*[@id=\"radiobuttonMentalHealth\"]/div[1]/label") private WebElement mentalHealthYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonMentalHealth\"]/div[2]/label") private WebElement mentalHealthNo;




	public DIeForm6_InjuryDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void PhysicalInjuryDetails(String whathappened) {
		waitUntilElementVisible(this.injuryDetailsInput);
		Enter(this.injuryDetailsInput, whathappened);
		Click(nextButton);

	}

	public void enterInjuryDateAndTime(String injuryOneTimeDate) {
		waitUntilElementVisible(this.injuryOneTimeDate);
		Click(this.injuryOneTimeDate);
		Enter(this.injuryOneTimeDate, injuryOneTimeDate);

		Click(nextButton);

	}

	public void selectBodyPart(String physicalInj, String bodypart)  {
		waitUntilElementVisible(this.addBodyPartsButton);
		Click(this.addBodyPartsButton);

		String dynamicXPath = "//*[@id='tabBarContainer']/div/button[contains(text(),'" + physicalInj + "')]";
		WebElement PhysicalInjury = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		Click(PhysicalInjury);

		dynamicXPath = "//*[contains(@id,'CheckboxGroup')]/div//label/span[contains(text(),'" + bodypart + "')]";
		WebElement BodyParts = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		Click(BodyParts);
		waitUntilElementVisible(this.saveButton);
		Click(this.saveButton);
		Sleep(2000);
		Click(nextButton);
	}

	public void selectReportedToEmpl(String ReportedtoEmpl, String ReportedDate)  {
		if(isDisplayed(injuryReportedtoEmpl_Yes)) {
			Click(injuryReportedtoEmpl_Yes);

			Enter(this.ReportedDate, ReportedDate);

			Sleep(2000);
			Click(nextButton);
		}
		else
		{
			Hooks.log("WARNING: Date and Time Injury Reported to Employer is not displayed");
		}
	}

	public void enterWitnessDetails(String witnessInvolved, String witnessFirstName, String witnessLastName, String witnessPosition, String witnessPhoneNumber) {
		waitUntilElementVisible(this.witnessInvolvedYes);
		if(witnessInvolved.equalsIgnoreCase("yes")) {
			Click(this.witnessInvolvedYes);
			Click(AddContactButton);
			Enter(WitnessFirstName, witnessFirstName);
			Enter(WitnessLastName, witnessLastName);
			Enter(WitnessPosition, witnessPosition);
			jsMoveSendKeys(WitnessPhoneNumber, witnessPhoneNumber);
			Click(saveButtonWitness);
		} else {
			Click(this.witnessInvolvedNo);
		}
		Click(nextButton);
	}

	public void enterIncidentAddress() {
		waitUntilElementVisible(this.copyFromEmployerAddressButton);
		Click(this.copyFromEmployerAddressButton);
		Click(nextButton);
	}

	public void enterSimilarInjuryDetails(String similarInjury, String previousClaim, String previousClaimNumber,
										  String sameAreaofInjury, String tellUsAboutPreviousInjury) {
		waitUntilElementVisible(this.exposureToYes);
		if(similarInjury.equalsIgnoreCase("yes")) {
			Click(this.exposureToYes);
			if(previousClaim.equalsIgnoreCase("yes")) {
				Click(this.priorClaimYes);
				Click(AddPreviousClaimButton);
				Enter(ClaimNumber, previousClaimNumber);
				Click(saveButtonClaim);
				if(sameAreaofInjury.equalsIgnoreCase("yes")) {
					Click(this.sameAreaOfInjuryYes);
				} else {
					Click(this.sameAreaOfInjuryNo);
				}
				Enter(this.previousInjuryInfo, tellUsAboutPreviousInjury);

			} else {
				Click(this.priorClaimNo);
			}
		} else {
			Click(this.exposureToNo);
		}
		Click(nextButton);
	}

	public void enterInjuryDetails(String conditionInput, String relatedToWorkInput, String symptomsInput) {
		waitUntilElementVisible(this.conditionInput);
		Enter(this.conditionInput, conditionInput);
		Enter(this.relatedToWorkInput, relatedToWorkInput);
		Enter(this.symptomsInput, symptomsInput);
		Click(nextButton);

	}

	public void enterIllnessDetails(String illnessStartDate) {

		waitUntilElementVisible(this.IllnessStartDate);
		Enter(this.IllnessStartDate, illnessStartDate);

		Click(nextButton);
	}

	public void DescribeInjury(String describeInjury) {

		String dynamicXPath = "//*[@id='radiobuttonDescribedBy']/div//label/span[contains(text(),'" + describeInjury + "')]";
		WebElement DescribeInjury = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		Click(DescribeInjury);
		Click(nextButton);

	}

	public void enterMentalInjuryDetails(String injuryDetailsInput) {

		waitUntilElementVisible(this.injuryDetailsInput);
		Enter(this.injuryDetailsInput, injuryDetailsInput);
		Click(nextButton);
	}

	public void enterMentalInjuryDateDetails(String injuryStartDate, String injuryEndDate) {

		waitUntilElementVisible(this.MentalStartDate);
		Enter(this.MentalStartDate, injuryStartDate);
		Enter(this.MentalEndDate, injuryEndDate);
		Click(nextButton);
	}

	public void InjuredQuestion(String physicalInjury) {

		waitUntilElementVisible(this.mentalHealthYes);
		if(physicalInjury.equalsIgnoreCase("yes")) {
			Click(this.mentalHealthYes);
		} else {
			Click(this.mentalHealthNo);
		}
	}
}
