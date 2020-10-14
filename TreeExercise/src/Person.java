
public class Person {
	private String name;
	private Person before;
	private Person after;
	
	public Person(String name) {
		this.name = name;
		this.before = null;
		this.after = null;
	}
	
	public Person(String name, Person before, Person after) {
		this.name = name;
		this.before = before;
		this.after = after;
	}

	// Getters and Setters
	
	public Person getBefore() {
		return before;
	}

	public void setBefore(Person before) {
		this.before = before;
	}

	public Person getAfter() {
		return after;
	}

	public void setAfter(Person after) {
		this.after = after;
	}

	public String getName() {
		return name;
	}

}
