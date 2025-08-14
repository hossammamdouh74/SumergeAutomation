package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ReservationPage {
    private final WebDriver driver;
    private final By hotelNameBox = By.cssSelector("h1[class='e7addce19e']");

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHotelName() {
        // Wait for the hotel name element to be visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement hotelNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(hotelNameBox));

        return hotelNameElement.getText();
    }
}
