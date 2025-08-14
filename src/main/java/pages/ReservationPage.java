package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReservationPage {
    private final WebDriver driver;

    private final By hotelNameBox = By.cssSelector("div.hp__hotel-name");

    public ReservationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHotelName() {
        return driver.findElement(hotelNameBox).getText();
    }
}
