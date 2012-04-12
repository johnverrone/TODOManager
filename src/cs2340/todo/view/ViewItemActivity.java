package cs2340.todo.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cs2340.todo.model.Category;
import cs2340.todo.model.TODOItem;
import cs2340.todo.model.User;

public class ViewItemActivity extends Activity {

	private TextView txtTitle, txtDescription, txtDate, txtTime, txtLocation, txtCategory;
	private User user;
	private TODOItem item;
	

	/**
	 * Called when the activity is created
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_item);
		
		Bundle getU = getIntent().getExtras();
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        user = null;
        item = null;
        try {
			item = new TODOItem(getU.getString("username"), getU.getString("title"), getU.getString("description"), 
					df.parse(getU.getString("date")), getU.getString("location"), new Category(getU.getString("category")));
			user = new User(getU.getString("name"), getU.getString("username"), getU.getString("email"), getU.getString("password"));
		} catch (ParseException e) {
			
		}
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        String itemDate = dateFormat.format(item.getDate());
        String itemTime = timeFormat.format(item.getDate());
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtDescription = (TextView) findViewById(R.id.txtDescription);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtTime = (TextView) findViewById(R.id.txtTime);
		txtLocation = (TextView) findViewById(R.id.txtLocation);
		txtCategory = (TextView) findViewById(R.id.txtCategory);
		
		txtTitle.setText(item.getTitle());
		txtDescription.setText(item.getDescription());
		txtDate.setText(itemDate);
		txtTime.setText(itemTime);
		txtLocation.setText(item.getLocation());
		txtCategory.setText(item.getCategory().toString());
	
		//Category spin adapter
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
		this, R.array.categories_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		txtLocation.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent maps = new Intent(ViewItemActivity.this, MainMapActivity.class);
				maps.putExtra("sLoc", item.getLocation());
				startActivity(maps);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.viewitemmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			setResult(RESULT_OK);
			finish();
			return true;
		case R.id.editItem:
			Intent edit = new Intent(ViewItemActivity.this, EditItemActivity.class);
			Bundle u = new Bundle();
			u.putString("name", user.getName());
			u.putString("username", user.getUsername());
			u.putString("password", user.getPassword());
			u.putString("email", user.getEmail());
			u.putString("title", this.item.getTitle().toString());
			u.putString("description", this.item.getDescription().toString());
			u.putString("date", this.item.getDate().toString());
			u.putString("location", this.item.getLocation().toString());
			u.putString("category", this.item.getCategory().toString());
			edit.putExtras(u);
			startActivity(edit);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
