import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Queue;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Tree {

	private Person root;
	private Person surnameRoot;
	
	// for printing tree nodes
	private int treeHeight;
	private int nodeWidth;
	private int treePaneWidth;

	public Tree() {
		this.root = null;
	}
	
	public Tree(Person root) {
		this.root = root;
	}

	// read in first and last name data from csv file.
	public void addNamesFromCSVFile(String fileName) {
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
			// instantiate name variable to be read
			String name;
			while ((name = csvReader.readLine()) != null) {
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
	
	// want to be able to remove a person from both trees, for the sake of changing their names. yeah?
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
		
		// repeat above for the surname connections
		
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

		// now we need to remove each of its child nodes from the trees, and then add each one back to both trees.
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
	public void printAll(Pane display) {
		// okay, we're going to cheat a bit or at least make this less efficient by first figuring out the height of the tree.
		treeHeight = computeTreeHeight();
		nodeWidth = UserInterface.treeNodeWidth;
		
		// now the width of the treepane should be
		treePaneWidth = (int)(Math.pow(2, treeHeight)) * nodeWidth;
		UserInterface.setHorizontalTreePaneScrollPosition((double)(treePaneWidth / 2));
		
		// and then so the x position of each node should be treePaneWidth / Math.pow(2, depth) - nodeWidth / 2;
		printNames(root, display, 1, 1);
	}
	
	public void printNames(Person current, Pane display, int depth, int numAcross) {
		
		if (current.getBefore() != null) {
//			System.out.println(current.getName() + ", before: " + current.getBefore().getName());
			// for before node, set numAcross as current numAcross * 2 - 1
			printNames(current.getBefore(), display, depth + 1, numAcross * 2 - 1);				
		}
		
		// display current name
		Text text = new Text();
		text.setText(current.getName());
		
		// set horizontal and vertical placement and add to display
		double x = ((treePaneWidth / Math.pow(2, depth)) * numAcross) - nodeWidth / 2; 
		text.setX(x);
		text.setY(depth * UserInterface.treeNodeHeight);			
		display.getChildren().add(text);
		
		// check for after person and run recursion on them
		if (current.getAfter() != null) {
//			System.out.println(current.getName() + ", after: " + current.getAfter().getName());
			// for before node, set numAcross as current numAcross * 2 
			printNames(current.getAfter(), display, depth + 1, numAcross * 2);				
 		}
	}
	
	// in-order depth-first (sorted by )
	public void printAllOrdered(ScrollPane display) {
		printAllOrdered(surnameRoot);
	}
	public void printAllOrdered(Person current) {
		// check for before person and run recursion on them
		if (current.getBeforeBySurname() != null) {
			printAllOrdered(current.getBefore());
		}
		// print current person's full name after exhausting before persons recursive calls
		System.out.println(current.getSurnameFirstName());
		// display current name
		Text text = new Text();
		text.setText(current.getName());
		text.setX(x);
		text.setY(depth * UserInterface.treeNodeHeight);			
		display.getChildren().add(text);
		
		// check for after person and run recursion on them
		if (current.getAfterBySurname() != null) {
			printAllOrdered(current.getAfter());
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
	public void printNamesBreadthFirst() {
		printNamesBreadthFirst(root);
	}
	
	public void printNamesBreadthFirst(Person root) {
		Queue<Person> queue = new ArrayDeque<Person>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			// grab first person in the queue and print their name
			Person person = queue.poll();
			System.out.println(person.getName());
			// add the before and after people to the queue. Ahhh I get it now :) lol so simple! so neat
			if (person.getBefore() != null) {
				queue.offer(person.getBefore());
			}
			if (person.getAfter() != null) {
				queue.offer(person.getAfter());
			}
		}
	}
	
	// breadth-first traversal of surname-linked tree level by level. use
	public void printNamesBySurnameBreadthFirst() {
		printNamesBySurnameBreadthFirst(surnameRoot);
	}
	
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
		// check if the current one counts as longer than!
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
		// check if the current one counts as longer than!
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
	
	public void chooseCSVFile(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(stage);
		addNamesFromCSVFile(selectedFile.getPath());
	}
	
}
