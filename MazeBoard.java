package indy;

import javafx.scene.layout.Pane;

public class MazeBoard {

    private Pane gamePane;
    private MazeBlock[][] blockArray;
    private Pellet[][] pelletArray; //note: this will have MANY empty spots

    public MazeBoard(Pane gamePane) {
        this.gamePane = gamePane;
        this.blockArray = new MazeBlock[Constants.NUM_COLUMNS][Constants.NUM_ROWS];
        //instantiate pelletArray with dimensions tileNum * tileNum

        new MazeBlock();

        this.setupBorder();
        //this.makeExit()
        //this.makeFirstBlock() (so that pacman spawns same place)
        //this.fillMaze()
        //this.addPellets
    }

    private void setupBorder() {
        for (int i = 0; i <= Constants.NUM_ROWS*3; i++) {

        }
    }

    /*
    helper method to set up a wall border (beyond the 2d array)
        iteratively set up all 4 borders graphically
     */

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
    method to make the first block
        instantiates this.blockArray[0][0]
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

    /*
    helper method to create an exit tile
        set the bottom-right-most border tile to be a MazeTile w
        setFill green
        set the bottom-right-most block to be an upside down T

     */

}
