package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import play.data.Form;
import play.libs.Json;

public class FinancialMovementSubtypeTest {

    @Test
    public void descriptionRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementSubtype fms = new FinancialMovementSubtype();

			Form<FinancialMovementSubtype> form = Form.form(FinancialMovementSubtype.class).bind(Json.toJson(fms));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("description").errors().size());
		});
    }

    @Test
    public void saveFinancialMovementSubtype() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementSubtype fms = insertFinancialMovementSubtype("Gasoil");
			
			FinancialMovementSubtype fmsSaved = FinancialMovementSubtype.find.byId(fms.getId());
			
			assertEquals(fms.getDescription(), fmsSaved.getDescription());
			assertNotNull(fmsSaved.getId());
		});
    }
    
    @Test
    public void deleteFinancialMovementSubtype() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementSubtype fms = insertFinancialMovementSubtype("Gasoil");
			
			FinancialMovementSubtype fmsSaved = FinancialMovementSubtype.find.byId(fms.getId());
			fmsSaved.delete();
			
			assertNull(FinancialMovementSubtype.find.byId(fms.getId()));
		});
    }

    @Test
    public void updateFinancialMovementSubtype() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			FinancialMovementSubtype fms = insertFinancialMovementSubtype("Gasoil");
			
			FinancialMovementSubtype fmsSaved = FinancialMovementSubtype.find.byId(fms.getId());
			fmsSaved.setDescription("MOT");
			boolean changed = fms.changeData(fmsSaved);
			fms.save();
			
			assertTrue(changed);
			
			FinancialMovementSubtype fmsUpdated = FinancialMovementSubtype.find.byId(fms.getId());
			assertEquals(fmsSaved.getId(), fmsUpdated.getId());
			assertEquals(fmsSaved.getDescription(), fmsUpdated.getDescription());
		});
    }    

    public static FinancialMovementSubtype getFinancialMovementSubtype(String description) {
    	FinancialMovementSubtype fms = new FinancialMovementSubtype();
		fms.setDescription(description);
		return fms;
    }
    
    public static FinancialMovementSubtype insertFinancialMovementSubtype(String description) {
    	FinancialMovementSubtype fms = getFinancialMovementSubtype(description);
    	
    	fms.save();
		
		return fms;
    }
    
}
