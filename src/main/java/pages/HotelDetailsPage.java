package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class HotelDetailsPage {
    private final WebDriver driver;

    // Locators (adjust if Booking.com changes HTML)
    private final By bedSelection = By.cssSelector("input[value='2'][name='bedPreference_78883128']");
    private final By selectAmount = By.cssSelector("#hprt_nos_select_bbasic_0");
    private final By reserveButton = By.xpath("(//span[@class='bui-button__text js-reservation-button__text'])[1]");

    public HotelDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Scrolls the main window until the element is visible, waits until clickable, and returns it.
     */
    private WebElement scrollToElement(By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));

        // Wait until present in DOM
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        // Wait until visible on screen
        wait.until(ExpectedConditions.visibilityOf(element));

        // Scroll into view directly
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);

        // Wait until clickable
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Selects the first bed option.
     */
    public void selectBed() {
        WebElement bedOption = scrollToElement(bedSelection, 15);
        bedOption.click();
        System.out.println("🛏 Bed option selected");
    }

    /**
     * Selects the first available amount option from the dropdown.
     */
    public void selectAmount() {
        WebElement dropdown = scrollToElement(selectAmount, 15);

        // Wait for dropdown options to load
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(d -> dropdown.findElements(By.tagName("option")).size() > 0);

        try {
            Select select = new Select(dropdown);
            if (select.getOptions().size() > 1) {
                select.selectByIndex(1); // choose second option if available
                System.out.println("💰 Amount option selected (index 1)");
            } else {
                dropdown.click(); // fallback
                System.out.println("💰 Amount option clicked (only 1 available)");
            }
        } catch (UnexpectedTagNameException e) {
            dropdown.click();
        }
    }

    /**
     * Clicks the reserve button.
     */
    public void clickReserve() {
        scrollToElement(reserveButton, 15).click();
        System.out.println("✅ Reserve button clicked");
    }

    /**
     * Master reservation flow.
     */
    public void performReserve() {
        selectBed();
        selectAmount();
        clickReserve();
    }
}
