package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HotelDetailsPage {
    private final WebDriver driver;

    private final By checkInDisplay = By.cssSelector("button[data-testid='date-display-field-start'] span[class='be2db1c937']");
    private final By checkOutDisplay = By.cssSelector("button[data-testid='date-display-field-end'] span[class='be2db1c937']");
    private final By reserveButton = By.xpath("(//span[@class='bui-button__text js-reservation-button__text'])[1]");
    private final By bedTypeSelector = By.cssSelector("input[value='2'][name='bedPreference_78883120']");
    private final By selectAmount = By.cssSelector("#hprt_nos_select_bbasic_0");

    public HotelDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Scrolls to the element, waits until it's clickable, and returns it.
     */
    private WebElement scrollToElement(By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));

        // Wait for presence in DOM
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        // Scroll into view
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        // Wait until clickable
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public String getCheckInDate() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(checkInDisplay))
                .getText();
    }

    public String getCheckOutDate() {
        return new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(checkOutDisplay))
                .getText();
    }

    private void selectBedTypeIfAvailable() {
        try {
            scrollToElement(bedTypeSelector, 5).click();
        } catch (TimeoutException e) {
            System.out.println("No specific bed selection found, skipping...");
        }
    }

    private void clickReserveButton() {
        scrollToElement(reserveButton, 10).click();
    }

    private void selectAmount() {
        scrollToElement(selectAmount, 10).click();
    }

    public void performReserve() {
        selectBedTypeIfAvailable();
        selectAmount();
        clickReserveButton();
    }
}
