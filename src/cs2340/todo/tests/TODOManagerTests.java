package cs2340.todo.tests;

import java.util.List;

import cs2340.todo.model.User;
import cs2340.todo.model.UserManager;
import cs2340.todo.view.NewUserActivity;
import junit.framework.TestCase;

public class TODOManagerTests extends TestCase {
	
	public TODOManagerTests(String name) {
		super(name);
	}
	
	public void setUp() throws Exception {
	}
	
	public void testName() {
		User test = new User("Test", "test","test@test.com", "test");
		test.setName("change");
		assertEquals("change", test.getName());
	}
	
	public void testAddUser() {
		UserManager manager = new UserManager(new NewUserActivity().getApplicationContext());
		manager.open();
		manager.addUser(new User("Test", "test2", "test@yahoo.com", "test"));
		List<User> users = manager.getAllUsers();
		manager.close();
		assertTrue(users.contains(new User("Test", "test3", "test@yahoo.com", "test")));
	}
	
}
