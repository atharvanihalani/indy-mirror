package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PaneOrganizer {

    private BorderPane root;


    public PaneOrganizer() {
        this.root = new BorderPane();
        new IndyGame();

        this.createQuitButton();
    }

    private void createQuitButton() {
        HBox quitPane = new HBox();
        Button quitButton = new Button("Quit");

        quitButton.setOnAction((ActionEvent e) -> System.exit(0));
        quitButton.setFocusTraversable(false);
        quitPane.setPrefSize(Constants.SCENE_WIDTH, 35);
        quitPane.setStyle("-fx-background-color: #DCDCDC");
        quitPane.getChildren().add(quitButton);
        quitPane.setAlignment(Pos.CENTER);

        this.root.setBottom(quitPane);
    }

    public Pane getRoot() {
        return this.root;
    }

}

