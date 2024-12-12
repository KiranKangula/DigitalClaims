package pages;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class OSPCClaimOverview extends BaseDriverClass {

	@FindBy(xpath = "//*[@id='mainHeading']")
	@CacheLookup
	private WebElement myclaimoverview;

	public OSPCClaimOverview(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean verifyOnClaimOverview() {
		waitUntilElementVisible(myclaimoverview);
		Assert.assertEquals(myclaimoverview.getText(), "My claim overview");
		return myclaimoverview.isDisplayed();
	}

}
