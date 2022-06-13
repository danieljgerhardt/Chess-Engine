import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;
import java.net.*;

public class DisplayGUI extends JFrame implements ActionListener {

     private Board gameBoard;

     private JButton[][] tileButtons = new JButton[8][8];

     private JPanel buttonPanel;

     private ArrayList<Piece> piecesClicked = new ArrayList<Piece>();

     private boolean whiteToMove = true;

     private ArrayList<Move> moveList = new ArrayList<Move>();

     private JOptionPane colorSelect;

     private String colorChoice;

     private Game game;

     private int row1, col1, row2, col2; //used for tile colorization during moves

     private Color lightColor = new Color(238,238,210);
     private Color darkColor = new Color(118, 150, 86);


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
          this.colorChoice = JOptionPane.showInputDialog(null, "Which color would you like to play as?");
          game = new Game(this.moveList, this.gameBoard);
     }


     public void makeGrid() {
          buttonPanel = new JPanel((new GridLayout(8, 8)));
          for(int i = 0; i < 8; i++) {
               for(int j = 0; j < 8; j++) {
                    tileButtons[i][j] = new JButton();
                    tileButtons[i][j].addActionListener(this);
                    tileButtons[i][j].setEnabled(true);
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
                    //tileButtons[i][j].setIcon(new ImageIcon(directory));
                    tileButtons[i][j].setIcon(new ImageIcon(((new ImageIcon(directory).getImage().getScaledInstance(117, 117, java.awt.Image.SCALE_SMOOTH)))));
               }
          }
     }

     public void actionPerformed(ActionEvent e) {
          if ((row1 + col1) % 2 == 0) {
               tileButtons[row1][col1].setBackground(lightColor);
          } else {
               tileButtons[row1][col1].setBackground(darkColor);
          }
          if ((row2 + col2) % 2 == 0) {
               tileButtons[row2][col2].setBackground(lightColor);
          } else {
               tileButtons[row2][col2].setBackground(darkColor);
          }
          for(int i = 0; i < 8; i++) {
               for(int j = 0; j < 8; j++) {
                    if(e.getSource() == tileButtons[i][j]) {
                         piecesClicked.add(this.gameBoard.getTileArray()[i][j].getPiece());
                         if (piecesClicked.size() > 1) {
                              if (this.game.executePlayerMove(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1))) {
                                   //Color tiles of move
                                   this.row1 = piecesClicked.get(piecesClicked.size() - 2).getRow();
                                   this.col1 = piecesClicked.get(piecesClicked.size() - 2).getColumn();
                                   this.row2 = piecesClicked.get(piecesClicked.size() - 1).getRow();
                                   this.col2 = piecesClicked.get(piecesClicked.size() - 1).getColumn();

                                   this.setTileColor(new Color(186, 202, 68), row1, col1);
                                   this.setTileColor(new Color(186, 202, 68), row2, col2);
                              }

                         }
                    }
               }
           }
           //Remove to implement blindfold chess!
           this.addPieces();
     }


     public Board getBoard() {
          return this.gameBoard;
     }

     public static void main(String[] args) {
          DisplayGUI GUI = new DisplayGUI();
     }

     public void setTileColor(Color c, int row, int column) {
          this.tileButtons[row][column].setBackground(c);
     }

}
