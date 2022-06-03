import java.util.ArrayList;

public class Move {

     private Piece startingPiece;
     private Piece endingPiece;
     private Tile endingTile;
     private Board board;
     private boolean canEnPassant = false;
     private Tile enPassantTile;
     private boolean engagingEnPassant = false;

     public Move(Piece start, Piece end, Board board) {
          this.startingPiece = start;
          this.endingPiece = end;
          this.board = board;
          this.endingTile = this.board.getTileArray()[this.endingPiece.getRow()][this.endingPiece.getColumn()];
     }

     public Move(Piece start, Piece end, Board board, boolean enPassantAbility, Tile enPassantTile) {
          this.startingPiece = start;
          this.endingPiece = end;
          this.board = board;
          this.endingTile = this.board.getTileArray()[this.endingPiece.getRow()][this.endingPiece.getColumn()];
          this.canEnPassant = enPassantAbility;
          this.enPassantTile = enPassantTile;
          //Make engagingEnPassant true if this move is en passant
          if (this.startingPiece.getColor().equals("w")){
               if (this.startingPiece.getType().equals("P") && this.endingTile.getRow() + 1 == this.startingPiece.getRow()) {
                    this.engagingEnPassant = true;
               }
          } else {
               if (this.startingPiece.getType().equals("P") && this.endingTile.getRow() - 1 == this.startingPiece.getRow()) {
                    this.engagingEnPassant = true;
               }
          }
     }

