package cs2340.todo.tests;

import java.util.List;

import cs2340.todo.model.Category;
import cs2340.todo.model.User;
import cs2340.todo.model.UserManager;
import cs2340.todo.view.NewItemActivity;
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
		assertTrue("User was not added!",users.contains(new User("Test", "test3", "test@yahoo.com", "test")));
	}


	public void testUser() {
		NewUserActivity mActivity = new NewUserActivity();
		mActivity.setVars("", "fish", "fish", "fish@fish.com", "fish");
		assertFalse("Does not fail on a blank name!", mActivity.checkUser());
		mActivity.setVars("Fish", "fish", "fish", "fish@fish.com", "");
		assertFalse("Does not fail on a blank username!", mActivity.checkUser());
		mActivity.setVars("Fish", "fish", "fish", "fish@fish.fish", "fish");
		assertFalse("Does not fail on a bad email!", mActivity.checkUser());
		mActivity.setVars("Fish", "fish", "notfish", "fish@fish.com", "fish");
		assertFalse("Does not fail on a bad password!", mActivity.checkUser());
		mActivity.setVars("Fish", "fish", "fish", "fish@fish.com", "fish");
		assertTrue("Does not pass with good arguments!", mActivity.checkUser());
	}

	public void testCheckItem() { 
		String title = "";
		Category cat =  new Category("Pets");
		NewItemActivity mActivity = new NewItemActivity();
		mActivity.setVaribles(title, cat);
		boolean answer = mActivity.checkItem();
		assertEquals(false, answer);
		title = "Simba";
		mActivity.setVaribles(title, cat);
		answer = mActivity.checkItem();
		assertEquals(true, answer);
	} 

}
