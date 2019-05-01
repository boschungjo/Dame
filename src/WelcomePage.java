import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
public class WelcomePage extends Application {
 
    @Override
    public void start(Stage theStage) throws Exception {
         
        final Image titleScreen = new Image("checkers1.png", 700, 450, false, false);   
 
        final ImageView icon = new ImageView();
        icon.setImage(titleScreen);
         
        theStage.setTitle( "D A M E S P I E L" );
        theStage.getIcons().add(titleScreen);
 
        final double CANVAS_WIDTH = 700;
        final double CANVAS_HEIGHT = 550;
        
        Label welcome = new Label("DAMESPIEL");
        
        
        Button startBtn = new Button("Spiel starten");
        startBtn.setTranslateX(CANVAS_WIDTH/2.25);
        startBtn.setTranslateY(450);
        startBtn.setMinSize(100, 30);
        
        Button exitBtn = new Button("Spiel verlassen");
        exitBtn.setTranslateX(CANVAS_WIDTH/2.25);
        exitBtn.setTranslateY(490);
        exitBtn.setMinSize(100, 30);

 
        Group root = new Group();
        root.getChildren().addAll(icon, welcome, startBtn, exitBtn); 
         
        Scene theScene = new Scene( root, CANVAS_WIDTH, CANVAS_HEIGHT, Color.BLACK );
        theStage.setScene( theScene );
        theStage.show(); 
 
    } 
 
    public static void main(String[] args) {
        launch(args);
    }
 
}