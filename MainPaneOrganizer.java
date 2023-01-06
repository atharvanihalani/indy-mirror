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
public class MainPaneOrganizer {

    private BorderPane root;
    private Pane gamePane;
    private Label timerLabel;
    private IndyGame myIndyGame;
    private App myApp;

    public MainPaneOrganizer(App myApp) {
        this.gamePane = new Pane();
        this.root = new BorderPane(this.gamePane);
        this.timerLabel = new Label();
        this.myIndyGame = new IndyGame(this.gamePane, this.timerLabel);
        this.myApp = myApp;

        this.createQuitBar();
        this.createTimerBar();
    }

    /**
     * Method to create the bottom bar with the quit button.
     */
    private void createQuitBar() {
        HBox quitBox = new HBox();
        Button quitButton = new Button("Quit");
        Button restartButton = new Button("Restart");

        quitButton.setOnAction((ActionEvent e) -> System.exit(0));
        quitButton.setFocusTraversable(false);
        restartButton.setOnAction((ActionEvent e) -> this.onRestart());
        restartButton.setFocusTraversable(false);
        quitBox.setPrefSize(Constants.SCENE_WIDTH, Constants.QUIT_PANE_HEIGHT);
        quitBox.setStyle("-fx-background-color: #DCDCDC");
        quitBox.getChildren().addAll(quitButton, restartButton);
        quitBox.setAlignment(Pos.CENTER);

        this.root.setBottom(quitBox);
    }

    private void onRestart() {
        System.out.println("restarted");
        this.myIndyGame.stopGame();
        this.myApp.loadHomeScreen();
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

