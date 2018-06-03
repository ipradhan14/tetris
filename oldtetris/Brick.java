package oldtetris;
import java.util.*;
import javafx.scene.paint.Color;

public class Brick {
    public static final int TILE_SIZE = 20;
    public static final int BOARD_WIDTH_TILES = 10;
    public static final int BOARD_HEIGHT_TILES = 500;
    public static final Color COLOR1 = Color.hsb(0, 1.0, 1.0);
    public static final Color COLOR2 = Color.hsb(100, 1.0, 1.0);
    public static final Color COLOR3 = Color.hsb(200, 1.0, 1.0);

    private List<Tile> tiles;
    private int xPos;
    private int yPos;
    private int orientation;
    private int type;
    private Color color;

    public Brick() {
        this((int)(Math.random() * 7));
    }

    public Brick(int myType) {
        tiles = new ArrayList<Tile>();
        xPos = ((int) (BOARD_WIDTH_TILES * Math.random())) * TILE_SIZE; // need to fix this so that tiles don't spawn partially off screen - brick width is dependent on type
        yPos = BOARD_HEIGHT_TILES * TILE_SIZE;
        orientation = (int) (Math.random() * 4);
        type = myType;
        color = COLOR1;

        switch (type) {
            case 0: // "T"
                tiles.add(new Tile(xPos, yPos, color));
                tiles.add(new Tile(xPos + TILE_SIZE, yPos, color));
                tiles.add(new Tile(xPos - TILE_SIZE, yPos, color));
                tiles.add(new Tile(xPos, yPos + TILE_SIZE, color));
                break;
        }

    }

    public Brick(ArrayList<Tile> myTiles, int myType) {
        tiles = myTiles;
        xPos = 0;
        yPos = 0;
        orientation = (int) (Math.random() * 4);
        type = myType;
    }

    //	public void rotate(int dir) { // 1 = 90 degrees cw; -1 = 90 degrees ccw
//		orientation = (orientation + dir) % 4;
//		for (Tile t: tiles) {
//			if (dir == 1) {
//
//			}
//		}
//	}
    public void down() {
        yPos -= TILE_SIZE;
        updatePos();
    }
    public void left() {
        xPos -= TILE_SIZE;
        updatePos();
    }
    public void right() {
        xPos += TILE_SIZE;
        updatePos();
    }
    public void updatePos() {

    }
}
