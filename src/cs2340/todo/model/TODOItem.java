package cs2340.todo.model;

import java.util.Date;

/**
 * 
 * @author Vertigo
 *
 */
public class TODOItem {
	private String user;
	private String title;
	private String description;
	private Date date;
	private String location;
	private Category category;
	private int complete;
	private int id;

	/**
	 * Build new Item
	 * @param u
	 * @param t
	 * @param d
	 * @param time
	 * @param l
	 * @param c
	 */
	public TODOItem(String u, String t, String d, Date date, String l, Category c) {
		this.setUser(u);
		this.setTitle(t);
		this.setDescription(d);
		this.setDate(date);
		this.setLocation(l);
		this.setCategory(c);
		setComplete(0);
	}
	
	public TODOItem(String u, String t, String d, Date date, String l, Category c, int complete) {
		this.setUser(u);
		this.setTitle(t);
		this.setDescription(d);
		this.setDate(date);
		this.setLocation(l);
		this.setCategory(c);
		setComplete(complete);
	}

	/**
	 * getTitle
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * change the title
	 * @param title new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * getDescription
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * change the description
	 * @param description new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}


	/**
	 * getLocation
	 * @return String - Location
	 */
	public String getLocation() {
		return location;
	}
	

	/**
	 * change the Location
	 * @param location - new Location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * getCategory
	 * @return category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * change the category
	 * @param category new category
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/**
	 * get user affiliated with item
	 * @return user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * change the user
	 * @param u new user
	 */
	public void setUser(String u) {
		this.user = u;
	}
	
	@Override
	public String toString() {
		return id + " " + title + " " + description + " " + date + " " + category.toString() + " " + complete;
	}

	/**
	 * get the date
	 * @return Date - Date and Time of event
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set the date.
	 * @param date - new date of event
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * See if the item has been completed
	 * @return int - 0 if not, 1 if completed
	 */
	public int getComplete() {
		return complete;
	}

	/**
	 * Set the completeness of the item
	 * @param complete - 0 or 1
	 */
	public void setComplete(int complete) {
		this.complete = complete;
	}

	/**
	 * get the id of the task
	 * @return int - id
	 */
	public int getId() {
		return id;
	}

	/**
	 * set the id of the task
	 * @param id - id from database
	 */
	public void setId(int id) {
		this.id = id;
	}
}
