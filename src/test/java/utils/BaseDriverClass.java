package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BaseDriverClass {
	private static WebDriver driver;
    private static WebDriverWait wait;

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
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].click();", element);
    }

    // Utility to send keys using JavascriptExecutor
    protected void jsSendKeys(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    // Utility to scroll to an element
    protected void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }


    protected WebDriver getDriver() {
        return driver;
    }
}
