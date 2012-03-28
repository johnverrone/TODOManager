package cs2340.todo.model;

/**
 * 
 * @author John
 *
 */
public class User {
	
	private String name;
	private String username;
	private String email;
	private String password;
	
	/**
	 * Empty constructor
	 */
	public User() {
		
	}
	
	/**
	 * Constructor with just username and password
	 * @param username username of new user
	 * @param password password of new user
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Contructor with full user information
	 * @param name name of new user
	 * @param username username of new user
	 * @param email email of new user
	 * @param password password of new user
	 */
	public User(String name, String username, String email, String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	/**
	 * Convert user to a string
	 * @return name + username + password + email
	 */
	public String toString() {
		return "Name: " + name + "\tUsername: " + username + "\tPassword: " + password + "\tEmail Address: " + email + "\n";
	}
	
	/**
	 * Get users name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of user
	 * @param name new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get users username
	 * @return username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Set the users username
	 * @param username neew username
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Get the email address
	 * @return email address of user
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Change the email address
	 * @param email new email address
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Get the password of the user
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Change the password of the user
	 * @param password new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
