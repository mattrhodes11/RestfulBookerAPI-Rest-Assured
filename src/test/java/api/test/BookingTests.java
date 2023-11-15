package api.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.BookingEndpoints;
import api.payload.Booking;
import api.payload.BookingDates;
import api.payload.Credentials;
import io.restassured.response.Response;

public class BookingTests {
	
	Faker faker;
	Booking createBooking;
	BookingDates bookingDates;
	Credentials credentials;
	
	String bookingId;
	String token;
	
	public Logger logger;
	
	@BeforeClass
	public void setup()
	{			
		faker = new Faker();
		createBooking = new Booking();
		bookingDates = new BookingDates();
		credentials = new Credentials();
		
		credentials.setUsername("admin");
		credentials.setPassword("password123");
		
		createBooking.setFirstname(faker.name().firstName());
		createBooking.setLastname(faker.name().lastName());
		createBooking.setTotalprice(faker.number().numberBetween(100, 1000));
		createBooking.setDepositpaid(faker.bool().bool());

		Calendar cal = Calendar.getInstance(); 
		Date checkIn = faker.date().future(10, TimeUnit.DAYS, new Date());
		cal.setTime(checkIn);
		cal.add(Calendar.DATE, 7);  
		Date checkOut = cal.getTime();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		bookingDates.setCheckin(formatter.format(checkIn));
		bookingDates.setCheckout(formatter.format(checkOut));
		
		createBooking.setBookingdates(bookingDates);
		
		createBooking.setAdditionalneeds(faker.lorem().word());
		
		logger = LogManager.getLogger(this.getClass());
	}
	
	@Test(priority=1)
	public void createAuthToken()
	{
		logger.info("*** Creating Auth token ***");
		
		Response response = BookingEndpoints.createAuthToken(credentials);
		response.then()
			.log().all();
		
		token = response.then().extract().path("token");
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*** Auth token created successfully ***");
	}
	
	@Test(priority=2)
	public void createBooking()
	{
		logger.info("*** Creating Booking ***");
		
		Response response = BookingEndpoints.createBooking(createBooking);
		response.then()
			.log().all();
		
		bookingId = Integer.toString(response.then().extract().path("bookingid"));
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*** Created booking with ID: " + this.bookingId + " ***");
	}
	
	@Test(priority=3)
	public void getBookingById()
	{
		logger.info("*** Getting Booking with ID: " + this.bookingId + " ***");
		
		Response response = BookingEndpoints.getBooking(this.bookingId);
		response.then().
			log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*** Successfully retrieved booking with ID: " + this.bookingId + " ***");
	}
	
	@Test(priority=4)
	public void updateBookingById()
	{
		logger.info("*** Updating Booking with ID: " + this.bookingId + " ***");
		
		createBooking.setFirstname(faker.name().firstName());
		createBooking.setLastname(faker.name().lastName());
		
		Response response = BookingEndpoints.updateBooking(this.bookingId, createBooking, token);
		response.then()
			.log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		// Assert data is updated correctly
		Response responseAfterUpdate = BookingEndpoints.getBooking(this.bookingId);
		responseAfterUpdate.then()
			.log().all();
		Assert.assertEquals(responseAfterUpdate.then().extract().path("firstname"), this.createBooking.getFirstname());
		Assert.assertEquals(responseAfterUpdate.then().extract().path("lastname"), this.createBooking.getLastname());
		
		logger.info("*** Booking with ID: " + this.bookingId + " updated successfully ***");
	}
	
	@Test(priority=5)
	public void deleteBookingById()
	{
		logger.info("*** Deleting Booking with ID: " + this.bookingId + " ***");
		
		Response response = BookingEndpoints.deleteBooking(this.bookingId, token);
		response.then().
			log().all();
		
		Assert.assertEquals(response.getStatusCode(), 201);
		
		logger.info("*** Booking with ID: " + this.bookingId + " deleted successfully ***");
	}
}
