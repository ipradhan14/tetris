package oldtetris;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class Tile extends Rectangle {
    private int offsetX;
    private int offsetY;
    private int xPos;
    private int yPos;

    public Tile(double x, double y, Paint value) {
        super(x, y, 20, 20);
        setArcWidth(3);
        setArcHeight(3);
        setStrokeType(StrokeType.INSIDE);
        setStroke(Color.WHITE);
        setStrokeWidth(2);
        setFill(value);
    }
    public int getOffsetX() {
        return offsetX;
    }
    public int getOffsetY() {
        return offsetY;
    }
    public void setOffset(int x, int y) {
        offsetX = x;
        offsetY = y;
    }
}
