package indy;

import javafx.scene.layout.Pane;

/**
 * creates the block shaped like a plus
 */
public class PlusBlock extends MazeBlock {

    private MazeTile[][] tileArray;

    /**
     * initializes this block with a specific position, INVARIANT of orientation
     * naturally, cuz a plus has rotational symmetry order 4
     * @param gamePane
     * @param xIndex
     * @param yIndex
     * @param rotateNum
     */
    public PlusBlock(Pane gamePane, int xIndex, int yIndex, int rotateNum) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
        this.setWayTiles();
    }

    /**
     * sets the centre and four adjacent tiles to be ways
     * ie. in a plus shape
     */
    private void setWayTiles() {
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[1][0].setIsWall(false);
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[1][2].setIsWall(false);
        this.tileArray[2][1].setIsWall(false);
    }
}
