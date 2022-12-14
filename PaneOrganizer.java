package indy;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Nothing special, just your regular pane organizer.
 * Adds a quit button and a timer to the scene.
 * Has a getRoot() method.
 * Same old, same old.
 */
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

    /**
     * Method to create the bottom bar with the quit button.
     */
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

    /**
     * Method to create the top bar with the timer.
     */
    private void createTimerBar() {
        HBox timerBox = new HBox();

        timerBox.setFocusTraversable(false);
        timerBox.setPrefSize(Constants.SCENE_WIDTH, Constants.TIMER_PANE_HEIGHT);
        timerBox.setStyle("-fx-background-color: #fafafa");
        timerBox.getChildren().add(this.timerLabel);
        timerBox.setAlignment(Pos.CENTER);

        this.root.setTop(timerBox);
    }

    /**
     * Accessor method to get the root node.
     * @return the root Pane
     */
    public Pane getRoot() {
        return this.root;
    }

}

