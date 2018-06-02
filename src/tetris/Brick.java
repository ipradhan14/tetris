package tetris;

public class Brick {


    // make sure new bricks don't spawn offscreen
//    public static final String[] TYPES = {"T", "J", "Z", "O", "S", "L", "I"};
    private static final int[][][] T_POS = {
            {{0, -1}, {0, 0}, {0, 1}, {1, 0}},
            {{-1, 0}, {0, 0}, {1, 0}, {0, -1}},
            {{0, 1}, {0, 0}, {0, -1}, {-1, 0}},
            {{1, 0}, {0, 0}, {-1, 0}, {0, 1}}
    };
    private static final int[][][] O_POS = {{{0, 0}, {0, 1}, {1, 0}, {1, 1}}};
    private static final int[][][] I_POS = {
            {{0, 0}, {1, 0}, {2, 0}, {3, 0}},
            {{1, -1}, {1, 0}, {1, 1}, {1, 2}}
    };
    private static final int[][][] J_POS = {
            {{0, -1}, {1, -1}, {1, 0}, {1, 1}},
            {{0, 1}, {0, 0}, {1, 0}, {2, 0}},
            {{2, 1}, {1, 1}, {1, 0}, {1, -1}},
            {{0, 0}, {1, 0}, {2, 0}, {2, -1}}
    };
    private static final int[][][] L_POS = {
            {{0, 1}, {1, -1}, {1, 0}, {1, 1}},
            {{0, 0}, {1, 0}, {2, 0}, {2, 1}},
            {{1, -1}, {1, 1}, {1, 0}, {2, -1}},
            {{0, -1}, {0, 0}, {1, 0}, {2, 0}}
    };
    private static final int[][][] S_POS = {
            {{1, 0}, {1, 1}, {2, 0}, {2, -1}},
            {{0, 0}, {1, 0}, {1, 1}, {2, 1}}
    };
    private static final int[][][] Z_POS = {
            {{0, 0}, {0, 1}, {1, 1}, {1, 2}},
            {{0, 1}, {1, 1}, {1, 0}, {2, 0}}
    };

    private static final Brick[] BRICKS = {
            new Brick(L_POS),
            new Brick(Z_POS),
            new Brick(T_POS),
            new Brick(O_POS),
            new Brick(I_POS),
            new Brick(J_POS),
            new Brick(S_POS)
    };

    private Position[][] rotatedPositions;

    private Brick(int[][][] posList) {
        rotatedPositions = new Position[posList.length][posList[0].length];
        for (int i = 0; i < posList.length; i++) {
            for (int j = 0; j < posList[i].length; j++) {
                rotatedPositions[i][j] = new Position(posList[i][j][0], posList[i][j][1]);
            }
        }
    }

    public static Brick getRandomBrick() {
        int i = (int) (Math.random() * BRICKS.length);
        return BRICKS[i];
    }

    public Position[] getPositions(int r) {
        return rotatedPositions[r];
    }
    public int getNumRotations() { return rotatedPositions.length; }
}
