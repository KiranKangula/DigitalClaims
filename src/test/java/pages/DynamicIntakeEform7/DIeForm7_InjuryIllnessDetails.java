package pages.DynamicIntakeEform7;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm7_InjuryIllnessDetails extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id='myModal' and @class='modal show']") private WebElement ModalPopup;
	@FindBy(xpath = "//*[@class='DistributionPopUpButton2']") private WebElement DistributionPopUpNo;
	@FindBy(xpath = "//*[contains(@class,'rightImgIcon')]") private WebElement NextButton;

	@FindBy(id = "injuryDetailsOneTime") private WebElement InjuryDetailsHeading;
	@FindBy(id = "injuryDetailsInput") private WebElement WhatHappenedInput;
	@FindBy(id = "dateContainer") private WebElement dateContainerW;
	private String InjuryDate = "injuryDatePicker";
//	@FindBy(id = "dropdownDobDate-wrapper") private WebElement InjuryDateDay;
//	@FindBy(id = "dropdownDobMonth-wrapper") private WebElement InjuryDateMonth;
//	@FindBy(id = "dropdownDobYear-wrapper") private WebElement InjuryDateYear;
	//Injury or illness details - affected areas
	@FindBy(id = "selectBodyparts") private WebElement selectBodypartsW;
	@FindBy(id = "addBodyPartsBtn") private WebElement addBodyPartsBtn;
	private static final String BodyParts_Checkbox = "editorContainer";
	@FindBy(id = "save") private WebElement save;
	//Injury or illness reporting details
	@FindBy(xpath = "//h1[contains(text(),'Injury or illness reporting details')]") private WebElement ReportingDetailsHeader;
	private String EmployerKnownDate = "injuryDatePicker";
//	@FindBy(id = "dropdownDobDate-wrapper") private WebElement EmployerKnownDateDay;
//	@FindBy(id = "dropdownDobMonth-wrapper") private WebElement EmployerKnownDateMonth;
//	@FindBy(id = "dropdownDobYear-wrapper") private WebElement EmployerKnownDateYear;
	//Injury or illness reporting details - contact
	@FindBy(xpath = "//h1[contains(text(),'Injury or illness reporting details')]") private WebElement ReportingContactHeader;
	@FindBy(id = "addSupervisorDetails") private WebElement AddContactButton;
	@FindBy(id = "myCard") private WebElement myCardContact;
	@FindBy(id = "firstName") private WebElement ContactfirstName;
	@FindBy(id = "lastName") private WebElement ContactlastName;
	@FindBy(id = "LanguageofService-wrapper") private WebElement ContactLanguage;
	@FindBy(id = "workNumber") private WebElement ContactphoneNumber;
	@FindBy(id = "saveSupervisorDetails") private WebElement ContactSaveButton;
	//Witness information
	@FindBy(id = "witnessInformation") private WebElement WitnessInformationHeading;
	private static final String Witness_radio = "radiobuttonWitnessInformation";
	//Injury or illness details - incident location
	@FindBy(id = "InjuryIncidentLocation") private WebElement InjuryIncidentLocationHeading;
	@FindBy(id = "copyAddressButton") private WebElement copyAddressButton;
	//Injury or illness details - prior related condition
	@FindBy(xpath = "//h1[contains(text(),'prior related condition')]") private WebElement PriorConditionHeader;
	private static final String PriorConditions_radio = "radiobuttonReporttoMinEnv";
	//Injury or illness details - third party
	@FindBy(xpath = "//h1[contains(text(),'third party')]") private WebElement ThirdPartyHeader;
	private static final String ThirdParty_radio = "thirdPartyInfoInvolved";
	//Injury or illness details - objection
	@FindBy(xpath = "//h1[contains(text(),'objection')]") private WebElement ObjectionHeader;
	private static final String Objection_radio = "radiobuttonobjectionQuestion";



	public DIeForm7_InjuryIllnessDetails(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean selectInjuryIllnessDetails(String whatHappenedInput, String injuryDate, String bodyParts, String employerKnownDate, String reportedTo, String witness, String priorConditions, String thirdParty, String objection) throws InterruptedException {
		waitUntilElementVisible(InjuryDetailsHeading);
		Enter(WhatHappenedInput, whatHappenedInput); // This is a sendKeys, not a click
		Click(NextButton);
		waitUntilElementVisible(dateContainerW);
		DatePicker(InjuryDate, injuryDate);
		Click(NextButton);
		waitUntilElementVisible(selectBodypartsW);
		Click(addBodyPartsBtn);
		selectRadioButtonOption(BodyParts_Checkbox, bodyParts); // This is a radio button selection, not a direct click
		Click(save);
		Sleep(2000);
		Click(NextButton);
		waitUntilElementVisible(ReportingDetailsHeader);
		DatePicker(EmployerKnownDate, employerKnownDate);
		Click(NextButton);
		waitUntilElementVisible(ReportingContactHeader);
		Click(AddContactButton);
		waitUntilElementVisible(myCardContact);
		Enter(ContactfirstName, reportedTo.split("\\|")[0]);
		Enter(ContactlastName, reportedTo.split("\\|")[1]);
		Select(ContactLanguage, reportedTo.split("\\|")[3]);
		jsMoveSendKeys(ContactphoneNumber,reportedTo.split("\\|")[2] );
		Click(ContactSaveButton);
		Click(NextButton);
		waitUntilElementVisible(WitnessInformationHeading);
		selectRadioButtonOption(Witness_radio, witness); // This is a radio button selection, not a direct click
		Click(NextButton);
		waitUntilElementVisible(InjuryIncidentLocationHeading);
		Click(copyAddressButton);
		Click(NextButton);
		waitUntilElementVisible(PriorConditionHeader);
		selectRadioButtonOption(PriorConditions_radio, priorConditions); // This is a radio button selection, not a direct click
		Click(NextButton);
		waitUntilElementVisible(ThirdPartyHeader);
		selectRadioButtonOption(ThirdParty_radio, thirdParty); // This is a radio button selection, not a direct click
		Click(NextButton);
		waitUntilElementVisible(ObjectionHeader);
		selectRadioButtonOption(Objection_radio, objection); // This is a radio button selection, not a direct click
		Click(NextButton);
		return true;

	}
}