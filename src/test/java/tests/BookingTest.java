package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.ExcelUtils;

public class BookingTest extends BaseTest {

    @BeforeMethod
    public void start() {
        // ✅ Setup WebDriver and open browser before each test
        setUp();
    }

    @AfterMethod
    public void close() {
        // ✅ Close browser after each test
        tearDown();
    }

    /**
     * Provides test data from Excel sheet
     */
    @DataProvider(name = "bookingData")
    public Object[][] getData() {
        return ExcelUtils.getTestData("src/test/resources/testdata.xlsx", "Sheet1");
    }

    /**
     * Full booking flow test:
     * 1. Enter location
     * 2. Select dates
     * 3. Search
     * 4. Select Tolip Hotel Alexandria
     * 5. Select bed & reserve room
     * 6. Validate dates on hotel details page
     * 7. Validate hotel name on reservation page
     */
    @Test(dataProvider = "bookingData")
    public void bookingFlowTest(String location, String checkIn, String checkOut,
                                String actualCheckIn, String actualCheckOut) {
        HomePage home = new HomePage(driver);
        SearchResultsPage results = new SearchResultsPage(driver);
        HotelDetailsPage details = new HotelDetailsPage(driver);
        ReservationPage reservation = new ReservationPage(driver);

        System.out.println("Step 1: Enter location");
        home.enterLocation(location);

        System.out.println("Step 2: Select dates");
        home.selectDates(checkIn, checkOut);

        System.out.println("Step 3: Click search");
        home.clickSearch();

        System.out.println("Step 4: Select Tolip Hotel Alexandria");
        results.selectTolipHotel();

        // ✅ Step 5 & 6: Validate using actualCheckIn / actualCheckOut (من الإكسيل)
        details.performReservation(actualCheckIn, actualCheckOut);

        System.out.println("Step 7: Validate hotel name");
        Assert.assertTrue(reservation.getHotelName().contains("Tolip Hotel Alexandria"),
                "❌ Hotel name mismatch!");
    }
}
