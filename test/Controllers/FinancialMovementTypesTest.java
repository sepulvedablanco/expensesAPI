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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import models.FinancialMovementType;
import models.FinancialMovementTypeTest;
import models.User;
import models.UserTest;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.api.http.HeaderNames;
import play.mvc.Http;
import play.mvc.Http.RequestBuilder;
import play.mvc.Result;

public class FinancialMovementTypesTest {

    @Test
    public void findFinancialMovementTypeJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + 
    				"/financialMovementTypes?id=" + u.getFinancialMovementTypes().get(0).getId())
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
	   		assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	  		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementTypeWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + 
    				"/financialMovementTypes?id=" + u.getFinancialMovementTypes().get(0).getId())
					.header(ACCEPT, ContentType.APPLICATION_XML.getMimeType())
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	   		assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementTypeWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + 
    				"/financialMovementTypes?id=" + u.getFinancialMovementTypes().get(0).getId())
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
					
	    	Result result = route(request);
	    	assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	   		assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.contentType());
		});
    }
    
    @Test
    public void findFinancialMovementTypesJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovementTypes")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
			assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementTypesWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovementTypes")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.APPLICATION_XML.getMimeType());
					
    		Result result = route(request);
    		assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementTypesWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			
			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovementTypes")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
					
    		Result result = route(request);
    		assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_XML.getMimeType(), result.contentType());
		});
    }
    
    
    @Test
    public void insertFinancialMovementType() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(false);

	    	JsonNode jsonFMT = Json.toJson(u.getFinancialMovementTypes().get(0));

			RequestBuilder request = fakeRequest(POST, "/user/" + u.getId() + "/financialMovementType")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonFMT);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.CREATED, result.status());
		});
    }

    @Test
    public void updateFinancialMovementType() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			FinancialMovementType fmt = u.getFinancialMovementTypes().get(0);
			fmt.setDescription("New description");
			
	    	JsonNode jsonFMT = Json.toJson(fmt);

			RequestBuilder request = fakeRequest(PUT, "/user/" + u.getId() + "/financialMovementType/" + fmt.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonFMT);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }

    @Test
    public void deleteFinancialMovementType() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			FinancialMovementType fmt = u.getFinancialMovementTypes().get(0);

			RequestBuilder request = fakeRequest(DELETE, "/user/" + u.getId() + "/financialMovementType/" + fmt.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }
    
    private User initializeData(boolean inserFinancialMovementType) {
		User u = UserTest.insertUser("Die", "dD1234", "Diego S");
		FinancialMovementType fmt;
		if(inserFinancialMovementType) {
			fmt = FinancialMovementTypeTest.insertFinancialMovementType("Description");
			
			fmt.setUser(u);
			fmt.update();
		} else {
			fmt = FinancialMovementTypeTest.getFinancialMovementType("Description");
		}
		
		List<FinancialMovementType> lstFmt = new ArrayList<FinancialMovementType>();
		lstFmt.add(fmt);
		u.setFinancialMovementTypes(lstFmt);
		
		return u;
    }

}
