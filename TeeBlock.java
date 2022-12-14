package indy;

import javafx.scene.layout.Pane;

/**
 * Creates the block shaped like a "T"
 */
public class TeeBlock extends MazeBlock {

    private MazeTile[][] tileArray;

    /**
     * initializes the block with a specific position and orientation
     * @param gamePane
     * @param xIndex
     * @param yIndex
     * @param rotateNum
     */
    public TeeBlock(Pane gamePane, int xIndex, int yIndex, int rotateNum) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
        this.setWayTiles();
        this.rotateBlock(rotateNum);
    }

    /**
     * sets four tiles to be ways in the shape of a T
     */
    private void setWayTiles() {
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[1][2].setIsWall(false);
        this.tileArray[1][0].setIsWall(false);
    }
}
