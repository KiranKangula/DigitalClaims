package definitions;

import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.NIHL.*;
import pages.OSPC.HomePage;
import pages.OSPC.LoginPage;
import utils.ConfigReader;
import utils.JsonUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static utils.BaseDriverClass.generateRandomText;
import static utils.BaseDriverClass.getCurrentDate;

public class NIHL {
	private WebDriver driver;
	private WebDriverWait wait;
	private LoginPage loginPage;
	private HomePage homePage;
	private NIHLLandingPage nihllandingpage;
	private MyInformation myinformation;
	private HealthCare healthcare;
	private Employement employement;
	private SupportingDocuments	supportingdocuments;
	private ReviewandConfirm reviewandconfirm;
	ConfigReader prop= Hooks.getConfigReader();
	ConfigReader common = Hooks.getCommonReader();
	public NIHL() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(common.getProperty("timeout"))));
		loginPage = new LoginPage(driver, wait);
		nihllandingpage = new NIHLLandingPage(driver, wait);
		myinformation = new MyInformation(driver, wait);
		healthcare = new HealthCare(driver, wait);
		employement = new Employement(driver, wait);
		supportingdocuments = new SupportingDocuments(driver, wait);
		reviewandconfirm = new ReviewandConfirm(driver, wait);
		homePage = new HomePage(driver, wait);
	}

	@Given("User is on NIHL landing page")
	public void userIsOnNIHLLandingPage() throws InterruptedException {
		Hooks.log("Verifying user is on NIHL landing page.");
		Assert.assertTrue(nihllandingpage.verifyOnNIHLLandingPage());

	}


	@When("User clicks on Gradual over a period of a few years radio button")
	public void userClicksOnGradualOverAPeriodOfAFewYearsRadioButton() {
		Hooks.log("Clicking on 'Gradual over a period of a few years' radio button.");
		Assert.assertTrue(nihllandingpage.ClickGradualRadioButton());
	}

	@Then("User should be able to enter the Email address and Date of birth and click on Start Claim button")
	public void userShouldBeAbleToEnterTheEmailAddressAndDateOfBirthAndClickOnStartClaimButton() {
		String email = Hooks.currentRow.get().get("Email");
		String dob = Hooks.currentRow.get().get("dateOfBirth");
		Hooks.log("Entering Email address: " + email + " and Date of birth: " + dob + " and clicking Start Claim button.");
		nihllandingpage.EnterUserDetails(email, dob);
	}

	@And("Enter data in My Information Screen")
	public void enterDataInMyInformationScreen() {
		String firstname = Hooks.currentRow.get().get("First Name");
		String lastname = Hooks.currentRow.get().get("Last Name");
		String SIN = Hooks.currentRow.get().get("Social Insurance Number");
		String address = Hooks.currentRow.get().get("Address");
		String phone = Hooks.currentRow.get().get("Primary Phone");
		String Language = Hooks.currentRow.get().get("Language Preference");


		// Manipulate the first name and last name
		String manipulatedFirstName = "KKFirst"+ generateRandomText(5) ;
		String manipulatedLastName = "KKLast"+ generateRandomText(5);
		Hooks.log("Manipulating First Name to: " + manipulatedFirstName + " and Last Name to: " + manipulatedLastName);

		// Update the current row data in memory
		Hooks.currentRow.get().put("First Name", manipulatedFirstName);
		Hooks.currentRow.get().put("Last Name", manipulatedLastName);
		
		// Update the JSON file with the manipulated names
		JsonUtils.writeJsonData(Hooks.sheetName.get(), Hooks.data.get());
		Hooks.log("Updated JSON data with manipulated names.");

        myinformation.EnterMyInformation(manipulatedFirstName, manipulatedLastName, SIN, address, phone, Language);
        Hooks.log("Entered data in My Information Screen.");
	}

	@And("Enter data in Health care Screen")
	public void enterDataInHealthCareScreen() throws InterruptedException {
		String HearingLossNoticedYear= Hooks.currentRow.get().get("HearingLossNoticedYear");
		String wearHearingAid= Hooks.currentRow.get().get("wearHearingAid");
		String hearingAidUsageDate= Hooks.currentRow.get().get("hearingAidUsageDate");
		String dateOfFirstAudiogram= Hooks.currentRow.get().get("dateOfFirstAudiogram");
		String audiogramClinicName= Hooks.currentRow.get().get("audiogramClinicName");
		String audiogramClinicAddress= Hooks.currentRow.get().get("audiogramClinicAddress");
		String audiogramClinicPhoneNumber= Hooks.currentRow.get().get("audiogramClinicPhoneNumber");
		String hasVisitedEntSpecialist= Hooks.currentRow.get().get("hasVisitedEntSpecialist");
		String entAppointmentDate= Hooks.currentRow.get().get("entAppointmentDate");
		String entSpecialistName= Hooks.currentRow.get().get("entSpecialistName");
		String entSpecialistAddress= Hooks.currentRow.get().get("entSpecialistAddress");
		String entSpecialistPhoneNumber= Hooks.currentRow.get().get("entSpecialistPhoneNumber");
		String experienceRingingQuestion= Hooks.currentRow.get().get("experienceRingingQuestion");
		String ringingEarDuration= Hooks.currentRow.get().get("ringingEarDuration");
		String hasSevereRingingInEar= Hooks.currentRow.get().get("hasSevereRingingInEar");
		String isRingingConstant= Hooks.currentRow.get().get("isRingingConstant");
		Hooks.log("Entering data in Health Care Screen.");
		healthcare.EnterHealthCareDetails(HearingLossNoticedYear,wearHearingAid,hearingAidUsageDate,dateOfFirstAudiogram,
				audiogramClinicName,audiogramClinicAddress,audiogramClinicPhoneNumber,hasVisitedEntSpecialist,entAppointmentDate,entSpecialistName,
				entSpecialistAddress,entSpecialistPhoneNumber,experienceRingingQuestion,ringingEarDuration,hasSevereRingingInEar,isRingingConstant);

	}

	@And("Enter data in Employment Screen")
	public void enterDataInEmploymentScreen() throws InterruptedException {
		Map<String, String> row = Hooks.currentRow.get();
		String Areyouretired= row.getOrDefault("Are you retired?", "");
		String RetirementDate = row.getOrDefault("RetirementDate", "");
		String EmployedRadio= row.getOrDefault("Are you currently employed?", "");
		String EmployerName= row.getOrDefault("EmployerName", "");
		String EmployerAddress= row.getOrDefault("EmployerAddress", "");
		String EmployerPhone= row.getOrDefault("EmployerPhone", "");
		String HazardousNoiseRadio= row.getOrDefault("HazardousNoiseRadio", "");
		String SelfEmployedRadio= row.getOrDefault("SelfEmployedRadio", "");
		String InsuranceRadio= row.getOrDefault("InsuranceRadio", "");
		String BusinessName= row.getOrDefault("BusinessName", "");
		String BusinessAddress= row.getOrDefault("BusinessAddress", "");
		String BusinessFrom= row.getOrDefault("BusinessFrom", "");
		String BusinessTo= row.getOrDefault("BusinessTo", "");
		String NoisyMachineryOffwork= row.getOrDefault("noisyMachineryOffwork", "");
		String UsedNoisyMachinery= row.getOrDefault("usedNoisyMachinery", "");
		String StillInBusiness= row.getOrDefault("StillInBusiness", "");
		String JobTitle= row.getOrDefault("JobTitle", "");
		String EmployedFrom= row.getOrDefault("EmployedFrom", "");
		String EmployedTo= row.getOrDefault("EmployedTo", "");
		String ToolsEquipmentUsed= row.getOrDefault("ToolsEquipmentUsed", "");
		String HoursUsed= row.getOrDefault("HoursUsed", "");
		Hooks.log("Entering data in Employment Screen.");
		employement.EnterEmploymentDetails(Areyouretired,RetirementDate, EmployedRadio,EmployerName,EmployerAddress,EmployerPhone,
				HazardousNoiseRadio,SelfEmployedRadio,InsuranceRadio,BusinessName,BusinessAddress,BusinessFrom,BusinessTo,
				NoisyMachineryOffwork,UsedNoisyMachinery,StillInBusiness,JobTitle,EmployedFrom,EmployedTo,ToolsEquipmentUsed,HoursUsed);

	}

	@And("Enter data in Supporting Documents Screen")
	public void enterDataInSupportingDocumentsScreen()
			throws InterruptedException {

		Map<String, String> row = Hooks.currentRow.get();

		if (row == null) {
			throw new IllegalStateException(
					"NIHL test data is not initialized for this scenario.");
		}

		String healthCareFiles =
				row.getOrDefault("HealthCareFiles", "");

		String employmentFiles =
				row.getOrDefault("EmploymentFiles", "");

		String uploadFolder = prop.getProperty("upload.folder");

		Hooks.log("Entering data in Supporting Documents Screen.");

		supportingdocuments.EnterSupportingDocuments(
				healthCareFiles,
				employmentFiles,
				uploadFolder);
	}

	@And("Verify the data in Review screen and Submit the claim")
	public void verifyTheDataInReviewScreenAndSubmitTheClaim() throws InterruptedException {
		Hooks.log("Verifying data in Review screen and submitting the claim.");
		reviewandconfirm.reviewandSubmitNIHLClaim();
	}

	@And("Verify the claim is submitted successfully")
	public void verifyTheClaimIsSubmittedSuccessfully() {
		Hooks.log("Verifying claim submission success.");
		Assert.assertTrue(reviewandconfirm.verifyClaimSubmitted());
		Hooks.log("Claim Submitted at: " + getCurrentDate("MMM dd, yyyy hh:mm:ss a"));
	}


	@And("Enter data in Supporting Documents Screen with health care files {string} and employment files {string}")
	public void enterSupportingDocumentsWithFiles(
			String healthCareFiles,
			String employmentFiles) throws InterruptedException {

		Map<String, String> row = Hooks.currentRow.get();

		if (row == null) {
			throw new IllegalStateException(
					"NIHL test data is not initialized for this scenario.");
		}

		// Always capture a fresh date for this run—including no-document cases.
		row.put("NIHLSubmissionDate", getCurrentDate("yyyy-MM-dd"));
		// Retain this scenario's actual upload inputs for GWCC verification.
		row.put("HealthCareFiles",
				healthCareFiles == null ? "" : healthCareFiles.trim());
		row.put("EmploymentFiles",
				employmentFiles == null ? "" : employmentFiles.trim());
		Hooks.log("[NIHL] Captured submission date: "
				+ row.get("NIHLSubmissionDate"));

		String healthFiles = healthCareFiles == null
				? "" : healthCareFiles.trim();

		String employerFiles = employmentFiles == null
				? "" : employmentFiles.trim();

		String uploadFolder = prop.getProperty("upload.folder");

		Hooks.log("Health care files: " + healthFiles);
		Hooks.log("Employment files: " + employerFiles);

		supportingdocuments.EnterSupportingDocuments(
				healthFiles,
				employerFiles,
				uploadFolder);

		// Retain this example's actual upload inputs for GWCC verification.
		// This changes scenario data in memory; it does not write the JSON file.
		row.put("HealthCareFiles", healthFiles);
		row.put("EmploymentFiles", employerFiles);
	}
}
