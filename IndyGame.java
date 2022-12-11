package indy;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class IndyGame {

    private Pane gamePane;
    private MazeBoard mazeBoard;

    public IndyGame(Pane gamePane) {
        this.gamePane = gamePane;
        this.mazeBoard = new MazeBoard(gamePane);

        this.startGame();
    }

    private void startGame() {
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((KeyEvent event) ->
                this.mazeBoard.keyHandler(event.getCode()));

        KeyFrame gameFrame = new KeyFrame(Duration.seconds(1),
                (ActionEvent event) -> {this.updateGame();});
        KeyFrame timerFrame = new KeyFrame(Duration.seconds(1),
                (ActionEvent event) -> {this.updateTimer();});

        Timeline timeline = new Timeline(gameFrame, timerFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateGame() {
        //System.out.println("ur mom a hoe");
    }

    private void updateTimer() {
       //update timer
    }

    /*
    method for timer countdown
     */

    /*
    method for scoretracking????
     */

}
