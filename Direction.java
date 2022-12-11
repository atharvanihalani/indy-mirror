package indy;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction getOpposite(Direction this) {
        switch (this) {
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                throw new IllegalArgumentException("bro's entered the third dimension");
        }
    }
}
