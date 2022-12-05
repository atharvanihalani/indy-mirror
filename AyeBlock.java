package indy;

import javafx.scene.layout.Pane;

public class AyeBlock extends MazeBlock{

//    creates a copy of MazeBlock's generic tileArray & specifies
//    its properties
    private MazeTile[][] tileArray;

    public AyeBlock(Pane gamePane, int xIndex, int yIndex) {
        super(gamePane, xIndex, yIndex);

        this.tileArray = super.getTileArray();
    }


    private void setWayTiles() {

    }

}
