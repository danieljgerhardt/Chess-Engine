public class DisplayConsole {

  public static void main(String[] args) {
    Board test = new Board();
    test.arrangePiecesWhite();
    for(Tile[] t : test.getTileArray()) {
      for (Tile tTile : t) {
        System.out.print(tTile.toString());
      }
      System.out.println();
    }
  }

}
