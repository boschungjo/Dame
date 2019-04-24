package application;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import view.Feld;

public class DameApp extends Application {
	public static final int FELDGROESSE = 100;
	public static final int BREITE = 8;
	public static final int HOEHE = 8;
	
	private Feld[][] brett = new Feld[BREITE][HOEHE]; 
	
	private Group feldGruppe = new Group();
	private Group steinGruppe = new Group();
	
	private Parent erstelleFenster() {
		Pane root = new Pane();
		root.setPrefSize(BREITE * FELDGROESSE, HOEHE * FELDGROESSE);
		root.getChildren().addAll(feldGruppe, steinGruppe);
		
		
		for(int y = 0; y < HOEHE; y++) {
			for(int x = 0; x < BREITE; x++) {
				Feld feld = new Feld();
				brett[x][y] = feld;
				
				feldGruppe.getChildren().add(feld);
			}
		}
	}
	
	
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
