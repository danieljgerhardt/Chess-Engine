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
          frame.add(buttonPanel);
     }

     public void makeGrid() {
          buttonPanel = new JPanel((new GridLayout(8, 8)));
          for(int i = 0; i < 8; i++) {
               for(int j = 0; j < 8; j++) {
                    tileButtons[i][j] = new JButton();

                    tileButtons[i][j].addActionListener(this);
                    tileButtons[i][j].setEnabled(true);
                    if ((i + j) % 2 == 0) {
                         tileButtons[i][j].setBackground(Color.WHITE);
                    } else {
                         tileButtons[i][j].setBackground(Color.BLACK);
                    }

                    buttonPanel.add(tileButtons[i][j]);


               }
         }
     }

     public void actionPerformed(ActionEvent e) {

     }

     public static void main(String[] args) {
          new DisplayGUI();
     }

}
