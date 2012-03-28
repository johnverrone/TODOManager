package cs2340.todo.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import cs2340.todo.model.TODOItem;
import cs2340.todo.model.TODOManager;
import cs2340.todo.model.User;

/**
 *
 * @author Vertigo (43)
 *
 */
public class TODOManagerActivity extends ListActivity {

	protected static final int CREATE_REQUEST_CODE = 1;

	private User user;
	private TODOManager items;

	private ListAdapter adapter;
	private List<TODOItem> data;
	private int mYear, mMonth, mDay;
	private Date date;

	private EditText filterDate;
	private SearchView filterText;
	private Spinner filterCategory, filterComplete;

	private static final int DATE_DIALOG_ID = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//gets user information from login screen
		Bundle getU = getIntent().getExtras();
		user = new User(getU.getString("name"), getU.getString("username"), getU.getString("email"), getU.getString("password"));   
		setContentView(R.layout.main);

		items = new TODOManager(TODOManagerActivity.this);
		items.open();
		data = items.getUserItems(user);
		items.close();
		adapter = new ListAdapter(this, R.layout.list_item, data);
		setListAdapter(adapter);

		filterDate = (EditText) findViewById(R.id.filterDate);
		filterCategory = (Spinner) findViewById(R.id.spinFilterCategory);
		filterComplete = (Spinner) findViewById(R.id.spinFilterComplete);

		final ListView list = getListView();

