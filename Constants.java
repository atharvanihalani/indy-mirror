package indy;

import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Constants {



    public static final int TILE_SIZE = 25;
    public static final int NUM_ROWS = 8; //of blocks
    public static final int NUM_COLS = 6;

    public static final int SCENE_WIDTH = (NUM_COLS*3 + 2)*TILE_SIZE;
    public static final int SCENE_HEIGHT = (NUM_ROWS*3 + 2)*TILE_SIZE + 35;

    //TODO make scene slightly bigger for timer, etc

    public static final Color WALL_COLOR = Color.DARKGRAY.darker();
    public static final Color WAY_COLOR = Color.BLACK;


    public static final Duration UPDATE_GAME_EVERY = Duration.millis(1);
    public static final Double PAC_DISPLACEMENT = 0.05;


}
