import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class TreeTextInterface {

	private Person root;
	private Person surnameRoot;
	
	// scanner for user input
	private Scanner input = new Scanner(System.in);

	public TreeTextInterface() {
		this.root = null;
		runTextUI();
	}
	
	public TreeTextInterface(Person root) {
		this.root = root;
		runTextUI();
	}

	// read in first and last name data from csv file.
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

	// overload method. calling with just one argument will add the new person to the tree by first comparing to the root.
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
	
	// remove a person from both trees along with their children, change their name, and add back to both trees.
	public void changeName(Person person, String newName) {
		
		// first find parent node
		Person parent = findParent(person);
		
		// remove person from parent node's children
		if (parent != null) {
			if (parent.getBefore() != null && person == parent.getBefore()) {
				parent.setBefore(null);
			} else if (parent.getAfter() != null && person == parent.getAfter()) {
				parent.setAfter(null);
			} else {
				// something went wrong
				System.out.println("Error!!!! ");
				return;
			}
		}
		
		// repeat above for the surname connections:
		
		// first find surname parent node 
		Person surnameParent = findSurnameParent(person);
		
		// remove person from parent node's children
		if (surnameParent != null) {
			if (surnameParent.getBeforeBySurname() != null && person == surnameParent.getBeforeBySurname()) {
				surnameParent.setBeforeBySurname(null);
			} else if (surnameParent.getAfterBySurname() != null && person == surnameParent.getAfterBySurname()) {
				surnameParent.setAfterBySurname(null);
			} else {
				// something went wrong
				System.out.println("Error!!!! ");
				return;
			}
		}
		
		// now change the name
		person.setName(newName);

		// now remove each of its child nodes from the tree, and then add each one back to both trees. 
		removeAndAddBack(person);
		removeSurnameVerticesAndAddBack(person);
		
	}
	
	public void removeAndAddBack(Person person) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(person);
		while (!queue.isEmpty()) {
			Person current = queue.poll();
			// add the before and after links to the queue
			if (current.getBefore() != null) {
				queue.offer(current.getBefore());
			}
			if (current.getAfter() != null) {
				queue.offer(current.getAfter());
			}
			// now, remove the before and after links
			current.setBefore(null);
			current.setAfter(null);
			
			// now add the person back to the tree
			addPerson(current);
			
		}
	}
	
	public void removeSurnameVerticesAndAddBack(Person person) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(person);
		while (!queue.isEmpty()) {
			Person current = queue.poll();
			// add the before and after links to the queue
			if (current.getBeforeBySurname() != null) {
				queue.offer(current.getBeforeBySurname());
			}
			if (current.getAfterBySurname() != null) {
				queue.offer(current.getAfterBySurname());
			}
			// now, remove the before and after links
			current.setBeforeBySurname(null);
			current.setAfterBySurname(null);

			// now add the person back to the surname tree
			addSurnameLinks(current);
			
		}
	}

	// use queue to run breadth first search from root to find parent node of a given node
	public Person findParent(Person person) {
		// check if root
		if (root == person) {
			return null;
		}
		Queue<Person> queue = new ArrayDeque<Person>();
		// you have to start at the root.
		queue.offer(root);
		while (!queue.isEmpty()) {
			Person current = queue.poll();
			if (current.getBefore() != null) {
				if (current.getBefore().equals(person)) {
					return current;
				} else {
					queue.offer(current.getBefore());
				}
			}
			if (current.getAfter() != null) {
				if (current.getAfter().equals(person)) {
					return current;
				} else {
					queue.offer(current.getAfter());
				}
			}
		}
		return null;
	}
	
	public Person findSurnameParent(Person person) {
		// check if root
		if (surnameRoot == person) {
			return null;
		}
		Queue<Person> queue = new ArrayDeque<Person>();
		// you have to start at the root.
		queue.offer(surnameRoot);
		while (!queue.isEmpty()) {
			Person current = queue.poll();
			if (current.getBeforeBySurname() != null) {
				if (current.getBeforeBySurname().equals(person)) {
					return current;
				} else {
					queue.offer(current.getBeforeBySurname());
				}
			}
			if (current.getAfterBySurname() != null) {
				if (current.getAfterBySurname().equals(person)) {
					return current;
				} else {
					queue.offer(current.getAfterBySurname());
				}
			}
		}
		return null;
	}
	
	
	// create links for the surname-ordered binary tree.
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
				currentP.setBeforeBySurname(newP);
			} else {
				addSurnameLinks(currentP.getBeforeBySurname(), newP);
			}
			
		} else {
			if (currentP.getAfterBySurname() == null) {
				currentP.setAfterBySurname(newP);
			} else {
				addSurnameLinks(currentP.getAfterBySurname(), newP);
			}
		}
	}
	
	// in-order depth-first (sorted)
	public void printAll() {
		System.out.println("- Alphabetically ordered depth-first tree traversal -");
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
		System.out.println("- Alphabetically ordered depth-first surname tree traversal -");
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
		System.out.println("- Pre-ordered depth-first tree traversal -");
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
		System.out.println("- Pre-ordered depth-first surname tree traversal -");
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
		System.out.println("- Post-ordered depth-first tree traversal -");
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
		System.out.println("- Post-ordered depth-first surname tree traversal -");
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
	public void printNamesBreadthFirst() {
		System.out.println("- Breadth-first tree traversal -");
		printNamesBreadthFirst(root);
	}
	
	public void printNamesBreadthFirst(Person root) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			// grab first person in the queue and print their name
			Person person = queue.poll();
			System.out.println(person.getName());
			// add the before and after people to the queue
			if (person.getBefore() != null) {
				queue.offer(person.getBefore());
			}
			if (person.getAfter() != null) {
				queue.offer(person.getAfter());
			}
		}
	}
	
	// breadth-first traversal of surname-linked tree level by level.
	public void printNamesBySurnameBreadthFirst() {
		System.out.println("- Breadth-first surname tree traversal -");
		printNamesBySurnameBreadthFirst(surnameRoot);
	}
	
	public void printNamesBySurnameBreadthFirst(Person root) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			// grab first person in the queue and print their name
			Person person = queue.poll();
			System.out.println(person.getSurnameFirstName());
			// add the before and after people to the queue
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
	public int computeTreeHeight() {
		return computeTreeHeight(root);
	}
	
	public int computeTreeHeight(Person root) {
		// if we have reached a leaf in the last step of the recursion and therefore been sent a null object, return 0.
		if (root == null) {
			return 0;
		} else {
			// compute height of each subtree
			int beforeHeight = computeTreeHeight(root.getBefore()); 
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
	public int computeSurnameTreeHeight() {
		return computeSurnameTreeHeight(surnameRoot);
	}
	
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
	
	// overloaded method. invoke method starting at root.
	public void printNamesLongerThan(int numChar) {
		System.out.println("- First names with at least " + numChar + " characters: -");
		printNamesLongerThan(root, numChar);
	}
	
	public void printNamesLongerThan(Person current, int numChar) {
		if (current.getFirstName().length() >= numChar) {
			System.out.println(current.getFirstName());
		}
		// recursively call this on before and after.
		// do the null check before sending off the recursive call, eh
		if (current.getBefore() != null) {
			printNamesLongerThan(current.getBefore(), numChar);
		}
		if (current.getAfter() != null) {
			printNamesLongerThan(current.getAfter(), numChar);
		}
	}
	
	// overloaded method. invoke method starting at root.
	public void printSurnamesLongerThan(int numChar) {
		System.out.println("- Surnames with at least " + numChar + " characters: -");
		printSurnamesLongerThan(surnameRoot, numChar);
	}
	
	public void printSurnamesLongerThan(Person current, int numChar) {
		if (current.getSurname().length() >= numChar) {
			System.out.println(current.getSurname());
		}
		// recursively call this on before and after.
		// do the null check before sending off the recursive call, eh
		if (current.getBeforeBySurname() != null) {
			printSurnamesLongerThan(current.getBeforeBySurname(), numChar);
		}
		if (current.getAfterBySurname() != null) {
			printSurnamesLongerThan(current.getAfterBySurname(), numChar);
		}
	}
	
	// print people whose first names are longer than their surnames
	public void printNamesLongerThanSurname() {
		System.out.println("- People whose first names are longer than their surnames: -");
		printNamesLongerThanSurname(root);
	}
	
	public void printNamesLongerThanSurname(Person current) {
		// does it apply to current person?
		if (current.getFirstName().length() > current.getSurname().length()) {
			System.out.println(current.getName());
		}
		// check with the before and after nodes
		if (current.getBefore() != null) {
			printNamesLongerThanSurname(current.getBefore());
		}
		if (current.getAfter() != null) {
			printNamesLongerThanSurname(current.getAfter());
		}
	}
	
	// print people whose first names are longer than their surnames
	public void printSurnamesLongerThanFirstName() {
		System.out.println("- People whose surnames are longer than their first names: -");
		printSurnamesLongerThanFirstName(surnameRoot);
	}
	
	public void printSurnamesLongerThanFirstName(Person current) {
		// does it apply to current person?
		if (current.getSurname().length() > current.getFirstName().length()) {
			System.out.println(current.getName());
		}
		// check with the before and after nodes
		if (current.getBeforeBySurname() != null) {
			printSurnamesLongerThanFirstName(current.getBeforeBySurname());
		}
		if (current.getAfterBySurname() != null) {
			printSurnamesLongerThanFirstName(current.getAfterBySurname());
		}
	}
	
	
	public void runTextUI() {
		while (true) {
			System.out.println("Enter (not case sensitive):");
			System.out.println(" L to load a CSV file of names;");
			System.out.println(" P to print all names, ordered by first name;");
			System.out.println(" PS to print all names, ordered by surname;");
			System.out.println(" PRE to print names using a pre-ordered tree traversal;");
			System.out.println(" PRES to print names using a pre-ordered surname tree traversal;");
			System.out.println(" POST to print names using a post-ordered tree traversal;");
			System.out.println(" POSTS to print names using a post-ordered surname tree traversal;");
			System.out.println(" B to print names in level order using a breadth-first tree traversal;");
			System.out.println(" BS to print names in level order using a breadth-first surname tree traversal;");
			System.out.println(" H to compute the height of the tree;");
			System.out.println(" HS to compute the height of the surname tree;");
			System.out.println(" E to print the tree level by level;");
			System.out.println(" ES to print the surname tree level by level;");
			System.out.println(" N to find people with first names longer than a certain number of characters;");
			System.out.println(" NS to find people with surnames longer than a certain number of characters;");
			System.out.println(" D to find people whose first names are longer than their surname;");
			System.out.println(" DS to find people whose surnames are longer than their first names;");
			System.out.println(" S to search for a person by first or full name;");
			System.out.println(" SS to search for a person by surname;");
			System.out.println(" C to change a person's name;");
			System.out.println(" Q to quit.");
			
			String command = input.nextLine();
			
			if (command.equalsIgnoreCase("l")) {
				loadCSVFile();
			} else if (command.equalsIgnoreCase("p")) {
				printAll();
			} else if (command.equalsIgnoreCase("ps")) {
				printAllOrderedBySurname();
			} else if (command.equalsIgnoreCase("pre")) {
				printAllPreOrder();
			} else if (command.equalsIgnoreCase("pres")) {
				printAllPreOrderedBySurname();
			} else if (command.equalsIgnoreCase("post")) {
				printAllPostOrder();
			} else if (command.equalsIgnoreCase("posts")) {
				printAllPostOrderedBySurname();
			} else if (command.equalsIgnoreCase("b")) {
				printNamesBreadthFirst();
			} else if (command.equalsIgnoreCase("bs")) {
				printNamesBySurnameBreadthFirst();
			} else if (command.equalsIgnoreCase("h")) {
				System.out.println("Tree height: " + computeTreeHeight());
			} else if (command.equalsIgnoreCase("hs")) {
				System.out.println("Surname tree height: " + computeSurnameTreeHeight());
			} else if (command.equalsIgnoreCase("e")) {
				printLevels();
			} else if (command.equalsIgnoreCase("es")) {
				printSurnameLevels();
			} else if (command.equalsIgnoreCase("n")) {
				printNamesLongerThan();
			} else if (command.equalsIgnoreCase("ns")) {
				printSurnamesLongerThan();
			} else if (command.equalsIgnoreCase("d")) {
				printNamesLongerThanSurname();
			} else if (command.equalsIgnoreCase("ds")) {
				printSurnamesLongerThanFirstName();
			} else if (command.equalsIgnoreCase("s")) {
				findByName();
			} else if (command.equalsIgnoreCase("ss")) {
				findBySurname();
			} else if (command.equalsIgnoreCase("c")) {
				changeName();
			} else if (command.equalsIgnoreCase("q")) {
				return;
			}
		}
	}
	
	// user prompt methods - mostly overloaded.

	public void loadCSVFile() {
		System.out.println("Enter filename, including .csv extension, and including relative path if not in project folder: ");
		String fileName = input.nextLine();
		addNamesFromCSVFile(fileName);
	}
	
	public void printNamesLongerThan() {
		System.out.println("Enter number of characters: ");
		int number = Integer.parseInt(input.nextLine());
		printNamesLongerThan(number);
	}
	
	public void printSurnamesLongerThan() {
		System.out.println("Enter number of characters: ");
		int number = Integer.parseInt(input.nextLine());
		printSurnamesLongerThan(number);
	}
	
	public void findByName() {
		System.out.println("Enter name to search for: ");
		String search = input.nextLine();
		Person person = findByName(search);
		if (person == null) {
			System.out.println("No one found with name " + search);
		}
	}
	
	public void findBySurname() {
		System.out.println("Enter surname to search for: ");
		String search = input.nextLine();
		Person person = findBySurname(search);
		if (person == null) {
			System.out.println("No one found with surname " + search);
		}
	}
	
	public void changeName() {
		System.out.println("Enter name to search for: ");
		String search = input.nextLine();
		Person person = findByName(search);
		if (person != null) {
			System.out.println("Enter new name: ");
			String newName = input.nextLine();
			String oldName = person.getName();
			changeName(person, newName);
			System.out.println("Name changed from " + oldName + " to " + newName + ".");
			System.out.println("Run print to view new order.");
		} else {
			System.out.println("No one found with name " + search);
		}
	}
	
	public static void main(String[] args) {
		TreeTextInterface tree = new TreeTextInterface();
	}

}
