package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private static WebDriver driver;
    private final WebDriverWait wait;

   /**
    * Locators
    */
    private final By searchBox = By.cssSelector("input[name='ss']"); // Booking.com search input
    private final By searchButton = By.cssSelector("button[type='submit']"); // Search button
    private final By searchResult = By.cssSelector("li[id='autocomplete-result-3'] div[role='button']"); // Autocomplete suggestion for location

    public HomePage(WebDriver driver) {
        HomePage.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds explicit wait
    }

    /**
     * Enters the location into the search box and selects the suggested result
     * @param location city or hotel name
     */
    public void enterLocation(String location) {
        // ✅ Accept cookies popup if shown
        acceptCookiesIfPresent();

        // Wait for search box to be visible, clear it, and type the location
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox)).clear();
        driver.findElement(searchBox).sendKeys(location);

        // Wait for autocomplete result to be clickable and select it
        wait.until(ExpectedConditions.elementToBeClickable(searchResult)).click();
    }

    /**
     * Selects check-in and check-out dates in the calendar widget
     * @param checkIn date in format recognized by Booking.com (e.g., "Monday, August 19, 2025")
     * @param checkOut date in same format
     */
    public void selectDates(String checkIn, String checkOut) {
        // Navigate calendar until check-in date is visible
        while (driver.findElements(By.cssSelector("span[aria-label='" + checkIn + "']")).isEmpty()) {
            By nextButton = By.cssSelector("button[aria-label='Next month']");
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();
        }

        // Click check-in date
        driver.findElement(By.cssSelector("span[aria-label='" + checkIn + "']")).click();

        // Wait until check-out date is clickable and click it
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("span[aria-label='" + checkOut + "']"))).click();
    }

    /**
     * Clicks the main search button to submit the location and dates
     */
    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    /**
     * Accept cookies if the banner appears
     * Uses a short 2-second wait to check presence
     */
    protected static void acceptCookiesIfPresent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement cookieButton = shortWait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector(
                            "button[aria-label=\"Dismiss sign-in info.\"]"))
            );
            if (cookieButton.isDisplayed() && cookieButton.isEnabled()) {
                cookieButton.click(); // Click to dismiss cookie banner
            }
        } catch (TimeoutException | NoSuchElementException ignored) {
            // Cookie banner not shown → continue without error
        }
    }
}
