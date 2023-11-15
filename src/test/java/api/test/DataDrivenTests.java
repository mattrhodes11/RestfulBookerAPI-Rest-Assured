package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import api.endpoints.BookingEndpoints;
import api.payload.Booking;
import api.payload.BookingDates;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTests {

	public Logger logger;
	
	@BeforeClass
	public void setup()
	{
		logger = LogManager.getLogger(this.getClass());
	}
	
	@Test(priority=1, dataProvider="Data", dataProviderClass=DataProviders.class)
	public void createBookings(String firstName, String lastName, String totalPrice, String depositPaid, String checkIn, String checkOut, String additionalNeeds)
	{
		logger.info("*** Creating Booking using test data from excel sheet ***");
		
		Booking createBooking = new Booking();
		BookingDates bookingDates = new BookingDates();
		
		createBooking.setFirstname(firstName);
		createBooking.setLastname(lastName);
		createBooking.setTotalprice(Integer.parseInt(totalPrice));
		createBooking.setDepositpaid(Boolean.parseBoolean(depositPaid));
		
		bookingDates.setCheckin(checkIn);
		bookingDates.setCheckout(checkOut);

		createBooking.setBookingdates(bookingDates);	
		
		createBooking.setAdditionalneeds(additionalNeeds);
		
		Response response = BookingEndpoints.createBooking(createBooking);
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*** Booking successfully created ***");
	}
}
