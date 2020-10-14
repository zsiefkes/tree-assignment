import java.io.BufferedReader;
import java.io.FileReader;

public class Tree {

	private Person root;

	public Tree() {
		this.root = null;
	}
	
	public Tree(Person root) {
		this.root = root;
	}

	// read in first and last name data from csv file. learned from https://stackabuse.com/reading-and-writing-csvs-in-java/ 
	public void addNamesFromCSVFile(String fileName) {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
			while (csvReader.readLine() != null) {
				String name = csvReader.readLine();
				// add person to existing tree by name and surname
				this.addPerson(new Person(name));
//				this.addPersonToSurnameTree(new Person(name));
			}
			csvReader.close();
			
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
	}

	// overload method - calling with just one argument calls the method that tries to add the new person compared to the root.
	public void addPerson(Person newPerson) {
		if (root == null) {
			this.root = newPerson;
		} else {
			addPerson(root, newPerson);
		}
	}
	
	public void addPerson(Person currentP, Person newP) {
		if (newP.getName().compareTo(currentP.getName()) < 0) {
			if (currentP.getBefore() == null) {
				currentP.setBefore(newP);
				// yeah nah, it would be INCORRECT to set the after person on the new person to the current person. This is not a doubly linked list!!! It's a binary tree!! 
			} else {
				addPerson(currentP.getBefore(), newP);
			}
			
		} else {
			if (currentP.getAfter() == null) {
				currentP.setAfter(newP);
			} else {
				addPerson(currentP.getAfter(), newP);
			}
		}

		
	}
//	// gotta repeat the above for ordering by surname now too! right?
//	public void addPersonToSurnameTree(Person newPerson) {
//		if (root == null) {
//			this.root = newPerson;
//		} else {
//			addPerson(root, newPerson);
//		}
//	}
//	public void addPersonToSurnameTree(Person currentP, Person newP) {
//		if (newP.getSurname().compareTo(currentP.getSurname()) < 0) {
//					
//			if (currentP.getBeforeBySurname() == null) {
////				System.out.println("setting " + newP.getName() + " before " + currentP.getName());
//				currentP.setBeforeBySurname(newP);
//			} else {
//				addPerson(currentP.getBeforeBySurname(), newP);
//			}
//			
//		} else {
//			if (currentP.getAfterBySurname() == null) {
////				System.out.println("setting " + newP.getName() + " after " + currentP.getName());
//				currentP.setAfterBySurname(newP);
//			} else {
//				addPerson(currentP.getAfterBySurname(), newP);
//			}
//		}
//	}
	
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
//
//	public void printAllOrderedBySurname() {
//		printNamesOrderedBySurname(root);
//	}
//	public void printNamesOrderedBySurname(Person current) {
//		// check for before person and run recursion on them
//		if (current.getBeforeBySurname() != null) {
//			printNamesOrderedBySurname(current.getBeforeBySurname());
//		}
//		// print current person's full name after exhausting before persons recursive calls
//		System.out.println(current.getName());
//		// check for after person and run recursion on them
//		if (current.getAfterBySurname() != null) {
//			printNamesOrderedBySurname(current.getAfterBySurname());
//		}
//	}
	
	
	// we don't want the tree to be skewed, but actually for an A grade assignment we wanna be able to detect skew and also change the tree to lessen it so. might as well just set the first name as the root for now and work on the code to sort the skew later :)
	
	public void testNames(Tree tree) {
		tree.addPerson(new Person("Helen"));
		tree.addPerson(new Person("Helen"));
		tree.addPerson(new Person("Phoebe"));
		tree.addPerson(new Person("Zachary"));
		tree.addPerson(new Person("Abel"));
		tree.addPerson(new Person("Simon"));
		tree.addPerson(new Person("Paula"));
		tree.addPerson(new Person("Frankie"));
		tree.addPerson(new Person("Wilhemina"));
	}
	
}
