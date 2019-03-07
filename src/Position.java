class Position {

  private final int row;
  private final int col;

  Position(int r, int c) {
    row = r;
    col = c;
  }

  int getRow() {
    return row;
  }

  int getCol() {
    return col;
  }

  Position translate(Position d) {
    return new Position(row + d.getRow(), col + d.getCol());
  }
}
