package pages.Guidewire;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

import java.util.List;

public class GWAppealsPage extends BaseDriverClass {

    @FindBy(id = "Claim:MenuLinks:Claim_ClaimMattersGroup")
    private WebElement AppealsLink;
    @FindBy(id = "ClaimMatters:ClaimMatterScreen:ClaimMatters_NewMatterButton")
    private WebElement NewMatterButton;
    @FindBy(id = "NewMatter:NewMatterScreen:NewMatterDV:Matter_Name")
    private WebElement Matter_Name;
    @FindBy(id = "NewMatter:NewMatterScreen:NewMatterDV:Appealtype")
    private WebElement Appealtype;
    @FindBy(id = "NewMatter:NewMatterScreen:NewMatterDV:Objectingparty")
    private WebElement Objectingparty;
    @FindBy(id = "NewMatter:NewMatterScreen:NewMatterDV_tb:Update")
    private WebElement AppealsUpdateButton;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:Edit")
    private WebElement AppealsEditButton;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:OITandInitialDVTab")
    private WebElement AppealTab;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:WSIB_AppealTypeCase")
    private WebElement WSIB_AppealTypeCase;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:WSIB_NewAppealType")
    private WebElement WSIB_NewAppealType;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:language")
    private WebElement language;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:CaseRefferal")
    private WebElement CaseRefferal;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:otherparticipating")
    private WebElement otherparticipating;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:DualObj")
    private WebElement ObjOrigin;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:Update")
    private WebElement MatterDetailPage_update;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:ToolbarButton_AppealRequest")
    private WebElement CreateNumber;
    @FindBy(id = "MatterDetailPage:MatterDetailScreen:Matter_Name")
    private WebElement AppealNumber;

    @FindBy(id = "BatchProcessInfo:BatchProcessScreen:ttlBar")
    private WebElement BatchProcessInfoBar;
    @FindBy(id = "InternalToolsTabBar:ReturnTabBarLink")
    private WebElement ReturnToClaimCenter;

    public GWAppealsPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }

    public boolean createAppeal() {
        Click(AppealsLink);
        Click(NewMatterButton);
        Enter(Matter_Name, "AppealTest");
        new Select(Appealtype).selectByVisibleText("Appeal");
        new Select(Objectingparty).selectByIndex(1);
        Sleep(2000);
        Click(AppealsUpdateButton);
        waitUntilElementVisible(AppealsEditButton);
        Click(AppealsEditButton);
        Click(AppealTab);
        new Select(WSIB_AppealTypeCase).selectByVisibleText("Regular");
        Sleep(2000);
        new Select(WSIB_NewAppealType).selectByVisibleText("Claims");
        Sleep(2000);
        new Select(language).selectByVisibleText("English");
        Sleep(2000);
        new Select(CaseRefferal).selectByVisibleText("OD");
        Sleep(2000);
        new Select(otherparticipating).selectByVisibleText("No");
        Sleep(2000);
        new Select(ObjOrigin).selectByVisibleText("Worker");
        Sleep(2000);
        Click(CreateNumber);
        waitUntilElementVisible(AppealNumber);
        if (!AppealNumber.getText().isEmpty()) {
            Click(MatterDetailPage_update);
            return true;
        }
        return false;
    }

    public boolean runDIBatchJob(String batchNameToRun) throws InterruptedException {
        mimicAltShiftT(getDriver());
        waitUntilElementVisible(BatchProcessInfoBar);
        List<WebElement> rows = getDriver().findElements(By.xpath("//table[@id='BatchProcessInfo:BatchProcessScreen:BatchProcessesLV']/tbody/tr"));
        WebElement lastRunStatus = null;

        for (WebElement row : rows) {
            if (row.getText().contains(batchNameToRun)) {
                WebElement runButton = row.findElement(By.xpath(".//a[contains(@id, 'RunBatchWithoutNotify')]"));
                Click(runButton);
                Sleep(5000);

                for (int i = 0; i < 10; i++) {
                    try {
                        WebElement updatedRow = getDriver().findElement(By.xpath("//table[@id='BatchProcessInfo:BatchProcessScreen:BatchProcessesLV']/tbody/tr[.//span[text()='" + batchNameToRun + "']]"));
                        lastRunStatus = updatedRow.findElement(By.xpath(".//span[contains(@id, 'LastRunStatus')]"));
                        if (lastRunStatus.getText().equalsIgnoreCase("Completed")) break;
                    } catch (StaleElementReferenceException e) {}
                    Sleep(5000);
                }
                break;
            }
        }

        if (lastRunStatus != null && lastRunStatus.getText().equalsIgnoreCase("Completed")) {
            Click(ReturnToClaimCenter);
            return true;
        }
        return false;
    }
}
