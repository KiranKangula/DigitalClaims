package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DynamicIntakeEform6.*;
import pages.GuidewireClaimCenter.*;
import pages.OSPC.*;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

public class EForm6 {

    private WebDriver driver;
    private WebDriverWait wait;
    private DIeForm6 eform6;
    private DIeForm6_WorkHistory eform6WorkHistory;
    private DIeForm6_InjuryDetails eform6InjuryDetails;
    private DIeForm6_ReviewandSubmit eform6ReviewandSubmit;
    private DIeForm6_SupportingDocuments eform6SupportingDocuments;
    private DIeForm6_LostTimeandEarnings eform6LostTimeandEarnings;
    private DIeForm6_HealthCare eform6HealthCare;
    private DIeForm6_ExposureDetails eform6ExposureDetails;
    private GuidewirePage guidewirecc;
    private pages.OSPC.OSPCClaimOverview OSPCClaimOverview;
    ConfigReader prop= Hooks.getConfigReader();
    ConfigReader common = Hooks.getCommonReader();
    public EForm6() throws Exception {
        driver = Hooks.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
        eform6= new DIeForm6(driver, wait);
        eform6WorkHistory = new DIeForm6_WorkHistory(driver, wait);
        eform6InjuryDetails = new DIeForm6_InjuryDetails(driver, wait);
        eform6ReviewandSubmit = new DIeForm6_ReviewandSubmit(driver, wait);
        eform6SupportingDocuments = new DIeForm6_SupportingDocuments(driver, wait);
        eform6LostTimeandEarnings = new DIeForm6_LostTimeandEarnings(driver, wait);
        eform6HealthCare = new DIeForm6_HealthCare(driver, wait);
        eform6ExposureDetails = new DIeForm6_ExposureDetails(driver, wait);
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
        eform6.selectReportType(reporttype);
    }

    @And("User should be able to select Injury Type {string}")
    public void userShouldBeAbleToSelectInjuryType(String injurytype) {
        injurytype = Hooks.currentRow.get().get("InjuryType");
        eform6.selectInjuryType(injurytype);

    }

