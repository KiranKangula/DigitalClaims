package pages.OSPC;

import definitions.Hooks;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseDriverClass;
import utils.ConfigReader;
import utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class Tombstone extends BaseDriverClass {
	ConfigReader prop= Hooks.getConfigReader();

	@FindBy(id = "contactInfoPartTwo") private WebElement contactInfoLink;
	@FindBy(id = "contactInformation") private WebElement contactInformationHeading;
	@FindBy(id = "contactFullName") private WebElement contactFullName;
	@FindBy(id = "addressPlaceholder") private WebElement addressPlaceholder;
	@FindBy(id = "phonePlaceholder") private WebElement phonePlaceholder;
	@FindBy(id = "editMailingAddressLink") private WebElement editMailingAddressLink;
	@FindBy(id = "editContactHeader") private WebElement editContactHeader;
	@FindBy(id = "startChangeButton") private WebElement startChangeButton;
	@FindBy(id = "mailingAddressSubheader") private WebElement mailingAddressSubheader;
	@FindBy(id = "inputAddressLine1") private WebElement inputAddressLine1;
	@FindBy(id = "inputCity") private WebElement inputCity;
	@FindBy(id = "dropdownProvince-wrapper") private WebElement dropdownProvince;
	@FindBy(id = "inputPostalCode") private WebElement inputPostalCode;
	@FindBy(id = "dropdownCountry-wrapper") private WebElement dropdownCountry;
	@FindBy(xpath = "//*[@id=\"divConcentContainer\"]/div/div/div[1]/span") private WebElement AddressCheckbox;
	@FindBy(id = "submitButton") private WebElement addressSubmitButton;
	@FindBy(id = "successfulbannerCotainerInfo") private WebElement successfulbannerCotainerInfo;

	@FindBy(id = "editPhoneNumberLink") private WebElement editPhoneNumberLink;
	@FindBy(id = "inputPhoneNumber") private WebElement inputPhoneNumber;
	@FindBy(xpath = "//*[@id=\"acknWrapper\"]/div[1]/div/div[1]/span") private WebElement PhoneCheckbox;
	@FindBy(id = "submitBtn") private WebElement phoneSubmitButton;


	public Tombstone(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
		PageFactory.initElements(driver, this);
	}


	public boolean clickContactInfoLink() {
		waitUntilElementVisible(contactInfoLink);
		Click(contactInfoLink);

		waitUntilElementVisible(contactInformationHeading);
		return contactInformationHeading.isDisplayed();
	}

	public boolean verifyFullName() {
		waitUntilElementVisible(contactFullName);
		return contactFullName.isDisplayed();
	}

	public boolean verifyAddress() {
		waitUntilElementVisible(addressPlaceholder);
		return addressPlaceholder.isDisplayed();
	}
	public boolean verifyPhoneNumber() {
		waitUntilElementVisible(phonePlaceholder);
		return phonePlaceholder.isDisplayed();
	}

	public boolean clickEditMailingAddress() {
		waitUntilElementVisible(editMailingAddressLink);
		Click(editMailingAddressLink);

		waitUntilElementVisible(editContactHeader);
		waitUntilElementVisible(startChangeButton);
		Click(startChangeButton);

		waitUntilElementVisible(mailingAddressSubheader);
		return mailingAddressSubheader.isDisplayed();
	}

	public boolean updateMailingAddress() {
		int size = Hooks.currentRow.get().get("Address Line1").split("-").length;
		String addressline1 = generateRandomNumber(3)+"-"+ Hooks.currentRow.get().get("Address Line1").split("-")[size-1];
		String city = Hooks.currentRow.get().get("City");
		String province = Hooks.currentRow.get().get("Province");
		String postalcode = Hooks.currentRow.get().get("Postal Code");
		String country = Hooks.currentRow.get().get("Country");
		waitUntilElementVisible(inputAddressLine1);
		Enter(inputAddressLine1, addressline1);
		Enter(inputCity, city);
		//selectDropdownValue(dropdownProvince, province);
		Enter(inputPostalCode, postalcode);
		//selectDropdownValue(dropdownCountry, country);
		Assert.assertTrue(dropdownCountry.getText().contains(country));
		
		scrollToElement(AddressCheckbox);
		Click(AddressCheckbox);
		
		scrollToElement(addressSubmitButton);
		try {
			Click(addressSubmitButton);
		} catch (Exception e) {
			jsClick(addressSubmitButton);
		}

		try {
			waitUntilElementVisible(successfulbannerCotainerInfo);
		} catch (TimeoutException e) {
			// If banner doesn't appear, check if we're still on the input screen (implies error)
			if (isDisplayed(inputAddressLine1)) {
				Hooks.log("Mailing Address update failed. Successful banner did not appear.");
				return false;
			}
		}

		// Update the current row data with the new values
		Map<String, String> currentRowData = Hooks.currentRow.get();
		currentRowData.put("Address Line1", addressline1);

		String sheetName = Hooks.sheetName.get();
		JsonUtils.writeJsonData(sheetName, Hooks.data.get());
		Hooks.currentRow.set(currentRowData);

		String provinceAbbreviation = province; // Default to the full name if no match is found

// Map of province names to their abbreviations
		Map<String, String> provinceMap = new HashMap<>();
		provinceMap.put("Alberta", "AB");
		provinceMap.put("British Columbia", "BC");
		provinceMap.put("Manitoba", "MB");
		provinceMap.put("New Brunswick", "NB");
		provinceMap.put("Newfoundland and Labrador", "NL");
		provinceMap.put("Nova Scotia", "NS");
		provinceMap.put("Ontario", "ON");
		provinceMap.put("Prince Edward Island", "PE");
		provinceMap.put("Quebec", "QC");
		provinceMap.put("Saskatchewan", "SK");
		provinceMap.put("Northwest Territories", "NT");
		provinceMap.put("Nunavut", "NU");
		provinceMap.put("Yukon", "YT");

// Get the abbreviation for the province
		if (provinceMap.containsKey(province)) {
			provinceAbbreviation = provinceMap.get(province);
		}
		Sleep(3000);
		// Normalize strings by trimming and replacing multiple whitespace/newlines with a single space
		String expectedAddress = (addressline1 + ", " + city + ", " + provinceAbbreviation + " " + postalcode).trim().replaceAll("\\s+", " ");
		String actualAddress = addressPlaceholder.getText().trim().replaceAll("\\s+", " ");


		if(expectedAddress.equalsIgnoreCase(actualAddress)) {
			Hooks.log("Mailing address updated successfully in the application.");
			return true;
		} else {
			Hooks.log("Mailing address update failed in the application.");
			Hooks.log("Address Expected: [" + expectedAddress + "]");
			Hooks.log("Address Actual  : [" + actualAddress + "]");
			return false;
		}

	}

	public boolean clickEditPhoneNumber() {
		waitUntilElementVisible(editPhoneNumberLink);
		Click(editPhoneNumberLink);
		waitUntilElementVisible(inputPhoneNumber);
		return inputPhoneNumber.isDisplayed();
	}

	public boolean updatePhoneNumber() {
		String phoneNumber = generateRandomNumber(10);
		waitUntilElementVisible(inputPhoneNumber);
		jsMoveSendKeys(inputPhoneNumber, phoneNumber);
		
		scrollToElement(PhoneCheckbox);
		Click(PhoneCheckbox);
		
		scrollToElement(phoneSubmitButton);
		try {
			Click(phoneSubmitButton);
		} catch (Exception e) {
			jsClick(phoneSubmitButton);
		}

		try {
			waitUntilElementVisible(successfulbannerCotainerInfo);
			// Update the current row data with the phone number
			Map<String, String> currentRowData = Hooks.currentRow.get();
			currentRowData.put("QPhoneNumber", phoneNumber);
			phoneNumber = phoneNumber.substring(0,3)+"-"+phoneNumber.substring(3,6)+"-"+phoneNumber.substring(6);
			currentRowData.put("Phone Number", phoneNumber);


			String sheetName = Hooks.sheetName.get();
			JsonUtils.writeJsonData(sheetName, Hooks.data.get());
			Hooks.currentRow.set(currentRowData);

		} catch (TimeoutException e) {
			if (isDisplayed(inputPhoneNumber)) {
				Hooks.log("Phone Number update failed. Successful banner did not appear.");
				return false;
			}
		}
		Sleep(4000);


		String actualPhone = phonePlaceholder.getText().trim().replaceAll("\\s+", " ");
		if(actualPhone.contains(phoneNumber)) {
			Hooks.log("Phone number updated successfully in the application.");
			return true;
		} else {
			Hooks.log("Phone number update failed in the application.");
			return false;
		}
	}
}
