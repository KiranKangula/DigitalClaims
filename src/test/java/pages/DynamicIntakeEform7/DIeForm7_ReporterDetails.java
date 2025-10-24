package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_ReporterDetails extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(text(),'Next')]") private WebElement NextButton;
	private static final String ReporterDetails_radio = "reporterDetailsContainer";
	@FindBy(id = "claimReportedByType") private WebElement claimReportedByType;

	public DIeForm7_ReporterDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean selectReportedByDetails(String reportedby) {
		waitUntilElementVisible(claimReportedByType);

		selectRadioButtonOption(ReporterDetails_radio, reportedby);

		NextButton.click();
		return true;
	}
}
