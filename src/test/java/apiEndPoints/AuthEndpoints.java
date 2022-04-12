package apiEndPoints;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.json.simple.JSONObject;

//import apiEndpoints.Routes;
import io.restassured.response.Response;

public class AuthEndpoints {
	public static String login(JSONObject obj) {
		baseURI=Routes.base_uri;
		basePath ="auth/";
			return given().
				header("Content-Type", "application/json").
				body(obj.toJSONString()). 
			when(). 
				post().
			then().
				assertThat().
				body("token", not(nullValue())).
				extract().
				path("token");
	}
}
