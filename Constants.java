package indy;

import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Constants {


    public static final int TILE_SIZE = 25;
    public static int NUM_ROWS;
    public static int NUM_COLS;
    public static final int QUIT_PANE_HEIGHT = 35;
    public static final int TIMER_PANE_HEIGHT = 50;

    public static int SCENE_WIDTH;  // pretty janky, not sure how 2 work around static binding
    public static int SCENE_HEIGHT;

    public static final Color WALL_COLOR = Color.DARKGRAY.darker();
    public static final Color WAY_COLOR = Color.BLACK.brighter().brighter();
    public static final Duration UPDATE_GAME_EVERY = Duration.millis(1);
    public static final Double MOVE_SPEED = 0.07;
    public static final int PAC_RADIUS =  12;
    public static int TIMER_COUNT;


}
