package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.ArrayList;

public class GWLoginPage extends BaseDriverClass {
    ConfigReader prop = Hooks.getConfigReader();

    @FindBy(id = "Login:LoginScreen:LoginDV:username")
    private WebElement usernameTextField;
    @FindBy(id = "Login:LoginScreen:LoginDV:password")
    private WebElement passwordTextField;
    @FindBy(id = "Login:LoginScreen:LoginDV:submit")
    private WebElement loginButton;
    @FindBy(id = "TabBar:LogoutTabBarLink")
    private WebElement GWCCLogout;

    public GWLoginPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public void loginToGWCC(String username, String password) {
        getDriver().switchTo().defaultContent();
        getDriver().switchTo().frame("top_frame");
        if(isDisplayed(usernameTextField)) {
            waitUntilElementVisible(usernameTextField);
            Enter(usernameTextField, username);
            Enter(passwordTextField, password);
            Click(loginButton);
        }
        else
        {
            Hooks.log("User has already logged in, login page is not displayed");
        }
    }

    public void GWCClogout() {
        Click(GWCCLogout);
    }

    public boolean verifyonGWCC() {
        if (System.getProperty("env").equalsIgnoreCase("BAT")) {
            getDriver().switchTo().frame("top_frame");
            return usernameTextField.isDisplayed();
        } else {
            getDriver().switchTo().defaultContent();
            getDriver().switchTo().frame("top_frame");
            return true;
        }
    }

    public ArrayList<String> switchtab() {
        ((JavascriptExecutor) getDriver()).executeScript("window.open();");
        ArrayList<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
        getDriver().get(prop.getProperty("GWURl"));
        return tabs;
    }
}
