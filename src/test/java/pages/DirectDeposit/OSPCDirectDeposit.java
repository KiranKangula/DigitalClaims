package pages.DirectDeposit;

import definitions.Hooks;
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
	ConfigReader prop= Hooks.getConfigReader();
	public static String randommessage;

	@FindBy(id = "setupDepositButton") private WebElement directDepositButton;
	@FindBy(id = "Question1-wrapper") private WebElement Question1;
	@FindBy(id = "answer_oneQuestion1") private WebElement answer_oneQuestion1;
	@FindBy(id = "Question2-wrapper") private WebElement Question2;
	@FindBy(id = "answer_oneQuestion2") private WebElement answer_oneQuestion2;
	@FindBy(id = "Question3-wrapper") private WebElement Question3;
	@FindBy(id = "answer_oneQuestion3") private WebElement answer_oneQuestion3;



	@FindBy(xpath = "//*[@id=\"diBodySection\"]/div/div/div[2]/div/div[2]/h1") private WebElement AddDirectdepositHeader;
	@FindBy(xpath = "//*[contains(text(), 'Edit')]") private WebElement editDirectdepositLink;
	@FindBy(id = "monthDropdown-wrapper") private WebElement monthDropdown;
	@FindBy(id = "yearDropdown-wrapper") private WebElement yearDropdown;
	@FindBy(xpath = "//*[@id=\"dobinputSection\"]/div/div/button/span/span") private WebElement ConfirmButton;
	@FindBy(id = "transitInput") private WebElement transitInput;
	@FindBy(id = "institutionInput") private WebElement institutionInput;
	@FindBy(id = "accountNumber1") private WebElement accountNumber;
	@FindBy(id = "accountNumber2") private WebElement Reenter_accountNumber;
	@FindBy(xpath = "//*[@id=\"termagreeSection\"]/div/div/div/div[1]/span") private WebElement confirmCheckbox;
	@FindBy(id = "nextButton") private WebElement nextButton;
	@FindBy(id = "review") private WebElement reviewStep;
	@FindBy(xpath = "//*[@id=\"termagreeSection\"]/div[1]/div[1]/div/div/div[1]/span") private WebElement confirmCheckbox_Review;
	@FindBy(xpath = "//*[contains(text(), 'Submit')]") private WebElement SubmitButton;
	@FindBy(xpath = "//*[contains(text(), 'Submit changes')]") private WebElement SubmitChanges;

	@FindBy(xpath = "//*[@id=\"navigationSection\"]/a/span") private WebElement navigationSection;
	@FindBy(xpath = "//*[@id=\"diBodySection\"]/div/div/div[2]/div[1]/div") private WebElement NoDirectDepositMessage;

	public OSPCDirectDeposit(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public boolean LandOnDirectDeposit()  {
		Click(directDepositButton);
		Sleep(2000);
		waitUntilElementVisible(Question1);
		return	Question1.isDisplayed();

	}

	public boolean AddDirectDepositInformation() {
		//Click(AddDirectdepositLink);
		//waitUntilElementVisible(monthDropdown);
		//Click(monthDropdown);
		selectDropdownValue(Question1, "Name of the city or town where your grandparents live(d).");
		Enter(answer_oneQuestion1, "Toronto");
		selectDropdownValue(Question2, "In what city or town did your parents meet?");
		Enter(answer_oneQuestion2, "Toronto");
		selectDropdownValue(Question3, "Name of a college or university you applied to but didn't attend.");
		Enter(answer_oneQuestion3, "Toronto");
		Click(nextButton);

		if(isDisplayed(AddDirectdepositHeader)) {
			return AddDirectdepositHeader.isDisplayed();
		}else if(isDisplayed(editDirectdepositLink)) {
			return editDirectdepositLink.isDisplayed();
		}
		return false;
	}

	public boolean editDirectDepositInformation(String Month, String Year) {
		if(isDisplayed(editDirectdepositLink)) {
			Click(editDirectdepositLink);
			waitUntilElementVisible(transitInput);
		}
		else{
			AddDirectDepositInformation();
		}
		return transitInput.isDisplayed();
	}

	public boolean AddandValidateDDInformation(String transit, String institution, String AcctNumber) {
		if(isDisplayed(editDirectdepositLink)) {
			Click(editDirectdepositLink);
			waitUntilElementVisible(transitInput);
		}
		Enter(transitInput, transit);
		Enter(institutionInput, institution);
		Enter(accountNumber, AcctNumber);
		Enter(Reenter_accountNumber, AcctNumber);
		Click(confirmCheckbox);
		if(isDisplayed(nextButton))
		{
			Click(nextButton);
		}else {
			Click(SubmitChanges);
		}

		if(isDisplayed(confirmCheckbox_Review))
		{
			Click(confirmCheckbox_Review);
			Click(SubmitButton);
		}
		waitUntilElementVisible(nextButton);
		return nextButton.isDisplayed();

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
		getDriver().switchTo().window(tabs.get(0)); // Assuming the first tab is the main one
		Click(navigationSection);
		waitUntilElementVisible(directDepositButton);
		Click(directDepositButton);
		waitUntilElementVisible(NoDirectDepositMessage);
		return NoDirectDepositMessage.isDisplayed();

	}
}