		ActionBar bar = getActionBar();
		bar.setDisplayShowTitleEnabled(true);

		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> a, View v, int i, long l) {
				Intent viewItem = new Intent(TODOManagerActivity.this, ViewItemActivity.class);
				Bundle u = new Bundle();
				u.putString("username", user.getUsername());
				u.putString("title", ((TODOItem) list.getItemAtPosition(i)).getTitle());
				u.putString("description", ((TODOItem) list.getItemAtPosition(i)).getDescription());
				u.putString("date", ((TODOItem) list.getItemAtPosition(i)).getDate().toString());
				u.putString("location", ((TODOItem) list.getItemAtPosition(i)).getLocation());
				u.putString("category", ((TODOItem) list.getItemAtPosition(i)).getCategory().toString());
				viewItem.putExtras(u);
				startActivityForResult(viewItem, CREATE_REQUEST_CODE);
			}
		});

		filterDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.filter_categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filterCategory.setAdapter(adapter);
		filterCategory.setOnItemSelectedListener(new MyOnItemSelectedListener());
		
		ArrayAdapter<CharSequence> fadapter = ArrayAdapter.createFromResource(
				this, R.array.completion_array, android.R.layout.simple_spinner_item);
		fadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filterComplete.setAdapter(fadapter);
		filterComplete.setOnItemSelectedListener(new MyOnItemSelectedListener());

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		date = new Date();
		updateDisplay();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_REQUEST_CODE && resultCode == RESULT_OK) {
			items.open();
			this.data = items.getUserItems(user);
			items.close();	
			adapter = new ListAdapter(this, R.layout.list_item, this.data);
			setListAdapter(adapter);
		}
	}

	/**
	 * update the display after opening a dialog
	 */
	private void updateDisplay() {
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/d/yy");
		filterDate.setText(dateFormatter.format(date));
	}

	/**
	 * DatePicker dialog
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			date.setYear(year);
			date.setMonth(monthOfYear);
			date.setDate(dayOfMonth);
			updateDisplay();
			adapter.getFilter().filter(filterText.getQuery());
			adapter.notifyDataSetChanged();
		}
	};

	/**
	 * custom dialog for creating new category
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener, mYear, mMonth, mDay);
		}
		return null;
	}


	/**
	 * Called when the options menu is created
	 * @param menu - the menu to be created
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		filterText = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		filterText.setOnQueryTextListener(new OnQueryTextListener() {

			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				setListAdapter(adapter);
				adapter.getFilter().filter(newText);
				adapter.notifyDataSetChanged();
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				return false;
			}

		});
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.newItem:
			Intent newItem = new Intent(TODOManagerActivity.this, NewItemActivity.class);
			Bundle u = new Bundle();
			u.putString("name", user.getName());
			u.putString("username", user.getUsername());
			u.putString("password", user.getPassword());
			u.putString("email", user.getEmail());
			newItem.putExtras(u);
			startActivityForResult(newItem, CREATE_REQUEST_CODE);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @author Vertigo
	 *
	 */
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			setListAdapter(adapter);
			adapter.getFilter().filter(filterText.getQuery());
			adapter.notifyDataSetChanged();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}


	/**
	 * 
	 * @author Vertigo
	 *
	 */
	private class ListAdapter extends ArrayAdapter<TODOItem> implements Filterable {

		private List<TODOItem> items;
		private List<TODOItem> filteredItems;
		private Filter searchFilter;
		private TODOManager manager;


		public ListAdapter(Context context, int textViewResourceId, List<TODOItem> items) {
			super(context, textViewResourceId, items);
			this.items = new ArrayList<TODOItem>();
			this.items.addAll(items);
			this.filteredItems = new ArrayList<TODOItem>();
			this.filteredItems.addAll(this.items);
			manager = new TODOManager(TODOManagerActivity.this);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item, null);
			}
			final TODOItem o = filteredItems.get(position);
			if (o != null) {
				TextView title = (TextView) v.findViewById(R.id.txtTitle);
				TextView time = (TextView) v.findViewById(R.id.txtTime);
				if (title != null) {
					title.setText(o.getTitle());
				}
				if(time != null){
					SimpleDateFormat format = new SimpleDateFormat("MM/d/yy hh:mm a");
					time.setText("Due: "+ format.format(o.getDate()));
				}
				CheckBox check = (CheckBox) v.findViewById(R.id.check);
				check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {						
						if(isChecked) {
							manager.open();
							o.setComplete(1);
							manager.updateItem(user, o);
							manager.close();

						}else{
							manager.open();
							o.setComplete(0);
							manager.updateItem(user, o);
							manager.close();
						}
					}
				});
				if(o.getComplete()==0) {
					check.setChecked(false);
				}else{
					check.setChecked(true);
				}
			}
			return v;
		}

		@Override
		public Filter getFilter() {
			if (searchFilter == null) {
				searchFilter = new ItemFilter();
			}
			return searchFilter;
		}

		private class ItemFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				constraint = constraint.toString().toLowerCase();
				FilterResults result = new FilterResults();
				ArrayList<TODOItem> filteredItemsArray = new ArrayList<TODOItem>();
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
				Date fDate = new Date();
				try {
					fDate = sdf.parse(filterDate.getText().toString());
					Log.d("date", fDate.toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String category = "all";
				if(!filterCategory.getSelectedItem().toString().equals("All Categories")) {
					category = filterCategory.getSelectedItem().toString();
				}
				int completion = -1;
				if(filterComplete.getSelectedItem().toString().equals("Complete"))
					completion = 1;
				else if (filterComplete.getSelectedItem().toString().equals("Incomplete"))
					completion = 0;
				else
					completion = -1;

				for(int i = 0, l = items.size(); i < l; i++)
				{
					TODOItem item = items.get(i);
					if(item.getTitle().toLowerCase().contains(constraint))
						if(item.getCategory().toString().equals(category) || category.equals("all"))
							if(item.getDate().after(fDate))
								if(item.getComplete() == completion || completion == -1)
									filteredItemsArray.add(item);
				}
				result.count = filteredItemsArray.size();
				result.values = filteredItemsArray;

				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {

				filteredItems = (ArrayList<TODOItem>) results.values;
				notifyDataSetChanged();
				clear();
				for(int i = 0, l = filteredItems.size(); i < l; i++)
					add(filteredItems.get(i));
				notifyDataSetInvalidated();
			}
		}
	}
}