class TetrisGame {

  private Board board;
  private Brick currentBrick;
  private Brick nextBrick;
  private Position currentPos;
  private int currentOrient;
  private int currentColor;
  private int nextColor;
  private int score;
  private static final int NUMBER_OF_COLORS = 4;

  TetrisGame() {
    board = new Board();
    nextBrick = Brick.getRandomBrick();
    nextColor = Math.floorMod((currentColor + 1), NUMBER_OF_COLORS);
    createNewBrick();
  }

  void tick() {
    Position shift = new Position(1, 0);
    if (hasCollision(shift, 0)) {
      board.addTiles(currentBrick.getPositions(currentOrient),
          currentPos,
          currentColor);
      createNewBrick();
      score += 50;
    } else {
      currentPos = currentPos.translate(shift);
    }
  }

  private void createNewBrick() {
    currentBrick = nextBrick;
    nextBrick = Brick.getRandomBrick();
    currentColor = nextColor;
    nextColor = Math.floorMod((currentColor + 1), NUMBER_OF_COLORS);
    currentPos = new Position(0, (Board.NUM_COLUMNS - 1) / 2);
    currentOrient = 0;
  }

  private boolean hasCollision(Position p, int r) {
    int newOrient = Math.floorMod((currentOrient + r), currentBrick.getNumRotations());
    for (Position offset : currentBrick.getPositions(newOrient)) {
      Position newPos = currentPos.translate(offset).translate(p);
      if (!isValid(newPos)) {
        return true;
      }
    }
    return false;
  }

  int[] deleteRows() {
    int[] rows = board.getFullRows();
    for (int row : rows) {
      board.deleteRow(row);
    }
    if (rows.length == 1) {
      score += 250;
    } else if (rows.length == 2) {
      score += 750;
    } else if (rows.length == 3) {
      score += 2000;
    } else if (rows.length == 4) {
      score += 5000;
    }
    return rows;
  }

  Board getDisplay() {
    return board.copyAndAdd(currentBrick.getPositions(currentOrient), currentPos, currentColor);
  }

  boolean isGameOver() {
    return hasCollision(new Position(0, 0), 0);
  }

  boolean handleInput(Position p, int r) {
    if (!hasCollision(p, r)) {
      currentPos = currentPos.translate(p);
      currentOrient = Math.floorMod((currentOrient + r), currentBrick.getNumRotations());
      return true;
    }
    return false;
  }

  private boolean isValid(Position p) {
    return (p.getCol() >= 0)
        && (p.getCol() < board.getCols())
        && (p.getRow() >= 0)
        && (p.getRow() < board.getRows())
        && (board.getTile(p) == Tile.AIR);
  }

  Position[] getNextBrickPositions() {
    return nextBrick.getPositions(0);
  }

  int getNextColor() {
    return nextColor;
  }

  int getScore() {
    return score;
  }

}
