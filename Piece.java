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

    @Override
    public String toString() {
      return this.type;
    }

}
