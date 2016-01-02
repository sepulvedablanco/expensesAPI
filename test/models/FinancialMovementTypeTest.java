package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import play.data.Form;
import play.libs.Json;

public class FinancialMovementTypeTest {

    @Test
    public void descriptionRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
	    	FinancialMovementType fmt = new FinancialMovementType();

			Form<FinancialMovementType> form = Form.form(FinancialMovementType.class).bind(Json.toJson(fmt));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("description").errors().size());
		});
    }

    @Test
    public void saveFinancialMovementType() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementType fmt = insertFinancialMovementType("Car");
			
			FinancialMovementType fmtSaved = FinancialMovementType.find.byId(fmt.getId());
			
			assertEquals(fmt.getDescription(), fmtSaved.getDescription());
			assertNotNull(fmtSaved.getId());
		});
    }
    
    @Test
    public void deleteFinancialMovementType() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementType fmt = insertFinancialMovementType("Car");
			
			FinancialMovementType fmtSaved = FinancialMovementType.find.byId(fmt.getId());
			fmtSaved.delete();
			
			assertNull(FinancialMovementType.find.byId(fmt.getId()));
		});
    }

    @Test
    public void updateFinancialMovementType() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementType fmt = insertFinancialMovementType("Car");
			
			FinancialMovementType fmtSaved = FinancialMovementType.find.byId(fmt.getId());
			fmtSaved.setDescription("Food");
			boolean changed = fmt.changeData(fmtSaved);
			fmt.save();
			
			assertTrue(changed);
			
			FinancialMovementType fmtUpdated = FinancialMovementType.find.byId(fmt.getId());
			assertEquals(fmtSaved.getId(), fmtUpdated.getId());
			assertEquals(fmtSaved.getDescription(), fmtUpdated.getDescription());
		});
    }    

    public static FinancialMovementType getFinancialMovementType(String description) {
    	FinancialMovementType fmt = new FinancialMovementType();
		fmt.setDescription(description);
		
		return fmt;
    }
    
    public static FinancialMovementType insertFinancialMovementType(String description) {
    	FinancialMovementType fmt = getFinancialMovementType(description);
    	
    	fmt.save();
		
		return fmt;
    }
    
}
