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
        iteratively calls setupMazeBlock

        rules
        - there should be a border of walls
        - there should be an end block (that's REACHABLE)
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
