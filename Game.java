import java.util.ArrayList;

public class Game {

     private ArrayList<Move> moveList = new ArrayList<Move>();

     private boolean whiteToMove;

     private final int WHITE_EN_PASSANT_INT = 3;

     private final int BLACK_EN_PASSANT_INT = 4;

     private int enPassantInt;

     private Board gameBoard;

     private Engine engine;

     public Game(ArrayList<Move> ml, Board b) {
          this.moveList = ml;
          this.gameBoard = b;
          this.whiteToMove = true;
          this.engine = new Engine(this.gameBoard);
     }

     //piece one and piece two are two most recent pieces in the DisplayGUI's ArrayList
     public boolean executePlayerMove(Piece pieceOne, Piece pieceTwo) {
          String currentColor = "";
          if (whiteToMove) {
               this.enPassantInt = WHITE_EN_PASSANT_INT;
               currentColor = "w";
          } else {
               this.enPassantInt = BLACK_EN_PASSANT_INT;
               currentColor = "b";
          }
          if(pieceOne.getColor().equals(currentColor)) {
               Move previousMove = null;
               Move move;
               if (this.moveList.size() > 1) {
                    previousMove = this.moveList.get(this.moveList.size() - 1);
               }
               if((whiteToMove && pieceOne.getColor().equals("w")) || (!whiteToMove && pieceOne.getColor().equals("b"))) {
                    if(previousMove != null) {
                         //Next line checks if en passant is legal
                         //Final number on next line is 3 for white and 4 for black
                         if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == this.enPassantInt) {
                              //moved pawn 2 squares on previous move -- en passant is available
                              move = new Move(pieceOne, pieceTwo, this.gameBoard, true, previousMove.getEndingTile());
                         } else if (pieceOne.getType().equals("K") && Math.abs((pieceOne.getColumn() - pieceTwo.getColumn())) == 2) {
                              //castling
                              move = new Move(pieceOne, pieceTwo, this.gameBoard, true);
                         } else {
                              move = new Move(pieceOne, pieceTwo, this.gameBoard);
                         }
                    } else {
                         move = new Move(pieceOne, pieceTwo, this.gameBoard);
                    }
                    if (move.makeMove()) {
                         this.moveList.add(move);
                         System.out.println(move.toString());
                         this.whiteToMove = !whiteToMove;
                         return true;
                     } /*else {
                         System.out.println("Illegal move");
                     }*/

               }
          }
          return false;
     }

     public boolean executeComputerMove() {
          Move move = engine.generateMove();
          if (move.makeMove()) {
               this.moveList.add(move);
               System.out.println(move.toString());
               this.whiteToMove = !whiteToMove;
               return true;
           }
           return false;
     }

}
