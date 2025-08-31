package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HotelDetailsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    /**
     * Locators (dynamic, stable selectors)
     */
    private final By roomTable = By.cssSelector("table.hprt-table");
    private final By bedRadioButton = By.xpath("(//div[@class='rt-bed-type-select'])[2]");
    private final By amountDropdown = By.cssSelector("select[id^='hprt_nos_select']");
    private final By reserveButton = By.xpath("(//span[@class='bui-button__text js-reservation-button__text'])[1]");

    public HotelDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    /**
     * Selects a bed and clicks the reserve button.
     */
    private void selectBedAndReserve() {
        System.out.println("🔵 [START] Selecting bed, choosing options, and reserving room...");

        try {
            // Step 0: Wait for room table
            System.out.println("⏳ Waiting for room table...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(roomTable));
            System.out.println("✅ Room table found!");

            // Step 1: Select bed radio button (REQUIRED)
            System.out.println("⏳ Waiting for bed radio button...");
            WebElement bedOption = wait.until(ExpectedConditions.elementToBeClickable(bedRadioButton));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", bedOption);
            bedOption.click();
            System.out.println("✅ Bed radio button selected");

            // Step 2: Select amount from dropdown (always required)
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(amountDropdown));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);

            dropdown.click();
            dropdown.findElement(By.xpath(".//option[not(@value='0')][1]")).click();
            System.out.println("✅ Amount selected from dropdown");

            // Step 3: Click reserve button with retries
            boolean clicked = false;
            int attempts = 0;

            while (!clicked && attempts < 5) {
                attempts++;
                try {
                    WebElement reserveBtn = driver.findElement(reserveButton);

                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block:'center'});", reserveBtn
                    );

                    wait.until(ExpectedConditions.elementToBeClickable(reserveBtn)).click();

                    clicked = true;
                    System.out.println("🟢 [PASS] Room reserved successfully on attempt " + attempts + "!");

                } catch (ElementClickInterceptedException | TimeoutException ex) {
                    System.out.println("⚠️ Attempt " + attempts + " failed, retrying...");
                    wait.until(ExpectedConditions.refreshed(
                            ExpectedConditions.elementToBeClickable(reserveButton)
                    ));
                }
            }

            if (!clicked) {
                throw new RuntimeException("❌ Failed to click reserve button after retries");
            }

        } catch (Exception e) {
            System.out.println("❌ [FAIL] Unexpected error: " + e.getMessage());
            throw new RuntimeException("❌ Failed during selectBedAndReserve()", e);
        }

        System.out.println("🔵 [END] selectBedAndReserve()");
    }

    /**
     * Validates that displayed dates match expected dates
     */
    private void assertDatesMatch(String expectedFromDate, String expectedToDate) {
        System.out.println("🔵 [START] Validating check-in and check-out dates");

        try {
            By actualFromDay = By.xpath("(//span[@data-testid='date-display-field-start'])[1]");
            By actualToDay   = By.xpath("(//span[@data-testid='date-display-field-end'])[1]");

            WebElement fromElement = wait.until(ExpectedConditions.visibilityOfElementLocated(actualFromDay));
            WebElement toElement   = wait.until(ExpectedConditions.visibilityOfElementLocated(actualToDay));

            String fromDay = fromElement.getText().trim();
            String toDay   = toElement.getText().trim();

            System.out.println("📅 Actual From Date: " + fromDay);
            System.out.println("📅 Actual To Date: " + toDay);
            System.out.println("📅 Expected From Date: " + expectedFromDate);
            System.out.println("📅 Expected To Date: " + expectedToDate);

            if (fromDay.equals(expectedFromDate) && toDay.equals(expectedToDate)) {
                System.out.println("🟢 [PASS] Dates match!");
            } else {
                throw new AssertionError("❌ Dates do not match! Expected [" +
                        expectedFromDate + " → " + expectedToDate + "] but found [" +
                        fromDay + " → " + toDay + "]");
            }

        } catch (TimeoutException e) {
            System.out.println("❌ [FAIL] Timeout while waiting for date elements");
            throw e;
        } catch (Exception e) {
            System.out.println("❌ [FAIL] Error while validating dates: " + e.getMessage());
            throw e;
        }

        System.out.println("🔵 [END] assertDatesMatch()");
    }

    public void performReservation(String expectedFromDate, String expectedToDate){
        System.out.println("Step 5: Validate dates");
        assertDatesMatch(expectedFromDate,expectedToDate);
        System.out.println("Step 6: Reserve room");
        selectBedAndReserve();
    }
}
