package cs2340.todo.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import cs2340.todo.model.Category;
import cs2340.todo.model.TODOItem;
import cs2340.todo.model.TODOManager;
import cs2340.todo.model.User;

/**
 * 
 * @author Vertigo
 *
 */
public class NewItemActivity extends Activity {

	private Button btnDone, btnCancel;
	private EditText txtTitle, txtDescription, txtDate, txtTime, txtLocation;
	private Spinner spinCategory;
	private String title, description, location;
	private Category category;
	private int mYear, mMonth, mDay;
	private int mHour, mMinute;
	private Date date;
	private User user;
	private TODOManager manager;
	
	
	private static final int DATE_DIALOG_ID = 0;
	private static final int TIME_DIALOG_ID = 1;

	/**
	 * Called when the activity is created
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_item);
		
		//get user info from TODOManagerActivity
		Bundle getU = getIntent().getExtras();
        user = new User(getU.getString("name"), getU.getString("username"), getU.getString("email"), getU.getString("password"));

		btnDone = (Button) findViewById(R.id.btnDone);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		txtDescription = (EditText) findViewById(R.id.txtDescription);
		txtDate = (EditText) findViewById(R.id.txtDate);
		txtTime = (EditText) findViewById(R.id.txtTime);
		txtLocation = (EditText) findViewById(R.id.txtLocation);
		spinCategory = (Spinner) findViewById(R.id.spinCategory);
	
		//Category spin adapter
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		this, R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCategory.setAdapter(adapter);
		
		manager = new TODOManager(NewItemActivity.this);

		btnDone.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				title = txtTitle.getText().toString().trim();
				description = txtDescription.getText().toString().trim();
				location = txtLocation.getText().toString();
				category = new Category(spinCategory.getSelectedItem().toString());
				TODOItem newItem = new TODOItem(user.getUsername(), title, description, date, location, category);
				
				
				if (checkItem()) {
					manager.open();
					manager.addItem(user.getUsername(), newItem);
				    for (TODOItem i : manager.getUserItems(user)) {
						Log.d("items", i.toString());
					}
					manager.close();
					
					AlertDialog newItemCreated = new AlertDialog.Builder(NewItemActivity.this).create();
					newItemCreated.setTitle("New Item Created!");
					newItemCreated.setMessage("Your task has been created.");
					newItemCreated.setButton("Continue", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							setResult(RESULT_OK);
							finish();
						}
					});
					newItemCreated.show();

				}
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		txtTitle.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				manager.open();
				for(TODOItem i : manager.getUserItems(user)) {
					if(txtTitle.getText().toString().equals(i.getTitle())) {
						Toast itemExists = Toast.makeText(txtTitle.getContext(), "Item already exists", Toast.LENGTH_SHORT);
						itemExists.setGravity(Gravity.RIGHT, -10, -185);
						itemExists.show();
					}
				}
				manager.close();
			}
			
		});
		

		txtDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		txtTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mHour = c.get(Calendar.HOUR);
		mMinute = c.get(Calendar.MINUTE);

		date = new Date();
		
		spinCategory.setOnItemSelectedListener(new MyOnItemSelectedListener());
		updateDisplay();
	}

	/**
	 * checks if new item info is correct
	 * @return boolean
	 */
	public boolean checkItem() {
		if(category.toString().equals("Choose a category")) {
			Toast.makeText(NewItemActivity.this, "Please choose a category", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(title.isEmpty()) {
			Toast.makeText(NewItemActivity.this, "Item must have a title!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * update the display after opening a dialog
	 */
	private void updateDisplay() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/d/yy");
		txtDate.setText(dateFormatter.format(date));
		SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
		txtTime.setText(timeFormatter.format(date));
	}

	/**
	 * DatePicker dialog
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			date.setYear(year-1900);
			date.setMonth(monthOfYear);
			date.setDate(dayOfMonth);
			updateDisplay();
		}
	};

	/**
	 * TimePicker dialog
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			date.setHours(hourOfDay);
			date.setMinutes(minute);
			updateDisplay();
		}
	};
	
	/**
	 * custom dialog for creating new category
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this,
					mTimeSetListener, mHour, mMinute, false);
		}
		return null;
	}
	
	/**
	 * 
	 * @author Vertigo
	 *
	 */
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	      // Do nothing.
	    }
	}

}