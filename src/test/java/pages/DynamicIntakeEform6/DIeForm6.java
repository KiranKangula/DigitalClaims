package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.time.Duration;
import java.util.Date;

import static definitions.Hooks.scenario;

public class DIeForm6 extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(id = "submitnewreport") private WebElement submitnewreport;
	@FindBy(xpath = "//*[@id=\"divConcentContainer\"]/div/div/div[1]/span") private WebElement AgreeCheckbox;
	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement NextButton;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[1]/div/div[4]") private WebElement InjuryorIllnessRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[2]/div/div[4]") private WebElement ExposureIncidentRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[1]/div/div[4]") private WebElement PhysicalInjuryRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[2]/div/div[4]") private WebElement IllnessRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[3]/div/div[4]") private WebElement MentalHealthRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[4]/div/div[4]") private WebElement NoiseInducedHearingLossRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[1]/div/div[4]") private WebElement ConstructionIncidentRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttoncard\"]/div[2]/div/div[4]") private WebElement OtherIncidentRadio;
	@FindBy(xpath = "//*[@id=\"radiobuttonOccurance\"]/div[1]/label") private WebElement InjuryTypeOccuranceYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonOccurance\"]/div[2]/label") private WebElement InjuryTypeOccuranceNo;
	@FindBy(id = "inputAcn") private WebElement ExistingClaimNumber;
	@FindBy(id = "inputFirstName") private WebElement FirstName;
	@FindBy(id = "inputLastName") private WebElement LastName;
	@FindBy(id = "dropdownPronoun-wrapper") private WebElement PronounsDropdown;
	@FindBy(id = "inputPhoneNumeber") private WebElement PhoneNumber;
	@FindBy(xpath = "//*[@id=\"radiobuttonGender\"]/div[2]/label") private WebElement GenderMale;
	@FindBy(xpath = "//*[@id=\"radiobuttonGender\"]/div[1]/label")	private WebElement GenderFemale;
	@FindBy(id = "dob-atepicker") private WebElement DateOfBirthDate;
