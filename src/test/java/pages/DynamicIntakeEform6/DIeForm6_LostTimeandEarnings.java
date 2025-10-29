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
	@FindBy(id = "dropdownMissedDate-wrapper") private WebElement missedDateDay;
	@FindBy(id = "dropdownMissedMonth-wrapper") private WebElement missedDateMonth;
	@FindBy(id = "dropdownMissedYear-wrapper") private WebElement missedDateYear;
	@FindBy(xpath = "//*[@id=\"radioOfferedTo\"]/div[1]/label") private WebElement offeredToYes;
	@FindBy(xpath = "//*[@id=\"radioOfferedTo\"]/div[2]/label") private WebElement offeredToNo;
	@FindBy(id = "dropdownModifiedDate-wrapper") private WebElement modifiedDateDay;
	@FindBy(id = "dropdownModifiedMonth-wrapper") private WebElement modifiedDateMonth;
	@FindBy(id = "dropdownModifiedYear-wrapper") private WebElement modifiedDateYear;
	@FindBy(xpath = "//*[@id=\"radioModifiedWork\"]/div[1]/label") private WebElement modifiedWorkAccepted;
	@FindBy(xpath = "//*[@id=\"radioModifiedWork\"]/div[2]/label") private WebElement modifiedWorkDeclined;
	@FindBy(id = "declineReasonArea") private WebElement declineReason;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadio\"]/div[1]/label") private WebElement returnToWorkYes;
	@FindBy(xpath = "//*[@id=\"returnToWorkRadio\"]/div[2]/label") private WebElement returnToWorkNo;
	@FindBy(id = "dropdownReturnedDate-wrapper") private WebElement returnedDateDay;
	@FindBy(id = "dropdownReturnedMonth-wrapper") private WebElement returnedDateMonth;
	@FindBy(id = "dropdownReturnedYear-wrapper") private WebElement returnedDateYear;
	@FindBy(xpath = "//*[@id=\"radioTypeOfWork\"]/div[1]/label") private WebElement typeOfWorkRegular;
	@FindBy(xpath = "//*[@id=\"radioTypeOfWork\"]/div[2]/label") private WebElement typeOfWorkModified;
	@FindBy(id = "dropdownModifiedDate-wrapper") private WebElement expectedModifiedDateDay;
	@FindBy(id = "dropdownModifiedMonth-wrapper") private WebElement expectedModifiedMonth;
	@FindBy(id = "dropdownModifiedYear-wrapper") private WebElement expectedModifiedYear;
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



	public DIeForm6_LostTimeandEarnings(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void enterMissedTimeFromWorkDetails(String missedTimeFromWork, String missedTimeFromWorkStartDate) {
		waitUntilElementVisible(timeLostYes);
		if(missedTimeFromWork.equalsIgnoreCase("yes")) {
			timeLostYes.click();
			missedDateDay.click();
			ClickusingActions(missedTimeFromWorkStartDate.split("\\|")[0]);
			missedDateMonth.click();
			ClickusingActions(missedTimeFromWorkStartDate.split("\\|")[1]);
			missedDateYear.click();
			ClickusingActions(missedTimeFromWorkStartDate.split("\\|")[2]);

		} else {
			timeLostNo.click();
		}
		nextButton.click();
	}

	public void enterModifiedWorkDetails(String offeredTo, String modifiedDate, String modifiedWork, String declineReason) {
		waitUntilElementVisible(offeredToYes);
		if(offeredTo.equalsIgnoreCase("yes")) {
			offeredToYes.click();
			modifiedDateDay.click();
			ClickusingActions(modifiedDate.split("\\|")[0]);
			modifiedDateMonth.click();
			ClickusingActions(modifiedDate.split("\\|")[1]);
			modifiedDateYear.click();
			ClickusingActions(modifiedDate.split("\\|")[2]);
			if(modifiedWork.equalsIgnoreCase("accepted")) {
				modifiedWorkAccepted.click();
			} else {
				modifiedWorkDeclined.click();
				this.declineReason.sendKeys(declineReason);
			}
		} else {
			offeredToNo.click();
		}
		nextButton.click();
	}

	public void enterReturnToWorkDetails(String returningToWork, String returningDate, String typeOfReturn, String modifiedDate, String wageLoss, String rtwContact, String returnedDate) throws InterruptedException {

		waitUntilElementVisible(returnToWorkYes);
		if(returningToWork.equalsIgnoreCase("yes")) {
			returnToWorkYes.click();
			Thread.sleep(1000);
			returnedDateDay.click();
			ClickusingActions(returnedDate.split("\\|")[0]);
			returnedDateMonth.click();
			ClickusingActions(returnedDate.split("\\|")[1]);
			returnedDateYear.click();
			ClickusingActions(returnedDate.split("\\|")[2]);
			if(typeOfReturn.equalsIgnoreCase("regular")) {
				typeOfWorkRegular.click();
				if(wageLoss.equalsIgnoreCase("yes")) {
					wageLostYes.click();
					addContactButton.click();
					ContactFirstName.sendKeys(rtwContact.split("\\|")[0]);
					ContactLastName.sendKeys(rtwContact.split("\\|")[1]);
					jsMoveSendKeys(ContactPhoneNumber, rtwContact.split("\\|")[2]);
					saveButton.click();
				} else {
					wageLostNo.click();
				}

			} else {
				typeOfWorkModified.click();
				waitUntilElementVisible(expectedModifiedDateDay);
				selectDropdownValue(expectedModifiedDateDay, modifiedDate.split("\\|")[0]);
				selectDropdownValue(expectedModifiedMonth, modifiedDate.split("\\|")[1]);
				selectDropdownValue(expectedModifiedYear, modifiedDate.split("\\|")[2]);

				if(wageLoss.equalsIgnoreCase("yes")) {
					wageLostYes.click();
				} else {
					wageLostNo.click();
				}
				addContactButton.click();
				ContactFirstName.sendKeys(rtwContact.split("\\|")[0]);
				ContactLastName.sendKeys(rtwContact.split("\\|")[1]);
				jsMoveSendKeys(ContactPhoneNumber, rtwContact.split("\\|")[2]);
				saveButton.click();
			}
		} else {
			returnToWorkNo.click();
			returningDateDay.click();
			ClickusingActions(returningDate.split("\\|")[0]);
			returningDateMonth.click();
			ClickusingActions(returningDate.split("\\|")[1]);
			returningDateYear.click();
			ClickusingActions(returningDate.split("\\|")[2]);
			addContactButton.click();
			ContactFirstName.sendKeys(rtwContact.split("\\|")[0]);
			ContactLastName.sendKeys(rtwContact.split("\\|")[1]);
			jsMoveSendKeys(ContactPhoneNumber, rtwContact.split("\\|")[2]);
			saveButton.click();

		}
		nextButton.click();
	}

	public void enterAdditionalEarningsAndBenefits(String additionalEarnings, String additionalBenefits) {
		waitUntilElementVisible(empContinueToPayYes);
		if(additionalEarnings.equalsIgnoreCase("yes")) {
			empContinueToPayYes.click();
		} else {
			empContinueToPayNo.click();
		}
		if(additionalBenefits.equalsIgnoreCase("yes")) {
			otherBenefitYes.click();
		} else {
			otherBenefitNo.click();
		}
		nextButton.click();

	}

	public void enterAuthorizedRepDetails() throws InterruptedException {
		Thread.sleep(2000);
		nextButton.click();

	}
}
