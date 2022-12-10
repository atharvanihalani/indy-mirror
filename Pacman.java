package indy;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Pacman {

    private StackPane pacPane;
    private Pane gamePane;
    public Pacman(Pane gamePane) {
        this.gamePane = gamePane;
        this.pacPane = new StackPane();

        this.setupPac();

        this.pacPane.setLayoutX(50);
        this.pacPane.setLayoutY(50);
        this.pacPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderStroke.THIN)));
        System.out.println(this.pacPane.getBoundsInParent());
        System.out.println(this.pacPane.getLayoutBounds());
        System.out.println(this.pacPane.getBoundsInLocal());
    }

    private void setupPac() {
        double openMouth = 5.0;
        //TODO make these constants
        Circle pacCircle = new Circle(12, Color.YELLOW);
        Polygon pacMouth = new Polygon(0, 10-openMouth, 0, 10+openMouth, 15, 10);
        pacMouth.setFill(Color.BLACK);

        this.pacPane.setMaxWidth(24);
        this.pacPane.setMaxHeight(24);

        this.pacPane.getChildren().addAll(pacCircle, pacMouth);
        this.gamePane.getChildren().add(this.pacPane);
//        pacCircle.setCenterX(100);
//        pacCircle.setCenterY(100);
//        pacMouth.setLayoutX(100);
//        pacMouth.setLayoutY(100);
    }

    /*
    method to graphically create pacman
        can honestly just copy-paste cartoon's code here
     */

    /*
    method to position pacman in the maze
        place it at the centre tile of the first block
     */

    /*
    method to handle (arrow-)key-input
        well, duh
     */

    /*
    method to move pacman (copy-paste from cartoon)
        continuously checks if it's colliding with a wall (timeline updated every moment)
            if no, continue moving in same direction
            if yes, move a tiny bit away, and stop
     */

    /*
    helper method to return pacman's position in the array
        gets pacman's current x and y pos
        uses that to find out the current position in NESTED 2d array
            divide by blocksize (& floor) to calc outer array coordinates
            calc mod when dividing by blocksize, THEN divide by tilesize (& floor) to calc nested array coords.
     */

    /*
    method to check if pacman is colliding w a wall
        //updated continuously by timeline
        //returns a boolean
        this.getPos()
        checks if the current TILE isWall or not
     */

    /*
    helper method to rotate pacman
        called when moving in a diff direction
     */

}
