package indy;

import javafx.scene.layout.Pane;

/**
 * Creates the block shaped like an "I"
 * Doesn't really do much, just sets specific tiles as paths to make
 * this block the shape of an I.
 */
public class AyeBlock extends MazeBlock {

//    creates a copy of MazeBlock's generic tileArray & specifies
//    its properties
    private MazeTile[][] tileArray;

    /**
     * Constructor always initializes block with a specific location
     * and orientation
     * @param gamePane
     * @param xIndex
     * @param yIndex
     * @param rotateNum
     */
    public AyeBlock(Pane gamePane, int xIndex, int yIndex, int rotateNum) {
        super(gamePane, xIndex, yIndex);
        this.tileArray = super.getTileArray();
        this.setWays();

        //unlike other tiles, this has rotational symmetry order 2.
        //the following line is to handle bad input
        this.rotateBlock(rotateNum % 2);
    }

    /**
     * Sets the centre three vertical lines to be ways
     */
    private void setWays() {
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[2][1].setIsWall(false);
    }

}
