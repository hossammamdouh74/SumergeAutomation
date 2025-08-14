package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Updated selector for Booking.com search field
    private final By searchBox = By.cssSelector("input[name='ss']");
    private final By searchButton = By.cssSelector("button[type='submit']");
    private final By searchResult = By.cssSelector("li[id='autocomplete-result-3'] div[role='button']");
    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void enterLocation(String location) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).clear();
        driver.findElement(searchBox).sendKeys(location);
        wait.until(ExpectedConditions.elementToBeClickable(searchResult)).click();
    }

    public void selectDates(String checkIn, String checkOut) {

        // navigate until check-in date appears
        while (driver.findElements(By.cssSelector( checkIn )).isEmpty()) {
            By nextButton = By.cssSelector("button[aria-label='Next month']");
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
        }

        driver.findElement(By.cssSelector( checkIn )).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector( checkOut))).click();
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }
}