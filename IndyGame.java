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


    /**
     * Method called to set up the timelines, configure key input
     * and start the game.
     * note: I have two timelines because both keyframes are repeated
     * at different intervals
     */
    private void startGame() {
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((KeyEvent event) ->
                this.mazeBoard.keyHandler(event.getCode()));

        KeyFrame gameFrame = new KeyFrame(Duration.millis(1),
                (ActionEvent event) -> this.updateGame());
        Timeline gameTimeline = new Timeline(gameFrame);
        gameTimeline.setCycleCount(Animation.INDEFINITE);
        gameTimeline.play();
        
        KeyFrame timerFrame = new KeyFrame(Duration.seconds(1),
                (ActionEvent event) -> this.updateTimer());
        Timeline timerTimeline = new Timeline(timerFrame);
        timerTimeline.setCycleCount(Animation.INDEFINITE);
        timerTimeline.play();
    }

    private void updateGame() {
        this.mazeBoard.updateBoard();
    }

    private void updateTimer() {
       //update timer
    }

    /*
    method for timer countdown
     */



}
