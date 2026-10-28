package pages.DynamicIntakeEform6;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm6_LostTimeandEarnings extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(xpath = "//*[@id=\"radioTimeLost\"]/div[1]/label/span") private WebElement timeLostYes;
	@FindBy(xpath = "//*[@id=\"radioTimeLost\"]/div[2]/label/span") private WebElement timeLostNo;
	@FindBy(id = "lost-time-date-picker") private WebElement missedDate;

	@FindBy(xpath = "//*[@id=\"radioOfferedTo\"]/div[1]/label") private WebElement offeredToYes;
	@FindBy(xpath = "//*[@id=\"radioOfferedTo\"]/div[2]/label") private WebElement offeredToNo;
	@FindBy(id = "modified-offer-date-picker") private WebElement modifiedDate;

	@FindBy(xpath = "//*[@id=\"radioModifiedWork\"]/div[1]/label") private WebElement modifiedWorkAccepted;
	@FindBy(xpath = "//*[@id=\"radioModifiedWork\"]/div[2]/label") private WebElement modifiedWorkDeclined;
	@FindBy(id = "declineReasonArea") private WebElement declineReason;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadio\"]/div[1]/label") private WebElement returnToWorkYes;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadio\"]/div[2]/label") private WebElement returnToWorkNo;
	@FindBy(id = "rtw-work-date-picker") private WebElement returnedDate;

	@FindBy(xpath = "//*[@id=\"radioTypeOfWork\"]/div[1]/label") private WebElement typeOfWorkRegular;
	@FindBy(xpath = "//*[@id=\"radioTypeOfWork\"]/div[2]/label") private WebElement typeOfWorkModified;
	@FindBy(id = "expected-rtw-date-picker") private WebElement expectedModifiedDate;

	@FindBy(xpath = "//*[@id=\"radioIsWageLost\"]/div[1]/label") private WebElement wageLostYes;
	@FindBy(xpath = "//*[@id=\"radioIsWageLost\"]/div[2]/label") private WebElement wageLostNo;
	@FindBy(xpath = "//*[@id=\"AddContactButton\"]/span/span") private WebElement addContactButton;
	@FindBy(id = "inputFirstName") private WebElement ContactFirstName;
	@FindBy(id = "inputLastName") private WebElement ContactLastName;
	@FindBy(id = "inputPhoneNumeber") private WebElement ContactPhoneNumber;
	@FindBy(id = "btnSave") private WebElement saveButton;
	@FindBy(id = "dropdownReturningDate-wrapper") private WebElement returningDateDay;
	@FindBy(id = "dropdownReturningMonth-wrapper") private WebElement returningDateMonth;
	@FindBy(id = "dropdownReturningYear-wrapper") private WebElement returningDateYear;
	@FindBy(xpath = "//*[@id=\"radioIsPaid\"]/div[1]/label") private WebElement empContinueToPayYes;
	@FindBy(xpath = "//*[@id=\"radioIsPaid\"]/div[2]/label") private WebElement empContinueToPayNo;
	@FindBy(xpath = "//*[@id=\"radioOtherBenefit\"]/div[1]/label") private WebElement otherBenefitYes;
	@FindBy(xpath = "//*[@id=\"radioOtherBenefit\"]/div[2]/label") private WebElement otherBenefitNo;
	@FindBy(xpath = "//*[@id=\"radioMultipleEmployment\"]/div[1]/label") private WebElement radioMultipleEmploymentYes;
	@FindBy(xpath = "//*[@id=\"radioMultipleEmployment\"]/div[2]/label") private WebElement radioMultipleEmploymentNo;




	public DIeForm6_LostTimeandEarnings(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void enterMissedTimeFromWorkDetails(String missedTimeFromWork, String missedTimeFromWorkStartDate) {
		waitUntilElementVisible(timeLostYes);
		if(missedTimeFromWork.equalsIgnoreCase("yes")) {
			Click(timeLostYes);
			Click(missedDate);
			Enter(missedDate, missedTimeFromWorkStartDate);

		} else {
			Click(timeLostNo);
		}
		Click(nextButton);
	}

	public void enterModifiedWorkDetails(String offeredTo, String modifiedDate, String modifiedWork, String declineReason) {
		waitUntilElementVisible(offeredToYes);
		if(offeredTo.equalsIgnoreCase("yes")) {
			Click(offeredToYes);
			Click(this.modifiedDate);
			Enter(this.modifiedDate, modifiedDate);
			if(modifiedWork.equalsIgnoreCase("accepted")) {
				Click(modifiedWorkAccepted);
			} else {
				Click(modifiedWorkDeclined);
				Enter(this.declineReason, declineReason);
			}
		} else {
			Click(offeredToNo);
		}
		Click(nextButton);
	}

	public void enterReturnToWorkDetails(String returningToWork, String returningDate, String typeOfReturn, String modifiedDate, String wageLoss, String rtwContact, String returnedDate) {

		waitUntilElementVisible(returnToWorkYes);
		if(returningToWork.equalsIgnoreCase("yes")) {
			Click(returnToWorkYes);
			Sleep(1000);
			Click(this.returnedDate);
			Enter(this.returnedDate, returningDate);
			if(typeOfReturn.equalsIgnoreCase("regular")) {
				Click(typeOfWorkRegular);
				if(wageLoss.equalsIgnoreCase("yes")) {
					Click(wageLostYes);
					Click(addContactButton);
					Enter(ContactFirstName, rtwContact.split("\\|")[0]);
					Enter(ContactLastName, rtwContact.split("\\|")[1]);
					jsMoveSendKeys(ContactPhoneNumber, rtwContact.split("\\|")[2]);
					Click(saveButton);
				} else {
					Click(wageLostNo);
				}

			} else {
				Click(typeOfWorkModified);
				waitUntilElementVisible(expectedModifiedDate);
				Enter(expectedModifiedDate, modifiedDate);
				Click(typeOfWorkModified);
				if(wageLoss.equalsIgnoreCase("yes")) {
					Click(wageLostYes);
				} else {
					Click(wageLostNo);
				}
				Click(addContactButton);
				Enter(ContactFirstName, rtwContact.split("\\|")[0]);
				Enter(ContactLastName, rtwContact.split("\\|")[1]);
				jsMoveSendKeys(ContactPhoneNumber, rtwContact.split("\\|")[2]);
				Click(saveButton);
			}
		} else {
			Click(returnToWorkNo);
			Click(returningDateDay);
			ClickusingActions(returningDate.split("\\|")[0]);
			Click(returningDateMonth);
			ClickusingActions(returningDate.split("\\|")[1]);
			Click(returningDateYear);
			ClickusingActions(returningDate.split("\\|")[2]);
			Click(addContactButton);
			Enter(ContactFirstName, rtwContact.split("\\|")[0]);
			Enter(ContactLastName, rtwContact.split("\\|")[1]);
			jsMoveSendKeys(ContactPhoneNumber, rtwContact.split("\\|")[2]);
			Click(saveButton);

		}
		Click(nextButton);
	}

	public void enterAdditionalEarningsAndBenefits(String additionalEarnings, String additionalBenefits, String didYouWorkforMorethanOneEmpl) {
		waitUntilElementVisible(empContinueToPayYes);
		if(additionalEarnings.equalsIgnoreCase("yes")) {
			Click(empContinueToPayYes);
		} else {
			Click(empContinueToPayNo);
		}
		if(additionalBenefits.equalsIgnoreCase("yes")) {
			Click(otherBenefitYes);
		} else {
			Click(otherBenefitNo);
		}
		if(isDisplayed(radioMultipleEmploymentYes)) {
			if (didYouWorkforMorethanOneEmpl.equalsIgnoreCase("yes")) {
				Click(radioMultipleEmploymentYes);
			} else {
				Click(radioMultipleEmploymentNo);
			}
		}
		else {
			Hooks.log("Did you work for more than 1 employer is not displayed");
		}
		Click(nextButton);

	}

	public void enterAuthorizedRepDetails()  {
		Sleep(2000);
		Click(nextButton);

	}
}
