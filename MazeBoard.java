package indy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * oooo things get pretty interesting over here. this class does most of the
 * logical heavy lifting in this project. It contains methods to randomly set
 * up the entire maze, the border, the first and last blocks; accessor methods
 * to check whether a tile is a path, or whether the game is over; and helper
 * methods to update the game, handle key input, check the index of coordinates
 * in both the block and tile array, and methods to set up the end screen in
 * different scenarios.
 * design could be polished, but pretty solid class overall.
 */
public class MazeBoard {

    private Pane gamePane;
    private MazeBlock[][] blockArray;
    private Pacman pacman;
    private RedSus redSus;
    private boolean gameOver; //boolean indicating if the game is over or not

    public MazeBoard(Pane gamePane) {
        this.gamePane = gamePane;
        this.blockArray = new MazeBlock[Constants.NUM_ROWS][Constants.NUM_COLS];
        this.gameOver = false;

        this.setupBorder();
        this.setupFirstBlock();
        this.setupExit();
        this.setupMaze();

        //these are set up last in order to show up on top of the gamepane
        this.pacman = new Pacman(this.gamePane, this);
        this.redSus = new RedSus(this.gamePane, this);



    }

    /**
     * Method that graphically sets up the borders surrounding the maze
     * Doesn't store em logically cuz I don't need to know about them
     * once they've been set up.
     */
    private void setupBorder() {

        //loop that sets up both horizontal walls simultaneously
        for (int i = 0; i <= Constants.NUM_COLS *3; i++) {
            MazeTile topRow = new MazeTile(this.gamePane, true, i*Constants.TILE_SIZE, 0);
            topRow.resetColor();
            MazeTile bottomRow = new MazeTile(this.gamePane, true, (i+1)*Constants.TILE_SIZE,
                    ((Constants.NUM_ROWS*3)+1)*Constants.TILE_SIZE);
            bottomRow.resetColor();
        }

        //loop that sets up both vertical walls simultaneously
        for (int i = 0; i <= Constants.NUM_ROWS*3; i++) {
            MazeTile leftCol = new MazeTile(this.gamePane, true, 0, (i+1)*Constants.TILE_SIZE);
            leftCol.resetColor();
            MazeTile rightCol = new MazeTile(this.gamePane, true,
                    ((Constants.NUM_COLS *3)+1)*Constants.TILE_SIZE,
                    i*Constants.TILE_SIZE);
            rightCol.resetColor();
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
     * leading to this block. If a path exists, then regeneration isn't
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

                //ensures we don't replace the first or last block
                if ((i == 0 && j == 0) || (i == Constants.NUM_ROWS - 1 &&
                        j == Constants.NUM_COLS-1)) {
                    continue;
                }

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
                    if (j == 0) {
                        j = Constants.NUM_COLS - 1;
                        i--;
                    } else {
                        j--;
                    }
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

    /**
     * This is the second helper method for setupMaze(). It checks whether the alg
     * should backtrack and regenerate one of the previous blocks (ie. either above
     * or to the left) leading to the current one. If there is a connecting path on
     * either of the previous blocks, then backtracking isn't required. If there
     * isn't a path, then the alg randomly chooses which one to backtrack to.
     * <p>
     * Again, this is to ensure that the maze is globally consistent
     *
     * @param outerConstraints the array of outer constraints for that particular
     *                         block
     * @param i the row-index of the block
     * @param j the column index of the block
     * @return An arraylist with backtracking information. The first index has a
     * boolean indicating whether backtracking should occur or not. If it should,
     * it also returns the i and j values that the nested loop should return to.
     */
    private ArrayList<Object> checkBacktracking(boolean[] outerConstraints, int i, int j) {

        ArrayList<Object> backtrackingInfo = new ArrayList<>();

        //if there is a connecting path on either the block above, or to
        //the left, then backtracking isn't required
        if (outerConstraints[0] || outerConstraints[3]) {
            backtrackingInfo.add(false);
            return backtrackingInfo;
        }
        backtrackingInfo.add(true);

        //randomly chooses a direction to backtrack in
        boolean whereToBacktrack;
        switch ((int) Math.floor(Math.random()*2)) {
            case 0:
                whereToBacktrack = true;
                break;
            case 1:
                whereToBacktrack = false;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +
                        (int) Math.floor(Math.random() * 2));
        }

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

        backtrackingInfo.add(i);
        backtrackingInfo.add(j);
        return backtrackingInfo;
    }

    /**
     * Yet another helper method for setupMaze(). This semi-randomly generates
     * one of the five blocks and adds them to the current slot in the 2d array.
     * It also simultaneously returns the block's constraints for that particular
     * orientation. The blocks have a range of possibilities for being generated.
     * This was done in order to balance gameplay, and not make the maze too easy.
     * @param i row index of this current slot
     * @param j column index of this current slot
     * @return the 'inner constraints' of that particular block, at that particular
     * orientation
     */
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

    /**
     * Nope, still not the last helper method for setupMaze(). This checks if
     * the top & left outer constraints match with the corresponding inner
     * constraints of the block.
     * <p>
     * The logic is such that, if there is an external path, there ALWAYS has
     * to be an internal path leading to it. However, an external wall can be
     * matched with either an internal wall OR path. Again, this ensures local
     * solvability.
     * @param outerConstraints self-explanatory
     * @param innerConstraints ditto
     * @param isLast used for an annoying edge case. The second last block in
     *               the array has to ensure it's also connected to the block
     *               to its right (ie. the constant exitTile). Hence this boolean
     *               indicates whether the current block is the second-last in
     *               the array (ie. the last one being generated).
     * @return boolean indicating whether the constraints match
     */
    private boolean checkConstraintsMatch(boolean[] outerConstraints,
                                       boolean[] innerConstraints, boolean isLast) {
        boolean constraintsMatch = true;
        if (outerConstraints[0] && !innerConstraints[0]) {
            constraintsMatch = false;
        }
        if (outerConstraints[3] && !innerConstraints[3]) {
            constraintsMatch = false;
        }

        //also checks constraints for the block to the right if it's the last block
        if (isLast && outerConstraints[1] && !innerConstraints[1]) {
            constraintsMatch = false;
        }
        return constraintsMatch;
    }

    /**
     * Helper method for a helper method rip. This returns whether a specific
     * tile in a specific block is a 'way' or not. The actual method is delegated
     * to MazeBlock cuz better design (low coupling?)
     * Also, it returns false if the block hasn't been instantiated.
     *
     * @param blArrRow block array row index
     * @param blArrCol block array column index
     * @param tlArrRow tile array row index (within that specific block)
     * @param tlArrCol tile array column index
     * @return boolean indicating if the tile is a path/way or a wall
     */
    public boolean getIsXWay(int blArrRow, int blArrCol, int tlArrRow, int tlArrCol) {
        if (this.blockArray[blArrRow][blArrCol] != null) {
            return this.blockArray[blArrRow][blArrCol].isTileWay(tlArrRow, tlArrCol);
        } else {
            return false;
        }
    }


    /**
     * Helper method that updates the game state every instant. It's always checking
     * for any collision bw pacman and the ghost. It delegates everything else to
     * those two respective classes.
     */
    public void updateBoard() {
        this.pacman.updatePacman();
        this.redSus.updateSus();
        this.checkCollision();
        this.updateMazeLighting();
    }

    /**
     * Simply passes on the input to pacman's keyhandler method
     * @param keyCode keycode
     */
    public void keyHandler(KeyCode keyCode) {
        this.pacman.keyHandler(keyCode);
    }

    /**
     * Helper method that takes in a pair of coordinates, and returns the
     * corresponding index in the nested 2D array.
     * @param coords array of size 2 with the coordinates of a certain
     *               point (x, y)
     * @return an integer array of size 4 with the ordered indexes of
     * both 2d arrays. note the y index is calculated first because the
     * 2d arrays are in row-major order
     */
    public int[] checkPosInArray(double[] coords) {

        //external (block) array coordinates
        int[] arraysIndex = new int[4];
        arraysIndex[0] = (int) Math.floor((coords[1] - Constants.TILE_SIZE) /
                (Constants.TILE_SIZE*3));
        arraysIndex[1] = (int) Math.floor((coords[0] - Constants.TILE_SIZE) /
                (Constants.TILE_SIZE*3));

        double tempXVar = (coords[0] - Constants.TILE_SIZE) % (Constants.TILE_SIZE*3);
        double tempYVar = (coords[1] - Constants.TILE_SIZE) % (Constants.TILE_SIZE*3);

        //internal (tile) array coordinates
        arraysIndex[2] = (int) Math.floor(tempYVar / Constants.TILE_SIZE);
        arraysIndex[3] = (int) Math.floor(tempXVar / Constants.TILE_SIZE);

        return arraysIndex;
    }

    /**
     * Accessor method that returns whether the game is over or not.
     * @return boolean
     */
    public boolean getGameOver() {
        return this.gameOver;
    }


    /**
     * Method called when the timer has run out. Adds an end-screen image
     * and sets game over to be true
     */
    public void timeUp() {
        Image timesUp = new Image("./indy/timesup.png", Constants.SCENE_WIDTH,
                Constants.SCENE_HEIGHT - Constants.QUIT_PANE_HEIGHT -
                        Constants.TIMER_PANE_HEIGHT, false, true);
        ImageView timesUpView = new ImageView(timesUp);
        this.gamePane.getChildren().add(timesUpView);
        this.gameOver = true;
    }

    /**
     * Method called when pacman reaches the exit and wins. Adds an image
     * and sets the game to be over.
     */
    public void pacDub() {
        Image dubScreen = new Image("./indy/paccywinscreen.png", Constants.SCENE_WIDTH,
                Constants.SCENE_HEIGHT - Constants.QUIT_PANE_HEIGHT -
                        Constants.TIMER_PANE_HEIGHT, false, true);
        ImageView dubScreenView = new ImageView(dubScreen);
        this.gamePane.getChildren().add(dubScreenView);
        this.gameOver = true;
    }

    /**
     * Method that constantly checks for a collision bw pacman and blinky.
     * If it detects one, it adds an end screen and sets to game to be over.
     */
    public void checkCollision() {
        int[] pacPos = this.pacman.getPosInArray();
        int[] redPos = this.redSus.getPosInArray();

        if (Arrays.equals(pacPos, redPos)) {
            System.out.println("dead ass. deadass.");
            Image killScreen = new Image("./indy/amogushaha.png", Constants.SCENE_WIDTH,
                    Constants.SCENE_HEIGHT - Constants.QUIT_PANE_HEIGHT -
                            Constants.TIMER_PANE_HEIGHT, false, true);
            ImageView deadView = new ImageView(killScreen);
            this.gamePane.getChildren().add(deadView);
            this.gameOver = true;
        }
    }


    //TODO SHOULD BE called every time a helper method detects that the tile has changed

    /**
     * This calls two methods.
     * The first sets a 7x7 box of tiles (centred around pacman) to
     * be invisible.
     * The second sets a 5x5 box of tiles (centred around pacman) to
     * be visible.
     * The effect is, that a visibility box follows pacman along with
     * its movement.
     * Note: both methods make a box that stops at the border. ie. the
     * border remains visible no matter where pacman is.
     */
    public void updateMazeLighting() {
        this.setLightingBlack();
        this.setLightingVisible();
    }

    public void setLightingVisible() {

        int[] pacPos = this.pacman.getPosInArray();

        int[] initialPacPos = new int[4];
        System.arraycopy(pacPos, 0, initialPacPos, 0, 4);
        int[] finalPacPos = new int[4];
        System.arraycopy(pacPos, 0, finalPacPos, 0, 4);

        if (pacPos[0] == 0) {
            initialPacPos[2] = 0;

            if (finalPacPos[2] == 0) {
                finalPacPos[2] = 2;
            } else {
                finalPacPos[0]++;
                finalPacPos[2]--;
            }
        } else if (pacPos[0] == Constants.NUM_ROWS - 1) {
            if (initialPacPos[2] == 2) {
                initialPacPos[2] = 0;
            } else {
                initialPacPos[0]--;
                initialPacPos[2]++;
            }

            finalPacPos[2] = 2;
        } else {
            if (initialPacPos[2] == 2) {
                initialPacPos[2] = 0;
            } else {
                initialPacPos[0]--;
                initialPacPos[2]++;
            }

            if (finalPacPos[2] == 0) {
                finalPacPos[2] = 2;
            } else {
                finalPacPos[0]++;
                finalPacPos[2]--;
            }
        }


        if (pacPos[1] == 0) {
            initialPacPos[3] = 0;

            if (finalPacPos[3] == 0) {
                finalPacPos[3] = 2;
            } else {
                finalPacPos[1]++;
                finalPacPos[3]--;
            }
        } else if (pacPos[1] == Constants.NUM_COLS - 1) {
            if (initialPacPos[3] == 2) {
                initialPacPos[3] = 0;
            } else {
                initialPacPos[1]--;
                initialPacPos[3]++;
            }

            finalPacPos[3] = 2;
        } else {
            if (initialPacPos[3] == 2) {
                initialPacPos[3] = 0;
            } else {
                initialPacPos[1]--;
                initialPacPos[3]++;
            }

            if (finalPacPos[3] == 0) {
                finalPacPos[3] = 2;
            } else {
                finalPacPos[1]++;
                finalPacPos[3]--;
            }
        }

        int[] currentPacPos = new int[4];
        System.arraycopy(initialPacPos, 0, currentPacPos, 0, 4);

        int horizontalLength = 3*(finalPacPos[1] - initialPacPos[1]);
        horizontalLength = horizontalLength + (finalPacPos[3] - initialPacPos[3]) + 1;
        int verticalLength = 3*(finalPacPos[0] - initialPacPos[0]);
        verticalLength = verticalLength + (finalPacPos[2] - initialPacPos[2]) + 1;

        for (int i = 0; i < horizontalLength*verticalLength; i++) {

            System.out.println("iteration count light: " + i);

            int[] tempTileArray = new int[]{currentPacPos[2], currentPacPos[3]};
            this.blockArray[currentPacPos[0]][currentPacPos[1]].setTilesVisibility(tempTileArray, true);

            currentPacPos = this.getNextLightingTile(currentPacPos, initialPacPos, finalPacPos);
        }
    }

    public void setLightingBlack() {
        /*
        set all blocks to black at the setup

        illuminate the 5x5 block grid around pacman & ensure that the border
        square surrounding the 7x7 is all black
         */

        int[] pacPos = this.pacman.getPosInArray();

        int[] initialPacPos = new int[4];
        System.arraycopy(pacPos, 0, initialPacPos, 0, 4);
        int[] finalPacPos = new int[4];
        System.arraycopy(pacPos, 0, finalPacPos, 0, 4);

        if (pacPos[0] == 0) {
            initialPacPos[2] = 0;
            finalPacPos[0]++;
        } else if (pacPos[0] == Constants.NUM_ROWS - 1) {
            initialPacPos[0]--;
            finalPacPos[2] = 2;
        } else {
            initialPacPos[0]--;
            finalPacPos[0]++;
        }

        if (pacPos[1] == 0) {
            initialPacPos[3] = 0;
            finalPacPos[1]++;
        } else if (pacPos[1] == Constants.NUM_COLS - 1) {
            initialPacPos[1]--;
            finalPacPos[3] = 2;
        } else {
            initialPacPos[1]--;
            finalPacPos[1]++;
        }

        int[] currentPacPos = new int[4];
        System.arraycopy(initialPacPos, 0, currentPacPos, 0, 4);
        /*
        at this point, we have three arrays.
        initialPacPos stores the coordinates of the initial tile the next loop
        will start iterating from.
        finalPacPos stores the coordinates of the final tile the loop will be
        iterating till.
        currentPacPos stores the constantly changing value of the tile while the
        loop iterates through them all.
         */

        int horizontalLength = 3*(finalPacPos[1] - initialPacPos[1]);
        horizontalLength = horizontalLength + (finalPacPos[3] - initialPacPos[3]) + 1;
        int verticalLength = 3*(finalPacPos[0] - initialPacPos[0]);
        verticalLength = verticalLength + (finalPacPos[2] - initialPacPos[2]) + 1;

        for (int i = 0; i < horizontalLength*verticalLength; i++) {

            System.out.println("iteration count dark: " + i);

            int[] tempTileArray = new int[]{currentPacPos[2], currentPacPos[3]};
            this.blockArray[currentPacPos[0]][currentPacPos[1]].setTilesVisibility(tempTileArray, false);

            currentPacPos = this.getNextLightingTile(currentPacPos, initialPacPos, finalPacPos);
        }
    }

    private int[] getNextLightingTile(int[] currentArray, int[] initialArray, int[] finalArray) {

        int[] nextArray = new int[4];
        System.arraycopy(currentArray, 0, nextArray, 0, nextArray.length);

        //checks if it's at the end of a row
        if ((nextArray[1] == finalArray[1]) && (nextArray[3] == finalArray[3])) {
            nextArray[1] = initialArray[1];
            nextArray[3] = initialArray[3];

            //increment row position while checking for block border
            if (nextArray[2] == 2) {
                nextArray[0]++;
                nextArray[2] = 0;
            } else {
                nextArray[2]++;
            }
            return nextArray;
        }

        //goes to the next column, checking for the border of a block
        if (nextArray[3] == 2) {
            nextArray[1]++;
            nextArray[3] = 0;
        } else {
            nextArray[3]++;
        }
        return nextArray;

    }

}
