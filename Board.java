public class Board {

  private Tile[][] tileArray;
  private final int BOARD_SIZE = 8;

  private String[][] pieces = {
    {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"},
    {"bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP"},
    {"ee", "ee", "ee", "ee", "ee", "ee", "ee", "ee"},
    {"ee", "ee", "ee", "ee", "ee", "ee", "ee", "ee"},
    {"ee", "ee", "ee", "ee", "ee", "ee", "ee", "ee"},
    {"ee", "ee", "ee", "ee", "ee", "ee", "ee", "ee"},
    {"wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP"},
    {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"}
  };

  public Board() {
    tileArray = new Tile[BOARD_SIZE][BOARD_SIZE];
    String color = "";
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int column = 0; column < BOARD_SIZE; column++) {
        if ((row + column) % 2 == 0) {
          color = "w";
        } else {
          color = "b";
        }
        Tile toAdd = new Tile(row, column, color);
        this.tileArray[row][column] = toAdd;
      }
    }
  }

  public void arrangePiecesWhite() {
    for (int row = 0; row < BOARD_SIZE; row++) {
      for (int column = 0; column < BOARD_SIZE; column++) {
        String pieceType = Character.toString(this.pieces[row][column].charAt(1));
        String color = Character.toString(this.pieces[row][column].charAt(0));
        Piece toAdd = new Piece(pieceType, color, row, column);
        this.tileArray[row][column].setPiece(toAdd);
      }
    }
  }

  public Tile[][] getTileArray() {
    return this.tileArray;
  }

}
