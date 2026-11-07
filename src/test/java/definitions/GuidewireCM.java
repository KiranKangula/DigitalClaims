package definitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.Guidewire.GuidewireCMPage;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import utils.ConfigReader;
import utils.ExternalValidation;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static definitions.Hooks.getDriver;

public class GuidewireCM {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private GuidewireCMPage guidewirecm;

    ConfigReader prop= Hooks.getConfigReader();
    ConfigReader common = Hooks.getCommonReader();
    public GuidewireCM() throws Exception {
        driver = getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
        loginPage = new LoginPage(driver, wait);
        homePage = new HomePage(driver, wait);
        guidewirecm = new GuidewireCMPage(driver, wait);

    }


    @And("Switch to New tab and Login to Contact Manager")
    public void switchToNewTabAndLoginToContactManager() {
        if (!ExternalValidation.shouldRun(
                "Switch to New tab and Login to Contact Manager")) {
            return;
        }
        guidewirecm.switchtabCM();
        if(System.getProperty("env").equalsIgnoreCase("BAT") || System.getProperty("env").equalsIgnoreCase("PPD")) {
            guidewirecm.loginToGWCM(prop.getProperty("GWCM_Username"), prop.getProperty("GWCM_Password"));
        }
        else {

        }
    }

    @And("Search for the Contact using {string} and {string}")
    public void searchForTheContactUsingAnd(String Firstname, String Lastname) {
        if (!ExternalValidation.shouldRun(
                "Search for the Contact ")) {
            return;
        }
        Firstname = Hooks.currentRow.get().get("Firstname");
         Lastname = Hooks.currentRow.get().get("Lastname");
         String WCBID = Hooks.currentRow.get().get("WCB Id");
        guidewirecm.searchContactInGWCM(Firstname, Lastname, WCBID);


    }

    @And("Verify the Mailing address is updated along with history events in Contact Manager")
    public void verifyTheMailingAddressIsUpdatedAlongWithHistoryEventsInContactManager() {
        if (!ExternalValidation.shouldRun(
                "Verify the Mailing address is updated along with history events in Contact Manager")) {
            return;
        }
        String address = Hooks.currentRow.get().get("Address Line1") ;
        String City=    Hooks.currentRow.get().get("City") ;
        String PostalCode= Hooks.currentRow.get().get("Postal Code");
        Assert.assertTrue(guidewirecm.verifyMailingAddressUpdateInGWCM(address, City, PostalCode));
    }

    @And("Verify the Phone Number is updated along with history events in Contact Manager")
    public void verifyThePhoneNumberIsUpdatedAlongWithHistoryEventsInContactManager() {
        if (!ExternalValidation.shouldRun(
                "Verify the Phone Number is updated along with history events in Contact Manager")) {
            return;
        }
        String phonenumber = Hooks.currentRow.get().get("Phone Number") ;
        Assert.assertTrue(guidewirecm.verifyPhoneNumberUpdateInGWCM(phonenumber));
    }

    @And("Verify the Direct Deposit is displayed in ContactManager")
    public void verifyTheDirectDepositIsDisplayedInContactManager() {
        // Write code here that turns the phrase above into concrete actions
        if (!ExternalValidation.shouldRun(
                "Verify the Direct Deposit is displayed in ContactManager")) {
            return;
        }
        String TransitNumber = Hooks.currentRow.get().get("TransitNumber") ;
        String InstitutionNumber = Hooks.currentRow.get().get("InstitutionNumber") ;
        String AccountNumber = Hooks.currentRow.get().get("AccountNumber") ;
        Assert.assertTrue(guidewirecm.verifyDDUpdateInGWCM(TransitNumber, InstitutionNumber, AccountNumber));


    }

    @And("Remove the Direct Deposit from the Contact")
    public void removeTheDirectDepositFromTheContact() {
        // Write code here that turns the phrase above into concrete actions
        if (!ExternalValidation.shouldRun(
                "Remove the Direct Deposit from the Contact")) {
            return;
        }
        String TransitNumber = Hooks.currentRow.get().get("TransitNumber") ;
        String InstitutionNumber = Hooks.currentRow.get().get("InstitutionNumber") ;
        String AccountNumber = Hooks.currentRow.get().get("AccountNumber") ;
        Assert.assertTrue(guidewirecm.VerifyDDRemoveInGWCM(TransitNumber, InstitutionNumber, AccountNumber));

    }
}
