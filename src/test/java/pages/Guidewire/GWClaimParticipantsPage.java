package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.JsonUtils;

import java.util.List;
import java.util.Map;

import static pages.OSPC.OSPCClaimOverview.randommessage;

public class GWClaimParticipantsPage extends BaseDriverClass {

    @FindBy(id = "Claim:MenuLinks:Claim_ClaimPartiesGroup")
    private WebElement ClaimParticipantsLink;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:PeopleInvolvedDetailedLV")
    private WebElement ClaimParticipantsTable;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:WSIB_AddressSummaryInputSet:addressLine1")
    private WebElement addressLine1;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:WSIB_AddressSummaryInputSet:addressLine2")
    private WebElement addressLine2;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:WSIB_MailingAddressInputSet:FNOLAddressInputSet:WSIB_AddressSummaryInputSet:addressLine3")
    private WebElement addressLine3;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV_tb:ContactDetailToolbarButtonSet:LinkContactToolbarButtonSet:LinkContactToolbarButtons_SyncFromAddressBookButton")
    private WebElement CopyfromContactManagerButton;

    @FindBy(xpath = "//*[@id=\"ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:ContactEFTLV:0:0\"]/td[2]") private WebElement TransitNumber;
    @FindBy(xpath = "//*[@id=\"ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:ContactEFTLV:0:BankNumber\"]") private WebElement BankNumber;
    @FindBy(xpath = "//*[@id=\"ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:ContactEFTLV:0:Reason\"]") private WebElement DDReason;




    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PersonNameInputSet:FirstName")
    private WebElement WorkerFirstName;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PersonNameInputSet:LastName")
    private WebElement WorkerLastName;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PersonNameInputSet:WCBID")
    private WebElement WorkerWCBID;
    @FindBy(id = "ClaimContacts:ClaimContactsScreen:PeopleInvolvedDetailedListDetail:ContactBasicsDV:PersonContactInfoInputSet:PrimaryPhone")
    private WebElement PrimaryPhoneNumber;
    @FindBy(id = "Claim:MenuLinks:Claim_WSIB_MessagingGroup")
    private WebElement MessagingLink;
    @FindBy(id = "WSIB_ClaimCustomerMessages:WSIB_MessagingScreen:WSIB_MessagePanelSet:0:MessageBody")
    private WebElement MessagingBody;

    public GWClaimParticipantsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean removeDirectDeposit() {
        Click(ClaimParticipantsLink);
        List<WebElement> rows = ClaimParticipantsTable.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            if (row.getText().contains("Worker")) {
                WebElement role = row.findElement(By.xpath(".//span[@value='Worker']"));
                Click(role);
                break;
            }
        }
        Click(CopyfromContactManagerButton);
        return true;
    }

    public boolean addDirectDeposit() {
        Click(ClaimParticipantsLink);
        List<WebElement> rows = ClaimParticipantsTable.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            if (row.getText().contains("Worker")) {
                WebElement role = row.findElement(By.xpath(".//span[@value='Worker']"));
                Click(role);
                break;
            }
        }

        if(TransitNumber.getText().contentEquals("01001") && BankNumber.getText().contentEquals("0010") && DDReason.getText().contentEquals("Update from OSPC"))
        {
            return true;
        }
        else {
            return false;
        }

    }

    public boolean verifyUpdatedMailingAddress(String addressLine1, String addressLine2, String addressLine3) {
        Click(ClaimParticipantsLink);
        List<WebElement> rows = ClaimParticipantsTable.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            if (row.getText().contains("Worker")) {
                WebElement role = row.findElement(By.xpath(".//span[@value='Worker']"));
                Click(role);
                break;
            }
        }

        if (this.addressLine1.isDisplayed() && this.addressLine1.getText().equalsIgnoreCase(addressLine1) &&
            this.addressLine2.isDisplayed() && this.addressLine2.getText().equalsIgnoreCase(addressLine2) &&
            this.addressLine3.isDisplayed() && this.addressLine3.getText().equalsIgnoreCase(addressLine3)) {

            Map<String, String> currentRowData = Hooks.currentRow.get();
            currentRowData.put("Firstname", WorkerFirstName.getText());
            currentRowData.put("Lastname", WorkerLastName.getText());
            currentRowData.put("WCB Id", WorkerWCBID.getText().split(" ")[0]);
            JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());
            Hooks.currentRow.set(currentRowData);
            return true;
        }
        return false;
    }

    public boolean verifyUpdatedPhoneNumber(String phonenumber) {
        Click(ClaimParticipantsLink);
        List<WebElement> rows = ClaimParticipantsTable.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            if (row.getText().contains("Worker")) {
                WebElement role = row.findElement(By.xpath(".//span[@value='Worker']"));
                Click(role);
                break;
            }
        }

        if (this.PrimaryPhoneNumber.isDisplayed() && this.PrimaryPhoneNumber.getText().equalsIgnoreCase(phonenumber)) {
            Map<String, String> currentRowData = Hooks.currentRow.get();
            currentRowData.put("Firstname", WorkerFirstName.getText());
            currentRowData.put("Lastname", WorkerLastName.getText());
            currentRowData.put("WCB Id", WorkerWCBID.getText().split(" ")[0]);
            JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());
            Hooks.currentRow.set(currentRowData);
            return true;
        }
        return false;
    }

    public boolean verifySentMessage() {
        Click(MessagingLink);
        return MessagingBody.getText().contentEquals(randommessage);
    }
}
