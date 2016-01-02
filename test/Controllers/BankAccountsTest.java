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
import models.BankAccount;
import models.BankAccountTest;
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

public class BankAccountsTest {

    @Test
    public void findBankAccountJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + 
    				"/bankAccounts?id=" + u.getBankAccounts().get(0).getId())
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
	   		assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	  		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findBankAccountWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + 
    				"/bankAccounts?id=" + u.getBankAccounts().get(0).getId())
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
    public void findBankAccountWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + 
    				"/bankAccounts?id=" + u.getBankAccounts().get(0).getId())
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
    public void findBankAccountsJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/bankAccounts")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
			assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findBankAccountsWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/bankAccounts")
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
    public void findBankAccountsWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			
			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/bankAccounts")
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
    public void insertBankAccount() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(false);

	    	JsonNode jsonBankAccount = Json.toJson(u.getBankAccounts().get(0));

			RequestBuilder request = fakeRequest(POST, "/user/" + u.getId() + "/bankAccount")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonBankAccount);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.CREATED, result.status());
		});
    }

    @Test
    public void updateBankAccount() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			BankAccount b = u.getBankAccounts().get(0);

			b.setDescription("Other description");
			b.setBalance(BigDecimal.valueOf(459.95));
			
	    	JsonNode jsonBankAccount = Json.toJson(b);

			RequestBuilder request = fakeRequest(PUT, "/user/" + u.getId() + "/bankAccount/" + b.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonBankAccount);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }

    @Test
    public void deleteBankAccount() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User u = initializeData(true);
			BankAccount b = u.getBankAccounts().get(0);

			RequestBuilder request = fakeRequest(DELETE, "/user/" + u.getId() + "/bankAccount/" + b.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }
    
    private User initializeData(boolean insertBankAccount) {
		User u = UserTest.insertUser("Die", "dD1234", "Diego S");
		BankAccount b;
		if(insertBankAccount) {
			b = BankAccountTest.insertBankAccount("0418", "2100", "45", "0200051332", 
					BigDecimal.valueOf(24512.12), "Description");
			
			b.setUser(u);
			b.update();
		} else {
			b = BankAccountTest.getBankAccount("0418", "2100", "45", "0200051332", 
					BigDecimal.valueOf(24512.12), "Description");
		}
		
		List<BankAccount> lstBanks = new ArrayList<BankAccount>();
		lstBanks.add(b);
		u.setBankAccounts(lstBanks);
		
		return u;
    }

}
