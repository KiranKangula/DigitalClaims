package pages.CAA;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;

public class Clothingallowance extends BaseDriverClass {

	@FindBy(id = "telephoneNumber") private WebElement telephoneNumber;
	@FindBy(xpath = "(//input[@id='pdpNel'])[1]") private WebElement PDPNelYes;
	@FindBy(xpath = "(//input[@id='pdpNel'])[2]") private WebElement PDPNelNo;
	@FindBy(xpath = "(//input[@id='receivedCa'])[1]") private WebElement receivedCaYes;
	@FindBy(xpath = "(//input[@id='receivedCa'])[2]") private WebElement receivedCaNo;
	@FindBy(id = "otherClaimNumber") private WebElement otherClaimNumber;
	@FindBy(xpath = "(//input[@id='isEmployed'])[1]") private WebElement isEmployedYes;
	@FindBy(xpath = "(//input[@id='isEmployed'])[2]") private WebElement isEmployedNo;
	@FindBy(xpath = "(//input[@id='usesDeviceAtWork'])[1]") private WebElement usesDeviceAtWorkYes;
	@FindBy(xpath = "(//input[@id='usesDeviceAtWork'])[2]") private WebElement usesDeviceAtWorkNo;
	@FindBy(xpath = "(//input[@id='employerPaysForUniform'])[1]") private WebElement employerPaysForUniformYes;
	@FindBy(xpath = "(//input[@id='employerPaysForUniform'])[2]") private WebElement employerPaysForUniformNo;
	@FindBy(xpath = "//div[@id='uniformOverClothing']/label[contains(text(),'Over')]") private WebElement uniformOverClothing;
	@FindBy(xpath = "//div[@id='uniformOverClothing']/label[contains(text(),'Instead')]") private WebElement uniformInsteadClothing;
	@FindBy(id = "damage") private WebElement damage;
	@FindBy(id = "day") private WebElement day;
	@FindBy(id = "month") private WebElement month;
	@FindBy(id = "year") private WebElement year;
	@FindBy(id = "hoursPerDay") private WebElement hoursPerDay;
	@FindBy(id = "daysPerWeek") private WebElement daysPerWeek;
	@FindBy(id = "armBrace") private WebElement armBrace;
	@FindBy(id = "backBrace") private WebElement backBrace;
	@FindBy(id = "backSupport") private WebElement backSupport;
	@FindBy(id = "cervicalBrace") private WebElement cervicalBrace;
	@FindBy(id = "legBrace") private WebElement legBrace;
	@FindBy(id = "legProsthesis") private WebElement legProsthesis;
	@FindBy(id = "crutches") private WebElement crutches;
	@FindBy(id = "wheelChair") private WebElement wheelChair;
	@FindBy(id = "other") private WebElement otherDevice;

	@FindBy(xpath = "(//input[@id='metalParts'])[1]") private WebElement metalPartsYes;
	@FindBy(xpath = "(//input[@id='metalParts'])[2]") private WebElement metalPartsNo;
	@FindBy(xpath = "(//input[@id='repairOrReplaced'])[1]") private WebElement repairOrReplacedYes;
	@FindBy(xpath = "(//input[@id='repairOrReplaced'])[2]") private WebElement repairOrReplacedNo;

	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Next')]") private WebElement NextButton;
	@FindBy(xpath = "//h2[contains(text(),'Review')]") private WebElement ReviewHeader;
	@FindBy(id = "isEmailNeeded") private WebElement isEmailNeeded;
	@FindBy(id = "emailAddress") private WebElement emailAddress;
	@FindBy(id = "declared") private WebElement declaration;


	@FindBy(xpath = "//button[@class='btn-wsib'][contains(text(),'Submit')]") private WebElement SubmitButton;

	@FindBy(xpath = "//h2[contains(text(),'Submission was successful')]") private WebElement ConfirmationHeader;

	public Clothingallowance(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}

	public Boolean enterClothingAllowanceDetails(String telephone, String pdpNel, String receivedCa, String otherClaim, String isEmployed,
												 String usesDeviceAtWork, String employerPaysForUniform, String uniform,
												 String damage, String date, String hoursPerDay, String daysPerWeek,
												 String UpperBody, String LowerBody, String OtherDevice, String metalParts, String repairOrReplaced) throws InterruptedException {
		waitUntilElementVisible(telephoneNumber);
		Enter(telephoneNumber, telephone);
		if(pdpNel.equalsIgnoreCase("Yes")){
			Click(PDPNelYes);
		}else{
			Click(PDPNelNo);
		}
		if(receivedCa.equalsIgnoreCase("Yes")){
			Click(receivedCaYes);
			Enter(otherClaimNumber, otherClaim);
		}else{
			Click(receivedCaNo);
		}

		if(isEmployed.equalsIgnoreCase("Yes")){
			Click(isEmployedYes);
		}else{
			Click(isEmployedNo);
		}
		if(usesDeviceAtWork.equalsIgnoreCase("Yes")){
			Click(usesDeviceAtWorkYes);
		}else{
			Click(usesDeviceAtWorkNo);

		}
		if(employerPaysForUniform.equalsIgnoreCase("Yes")){
			Click(employerPaysForUniformYes);
		}else{
			Click(employerPaysForUniformNo);
		}
		if(uniform.equalsIgnoreCase("Over")){
			Click(uniformOverClothing);
		}else{
			Click(uniformInsteadClothing);
		}
		Enter(this.damage, damage);
		String[] dob = date.split("/");
		GWSelect(day, dob[0]);
		GWSelect(month, dob[1]);
		GWSelect(year, dob[2]);
		Enter(this.hoursPerDay, hoursPerDay);
		Enter(this.daysPerWeek, daysPerWeek);
		if (UpperBody.equalsIgnoreCase("Arm Brace")) {
			Click(armBrace);
		} else if (UpperBody.equalsIgnoreCase("Back Brace")) {
			Click(backBrace);
		} else if (UpperBody.equalsIgnoreCase("Back Support")) {
			Click(backSupport);
		} else if (UpperBody.equalsIgnoreCase("Cervical Brace")) {
			Click(cervicalBrace);
		}

		if(LowerBody.equalsIgnoreCase("Leg brace (permanent)")){
			Click(legBrace);
		}else if(LowerBody.equalsIgnoreCase("Leg Prothesis")){
			Click(legProsthesis);
		}else if(LowerBody.equalsIgnoreCase("Under arm/forearm supported crutches")){
			Click(crutches);
		} else if(LowerBody.equalsIgnoreCase("Wheelchair")) {
			Click(wheelChair);
		}

		if(!OtherDevice.isEmpty()){
			Enter(otherDevice, OtherDevice);
		}
		if(metalParts.equalsIgnoreCase("Yes")){
			Click(metalPartsYes);
			//Click(metalPartsYes);
		}else{
			Click(metalPartsNo);
		}

		if(repairOrReplaced.equalsIgnoreCase("Yes")){
			jsMoveClick(repairOrReplacedYes);
		}else{
			jsMoveClick(repairOrReplacedNo);
		}

		jsMoveClick(NextButton);
		return ReviewHeader.isDisplayed();

	}

	public boolean reviewAndSubmit(String email) {

		if(!email.isEmpty()){
			Click(isEmailNeeded);
			Enter(emailAddress, email);
		}
		Click(declaration);
		Click(SubmitButton);
		ConfirmationHeader.isDisplayed();
		return ConfirmationHeader.isDisplayed();
	}
}