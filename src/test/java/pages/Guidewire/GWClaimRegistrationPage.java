package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.Assert;
import utils.BaseDriverClass;

public class GWClaimRegistrationPage extends BaseDriverClass {

    @FindBy(id = "TabBar:DesktopTab")
    private WebElement DesktopTab;
    @FindBy(xpath = "//*[@id=\"TabBar:ClaimTab_arrow\"]/span")
    private WebElement ClaimTab_arrow;
    @FindBy(id = "TabBar:ClaimTab:QuickClaimRegistrarion")
    private WebElement ManualRegistrationlink;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ttlBar")
    private WebElement ttlBar;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:IntakeIdDV:WSIB_IntakeForm")
    private WebElement SourceofInformation;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:IntakeIdDV:WSIB_IntakeChannel")
    private WebElement IntakeChannel;

    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Claimant:ClaimantMenuIcon")
    private WebElement ClaimantMenuIcon;
    @FindBy(xpath = "//*[contains(@id,'ClaimNewWorkerOnlyPickerMenuItemSet_NewWorkerMenuItem')]")
    private WebElement NewWorkerMenuItem;
    @FindBy(id="WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Claimant:MenuItem_Search")
    private WebElement ClaimantMenuItem_Search;
    @FindBy(xpath = "//*[contains(@id,'WSIB_ContactSearchUIDInputSet:WSBId')]")
    private WebElement WCBID;
    
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:PersonNameInputSet:FirstName')]")
    private WebElement FirstName;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:PersonNameInputSet:LastName')]")
    private WebElement LastName;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:PersonContactInfoInputSet:PrimaryPhone')]")
    private WebElement PrimaryPhone;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:Address_PostalCode')]")
    private WebElement Address_PostalCode;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:Standardize')]/span[2]")
    private WebElement AddressLookUp;
    @FindBy(xpath = "//*[contains(@id,'WSIB_AddressStandardizationPopup:WSIB_AddressStandardizationLV:0:_Select_link')]")
    private WebElement AddressSelectButton;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:AdditionalInfoInputSet:Gender')]")
    private WebElement Gender;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:AdditionalInfoInputSet:WSIB_ContactDateOfBirthInputSet:DateOfBirth')]")
    private WebElement DateOfBirth;
    @FindBy(xpath = "//*[@id=\"NewContactPopup:ContactDetailScreen:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Update\"]/span[2]")
    private WebElement UpdateContactButton;
    @FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:AdditionalInfoInputSet:SINDoesNotExist')]")
    private WebElement SINDoesNotExist;
    @FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:AddressValidation\"]/span[2]")
    private WebElement ValidateAddressButton;
    @FindBy(xpath = "//*[contains(@id,'AddressBookMatchWorksheet_CreateNewContactButton')]/span[2]")
    private WebElement CreateNewWorkerCM;

    @FindBy(xpath = "//a[contains(@id,'WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:employerName:employerNameMenuIcon')]")
    private WebElement employerNameMenuIcon;
    @FindBy(xpath = "//*[contains(@id,'WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:employerName:MenuItem_Search')]")
    private WebElement MenuItem_Search;
    @FindBy(xpath = "//*[contains(@id,'AddressBookPolicyCenterPanel:AddressBookPolicyCenterSearchPanelSet:AddressBookSearchDV:accountNumber')]")
    private WebElement accountNumberInput;
    @FindBy(xpath = "//*[contains(@id,'AddressBookPolicyCenterSearchPanelSet:AddressBookSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search_link')]")
    private WebElement EmployerSearch_link;
    @FindBy(xpath = "//*[contains(@id,'AddressBookPolicyCenterSearchPanelSet:WSIB_AddressBookSearchLV:0:_Select_link')]")
    private WebElement EmployerSelect_link;
    @FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ClaimPolicyGeneral_SelectPolicyButton\"]/span[2]")
    private WebElement SelectBussActivity;
    @FindBy(xpath = "//*[contains(@id,'ClaimPolicySelectPolicyScreen:PolicyResultLV:0:_Select_link')]")
    private WebElement BussinessActivitySelect_link;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_BranchLocation:AddressInputSet:AddressPicker')]")
    private WebElement BranchAddressPicker;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_MailingAddress:AddressInputSet:AddressPicker')]")
    private WebElement MailingAddressPicker;

    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimLossDate')]")
    private WebElement DateofAccident;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_DateRptedToEmployer')]")
    private WebElement DateReportedToEmployer;

    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:EditableBodyPartDetailsLV:0:PrimaryBodyPart')]")
    private WebElement AreaofBody;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:EditableBodyPartDetailsLV:0:DetailedBodyPart')]")
    private WebElement AreaofInjury;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_AccidentType')]")
    private WebElement AccidentType;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_TypeOfInjury')]")
    private WebElement DetailedAccidentType;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_NatureOfInjury')]")
    private WebElement NatureOfInjury;
    @FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:EditableBodyPartDetailsLV_tb:Add\"]/span[2]")
    private WebElement AddBodyPart;

    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ThirdPartyYesNo_false')]")
    private WebElement ThirdPartyNo;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ThirdPartyYesNo_true')]")
    private WebElement ThirdPartyYes;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:accidentlocation2:AddressInputSet:AddressPicker')]")
    private WebElement AccidentLocation;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_HCReceivedWorker')]")
    private WebElement HealthCareReceived;
    @FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimType')]")
    private WebElement ClaimType;
    @FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ToolbarButton__dup_1\"]/span[2]")
    private WebElement RegisterClaim;
    @FindBy(xpath = "//*[@id=\"NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:Header\"]")
    private WebElement NewClaimHeader;
    @FindBy(xpath = "//*[@id=\"NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:GoToClaim\"]/span")
    private WebElement GoToClaim;
    @FindBy(xpath = "//*[contains(@id,'Claim:ClaimInfoBar:WSIB_WorkerSelfEnrollmentPIN_link')]")
    private WebElement PIN;
    @FindBy(xpath = "//*[contains(@id,'Claim:ClaimInfoBar:DOB_link')]")
    private WebElement DOB;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimantFirstName")
    private WebElement ClaimantFName;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimantLastName")
    private WebElement ClaimantLName;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimantGender")
    private WebElement GenderClaimant;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimantPronoun")
    private WebElement ClaimantPronouns;
    @FindBy(xpath = "//*[@id=\"WebMessageWorksheet:WebMessageWorksheetScreen:grpMsgs_msgs\"]/div[1]")
    private WebElement WarningMessage;
    @FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:PriorClaimsTabTab")
    private WebElement PriorClaimsTab;

    public GWClaimRegistrationPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean KickManualRegistration() {
        Click(DesktopTab);
        Click(ClaimTab_arrow);
        Click(ManualRegistrationlink);
        return ttlBar.isDisplayed();
    }

    public void selectSourceofInformation() {
        String source = Hooks.currentRow.get().get("Source of Information");
        new Select(SourceofInformation).selectByVisibleText(source);
    }

    public void createNewWorker(String firstname, String lastname, String gender, String dob, String postalcode, String phonenumber) throws InterruptedException {
        Sleep(2000);
        Click(ClaimantMenuIcon);
        Click(NewWorkerMenuItem);
        Enter(FirstName, firstname);
        Enter(LastName, lastname);
        jsSendKeys(PrimaryPhone, phonenumber);
        jsSendKeys(DateOfBirth, dob);
        Enter(Gender, gender);
        jsSendKeys(Address_PostalCode, postalcode);
        Click(AddressLookUp);
        Click(AddressSelectButton);
        Click(SINDoesNotExist);
        Click(UpdateContactButton);
        waitUntilElementVisible(ValidateAddressButton);
        Click(ValidateAddressButton);
        Click(CreateNewWorkerCM);
    }

    public void searchAccountNumber() throws InterruptedException {
        String accountnumber = Hooks.currentRow.get().get("Account#");
        Sleep(2000);
        Click(employerNameMenuIcon);
        Click(MenuItem_Search);
        Enter(accountNumberInput, accountnumber);
        Click(EmployerSearch_link);
        waitUntilElementVisible(EmployerSelect_link);
        Click(EmployerSelect_link);
        Click(SelectBussActivity);
        Click(BussinessActivitySelect_link);
        new Select(BranchAddressPicker).selectByIndex(1);
        Sleep(2000);
        new Select(MailingAddressPicker).selectByIndex(1);
    }

    public void enterAccidentDetails(String DateofAccident, String DateReportedToEmployer) throws InterruptedException {
        waitUntilElementVisible(this.DateofAccident);
        jsSendKeys(this.DateofAccident, DateofAccident);
        jsSendKeys(this.DateReportedToEmployer, DateReportedToEmployer);
    }

    public void enterBodyPartDetails(String AreaofBody, String AccidentType, String AreaofInjury, String DetailedAccidentType, String NatureOfInjury) throws InterruptedException {
        try {
            if (!this.AreaofBody.isDisplayed()) {
                Click(AddBodyPart);
            }
        } catch (Exception e) {
            Click(AddBodyPart);
        }
        Assert.assertTrue("Failed to select Area of Body", Select(this.AreaofBody, AreaofBody));
        Assert.assertTrue("Failed to select Area of Injury", Select(this.AreaofInjury, AreaofInjury));
        Assert.assertTrue("Failed to select Accident Type", Select(this.AccidentType, AccidentType));
        Assert.assertTrue("Failed to select Detailed Accident Type", Select(this.DetailedAccidentType, DetailedAccidentType));
        Assert.assertTrue("Failed to select Nature of Injury", Select(this.NatureOfInjury, NatureOfInjury));
    }

    public void TPASelection() throws InterruptedException {
        String tpa = Hooks.currentRow.get().get("Thirdparty");
        Sleep(2000);
        if (tpa.contentEquals("Yes")) {
            Click(ThirdPartyYes);
        } else {
            Click(ThirdPartyNo);
        }
    }

    public void selectAccidentLocation() throws InterruptedException {
        Sleep(2000);
        new Select(AccidentLocation).selectByIndex(1);
    }

    public void selectHealthCareRecieved(String hcIndicator) throws InterruptedException {
        Sleep(2000);
        new Select(HealthCareReceived).selectByVisibleText(hcIndicator);
    }

    public void selectClaimType(String claimtype) throws InterruptedException {
        Sleep(2000);
        new Select(ClaimType).selectByVisibleText(claimtype);
    }

    public String[] registerClaim() throws InterruptedException {
        Sleep(2000);
        Click(RegisterClaim);
        try {
            if (WarningMessage.isDisplayed() && WarningMessage.getText().contains("This worker has recent prior claims")) {
                Click(PriorClaimsTab);
                Sleep(2000);
                Click(RegisterClaim);
            }
        } catch (Exception e) {}
        waitUntilElementVisible(NewClaimHeader);
        String claimnumber = NewClaimHeader.getText().split(" ")[1];
        Click(GoToClaim);
        return new String[]{claimnumber, DOB.getText().split(" ")[1]};
    }

    public void verifyIntakeWorkerInformation(String fname, String lname, String gender, String pronouns) {
        Assert.assertTrue(IntakeChannel.getText().equalsIgnoreCase("Online services for people with claims"));
        Assert.assertTrue(ClaimantFName.getAttribute("value").equalsIgnoreCase(fname));
        Assert.assertTrue(ClaimantLName.getAttribute("value").equalsIgnoreCase(lname));
        Assert.assertTrue(GenderClaimant.getText().equalsIgnoreCase(gender));
        Assert.assertTrue(ClaimantPronouns.getText().equalsIgnoreCase(pronouns));
        Click(ValidateAddressButton);
        Click(CreateNewWorkerCM);
    }

    public boolean searchExistingWorker(String wcbid) {
        Sleep(2000);
        Click(ClaimantMenuIcon);
        Click(ClaimantMenuItem_Search);
        Enter(WCBID, wcbid);
        Click(EmployerSearch_link);
        waitUntilElementVisible(EmployerSelect_link);
        Click(EmployerSelect_link);
        return true;
    }
}
