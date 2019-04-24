package application;
	
import javax.swing.LayoutFocusTraversalPolicy;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class DameApp extends Application {
	private Stage fenster;
	private Scene scene;
	//private Button[] whiteButtons = new Button[32];
	//private Button[] blackButtons = new Button[32];
	private Button field;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//GridPane erzeugen
			GridPane layout = new GridPane();
			this.scene = new Scene(layout,800,800);
			
			//Felder in die GridPlane hinzufügen
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					field = new Button();
					field.setPrefSize(100, 100);
					layout.add(field, i, j);
					if ((i % 2 != 0 && j % 2 == 0) || (i % 2 == 0 && j % 2 != 0) ) {
						field.setStyle("-fx-background-color: #994C00; ");
					}
					else {
						field.setStyle("-fx-background-color: #F5DEB3; ");
					}
				}
			}			
			
			
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Dame");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
