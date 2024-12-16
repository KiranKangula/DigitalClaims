package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;

import static pages.OSPCClaimOverview.randommessage;

public class GuidewirePage extends BaseDriverClass {
	ConfigReader prop=new ConfigReader();

	@FindBy(id = "Login:LoginScreen:LoginDV:username")
	@CacheLookup
	private WebElement usernameTextField;

	@FindBy(id = "Login:LoginScreen:LoginDV:password")
	@CacheLookup
	private WebElement passwordTextField;

	@FindBy(id = "Login:LoginScreen:LoginDV:submit")
	@CacheLookup
	private WebElement loginButton;

	@FindBy(id = "TabBar:SearchTab")
	@CacheLookup
	private WebElement SearchTab;

	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimNumber")
	@CacheLookup
	private WebElement ClaimSearchField;

	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search_link")
	@CacheLookup
	private WebElement SearchButton;

	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchResultsLV:0:ClaimNumber")
	@CacheLookup
	private WebElement SelectClaimfromSearch;

	@FindBy(id = "Claim:MenuLinks:Claim_WSIB_MessagingGroup")
	@CacheLookup
	private WebElement MessagingLink;

	@FindBy(id = "WSIB_ClaimCustomerMessages:WSIB_MessagingScreen:WSIB_MessagePanelSet:0:MessageBody")
	@CacheLookup
	private WebElement MessagingBody;

	@FindBy(id = "ClaimSummary:ClaimSummaryScreen:ttlBar")
	@CacheLookup
	private WebElement SummaryTitlebar;

	@FindBy(id = "TabBar:AdminTab")
	@CacheLookup
	private WebElement AdminTab;

	@FindBy(id = "Admin:MenuLinks:Admin_PortalAuthorizationAdminPage")
	@CacheLookup
	private WebElement PortalAuthorizationLink;

	@FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalUserSearchFieldsDV:SearchClaimNumberUserInput")
	@CacheLookup
	private WebElement SearchClaimNumberUserInput;

	@FindBy(xpath = "//*[@id=\"PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalAuthUsersLV_tb:ToolbarButton\"]/span[2]")
	@CacheLookup
	private WebElement SearchPortalAuth;

	@FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalAuthUsersLV:0:UserNameCell")
	@CacheLookup
	private WebElement EmailProfileLink;

	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserDV_tb:Edit")
	@CacheLookup
	private WebElement EditButton;

	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV")
	@CacheLookup
	private WebElement PortalAuthTable;

	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV_tb:deleteAuthoritiesButton")
	@CacheLookup
	private WebElement PortalAuthDeleteButton;

	@FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserDV_tb:Update")
	@CacheLookup
	private WebElement PortalAuthOKButton;

	@FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:0")
	@CacheLookup
	private WebElement PortalAuthAdminScreen;

	@FindBy(id = "TabBar:LogoutTabBarLink")
	private WebElement GWCCLogout;

	@FindBy(id = "Claim:MenuLinks:Claim_ClaimPartiesGroup")
	private WebElement ClaimParticipantsLink;

	@FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV")
	private WebElement ClaimParticipantsTable;

	@FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:LinkContactToolbarButtonSet:LinkContactToolbarButtons_SyncFromAddressBookButton")
	private WebElement CopyfromContactManagerButton;

	@FindBy(id = "Claim:MenuLinks:Claim_ClaimDocumentsGroup")
	private WebElement ClaimDocumentsLink;

	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:Hidden")
	private WebElement HiddenDropdown;

	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:RedactionRequired")
	private WebElement RedactionRequiredDropdown;

	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:NoAccess")
	private WebElement NoAccessDropdown;

	@FindBy(id = "ClaimDocuments:DocumentsMemosScreen:ClaimDocumentSearchDV:SearchAndResetInputSet:SearchLinksInputSet:Search_link")
	private WebElement Search_link;

	@FindBy(xpath = "//*[@id='ClaimDocuments:DocumentsMemosScreen:DocumentsLV']/tbody")
	private WebElement DocumentsTable;

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

		for (int i = 0; i < 10; i++) {
			List<WebElement> rows = DocumentsTable.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				String Author = row.findElement(By.xpath(".//span[@label='Author']")).getText().toLowerCase();
				String Description = row.findElement(By.xpath(".//span[@label='Description']")).getText().toLowerCase();
				String FinalDate = row.findElement(By.xpath(".//span[@label='Date Finalized']")).getText().toLowerCase();

				if (Description.contains(description.toLowerCase()) && Author.contains(author.toLowerCase()) && FinalDate.contentEquals(getCurrentDate("dd/MMM/yyyy"))) {
					recordFound = true;
					break;
				}
			}
			if (recordFound) {
				break;
			} else {
				Thread.sleep(5000);
				Search_link.click();
			}
		}

		return recordFound;

	}
}
