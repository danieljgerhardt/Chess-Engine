public class DisplayConsole {

     public static void main(String[] args) {
          Board test = new Board();
          test.arrangePiecesWhite();
          System.out.println(test.toString());
          Piece testOne = new Piece("P", "w", 6, 0);
          Piece testTwo = new Piece("e", "e", 4, 0);
          Move move = new Move(testOne, testTwo, test);
          move.makeMove();
          System.out.println(test.toString());
     }



}
