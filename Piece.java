import java.util.ArrayList;

public class Piece {

     private String type;
     private String color;
     private int row;
     private int column;
     private int value;
     private ArrayList<Tile> possibleMoves = new ArrayList<Tile>(); //This is a list of all possible moves INCLUDING illegal moves and on the board
     private boolean hasMoved;
     private ArrayList<Tile> storedMoves = new ArrayList<Tile>();

     public Piece(String type, String color, int row, int column) {
          this.type = type;
          this.color = color;
          this.row = row;
          this.column = column;
          this.hasMoved = false;
          switch (this.type) {
               case "P":
                    this.value = 1;
                    break;
               case "N":
                    this.value = 3;
                    break;
               case "B":
                    this.value = 3;
                    break;
               case "R":
                    this.value = 5;
                    break;
               case "Q":
                    this.value = 9;
                    break;
               case "K":
                    this.value = 150;
                    break;
          }
     }

     public void storePossibleMoves() {
          for (int i = 0; i < this.possibleMoves.size(); i++) {
               Tile toAdd = new Tile(this.possibleMoves.get(i).getRow(), this.possibleMoves.get(i).getColumn(), this.possibleMoves.get(i).getColor());
               toAdd.setPiece(this.possibleMoves.get(i).getPiece());
               this.storedMoves.add(toAdd);
          }
     }

     public ArrayList<Tile> returnStoredPossibleMoves() {
          return this.storedMoves;
     }

     public void restorePossibleMoves() {
          this.possibleMoves = this.storedMoves;
     }

     public void addToPossibleMoves(Tile tile) {
         this.possibleMoves.add(tile);
     }

     public void clearPossibleMoves() {
         this.possibleMoves.clear();
     }

     public String getType() {
         return this.type;
     }

     public int getValue() {
          return this.value;
     }

     public void setType(String newType) {
         this.type = newType;
     }

     public void setHasMoved(Boolean moved) {
         this.hasMoved = moved;
     }

     public boolean getHasMoved() {
         return this.hasMoved;
     }

     public String getColor() {
         return this.color;
     }

     public String getOppositeColor() {
         if (this.color.equals("w")) {
              return "b";
         } else {
              return "w";
         }
     }

     public int getRow() {
         return this.row;
     }

     public int getColumn() {
         return this.column;
     }

     public ArrayList<Tile> getPossibleMoves() {
         return this.possibleMoves;
     }

     public void setRow(int row) {
        this.row = row;
     }

     public void setColumn(int column) {
        this.column = column;
     }

     @Override
     public String toString() {
         return this.color + this.type + " ";
     }

     @Override
     public boolean equals(Object other) {
         if (other == this) {
           return true;
       }

       if (!(other instanceof Piece)) {
           return false;
       }

       Piece test = (Piece) other;

       //row, column, color, piece
       if (test.getRow() == this.getRow() && test.getColumn() == this.getColumn() && test.getColor().equals(this.getColor()) && test.getType().equals(this.getType())) {
            return true;
       } else {
            return false;
       }
     }
}
