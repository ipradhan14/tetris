package tetris;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private Tile[][] tiles;

    public Board() {
        tiles = new Tile[20][10];
        for (Tile[] row : tiles) {
            for (int i = 0; i < row.length; i++) row[i] = Tile.AIR;
        }
    }
    private Board(Tile[][] otherTiles) {
        tiles = new Tile[20][10];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) tiles[i][j] = otherTiles[i][j];
        }
    }
    public void addTiles(Position[] positions, Position offset, int color) {
        for (Position p : positions) setTile(p.translate(offset), color);
    }
    private void setTile(Position p, int color) {
        tiles[p.getRow()][p.getCol()] = new Tile(color);
    }
    public Tile getTile(Position p) {
        return tiles[p.getRow()][p.getCol()];
    }
    public Board copyAndAdd(Position[] positions, Position offset, int color) {
        Board b = new Board(tiles);
        b.addTiles(positions, offset, color);
        return b;
    }
    public int getRows() { return tiles.length; }
    public int getCols() {return tiles[0].length; }
    public void printBoard() {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                System.out.print((t == Tile.AIR) ? " " : "O");
            }
            System.out.println();
        }
    }

    public int[] getFullRows() {
        List<Integer> rows = new ArrayList<Integer>();
        for (int i = 0; i < tiles.length; i++) {
            boolean isFull = true;
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == Tile.AIR) isFull = false;
            }
            if (isFull) rows.add(i);
        }

        int[] rowsInt = new int[rows.size()];
        for (int i = 0; i < rows.size(); i++) {
            rowsInt[i] = rows.get(i);
        }
        return rowsInt;

    }

    public void deleteRow(int r) {
        for (int i = r; i >= 0; i--) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (i == 0) {
                    tiles[i][j] = Tile.AIR;
                } else {
                    tiles[i][j] = tiles[i - 1][j];
                }
            }
        }
    }

}
