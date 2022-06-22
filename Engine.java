import java.util.*;

public class Engine {

     private Board board;
     private Board testBoard;
     private Game testGame;

     public Engine(Board b) {
          this.board = b;
     }

     public Move generateMove() {
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
          int maxEval = 0;
          int maxIndex = 0;
          for (Piece piece : possibleStartingPieces) {
               possibleMovesPerPiece = piece.getPossibleMoves();
               for (int i = 0; i < possibleMovesPerPiece.size(); i++) {
                    this.testBoard = this.getGameBoardCopy();
                    Move testMove = new Move(piece, possibleMovesPerPiece.get(i).getPiece(), this.testBoard);
                    if (testMove.makeMove()) {
                         System.out.println("CANDIDATE MOVE! " + testMove.toString());
                         candidateMoves.add(testMove);
                         double currentEval = this.evaluatePosition(this.testBoard);
                         if (currentEval < maxEval) {
                              maxIndex = candidateMoves.size() - 1;
                              maxEval = this.evaluatePosition(this.testBoard);
                         }
                    }
                    testMove.undoMove();
               }
          }

          Move toMake = new Move(candidateMoves.get(maxIndex).getStartingPiece(), candidateMoves.get(maxIndex).getEndingTile().getPiece(), this.board);
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

     public int evaluatePosition(Board b) {
          //Material, king safety, piece activity
          //Positive = better for white, negative = better for black
          //More extreme value = higher advantage
          //System.out.println((b.getTotalPieceValue("w") -  b.getTotalPieceValue("b")));
          return (b.getTotalPieceValue("w") -  b.getTotalPieceValue("b"));
     }
}
