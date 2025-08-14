package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SearchResultsPage {
    private final WebDriver driver;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectTolipHotel() {
        String hotelXpath = "//div[contains(text(),'Tolip Hotel Alexandria')]";
        String loadMoreResultsCss = "button.de576f5064.b46cd7aad7.d0a01e3d83.dda427e6b5.bbf83acb81.a0ddd706cc";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1)); // small wait per step
        JavascriptExecutor js = (JavascriptExecutor) driver;

        boolean found = false;
        int scrollStep = 1500; // pixels per scroll
        int maxScroll = 100000; // safety limit

        for (int y = 0; y <= maxScroll; y += scrollStep) {
            try {
                // Try to find the hotel
                WebElement hotel = driver.findElement(By.xpath(hotelXpath));
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", hotel);
                wait.until(ExpectedConditions.elementToBeClickable(hotel)).click();
                found = true;
                break; // hotel found → stop
            } catch (NoSuchElementException e) {
                // Hotel not found yet
                try {
                    // Check if "Load more results" button is visible
                    WebElement loadMoreBtn = driver.findElement(By.cssSelector(loadMoreResultsCss));
                    if (loadMoreBtn.isDisplayed() && loadMoreBtn.isEnabled()) {
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", loadMoreBtn);
                        loadMoreBtn.click();

                        // Wait until hotel list updates (example: wait for any hotel card to be present)
                        wait.withTimeout(Duration.ofSeconds(5))
                                .until(ExpectedConditions.presenceOfElementLocated(
                                        By.cssSelector("div[data-testid='property-card']")));
                    }
                } catch (NoSuchElementException ignored) {
                    // No load more button — just scroll
                    js.executeScript("window.scrollBy(0, arguments[0]);", scrollStep);
                }
            }
        }

        if (!found) {
            throw new TimeoutException("Tolip Hotel Alexandria not found after scrolling and loading more results.");
        }
    }
}
