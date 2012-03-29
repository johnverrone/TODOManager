package cs2340.todo.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import cs2340.todo.model.Category;
import cs2340.todo.model.TODOItem;
import cs2340.todo.model.TODOManager;
import cs2340.todo.model.User;

public class EditItemActivity extends Activity {

	private Button btnDone, btnCancel;
	private EditText txtTitle, txtDescription, txtDate, txtTime, txtLocation;
	private Spinner spinCategory;
	private int mYear, mMonth, mDay;
	private int mHour, mMinute;
	private Date date;
	private TODOItem item;
	private User user;
	private TODOManager manager;
	
	
	private static final int DATE_DIALOG_ID = 0;
	private static final int TIME_DIALOG_ID = 1;

	/**
	 * Called when the activity is created
	 * @param Bundle saveInstanceState
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_item);
		
		//get user info from TODOManagerActivity
		Bundle getU = getIntent().getExtras();
        user = new User(getU.getString("name"), getU.getString("username"), getU.getString("email"), getU.getString("password"));
        
        //get item info from TODOManagerActivity
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        item = null;
        try {
			item = new TODOItem(getU.getString("username"), getU.getString("title"), getU.getString("description"), 
					df.parse(getU.getString("date")), getU.getString("location"), new Category(getU.getString("category")));
		} catch (ParseException e) {
			
		}
        
        //Set up views and date formats
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        String itemDate = dateFormat.format(item.getDate());
        String itemTime = timeFormat.format(item.getDate());
        
		btnDone = (Button) findViewById(R.id.btnDone);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		txtDescription = (EditText) findViewById(R.id.txtDescription);
		txtDate = (EditText) findViewById(R.id.txtDate);
		txtTime = (EditText) findViewById(R.id.txtTime);
		txtLocation = (EditText) findViewById(R.id.txtLocation);
		spinCategory = (Spinner) findViewById(R.id.spinCategory);
		
		txtTitle.setText(item.getTitle());
		txtDescription.setText(item.getDescription());
		txtDate.setText(itemDate);
		txtTime.setText(itemTime);
		txtLocation.setText(item.getLocation());
	
		//Category spinner adapter
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		this, R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinCategory.setAdapter(adapter);
		spinCategory.setSelection(adapter.getPosition(item.getCategory().toString()));
		spinCategory.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		//Instantiate the TODOManager that reads from database
		manager = new TODOManager(EditItemActivity.this);

		//Set up onClickListeners()
		btnDone.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//item.setTitle(txtTitle.getText().toString().trim());
				item.setDescription(txtDescription.getText().toString().trim());
				item.setDate(new Date(mYear-1900, mMonth, mDay, mHour, mMinute, 0));
				item.setLocation(txtLocation.getText().toString().trim());
				item.setCategory(new Category(spinCategory.getSelectedItem().toString()));
				manager.open();
				manager.updateItem(user, item);
				manager.close();
				setResult(RESULT_OK);
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		
		txtLocation.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent maps = new Intent(EditItemActivity.this, MainMapActivity.class);			
				startActivity(maps);
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

		//Get date of item
		mYear = Integer.parseInt(new SimpleDateFormat("yyyy").format(item.getDate()));
		mMonth =  Integer.parseInt(new SimpleDateFormat("MM").format(item.getDate()));
		mDay =  Integer.parseInt(new SimpleDateFormat("dd").format(item.getDate()));
		mHour =  Integer.parseInt(new SimpleDateFormat("hh").format(item.getDate()));
		mMinute =  Integer.parseInt(new SimpleDateFormat("mm").format(item.getDate()));

		date = new Date();
	}

	/**
	 * checks if new item info is correct
	 * @return boolean
	 */
	public boolean checkItem() {
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

	//DatePicker dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			date.setYear(year);
			date.setMonth(monthOfYear);
			date.setDate(dayOfMonth);
			updateDisplay();
		}
	};

	//TimePickerDialog
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			date.setHours(hourOfDay);
			date.setMinutes(minute);
			updateDisplay();
		}
	};
	
	/**
	 * Custom dialog for creating new category
	 * @param int id - id of dialog to be created
	 * @return Dialog - dialog to be created
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
