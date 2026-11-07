package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.time.Duration;

public class DIeForm6_ExposureDetails extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(xpath = "//*[@id=\"radiobuttonOccurance\"]/div[1]/label") private WebElement exposureToInfectiousSubstance;
	@FindBy(xpath = "//*[@id=\"radiobuttonOccurance\"]/div[2]/label") private WebElement exposureToOther;
	@FindBy(id = "exposure-date-picker") private WebElement exposureDate;

	@FindBy(id = "sourceOfInfection-wrapper") private WebElement sourceOfInfection;
	@FindBy(id = "exposureSource") private WebElement exposureSource;
	@FindBy(id = "suspectedSubstance-wrapper") private WebElement suspectedSubstance;
	@FindBy(id = "contactedByCeir-wrapper") private WebElement contactedByCeir;
	@FindBy(id = "affectedAreaDetails") private WebElement affectedAreaDetails;
	@FindBy(id = "protectiveEquipmentInfo") private WebElement protectiveEquipmentInfo;

	public DIeForm6_ExposureDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void enterExposureDetails(String exposureTo, String exposureDate, String howContacted, String sourceofExposure,
									 String whatSubstance, String DescribeWhere, String Whatareyouwearing) throws InterruptedException {
			waitUntilElementVisible(exposureToInfectiousSubstance);
		if(exposureTo.equalsIgnoreCase("infectious Substance")) {
			Click(exposureToInfectiousSubstance);
			waitUntilElementVisible(this.exposureDate);
			Enter(this.exposureDate, exposureDate);
			Click(sourceOfInfection);
			DynamicTextClick(howContacted);
			Enter(exposureSource, sourceofExposure);
			Click(suspectedSubstance);
			DynamicTextClick(whatSubstance);
		} else {
			Click(exposureToOther);
			waitUntilElementVisible(this.exposureDate);
			Enter(this.exposureDate, exposureDate);
			Click(contactedByCeir);
			DynamicTextClick(howContacted);
			Enter(affectedAreaDetails, DescribeWhere);
			Enter(protectiveEquipmentInfo, Whatareyouwearing);

		}
		Click(nextButton);
	}
}
