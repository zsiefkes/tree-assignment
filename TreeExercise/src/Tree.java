
public class Tree {

	private Person root;

	public Tree() {
		this.root = null;
	}
	
	public Tree(Person root) {
		this.root = root;
	}

	// overload method - calling with just one argument calls the method that tries to add the new person compared to the root.
	public void addPerson(Person newPerson) {
		if (root == null) {
			this.root = newPerson;
		} else {
			addPerson(root, newPerson);
		}
	}
	
	// add person to Tree tree structure in alphabetical order using recursion.
	public void addPerson(Person currentP, Person newP) {
		
		// if new person's name alphabetically precedes current person's name,
		if (newP.getName().compareTo(currentP.getName()) < 0) {
			
			// check for an existing "before" person
			if (currentP.getBefore() == null) {
				System.out.println("setting " + newP.getName() + " before " + currentP.getName());
				currentP.setBefore(newP);
//				newP.setAfter(currentP); // is this necessary? will doing this cause this recursive structure to just go back and forth between the two names where the new person's name fits? I feel like it will, and obviously we don't want that.....
				
			} else {
				// send off to be compared with current person's "before" person
				addPerson(currentP.getBefore(), newP);
			}
			
//		} else if (newP.getName().compareTo(currentP.getName()) > 0) { // for some reason adding all the names doesn't work if we run it with this else if. works using an else.
		} else {
			if (currentP.getAfter() == null) {
				System.out.println("setting " + newP.getName() + " after " + currentP.getName());
				currentP.setAfter(newP);
			} else {
				addPerson(currentP.getAfter(), newP);
			}
		}
	}

	public void printAll() {
		printNames(root);
	}
	public void printNames(Person current) {
		// check for before person and run recursion on them
		if (current.getBefore() != null) {
			printNames(current.getBefore());
		}
		// print current person's name after exhausting before persons recursive calls
		System.out.println(current.getName());
		// check for after person and run recursion on them
		if (current.getAfter() != null) {
			printNames(current.getAfter());
		}
	}
	
	public static void main(String[] args) {
		Tree tree = new Tree(new Person("Mike"));
		tree.addPerson(new Person("Helen"));
		tree.addPerson(new Person("Helen"));
		tree.addPerson(new Person("Phoebe"));
		tree.addPerson(new Person("Zachary"));
		tree.addPerson(new Person("Abel"));
		tree.addPerson(new Person("Simon"));
		tree.addPerson(new Person("Paula"));
		tree.addPerson(new Person("Frankie"));
		tree.addPerson(new Person("Wilhemina"));
		tree.printAll();
	}

}
