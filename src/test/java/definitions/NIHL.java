package definitions;

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
import pages.WT.TravelExpense;
import pages.WT.WTLandingPage;
import utils.BaseDriverClass;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static utils.BaseDriverClass.generateRandomText;

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
	public NIHL() throws Exception {
		driver = Hooks.getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(prop.getProperty("timeout"))));
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
	public void userIsOnLandingPage() throws InterruptedException {
		Assert.assertTrue(nihllandingpage.verifyOnNIHLLandingPage());

	}


	@When("User clicks on Gradual over a period of a few years radio button")
	public void userClicksOnGradualOverAPeriodOfAFewYearsRadioButton() {
		Assert.assertTrue(nihllandingpage.ClickGradualRadioButton());
	}

	@Then("User should be able to enter the Email address and Date of birth and click on Start Claim button")
	public void userShouldBeAbleToEnterTheEmailAddressAndDateOfBirthAndClickOnStartClaimButton() {
		String email = Hooks.currentRow.get().get("Email");
		String dob = Hooks.currentRow.get().get("DateofBirth");
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

		// Update the Excel sheet with the manipulated names
		Map<String, String> updatedRowData = new HashMap<>();
		updatedRowData.put("First Name", manipulatedFirstName);
		updatedRowData.put("Last Name", manipulatedLastName);
		try {
			ExcelUtils.updateExcelData(Hooks.sheetName, updatedRowData);
		} catch (IOException e) {
            throw new RuntimeException(e);
        }


        myinformation.EnterMyInformation(firstname, lastname, SIN, address, phone, Language);
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
		healthcare.EnterHealthCareDetails(HearingLossNoticedYear,wearHearingAid,hearingAidUsageDate,dateOfFirstAudiogram,
				audiogramClinicName,audiogramClinicAddress,audiogramClinicPhoneNumber,hasVisitedEntSpecialist,entAppointmentDate,entSpecialistName,
				entSpecialistAddress,entSpecialistPhoneNumber,experienceRingingQuestion,ringingEarDuration,hasSevereRingingInEar,isRingingConstant);

	}

	@And("Enter data in Employment Screen")
	public void enterDataInEmploymentScreen() throws InterruptedException {
		String Areyouretired= Hooks.currentRow.get().get("Are you retired?");
		String RetirementDate = Hooks.currentRow.get().get("RetirementDate");
		String EmployedRadio= Hooks.currentRow.get().get("Are you currently employed?");
		String EmployerName= Hooks.currentRow.get().get("EmployerName");
		String EmployerAddress= Hooks.currentRow.get().get("EmployerAddress");
		String EmployerPhone= Hooks.currentRow.get().get("EmployerPhone");
		String HazardousNoiseRadio= Hooks.currentRow.get().get("HazardousNoiseRadio");
		String SelfEmployedRadio= Hooks.currentRow.get().get("SelfEmployedRadio");
		String InsuranceRadio= Hooks.currentRow.get().get("InsuranceRadio");
		String BusinessName= Hooks.currentRow.get().get("BusinessName");
		String BusinessAddress= Hooks.currentRow.get().get("BusinessAddress");
		String BusinessFrom= Hooks.currentRow.get().get("BusinessFrom");
		String BusinessTo= Hooks.currentRow.get().get("BusinessTo");
		String NoisyMachineryOffwork= Hooks.currentRow.get().get("noisyMachineryOffwork");
		String UsedNoisyMachinery= Hooks.currentRow.get().get("usedNoisyMachinery");
		String StillInBusiness= Hooks.currentRow.get().get("StillInBusiness");
		String JobTitle= Hooks.currentRow.get().get("JobTitle");
		String EmployedFrom= Hooks.currentRow.get().get("EmployedFrom");
		String EmployedTo= Hooks.currentRow.get().get("EmployedTo");
		String ToolsEquipmentUsed= Hooks.currentRow.get().get("ToolsEquipmentUsed");
		String HoursUsed= Hooks.currentRow.get().get("HoursUsed");
		employement.EnterEmploymentDetails(Areyouretired,RetirementDate, EmployedRadio,EmployerName,EmployerAddress,EmployerPhone,
				HazardousNoiseRadio,SelfEmployedRadio,InsuranceRadio,BusinessName,BusinessAddress,BusinessFrom,BusinessTo,
				NoisyMachineryOffwork,UsedNoisyMachinery,StillInBusiness,JobTitle,EmployedFrom,EmployedTo,ToolsEquipmentUsed,HoursUsed);

	}

	@And("Enter data in Supporting Documents Screen")
	public void enterDataInSupportingDocumentsScreen() throws InterruptedException {
		supportingdocuments.EnterSupportingDocuments();
	}

	@And("Verify the data in Review screen and Submit the claim")
	public void verifyTheDataInReviewScreenAndSubmitTheClaim() throws InterruptedException {
		reviewandconfirm.reviewandSubmitNIHLClaim();
	}

	@And("Verify the claim is submitted successfully")
	public void verifyTheClaimIsSubmittedSuccessfully() {
		Assert.assertTrue(reviewandconfirm.verifyClaimSubmitted());
	}
}
