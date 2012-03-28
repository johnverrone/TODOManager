package cs2340.todo.model;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Vertigo
 *
 */
public class UserManager {
	private UserOpenHelper ourHelper;
	private SQLiteDatabase users;
	private final Context ourContext;
	public static final String USER_ID = "_id";
	public static final String USER_NAME = "user_name";
	public static final String USER_USERNAME = "user_username";
	public static final String USER_PASSWORD = "user_password";
	public static final String USER_EMAIL = "user_email";
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "users";
	private static final String DATABASE_TABLE_NAME = "userTable";
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE_NAME + " (" + USER_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME
			+ " TEXT NOT NULL, " + USER_USERNAME + " TEXT NOT NULL, "
			+ USER_PASSWORD + " TEXT NOT NULL, " + USER_EMAIL
			+ " TEXT NOT NULL);";

	public class UserOpenHelper extends SQLiteOpenHelper {

		public UserOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DATABASE_CREATE);	
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

		}

	}

	public UserManager(Context c) {
		ourContext = c;
			
	}

	/**
	 * Opens the database help to allow writing to the database.
	 * 
	 * @return this
	 */
	public UserManager open() {
		ourHelper = new UserOpenHelper(ourContext);
		users = ourHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the helper to prevent stray database manipulation.
	 */
	public void close() {
		ourHelper.close();
	}
	
	/**
	 * Reset the database to just a default user.
	 */
	public void reset() {
		ourHelper.getWritableDatabase().delete(DATABASE_TABLE_NAME, null, null);
		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, "defalt");
		cv.put(USER_USERNAME, "default");
		cv.put(USER_PASSWORD, "default");
		cv.put(USER_EMAIL, "default@default.com");
		users.insert(DATABASE_TABLE_NAME, null, cv);
	}

	/**
	 * Add a new user to the database.
	 * @param u new user
	 * @return the row id of the new user
	 */
	public long addUser(User u) {
		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, u.getName());
		cv.put(USER_USERNAME, u.getUsername());
		cv.put(USER_PASSWORD, u.getPassword());
		cv.put(USER_EMAIL, u.getEmail());
		return users.insert(DATABASE_TABLE_NAME, null, cv);
	}
	
	/**
	 * get all users
	 * @return array of users
	 */
	public List<User> getAllUsers() {
		String[] columns = new String[]{ USER_ID, USER_NAME, USER_USERNAME, USER_PASSWORD, USER_EMAIL};
		List<User> userArray = new LinkedList<User>();
		Cursor c = users.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			userArray.add(new User(c.getString(1), c.getString(2), c.getString(4), c.getString(3)));
		}
		return userArray;
	}
	
	/**
	 * remove a user
	 * @param u user to be removed
	 */
	public void removeUser(User u) {

	}

}
