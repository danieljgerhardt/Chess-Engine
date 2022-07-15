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
          //these next two lines should be monitored when branching out the use case of this method
          double currentEval = this.evaluatePosition(this.testBoard);
          double bestEval = currentEval;
          int bestIndex = 0;
          for (Piece piece : possibleStartingPieces) {
               possibleMovesPerPiece = piece.getPossibleMoves();
               for (int i = 0; i < possibleMovesPerPiece.size(); i++) {
                    //System.out.println(possibleMovesPerPiece.size());
                    this.testBoard = this.board.getBoardCopy();
                    if (piece.getType().equals("K") && Math.abs((piece.getColumn() - possibleMovesPerPiece.get(i).getColumn())) == 2) {
                         castling = true;
                    }
                    if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getStartingPiece().getRow() == 2 && piece.getType().equals("P")) { //this line is questionable
                         enPassant = true;
                    }
                    Move testMove = new Move(piece, possibleMovesPerPiece.get(i).getPiece(), this.testBoard, enPassant, previousMove.getEndingTile(), castling);
                    if (testMove.makeMove()) {
                         currentEval = this.evaluatePosition(this.testBoard);
                         candidateAmount++;
                         candidateMoves.add(testMove);
                         System.out.println("Candidate " + candidateAmount + ": " + testMove.toString() + "; EVAL: " + currentEval);
                         if (currentEval < bestEval) {
                              bestIndex = candidateAmount - 1;
                              bestEval = currentEval;
                              System.out.println("new better index: " + bestIndex);
                    }
                    testMove.undoMove();
                    castling = false;
                    enPassant = false;
               }
          }
          if (candidateMoves.size() < 1) {
               System.out.println("ENGINE HAS NO MOVES -- GAME OVER");
               System.out.println(this.board.toString());
               System.exit(0);
          }
          System.out.println("Selected Candidate Move " + (bestIndex + 1) + " ; Eval: " + bestEval);
          Move toMake = new Move(candidateMoves.get(bestIndex).getStartingPiece(), candidateMoves.get(bestIndex).getEndingTile().getPiece(), this.board, enPassant, previousMove.getEndingTile(), castling);
          return toMake;
     }

     public double evaluatePosition(Board board) {
          //Positive = better for white, negative = better for black
          //More extreme value = higher advantage

          double materialEval = board.getTotalPieceValue("w") - board.getTotalPieceValue("b");
          //pawns on the same column(pawn structure eval)
          double pawnStructureEval = this.pawnStructureEval(board);
          //bishop pair
          double bishopPairEval = this.bishopPairEval(board);
          //centralized pieces

          //knights on the edge

          //empty squares near a king

          //avoid repeating moves

          //ability to promote

          //color complexes, having bishops that are not the same as the enemy pawn color

          //Weighting evals
          //Material = 90%
          //Pawn structure = 5%
          //Having bishop pair = 5%
          double totalEval = (materialEval * .9) + (pawnStructureEval * .05) + (bishopPairEval * .05);
          return totalEval;
     }

     public int pawnStructureEval(Board board) {
          int whitePawnsPerColumn = 0;
          int blackPawnsPerColumn = 0;
          int whiteBadColumns = 0;
          int blackBadColumns = 0;
          for (int column = 0; column < 8; column++) {
               whitePawnsPerColumn = 0;
               blackPawnsPerColumn = 0;
               for (int row = 0; row < 8; row++) {
                    Piece test = board.getTileArray()[row][column].getPiece();
                    if (test.getType().equals("P")) {
                         if (test.getColor().equals("w")) {
                              whitePawnsPerColumn++;
                              //System.out.println("white pawn " + row + ", " + column);
                         } else if (test.getColor().equals("b")) {
                              blackPawnsPerColumn++;
                              //System.out.println("black pawn " + row + ", " + column);
                         }
                    }
               }
               if (whitePawnsPerColumn > 1) {
                    whiteBadColumns++;
               } else if (blackPawnsPerColumn > 1){
                    blackBadColumns++;
               }
          }
          return blackBadColumns - whiteBadColumns;
     }

     public int pushedPawnsEval(Board board) {
          //passed pawns(pawns that have no opposition from enemy pawns)
          //pawns closer to promotion
          return 0;
     }

     public int bishopPairEval(Board board) {
          int bishopDifferential = 0;
          int whiteBishops = 0;
          int whiteKnights = 0;
          int blackBishops = 0;
          int blackKnights = 0;
          for (int row = 0; row < 8; row++) {
               for (int column = 0; column < 8; column++) {
                    if (board.getTileArray()[row][column].getPiece().getType().equals("B")) {
                         if (board.getTileArray()[row][column].getPiece().getColor().equals("w")) {
                              whiteBishops++;
                         } else if (board.getTileArray()[row][column].getPiece().getColor().equals("b")) {
                              blackBishops++;
                         }
                    } else if (board.getTileArray()[row][column].getPiece().getType().equals("N")) {
                         if (board.getTileArray()[row][column].getPiece().getColor().equals("w")) {
                              whiteKnights++;
                         } else if (board.getTileArray()[row][column].getPiece().getColor().equals("b")) {
                              blackKnights++;
                         }
                    }
               }
          }
          if ((whiteBishops + whiteKnights) == (blackBishops + blackKnights)) {
               if (whiteBishops == 2 && whiteBishops > blackBishops) {
                    bishopDifferential = 1;
               } else if (blackBishops == 2 && blackBishops > whiteBishops) {
                    bishopDifferential = -1;
               }
          }
          //2 = white has 2 bishops, black has none
          //-2 = black has 2 bishops, white has none
          return bishopDifferential;
     }

}
