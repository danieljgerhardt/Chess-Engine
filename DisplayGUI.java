import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.io.IOException;

public class DisplayGUI extends JFrame implements ActionListener {

     private Board gameBoard;

     private JButton[][] tileButtons = new JButton[8][8];

     private JPanel buttonPanel;

     public DisplayGUI() {
          gameBoard = new Board();
          gameBoard.arrangePiecesWhite();
          JFrame frame = new JFrame("Chess");
          frame.setSize(1024, 1024);
          frame.setLocationRelativeTo(null);
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setVisible(true);
          this.makeGrid();
          this.addPieces();
          frame.add(buttonPanel);
     }


     public void makeGrid() {
          buttonPanel = new JPanel((new GridLayout(8, 8)));
          for(int i = 0; i < 8; i++) {
               for(int j = 0; j < 8; j++) {
                    tileButtons[i][j] = new JButton();
                    tileButtons[i][j].addActionListener(this);
                    tileButtons[i][j].setEnabled(true);
                    Color lightColor = new Color(238,238,210);
                    Color darkColor = new Color(118, 150, 86);
                    if ((i + j) % 2 == 0) {
                         tileButtons[i][j].setBackground(lightColor);
                    } else {
                         tileButtons[i][j].setBackground(darkColor);
                    }

                    buttonPanel.add(tileButtons[i][j]);

               }
          }
     }

     public void addPieces() {
          for(int i = 0; i < 8; i++) {
               for (int j = 0; j < 8; j++) {
                    String pieceString = this.gameBoard.getPieces()[i][j];
                    String directory = "Images/" + pieceString + ".png";
                    tileButtons[i][j].setIcon(new ImageIcon(directory));
               }
          }
     }

     public void actionPerformed(ActionEvent e) {
          
     }

     public Board getBoard() {
          return this.gameBoard;
     }

     public static void main(String[] args) {
          DisplayGUI GUI = new DisplayGUI();
          Piece testOne = new Piece("P", "w", 6, 0);
          Piece testTwo = new Piece("e", "e", 4, 0);
          Move move = new Move(testOne, testTwo, GUI.getBoard());
          move.makeMove();
          GUI.addPieces();
     }

}
