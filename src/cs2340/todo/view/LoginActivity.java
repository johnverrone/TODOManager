package cs2340.todo.view;

import java.util.List;

import cs2340.todo.model.User;
import cs2340.todo.model.UserManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author Vertigo (43)
 * 
 */
public class LoginActivity extends Activity {

	private Button btnLogin, btnNewUser;
	private TextView txtUser, txtPassword;
	private User goodLoginUser;

	/** Called when Login Activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState) {
			
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
//		Remove comment and run program to reset the database
//		UserManager entry = new UserManager(LoginActivity.this);
//		entry.open();
//		entry.reset();
//		entry.close();
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnNewUser = (Button) findViewById(R.id.btnNewUser);
		txtUser = (TextView) findViewById(R.id.txtUser);
		txtPassword = (TextView) findViewById(R.id.txtPassword);
		
		//Set onClickListeners
		btnLogin.setOnClickListener(new View.OnClickListener() {

			/**
			 * Called when user clicks on btnLogin
			 * Checks login, displays error or continues to task manager screen.
			 * @param View
			 */
			public void onClick(View v) {
				UserManager debugList = new UserManager(LoginActivity.this);
				debugList.open();
				for (User u : debugList.getAllUsers()) {
					Log.d("users", u.toString());
				}
				debugList.close();
				if (checkLogin()) {
					Intent proceed = new Intent(LoginActivity.this, TODOManagerActivity.class);
					Bundle u = new Bundle();
					u.putString("name", goodLoginUser.getName());
					u.putString("username", goodLoginUser.getUsername());
					u.putString("password", goodLoginUser.getPassword());
					u.putString("email", goodLoginUser.getEmail());
					proceed.putExtras(u);
					startActivity(proceed);
					finish();
				} else {
					AlertDialog loginError = new AlertDialog.Builder(LoginActivity.this).create();
					loginError.setTitle("Login Error");
					loginError.setMessage("The username and/or password are incorrect or this user does not exist!");
					loginError.setButton("Okay", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							txtPassword.setText("");
						}
					});
					loginError.show();
				}
			}
		});
		
		btnNewUser.setOnClickListener(new View.OnClickListener() {

			/**
			 * Called when user clicks on New User button
			 * Create a new user
			 * @param View
			 */
			public void onClick(View v) {
				Intent newUser = new Intent(LoginActivity.this, NewUserActivity.class);
				startActivity(newUser);
			}
		});

	}

	/**
	 * Check for valid login
	 * 
	 * @return boolean true if successful login, false otherwise.
	 */
	protected boolean checkLogin() {
		String username = txtUser.getText().toString().trim();
		String password = txtPassword.getText().toString().trim();
		UserManager get = new UserManager(LoginActivity.this);
		get.open();
		List<User> userArray = get.getAllUsers();
		get.close();
		for (User u : userArray) {
			if (u.getUsername().equals(username)) {
				if (u.getPassword().equals(password)) {
					goodLoginUser = u;
					return true;
				}
			}
		}
		return false;
	}
}
