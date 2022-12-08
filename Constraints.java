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

        int rotateBy = rotateNum % 4;
        boolean[] tempConstraints = new boolean[4];
        System.arraycopy(blockConstraints, 0, tempConstraints, 0, 4);

        for (int i = 0; i < rotateBy; i++) {
            boolean constraintA = tempConstraints[0];
            boolean constraintB = tempConstraints[1];
            boolean constraintC = tempConstraints[2];
            boolean constraintD = tempConstraints[3];

            tempConstraints[0] = constraintD;
            tempConstraints[3] = constraintC;
            tempConstraints[2] = constraintB;
            tempConstraints[1] = constraintA;
        }

        return tempConstraints;

    }

}
