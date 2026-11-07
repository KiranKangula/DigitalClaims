package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class HealthCare extends BaseDriverClass {

	@FindBy(xpath = "//*[@name='hearingLossNoticedYear']") private WebElement HearingLossNoticedYear;
	@FindBy(id = "wearHearingAidyes") private WebElement wearHearingAidyes;
	@FindBy(id = "wearHearingAidno") private WebElement wearHearingAidno;
	@FindBy(xpath = "//*[@name='hearingAidUsageDate']") private WebElement hearingAidUsageDate;
	@FindBy(xpath = "//*[@name='dateOfFirstAudiogram']") private WebElement dateOfFirstAudiogram;
	@FindBy(xpath = "//*[@name='audiogramClinicName']") private WebElement audiogramClinicName;
	@FindBy(xpath = "//*[@name='audiogramClinicAddress']") private WebElement audiogramClinicAddress;
	@FindBy(xpath = "//*[@name='audiogramClinicPhoneNumber']") private WebElement audiogramClinicPhoneNumber;
	@FindBy(xpath = "(//*[@name='hasVisitedEntSpecialist'])[1]") private WebElement hasVisitedEntSpecialistYes;
	@FindBy(xpath = "//*[@name='entAppointmentDate']") private WebElement entAppointmentDate;
	@FindBy(xpath = "//*[@name='entSpecialistName']") private WebElement entSpecialistName;
	@FindBy(xpath = "//*[@name='entSpecialistAddress']") private WebElement entSpecialistAddress;
	@FindBy(xpath = "//*[@name='entSpecialistPhoneNumber']") private WebElement entSpecialistPhoneNumber;
	@FindBy(xpath = "(//*[@name='hasVisitedEntSpecialist'])[1]") private WebElement hasVisitedEntSpecialistNo;
	@FindBy(xpath = "//*[@id='experienceRingingQuestionyes']") private WebElement experienceRingingQuestionyes;
	@FindBy(xpath = "//*[@id='experienceRingingQuestionno']") private WebElement experienceRingingQuestionno;
	@FindBy(xpath = "//*[@id='ringingEarDurationyes']") private WebElement ringingEarDurationyes;
	@FindBy(xpath = "//*[@id='ringingEarDurationno']") private WebElement ringingEarDurationno;
	@FindBy(xpath = "//*[@id='hasSevereRingingInEaryes']") private WebElement hasSevereRingingInEaryes;
	@FindBy(xpath = "//*[@id='hasSevereRingingInEarno']") private WebElement hasSevereRingingInEarno;
	@FindBy(xpath = "//*[@id='isRingingConstantyes']") private WebElement isRingingConstantyes;
	@FindBy(xpath = "//*[@id='isRingingConstantno']") private WebElement isRingingConstantno;
	@FindBy(xpath = "(//button[@role='button' and contains(., 'Next')])[2]") private WebElement NextButton;


	public HealthCare(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void EnterHealthCareDetails(String hearingLossNoticedYear, String wearHearingAid, String hearingAidUsageDate, String dateOfFirstAudiogram,
									   String audiogramClinicName, String audiogramClinicAddress, String audiogramClinicPhoneNumber,
									   String hasVisitedEntSpecialist, String entAppointmentDate, String entSpecialistName, String entSpecialistAddress,
									   String entSpecialistPhoneNumber, String experienceRingingQuestion, String ringingEarDuration, String hasSevereRingingInEar,
									   String isRingingConstant) throws InterruptedException {
		Sleep(5000);
		waitUntilElementVisible(HearingLossNoticedYear);

		Enter(HearingLossNoticedYear, hearingLossNoticedYear);
		if (wearHearingAid.equalsIgnoreCase("yes")) {
			Click(wearHearingAidyes);
			Enter(this.hearingAidUsageDate, hearingAidUsageDate);
		} else {
			Click(wearHearingAidno);
		}
		Enter(this.dateOfFirstAudiogram, dateOfFirstAudiogram);
		Enter(this.audiogramClinicName, audiogramClinicName);
		Enter(this.audiogramClinicAddress, audiogramClinicAddress);
		Enter(this.audiogramClinicPhoneNumber, audiogramClinicPhoneNumber);
		if (hasVisitedEntSpecialist.equalsIgnoreCase("yes")) {
			Click(hasVisitedEntSpecialistYes);
			Enter(this.entAppointmentDate, entAppointmentDate);
			Enter(this.entSpecialistName, entSpecialistName);
			Enter(this.entSpecialistAddress, entSpecialistAddress);
			Enter(this.entSpecialistPhoneNumber, entSpecialistPhoneNumber);
		} else {
			Click(hasVisitedEntSpecialistNo);
		}
		if (experienceRingingQuestion.equalsIgnoreCase("yes")) {
			Click(experienceRingingQuestionyes);
			if (ringingEarDuration.equalsIgnoreCase("Less than 2 years")) {
				Click(ringingEarDurationyes);
			} else {
				Click(ringingEarDurationno);
			}
			if (hasSevereRingingInEar.equalsIgnoreCase("yes")) {
				Click(hasSevereRingingInEaryes);
			} else {
				Click(hasSevereRingingInEarno);
			}
			if (isRingingConstant.equalsIgnoreCase("yes")) {
				Click(isRingingConstantyes);
			} else {
				Click(isRingingConstantno);
			}
		} else {
			Click(experienceRingingQuestionno);
		}
		Click(NextButton);
	Sleep(5000);

	}
}