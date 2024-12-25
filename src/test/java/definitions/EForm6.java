package definitions;

import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class EForm6 {

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private HomePage homePage;
    private DIeForm6 eform6;
    private GuidewirePage guidewirecc;
    private pages.OSPCClaimOverview OSPCClaimOverview;
    ConfigReader prop= Hooks.getConfigReader();
    public EForm6() throws Exception {
        driver = Hooks.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
        loginPage = new LoginPage(driver, wait);
        homePage = new HomePage(driver, wait);
        eform6= new DIeForm6(driver, wait);
        guidewirecc = new GuidewirePage(driver, wait);
        OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
    }

    @When("User click on Submit Report button")
    public void userClickOnSubmitReportButton() {
        Assert.assertTrue(eform6.verifySubmitReportButtonDisplayed());
        Assert.assertTrue(eform6.clickSubmitReportButton());

    }

    @Then("User should be able to provide consent to the privacy statement")
    public void userShouldBeAbleToProvideConsentToThePrivacyStatement() {
        Assert.assertTrue(eform6.clickPrivacyStatement());
    }

    @And("User should be able to select report type {string}")
    public void userShouldBeAbleToSelectreporttype(String reporttype) {
        reporttype = Hooks.currentRow.get().get("ReportType");
        Assert.assertTrue(eform6.selectReportType(reporttype));
    }

    @And("User should be able to select Injury Type {string}")
    public void userShouldBeAbleToSelectInjuryType(String injurytype) {
        injurytype = Hooks.currentRow.get().get("InjuryType");
        Assert.assertTrue(eform6.selectInjuryType(injurytype));

    }

    @And("User should be able to select radio {string}, Body Part {string}")
    public void userShouldBeAbleToSelectRadioBodyPart(String PhysicalInj, String Bodypart) {
        PhysicalInj = Hooks.currentRow.get().get("PhysicalInjury");
        Bodypart = Hooks.currentRow.get().get("BodyPart");
        Assert.assertTrue(eform6.selectBodyPart(PhysicalInj, Bodypart));
    }

    @And("User should be able to enter {string} if applicable")
    public void userShouldBeAbleToEnterIfApplicable(String existingClaimnumber) {
        existingClaimnumber = Hooks.currentRow.get().get("ExistingClaimNumber");
        Assert.assertTrue(eform6.enterExistingClaimNumber(existingClaimnumber));

    }

    @And("User should be able to enter {string}, {string}, {string}, {string}")
    public void userShouldBeAbleToEnter(String FirstName, String lastname, String Phonenumber, String Pronouns) throws InterruptedException {
        FirstName = Hooks.currentRow.get().get("FirstName");
        lastname = Hooks.currentRow.get().get("LastName");
        Phonenumber = Hooks.currentRow.get().get("PhoneNumber");
        Pronouns = Hooks.currentRow.get().get("Pronouns");
        Assert.assertTrue(eform6.enterPersonalInformation(FirstName, lastname, Phonenumber, Pronouns));

    }

    @And("Enter Additional details like {string}, {string}, {string}, {string},{string}, {string}")
    public void enterAdditionalDetailsLike(String gender, String Day, String Month, String Year, String PreferredLang, String SIN) throws InterruptedException {
        gender= Hooks.currentRow.get().get("Gender");
        Day= Hooks.currentRow.get().get("DateofBirthDay");
        Month= Hooks.currentRow.get().get("DateofBirthMonth");
        Year= Hooks.currentRow.get().get("DateofBirthYear");
        PreferredLang= Hooks.currentRow.get().get("PreferredLanguage");
        SIN= Hooks.currentRow.get().get("sinNumber");
        Assert.assertTrue(eform6.enterAdditionalDetails(gender, Day, Month, Year, PreferredLang, SIN));

    }

    @And("Enter {string}, {string}, {string}, {string}, {string}")
    public void enter(String Address1, String City, String Province, String postalcode, String country) throws InterruptedException {
        Address1= Hooks.currentRow.get().get("Address");
        City= Hooks.currentRow.get().get("City");
        Province= Hooks.currentRow.get().get("Province");
        postalcode= Hooks.currentRow.get().get("PostalCode");
        country= Hooks.currentRow.get().get("Country");
        Assert.assertTrue(eform6.enterMailingAddress(Address1, City, Province, postalcode, country));
    }

    @And("Enter {string}, {string}, {string}, {string}, {string}, {string}, {string}")
    public void enter(String Employername, String EmpPhone, String EmpAddress1, String EmpCity, String EmpProvince, String EmpPostalcode, String EmpCountry) throws InterruptedException {
        Employername= Hooks.currentRow.get().get("EmployerName");
        EmpPhone= Hooks.currentRow.get().get("EmployerPhone");
        EmpAddress1= Hooks.currentRow.get().get("EmployerAddress");
        EmpCity= Hooks.currentRow.get().get("EmployerCity");
        EmpProvince= Hooks.currentRow.get().get("EmployerProvince");
        EmpPostalcode= Hooks.currentRow.get().get("EmployerPostalCode");
        EmpCountry= Hooks.currentRow.get().get("EmployerCountry");
        eform6.enterEmployerDetails(Employername, EmpPhone, EmpAddress1, EmpCity, EmpProvince, EmpPostalcode, EmpCountry);


    }

    @Then("Verify the reference number is generated and displayed")
    public void verifyTheReferenceNumberIsGeneratedAndDisplayed() throws IOException {
       String referenceClaimNumber = eform6.verifyReferenceNumber();
       System.out.println("Reference Claim Number is: "+referenceClaimNumber);
         Assert.assertTrue(referenceClaimNumber.length()>0);
        ExcelUtils.updateExcelData("eForm6", "ReferenceNumber", referenceClaimNumber);

    }
}
