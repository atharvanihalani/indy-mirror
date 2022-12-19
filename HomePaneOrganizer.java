package indy;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HomePaneOrganizer {
    private AnchorPane root;
    private App myApp;
    private TextField inputWidth;
    private TextField inputHeight;

    public HomePaneOrganizer(App myApp) {
        this.root = new AnchorPane();
        this.myApp = myApp;
        this.inputWidth = new TextField();
        this.inputHeight = new TextField();

        this.setupStartButton();
        this.setupInputBox();
    }

    private void setupBackground() {

    }

    private void setupInputBox() {
        this.inputWidth.setFocusTraversable(false);
        this.inputHeight.setFocusTraversable(false);
        this.inputWidth.setPromptText("6");
        this.inputHeight.setPromptText("4");

        this.root.getChildren().addAll(this.inputWidth, this.inputHeight);
        //ADD INPUT HEIGHT
        this.inputWidth.setLayoutX(80);
        this.inputWidth.setLayoutY(140);
        this.inputHeight.setLayoutX(80);
        this.inputHeight.setLayoutY(180);
    }

    private void setupStartButton() {
        System.out.println("start setup");
        Button startButton = new Button("Start Game");

        startButton.setFocusTraversable(false);
        startButton.setOnAction((ActionEvent e) -> this.startGame());

        this.root.getChildren().add(startButton);
        startButton.setLayoutX(120);
        startButton.setLayoutY(240);
    }

    private boolean checkInput(TextField inputField, boolean isWidth) {
        CharSequence sizeChars = inputField.getCharacters();
        int[] sizeNum = new int[sizeChars.length()];

        for (int i = 0; i < sizeChars.length(); i++) {

            if (!Character.isDigit(sizeChars.charAt(i))) {
                System.out.println("sorry, your maze can't be of size (insert size)" +
                        " – maybe in base 36 (include other error messages for " +
                        "diff input)");
                return false;
            }
            sizeNum[i] = Character.getNumericValue(sizeChars.charAt(i)); //TODO account for leading zeros
        }

        if (sizeNum.length != 1 || sizeNum[0] == 0 || sizeNum[0] == 1) {
            System.out.println("try a digit between 2-9");
            return false;
        }

        if (isWidth) {
            Constants.NUM_COLS = sizeNum[0];
        } else {
            Constants.NUM_ROWS = sizeNum[0];
        }

        return true;
    }

    private void startGame() {
        if (this.checkInput(this.inputWidth, true) &&
                this.checkInput(this.inputHeight, false)) {
            Constants.SCENE_WIDTH = (Constants.NUM_COLS*3 + 2)*Constants.TILE_SIZE;
            Constants.SCENE_HEIGHT  = (Constants.NUM_ROWS*3 + 2)*Constants.TILE_SIZE +
                    Constants.QUIT_PANE_HEIGHT + Constants.TIMER_PANE_HEIGHT;
            Constants.TIMER_COUNT  = Constants.NUM_COLS*Constants.NUM_ROWS*2;

            this.myApp.changeScene();
        }
    }

    public AnchorPane getRoot() {
        return this.root;
    }
}