    @And("User should be able to select radio {string}, Body Part {string}")
    public void userShouldBeAbleToSelectRadioBodyPart(String PhysicalInj, String Bodypart) {
        PhysicalInj = Hooks.currentRow.get().get("PhyInjury");
        Bodypart = Hooks.currentRow.get().get("DescribeInjury");
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

    @And("Enter Additional personal details")
    public void enterAdditionalDetailsLike() throws InterruptedException {
       String gender= Hooks.currentRow.get().get("Gender");
        String DateofBirth= Hooks.currentRow.get().get("DateofBirth");
        String  PreferredLang= Hooks.currentRow.get().get("PreferredLanguage");
        String  SIN= Hooks.currentRow.get().get("sinNumber");
        Assert.assertTrue(eform6.enterAdditionalDetails(gender, DateofBirth, PreferredLang, SIN));

    }

    @And("Enter Additional personal details - Exposure")
    public void enterAdditionalDetailsExposure() throws InterruptedException {
        String gender= Hooks.currentRow.get().get("Gender");
        String DateofBirth= Hooks.currentRow.get().get("DateofBirth");
        Assert.assertTrue(eform6.enterAdditionalDetails(gender, DateofBirth));

    }

    @And("Enter Worker Address Details")
    public void enterWorkerAddressDetails() throws InterruptedException {
        String Address= Hooks.currentRow.get().get("Address");
        Assert.assertTrue(eform6.enterMailingAddress(Address));
    }

    @And("Enter Employers Details")
    public void enterEmployerDetails() throws InterruptedException {
       String Employername= Hooks.currentRow.get().get("EmployerName");
        String EmpPhone= Hooks.currentRow.get().get("EmployerPhone");
        String EmployerAddress= Hooks.currentRow.get().get("EmployerAddress");
        eform6.enterEmployerDetails(Employername, EmpPhone, EmployerAddress);
    }

    @Then("Verify the reference number is generated and displayed")
    public void verifyTheReferenceNumberIsGeneratedAndDisplayed() throws IOException {
       String referenceClaimNumber = eform6.verifyReferenceNumber();
       System.out.println("Reference Claim Number is: "+referenceClaimNumber);
         Assert.assertTrue(referenceClaimNumber.length()>0);
        // Update the current row data with the new values
        Map<String, String> currentRowData = Hooks.currentRow.get();
        currentRowData.put("ReferenceNumber", referenceClaimNumber);

        String sheetName = Hooks.sheetName;
        ExcelUtils.updateExcelData(sheetName, currentRowData);
        Hooks.currentRow.set(currentRowData);

    }

    @And("Enter the Job Details of the Worker")
    public void enterTheJobDetailsOfTheWorker() {
        String JobTitle= Hooks.currentRow.get().get("JobTitle");
        String StartDateDay= Hooks.currentRow.get().get("JobStartDateDay");
        String StartDateMonth= Hooks.currentRow.get().get("JobStartDateMonth");
        String StartDateYear= Hooks.currentRow.get().get("JobStartDateYear");
        String EmpDurationYears= Hooks.currentRow.get().get("EmpDurationYears");
        String EmpDurationMonths= Hooks.currentRow.get().get("EmpDurationMonths");
        String SupervisorFirstName= Hooks.currentRow.get().get("SupervisorFirstName");
        String SupervisorLastName= Hooks.currentRow.get().get("SupervisorLastName");
        String SupervisorPhoneNumber= Hooks.currentRow.get().get("SupervisorPhoneNumber");
        String CompanySize= Hooks.currentRow.get().get("CompanySize");
        String ThirdPartyUnion= Hooks.currentRow.get().get("ThirdPartyUnion");
        String NameOfTheUnion= Hooks.currentRow.get().get("NameOfTheUnion");
        Assert.assertTrue(eform6WorkHistory.enterJobDetails(JobTitle, StartDateDay, StartDateMonth, StartDateYear, EmpDurationYears,
                EmpDurationMonths, SupervisorFirstName, SupervisorLastName, SupervisorPhoneNumber, CompanySize, ThirdPartyUnion, NameOfTheUnion));
    }

    @And("Enter the Job Details of the Worker - Exposure Incident")
    public void enterTheJobDetailsOfTheWorkerExposureIncident() {
        String JobTitle= Hooks.currentRow.get().get("JobTitle");
        String StartDate= Hooks.currentRow.get().get("JobStartDate");
        String WhatTypeofWork= Hooks.currentRow.get().get("WhatTypeofWork");
        String CompanySize= Hooks.currentRow.get().get("CompanySize");
      eform6WorkHistory.enterJobDetails(JobTitle,StartDate, WhatTypeofWork, CompanySize);
    }


    @And("Enter Tell us about your work details")
    public void enterTellUsAboutYourWorkDetails() {
        String Relative= Hooks.currentRow.get().get("Relative");
        String TypesofEmployment= Hooks.currentRow.get().get("TypesofEmployment");
        eform6WorkHistory.enterWorkDetails(Relative, TypesofEmployment);
    }

    @And("Enter the Physical injury or illness details")
    public void enterThePhysicalInjuryOrIllnessDetails() {
        String whathappened = Hooks.currentRow.get().get("WhatHappened");
        eform6InjuryDetails.PhysicalInjuryDetails(whathappened);
    }

    @And("Enter Injury date and time")
    public void enterInjuryDateAndTime() {
        String injuryOneTimeDateDay= Hooks.currentRow.get().get("InjuryDateDay");
        String injuryOneTimeDateMonth= Hooks.currentRow.get().get("InjuryDateMonth");
        String injuryOneTimeDateYear= Hooks.currentRow.get().get("InjuryDateYear");
        eform6InjuryDetails.enterInjuryDateAndTime(injuryOneTimeDateDay, injuryOneTimeDateMonth, injuryOneTimeDateYear);
    }

    @And("Enter Areas of Injury")
    public void enterAreasOfInjury() throws InterruptedException {
        String PhysicalInj= Hooks.currentRow.get().get("PhysicalInjury");
        String Bodypart= Hooks.currentRow.get().get("BodyPart");
        eform6InjuryDetails.selectBodyPart(PhysicalInj, Bodypart);
    }

    @And("Enter Witnesses contact information")
    public void enterWitnessesContactInformation() {
        String WitnessInvolved= Hooks.currentRow.get().get("WitnessInvolved");
        String WitnessFirstName= Hooks.currentRow.get().get("WitnessFirstName");
        String WitnessLastName= Hooks.currentRow.get().get("WitnessLastName");
        String WitnessPosition= Hooks.currentRow.get().get("WitnessPosition");
        String WitnessPhoneNumber= Hooks.currentRow.get().get("WitnessPhoneNumber");
        eform6InjuryDetails.enterWitnessDetails(WitnessInvolved, WitnessFirstName, WitnessLastName, WitnessPosition, WitnessPhoneNumber);
    }

    @And("Enter Injury, illness or exposure incident address")
    public void enterInjuryIllnessOrExposureIncidentAddress() {
        eform6InjuryDetails.enterIncidentAddress();
    }

    @And("Enter Similar or related previous injury or illness details")
    public void enterSimilarOrRelatedPreviousInjuryOrIllnessDetails() {
        String SimilarInjury= Hooks.currentRow.get().get("SimilarInjury");
        String previousClaim= Hooks.currentRow.get().get("PreviousClaim");
        String previousClaimNumber = Hooks.currentRow.get().get("PreviousClaimNumber");
        String sameAreaofInjury= Hooks.currentRow.get().get("SameAreaofInjury");
        String TellUsAboutPreviousInjury= Hooks.currentRow.get().get("TellUsAboutPreviousInjury");
        eform6InjuryDetails.enterSimilarInjuryDetails(SimilarInjury, previousClaim, previousClaimNumber, sameAreaofInjury, TellUsAboutPreviousInjury);

    }

    @And("Enter Health care for injury or illness details")
    public void enterHealthCareForInjuryOrIllnessDetails() throws InterruptedException {

      String HealthCareReceived= Hooks.currentRow.get().get("HealthCareReceived");
      String Whendoyouexpect = Hooks.currentRow.get().get("Whendoyouexpect");
      String whereHealthCareReceived= Hooks.currentRow.get().get("WhereHealthCareReceived");
      String HealthCareReceivedDate= Hooks.currentRow.get().get("HealthCareReceivedDate");
      String emplReportedDate= Hooks.currentRow.get().get("EmplReportedDate");
      String reporter= Hooks.currentRow.get().get("Reporter");
      String Provider= Hooks.currentRow.get().get("Provider");
      String providerOrg= Hooks.currentRow.get().get("ProviderOrg");
      String providerOrgAddress= Hooks.currentRow.get().get("ProviderOrgAddress");
        eform6HealthCare.enterHealthCareDetails(HealthCareReceived, whereHealthCareReceived, HealthCareReceivedDate, emplReportedDate,
                reporter, Provider, providerOrg, providerOrgAddress, Whendoyouexpect);


       }

    @And("Enter Missed time from work details")
    public void enterMissedTimeFromWorkDetails() {
        String MissedTimeFromWork= Hooks.currentRow.get().get("MissedTimeFromWork");
        String MissedTimeFromWorkStartDate= Hooks.currentRow.get().get("MissedTimeStartDate");
        eform6LostTimeandEarnings.enterMissedTimeFromWorkDetails(MissedTimeFromWork, MissedTimeFromWorkStartDate);


    }

    @And("Enter Modified Work Details")
    public void enterModifiedWorkDetails() {
        String OfferedTo= Hooks.currentRow.get().get("OfferedTo");
        String ModifiedDate= Hooks.currentRow.get().get("ModifiedDate");
        String ModifiedWork= Hooks.currentRow.get().get("ModifiedWork");
        String DeclineReason= Hooks.currentRow.get().get("DeclineReason");
        eform6LostTimeandEarnings.enterModifiedWorkDetails(OfferedTo, ModifiedDate, ModifiedWork, DeclineReason);
    }

    @And("Enter Return to work details")
    public void enterReturnToWorkDetails() throws InterruptedException {
        String ReturningToWork= Hooks.currentRow.get().get("ReturningToWork");
        String ReturningDate= Hooks.currentRow.get().get("ReturningDate");
        String ReturnedDate= Hooks.currentRow.get().get("ReturnedDate");
        String TypeOfReturn= Hooks.currentRow.get().get("TypeOfReturn");
        String modifiedDate= Hooks.currentRow.get().get("ExpecttoReturn");
        String WageLoss= Hooks.currentRow.get().get("WageLoss");
        String RTWContact= Hooks.currentRow.get().get("RTWContact");
        eform6LostTimeandEarnings.enterReturnToWorkDetails(ReturningToWork, ReturningDate, TypeOfReturn, modifiedDate, WageLoss, RTWContact, ReturnedDate);
    }

    @And("Enter Additional earnings and benefits information")
    public void enterAdditionalEarningsAndBenefitsInformation() {
        String AdditionalEarnings= Hooks.currentRow.get().get("AdditionalEarnings?");
        String AdditionalBenefits= Hooks.currentRow.get().get("AdditionalBenefits?");
        eform6LostTimeandEarnings.enterAdditionalEarningsAndBenefits(AdditionalEarnings, AdditionalBenefits);
    }

    @And("Enter Authorized representative details")
    public void enterAuthorizedRepresentativeDetails() throws InterruptedException {
        eform6LostTimeandEarnings.enterAuthorizedRepDetails();
    }

    @And("Enter Submit supporting documents details")
    public void enterSubmitSupportingDocumentsDetails() throws InterruptedException {
        eform6SupportingDocuments.enterSupportingDocuments();
    }

    @And("Enter Additional Comments details")
    public void enterAdditionalCommentsDetails() {
        String AdditionalComments= Hooks.currentRow.get().get("AdditionalComments");
        eform6SupportingDocuments.enterAdditionalComments(AdditionalComments);
    }

    @And("Verify Review and submit report and Select Declaration checkbox")
    public void verifyReviewAndSubmitReportAndSelectDeclarationCheckbox() {
        Assert.assertTrue(eform6ReviewandSubmit.verifyReviewAndSubmitReport());
    }

    @And("Verify Review and submit report and Select Declaration checkbox and Submit Exposure")
    public void verifyReviewAndSubmitReportAndSelectDeclarationCheckboxSubmitExposure() {
        Assert.assertTrue(eform6ReviewandSubmit.verifyReviewAndSubmitExposure());
        eform6ReviewandSubmit.verifyReturnToDashBoard();
    }

    @And("Select decision to Add claim to your account and Submit Report")
    public void selectDecisionToAddClaimToYourAccount() throws InterruptedException {
        String AddClaimtoProfile= Hooks.currentRow.get().get("AddClaimtoProfile");
        Assert.assertTrue(eform6ReviewandSubmit.selectAddClaimtoProfile(AddClaimtoProfile));
        Assert.assertTrue(eform6ReviewandSubmit.verifyReturnToDashBoard());
    }

    @And("Enter the injury or illness details")
    public void enterTheInjuryOrIllnessDetails() {
        String conditionInput= Hooks.currentRow.get().get("ConditionInput");
        String relatedToWorkInput= Hooks.currentRow.get().get("RelatedToWorkInput");
        String symptomsInput= Hooks.currentRow.get().get("SymptomsInput");
        eform6InjuryDetails.enterInjuryDetails(conditionInput, relatedToWorkInput, symptomsInput);
    }

    @And("Enter Illness or medical condition details")
    public void enterIllnessOrMedicalConditionDetails() {
        String IllnessStartDate= Hooks.currentRow.get().get("IllnessStartDate");
        eform6InjuryDetails.enterIllnessDetails(IllnessStartDate);
    }

    @And("User should be able to Describe your Injury")
    public void userShouldBeAbleToDescribeYourInjury() {
        String DescribeInjury= Hooks.currentRow.get().get("DescribeInjury");
        eform6InjuryDetails.DescribeInjury(DescribeInjury);

    }

    @And("Enter the Mental stress psychological injury details")
    public void enterTheMentalStressPsychologicalInjuryDetails() {
        String InjuryDetailsInput= Hooks.currentRow.get().get("InjuryDetailsInput");
        eform6InjuryDetails.enterMentalInjuryDetails(InjuryDetailsInput);
    }

    @And("Enter Mental stress psychological injury date details")
    public void enterMentalStressPsychologicalInjuryDateDetails() {

        String InjuryStartDate= Hooks.currentRow.get().get("InjuryStartDate");
        String InjuryEndDate= Hooks.currentRow.get().get("InjuryEndDate");
        eform6InjuryDetails.enterMentalInjuryDateDetails(InjuryStartDate, InjuryEndDate);

    }

    @And("Enter Areas of Injury for Mental")
    public void enterAreasOfInjuryForMental() throws InterruptedException {
        String PhysicalInjury= Hooks.currentRow.get().get("PhysicalInjury?");
        eform6InjuryDetails.InjuredQuestion(PhysicalInjury);
        enterAreasOfInjury();
    }

    @And("User should be able to select {string} report type")
    public void userShouldBeAbleToSelectReportType(String reporttype) {
        eform6.selectReportType(reporttype);

    }

    @And("User should be able to select {string} Injury Type")
    public void userShouldBeAbleToSelectInjuryTypeNIHL(String Injurytype) {
        eform6.selectInjuryType(Injurytype);
    }

    @And("User should be able to land on Continue to different online service to make a claim for noise-induced hearing loss screen")
    public void userShouldBeAbleToLandOnContinueToDifferentOnlineServiceToMakeAClaimForNoiseInducedHearingLossScreen() {
        Assert.assertTrue(eform6.verifyNIHLPage());
    }

    @And("User selects Submission Requirement")
    public void userSelectsSubmissionRequirement() {
        String SubmissionRequirement= Hooks.currentRow.get().get("SubmissionRequirement");
        eform6.selectSubmissionRequirement(SubmissionRequirement);


    }

    @And("Enter Joint Health and Safety Committee details")
    public void enterJointHealthAndSafetyCommitteeDetails() {
        String SafetyCommittee= Hooks.currentRow.get().get("SafetyCommittee?");
        String SafetyRepresentative= Hooks.currentRow.get().get("SafetyRepresentative?");
        String NameOfTheUnion = Hooks.currentRow.get().get("NameOfTheUnion");
        eform6WorkHistory.enterJointHealthAndSafetyCommittee(SafetyCommittee, SafetyRepresentative, NameOfTheUnion);
    }

    @And("Enter the Exposure Incident details")
    public void enterTheExposureIncidentDetails() throws InterruptedException {
        String ExposureTo= Hooks.currentRow.get().get("ExposureTo?");
        String ExposureDate= Hooks.currentRow.get().get("ExposureDate");
        String HowContacted= Hooks.currentRow.get().get("HowContacted?");
        String SourceofExposure= Hooks.currentRow.get().get("Source");
        String WhatSubstance= Hooks.currentRow.get().get("WhatSubstance");
        String DescribeWhere= Hooks.currentRow.get().get("DescribeWhere?");
        String WhatareYouWearing= Hooks.currentRow.get().get("WhatareYouWearing?");
        eform6ExposureDetails.enterExposureDetails(ExposureTo, ExposureDate, HowContacted, SourceofExposure, WhatSubstance, DescribeWhere, WhatareYouWearing);


    }


}
