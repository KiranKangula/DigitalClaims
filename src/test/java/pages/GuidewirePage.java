package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;

import static pages.OSPCClaimOverview.randommessage;

public class GuidewirePage extends BaseDriverClass {
	ConfigReader prop=new ConfigReader();

	@FindBy(id = "Login:LoginScreen:LoginDV:username")
	@CacheLookup
	private WebElement usernameTextField;

	@FindBy(id = "Login:LoginScreen:LoginDV:password")
	@CacheLookup
	private WebElement passwordTextField;

	@FindBy(id = "Login:LoginScreen:LoginDV:submit")
	@CacheLookup
	private WebElement loginButton;

	@FindBy(id = "TabBar:SearchTab")
	@CacheLookup
	private WebElement SearchTab;

	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchOptionalInputSet:ClaimNumber")
	@CacheLookup
	private WebElement ClaimSearchField;

	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchDV:ClaimSearchAndResetInputSet:Search_link")
	@CacheLookup
	private WebElement SearchButton;

	@FindBy(id = "ClaimSearch:ClaimSearchScreen:ClaimSearchResultsLV:0:ClaimNumber")
	@CacheLookup
	private WebElement SelectClaimfromSearch;

	@FindBy(id = "Claim:MenuLinks:Claim_WSIB_MessagingGroup")
	@CacheLookup
	private WebElement MessagingLink;

	@FindBy(id = "WSIB_ClaimCustomerMessages:WSIB_MessagingScreen:WSIB_MessagePanelSet:0:MessageBody")
	@CacheLookup
	private WebElement MessagingBody;

	@FindBy(id = "ClaimSummary:ClaimSummaryScreen:ttlBar")
	@CacheLookup
	private WebElement SummaryTitlebar;

	public GuidewirePage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public void switchtab() {
		// Dynamically create the XPath using the claim number
		String gettitle = getDriver().getCurrentUrl();
		((JavascriptExecutor) getDriver()).executeScript("window.open();");
		ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
		getDriver().switchTo().window(tabs.get(1));
		getDriver().get(prop.getProperty("GWURl"));
		//return tabs.get(0);
	}

	public void loginToGWCC(String username, String password) {
		getDriver().switchTo().frame("top_frame");
		waitUntilElementVisible(usernameTextField);
		usernameTextField.sendKeys(username);
		passwordTextField.sendKeys(password);
		loginButton.click();
	}


	public boolean searchClaim(String claimnumber) {
			SearchTab.click();
			ClaimSearchField.sendKeys(claimnumber);
			SearchButton.click();
			SelectClaimfromSearch.click();
		return SummaryTitlebar.isDisplayed();
	}

	public boolean verifySentMessage() {
		MessagingLink.click();
		return MessagingBody.getText().contentEquals(randommessage);
	}
}
