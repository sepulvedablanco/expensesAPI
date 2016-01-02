package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static org.junit.Assert.*;
import helpers.Constants;
import helpers.ControllerHelper;
import helpers.Constants.JsonResponseTag;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.Form;
import play.libs.Json;

public class FinancialMovementTest {

    @Test
    public void expenseRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
	    	FinancialMovement fm = new FinancialMovement();
	    	fm.setAmount(BigDecimal.valueOf(124.55));
	    	fm.setConcept("Photo camera");
			fm.setTransactionDate(new Date());
			
			Form<FinancialMovement> form = Form.form(FinancialMovement.class).bind(Json.toJson(fm));
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("expense").errors().size());
    		assertTrue(form.field("amount").errors().isEmpty());
    		assertTrue(form.field("concept").errors().isEmpty());
    		assertTrue(form.field("transactionDate").errors().isEmpty());
		});
    }

    @Test
    public void conceptRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
	    	FinancialMovement fm = new FinancialMovement();
	    	fm.setAmount(BigDecimal.valueOf(124.55));
	    	fm.setExpense(true);
			fm.setTransactionDate(new Date());

			Form<FinancialMovement> form = Form.form(FinancialMovement.class).bind(Json.toJson(fm));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("concept").errors().size());
    		assertTrue(form.field("amount").errors().isEmpty());
    		assertTrue(form.field("expense").errors().isEmpty());
    		assertTrue(form.field("transactionDate").errors().isEmpty());
		});
    }
    
    @Test
    public void amountRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
	    	FinancialMovement fm = new FinancialMovement();
	    	fm.setConcept("Photo camera");
	    	fm.setExpense(true);
			fm.setTransactionDate(new Date());

			Form<FinancialMovement> form = Form.form(FinancialMovement.class).bind(Json.toJson(fm));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("amount").errors().size());
    		assertTrue(form.field("concept").errors().isEmpty());
    		assertTrue(form.field("expense").errors().isEmpty());
    		assertTrue(form.field("transactionDate").errors().isEmpty());
		});
    }

    @Test
    public void saveFinancialMovement() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = insertFinancialMovement(BigDecimal.valueOf(124.55), 
					"Photo camera", true, new Date());
			
			FinancialMovement fmSaved = FinancialMovement.find.byId(fm.getId());
			
			assertEquals(fm.getAmount(), fmSaved.getAmount());
			assertEquals(fm.getConcept(), fmSaved.getConcept());
			assertEquals(fm.getExpense(), fmSaved.getExpense());
			assertEquals(fm.getTransactionDate(), fmSaved.getTransactionDate());
			assertNotNull(fmSaved.getId());
		});
    }
    
    @Test
    public void deleteFinancialMovement() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovement fm = insertFinancialMovement(BigDecimal.valueOf(124.55), 
					"Photo camera", true, new Date());
			
			FinancialMovement fmSaved = FinancialMovement.find.byId(fm.getId());
			fmSaved.delete();
			
			assertNull(FinancialMovement.find.byId(fm.getId()));
		});
    }
    
    public static FinancialMovement getFinancialMovement(BigDecimal amount, String concept, 
    		boolean expense, Date transactionDate) {
    	FinancialMovement fm = new FinancialMovement();
    	fm.setAmount(amount);
    	fm.setConcept(concept);
    	fm.setExpense(expense);
		fm.setTransactionDate(transactionDate);
		
		return fm;
    }
    
    public static FinancialMovement insertFinancialMovement(BigDecimal amount, String concept, 
    		boolean expense, Date transactionDate) {
    	FinancialMovement fm = getFinancialMovement(amount, concept, expense, transactionDate);
    	
    	fm.save();
		
		return fm;
    }
    
}
