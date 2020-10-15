// add search by name and last name
// add search for people names with length properties
// ^ note both searches need to be recursively structured!
// make it javafx-y!
// measure skew
// fix skew. phwoar
// add timing shizz

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Queue;

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
			// instantiate name variable to be read
			String name;
			while ((name = csvReader.readLine()) != null) {
				System.out.println("reading: " + name);
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
	
	// breadth-first search to print names - traversing tree level by level. use a queue.
	public void printNamesBreadthFirst(Person root) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			// grab first person in the queue and print their name
			Person person = queue.poll();
			System.out.println(person.getName());
			// add the before and after people to the queue... i really don't get how this works :S how tf this work?
			if (person.getBefore() != null) {
				queue.offer(person.getBefore());
			}
			if (person.getAfter() != null) {
				queue.offer(person.getAfter());
			}
		}
	}
	
	// breadth-first traversal of surname-linked tree level by level. use 
	public void printNamesBySurnameBreadthFirst(Person root) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			// grab first person in the queue and print their name
			Person person = queue.poll();
			System.out.println(person.getSurnameFirstName());
			// add the before and after people to the queue... i really don't get how this works :S how tf this work?
			if (person.getBeforeBySurname() != null) {
				queue.offer(person.getBeforeBySurname());
			}
			if (person.getAfterBySurname() != null) {
				queue.offer(person.getAfterBySurname());
			}
		}
	}
	
	// print people at a given level from a starting person == level 1. find given level by recursive calling
	public void printGivenLevel(Person root, int level) {
		// handle null case
		if (root == null) {
			return;
		}
		if (level == 1) {
			System.out.println(root.getName());
		} else if (level > 1) {
			// recursion step. run same function on before and after nodes, with 1 subtracted from level
			printGivenLevel(root.getBefore(), level - 1);
			printGivenLevel(root.getAfter(), level - 1);
		}
	}
	
	// print people at a given level based on surname tree
	public void printGivenSurnameLevel(Person root, int level) {
		if (root == null) {
			return;
		}
		if (level == 1) {
			System.out.println(root.getSurnameFirstName());
		} else if (level > 1) {
			printGivenSurnameLevel(root.getBeforeBySurname(), level - 1);
			printGivenSurnameLevel(root.getAfterBySurname(), level - 1);
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
			// compute height of each subtree using recursive call
			int beforeHeight = computeSurnameTreeHeight(root.getBeforeBySurname());
			int afterHeight = computeSurnameTreeHeight(root.getAfterBySurname());
			
			// return largest "height" incrementing by 1 each step
			if (beforeHeight > afterHeight) {
				return beforeHeight + 1;
			} else {
				return afterHeight + 1;
			}
		}
	}
	
	// print each successive level of name tree.
	public void printLevels() {
		// compute tree height
		int height = computeTreeHeight(root);
		// root person is considered level 1. iterate through and print levels 1 through height
		for (int i=1; i<height + 1; i++) {
			System.out.println("- Name Tree Level " + i + " -");
			printGivenLevel(root, i);
		}
	}
	
	// print each successive level of surname tree.
	public void printSurnameLevels() {
		int height = computeSurnameTreeHeight(surnameRoot);
		for (int i=1; i<height + 1; i++) {
			System.out.println("- Surname Tree Level " + i + " -");
			printGivenSurnameLevel(surnameRoot, i);
		}
	}
	
	// search for names using depth-first traversal
	
	// overloaded method. start search at the root person.
	public Person findByName(String name) {
		return this.findByName(root, name);
	}
	
	public Person findByName(Person person, String name) {
		if (person == null) { 
			return null;
		}
		if (person.getName().toLowerCase().contains(name.toLowerCase())) {
			System.out.println("found match: " + person.getName());
			return person;
		}
		Person before = findByName(person.getBefore(), name);
		if (before != null) {
			return before;
		}
		Person after = findByName(person.getAfter(), name);
		if (after != null) {
			return after;
		}
		return null;
	}
	
	// overloaded method. start search at the root person.
	public Person findBySurname(String surname) {
		return this.findBySurname(surnameRoot, surname);
	}
	
	public Person findBySurname(Person person, String surname) {
		// ugh...........
		// case-insensitive search for full or partial name? yeah.
		// first do the logic of the traversal for an full case-insensitive match.
		if (person == null) { 
			return null;
		}
		if (person.getSurname().toLowerCase().contains(surname.toLowerCase())) {
			System.out.println("found match: " + person.getSurnameFirstName());
			return person;
		}
		Person before = findBySurname(person.getBeforeBySurname(), surname);
		if (before != null) {
			return before;
		}
		Person after = findBySurname(person.getAfterBySurname(), surname);
		if (after != null) {
			return after;
		}
		return null;
	}
	
	// we don't want the tree to be skewed, but actually for an A grade assignment we wanna be able to detect skew and also change the tree to lessen it so. might as well just set the first name as the root for now and work on the code to sort the skew later :)
	
	public static void main(String[] args) {
		Tree tree = new Tree();
		tree.addNamesFromCSVFile("mswdev.csv");
		tree.printAll();
		tree.printAllOrderedBySurname();
//		tree.printAllPreOrder();
//		tree.printAllPreOrderedBySurname();
//		tree.printAllPostOrder();
//		tree.printAllPostOrderedBySurname();
		System.out.println(tree.computeTreeHeight(tree.root));
		System.out.println(tree.computeSurnameTreeHeight(tree.surnameRoot));
		
		// print successive levels of the trees using composite method breadth first search
		tree.printLevels();
		tree.printSurnameLevels();

		// print successive levels using queue-employing breadth first search
		tree.printNamesBreadthFirst(tree.root);
		tree.printNamesBySurnameBreadthFirst(tree.surnameRoot);
		
		// search for people by first and surnames using depth-first traversal
		tree.findByName("jo");
		tree.findBySurname("ao");
	}

}
