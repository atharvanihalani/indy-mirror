package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HomePaneOrganizer {
    private AnchorPane root;
    private App myApp;

    public HomePaneOrganizer(App myApp) {
        this.root = new AnchorPane();
        this.myApp = myApp;

        this.setupStartButton();
        this.setupInputBox();
    }

    private void setupBackground() {
        //TODO come up with good graphic (& include rules/info)

    }

    private void setupInputBox() {
        TextField inputWidth = new TextField();
        inputWidth.setFocusTraversable(true);

        this.root.getChildren().add(inputWidth);
        inputWidth.setLayoutX(80);
        inputWidth.setLayoutY(150);
    }

    private void setupStartButton() {
        System.out.println("start setup");
        Button startButton = new Button("Start Game");

        startButton.setFocusTraversable(false);
        startButton.setOnAction((ActionEvent e) -> this.myApp.changeScene());

        this.root.getChildren().add(startButton);
        startButton.setLayoutX(120);
        startButton.setLayoutY(240);
        //TODO reposition
    }

    public AnchorPane getRoot() {
        return this.root;
    }
}
