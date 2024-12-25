package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BaseDriverClass {
	private WebDriver driver;
    private WebDriverWait wait;

    public BaseDriverClass(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    protected void waitUntilElementVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected String getCurrentDate(String format)
    {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return currentDate.format(formatter);
    }

    protected String generateRandomText() {
        int leftLimit = 97; // 'a'
        int rightLimit = 122; // 'z'
        int length = 15;
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomChar = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            builder.append((char) randomChar);
        }
        return builder.toString();
    }

    // Utility to click using JavascriptExecutor
    protected void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    // Utility to send keys using JavascriptExecutor
    protected void jsSendKeys(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    // Utility to send keys using Actions and JavascriptExecutor
    protected void jsMoveSendKeys(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='';", element);
        new Actions(getDriver()).moveToElement(element).click().sendKeys(value).perform();
    }

    // Utility to scroll to an element
    protected void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void DynamicTextClick(String text ) throws InterruptedException {
        String dynamicXPath = "//*[contains(text(),'" + text + "')]";
        WebElement Element = new WebDriverWait(getDriver(), Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));
        Thread.sleep(2000);
        Element.click();
    }

    protected void ClickusingActions(String element) {
        Actions actions = new Actions(getDriver());
        actions.sendKeys(element).build().perform();
        actions.sendKeys(Keys.ENTER).build().perform();
    }


    protected WebDriver getDriver() {
        return driver;
    }
}
