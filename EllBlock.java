package indy;

import javafx.scene.layout.Pane;

/**
 * Creates the block shaped like an "L"
 */
public class EllBlock extends MazeBlock {

    private MazeTile[][] tileArray;

    /**
     * initializes the block with a specific position and orientation
     * @param gamePane
     * @param xIndex
     * @param yIndex
     * @param rotateNum
     */
    public EllBlock(Pane gamePane, int xIndex, int yIndex, int rotateNum) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
        this.setWays();
        this.rotateBlock(rotateNum);
    }

    /**
     * sets the center, top-center, and right-centre blocks to be ways
     */
    private void setWays() {
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[1][2].setIsWall(false);
    }
}
