package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7 extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();



	@FindBy(xpath = "//*[@id='myModal' and @class='modalSurveyLink']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@aria-label,'Report an injury or illness Go')]") private WebElement GOButton;
	@FindBy(id = "businessServicesTitle") private WebElement businessServicesTitle;
	@FindBy(id = "reportsGoButton") private WebElement reportsGoButton;
	@FindBy(id = "reportInjuryOrIllness") private WebElement submitNewReportGoButton;
	@FindBy(id = "accountListSingleSelect-wrapper") private WebElement accountListSingleSelect;
	@FindBy(xpath = "//*[@id=\"submitnewreport\"]/span/span") private WebElement submitnewreport;
	@FindBy(id = "privacyPolicyH3") private WebElement privacyPolicyH3;
	@FindBy(xpath = "//*[@id=\"divConcentContainer\"]/div/div/div[1]/span") private WebElement AgreeCheckbox;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;
	@FindBy(id = "injuryIllnessCard") private WebElement injuryIllnessCard;
	@FindBy(id = "exposureCard") private WebElement exposureCard;
	@FindBy(id = "fatalityCard") private WebElement fatalityCard;
	@FindBy(id = "physicalInjuryCard") private WebElement physicalInjuryCard;
	@FindBy(id = "illnessCard") private WebElement illnessCard;
	@FindBy(id = "mentalHealthCard") private WebElement mentalHealthCard;
	@FindBy(id = "nihlCard") private WebElement nihlCard;
	@FindBy(id = "seriousInjuryCard") private WebElement seriousInjuryCard;




	public DIeForm7(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyBusinessHomepage() {

		waitUntilElementVisible(GOButton);
		return GOButton.isDisplayed();

	}


	public boolean clickOnGOButton() throws InterruptedException {
		if(isDisplayed(ModalPopup)) {
			DistributionPopUpNo.click();
		}
//		GOButton.click();
//		Sleep(5000);
		//waitUntilElementVisible(businessServicesTitle);
		Click(GOButton);
		Click(submitNewReportGoButton);
		waitUntilElementVisible(submitnewreport);
		return submitnewreport.isDisplayed();
	}

	public boolean selectAccountAndStartReport(String accountNumber) throws InterruptedException {
		Select(accountListSingleSelect,accountNumber);
		Click(submitnewreport);
		waitUntilElementVisible(privacyPolicyH3);
		Sleep(2000);
		Click(AgreeCheckbox);
		Click(NextButton);
		Sleep(2000);
		if(isDisplayed(ModalPopup)) {
			Click(DistributionPopUpNo);
		}
		waitUntilElementVisible(injuryIllnessCard);
		return injuryIllnessCard.isDisplayed();
	}

	public boolean selectReportType(String reporttype) {
//		if(isDisplayed(ModalPopup)) {
//			Click(DistributionPopUpNo);
//		}
		if(reporttype.equalsIgnoreCase("Injury or Illness")) {
			Click(injuryIllnessCard);
		}
		else if(reporttype.equalsIgnoreCase("Exposure")) {
			Click(exposureCard);
		}
		else if(reporttype.equalsIgnoreCase("Fatality")) {
			Click(fatalityCard);
		}
		Click(NextButton);
		return true;
	}

	public boolean selectInjuryCategory(String injurycategory) {
		if(injurycategory.equalsIgnoreCase("Physical Injury")) {
			Click(physicalInjuryCard);
		}
		else if(injurycategory.equalsIgnoreCase("Illness")) {
			Click(illnessCard);
		}
		else if(injurycategory.equalsIgnoreCase("Mental Health")) {
			Click(mentalHealthCard);
		}
		else if(injurycategory.equalsIgnoreCase("NIHL")) {
			Click(nihlCard);
		}
		else if(injurycategory.equalsIgnoreCase("Serious Injury")) {
			Click(seriousInjuryCard);
		}
		Click(NextButton);
		return true;
	}
}
