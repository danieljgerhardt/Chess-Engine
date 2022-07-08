import java.util.*;

public class Engine {

     private Board board;
     private Board testBoard;
     private Game testGame;

     public Engine(Board b) {
          this.board = b;
     }

     public Move generateMove(Move previousMove) {
          int candidateAmount = 0;
          boolean castling = false;
          boolean enPassant = false;
          this.testBoard = this.board.getBoardCopy();
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
               possibleMovesPerPiece = piece.getPossibleMoves();
               for (int i = 0; i < possibleMovesPerPiece.size(); i++) {
                    //System.out.println(possibleMovesPerPiece.size());
                    this.testBoard = this.board.getBoardCopy();
                    if (piece.getType().equals("K") && Math.abs((piece.getColumn() - possibleMovesPerPiece.get(i).getColumn())) == 2) {
                         castling = true;
                    }
                    if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == 4) {
                         enPassant = true;
                    }
                    Move testMove = new Move(piece, possibleMovesPerPiece.get(i).getPiece(), this.testBoard, enPassant, previousMove.getEndingTile(), castling);
                    if (testMove.makeMove()) {
                         double currentEval = this.evaluatePosition(this.testBoard);
                         candidateAmount++;
                         candidateMoves.add(testMove);
                         //int printEval = (int) Math.round(currentEval);
                         //System.out.println("CANDIDATE MOVE " + candidateAmount + ": " + testMove.toString());
                         //System.out.println("eval: " + printEval);
                         if (currentEval < maxEval) {
                              maxIndex = candidateMoves.size() - 1;
                              maxEval = currentEval;
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

     public double evaluatePosition(Board board) {
          //Material, king safety, piece activity
          //Positive = better for white, negative = better for black
          //More extreme value = higher advantage
          //System.out.println((b.getTotalPieceValue("w") -  b.getTotalPieceValue("b")));

          double materialEval = board.getTotalPieceValue("w") -  board.getTotalPieceValue("b");
          //pawns on the same column(pawn structure eval)
          //double pawnStructureEval = this.pawnStructureEval(board, "w") - this.pawnStructureEval(board, "b");
          //hanging pieces

          //bishop pair

          //centralized pieces

          //knights on the edge


          //double totalEval = (materialEval * .9) + (pawnStructureEval * .1);
          //return totalEval;
          return materialEval;
     }

     public int pawnStructureEval(Board board, String color) {
          int columns = 0;
          for (int column = 0; column < 8; column++) {
               int pawnsPerColumn = 0;
               for (int row = 0; row < 8; row++) {
                    if (board.getTileArray()[row][column].getPiece().getType().equals("P") && board.getTileArray()[row][column].getPiece().getColor().equals(color)) {
                         pawnsPerColumn++;
                    }
               }
               if (pawnsPerColumn > 1) columns++;
          }
          if (columns > 0) System.out.println("PAWN STRUCTURE");
          return columns;
     }
}
