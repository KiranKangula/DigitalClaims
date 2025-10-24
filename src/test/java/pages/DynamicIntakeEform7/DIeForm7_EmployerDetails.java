package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_EmployerDetails extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']")
	private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']")
	private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(text(),'Next')]")
	private WebElement NextButton;
	@FindBy(id = "employerDetailsHeading")
	private WebElement employerDetailsHeading;
	@FindBy(xpath = "//*[@id=\"claimsNumberBody\"]/p")
	private WebElement ReferenceNumber;
	@FindBy(id = "onBoardContinueId")
	private WebElement onBoardContinueId;
	private static final String employerInfoAddress_radio = "employerInfoAddress";
	private static final String intakeBodySection_checkbox = "intakeBodySection";
	@FindBy(id = "dropdownYear-wrapper")
	private WebElement dropdownYear;
	@FindBy(id = "Button")
	private WebElement SearchButton;
	private static final String NAICS_radio = "radiobutton";


	public DIeForm7_EmployerDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public String enterEmployerDetails(String mailingaddress, String branchAddress, String businessYear, String naicsCode) throws InterruptedException {
		waitUntilElementVisible(employerDetailsHeading);
		NextButton.click();
		String RefNum = ReferenceNumber.getText();
		onBoardContinueId.click();

		selectRadioButtonOption(employerInfoAddress_radio, mailingaddress);
		NextButton.click();
		selectRadioButtonOption(employerInfoAddress_radio, branchAddress);
		NextButton.click();
		selectRadioButtonOption(intakeBodySection_checkbox, "Pick");
		Select(dropdownYear, businessYear);
		SearchButton.click();
		selectRadioButtonOption(NAICS_radio, naicsCode);
		NextButton.click();
		return RefNum;
	}
}