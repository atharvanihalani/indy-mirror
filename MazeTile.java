package indy;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Wrapper class for a single maze 'tile'. Nine tiles make up
 * a 'block.' Contains methods to set the tile color, position,
 * _____, _____
 */
public class MazeTile {

    private Rectangle myTile;
    //instance var – is it a Wall or a Path; boolean

    public MazeTile() {
        this.myTile = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);
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
