package indy;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Top level logic class. It mainly handles everything to do
 * with the timeline. This includes starting and updating the
 * game, updating the timer, and checking if the game is over.
 */
public class IndyGame {

    private Pane gamePane;
    private Label timerLabel;
    private int timerCount;
    private MazeBoard mazeBoard;
    private Timeline gameTimeline;
    private Timeline timerTimeline;

    public IndyGame(Pane gamePane, Label timerLabel) {
        this.gamePane = gamePane;
        this.timerLabel = timerLabel;
        this.timerCount = Constants.TIMER_COUNT;
        this.mazeBoard = new MazeBoard(gamePane);
        this.gameTimeline = new Timeline();
        this.timerTimeline = new Timeline();

        this.startGame();
    }


    /**
     * Method called to set up the timelines, configure key input
     * and start the game.
     * note: I have two timelines because both keyframes are repeated
     * independently at different intervals
     */
    private void startGame() {
        this.gamePane.setFocusTraversable(true);
        this.gamePane.setOnKeyPressed((KeyEvent event) ->
                this.mazeBoard.keyHandler(event.getCode()));

        KeyFrame gameFrame = new KeyFrame(Constants.UPDATE_GAME_EVERY,
                (ActionEvent event) -> this.updateGame());
        this.gameTimeline.getKeyFrames().add(gameFrame);
        this.gameTimeline.setCycleCount(Animation.INDEFINITE);
        this.gameTimeline.play();
        
        KeyFrame timerFrame = new KeyFrame(Duration.seconds(1),
                (ActionEvent event) -> this.updateTimer());
        this.timerTimeline.getKeyFrames().add(timerFrame);
        this.timerTimeline.setCycleCount(Animation.INDEFINITE);
        this.timerTimeline.play();
    }

    /**
     * Top level method that updates the entire game. It calls the
     * helper method that constantly checks if the game is over. Most
     * of the remaining functionality is handled by the mazeBoard.
     */
    private void updateGame() {
        this.mazeBoard.updateBoard();
        this.ifGameOver();
    }

    /**
     * Method that updates the timer count every second. Ends the game
     * (with a helper) if time is up.
     */
    private void updateTimer() {
        this.timerCount--;
        this.timerLabel.setText("Time Left: " + this.timerCount);

        if (this.timerCount == 0) {
            this.mazeBoard.timeUp();
        }
    }

    /**
     * Helper method that stops both timelines when the game is over.
     */
    private void ifGameOver() {
        if (this.mazeBoard.getGameOver()) {
            this.gameTimeline.stop();
            this.timerTimeline.stop();
        }
    }

}
