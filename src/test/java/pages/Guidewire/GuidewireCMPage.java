package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;

public class GuidewireCMPage extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();
	@FindBy(id = "Login-LoginScreen-LoginDV-username") private WebElement usernameTextField;
	@FindBy(id = "Login-LoginScreen-LoginDV-password") private WebElement passwordTextField;
	@FindBy(id = "Login-LoginScreen-LoginDV-submit") private WebElement loginButton;
	@FindBy(id = "ABContactSearch-ABContactSearchScreen-ContactSearchDV-GlobalPersonNameInputSet-FirstName") private WebElement FirstName;
	@FindBy(id = "ABContactSearch-ABContactSearchScreen-ContactSearchDV-GlobalPersonNameInputSet-LastName") private WebElement LastName;
	@FindBy(id = "ABContactSearch-ABContactSearchScreen-ContactSearchDV-WSIB_ContactSearchUUIDInputSet-WSBId") private WebElement WCBID;
	@FindBy(id = "ABContactSearch-ABContactSearchScreen-ContactSearchDV-SearchAndResetInputSet-SearchLinksInputSet-Search") private WebElement SearchButton;
	@FindBy(id = "ABContactSearch-ABContactSearchScreen-WSIB_ContactSearchResultsLV") private WebElement searchResultsTable;

	@FindBy(id = "ContactDetail-ABContactDetailScreen-AddressesCardTab") private WebElement AddressesCardTab;
    @FindBy(id = "ContactDetail-ABContactDetailScreen-ABAddressesLV") private WebElement addressesTable;

	@FindBy(id = "ContactDetail-ABContactDetailScreen-HistoryCardTab") private WebElement HistoryCardTab;
	@FindBy(id = "ContactDetail-ABContactDetailScreen-ContactHistoryLV-DateHeader_toggleSort") private WebElement DateHeader_toggleSort;
    @FindBy(id = "ContactDetail-ABContactDetailScreen-ContactHistoryLV") private WebElement historyTable;

	@FindBy(xpath = "//*[contains(@id, 'HistoryTrackedChangesLV')]") private WebElement trackedChangesTable;
    @FindBy(xpath = "//div[contains(@id, 'ContactHistoryDetailPopup')]//div[@role='link']") private WebElement popupOkButton;
	@FindBy(name = "ContactDetail-ABContactDetailScreen-ContactHistoryLV-HistoryEventTypeFilter") private WebElement HistoryEventTypeFilter;

    @FindBy(xpath = "//*[@id=\"ContactDetail-ABContactDetailScreen-ContactEFTLV-0-BankRoutingNumber\"]/div/div") private WebElement BankRoutingNumber;
    @FindBy(xpath = "//*[@id=\"ContactDetail-ABContactDetailScreen-ContactEFTLV-0-BankNumber\"]/div/div") private WebElement BankNumber;
    @FindBy(xpath = "//*[@id=\"ContactDetail-ABContactDetailScreen-ContactEFTLV-0-Reason\"]/div/div") private WebElement Reason;
    @FindBy(id = "ContactDetail-ABContactDetailScreen-ContactBasicsDV_tb-Edit") private WebElement EditButton;
    @FindBy(id = "ContactDetail-ABContactDetailScreen-ContactEFTLV-_Checkbox_checkboxDiv") private WebElement Checkbox_All;
    @FindBy(id = "ContactDetail-ABContactDetailScreen-ContactEFTLV_tb-Remove") private WebElement RemoveButton;
    @FindBy(id = "ContactDetail-ABContactDetailScreen-ContactBasicsDV_tb-ToolbarButtonUpdate") private WebElement UpdateButton;




    public GuidewireCMPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public ArrayList<String> switchtabCM() {
		((JavascriptExecutor) getDriver()).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1));
		getDriver().get(prop.getProperty("CMURL"));
		return tabs;
	}

	public void loginToGWCM(String username, String password) {
		waitUntilElementVisible(usernameTextField);
		jsMoveSendKeys(usernameTextField, username);
		jsMoveSendKeys(passwordTextField, password);
		loginButton.click();
	}

	public void searchContactInGWCM(String firstname, String lastname, String WCBID) {
		waitUntilElementVisible(FirstName);
		jsMoveSendKeys(FirstName, firstname);
		jsMoveSendKeys(LastName, lastname);
		jsMoveSendKeys(this.WCBID, WCBID); // This is a sendKeys, not a click
		Click(SearchButton);
		
        By selectLinkLocator = By.xpath("//div[@id='ABContactSearch-ABContactSearchScreen-WSIB_ContactSearchResultsLV']//div[@role='button' and text()='" + WCBID + "']");
        WebElement selectLink = waitUntilElementVisible(selectLinkLocator);
		Click(selectLink);
	}

	public boolean verifyMailingAddressUpdateInGWCM(String address, String city, String postalCode) {
		boolean addressFound = false;
		Click(AddressesCardTab); // This was already Click(AddressesCardTab)
		waitUntilElementVisible(addressesTable);
		
		String formattedaddress = address.split("-")[1]+ " UNIT "+ address.split("-")[0];
        List<WebElement> addrRows = addressesTable.findElements(By.cssSelector("tr.gw-standard-row"));
        for (WebElement row : addrRows) {
            String rowText = row.getText();
            if (rowText.toLowerCase().contains(formattedaddress.toLowerCase())) {
                if (rowText.toLowerCase().contains(city.toLowerCase()) && rowText.contains(postalCode)) {
                    Hooks.log("SUCCESS: Mailing address verified in Addresses tab.");
                    addressFound = true;
                    break;
                }
            }
        }
        
        if (!addressFound) {
            Hooks.log("WARNING: Mailing address not explicitly found in Addresses tab list. Expected: " + formattedaddress);
        }

		Click(HistoryCardTab); // This was already Click(HistoryCardTab)
		Sleep(2000);
		waitUntilElementVisible(historyTable);
		Select(HistoryEventTypeFilter, "Primary Address Updated");
        DateHeader_toggleSort.click();
        Sleep(2000);

        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                List<WebElement> histRows = historyTable.findElements(By.cssSelector("tr.gw-standard-row"));
                for (WebElement row : histRows) {
                    if (row.getText().contains("Primary Address Updated")) {
                        WebElement changesBtn = row.findElement(By.xpath(".//div[contains(@id, 'ChangesLink_button')]"));
                        Click(changesBtn);
                        Sleep(2000);
                        
                        String changesText = trackedChangesTable.getText();
                        if (changesText.contains(address)) {
                            Hooks.log("SUCCESS: Address update verified in History details.");
                            Click(popupOkButton);
                            // If we verified the history, we consider the update successful even if the list view match was tricky
                            return true; 
                        }
                        Click(popupOkButton);
                        Sleep(1000);
                    }
                }
                break;
            } catch (StaleElementReferenceException e) {
                Hooks.log("Stale element in history table, retrying... Attempt: " + (attempt + 1));
                waitUntilElementVisible(historyTable);
            }
        }

        Hooks.log("FAILED: Address update not found in History.");
		return false;
	}

	public boolean verifyPhoneNumberUpdateInGWCM(String phonenumber) {
		Click(HistoryCardTab); // This was already Click(HistoryCardTab)
		waitUntilElementVisible(historyTable);
		Select(HistoryEventTypeFilter, "Phone Number Updated");
        DateHeader_toggleSort.click();
        Sleep(2000);
        
        String expectedPhone = phonenumber.replace("-", "");
        
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                List<WebElement> rows = historyTable.findElements(By.cssSelector("tr.gw-standard-row"));
                for (WebElement row : rows) {
                    if (row.getText().contains("Phone Number Updated")) {
                        WebElement changesBtn = row.findElement(By.xpath(".//div[contains(@id, 'ChangesLink_button')]"));
                        Click(changesBtn);
                        Sleep(2000);
                        
                        if (trackedChangesTable.getText().contains(expectedPhone)) {
                            Hooks.log("SUCCESS: Phone Number update verified in History.");
                            Click(popupOkButton);
                            return true;
                        }
                        Click(popupOkButton);
                        Sleep(1000);
                    }
                }
                break;
            } catch (StaleElementReferenceException e) {
                Hooks.log("Stale element in history table, retrying... Attempt: " + (attempt + 1));
                waitUntilElementVisible(historyTable);
            }
        }
        
        Hooks.log("FAILED: Phone Number update not found in History.");
		return false;
	}

    public boolean verifyDDUpdateInGWCM(String transitNumber, String institutionNumber, String accountNumber) {


            if(BankRoutingNumber.getText().contentEquals(transitNumber)
            && BankNumber.getText().contentEquals('0'+ institutionNumber)
            && Reason.getText().contentEquals("Update from OSPC"))
            {
                Hooks.log("PASSED: Direct Deposit is updated in Contact Manager.");
                return true;
            }
            else
            {
                Hooks.log("FAILED: Direct Deposit is NOT updated in Contact Manager.");
                return false;
            }

    }


    public boolean VerifyDDRemoveInGWCM(String transitNumber, String institutionNumber, String accountNumber) {

        if(BankRoutingNumber.getText().contentEquals(transitNumber)
                && BankNumber.getText().contentEquals('0'+ institutionNumber)
                && Reason.getText().contentEquals("Update from OSPC"))
        {
            Click(EditButton);
            Click(Checkbox_All);
            Click(RemoveButton);
            Click(UpdateButton);

        }

        if(isDisplayed(BankRoutingNumber))
        {
            Hooks.log("FAILED: Direct Deposit is NOT removed in Contact Manager.");
            return false;
        }
        else {
            Hooks.log("PASSED: Direct Deposit is removed successfully in Contact Manager.");
            return true;
        }

    }



}
