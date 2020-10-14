import java.io.BufferedReader;
import java.io.FileReader;

public class Tree {

	private Person root;
	private Person surnameRoot;

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
				Person person = new Person(name);
				this.addPerson(person);
				this.addSurnameLinks(person);
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
	// gotta repeat the above for ordering by surname now too! right?
	public void addSurnameLinks(Person newPerson) {
		if (surnameRoot == null) {
			this.surnameRoot = newPerson;
		} else {
			addSurnameLinks(surnameRoot, newPerson);
		}
	}
	public void addSurnameLinks(Person currentP, Person newP) {
		if (newP.getSurname().compareTo(currentP.getSurname()) < 0) {
					
			if (currentP.getBeforeBySurname() == null) {
//				System.out.println("setting " + newP.getSurnameFirstName() + " before " + currentP.getName());
				currentP.setBeforeBySurname(newP);
			} else {
				addSurnameLinks(currentP.getBeforeBySurname(), newP);
			}
			
		} else {
			if (currentP.getAfterBySurname() == null) {
//				System.out.println("setting " + newP.getSurnameFirstName() + " after " + currentP.getName());
				currentP.setAfterBySurname(newP);
			} else {
				addSurnameLinks(currentP.getAfterBySurname(), newP);
			}
		}
	}
	
	// in-order depth-first (sorted)
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
	
	// in-order depth-first (sorted by surname)
	public void printAllOrderedBySurname() {
		printNamesOrderedBySurname(surnameRoot);
	}
	public void printNamesOrderedBySurname(Person current) {
		// check for before person and run recursion on them
		if (current.getBeforeBySurname() != null) {
			printNamesOrderedBySurname(current.getBeforeBySurname());
		}
		// print current person's full name after exhausting before persons recursive calls
		System.out.println(current.getSurnameFirstName());
		// check for after person and run recursion on them
		if (current.getAfterBySurname() != null) {
			printNamesOrderedBySurname(current.getAfterBySurname());
		}
	}
	
	
	// pre-ordered depth-first
	public void printAllPreOrder() {
		printNamesPreOrder(root);
	}
	public void printNamesPreOrder(Person current) {
		System.out.println(current.getName());
		if (current.getBefore() != null) {
			printNamesPreOrder(current.getBefore());
		}
		if (current.getAfter() != null) {
			printNamesPreOrder(current.getAfter());
		}
	}
	
	// pre-ordered depth-first
	public void printAllPreOrderedBySurname() {
		printNamesPreOrderedBySurname(surnameRoot);
	}
	public void printNamesPreOrderedBySurname(Person current) {
		System.out.println(current.getSurnameFirstName());
		if (current.getBeforeBySurname() != null) {
			printNamesPreOrderedBySurname(current.getBeforeBySurname());
		}
		if (current.getAfterBySurname() != null) {
			printNamesPreOrderedBySurname(current.getAfterBySurname());
		}
	}
	
	
	
	// post-ordered depth-first
	public void printAllPostOrder() {
		printNamesPostOrder(root);
	}
	public void printNamesPostOrder(Person current) {
		if (current.getBefore() != null) {
			printNamesPostOrder(current.getBefore());
		}
		if (current.getAfter() != null) {
			printNamesPostOrder(current.getAfter());
		}
		System.out.println(current.getName());
	}

	// post-ordered depth-first
	public void printAllPostOrderedBySurname() {
		printNamesPostOrderedBySurname(surnameRoot);
	}
	public void printNamesPostOrderedBySurname(Person current) {
		if (current.getBeforeBySurname() != null) {
			printNamesPostOrderedBySurname(current.getBeforeBySurname());
		}
		if (current.getAfterBySurname() != null) {
			printNamesPostOrderedBySurname(current.getAfterBySurname());
		}
		System.out.println(current.getSurnameFirstName());
	}
	
	// breadth-first printing
	public void printAllBreadthFirst() {
		printNamesBreadthFirst(root);
	}
	public void printNamesBreadthFirst(Person person) {

	}
	
	public void printAllBySurnameBreadthFirst() {
		printNamesBreadthFirst(surnameRoot);
	}
	public void printNamesBySurnameBreadthFirst(Person person) {
		
	}
	
	// print people at a given level from a starting person == level 1. find given level by recursive calling
	public void printGivenLevel(Person person, int level) {
		if (person == null) {
			return;
		}
		if (level == 1) {
			System.out.println(person.getName());
		} else if (level > 1) {
			// recursion step. run same function on before and after nodes, with 1 subtracted from level
			printGivenLevel(person.getBefore(), level - 1);
			printGivenLevel(person.getAfter(), level - 1);
		}
	}
	
	// compute height of the tree (longest depth traversal) starting from root node
	public int computeTreeHeight(Person root) {
		// if we have reached a leaf in the last step of the recursion and therefore been sent a null object, return 0.
		if (root == null) {
			return 0;
		} else {
			// compute height of each subtree
			int beforeHeight = computeTreeHeight(root.getBefore()); // will it work sending null?? apparently, yeah. I mean we have the uh... catcher for it in the first line. if we didn't have that, the recursive call here throws a null pointer exception.
			int afterHeight = computeTreeHeight(root.getAfter());
			
			// use the larger one
			if (beforeHeight > afterHeight) {
				return beforeHeight + 1;
			} else {
				return afterHeight + 1;
			}
		}
	}

	// compute height of the surname linked tree starting from surname root name
	public int computeSurnameTreeHeight(Person root) {
		if (root == null) {
			return 0;
		} else {
			// compute height of each subtree
			int beforeHeight = computeTreeHeight(root.getBeforeBySurname());
			int afterHeight = computeTreeHeight(root.getAfterBySurname());
			
			// return largest "height"
			if (beforeHeight > afterHeight) {
				return beforeHeight + 1;
			} else {
				return afterHeight + 1;
			}
		}
	}
	
	// we don't want the tree to be skewed, but actually for an A grade assignment we wanna be able to detect skew and also change the tree to lessen it so. might as well just set the first name as the root for now and work on the code to sort the skew later :)
	
	public static void main(String[] args) {
		Tree tree = new Tree();
		tree.addNamesFromCSVFile("mswdev.csv");
		tree.printAll();
		tree.printAllOrderedBySurname();
		tree.printAllPreOrder();
		tree.printAllPreOrderedBySurname();
		tree.printAllPostOrder();
		tree.printAllPostOrderedBySurname();
		System.out.println(tree.computeTreeHeight(tree.root));
		System.out.println(tree.computeSurnameTreeHeight(tree.surnameRoot));
	}

}
