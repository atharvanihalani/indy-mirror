package indy;

public class MazeBlock {

    private MazeTile[][] tileArray;
    //private (uninstantiated) array storing the constraints' information

    public MazeBlock() {
        this.tileArray = new MazeTile[3][3];

        new MazeTile(true); //test

        //concrete method to instantiate the array logically
    }

    /*
    helper method to set up a new maze block graphically.
        graphically sets up the tileArray to get a 3x3 block
        randomly rotates it by any of the possible arguments
        sets it up graphically within the MazeBoard
     */


    /*
    method to check ifTileWall
        accepts coords of tileArray as arg
        checks if tile isWall and returns boolean
     */


    /*
    //concrete method to rotate the array

     */


    //
}
