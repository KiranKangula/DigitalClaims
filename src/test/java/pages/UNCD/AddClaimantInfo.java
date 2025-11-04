package pages.UNCD;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

import static definitions.Hooks.scenario;

public class AddClaimantInfo extends BaseDriverClass {

	@FindBy(id = "claim-number") private WebElement claimNumber;
	@FindBy(id = "first-name") private WebElement firstName;
	@FindBy(id = "last-name") private WebElement lastName;
	@FindBy(id = "birthdate-select") private WebElement birthdateSelect;
	@FindBy(id = "birthmonth-select") private WebElement birthmonthSelect;
	@FindBy(id = "birthyear-select") private WebElement birthyearSelect;
	@FindBy(id = "accidentdate-select") private WebElement accidentdateSelect;
	@FindBy(id = "accidentmonth-select") private WebElement accidentmonthSelect;
	@FindBy(id = "accidentyear-select") private WebElement accidentyearSelect;
	@FindBy(xpath = "//*[@type='checkbox']") private WebElement confirmCheckbox;
	@FindBy(xpath = "//*[@type='submit']") private WebElement NextButton;
	@FindBy(xpath = "//*[@id=\"dateof-birth-helper-text\"]/span") private WebElement dateOfBirthHelperText;
	public static String firstname;
	public static String lastname;

	public AddClaimantInfo(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public void navigateToURL(String url) {
		getDriver().get(url);
	}


	public void AddClaimantInformation(String claimNumber, String lastName, String dateOfBirth) {
		waitUntilElementVisible(this.claimNumber);
		this.claimNumber.sendKeys(claimNumber);
		this.lastName.sendKeys(lastName);
		String[] dob = dateOfBirth.split("/");
		this.birthdateSelect.sendKeys(dob[0]);
		this.birthmonthSelect.sendKeys(dob[1]);
		this.birthyearSelect.sendKeys(dob[2]);
		scenario.get().log("Claim Number: " + claimNumber + ", Last Name: " + lastName + ", Date of Birth: " + dateOfBirth);
		NextButton.click();
		try{
			if(dateOfBirthHelperText.isDisplayed()){
				System.out.println("Date of Birth is invalid error message displayed");
				NextButton.click();
			}
		}
		catch(Exception e){
			System.out.println("Date of Birth is valid");
		}


	}

	public void AddClaimantInformationUNCD(String firstname, String lastname, String dateofbirth, String dateofinjury) {
		waitUntilElementVisible(this.firstName);
		if(firstname.contentEquals("<dynamicFN>") && lastname.contentEquals("<dynamicLN>"))
		{
			this.firstname = generateRandomText(5);
			this.lastname = generateRandomText(5);
			firstname = this.firstname;
			lastname= this.lastname;
		}
		scenario.get().log("First Name: " + firstname + ", Last Name: " + lastname + ", Date of Birth: " + dateofbirth );
		this.firstName.sendKeys(firstname);
		this.lastName.sendKeys(lastname);
		String[] dob = dateofbirth.split("/");
		this.birthdateSelect.sendKeys(dob[0]);
		this.birthmonthSelect.sendKeys(dob[1]);
		this.birthyearSelect.sendKeys(dob[2]);
		String[] doi = dateofinjury.split("/");
		this.accidentdateSelect.sendKeys(doi[0]);
		this.accidentmonthSelect.sendKeys(doi[1]);
		this.accidentyearSelect.sendKeys(doi[2]);
		confirmCheckbox.click();
		NextButton.click();
		try{
			if(dateOfBirthHelperText.isDisplayed()){
				System.out.println("Date of Birth is invalid error message displayed");
				NextButton.click();
			}
		}
		catch(Exception e){
			System.out.println("Date of Birth is valid");
		}

	}
}