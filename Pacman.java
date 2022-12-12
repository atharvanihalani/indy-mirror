package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

import java.util.Arrays;

public class Pacman {

    private Circle pacCircle;
    private Polygon pacMouth;
    private Pane gamePane;
    private MazeBoard mazeBoard;
    private Direction currentDirection;
    private Direction intendedDirection;
    private Rotate standardRotate;
    public Pacman(Pane gamePane, MazeBoard mazeBoard) {
        this.pacCircle = new Circle(12, Color.YELLOW);
        this.pacMouth = new Polygon();
        this.gamePane = gamePane;
        this.mazeBoard = mazeBoard;
        this.currentDirection = Direction.RIGHT;
        this.intendedDirection = null;
        this.standardRotate = new Rotate(90, 0, 8);

        this.setupPac();
    }

    public void updatePacman() {
        if (this.checkMotionValidity(this.currentDirection)) {
            this.pacMover(this.currentDirection);
        }
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

    /**
     * This method checks whether motion is possible in a given
     * direction. ie. whether the tile ahead/behind/above/below
     * pacman is a wall or not.
     * @param direction the direction for which motion validity
     *                  will be checked
     * @return indicates whether motion is possible or not
     */
    private boolean checkMotionValidity(Direction direction) {
        double[] pacCoords = new double[2];
        pacCoords[0] = this.pacCircle.getCenterX();
        pacCoords[1] = this.pacCircle.getCenterY();

        /*
        increments coords in the direction of motion in order to get
        the coordinates of a specific tile T. We're then checking if
        T is a wall or not
         */
        switch (direction) {
            case RIGHT:
                pacCoords[0] = pacCoords[0] + Constants.TILE_SIZE/2.0;
                break;
            case LEFT:
                pacCoords[0] = pacCoords[0] - Constants.TILE_SIZE/2.0;
                break;
            case DOWN:
                pacCoords[1] = pacCoords[1] + Constants.TILE_SIZE/2.0;
                break;
            case UP:
                pacCoords[1] = pacCoords[1] - Constants.TILE_SIZE/2.0;
                break;
        }

        System.out.println(Arrays.toString(pacCoords));

        int[] arrayIndex = this.checkPosInArray(pacCoords);
        return this.mazeBoard.getIsXWay(arrayIndex[0], arrayIndex[1],
                arrayIndex[2], arrayIndex[3]);
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

        //ensures the index is always within bounds
        if (arraysIndex[0] >= Constants.NUM_COLS) {
            arraysIndex[0] = Constants.NUM_COLS - 1;
        } else if (arraysIndex[0] < 0) {
            arraysIndex[0] = 0;
        }
        if (arraysIndex[1] >= Constants.NUM_ROWS) {
            arraysIndex[1] = Constants.NUM_ROWS - 1;
        } else if (arraysIndex[1] < 0) {
            arraysIndex[1] = 0;
        }

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
            case SPACE:
                //backtracking with stack
                break;
        }
    }


    private void moveLogic(Direction newDirection) {
        if (newDirection == this.currentDirection) {
            //exit method
        } else if (newDirection.getOpposite() == this.currentDirection) {
            this.currentDirection = newDirection;
        } else {

            if (this.checkMotionValidity(newDirection)) {
                this.currentDirection = newDirection;
            } else {
                this.moveLogic(newDirection);
            }


            /*
            check motion validity for newDir
            if motion is valid (note: only when pacman is stationary)
                this.move(newDir) + this.currentDir = newDir;
            if motion is not valid
               this.keepCheckingValidity(newDir)
               if it's ever true, call this.move(newDir) AFTER pacman has moved 12.5 steps further
             */


            /*
            TODO
                don't let paccy noclip thru the borders
                glitch where paccy turns into a wall while moving and bugs out for a sec (think its
                    the same as the occasional stack overflow overflowing terminal)
                ensure that paccy can only turn perpendicularly when at center of the border
             */
        }
    }

    private void pacMover(Direction direction) {
        this.intendedDirection = null;
        this.rotatePac(direction);

        switch (direction) {
            case RIGHT:
                System.out.println("move right");
                this.pacMouth.setLayoutX(this.pacMouth.getLayoutX() + 0.05);
                this.pacCircle.setCenterX(this.pacCircle.getCenterX() + 0.05);
                break;
            case LEFT:
                System.out.println("move left");
                this.pacMouth.setLayoutX(this.pacMouth.getLayoutX() - 0.05);
                this.pacCircle.setCenterX(this.pacCircle.getCenterX() - 0.05);
                break;
            case UP:
                System.out.println("move up");
                this.pacMouth.setLayoutY(this.pacMouth.getLayoutY() - 0.05);
                this.pacCircle.setCenterY(this.pacCircle.getCenterY() - 0.05);
                break;
            case DOWN:
                System.out.println("move down");
                this.pacMouth.setLayoutY(this.pacMouth.getLayoutY() + 0.05);
                this.pacCircle.setCenterY(this.pacCircle.getCenterY() + 0.05);
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
