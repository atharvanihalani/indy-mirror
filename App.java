package indy;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;


/**
 * It's time for Indy! This is the main class to get things started.
 *
 * Class comments here...
 *
 * ...move along, nothing to see here
 */

public class App extends Application {

    private Stage myStage;
    @Override
    public void start(Stage stage) throws Exception {
        this.myStage = stage;

        HomePaneOrganizer myPaneOrg = new HomePaneOrganizer(this);
        Scene homeScene = new Scene(myPaneOrg.getRoot(), 300, 300);
        this.myStage.setScene(homeScene);
        this.myStage.setTitle("home scene");

        stage.show();
    }

    public void changeScene() {
        MainPaneOrganizer myPaneOrg = new MainPaneOrganizer(this);
        Scene myScene = new Scene(myPaneOrg.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        this.myStage.setScene(myScene);
        this.myStage.setTitle("ur mom");
    }

    public static void main(String[] args) {
        launch(args); // launch is a method inherited from Application
    }
}
