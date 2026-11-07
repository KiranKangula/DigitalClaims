package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class BaseDriverClass {
	private WebDriver driver;
    private WebDriverWait wait;

    public BaseDriverClass(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public static String getProvinceAbbreviation(String province) {
        return switch (province.toLowerCase()) {
            case "alberta" -> "AB";
            case "british columbia" -> "BC";
            case "manitoba" -> "MB";
            case "new brunswick" -> "NB";
            case "newfoundland and labrador" -> "NL";
            case "nova scotia" -> "NS";
            case "ontario" -> "ON";
            case "prince edward island" -> "PE";
            case "quebec" -> "QC";
            case "saskatchewan" -> "SK";
            case "northwest territories" -> "NT";
            case "nunavut" -> "NU";
            case "yukon" -> "YT";
            default -> "";
        };
    }

    protected void waitUntilElementVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (StaleElementReferenceException e) {
            // If the element is stale, accessing it again (if it's a PageFactory element) might re-find it.
            // However, this is still risky. A better way is to use By locators.
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
            } catch (Exception ex) {
                System.out.println("Failed to wait for element even after retry: " + ex.getMessage());
            }
        }
    }

    /**
     * Robust wait that uses a By locator to find and wait for an element.
     * This is much more resistant to StaleElementReferenceException.
     */
    protected WebElement waitUntilElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static String getCurrentDate(String format)
    {
        // Changed from LocalDate to LocalDateTime to support time-based formatting
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDateTime.format(formatter);
    }


    public static String getDateFormat(String inputDate, String inFormat, String outFormat)
    {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inFormat);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outFormat);
        String formattedDate = "";
        try {
            Date date = inputFormat.parse(inputDate);
            formattedDate = outputFormat.format(date);
            System.out.println("Formatted Date: " + formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formattedDate;
    }


    public static String convertDate(String inputDate, String outputFormat) {
        if (inputDate == null || outputFormat == null) {
            throw new IllegalArgumentException(
                    "Date and output format must not be null.");
        }
        String[] inputFormats = {
                "uuuu-MM-dd",
                "uuuu/MM/dd",
                "dd/MM/uuuu",
                "MM/dd/uuuu",
                "dd-MM-uuuu",
                "MM-dd-uuuu",
                "dd.MM.uuuu",
                "d MMM uuuu",
                "d MMMM uuuu",
                "MMM d, uuuu",
                "MMMM d, uuuu"
        };
        DateTimeFormatter outputFormatter =
                DateTimeFormatter.ofPattern(outputFormat, Locale.ENGLISH);
        for (String pattern : inputFormats) {
            DateTimeFormatter inputFormatter =
                    DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
                            .withResolverStyle(ResolverStyle.STRICT);
            try {
                LocalDate date =
                        LocalDate.parse(inputDate.trim(), inputFormatter);
                return date.format(outputFormatter);
            } catch (DateTimeParseException ignored) {
                // Try the next input format.
            }
        }
        throw new IllegalArgumentException(
                "Invalid or unsupported date: " + inputDate);
    }

    public static String generateRandomText(int length) {
        int leftLimit = 97; // 'a'
        int rightLimit = 122; // 'z'
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomChar = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            builder.append((char) randomChar);
        }
        return builder.toString();
    }

    public static String generateRandomNumber(int length) {
        int leftLimit = 48; // '0'
        int rightLimit = 57; // '9'
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomChar = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            builder.append((char) randomChar);
        }
        return builder.toString();
    }

    // New robust click utility that tries multiple methods
    protected void Click(WebElement element) {
        int attempts = 0;
        boolean success = false;
        Exception lastException = null;
        while (attempts < 3) {
            try {
                // 1. Wait for element visibility & clickability (PageFactory re-locates on every call)
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                wait.ignoring(StaleElementReferenceException.class);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                // 2. Scroll element into center view to avoid header/footer/scrollbar interception
                try {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                            element
                    );
                } catch (Exception ignored) {}
                // 3. Try standard Selenium click
                element.click();
                success = true;
                break;
            } catch (Exception e) {
                lastException = e;
                System.out.println("Standard click failed (attempt " + (attempts + 1) + "): " + e.getMessage());
                // 4. Fallback 1: JavaScript Click
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    success = true;
                    break;
                } catch (Exception jsEx) {
                    lastException = jsEx;
                    System.out.println("JS click failed: " + jsEx.getMessage());
                    // 5. Fallback 2: Actions moveToElement + Click
                    try {
                        Actions actions = new Actions(driver);
                        actions.moveToElement(element).pause(Duration.ofMillis(200)).click().perform();
                        success = true;
                        break;
                    } catch (Exception actEx) {
                        lastException = actEx;
                        System.out.println("Actions click failed: " + actEx.getMessage());
                        // 6. Fallback 3: Guidewire native event trigger (mouse event dispatch)
                        try {
                            String gwClickScript =
                                    "var evt = new MouseEvent('click', {bubbles: true, cancelable: true, view: window});" +
                                            "arguments[0].dispatchEvent(evt);";
                            ((JavascriptExecutor) driver).executeScript(gwClickScript, element);
                            success = true;
                            break;
                        } catch (Exception dispatchEx) {
                            lastException = dispatchEx;
                        }
                    }
                }
            }
            attempts++;
            try {
                Thread.sleep(1000); // Allow DOM / Guidewire table to settle before retry
            } catch (InterruptedException ignored) {}
        }
        if (!success) {
            throw new RuntimeException("Failed to click element after 3 attempts. Last error: "
                    + (lastException != null ? lastException.getMessage() : "Unknown"), lastException);
        }
    }

    /**
     * Selects an option from a Guidewire Jutro / React-Select dropdown field.
     *
     * @param dropdownId e.g., "dropdownEmploymentType" or "dropdownEmploymentType-wrapper"
     * @param optionText The exact visible text of the option to select (e.g. "Full-time", "Part-time")
     */
    public void selectJutroDropdownOption(String dropdownId, String optionText) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        // Normalize ID in case wrapper or input ID is passed
        String wrapperId = dropdownId.endsWith("-wrapper") ? dropdownId : dropdownId + "-wrapper";
        // 1. Locate and click the dropdown control to open the menu
        By dropdownControlLocator = By.xpath(
                "//*[@id='" + wrapperId + "' or @id='" + dropdownId + "']" +
                        "//div[contains(@class, 'jut__GenericSelectControl__control')]"
        );
        WebElement dropdownControl = wait.until(ExpectedConditions.elementToBeClickable(dropdownControlLocator));
        // Scroll into view & click to expand
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'center'});", dropdownControl);
        dropdownControl.click();
        // 2. Locate the option from the opened Jutro menu listbox
        By optionLocator = By.xpath(
                "//div[contains(@class, 'jut__GenericSelectControl__menu') or contains(@class, 'jut__DropdownSelectField__menu')]" +
                        "//div[contains(@class, 'jut__GenericSelectControl__option') or @role='option'][normalize-space(.)='" + optionText + "']"
        );
        try {
            WebElement optionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));
            wait.until(ExpectedConditions.elementToBeClickable(optionElement));
            optionElement.click();
        } catch (Exception e) {
            // Fallback: If option is outside visible menu area, scroll inside menu or try broad text match
            By fallbackOptionLocator = By.xpath(
                    "//div[contains(@class, 'jut__GenericSelectControl__menu')]//*[normalize-space(text())='" + optionText + "']"
            );
            WebElement fallbackOption = wait.until(ExpectedConditions.presenceOfElementLocated(fallbackOptionLocator));
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'nearest'});", fallbackOption);
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", fallbackOption);
        }
    }

    // Robust sendKeys utility that supports Standard, Angular, Masked, and Google Places Autocomplete fields
    protected void Enter(WebElement element, String value) {
        if (element == null || value == null) {
            throw new IllegalArgumentException("WebElement and value cannot be null");
        }

        int attempts = 0;
        boolean success = false;
        Keys controlKey = System.getProperty("os.name").toLowerCase().contains("mac") ? Keys.COMMAND : Keys.CONTROL;

        while (attempts < 3) {
            try {
                waitUntilElementVisible(element);
                String classAttr = element.getAttribute("class");
                boolean isMaskedField = classAttr != null && classAttr.contains("InputMaskField");
                boolean isGooglePlaces = classAttr != null && classAttr.contains("pac-target-input");

                String textToSend = value;
                if (isMaskedField && value.matches(".*\\d.*")) {
                    textToSend = value.replaceAll("[^0-9a-zA-Z]", "");
                }

                // ==========================================
                // OPTION 1: Standard Clear & SendKeys
                // ==========================================
                try {
                    element.click();
                    element.sendKeys(Keys.chord(controlKey, "a"), Keys.BACK_SPACE);
                    element.sendKeys(textToSend);

                    if (isGooglePlaces) {
                        Sleep(500);
                        element.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                    } else {
                        element.sendKeys(Keys.TAB); // Preserves standard blur/validation for regular apps
                    }

                    if (isInputValuePopulated(element)) {
                        success = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Option 1 failed: " + e.getMessage());
                }

                // ==========================================
                // OPTION 2: Slow Typing
                // ==========================================
                try {
                    System.out.println("Trying Option 2 (Slow Keystroke typing)...");
                    element.click();
                    element.sendKeys(Keys.chord(controlKey, "a"), Keys.BACK_SPACE);
                    for (char ch : textToSend.toCharArray()) {
                        element.sendKeys(String.valueOf(ch));
                        Sleep(40);
                    }

                    if (isGooglePlaces) {
                        Sleep(500);
                        element.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                    } else {
                        element.sendKeys(Keys.TAB);
                    }

                    if (isInputValuePopulated(element)) {
                        success = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Option 2 failed: " + e.getMessage());
                }

                // ==========================================
                // OPTION 3: Universal JS Setter (Angular + React + Standard)
                // ==========================================
                try {
                    System.out.println("Trying Option 3 (Universal JS fallback)...");
                    setUniversalInputValueJs(element, textToSend, isGooglePlaces);
                    if (isInputValuePopulated(element)) {
                        success = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Option 3 failed: " + e.getMessage());
                }

                attempts++;
                Sleep(500);
            } catch (StaleElementReferenceException e) {
                attempts++;
                Sleep(500);
            } catch (Exception e) {
                attempts++;
                Sleep(500);
            }
        }

        if (!success) {
            throw new RuntimeException("Failed to enter value '" + value + "' after 3 attempts.");
        }
    }

    private void setUniversalInputValueJs(WebElement element, String value, boolean isGooglePlaces) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Dispatches events for Angular & React, only blurs if not an autocomplete field
        String script =
                "var input = arguments[0];" +
                        "var val = arguments[1];" +
                        "var isGooglePlaces = arguments[2];" +
                        "input.focus();" +
                        "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value');" +
                        "if (setter && setter.set) { setter.set.call(input, val); } else { input.value = val; }" +
                        "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                        "input.dispatchEvent(new Event('change', { bubbles: true }));" +
                        "if (!isGooglePlaces) { input.dispatchEvent(new Event('blur', { bubbles: true })); }";
        js.executeScript(script, element, value, isGooglePlaces);
    }

    /**
     * Checks if the input element actually received the text value.
     */
    private boolean isInputValuePopulated(WebElement element) {
        try {
            String val = element.getAttribute("value");
            return val != null && !val.trim().isEmpty() && !val.equals("__-___-____");
        } catch (Exception e) {
            return false;
        }
    }
    // Utility to click using JavascriptExecutor
    protected void jsClick(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
                break;
            } catch (Exception e) {
                attempts++;
                if (attempts == 3) throw e;
                Sleep(1000);
            }
        }
    }

    // Utility to send keys using JavascriptExecutor
    protected void jsSendKeys(WebElement element, String value) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value='" + value + "';", element);
                break;
            } catch (Exception e) {
                attempts++;
                if (attempts == 3) throw e;
                Sleep(1000);
            }
        }
    }

    // Utility to send keys using Actions and JavascriptExecutor
    protected void jsMoveSendKeys(WebElement element, String value) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value='';", element);
                new Actions(getDriver()).moveToElement(element).click().sendKeys(value).perform();
                break;
            } catch (Exception e) {
                attempts++;
                if (attempts == 3) throw e;
                Sleep(1000);
            }
        }
    }

    // Utility to send keys using Actions and JavascriptExecutor
    protected void jsMoveClick(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                new Actions(getDriver()).moveToElement(element).click().perform();
                break;
            } catch (Exception e) {
                attempts++;
                if (attempts == 3) throw e;
                Sleep(1000);
            }
        }
    }

    // Utility to scroll to an element
    protected void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void DynamicTextClick(String text )  {
        String dynamicXPath = "//*[contains(text(),'" + text + "')]";
        WebElement Element = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
       Sleep(2000);
        Element.click();
    }

    protected void ClickusingActions(String element) {
        Actions actions = new Actions(getDriver());
        actions.sendKeys(element).build().perform();
        actions.sendKeys(Keys.ENTER).build().perform();
    }
    // Utility to verify if the element is displayed
    protected boolean isDisplayed(WebElement element) {
        if (element == null) {
            return false;
        }
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    // Utility to select partial value from the dropdown with retry and JS fallback
    protected boolean Select(WebElement element, String value) {
        int attempts = 0;
        boolean isSelected = false;

        while (attempts < 3) {
            try {
                System.out.println("Attempting to select '" + value + "'. Attempt: " + (attempts + 1));

                // 1. Click the dropdown to open it
                try {
                    element.click();
                } catch (Exception e) {
                    System.out.println("Standard click on dropdown failed, trying JS click...");
                    jsClick(element);
                }
                Sleep(1000); // Wait for dropdown to open

                // 2. Locate the option element
                // Use a shorter wait for finding the option, as the dropdown should be open
                String dynamicXPath = "//*[contains(text(),'" + value + "')]";
                WebElement optionElement = new WebDriverWait(getDriver(), Duration.ofSeconds(5))
                        .until(ExpectedConditions.presenceOfElementLocated(By.xpath(dynamicXPath)));

                // 3. Try standard click on option first, then JS fallback
                try {
                    optionElement.click();
                } catch (Exception clickEx) {
                    System.out.println("Standard click on option failed, trying JS click...");
                    jsClick(optionElement);
                }

               Sleep(1000); // Wait for selection to process

                // 4. Verification: Check if the displayed text matches the expected value
                // or if the element's 'value' attribute contains the expected value
                String actualText = element.getText();
                if (actualText.contains(value) || isSelectedViaAttribute(element, value)) {
                    System.out.println("Successfully selected: " + value);
                    isSelected = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("Exception during selection attempt " + (attempts + 1) + ": " + e.getMessage());
            }
            attempts++;
        }

        if (!isSelected) {
            System.err.println("Failed to select '" + value + "' after 3 attempts.");
        }
        return isSelected;
    }

    /**
     * Specialized select for standard HTML <select> tags, typically used in Guidewire ClaimCenter.
     * Uses the Selenium Select class with retry logic.
     */
    protected boolean GWSelect(WebElement element, String value) {
        int attempts = 0;
        boolean isSelected = false;

        while (attempts < 3) {
            try {
                System.out.println("Attempting Select for '" + value + "'. Attempt: " + (attempts + 1));
                
                // Wait for element to be visible
                waitUntilElementVisible(element);
                
                Select select = new Select(element);
                
                // Try selecting by visible text (common for Guidewire)
                try {
                    select.selectByVisibleText(value);
                } catch (NoSuchElementException e) {
                    // Fallback to partial text match if exact match fails
                    System.out.println("Exact visible text match failed, trying partial match...");
                    boolean partialFound = false;
                    for (WebElement option : select.getOptions()) {
                        if (option.getText().toLowerCase().contains(value.toLowerCase())) {
                            select.selectByVisibleText(option.getText());
                            partialFound = true;
                            break;
                        }
                    }
                    if (!partialFound) throw e;
                }

                isSelected = true;
                System.out.println("Successfully selected in : " + value);
                break;
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element during Select, retrying...");
            } catch (Exception e) {
                System.out.println( "Select failed on attempt " + (attempts + 1) + ": " + e.getMessage());
            }
            attempts++;
            Sleep(1000);
        }

        if (!isSelected) {
            System.err.println("Failed to select '" + value + "' after 3 attempts.");
        }
        return isSelected;
    }

    // Helper to check if value is selected via attribute (common in Guidewire)
    private boolean isSelectedViaAttribute(WebElement element, String value) {
        try {
            // This is typically for <select> elements where the 'value' attribute of the <select>
            // itself changes, or for input fields that get populated.
            String val = element.getAttribute("value");
            return val != null && val.contains(value);
        } catch (Exception e) {
            return false;
        }
    }

    protected void selectRadioButtonOption(String fieldId, String option) {
        int attempts = 0;
        boolean isSelected = false;
        String dynamicXPath = "//*[@id='" + fieldId + "']//label/span[contains(text(),'" + option + "')]";

        while (attempts < 3) {
            try {
                WebElement radioButtonOption = new WebDriverWait(getDriver(), Duration.ofSeconds(10))
                        .until(ExpectedConditions.elementToBeClickable(By.xpath(dynamicXPath)));
                
                try {
                    radioButtonOption.click();
                } catch (Exception clickEx) {
                    jsClick(radioButtonOption);
                }
                
                // Add a small wait to let the selection reflect in the UI
                Sleep(1000);
                
                // You might want to add a verification step here to set isSelected = true
                // For now, if no exception is thrown, we assume success
                isSelected = true;
                break;
            } catch (Exception e) {
                attempts++;
                System.out.println("Attempt " + attempts + " to select radio button '" + option + "' failed: " + e.getMessage());
            }
        }
        
        if (!isSelected) {
            System.err.println("Failed to select radio button option '" + option + "' after 3 attempts.");
        }
    }

    protected void selectDropdownValue(WebElement element, String value) {
        if (element == null || value == null) {
            System.err.println("Dropdown element or target value cannot be null.");
            return;
        }

        // 0. Handle standard HTML <select> tag if encountered
        if ("select".equalsIgnoreCase(element.getTagName())) {
            try {
                Select select = new Select(element);
                select.selectByVisibleText(value);
                return;
            } catch (Exception e) {
                System.out.println("Standard Select failed: " + e.getMessage());
            }
        }

        int attempts = 0;
        boolean isSelected = false;
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(4));

        // Normalize target text: strip punctuation (dots, parens, hyphens) and collapse whitespace
        String cleanTarget = normalizeText(value);

        while (attempts < 3) {
            try {
                // 1. Scroll into view
                js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);

                // 2. Locate the clickable control area (Jutro / React-Select control wrapper)
                WebElement clickableControl = element;
                try {
                    // If the element passed is the dummy input, navigate up to the control wrapper
                    List<WebElement> controlWrappers = element.findElements(By.xpath(
                            "./ancestor-or-self::div[contains(@class, 'jut__GenericSelectControl__control') or " +
                                    "contains(@class, '__control') or " +
                                    "contains(@class, '-control')][1]"
                    ));
                    if (!controlWrappers.isEmpty()) {
                        clickableControl = controlWrappers.get(0);
                    } else {
                        List<WebElement> childControls = element.findElements(By.xpath(
                                ".//div[contains(@class, 'jut__GenericSelectControl__control') or contains(@class, '-control')]"
                        ));
                        if (!childControls.isEmpty()) {
                            clickableControl = childControls.get(0);
                        }
                    }
                } catch (Exception ignored) {}

                // 3. Open dropdown using mousedown/click events (React-Select listens to mousedown)
                try {
                    new Actions(getDriver()).moveToElement(clickableControl).click().perform();
                } catch (Exception e) {
                    js.executeScript(
                            "var el = arguments[0];" +
                                    "el.dispatchEvent(new MouseEvent('mousedown', {bubbles: true, cancelable: true, view: window}));" +
                                    "el.dispatchEvent(new MouseEvent('mouseup', {bubbles: true, cancelable: true, view: window}));" +
                                    "el.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));",
                            clickableControl
                    );
                }

                // 4. Wait for menu to appear in DOM
                By menuLocator = By.xpath(
                        "//div[contains(@class, 'jut__GenericSelectControl__menu') or " +
                                "contains(@class, 'jut__DropdownSelectField__menu') or " +
                                "contains(@class, '-menu') or " +
                                "@role='listbox']"
                );

                WebElement menu;
                try {
                    menu = wait.until(ExpectedConditions.visibilityOfElementLocated(menuLocator));
                } catch (TimeoutException te) {
                    // Retry opening with JavaScript dispatch if not opened yet
                    js.executeScript(
                            "var el = arguments[0];" +
                                    "el.dispatchEvent(new MouseEvent('mousedown', {bubbles: true, cancelable: true, view: window}));" +
                                    "el.dispatchEvent(new MouseEvent('mouseup', {bubbles: true, cancelable: true, view: window}));" +
                                    "el.dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true, view: window}));",
                            clickableControl
                    );
                    menu = wait.until(ExpectedConditions.presenceOfElementLocated(menuLocator));
                }

                // 5. Target actual option items ONLY (exclude generic container divs)
                List<WebElement> options = menu.findElements(By.xpath(
                        ".//*[(contains(@class, 'option') or contains(@class, 'Option') or @role='option') and not(contains(@class, 'menu'))]"
                ));

                // If none found with standard option classes, fallback to direct children of listbox/menu list
                if (options.isEmpty()) {
                    options = menu.findElements(By.xpath(".//div[contains(@class, '-menu-list')]/* | .//div[@role='listbox']/*"));
                }

                WebElement exactMatch = null;
                WebElement partialMatch = null;

                for (WebElement opt : options) {
                    String optText = opt.getText();
                    if (optText == null || optText.trim().isEmpty()) {
                        optText = (String) js.executeScript("return arguments[0].textContent;", opt);
                    }
                    if (optText != null && !optText.trim().isEmpty()) {
                        String cleanOpt = normalizeText(optText);

                        // Prefer exact match first
                        if (cleanOpt.equals(cleanTarget)) {
                            exactMatch = opt;
                            break;
                        }
                        // Retain partial match only if exact isn't found
                        if (partialMatch == null && (cleanOpt.contains(cleanTarget) || cleanTarget.contains(cleanOpt))) {
                            partialMatch = opt;
                        }
                    }
                }

                WebElement matchingOption = (exactMatch != null) ? exactMatch : partialMatch;

                if (matchingOption != null) {
                    js.executeScript("arguments[0].scrollIntoView({block: 'nearest'});", matchingOption);
                    try {
                        matchingOption.click();
                    } catch (Exception clickEx) {
                        js.executeScript("arguments[0].click();", matchingOption);
                    }
                    isSelected = true;
                    break;
                } else {
                    System.out.println("Could not find matching option for '" + value + "'. Available options:");
                    for (WebElement opt : options) {
                        String text = opt.getText().trim();
                        if (!text.isEmpty()) {
                            System.out.println(" -> " + text);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Attempt " + (attempts + 1) + " failed: " + e.getMessage());
            }

            attempts++;
            try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        }

        if (!isSelected) {
            throw new RuntimeException("Failed to select dropdown value '" + value + "' after 3 attempts.");
        }
    }

    /**
     * Normalizes string for reliable text comparison:
     * Removes punctuation, special characters (e.g., live(d) -> live d), and collapses spaces.
     */
    private String normalizeText(String input) {
        if (input == null) return "";
        return input.toLowerCase()
                .replaceAll("[()\\.,\\-'\"]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected void mimicAltShiftT(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.ALT)
                .keyDown(Keys.SHIFT)
                .sendKeys("t")
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.ALT)
                .perform();
    }

    protected void DatePicker(String uniqueInputId, String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/MMM/yyyy", Locale.ENGLISH);
        LocalDate targetDate = LocalDate.parse(dateString, inputFormatter);

        String targetYear = String.valueOf(targetDate.getYear());
        String targetMonthValue = String.valueOf(targetDate.getMonthValue() - 1);
        String targetDay = String.valueOf(targetDate.getDayOfMonth());

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Locate the specific input field using the uniqueInputId
        WebElement inputField = driver.findElement(By.id(uniqueInputId));

        // 2. Locate the Calendar Icon relative to the input field
        WebElement calendarIconBox = inputField.findElement(By.xpath("./ancestor::div[contains(@class, 'wsib-date-picker_datePickerRow__2bX8k')]/div[@role='button']"));

        // Wait for the calendar icon to be clickable
        wait.until(ExpectedConditions.elementToBeClickable(calendarIconBox));
        calendarIconBox.click();

        // 3. Wait for the main calendar container modal to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker-popper")));
        System.out.println("Calendar picker is visible for input: " + uniqueInputId);

        // 4. Locate the Year and Month Select elements
        WebElement yearSelectElement = driver.findElement(By.cssSelector(".wsib-date-picker_yearSelect__1LIag"));
        WebElement monthSelectElement = driver.findElement(By.cssSelector(".wsib-date-picker_monthSelect__3GLvO"));

        Select yearDropdown = new Select(yearSelectElement);
        Select monthDropdown = new Select(monthSelectElement);

        // 5. Select the target Year and Month
        yearDropdown.selectByValue(targetYear);
        monthDropdown.selectByValue(targetMonthValue);

        // Wait for the UI to update
        String expectedMonthName = targetDate.format(DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".wsib-date-picker_monthSelect__3GLvO option[value='" + targetMonthValue + "']"),
                expectedMonthName
        ));

        // 6. Select the target day
        String dayLocatorXPath = String.format("//div[@role='option' and contains(@aria-label, 'Choose %s, %s %s')]",
                targetDate.getDayOfWeek().toString().toLowerCase().substring(0, 1).toUpperCase() + targetDate.getDayOfWeek().toString().toLowerCase().substring(1),
                targetDate.getMonth().toString().toLowerCase().substring(0, 1).toUpperCase() + targetDate.getMonth().toString().toLowerCase().substring(1),
                targetDate.getDayOfMonth());
        WebElement targetDayElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayLocatorXPath)));
        targetDayElement.click();

        System.out.println("Selected day: " + targetDay);

        // 7. Validate the result in the specific input field
        String expectedUIFormat = targetDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        wait.until(ExpectedConditions.attributeToBe(inputField, "value", expectedUIFormat));
        System.out.println("Successfully selected date for " + uniqueInputId + ": " + inputField.getAttribute("value"));
    }

        protected void Sleep(int milliseconds) {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    public List<String> uploadDocuments(String filenamesString) throws IOException {
        List<String> originalFilenames = Arrays.stream(filenamesString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<String> timestampedAbsolutePaths = new ArrayList<>();
        List<String> timestampedFilenames = new ArrayList<>();

        // Generate a unique timestamp for the batch
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS"));

        for (int i = 0; i < originalFilenames.size(); i++) {
            String originalFilename = originalFilenames.get(i);
            Path originalFilePath = Paths.get("src/test/resources/DocUploadFiles", originalFilename);

            // Extract filename without extension and its extension
            String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));

            // Create new filename with timestamp and index to ensure uniqueness within the batch
            String newFilename = fileNameWithoutExtension + "_" + timestamp + "_" + i + fileExtension;
            Path newFilePath = Paths.get("src/test/resources/DocUploadFiles", newFilename);

            // Copy the original file to the new timestamped file path, overwriting if exists
            Files.copy(originalFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Copied original file '" + originalFilename + "' to '" + newFilename + "'");

            timestampedAbsolutePaths.add(newFilePath.toAbsolutePath().toString());
            timestampedFilenames.add(newFilename);
        }

        WebElement input = getDriver().findElement(By.cssSelector("input[type='file']"));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].style.display = 'block';", input);

        // Join the timestamped absolute paths with newline characters for multiple file upload
        String filePaths = String.join("\n", timestampedAbsolutePaths);
        input.sendKeys(filePaths);

        // Return the list of timestamped filenames for further use
        return timestampedFilenames;
    }


}
