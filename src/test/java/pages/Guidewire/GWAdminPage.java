package pages.Guidewire;

import definitions.Hooks;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;

import java.util.List;

public class GWAdminPage extends BaseDriverClass {
    ConfigReader prop = Hooks.getConfigReader();

    @FindBy(id = "TabBar:AdminTab")
    private WebElement AdminTab;
    @FindBy(id = "Admin:MenuLinks:Admin_PortalAuthorizationAdminPage")
    private WebElement PortalAuthorizationLink;
    @FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalUserSearchFieldsDV:SearchClaimNumberUserInput")
    private WebElement SearchClaimNumberUserInput;
    @FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalUserSearchFieldsDV:SearchUsersUserNameInput")
    private WebElement SearchUsersUserNameInput;
    @FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV:_Checkbox")
    private WebElement CheckboxAll;
    @FindBy(xpath = "//*[@id=\"PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalAuthUsersLV_tb:ToolbarButton\"]/span[2]")
    private WebElement SearchPortalAuth;
    @FindBy(xpath = "//table[@id='PortalAuthorizationAdminPage:PortalAuthAdminScreen:PortalAuthUsersLV']//tbody//tr[contains(@id, 'PortalAuthUsersLV:') and contains(@id, ':') and .//input[@type='checkbox']]//a[contains(@id, 'UserNameCell') and not(contains(@id, 'Header'))]")
    private WebElement EmailProfileLink;
    @FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserDV_tb:Edit")
    private WebElement EditButton;
    @FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV")
    private WebElement PortalAuthTable;
    @FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserGrantedAuthoritiesLV_tb:deleteAuthoritiesButton")
    private WebElement PortalAuthDeleteButton;
    @FindBy(id = "PortalAuthUserDetailPopup:PortalAuthUserDV_tb:Update")
    private WebElement PortalAuthOKButton;
    @FindBy(id = "PortalAuthorizationAdminPage:PortalAuthAdminScreen:0")
    private WebElement PortalAuthAdminScreen;

    public GWAdminPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean navigatePortalAuthScreen() {
        Click(AdminTab);
        Click(PortalAuthorizationLink);
        waitUntilElementVisible(SearchClaimNumberUserInput);
        return SearchClaimNumberUserInput.isDisplayed();
    }

    public boolean removeClaimfromPortalAuth(String claimnumber) {
        Enter(SearchClaimNumberUserInput, claimnumber);
        SearchPortalAuth.click();
        Click(EmailProfileLink);
        Click(EditButton);
        List<WebElement> rows = PortalAuthTable.findElements(By.tagName("tr"));
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            for (WebElement cell : cells) {
                if (cell.getText().equals(claimnumber)) {
                    WebElement checkbox = row.findElement(By.xpath(".//input[@type='checkbox']"));
                    Click(checkbox);
                    break;
                }
            }
        }
        Click(PortalAuthDeleteButton);
        Click(PortalAuthOKButton);
        waitUntilElementVisible(PortalAuthAdminScreen);
        return PortalAuthAdminScreen.isDisplayed();
    }

    public boolean removeAllClaimfromPortalAuth() {
        Enter(SearchUsersUserNameInput, prop.getProperty("OSPC_username"));
        Click(SearchPortalAuth);
        Click(EmailProfileLink);
        Click(EditButton);
        if (isDisplayed(CheckboxAll)) {
            Click(CheckboxAll);
            Click(PortalAuthDeleteButton);
        }
        Click(PortalAuthOKButton);
        waitUntilElementVisible(PortalAuthAdminScreen);
        return PortalAuthAdminScreen.isDisplayed();
    }
}
