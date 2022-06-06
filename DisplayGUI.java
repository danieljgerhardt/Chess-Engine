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

     private Engine engine;


     public DisplayGUI() {
          gameBoard = new Board();
          engine = new Engine(gameBoard);
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
                    //tileButtons[i][j].setIcon(new ImageIcon(directory));
                    tileButtons[i][j].setIcon(new ImageIcon(((new ImageIcon(directory).getImage().getScaledInstance(117, 117, java.awt.Image.SCALE_SMOOTH)))));
               }
          }
     }

     public void actionPerformed(ActionEvent e) {
          for(int i = 0; i < 8; i++) {
               for(int j = 0; j < 8; j++) {
                    if(e.getSource() == tileButtons[i][j]) {
                         piecesClicked.add(this.gameBoard.getTileArray()[i][j].getPiece());
                         this.executeMove();
                    }
               }
           }
           //Remove to implement blindfold chess!
           this.addPieces();
     }

     public void executeMove() {
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
                         } else if (piecesClicked.get(piecesClicked.size() - 2).getType().equals("K") && Math.abs((piecesClicked.get(piecesClicked.size() - 2).getColumn() - piecesClicked.get(piecesClicked.size() - 1).getColumn())) == 2) {
                              //castling
                              move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard, true);
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
               } /*else if (!whiteToMove && piecesClicked.get(piecesClicked.size() - 2).getColor().equals("b")){
                    if (previousMove != null) {
                         //Next line checks if en passant is legal
                         if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == 4) {
                              //moved black pawn 2 squares on previous move -- en passant is available
                              move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard, true, previousMove.getEndingTile());
                         } else if (piecesClicked.get(piecesClicked.size() - 2).getType().equals("K") && Math.abs((piecesClicked.get(piecesClicked.size() - 2).getColumn() - piecesClicked.get(piecesClicked.size() - 1).getColumn())) == 2) {
                              //castling
                              move = new Move(piecesClicked.get(piecesClicked.size() - 2), piecesClicked.get(piecesClicked.size() - 1), this.gameBoard, true);
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
               }*/
               else if (!whiteToMove && piecesClicked.get(piecesClicked.size() - 2).getColor().equals("b")){
                    Move engineMove = engine.generateMove();
                    if (previousMove != null) {
                         //Next line checks if en passant is legal
                         if (previousMove.getStartingPiece().getType().equals("P") && previousMove.getEndingTile().getRow() == 4) {
                              //moved black pawn 2 squares on previous move -- en passant is available
                              move = new Move(engineMove.getStartingPiece(), engineMove.getEndingTile().getPiece(), this.gameBoard, true, previousMove.getEndingTile());
                         } else if (engineMove.getStartingPiece().getType().equals("K") && Math.abs((engineMove.getStartingPiece().getColumn() - engineMove.getEndingTile().getPiece().getColumn())) == 2) {
                              //castling
                              move = new Move(engineMove.getStartingPiece(), engineMove.getEndingTile().getPiece(), this.gameBoard, true);
                         } else {
                              move = new Move(engineMove.getStartingPiece(), engineMove.getEndingTile().getPiece(), this.gameBoard);
                         }
                    } else {
                         move = new Move(engineMove.getStartingPiece(), engineMove.getEndingTile().getPiece(), this.gameBoard);
                    }
                    if (move.makeMove()) {
                         this.moveList.add(move);
                         System.out.println(move.toString());
                         this.whiteToMove = !whiteToMove;
                    } else {
                         System.out.println("Illegal move");
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
