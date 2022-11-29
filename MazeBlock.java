package indy;

public class MazeBlock {

    private MazeTile[][] tileArray;
    //private (uninstantiated) array storing the constraints' information

    public MazeBlock() {
        this.tileArray = new MazeTile[3][3];

        //concrete method to instantiate the array logically
    }

    /*
    helper method to set up a new maze block graphically
        looks at the MazeTile at [1, 2] in the MazeBlock below it and the
        MazeTile at [2, 1] in the MazeBlock to the right. checks whether
        these two tiles are walls or ways.
            (MazeBlock needs to know blockArray, or MazeBoard can directly
            pass that as an argument)
            if both are walls
                can have L (1) or dead ends (2 each)
            if one is a wall
                can have L (2), dead ends (3 each), path (1), or T (1)
            if neither is a wall
                all orientations of anything are possible
     */



    /*
    //concrete method to rotate the array

     */


    //
}
