package api.endpoints;

import static io.restassured.RestAssured.given;

import java.util.ResourceBundle;

import api.payload.Booking;
import api.payload.Credentials;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BookingEndpoints {
	
	static ResourceBundle getUrl()
	{
		ResourceBundle routes = ResourceBundle.getBundle("routes");
		return routes;
	}
	
	public static Response createAuthToken(Credentials credentials)
	{
		Response response = given()
			.contentType(ContentType.JSON)
			.body(credentials)
			.log().all()
		.when()
			.post(getUrl().getString("auth_url"));
			
		return response;
	}
	
	public static Response createBooking(Booking createBooking) 
	{
		Response response = given()
			.contentType(ContentType.JSON)
			.body(createBooking)
			.log().all()
		.when()
			.post(getUrl().getString("post_url"));
		
		return response;
	}
	
	public static Response getBooking(String bookingId) 
	{
		Response response = given()
			.pathParam("id", bookingId)
			.log().all()
		.when()
			.get(getUrl().getString("get_url"));
		
		return response;
	}
	
	public static Response updateBooking(String bookingId, Booking createBooking, String authToken) 
	{
		Response response = given()
			.contentType(ContentType.JSON)
			.pathParam("id", bookingId)
			.cookie("token", authToken)
			.body(createBooking)
			.log().all()
		.when()
			.put(getUrl().getString("update_url"));
		
		return response;
	}
	
	public static Response deleteBooking(String bookingId, String authToken) 
	{
		Response response = given()
			.pathParam("id", bookingId)
			.cookie("token", authToken)
			.log().all()
		.when()
			.delete(getUrl().getString("delete_url"));
		
		return response;
	}
}
