package indy;

public class Pacman {

    public Pacman() {

        //createPacman
        //placePacman
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
    method to check if pacman is colliding w a wall
        //updated continuously by timeline
        //returns a boolean
        gets pacman's current x and y pos
        uses that to find out the current position in NESTED 2d array
            divide by blocksize (& floor) to calc outer array coordinates
            calc mod when dividing by blocksize, THEN divide by tilesize (& floor) to calc nested array coords.
        checks if the current TILE isWall or not
     */

    /*
    helper method to rotate pacman
        called when moving in a diff direction
     */

}
