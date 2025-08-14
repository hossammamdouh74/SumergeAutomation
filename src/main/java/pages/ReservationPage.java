package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ReservationPage {
    private final WebDriver driver;
    private final By hotelNameBox = By.xpath("//div[@class='bp_hotel_name_title']");

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHotelName() {
        // Switch to last opened tab (sometimes Reserve opens a new one)
        for (String handle : driver.getWindowHandles()) {
            driver.switchTo().window(handle);
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement hotelNameElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(hotelNameBox)
        );

        return hotelNameElement.getText().trim();
    }
}