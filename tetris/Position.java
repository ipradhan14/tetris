package tetris;

public class Position {
    private int row;
    private int col;
    public Position(int r, int c) {
        row = r;
        col = c;
    }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Position translate(Position d) {
        return new Position(row + d.getRow(), col + d.getCol());
    }
}
