package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Wrapper class for a single maze 'tile'. Nine tiles make up
 * a 'block.' Contains methods to set the tile color, position,
 * _____, _____
 */
public class MazeTile {

    private boolean isWall;
    private Rectangle myTile;

    public MazeTile(boolean isWall) {
        this.isWall = isWall;
        this.myTile = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);

        this.setupTile();
    }

    /**
     * Method that sets up the tile's initial color, and positions
     * it graphically
     */
    private void setupTile() {
        if (this.isWall) {
            this.colorTile(Color.DARKGRAY);
        } else {
            this.colorTile(Color.LIGHTGRAY);
        }

        //add tile to pane
    }

    public void colorTile(Color color) {
        this.myTile.setFill(color);
    }

    /*
    //called every step if the motion is discrete, otherwise called every second

    method to color the wall
        first check distance bw wall and pacman
        if that's greater than ___, setFill to grey
        else
            if isWall then setFill ___
            else if !isWall then setFill ___
     */

    //getter to check if the tile is a wall or not

    private void makeWall() {
        this.myTile.setFill(Color.DARKGRAY);
    }

    private void makeWay() {
        this.myTile.setFill(Color.LIGHTGRAY);
    }


}
