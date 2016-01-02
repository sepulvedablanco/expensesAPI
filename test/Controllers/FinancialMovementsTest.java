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
import java.util.Date;
import java.util.List;
import models.BankAccount;
import models.BankAccountTest;
import models.FinancialMovement;
import models.FinancialMovementSubtype;
import models.FinancialMovementSubtypeTest;
import models.FinancialMovementTest;
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

public class FinancialMovementsTest {

    @Test
    public void findFinancialMovementJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements?id=" + fm.getId())
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
	   		assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
	  		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements?id=" + fm.getId())
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
    public void findFinancialMovementWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements?id=" + fm.getId())
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
    public void findFinancialMovementsJson() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
			assertNotNull(result);
    		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
    		assertEquals(ContentType.APPLICATION_JSON.getMimeType(), result.contentType());
		});
    }

    @Test
    public void findFinancialMovementsWithApplicationXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements")
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
    public void findFinancialMovementsWithTextXml() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements")
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
    public void amountFinancialMovement() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();

			RequestBuilder request = fakeRequest(GET, "/user/" + u.getId() + "/financialMovements/amounts")
    				.header(Constants.Headers.USER_TOKEN, u.getAuthToken());
	    		
	    	Result result = route(request);
	   		assertNotNull(result);
	   		assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }
    
    @Test
    public void insertFinancialMovement() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(false);
			User u = fm.getUser();
			
	    	JsonNode jsonFM = Json.toJson(fm);
	    	ObjectNode jsonObject = (ObjectNode) jsonFM;
	    	// bankAccount field is ignored while is converted to JSON
	    	jsonObject.put("bankAccount.id", fm.getBankAccount().getId());
	    	// financialMovementType field is ignored while is converted to JSON
	    	jsonObject.put("financialMovementType.id", fm.getFinancialMovementType().getId());
	    	// financialMovementSubtype field is ignored while is converted to JSON
	    	jsonObject.put("financialMovementSubtype.id", fm.getFinancialMovementSubtype().getId());

			RequestBuilder request = fakeRequest(POST, "/user/" + u.getId() + "/financialMovement")
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType())
					.bodyJson(jsonFM);
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.CREATED, result.status());
		});
    }

    @Test
    public void updateFinancialMovement() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();
			
			RequestBuilder request = fakeRequest(PUT, "/user/" + u.getId() + 
					"/financialMovement/" + fm.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.NOT_IMPLEMENTED, result.status());
		});
    }

    @Test
    public void deleteFinancialMovement() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = initializeData(true);
			User u = fm.getUser();
			
	    	JsonNode jsonFM = Json.toJson(fm);

			RequestBuilder request = fakeRequest(DELETE, "/user/" + u.getId() + 
					"/financialMovement/" + fm.getId())
					.header(Constants.Headers.USER_TOKEN, u.getAuthToken())
					.header(ACCEPT, ContentType.TEXT_XML.getMimeType());
			
	    	Result result = route(request);
	    	assertNotNull(result);
	    	assertNotNull(result.contentType());
	    	assertEquals(Http.Status.OK, result.status());
		});
    }
    
    private FinancialMovement initializeData(boolean insertFinancialMovement) {
		User u = UserTest.insertUser("Die", "dD1234", "Diego S");
		BankAccount b = BankAccountTest.insertBankAccount("0418", "2100", "45", "0200051332", 
				BigDecimal.valueOf(24512.12), "Description");
		FinancialMovementType fmt = FinancialMovementTypeTest.
				insertFinancialMovementType("Type description");
		FinancialMovementSubtype fms = FinancialMovementSubtypeTest.
				insertFinancialMovementSubtype("Subtype description");
		
		FinancialMovement fm;
		if(insertFinancialMovement) {
			fm = FinancialMovementTest.insertFinancialMovement(BigDecimal.valueOf(24.55), 
					"Finan Mov. test", true, new Date());
		} else {
			fm = FinancialMovementTest.getFinancialMovement(BigDecimal.valueOf(24.55), 
					"Finan Mov. test", true, new Date());
		}
		
		b.setUser(u);
		b.update();
		
		List<BankAccount> lstBanks = new ArrayList<BankAccount>();
		lstBanks.add(b);
		u.setBankAccounts(lstBanks);

		fms.setFinancialMovementType(fmt);
		fms.update();

		List<FinancialMovementSubtype> lstFms = new ArrayList<FinancialMovementSubtype>();
		lstFms.add(fms);
		fmt.setFinancialMovementSubtypes(lstFms);
		fmt.setUser(u);
		fmt.update();
		
		fm.setUser(u);
		fm.setBankAccount(b);
		fm.setFinancialMovementType(fmt);
		fm.setFinancialMovementSubtype(fms);
		
		if(insertFinancialMovement) {
			fm.update();
		}

		List<FinancialMovementType> lstFmt = new ArrayList<FinancialMovementType>();
		lstFmt.add(fmt);
		u.setFinancialMovementTypes(lstFmt);
		
		fm.setUser(u);

		return fm;
    }

}
