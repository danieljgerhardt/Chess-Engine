import java.util.*;


public class Engine {

     private Board board;

     public Engine(Board b) {
          this.board = b;
     }

     public Move generateMove() {
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
          /*Random rand = new Random(numPieces);
          int count = 0;
          boolean loop = true;
          while(loop) {
               count = 0;
               int randomNow = rand.nextInt();
               for (Tile[] tArray : this.board.getTileArray()) {
                    for(Tile t : tArray) {
                         if(t.getPiece().getColor().equals("b")) {
                              count++;
                              if (count == randomNow) {
                                   if (t.getPiece().getPossibleMoves().size() > 0) {
                                        start = t.getPiece();
                                        loop = false;
                                   }
                              }
                         }
                    }
               }
          }
          Random rand2 = new Random(start.getPossibleMoves().size());
          count = 0;
          int randomNow2 = rand2.nextInt();
          for (Tile t : start.getPossibleMoves()) {
               count++;
               if(count == randomNow2) {
                    end = t.getPiece();
               }
          }*/
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

}
