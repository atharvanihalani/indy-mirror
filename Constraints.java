package indy;

public enum Constraints {
    ; //TODO what rlly is this for??
    static boolean[] DEAD_BLOCK = {true, false, false, false};
    static boolean[] AYE_BLOCK = {true, false, true, false};
    static boolean[] ELL_BLOCK = {true, true, false, false};
    static boolean[] TEE_BLOCK = {true, true, false, true};
    static boolean[] PLUS_BLOCK = {true, true, true, true};

    public static boolean[] rotateBlock(boolean[] blockConstraints,
                                        int rotateNum) {

        boolean[] currentConstraints = blockConstraints;
        int rotateBy = rotateNum % 4;

        for (int i = 0; i < rotateBy; i++) {
            boolean firstConstraint = currentConstraints[0];
            currentConstraints[0] = currentConstraints[3];
            currentConstraints[3] = currentConstraints[2];
            currentConstraints[2] = currentConstraints[1];
            currentConstraints[1] = firstConstraint;
        }

        return currentConstraints;
    }

}
