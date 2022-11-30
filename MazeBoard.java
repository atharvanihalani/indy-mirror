package indy;

public class MazeBoard {

    private MazeBlock[][] blockArray;

    public MazeBoard() {
        this.blockArray = new MazeBlock[Constants.NUM_COLUMNS][Constants.NUM_ROWS];

        new MazeBlock();

        //this.setupBorder()
        //this.makeExit()
        //this.fillMaze()
    }

    /*
    method to fill the maze graphically
        iteratively goes thru blockArray
        looks at the MazeTile at [1, 2] in the MazeBlock below it and the
        MazeTile at [2, 1] in the MazeBlock to the right. checks whether
        these two tiles are walls or ways.

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
    helper method to set up a wall border (beyond the 2d array)
        iteratively set up all 4 borders graphically
     */

    /*
    helper method to create an exit tile
        set the bottom-right-most border tile to be a MazeTile w
        setFill green
        set the bottom-right-most block to be an upside down T

     */

}
