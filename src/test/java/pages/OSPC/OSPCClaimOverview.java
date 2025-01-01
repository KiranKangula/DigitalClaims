package pages.OSPC;

import definitions.Hooks;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.GuidewireClaimCenter.GuidewirePage;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OSPCClaimOverview extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();
	public static String randommessage;

	@FindBy(xpath = "//*[@id='mainHeading']") private WebElement myclaimoverview;
	@FindBy(xpath = "//*[@id=\"benefitPaymentLink\"]/a/span") private WebElement benefitpaymentslink;
	@FindBy(id = "benefitPaymentHeading") private WebElement benefitpaymentsheading;
	@FindBy(xpath = "//*[@id=\"healthCareBenefitslink\"]/a/span") private WebElement healthcarebenefitslink;
	@FindBy(id = "mainHeading") private WebElement healthcarebenefitsheading;
	@FindBy(xpath = "//*[@id=\"nonHealthCareBenefitslink\"]/a/span") private WebElement nonhealthcarebenefitslink;
	@FindBy(id = "pageHeadingLabel") private WebElement nonhealthcarebenefitsheading;
	@FindBy(xpath = "//*[@id=\"appealsLink\"]/a/span") private WebElement appealslink;
	@FindBy(id = "mainHeading") private WebElement appealsheading;
	@FindBy(xpath = "//*[@id=\"DistributionPopUpNo\"]/div") private WebElement ModelWindow;
	@FindBy(xpath = "//*[@id=\"claimDocumentsLink\"]/a/span") private WebElement submitdocumentslink;
	@FindBy(xpath = "//h1[contains(text(),'Submit document(s)')]") private WebElement submitdocumentsheading;
	@FindBy(xpath = "//*[@id=\"docSource0\"]/div[1]/label") private WebElement OSPCWsibFormYes;
	@FindBy(xpath = "//*[@id=\"termOfUseCheckbox\"]/div/div/div[1]/span") private WebElement OSPCConfirmCheckbox;
	@FindBy(id = "wsibformselection-wrapper") private WebElement OSPCWSIBformDropdown;
	@FindBy(xpath = "//span[(text()='Submit')]") private WebElement OSPCDocumentSubmit;
	@FindBy(xpath = "//h2[contains(text(),'Confirmation')]") private WebElement submitdocumentsconfirmation;
	@FindBy(xpath = "//span[contains(text(),'Close')]") private WebElement CloseButton;
	@FindBy(xpath = "//*[@id=\"claimDocumentHistoryLink\"]/a/span") private WebElement DocumentHistoryLink;
	@FindBy(id = "submittedDocumentsHeading") private WebElement submittedDocumentsHeading;
	@FindBy(xpath = "//*[@id=\"SubmittedDocumentsDataTable\"]/div/div[1]/div[2]/div[1]/div/div[1]") private WebElement DocumentDescription;
	@FindBy(xpath = "//*[@id=\"SubmittedDocumentsDataTable\"]/div/div[1]/div[2]/div[1]/div/div[2]") private WebElement DocumentDate;
	@FindBy(xpath = "\t//*[@id=\"sendUsMessageLink\"]/a/span") private WebElement SendusmessageLink;
	@FindBy(id = "secondaryHeading") private WebElement SendusmessageHeader;
	@FindBy(id = "ddlSubject-wrapper") private WebElement SubjectDropdown;
	@FindBy(id = "txtBody") private WebElement txtBody;
	@FindBy(xpath = "//*[@id=\"btnSend\"]/span/span") private WebElement SendButton;
	@FindBy(xpath = "//*[@id=\"returnToMailLink\"]/span") private WebElement ReturntoMail;
	@FindBy(id = "mailListPageHeading") private WebElement mailListPageHeading;
	@FindBy(xpath = "//*[@id=\"mailListDatatable\"]/div/div[1]/div[2]/div[1]/div/div[4]/div") private WebElement SentMessageBody;
	@FindBy(xpath = "//*[@id=\"addClaimButton\"]/span/span") private WebElement AddClaimButton;
	@FindBy(id = "claimNumberInput") private WebElement ClaimNumberInput;
	@FindBy(id = "yearOfBirthInput") private WebElement yearOfBirthInput;
	@FindBy(id = "personalIdentificationInput") private WebElement personalIdentificationInput;
	@FindBy(xpath = "//*[@id=\"termOfUseCheckBox\"]/div/div/div/div[1]/span") private WebElement termOfUseCheckBox;
	@FindBy(id = "nextButton") private WebElement SubmitButton;
	@FindBy(id = "pageHeadingLabel") private WebElement pageHeadingLabel;
	@FindBy(id = "secondaryHeading") private WebElement secondaryHeading;
	@FindBy(id = "returnToClaimsHomelLink") private WebElement returnToClaimsHomelLink;
	@FindBy(id = "viewClaimLink") private WebElement viewClaimLink;
	@FindBy(css = "div.rt-tbody.jut__DataTable__tableBody") private WebElement tableBody;
	@FindBy(id = "isDuplicateClaimMsg") private WebElement isDuplicateClaimMsg;

	public OSPCClaimOverview(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnClaimOverview() {
		waitUntilElementVisible(myclaimoverview);
		if(ModelWindow.isDisplayed())
		{
			ModelWindow.click();
		}
		Assert.assertEquals(myclaimoverview.getText(), "My claim overview");
		return myclaimoverview.isDisplayed();
	}

	public Boolean verifyOnBenefitpayments() {
		benefitpaymentslink.click();
		waitUntilElementVisible(benefitpaymentsheading);
		Assert.assertEquals(benefitpaymentsheading.getText(), "Benefit payments");
		return benefitpaymentsheading.isDisplayed();
	}

	public Boolean verifyOnHealthcarebenefits() {
		healthcarebenefitslink.click();
		waitUntilElementVisible(healthcarebenefitsheading);
		Assert.assertEquals(healthcarebenefitsheading.getText(), "Health care benefits");
		return healthcarebenefitsheading.isDisplayed();
	}

	public Boolean verifyOnNonHealthcarebenefits() {
		nonhealthcarebenefitslink.click();
		waitUntilElementVisible(nonhealthcarebenefitsheading);
		Assert.assertEquals(nonhealthcarebenefitsheading.getText(), "Non-health care benefits");
		return nonhealthcarebenefitsheading.isDisplayed();
	}

	public Boolean verifyOnAppeals() {
		appealslink.click();
		waitUntilElementVisible(appealsheading);
		Assert.assertEquals(appealsheading.getText(), "Appeals");
		return appealsheading.isDisplayed();
	}

	public Boolean verifyOnSubmitDocuments() {
		submitdocumentslink.click();
		waitUntilElementVisible(submitdocumentsheading);
		Assert.assertEquals(submitdocumentsheading.getText(), "Submit document(s)");
		return submitdocumentsheading.isDisplayed();
	}

	public Boolean uploadDocuments(String filename) {

		String path = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\Document Upload\\" + filename;
		WebElement input = getDriver().findElement(By.cssSelector("input[type='file']"));
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.display = 'block';", input);
		input.sendKeys(path);
		waitUntilElementVisible(OSPCWsibFormYes);
		return OSPCWsibFormYes.isDisplayed();

	}

	public Boolean selectWSIBForm(String wsibform) {
		OSPCWsibFormYes.click();
		waitUntilElementVisible(OSPCWSIBformDropdown);
		OSPCWSIBformDropdown.click();
		String dynamicXPath = "//*[contains(text(),'" + wsibform + "')]";
		WebElement WsibformElement = getDriver().findElement(By.xpath(dynamicXPath));
		waitUntilElementVisible(WsibformElement);
		WsibformElement.click();
		waitUntilElementVisible(OSPCWSIBformDropdown);
		return true;
	}

	public Boolean SubmitDocument() throws InterruptedException {
		OSPCConfirmCheckbox.click();
		OSPCDocumentSubmit.click();
		try{
			if(CloseButton.isDisplayed()) {
				CloseButton.click();
				Thread.sleep(10000);
				OSPCConfirmCheckbox.click();
				OSPCDocumentSubmit.click();
			}
		}
		catch(NoSuchElementException ignored)
		{}
		waitUntilElementVisible(submitdocumentsconfirmation);
		return submitdocumentsconfirmation.isDisplayed();

	}

	public Boolean DocumentHistory(String filename) {
		DocumentHistoryLink.click();
		waitUntilElementVisible(submittedDocumentsHeading);
		for (int i = 0; i < 10; i++) {
			System.out.println("Running the step for "+ i+ " times.");
			try {

				if (DocumentDescription.getText().isEmpty() || !DocumentDate.getText().contentEquals(getCurrentDate("dd MMM yyyy"))) {
					getDriver().navigate().refresh();
				} else return DocumentDescription.getText().contentEquals(filename) &&
						DocumentDate.getText().contentEquals(getCurrentDate("dd MMM yyyy"));
			}
			catch (Exception e){
				getDriver().navigate().refresh();
			}
			}
		return false;
    }


	public boolean verifyOnSendMessageScreen() {
		SendusmessageLink.click();
		waitUntilElementVisible(SendusmessageHeader);
		Assert.assertEquals(SendusmessageHeader.getText(), "Send us a message");
		return SendusmessageHeader.isDisplayed();
	}

	public boolean sendusamessage(String subject, String message) {
		if (message.equals("<dynamic>")) {
			randommessage = generateRandomText(); // Replace "<dynamic>" with random text
		}
		SubjectDropdown.click();
		String dynamicXPath = "//*[contains(text(),'" + subject + "')]";
		WebElement SubjectElement = getDriver().findElement(By.xpath(dynamicXPath));
		waitUntilElementVisible(SubjectElement);
		SubjectElement.click();
		txtBody.sendKeys(randommessage);
		return SubjectDropdown.getText().contentEquals(subject);
	}

	public boolean sendandconfirm() {
		SendButton.click();
		waitUntilElementVisible(ReturntoMail);
		//Assert.assertEquals(ReturntoMail.getText(), "Message sent");
		return ReturntoMail.isDisplayed();
	}

	public boolean mailscreen() {
		ReturntoMail.click();
		waitUntilElementVisible(mailListPageHeading);
		return SentMessageBody.getText().contentEquals(randommessage);
	}

	public boolean addClaim() {
		AddClaimButton.click();
		waitUntilElementVisible(pageHeadingLabel);
		return pageHeadingLabel.isDisplayed();
	}

	public boolean AddaClaimOSPC(String claimnumber, String yob, String pin, GuidewirePage guidewirecc) {

		ClaimNumberInput.sendKeys(claimnumber);
		yearOfBirthInput.sendKeys(yob);
		personalIdentificationInput.sendKeys(pin);
		termOfUseCheckBox.click();
		SubmitButton.click();
		try {
			if (isDuplicateClaimMsg.isDisplayed()) {
				ArrayList<String> tabs = guidewirecc.switchtab();
				guidewirecc.loginToGWCC(prop.getProperty("GWCC_Username"), prop.getProperty("GWCC_Password"));
				guidewirecc.navigatePortalAuthScreen();
				guidewirecc.removeClaimfromPortalAuth(claimnumber);
				guidewirecc.GWCClogout();
				getDriver().close();
				getDriver().switchTo().window(tabs.get(0));
				termOfUseCheckBox.click();
				SubmitButton.click();
			}
		}
		catch(NoSuchElementException ignored)
		{}

		waitUntilElementVisible(secondaryHeading);
		returnToClaimsHomelLink.click();

		// Locate all rows in the table
		List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));

		// Iterate through each row to search for the claim number
		for (WebElement row : rows) {
			// Locate the Claim Number cell (assuming it's the first column)
			WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
			String cNumber = claimCell.getText();

			// Check if the claim number matches the desired value
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return true;
            }
			else {
				System.out.println("Claim number " + claimnumber + " not found.");
				return false;
			}
		}
			return false;
	}

	public boolean claimRemovalConfirmationOSPC(String claimnumber) throws InterruptedException {
		Set<String> windowHandles = getDriver().getWindowHandles();
		List<String> tabs = new ArrayList<>(windowHandles);
		getDriver().close();
		getDriver().switchTo().window(tabs.get(0));
		getDriver().navigate().refresh();
		//Thread.sleep(5000);
		waitUntilElementVisible(tableBody);

		// Locate all rows in the table
		List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));

		// Iterate through each row to search for the claim number
		for (WebElement row : rows) {
			// Locate the Claim Number cell (assuming it's the first column)
			WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
			String cNumber = claimCell.getText();

			// Check if the claim number matches the desired value
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return false;
			}
			else {
				System.out.println("Claim number " + claimnumber + " not found.");
				return true;
			}
		}
		return false;
	}

	public boolean claimAdditionalConfirmationOSPC(String claimnumber) throws InterruptedException {
		List<String> tabs = new ArrayList<>(getDriver().getWindowHandles());
		getDriver().close();
		getDriver().switchTo().window(tabs.get(0));
		getDriver().navigate().refresh();
		Thread.sleep(5000);
		waitUntilElementVisible(tableBody);

		for (WebElement row : tableBody.findElements(By.cssSelector("div.rt-tr"))) {
			String cNumber = row.findElement(By.cssSelector("div.rt-td a.bluelink")).getText();
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return true;
			}
		}
		System.out.println("Claim number " + claimnumber + " not found.");
		return false;
	}


	public boolean claimAdditionConfirmationOSPC(String claimnumber) throws InterruptedException {

		//Thread.sleep(5000);
		waitUntilElementVisible(tableBody);

		// Locate all rows in the table
		List<WebElement> rows = tableBody.findElements(By.cssSelector("div.rt-tr"));

		// Iterate through each row to search for the claim number
		for (WebElement row : rows) {
			// Locate the Claim Number cell (assuming it's the first column)
			WebElement claimCell = row.findElement(By.cssSelector("div.rt-td a.bluelink"));
			String cNumber = claimCell.getText();

			// Check if the claim number matches the desired value
			if (cNumber.equals(claimnumber)) {
				System.out.println("Claim number " + claimnumber + " found.");
				return true;
			}
			else {
				System.out.println("Claim number " + claimnumber + " not found.");
				return false;
			}
		}
		return false;
	}

}
