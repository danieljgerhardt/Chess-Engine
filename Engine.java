import java.util.*;

public class Engine {

     private Board board;
     private Board testBoard;
     private Game testGame;

     public Engine(Board b) {
          this.board = b;
     }

     public Move generateMove(Move previousMove) {
          boolean castling = false;
          boolean enPassant = false;
          this.testBoard = this.getGameBoardCopy();
          Piece empty = new Piece("e", "e", 0, 0);
          Move generator = new Move(empty, empty, this.testBoard);
          ArrayList<Piece> possibleStartingPieces = new ArrayList<Piece>();
          ArrayList<Move> candidateMoves = new ArrayList<Move>();
          for(Tile[] tArray : this.board.getTileArray()) {
               for(Tile t : tArray) {
                    if(t.getPiece().getColor().equals("b")) {
                         possibleStartingPieces.add(t.getPiece());
                         generator.generatePossibleMoves(t.getPiece());
                    }
               }
          }
          ArrayList<Tile> possibleMovesPerPiece = new ArrayList<Tile>();
          double maxEval = 0;
          int maxIndex = 0;
          for (Piece piece : possibleStartingPieces) {
               System.out.println(piece.toString());
               possibleMovesPerPiece = piece.getPossibleMoves();
               for (int i = 0; i < possibleMovesPerPiece.size(); i++) {
                    System.out.println(possibleMovesPerPiece.get(i).toString());
                    this.testBoard = this.getGameBoardCopy();
                    if (piece.getType().equals("K") && Math.abs((piece.getColumn() - possibleMovesPerPiece.get(i).getColumn())) == 2) {
                         castling = true;
                    }
                    if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == 4) {
                         enPassant = true;
                    }
                    Move testMove = new Move(piece, possibleMovesPerPiece.get(i).getPiece(), this.testBoard, enPassant, previousMove.getEndingTile(), castling);
                    if (testMove.makeMove()) {
                         System.out.println(testMove.toString());
                         testMove.makeMove();
                         candidateMoves.add(testMove);
                         double currentEval = this.evaluatePosition(this.testBoard);
                         int printEval = (int) Math.round(currentEval);
                         System.out.println("CANDIDATE MOVE! " + testMove.toString());
                         System.out.println("eval: " + printEval);
                         if (currentEval < maxEval) {
                              maxIndex = candidateMoves.size() - 1;
                              maxEval = this.evaluatePosition(this.testBoard);
                         }
                    }
                    testMove.undoMove();
                    castling = false;
                    enPassant = false;
               }
          }

          Move toMake = new Move(candidateMoves.get(maxIndex).getStartingPiece(), candidateMoves.get(maxIndex).getEndingTile().getPiece(), this.board, enPassant, previousMove.getEndingTile(), castling);
          return toMake;
     }

     public Board getGameBoardCopy() {
          //board copy used to test moves
          Board boardCopy = new Board();
          Tile[][] tileArrayCopy = new Tile[8][8];
          for (int row = 0; row < 8; row++) {
               for (int column = 0; column < 8; column++) {
                    Tile currentTile = this.board.getTileArray()[row][column];
                    Tile toAdd = new Tile(currentTile.getRow(), currentTile.getColumn(), currentTile.getColor());
                    toAdd.setPiece(currentTile.getPiece());
                    tileArrayCopy[row][column] = toAdd;
               }
          }
          boardCopy.setTileArray(tileArrayCopy);
          return boardCopy;
     }

     public double evaluatePosition(Board b) {
          //Material, king safety, piece activity
          //Positive = better for white, negative = better for black
          //More extreme value = higher advantage
          //System.out.println((b.getTotalPieceValue("w") -  b.getTotalPieceValue("b")));
          return (b.getTotalPieceValue("w") -  b.getTotalPieceValue("b"));
     }
}
