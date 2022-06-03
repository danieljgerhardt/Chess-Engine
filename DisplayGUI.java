import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.io.IOException;
import java.util.ArrayList;

public class DisplayGUI extends JFrame implements ActionListener {

     private Board gameBoard;

     private JButton[][] tileButtons = new JButton[8][8];

     private JPanel buttonPanel;

     private ArrayList<Piece> piecesClicked = new ArrayList<Piece>();

     private boolean whiteToMove = true;

     private ArrayList<Move> moveList = new ArrayList<Move>();


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
          for(int i = 0; i < 8; i++) {
               for(int j = 0; j < 8; j++) {
                    if(e.getSource() == tileButtons[i][j]) {
                         piecesClicked.add(this.gameBoard.getTileArray()[i][j].getPiece());
                         if(piecesClicked.size() % 2 == 0) {
                              Move previousMove = null;
                              Move move;
                              if (this.moveList.size() > 1) {
                                   previousMove = this.moveList.get(this.moveList.size() - 1);
                              }
                              if(whiteToMove && piecesClicked.get(piecesClicked.size() - 2).getColor().equals("w")) {
                                   if(previousMove != null) {
                                        //Next line checks if en passant is legal
                                        if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == 3) {
                                             //moved white pawn 2 squares on previous move -- en passant is available
                                             move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard, true, previousMove.getEndingTile());
                                        } else {
                                             move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard);
                                        }
                                   } else {
                                        move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard);
                                   }
                                   if (move.makeMove()) {
                                        this.moveList.add(move);
                                        System.out.println(move.toString());
                                        this.whiteToMove = !whiteToMove;
                                   } else {
                                        System.out.println("Illegal move");
                                   }
                                   this.addPieces();
                              } else if (!whiteToMove && piecesClicked.get(piecesClicked.size() - 2).getColor().equals("b")){
                                   if (previousMove != null) {
                                        //Next line checks if en passant is legal
                                        if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == 4) {
                                             //moved black pawn 2 squares on previous move -- en passant is available
                                             move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard, true, previousMove.getEndingTile());
                                        } else {
                                             move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard);
                                        }
                                   } else {
                                        move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard);
                                   }
                                   if (move.makeMove()) {
                                        this.moveList.add(move);
                                        System.out.println(move.toString());
                                        this.whiteToMove = !whiteToMove;
                                   } else {
                                        System.out.println("Illegal move");
                                   }
                                   this.addPieces();
                              }
                         }
                    }
               }
           }
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
