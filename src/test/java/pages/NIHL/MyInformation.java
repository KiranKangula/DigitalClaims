package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class MyInformation extends BaseDriverClass {

	@FindBy(xpath = "//*[@name='firstName']") private WebElement FirstName;
	@FindBy(xpath = "//*[@name='lastName']") private WebElement LastName;
	@FindBy(xpath = "//*[@name='dateOfBirth']") private WebElement DateOfBirth;
	@FindBy(xpath = "//*[@name='Socialinsurancenumber']") private WebElement SocialInsuranceNumber;
	@FindBy(xpath = "//*[@name='streetAddress']") private WebElement StreetAddress;
	@FindBy(xpath = "//*[@name='city']") private WebElement City;
	@FindBy(xpath = "//*[@name='provinceState']") private WebElement Province;
	@FindBy(xpath = "//*[@name='postalCode']") private WebElement PostalCode;
	@FindBy(xpath = "//*[@name='country']") private WebElement Country;
	@FindBy(xpath = "//*[@id='phone1']") private WebElement PhoneNumber;
	@FindBy(xpath = "//*[@aria-label='Language preference']") private WebElement LanguagePreference;
	@FindBy(xpath = "(//*[@aria-label='Next'])[1]") private WebElement NextButton;

	public MyInformation(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean enterMyInformationDetails() throws InterruptedException {

        return null;
    }


	public void EnterMyInformation(String firstname, String lastname, String sin, String address, String phone, String language) {
		waitUntilElementVisible(FirstName);
		FirstName.sendKeys(firstname);
		LastName.sendKeys(lastname);
		if(!sin.isEmpty()){
			SocialInsuranceNumber.sendKeys(sin);
		}
		StreetAddress.sendKeys(address.split("//|")[0]);
		City.sendKeys(address.split("//|")[1]);
		Province.sendKeys(address.split("//|")[2]);
		PostalCode.sendKeys(address.split("//|")[3]);
		Country.sendKeys(address.split("//|")[4]);
		PhoneNumber.sendKeys(phone);
		LanguagePreference.sendKeys(language);
		NextButton.click();

	}
}