package definitions;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.Guidewire.*;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import utils.ConfigReader;
import utils.ExternalValidation;
import utils.JsonUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static definitions.Hooks.getDriver;
import static pages.UNCD.AddClaimantInfo.firstname;
import static pages.UNCD.AddClaimantInfo.lastname;
import static utils.BaseDriverClass.*;

public class GuidewireCC {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private GWLoginPage gwLogin;
    private GWSearchPage gwSearch;
    private GWClaimRegistrationPage gwRegistration;
    private GWClaimParticipantsPage gwParticipants;
    private GWDocumentsPage gwDocuments;
    private GWAdminPage gwAdmin;
    private GWRecoveryPage gwRecovery;
    private GWAppealsPage gwAppeals;
    private GWCC_History gwcc_history;
    
    ConfigReader prop = Hooks.getConfigReader();
    ConfigReader common = Hooks.getCommonReader();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public GuidewireCC() throws Exception {
        driver = getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
        loginPage = new LoginPage(driver, wait);
        homePage = new HomePage(driver, wait);
        gwLogin = new GWLoginPage(driver, wait);
        gwSearch = new GWSearchPage(driver, wait);
        gwRegistration = new GWClaimRegistrationPage(driver, wait);
        gwParticipants = new GWClaimParticipantsPage(driver, wait);
        gwDocuments = new GWDocumentsPage(driver, wait);
        gwAdmin = new GWAdminPage(driver, wait);
        gwRecovery = new GWRecoveryPage(driver, wait);
        gwAppeals = new GWAppealsPage(driver, wait);
        gwcc_history = new GWCC_History(driver, wait);
    }

    @Then("Switch to New tab and Login to ClaimCenter")
    public void switchToNewTabAndLoginToClaimCenter() {
        if (!ExternalValidation.shouldRun(
                "Switch to New tab and Login to ClaimCenter")) {
            return;
        }
        Hooks.log("Switching to New tab and Logging into ClaimCenter");
        gwLogin.switchtab();
        if(!System.getProperty("env").equalsIgnoreCase("PPD")) {
            String username = prop.getProperty("GWCC_Username");
            String password = prop.getProperty("GWCC_Password");
            Hooks.log("Logging into GWCC with Username: " + username + " and Password: " + password);
            gwLogin.loginToGWCC(username, password);
        } else {
            getDriver().switchTo().defaultContent();
            getDriver().switchTo().frame("top_frame");
        }
    }

    @And("Search for {string}")
    public void searchForClaimnumber(String claimnumber) {
        if (!ExternalValidation.shouldRun(
                "Search for ClaimNumber")) {
            return;
        }
        claimnumber = Hooks.currentRow.get().get("Claim Number");
        Hooks.log("Searching for Claim Number: " + claimnumber);
        Assert.assertTrue(gwSearch.searchClaim(claimnumber));
    }

    @And("Search for claim number")
    public void searchForClaimnumber2() {
        if (!ExternalValidation.shouldRun(
                "Search for Claim Number")) {
            return;
        }
        String claimnumber = Hooks.currentRow.get().get("Claim Number");
        Hooks.log("Searching for Claim Number: " + claimnumber);
        Assert.assertTrue(gwSearch.searchClaim(claimnumber));
    }

    @And("Search for UNCD {string}")
    public void searchForUNCDClaimnumber(String claimnumber) {
        if (!ExternalValidation.shouldRun(
                "Search for UNCD")) {
            return;
        }
        claimnumber = Hooks.currentRow.get().get("claimnumber");
        Hooks.log("Searching for UNCD Claim Number: " + claimnumber);
        Assert.assertTrue(gwSearch.searchClaim(claimnumber));
    }

    @Then("User should be able to see the message displayed from OSPC")
    public void userShouldBeAbleToSeeTheMessageDisplayedFromOSPC() {
        if (!ExternalValidation.shouldRun(
                "User should be able to see the message displayed from OSPC")) {
            return;
        }
        Hooks.log("Verifying message displayed from OSPC");
        Assert.assertTrue(gwParticipants.verifySentMessage());
    }

    @And("Navigate to Portal Authorization screen")
    public void navigateToPortalAuthorizationScreen() {
        if (!ExternalValidation.shouldRun(
                "Navigate to Portal Authorization Screen")) {
            return;
        }
        Hooks.log("Navigating to Portal Authorization screen");
        Assert.assertTrue(gwAdmin.navigatePortalAuthScreen());
    }

    @And("Remove the {string} from profile")
    public void removeTheFromProfile(String claimnumber) {
        if (!ExternalValidation.shouldRun(
                "Remove the Claim Number")) {
            return;
        }
        claimnumber = Hooks.currentRow.get().get("Claim Number");
        Hooks.log("Removing Claim Number from profile: " + claimnumber);
        Assert.assertTrue(gwAdmin.removeClaimfromPortalAuth(claimnumber));
    }

    @And("Remove all the claims from profile")
    public void removeAllTheClaimsFromProfile() {
        if (!ExternalValidation.shouldRun(
                "Remove all the claims from Profile")) {
            return;
        }
        Hooks.log("Removing all claims from profile");
        Assert.assertTrue(gwAdmin.removeAllClaimfromPortalAuth());
    }

    @And("Search for Claim number")
    public void searchForClaimNumber() {
        if (!ExternalValidation.shouldRun(
                "Search for the Claim")) {
            return;
        }
        Hooks.log("Searching for Claim Number: " + DirectDeposit.Claimnumber);
        Assert.assertTrue(gwSearch.searchClaim(DirectDeposit.Claimnumber));
    }

