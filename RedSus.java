package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class RedSus {

    private Pane gamePane;
    private Shape[] imposter;
    private Direction currentDirection;

    public RedSus(Pane gamePane) {
        this.gamePane = gamePane;
        this.imposter = new Shape[5];
        this.currentDirection = Direction.LEFT;

        this.constructCrewmate();
    }

    private void constructCrewmate() {
        double tilePosX = (Constants.NUM_COLS)*Constants.TILE_SIZE*3;
        double tilePosY = Constants.NUM_ROWS*Constants.TILE_SIZE*3;
        this.imposter[0] = new Rectangle(16, 8, Color.RED);
        this.imposter[1] = new Rectangle(6, 8, Color.RED);
        this.imposter[2] = new Rectangle(6, 8, Color.RED);
        this.imposter[3] = new Circle(8, Color.RED);
        this.imposter[4] = new Ellipse(6, 4);
        this.imposter[4].setFill(Color.LIGHTBLUE);

        for (Shape bodyPart : this.imposter) {
            bodyPart.setLayoutX(tilePosX);
            bodyPart.setLayoutY(tilePosY);
        }

        this.imposter[0].setLayoutY(this.imposter[0].getLayoutY() - 16);
        this.imposter[1].setLayoutY(this.imposter[1].getLayoutY() - 8);
        this.imposter[2].setLayoutY(this.imposter[2].getLayoutY() - 8);
        this.imposter[2].setLayoutX(this.imposter[2].getLayoutX() + 10);
        this.imposter[3].setLayoutY(this.imposter[2].getLayoutY() - 8);
        this.imposter[3].setLayoutX(this.imposter[3].getLayoutX() + 8);
        this.imposter[4].setLayoutY(this.imposter[4].getLayoutY() - 16);
        this.imposter[4].setLayoutX(this.imposter[4].getLayoutX() + 8);

        this.gamePane.getChildren().addAll(this.imposter);
    }

    public void updateSus() {
        this.vent(this.currentDirection);
    }


    private void vent(Direction direction) {
        switch (direction) {
            case RIGHT:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutX(bodyPart.getLayoutX() +
                            Constants.MOVE_SPEED);
                }
                break;
            case LEFT:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutX(bodyPart.getLayoutX() -
                            Constants.MOVE_SPEED);
                }
                break;
            case UP:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutY(bodyPart.getLayoutY() -
                            Constants.MOVE_SPEED);
                }
                break;
            case DOWN:
                for (Shape bodyPart : this.imposter) {
                    bodyPart.setLayoutY(bodyPart.getLayoutY() +
                            Constants.MOVE_SPEED);
                }
                break;
        }
    }

}
