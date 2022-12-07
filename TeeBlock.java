package indy;

import javafx.scene.layout.Pane;

public class TeeBlock extends MazeBlock {

    private MazeTile[][] tileArray;
    private static boolean[] myConstraintsArray;


    public TeeBlock(Pane gamePane, int xIndex, int yIndex) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
        myConstraintsArray = MazeBlock.getConstraints(0);
        this.setWayTiles();
    }

    private void setWayTiles() {
        this.tileArray[1][1].setIsWall(false);
        this.tileArray[0][1].setIsWall(false);
        myConstraintsArray[0] = true;
        this.tileArray[1][2].setIsWall(false);
        myConstraintsArray[1] = true;
        this.tileArray[1][0].setIsWall(false);
        myConstraintsArray[2] = true;
    }
}
