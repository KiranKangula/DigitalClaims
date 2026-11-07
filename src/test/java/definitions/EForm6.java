package definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.DynamicIntakeEform6.*;
import pages.OSPC.*;
import utils.ConfigReader;
import utils.JsonUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static utils.BaseDriverClass.getCurrentDate;

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
        OSPCClaimOverview = new OSPCClaimOverview(driver, wait);
    }

    @When("User click on Submit Report button")
    public void userClickOnSubmitReportButton() {
        Hooks.log("Clicking on 'Submit Report' button.");
        Assert.assertTrue(eform6.verifySubmitReportButtonDisplayed());
        Assert.assertTrue(eform6.clickSubmitReportButton());

    }

    @Then("User should be able to provide consent to the privacy statement")
    public void userShouldBeAbleToProvideConsentToThePrivacyStatement() {
        Hooks.log("Providing consent to the privacy statement.");
        Assert.assertTrue(eform6.clickPrivacyStatement());
    }

    @And("User should be able to select report type {string}")
    public void userShouldBeAbleToSelectreporttype(String reporttype) {
        reporttype = Hooks.currentRow.get().get("ReportType");
        Hooks.log("Selecting report type: " + reporttype);
        eform6.selectReportType(reporttype);
    }

    @And("User should be able to select Injury Type {string}")
    public void userShouldBeAbleToSelectInjuryType(String injurytype) {
        injurytype = Hooks.currentRow.get().get("InjuryType");
        Hooks.log("Selecting Injury Type: " + injurytype);
        eform6.selectInjuryType(injurytype);

    }

    @And("User should be able to select radio {string}, Body Part {string}")
    public void userShouldBeAbleToSelectRadioBodyPart(String PhysicalInj, String Bodypart) {
        PhysicalInj = Hooks.currentRow.get().get("PhyInjury");
        Bodypart = Hooks.currentRow.get().get("DescribeInjury");
        Hooks.log("Selecting Physical Injury: " + PhysicalInj + ", Body Part: " + Bodypart);
        Assert.assertTrue(eform6.selectBodyPart(PhysicalInj, Bodypart));
    }

    @And("User should be able to enter {string} if applicable")
    public void userShouldBeAbleToEnterIfApplicable(String existingClaimnumber) {
        existingClaimnumber = Hooks.currentRow.get().get("ExistingClaimNumber");
        Hooks.log("Entering existing claim number (if applicable): " + existingClaimnumber);
        Assert.assertTrue(eform6.enterExistingClaimNumber(existingClaimnumber));

    }

    @And("User should be able to enter {string}, {string}, {string}, {string}")
    public void userShouldBeAbleToEnter(String FirstName, String lastname, String Phonenumber, String Pronouns) throws InterruptedException {
        FirstName = Hooks.currentRow.get().get("FirstName");
        lastname = Hooks.currentRow.get().get("LastName");
        Phonenumber = Hooks.currentRow.get().get("PhoneNumber");
        Pronouns = Hooks.currentRow.get().get("Pronouns");
        Hooks.log("Entering personal information for: " + FirstName + " " + lastname);
        Assert.assertTrue(eform6.enterPersonalInformation(FirstName, lastname, Phonenumber, Pronouns));

    }

    @And("Enter Additional personal details")
    public void enterAdditionalDetailsLike() throws InterruptedException {
       String gender= Hooks.currentRow.get().get("Gender");
        String DateofBirth= Hooks.currentRow.get().get("DateofBirth");
        String  PreferredLang= Hooks.currentRow.get().get("PreferredLanguage");
        String  SIN= Hooks.currentRow.get().get("sinNumber");
        Hooks.log("Entering additional personal details: Gender=" + gender + ", DOB=" + DateofBirth);
        Assert.assertTrue(eform6.enterAdditionalDetails(gender, DateofBirth, PreferredLang, SIN));

    }

    @And("Enter Additional personal details - Exposure")
    public void enterAdditionalDetailsExposure() throws InterruptedException {
        String gender= Hooks.currentRow.get().get("Gender");
        String DateofBirth= Hooks.currentRow.get().get("DateofBirth");
        Hooks.log("Entering additional details for exposure: Gender=" + gender + ", DOB=" + DateofBirth);
        Assert.assertTrue(eform6.enterAdditionalDetails(gender, DateofBirth));

    }

    @And("Enter Worker Address Details")
    public void enterWorkerAddressDetails() throws InterruptedException {
        String Address= Hooks.currentRow.get().get("Address");
        Hooks.log("Entering worker address details: " + Address);
        Assert.assertTrue(eform6.enterMailingAddress(Address));
    }

    @And("Enter Employers Details")
    public void enterEmployerDetails() throws InterruptedException {
       String Employername= Hooks.currentRow.get().get("EmployerName");
        String EmpPhone= Hooks.currentRow.get().get("EmployerPhone");
        String EmployerAddress= Hooks.currentRow.get().get("EmployerAddress");
        Hooks.log("Entering employer details: " + Employername);
        eform6.enterEmployerDetails(Employername, EmpPhone, EmployerAddress);
    }

    @Then("Verify the reference number is generated and displayed")
    public void verifyTheReferenceNumberIsGeneratedAndDisplayed() {
       String referenceClaimNumber = eform6.verifyReferenceNumber();
       Hooks.log("Reference Claim Number is: " + referenceClaimNumber);
         Assert.assertTrue(referenceClaimNumber.length()>0);
        // Update the current row data with the new values
        Map<String, String> currentRowData = Hooks.currentRow.get();
        currentRowData.put("ReferenceNumber", referenceClaimNumber);

        String sheetName = Hooks.sheetName.get();
        JsonUtils.writeJsonData(sheetName, Hooks.data.get());
        Hooks.currentRow.set(currentRowData);
    }

    @And("Enter the Job Details of the Worker")
    public void enterTheJobDetailsOfTheWorker() {
        String JobTitle= Hooks.currentRow.get().get("JobTitle");
        String StartDate= Hooks.currentRow.get().get("JobStartDate");
        String EmpDurationYears= Hooks.currentRow.get().get("EmpDurationYears");
        String EmpDurationMonths= Hooks.currentRow.get().get("EmpDurationMonths");
        String SupervisorFirstName= Hooks.currentRow.get().get("SupervisorFirstName");
        String SupervisorLastName= Hooks.currentRow.get().get("SupervisorLastName");
        String SupervisorPhoneNumber= Hooks.currentRow.get().get("SupervisorPhoneNumber");
        String CompanySize= Hooks.currentRow.get().get("CompanySize");
        String ThirdPartyUnion= Hooks.currentRow.get().get("ThirdPartyUnion");
        String NameOfTheUnion= Hooks.currentRow.get().get("NameOfTheUnion");
        Hooks.log("Entering job details: Title=" + JobTitle + ", Start Date=" + StartDate);
        Assert.assertTrue(eform6WorkHistory.enterJobDetails(JobTitle, StartDate, EmpDurationYears,
                EmpDurationMonths, SupervisorFirstName, SupervisorLastName, SupervisorPhoneNumber, CompanySize, ThirdPartyUnion, NameOfTheUnion));
    }

    @And("Enter the Job Details of the Worker - Exposure Incident")
    public void enterTheJobDetailsOfTheWorkerExposureIncident() {
        String JobTitle= Hooks.currentRow.get().get("JobTitle");
        String StartDate= Hooks.currentRow.get().get("JobStartDate");
        String WhatTypeofWork= Hooks.currentRow.get().get("WhatTypeofWork");
        String CompanySize= Hooks.currentRow.get().get("CompanySize");
        Hooks.log("Entering job details for exposure: Title=" + JobTitle);
      eform6WorkHistory.enterJobDetails(JobTitle,StartDate, WhatTypeofWork, CompanySize);
    }


    @And("Enter Tell us about your work details")
    public void enterTellUsAboutYourWorkDetails() {
        String Relative= Hooks.currentRow.get().get("Relative");
        String TypesofEmployment= Hooks.currentRow.get().get("TypesofEmployment");
        Hooks.log("Entering work details: Employment Type=" + TypesofEmployment);
        eform6WorkHistory.enterWorkDetails(Relative, TypesofEmployment);
    }

    @And("Enter the Physical injury or illness details")
    public void enterThePhysicalInjuryOrIllnessDetails() {
        String whathappened = Hooks.currentRow.get().get("WhatHappened");
        Hooks.log("Entering physical injury details: " + whathappened);
        eform6InjuryDetails.PhysicalInjuryDetails(whathappened);
    }

    @And("Enter Injury date and time")
    public void enterInjuryDateAndTime() {
        String injuryOneTimeDate= Hooks.currentRow.get().get("InjuryDate");
        Hooks.log("Entering injury date and time: " + injuryOneTimeDate);
        eform6InjuryDetails.enterInjuryDateAndTime(injuryOneTimeDate);
    }

    @And("Enter Areas of Injury")
    public void enterAreasOfInjury() throws InterruptedException {
        String PhysicalInj= Hooks.currentRow.get().get("PhysicalInjury");
        String Bodypart= Hooks.currentRow.get().get("BodyPart");
        Hooks.log("Entering areas of injury: " + PhysicalInj + " / " + Bodypart);
        eform6InjuryDetails.selectBodyPart(PhysicalInj, Bodypart);
    }

    @And("Enter Date and Time Injury Reported to Employer")
    public void enterInjuryReportedtoEmployer() throws InterruptedException {
        String ReportedtoEmp= Hooks.currentRow.get().get("ReportedtoEmp");
        String InjurDateReptoEmployer= Hooks.currentRow.get().get("InjurDateReptoEmployer");
        Hooks.log("Entering Date and contact information of reporting injury or illness to employer: " + ReportedtoEmp + " / " + InjurDateReptoEmployer);
        eform6InjuryDetails.selectReportedToEmpl(ReportedtoEmp, InjurDateReptoEmployer);
    }

    @And("Enter Witnesses contact information")
    public void enterWitnessesContactInformation() {
        String WitnessInvolved= Hooks.currentRow.get().get("WitnessInvolved");
        String WitnessFirstName= Hooks.currentRow.get().get("WitnessFirstName");
        String WitnessLastName= Hooks.currentRow.get().get("WitnessLastName");
        String WitnessPosition= Hooks.currentRow.get().get("WitnessPosition");
        String WitnessPhoneNumber= Hooks.currentRow.get().get("WitnessPhoneNumber");
        Hooks.log("Entering witness contact information if involved: " + WitnessInvolved);
        eform6InjuryDetails.enterWitnessDetails(WitnessInvolved, WitnessFirstName, WitnessLastName, WitnessPosition, WitnessPhoneNumber);
    }

    @And("Enter Injury, illness or exposure incident address")
    public void enterInjuryIllnessOrExposureIncidentAddress() {
        Hooks.log("Entering incident address.");
        eform6InjuryDetails.enterIncidentAddress();
    }

    @And("Enter Similar or related previous injury or illness details")
    public void enterSimilarOrRelatedPreviousInjuryOrIllnessDetails() {
        String SimilarInjury= Hooks.currentRow.get().get("SimilarInjury");
        String previousClaim= Hooks.currentRow.get().get("PreviousClaim");
        String previousClaimNumber = Hooks.currentRow.get().get("PreviousClaimNumber");
        String sameAreaofInjury= Hooks.currentRow.get().get("SameAreaofInjury");
        String TellUsAboutPreviousInjury= Hooks.currentRow.get().get("TellUsAboutPreviousInjury");
        Hooks.log("Entering similar previous injury details.");
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
        Hooks.log("Entering health care details for injury.");
        eform6HealthCare.enterHealthCareDetails(HealthCareReceived, whereHealthCareReceived, HealthCareReceivedDate, emplReportedDate,
                reporter, Provider, providerOrg, providerOrgAddress, Whendoyouexpect);


       }

    @And("Enter Missed time from work details")
    public void enterMissedTimeFromWorkDetails() {
        String MissedTimeFromWork= Hooks.currentRow.get().get("MissedTimeFromWork");
        String MissedTimeFromWorkStartDate= Hooks.currentRow.get().get("MissedTimeStartDate");
        Hooks.log("Entering missed time details: " + MissedTimeFromWork);
        eform6LostTimeandEarnings.enterMissedTimeFromWorkDetails(MissedTimeFromWork, MissedTimeFromWorkStartDate);


    }

    @And("Enter Modified Work Details")
    public void enterModifiedWorkDetails() {
        String OfferedTo= Hooks.currentRow.get().get("OfferedTo");
        String ModifiedDate= Hooks.currentRow.get().get("ModifiedDate");
        String ModifiedWork= Hooks.currentRow.get().get("ModifiedWork");
        String DeclineReason= Hooks.currentRow.get().get("DeclineReason");
        Hooks.log("Entering modified work details.");
        eform6LostTimeandEarnings.enterModifiedWorkDetails(OfferedTo, ModifiedDate, ModifiedWork, DeclineReason);
    }

    @And("Enter Return to work details")
    public void enterReturnToWorkDetails() throws InterruptedException {
        String nextYearDate = LocalDate.parse(getCurrentDate("yyyy-MM-dd"))
                .plusYears(1)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String ReturningToWork= Hooks.currentRow.get().get("ReturningToWork");
        String ReturningDate= Hooks.currentRow.get().get("ReturningDate");
        String ReturnedDate= Hooks.currentRow.get().get("ReturnedDate");
        String TypeOfReturn= Hooks.currentRow.get().get("TypeOfReturn");
        String modifiedDate= nextYearDate;
        String WageLoss= Hooks.currentRow.get().get("WageLoss");
        String RTWContact= Hooks.currentRow.get().get("RTWContact");
        Hooks.log("Entering return to work details: Returning=" + ReturningToWork);
        eform6LostTimeandEarnings.enterReturnToWorkDetails(ReturningToWork, ReturningDate, TypeOfReturn, modifiedDate, WageLoss, RTWContact, ReturnedDate);
    }

    @And("Enter Additional earnings and benefits information")
    public void enterAdditionalEarningsAndBenefitsInformation() {
        String AdditionalEarnings= Hooks.currentRow.get().get("AdditionalEarnings?");
        String AdditionalBenefits= Hooks.currentRow.get().get("AdditionalBenefits?");
        String DidYouWorkforMorethanOneEmpl = Hooks.currentRow.get().get("DidYouWorkforMorethanOneEmpl?");
        Hooks.log("Entering additional earnings and benefits.");
        eform6LostTimeandEarnings.enterAdditionalEarningsAndBenefits(AdditionalEarnings, AdditionalBenefits, DidYouWorkforMorethanOneEmpl);
    }

    @And("Enter Authorized representative details")
    public void enterAuthorizedRepresentativeDetails() throws InterruptedException {
        Hooks.log("Entering authorized representative details.");
        eform6LostTimeandEarnings.enterAuthorizedRepDetails();
    }

    @And("Enter Submit supporting documents details")
    public void enterSubmitSupportingDocumentsDetails() throws InterruptedException {
        Hooks.log("Entering supporting documents details.");
        eform6SupportingDocuments.enterSupportingDocuments();
    }

    @And("Enter Additional Comments details")
    public void enterAdditionalCommentsDetails() {
        String AdditionalComments= Hooks.currentRow.get().get("AdditionalComments");
        Hooks.log("Entering additional comments.");
        eform6SupportingDocuments.enterAdditionalComments(AdditionalComments);
    }

    @And("Verify Review and submit report and Select Declaration checkbox")
    public void verifyReviewAndSubmitReportAndSelectDeclarationCheckbox() {
        Hooks.log("Verifying Review and submit report screen and selecting Declaration.");
        Assert.assertTrue(eform6ReviewandSubmit.verifyReviewAndSubmitReport());
    }

    @And("Verify Review and submit report and Select Declaration checkbox and Submit Exposure")
    public void verifyReviewAndSubmitReportAndSelectDeclarationCheckboxSubmitExposure() {
        Hooks.log("Verifying Review and submit for Exposure and submitting.");
        Assert.assertTrue(eform6ReviewandSubmit.verifyReviewAndSubmitExposure());
        eform6ReviewandSubmit.verifyReturnToDashBoard();
        Hooks.log("Report Submitted at: " + getCurrentDate("MMM dd, yyyy hh:mm:ss a"));
    }

    @And("Select decision to Add claim to your account and Submit Report")
    public void selectDecisionToAddClaimToYourAccount() throws InterruptedException {
        String AddClaimtoProfile= Hooks.currentRow.get().get("AddClaimtoProfile");
        Hooks.log("Selecting decision to add claim to profile: " + AddClaimtoProfile);
        Assert.assertTrue(eform6ReviewandSubmit.selectAddClaimtoProfile(AddClaimtoProfile));
        Assert.assertTrue(eform6ReviewandSubmit.verifyReturnToDashBoard());
        Hooks.log("Report Submitted at: " + getCurrentDate("MMM dd, yyyy hh:mm:ss a"));
    }

    @And("Enter the injury or illness details")
    public void enterTheInjuryOrIllnessDetails() {
        String conditionInput= Hooks.currentRow.get().get("ConditionInput");
        String relatedToWorkInput= Hooks.currentRow.get().get("RelatedToWorkInput");
        String symptomsInput= Hooks.currentRow.get().get("SymptomsInput");
        Hooks.log("Entering injury or illness details.");
        eform6InjuryDetails.enterInjuryDetails(conditionInput, relatedToWorkInput, symptomsInput);
    }

    @And("Enter Illness or medical condition details")
    public void enterIllnessOrMedicalConditionDetails() {
        String IllnessStartDate= Hooks.currentRow.get().get("IllnessStartDate");
        Hooks.log("Entering illness details: Start Date=" + IllnessStartDate);
        eform6InjuryDetails.enterIllnessDetails(IllnessStartDate);
    }

    @And("User should be able to Describe your Injury")
    public void userShouldBeAbleToDescribeYourInjury() {
        String DescribeInjury= Hooks.currentRow.get().get("DescribeInjury");
        Hooks.log("Describing injury: " + DescribeInjury);
        eform6InjuryDetails.DescribeInjury(DescribeInjury);

    }

    @And("Enter the Mental stress psychological injury details")
    public void enterTheMentalStressPsychologicalInjuryDetails() {
        String InjuryDetailsInput= Hooks.currentRow.get().get("InjuryDetailsInput");
        Hooks.log("Entering mental stress injury details.");
        eform6InjuryDetails.enterMentalInjuryDetails(InjuryDetailsInput);
    }

    @And("Enter Mental stress psychological injury date details")
    public void enterMentalStressPsychologicalInjuryDateDetails() {

        String InjuryStartDate= Hooks.currentRow.get().get("InjuryStartDate");
        String InjuryEndDate= Hooks.currentRow.get().get("InjuryEndDate");
        Hooks.log("Entering mental stress injury dates: " + InjuryStartDate + " to " + InjuryEndDate);
        eform6InjuryDetails.enterMentalInjuryDateDetails(InjuryStartDate, InjuryEndDate);

    }

    @And("Enter Areas of Injury for Mental")
    public void enterAreasOfInjuryForMental() throws InterruptedException {
        String PhysicalInjury= Hooks.currentRow.get().get("PhysicalInjury?");
        Hooks.log("Entering areas of injury for mental stress. Physical Injury Involved?=" + PhysicalInjury);
        eform6InjuryDetails.InjuredQuestion(PhysicalInjury);
        enterAreasOfInjury();
    }

    @And("User should be able to select {string} report type")
    public void userShouldBeAbleToSelectReportType(String reporttype) {
        Hooks.log("Selecting report type: " + reporttype);
        eform6.selectReportType(reporttype);

    }

    @And("User should be able to select {string} Injury Type")
    public void userShouldBeAbleToSelectInjuryTypeNIHL(String Injurytype) {
        Hooks.log("Selecting injury type: " + Injurytype);
        eform6.selectInjuryType(Injurytype);
    }

    @And("User should be able to land on Continue to different online service to make a claim for noise-induced hearing loss screen")
    public void userShouldBeAbleToLandOnContinueToDifferentOnlineServiceToMakeAClaimForNoiseInducedHearingLossScreen() {
        Hooks.log("Verifying landing on NIHL screen.");
        Assert.assertTrue(eform6.verifyNIHLPage());
    }

    @And("User selects Submission Requirement")
    public void userSelectsSubmissionRequirement() {
        String SubmissionRequirement= Hooks.currentRow.get().get("SubmissionRequirement");
        Hooks.log("Selecting submission requirement: " + SubmissionRequirement);
        eform6.selectSubmissionRequirement(SubmissionRequirement);


    }

    @And("Enter Joint Health and Safety Committee details")
    public void enterJointHealthAndSafetyCommitteeDetails() {
        String SafetyCommittee= Hooks.currentRow.get().get("SafetyCommittee?");
        String SafetyRepresentative= Hooks.currentRow.get().get("SafetyRepresentative?");
        String NameOfTheUnion = Hooks.currentRow.get().get("NameOfTheUnion");
        Hooks.log("Entering Joint Health and Safety Committee details.");
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
        Hooks.log("Entering exposure incident details.");
        eform6ExposureDetails.enterExposureDetails(ExposureTo, ExposureDate, HowContacted, SourceofExposure, WhatSubstance, DescribeWhere, WhatareYouWearing);


    }

    @And("Upload and verify Form 6 supporting documents")
    public void uploadAndVerifyForm6SupportingDocuments() {
        Map<String, String> row = Hooks.currentRow.get();
        if (row == null) {
            throw new IllegalStateException("Form 6 scenario data is not initialized.");
        }
        String files = row.get("SupportingFiles");
        String documentTypes = row.get("SupportingDocumentTypes");
        if (files == null || files.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "The upload scenario requires SupportingFiles in the selected JSON row.");
        }
        if (documentTypes == null || documentTypes.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "The upload scenario requires actual SupportingDocumentTypes dropdown labels.");
        }
        Hooks.log("[FORM 6 DOCUMENTS] Uploading and verifying: " + files);
        java.util.List<String> verified = eform6SupportingDocuments.enterSupportingDocuments(
                files, documentTypes, prop.getProperty("upload.folder"));
        row.put("VerifiedSupportingFiles", String.join("|", verified));
        Hooks.log("[FORM 6 DOCUMENTS] All configured files were verified in Your submitted documents: "
                + String.join(" | ", verified));
    }


}
