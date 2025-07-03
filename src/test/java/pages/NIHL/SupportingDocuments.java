package pages.NIHL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class SupportingDocuments extends BaseDriverClass {



	@FindBy(xpath = "(//button[@role='button' and contains(., 'Next')])[4]") private WebElement NextButton;
	@FindBy(xpath = "//*[@aria-label='Attached audiogram file']") private WebElement AttachedAudiogramFile;


	public SupportingDocuments(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public void EnterSupportingDocuments() throws InterruptedException {
		waitUntilElementVisible(AttachedAudiogramFile);
		Thread.sleep(5000);
		NextButton.click();
		Thread.sleep(5000);
	}
}