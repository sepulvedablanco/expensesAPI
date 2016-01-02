package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static org.junit.Assert.*;
import java.util.List;
import java.math.BigDecimal;
import org.junit.Test;
import play.data.Form;
import play.libs.Json;

public class BankAccountTest {

    @Test
    public void entityRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setOffice("0418");
			bankAccount.setControlDigit("45");
			bankAccount.setAccountNumber("0200051332");
			bankAccount.setBalance(BigDecimal.valueOf(24512.12));
			bankAccount.setDescription("Description");
			
			Form<BankAccount> form = Form.form(BankAccount.class).bind(Json.toJson(bankAccount));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("entity").errors().size());
    		assertTrue(form.field("office").errors().isEmpty());
    		assertTrue(form.field("controlDigit").errors().isEmpty());
    		assertTrue(form.field("accountNumber").errors().isEmpty());
    		assertTrue(form.field("balance").errors().isEmpty());
    		assertTrue(form.field("description").errors().isEmpty());
		});
    }

    @Test
    public void officeRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setEntity("2100");
			bankAccount.setControlDigit("45");
			bankAccount.setAccountNumber("0200051332");
			bankAccount.setBalance(BigDecimal.valueOf(24512.12));
			bankAccount.setDescription("Description");
			
			Form<BankAccount> form = Form.form(BankAccount.class).bind(Json.toJson(bankAccount));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("office").errors().size());
    		assertTrue(form.field("entity").errors().isEmpty());
    		assertTrue(form.field("controlDigit").errors().isEmpty());
    		assertTrue(form.field("accountNumber").errors().isEmpty());
    		assertTrue(form.field("balance").errors().isEmpty());
    		assertTrue(form.field("description").errors().isEmpty());
		});
    }
    
    @Test
    public void controlDigitRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setOffice("0418");
			bankAccount.setEntity("2100");
			bankAccount.setAccountNumber("0200051332");
			bankAccount.setBalance(BigDecimal.valueOf(24512.12));
			bankAccount.setDescription("Description");
			
			Form<BankAccount> form = Form.form(BankAccount.class).bind(Json.toJson(bankAccount));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("controlDigit").errors().size());
    		assertTrue(form.field("entity").errors().isEmpty());
    		assertTrue(form.field("office").errors().isEmpty());
    		assertTrue(form.field("accountNumber").errors().isEmpty());
    		assertTrue(form.field("balance").errors().isEmpty());
    		assertTrue(form.field("description").errors().isEmpty());
		});
    }
    
    public void accountNumberRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setOffice("0418");
			bankAccount.setEntity("2100");
			bankAccount.setControlDigit("45");
			bankAccount.setBalance(BigDecimal.valueOf(24512.12));
			bankAccount.setDescription("Description");
			
			Form<BankAccount> form = Form.form(BankAccount.class).bind(Json.toJson(bankAccount));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("accountNumber").errors().size());
    		assertTrue(form.field("entity").errors().isEmpty());
    		assertTrue(form.field("office").errors().isEmpty());
    		assertTrue(form.field("controlDigit").errors().isEmpty());
    		assertTrue(form.field("balance").errors().isEmpty());
    		assertTrue(form.field("description").errors().isEmpty());
		});
    }

    public void balanceRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setOffice("0418");
			bankAccount.setEntity("2100");
			bankAccount.setControlDigit("45");
			bankAccount.setAccountNumber("0200051332");
			bankAccount.setDescription("Description");
			
			Form<BankAccount> form = Form.form(BankAccount.class).bind(Json.toJson(bankAccount));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("balance").errors().size());
    		assertTrue(form.field("entity").errors().isEmpty());
    		assertTrue(form.field("office").errors().isEmpty());
    		assertTrue(form.field("controlDigit").errors().isEmpty());
    		assertTrue(form.field("accountNumber").errors().isEmpty());
    		assertTrue(form.field("description").errors().isEmpty());
		});
    }
    
    public void descriptionRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = new BankAccount();
			bankAccount.setOffice("0418");
			bankAccount.setEntity("2100");
			bankAccount.setControlDigit("45");
			bankAccount.setAccountNumber("0200051332");
			bankAccount.setBalance(BigDecimal.valueOf(24512.12));
			
			Form<BankAccount> form = Form.form(BankAccount.class).bind(Json.toJson(bankAccount));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("description").errors().size());
    		assertTrue(form.field("entity").errors().isEmpty());
    		assertTrue(form.field("office").errors().isEmpty());
    		assertTrue(form.field("controlDigit").errors().isEmpty());
    		assertTrue(form.field("accountNumber").errors().isEmpty());
    		assertTrue(form.field("balance").errors().isEmpty());
		});
    }    

    @Test
    public void saveBankAccount() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = insertBankAccount("0418", "2100", "45", "0200051332", 
					BigDecimal.valueOf(24512.12), "Description");
			
			BankAccount bankAccountSaved = BankAccount.find.byId(bankAccount.getId());
			
			assertEquals(bankAccount.getOffice(), bankAccountSaved.getOffice());
			assertEquals(bankAccount.getEntity(), bankAccountSaved.getEntity());
			assertEquals(bankAccount.getControlDigit(), bankAccountSaved.getControlDigit());
			assertEquals(bankAccount.getAccountNumber(), bankAccountSaved.getAccountNumber());
			assertEquals(bankAccount.getBalance(), bankAccountSaved.getBalance());
			assertEquals(bankAccount.getDescription(), bankAccountSaved.getDescription());
			assertNotNull(bankAccountSaved.getId());
		});
    }
    
    @Test
    public void checkGeneratedIban() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = insertBankAccount("0418", "2100", "55", "0200051332", 
					BigDecimal.valueOf(24512.12), "Description");
			
			BankAccount bankAccountSaved = BankAccount.find.byId(bankAccount.getId());
			
			assertEquals("ES53", bankAccountSaved.getIban());
		});
    }
    
    @Test
    public void deleteBankAccount() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = insertBankAccount("0418", "2100", "45", "0200051332", 
					BigDecimal.valueOf(24512.12), "Description");
			
			BankAccount bankAccountSaved = BankAccount.find.byId(bankAccount.getId());
			bankAccountSaved.delete();
			
			assertNull(BankAccount.find.byId(bankAccount.getId()));
		});
    }

    @Test
    public void updateBankAccount() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			BankAccount bankAccount = insertBankAccount("0418", "2100", "45", "0200051332", 
					BigDecimal.valueOf(24512.12), "Description");
			
			BankAccount bankAccountSaved = BankAccount.find.byId(bankAccount.getId());
			bankAccountSaved.setDescription("Updated description");
			bankAccountSaved.setBalance(BigDecimal.valueOf(93518.12));
			boolean changed = bankAccount.changeData(bankAccountSaved);
			bankAccount.save();
			
			assertTrue(changed);
			
			BankAccount bankAccountUpdated = BankAccount.find.byId(bankAccount.getId());
			assertEquals(bankAccountSaved.getDescription(), bankAccountUpdated.getDescription());
			assertEquals(bankAccountSaved.getBalance(), bankAccountUpdated.getBalance());
			assertEquals(bankAccountSaved.getId(), bankAccountUpdated.getId());
		});
    }    

    public static BankAccount getBankAccount(String office, String entity, String controlDigit,
    		String accountNumber, BigDecimal balance, String description) {	
    	BankAccount bankAccount = new BankAccount();
		bankAccount.setOffice(office);
		bankAccount.setEntity(entity);
		bankAccount.setControlDigit(controlDigit);
		bankAccount.setAccountNumber(accountNumber);
		bankAccount.setBalance(balance);
		bankAccount.setDescription(description);

		return bankAccount;
    }

    public static BankAccount insertBankAccount(String office, String entity, String controlDigit,
    		String accountNumber, BigDecimal balance, String description) {
		BankAccount bankAccount = getBankAccount(office, entity, controlDigit, accountNumber, balance, description);
		
		bankAccount.save();
		
		return bankAccount;
    }
    
}
