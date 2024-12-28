package pages;

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
	@FindBy(xpath = "//*[@id=\"radiobuttonOccurance\"]/div[1]/label") private WebElement InjuryTypeOccuranceYes;
	@FindBy(xpath = "//*[@id=\"radiobuttonOccurance\"]/div[2]/label") private WebElement InjuryTypeOccuranceNo;
	@FindBy(id = "inputAcn") private WebElement ExistingClaimNumber;
	@FindBy(id = "inputFirstName") private WebElement FirstName;
	@FindBy(id = "inputLastName") private WebElement LastName;
	@FindBy(id = "dropdownPronoun-wrapper") private WebElement PronounsDropdown;
	@FindBy(id = "inputPhoneNumeber") private WebElement PhoneNumber;
	@FindBy(xpath = "//*[@id=\"radiobuttonGender\"]/div[2]/label") private WebElement GenderMale;
	@FindBy(xpath = "//*[@id=\"radiobuttonGender\"]/div[1]/label")	private WebElement GenderFemale;
	@FindBy(id = "dropdownDobDate-wrapper") private WebElement DateOfBirthDate;
	@FindBy(id = "dropdownDobMonth-wrapper") private WebElement DateOfBirthMonth;
	@FindBy(id = "dropdownDobYear-wrapper") private WebElement DateOfBirthYear;
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
		submitnewreport.click();
		waitUntilElementVisible(AgreeCheckbox);
		return AgreeCheckbox.isDisplayed();

	}

	public boolean clickPrivacyStatement() {
		AgreeCheckbox.click();
		NextButton.click();
		waitUntilElementVisible(InjuryorIllnessRadio);
		return InjuryorIllnessRadio.isDisplayed();

	}

	public boolean selectReportType(String reporttype) {
		if(reporttype.contains("Injury")) {
			InjuryorIllnessRadio.click();
		}
		else if(reporttype.contains("Exposure")) {
			ExposureIncidentRadio.click();
		}
		NextButton.click();
		waitUntilElementVisible(PhysicalInjuryRadio);
		return PhysicalInjuryRadio.isDisplayed();
	}

	public void selectInjuryType(String injurytype) {
		if(injurytype.contains("Physical")) {
			PhysicalInjuryRadio.click();
		}
		else if(injurytype.contains("Illness")) {
			IllnessRadio.click();
		}
		else if(injurytype.contains("Mental")) {
			MentalHealthRadio.click();
		}
		else if(injurytype.contains("Noise")) {
			NoiseInducedHearingLossRadio.click();
		}
		NextButton.click();
	}

	public boolean selectBodyPart(String PhyscialInjury, String bodypart) {
		if(PhyscialInjury.equalsIgnoreCase("yes")) {
			InjuryTypeOccuranceYes.click();
		}
		else if(PhyscialInjury.equalsIgnoreCase("no")) {
			InjuryTypeOccuranceNo.click();
		}

		String dynamicXPath = "//*[@id='radiobuttonFollowupQuestion']/div/label/span[contains(text(),'" + bodypart + "')]";
		WebElement Bodypart = new WebDriverWait(getDriver(), Duration.ofSeconds(20))
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
		Bodypart.click();
		NextButton.click();
		waitUntilElementVisible(ExistingClaimNumber);
		return ExistingClaimNumber.isDisplayed();

	}

	public boolean enterExistingClaimNumber(String existingClaimnumber) {
		if(!existingClaimnumber.isEmpty())
		{
			ExistingClaimNumber.sendKeys(existingClaimnumber);
		}

		NextButton.click();
		waitUntilElementVisible(FirstName);
		return FirstName.isDisplayed();
	}

	public boolean enterPersonalInformation(String firstName, String lastname, String phonenumber, String pronouns) throws InterruptedException {
		FirstName.clear();
		FirstName.sendKeys(firstName);
		LastName.clear();
		LastName.sendKeys(lastname);
		PronounsDropdown.click();
		DynamicTextClick(pronouns);

		jsMoveSendKeys(PhoneNumber, phonenumber);
		NextButton.click();
		waitUntilElementVisible(DateOfBirthDate);
		return DateOfBirthDate.isDisplayed();

	}

	public boolean enterAdditionalDetails(String gender, String day, String month, String year, String preferredLang, String sin) throws InterruptedException {
		if(gender.contains("Male")) {
			GenderMale.click();
		}
		else{
			GenderFemale.click();
		}

		DateOfBirthDate.click();
		Actions actions = new Actions(getDriver());
		actions.sendKeys(day).build().perform();
		actions.sendKeys(Keys.ENTER).build().perform();
		DateOfBirthMonth.click();
		actions.sendKeys(month).build().perform();
		actions.sendKeys(Keys.ENTER).build().perform();
		DateOfBirthYear.click();
		actions.sendKeys(year).build().perform();
		actions.sendKeys(Keys.ENTER).build().perform();
		LanguageDropdown.click();
		DynamicTextClick(preferredLang);
		if(!sin.isEmpty()){
		jsMoveSendKeys(SocialInsuranceNumber, sin);}
		else {
			NoSINCheckbox.click();
		}
		NextButton.click();
		waitUntilElementVisible(AddressLine1);
		return AddressLine1.isDisplayed();
	}

	public boolean enterMailingAddress(String address) throws InterruptedException {
		AddressLine1.clear();
		AddressLine1.sendKeys(address.split("\\|")[0]);
		City.clear();
		City.sendKeys(address.split("\\|")[1]);
		ProvinceDropdown.click();
		Thread.sleep(2000);
		//DynamicTextClick(province);
//		String dynamicXPath = "//*[contains(text(),'" + province + "')]";
//		WebElement Element = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
//		Thread.sleep(6000);
//		Element.click();
		ClickusingActions(address.split("\\|")[2]);
		PostalCode.clear();
		PostalCode.sendKeys(address.split("\\|")[3]);
		CountryDropdown.click();
		Thread.sleep(2000);
		DynamicTextClick(address.split("\\|")[4]);
		Thread.sleep(2000);
		NextButton.click();
		waitUntilElementVisible(EmployerName);
		return EmployerName.isDisplayed();
	}

	public Boolean enterEmployerDetails(String employername, String empPhone, String empAddress1, String empCity, String empProvince, String empPostalcode, String empCountry) throws InterruptedException {
		EmployerName.clear();
		EmployerName.sendKeys(employername);
		jsMoveSendKeys(EmployerPhoneNumber, empPhone);
		EmployerAddressLine1.clear();
		EmployerAddressLine1.sendKeys(empAddress1);
		EmployerCity.clear();
		EmployerCity.sendKeys(empCity);
		EmployerProvinceDropdown.click();
		DynamicTextClick(empProvince);
		EmployerPostalCode.clear();
		EmployerPostalCode.sendKeys(empPostalcode);
		EmployerCountryDropdown.click();
		DynamicTextClick(empCountry);
		NextButton.click();
		waitUntilElementVisible(ReferenceNumber);
		return ReferenceNumber.isDisplayed();
	}

	public String verifyReferenceNumber() {
		String reference =  ReferenceNumber.getText();
		ContinueButton.click();
		return reference;
	}

	public boolean verifyNIHLPage() {
		waitUntilElementVisible(NIHLPageHeader);
		return NIHLPageHeader.getText().contains("Continue to different online service to make a claim for noise-induced hearing loss");
	}
}
