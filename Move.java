public class Move {

     private Piece startingPiece;
     private Piece endingPiece;
     private Board board;

     public Move(Piece start, Piece end, Board board) {
          this.startingPiece = start;
          this.endingPiece = end;
          this.board = board;
     }

     public boolean makeMove() {
          Piece empty = new Piece("e", "e", this.startingPiece.getRow(), this.startingPiece.getColumn());
          this.board.setTile(this.startingPiece.getRow(), this.startingPiece.getColumn(), empty);
          this.board.setTile(this.endingPiece.getRow(), this.endingPiece.getColumn(), this.startingPiece);
          this.startingPiece.setRow(endingPiece.getRow());
          this.startingPiece.setColumn(endingPiece.getColumn());
          return true;
     }


}
