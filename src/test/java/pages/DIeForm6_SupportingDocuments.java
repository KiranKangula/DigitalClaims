package pages;

import definitions.Hooks;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

public class DIeForm6_SupportingDocuments extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(xpath = "//*[@id=\"intakeBodySection\"]/div[2]/div[1]/span[2]/a/span") private WebElement nextButton;
	@FindBy(id = "additionalCommentsInput") private WebElement additionalCommentsInput;


	public DIeForm6_SupportingDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void enterSupportingDocuments() throws InterruptedException {
		Thread.sleep(2000);
		nextButton.click();
	}

	public void enterAdditionalComments(String additionalComments) {
		waitUntilElementVisible(this.additionalCommentsInput);
		this.additionalCommentsInput.sendKeys(additionalComments);
		nextButton.click();
	}
}
