package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Blinky but sus. Pretty much all the ghost can do is move
 * about randomly in the maze. Oh, and kill pacman at will.
 * It's also associated with MazeBoard – to calculate its
 * position in the array.
 */
public class RedSus {

    private Pane gamePane;
    private Shape[] imposter;
    private Direction currentDirection;
    private MazeBoard mazeBoard;

    /**
     * constructor instantiates instance variables and graphically
     * sets up blinky
     * @param gamePane
     * @param mazeBoard
     */
    public RedSus(Pane gamePane, MazeBoard mazeBoard) {
        this.gamePane = gamePane;
        this.imposter = new Shape[5];
        this.currentDirection = Direction.LEFT;
        this.mazeBoard = mazeBoard;

        this.constructCrewmate();
    }

    /**
     * graphically sets up and positions beta Blinky
     * first moves all body parts to the same destination, then
     * adjuests their relative position.
     */
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

    public void makeVisible(boolean isVisible) {
        if (isVisible) {
            for (Shape bodyPart : this.imposter) {
                bodyPart.setFill(Color.RED);
            }
            this.imposter[4].setFill(Color.LIGHTBLUE);
        } else {
            for (Shape bodyPart : this.imposter) {
                bodyPart.setFill(Color.BLACK);
            }
        }
    }


    public void updateSus() {
        if (this.checkMotionValidity(this.currentDirection)) {
            this.vent(this.currentDirection);
        } else {
            this.currentDirection = this.getRandomDirection();
        }
    }

    /**
     * returns a random direction
     * @return random direction of motion
     */
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

    /**
     * Helper method that returns blinky's position both 2-d arrays
     * @return an array of 4 integers. first two being coordinates of
     * the block array; the latter two being the coordinates of the
     * tile array.
     */
    public int[] getPosInArray() {
        double[] coords = new double[2];
        coords[0] = this.imposter[0].getLayoutX();
        coords[1] = this.imposter[0].getLayoutY();

        return this.mazeBoard.checkPosInArray(coords);
    }

    public double[] getPos() {
        return new double[]{this.imposter[0].getLayoutX(), this.imposter[0].getLayoutY()};
    }

    /**
     * exactly the same as pacman's movePac() method
     * simply moves blinky in a particular direction by a smidgen.
     * @param direction
     */
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
