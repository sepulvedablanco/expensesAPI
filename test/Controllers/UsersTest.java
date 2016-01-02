package Controllers;

import static play.test.Helpers.GET;
import static play.test.Helpers.POST;
import static play.test.Helpers.PUT;
import static play.test.Helpers.DELETE;
import static play.mvc.Http.HeaderNames.ACCEPT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;
import static play.test.Helpers.running;
import static org.junit.Assert.*;
import helpers.Constants;
import models.User;
import models.UserTest;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.api.http.HeaderNames;
import play.libs.Json;
import play.mvc.Http.RequestBuilder;
import play.mvc.Http;
import play.mvc.Result;

public class UsersTest {
	
    @Test
    public void loginUserJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = UserTest.insertUser("Die", "dD1234", "Diego S");

	    	JsonNode jsonUser = Json.toJson(u);
	    	ObjectNode jsonObject = (ObjectNode) jsonUser;
	    	jsonObject.put("pass", "dD1234"); // pass field is ignored while is converted to JSON

			RequestBuilder request = fakeRequest(POST, "/user/login")
					.header(ACCEPT, ContentType.APPLICATION_JSON.getMimeType())
					.bodyJson(jsonUser);

	    	Result result = route(request);	    		
    		assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertNotNull(result.headers().get(Constants.Headers.USER_TOKEN));
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void loginUserWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = UserTest.insertUser("Die", "dD1234", "Diego S");

	    	JsonNode jsonUser = Json.toJson(u);
	    	ObjectNode jsonObject = (ObjectNode) jsonUser;
	    	jsonObject.put("pass", "dD1234"); // pass field is ignored while is converted to JSON

			RequestBuilder request = fakeRequest(POST, "/user/login")
					.header(ACCEPT, ContentType.APPLICATION_XML.getMimeType())
					.bodyJson(jsonUser);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.headers().get(Constants.Headers.USER_TOKEN));
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	    	assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.contentType());
		});
    }

    @Test
    public void loginUserWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = UserTest.insertUser("Die", "dD1234", "Diego S");

	    	JsonNode jsonUser = Json.toJson(u);
	    	ObjectNode jsonObject = (ObjectNode) jsonUser;
	    	jsonObject.put("pass", "dD1234"); // pass field is ignored while is converted to JSON

			RequestBuilder request = fakeRequest(POST, "/user/login")
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonUser);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertNotNull(result.headers().get(Constants.Headers.USER_TOKEN));
	    	assertEquals(Http.Status.OK, result.status());
	    	assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.contentType());
		});
    }
    
    @Test
    public void insertUser() {
		running(fakeApplication(inMemoryDatabase()), () -> {
	    	User u = UserTest.getUser("Die", "dD1234", "Diego S");
	    	JsonNode jsonUser = Json.toJson(u);
	    	ObjectNode jsonObject = (ObjectNode) jsonUser;
	    	jsonObject.put("pass", "dD1234"); // pass field is ignored while is converted to JSON

			RequestBuilder request = fakeRequest(POST, "/user")
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonUser);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertNotNull(result.headers().get(Constants.Headers.USER_TOKEN));
	    	assertEquals(Http.Status.CREATED, result.status());
		});
    }

    @Test
    public void updateUser() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User uSaved = UserTest.insertUser("Die", "dD1234", "Diego S");

			uSaved.setUser("Other");
			uSaved.setName("Other name");
			
	    	JsonNode jsonUser = Json.toJson(uSaved);
	    	ObjectNode jsonObject = (ObjectNode) jsonUser;
	    	jsonObject.put("pass", "dD1234dD1234"); // pass field is ignored while is converted to JSON

			RequestBuilder request = fakeRequest(PUT, "/user/" + uSaved.getId())
					.header(Constants.Headers.USER_TOKEN, uSaved.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonUser);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }

    @Test
    public void deleteUser() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User uSaved = UserTest.insertUser("Die", "dD1234", "Diego S");

			RequestBuilder request = fakeRequest(DELETE, "/user/" + uSaved.getId())
					.header(Constants.Headers.USER_TOKEN, uSaved.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }
}
