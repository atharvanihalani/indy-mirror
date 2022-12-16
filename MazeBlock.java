package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Parent class for all mazeblocks. has methods to logically
 * and graphically position the block, rotate it, and to return
 * if a specific tile is a path.
 */
public abstract class MazeBlock {

    private final Pane gamePane;
    private final MazeTile[][] tileArray;

    public MazeBlock(Pane gamePane, int xIndex, int yIndex) {
        this.gamePane = gamePane;
        this.tileArray = new MazeTile[3][3];

        this.setupTileArray(xIndex, yIndex);
    }


    /**
     * Helper method to rotate the block by multiples of 90 degrees
     * <p>
     * When rotating a block, the centre and the corner tiles stay the
     * same (all walls). It's just the ones in-between corners that are
     * changed. This method rotates these tiles clockwise, by setting
     * each to the value of the tile one rotation 'behind' it. This
     * method's also got a loop that iterates for the number of rotations.
     * @param rotateNum The number of times the block should be rotated
     */
    public void rotateBlock(int rotateNum) {

        int rotateBy = rotateNum % 4; //to correct potential bad input

        for (int i = 0; i < rotateBy; i++) {
            boolean tileA = this.tileArray[0][1].getIsWall();
            boolean tileB = this.tileArray[1][2].getIsWall();
            boolean tileC = this.tileArray[2][1].getIsWall();
            boolean tileD = this.tileArray[1][0].getIsWall();

            this.tileArray[0][1].setIsWall(tileD);
            this.tileArray[1][2].setIsWall(tileA);
            this.tileArray[2][1].setIsWall(tileB);
            this.tileArray[1][0].setIsWall(tileC);
        }
    }

    /**
     * Logically and graphically sets up the Tile Array for all
     * subclasses. It first lays all nine tiles out at the same
     * position (top left corner). Then, it moves each tile either
     * right or down, proportional to the tile's postion in the 3x3
     * tile array.
     * oh lawd bless that sweet sweet polymorphism
     * @param xIndex x-index of this tile in the board's blockArray
     * @param yIndex y-index of this tile in the board's blockArray
     */
    private void setupTileArray(int xIndex, int yIndex) {

        /*
        these variables store the coordinates of the (top left point
        of this) maze block.
         */
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
     * Accessor method for the subclasses to get the tile array
     * @return
     */
    public MazeTile[][] getTileArray() {
        return this.tileArray;
    }

    /**
     * technically a helper for a helper for a helper but jeez we'll
     * ignore that.
     * just returns whether a particular tile is a way or not.
     * @param tileArrayRow tile row index
     * @param tileArrayCol tile column index
     * @return is way?
     */
    public boolean isTileWay(int tileArrayRow, int tileArrayCol) {
        return !this.tileArray[tileArrayRow][tileArrayCol].getIsWall();
    }


    public void setTilesVisibility(int[] mazeTile, boolean isVisible) {

        if (isVisible) {
            this.tileArray[mazeTile[0]][mazeTile[1]].resetColor();
        } else {
            this.tileArray[mazeTile[0]][mazeTile[1]].colorTile(Color.BLACK);
        }

    }

}
