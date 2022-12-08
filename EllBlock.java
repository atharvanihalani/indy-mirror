package indy;

import javafx.scene.layout.Pane;

public class EllBlock extends MazeBlock {

    private MazeTile[][] tileArray;


    public EllBlock(Pane gamePane, int xIndex, int yIndex) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
        this.setWays();
    }



    private void setWays() {
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[0][1].setIsWall(false);
        this.tileArray[1][2].setIsWall(false);
    }
}
