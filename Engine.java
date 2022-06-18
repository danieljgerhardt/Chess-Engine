import java.util.*;

public class Engine {

     private Board board;
     private Board testBoard;
     private Game testGame;

     public Engine(Board b) {
          this.board = b;
     }

     //Currently will spit out the first move the engine sees, need to implement evaluation of positions and moves
     public Move generateMoveT() {
          Piece start = new Piece("e", "e", 0, 0);
          Piece end = new Piece("e", "e", 0, 0);
          Move move = new Move(start, end, this.board);
          int numPieces = 0;
          for(Tile[] tArray : this.board.getTileArray()) {
               for(Tile t : tArray) {
                    if(t.getPiece().getColor().equals("b")) {
                         move.generatePossibleMoves(t.getPiece());
                         numPieces++;
                    }
               }
          }
          for (Tile[] tArray : this.board.getTileArray()) {
               for(Tile t : tArray) {
                    if(t.getPiece().getColor().equals("b")) {
                         if (t.getPiece().getPossibleMoves().size() > 0) {
                              start = t.getPiece();
                         }
                    }
               }
          }
          for (Tile t : start.getPossibleMoves()) {
               end = t.getPiece();
          }
          move = new Move(start, end, this.board);
          return move;
     }

     public Move generateMove() {
          Piece start = new Piece("e", "e", 0, 0);
          Piece end = new Piece("e", "e", 0, 0);
          ArrayList<Piece> possibleStartingPieces = new ArrayList<Piece>();
          ArrayList<Piece> possibleEndingPieces = new ArrayList<Piece>();
          ArrayList<Integer> evaluations = new ArrayList<Integer>();
          for(Tile[] tArray : this.board.getTileArray()) {
               for(Tile t : tArray) {
                    if(t.getPiece().getColor().equals("b")) {
                         this.testBoard = this.getGameBoardCopy();
                         possibleStartingPieces.add(t.getPiece());
                    }
               }
          }
          for (int i = 0; i < possibleStartingPieces.size(); i++) {
               Move move = new Move(possibleStartingPieces.get(i), end, this.testBoard);
               move.generatePossibleMoves(possibleStartingPieces.get(i));
               int possibleMoves = 0;
               for (int t = 0; t < possibleStartingPieces.get(i).getPossibleMoves().size(); t++) {
                    possibleMoves++;
               }
               //System.out.println(possibleStartingPieces.get(i).toString() + " " + possibleMoves);
               for (int j = 0; j < possibleStartingPieces.get(i).getPossibleMoves().size(); j++) {
                    //System.out.println(possibleStartingPieces.get(i).toString());
                    //THE FOLLOWING LINES ARE BREAKING EVERYTHING
                    //System.out.println("test");
                    end = possibleStartingPieces.get(i).getPossibleMoves().get(j).getPiece();
                    possibleStartingPieces.add(start);
                    possibleEndingPieces.add(end);
                    evaluations.add(this.evaluatePosition());
               }

          }
          int max = 0;
          int maxIndex = 0;
          for (int i = 0; i < possibleStartingPieces.size(); i++) {
               if ((evaluations.size() - 1) >= i && evaluations.get(i) >= max) {
                    maxIndex = i;
                    max = evaluations.get(i);
               }
          }
          for (int i = maxIndex; i > 0; i--) {
               if (!possibleStartingPieces.get(i).equals(start)) {
                    possibleStartingPieces.set(maxIndex, possibleStartingPieces.get(i));
                    break;
               }
          }
          System.out.println(possibleStartingPieces.get(maxIndex).toString());
          System.out.println(possibleEndingPieces.get(maxIndex).toString());
          Move toMake = new Move(possibleStartingPieces.get(maxIndex), possibleEndingPieces.get(maxIndex), this.board);
          //System.out.println("WE MADE IT " + toMake.toString());
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
                    tileArrayCopy[row][column] = toAdd;
               }
          }
          return boardCopy;
     }

     /*public boolean executeTestMove(Move move) {
          if (move.makeMove()) {
               System.out.println("EXECUTE TEST MOVE: " + move.toString());
               return true;
           }
           return false;
     }*/

     public int evaluatePosition() {
          //Material, king safety, piece activity
          //Positive = better for white, negative = better for black
          //More extreme value = higher advantage
          if (board.getTotalPieceValue("w") > board.getTotalPieceValue("b")) {
               return (board.getTotalPieceValue("w") -  board.getTotalPieceValue("b"));
          } else {
               return (board.getTotalPieceValue("w") -  board.getTotalPieceValue("b"));
          }
     }
}
