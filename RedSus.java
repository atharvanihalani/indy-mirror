package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RedSus {

    private Pane gamePane;
    private Shape[] imposter;
    private Direction currentDirection;
    private MazeBoard mazeBoard;

    public RedSus(Pane gamePane, MazeBoard mazeBoard) {
        this.gamePane = gamePane;
        this.imposter = new Shape[5];
        this.currentDirection = Direction.LEFT;
        this.mazeBoard = mazeBoard;

        this.constructCrewmate();
    }

    private void constructCrewmate() {
        double tilePosX = (Constants.NUM_COLS)*Constants.TILE_SIZE*3;
        double tilePosY = Constants.NUM_ROWS*Constants.TILE_SIZE*3;
        this.imposter[0] = new Rectangle(16, 8, Color.RED);
        this.imposter[1] = new Rectangle(6, 8, Color.RED);
        this.imposter[2] = new Rectangle(6, 8, Color.RED);
        this.imposter[3] = new Circle(8, Color.RED);
        this.imposter[4] = new Ellipse(6, 4);
        this.imposter[4].setFill(Color.LIGHTBLUE);

        for (Shape bodyPart : this.imposter) {
            bodyPart.setLayoutX(tilePosX);
            bodyPart.setLayoutY(tilePosY);
        }

        this.imposter[0].setLayoutY(this.imposter[0].getLayoutY() - 16);
        this.imposter[1].setLayoutY(this.imposter[1].getLayoutY() - 8);
        this.imposter[2].setLayoutY(this.imposter[2].getLayoutY() - 8);
        this.imposter[2].setLayoutX(this.imposter[2].getLayoutX() + 10);
        this.imposter[3].setLayoutY(this.imposter[2].getLayoutY() - 8);
        this.imposter[3].setLayoutX(this.imposter[3].getLayoutX() + 8);
        this.imposter[4].setLayoutY(this.imposter[4].getLayoutY() - 16);
        this.imposter[4].setLayoutX(this.imposter[4].getLayoutX() + 8);

        this.gamePane.getChildren().addAll(this.imposter);
    }

    public void updateSus() {
        if (this.checkMotionValidity(this.currentDirection)) {
            this.vent(this.currentDirection);
        } else {
            this.currentDirection = this.getRandomDirection();
        }
        //TODO fix motion if got time
    }

    public Direction getRandomDirection() {
        switch ((int) Math.floor(Math.random()*4)) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.DOWN;
            case 2:
                return Direction.RIGHT;
            case 3:
                return Direction.LEFT;
            default:
                throw new IllegalStateException("where tf u goin");
        }
    }

    private boolean checkMotionValidity(Direction direction) {
        double[] checkWall = new double[2];
        checkWall[0] = this.imposter[0].getLayoutX() + 8;
        checkWall[1] = this.imposter[1].getLayoutY() + 4;

        switch (direction) {
            case RIGHT:
                checkWall[0] = checkWall[0] + 15;
                break;
            case LEFT:
                checkWall[0] = checkWall[0] - 15;
                break;
            case UP:
                checkWall[1] = checkWall[1] - 20;
                break;
            case DOWN:
                checkWall[1] = checkWall[1] + 20;
                break;
        }

        int[] arrayIndexWall = this.mazeBoard.checkPosInArray(checkWall);

        if ((arrayIndexWall[0] >= Constants.NUM_ROWS) || (arrayIndexWall[0] < 0) ||
                (arrayIndexWall[1] >= Constants.NUM_COLS) || (arrayIndexWall[1] < 0)) {
            return false;
        }

        return this.mazeBoard.getIsXWay(arrayIndexWall[0], arrayIndexWall[1],
                arrayIndexWall[2], arrayIndexWall[3]);
    }

    public int[] getPosInArray() {
        double[] coords = new double[2];
        coords[0] = this.imposter[0].getLayoutX();
        coords[1] = this.imposter[0].getLayoutY();

        return this.mazeBoard.checkPosInArray(coords);

    }

    private void vent(Direction direction) {
        switch (direction) {
            case RIGHT:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutX(bodyPart.getLayoutX() +
                            Constants.MOVE_SPEED);
                }
                break;
            case LEFT:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutX(bodyPart.getLayoutX() -
                            Constants.MOVE_SPEED);
                }
                break;
            case UP:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutY(bodyPart.getLayoutY() -
                            Constants.MOVE_SPEED);
                }
                break;
            case DOWN:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutY(bodyPart.getLayoutY() +
                            Constants.MOVE_SPEED);
                }
                break;
        }
    }

}
