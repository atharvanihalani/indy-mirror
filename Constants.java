package indy;

import javafx.scene.paint.Color;

public class Constants {





    public static final int TILE_SIZE = 25;
    public static final int NUM_ROWS = 6; //of blocks
    public static final int NUM_COLS = 4;

    public static final int SCENE_WIDTH = NUM_COLS *3*TILE_SIZE + 100; //100 is margin
    public static final int SCENE_HEIGHT = NUM_ROWS*3*TILE_SIZE + 150;

    //TODO make scene slightly bigger for timer, etc

    public static final Color WALL_COLOR = Color.DARKGRAY.darker();
    public static final Color WAY_COLOR = Color.LIGHTGRAY;

    /*
    scene size should depend on row/column num
    set up a start screen to let u select options
     */



}
