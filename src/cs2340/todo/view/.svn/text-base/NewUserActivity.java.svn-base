package cs2340.todo.view;

import cs2340.todo.model.User;
import cs2340.todo.model.UserManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.Gravity;

/**
 * 
 * @author Vertigo
 *
 */
public class NewUserActivity extends Activity {
	
	private Button btnCreate, btnCancel;
	private EditText txtName, txtUsername, txtPassword, txtPassword2, txtEmail;
	private String name, username, password, password2, email;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user);
		
		btnCreate = (Button) findViewById(R.id.btnCreate);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		txtName = (EditText) findViewById(R.id.txtName);
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword2 = (EditText) findViewById(R.id.txtPassword2);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		
		txtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					UserManager check = new UserManager(NewUserActivity.this);
					check.open();
					for(User u : check.getAllUsers()) {
						if(txtUsername.getText().toString().equals(u.getUsername())) {
							Toast usernameExists = Toast.makeText(NewUserActivity.this, "Username already exists", Toast.LENGTH_SHORT);
							usernameExists.setGravity(Gravity.RIGHT, 0, -120);
							usernameExists.show();
							//Toast.makeText(NewUserActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
						}
					}
					check.close();
				}
			}
		});

		btnCreate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				name = txtName.getText().toString().trim();
				username = txtUsername.getText().toString().trim();
				password = txtPassword.getText().toString().trim();
				password2 = txtPassword2.getText().toString().trim();
				email = txtEmail.getText().toString().trim();
				
				if (checkUser()) {
					User newUser = new User(name, username, email, password);

					UserManager entry = new UserManager(NewUserActivity.this);
					entry.open();
					if (entry.addUser(newUser) == -1) {
						AlertDialog newUserError = new AlertDialog.Builder(NewUserActivity.this).create();
						newUserError.setTitle("Error");
						newUserError.setMessage("There was an error adding the user to the database.");
						newUserError.setButton("Continue", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
				
							}
						});
						newUserError.show();
					}
					entry.close();

					AlertDialog newUserCreated = new AlertDialog.Builder(NewUserActivity.this).create();
					newUserCreated.setTitle("New User Created!");
					newUserCreated.setMessage("Your user has been created. Please continue to login.");
					newUserCreated.setButton("Continue to Login", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Intent login = new Intent(NewUserActivity.this, LoginActivity.class);
							startActivity(login);
							finish();
						}
					});
					newUserCreated.show();

				} else {
					AlertDialog userError = new AlertDialog.Builder(NewUserActivity.this).create();
					userError.setTitle("Error");
					userError.setMessage("Enter valid login information.");
					userError.setButton("Continue", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
			
						}
					});
					userError.show();
				}
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

	}
	
	/**
	 * checks to see if the user info is correct
	 * @return boolean
	 */
	public boolean checkUser() {
		UserManager check = new UserManager(NewUserActivity.this);
		check.open();
		for(User u : check.getAllUsers()) {
			if(username.equals(u.getUsername())) {
				return false;
			}
		}
		check.close();
		if(username.equals("") || username.contains(" ")) {
			return false;
		}
		if(!password.equals(password2)) {
			return false;
		}
		if(name.equals("")) {
			return false;
		}
		if(email.equals("") || !email.contains("@") || !email.contains(".")) {
			return false;
		}
		return true;
	}

}
