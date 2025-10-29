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
	@FindBy(xpath = "//*[contains(@aria-label,'Go to Report an injury or illness (Form 7)')]") private WebElement GOButton;
	@FindBy(id = "businessServicesTitle") private WebElement businessServicesTitle;
	@FindBy(id = "reportsGoButton") private WebElement reportsGoButton;
	@FindBy(id = "submitNewReportHeading") private WebElement submitNewReportHeading;
	@FindBy(id = "accountListSingleSelect-wrapper") private WebElement accountListSingleSelect;
	@FindBy(id = "submitnewreport") private WebElement submitnewreport;
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
		waitUntilElementVisible(reportsGoButton);

		return reportsGoButton.isDisplayed();

	}


	public boolean clickOnGOButton() throws InterruptedException {
		if(isDisplayed(ModalPopup)) {
			DistributionPopUpNo.click();
		}
//		GOButton.click();
//		Thread.sleep(5000);
		waitUntilElementVisible(businessServicesTitle);

		reportsGoButton.click();
		waitUntilElementVisible(submitNewReportHeading);
		return submitNewReportHeading.isDisplayed();
	}

	public boolean selectAccountAndStartReport(String accountNumber) throws InterruptedException {
		Select(accountListSingleSelect,accountNumber);
		submitnewreport.click();
		waitUntilElementVisible(privacyPolicyH3);
		Thread.sleep(2000);
		AgreeCheckbox.click();
		NextButton.click();
		Thread.sleep(2000);
		if(isDisplayed(ModalPopup)) {
			DistributionPopUpNo.click();
		}
		waitUntilElementVisible(injuryIllnessCard);
		return injuryIllnessCard.isDisplayed();
	}

	public boolean selectReportType(String reporttype) {
//		if(isDisplayed(ModalPopup)) {
//			DistributionPopUpNo.click();
//		}
		if(reporttype.equalsIgnoreCase("Injury or Illness")) {
			injuryIllnessCard.click();
		}
		else if(reporttype.equalsIgnoreCase("Exposure")) {
			exposureCard.click();
		}
		else if(reporttype.equalsIgnoreCase("Fatality")) {
			fatalityCard.click();
		}
		NextButton.click();
		return true;
	}

	public boolean selectInjuryCategory(String injurycategory) {
		if(injurycategory.equalsIgnoreCase("Physical Injury")) {
			physicalInjuryCard.click();
		}
		else if(injurycategory.equalsIgnoreCase("Illness")) {
			illnessCard.click();
		}
		else if(injurycategory.equalsIgnoreCase("Mental Health")) {
			mentalHealthCard.click();
		}
		else if(injurycategory.equalsIgnoreCase("NIHL")) {
			nihlCard.click();
		}
		else if(injurycategory.equalsIgnoreCase("Serious Injury")) {
			seriousInjuryCard.click();
		}

		NextButton.click();
		return true;
	}
}
