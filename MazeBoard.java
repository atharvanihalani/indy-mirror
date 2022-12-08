package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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


        //this.fillMaze()
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
                        Constants.NUM_ROWS-1);
    }

    private void setupFirstBlock() {
        this.blockArray[0][0] = new EllBlock(this.gamePane, 0, 0);
        this.blockArray[0][0].rotateBlock();
    }

    private void setupMaze() {
        for (int i = 0; i < Constants.NUM_ROWS; i++) {
            for (int j = 0; j < Constants.NUM_COLS; j++) {
                if (i == 0) {j++;} //ensures we don't replace the first block

                boolean[] outerConstraints = this.getOuterConstraints(i, j);
                /*
                if the outer constraints are all false, go to previous block and regenerate
                else, continue
                 */
                int randomBlockSwitch = (int) Math.floor(Math.random()*5);
                int randomRotation = (int) Math.floor(Math.random()*4);
                boolean[] randomBlockConstraints;

                switch (randomBlockSwitch) {
                    case 0:
                        randomBlockConstraints = Constraints.rotateBlock(
                                Constraints.DEAD_BLOCK, randomRotation);
                        break;
                    case 1:
                        randomBlockConstraints = Constraints.rotateBlock(
                                Constraints.AYE_BLOCK, randomRotation);
                        break;
                    case 2:
                        randomBlockConstraints = Constraints.rotateBlock(
                                Constraints.ELL_BLOCK, randomRotation);
                        break;
                    case 3:
                        randomBlockConstraints = Constraints.rotateBlock(
                                Constraints.TEE_BLOCK, randomRotation);
                        break;
                    case 4:
                        randomBlockConstraints = Constraints.rotateBlock(
                                Constraints.PLUS_BLOCK, randomRotation);
                        break;
                }

                /*
                if the random block constraints match up with the outer constraints
                    generate that block
                    else, try another block
                 */

                //we don't replace the last block
            }
        }
    }


    private boolean[] getOuterConstraints(int i, int j) {

        boolean[] externalConstraints = new boolean[4];
        for (int k = 0; i < 4; i++) {
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
        } else if (i == (Constants.NUM_COLS - 1)) {
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
        if (this.blockArray[blArrRow][blArrCol] != null) {
            return !this.blockArray[blArrRow][blArrCol].getTileArray()[tlArrRow][tlArrCol].getIsWall();
        } else {
            return false;
        }
    }


    /*
    method to fill the maze graphically
        iteratively goes thru blockArray
            at each iteration, it looks at the four center-edge tiles in the
            blocks surrounding it (eg. MazeTile at [1, 2] in the MazeBlock
            above it, etc) IF the blocks have been instantiated. Checks whether
            these four tiles are walls or ways.
                stores these constraints in a local arraylist of enums (maxsize 4)
                //if the current block is at a border (ie. index = 0/max), add that constraint too


            (processes these constraints and either
                generates a new tile
                or goes back to the previous one (if impossible))

            if both are walls
                (can have L (1) or dead ends (2 each))
                calls set up L-block, and passes in rotation (enum) as arg
            if one is a wall
                (can have L (2), dead ends (3 each), path (1), or T (1))
                same as above
            if neither is a wall
                (all orientations of anything are possible)
                same as above
     */

    /*
    {method to process enumArraylist
        //case it and return two values: the case TYPE and ORIENTATION
        if enumArrayList.length = 0
            return no constraints + any orientation
        if enumArrayList.length = 1
            if enumArrayList.get(0) = UP
                return}
     */




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
