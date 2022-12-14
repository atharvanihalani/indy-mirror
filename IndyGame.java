package indy;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class IndyGame {

    private Pane gamePane;
    private MazeBoard mazeBoard;
    private Label timerLabel;
    private int timerCount;
    private Timeline gameTimeline;
    private Timeline timerTimeline;

    public IndyGame(Pane gamePane, Label timerLabel) {
        this.gamePane = gamePane;
        this.mazeBoard = new MazeBoard(gamePane);
        this.timerLabel = timerLabel;
        this.timerCount = Constants.TIMER_COUNT;
        this.gameTimeline = new Timeline();
        this.timerTimeline = new Timeline();

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

    private void updateGame() {
        this.mazeBoard.updateBoard();
        this.ifGameOver();
    }

    private void updateTimer() {
        this.timerCount--;
        this.timerLabel.setText("Time Left: " + this.timerCount);
        System.out.println(this.timerCount);

        if (this.timerCount == 0) {
            this.mazeBoard.timeUp();
        }
    }

    private void ifGameOver() {
        if (this.mazeBoard.getGameOver()) {
            this.gameTimeline.stop();
            this.timerTimeline.stop();
        }
    }



}
