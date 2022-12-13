package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.Stack;

public class Pacman {

    private Circle pacCircle;
    private Polygon pacMouth;
    private Pane gamePane;
    private MazeBoard mazeBoard;
    private Direction currentDirection;
    private Direction intendedDirection;
    private Rotate standardRotate;
    private Stack<double[]> backTrackStack;
    public Pacman(Pane gamePane, MazeBoard mazeBoard) {
        this.pacCircle = new Circle(Constants.PAC_RAD, Color.YELLOW);
        this.pacMouth = new Polygon();
        this.gamePane = gamePane;
        this.mazeBoard = mazeBoard;
        this.currentDirection = Direction.RIGHT;
        this.intendedDirection = null;
        this.standardRotate = new Rotate(90, 0, 8);
        this.backTrackStack = new Stack<>();

        this.setupPac();
    }

    private void setupPac() {
        double openMouth = 5.0;
        //TODO make these constants

        this.pacMouth.getPoints().addAll(12.0, 8-openMouth,
                12.0, 8+openMouth,
                0.0, 8.0);
        this.pacMouth.setFill(Color.GREEN);

        this.gamePane.getChildren().addAll(pacCircle, pacMouth);

        this.pacCircle.setCenterX(Constants.TILE_SIZE*2.5); //TODO tweak these
        this.pacCircle.setCenterY(Constants.TILE_SIZE*2.5);
        this.pacMouth.setLayoutX(Constants.TILE_SIZE*2.5);
        this.pacMouth.setLayoutY(Constants.TILE_SIZE*2.5 - 8);
    }


    public void updatePacman() {
        if (this.checkMotionValidity(this.currentDirection)) {
            this.pacMover(this.currentDirection);
            this.checkWallAtTurn();
        }
    }

    /**
     * This method checks whether motion is possible in a given
     * direction. ie. whether the tile ahead/behind/above/below
     * pacman is a wall or not.
     * @param direction the direction for which motion validity
     *                  will be checked
     * @return indicates whether motion is possible or not
     */
    private boolean checkMotionValidity(Direction direction) {
        double[] checkWallA = new double[2];
        double[] checkWallB = new double[2];
        checkWallA[0] = checkWallB[0] = this.pacCircle.getCenterX();
        checkWallA[1] = checkWallB[1] = this.pacCircle.getCenterY();


        /*
        increments coords in the direction of motion in order to get
        the coordinates of a specific tile T. We're then checking if
        T is a wall or not
         */
        switch (direction) {
            case RIGHT:
                checkWallA[0] = checkWallB[0] = checkWallA[0] + Constants.PAC_RAD;
                if (this.currentDirection == Direction.UP) {
                    checkWallA[1] = checkWallA[1] + Constants.PAC_RAD;
                } else if (this.currentDirection == Direction.DOWN) {
                    checkWallA[1] = checkWallA[1] - Constants.PAC_RAD;
                }
                break;
            case LEFT:
                checkWallA[0] = checkWallB[0] = checkWallA[0] - Constants.PAC_RAD;
                if (this.currentDirection == Direction.UP) {
                    checkWallA[1] = checkWallA[1] + Constants.PAC_RAD;
                } else if (this.currentDirection == Direction.DOWN) {
                    checkWallA[1] = checkWallA[1] - Constants.PAC_RAD;
                }
                break;
            case DOWN:
                checkWallA[1] = checkWallB[1] = checkWallA[1] + Constants.PAC_RAD;
                if (this.currentDirection == Direction.RIGHT) {
                    checkWallA[0] = checkWallA[0] - Constants.PAC_RAD;
                } else if (this.currentDirection == Direction.LEFT) {
                    checkWallA[0] = checkWallA[0] + Constants.PAC_RAD;
                }
                break;
            case UP:
                checkWallA[1] = checkWallB[1] = checkWallA[1] - Constants.PAC_RAD;
                if (this.currentDirection == Direction.RIGHT) {
                    checkWallA[0] = checkWallA[0] - Constants.PAC_RAD;
                } else if (this.currentDirection == Direction.LEFT) {
                    checkWallA[0] = checkWallA[0] + Constants.PAC_RAD;
                }
                break;
        }


        int[] arrayIndexWallA = this.checkPosInArray(checkWallA);

        //returns false if the array index is out of bounds
        //ie. paccy doesn't noclip through the border
        if ((arrayIndexWallA[0] >= Constants.NUM_ROWS) || (arrayIndexWallA[0] < 0) ||
                (arrayIndexWallA[1] >= Constants.NUM_COLS) || (arrayIndexWallA[1] < 0)) {
            return false;
        }

        return this.mazeBoard.getIsXWay(arrayIndexWallA[0], arrayIndexWallA[1],
                arrayIndexWallA[2], arrayIndexWallA[3]);
    }

    private void checkWallAtTurn() {
        if (this.intendedDirection != null) {
            System.out.println("checking for move");
            if (this.checkMotionValidity(this.intendedDirection)) {
                this.currentDirection = this.intendedDirection;
                this.intendedDirection = null;
            }
        }
    }


