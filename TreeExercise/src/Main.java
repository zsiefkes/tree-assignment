
public class Main {

	public static void main(String[] args) {
//		Tree tree = new Tree(new Person("Mike"));
//		tree.testNames(tree);
//		tree.printAll();
		Tree tree = new Tree();
		tree.addNamesFromCSVFile("mswdev.csv");
		tree.printAll();
//		tree.printAllOrderedBySurname();
	}

}
