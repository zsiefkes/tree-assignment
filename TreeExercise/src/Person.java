
public class Person {
	private String name;
	private Person before;
	private Person after;
	
	public Person(String name, Person before, Person after) {
		this.name = name;
		this.before = before;
		this.after = after;
	}
	
}
