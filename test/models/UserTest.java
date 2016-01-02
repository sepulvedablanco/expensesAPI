package models;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import play.data.Form;
import play.libs.Json;

public class UserTest {

    @Test
    public void userRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User user = new User();
			user.setPass("dD1234");
			user.setName("Diego");
			
			Form<User> form = Form.form(User.class).bind(Json.toJson(user));
    		
    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("user").errors().size());
//    		assertTrue(form.field("pass").errors().isEmpty()); // This field is ignored when calling toJson
    		assertTrue(form.field("name").errors().isEmpty());
		});
    }

    @Test
    public void passRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User user = new User();
			user.setUser("die");
			user.setName("Diego");
			
    		Form<User> form = Form.form(User.class).bind(Json.toJson(user));

    		assertTrue(form.hasErrors());
    		assertEquals(2, form.field("pass").errors().size());
    		assertTrue(form.field("user").errors().isEmpty());
    		assertTrue(form.field("name").errors().isEmpty());
		});
    }
    
    @Test
    public void nameRequired() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User user = new User();
			user.setUser("die");
			user.setPass("DASas1204");
			
    		Form<User> form = Form.form(User.class).bind(Json.toJson(user));

    		assertTrue(form.hasErrors());
    		assertEquals(1, form.field("name").errors().size());
    		assertTrue(form.field("user").errors().isEmpty());
//    		assertTrue(form.field("pass").errors().isEmpty()); // This field is ignored when calling toJson
		});
    }

    
    @Test
    public void saveUser() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User user = insertUser("Die", "AEFeaef214", "Diego");
			
			User userSaved = User.find.byId(user.getId());
			
			assertEquals(user.getUser(), userSaved.getUser());
			assertEquals(user.getPass(), userSaved.getPass());
			assertEquals(user.getName(), userSaved.getName());
			assertNotNull(user.getId());
			
		});
    }
    
    @Test
    public void deleteUser() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User user = insertUser("Die", "AEFeaef214", "Diego");
			
			User userSaved = User.find.byId(user.getId());
			userSaved.delete();
			
			assertNull(User.find.byId(user.getId()));
		});
    }

    @Test
    public void updateUser() {
		running(fakeApplication(inMemoryDatabase()), () -> {
			User user = insertUser("Die", "AEFeaef214", "Diego");
			
			User userSaved = User.find.byId(user.getId());
			userSaved.setUser("Die2");
			userSaved.setPass("rbsSRG45");
			boolean changed = user.changeData(userSaved);
			user.save();
			
			assertTrue(changed);
			
			User userUpdated = User.find.byId(user.getId());
			assertEquals(userSaved.getUser(), userUpdated.getUser());
			assertNotEquals(userSaved.getPass(), userUpdated.getPass()); // The pass has been encrypted
			assertEquals(userSaved.getName(), userUpdated.getName());
			assertEquals(userSaved.getId(), userUpdated.getId());
		});
    }    
    
    public static User getUser(String userName, String pass, String name) {
		User user = new User();
		user.setUser(userName);
		user.setPass(pass);
		user.setName(name);
		
		return user;
    }
    
    public static User insertUser(String userName, String pass, String name) {
		User user = getUser(userName, pass, name);
		
		user.save();
		
		return user;
    }
    
}
