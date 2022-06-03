import java.util.ArrayList;

public class Tile {

     private int row;
     private int column;
     private String color;
     private Piece piece;

     public Tile(int row, int column, String color) {
          this.row = row;
          this.column = column;
          this.color = color;
          this.piece = new Piece("e", "e", this.row, this.column);
     }

     public void setPiece(Piece piece) {
          this.piece = piece;
     }

     public Piece getPiece() {
          return this.piece;
     }

     public int getRow() {
          return this.row;
     }

     public int getColumn() {
          return this.column;
     }

     @Override
     public String toString() {
          //return row + ", " + column + ", " + color + "\n";
          return this.piece.getColor() + this.piece.getType() + " ";
     }

}
