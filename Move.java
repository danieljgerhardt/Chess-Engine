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
     private boolean castle = false;

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

     public Move(Piece start, Piece end, Board board, boolean castle) {
          this.startingPiece = start;
          this.endingPiece = end;
          this.board = board;
          this.startingTile = this.board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()];
          this.endingTile = this.board.getTileArray()[this.endingPiece.getRow()][this.endingPiece.getColumn()];
          this.castle = castle;
     }

     public boolean makeMove() {
          if(!neutralizeThreats("w")) return false;
          if(!neutralizeThreats("b")) return false;
          this.generatePossibleMoves(this.startingPiece);
          if(this.startingPiece.getPossibleMoves().contains(this.endingTile)) {
               if (engagingEnPassant) {
                    this.makeEnPassantCapture();
               }
               if (castle) {
                    this.executeCastle();
               }
               Piece empty = new Piece("e", "e", this.startingPiece.getRow(), this.startingPiece.getColumn());
               this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn(), empty);
               this.board.setTile(this.endingPiece.getRow(), this.endingPiece.getColumn(), this.startingPiece);
               this.startingPiece.setRow(endingPiece.getRow());
               this.startingPiece.setColumn(endingPiece.getColumn());
               this.startingPiece.setHasMoved(true);
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

     public void executeCastle() {
          Piece whiteRook, blackRook;
          if (this.startingPiece.getColor().equals("w")) {
               if (this.endingTile.getColumn() == 2) {
                    //Queen side white castling
                    whiteRook = new Piece("R", "w", this.startingPiece.getRow(), this.startingPiece.getColumn() + 1);
                    Piece empty = new Piece("e", "e", this.endingTile.getRow(), this.endingTile.getColumn() - 2);
                    this.board.setTile(this.endingTile.getRow(), this.endingTile.getColumn() - 2, empty);
                    this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn() - 1, whiteRook);
                    whiteRook.setHasMoved(true);
               } else {
                    //King side white castling
                    whiteRook = new Piece("R", "w", this.startingPiece.getRow(), this.startingPiece.getColumn() + 1);
                    Piece empty = new Piece("e", "e", this.endingTile.getRow(), this.endingTile.getColumn() + 1);
                    this.board.setTile(this.endingTile.getRow(), this.endingTile.getColumn() + 1, empty);
                    this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn() + 1, whiteRook);
                    whiteRook.setHasMoved(true);
               }
          } else {
               if (this.endingTile.getColumn() == 2) {
                    //Queen side black castling
                    blackRook = new Piece("R", "b", this.startingPiece.getRow(), this.startingPiece.getColumn() + 1);
                    Piece empty = new Piece("e", "e", this.endingTile.getRow(), this.endingTile.getColumn() - 2);
                    this.board.setTile(this.endingTile.getRow(), this.endingTile.getColumn() - 2, empty);
                    this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn() - 1, blackRook);
                    blackRook.setHasMoved(true);
               } else {
                    //King side black castling
                    blackRook = new Piece("R", "b", this.startingPiece.getRow(), this.startingPiece.getColumn() + 1);
                    Piece empty = new Piece("e", "e", this.endingTile.getRow(), this.endingTile.getColumn() + 1);
                    this.board.setTile(this.endingTile.getRow(), this.endingTile.getColumn() + 1, empty);
                    this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn() + 1, blackRook);
                    blackRook.setHasMoved(true);
               }
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
                    this.startingPiece.setType("Q");
                    this.board.setTile( this.endingTile.getRow(), this.endingTile.getColumn(), startingPiece);
                    return true;
               }
               if (this.startingPiece.getRow() == 7) {
                    this.startingPiece.setType("Q");
                    this.board.setTile( this.endingTile.getRow(), this.endingTile.getColumn(), startingPiece);
                    return true;
               }
          }
          return false;
     }

     public boolean neutralizeThreats(String color) {
          ArrayList<Tile> threats = new ArrayList<Tile>();
          if (this.startingPiece.getColor().equals(color)) {
               threats = this.detectKingThreats(color);
               if(threats != null && threats.size() == 1) {
                    if(!this.startingPiece.getType().equals("K")) {
                         //block checks
                         //for every square on a checking piece's line to the king:
                         //determine what pieces can land on those squares
                         //add those moves to their possible move lists
                         Piece king = this.board.getKing(color);
                         Tile kingTile = this.board.getTileArray()[king.getRow()][king.getColumn()];
                         Tile checkerTile = threats.get(0);
                         ArrayList<Tile> tilesBetween = new ArrayList<Tile>();
                         tilesBetween = this.board.getPathBetween(kingTile, checkerTile);
                         //if a piece of the right color can go on to tilesBetween, allow it
                         for (Tile t : tilesBetween) {
                              if (this.endingTile.equals(t))  {
                                   return true;
                              }
                              System.out.println(t.toTileNotation());
                         }

                         //Capture checking piece
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
               } else if(threats != null && threats.size() == 2) {
                    //double checks -- only allow kings to move
                    if(!this.startingPiece.getType().equals("K")) {
                         return false;
                    }
               }
          }

          return true;
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
          this.generatePossibleBishopMoves(king);
          for (Tile t : king.getPossibleMoves()) {
               if ((t.getPiece().getType().equals("Q") || t.getPiece().getType().equals("B")) && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          this.generatePossibleKnightMoves(king);
          for (Tile t : king.getPossibleMoves()) {
               if (t.getPiece().getType().equals("N") && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          this.generatePossiblePawnMoves(king);
          for (Tile t : king.getPossibleMoves()) {
               if (t.getPiece().getType().equals("P") && !t.getPiece().getColor().equals(color)) {
                    if (color.equals("w") && (t.getPiece().getRow() - king.getRow() == -1) && (Math.abs(t.getPiece().getColumn() - king.getColumn()) == 1)) {
                         threats.add(t);
                    } else if (color.equals("b") && (t.getPiece().getRow() - king.getRow() == 1) && (Math.abs(t.getPiece().getColumn() - king.getColumn()) == 1)) {
                         threats.add(t);
                    }
               }
          }
          Set<Tile> set = new HashSet<>(threats);
          threats.clear();
          threats.addAll(set);
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

     public ArrayList<Tile> detectThreatsToTile(String color, Tile tile) {
          ArrayList<Tile> threats = new ArrayList<Tile>();
          threats.clear();
          Piece piece = tile.getPiece();
          piece.clearPossibleMoves();
          this.generatePossibleRookMoves(piece);
          for (Tile t : piece.getPossibleMoves()) {
               if ((t.getPiece().getType().equals("Q") || t.getPiece().getType().equals("R")) && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          //king.clearPossibleMoves();
          this.generatePossibleBishopMoves(piece);
          for (Tile t : piece.getPossibleMoves()) {
               if ((t.getPiece().getType().equals("Q") || t.getPiece().getType().equals("B")) && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          //king.clearPossibleMoves();
          this.generatePossibleKnightMoves(piece);
          for (Tile t : piece.getPossibleMoves()) {
               if (t.getPiece().getType().equals("N") && !t.getPiece().getColor().equals(color)) {
                    threats.add(t);
               }
          }
          Set<Tile> set = new HashSet<>(threats);
          threats.clear();
          threats.addAll(set);
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
         //Castling
         //King moves 2 squares to the outside and rook moves to the inside of the king
         //Castling does not work if:
         //Piece is in the way
         //King has moved
         //Rook on the same side of castling has moved
         //King is in check
         //King moves through check while Castling
         boolean emptyBetweenRookAndKing = true;
         if (!this.startingPiece.getHasMoved()) {
               if(this.detectKingThreats("w") == null) {
                    if(this.startingPiece.getColor().equals("w"))  {
                         Piece whiteQueenRook = this.board.getTileArray()[7][0].getPiece();
                         Piece whiteKingRook = this.board.getTileArray()[7][7].getPiece();
                         if (this.board.getTileArray()[7][0].getPiece().getType().equals("R")) {
                              //check if there are pieces in the middle, the rook has moved, or the king would move through check
                              if (!whiteQueenRook.getHasMoved()) {
                                   for (Tile t : this.board.getPathBetween(this.board.getTileArray()[7][0], this.startingTile)) {
                                        if (!t.getPiece().getType().equals("e")) {
                                             //there is a piece in between the king and rook
                                             emptyBetweenRookAndKing = false;
                                        }
                                   }
                                   if (emptyBetweenRookAndKing) {
                                        //see if king would go through checks
                                        if (this.detectThreatsToTile("w", this.board.getTileArray()[7][2]) == null && this.detectThreatsToTile("w", this.board.getTileArray()[7][3]) == null) {
                                             //we can castle
                                             piece.addToPossibleMoves(board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()]);
                                        }
                                   }
                              }
                         }
                         if (this.board.getTileArray()[7][7].getPiece().getType().equals("R")) {
                              //check if there are pieces in the middle, the rook has moved, or the king would move through check
                              if (!whiteKingRook.getHasMoved()) {
                                   for (Tile t: this.board.getPathBetween(this.board.getTileArray()[7][7], this.startingTile)) {
                                        if (!t.getPiece().getType().equals("e")) {
                                             //there is a piece in between the king and rook
                                             emptyBetweenRookAndKing = false;
                                        }
                                   }
                                   if (emptyBetweenRookAndKing) {
                                        //see if king would go through checks
                                        if (this.detectThreatsToTile("w", this.board.getTileArray()[7][5]) == null && this.detectThreatsToTile("w", this.board.getTileArray()[7][6]) == null) {
                                             //we can castle
                                             piece.addToPossibleMoves(board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()]);
                                        }
                                   }
                              }
                         }
                    }
              }
              if(this.detectKingThreats("b") == null) {
                    if(this.startingPiece.getColor().equals("b"))  {
                         Piece blackQueenRook = this.board.getTileArray()[0][0].getPiece();
                         Piece blackKingRook = this.board.getTileArray()[0][7].getPiece();
                         if (this.board.getTileArray()[0][0].getPiece().getType().equals("R")) {
                              //check if there are pieces in the middle, the rook has moved, or the king would move through check
                              if (!blackQueenRook.getHasMoved()) {
                                   for (Tile t: this.board.getPathBetween(this.board.getTileArray()[0][0], this.startingTile)) {
                                        if (!t.getPiece().getType().equals("e")) {
                                             //there is a piece in between the king and rook
                                             emptyBetweenRookAndKing = false;
                                        }
                                   }
                                   if (emptyBetweenRookAndKing) {
                                        //see if king would go through checks
                                        if (this.detectThreatsToTile("b", this.board.getTileArray()[0][2]) == null && this.detectThreatsToTile("b", this.board.getTileArray()[0][3]) == null) {
                                             //we can castle
                                             piece.addToPossibleMoves(board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()]);
                                        }
                                   }
                              }
                         }
                         if (this.board.getTileArray()[0][7].getPiece().getType().equals("R")) {
                              //check if there are pieces in the middle, the rook has moved, or the king would move through check
                              if (!blackKingRook.getHasMoved()) {
                                   for (Tile t: this.board.getPathBetween(this.board.getTileArray()[0][7], this.startingTile)) {
                                        if (!t.getPiece().getType().equals("e")) {
                                             //there is a piece in between the king and rook
                                             emptyBetweenRookAndKing = false;
                                        }
                                   }
                                   if (emptyBetweenRookAndKing) {
                                        //see if king would go through checks
                                        if (this.detectThreatsToTile("b", this.board.getTileArray()[0][5]) == null && this.detectThreatsToTile("b", this.board.getTileArray()[0][6]) == null) {
                                             //we can castle
                                             piece.addToPossibleMoves(board.getTileArray()[this.startingPiece.getRow()][this.startingPiece.getColumn()]);
                                        }
                                   }
                              }
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
