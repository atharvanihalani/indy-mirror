package indy;

import javafx.scene.layout.Pane;

public class TeeBlock extends MazeBlock {

    private MazeTile[][] tileArray;

    public TeeBlock(Pane gamePane, int xIndex, int yIndex) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
        this.setWayTiles();
    }

    private void setWayTiles() {
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[1][0].setIsWall(false);
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[1][2].setIsWall(false);
    }
}
