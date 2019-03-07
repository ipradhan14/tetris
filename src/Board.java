import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

  private Tile[][] tiles;

  static final int NUM_ROWS = 20;
  static final int NUM_COLUMNS = 10;

  Board() {
    tiles = new Tile[NUM_ROWS][NUM_COLUMNS];
    for (Tile[] row : tiles) {
      for (int i = 0; i < row.length; i++) {
        row[i] = Tile.AIR;
      }
    }
  }

  private Board(Tile[][] otherTiles) {
    tiles = new Tile[otherTiles.length][otherTiles[0].length];
    for (int i = 0; i < tiles.length; i++) {
      tiles[i] = Arrays.copyOf(otherTiles[i], otherTiles[i].length);
    }
  }

  void addTiles(Position[] positions, Position offset, int color) {
    for (Position p : positions) {
      setTile(p.translate(offset), color);
    }
  }

  private void setTile(Position p, int color) {
    tiles[p.getRow()][p.getCol()] = new Tile(color);
  }

  Tile getTile(Position p) {
    return tiles[p.getRow()][p.getCol()];
  }

  Board copyAndAdd(Position[] positions, Position offset, int color) {
    Board b = new Board(tiles);
    b.addTiles(positions, offset, color);
    return b;
  }

  int getRows() {
    return tiles.length;
  }

  int getCols() {
    return tiles[0].length;
  }

  public void printBoard() { // test method
    for (Tile[] row : tiles) {
      for (Tile t : row) {
        System.out.print((t == Tile.AIR) ? " " : "O");
      }
      System.out.println();
    }
  }

  int[] getFullRows() {
    List<Integer> rows = new ArrayList<>();
    for (int i = 0; i < tiles.length; i++) {
      boolean isFull = true;
      for (int j = 0; j < tiles[i].length; j++) {
        if (tiles[i][j] == Tile.AIR) {
          isFull = false;
        }
      }
      if (isFull) {
        rows.add(i);
      }
    }
    int[] rowsInt = new int[rows.size()];
    for (int i = 0; i < rows.size(); i++) {
      rowsInt[i] = rows.get(i);
    }
    return rowsInt;
  }

  void deleteRow(int r) {
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
