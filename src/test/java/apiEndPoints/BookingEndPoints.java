package apiEndPoints;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

import org.json.simple.JSONObject;

import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;
import java.io.File;

public class BookingEndPoints {

	public static void verifyGetBookingIds() {
		baseURI = Routes.base_uri;
		basePath = "booking/";
		File schema = new File("src/test/resources/booking-list-schema.json");
		
		given().
		when().
			get("").
		then().
			assertThat().
			statusCode(200).
			body(matchesJsonSchema(schema));
	}

	public static void getBookingIdsByName(JSONObject obj) {
		baseURI = Routes.base_uri;
		basePath = "booking/";

		given()
			.queryParam("firstname", obj.get("firstname"))
			.queryParam("lastname", obj.get("lastname"))
		.expect()
			.statusCode(200)
		.when()
			.get("");
	}
	
	public static void getBookingIdsByCheckinCheckout(JSONObject obj) {
		baseURI = Routes.base_uri;
		basePath = "booking/";

		given()
			.queryParam("checkin", obj.get("checkin"))
			.queryParam("checkout", obj.get("checkout"))
		.expect()
			.statusCode(200)
		.when()
			.get("");
	}
	
	public static void getBookingById(int id) {
		baseURI = Routes.base_uri;
		basePath = "booking/";
		File schema = new File("src/test/resources/existing-booking-schema.json");
		given()
			.basePath(basePath+id).
		when()
			.get().
		then().
			assertThat().
			statusCode(200).
			body(matchesJsonSchema(schema));
	}
	
	public static int verifyCreateBooking(JSONObject obj) {
		baseURI = Routes.base_uri;
		basePath = "booking";
		JSONObject bookingDates = (JSONObject) obj.get("bookingdates");
		File schema = new File("src/test/resources/created-booking-schema.json");
		return given().
			contentType(ContentType.JSON).
			body(obj).
		when()
			.post().
		then().
			assertThat().
			statusCode(200).
			body("bookingid", not(nullValue())).
			body("booking.firstname", equalTo(obj.get("firstname"))).
			body("booking.lastname", equalTo(obj.get("lastname"))).
			body("booking.totalprice", equalTo(obj.get("totalprice"))).
			body("booking.depositpaid", equalTo(obj.get("depositpaid"))).
			body("booking.bookingdates.checkin", equalTo(bookingDates.get("checkin"))).
			body("booking.bookingdates.checkout", equalTo(bookingDates.get("checkout"))).
			body("booking.additionalneeds", equalTo(obj.get("additionalneeds"))).
			body(matchesJsonSchema(schema)).extract().path("bookingid");
	}
	
	public static void verifyUpdateBooking(JSONObject obj, int bookingId, String token) {
		baseURI = Routes.base_uri;
		basePath = "booking/";
		JSONObject bookingDates = (JSONObject) obj.get("bookingdates");
		
		 given().
			contentType(ContentType.JSON).
			body(obj).
			basePath(basePath+bookingId).
		when()
			.header("cookie", token)
			.put().
		then().
			assertThat().
			statusCode(200).
			body("firstname", equalTo(obj.get("firstname"))).
			body("lastname", equalTo(obj.get("lastname"))).
			body("totalprice", equalTo(obj.get("totalprice"))).
			body("depositpaid", equalTo(obj.get("depositpaid"))).
			body("bookingdates.checkin", equalTo(bookingDates.get("checkin"))).
			body("bookingdates.checkout", equalTo(bookingDates.get("checkout"))).
			body("additionalneeds", equalTo(obj.get("additionalneeds")));
	}
	
	public static void verifyPartialUpdateBooking(JSONObject originalBooking, JSONObject partialBooking, int bookingId, String token) {
		baseURI = Routes.base_uri;
		basePath = "booking/";
		JSONObject bookingDates = (JSONObject) originalBooking.get("bookingdates");
		
		 given().
			contentType(ContentType.JSON).
			body(partialBooking).
			basePath(basePath+bookingId).
		when()
			.header("cookie", token)
			.patch().
		then().
			assertThat().
			statusCode(200).
			body("firstname", equalTo(partialBooking.get("firstname"))).
			body("lastname", equalTo(originalBooking.get("lastname"))).
			body("totalprice", equalTo(partialBooking.get("totalprice"))).
			body("depositpaid", equalTo(originalBooking.get("depositpaid"))).
			body("bookingdates.checkin", equalTo(bookingDates.get("checkin"))).
			body("bookingdates.checkout", equalTo(bookingDates.get("checkout"))).
			body("additionalneeds", equalTo(originalBooking.get("additionalneeds")));
	}
	
	public static void verifyDeleteBooking(int bookingId, String token) {
		baseURI = Routes.base_uri;
		basePath = "booking/";
		given().
		basePath(basePath+bookingId).
		when()
			.header("cookie", token)
			.delete().
		then().
			assertThat().
			statusCode(201);
		
		given().
		when().
			get(basePath+bookingId)
		.then().
		statusCode(404);
	}
}
