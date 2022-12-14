package indy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        this.pacCircle = new Circle(Constants.PAC_RADIUS, Color.YELLOW);
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
        this.pacMouth.setFill(Color.BLACK);

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
        this.checkPacDub();
    }

    private void checkPacDub() {
        int[] pacPosInArray;
        double[] pacPos = new double[2];
        pacPos[0] = this.pacCircle.getCenterX() - Constants.PAC_RADIUS;
        pacPos[1] = this.pacCircle.getCenterY();
        pacPosInArray = this.mazeBoard.checkPosInArray(pacPos);

        if ((pacPosInArray[0] == Constants.NUM_ROWS - 1) &&
                (pacPosInArray[1] == Constants.NUM_COLS - 1) &&
                (pacPosInArray[2] == 1) && (pacPosInArray[3] == 2)) {
            this.mazeBoard.pacDub();
        }
    }


    public int[] getPosInArray() {
        double[] coords = new double[2];
        coords[0] = this.pacCircle.getCenterX();
        coords[1] = this.pacCircle.getCenterY();

        return this.mazeBoard.checkPosInArray(coords);
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
        double[] checkWall = new double[2];
        checkWall[0] = this.pacCircle.getCenterX();
        checkWall[1] = this.pacCircle.getCenterY();

        /*
        increments coords in the direction of motion in order to get
        the coordinates of a specific tile T. We're then checking if
        T is a wall or not
         */
        switch (direction) {
            case RIGHT:
                checkWall[0] = checkWall[0] + Constants.PAC_RADIUS;
                if (this.currentDirection == Direction.UP) {
                    checkWall[1] = checkWall[1] + Constants.PAC_RADIUS;
                } else if (this.currentDirection == Direction.DOWN) {
                    checkWall[1] = checkWall[1] - Constants.PAC_RADIUS;
                }
                break;
            case LEFT:
                checkWall[0] = checkWall[0] - Constants.PAC_RADIUS;
                if (this.currentDirection == Direction.UP) {
                    checkWall[1] = checkWall[1] + Constants.PAC_RADIUS;
                } else if (this.currentDirection == Direction.DOWN) {
                    checkWall[1] = checkWall[1] - Constants.PAC_RADIUS;
                }
                break;
            case DOWN:
                checkWall[1] = checkWall[1] + Constants.PAC_RADIUS;
                if (this.currentDirection == Direction.RIGHT) {
                    checkWall[0] = checkWall[0] - Constants.PAC_RADIUS;
                } else if (this.currentDirection == Direction.LEFT) {
                    checkWall[0] = checkWall[0] + Constants.PAC_RADIUS;
                }
                break;
            case UP:
                checkWall[1] = checkWall[1] - Constants.PAC_RADIUS;
                if (this.currentDirection == Direction.RIGHT) {
                    checkWall[0] = checkWall[0] - Constants.PAC_RADIUS;
                } else if (this.currentDirection == Direction.LEFT) {
                    checkWall[0] = checkWall[0] + Constants.PAC_RADIUS;
                }
                break;
        }

        int[] arrayIndexWall = this.mazeBoard.checkPosInArray(checkWall);

        //returns false if the array index is out of bounds
        //ie. paccy doesn't noclip through the border
        if ((arrayIndexWall[0] >= Constants.NUM_ROWS) || (arrayIndexWall[0] < 0) ||
                (arrayIndexWall[1] >= Constants.NUM_COLS) || (arrayIndexWall[1] < 0)) {
            return false;
        }

        return this.mazeBoard.getIsXWay(arrayIndexWall[0], arrayIndexWall[1],
                arrayIndexWall[2], arrayIndexWall[3]);
    }

    private void checkWallAtTurn() {
        if (this.intendedDirection != null) {
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
            } else {
                this.intendedDirection = newDirection;
            }
        }
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
                        Constants.MOVE_SPEED);
                this.pacCircle.setCenterX(this.pacCircle.getCenterX() +
                        Constants.MOVE_SPEED);
                break;
            case LEFT:
                this.pacMouth.setLayoutX(this.pacMouth.getLayoutX() -
                        Constants.MOVE_SPEED);
                this.pacCircle.setCenterX(this.pacCircle.getCenterX() -
                        Constants.MOVE_SPEED);
                break;
            case UP:
                this.pacMouth.setLayoutY(this.pacMouth.getLayoutY() -
                        Constants.MOVE_SPEED);
                this.pacCircle.setCenterY(this.pacCircle.getCenterY() -
                        Constants.MOVE_SPEED);
                break;
            case DOWN:
                this.pacMouth.setLayoutY(this.pacMouth.getLayoutY() +
                        Constants.MOVE_SPEED);
                this.pacCircle.setCenterY(this.pacCircle.getCenterY() +
                        Constants.MOVE_SPEED);
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
