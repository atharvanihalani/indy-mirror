package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class MazeBoard {

    private Pane gamePane;
    private MazeBlock[][] blockArray;
    private Pellet[][] pelletArray; //note: this will have MANY empty spots

    public MazeBoard(Pane gamePane) {
        this.gamePane = gamePane;
        this.blockArray = new MazeBlock[Constants.NUM_ROWS][Constants.NUM_COLS];
        //instantiate pelletArray with dimensions tileNum * tileNum


        this.setupBorder();
        this.setupFirstBlock(); // so that pacman spawns at the same place
        this.setupExit();
        this.setupMaze();

        new Pacman(this.gamePane);
        //add pacman
        //this.addPellets
    }





    /**
     * Method that graphically sets up the borders surrounding the maze
     * Doesn't store em logically cuz I don't need to know about them
     * once they've been set up.
     */
    private void setupBorder() {

        //loop that sets up both horizontal walls simultaneously
        for (int i = 0; i <= Constants.NUM_COLS *3; i++) {
            new MazeTile(this.gamePane, true, i*Constants.TILE_SIZE, 0);
            new MazeTile(this.gamePane, true, (i+1)*Constants.TILE_SIZE,
                    ((Constants.NUM_ROWS*3)+1)*Constants.TILE_SIZE);
        }

        //loop that sets up both vertical walls simultaneously
        for (int i = 0; i <= Constants.NUM_ROWS*3; i++) {
            new MazeTile(this.gamePane, true, 0, (i+1)*Constants.TILE_SIZE);
            new MazeTile(this.gamePane, true,
                    ((Constants.NUM_COLS *3)+1)*Constants.TILE_SIZE,
                    i*Constants.TILE_SIZE);
        }
    }

    /**
     * Sets up the exit tile at the bottom right corner, and ensures
     * it's reachable.
     */
    private void setupExit() {
        MazeTile exitTile = new MazeTile(this.gamePane, false,
                ((Constants.NUM_COLS *3)+1)*Constants.TILE_SIZE,
                (Constants.NUM_ROWS*3-1)*Constants.TILE_SIZE);
        exitTile.colorTile(Color.GREEN.brighter());

//        note the NUM_ROWS and NUM_COLS arguments are 'switched; because
//        the array's been set up in row-major format
        this.blockArray[Constants.NUM_ROWS-1][Constants.NUM_COLS -1] =
                new TeeBlock(this.gamePane, Constants.NUM_COLS -1,
                        Constants.NUM_ROWS-1, 0);
    }

    private void setupFirstBlock() {
        this.blockArray[0][0] = new EllBlock(this.gamePane, 0, 0, 1);
    }

    private void setupMaze() {
        for (int i = 0; i < Constants.NUM_ROWS; i++) {
            for (int j = 0; j < Constants.NUM_COLS; j++) {

                //ensures we don't replace the first or last block
                if ((i == 0 && j == 0) || (i == Constants.NUM_ROWS - 1 &&
                        j == Constants.NUM_COLS-1)) {
                    continue;
                }

                int randomBlockSwitch = (int) Math.floor(Math.random()*30);
                int randomRotation = (int) Math.floor(Math.random()*4);

                System.out.println(String.format("Currently iterating through block [%s, %s] \r\n" +
                        "random block: %s \r\n" +
                        "random rotation: %s", i, j, randomBlockSwitch, randomRotation));

                boolean[] outerConstraints = this.getOuterConstraints(i, j);
                boolean[] innerConstraints = this.tryRandomBlock(i, j,
                        randomBlockSwitch, randomRotation);

                ArrayList<Object> backtrackingInfo = this.checkBacktracking(outerConstraints, i, j);
                if ((boolean) backtrackingInfo.get(0)) {
                    i = (int) backtrackingInfo.get(1);
                    j = (int) backtrackingInfo.get(2);
                    continue;
                }

                boolean isLast = false;
                if (i == Constants.NUM_ROWS - 1 && j == Constants.NUM_COLS - 2) {
                    isLast = true;
                }
                if (!checkConstraintsMatch(
                        outerConstraints, innerConstraints, isLast)) {
                    System.out.println("lol chutiya, try again \r\n");
                    if (j == 0) {
                        j = Constants.NUM_COLS - 1;
                        i--;
                    } else {
                        j--;
                    }
                } else {
                    System.out.println("block gen successful \r\n");
                }
            }
        }
    }

    private ArrayList<Object> checkBacktracking(boolean[] outerConstraints, int i, int j) {

        ArrayList<Object> backtrackingInfo = new ArrayList<>();

        boolean backtrack = true;
        if (outerConstraints[0] || outerConstraints[3]) {
            backtrack = false;
        }

        backtrackingInfo.add(backtrack);


        System.out.println("backtracking? " + backtrack);

        boolean whereToBacktrack = true;
        switch ((int) Math.floor(Math.random()*2)) {
            case 0:
            case 1:
                whereToBacktrack = false;
        }

        System.out.println("backtracking to: " + whereToBacktrack);

        if (backtrack) {
            if (j == 0) {
                j--;
                i--;
            } else if (j == 1) {
                if (whereToBacktrack) {
                    j = Constants.NUM_COLS - 1;
                    i--;
                } else {
                    j--;
                    i--;
                }
            } else {
                if (!whereToBacktrack && i != 0) {
                    j--;
                    i--;
                } else {
                    j = j - 2;
                }
            }
        }

        backtrackingInfo.add(i);
        backtrackingInfo.add(j);
        return backtrackingInfo;
    }

    private boolean[] getOuterConstraints(int i, int j) {

        boolean[] externalConstraints = new boolean[4];
        for (int k = 0; k < 4; k++) {
            externalConstraints[k] = false;
        }

        //checks for constraints above and below the block
        if (i == 0) {
            if (this.getIsXWay(i + 1, j, 0, 1)) {
                externalConstraints[2] = true;
            }
        } else if (i == (Constants.NUM_ROWS - 1)) {
            if (this.getIsXWay(i - 1, j, 2, 1)) {
                externalConstraints[0] = true;
            }
        } else {
            if (this.getIsXWay(i + 1, j, 0, 1)) {
                externalConstraints[2] = true;
            }
            if (this.getIsXWay(i - 1, j, 2, 1)) {
                externalConstraints[0] = true;
            }
        }

        //checks for constraints to the left and right of the block
        if (j == 0) {
            if (this.getIsXWay(i, j + 1, 1, 0)) {
                externalConstraints[1] = true;
            }
        } else if (j == (Constants.NUM_COLS - 1)) {
            if (this.getIsXWay(i, j - 1, 1, 2)) {
                externalConstraints[3] = true;
            }
        } else {
            if (this.getIsXWay(i, j + 1, 1, 0)) {
                externalConstraints[1] = true;
            }
            if (this.getIsXWay(i, j - 1, 1, 2)) {
                externalConstraints[3] = true;
            }
        }

        return externalConstraints;
    }

    private boolean[] tryRandomBlock(int i, int j, int randomBlockSwitch,
                                     int randomRotation) {
        boolean[] randomBlockConstraints;

        switch (randomBlockSwitch) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.DEAD_BLOCK, randomRotation);
                this.blockArray[i][j] = new DeadBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.AYE_BLOCK, randomRotation);
                this.blockArray[i][j] = new AyeBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.ELL_BLOCK, randomRotation);
                this.blockArray[i][j] = new EllBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.TEE_BLOCK, randomRotation);
                this.blockArray[i][j] = new TeeBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 29:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.PLUS_BLOCK, randomRotation);
                this.blockArray[i][j] = new PlusBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + randomBlockSwitch);
        }

        return randomBlockConstraints;
    }

    private boolean checkConstraintsMatch(boolean[] outerConstraints,
                                       boolean[] innerConstraints, boolean isLast) {
        boolean constraintsMatch = true;
        if (outerConstraints[0] && !innerConstraints[0]) {
            constraintsMatch = false;
        }
        if (outerConstraints[3] && !innerConstraints[3]) {
            constraintsMatch = false;
        }
        if (isLast && outerConstraints[1] && !innerConstraints[1]) {
            constraintsMatch = false;
        }
        return constraintsMatch;
    }


    /**
     * Helper method that returns whether a specific tile in a specific block
     * is a 'way' or not. Returns false if the block hasn't been initialized.
     *
     * TODO better design would delegate this method to MazeBlock (low coupling)
     * @param blArrRow block array row index
     * @param blArrCol block array column index
     * @param tlArrRow tile array row index (within that specific block)
     * @param tlArrCol tile array column index
     * @return
     */
    private boolean getIsXWay(int blArrRow, int blArrCol, int tlArrRow, int tlArrCol) {

        if (blArrRow < 0) {
            throw new IllegalStateException("Block Array Row is " + blArrRow);
        }
        if (blArrCol < 0) {
            throw new IllegalStateException("Block Array Column is " + blArrCol);
        }

        if (this.blockArray[blArrRow][blArrCol] != null) {
            return !this.blockArray[blArrRow][blArrCol].getTileArray()[tlArrRow][tlArrCol].getIsWall();
        } else {
            return false;
        }
    }




    /*
    method to add the pellets to the maze
        iterates through the blockArray
            iterates through the tileArray
                if !isTileWall then
                    add pellet to pelletArray
                    add pellet graphically to maze (helper)
     */

    /*
    method to remove a pellet from the maze
        //takes in pelletArray coordinates as arg
            call helper method to remove it graphically
            remove it from array
     */

    /*
    method to check if pacman is colliding w a pellet
        myPacman.getPos()
        //pass this into pellet array
        if pelletArray[_][_] != null
            this.removePelletAt([][])
     */


}
