package indy;

import javafx.scene.layout.Pane;

public abstract class MazeBlock {

    private Pane gamePane;
    private MazeTile[][] tileArray;

    //private (uninstantiated) array storing the constraints' information

    public MazeBlock(Pane gamePane, int xIndex, int yIndex) {
        this.gamePane = gamePane;
        this.tileArray = new MazeTile[3][3];

        this.setupTileArray(xIndex, yIndex);
    }

    /*
    //concrete method to rotate the array
     */

    public void rotateBlock() {
        boolean tileA = this.tileArray[0][1].getIsWall();
        boolean tileB = this.tileArray[1][2].getIsWall();
        boolean tileC = this.tileArray[2][1].getIsWall();
        boolean tileD = this.tileArray[1][0].getIsWall();

        this.tileArray[0][1].setIsWall(tileD);
        this.tileArray[1][2].setIsWall(tileA);
        this.tileArray[2][1].setIsWall(tileB);
        this.tileArray[1][0].setIsWall(tileC);
    }

    /**
     * Logically sets up the Tile Array for all subclasses
     * oh lawd bless that sweet sweet polymorphism
     */
    private void setupTileArray(int xIndex, int yIndex) {

//        these variables store the coordinates of the (top left point
//        of this) maze block.
        int xPos = xIndex*Constants.TILE_SIZE*3 + Constants.TILE_SIZE;
        int yPos = yIndex*Constants.TILE_SIZE*3 + Constants.TILE_SIZE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.tileArray[i][j] = new MazeTile(this.gamePane);
                this.tileArray[i][j].setTilePos((xPos + j*Constants.TILE_SIZE),
                        (yPos + i*Constants.TILE_SIZE));

                this.tileArray[i][j].setIsWall(true);
            }
        }


    }

    /**
     * Accessor method for the subclasses to access the tile array
     * @return
     */
    public MazeTile[][] getTileArray() {
        return this.tileArray;
    }



    /*
    method that sets the position of the block
        takes in a default arg and then offsets the individual tiles
        based on their position in the tileArray
     */

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





}
