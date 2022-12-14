package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class PaneOrganizer {

    private BorderPane root;
    private Pane gamePane;
    private Label timerLabel;

    public PaneOrganizer() {
        this.gamePane = new Pane();
        this.root = new BorderPane(this.gamePane);
        this.timerLabel = new Label();

        new IndyGame(this.gamePane, this.timerLabel);

        this.createQuitBar();
        this.createTimerBar();
    }

    private void createQuitBar() {
        HBox quitBox = new HBox();
        Button quitButton = new Button("Quit");

        quitButton.setOnAction((ActionEvent e) -> System.exit(0));
        quitButton.setFocusTraversable(false);
        quitBox.setPrefSize(Constants.SCENE_WIDTH, Constants.QUIT_PANE_HEIGHT);
        quitBox.setStyle("-fx-background-color: #DCDCDC");
        quitBox.getChildren().add(quitButton);
        quitBox.setAlignment(Pos.CENTER);

        this.root.setBottom(quitBox);
    }

    private void createTimerBar() {
        HBox timerBox = new HBox();

        timerBox.setFocusTraversable(false);
        timerBox.setPrefSize(Constants.SCENE_WIDTH, Constants.TIMER_PANE_HEIGHT);
        timerBox.setStyle("-fx-background-color: #B8C1FD");
        timerBox.getChildren().add(this.timerLabel);
        timerBox.setAlignment(Pos.CENTER);

        this.root.setTop(timerBox);
    }

    public Pane getRoot() {
        return this.root;
    }

}