    @And("Verify the document is landed in GWCC with valid Author and Description")
    public void verifyTheDocumentIsDisplayedIsLandedInGWCCWith() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Verify the document is landed in GWCC with valid Author and Description")) {
            return;
        }
        String Author = Hooks.currentRow.get().get("Author");
        String description;
        String scenarioType = Hooks.getScenarioType();
        if ("WSIB".equals(scenarioType)) {
            description = Hooks.currentRow.get().get("Description");
            Hooks.log("Using 'Description' for WSIB scenario.");
        } else if ("NonWSIB".equals(scenarioType)) {
            description = Hooks.currentRow.get().get("nonDescription");
            Hooks.log("Using 'nonDescription' for NonWSIB scenario.");
        } else {
            // Fallback or error handling if scenarioType is not explicitly WSIB or NonWSIB
            Hooks.log("Unknown scenario type or neither WSIB nor NonWSIB tag found. Defaulting to 'Description'.");
            description = Hooks.currentRow.get().get("Description"); // Default to Description
        }
        Hooks.log("Verifying document in GWCC with Author: " + Author + " and Description: " + description);
        Assert.assertTrue(gwDocuments.verifyLandedDocument(Author,description));
    }



    @And("Verify the document are landed in GWCC with valid Author and Description")
    public void verifyTheDocumentIsDisplayedAreLandedInGWCCWith() throws InterruptedException {

        if (!ExternalValidation.shouldRun(
                "Verify the document are landed in GWCC with valid Author and Description")) {
            return;
        }
        String author = Hooks.currentRow.get().get("Author");
        String descriptionsJsonString; // This will hold the JSON array as a string

        String scenarioType = Hooks.getScenarioType();
        if ("WSIB".equals(scenarioType)) {
            descriptionsJsonString = Hooks.currentRow.get().get("Descriptions");
            Hooks.log("Using 'Descriptions' for WSIB scenario.");
        } else if ("NonWSIB".equals(scenarioType)) {
            descriptionsJsonString = Hooks.currentRow.get().get("nonDescriptions");
            Hooks.log("Using 'nonDescriptions' for NonWSIB scenario.");
        } else {
            Hooks.log("Unknown scenario type or neither WSIB nor NonWSIB tag found. Defaulting to 'Descriptions'.");
            descriptionsJsonString = Hooks.currentRow.get().get("Descriptions"); // Default to Descriptions
        }

        List<String> descriptions;
            descriptions = Arrays.stream(descriptionsJsonString.split("\\|"))
                    .map(String::trim)
                    .collect(Collectors.toList());
        Hooks.log("Verifying multiple documents in GWCC with Author: " + author + " and Descriptions: " + descriptions);
        
        // Use the plural version of the verification method for bulk validation
        Assert.assertTrue("Document validation failed in GWCC for descriptions: " + descriptionsJsonString,
                gwDocuments.verifyLandedDocuments(author, descriptions));
    }

    @And("Verify the document is landed in GWCC")
    public void verifyTheDocumentIsDisplayedIsLandedInGWCC() throws InterruptedException {

        if (!ExternalValidation.shouldRun(
                "Verify the document is landed in GWCC")) {
            return;
        }
        String Author = Hooks.currentRow.get().get("Author");
        String description = Hooks.currentRow.get().get("Description"); // Default to Description
        Hooks.log("Verifying Unnumbered document in GWCC with Author: " + Author + " and Description: " + description);
        Assert.assertTrue(gwDocuments.verifyLandedUNCDDocument(Author,description));
    }

    @And("Verify the EDOB document is landed in GWCC with {string}, {string}")
    public void verifyTheEDOBDocumentIsDisplayedIsLandedInGWCCWith(String Author, String Description) throws InterruptedException {

        if (!ExternalValidation.shouldRun(
                "Verify the EDOB document is landed in GWCC with")) {
            return;
        }
        Author = Hooks.currentRow.get().get("Author");
        Description = Hooks.currentRow.get().get("Description");

        Hooks.log("Verifying EDOB document in GWCC with Author: " + Author + " and Description: " + Description);
        Assert.assertTrue(gwDocuments.verifyLandedDocument(Author, Description));
    }

    @Given("User is on GWlogin page")
    public void userIsOnGWloginPage() {
        if (!ExternalValidation.shouldRun(
                "User is on GWlogin page")) {
            return;
        }
        Hooks.log("Verifying User is on GW Login Page");
        Assert.assertTrue(gwLogin.verifyonGWCC());
    }

    @When("User submit username and password")
    public void userSubmitUsernameAndPassword() {
        if (!ExternalValidation.shouldRun(
                "User submit username and password")) {
            return;
        }
        if(!System.getProperty("env").equalsIgnoreCase("PPD")) {
            String username = prop.getProperty("GWCC_Username");
            String password = prop.getProperty("GWCC_Password");
            Hooks.log("Submitting GWCC Username: " + username + " and Password: " + password);
            gwLogin.loginToGWCC(username, password);
        }
    }

    @Then("User should be able to login successfully into GWCC")
    public void userShouldBeAbleToLoginSuccessfullyIntoGWCC() {
        if (!ExternalValidation.shouldRun(
                "User should be able to login successfully into GWCC")) {
            return;
        }
        Hooks.log("Verifying successful login into GWCC");
        Assert.assertTrue(gwSearch.GWCCLoginsuccess());
    }

    @Given("User Logged into ClaimCenter")
    public void userLoggedIntoClaimCenter() {
        if (!ExternalValidation.shouldRun(
                "User Logged into ClaimCenter")) {
            return;
        }
        Hooks.log("Verifying User is logged into ClaimCenter");
        Assert.assertTrue(gwSearch.GWCCLoginsuccess());
    }

    @When("User initiates Manual Registration")
    public void userInitiatesManualRegistration() {
        if (!ExternalValidation.shouldRun(
                "User initiates Manual Registration")) {
            return;
        }
        Hooks.log("Initiating Manual Registration");
        Assert.assertTrue(gwRegistration.KickManualRegistration());
    }

    @Then("User selects Source of Information")
    public void userSelectsSourceOfInformationAs() {
        if (!ExternalValidation.shouldRun(
                "User selects Source of Information")) {
            return;
        }
        Hooks.log("Selecting Source of Information");
        gwRegistration.selectSourceofInformation();
    }

    @And("User enters new worker information")
    public void userEntersNewWorkerInformationAsBelow() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "User enters new worker information")) {
            return;
        }
        String gender = Hooks.currentRow.get().get("gender");
        String dob = Hooks.currentRow.get().get("dateofbirth");
        String postalcode = Hooks.currentRow.get().get("postalcode");
        String phonenumber = Hooks.currentRow.get().get("phonenumber");

        String manipulatedFirstName = "KKFirst"+ generateRandomText(5) ;
        String manipulatedLastName = "KKLast"+ generateRandomText(5);
        Hooks.log("Manipulating Name to: " + manipulatedFirstName + " " + manipulatedLastName);

        Hooks.currentRow.get().put("firstname", manipulatedFirstName);
        Hooks.currentRow.get().put("lastname", manipulatedLastName);
        JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());

        Hooks.log("Entering New Worker Information");
        gwRegistration.createNewWorker(manipulatedFirstName, manipulatedLastName, gender, dob, postalcode,phonenumber);
    }

    @And("User searches for Account with bussiness activity")
    public void userSearchesForWithBussinessActivity() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "User searches for Account with bussiness activity")) {
            return;
        }
        Hooks.log("Searching for Account with business activity");
        gwRegistration.searchAccountNumber();
    }

    @And("User enters Accident Details")
    public void userEntersAccidentDetailsAsBelow() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "User enters Accident Details")) {
            return;
        }
        String dateofAccident = Hooks.currentRow.get().get("dateofaccident");
        String dateReportedToEmployer = Hooks.currentRow.get().get("dateReported");
        Hooks.log("Entering Accident Details");
        gwRegistration.enterAccidentDetails(dateofAccident, dateReportedToEmployer);
    }

    @And("User Enters Body part details")
    public void userEntersBodyPartDetailsAsBelow() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "User Enters Body part details")) {
            return;
        }
        String areaofbody = Hooks.currentRow.get().get("areaofbody");
        String areaofinjury = Hooks.currentRow.get().get("areaofinjury");
        String accidentType = Hooks.currentRow.get().get("accidentType");
        String detailedaccidentType = Hooks.currentRow.get().get("detailedaccidentType");
        String natureofinjury = Hooks.currentRow.get().get("natureofinjury");
        Hooks.log("Entering Body part details");
        gwRegistration.enterBodyPartDetails(areaofbody, accidentType, areaofinjury, detailedaccidentType, natureofinjury);
    }

    @And("Select Third Party accident")
    public void selectThirdPartyAccidentAs() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Select Third Party accident")) {
            return;
        }
        Hooks.log("Selecting Third Party Accident");
        gwRegistration.TPASelection();
    }

    @And("Select Accident Location from the dropdown")
    public void selectAccidentLocationFromTheDropdown() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Select Accident Location from the dropdown")) {
            return;
        }
        Hooks.log("Selecting Accident Location");
        gwRegistration.selectAccidentLocation();
    }

    @And("Select Health Care Received")
    public void selectHealthCareReceivedAs() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Select Health Care Received")) {
            return;
        }
        String HCIndicator = Hooks.currentRow.get().get("HealthCareReceived");
        Hooks.log("Selecting Health Care Received");
        gwRegistration.selectHealthCareRecieved(HCIndicator);
    }

    @And("Select Claim Type")
    public void selectClaimTypeAs() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Select Claim Type")) {
            return;
        }
        String claimtype = Hooks.currentRow.get().get("ClaimType");
        Hooks.log("Selecting Claim Type");
        gwRegistration.selectClaimType(claimtype);
    }

    @And("Click on Register Claim button and verify that Claim has been registered Successfully")
    public void clickOnRegisterClaimButtonAndVerifyThatClaimHasBeenRegisteredSuccessfully() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Click on Register Claim button and verify that Claim has been registered Successfully")) {
            return;
        }
        Hooks.log("Registering Claim...");
        String[] claimdata = gwRegistration.registerClaim();
        
        Map<String, String> currentRowData = Hooks.currentRow.get();
        currentRowData.put("Claim Number", claimdata[0]);
        currentRowData.put("DOB", claimdata[1]);
        //currentRowData.put("PIN", claimdata[2]);

        Hooks.log("Claim Registered Successfully:: Claim Number:" + claimdata[0] + " DOB:" + claimdata[1] );
        JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());
        Hooks.currentRow.set(currentRowData);
    }

    @And("Search for Reference Number")
    public void searchForReferenceNumber() {
        if (!ExternalValidation.shouldRun(
                "Search for Reference Number")) {
            return;
        }
        String referenceNumber = Hooks.currentRow.get().get("ReferenceNumber");
        Hooks.log("Searching for Reference Number: " + referenceNumber);
        Assert.assertTrue(gwSearch.searchReference(referenceNumber));
    }

    @And("Verify intake and Worker Information and Validate worker Address")
    public void verifyIntakeAndWorkerInformationAndValidateWorkerAddress() {
        if (!ExternalValidation.shouldRun(
                "Verify intake and Worker Information and Validate worker Address")) {
            return;
        }
        String Fname = Hooks.currentRow.get().get("FirstName");
        String Lname = Hooks.currentRow.get().get("LastName");
        String Gender = Hooks.currentRow.get().get("Gender");
        String Pronouns = Hooks.currentRow.get().get("Pronouns");
        Hooks.log("Verifying intake and Worker Information and Validate worker Address");
        gwRegistration.verifyIntakeWorkerInformation(Fname, Lname, Gender, Pronouns);
    }

    @And("Search with firstname and lastname & dateofbirth {string}")
    public void searchWithFirstnameAndLastnameDateofbirth(String dateofbirth) throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Search with firstname and lastname & dateofbirth")) {
            return;
        }
        dateofbirth = Hooks.currentRow.get().get("dateofbirth");

        Hooks.log("Searching for firstname and lastname and dateofbirth");
        Assert.assertTrue(gwSearch.searchClaimant(firstname, lastname, dateofbirth));
    }

    @And("Verify the UNCD document is landed in GWCC with {string}, {string}")
    public void verifyTheUNCDDocumentIsLandedInGWCCWith(String Author, String Description) throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Verify the UNCD document is landed in GWCC with ")) {
            return;
        }
        Author = Hooks.currentRow.get().get("Author");
        Description = Hooks.currentRow.get().get("Description");

        Hooks.log("Verifying UNCD document in GWCC with Author: " + Author + " and Description: " + Description);
        Assert.assertTrue(gwDocuments.verifyLandedUNCDDocument(Author,Description));
    }

    @And("Search with firstname and lastname & dateofbirth")
    public void searchWithFirstnameAndLastnameDateofbirth() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Search with firstname and lastname & dateofbirth ")) {
            return;
        }
        String Firstname = Hooks.currentRow.get().get("First Name");
        String Lastname = Hooks.currentRow.get().get("Last Name");
        String dateofbirth = Hooks.currentRow.get().get("dateOfBirth");
        dateofbirth  = getDateFormat(dateofbirth, "d/MMMM/yyyy", "dd/MM/yyyy") ;
        Hooks.log("Searching for firstname and lastname and dateofbirth");
        Assert.assertTrue(gwSearch.searchClaimant(Firstname, Lastname, dateofbirth));
    }

    @And("Verify the Appeal is created in ClaimCenter")
    public void verifyTheAppealIsCreatedInClaimCenter() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Verify the Appeal is created in ClaimCenter")) {
            return;
        }
        Hooks.log("Verifying the Appeal is created in ClaimCenter");
        Assert.assertTrue(gwAppeals.createAppeal());
    }

    @And("Run the DI batch job in ClaimCenter to process the Auto Registration")
    public void runTheDIBatchJobInClaimCenterToProcessTheAutoRegistration() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Run the DI batch job in ClaimCenter to process the Auto Registration")) {
            return;
        }
        Hooks.log("Running DI7 Auto Registration...");
        Assert.assertTrue(gwAppeals.runDIBatchJob("Process Auto Reg Claims"));
    }

    @And("Verify the Claim is Auto Registered in ClaimCenter")
    public void verifyTheClaimIsAutoRegisteredInClaimCenter() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Verify the Claim is Auto Registered in ClaimCenter")) {
            return;
        }
        String Firstname = Hooks.currentRow.get().get("First Name");
        String Lastname = Hooks.currentRow.get().get("Last Name");
        String dateofbirth = Hooks.currentRow.get().get("Date of Birth");
        dateofbirth  = getDateFormat(dateofbirth, "d/MMMM/yyyy", "dd/MM/yyyy") ;
        Hooks.log("Verifying the Claim is Auto Registered in ClaimCenter");
        Assert.assertTrue("Failed DI7 Auto Registration...", gwSearch.searchDIClaim(Firstname, Lastname, dateofbirth));
    }

    @And("Verify the Mailing address is updated in GWCC")
    public void verifyTheMailingAddressIsUpdatedInGWCC() {
        if (!ExternalValidation.shouldRun(
                "Verify the Mailing address is updated in GWCC")) {
            return;
        }

        String addressLine1 = Hooks.currentRow.get().get("Address Line1").split("-")[1]
                +" "+ "UNIT "+Hooks.currentRow.get().get("Address Line1").split("-")[0];
        String addressLine2 = Hooks.currentRow.get().get("City")+ " "+
                getProvinceAbbreviation(Hooks.currentRow.get().get("Province"))+ " "+
                Hooks.currentRow.get().get("Postal Code");
        String addressLine3 = Hooks.currentRow.get().get("Country");
        Hooks.log("Verifying Mailing Address is updated in GWCC");
        Assert.assertTrue(gwParticipants.verifyUpdatedMailingAddress(addressLine1,addressLine2,addressLine3));
    }

    @And("Verify that history events are generated for Mailing address update in GWCC")
    public void verifyThatHistoryEventsAreGeneratedForMailingAddressUpdateInGWCC() {
        if (!ExternalValidation.shouldRun(
                "Verify that history events are generated for Mailing address update in GWCC")) {
            return;
        }

        String address = Hooks.currentRow.get().get("Address Line1") +
                ",\n"+ Hooks.currentRow.get().get("City")+ ", "+
                getProvinceAbbreviation(Hooks.currentRow.get().get("Province"))+ " "+
                Hooks.currentRow.get().get("Postal Code");
        String onlineuser= prop.getProperty("OSPC_username");
        Hooks.log("Verifying that history events are generated for Mailing address update in GWCC");
        Assert.assertTrue(gwcc_history.verifyHistoryEventForMailingAddressUpdate( address, onlineuser));
    }

    @And("Verify the Phone Number is updated in GWCC")
    public void verifyThePhoneNumberIsUpdatedInGWCC() {
        if (!ExternalValidation.shouldRun(
                "Verify the Phone Number is updated in GWCC")) {
            return;
        }
        String phonenumber = Hooks.currentRow.get().get("Phone Number");
        Hooks.log("Verifying Phone Number is updated in GWCC");
        Assert.assertTrue(gwParticipants.verifyUpdatedPhoneNumber(phonenumber));
    }

    @And("Verify that history events are generated for Phone Number update in GWCC")
    public void verifyThatHistoryEventsAreGeneratedForPhoneNumberUpdateInGWCC() {
        if (!ExternalValidation.shouldRun(
                "Verify that history events are generated for Phone Number update in GWCC")) {
            return;
        }
        String phonenumber = Hooks.currentRow.get().get("Phone Number");
        String onlineuser= prop.getProperty("OSPC_username");
        Hooks.log("Verifying that history events are generated for Phone Number update in GWCC");
        Assert.assertTrue(gwcc_history.verifyHistoryEventForPhoneNumberUpdate( phonenumber, onlineuser));
    }

    @Given("I have created a new claim in GW ClaimCenter with New Worker")
    public void iHaveCreatedANewClaimInGWClaimCenterNW() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "I have created a new claim in GW ClaimCenter with New Worker")) {
            return;
        }
        userLoggedIntoClaimCenter();
        userInitiatesManualRegistration();
        userSelectsSourceOfInformationAs();
        userEntersNewWorkerInformationAsBelow();
        userSearchesForWithBussinessActivity();
        userEntersAccidentDetailsAsBelow();
        userEntersBodyPartDetailsAsBelow();
        selectThirdPartyAccidentAs();
        selectAccidentLocationFromTheDropdown();
        selectHealthCareReceivedAs();
        selectClaimTypeAs();
        clickOnRegisterClaimButtonAndVerifyThatClaimHasBeenRegisteredSuccessfully();
    }

    @Then("Setup Appeals Data on the Claim")
    public void setupAppealsDataOnTheClaim() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "Setup Appeals Data on the Claim")) {
            return;
        }
        Hooks.log("Setup Appeals Data on the Claim");
        verifyTheAppealIsCreatedInClaimCenter();
    }

    @Then("Setup RTW information on the Claim")
    public void setupRTWInformationOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup RTW information on the Claim")) {
            return;
        }
        String Proposed_RTW_Goal= Hooks.currentRow.get().get("Proposed_RTW_Goal");
        String Proposed_Interim_RTW_Goal= Hooks.currentRow.get().get("Proposed_Interim_RTW_Goal");
        String Proposed_Recovery_Goal= Hooks.currentRow.get().get("Proposed_Recovery_Goal");
        String ProposedDate_RTW_Goal= Hooks.currentRow.get().get("ProposedDate_RTW_Goal");
        String ProposedDate_Interim_RTW_Goal= Hooks.currentRow.get().get("ProposedDate_Interim_RTW_Goal");
        String ProposedDate_Recovery_Goal= Hooks.currentRow.get().get("ProposedDate_Recovery_Goal");
        String Current_RTW_Status= Hooks.currentRow.get().get("Current_RTW_Status");
        String Current_Recovery_Status= Hooks.currentRow.get().get("Current_Recovery_Status");
        String Actual_RTW_Goal= Hooks.currentRow.get().get("Actual_RTW_Goal");
        String Actual_Recovery_Goal= Hooks.currentRow.get().get("Actual_Recovery_Goal");
        String Actual_RTW_Date= Hooks.currentRow.get().get("Actual_RTW_Date");
        String Actual_Recovery_Date= Hooks.currentRow.get().get("Actual_Recovery_Date");
        Hooks.log("Setup RTW information on the Claim");
        Hooks.log("RTW Data: Proposed=" + Proposed_RTW_Goal +
                ", Interim=" + Proposed_Interim_RTW_Goal +
                ", Recovery=" + Proposed_Recovery_Goal +
                ", Proposed Date=" + ProposedDate_RTW_Goal
                + ", Interim Date=" + ProposedDate_Interim_RTW_Goal
                + ", Recovery Date=" + ProposedDate_Recovery_Goal
                + ", Current Status=" + Current_RTW_Status
                + ", Recovery Status=" + Current_Recovery_Status
                + ", Actual RTW=" + Actual_RTW_Goal
                + ", Actual Recovery=" + Actual_Recovery_Goal
                + ", Actual RTW Date=" + Actual_RTW_Date
                + ", Actual Recovery Date=" + Actual_Recovery_Date);

        Assert.assertTrue(gwRecovery.setupRTWData(Proposed_RTW_Goal,Proposed_Interim_RTW_Goal,Proposed_Recovery_Goal,ProposedDate_RTW_Goal,
                ProposedDate_Interim_RTW_Goal,ProposedDate_Recovery_Goal,Current_RTW_Status,Current_Recovery_Status,Actual_RTW_Goal,
                Actual_Recovery_Goal,Actual_RTW_Date,Actual_Recovery_Date));
    }

    @And("User searches for Existing Worker")
    public void userSearchesForExistingWorker() {
        if (!ExternalValidation.shouldRun(
                "User searches for Existing Worker")) {
            return;
        }
        String WCBID = Hooks.currentRow.get().get("WCB ID");
        Hooks.log("Searching for Existing Worker with WCB ID: " + WCBID);
        Assert.assertTrue(gwRegistration.searchExistingWorker(WCBID));
    }

    @Given("I have created a new claim in GW ClaimCenter with Existing Worker")
    public void iHaveCreatedANewClaimInGWClaimCenterWithExistingWorker() throws InterruptedException {
        if (!ExternalValidation.shouldRun(
                "I have created a new claim in GW ClaimCenter with Existing Worker")) {
            return;
        }
        userLoggedIntoClaimCenter();
        userInitiatesManualRegistration();
        userSelectsSourceOfInformationAs();
        userSearchesForExistingWorker();
        userSearchesForWithBussinessActivity();
        userEntersAccidentDetailsAsBelow();
        userEntersBodyPartDetailsAsBelow();
        selectThirdPartyAccidentAs();
        selectAccidentLocationFromTheDropdown();
        selectHealthCareReceivedAs();
        selectClaimTypeAs();
        clickOnRegisterClaimButtonAndVerifyThatClaimHasBeenRegisteredSuccessfully();
    }

    @Then("Setup Device Details on the Claim")
    public void setupDeviceDetailsOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Device Details on the Claim")) {
            return;
        }
        Map<String, String> rowData = Hooks.currentRow.get();
        List<Map<String, String>> devices = new ArrayList<>();

        try {
            if (rowData.containsKey("DevicesData")) {
                devices = objectMapper.readValue(rowData.get("DevicesData"), new TypeReference<List<Map<String, String>>>(){});
            } else {
                if (rowData.containsKey("Device_Category")) {
                    Map<String, String> firstDevice = new java.util.HashMap<>(rowData);
                    devices.add(firstDevice);
                }
                int index = 1;
                while (rowData.containsKey("Device_Category_" + index)) {
                    Map<String, String> nextDevice = new java.util.HashMap<>();
                    String suffix = "_" + index;
                    nextDevice.put("Device_Category", rowData.get("Device_Category" + suffix));
                    nextDevice.put("Device_Type", rowData.get("Device_Type" + suffix));
                    nextDevice.put("Device_Status", rowData.get("Device_Status" + suffix));
                    nextDevice.put("Device_ServiceType", rowData.get("Device_ServiceType" + suffix));
                    nextDevice.put("Device_StartDate", rowData.get("Device_StartDate" + suffix));
                    nextDevice.put("Device_EndDate", rowData.get("Device_EndDate" + suffix));
                    nextDevice.put("Device_DecisionDate", rowData.get("Device_DecisionDate" + suffix));
                    nextDevice.put("PurposeReview", rowData.get("PurposeReview" + suffix));
                    nextDevice.put("Rationale", rowData.get("Rationale" + suffix));
                    devices.add(nextDevice);
                    index++;
                }
            }
        } catch (Exception e) {
            Hooks.log("Error parsing Device Data: " + e.getMessage());
        }

        if (devices.isEmpty()) {
            Hooks.log("No device data found in JSON.");
            return;
        }

        int count = 1;
        for (Map<String, String> device : devices) {
            String DeviceCategory = device.get("Device_Category");
            String DeviceType = device.get("Device_Type");
            String Device_Status = device.get("Device_Status");
            String Device_ServiceType = device.get("Device_ServiceType");
            String Device_StartDate = device.get("Device_StartDate");
            String Device_EndDate = device.get("Device_EndDate");
            String Device_DecisionDate = device.get("Device_DecisionDate");
            String Device_Review = device.get("PurposeReview");
            String Rationale = device.get("Rationale");

            Hooks.log("Setup Device Details on the Claim (Record " + count + ")");
            Hooks.log("Device Data: Device Category="+ DeviceCategory +
                    ", Device Type=" + DeviceType +
                    ", Device Status=" + Device_Status +
                    ", Device Service Type=" + Device_ServiceType +
                    ", Device Start Date=" + Device_StartDate +
                    ", Device End Date=" + Device_EndDate +
                    ", Device Decision Date=" + Device_DecisionDate +
                    ", Review=" + Device_Review +
                    ", Rationale=" + Rationale);
            
            Assert.assertTrue(gwRecovery.setupDeviceData(DeviceCategory,DeviceType,Device_Status,Device_ServiceType,
                    Device_StartDate,Device_EndDate,Device_DecisionDate,Device_Review,Rationale));
            count++;
        }
    }

    @Then("Setup Independent Living Allowance on the Claim")
    public void setupIndependentLivingAllowanceOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Independent Living Allowance on the Claim")) {
            return;
        }
        Map<String, String> rowData = Hooks.currentRow.get();
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            if (rowData.containsKey("ILA_Data")) {
                dataList = objectMapper.readValue(rowData.get("ILA_Data"), new TypeReference<List<Map<String, String>>>(){});
            } else if (rowData.containsKey("ILA_Status")) {
                dataList.add(rowData);
            }
        } catch (Exception e) { Hooks.log("Error parsing ILA Data: " + e.getMessage()); }

        int count = 1;
        for (Map<String, String> data : dataList) {
            String ilaStatus = data.get("ILA_Status");
            String ilaFromDate = data.get("ILA_FromDate");
            String ilaToDate = data.get("ILA_ToDate");
            Hooks.log("Setup Independent Living Allowance on the Claim (Record " + count + ")");
            Hooks.log("ILA Data: Status=" + ilaStatus + ", From=" + ilaFromDate + ", To=" + ilaToDate);
            Assert.assertTrue(gwRecovery.setupILAData(ilaStatus,ilaFromDate,ilaToDate));
            count++;
        }
    }

    @Then("Setup Personal Care Allowance on the Claim")
    public void setupPersonalCareAllowanceOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Personal Care Allowance on the Claim")) {
            return;
        }
        Map<String, String> rowData = Hooks.currentRow.get();
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            if (rowData.containsKey("PCA_Data")) {
                dataList = objectMapper.readValue(rowData.get("PCA_Data"), new TypeReference<List<Map<String, String>>>(){});
            } else if (rowData.containsKey("PCA_AssesmentType")) {
                dataList.add(rowData);
            }
        } catch (Exception e) { Hooks.log("Error parsing PCA Data: " + e.getMessage()); }

        int count = 1;
        for (Map<String, String> data : dataList) {
            String pcaAssesmentType = data.get("PCA_AssesmentType");
            String pcaAssessmentOutcome = data.get("PCA_AssessmentOutcome");
            String pcaFromDate = data.get("PCA_FromDate");
            String pcaToDate = data.get("PCA_ToDate");
            String pcaReassesRequired = data.get("PCA_ReassesRequired");
            String pcaReassessmentDate = data.get("PCA_ReassessmentDate");
            Hooks.log("Setup Personal Care Allowance on the Claim (Record " + count + ")");
            Hooks.log("PCA Data: Assesment Type=" + pcaAssesmentType + ", Outcome=" + pcaAssessmentOutcome);
            Assert.assertTrue(gwRecovery.setupPCAData(pcaAssesmentType,pcaAssessmentOutcome,pcaFromDate,
                    pcaToDate,pcaReassesRequired,pcaReassessmentDate));
            count++;
        }
    }

    @Then("Setup Treatment Details on the Claim")
    public void setupTreatmentDetailsOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Treatment Details on the Claim")) {
            return;
        }
        Map<String, String> rowData = Hooks.currentRow.get();
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            if (rowData.containsKey("Treatment_Data")) {
                dataList = objectMapper.readValue(rowData.get("Treatment_Data"), new TypeReference<List<Map<String, String>>>(){});
            } else if (rowData.containsKey("Treatment_Type")) {
                dataList.add(rowData);
            }
        } catch (Exception e) { Hooks.log("Error parsing Treatment Data: " + e.getMessage()); }

        int count = 1;
        for (Map<String, String> data : dataList) {
            String treatmentType = data.get("Treatment_Type");
            String treatmentStatus = data.get("Treatment_Status");
            String treatmentStartDate = data.get("Treatment_StartDate");
            String treatmentEndDate = data.get("Treatment_EndDate");
            String treatmentFreqNum = data.get("Treatment_FreqNum");
            String treatmentEntitlementDUnit = data.get("Treatment_EntitlementDUnit");
            String Device_Review = data.get("PurposeReview");
            String Rationale = data.get("Rationale");
            Hooks.log("Setup Treatment Details on the Claim (Record " + count + ")");
            Assert.assertTrue(gwRecovery.setupTreatmentData(treatmentType,treatmentStatus,treatmentStartDate,
                    treatmentEndDate, treatmentFreqNum, treatmentEntitlementDUnit, Device_Review, Rationale));
            count++;
        }
    }

    @Then("Setup Medication Details on the Claim")
    public void setupMedicationDetailsOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Medication Details on the Claim")) {
            return;
        }
        Map<String, String> rowData = Hooks.currentRow.get();
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            if (rowData.containsKey("Medication_Data")) {
                dataList = objectMapper.readValue(rowData.get("Medication_Data"), new TypeReference<List<Map<String, String>>>(){});
            } else if (rowData.containsKey("Medication_Type")) {
                dataList.add(rowData);
            }
        } catch (Exception e) { Hooks.log("Error parsing Medication Data: " + e.getMessage()); }

        int count = 1;
        for (Map<String, String> data : dataList) {
            String medicationType = data.get("Medication_Type");
            String medicationStatus = data.get("Medication_Status");
            String Formulary = data.get("Medication_Formulary");
            String medicationStartDate = data.get("Medication_StartDate");
            String medicationEndDate = data.get("Medication_EndDate");
            String Device_Review = data.get("PurposeReview");
            String Rationale = data.get("Rationale");

            Hooks.log("Setup Medication Details on the Claim (Record " + count + ")");
            Assert.assertTrue(gwRecovery.setupMedicationData(medicationType,medicationStatus,Formulary,
                    medicationStartDate,medicationEndDate, Device_Review, Rationale));
            count++;
        }
    }

    @Then("Setup Payments data on the Claim")
    public void setupPaymentsDataOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Payments data on the Claim")) {
            return;
        }
        String checkSourceId = Hooks.currentRow.get().get("CheckSourceId");
            String Payee = Hooks.currentRow.get().get("firstname");
            String PayTo = Hooks.currentRow.get().get("lastname");
            String Status = Hooks.currentRow.get().get("Status");
            String TAmount = Hooks.currentRow.get().get("TAmount");
            String IssuedDate = Hooks.currentRow.get().get("IssuedDate");

            String CostCategory =   Hooks.currentRow.get().get("CostCategory");
            String CostType = Hooks.currentRow.get().get("CostType");
            String Amount = Hooks.currentRow.get().get("Amount");
            String PayFromDate = Hooks.currentRow.get().get("PayFromDate");
            String PayToDate = Hooks.currentRow.get().get("PayToDate");

            Hooks.log("Setup Payments data on the Claim");
            Hooks.log("Payments Data: CheckSourceId=" + checkSourceId
                    + ", Payee=" + Payee
                    + ", PayTo=" + PayTo
                    + ", Status=" + Status
                    + ", TAmount=" + TAmount
                    + ", IssuedDate=" + IssuedDate
                    + ", CostCategory=" + CostCategory
                    + ", CostType=" + CostType
                    + ", Amount=" + Amount
                    + ", PayFromDate=" + PayFromDate
                    + ", PayToDate=" + PayToDate);

            Assert.assertTrue(gwRecovery.setupPaymentsData(checkSourceId,Payee,PayTo,Status,TAmount,IssuedDate,
                    CostCategory,CostType,Amount,PayFromDate,PayToDate));


    }

    @Then("Setup Clothing Allowance on the Claim")
    public void setupClothingAllowanceOnTheClaim() {
        if (!ExternalValidation.shouldRun(
                "Setup Clothing Allowance on the Claim")) {
            return;
        }
        Map<String, String> rowData = Hooks.currentRow.get();
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            if (rowData.containsKey("Clothing_Data")) {
                dataList = objectMapper.readValue(rowData.get("Clothing_Data"), new TypeReference<List<Map<String, String>>>(){});
            } else if (rowData.containsKey("ClothingAllowanceApproved")) {
                dataList.add(rowData);
            }
        } catch (Exception e) { Hooks.log("Error parsing Clothing Data: " + e.getMessage()); }

        int count = 1;
        for (Map<String, String> data : dataList) {
            String ClothingAllowanceApproved = data.get("ClothingAllowanceApproved");
            String ClothingAllowance_DecisionDate = data.get("ClothingAllowance_DecisionDate");

            Hooks.log("Setup Clothing Allowance on the Claim (Record " + count + ")");
            Assert.assertTrue(gwRecovery.setupClothingAllowanceData(ClothingAllowanceApproved,ClothingAllowance_DecisionDate));
            count++;
        }
    }

    ///NIHL Releated code
    @And("Search for the NIHL claimant in GWCC")
    public void searchForNIHLClaimantInGWCC() throws InterruptedException {
        if (!ExternalValidation.shouldRun("Search for the NIHL claimant in GWCC")) { return; }
        Map<String, String> row = nihlCurrentData();
        String firstName = nihlRequiredValue(row, "First Name");
        String lastName = nihlRequiredValue(row, "Last Name");
        String dob = getDateFormat(nihlRequiredValue(row, "dateOfBirth"),
                "d/MMMM/yyyy", "dd/MM/yyyy");
        Assert.assertTrue("NIHL claimant was not opened in GWCC within the configured search budget.",
                gwSearch.searchNIHLClaimant(firstName, lastName, dob,
                        nihlPositiveSetting("NIHLClaimTimeoutSeconds", 900),
                        nihlPositiveSetting("NIHLPollSeconds", 30)));
    }

    @And("Verify the NIHL submission documents landed in GWCC")
    public void verifyNIHLSubmissionDocumentsInGWCC(io.cucumber.datatable.DataTable table)
            throws InterruptedException {
        if (!ExternalValidation.shouldRun("Verify the NIHL submission documents landed in GWCC")) { return; }
        Map<String, String> row = nihlCurrentData();
        String reference = nihlRequiredValue(row, "NIHLGWCCReference");
        java.time.LocalDate submittedOn = java.time.LocalDate.parse(
                nihlRequiredValue(row, "NIHLSubmissionDate"));
        Map<String, String> options = table.asMap(String.class, String.class);
        if (!row.containsKey("HealthCareFiles") || !row.containsKey("EmploymentFiles")) {
            throw new IllegalStateException("Upload inputs were not retained in this scenario's data. "
                    + "Apply the accompanying NIHL upload-step change.");
        }
        List<String> uploaded = new ArrayList<>();
        uploaded.addAll(nihlPipeValues(row.get("HealthCareFiles")));
        uploaded.addAll(nihlPipeValues(row.get("EmploymentFiles")));
        List<String> descriptions = nihlPipeValues(options.get("SupportingDescriptions"));
        List<String> authors = nihlPipeValues(options.get("SupportingAuthors"));
        if (descriptions.size() != uploaded.size()) {
            throw new IllegalArgumentException("Configured " + descriptions.size()
                    + " supporting GWCC descriptions for " + uploaded.size()
                    + " uploaded files. Supply one actual description per file, in health-care order "
                    + "followed by employment order. Filenames are not assumed to be descriptions.");
        }
        if (!uploaded.isEmpty()) {
            if (authors.size() != 1
                    && authors.size() != uploaded.size()) {
                throw new IllegalArgumentException(
                        "Supply one supporting-document author "
                                + "or one author per uploaded file.");
            }
        } else if (!authors.isEmpty()) {
            throw new IllegalArgumentException(
                    "SupportingAuthors must be blank "
                            + "when no files were uploaded.");
        }
        List<GWDocumentsPage.NIHLDocumentExpectation> expected = new ArrayList<>();
        // Preserve the report's existing contains-matching, not its legacy retry/baseline logic.
        expected.add(new GWDocumentsPage.NIHLDocumentExpectation(
                nihlRequiredValue(row, "Author"), nihlRequiredValue(row, "Description"), false));
        for (int i = 0; i < descriptions.size(); i++) {
            expected.add(new GWDocumentsPage.NIHLDocumentExpectation(
                    authors.size() == 1 ? authors.get(0) : authors.get(i), descriptions.get(i), false));
        }
        Hooks.log("[NIHL GWCC] Verifying reference " + reference
                + "; expected report/supporting row occurrences=" + expected.size());
        Assert.assertTrue("NIHL GWCC document verification timed out for reference " + reference
                        + ". Expected: " + expected,
                gwDocuments.verifyNIHLUNCDDocuments(expected, submittedOn,
                        nihlPositiveSetting("NIHLDocumentTimeoutSeconds", 900),
                        nihlPositiveSetting("NIHLPollSeconds", 30),
                        nihlPositiveSetting("NIHLMaxDocumentPages", 200)));
    }

    private Map<String, String> nihlCurrentData() {
        Map<String, String> row = Hooks.currentRow.get();
        if (row == null) { throw new IllegalStateException("NIHL scenario data is not initialized."); }
        return row;
    }

    private String nihlRequiredValue(Map<String, String> values, String key) {
        String value = values.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Missing NIHL test value: " + key);
        }
        return value.trim();
    }

    private List<String> nihlPipeValues(String value) {
        List<String> result = new ArrayList<>();
        if (value == null || value.trim().isEmpty()) { return result; }
        for (String token : value.split("\\|", -1)) {
            String item = token.trim();
            if (item.isEmpty()) {
                throw new IllegalArgumentException("Empty item in NIHL pipe-separated input: " + value);
            }
            if (item.startsWith("REPLACE_")) {
                throw new IllegalArgumentException("Replace the NIHL GWCC mapping placeholder: " + item);
            }
            result.add(item);
        }
        return result;
    }

    private int nihlPositiveSetting(String key, int defaultValue) {
        String configured = prop.getProperty(key);
        if (configured == null || configured.trim().isEmpty()) { return defaultValue; }
        try {
            int value = Integer.parseInt(configured.trim());
            if (value <= 0) { throw new NumberFormatException("not positive"); }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a positive integer; found " + configured, e);
        }
    }



}