import java.util.ArrayList;
import java.util.*;

public class Move {

     private Piece startingPiece;
     private Piece endingPiece;
     private Tile startingTile;
     private Tile endingTile;
     private Board board;
     private boolean canEnPassant = false;
     private Tile enPassantTile;
     private boolean engagingEnPassant = false;

     public Move(Piece start, Piece end, Board board) {
          this.startingPiece = start;
          this.endingPiece = end;
          this.board = board;
          this.startingTile = this.board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()];
          this.endingTile = this.board.getTileArray()[this.endingPiece.getRow()][this.endingPiece.getColumn()];
     }

     public Move(Piece start, Piece end, Board board, boolean enPassantAbility, Tile enPassantTile) {
          this.startingPiece = start;
          this.endingPiece = end;
          this.board = board;
          this.startingTile = this.board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()];
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
          if(!neutralizeThreat("w")) return false;
          if(!neutralizeThreat("b")) return false;
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
               //check for pins
               ArrayList<Tile> postMoveThreats = new ArrayList<Tile>();
               postMoveThreats = this.detectKingThreats(this.startingPiece.getColor());
               if (postMoveThreats != null && postMoveThreats.size() > 0) {
                    //System.out.println(this.startingPiece.toString());
                    this.board.setTile(this.startingTile.getRow(), this.startingTile.getColumn(), this.startingPiece);
                    this.board.setTile(this.endingTile.getRow(), this.endingTile.getColumn(), this.endingPiece);
                    this.startingPiece.setRow(startingTile.getRow());
                    this.startingPiece.setColumn(startingTile.getColumn());
                    this.endingPiece.setRow(endingTile.getRow());
                    this.endingPiece.setColumn(endingTile.getColumn());
                    return false;
               }
               //execute promotion
               this.checkPromotion();
               return true;
          } else {
               return false;
          }

     }

     public void makeEnPassantCapture() {
          Piece empty;
          if (this.startingPiece.getColor().equals("w")) {
               //if the pawn is next to a pawn of the opposite color
               if(this.board.getTileArray()[this.endingTile.getRow() + 1][this.endingTile.getColumn()].toString().equals("bP ")) {
                    empty = new Piece("e", "e", this.endingTile.getRow() + 1, this.endingTile.getColumn());
                    this.board.setTile( this.endingTile.getRow() + 1, this.endingTile.getColumn(), empty);
               }
          } else {
               //if the pawn is next to a pawn of the opposite color
               if(this.board.getTileArray()[this.endingTile.getRow() - 1][this.endingTile.getColumn()].toString().equals("wP ")) {
                    empty = new Piece("e", "e", this.endingTile.getRow() - 1, this.endingTile.getRow());
                    this.board.setTile( this.endingTile.getRow() - 1, this.endingTile.getColumn(), empty);
               }
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

     public boolean neutralizeThreat(String color) {
          ArrayList<Tile> threats = new ArrayList<Tile>();
          if (this.startingPiece.getColor().equals(color)) {
               threats = this.detectKingThreats(color);
               if(threats != null && threats.size() > 0) {
                    if(!this.startingPiece.getType().equals("K")) {
                         boolean captureChecker = false;
                         for(Tile t : threats) {
                              if (t.equals(this.endingTile)) {
                                   captureChecker = true;
                              }
                         }
                         if (!captureChecker) {
                              return false;
                         }
                    }
               }
          }
          //block checks
          
          return true;
     }

     public void promotion(String newType, String color) {
          this.startingPiece.setType("Q");
          this.board.setTile( this.endingTile.getRow(), this.endingTile.getColumn(), startingPiece);
    }

     public Tile getEndingTile() {
          return this.endingTile;
     }

     public Piece getStartingPiece() {
          return this.startingPiece;
     }

     @Override
     public String toString() {
          String ret = "";
          if (!this.startingPiece.getType().equals("P")) {
               ret += this.startingPiece.getType();
          }
          ret += this.endingTile.toTileNotation();
          return ret;
     }

     public ArrayList<Tile> detectKingThreats(String color) {
          //Detect all possible threats on straights, diagonals, and knight jumps
          ArrayList<Tile> threats = new ArrayList<Tile>();
          threats.clear();
          Piece king = this.board.getKing(color);
          king.clearPossibleMoves();
          this.generatePossibleRookMoves(king);
          for (Tile t : king.getPossibleMoves()) {
               if ((t.getPiece().getType().equals("Q") || t.getPiece().getType().equals("R")) && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          //king.clearPossibleMoves();
          this.generatePossibleBishopMoves(king);
          for (Tile t : king.getPossibleMoves()) {
               if ((t.getPiece().getType().equals("Q") || t.getPiece().getType().equals("B")) && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          //king.clearPossibleMoves();
          this.generatePossibleKnightMoves(king);
          for (Tile t : king.getPossibleMoves()) {
               if (t.getPiece().getType().equals("N") && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          /*Set<Tile> set = new HashSet<>(threats);
          threats.clear();
          threats.addAll(set);*/
          /*for(Tile t : threats) {
               if (!t.getPiece().getType().equals("e") && !t.getPiece().getColor().equals(color)) {
                    System.out.println(t.getRow() + ", " + t.getColumn());
               }
          }*/
          if (threats.size() > 0) {
               return threats;
          } else {
               return null;
          }

     }

     public boolean kingCanMoveToSquare(Tile tile) {
          //Can an enemy piece see this square
          //See what enemy pieces are viewing the square the king is trying to move to
          //Make another generatePossibleMoves method
          return false;
     }

     public void generatePossibleMoves(Piece piece) {
          this.startingPiece.clearPossibleMoves();
          if (piece.getType().equals("K")) {
               this.generatePossibleKingMoves(piece);
          } else if (piece.getType().equals("R")) {
               this.generatePossibleRookMoves(piece);
          } else if (piece.getType().equals("B")) {
               this.generatePossibleBishopMoves(piece);
          } else if (piece.getType().equals("Q")) {
               this.generatePossibleRookMoves(piece);
               this.generatePossibleBishopMoves(piece);
          } else if (piece.getType().equals("N")) {
               this.generatePossibleKnightMoves(piece);
          } else if (piece.getType().equals("P")) {
               this.generatePossiblePawnMoves(piece);
          }
     }

    public void generatePossibleKingMoves(Piece piece) {
         for (int i = -1; i < 2; i++) {
              for (int j = -1; j < 2; j++) {
                   if ((i + piece.getRow() >= 0) && (i + piece.getRow() < 8) && (j + piece.getColumn() >= 0) && (j + piece.getColumn() < 8)) {
                        if (!this.board.getTileArray()[i + piece.getRow()][j + piece.getColumn()].getPiece().getColor().equals(piece.getColor())) {
                             piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + i][piece.getColumn() + j]);
                        }
                   }
              }
         }
    }

    public void generatePossibleRookMoves(Piece piece) {
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
    }
    public void generatePossibleBishopMoves(Piece piece) {
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
    }

    public void generatePossibleKnightMoves(Piece piece) {
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
    }

    public void generatePossiblePawnMoves(Piece piece) {
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
              if(piece.getColumn() != 7) {
                   if (this.board.getTileArray()[piece.getRow() - 1][piece.getColumn() + 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                        piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn() + 1]);
                   }
              }
              if (piece.getColumn() != 0) {
                   if (this.board.getTileArray()[piece.getRow() - 1][piece.getColumn() - 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                        piece.addToPossibleMoves(board.getTileArray()[piece.getRow() - 1][piece.getColumn() - 1]);
                   }
              }
         }
         if (piece.getColor().equals("b")) {
              if (piece.getColumn() != 7) {
                   if (this.board.getTileArray()[piece.getRow() + 1][piece.getColumn() + 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                        piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn() + 1]);
                   }
              }
              if (piece.getColumn() != 0) {
                   if (this.board.getTileArray()[piece.getRow() + 1][piece.getColumn() - 1].getPiece().getColor().equals(piece.getOppositeColor())) {
                        piece.addToPossibleMoves(board.getTileArray()[piece.getRow() + 1][piece.getColumn() - 1]);
                   }
              }

         }
    }

}
