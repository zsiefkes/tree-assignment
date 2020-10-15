
public class Person {
	private String name;
	private String firstName;
	private String surname;
	private String surnameFirstName;
	private Person before;
	private Person after;
	private Person beforeBySurname;
	private Person afterBySurname;
	
	public Person(String name) {
		this.name = name;
		processName(name);
		this.before = null;
		this.after = null;
	}
	
	private void processName(String name) {
		// set surname, if applicable. assumes that surname is the last word in the string.
		if (name.contains(" ")) {
			String[] names = name.split(" ");
			this.surname = names[names.length - 1];
			this.firstName = name.substring(0, name.length() - surname.length() - 1);
			this.surnameFirstName = surname + ", " + firstName;
		} else {
			this.surname = "";
			this.firstName = name;
			this.surnameFirstName = name;
		}
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

	public Person getBeforeBySurname() {
		return beforeBySurname;
	}

	public void setBeforeBySurname(Person beforeBySurname) {
		this.beforeBySurname = beforeBySurname;
	}

	public Person getAfterBySurname() {
		return afterBySurname;
	}

	public void setAfterBySurname(Person afterBySurname) {
		this.afterBySurname = afterBySurname;
	}

	public String getName() {
		return name;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getSurname() {
		return surname;
	}

	public String getSurnameFirstName() {
		return surnameFirstName;
	}
	
	// set new full name and process first and last names. 
	public void setName(String name) {
		this.name = name;
		processName(name);
	}

}
