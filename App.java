package indy;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;


/**
 * It's time for Indy! This is the main class to get things started.
 *
 * Class comments here...
 *
 */

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        PaneOrganizer myPaneOrg = new PaneOrganizer();
        Scene myScene = new Scene(myPaneOrg.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        stage.setScene(myScene);
        stage.setTitle("ur mom");

        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // launch is a method inherited from Application
    }
}
