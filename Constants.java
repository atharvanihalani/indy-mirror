package indy;

import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Constants {


    public static final int TILE_SIZE = 25;
    public static final int NUM_ROWS = 5;
    public static final int NUM_COLS = 5;
    public static final int QUIT_PANE_HEIGHT = 35;
    public static final int TIMER_PANE_HEIGHT = 50;

    public static final int SCENE_WIDTH = (NUM_COLS*3 + 2)*TILE_SIZE;
    public static final int SCENE_HEIGHT = (NUM_ROWS*3 + 2)*TILE_SIZE +
            QUIT_PANE_HEIGHT + TIMER_PANE_HEIGHT;

    public static final Color WALL_COLOR = Color.DARKGRAY.darker();
    public static final Color WAY_COLOR = Color.BLACK.brighter().brighter();
    public static final Duration UPDATE_GAME_EVERY = Duration.millis(1);
    public static final Double MOVE_SPEED = 0.05;
    public static final int PAC_RADIUS =  12;
    public static final int TIMER_COUNT = NUM_COLS*NUM_ROWS*2;


}
