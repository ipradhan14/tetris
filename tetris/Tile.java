package tetris;

public class Tile {

    public static final Tile AIR = new Tile(-1);

    private int color;

    public Tile(int c) {
        color = c;
    }

    public int getColor() { return color; }


}
