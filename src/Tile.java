final class Tile {

  static final Tile AIR = new Tile(-1);

  private final int color;

  Tile(int c) {
    color = c;
  }

  int getColor() {
    return color;
  }
}
