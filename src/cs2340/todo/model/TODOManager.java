package cs2340.todo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class TODOManager {
	private UserOpenHelper ourHelper;
	private SQLiteDatabase items;
	private final Context ourContext;
	public static final String ITEM_ID = "_id";
	public static final String ITEM_USER = "item_user";
	public static final String ITEM_TITLE = "item_title";
	public static final String ITEM_DESCRIPTION = "item_description";
	public static final String ITEM_DATE = "item_date";
	public static final String ITEM_LOCATION="item_location";
	public static final String ITEM_CATEGORY = "item_category";
	public static final String ITEM_COMPLETE = "item_complete";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "items";
	private static final String DATABASE_TABLE_NAME = "itemTable";
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ DATABASE_TABLE_NAME + " (" + ITEM_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM_USER + " TEXT NOT NULL, "+ ITEM_TITLE + " TEXT NOT NULL, "
			+ ITEM_DESCRIPTION + " TEXT NOT NULL, " + ITEM_DATE + " TEXT NOT NULL, " + ITEM_LOCATION + " TEXT NOT NULL, " 
			+ ITEM_CATEGORY + " TEXT NOT NULL, " + ITEM_COMPLETE + " TINYINT);";
	
	/**
	 * 
	 * @author Vertigo
	 *
	 */
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
			db.delete(DATABASE_TABLE_NAME, null, null);
		}

	}
	
	public TODOManager(Context c) {
		ourContext = c;
			
	}

	/**
	 * Opens the database help to allow writing to the database.
	 * 
	 * @return this
	 */
	public TODOManager open() {
		ourHelper = new UserOpenHelper(ourContext);
		items = ourHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the helper to prevent stray database manipulation.
	 */
	public void close() {
		ourHelper.close();
	}
	
	/**
	 * add a new item to item database
	 * @param i item to be added
	 * @return long
	 */
	public long addItem(String u, TODOItem i) {
		ContentValues cv = new ContentValues();
		cv.put(ITEM_USER, u);
		cv.put(ITEM_TITLE, i.getTitle());
		cv.put(ITEM_DESCRIPTION, i.getDescription());
		cv.put(ITEM_DATE, i.getDate().toString());
		cv.put(ITEM_LOCATION, i.getLocation());
		cv.put(ITEM_CATEGORY, i.getCategory().toString());
		cv.put(ITEM_COMPLETE, i.getComplete());
		return items.insert(DATABASE_TABLE_NAME, null, cv);
	}
	
	/**
	 * reset the TODOItem database
	 */
	public void reset() {
		ourHelper.getWritableDatabase().delete(DATABASE_TABLE_NAME, null, null);
		items.insert(DATABASE_TABLE_NAME, null, null);
	}
	
	/**
	 * Update an item in the database
	 * @param u
	 * @param i
	 */
	public void updateItem(User u, TODOItem i) {
		String[] columns = new String[] { ITEM_ID, ITEM_USER, ITEM_TITLE, ITEM_DESCRIPTION, ITEM_DATE, ITEM_LOCATION, ITEM_CATEGORY, ITEM_COMPLETE};
		Cursor c = items.query(DATABASE_TABLE_NAME, columns, ITEM_USER + "='" + u.getUsername() + "'", null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			if(c.getString(2).equals(i.getTitle())) {
				int id = c.getInt(0);
				ContentValues cv = new ContentValues();
				cv.put(ITEM_TITLE, i.getTitle());
				cv.put(ITEM_DESCRIPTION, i.getDescription());
				cv.put(ITEM_DATE, i.getDate().toString());
				cv.put(ITEM_LOCATION, i.getLocation());
				cv.put(ITEM_CATEGORY, i.getCategory().toString());
				cv.put(ITEM_COMPLETE, i.getComplete());
				items.update(DATABASE_TABLE_NAME, cv, ITEM_ID + "='" + id + "'", null);
			}
		}
	}
	
	/**
	 * get all items for current user
	 * @return List of items
	 */
	public List<TODOItem> getUserItems(User u) {
		String[] columns = new String[] { ITEM_ID, ITEM_USER, ITEM_TITLE, ITEM_DESCRIPTION, ITEM_DATE, ITEM_LOCATION, ITEM_CATEGORY, ITEM_COMPLETE};
		List<TODOItem> itemArray = new LinkedList<TODOItem>();
		Cursor c = items.query(DATABASE_TABLE_NAME, columns, ITEM_USER + "='" + u.getUsername() + "'", null, null, null, null);
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
			try {
				TODOItem newItem = new TODOItem(c.getString(1), c.getString(2), c.getString(3), df.parse(c.getString(4)), c.getString(5), new Category(c.getString(6)), c.getInt(7));
				newItem.setId(c.getInt(0));
				itemArray.add(newItem);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return itemArray;
	}
}
