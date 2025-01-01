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
	@FindBy(id = "dropdownInjuryOneTimeDate-wrapper") private WebElement injuryOneTimeDateDay;
	@FindBy(id = "dropdownInjuryOneTimeMonth-wrapper") private WebElement injuryOneTimeDateMonth;
	@FindBy(id = "dropdownInjuryOneTimeYear-wrapper") private WebElement injuryOneTimeDateYear;
	@FindBy(id = "dropdownInjuryOneTimeHours-wrapper") private WebElement injuryOneTimeHours;
	@FindBy(id = "dropdownInjuryOneTimeMinute-wrapper") private WebElement injuryOneTimeMinutes;
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
	@FindBy(id = "dropdownInjuryStartDate-wrapper") private WebElement IllnessStartDateDay;
	@FindBy(id = "dropdownInjuryStartMonth-wrapper") private WebElement IllnessStartDateMonth;
	@FindBy(id = "dropdownInjuryStartYear-wrapper") private WebElement IllnessStartDateYear;
	@FindBy(id = "dropdownMentalStartDate-wrapper") private WebElement MentalStartDateDay;
	@FindBy(id = "dropdownMentalStartMonth-wrapper") private WebElement MentalStartDateMonth;
	@FindBy(id = "dropdownMentalStartYear-wrapper") private WebElement MentalStartDateYear;
	@FindBy(id = "dropdownMentalEndDate-wrapper") private WebElement MentalEndDateDay;
	@FindBy(id = "dropdownMentalEndMonth-wrapper") private WebElement MentalEndDateMonth;
	@FindBy(id = "dropdownMentalEndYear-wrapper") private WebElement MentalEndDateYear;
	@FindBy(xpath = "//*[@id=\"radiobuttonMentalHealth\"]/div[1]/label") private WebElement mentalHealthYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonMentalHealth\"]/div[2]/label") private WebElement mentalHealthNo;




	public DIeForm6_InjuryDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void PhysicalInjuryDetails(String whathappened) {
		waitUntilElementVisible(this.injuryDetailsInput);
		this.injuryDetailsInput.sendKeys(whathappened);
		nextButton.click();

	}

	public void enterInjuryDateAndTime(String injuryOneTimeDateDay, String injuryOneTimeDateMonth, String injuryOneTimeDateYear) {
		waitUntilElementVisible(this.injuryOneTimeDateDay);
		this.injuryOneTimeDateDay.click();
		ClickusingActions(injuryOneTimeDateDay);
		this.injuryOneTimeDateMonth.click();
		ClickusingActions(injuryOneTimeDateMonth);
		this.injuryOneTimeDateYear.click();
		ClickusingActions(injuryOneTimeDateYear);
		nextButton.click();

	}

	public void selectBodyPart(String physicalInj, String bodypart) throws InterruptedException {
		waitUntilElementVisible(this.addBodyPartsButton);
		this.addBodyPartsButton.click();

		String dynamicXPath = "//*[@id='tabBarContainer']/div/button[contains(text(),'" + physicalInj + "')]";
		WebElement PhysicalInjury = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		PhysicalInjury.click();

		dynamicXPath = "//*[contains(@id,'CheckboxGroup')]/div//label/span[contains(text(),'" + bodypart + "')]";
		WebElement BodyParts = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		BodyParts.click();
		waitUntilElementVisible(this.saveButton);
		this.saveButton.click();
		Thread.sleep(2000);
		nextButton.click();
	}

	public void enterWitnessDetails(String witnessInvolved, String witnessFirstName, String witnessLastName, String witnessPosition, String witnessPhoneNumber) {
		waitUntilElementVisible(this.witnessInvolvedYes);
		if(witnessInvolved.equalsIgnoreCase("yes")) {
			this.witnessInvolvedYes.click();
			AddContactButton.click();
			WitnessFirstName.sendKeys(witnessFirstName);
			WitnessLastName.sendKeys(witnessLastName);
			WitnessPosition.sendKeys(witnessPosition);
			jsMoveSendKeys(WitnessPhoneNumber, witnessPhoneNumber);
			saveButtonWitness.click();
		} else {
			this.witnessInvolvedNo.click();
		}
		nextButton.click();
	}

	public void enterIncidentAddress() {
		waitUntilElementVisible(this.copyFromEmployerAddressButton);
		this.copyFromEmployerAddressButton.click();
		nextButton.click();
	}

	public void enterSimilarInjuryDetails(String similarInjury, String previousClaim, String previousClaimNumber,
										  String sameAreaofInjury, String tellUsAboutPreviousInjury) {
		waitUntilElementVisible(this.exposureToYes);
		if(similarInjury.equalsIgnoreCase("yes")) {
			this.exposureToYes.click();
			if(previousClaim.equalsIgnoreCase("yes")) {
				this.priorClaimYes.click();
				AddPreviousClaimButton.click();
				ClaimNumber.sendKeys(previousClaimNumber);
				saveButtonClaim.click();
				if(sameAreaofInjury.equalsIgnoreCase("yes")) {
					this.sameAreaOfInjuryYes.click();
				} else {
					this.sameAreaOfInjuryNo.click();
				}
				this.previousInjuryInfo.sendKeys(tellUsAboutPreviousInjury);

			} else {
				this.priorClaimNo.click();
			}
		} else {
			this.exposureToNo.click();
		}
		nextButton.click();
	}

	public void enterInjuryDetails(String conditionInput, String relatedToWorkInput, String symptomsInput) {
		waitUntilElementVisible(this.conditionInput);
		this.conditionInput.sendKeys(conditionInput);
		this.relatedToWorkInput.sendKeys(relatedToWorkInput);
		this.symptomsInput.sendKeys(symptomsInput);
		nextButton.click();

	}

	public void enterIllnessDetails(String illnessStartDate) {

		waitUntilElementVisible(this.IllnessStartDateDay);
		this.IllnessStartDateDay.click();
		ClickusingActions(illnessStartDate.split("\\|")[0]);
		this.IllnessStartDateMonth.click();
		ClickusingActions(illnessStartDate.split("\\|")[1]);
		this.IllnessStartDateYear.click();
		ClickusingActions(illnessStartDate.split("\\|")[2]);
		nextButton.click();
	}

	public void DescribeInjury(String describeInjury) {

		String dynamicXPath = "//*[@id='radiobuttonDescribedBy']/div//label/span[contains(text(),'" + describeInjury + "')]";
		WebElement DescribeInjury = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		DescribeInjury.click();
		nextButton.click();

	}

	public void enterMentalInjuryDetails(String injuryDetailsInput) {

		waitUntilElementVisible(this.injuryDetailsInput);
		this.injuryDetailsInput.sendKeys(injuryDetailsInput);
		nextButton.click();
	}

	public void enterMentalInjuryDateDetails(String injuryStartDate, String injuryEndDate) {

		waitUntilElementVisible(this.MentalStartDateDay);
		this.MentalStartDateDay.click();
		ClickusingActions(injuryStartDate.split("\\|")[0]);
		this.MentalStartDateMonth.click();
		ClickusingActions(injuryStartDate.split("\\|")[1]);
		this.MentalStartDateYear.click();
		ClickusingActions(injuryStartDate.split("\\|")[2]);
		this.MentalEndDateDay.click();
		ClickusingActions(injuryEndDate.split("\\|")[0]);
		this.MentalEndDateMonth.click();
		ClickusingActions(injuryEndDate.split("\\|")[1]);
		this.MentalEndDateYear.click();
		ClickusingActions(injuryEndDate.split("\\|")[2]);
		nextButton.click();
	}

	public void InjuredQuestion(String physicalInjury) {

		waitUntilElementVisible(this.mentalHealthYes);
		if(physicalInjury.equalsIgnoreCase("yes")) {
			this.mentalHealthYes.click();
		} else {
			this.mentalHealthNo.click();
		}
	}
}
