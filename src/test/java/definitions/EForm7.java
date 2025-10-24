package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DynamicIntakeEform6.*;
import pages.DynamicIntakeEform7.DIeForm7;
import pages.DynamicIntakeEform7.DIeForm7_EmployerDetails;
import pages.DynamicIntakeEform7.DIeForm7_ReportType;
import pages.DynamicIntakeEform7.DIeForm7_ReporterDetails;
import pages.GuidewireClaimCenter.GuidewirePage;
import pages.OSPC.OSPCClaimOverview;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

public class EForm7 {

    private WebDriver driver;
    private WebDriverWait wait;
    private DIeForm7 eform7;
    private DIeForm7_ReportType eform7_reporttype;
    private DIeForm7_ReporterDetails eform7_reporterdetails;
    private DIeForm7_EmployerDetails eform7_employerdetails;

    private GuidewirePage guidewirecc;
    private pages.OSPC.OSPCClaimOverview OSPCClaimOverview;
    ConfigReader prop= Hooks.getConfigReader();
    public EForm7() throws Exception {
        driver = Hooks.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));

        guidewirecc = new GuidewirePage(driver, wait);
        OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
        eform7 = new DIeForm7(driver, wait);
        eform7_reporttype = new DIeForm7_ReportType(driver, wait);
        eform7_reporterdetails = new DIeForm7_ReporterDetails(driver, wait);
        eform7_employerdetails = new DIeForm7_EmployerDetails(driver, wait);
    }


    @Given("User is on online services screen")

    public void userIsOnOnlineServicesScreen() {
        Assert.assertTrue(eform7.verifyBusinessHomepage());

    }


    @When("User click on Report an injury or illness Form7 GO button")
    public void userClickOnReportAnInjuryOrIllnessFormGOButton() throws InterruptedException {
        Assert.assertTrue(   eform7.clickOnGOButton());

    }

    @Then("User selects an account and Start Report")
    public void userSelectsAnAccountAndStartReport() throws InterruptedException {
        String accountnumber = Hooks.currentRow.get().get("Account Number");
        Assert.assertTrue(eform7.selectAccountAndStartReport( accountnumber));
    }

    @And("User should be able to select report type Report Type")
    public void userShouldBeAbleToSelectReportTypeReportType() {
        String reporttype = Hooks.currentRow.get().get("Report Type");
        Assert.assertTrue(eform7.selectReportType(reporttype));
    }

    @And("User should be able to select Injury Category")
    public void userShouldBeAbleToSelectInjuryCategory() {
        String injurycategory = Hooks.currentRow.get().get("Injury Category");
        Assert.assertTrue(eform7.selectInjuryCategory(injurycategory));
    }

    @And("User should be able to select radio PhysicalInjury, Body Part")
    public void userShouldBeAbleToSelectRadioPhysicalInjuryBodyPart() {
        String PhysicalInjury = Hooks.currentRow.get().get("Physical Injury");
        String AccidentType = Hooks.currentRow.get().get("Accident Type");
        String DescribedBy = Hooks.currentRow.get().get("Described By");
        Assert.assertTrue(eform7_reporttype.selectPhysicalInjuryDetails(PhysicalInjury,AccidentType,DescribedBy));
    }

    @And("User should be able to Select Reported by")
    public void userShouldBeAbleToSelectReportedBy() {
        String ReportedBy = Hooks.currentRow.get().get("Reporter");
        Assert.assertTrue(eform7_reporterdetails.selectReportedByDetails(ReportedBy));
    }

    @And("User should be able to enter Employer details and get Reference Claim Number")
    public void userShouldBeAbleToEnterEmployerDetails() throws InterruptedException, IOException {
        String mailingaddress = Hooks.currentRow.get().get("Mailing Address");
        String BranchAddress = Hooks.currentRow.get().get("Branch Address");
        String BusinessYear = Hooks.currentRow.get().get("Business Year");
        String NAICSCode = Hooks.currentRow.get().get("NAICS Code");

        String referenceClaimNumber = eform7_employerdetails.enterEmployerDetails(mailingaddress,BranchAddress,BusinessYear,NAICSCode);

        System.out.println("Reference Claim Number is: "+referenceClaimNumber);
        Assert.assertTrue(referenceClaimNumber.length()>0);
        // Update the current row data with the new values
        Map<String, String> currentRowData = Hooks.currentRow.get();
        currentRowData.put("ReferenceNumber", referenceClaimNumber);

        String sheetName = Hooks.sheetName;
        ExcelUtils.updateExcelData(sheetName, currentRowData);
        Hooks.currentRow.set(currentRowData);




    }
}
