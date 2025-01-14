package pages.WT;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class TravelExpense extends BaseDriverClass {

	@FindBy(id = "repeatedExpense") private WebElement repeatedExpense;
	@FindBy(xpath = "/html/body/app-root/div/div[4]/div[2]/app-travel-expense/div/table/tfoot/tr/td[2]/div/select") private WebElement reasonForTravel;
	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Choose date')]") private WebElement ChooseDate;
	@FindBy(xpath = "//button[@aria-current='date']") private WebElement CurrentDate;
	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Ok')]") private WebElement OkButton;
	@FindBy(id = "selectButt2") private WebElement SelectExpense;
	@FindBy(xpath = "(//*[@type='checkbox'])[2]") private WebElement KilometersCheckbox;
	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'OK')]") private WebElement OKButton;
	@FindBy(id = "travel2Amt2") private WebElement Amount;
	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Update')]") private WebElement UpdateButton;
	@FindBy(id = "additiionalInfo") private WebElement AdditionalInfo;
	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Next')]") private WebElement NextButton;

	@FindBy(xpath = "//*[@name='newOtherType']") private WebElement OtherExpenseType;
	@FindBy(id = "newexpenseDesc") private WebElement OtherExpenseDescription;
	@FindBy(id = "expAmountNew_en") private WebElement OtherExpenseAmount;
	@FindBy(xpath = "//h2[contains(text(),'Summary')]") private WebElement SummaryHeader;

	@FindBy(id = "isEmailNeeded") private WebElement isEmailNeeded;
	@FindBy(id = "emailAddress") private WebElement emailAddress;
	@FindBy(id = "declared") private WebElement declaration;
	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Submit')]") private WebElement SubmitButton;

	@FindBy(xpath = "//h2[contains(text(),'Submission was successful')]") private WebElement ConfirmationHeader;
	@FindBy(xpath = "//*[@id='DistributionPopUpNo']/div") private WebElement DistributionPopUpNo;


	public TravelExpense(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean enterExpenseDetails(String expenseType, String reason, String expenses,
									   String totalKM, String pubTransit, String parking, String others,
									   String additionalInfo) throws InterruptedException {
		waitUntilElementVisible(repeatedExpense);
		if(expenseType.equalsIgnoreCase("Repeated Travel")){
			repeatedExpense.click();
			reasonForTravel.click();
			reasonForTravel.sendKeys(reason);
			ChooseDate.click();
			CurrentDate.click();
			OKButton.click();
			SelectExpense.click();
			if ( expenses.equalsIgnoreCase("Kilometers")) {
				KilometersCheckbox.click();
				Thread.sleep(2000);
				OkButton.click();
				Amount.sendKeys(totalKM);
			}

			UpdateButton.click();
			AdditionalInfo.sendKeys(additionalInfo);
			NextButton.click();
		}

		waitUntilElementVisible(OtherExpenseType);
		return OtherExpenseType.isDisplayed();
	}


	public boolean otherExpenseDetails(String otherExpenseType, String otherExpenseDescription, String otherExpenseAmount, String otherAdditionalInfo) {
		OtherExpenseType.sendKeys(otherExpenseType);
		ChooseDate.click();
		CurrentDate.click();
		OKButton.click();
		OtherExpenseDescription.sendKeys(otherExpenseDescription);
		OtherExpenseAmount.sendKeys(otherExpenseAmount);
		UpdateButton.click();
		AdditionalInfo.sendKeys(otherAdditionalInfo);
		NextButton.click();
		waitUntilElementVisible(SummaryHeader);
		return SummaryHeader.isDisplayed();
	}

	public boolean reviewAndSubmit(String email) {

		if(!email.isEmpty()){
			isEmailNeeded.click();
			emailAddress.sendKeys(email);
		}
		declaration.click();
		SubmitButton.click();

		try{
			Thread.sleep(3000);
			if(DistributionPopUpNo.isDisplayed()){
				DistributionPopUpNo.click();
			}
		}catch (Exception e){
			System.out.println("DistributionPopUpNo not displayed");
		}

		ConfirmationHeader.isDisplayed();
		return ConfirmationHeader.isDisplayed();
	}
}