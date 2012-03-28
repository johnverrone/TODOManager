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
public class CategoryManager {
	private CategoryOpenHelper ourHelper;
	private SQLiteDatabase categories;
	private final Context ourContext;
	public static final String CATEGORY_ID = "_id";
	public static final String CATEGORY_NAME = "user_name";
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "categories";
	private static final String DATABASE_TABLE_NAME = "userTable";
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE_NAME + " (" + CATEGORY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CATEGORY_NAME
			+ " TEXT NOT NULL);";

	/**
	 * 
	 * @author Vertigo
	 *
	 */
	public class CategoryOpenHelper extends SQLiteOpenHelper {

		/**
		 * 
		 * @param context
		 */
		public CategoryOpenHelper(Context context) {
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

	/**
	 * constructor that sets up context
	 * @param c
	 */
	public CategoryManager(Context c) {
		ourContext = c;	
	}

	/**
	 * Opens the database help to allow writing to the database.
	 * 
	 * @return this
	 */
	public CategoryManager open() {
		ourHelper = new CategoryOpenHelper(ourContext);
		categories = ourHelper.getWritableDatabase();
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
		cv.put(CATEGORY_NAME, "defalt");
		categories.insert(DATABASE_TABLE_NAME, null, cv);
	}

	/**
	 * Add a new user to the database.
	 * @param u new user
	 * @return the row id of the new user
	 */
	public long addCategory(Category c) {
		ContentValues cv = new ContentValues();
		cv.put(CATEGORY_NAME, c.getName());
		return categories.insert(DATABASE_TABLE_NAME, null, cv);
	}
	
	/**
	 * Get a Collection of all categories
	 * @return categoryArray
	 */
	public List<Category> getAllCategories() {
		String[] columns = new String[]{ CATEGORY_ID, CATEGORY_NAME};
		List<Category> categoryArray = new LinkedList<Category>();
		Cursor c = categories.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			categoryArray.add(new Category(c.getString(1)));
		}
		return categoryArray;
	}
	
	/**
	 * returns cursor that gets all categories
	 * @return all categories
	 */
	public Cursor fetchAllCategories() {
		return categories.query(DATABASE_TABLE_NAME, new String[]{CATEGORY_ID, CATEGORY_NAME}, null, null, null, null, null);
	}
	
	/**
	 * delete a category from the database
	 * @param c
	 */
	public void removeCategory(Category c) {

	}
}
