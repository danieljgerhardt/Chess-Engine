import java.util.ArrayList;

public class Piece {

    private String type;
    private String color;
    private int row;
    private int column;
    private ArrayList<Tile> possibleMoves = new ArrayList<Tile>(); //This is a list of all possible moves INCLUDING illegal moves

    public Piece(String type, String color, int row, int column) {
        this.type = type;
        this.color = color;
        this.row = row;
        this.column = column;
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
         String ret = "";
         for (int i = 0; i < this.possibleMoves.size(); i++) {
              ret += "[" + this.possibleMoves.get(i).getRow() + "," + this.possibleMoves.get(i).getColumn() + "] ";
         }
         System.out.println(ret);
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

}
