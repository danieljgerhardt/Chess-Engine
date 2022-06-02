public class Piece {

    private String type;
    private String color;
    private int row;
    private int column;

    public Piece(String type, String color, int row, int column) {
        this.type = type;
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public String getType() {
         return this.type;
    }

    public String getColor() {
         return this.color;
    }

    public int getRow() {
         return this.row;
    }

    public int getColumn() {
         return this.column;
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
