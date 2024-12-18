package pages;

import definitions.Hooks;
import io.cucumber.datatable.DataTable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static pages.OSPCClaimOverview.randommessage;

public class GuidewirePage extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(id = "Login:LoginScreen:LoginDV:username") private WebElement usernameTextField;
	@FindBy(id = "Login:LoginScreen:LoginDV:password") private WebElement passwordTextField;
	@FindBy(id = "Login:LoginScreen:LoginDV:submit") private WebElement loginButton;
	@FindBy(id = "TabBar:SearchTab") private WebElement SearchTab;
	@FindBy(id = "DesktopActivities") private WebElement DesktopActivities;
	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimNumber") private WebElement ClaimSearchField;
	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search_link") private WebElement SearchButton;
	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchResultsLV:0:ClaimNumber") private WebElement SelectClaimfromSearch;
	@FindBy(id = "Claim:MenuLinks:Claim_WSIB_MessagingGroup") private WebElement MessagingLink;
	@FindBy(id = "WSIB_ClaimCustomerMessages:WSIB_MessagingScreen:WSIB_MessagePanelSet:0:MessageBody") private WebElement MessagingBody;
	@FindBy(id = "ClaimSummary:ClaimSummaryScreen:ttlBar") private WebElement SummaryTitlebar;
	@FindBy(id = "TabBar:AdminTab") private WebElement AdminTab;
	@FindBy(id = "Admin:MenuLinks:Admin_PortalAuthorizationAdminPage") private WebElement PortalAuthorizationLink;
	@FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalUserSearchFieldsDV:SearchClaimNumberUserInput") private WebElement SearchClaimNumberUserInput;
	@FindBy(xpath = "//*[@id=\"PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalAuthUsersLV_tb:ToolbarButton\"]/span[2]") private WebElement SearchPortalAuth;
	@FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalAuthUsersLV:0:UserNameCell") private WebElement EmailProfileLink;
	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserDV_tb:Edit") private WebElement EditButton;
	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV") private WebElement PortalAuthTable;
	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV_tb:deleteAuthoritiesButton") private WebElement PortalAuthDeleteButton;
	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserDV_tb:Update") private WebElement PortalAuthOKButton;
	@FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:0") private WebElement PortalAuthAdminScreen;
	@FindBy(id = "TabBar:LogoutTabBarLink") private WebElement GWCCLogout;
	@FindBy(id = "Claim:MenuLinks:Claim_ClaimPartiesGroup") private WebElement ClaimParticipantsLink;
	@FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV") private WebElement ClaimParticipantsTable;
	@FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:LinkContactToolbarButtonSet:LinkContactToolbarButtons_SyncFromAddressBookButton") private WebElement CopyfromContactManagerButton;
	@FindBy(id = "Claim:MenuLinks:Claim_ClaimDocumentsGroup") private WebElement ClaimDocumentsLink;
	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:Hidden") private WebElement HiddenDropdown;
	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:RedactionRequired") private WebElement RedactionRequiredDropdown;
	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:NoAccess") private WebElement NoAccessDropdown;
	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search_link") private WebElement Search_link;
	@FindBy(xpath = "//*[@id='ClaimDocuments:DocumentsMemosScreen:DocumentsLV']") private WebElement DocumentsTable;
	@FindBy(xpath = "//*[@id=\"TabBar:ClaimTab_arrow\"]/span") private WebElement ClaimTab_arrow;
	@FindBy(id = "TabBar:ClaimTab:QuickClaimRegistrarion") private WebElement ManualRegistrationlink;
	@FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ttlBar") private WebElement ttlBar;
	@FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:IntakeIdDV:WSIB_IntakeForm") private WebElement SourceofInformation;
	@FindBy(id = "WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Claimant:ClaimantMenuIcon") private WebElement ClaimantMenuIcon;
	@FindBy(xpath = "//*[contains(@id,'ClaimNewWorkerOnlyPickerMenuItemSet_NewWorkerMenuItem')]") private WebElement NewWorkerMenuItem;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:PersonNameInputSet:FirstName')]") private WebElement FirstName;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:PersonNameInputSet:LastName')]") private WebElement LastName;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:PersonContactInfoInputSet:PrimaryPhone')]") private WebElement PrimaryPhone;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:Address_PostalCode')]") private WebElement Address_PostalCode;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:Standardize')]/span[2]") private WebElement AddressLookUp;
	@FindBy(xpath = "//*[contains(@id,'WSIB_AddressStandardizationPopup:WSIB_AddressStandardizationLV:0:_Select_link')]") private WebElement AddressSelectButton;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:AdditionalInfoInputSet:Gender')]") private WebElement Gender;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:AdditionalInfoInputSet:WSIB_ContactDateOfBirthInputSet:DateOfBirth')]") private WebElement DateOfBirth;
	@FindBy(xpath = "//*[@id=\"NewContactPopup:ContactDetailScreen:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:Update\"]/span[2]") private WebElement UpdateContactButton;
	@FindBy(xpath = "//*[contains(@id,'ContactBasicsDV:AdditionalInfoInputSet:SINDoesNotExist')]") private WebElement SINDoesNotExist;
	@FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:AddressValidation\"]/span[2]") private WebElement ValidateAddressButton;
	@FindBy(xpath = "//*[contains(@id,'AddressBookMatchWorksheet_CreateNewContactButton')]/span[2]") private WebElement CreateNewWorkerCM;
	@FindBy(xpath = "//a[contains(@id,'WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:employerName:employerNameMenuIcon')]") private WebElement employerNameMenuIcon;
	@FindBy(xpath = "//*[contains(@id,'WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:employerName:MenuItem_Search')]") private WebElement MenuItem_Search;
	@FindBy(xpath = "//*[contains(@id,'AddressBookPolicyCenterPanel:AddressBookPolicyCenterSearchPanelSet:AddressBookSearchDV:accountNumber')]") private WebElement accountNumberInput;
	@FindBy(xpath = "//*[contains(@id,'AddressBookPolicyCenterSearchPanelSet:AddressBookSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search_link')]") private WebElement EmployerSearch_link;
	@FindBy(xpath = "//*[contains(@id,'AddressBookPolicyCenterSearchPanelSet:WSIB_AddressBookSearchLV:0:_Select_link')]") private WebElement EmployerSelect_link;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_BranchLocation:AddressInputSet:AddressPicker')]") private WebElement BranchAddressPicker;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_MailingAddress:AddressInputSet:AddressPicker')]") private WebElement MailingAddressPicker;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimLossDate')]") private WebElement DateofAccident;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_DateRptedToEmployer')]") private WebElement DateReportedToEmployer;
	@FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ClaimPolicyGeneral_SelectPolicyButton\"]/span[2]") private WebElement SelectBussActivity;
	@FindBy(xpath = "//*[contains(@id,'ClaimPolicySelectPolicyScreen:PolicyResultLV:0:_Select_link')]") private WebElement BussinessActivitySelect_link;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:EditableBodyPartDetailsLV:0:PrimaryBodyPart')]") private WebElement AreaofBody;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:EditableBodyPartDetailsLV:0:DetailedBodyPart')]") private WebElement AreaofInjury;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_AccidentType')]") private WebElement AccidentType;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_TypeOfInjury')]") private WebElement DetailedAccidentType;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_NatureOfInjury')]") private WebElement NatureOfInjury;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ThirdPartyYesNo_false')]") private WebElement ThirdPartyNo;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ThirdPartyYesNo_true')]") private WebElement ThirdPartyYes;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:accidentlocation2:AddressInputSet:AddressPicker')]") private WebElement AccidentLocation;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_HCReceivedWorker')]") private WebElement HealthCareReceived;
	@FindBy(xpath = "//*[contains(@id,'WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:Registration_ClaimType')]") private WebElement ClaimType;
	@FindBy(xpath = "//*[@id=\"WSIB_Form8_FNOLWizard:WorkersCompWizardStepSet:WSIB_ClaimRegistrationScreen:ToolbarButton__dup_1\"]/span[2]") private WebElement RegisterClaim;
	@FindBy(xpath = "//*[@id=\"NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:Header\"]") private WebElement NewClaimHeader;
	@FindBy(xpath = "//*[@id=\"NewClaimSaved:NewClaimSavedScreen:NewClaimSavedDV:GoToClaim\"]/span") private WebElement GoToClaim;
	@FindBy(xpath = "//*[contains(@id,'Claim:ClaimInfoBar:WSIB_WorkerSelfEnrollmentPIN_link')]") private WebElement PIN;
	@FindBy(xpath = "//*[contains(@id,'Claim:ClaimInfoBar:DOB_link')]") private WebElement DOB;

	public GuidewirePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public ArrayList<String> switchtab() {
		// Dynamically create the XPath using the claim number
		String gettitle = getDriver().getCurrentUrl();
		((JavascriptExecutor) getDriver()).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1));
		getDriver().get(prop.getProperty("GWURl"));
		return tabs;
	}

	public void loginToGWCC(String username, String password) {
		getDriver().switchTo().defaultContent();
		getDriver().switchTo().frame("top_frame");
		waitUntilElementVisible(usernameTextField);
		usernameTextField.sendKeys(username);
		passwordTextField.sendKeys(password);
		loginButton.click();

	}

	public boolean searchClaim(String claimnumber) {
			SearchTab.click();
			ClaimSearchField.sendKeys(claimnumber);
			SearchButton.click();
			SelectClaimfromSearch.click();
		return SummaryTitlebar.isDisplayed();
	}

	public boolean verifySentMessage() {
		MessagingLink.click();
		return MessagingBody.getText().contentEquals(randommessage);
	}

	public boolean navigatePortalAuthScreen() {
        AdminTab.click();
		PortalAuthorizationLink.click();
		waitUntilElementVisible(SearchClaimNumberUserInput);
		return SearchClaimNumberUserInput.isDisplayed();
    }

	public boolean removeClaimfromPortalAuth(String claimnumber) {
		SearchClaimNumberUserInput.sendKeys(claimnumber);
		SearchPortalAuth.click();
		EmailProfileLink.click();
		EditButton.click();
		List<WebElement> rows = PortalAuthTable.findElements(By.tagName("tr"));
		for(WebElement row: rows)
		{
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(WebElement cell:cells)
			{
				if(cell.getText().equals(claimnumber)){
					WebElement checkbox=row.findElement(By.xpath(".//input[@type='checkbox']"));
					checkbox.click();
					break;
				}
			}
		}
		PortalAuthDeleteButton.click();
		PortalAuthOKButton.click();
		waitUntilElementVisible(PortalAuthAdminScreen);
		return PortalAuthAdminScreen.isDisplayed();
	}

	public void GWCClogout() {
		GWCCLogout.click();
	}

	public boolean removeDirectDeposit() {
		ClaimParticipantsLink.click();
		List<WebElement> rows = ClaimParticipantsTable.findElements(By.tagName("tr"));
		for(WebElement row: rows)
		{
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(WebElement cell:cells)
			{
				if(cell.getText().equals("Worker")){
					WebElement Role=row.findElement(By.xpath(".//span[@value='Worker']"));
					Role.click();
					break;
				}
			}
		}
		CopyfromContactManagerButton.click();
		return true;

	}

	public boolean verifyLandedDocument(String author, String description) throws InterruptedException {
		ClaimDocumentsLink.click();
		Select hidden = new Select(HiddenDropdown);
		hidden.selectByValue("Yes");
		Select Redaction = new Select(RedactionRequiredDropdown);
		Redaction.selectByValue("Yes");
		Select NoAccess = new Select(NoAccessDropdown);
		NoAccess.selectByValue("Yes");
		Search_link.click();

		boolean recordFound = false;

		for (int i = 0; i < 50; i++) {
			waitUntilElementVisible(DocumentsTable);
			List<WebElement> rows = DocumentsTable.findElements(By.tagName("tr"));
			for (WebElement row : rows) {

				String Author = row.findElement(By.xpath("./td[12]")).getText().toLowerCase();
				String Description = row.findElement(By.xpath("./td[5]")).getText().toLowerCase();
				String FinalDate = row.findElement(By.xpath("./td[18]")).getText().toLowerCase();

				if (Description.contains(description.toLowerCase()) && Author.contains(author.toLowerCase()) && FinalDate.contentEquals(getCurrentDate("dd/MMM/yyyy").toLowerCase())) {
					recordFound = true;
					break;
				}
			}
			if (recordFound) {
				break;
			} else {
				Thread.sleep(5000);
				System.out.println("Retrying for "+ i+ " times");
				Search_link.click();
			}
		}

		return recordFound;

	}

	public boolean verifyonGWCC() {
		getDriver().switchTo().frame("top_frame");
		return usernameTextField.isDisplayed();
	}

	public boolean GWCCLoginsuccess() {
		waitUntilElementVisible(DesktopActivities);
		return DesktopActivities.isDisplayed();
	}

	public boolean KickManualRegistration() {
		ClaimTab_arrow.click();
		ManualRegistrationlink.click();
		return ttlBar.isDisplayed();
	}

	public void selectSourceofInformation(String source) {
		Select sourcee = new Select(SourceofInformation);
		sourcee.selectByVisibleText(source);
	}

	public void createNewWorker(DataTable dataTable) throws InterruptedException {
		Thread.sleep(2000);
		ClaimantMenuIcon.click();
		NewWorkerMenuItem.click();

		// Convert the DataTable to a Map to access fields and values
		Map<String, String> workerDetails = dataTable.asMap(String.class, String.class);

		// Fill in the worker information dynamically based on the map
		for (Map.Entry<String, String> entry : workerDetails.entrySet()) {
			String field = entry.getKey();
			String value = entry.getValue();

			switch (field) {
				case "First Name":
					FirstName.sendKeys(value);
					break;
				case "Last Name":
					LastName.sendKeys(value);
					break;
				case "Primary Phone":
					jsSendKeys(PrimaryPhone, value);
					break;
				case "Postal Code":
					jsSendKeys(Address_PostalCode, value);
					break;
				case "Gender":
					new Select(Gender).selectByVisibleText(value);
					break;
				case "Date of Birth":
					jsSendKeys(DateOfBirth, value);
					break;
				default:
					throw new IllegalArgumentException("Unknown field: " + field);
			}
		}

		AddressLookUp.click();
		AddressSelectButton.click();
		SINDoesNotExist.click();
		UpdateContactButton.click();
		waitUntilElementVisible(ValidateAddressButton);
		ValidateAddressButton.click();
		CreateNewWorkerCM.click();

	}

	public void searchAccountNumber(String accountnumber) throws InterruptedException {
		Thread.sleep(2000);
		employerNameMenuIcon.click();
		MenuItem_Search.click();
		accountNumberInput.sendKeys(accountnumber);
		EmployerSearch_link.click();
		waitUntilElementVisible(EmployerSelect_link);
		EmployerSelect_link.click();
		SelectBussActivity.click();
		BussinessActivitySelect_link.click();
		new Select(BranchAddressPicker).selectByIndex(1);
		Thread.sleep(2000);
		new Select(MailingAddressPicker).selectByIndex(1);
	}

	public void enterAccidentDetails(DataTable dataTable) throws InterruptedException {

		// Convert the DataTable to a Map to access fields and values
		Map<String, String> workerDetails = dataTable.asMap(String.class, String.class);

		// Fill in the worker information dynamically based on the map
		for (Map.Entry<String, String> entry : workerDetails.entrySet()) {
			String field = entry.getKey();
			String value = entry.getValue();

			switch (field) {
				case "Date of Accident":
					Thread.sleep(2000);
					DateofAccident.sendKeys(value);
					break;
				case "Date Reported":
					DateReportedToEmployer.sendKeys(value);
					break;
				default:
					throw new IllegalArgumentException("Unknown field: " + field);
			}
		}

	}

	public void enterBodyPartDetails(DataTable dataTable) throws InterruptedException {

		// Convert the DataTable to a Map to access fields and values
		Map<String, String> workerDetails = dataTable.asMap(String.class, String.class);

		// Fill in the worker information dynamically based on the map
		for (Map.Entry<String, String> entry : workerDetails.entrySet()) {
			String field = entry.getKey();
			String value = entry.getValue();

			switch (field) {
				case "Area of Body":
					new Select(AreaofBody).selectByVisibleText(value);
					break;
				case "Area of Injury":
					new Select(AreaofInjury).selectByVisibleText(value);
					break;
				case "Accident Type":
					new Select(AccidentType).selectByVisibleText(value);
					break;
				case "Detailed Accident Type":
					Thread.sleep(2000);
					new Select(DetailedAccidentType).selectByVisibleText(value);
					break;
				case "Nature of Injury":
					Thread.sleep(2000);
					new Select(NatureOfInjury).selectByVisibleText(value);
					break;
				default:
					throw new IllegalArgumentException("Unknown field: " + field);
			}
		}
	}

	public void TPASelection(String tpa) throws InterruptedException {
		Thread.sleep(2000);
		if (tpa.contentEquals("Yes")) {
			ThirdPartyYes.click();
		} else {
			ThirdPartyNo.click();
		}
	}

	public void selectAccidentLocation() throws InterruptedException {
		Thread.sleep(2000);
		new Select(AccidentLocation).selectByIndex(1);
	}

	public void selectHealthCareRecieved(String hcIndicator) throws InterruptedException {
		Thread.sleep(2000);
		new Select(HealthCareReceived).selectByVisibleText(hcIndicator);

	}

	public void selectClaimType(String claimtype) throws InterruptedException {
		Thread.sleep(2000);
		new Select(ClaimType).selectByVisibleText(claimtype);
	}

	public String[] registerClaim() throws InterruptedException {
		Thread.sleep(2000);
		RegisterClaim.click();
		waitUntilElementVisible(NewClaimHeader);
		String claimnumber = NewClaimHeader.getText().split(" ")[1];
		GoToClaim.click();
		String claimdetails[] = {claimnumber, DOB.getText().split(" ")[1], PIN.getText().split(" ")[1]};
		return	claimdetails;

	}
}
