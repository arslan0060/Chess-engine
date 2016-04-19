/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author Arslan Memon
 */
public class UI extends JPanel implements MouseListener, MouseMotionListener {

    static int squareSize = 64;
    static int mouseX, mouseY, destX, destY;
    public static boolean drag = false;

    @Override
    public void paintComponent(Graphics g) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        //draw board
        for (int i = 0; i < 8; i += 2) {
            for (int j = 0; j < 8; j++) {
                if (j % 2 == 0) {
                    g.setColor(new Color(255, 200, 100));
                    g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
                    g.setColor(new Color(150, 50, 30));
                    g.fillRect((i + 1) * squareSize, j * squareSize, squareSize, squareSize);
                } else {
                    g.setColor(new Color(150, 50, 30));
                    g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
                    g.setColor(new Color(255, 200, 100));
                    g.fillRect((i + 1) * squareSize, j * squareSize, squareSize, squareSize);
                }
            }
        }
        this.setBackground(Color.blue);
        Image chessImg = new ImageIcon("chess.png").getImage();
       //put pieces on board using picture
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int k = -1, l = -1;
                if (Chess.humanColor == 1) {
                    switch (Chess.chessboard[i][j]) {
                        case "wP":
                            k = 5;
                            l = 0;
                            break;
                        case "wR":
                            k = 4;
                            l = 0;
                            break;
                        case "wB":
                            k = 2;
                            l = 0;
                            break;
                        case "wN":
                            k = 3;
                            l = 0;
                            break;
                        case "wK":
                            k = 0;
                            l = 0;
                            break;
                        case "wQ":
                            k = 1;
                            l = 0;
                            break;
                        case "bP":
                            k = 5;
                            l = 1;
                            break;
                        case "bR":
                            k = 4;
                            l = 1;
                            break;
                        case "bB":
                            k = 2;
                            l = 1;
                            break;
                        case "bN":
                            k = 3;
                            l = 1;
                            break;
                        case "bK":
                            k = 0;
                            l = 1;
                            break;
                        case "bQ":
                            k = 1;
                            l = 1;
                            break;
                    }
                } else {
                    switch (Chess.chessboard[i][j]) {
                        case "wP":
                            k = 5;
                            l = 1;
                            break;
                        case "wR":
                            k = 4;
                            l = 1;
                            break;
                        case "wB":
                            k = 2;
                            l = 1;
                            break;
                        case "wN":
                            k = 3;
                            l = 1;
                            break;
                        case "wK":
                            k = 0;
                            l = 1;
                            break;
                        case "wQ":
                            k = 1;
                            l = 1;
                            break;
                        case "bP":
                            k = 5;
                            l = 0;
                            break;
                        case "bR":
                            k = 4;
                            l = 0;
                            break;
                        case "bB":
                            k = 2;
                            l = 0;
                            break;
                        case "bN":
                            k = 3;
                            l = 0;
                            break;
                        case "bK":
                            k = 0;
                            l = 0;
                            break;
                        case "bQ":
                            k = 1;
                            l = 0;
                            break;

                    }
                }
                if (k != -1 && l != -1) {
                    if (drag && i == mouseY / squareSize && j == mouseX / squareSize) {
                        g.drawImage(chessImg, destX - squareSize / 2, destY - squareSize / 2, destX + squareSize / 2, destY + squareSize / 2, k * 330, l * 330, (k + 1) * 330, (l + 1) * 330, this);
                    }

                    g.drawImage(chessImg, j * squareSize, i * squareSize, (j + 1) * squareSize, (i + 1) * squareSize, k * 330, l * 330, (k + 1) * 330, (l + 1) * 330, this);

                }
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) {
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drag = false;
        if (e.getX() < 8 * squareSize && e.getY() < 8 * squareSize) {
            destX = e.getX();
            destY = e.getY();
            int x = mouseX / squareSize;
            int x2 = destX / squareSize;
            int y = mouseY / squareSize;
            int y2 = destY / squareSize;
            String move;
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (y == 1 && y2 == 0 && "wP".equals(Chess.chessboard[y][x])) {
                    String stringInput = JOptionPane.showInputDialog("Q-queen N-Knight B-bishop R-rook"); 
                    move = "" + (y) + (x) + (y2) + (x2) + Chess.chessboard[y2][x2] + "Tw"+stringInput.toUpperCase();
                } else {

                    move = "" + (y) + (x) + (y2) + (x2) + Chess.chessboard[y2][x2];
                }
                String movesPossible = Chess.movesPossible();
                if(Chess.kingIsSafe()&&movesPossible.length() == 0)
                {
                int result = JOptionPane.showConfirmDialog((Component) null, "Stalemate!!!!",
                            "alert", JOptionPane.OK_CANCEL_OPTION);
                    System.exit(0);
                }
                else if (movesPossible.length() == 0) {
                    int result = JOptionPane.showConfirmDialog((Component) null, "CheckMate, you lose!!!!",
                            "alert", JOptionPane.OK_CANCEL_OPTION);
                    System.exit(0);

                } else if (movesPossible.contains(move)) {
                    Chess.move(move);
                    Chess.flipBoard();
                    String m = Chess.AlphaBetaTreePruning(Integer.MIN_VALUE, Integer.MAX_VALUE, "", -1, Chess.globalDepth, Chess.globalDepth);
                    if(Chess.kingIsSafe()&&(m.length()==0||(m.charAt(4)!='w'&&m.charAt(4)!='b'&&m.charAt(4)!=' ')))
                    {
                        int result = JOptionPane.showConfirmDialog((Component) null, "Stalemate!!!!",
                            "alert", JOptionPane.OK_CANCEL_OPTION);
                    System.exit(0);
                    }
                    else if (m.length()==0||(m.charAt(4)!='w'&&m.charAt(4)!='b'&&m.charAt(4)!=' ')) {
                        int result = JOptionPane.showConfirmDialog((Component) null, "You Win!!!!",
                                "alert", JOptionPane.OK_CANCEL_OPTION);
                        System.exit(0);
                    }
                    
                    Chess.move(m);
                    Chess.flipBoard();
                    repaint();
                }

            }

        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        drag = true;
        destX = e.getX();
        destY = e.getY();
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        {
        }
    }

}
