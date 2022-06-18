import java.util.ArrayList;

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

     public int getTotalPieceValue(String color) {
          int totalValue = 0;
          for (int row = 0; row < BOARD_SIZE; row++) {
               for (int column = 0; column < BOARD_SIZE; column++) {
                    if (!this.tileArray[row][column].getPiece().getType().equals("e")) {
                         if (this.tileArray[row][column].getPiece().getColor().equals(color)) {
                              totalValue += this.tileArray[row][column].getPiece().getValue();
                         }
                    }
               }
          }
          return totalValue;
     }

     public Tile[][] getTileArray() {
          return this.tileArray;
     }

     public String[][] getPieces() {
          return this.pieces;
     }

     public Piece getKing(String color) {
          for (int row = 0; row < BOARD_SIZE; row++) {
               for (int column = 0; column < BOARD_SIZE; column++) {
                    if (pieces[row][column].equals(color + "K")) {
                         return tileArray[row][column].getPiece();
                    }
               }
          }
          return null;
     }

     public ArrayList<Tile> getPathBetween(Tile tileOne, Tile tileTwo) {
          ArrayList<Tile> tilesBetween = new ArrayList<Tile>();
          if (Math.abs(tileTwo.getRow() - tileOne.getRow()) == Math.abs(tileTwo.getColumn() - tileOne.getColumn())) {
               //diagonal
               if(tileOne.getRow() > tileTwo.getRow() && tileOne.getColumn() > tileTwo.getColumn()) {
                    //down right
                    for (int i = 1; i < Math.abs(tileOne.getColumn() - tileTwo.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[tileTwo.getRow() + i][tileTwo.getColumn() + i]);
                    }
               } else if (tileOne.getRow() > tileTwo.getRow() && tileOne.getColumn() < tileTwo.getColumn()) {
                    //down left
                    for (int i = 1; i < Math.abs(tileOne.getColumn() - tileTwo.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[tileTwo.getRow() + i][tileTwo.getColumn() - i]);
                    }

               } else if(tileOne.getRow() < tileTwo.getRow() && tileOne.getColumn() > tileTwo.getColumn()) {
                    //up right
                    for (int i = 1; i < Math.abs(tileOne.getColumn() - tileTwo.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[tileTwo.getRow() - i][tileTwo.getColumn() + i]);
                    }

               } else {
                    //up left
                    for (int i = 1; i < Math.abs(tileOne.getColumn() - tileTwo.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[tileTwo.getRow() - i][tileTwo.getColumn() - i]);
                    }
               }

          } else if (tileOne.getRow() == tileTwo.getRow()) {
               //tiles connected horizontally
               if(tileOne.getColumn() > tileTwo.getColumn()) {
                    //tile one is right of tile two
                    for (int i = 1; i < (tileOne.getColumn() - tileTwo.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[tileOne.getRow()][tileOne.getColumn() - i]);
                    }
               } else if (tileOne.getColumn() < tileTwo.getColumn()) {
                    //tile one is left of tile two
                    for (int i = 1; i < (tileTwo.getColumn() - tileOne.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[tileOne.getRow()][i + tileOne.getColumn()]);
                    }
               }
          } else if (tileOne.getColumn() == tileTwo.getColumn()) {
               //tiles connected vertically
               if(tileOne.getRow() > tileTwo.getRow()) {
                    //tile one is below tile two
                    for (int i = 1; i < (tileOne.getRow() - tileTwo.getRow()); i++) {
                         tilesBetween.add(this.tileArray[tileOne.getRow() - i][tileOne.getColumn()]);
                    }
               } else if (tileOne.getColumn() < tileTwo.getColumn()) {
                    //tile one is above tile two
                    for (int i = 1; i < (tileTwo.getColumn() - tileOne.getColumn()); i++) {
                         tilesBetween.add(this.tileArray[i + tileOne.getRow()][tileOne.getColumn()]);
                    }
               }

          }
          return tilesBetween;
     }

     public void setTile(int row, int column, Piece piece) {
        this.tileArray[row][column].setPiece(piece);
        String pieceString = piece.getColor() + piece.getType();
        this.pieces[row][column] = pieceString;
     }

     @Override
     public String toString() {
          String ret = "";
          for(Tile[] tileRow : this.tileArray) {
               for (Tile tile : tileRow) {
                    ret += tile.toString();
               }
               ret += "\n";
         }
         return ret;
    }

}
