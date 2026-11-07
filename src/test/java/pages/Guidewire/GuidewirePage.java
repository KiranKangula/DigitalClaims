package pages.Guidewire;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

/**
 * @deprecated This class has been split into smaller, logical page objects:
 * <ul>
 *     <li>{@link GWLoginPage}</li>
 *     <li>{@link GWSearchPage}</li>
 *     <li>{@link GWClaimRegistrationPage}</li>
 *     <li>{@link GWClaimParticipantsPage}</li>
 *     <li>{@link GWDocumentsPage}</li>
 *     <li>{@link GWAdminPage}</li>
 *     <li>{@link GWRecoveryPage}</li>
 *     <li>{@link GWAppealsPage}</li>
 * </ul>
 */
@Deprecated
public class GuidewirePage extends BaseDriverClass {
    public GuidewirePage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
        PageFactory.initElements(driver, this);
    }
}
