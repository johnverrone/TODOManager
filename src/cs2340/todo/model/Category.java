package cs2340.todo.model;

/**
 * 
 * @author Vertigo
 *
 */
public class Category {
	private String name;
	
	/**
	 * constructor
	 * @param n name of category
	 */
	public Category(String n) {
		this.name = n;
	}

	/**
	 * returns the name of the category
	 * @return name
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
