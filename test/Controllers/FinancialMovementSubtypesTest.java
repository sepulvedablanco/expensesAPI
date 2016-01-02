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
import models.FinancialMovementSubtype;
import models.FinancialMovementSubtypeTest;
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

public class FinancialMovementSubtypesTest {

    @Test
    public void findFinancialMovementSubtypeJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			Long fmtId = u.getFinancialMovementTypes().get(0).getId();
			Long fmsId = u.getFinancialMovementTypes().get(0).getFinancialMovementSubtypes().get(0).getId();

			RequestBuilder request = fakeRequest(GET, "/user/financialMovementType/" + fmtId +
    				"/financialMovementSubtypes?id=" + fmsId)
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
	   		assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	  		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementSubtypeWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			Long fmtId = u.getFinancialMovementTypes().get(0).getId();
			Long fmsId = u.getFinancialMovementTypes().get(0).getFinancialMovementSubtypes().get(0).getId();

			RequestBuilder request = fakeRequest(GET, "/user/financialMovementType/" + fmtId +
    				"/financialMovementSubtypes?id=" + fmsId)
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
    public void findFinancialMovementSubtypeWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			Long fmtId = u.getFinancialMovementTypes().get(0).getId();
			Long fmsId = u.getFinancialMovementTypes().get(0).getFinancialMovementSubtypes().get(0).getId();

			RequestBuilder request = fakeRequest(GET, "/user/financialMovementType/" + fmtId +
    				"/financialMovementSubtypes?id=" + fmsId)
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
    public void findFinancialMovementSubtypesJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			Long fmtId = u.getFinancialMovementTypes().get(0).getId();

			RequestBuilder request = fakeRequest(GET, "/user/financialMovementType/" + fmtId +
    				"/financialMovementSubtypes")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
			assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementSubtypesWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			Long fmtId = u.getFinancialMovementTypes().get(0).getId();

			RequestBuilder request = fakeRequest(GET, "/user/financialMovementType/" + fmtId +
    				"/financialMovementSubtypes")
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
    public void findFinancialMovementSubtypesWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			Long fmtId = u.getFinancialMovementTypes().get(0).getId();

			RequestBuilder request = fakeRequest(GET, "/user/financialMovementType/" + fmtId +
    				"/financialMovementSubtypes")
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
    public void insertFinancialMovementSubtype() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(false);
			FinancialMovementType fmt = u.getFinancialMovementTypes().get(0);
			
	    	JsonNode jsonFMS = Json.toJson(fmt.getFinancialMovementSubtypes().get(0));

			RequestBuilder request = fakeRequest(POST, "/user/financialMovementType/" + 
					fmt.getId() + "/financialMovementSubtype")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonFMS);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.CREATED, result.status());
		});
    }

    @Test
    public void updateFinancialMovementSubtype() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			FinancialMovementType fmt = u.getFinancialMovementTypes().get(0);
			FinancialMovementSubtype fms = fmt.getFinancialMovementSubtypes().get(0);
			fms.setDescription("New description");

	    	JsonNode jsonFMS = Json.toJson(fms);
			
			RequestBuilder request = fakeRequest(PUT, "/user/financialMovementType/" +
					fmt.getId() + "/financialMovementSubtype/" + fms.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonFMS);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }

    @Test
    public void deleteFinancialMovementSubtype() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			FinancialMovementType fmt = u.getFinancialMovementTypes().get(0);
			FinancialMovementSubtype fms = fmt.getFinancialMovementSubtypes().get(0);

			RequestBuilder request = fakeRequest(DELETE, "/user/financialMovementType/" +
					fmt.getId() + "/financialMovementSubtype/" + fms.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }
    
    private User initializeData(boolean insertFinancialMovementSubtype) {
		User u = UserTest.insertUser("Die", "dD1234", "Diego S");
		FinancialMovementType fmt = FinancialMovementTypeTest.
				insertFinancialMovementType("Type description");
		FinancialMovementSubtype fms;
		if(insertFinancialMovementSubtype) {
			fms = FinancialMovementSubtypeTest.insertFinancialMovementSubtype("Subtype description");
			fms.setFinancialMovementType(fmt);
			fms.update();			
		} else {
			fms = FinancialMovementSubtypeTest.getFinancialMovementSubtype("Subtype description");
		}

		fmt.setUser(u);

		if(insertFinancialMovementSubtype) {
			List<FinancialMovementSubtype> lstFms = new ArrayList<FinancialMovementSubtype>();
			lstFms.add(fms);
			fmt.setFinancialMovementSubtypes(lstFms);

			fmt.update();
		} else {
			fmt.update();
			
			List<FinancialMovementSubtype> lstFms = new ArrayList<FinancialMovementSubtype>();
			lstFms.add(fms);
			fmt.setFinancialMovementSubtypes(lstFms);
		}
		
		List<FinancialMovementType> lstFmt = new ArrayList<FinancialMovementType>();
		lstFmt.add(fmt);
		u.setFinancialMovementTypes(lstFmt);
		
		return u;
    }
}
