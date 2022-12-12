package indy;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MazeBoard {

    private Pane gamePane;
    private MazeBlock[][] blockArray;
    private Pacman pacman;
    private boolean implementBacktracking;

    public MazeBoard(Pane gamePane) {
        this.gamePane = gamePane;
        this.blockArray = new MazeBlock[Constants.NUM_ROWS][Constants.NUM_COLS];
        this.implementBacktracking = false;

        this.setupBorder();
        this.setupFirstBlock();
        this.setupExit();
        this.setupMaze();

        this.pacman = new Pacman(this.gamePane, this);
    }

    public void keyHandler(KeyCode keyCode) {
        this.pacman.keyHandler(keyCode);
    }

    /**
     * Method that graphically sets up the borders surrounding the maze
     * Doesn't store em logically cuz I don't need to know about them
     * once they've been set up.
     */
    private void setupBorder() {

        //loop that sets up both horizontal walls simultaneously
        for (int i = 0; i <= Constants.NUM_COLS *3; i++) {
            new MazeTile(this.gamePane, true, i*Constants.TILE_SIZE, 0);
            new MazeTile(this.gamePane, true, (i+1)*Constants.TILE_SIZE,
                    ((Constants.NUM_ROWS*3)+1)*Constants.TILE_SIZE);
        }

        //loop that sets up both vertical walls simultaneously
        for (int i = 0; i <= Constants.NUM_ROWS*3; i++) {
            new MazeTile(this.gamePane, true, 0, (i+1)*Constants.TILE_SIZE);
            new MazeTile(this.gamePane, true,
                    ((Constants.NUM_COLS *3)+1)*Constants.TILE_SIZE,
                    i*Constants.TILE_SIZE);
        }
    }

    /**
     * Sets up the exit tile at the bottom right corner, and ensures
     * it's reachable.
     */
    private void setupExit() {
        MazeTile exitTile = new MazeTile(this.gamePane, false,
                ((Constants.NUM_COLS *3)+1)*Constants.TILE_SIZE,
                (Constants.NUM_ROWS*3-1)*Constants.TILE_SIZE);
        exitTile.colorTile(Color.GREEN.brighter());

//        note the NUM_ROWS and NUM_COLS arguments are 'switched; because
//        the array's been set up in row-major format
        this.blockArray[Constants.NUM_ROWS-1][Constants.NUM_COLS -1] =
                new TeeBlock(this.gamePane, Constants.NUM_COLS -1,
                        Constants.NUM_ROWS-1, 0);
    }

    /**
     * Sets up the same first block in the top left corner, ensuring that
     * pacman has a valid tile to spawn at every time
     */
    private void setupFirstBlock() {
        this.blockArray[0][0] = new EllBlock(this.gamePane, 0, 0, 1);

    }

    /**
     * Hoooo boy, buckle up; this is a spiiicy fuckin method. it's also
     * almost entirely deranged (like me). but at least it works (unlike me)
     * here's a brief, high-level overview of what it does. I'll be delving
     * into the details w helper-method-header & in-line comments.
     * <p>
     * In super broad strokes – this alg iterates over each 'slot' in the
     * MazeBoard (going left-to-right & top-to-bottom, in that order) and
     * semi-randomly generates a block at that slot.
     * <p>
     * In slightly narrower strokes, here's how it does that. The alg can
     * be explained in two parts:
     * A) At each iteration, the alg first checks if it needs to regenerate
     * a PREVIOUS block or not. It decides that by checking current paths
     * leading to this block. If a path exists, then regeneration is the
     * required. If a path does not exist, then the alg will regenerate one
     * of the blocks connected to this one. The purpose of this is to ensure
     * that the maze is always GLOBALLY solvable.
     * B) After an arbitrary number of backtracking iterations, we finally
     * have a path. Now, we randomly generate one of the five possible blocks
     * at one of their possible orientations in this current empty slot. The
     * alg then checks if this block is logically consistent (elaborated on
     * later) with the four surrounding it. This ensures that the maze is
     * always LOCALLY solvable.
     * <p>
     * Now let's get into the actual details :)
     */
    private void setupMaze() {

        for (int i = 0; i < Constants.NUM_ROWS; i++) {
            for (int j = 0; j < Constants.NUM_COLS; j++) {

                //TODO remove error detecting print lines

                //ensures we don't replace the first or last block
                if ((i == 0 && j == 0) || (i == Constants.NUM_ROWS - 1 &&
                        j == Constants.NUM_COLS-1)) {
                    continue;
                }

//                System.out.println(String.format("Currently iterating through " +
//                        "block [%s, %s] \r\n " +
//                        "random block: %s \r\n" +
//                        "random rotation: %s",
//                        i, j, randomBlockSwitch, randomRotation));

                /*
                This next bit checks backtracking based on the outer constraints.
                The helper method returns an arraylist, whose first item is a
                boolean stating whether backtracking is required or not.
                If backtracking is required, the arraylist contains two more
                pieces of information – the value of i and j. This changes the
                loop's iterator info, and goes to the block that's gotta be
                regenerated.
                Note: this block will always be behind the current one (either
                up or to the left), so the loop's gonna slowly come back round,
                regenerating every block on the way.
                 */
                boolean[] outerConstraints = this.getOuterConstraints(i, j);
                ArrayList<Object> backtrackingInfo =
                        this.checkBacktracking(outerConstraints, i, j);
                if ((boolean) backtrackingInfo.get(0)) {
                    i = (int) backtrackingInfo.get(1);
                    j = (int) backtrackingInfo.get(2);

                    //ensures we immediately backtrack & don't execute further code
                    continue;
                }

                boolean[] innerConstraints = this.tryRandomBlock(i, j);

                /*
                The next bit of code is used to store whether the current block
                being generated is the last possible one.
                This is passed into checkConstraintsMatch to deal with an edge
                case. It def feels kinda gimmicky, but I couldn't think of a
                much cleaner way around this.
                 */
                boolean isLast = false;
                if (i == Constants.NUM_ROWS - 1 && j == Constants.NUM_COLS - 2) {
                    isLast = true;
                }

                /*
                If the current block is not logically consistent with the ones
                surrounding it (determined by checkConstraintsMatch, then this
                code is executed. j (and maybe i) are decremented, so this loop
                merely runs again, generating another random block for this
                same slot in the board. Again, this is run an arbitrary number of
                times till a block fits.
                If a block is consistent, the code does nothing, and moves on
                to the next slot in the board.
                 */
                if (!checkConstraintsMatch(
                        outerConstraints, innerConstraints, isLast)) {
//                    System.out.println("lol chutiya, try again \r\n");
                    if (j == 0) {
                        j = Constants.NUM_COLS - 1;
                        i--;
                    } else {
                        j--;
                    }
                } else {
//                    System.out.println("block gen successful \r\n");
                }
            }
        }
    }

    /**
     * Helper method that returns a boolean array of four 'outer' constraints
     * for a certain block. Outer constraints contain info about whether the
     * four blocks surrounding this one have 'paths leading to it'.
     * <p>
     * Eg. whether the tile at [2, 1] (bottom-centre) for the block above it
     * is a path or wall. same for the tile at [1, 2] (right-centre) for the
     * block to the left of it. likewise for the other two blocks.
     * <p>
     * If there is a path in one of these directions, the constraint for that
     * direction is set to true. If the path doesn't exist, or the block isn't
     * yet generated, then the constraint is false (by default). The constraint
     * for the block above, is the first one in the array (ie. outerConstraints[0]).
     * The others are stored in clockwise ordinality.
     * @param i row index of the current block
     * @param j column index of the current block
     * @return array of four boolean constraints
     */
    private boolean[] getOuterConstraints(int i, int j) {

        boolean[] externalConstraints = new boolean[4];
        for (int k = 0; k < 4; k++) {
            externalConstraints[k] = false;
        }

        //checks for constraints above and below the block
        if (i == 0) {
            if (this.getIsXWay(i + 1, j, 0, 1)) {
                externalConstraints[2] = true;
            }
        } else if (i == (Constants.NUM_ROWS - 1)) {
            if (this.getIsXWay(i - 1, j, 2, 1)) {
                externalConstraints[0] = true;
            }
        } else {
            if (this.getIsXWay(i + 1, j, 0, 1)) {
                externalConstraints[2] = true;
            }
            if (this.getIsXWay(i - 1, j, 2, 1)) {
                externalConstraints[0] = true;
            }
        }

        //checks for constraints to the left and right of the block
        if (j == 0) {
            if (this.getIsXWay(i, j + 1, 1, 0)) {
                externalConstraints[1] = true;
            }
        } else if (j == (Constants.NUM_COLS - 1)) {
            if (this.getIsXWay(i, j - 1, 1, 2)) {
                externalConstraints[3] = true;
            }
        } else {
            if (this.getIsXWay(i, j + 1, 1, 0)) {
                externalConstraints[1] = true;
            }
            if (this.getIsXWay(i, j - 1, 1, 2)) {
                externalConstraints[3] = true;
            }
        }

        return externalConstraints;
    }


    private ArrayList<Object> checkBacktracking(boolean[] outerConstraints, int i, int j) {

        ArrayList<Object> backtrackingInfo = new ArrayList<>();

        boolean backtrack = true;
        if (outerConstraints[0] || outerConstraints[3]) {
            backtrack = false;
        }

        backtrackingInfo.add(backtrack);

        //System.out.println("backtracking? " + backtrack);

        boolean whereToBacktrack = true;
        switch ((int) Math.floor(Math.random()*2)) {
            case 0:
            case 1:
                whereToBacktrack = false;
        }

        //System.out.println("backtracking to: " + whereToBacktrack);

        if (backtrack) {
            if (j == 0) {
                j--;
                i--;
            } else if (j == 1) {
                if (whereToBacktrack) {
                    j = Constants.NUM_COLS - 1;
                    i--;
                } else {
                    j--;
                    i--;
                }
            } else {
                if (!whereToBacktrack && i != 0) {
                    j--;
                    i--;
                } else {
                    j = j - 2;
                }
            }
        }

        backtrackingInfo.add(i);
        backtrackingInfo.add(j);
        return backtrackingInfo;
    }

    private boolean[] tryRandomBlock(int i, int j) {

        int randomBlockSwitch = (int) Math.floor(Math.random()*30);
        int randomRotation = (int) Math.floor(Math.random()*4);

        boolean[] randomBlockConstraints;

        switch (randomBlockSwitch) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.DEAD_BLOCK, randomRotation);
                this.blockArray[i][j] = new DeadBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.AYE_BLOCK, randomRotation);
                this.blockArray[i][j] = new AyeBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.ELL_BLOCK, randomRotation);
                this.blockArray[i][j] = new EllBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.TEE_BLOCK, randomRotation);
                this.blockArray[i][j] = new TeeBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            case 29:
                randomBlockConstraints = Constraints.rotateBlock(
                        Constraints.PLUS_BLOCK, randomRotation);
                this.blockArray[i][j] = new PlusBlock(
                        this.gamePane, j, i, randomRotation);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + randomBlockSwitch);
        }

        return randomBlockConstraints;
    }

    private boolean checkConstraintsMatch(boolean[] outerConstraints,
                                       boolean[] innerConstraints, boolean isLast) {
        boolean constraintsMatch = true;
        if (outerConstraints[0] && !innerConstraints[0]) {
            constraintsMatch = false;
        }
        if (outerConstraints[3] && !innerConstraints[3]) {
            constraintsMatch = false;
        }
        if (isLast && outerConstraints[1] && !innerConstraints[1]) {
            constraintsMatch = false;
        }
        return constraintsMatch;
    }


    /**
     * Helper method that returns whether a specific tile in a specific block
     * is a 'way' or not. Returns false if the block hasn't been initialized.
     *
     * TODO better design would delegate this method to MazeBlock (low coupling)
     * @param blArrRow block array row index
     * @param blArrCol block array column index
     * @param tlArrRow tile array row index (within that specific block)
     * @param tlArrCol tile array column index
     * @return
     */
    public boolean getIsXWay(int blArrRow, int blArrCol, int tlArrRow, int tlArrCol) {

        if (blArrRow < 0) {
            throw new IllegalStateException("Block Array Row is " + blArrRow);
        }
        if (blArrCol < 0) {
            throw new IllegalStateException("Block Array Column is " + blArrCol);
        }

        if (this.blockArray[blArrRow][blArrCol] != null) {
            return !this.blockArray[blArrRow][blArrCol].getTileArray()[tlArrRow][tlArrCol].getIsWall();
        } else {
            return false;
        }
    }


    public void updateBoard() {
        this.pacman.updatePacman();
    }

}
