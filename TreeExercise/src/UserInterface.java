import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserInterface extends Application {

	// application state fields
//	private Person selectedPerson; // for changing name

	// set display sizes
	public static int treeNodeHeight = 20;
	public static int treeNodeWidth = 35;
	private static int width = 1200, height = 600, treePaneWidth = 1000;
	public static int treePaneCenter = (treePaneWidth - treeNodeWidth) / 2; 
	
	// declare tree
	Tree tree;
	
	// declare javaFX fields
	private static Pane treePane;
	private static BorderPane borderPane;
	private static VBox buttons;
	private static ScrollPane mainDisplay;
	
	// buttons
	Button loadCSVButton;
	Button printAllOrderedButton;
	Button printNameTreeButton;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// instantiate tree
		tree = new Tree();

		// for testing purposes pre-load csv
//		tree.addNamesFromCSVFile("mswdev.csv");
		
		// create buttons and set actions
		printNameTreeButton = new Button();
		printNameTreeButton.setText("Print Name Tree");
		printNameTreeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				tree.printAll(treePane);
				
			}
		});
		
		loadCSVButton = new Button();
		loadCSVButton.setText("Load CSV File");
		loadCSVButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				tree.chooseCSVFile(primaryStage);
			}
		});
		
		
		// create panes
		borderPane = new BorderPane();
		buttons = new VBox();
		mainDisplay = new ScrollPane();
		treePane = new Pane();
		
		treePane.setMinWidth(treePaneWidth);
		mainDisplay.setMinHeight(height);
		mainDisplay.setContent(treePane);
		
		// set borderpane elements
		borderPane.setLeft(buttons);
		borderPane.setCenter(mainDisplay);
		
		// add buttons
		buttons.getChildren().add(loadCSVButton);
		buttons.getChildren().add(printNameTreeButton);
		Scene scene = new Scene(borderPane, width, height);
		
		primaryStage.setTitle("Binary Trees");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}

	public static void setHorizontalTreePaneScrollPosition(double position) {
		mainDisplay.setHvalue(position);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
