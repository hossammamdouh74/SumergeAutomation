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
        setUp();
    }

    @AfterMethod
    public void close() {
        tearDown();
    }

    @DataProvider(name = "bookingData")
    public Object[][] getData() {
        return ExcelUtils.getTestData("src/test/resources/testdata.xlsx", "Sheet1");
    }

    @Test(dataProvider = "bookingData")
    public void bookingFlowTest(String location, String checkIn, String checkOut) {
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

        System.out.println("Step 5: Reserve room");
        details.performReserve();

        System.out.println("Step 6: Validate dates on hotel details page");
        Assert.assertTrue(details.getCheckInDate().contains("1 October 2024"));
        Assert.assertTrue(details.getCheckOutDate().contains("14 October 2024"));

        System.out.println("Step 7: Validate hotel name on reservation page");
        Assert.assertTrue(reservation.getHotelName().contains("Tolip Hotel Alexandria"));
    }
}
