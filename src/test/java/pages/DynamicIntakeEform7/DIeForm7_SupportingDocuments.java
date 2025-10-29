package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_SupportingDocuments extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;
	//Lost time and earnings details
	@FindBy(xpath = "//h1[contains(text(),'Supporting documents')]") private WebElement SupportingDocumentsHeading;
	@FindBy(xpath = "//h1[contains(text(),'Additional comments')]") private WebElement AdditionalCommentsHeading;

	public DIeForm7_SupportingDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean attachSupportingDocuments() throws InterruptedException {
		waitUntilElementVisible(SupportingDocumentsHeading);
		NextButton.click();
		Thread.sleep(2000);
		waitUntilElementVisible(AdditionalCommentsHeading);
		NextButton.click();
		return true;
	}
}