//	@FindBy(id = "dropdownDobMonth-wrapper") private WebElement DateOfBirthMonth;
//	@FindBy(id = "dropdownDobYear-wrapper") private WebElement DateOfBirthYear;
	@FindBy(id = "dropdownLanguage-wrapper") private WebElement LanguageDropdown;
	@FindBy(id = "inputSin") private WebElement SocialInsuranceNumber;
	@FindBy(xpath = "//*[@id=\"divNoSinContainer\"]/div/div/div[1]/span") private WebElement NoSINCheckbox;
	@FindBy(id = "inputAddressLine1") private WebElement AddressLine1;
	@FindBy(id = "inputCity") private WebElement City;
	@FindBy(id = "dropdownProvince-wrapper") private WebElement ProvinceDropdown;
	@FindBy(id = "inputPostalCode") private WebElement PostalCode;
	@FindBy(id = "dropdownCountry-wrapper") private WebElement CountryDropdown;
	@FindBy(id = "inputEmployer") private WebElement EmployerName;
	@FindBy(id = "inputPhoneNumber") private WebElement EmployerPhoneNumber;
	@FindBy(id = "inputAddressLine1") private WebElement EmployerAddressLine1;
	@FindBy(id = "inputCity") private WebElement EmployerCity;
	@FindBy(id = "dropdownProvince-wrapper") private WebElement EmployerProvinceDropdown;
	@FindBy(id = "inputPostalCode") private WebElement EmployerPostalCode;
	@FindBy(id = "dropdownCountry-wrapper") private WebElement EmployerCountryDropdown;
	@FindBy(xpath = "//*[@id=\"claimsNumberBody\"]/p") private WebElement ReferenceNumber;
	@FindBy(xpath = "//*[@id=\"onBoardContinueId\"]/span") private WebElement ContinueButton;
	@FindBy(xpath = "//*[@id=\"privacyPolicyH3\"]") private WebElement NIHLPageHeader;
	@FindBy(xpath = "//*[@id=\"radiobuttonCompanySize\"]/div[2]/label/span") private WebElement SubmissionReqNo;
	@FindBy(xpath = "//*[@id=\"radiobuttonCompanySize\"]/div[1]/label/span") private WebElement SubmissionReqYes;




	public DIeForm6(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifySubmitReportButtonDisplayed() {
		waitUntilElementVisible(submitnewreport);
		return submitnewreport.isDisplayed();

	}

	public boolean clickSubmitReportButton() {
		waitUntilElementVisible(submitnewreport);
		Click(submitnewreport);
		waitUntilElementVisible(AgreeCheckbox);
		return AgreeCheckbox.isDisplayed();

	}

	public boolean clickPrivacyStatement() {
		Click(AgreeCheckbox);
		jsMoveClick(NextButton);
		//NextButton.click();
		waitUntilElementVisible(InjuryorIllnessRadio);
		return InjuryorIllnessRadio.isDisplayed();

	}

	public void selectReportType(String reporttype) {
		if(reporttype.contains("Injury")) {
			Click(InjuryorIllnessRadio);
		}
		else if(reporttype.contains("Exposure")) {
			Click(ExposureIncidentRadio);
		}
		Click(NextButton);
	}

	public void selectInjuryType(String injurytype) {
		if(injurytype.contains("Physical")) {
			Click(PhysicalInjuryRadio);
		}
		else if(injurytype.contains("Illness")) {
			Click(IllnessRadio);
		}
		else if(injurytype.contains("Mental")) {
			Click(MentalHealthRadio);
		}
		else if(injurytype.contains("Noise")) {
			Click(NoiseInducedHearingLossRadio);
		}
		else if(injurytype.contains("Construction")) {
			Click(ConstructionIncidentRadio);
		}
		else if(injurytype.contains("Other")) {
			Click(OtherIncidentRadio);
		}
		Click(NextButton);
	}

	public boolean selectBodyPart(String PhyscialInjury, String bodypart) {
		if(PhyscialInjury.equalsIgnoreCase("yes")) {
			Click(InjuryTypeOccuranceYes);
		}
		else if(PhyscialInjury.equalsIgnoreCase("no")) {
			Click(InjuryTypeOccuranceNo);
		}

		String dynamicXPath = "//*[@id='radiobuttonFollowupQuestion']/div/label/span[contains(text(),'" + bodypart + "')]";
		WebElement Bodypart = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		Click(Bodypart);
		Click(NextButton);
		waitUntilElementVisible(ExistingClaimNumber);
		return ExistingClaimNumber.isDisplayed();

	}

	public boolean enterExistingClaimNumber(String existingClaimnumber) {
		if(!existingClaimnumber.isEmpty())
		{
			Enter(ExistingClaimNumber, existingClaimnumber);
		}

		Click(NextButton);
		waitUntilElementVisible(FirstName);
		return FirstName.isDisplayed();
	}

	public boolean enterPersonalInformation(String firstName, String lastname, String phonenumber, String pronouns) throws InterruptedException {
		Enter(FirstName, firstName);
		Enter(LastName, lastname);
		Click(PronounsDropdown);
		DynamicTextClick(pronouns);

		jsMoveSendKeys(PhoneNumber, phonenumber);
		Click(NextButton);
		waitUntilElementVisible(DateOfBirthDate);
		return DateOfBirthDate.isDisplayed();

	}

	public boolean enterAdditionalDetails(String gender, String DateofBirth,  String preferredLang, String sin) throws InterruptedException {
		if(gender.contains("Male")) {
			Click(GenderMale);
		}
		else{
			Click(GenderFemale);
		}

		Click(DateOfBirthDate);
		Enter(DateOfBirthDate, DateofBirth);
		Click(LanguageDropdown);
		DynamicTextClick(preferredLang);
		if(!sin.isEmpty()){
		jsMoveSendKeys(SocialInsuranceNumber, sin);}
		else {
			Click(NoSINCheckbox);
		}
		Click(NextButton);
		waitUntilElementVisible(AddressLine1);
		return AddressLine1.isDisplayed();
	}

	public boolean enterAdditionalDetails(String gender, String DateofBirth) throws InterruptedException {
		if(gender.contains("Male")) {
			Click(GenderMale);
		}
		else{
			Click(GenderFemale);
		}

		Click(DateOfBirthDate);
		Enter(DateOfBirthDate, DateofBirth);

		Click(NextButton);
		waitUntilElementVisible(AddressLine1);
		return AddressLine1.isDisplayed();
	}

	public boolean enterMailingAddress(String address)  {
		Enter(AddressLine1, address.split("\\|")[0]);
		Enter(City, address.split("\\|")[1]);
		Click(ProvinceDropdown);
		Sleep(2000);
		//DynamicTextClick(province);
//		String dynamicXPath = "//*[contains(text(),'" + province + "')]";
//		WebElement Element = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
//		Sleep(6000);
//		Element.click();
		ClickusingActions(address.split("\\|")[2]);
		Enter(PostalCode, address.split("\\|")[3]);
		Click(CountryDropdown);
		Sleep(2000);
		DynamicTextClick(address.split("\\|")[4]);
		Sleep(2000);
		Click(NextButton);
		waitUntilElementVisible(EmployerName);
		return EmployerName.isDisplayed();
	}

	public Boolean enterEmployerDetails(String employername, String empPhone, String EmployerAddress) throws InterruptedException {
		Enter(EmployerName, employername);
		jsMoveSendKeys(EmployerPhoneNumber, empPhone);
		Enter(EmployerAddressLine1, EmployerAddress.split("\\|")[0]);
		Enter(EmployerCity, EmployerAddress.split("\\|")[1]);
		Click(EmployerProvinceDropdown);
		DynamicTextClick(EmployerAddress.split("\\|")[2]);
		Enter(EmployerPostalCode, EmployerAddress.split("\\|")[3]);
		Click(EmployerCountryDropdown);
		DynamicTextClick(EmployerAddress.split("\\|")[4]);
		Click(NextButton);
		waitUntilElementVisible(ReferenceNumber);
		return ReferenceNumber.isDisplayed();
	}

	public String verifyReferenceNumber() {
		scenario.get().log("E-Form 6 Reference Number is: " + ReferenceNumber.getText());
		String reference =  ReferenceNumber.getText();
		Click(ContinueButton);
		return reference;
	}

	public boolean verifyNIHLPage() {
		waitUntilElementVisible(NIHLPageHeader);
		return NIHLPageHeader.getText().contains("Continue to different online service to make a claim for noise-induced hearing loss");
	}

	public void selectSubmissionRequirement(String submissionRequirement) {
		if (submissionRequirement.equalsIgnoreCase("yes")) {
			Click(SubmissionReqYes);
		} else {
			Click(SubmissionReqNo);
		}
		Click(NextButton);
	}
}
