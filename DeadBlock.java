package indy;

import javafx.scene.layout.Pane;

/**
 * Creates the dead-end block
 */
public class DeadBlock extends MazeBlock {

    private MazeTile[][] tileArray;

    /**
     * Constructor always initializes block with a specific
     * location and orientation
     * @param gamePane
     * @param xIndex
     * @param yIndex
     * @param rotateNum
     */
    public DeadBlock(Pane gamePane, int xIndex, int yIndex, int rotateNum) {
        super(gamePane, xIndex, yIndex);
        this.tileArray = super.getTileArray();
        this.setWayTiles();
        this.rotateBlock(rotateNum);
    }

    /**
     * sets the center and the top-center tiles to be ways
     */
    private void setWayTiles() {
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[1][1].setIsWall(false);
    }
}
