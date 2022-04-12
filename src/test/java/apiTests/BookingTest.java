package apiTests;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import apiEndPoints.AuthEndpoints;
import apiEndPoints.BookingEndPoints;
import apiPayload.AuthPayload;
import apiPayload.BookingPayload;

public class BookingTest {
	Faker faker;
	BookingPayload bookingPayload;
	AuthPayload authPayload;
	int createdBookingID;
	String token;

	@BeforeTest
	public void setup() {
		faker = new Faker();
		bookingPayload = new BookingPayload();

		bookingPayload.setFirstname(faker.name().firstName());
		bookingPayload.setLastname(faker.name().lastName());
		bookingPayload.setTotalprice(faker.random().nextInt(5, 100));
		bookingPayload.setDepositpaid(true);
		bookingPayload.setCheckin("2014-03-13");
		bookingPayload.setCheckout("2014-05-21");
		bookingPayload.setAdditionalneeds(faker.funnyName().name());

		authPayload = new AuthPayload();
		authPayload.setUsername("admin");
		authPayload.setPassword("password123");

		HashMap<String,Object> payload = new HashMap<String,Object>();
		payload.put("username", authPayload.getUsername());
		payload.put("password", authPayload.getPassword());

		JSONObject payloadJSON = new JSONObject(payload);
		
		token = "token="+AuthEndpoints.login(payloadJSON);
	}

	@Test
	public void createBooking() {
		HashMap<String,Object> payload = new HashMap<String,Object>();

		payload.put("firstname", bookingPayload.getFirstname());
		payload.put("lastname", bookingPayload.getLastname());
		payload.put("totalprice", bookingPayload.getTotalprice());
		payload.put("depositpaid", bookingPayload.isDepositpaid());
		payload.put("bookingdates", bookingPayload.getBookingDates());
		payload.put("additionalneeds", bookingPayload.getAdditionalneeds());

		JSONObject payloadJSON = new JSONObject(payload);

		createdBookingID = BookingEndPoints.verifyCreateBooking(payloadJSON);
	}

	@Test
	public void updateBooking() {
		HashMap<String,Object> payload = new HashMap<String,Object>();

		bookingPayload.setFirstname(bookingPayload.getFirstname()+" new");
		
		payload.put("firstname", bookingPayload.getFirstname());
		payload.put("lastname", bookingPayload.getLastname());
		payload.put("totalprice", bookingPayload.getTotalprice());
		payload.put("depositpaid", bookingPayload.isDepositpaid());
		payload.put("bookingdates", bookingPayload.getBookingDates());
		payload.put("additionalneeds", bookingPayload.getAdditionalneeds());

		JSONObject payloadJSON = new JSONObject(payload);
		BookingEndPoints.verifyUpdateBooking(payloadJSON, createdBookingID , token);
	}
	
	@Test
	public void updatePartialBooking() {
		HashMap<String,Object> payload = new HashMap<String,Object>();

		payload.put("firstname", bookingPayload.getFirstname());
		payload.put("lastname", bookingPayload.getLastname());
		payload.put("totalprice", bookingPayload.getTotalprice());
		payload.put("depositpaid", bookingPayload.isDepositpaid());
		payload.put("bookingdates", bookingPayload.getBookingDates());
		payload.put("additionalneeds", bookingPayload.getAdditionalneeds());

		JSONObject payloadJSON = new JSONObject(payload);
		
		HashMap<String,Object> updatedPayload = new HashMap<String,Object>();

		updatedPayload.put("firstname", bookingPayload.getFirstname()+"updated");
		updatedPayload.put("totalprice", bookingPayload.getTotalprice()+1);
		JSONObject updatedPayloadJSON = new JSONObject(updatedPayload);
		
		BookingEndPoints.verifyPartialUpdateBooking(payloadJSON, updatedPayloadJSON, createdBookingID, token);
	}
	
	@Test(priority=8)
	public void deleteBooking() {
		BookingEndPoints.verifyDeleteBooking(createdBookingID, token);
	}
//
	@Test
	public void getBookingIds() {
		BookingEndPoints.verifyGetBookingIds();
	}

	@Test
	public void getBookingIdsByName() {
		HashMap<String,Object> payload = new HashMap<String,Object>();
		payload.put("firstname", bookingPayload.getFirstname());
		payload.put("lastname", bookingPayload.getLastname());
		JSONObject payloadJSON = new JSONObject(payload);

		BookingEndPoints.getBookingIdsByName(payloadJSON);
	}

	@Test
	public void getBookingIdsByCheckinCheckout() {
		HashMap<String,Object> payload = new HashMap<String,Object>();
		payload.put("checkin", bookingPayload.getCheckin());
		payload.put("checkout", bookingPayload.getCheckout());
		JSONObject payloadJSON = new JSONObject(payload);

		BookingEndPoints.getBookingIdsByCheckinCheckout(payloadJSON);
	}

	@Test
	public void getBookingById() {
		BookingEndPoints.getBookingById(createdBookingID);
	}
}
