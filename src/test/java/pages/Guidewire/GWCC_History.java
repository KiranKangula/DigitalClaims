package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.List;

public class GWCC_History extends BaseDriverClass {
	ConfigReader prop = Hooks.getConfigReader();
	
	@FindBy(id = "Claim:MenuLinks:Claim_ClaimHistoryGroup") private WebElement ClaimHistoryGroup;
	@FindBy(id = "ClaimHistory:ClaimHistoryScreen:HistoryLV:HistoryFilter") private WebElement HistoryFilter;
    @FindBy(id = "ClaimHistory:ClaimHistoryScreen:HistoryLV") private WebElement historyTable;

	public GWCC_History(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public boolean verifyHistoryEventForMailingAddressUpdate(String address, String onlineuser) {
		try {
			waitUntilElementVisible(ClaimHistoryGroup);
			Click(ClaimHistoryGroup);
			waitUntilElementVisible(historyTable);
			Select(HistoryFilter, "Custom");
            
            // Wait for filter to apply
            Sleep(2000);

            List<WebElement> rows = historyTable.findElements(By.tagName("tr"));
            for (WebElement row : rows) {
                try {
                    String rowText = row.getText();
                    if (rowText.contains(address) && rowText.toLowerCase().contains(onlineuser.toLowerCase()) && rowText.contains("Mailing Address Updated")) {
                        Hooks.log("SUCCESS: Mailing Address update event found in History: " + rowText);
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {}
            }
            
            Hooks.log("FAILED: Mailing Address update event NOT found in History for: " + address);
            return false;
		}  catch (Exception e) {
			Hooks.log("ERROR searching history: " + e.getMessage());
			return false;
		}
	}

	public boolean verifyHistoryEventForPhoneNumberUpdate(String phonenumber, String onlineuser) {
		try {
			waitUntilElementVisible(ClaimHistoryGroup);
			Click(ClaimHistoryGroup);
			waitUntilElementVisible(historyTable);

            List<WebElement> rows = historyTable.findElements(By.tagName("tr"));
            for (WebElement row : rows) {
                try {
                    String rowText = row.getText();
                    if (rowText.contains(phonenumber) && rowText.toLowerCase().contains(onlineuser.toLowerCase())) {
                        Hooks.log("SUCCESS: Phone Number update event found in History: " + rowText);
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {}
            }

            Hooks.log("FAILED: Phone Number update event NOT found in History for: " + phonenumber);
            return false;
		}  catch (Exception e) {
			Hooks.log("ERROR searching history: " + e.getMessage());
			return false;
		}
	}
}
