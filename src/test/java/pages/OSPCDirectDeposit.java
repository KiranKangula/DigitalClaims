package pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OSPCDirectDeposit extends BaseDriverClass {
	ConfigReader prop = new ConfigReader();
	public static String randommessage;

	@FindBy(id = "directDepositButton")
	private WebElement directDepositButton;

	@FindBy(xpath = "//*[@id=\"diBodySection\"]/div/div/div[2]/div[1]/button/span/span/span")
	private WebElement AddDirectdepositLink;

	@FindBy(xpath = "\t//*[@id=\"diBodySection\"]/div/div/div[2]/div[1]/div[6]/button/span/span/span")
	private WebElement editDirectdepositLink;



	@FindBy(id = "monthDropdown-wrapper")
	private WebElement monthDropdown;

	@FindBy(id = "yearDropdown-wrapper")
	private WebElement yearDropdown;

	@FindBy(xpath = "//*[@id=\"dobinputSection\"]/div/div/button/span/span")
	private WebElement ConfirmButton;

	@FindBy(id = "transitInput")
	private WebElement transitInput;

	@FindBy(id = "institutionInput")
	private WebElement institutionInput;

	@FindBy(id = "accountNumber1")
	private WebElement accountNumber;

	@FindBy(id = "accountNumber2")
	private WebElement Reenter_accountNumber;

	@FindBy(xpath = "//*[@id=\"termagreeSection\"]/div/div/div/div[1]/span")
	private WebElement confirmCheckbox;

	@FindBy(id = "nextButton")
	private WebElement nextButton;

	@FindBy(id = "review")
	private WebElement reviewStep;

	@FindBy(xpath = "//*[@id=\"termagreeSection\"]/div[1]/div[1]/div/div/div[1]/span")
	private WebElement confirmCheckbox_Review;

	@FindBy(xpath = "//*[@id=\"diBodySection\"]/div/div/div[3]/div/div[4]/div/span[2]/a/span")
	private WebElement SubmitButton;

	@FindBy(xpath = "//*[@id=\"navigationSection\"]/a/span")
	private WebElement navigationSection;

	@FindBy(xpath = "//*[@id=\"diBodySection\"]/div/div/div[2]/div[1]/div")
	private WebElement NoDirectDepositMessage;


	public OSPCDirectDeposit(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean LandOnDirectDeposit() {
		directDepositButton.click();
		waitUntilElementVisible(AddDirectdepositLink);
		return AddDirectdepositLink.isDisplayed();
	}

	public boolean AddDirectDepositInformation(String Month, String Year) {
		AddDirectdepositLink.click();
		waitUntilElementVisible(monthDropdown);
		monthDropdown.click();
		//monthDropdown.sendKeys(Month);
		Actions actions = new Actions(getDriver());
		actions.sendKeys(Month).build().perform();//press down arrow key
		Actions actions2 = new Actions(getDriver());
		actions2.sendKeys(Keys.ENTER).build().perform();

		yearDropdown.click();
		//yearDropdown.sendKeys(Year);
		Actions actions3 = new Actions(getDriver());
		actions3.sendKeys(Year).build().perform();//press down arrow key
		Actions actions4 = new Actions(getDriver());
		actions4.sendKeys(Keys.ENTER).build().perform();

		ConfirmButton.click();
		waitUntilElementVisible(transitInput);
		return transitInput.isDisplayed();
	}

	public boolean editDirectDepositInformation(String Month, String Year) {
		editDirectdepositLink.click();
		waitUntilElementVisible(monthDropdown);
		monthDropdown.click();
		//monthDropdown.sendKeys(Month);
		Actions actions = new Actions(getDriver());
		actions.sendKeys(Month).build().perform();//press down arrow key
		Actions actions2 = new Actions(getDriver());
		actions2.sendKeys(Keys.ENTER).build().perform();

		yearDropdown.click();
		//yearDropdown.sendKeys(Year);
		Actions actions3 = new Actions(getDriver());
		actions3.sendKeys(Year).build().perform();//press down arrow key
		Actions actions4 = new Actions(getDriver());
		actions4.sendKeys(Keys.ENTER).build().perform();

		ConfirmButton.click();
		waitUntilElementVisible(transitInput);
		return transitInput.isDisplayed();
	}

	public boolean AddandValidateDDInformation(String transit, String institution, String AcctNumber) {
		transitInput.sendKeys(transit);
		institutionInput.sendKeys(institution);
		accountNumber.sendKeys(AcctNumber);
		Reenter_accountNumber.sendKeys(AcctNumber);
		confirmCheckbox.click();
		nextButton.click();
		waitUntilElementVisible(reviewStep);
		confirmCheckbox_Review.click();
		SubmitButton.click();
		waitUntilElementVisible(navigationSection);
		return navigationSection.isDisplayed();

	}

	public String getClaimNumberfromOSPC() {
		String url = getDriver().getCurrentUrl();
		String claimnumber = url.split("/")[5];
		return claimnumber;
	}

	public boolean verifyDirectDepositRemovalinOSPC() {

		Set<String> windowHandles = getDriver().getWindowHandles();
		List<String> tabs = new ArrayList<>(windowHandles);
		getDriver().close();
		getDriver().switchTo().window(tabs.get(0));
		navigationSection.click();
		waitUntilElementVisible(directDepositButton);
		directDepositButton.click();
		waitUntilElementVisible(NoDirectDepositMessage);
		return NoDirectDepositMessage.isDisplayed();

	}
}
