package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import static java.lang.Thread.sleep;

public class DIeForm7_ReportType extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();



	@FindBy(xpath = "//*[@id='myModal' and @class='modalSurveyLink']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;
	private static final String PhysicalInjury_radio = "occurrenceContainer";
	private static final String AccidentType_radio = "oneTimeContainer";
	private static final String InjuryType_radio = "injureContainer";
	@FindBy(id = "natureOfPhysicalInjury") private WebElement natureOfPhysicalInjury;





	public DIeForm7_ReportType(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean selectPhysicalInjuryDetails(String physicalInjury, String accidentType, String describedBy) {
		waitUntilElementVisible(natureOfPhysicalInjury);
		if(isDisplayed(ModalPopup)) {
			Click(DistributionPopUpNo);
		}
		Sleep(3000);
		selectRadioButtonOption(PhysicalInjury_radio, physicalInjury);
		selectRadioButtonOption(AccidentType_radio, accidentType);
		selectRadioButtonOption(InjuryType_radio, describedBy);
		Click(NextButton);
		return true;
	}
}
