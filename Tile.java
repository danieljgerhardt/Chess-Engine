import java.util.ArrayList;

public class Tile {

     private int row;
     private int column;
     private String color;
     private Piece piece;
     private boolean threatenedByWhite;
     private boolean threatenedByBlack;

     public Tile(int row, int column, String color) {
          this.row = row;
          this.column = column;
          this.color = color;
          this.piece = new Piece("e", "e", this.row, this.column);
          this.threatenedByWhite = false;
          this.threatenedByBlack = false;
     }

     public void setPiece(Piece piece) {
          this.piece = piece;
     }

     public void setThreatened(String color) {
          if (color.equals("w")) {
               this.threatenedByWhite = true;
          } else {
               this.threatenedByBlack = true;
          }
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

     @Override
     public boolean equals(Object other) {
          if (other == this) {
            return true;
        }

        if (!(other instanceof Tile)) {
            return false;
        }

        //row, column, color, piece
        if (other.getRow() == this.getRow() && other.getColumnn() == this.getColumn() && other.getColor().equals(this.getColor()) && other.getPiece().equals(this.getPiece())) {
             return true;
        } else {
             return false;
        }
     }

}