     public boolean makeMove() {
          this.generatePossibleMoves(this.startingPiece);
          if(this.startingPiece.getPossibleMoves().contains(this.endingTile)) {
               if (engagingEnPassant) {
                    this.makeEnPassantCapture();
               }
               Piece empty = new Piece("e", "e", this.startingPiece.getRow(), this.startingPiece.getColumn());
               this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn(), empty);
               this.board.setTile(this.endingPiece.getRow(), this.endingPiece.getColumn(), this.startingPiece);
               this.startingPiece.setRow(endingPiece.getRow());
               this.startingPiece.setColumn(endingPiece.getColumn());
               this.checkPromotion();
               return true;
          } else {
               return false;
          }
     }

     public void makeEnPassantCapture() {
          Piece empty;
          if (this.startingPiece.getColor().equals("w")) {
               empty = new Piece("e", "e", this.endingTile.getRow() + 1, this.endingTile.getColumn());
               this.board.setTile( this.endingTile.getRow() + 1, this.endingTile.getColumn(), empty);
          } else {
               empty = new Piece("e", "e", this.endingTile.getRow() - 1, this.endingTile.getRow());
               this.board.setTile( this.endingTile.getRow() - 1, this.endingTile.getColumn(), empty);
          }
          engagingEnPassant = false;
     }

     public boolean checkPromotion() {
          if (this.startingPiece.getType().equals("P")) {
               if (this.startingPiece.getRow() == 0) {
                    promotion("Q", "w");
                    return true;
               }
               if (this.startingPiece.getRow() == 7) {
                    promotion("Q", "b");
                    return true;
               }
          }
          return false;
     }

     public void promotion(String newType, String color) {
          Piece promotion = new Piece(newType, color, this.endingTile.getRow() - 1, this.endingTile.getRow());
          this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn(), promotion);
    }

     public Tile getEndingTile() {
          return this.endingTile;
     }

     public Piece getStartingPiece() {
          return this.startingPiece;
     }

     @Override
     public String toString() {
          String ret = "Moving ";
          ret += this.startingPiece.getColor();
          ret += this.startingPiece.getType();
          ret += " to ";
          ret += this.endingTile.getRow();
          ret += ", ";
          ret += this.endingTile.getColumn();
          return ret;
     }

     public void generatePossibleMoves(Piece piece) {
          this.startingPiece.clearPossibleMoves();
          if (piece.getType().equals("K")) {
              //row and column cannot change by more than one
              for (int i = -1; i < 2; i++) {
                   for (int j = -1; j < 2; j++) {
                        if ((i + piece.getRow() >= 0) && (i + piece.getRow() < 8) && (j + piece.getColumn() >= 0) && (j + piece.getColumn() < 8)) {
                             if (!this.board.getTileArray()[i + piece.getRow()][j + piece.getColumn()].getPiece().getColor().equals(piece.getColor())) {
                                  piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() + j]);
                             }
                        }
                   }
              }
          } else if (piece.getType().equals("R")) {
               //only row or column can change
               //left
               for (int i = piece.getColumn() - 1; i >= 0; i--) {
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
               }
               //right
               for (int i = piece.getColumn() + 1; i < 8; i++) {
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
               }
               //up
               for (int i = piece.getRow() + 1; i < 8; i++) {
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
               }
               //down
               for (int i = piece.getRow() - 1; i >= 0; i--) {
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
               }

          } else if (piece.getType().equals("B")) {
               //row and column change the same amount
               //Up left -- subtracting i from row and subtracting i from column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() - i >= 0 && piece.getColumn() - i >= 0) {
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() - i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() - i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() - i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() - i]);
                    } else {
                         break;
                    }
               }
               //Up right -- subtracting i from row and adding i to column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() - i >= 0 && piece.getColumn() + i < 8) {
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() + i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() + i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() + i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() + i]);
                    } else {
                         break;
                    }
               }
               //Down left -- add i to row and subtract i from column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() + i < 8  && piece.getColumn() - i >= 0) {
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() - i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() - i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() - i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() - i]);
                    } else {
                         break;
                    }
               }
               //Down right -- adding i to row and adding i to column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() + i < 8  && piece.getColumn() + i < 8) {
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() + i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() + i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() + i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() + i]);
                    } else {
                         break;
                    }
               }

          } else if (piece.getType().equals("Q")) {
               //rook movement
               //left
               for (int i = piece.getColumn() - 1; i >= 0; i--) {
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
               }
               //right
               for (int i = piece.getColumn() + 1; i < 8; i++) {
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[piece.getRow()][i].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[piece.getRow()][i]);
               }
               //up
               for (int i = piece.getRow() + 1; i < 8; i++) {
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
               }
               //down
               for (int i = piece.getRow() - 1; i >= 0; i--) {
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getColor())) {
                         break;
                    }
                    if (this.board.getTileArray()[i][piece.getColumn()].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
                         break;
                    }
                    piece.addToPossibleMoves(board.getTileArray()[i][piece.getColumn()]);
               }
               //bishop movement
               //Up left -- subtracting i from row and subtracting i from column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() - i >= 0 && piece.getColumn() - i >= 0) {
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() - i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() - i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() - i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() - i]);
                    } else {
                         break;
                    }
               }
               //Up right -- subtracting i from row and adding i to column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() - i >= 0 && piece.getColumn() + i < 8) {
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() + i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() - i][piece.getColumn() + i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() + i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - i][piece.getColumn() + i]);
                    } else {
                         break;
                    }
               }
               //Down left -- add i to row and subtract i from column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() + i < 8  && piece.getColumn() - i >= 0) {
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() - i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() - i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() - i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() - i]);
                    } else {
                         break;
                    }
               }
               //Down right -- adding i to row and adding i to column
               for (int i = 1; i < 8; i++) {
                    if (piece.getRow() + i < 8  && piece.getColumn() + i < 8) {
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() + i].getPiece().getColor().equals(piece.getColor())) {
                              break;
                         }
                         if (this.board.getTileArray()[piece.getRow() + i][piece.getColumn() + i].getPiece().getColor().equals(piece.getOppositeColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() + i]);
                              break;
                         }
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() + i]);
                    } else {
                         break;
                    }
               }

          } else if (piece.getType().equals("N")) {
               //Add 2 to columnn, add/subtract 1 to row
               if (piece.getColumn() + 2 < 8) {
                    if (piece.getRow() + 1 < 8) {
                         if (!this.board.getTileArray()[piece.getRow() + 1][piece.getColumn() + 2].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn() + 2]);
                         }
                    }
                    if (piece.getRow() - 1 >= 0) {
                         if (!this.board.getTileArray()[piece.getRow() - 1][piece.getColumn() + 2].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn() + 2]);
                         }
                    }
               }
               //Add 2 to row, add/subtract 1 to column
               if (piece.getRow() + 2 < 8) {
                    if (piece.getColumn() + 1 < 8) {
                         if (!this.board.getTileArray()[piece.getRow() + 2][piece.getColumn() + 1].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 2][piece.getColumn() + 1]);
                         }
                    }
                    if (piece.getColumn() - 1 >= 0) {
                         if (!this.board.getTileArray()[piece.getRow() + 2][piece.getColumn() - 1].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 2][piece.getColumn() - 1]);
                         }
                    }
               }
               //Subtract 2 from columnn, add/subtract 1 to row
               if (piece.getColumn() - 2 >= 0) {
                    if (piece.getRow() + 1 < 8) {
                         if (!this.board.getTileArray()[piece.getRow() + 1][piece.getColumn() - 2].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn() - 2]);
                         }
                    }
                    if (piece.getRow() - 1 >= 0) {
                         if (!this.board.getTileArray()[piece.getRow() - 1][piece.getColumn() - 2].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn() - 2]);
                         }
                    }
               }
               //Subtract 2 from row, add/subtract 1 to column
               if (piece.getRow() - 2 >= 0) {
                    if (piece.getColumn() + 1 < 8) {
                         if (!this.board.getTileArray()[piece.getRow() - 2][piece.getColumn() + 1].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 2][piece.getColumn() + 1]);
                         }
                    }
                    if (piece.getColumn() - 1 >= 0) {
                         if (!this.board.getTileArray()[piece.getRow() - 2][piece.getColumn() - 1].getPiece().getColor().equals(piece.getColor())) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 2][piece.getColumn() - 1]);
                         }
                    }
               }

          } else if (piece.getType().equals("P")) {
               //check if pawn is on home row -- if it is enable moving twice
               if (piece.getColor().equals("w") && piece.getRow() == 6) {
                    if (this.board.getTileArray()[piece.getRow() - 2][piece.getColumn()].getPiece().getColor().equals("e") && this.board.getTileArray()[piece.getRow() - 1][piece.getColumn()].getPiece().getColor().equals("e")) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 2][piece.getColumn()]);
                    }
               }
               if (piece.getColor().equals("b") && piece.getRow() == 1) {
                    if (this.board.getTileArray()[piece.getRow() + 2][piece.getColumn()].getPiece().getColor().equals("e") && this.board.getTileArray()[piece.getRow() + 1][piece.getColumn()].getPiece().getColor().equals("e")) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 2][piece.getColumn()]);
                    }
               }
               //if opponent's pawn has just moved twice and your pawn is adjacent allow en passant
               if (this.canEnPassant) {
                    if (this.enPassantTile.getColumn() + 1 == this.startingPiece.getColumn() || this.enPassantTile.getColumn() - 1 == this.startingPiece.getColumn()) {
                         if(piece.getColor().equals("b")) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][this.enPassantTile.getColumn()]);
                         }
                         if(piece.getColor().equals("w")) {
                              piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][this.enPassantTile.getColumn()]);
                         }
                    }

               }
               //check for move once forward
               if (piece.getColor().equals("w")) {
                    if (this.board.getTileArray()[piece.getRow() - 1][piece.getColumn()].getPiece().getColor().equals("e")) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn()]);
                    }
               }
               if (piece.getColor().equals("b")) {
                    if (this.board.getTileArray()[piece.getRow() + 1][piece.getColumn()].getPiece().getColor().equals("e")) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn()]);
                    }
               }
               //check for diagonal capture
               if (piece.getColor().equals("w")) {
                    if (this.board.getTileArray()[piece.getRow() - 1][piece.getColumn() + 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn() + 1]);
                    }
                    if (this.board.getTileArray()[piece.getRow() - 1][piece.getColumn() - 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn() - 1]);
                    }
               }
               if (piece.getColor().equals("b")) {
                    if (this.board.getTileArray()[piece.getRow() + 1][piece.getColumn() + 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn() + 1]);
                    }
                    if (this.board.getTileArray()[piece.getRow() + 1][piece.getColumn() - 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                         piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn() - 1]);
                    }
               }
          }
    }
}
