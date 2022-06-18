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
