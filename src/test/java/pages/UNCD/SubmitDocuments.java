package pages.UNCD;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
///same used for Upload Report - UNCD documents
public class SubmitDocuments extends BaseDriverClass {

	@FindBy(xpath = "//*[@aria-labelledby='formtype0']/label/span/input[@value='yes']") private WebElement formtype0Yes;
	@FindBy(xpath = "//*[@aria-labelledby='formtype0']/label/span/input[@value='no']") private WebElement formtype0No;
	@FindBy(id = "edobformselection0-select") private WebElement edobformselection0Select;
	@FindBy(id = "catogoryselection0-select") private WebElement catogoryselection0Select;
	@FindBy(id = "subformselection0-select") private WebElement subformselection0Select;
	@FindBy(xpath = "//*[@type='submit']") private WebElement NextButton;
	@FindBy(id = "formtype0") private WebElement isthisaWSIBform;

	@FindBy(id = "uncdformselection0-select") private WebElement uncdformselection0Select;
	@FindBy(id = "region-support") private WebElement regionSupportHeader;
	@FindBy(xpath = "//*[@aria-labelledby='support-doctype-label']/label/span/input[@value='yes']") private WebElement supportdoctypeYes;
	@FindBy(xpath = "//*[@aria-labelledby='support-doctype-label']/label/span/input[@value='no']") private WebElement supportdoctypeNo;
	@FindBy(id = "alert-dialog-title") private WebElement alertdialogtitle;
	@FindBy(xpath = "//button[text()='Fermer' or text()= 'Close']") private WebElement alertCloseButton;


	public SubmitDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public void navigateToURL(String url) {
		getDriver().get(url);
	}


	public Boolean uploadDocument(String filename) {
		String path = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\Document Upload\\" + filename;
		WebElement input = getDriver().findElement(By.cssSelector("input[type='file']"));
		((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.display = 'block';", input);
		input.sendKeys(path);
		return true;
	}

	public void selectWSIBForm(String wsibform) throws InterruptedException {
		formtype0Yes.click();
		waitUntilElementVisible(edobformselection0Select);
		edobformselection0Select.click();
		edobformselection0Select.sendKeys(wsibform);
		Thread.sleep(2000);
		NextButton.click();
	}

	public void selectNonWSIBForm(String category, String subform) throws InterruptedException {
		formtype0No.click();
		waitUntilElementVisible(catogoryselection0Select);
		catogoryselection0Select.click();
		catogoryselection0Select.sendKeys(category);
		waitUntilElementVisible(subformselection0Select);
		subformselection0Select.click();
		subformselection0Select.sendKeys(subform);
		Thread.sleep(2000);
		subformselection0Select.click();
		NextButton.click();

	}

	public void selectUNCDForm(String category) {
		waitUntilElementVisible(uncdformselection0Select);
		uncdformselection0Select.click();
		uncdformselection0Select.sendKeys(category);
		NextButton.click();
		try{
			if(alertdialogtitle.isDisplayed()){
				System.out.println("Alert dialog displayed");
				alertCloseButton.click();
				NextButton.click();
			}
		}
		catch(Exception e){
			System.out.println("Alert dialog not displayed");
		}

	}

	public void selectSupportingDocuments(String supportingdocument) {
		waitUntilElementVisible(regionSupportHeader);
		if (supportingdocument.equalsIgnoreCase("yes")) {
			supportdoctypeYes.click();
		} else {
			supportdoctypeNo.click();
		}
		NextButton.click();
	}
}