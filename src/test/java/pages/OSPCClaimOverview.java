package pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class OSPCClaimOverview extends BaseDriverClass {
	ConfigReader prop = new ConfigReader();
	public static String randommessage;

	@FindBy(xpath = "//*[@id='mainHeading']")
	@CacheLookup
	private WebElement myclaimoverview;

	@FindBy(xpath = "//*[@id=\"benefitPaymentLink\"]/a/span")
	@CacheLookup
	private WebElement benefitpaymentslink;

	@FindBy(id = "benefitPaymentHeading")
	@CacheLookup
	private WebElement benefitpaymentsheading;

	@FindBy(xpath = "//*[@id=\"healthCareBenefitslink\"]/a/span")
	@CacheLookup
	private WebElement healthcarebenefitslink;

	@FindBy(id = "mainHeading")
	@CacheLookup
	private WebElement healthcarebenefitsheading;

	@FindBy(xpath = "//*[@id=\"nonHealthCareBenefitslink\"]/a/span")
	@CacheLookup
	private WebElement nonhealthcarebenefitslink;

	@FindBy(id = "pageHeadingLabel")
	@CacheLookup
	private WebElement nonhealthcarebenefitsheading;

	@FindBy(xpath = "//*[@id=\"appealsLink\"]/a/span")
	@CacheLookup
	private WebElement appealslink;

	@FindBy(id = "mainHeading")
	@CacheLookup
	private WebElement appealsheading;

	@FindBy(xpath = "//*[@id=\"DistributionPopUpNo\"]/div")
	@CacheLookup
	private WebElement ModelWindow;

	@FindBy(xpath = "//*[@id=\"claimDocumentsLink\"]/a/span")
	@CacheLookup
	private WebElement submitdocumentslink;

	@FindBy(xpath = "//h1[contains(text(),'Submit document(s)')]")
	@CacheLookup
	private WebElement submitdocumentsheading;

	@FindBy(xpath = "//*[@id=\"docSource0\"]/div[1]/label")
	@CacheLookup
	private WebElement OSPCWsibFormYes;

	@FindBy(xpath = "//*[@id=\"termOfUseCheckbox\"]/div/div/div[1]/span")
	@CacheLookup
	private WebElement OSPCConfirmCheckbox;

	@FindBy(id = "wsibformselection-wrapper")
	@CacheLookup
	private WebElement OSPCWSIBformDropdown;

	@FindBy(xpath = "//span[(text()='Submit')]")
	@CacheLookup
	private WebElement OSPCDocumentSubmit;

	@FindBy(xpath = "//h2[contains(text(),'Confirmation')]")
	@CacheLookup
	private WebElement submitdocumentsconfirmation;

	@FindBy(xpath = "//span[contains(text(),'Close')]")
	@CacheLookup
	private WebElement CloseButton;

	@FindBy(xpath = "//*[@id=\"claimDocumentHistoryLink\"]/a/span")
	@CacheLookup
	private WebElement DocumentHistoryLink;

	@FindBy(id = "submittedDocumentsHeading")
	@CacheLookup
	private WebElement submittedDocumentsHeading;

	@FindBy(xpath = "//*[@id=\"SubmittedDocumentsDataTable\"]/div/div[1]/div[2]/div[1]/div/div[1]")
	@CacheLookup
	private WebElement DocumentDescription;

	@FindBy(xpath = "//*[@id=\"SubmittedDocumentsDataTable\"]/div/div[1]/div[2]/div[1]/div/div[2]")
	@CacheLookup
	private WebElement DocumentDate;

	@FindBy(xpath = "\t//*[@id=\"sendUsMessageLink\"]/a/span")
	@CacheLookup
	private WebElement SendusmessageLink;

	@FindBy(id = "secondaryHeading")
	@CacheLookup
	private WebElement SendusmessageHeader;

	@FindBy(id = "ddlSubject-wrapper")
	@CacheLookup
	private WebElement SubjectDropdown;

	@FindBy(id = "txtBody")
	@CacheLookup
	private WebElement txtBody;

	@FindBy(xpath = "//*[@id=\"btnSend\"]/span/span")
	@CacheLookup
	private WebElement SendButton;

	@FindBy(xpath = "//*[@id=\"returnToMailLink\"]/span")
	@CacheLookup
	private WebElement ReturntoMail;

	@FindBy(id = "mailListPageHeading")
	@CacheLookup
	private WebElement mailListPageHeading;

	@FindBy(xpath = "//*[@id=\"mailListDatatable\"]/div/div[1]/div[2]/div[1]/div/div[4]/div")
	@CacheLookup
	private WebElement SentMessageBody;

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
		if(CloseButton.isDisplayed()) {
			CloseButton.click();
			Thread.sleep(10000);
			OSPCConfirmCheckbox.click();
			OSPCDocumentSubmit.click();
		}
		waitUntilElementVisible(submitdocumentsconfirmation);
		return submitdocumentsconfirmation.isDisplayed();

	}

	public Boolean DocumentHistory(String filename) {
		DocumentHistoryLink.click();
		waitUntilElementVisible(submittedDocumentsHeading);
		for (int i = 0; i < 50; i++) {
			if (DocumentDescription.getText().isEmpty() || !DocumentDate.getText().contentEquals(getCurrentDate("dd MMM yyyy"))) {
				getDriver().navigate().refresh();
			} else return DocumentDescription.getText().contentEquals(filename) &&
                    DocumentDate.getText().contentEquals(getCurrentDate("dd MMM yyyy"));
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
}