    private void moveLogic(Direction newDirection) {
        if (newDirection == this.currentDirection) {
            this.intendedDirection = null;
        } else if (newDirection.getOpposite() == this.currentDirection) {
            this.currentDirection = newDirection;
            this.intendedDirection = null;
        } else {
            if (this.checkMotionValidity(newDirection)) {
                this.currentDirection = newDirection;
                this.intendedDirection = null;
                System.out.println("intended direction valid");
            } else {
                this.intendedDirection = newDirection;
                System.out.println("intended direction invalid");
            }


            /*
            TODO
                ensure that paccy can only turn perpendicularly when at center of the border
             */
        }
    }

    /**
     * Helper method that takes in a pair of coordinates, and returns the
     * corresponding index in the nested 2D array.
     * @param coords array of size 2 with the coordinates of a certain
     *               point (x, y)
     * @return an integer array of size 4 with the ordered indexes of
     * both 2d arrays. note the y index is calculated first because the
     * 2d arrays are in row-major order
     */
    private int[] checkPosInArray(double[] coords) {

        //external (block) array coordinates
        int[] arraysIndex = new int[4];
        arraysIndex[0] = (int) Math.floor((coords[1] - Constants.TILE_SIZE) /
                (Constants.TILE_SIZE*3));
        arraysIndex[1] = (int) Math.floor((coords[0] - Constants.TILE_SIZE) /
                (Constants.TILE_SIZE*3));

        double tempXVar = (coords[0] - Constants.TILE_SIZE) % (Constants.TILE_SIZE*3);
        double tempYVar = (coords[1] - Constants.TILE_SIZE) % (Constants.TILE_SIZE*3);

        //internal (tile) array coordinates
        arraysIndex[2] = (int) Math.floor(tempYVar / Constants.TILE_SIZE);
        arraysIndex[3] = (int) Math.floor(tempXVar / Constants.TILE_SIZE);

        return arraysIndex;
    }

    public void keyHandler(KeyCode keyCode) {
        switch (keyCode) {
            case RIGHT:
                this.moveLogic(Direction.RIGHT);
                break;
            case LEFT:
                this.moveLogic(Direction.LEFT);
                break;
            case UP:
                this.moveLogic(Direction.UP);
                break;
            case DOWN:
                this.moveLogic(Direction.DOWN);
                break;
            case S:
                this.packBackTrackStack();
                break;
            case SPACE:
                this.backTrackStackSubtract();
                break;
        }
    }

    private void packBackTrackStack() {
        double[] currentIndex = new double[2];
        currentIndex[0] = this.pacCircle.getCenterX();
        currentIndex[1] = this.pacCircle.getCenterY();
        this.backTrackStack.push(currentIndex);
    }

    private void backTrackStackSubtract() {
        if (!this.backTrackStack.empty()) {
            double[] backTrackIndex = this.backTrackStack.pop();
            this.pacCircle.setCenterX(backTrackIndex[0]);
            this.pacMouth.setLayoutX(backTrackIndex[0]);
            this.pacCircle.setCenterY(backTrackIndex[1]);
            this.pacMouth.setLayoutY(backTrackIndex[1] - 8);
        }
    }


    private void pacMover(Direction direction) {
        this.rotatePac(direction);

        switch (direction) {
            case RIGHT:
                this.pacMouth.setLayoutX(this.pacMouth.getLayoutX() +
                        Constants.PAC_DISPLACEMENT);
                this.pacCircle.setCenterX(this.pacCircle.getCenterX() +
                        Constants.PAC_DISPLACEMENT);
                break;
            case LEFT:
                this.pacMouth.setLayoutX(this.pacMouth.getLayoutX() -
                        Constants.PAC_DISPLACEMENT);
                this.pacCircle.setCenterX(this.pacCircle.getCenterX() -
                        Constants.PAC_DISPLACEMENT);
                break;
            case UP:
                this.pacMouth.setLayoutY(this.pacMouth.getLayoutY() -
                        Constants.PAC_DISPLACEMENT);
                this.pacCircle.setCenterY(this.pacCircle.getCenterY() -
                        Constants.PAC_DISPLACEMENT);
                break;
            case DOWN:
                this.pacMouth.setLayoutY(this.pacMouth.getLayoutY() +
                        Constants.PAC_DISPLACEMENT);
                this.pacCircle.setCenterY(this.pacCircle.getCenterY() +
                        Constants.PAC_DISPLACEMENT);
                break;
        }
    }

    private void rotatePac(Direction direction) {
        switch (direction) {
            case RIGHT:
                this.pacMouth.getTransforms().removeAll(this.standardRotate);
                break;
            case LEFT:
                this.pacMouth.getTransforms().removeAll(this.standardRotate);
                this.pacMouth.getTransforms().add(this.standardRotate);
                this.pacMouth.getTransforms().add(this.standardRotate);
                break;
            case UP:
                this.pacMouth.getTransforms().removeAll(this.standardRotate);
                this.pacMouth.getTransforms().add(this.standardRotate);
                this.pacMouth.getTransforms().add(this.standardRotate);
                this.pacMouth.getTransforms().add(this.standardRotate);
                break;
            case DOWN:
                this.pacMouth.getTransforms().removeAll(this.standardRotate);
                this.pacMouth.getTransforms().add(this.standardRotate);
                break;
        }
    }


}
