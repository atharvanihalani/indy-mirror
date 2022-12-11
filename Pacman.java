package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Pacman {

    private Circle pacCircle;
    private Polygon pacMouth;
    private Pane gamePane;
    private MazeBoard mazeBoard;
    private Direction currentDirection;
    public Pacman(Pane gamePane, MazeBoard mazeBoard) {
        this.pacCircle = new Circle(12, Color.YELLOW);
        this.pacMouth = new Polygon();
        this.gamePane = gamePane;
        this.mazeBoard = mazeBoard;
        this.currentDirection = Direction.RIGHT;

        this.setupPac();
        //this.move(Direction.RIGHT);


    }

    private void setupPac() {
        double openMouth = 5.0;
        //TODO make these constants

        this.pacMouth.getPoints().addAll(15.0, 10-openMouth,
                15.0, 10+openMouth,
                0.0, 10.0);
        this.pacMouth.setFill(Color.BLACK);

        this.gamePane.getChildren().addAll(pacCircle, pacMouth);

        this.pacCircle.setCenterX(Constants.TILE_SIZE*2.5); //TODO tweak these
        this.pacCircle.setCenterY(Constants.TILE_SIZE*2.5);
        this.pacMouth.setLayoutX(Constants.TILE_SIZE*2.5);
        this.pacMouth.setLayoutY(Constants.TILE_SIZE*2.5 - 10);
    }

    private boolean checkMotionValidity(Direction direction) {
        double[] pacCoords = new double[2];
        pacCoords[0] = this.pacCircle.getCenterX();
        pacCoords[1] = this.pacCircle.getCenterY();

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

        int[] arrayIndex = this.checkPosInArray(pacCoords);
        return this.mazeBoard.getIsXWay(arrayIndex[1], arrayIndex[0],
                arrayIndex[3], arrayIndex[2]);
    }

    private int[] checkPosInArray(double[] coords) {
        int[] arraysIndex = new int[4];
        arraysIndex[0] = (int) Math.floor((coords[0] - Constants.TILE_SIZE) /
                (Constants.TILE_SIZE*3));
        arraysIndex[1] = (int) Math.floor((coords[1] - Constants.TILE_SIZE) /
                (Constants.TILE_SIZE*3));

        double tempXVar = (coords[0] - Constants.TILE_SIZE) % (Constants.TILE_SIZE*3);
        double tempYVar = (coords[1] - Constants.TILE_SIZE) % (Constants.TILE_SIZE*3);

        arraysIndex[2] = (int) Math.floor(tempXVar / Constants.TILE_SIZE);
        arraysIndex[3] = (int) Math.floor(tempYVar / Constants.TILE_SIZE);

        return arraysIndex;
    }

    /*
    helper method to return pacman's position in the array
        gets pacman's current x and y pos
        uses that to find out the current position in NESTED 2d array
            divide by blocksize (& floor) to calc outer array coordinates
            calc mod when dividing by blocksize, THEN divide by tilesize (& floor) to calc nested array coords.
     */


    public void keyHandler(KeyCode keyCode) {
        switch (keyCode) {
            case RIGHT:
                System.out.println("move right");
                //move right
                /*
                if the direction is the same as the current direction
                of motion, do nothing (instance var)

                if the direction is opposite to the current direction
                of motion, directly turn around and move + update
                this.currentMotion

                else, check motion validity (by checking distance bw
                pac & wall)
                    if valid, move + update this.currentMotion
                    if not valid, constantly call validity checker until
                    it ends up valid (direction one is checking for should
                    also be stored in an instance variable, so there is only
                    ever one dir at any point)
                 */
                break;
            case LEFT:
                //move left
                break;
            case UP:
                //move up
                break;
            case DOWN:
                //move down
                break;
            case SPACE:
                //backtracking with stack
                break;
        }
    }



    public void moveLogic(Direction newDirection) {
        if (newDirection == this.currentDirection) {
            //exit method
        } else if (newDirection.getOpposite() == this.currentDirection) {
            //this.move(newDir) + set current direction to new one TODO write method
        } else {
            /*
            check motion validity for newDir
            if motion is valid (note: only when pacman is stationary)
                this.move(newDir) + this.currentDir = newDir;
            if motion is not valid
               this.keepCheckingValidity(newDir) TODO make this helper
               if it's ever true, call this.move(newDir)
               it should also reset if ANY arrow key is pressed
             */
        }
    }

    /*
    TODO make the move method such that it's constantly called for the
        direction pacman is currently facing

    generic move method accepting enum arg for direction
        if newDir == this.currentDir
            break;
        if newDir.getOpposite == this.currentDir
            this.move(newDir) TODO make this helper
            this.currentDir = newDir; (include this in the prev method)
        else (if newDir is perpendicular to this.currentDir)
            check motion validity for this new direction
            if motion is valid (note: only when pacman is stationary)
                this.move(newDir)
                this.currentDir = newDir;
            if motion is not valid
               this.keepCheckingValidity(newDir) TODO make this helper
               if it's ever true, call this.move(newDir)
               it should also reset if ANY arrow key is pressed

     */

    /*
    method that checks motion validity
        takes in an enum param for one of the four directions

        checks if the center of pacCircle is x units away from a wall

        this should
        a) stop pacman from moving in a direction it's impossible to move in
        b) stop pacman from colliding into a wall
     */




    /*
    method to move pacman (copy-paste from cartoon)
        continuously checks if it's colliding with a wall (timeline updated every moment)
            if no, continue moving in same direction
            if yes, move a tiny bit away, and stop
     */


    /*
    method to check if pacman is colliding w a wall
        //updated continuously by timeline
        //returns a boolean
        this.getPos()
        checks if the current TILE isWall or not
     */

    /*
    helper method to rotate pacman
        called when moving in a diff direction
     */

}
