package apiTests;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import apiEndPoints.AuthEndpoints;
import apiPayload.AuthPayload;

public class AuthTest {
	AuthPayload authPayload;
	static String token;
	@BeforeClass
	public void setup() {
		authPayload = new AuthPayload();
		authPayload.setUsername("admin");
		authPayload.setPassword("password123");
	}
	
	@Test
	public void login() {
		HashMap<String,Object> payload = new HashMap<String,Object>();
		payload.put("username", authPayload.getUsername());
		payload.put("password", authPayload.getPassword());

		JSONObject payloadJSON = new JSONObject(payload);
		
		 token = AuthEndpoints.login(payloadJSON);
	}
}
