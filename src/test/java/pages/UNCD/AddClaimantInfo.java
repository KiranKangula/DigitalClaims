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
	@FindBy(id = "birthdate-picker") private WebElement birthdate;
//	@FindBy(id = "birthmonth-select") private WebElement birthmonthSelect;
//	@FindBy(id = "birthyear-select") private WebElement birthyearSelect;
	@FindBy(id = "accidentdate-picker") private WebElement accidentdate;
//	@FindBy(id = "accidentmonth-select") private WebElement accidentmonthSelect;
//	@FindBy(id = "accidentyear-select") private WebElement accidentyearSelect;
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
		Enter(this.claimNumber, claimNumber);
		Enter(this.lastName, lastName);
		Enter(this.birthdate, dateOfBirth);
		this.claimNumber.click();
		scenario.get().log("Claim Number: " + claimNumber + ", Last Name: " + lastName + ", Date of Birth: " + dateOfBirth); // This is a log, not a click
		Click(NextButton);
		try{
			if(dateOfBirthHelperText.isDisplayed()){
				System.out.println("Date of Birth is invalid error message displayed");
				Click(NextButton);
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
		Enter(this.firstName, firstname);
		Enter(this.lastName, lastname);

		Enter(this.birthdate, dateofbirth);
		Enter(this.accidentdate, dateofinjury);
		this.firstName.click();
		Click(confirmCheckbox);
		Click(NextButton);
		try{
			if(dateOfBirthHelperText.isDisplayed()){
				System.out.println("Date of Birth is invalid error message displayed");
				Click(NextButton);
			}
		}
		catch(Exception e){
			System.out.println("Date of Birth is valid");
		}

	}
}