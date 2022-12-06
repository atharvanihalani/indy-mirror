package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Wrapper class for a single maze 'tile'. Nine tiles make up
 * a 'block.' Contains methods to set the tile color, position,
 * _____, _____
 */
public class MazeTile {

    private Pane gamePane;
    private boolean isWall; //stores whether this tile is a wall or not
    private Rectangle myTile;

    /**
     * constructor that initializes instance variables and sets up
     * the tile.
     * @param gamePane the main game pane. adds tiles directly to it.
     */
    public MazeTile(Pane gamePane) {
        this.gamePane = gamePane;
        this.myTile = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);

        this.gamePane.getChildren().add(this.myTile);
    }

    /**
     * overloaded constructor cuz we thought we'd have some fun with indy :)
     * lets us initialize a maze tile with more specification directly
     * helps w efficiency
     * @param gamePane same as before
     * @param isWall ditto
     * @param xPos ._.
     * @param yPos bruh
     */
    public MazeTile(Pane gamePane, boolean isWall, int xPos, int yPos) {
        this.gamePane = gamePane;
        this.isWall = isWall;
        this.myTile = new Rectangle(Constants.TILE_SIZE, Constants.TILE_SIZE);

        this.gamePane.getChildren().add(this.myTile);
        this.setIsWall(this.isWall);
        this.setTilePos(xPos, yPos);
    }


    public void setIsWall(boolean isWall) {
        this.isWall = isWall;

        if (this.isWall) {
            this.colorTile(Constants.WALL_COLOR);
        } else {
            this.colorTile(Constants.WAY_COLOR);
        }
    }

    public boolean getIsWall() {
        return this.isWall;
    }

    /**
     * Helper method to graphically set the position of a tile
     * @param xPos
     * @param yPos
     */
    public void setTilePos(int xPos, int yPos) {
        this.myTile.setX(xPos);
        this.myTile.setY(yPos);
    }

    /**
     * helper method to set/change the color of a tile
     * @param color
     */
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





}
