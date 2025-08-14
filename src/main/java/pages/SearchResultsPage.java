package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

import static pages.HomePage.acceptCookiesIfPresent;

public class SearchResultsPage {
    private final WebDriver driver;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectTolipHotel() {
        // ✅ Accept cookies if the popup appears
        acceptCookiesIfPresent();

        // Define the XPath for the Tolip Hotel card
        String hotelXpath = "//div[contains(text(),'Tolip Hotel Alexandria')]";

        // Define the CSS selector for "Load More Results" button if present
        String loadMoreResultsCss = "button.de576f5064.b46cd7aad7.d0a01e3d83.dda427e6b5.bbf83acb81.a0ddd706cc";

        // Explicit wait with 10 seconds timeout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // JavascriptExecutor to perform scrolling actions
        JavascriptExecutor js = (JavascriptExecutor) driver;

        boolean found = false; // Flag to indicate if hotel is found
        int scrollStep = 1500; // Amount to scroll per step
        int maxScroll = 100000; // Maximum scroll limit to avoid infinite loop

        // Loop through the page by scrolling to find the hotel
        for (int y = 0; y <= maxScroll; y += scrollStep) {
            try {
                // Try to locate the Tolip Hotel element
                WebElement hotel = driver.findElement(By.xpath(hotelXpath));

                // Scroll the hotel element into the center of the viewport
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", hotel);

                // Wait until the hotel element is clickable and then click it
                wait.until(ExpectedConditions.elementToBeClickable(hotel)).click();
                found = true;

                // ✅ Switch to the newly opened hotel details tab
                for (String windowHandle : driver.getWindowHandles()) {
                    driver.switchTo().window(windowHandle);
                }
                System.out.println("✅ Switched to Tolip Hotel Alexandria details tab");

                // Break the loop since hotel is found
                break;

            } catch (NoSuchElementException e) {
                // If hotel not found, try to click "Load More Results" button if it exists
                try {
                    WebElement loadMoreBtn = driver.findElement(By.cssSelector(loadMoreResultsCss));
                    if (loadMoreBtn.isDisplayed() && loadMoreBtn.isEnabled()) {
                        // Scroll the button into view and click it
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", loadMoreBtn);
                        loadMoreBtn.click();

                        // Wait shortly for new property cards to appear
                        wait.withTimeout(Duration.ofSeconds(3))
                                .until(ExpectedConditions.presenceOfElementLocated(
                                        By.cssSelector("div[data-testid='property-card']")));
                    }
                } catch (NoSuchElementException ignored) {
                    // If no "Load More" button, scroll down by scrollStep
                    js.executeScript("window.scrollBy(0, arguments[0]);", scrollStep);
                }
            }
        }

        // Throw exception if hotel is still not found after scrolling
        if (!found) {
            throw new TimeoutException("❌ Tolip Hotel Alexandria not found after scrolling and loading more results.");
        }
    }
}