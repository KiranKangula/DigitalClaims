package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DynamicIntakeEform7.*;
import pages.OSPC.OSPCClaimOverview;
import utils.ConfigReader;
import utils.JsonUtils;

import java.time.Duration;
import java.util.Map;

import static utils.BaseDriverClass.generateRandomText;
import static utils.BaseDriverClass.getCurrentDate;

public class EForm7 {

    private WebDriver driver;
    private WebDriverWait wait;
    private DIeForm7 eform7;
    private DIeForm7_ReportType eform7_reporttype;
    private DIeForm7_ReporterDetails eform7_reporterdetails;
    private DIeForm7_EmployerDetails eform7_employerdetails;
    private DIeForm7_EmployeeDetails eform7_employeedetails;
    private DIeForm7_WorkHistory eform7_workhistorydetails;
    private DIeForm7_InjuryIllnessDetails eform7_injuryillnessdetails;
    private DIeForm7_HealthCareDetails eform7_healthcaredetails;
    private DIeForm7_LosstimeEarningsDetails eform7_losstimeearningsdetails;
    private DIeForm7_SupportingDocuments eform7_supportingdocuments;
    private DIeForm7_ReviewandSubmit eform7_reviewandsubmit;

    private pages.OSPC.OSPCClaimOverview OSPCClaimOverview;
    ConfigReader prop= Hooks.getConfigReader();
    ConfigReader common = Hooks.getCommonReader();
    public EForm7() throws Exception {
        driver = Hooks.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));

        OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
        eform7 = new DIeForm7(driver, wait);
        eform7_reporttype = new DIeForm7_ReportType(driver, wait);
        eform7_reporterdetails = new DIeForm7_ReporterDetails(driver, wait);
        eform7_employerdetails = new DIeForm7_EmployerDetails(driver, wait);
        eform7_employeedetails = new DIeForm7_EmployeeDetails(driver, wait);
        eform7_workhistorydetails = new DIeForm7_WorkHistory(driver, wait);
        eform7_injuryillnessdetails = new DIeForm7_InjuryIllnessDetails(driver, wait);
        eform7_healthcaredetails = new DIeForm7_HealthCareDetails(driver, wait);
        eform7_losstimeearningsdetails = new DIeForm7_LosstimeEarningsDetails(driver, wait);
        eform7_supportingdocuments = new DIeForm7_SupportingDocuments(driver, wait);
        eform7_reviewandsubmit = new DIeForm7_ReviewandSubmit(driver, wait);
    }


    @Given("User is on online services screen")
    public void userIsOnOnlineServicesScreen() {
        Hooks.log("Verifying user is on Business Online Services screen.");
        Assert.assertTrue(eform7.verifyBusinessHomepage());

    }


    @When("User click on Report an injury or illness Form7 GO button")
    public void userClickOnReportAnInjuryOrIllnessFormGOButton() throws InterruptedException {
        Hooks.log("Clicking on 'GO' button for Report an injury or illness (Form 7).");
        Assert.assertTrue(eform7.clickOnGOButton());

    }

    @Then("User selects an account and Start Report")
    public void userSelectsAnAccountAndStartReport() throws InterruptedException {
        String accountnumber = Hooks.currentRow.get().get("Account Number");
        Hooks.log("Selecting Account Number: " + accountnumber + " and starting report.");
        Assert.assertTrue(eform7.selectAccountAndStartReport( accountnumber));
    }

    @And("User should be able to select report type Report Type")
    public void userShouldBeAbleToSelectReportTypeReportType() {
        String reporttype = Hooks.currentRow.get().get("Report Type");
        Hooks.log("Selecting Report Type: " + reporttype);
        Assert.assertTrue(eform7.selectReportType(reporttype));
    }

    @And("User should be able to select Injury Category")
    public void userShouldBeAbleToSelectInjuryCategory() {
        String injurycategory = Hooks.currentRow.get().get("Injury Category");
        Hooks.log("Selecting Injury Category: " + injurycategory);
        Assert.assertTrue(eform7.selectInjuryCategory(injurycategory));
    }

    @And("User should be able to select radio PhysicalInjury, Body Part")
    public void userShouldBeAbleToSelectRadioPhysicalInjuryBodyPart() {
        String PhysicalInjury = Hooks.currentRow.get().get("Physical Injury");
        String AccidentType = Hooks.currentRow.get().get("Accident Type");
        String DescribedBy = Hooks.currentRow.get().get("Described By");
        Hooks.log("Selecting Physical Injury Details: " + PhysicalInjury + ", Accident Type: " + AccidentType);
        Assert.assertTrue(eform7_reporttype.selectPhysicalInjuryDetails(PhysicalInjury,AccidentType,DescribedBy));
    }

    @And("User should be able to Select Reported by")
    public void userShouldBeAbleToSelectReportedBy() {
        String ReportedBy = Hooks.currentRow.get().get("Reporter");
        Hooks.log("Selecting Reported By: " + ReportedBy);
        Assert.assertTrue(eform7_reporterdetails.selectReportedByDetails(ReportedBy));
    }

    @And("User should be able to enter Employer details and get Reference Claim Number")
    public void userShouldBeAbleToEnterEmployerDetails() throws InterruptedException {
        String mailingaddress = Hooks.currentRow.get().get("Mailing Address");
        String BranchAddress = Hooks.currentRow.get().get("Branch Address");
        String BusinessYear = Hooks.currentRow.get().get("Business Year");
        String NAICSCode = Hooks.currentRow.get().get("NAICS Code");

        Hooks.log("Entering Employer details...");
        String referenceClaimNumber = eform7_employerdetails.enterEmployerDetails(mailingaddress,BranchAddress,BusinessYear,NAICSCode);

        Hooks.log("Reference Claim Number is: " + referenceClaimNumber);
        Assert.assertTrue(referenceClaimNumber.length()>0);
        // Update the current row data with the new values
        Map<String, String> currentRowData = Hooks.currentRow.get();
        currentRowData.put("ReferenceNumber", referenceClaimNumber);

        String sheetName = Hooks.sheetName.get();
        JsonUtils.writeJsonData(sheetName, Hooks.data.get());
        Hooks.currentRow.set(currentRowData);
    }

    @And("User should be able to enter Employee details")
    public void userShouldBeAbleToEnterEmployeeDetails() throws InterruptedException {
        String EmployeeFirstName = "Auto"+ generateRandomText(5) ;
        String EmployeeLastName = "Test"+ generateRandomText(5);
        String PhoneNumber = Hooks.currentRow.get().get("Phone Number");
        String Sex = Hooks.currentRow.get().get("Sex");
        String Pronouns = Hooks.currentRow.get().get("Pronouns");
        String DateOfBirth = Hooks.currentRow.get().get("Date of Birth");
        String Language = Hooks.currentRow.get().get("Language of Service");
        String SIN = Hooks.currentRow.get().get("SIN");
        String Address = Hooks.currentRow.get().get("Address");

        Hooks.log("Entering Employee Details for: " + EmployeeFirstName + " " + EmployeeLastName);

        // Update the current row data
        Hooks.currentRow.get().put("First Name", EmployeeFirstName);
        Hooks.currentRow.get().put("Last Name", EmployeeLastName);
        
        JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());
        Hooks.log("Updated JSON with auto-generated name.");

        Assert.assertTrue(eform7_employeedetails.selectEmployeeDetails(EmployeeFirstName,EmployeeLastName,
                PhoneNumber,Sex,Pronouns,DateOfBirth,Language,SIN,Address));
    }

    @And("User should be able to enter Work History details")
    public void userShouldBeAbleToEnterWorkHistoryDetails() throws InterruptedException {
        String JobTitle = Hooks.currentRow.get().get("Job Title");
        String EmpStartDate = Hooks.currentRow.get().get("Emp Start Date");
        String EmpYears = Hooks.currentRow.get().get("Emp Years");
        String EmpMonths = Hooks.currentRow.get().get("Emp Months");
        String SupervisorContact = Hooks.currentRow.get().get("Supervisor Contact");
        String Employees = Hooks.currentRow.get().get("# Employees");
        String Union = Hooks.currentRow.get().get("Union?");
        String IsSpouseRelavtive = Hooks.currentRow.get().get("IsSpouseRelavtive");
        String EmployerRelation = Hooks.currentRow.get().get("EmployerRelation");

        Hooks.log("Entering Work History details: Job Title=" + JobTitle);

        Assert.assertTrue(eform7_workhistorydetails.selectWorkHistoryDetails(JobTitle,EmpStartDate,EmpYears,EmpMonths,SupervisorContact,
                Employees,Union,IsSpouseRelavtive,EmployerRelation));
    }


    @And("User should be able to enter Injury Illness details")
    public void userShouldBeAbleToEnterInjuryIllnessDetails() throws InterruptedException {
        String WhatHappenedInput = Hooks.currentRow.get().get("WhatHappenedInput");
        String InjuryDate = Hooks.currentRow.get().get("Injury Date");
        String BodyParts = Hooks.currentRow.get().get("Body Parts");
        String EmployerKnownDate = Hooks.currentRow.get().get("EmployerKnownDate");
        String reportedTo = Hooks.currentRow.get().get("reported to?");
        String Witness = Hooks.currentRow.get().get("Witness");
        String PriorConditions = Hooks.currentRow.get().get("Prior Conditions");
        String ThirdParty = Hooks.currentRow.get().get("Third Party");
        String Objection = Hooks.currentRow.get().get("Objection");

        Hooks.log("Entering Injury/Illness details: " + WhatHappenedInput);

        Assert.assertTrue(eform7_injuryillnessdetails.selectInjuryIllnessDetails(WhatHappenedInput,InjuryDate,BodyParts,
                EmployerKnownDate,reportedTo,Witness,PriorConditions,ThirdParty,Objection));

    }

    @And("User should be able to enter Health care details")
    public void userShouldBeAbleToEnterHealthCareDetails() throws InterruptedException {
        String HealthCare = Hooks.currentRow.get().get("HealthCare");
        Hooks.log("Entering Health Care details: " + HealthCare);
        Assert.assertTrue(eform7_healthcaredetails.selectHealthCareDetails(HealthCare));
    }

    @And("User should be able to enter Loss Time and Earnings details")
    public void userShouldBeAbleToEnterLossTimeAndEarningsDetails() {
        String LOE = Hooks.currentRow.get().get("LOE");
        String TypeofWork = Hooks.currentRow.get().get("Type of Work");
        String WageLoss = Hooks.currentRow.get().get("Wage Loss");
        Hooks.log("Entering Loss Time and Earnings details: LOE=" + LOE);
        Assert.assertTrue(eform7_losstimeearningsdetails.selectLossTimeAndEarningsDetails(LOE,TypeofWork,WageLoss));
    }

    @And("User should be able to attach Supporting Documents")
    public void userShouldBeAbleToAttachSupportingDocuments() throws InterruptedException {
        Hooks.log("Attaching supporting documents.");
       Assert.assertTrue( eform7_supportingdocuments.attachSupportingDocuments());
    }

    @And("User should be able to review and submit the report")
    public void userShouldBeAbleToReviewAndSubmitTheReport() {
        Hooks.log("Reviewing and submitting the report.");
        Assert.assertTrue( eform7_reviewandsubmit.submitEform7Report());
        Hooks.log("Report Submitted at: " + getCurrentDate("MMM dd, yyyy hh:mm:ss a"));
    }


}
